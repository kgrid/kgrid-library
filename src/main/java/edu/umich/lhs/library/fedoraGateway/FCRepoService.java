package edu.umich.lhs.library.fedoraGateway;

import com.complexible.common.openrdf.model.ModelIO;
import edu.umich.lhs.library.exception.LibraryException;
import edu.umich.lhs.library.fusekiGateway.NamespaceConstants;
import edu.umich.lhs.library.knowledgeObject.ArkId;
import edu.umich.lhs.library.model.ServerDetails;
import edu.umich.lhs.library.services.FedoraConfiguration;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.URI;
import java.net.URISyntaxException;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import org.apache.commons.codec.binary.Base64;
import org.apache.http.Header;
import org.apache.http.HttpRequest;
import org.apache.http.HttpResponse;
import org.apache.http.auth.AuthenticationException;
import org.apache.http.auth.UsernamePasswordCredentials;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPatch;
import org.apache.http.entity.InputStreamEntity;
import org.apache.http.impl.auth.BasicScheme;
import org.apache.http.impl.client.DefaultRedirectStrategy;
import org.apache.http.impl.client.HttpClientBuilder;
import org.apache.jena.rdf.model.ModelFactory;
import org.apache.log4j.Logger;
import org.openrdf.model.IRI;
import org.openrdf.model.Model;
import org.openrdf.model.impl.ContextStatement;
import org.openrdf.model.impl.SimpleValueFactory;
import org.openrdf.rio.RDFFormat;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.RequestEntity;
import org.springframework.http.ResponseEntity;
import org.springframework.http.client.HttpComponentsClientHttpRequestFactory;
import org.springframework.stereotype.Component;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.ResourceAccessException;
import org.springframework.web.client.RestTemplate;

@Component
public class FCRepoService {

	private FedoraConfiguration fedoraConfiguration;

	private URI baseURI;

	private String userName;

	protected String password;

	private static final Logger logger = Logger.getLogger(FCRepoService.class);

	public FCRepoService(FedoraConfiguration fedoraConfiguration) {
		this.fedoraConfiguration = fedoraConfiguration;
		configureBaseURI();
	}

	public URI getBaseURI() {
		return baseURI;
	}

	// Transaction Handling:
	public URI createTransaction() throws LibraryException, URISyntaxException {

		URI transactionURI = new URI(baseURI + "fcr:tx/");

		HttpClient instance = HttpClientBuilder.create().setRedirectStrategy(new DefaultRedirectStrategy()).build();

		RestTemplate restTemplate = new RestTemplate(new HttpComponentsClientHttpRequestFactory(instance));

		ResponseEntity<String> response = restTemplate.exchange(transactionURI, HttpMethod.POST, authenticationHeader(), String.class);

		if(response.getStatusCodeValue() == HttpStatus.CREATED.value()) {
			transactionURI = new URI(response.getHeaders().getFirst("Location"));
		} else {
			String err = "Unable to create transaction.";
			logger.error(err);
			throw new LibraryException(err);
		}
		return transactionURI;
	}

	public void commitTransaction(URI transactionURI) throws LibraryException, URISyntaxException {
		URI transactionCommitURI = new URI(transactionURI + "/fcr:tx/fcr:commit");

		HttpClient instance = HttpClientBuilder.create().setRedirectStrategy(new DefaultRedirectStrategy()).build();

		RestTemplate restTemplate = new RestTemplate(new HttpComponentsClientHttpRequestFactory(instance));
		try {
			ResponseEntity<String> response = restTemplate.exchange(transactionCommitURI, HttpMethod.POST,
					authenticationHeader(), String.class);
			if(response.getStatusCodeValue() == HttpStatus.NO_CONTENT.value()) {
				logger.info("Transaction " + transactionCommitURI + " committed");
			} else {
				String err = "Unable to commit transaction id " + transactionURI;
				logger.error(err);
				throw new LibraryException(err);
			}

		} catch (ResourceAccessException e) {
			throw new LibraryException(e);
		}
	}

	public void rollbackTransaction(URI transactionURI) throws LibraryException, URISyntaxException {
		URI transactionRollBackURI = new URI(transactionURI + "/fcr:tx/fcr:rollback");

		HttpClient instance = HttpClientBuilder.create().setRedirectStrategy(new DefaultRedirectStrategy()).build();

		RestTemplate restTemplate = new RestTemplate(new HttpComponentsClientHttpRequestFactory(instance));

		try {
			ResponseEntity<String> response = restTemplate
					.exchange(transactionRollBackURI, HttpMethod.POST,
							authenticationHeader(), String.class);
			if(response.getStatusCodeValue() == HttpStatus.NO_CONTENT.value()) {
				logger.info("Transaction " + transactionRollBackURI + " rolled back");
			} else {
				String err = "Unable to roll back transaction with transaction URI " + transactionURI;
				logger.error(err);
				throw new LibraryException(err);
			}
		} catch (ResourceAccessException e) {
			throw new LibraryException(e);
		}
	}

	//Get:
	public boolean checkIfObjectExists(URI objectURI) throws LibraryException {

		HttpClient instance = HttpClientBuilder.create().setRedirectStrategy(new DefaultRedirectStrategy()).build();

		RestTemplate restTemplate = new RestTemplate(new HttpComponentsClientHttpRequestFactory(instance));

		try {
			ResponseEntity<String> response = restTemplate.exchange(objectURI, HttpMethod.GET,
					authenticationHeader(), String.class);
			return response.getStatusCodeValue() == HttpStatus.OK.value();

		} catch (HttpClientErrorException e) {
			return false;
		}
	}

	public org.apache.jena.rdf.model.Model getRdfJson(URI objectURI) throws LibraryException {

		HttpClient instance = HttpClientBuilder.create()
				.setRedirectStrategy(new DefaultRedirectStrategy()).build();

		RestTemplate restTemplate = new RestTemplate(
				new HttpComponentsClientHttpRequestFactory(instance));
		HttpHeaders headers = new HttpHeaders();
		headers.setAccept(Collections.singletonList(new MediaType("application", "ld+json")));
		headers.putAll(authenticationHeader().getHeaders());

		HttpEntity<String> entity = new HttpEntity<>("parameters", headers);

		ResponseEntity<String> response = restTemplate.exchange(objectURI, HttpMethod.GET,
				entity, String.class);

		InputStream ins = new ByteArrayInputStream(response.getBody().getBytes());

		return ModelFactory.createDefaultModel().read(ins, this.baseURI.toString(), "JSON-LD");

	}

	public Model getRDFData(URI objectURI) throws LibraryException {

		HttpClient httpClient = HttpClientBuilder.create().setRedirectStrategy(new DefaultRedirectStrategy()).build();

		HttpGet httpGetRequest = new HttpGet(objectURI);

		httpGetRequest.addHeader(authenticate(httpGetRequest));
		httpGetRequest.addHeader("Accept", "application/n-triples");

		try {
			HttpResponse httpResponse = httpClient.execute(httpGetRequest);
			if (httpResponse.getStatusLine().getStatusCode() == HttpStatus.OK.value()) {
				org.apache.http.HttpEntity entity = httpResponse.getEntity();
				return ModelIO.read(entity.getContent(), RDFFormat.NTRIPLES);
			}
		} catch (IOException e) {
			String err = "Exception occurred while verifying object id "+ objectURI +"."+ e.getMessage();
			logger.error(err);
			throw new LibraryException(err, e);
		}
		return null;
	}

	public String getStringObjectContent(String objectId, String dataStreamId) throws LibraryException {
		try {
			return getStringObjectContent(new URI(baseURI + objectId + "/" + dataStreamId + "/"));
		} catch (URISyntaxException e) {
			throw new LibraryException("Invalid object uri " + baseURI + objectId + "/" + dataStreamId + "/");
		}
	}

	public String getStringObjectContent(URI objectURI) throws LibraryException {
		HttpClient instance = HttpClientBuilder.create()
				.setRedirectStrategy(new DefaultRedirectStrategy()).build();

		RestTemplate restTemplate = new RestTemplate(
				new HttpComponentsClientHttpRequestFactory(instance));

		ResponseEntity<String> response = restTemplate.exchange(objectURI, HttpMethod.GET,
				authenticationHeader(), String.class);
		if (response.getStatusCode() == HttpStatus.OK && response.getBody() != null) {
			return response.getBody();
		} else {
			throw new LibraryException("Cannot fetch object at location " + objectURI);
		}
	}

	public List<URI> getChildrenURIs(URI containerURI) throws LibraryException, URISyntaxException {
		Model container = getRDFData(containerURI);

		ArrayList<URI> uris = new ArrayList<>();
		if(container != null && container.size() > 0) {
			for (Object item : container.toArray()) {
				ContextStatement statement = (ContextStatement) item;
				IRI iri = SimpleValueFactory.getInstance().createIRI(NamespaceConstants.CONTAINS);
				if (statement.getPredicate().equals(iri)) {
					uris.add(new URI(statement.getObject().stringValue()));
				}
			}
		}
		return uris;
	}

	public boolean ping() throws IOException, LibraryException {

		HttpClient instance = HttpClientBuilder.create()
				.setRedirectStrategy(new DefaultRedirectStrategy()).build();

		RestTemplate restTemplate = new RestTemplate(
				new HttpComponentsClientHttpRequestFactory(instance));

		ResponseEntity<String> response = restTemplate.exchange(baseURI, HttpMethod.GET,
				authenticationHeader(), String.class);

		return response.getStatusCode() == HttpStatus.OK;
	}

	//Post:
	public URI createContainerWithAutoGeneratedName(URI parent) throws LibraryException {

		HttpClient instance = HttpClientBuilder.create()
				.setRedirectStrategy(new DefaultRedirectStrategy()).build();

		RestTemplate restTemplate = new RestTemplate(
				new HttpComponentsClientHttpRequestFactory(instance));

		ResponseEntity<String> response = restTemplate.exchange(parent, HttpMethod.POST,
				authenticationHeader(), String.class);

		URI containerLocation;

		if(response.getStatusCode() == HttpStatus.CREATED) {
			try {
			containerLocation = new URI(response.getHeaders().getFirst("Location"));
			} catch (URISyntaxException e) {
				throw new LibraryException("Error creating new container in ko " + parent, e);
			}
		} else {
			throw new LibraryException("Error creating new container in ko " + parent + " expected created status but instead got " + response.getStatusCode());
		}

		return containerLocation;
	}

	// Put:
	public void putBinary(String binary, ArkId objIdentifier, String type) throws LibraryException, URISyntaxException {
		putStringBinary(binary, new URI(baseURI + objIdentifier.getFedoraPath() + "/" + type));
	}

	public void putBinary(String binary, URI objectURI, String type) throws LibraryException, URISyntaxException {
		putStringBinary(binary, new URI(objectURI + "/" + type));
	}

	public void putStringBinary(String binary, URI objectURI) throws LibraryException {
		if(binary == null)
			binary = "";

		HttpClient instance = HttpClientBuilder.create()
				.setRedirectStrategy(new DefaultRedirectStrategy()).build();

		RestTemplate restTemplate = new RestTemplate(
				new HttpComponentsClientHttpRequestFactory(instance));

		RequestEntity request = RequestEntity.put(objectURI)
				.header("Authorization", authenticationHeader().getHeaders().getFirst("Authorization"))
				.body(binary);

		ResponseEntity<String> response = restTemplate.exchange(request, String.class);

		if(response.getStatusCode() != HttpStatus.NO_CONTENT && response.getStatusCode() != HttpStatus.CREATED) {
			throw new LibraryException("Error occurred while creating binary for object " + objectURI + " got status code " + response.getStatusCode());
		}
	}

	public URI createContainer(URI uri) throws LibraryException, URISyntaxException {
		return createContainer(uri, "");
	}

	public URI createContainer(URI parentURI, String containerName) throws URISyntaxException {
		HttpClient instance = HttpClientBuilder.create()
				.setRedirectStrategy(new DefaultRedirectStrategy()).build();

		RestTemplate restTemplate = new RestTemplate(
				new HttpComponentsClientHttpRequestFactory(instance));

		URI objectURI;
		if(parentURI.toString().endsWith("/")) {
			objectURI = new URI(parentURI + containerName);
		} else {
			objectURI = new URI(parentURI + "/" + containerName);
		}

		ResponseEntity<String> response = restTemplate.exchange(objectURI, HttpMethod.PUT,
				authenticationHeader(), String.class);

		return new URI(response.getHeaders().getFirst("Location"));
	}

	public URI putRDFData(Model data, URI objectURI) throws LibraryException {
		if(data.size() < 1) {
			logger.warn("Tried to put empty data set into location " + objectURI);
			return objectURI;
		}
		URI metadataURI;
		try {
			metadataURI = new URI(objectURI + "/fcr:metadata");
		} catch (URISyntaxException e) {
			throw new LibraryException(e);
		}
		String dataStr = ModelIO.toString(data, RDFFormat.NTRIPLES);

		HttpClient instance = HttpClientBuilder.create()
				.setRedirectStrategy(new DefaultRedirectStrategy()).build();

		RestTemplate restTemplate = new RestTemplate(
				new HttpComponentsClientHttpRequestFactory(instance));

		RequestEntity request = RequestEntity.put(metadataURI)
				.header("Authorization", authenticationHeader().getHeaders().getFirst("Authorization"))
				//This header lets us overwrite triples without providing data for every triple in the fedora object
				.header("Prefer","handling=lenient; received=\"minimal\"")
				.contentType(new MediaType("application", "n-triples", StandardCharsets.UTF_8))
				.body(dataStr);

		ResponseEntity<String> response = restTemplate.exchange(request, String.class);
		if (response.getStatusCode() == HttpStatus.CREATED ||
				response.getStatusCode() == HttpStatus.NO_CONTENT) {
			logger.info("Successfully added new rdf data at " + objectURI +
					" HttpResponse is " + response.getStatusCode());
			return objectURI;
		} else {
			throw new LibraryException("HTTP Error: "+ response.getStatusCode() + " Error occurred while adding rdf data " + data + " at uri " + objectURI);
		}

	}

	//Patch:
	@Deprecated
	public URI sendPatchRequestForUpdatingTriples(String data, URI objectURI) throws LibraryException {

		logger.info("The Object URI is " + objectURI);
		HttpPatch httpPatch = new HttpPatch(objectURI);

		httpPatch.addHeader(authenticate(httpPatch));
		httpPatch.addHeader("Content-Type", "application/sparql-update");

		HttpClient client = HttpClientBuilder.create().build();

		try {
			InputStream requestEntity = new ByteArrayInputStream(data.getBytes(StandardCharsets.UTF_8));

			httpPatch.setEntity(new InputStreamEntity(requestEntity));
			HttpResponse response = client.execute(httpPatch);

			if (response != null && (response.getStatusLine().getStatusCode() == HttpStatus.NO_CONTENT.value())){
				logger.info("Successfully added triples for object " + objectURI + " and query " + data);
				Header location =  response.getFirstHeader("Location");
				if(location != null)
					return new URI (location.getValue());
				else
					return null;
			} else {
				String err = "Exception occurred while adding properties (triples) for object " + objectURI + " and query " + data + ". Got http response " + response.getStatusLine();
				logger.error(err);
				throw new LibraryException(err);
			}
		} catch (IOException|URISyntaxException e) {
			String err = "Exception occurred while adding properties (triples) for object "
					+ baseURI + objectURI + " and query " + data + " " + e;
			logger.error(err);
			throw new LibraryException(err, e);
		}
	}

	//Delete:
	public void deleteResource(URI objectURI) throws LibraryException {
		HttpClient instance = HttpClientBuilder.create()
				.setRedirectStrategy(new DefaultRedirectStrategy()).build();

		RestTemplate restTemplate = new RestTemplate(
				new HttpComponentsClientHttpRequestFactory(instance));

		ResponseEntity<String> response = restTemplate.exchange(objectURI, HttpMethod.DELETE,
				authenticationHeader(), String.class);

		if (response.getStatusCode() == HttpStatus.GONE || response.getStatusCode() == HttpStatus.NO_CONTENT) {
			logger.info("Fedora resource " + objectURI + " deleted.");
		} else {
			throw new LibraryException("Unable to delete fedora resource " + objectURI + " due to " + response.getBody());
		}

	}


	private void configureBaseURI(){

		ServerDetails configuration = fedoraConfiguration.getFedoraServerConfiguration();
		if(configuration != null){
			try {
				baseURI = new URI(configuration.getUrl());
			} catch (URISyntaxException e) {
				logger.error("Fedora uri is not valid. Check your properties.");
			}
			userName = configuration.getUsername();
			password= configuration.getPassword();

		} else{
			baseURI = null;
			logger.warn("No base uri configured for the fedora server.");
		}
	}

	private HttpEntity<HttpHeaders> authenticationHeader() {
		final String CHARSET = "US-ASCII";
		HttpHeaders header = new HttpHeaders();
		String auth = userName + ":" + password;
		byte[] encodedAuth = Base64.encodeBase64(auth.getBytes(Charset.forName(CHARSET)));
		String authHeader = "Basic " + new String(encodedAuth);
		header.set("Authorization", authHeader);
		return new HttpEntity<>(header);
	}

	public Header authenticate(HttpRequest request) throws LibraryException {
		Header header;
		try {
			header = new BasicScheme(StandardCharsets.UTF_8).authenticate(
					new UsernamePasswordCredentials(userName, password), request, null);
		} catch (AuthenticationException e) {
			String err = "Exception occurred while trying to authenticate at uri " + request + ". " + e;
			logger.error(err);
			throw new LibraryException(err, e);
		}
		return header;
	}
}
