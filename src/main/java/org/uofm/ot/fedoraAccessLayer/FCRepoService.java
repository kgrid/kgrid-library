package org.uofm.ot.fedoraAccessLayer;

import java.io.BufferedInputStream;
import java.net.URI;
import java.net.URISyntaxException;
import java.nio.charset.Charset;
import org.apache.http.Header;
import org.apache.http.HttpEntity;
import org.apache.http.HttpRequest;
import org.apache.http.HttpResponse;
import org.apache.http.auth.AuthenticationException;
import org.apache.http.auth.UsernamePasswordCredentials;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.*;
import org.apache.http.entity.InputStreamEntity;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.auth.BasicScheme;
import org.apache.http.impl.client.HttpClientBuilder;
import org.apache.log4j.Logger;
import org.springframework.http.HttpStatus;
import org.uofm.ot.knowledgeObject.ArkId;
import org.uofm.ot.services.FedoraConfiguration;
import org.uofm.ot.exception.ObjectTellerException;
import org.uofm.ot.model.ServerDetails;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;


public class FCRepoService {

	private FedoraConfiguration fedoraConfiguration;

	protected URI baseURI;

	protected String userName;

	protected String password;

	private static final Logger logger = Logger.getLogger(FCRepoService.class);

	public void setFedoraConfiguration(FedoraConfiguration fedoraConfiguration) {
		this.fedoraConfiguration = fedoraConfiguration;
		configureBaseURI();
	}

	public URI getBaseURI() {
		return baseURI;
	}

	// Transaction Handling:
	public URI createTransaction() throws ObjectTellerException, URISyntaxException {

		URI transactionURI = new URI(baseURI + "fcr:tx/");

		HttpPost httpPost = new HttpPost(transactionURI);
		httpPost.addHeader(authenticate(httpPost));

		HttpClient httpClient = HttpClientBuilder.create().build();

		try {
			HttpResponse response = httpClient.execute(httpPost) ;
			if(response.getStatusLine().getStatusCode() == HttpStatus.CREATED.value()) {
				transactionURI = new URI(response.getFirstHeader("Location").getValue());
			} else {
				String err = "Unable to create transaction.";
				logger.error(err);
				throw new ObjectTellerException(err);
			}
		} catch (IOException e) {
			String err = "Exception occurred while creating transaction " + e.getMessage();
			logger.error(err);
			throw new ObjectTellerException(err, e);
		}
		return transactionURI;
	}

	public void commitTransaction(URI transactionURI) throws ObjectTellerException, URISyntaxException {
		URI transactionCommitURL = new URI(transactionURI + "/fcr:tx/fcr:commit/");

		HttpPost httpPost = new HttpPost(transactionCommitURL);
		httpPost.addHeader(authenticate(httpPost));

		HttpClient httpClient = HttpClientBuilder.create().build();

		try {
			HttpResponse response = httpClient.execute(httpPost) ;
			if(response.getStatusLine().getStatusCode() == HttpStatus.NO_CONTENT.value()) {
				logger.info("Transaction " + transactionURI + " committed");
			} else {
				String err = "Unable to commit transaction with Id " + transactionURI;
				logger.error(err);
				throw new ObjectTellerException(err);
			}

		} catch (IOException e) {
			String err = "Exception occurred while committing the transaction with id " + transactionURI + ". " + e.getMessage();
			logger.error(err);
			throw new ObjectTellerException(err, e);
		}
	}

	public void rollbackTransaction(URI transactionURI) throws ObjectTellerException, URISyntaxException {
		URI transactionRollBackURL = new URI(transactionURI + "/fcr:tx/fcr:rollback");

		HttpPost httpPost = new HttpPost(transactionRollBackURL);
		httpPost.addHeader(authenticate(httpPost));

		HttpClient httpClient = HttpClientBuilder.create().build();
		try {
			HttpResponse response = httpClient.execute(httpPost) ;
			if(response.getStatusLine().getStatusCode() == HttpStatus.NO_CONTENT.value()) {
				logger.info("Transaction " + transactionURI + " rolled back.");
			} else {
				String err = "Unable to roll back transaction with transaction URI " + transactionURI;
				logger.error(err);
				throw new ObjectTellerException(err);
			}

		} catch (IOException e) {
			String err = "Exception occurred while rolling back the transaction with transaction URI " + transactionURI + ". " + e.getMessage();
			logger.error(err);
			throw new ObjectTellerException(err, e);
		}
	}

	//Get:
	public boolean checkIfObjectExists(URI objectURI) throws ObjectTellerException{

		HttpClient httpClient = HttpClientBuilder.create().build();

		HttpGet httpGetRequest = new HttpGet(objectURI);

		httpGetRequest.addHeader(authenticate(httpGetRequest));

		try {
			HttpResponse httpResponse = httpClient.execute(httpGetRequest);
			if (httpResponse.getStatusLine().getStatusCode() == HttpStatus.OK.value())
				return true;
		} catch (IOException e) {
			String err = "Exception occurred while verifying object id "+ objectURI +"."+ e.getMessage();
			logger.error(err);
			throw new ObjectTellerException(err, e);
		}
		return false;
	}

	public String getObjectContent(String objectId, String dataStreamId) throws ObjectTellerException  {

		HttpClient httpClient = HttpClientBuilder.create().build();

		HttpGet httpGetRequest = new HttpGet(baseURI+objectId+"/"+dataStreamId+"/");
		httpGetRequest.addHeader(authenticate(httpGetRequest));

		StringBuffer chunk = new StringBuffer();
		HttpResponse httpResponse;

		try {
			httpResponse = httpClient.execute(httpGetRequest);
			HttpEntity entity = httpResponse.getEntity();
			if(httpResponse.getStatusLine().getStatusCode() == 200){

				byte[] buffer = new byte[4096];
				if (entity != null) {
					InputStream inputStream = entity.getContent();
					int bytesRead = 0;
					BufferedInputStream bis = new BufferedInputStream(inputStream);

					while ((bytesRead = bis.read(buffer)) != -1) {
						// TODO: Get fedora to store things in a better character encoding and change this
						chunk.append(new String(buffer, 0, bytesRead, Charset.forName("ISO-8859-1")));
					}
				}
			} else {
				ObjectTellerException exception = new ObjectTellerException();
				logger.error("Exception occured while retrieving object content for object "+objectId+"/"+dataStreamId+". Request status code is "+httpResponse.getStatusLine().getStatusCode());
				exception.setErrormessage("Exception occured while retrieving object content for object "+objectId+"/"+dataStreamId+". Request status code is "+httpResponse.getStatusLine().getStatusCode());
				throw exception;
			}

		} catch (IOException e) {
			ObjectTellerException exception = new ObjectTellerException(e);
			logger.error("Exception occured while retrieving object content for object "+objectId+"/"+dataStreamId + e.getMessage());
			exception.setErrormessage("Exception occured while retrieving object content for object "+objectId+"/"+dataStreamId + e.getMessage());
			throw exception;
		}
		return chunk.toString();
	}

	//Post:
	public URI createContainerWithAutoGeneratedName(URI parent) throws ObjectTellerException {

		URI containerLocation;

		HttpPost httpPost = new HttpPost(parent);

		httpPost.addHeader(authenticate(httpPost));

		HttpClient httpClient = HttpClientBuilder.create().build();

		try {
			HttpResponse response = httpClient.execute(httpPost);
			if(response.getStatusLine().getStatusCode() == HttpStatus.CREATED.value()) {
				containerLocation = new URI(response.getFirstHeader("Location").getValue());

			} else {
				String err = "Unable to create child resource for parent " + parent;
				logger.error(err);
				throw new ObjectTellerException(err);
			}
		} catch (IOException|URISyntaxException e) {
			String err = "Exception occurred while creating child resource for parent " + parent + " " + e.getMessage();
			logger.error(err);
			throw new ObjectTellerException(err, e);
		}

		return containerLocation;
	}

	// Put:
	public void putBinary(String binary, ArkId objIdentifier, String type) throws ObjectTellerException, URISyntaxException {
		URI objURI = new URI(baseURI + objIdentifier.getFedoraPath());
		putBinary(binary, objURI, type);
	}

	public void putBinary(String binary, URI objectURI, String type) throws ObjectTellerException {

		HttpPut httpPutRequestPayload = new HttpPut(objectURI + "/" + type);

		httpPutRequestPayload.addHeader(authenticate(httpPutRequestPayload));

		HttpClient httpClient = HttpClientBuilder.create().build();

		try {
			StringEntity entity = new StringEntity(binary != null ? binary : "");
			httpPutRequestPayload.setEntity(entity);
			HttpResponse response = httpClient.execute(httpPutRequestPayload);

			if(response != null &&
					(response.getStatusLine().getStatusCode() == HttpStatus.CREATED.value() ||
							response.getStatusLine().getStatusCode() == HttpStatus.NO_CONTENT.value())) {
				logger.info("Binary added successfully in the Object " + httpPutRequestPayload.getURI());
			} else {
				String err = "Exception occurred while creating binary of type" + type + " for object " + objectURI + ". HTTPResponse is " + response;
				logger.error(err);
				throw new ObjectTellerException(err);
			}
		} catch (IOException e) {
			String errString = "Exception occurred while creating binary of type" + type + " for object " + objectURI + ". " + e.getMessage();
			logger.error(errString);
			throw new ObjectTellerException(errString, e);
		}
	}

	public URI createContainer(URI uri, String objectID) throws ObjectTellerException, URISyntaxException {
		HttpClient httpClient = HttpClientBuilder.create().build();
		URI objectURI = new URI(uri + "/" + objectID);

		HttpPut httpPutRequest = new HttpPut(objectURI);

		httpPutRequest.addHeader(authenticate(httpPutRequest));

		try {
			HttpResponse httpResponse = httpClient.execute(httpPutRequest);
			if (httpResponse != null
					&& httpResponse.getStatusLine().getStatusCode() == HttpStatus.CREATED.value()) {
				logger.info("Successfully added new container " + objectURI);
				return new URI(httpResponse.getFirstHeader("Location").getValue());
			} else {
				String err =
						"Error occurred while creating object " + objectURI + " HttpResponse is "
								+ httpResponse;
				logger.error(err);
				throw new ObjectTellerException(err);

			}
		} catch (IOException e) {
			String err = "Exception occurred while creating the container " + objectURI + ". " + e
					.getMessage() + " caused by " + e.getCause();
			logger.error(err);
			throw new ObjectTellerException(err, e);
		}
	}

	//Patch:
	public URI sendPatchRequestForUpdatingTriples(String data, String objectURI) throws ObjectTellerException, URISyntaxException {
		URI uri = new URI(baseURI + objectURI);
		return sendPatchRequestForUpdatingTriples(data, uri);
	}

	public URI sendPatchRequestForUpdatingTriples(String data, URI objectURI) throws ObjectTellerException {

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
				throw new ObjectTellerException(err);
			}
		} catch (IOException|URISyntaxException e) {
			String err = "Exception occurred while adding properties (triples) for object "
					+ baseURI + objectURI + " and query " + data + " " + e;
			logger.error(err);
			throw new ObjectTellerException(err, e);
		}
	}

	//Delete:
	public void deleteFedoraResource(URI deleteResourceURI) throws ObjectTellerException {
		try {
			HttpDelete httpDelete = new HttpDelete(deleteResourceURI);
			httpDelete.addHeader(authenticate(httpDelete));

			HttpClient httpClient = HttpClientBuilder.create().build();
			HttpResponse response = httpClient.execute(httpDelete);
			if (response.getStatusLine().getStatusCode() == HttpStatus.GONE.value()
					|| response.getStatusLine().getStatusCode() == HttpStatus.NO_CONTENT.value()) {
				logger.info("Fedora resource " + deleteResourceURI + " deleted.");
			} else {
				String err = "Unable to delete fedora resource " + deleteResourceURI;
				logger.error(err);
				throw new ObjectTellerException(err);
			}

		} catch (IOException e) {
			String err =
					"Exception occurred while deleting fedora resource with URI " + deleteResourceURI + ". "
							+ e.getMessage();
			logger.error(err);
			throw new ObjectTellerException(err, e);
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

	private Header authenticate(HttpRequest request) throws ObjectTellerException {
		Header header;
		try {
			header = new BasicScheme(StandardCharsets.UTF_8).authenticate(
					new UsernamePasswordCredentials(userName, password), request, null);
		} catch (AuthenticationException e) {
			String err = "Exception occurred while trying to authenticate at uri " + request + ". " + e;
			logger.error(err);
			throw new ObjectTellerException(err, e);
		}
		return header;
	}

}
