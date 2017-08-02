package org.uofm.ot.fusekiGateway;

import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.HttpClientBuilder;
import org.apache.jena.arq.querybuilder.SelectBuilder;
import org.apache.jena.query.*;
import org.apache.jena.rdf.model.RDFNode;
import org.apache.log4j.Logger;
import org.uofm.ot.exception.ObjectNotFoundException;
import org.uofm.ot.services.FedoraConfiguration;
import org.uofm.ot.exception.ObjectTellerException;
import org.uofm.ot.knowledgeObject.*;
import org.uofm.ot.model.ServerDetails;

import java.io.IOException;
import java.net.ConnectException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Locale;

public class FusekiService {
	
	private FedoraConfiguration fedoraConfiguration;
	
	private String fusekiServerURL;
	
	private static final Logger logger = Logger.getLogger(FusekiService.class);
	
		
	public void setFedoraConfiguration(FedoraConfiguration fedoraConfiguration) {
		this.fedoraConfiguration = fedoraConfiguration;
		initFusekiUrl();
	}
	
	private void initFusekiUrl(){
		ServerDetails fusekiServer = fedoraConfiguration.getFusekiServerConfiguration();
		if(fusekiServer != null){
			fusekiServerURL = fusekiServer.getUrl();
		}
	}

	public ArrayList<KnowledgeObject> getKnowledgeObjects(boolean published) throws ObjectTellerException {
		ArrayList< KnowledgeObject> list = new ArrayList<>();
		try {
			if(fedoraConfiguration.getFusekiServerConfiguration().getUrl() != null ) {
				if(testIfFusekiIsRunning()) {
					Query query = initQuery(published);

					list = getFedoraObjects(query);
				}
			} else {
				logger.error("Fuseki Server URL is not configured");
				throw new ObjectTellerException("Fuseki Server URL is not configured");
			} 
		} catch (ConnectException ex){
			logger.error("Check if fuseki server up and running. ");
			throw new ObjectTellerException(ex);
		}

		return list;
	}

	private Query initQuery(boolean published) {

		String subject = "?x";

		SelectBuilder getAllObjects = new SelectBuilder()
				.addPrefix(NamespaceConstants.OT_NAMESPACE_PREFIX, NamespaceConstants.OT_NAMESPACE_URL)
				.addPrefix(NamespaceConstants.DC_NAMESPACE_PREFIX, NamespaceConstants.DC_NAMESPACE_URL)
				.addPrefix(NamespaceConstants.FEDORA_NAMESPACE_PREFIX, NamespaceConstants.FEDORA_NAMESPACE_URL)
				.addVar("*")
				.addWhere(subject, "dc:title", "?title")
				.addWhere(subject, "fedora:created", "?created")
				.addWhere(subject, "fedora:lastModified", "?lastModified")
				.addOptional(subject, "ot:published", "?published")
				.addOptional(subject, "ot:keywords", "?keywords")
				.addOptional(subject, "ot:owner", "?owner")
				.addOptional(subject, "ot:contributors", "?contributors")
				.addOptional(subject, "ot:description", "?description")
				.addOptional(subject, "ot:arkId", "?arkId")
				.addOptional(subject, "ot:licenseName", "?licenseName")
				.addOptional(subject, "ot:licenseLink", "?licenseLink");

		if(published) {
			try {
				getAllObjects.addFilter("?published = \"yes\" || ?published = true");
			} catch(org.apache.jena.sparql.lang.sparql_11.ParseException e) {
				logger.error("Invalid filter for published objects " + e);
			}
		}
		return getAllObjects.build();
	}

	private ArrayList<KnowledgeObject> getFedoraObjects(Query query) throws ConnectException, ObjectTellerException {
		ArrayList< KnowledgeObject> list = new ArrayList<>();
		QueryExecution execution = QueryExecutionFactory.sparqlService(fusekiServerURL, query);
		ResultSet resultSet = execution.execSelect();

		while (resultSet.hasNext()) {
			QuerySolution binding = resultSet.nextSolution();
			KnowledgeObject knowledgeObject = mapQuerySolutionToFedoraObject(binding);
			if(knowledgeObject != null) {
				list.add(knowledgeObject);
			}
		}

		return list;
	}
	
	private boolean testIfFusekiIsRunning() throws ObjectTellerException{
		boolean result;

		String fusekiURL = fusekiServerURL;
		fusekiURL = fusekiURL.substring(0,fusekiURL.lastIndexOf("/"));

		HttpClient httpClient = HttpClientBuilder.create().build();

		HttpGet httpGetRequest = new HttpGet(fusekiURL);
		
		HttpResponse httpResponse;
		try {
			httpResponse = httpClient.execute(httpGetRequest);
			if ( 200 == httpResponse.getStatusLine().getStatusCode()) {
				result = true;
			} else {
				throw new ObjectNotFoundException("Cannot connect to fuseki service, throws " +
				httpResponse.getStatusLine() + " error. Check the application configuration fuseki url and your fuseki server");
			}
		} catch (IOException e) {
			logger.error("Not able to connect to the Fuseki with url "+fusekiURL);
			throw new ObjectTellerException("Not able to connect to the Fuseki with url "+fusekiURL, e);
		}
		return result;
	}
	
	private Date convertRDFNodetoDate(RDFNode o) throws ObjectTellerException{
		Date convertedDate;
		try {
			String date = o.toString();
			date = date.substring(0, 10);
			convertedDate = new SimpleDateFormat("yyyy-MM-dd", Locale.ENGLISH).parse(date);
		} catch (ParseException e) {
			logger.error("Unable to parse created on date or last updated date" + e.getCause());
			throw new ObjectTellerException("Unable to parse created on date or last updated date" + e.getCause(), e);
		}
		return convertedDate;
	}
	
	private KnowledgeObject mapQuerySolutionToFedoraObject(QuerySolution querySolution ) throws ObjectTellerException {

		// create knowledge object and set ark id

		RDFNode ark_node = querySolution.get("arkId");
		if (ark_node == null) {
			throw new ObjectTellerException("The object with iri " + querySolution.get("x") + " does not have an ark id.");
		}

		KnowledgeObject knowledgeObject = new KnowledgeObject(new ArkId(ark_node.toString()));

		Metadata metadata = new Metadata();

		if(querySolution.get("published") != null) {
			String publishedStringUC = querySolution.get("published").toString().toUpperCase();
			if(publishedStringUC.startsWith("YES") || publishedStringUC.startsWith("TRUE"))
				metadata.setPublished(true);
			else
				metadata.setPublished(false);
		} else {
				metadata.setPublished(false);
		}

		metadata.setTitle(querySolution.get("title").toString());
		Date createdOn = convertRDFNodetoDate(querySolution.get("created"));
		Date lastModified = convertRDFNodetoDate(querySolution.get("lastModified"));
		metadata.setLastModified(lastModified);
		metadata.setCreatedOn(createdOn);
		if(querySolution.get("keywords") != null)
			metadata.setKeywords(querySolution.get("keywords").toString());

		if(querySolution.get("owner") != null)
			metadata.setOwner(querySolution.get("owner").toString());

		if(querySolution.get("contributors") != null)
			metadata.setContributors(querySolution.get("contributors").toString());

		if(querySolution.get("description") != null)
			metadata.setDescription(querySolution.get("description").toString());

		License license = new License();
		if(querySolution.get("licenseName") != null)
			license.setLicenseName(querySolution.get("licenseName").toString());

		if(querySolution.get("licenseLink") != null)
			license.setLicenseLink(querySolution.get("licenseLink").toString());

		if(querySolution.get("arkId") != null)
			metadata.setArkId(ark_node.toString());

		metadata.setLicense(license);

		knowledgeObject.setMetadata(metadata);

		return knowledgeObject;
	}
}
