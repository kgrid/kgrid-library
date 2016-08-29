package org.uofm.ot.fusekiAccessLayer;

import java.io.IOException;
import java.net.ConnectException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

import org.apache.http.HttpResponse;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.jena.query.Query;
import org.apache.jena.query.QueryExecution;
import org.apache.jena.query.QueryExecutionFactory;
import org.apache.jena.query.QueryFactory;
import org.apache.jena.query.QuerySolution;
import org.apache.jena.query.ResultSet;
import org.apache.jena.rdf.model.RDFNode;
import org.apache.log4j.Logger;
import org.uofm.ot.dao.SystemConfigurationDAO;
import org.uofm.ot.exception.ObjectTellerException;
import org.uofm.ot.fedoraAccessLayer.ChildType;
import org.uofm.ot.knowledgeObject.Citation;
import org.uofm.ot.knowledgeObject.FedoraObject;
import org.uofm.ot.knowledgeObject.Metadata;
import org.uofm.ot.knowledgeObject.PayloadDescriptor;
import org.uofm.ot.model.Server_details;

public class FusekiService {
	
	private SystemConfigurationDAO sysConfDao;
	
	private String fusekiServerURL;
	
	private String fedoraServerURL; 
	
	private static final Logger logger = Logger.getLogger(FusekiService.class);
	
		
	public void setSysConfDao(SystemConfigurationDAO sysConfDao) {
		this.sysConfDao = sysConfDao;
		initFusekiUrl();
	}
	
	private void initFusekiUrl(){
		Server_details fusekiServer = sysConfDao.getFusekiServerConfiguration();
		if(fusekiServer != null){
			fusekiServerURL = fusekiServer.getComplete_url();
		} 
		Server_details fedoraServer = sysConfDao.getFedoraServerConfiguration();
		if(fedoraServer != null){
			fedoraServerURL = fedoraServer.getComplete_url();
		}
	}
	

	public ArrayList<FedoraObject> getFedoraObjects(boolean published) throws ObjectTellerException {
		ArrayList< FedoraObject> list = new ArrayList<FedoraObject>();
		try {
			if(fusekiServerURL != null ) {
				if(testIfFusekiIsRunning()) {
					String queryString = initQuery(false, null, published);

					list = getFedoraObjects(queryString,true);
				}

			} else {
				logger.error("Fuseki Server URL is not configured");
				ObjectTellerException exception = new ObjectTellerException("Fuseki Server URL is not configured");
				throw exception;
			} 
		} catch (ConnectException ex){
			logger.error("Check if fuseki server up and running. ");
			ObjectTellerException exception = new ObjectTellerException(ex); 
			throw exception;
		}

		return list;
	}


	public String getLibraryName() throws ObjectTellerException {
		String libraryName = null;

		if(fusekiServerURL != null && fedoraServerURL != null) {
			if(testIfFusekiIsRunning()) {
				String queryString = 
						FusekiConstants.PREFIX_OT+"\n"+  
								"SELECT  ?libraryName \n"+
								"WHERE { \n"+ 
								" <"+fedoraServerURL+">  ot:libraryName ?libraryName. \n"+
								"} ";

				Query query = QueryFactory.create(queryString) ;
				QueryExecution execution = QueryExecutionFactory.sparqlService(fusekiServerURL, query);
				ResultSet resultSet = execution.execSelect();

				if (resultSet.hasNext()) {
					QuerySolution binding = resultSet.nextSolution();
					libraryName = binding.get("libraryName").toString();
				}
			}
		} else {
			logger.error("Fuseki Server URL or Fedora Server URL is not configured");
			ObjectTellerException exception = new ObjectTellerException("Fuseki Server URL is not configured");
			throw exception;
		} 

		return libraryName;

	}
	
	private ArrayList<FedoraObject> getFedoraObjects(String queryString,boolean isPublicOnly) throws ConnectException, ObjectTellerException {
		ArrayList< FedoraObject> list = new ArrayList<FedoraObject>();
		Query query = QueryFactory.create(queryString) ;
		QueryExecution execution = QueryExecutionFactory.sparqlService(fusekiServerURL, query);
		ResultSet resultSet = execution.execSelect();

	
		while (resultSet.hasNext()) {
			QuerySolution binding = resultSet.nextSolution();
			FedoraObject fedoraObject = mapQuerySolutionToFedoraObject(binding, false, null, isPublicOnly);
			if(fedoraObject != null)
				list.add(fedoraObject);
		}

		return list;
	}

	public Integer getNumberOfPublishedObjects()  throws ObjectTellerException{
		Integer count = null;

		if(fusekiServerURL != null ) {
			if(testIfFusekiIsRunning()) {
				String queryString = FusekiConstants.PREFIX_OT+"\n"+

			 "SELECT  (COUNT(DISTINCT ?x) AS ?count) \n"+

			 "WHERE {  \n"+
			 "?x  ot:published \"yes\".\n"+ 
			 "} \n" ;

				Query query = QueryFactory.create(queryString) ;
				QueryExecution execution = QueryExecutionFactory.sparqlService(fusekiServerURL, query);
				ResultSet resultSet = execution.execSelect();

				if (resultSet.hasNext()) {
					QuerySolution binding = resultSet.nextSolution();
					String countString = binding.get("count").toString();


					String[] array =  countString.split("\\^\\^");
					if(array.length > 0) {
						count = Integer.valueOf(array[0]);
					}
				}
			}
		} else {
			logger.error("Fuseki Server URL is not configured");
			ObjectTellerException exception = new ObjectTellerException("Fuseki Server URL is not configured");
			throw exception;
		} 

		return count;
	}
	
	private boolean testIfFusekiIsRunning() throws ObjectTellerException{
		boolean result = false; 

		String fusekiURL = fusekiServerURL;
		fusekiURL = fusekiURL.substring(0,fusekiURL.lastIndexOf("/"));

		HttpClient httpClient = new DefaultHttpClient();

		HttpGet httpGetRequest = new HttpGet(fusekiURL);
		
		HttpResponse httpResponse;
		try {
			httpResponse = httpClient.execute(httpGetRequest);
			if ( 200 == httpResponse.getStatusLine().getStatusCode())
				result = true;
		} catch (ClientProtocolException e) {
			logger.error("Not able to connect to the Fuseki with url "+fusekiURL);
			ObjectTellerException exception = new ObjectTellerException(e);
			exception.setErrormessage("Not able to connect to the Fuseki with url "+fusekiURL);
			throw exception;
		} catch (IOException e) {
			ObjectTellerException exception = new ObjectTellerException(e);
			exception.setErrormessage("Not able to connect to the Fuseki with url "+fusekiURL);
			logger.error("Not able to connect to the Fuseki with url "+fusekiURL);
			throw exception;
		}
		
		return result;
	}
	
	public FedoraObject getKnowledgeObject(FedoraObject fedoraObject) throws ObjectTellerException {
		if(fusekiServerURL != null ) {
			if(testIfFusekiIsRunning()) {

				String uri = fedoraServerURL+fedoraObject.getURI();
			
				String queryString = initQuery(true, uri, false);

				Query query = QueryFactory.create(queryString) ;
				QueryExecution execution = QueryExecutionFactory.sparqlService(fusekiServerURL, query);
				ResultSet resultSet = execution.execSelect();
				
				while(resultSet.hasNext()) {
					QuerySolution querySolution = resultSet.next();
					fedoraObject = mapQuerySolutionToFedoraObject(querySolution,true,uri,false);
				}
			}
		} else {
			logger.error("Fuseki Server URL is not configured");
			ObjectTellerException exception = new ObjectTellerException("Fuseki Server URL is not configured");
			throw exception;
		} 
		return fedoraObject;
	
	} 
	
	private Date convertRDFNodetoDate(RDFNode o) throws ObjectTellerException{
		Date convertedDate = null;
		try {
			String date = o.toString();
			date = date.substring(0, 10);
			convertedDate = new SimpleDateFormat("yyyy-MM-dd", Locale.ENGLISH).parse(date);
		} catch (ParseException e) {
			logger.error("Unable to parse created on date or last updated date" + e.getCause());
			ObjectTellerException exception = new ObjectTellerException(e);
			exception.setErrormessage("Unable to parse created on date or last updated date" + e.getCause());
			throw exception;
		}
		return convertedDate;
	}
	
	public PayloadDescriptor getPayloadProperties( String objectURI) throws ObjectTellerException {
		PayloadDescriptor descriptor = null;
		if(fusekiServerURL != null ) {
			if(testIfFusekiIsRunning()) {

				String uri = fedoraServerURL+objectURI+"/"+ChildType.PAYLOAD.getChildType();
				String queryString = FusekiConstants.PREFIX_OT+"\n"+

				 "SELECT  ?p ?o \n"+

				 "WHERE {  \n"+
				 "<"+uri+">  ?p ?o.\n"+ 
				 "} \n" ;

				Query query = QueryFactory.create(queryString) ;
				QueryExecution execution = QueryExecutionFactory.sparqlService(fusekiServerURL, query);
				ResultSet resultSet = execution.execSelect();

				descriptor = new PayloadDescriptor();
				
				while (resultSet.hasNext()) {
					QuerySolution binding = resultSet.nextSolution();
					String predicate = binding.get("p").toString();
					if(predicate.contains(FusekiConstants.OT_NAMESPACE) == true) {
						String actualPredicate = predicate.substring(FusekiConstants.OT_NAMESPACE.length());
						if("functionName".equals(actualPredicate) )
							descriptor.setFunctionName(binding.get("o").toString());

						if( "executorType".equals(actualPredicate)) 
							descriptor.setEngineType(binding.get("o").toString());
					}
				}
			}
		} else {
			logger.error("Fuseki Server URL is not configured");
			ObjectTellerException exception = new ObjectTellerException("Fuseki Server URL is not configured");
			throw exception;
		} 
		return descriptor;
	} 
	
	public String getObjectProvProperties(String objectURI) throws ObjectTellerException {
		String logData = "";
		if(fusekiServerURL != null ) {
			if(testIfFusekiIsRunning()) {

				String uri = fedoraServerURL+objectURI;
				String queryString = FusekiConstants.PREFIX_OT+"\n"+

				 "SELECT  ?p ?o \n"+

				 "WHERE {  \n"+
				 "<"+uri+">  ?p ?o.\n"+ 
				 "} \n" ;

				Query query = QueryFactory.create(queryString) ;
				QueryExecution execution = QueryExecutionFactory.sparqlService(fusekiServerURL, query);
				ResultSet resultSet = execution.execSelect();

				while (resultSet.hasNext()) {
					QuerySolution binding = resultSet.nextSolution();
					String predicate = binding.get("p").toString();
					if(predicate.contains(FusekiConstants.PROV_NAMESPACE) == true) {
						String actualPredicate = predicate.substring(FusekiConstants.PROV_NAMESPACE.length());
						logData = logData + objectURI + " " + actualPredicate + " " + binding.get("o").toString() +" \n ";
					}
				}
			}
		} else {
			logger.error("Fuseki Server URL is not configured");
			ObjectTellerException exception = new ObjectTellerException("Fuseki Server URL is not configured");
			throw exception;
		} 
		return logData;
	}


	public List<Citation> getObjectCitations(String objectURI) throws ObjectTellerException {
		ArrayList<Citation> citations = new ArrayList<Citation>();
		String uri = fedoraServerURL+objectURI+"/"+ChildType.CITATIONS.getChildType();
		
		if(fusekiServerURL != null ) {
			if(testIfFusekiIsRunning()) {
		
				String queryString = FusekiConstants.PREFIX_FEDORA+"\n"+
							"SELECT  ?x  \n"+ 
							"WHERE { \n"+ 
							"?x  fedora:hasParent  <"+uri+">. \n"+ 
							"} \n";
				Query query = QueryFactory.create(queryString) ;
				QueryExecution execution = QueryExecutionFactory.sparqlService(fusekiServerURL, query);
				ResultSet resultSet = execution.execSelect();

				while (resultSet.hasNext()) {
					QuerySolution binding = resultSet.nextSolution();
					String child = binding.get("x").toString();
					Citation citation = new Citation();
					getCitationProperties(citation,child);
					String citationId = child.substring((uri+ "/").length());
					citation.setCitation_id(citationId);
					citations.add(citation);
				}
			}
		} else {
			logger.error("Fuseki Server URL is not configured");
			ObjectTellerException exception = new ObjectTellerException("Fuseki Server URL is not configured");
			throw exception;
		} 
		
		return citations; 
	}
	
	private void getCitationProperties(Citation citation, String uri){
		String queryString = FusekiConstants.PREFIX_OT+"\n"+
				"SELECT ?citationTitle  ?citationAt \n"+ 
				"WHERE { \n"+ 
				"<"+uri+">  ot:citationTitle ?citationTitle; \n"+ 
				"	ot:citationAt ?citationAt. \n"+
				"} \n";
		Query query = QueryFactory.create(queryString) ;
		QueryExecution execution = QueryExecutionFactory.sparqlService(fusekiServerURL, query);
		ResultSet resultSet = execution.execSelect();

		if(resultSet.hasNext()){
			QuerySolution binding = resultSet.nextSolution();
			String citationTitle = binding.get("citationTitle").toString();
			String citationAt = binding.get("citationAt").toString();
			citation.setCitation_at(citationAt);
			citation.setCitation_title(citationTitle);
		}
	}


	
	private String initQuery(boolean isSingleObject, String uri , boolean published ) {
		String query = FusekiConstants.PREFIX_OT + "\n"+
				FusekiConstants.PREFIX_DC + "\n"+
				FusekiConstants.PREFIX_FEDORA + "\n" + 
				"SELECT  * " + "\n" +
				"WHERE { " + "\n" ; 

		if(isSingleObject ) {
			query =  query + "<"+uri+"> dc:title ?title ; "+ "\n" ; 
		} else {
			query =  query + "?x dc:title ?title ; "+ "\n" ;
		}
	
		if(published)
			query =  query + "ot:published \"yes\" ; " + "\n" ;
		else
			query =  query + "ot:published ?published ; " + "\n" ;
    										  
											  
		query = query + " fedora:created ?created ; "+" \n "+
     					" fedora:lastModified ?lastModified ; "+" \n "+
     					" ot:keywords ?keywords ;"+" \n "+
  						" ot:owner ?owner ;"+" \n "+
    					" ot:contributors ?contributors ;"+" \n "+
    					" ot:description ?description ;"+" \n "+
    
        "} ";
		
		return query ;
	}
	
	private FedoraObject mapQuerySolutionToFedoraObject(QuerySolution querySolution,boolean isSingleObject,String existingUri, boolean published ) throws ObjectTellerException {

		FedoraObject fedoraObject =null;
		if(!isSingleObject){
			String uri = querySolution.get("x").toString();
			if(uri.length() > fedoraServerURL.length()){
				if(uri.contains(fedoraServerURL)) {
					fedoraObject = new FedoraObject();
					uri = uri.substring(fedoraServerURL.length());
					fedoraObject.setURI(uri);
				}
			} 
		} else {
			fedoraObject = new FedoraObject();
			fedoraObject.setURI(existingUri);
		}


		if(fedoraObject != null ) {
			Metadata metadata = new Metadata();

			if(querySolution.get("published") != null) {
				if("YES".equals(querySolution.get("published").toString().toUpperCase()))
					metadata.setPublished(true);
				else
					metadata.setPublished(false);
			} else {
				if(published){
					metadata.setPublished(true);
				}
			}

			metadata.setTitle(querySolution.get("title").toString());
			Date createdOn = convertRDFNodetoDate(querySolution.get("created"));
			Date lastModified = convertRDFNodetoDate(querySolution.get("lastModified"));
			metadata.setLastModified(lastModified);
			metadata.setCreatedOn(createdOn);
			metadata.setKeywords(querySolution.get("keywords").toString());
			metadata.setOwner(querySolution.get("owner").toString());
			metadata.setContributors(querySolution.get("contributors").toString());
			metadata.setDescription(querySolution.get("description").toString());

			fedoraObject.setMetadata(metadata);
		}
		return fedoraObject;
	}
}
