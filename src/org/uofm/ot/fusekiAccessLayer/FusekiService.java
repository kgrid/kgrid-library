package org.uofm.ot.fusekiAccessLayer;

import java.io.IOException;
import java.net.ConnectException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Locale;

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
import org.uofm.ot.fedoraAccessLayer.FedoraObject;
import org.uofm.ot.fedoraAccessLayer.PayloadDescriptor;
import org.uofm.ot.model.Server_details;


public class FusekiService {
	
	private SystemConfigurationDAO sysConfDao;
	
	private String fusekiServerURL;
	
	private String fedoraServerURL; 
	
	private static final Logger logger = Logger.getLogger(FusekiService.class);
	
	private String fusekiSubjectBaseURI = "http://localhost:8080/fcrepo/rest/";
		
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
	

	public ArrayList<FedoraObject> getPublicFedoraObjects() throws ObjectTellerException {
		ArrayList< FedoraObject> list = new ArrayList<FedoraObject>();

		try {
			if(fusekiServerURL != null ) {
				if(testIfFusekiIsRunning()) {
					String queryString = FusekiConstants.PREFIX_DC + "\n"+
							FusekiConstants.PREFIX_FEDORA+ "\n"+
							FusekiConstants.PREFIX_OT+"\n"+

					"SELECT  ?s ?title ?published ?created (MAX(?lastModified) AS ?lastUpdated) \n"+

					"WHERE \n"+
					"{ \n"+ 
					"?s  dc:title  ?title. \n"+
					"?s  ot:published \"yes\". \n"+
					"?s  fedora:lastModified  ?lastModified. \n"+
					"?s  fedora:created ?created. \n"+

					"} \n"+
					"GROUP BY ?s ?title ?published ?created";


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

	public ArrayList<FedoraObject> getAllFedoraObjects() throws ObjectTellerException {
		ArrayList< FedoraObject> list = new ArrayList<FedoraObject>();

		try{
			if(fusekiServerURL != null ) {
				if(testIfFusekiIsRunning()) {
					String queryString = FusekiConstants.PREFIX_DC + "\n"+
							FusekiConstants.PREFIX_FEDORA+ "\n"+
							FusekiConstants.PREFIX_OT+"\n"+

					"SELECT  ?s ?title ?published ?created (MAX(?lastModified) AS ?lastUpdated) \n"+

					"WHERE \n"+
					"{ \n"+ 
					"?s  dc:title  ?title. \n"+
					"?s  ot:published ?published. \n"+
					"?s  fedora:lastModified  ?lastModified. \n"+
					"?s  fedora:created ?created. \n"+

					"} \n"+
					"GROUP BY ?s ?title ?published ?created";


					list = getFedoraObjects(queryString,false);

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
	
	private ArrayList<FedoraObject> getFedoraObjects(String queryString, Boolean isPublicOnly) throws ConnectException, ObjectTellerException {
		ArrayList< FedoraObject> list = new ArrayList<FedoraObject>();
		Query query = QueryFactory.create(queryString) ;
		QueryExecution execution = QueryExecutionFactory.sparqlService(fusekiServerURL, query);
		ResultSet resultSet = execution.execSelect();

		while (resultSet.hasNext()) {
			QuerySolution binding = resultSet.nextSolution();

			FedoraObject fedoraObject = new FedoraObject();
			String uri = binding.get("s").toString();
			int startIndex = uri.lastIndexOf("/");
			uri = uri.substring(startIndex+1);
			fedoraObject.setURI(uri);
			
			
			if(binding.get("published") != null) {
				if("YES".equals(binding.get("published").toString().toUpperCase()))
					fedoraObject.setPublished(true);
				else
					fedoraObject.setPublished(false);
			} else {
				if(isPublicOnly){
					fedoraObject.setPublished(true);
				}
			}
			
			fedoraObject.setTitle(binding.get("title").toString());
			Date createdOn = convertRDFNodetoDate(binding.get("created"));
			Date lastModified = convertRDFNodetoDate(binding.get("lastUpdated"));
			fedoraObject.setLastModified(lastModified);
			fedoraObject.setCreatedOn(createdOn);

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
	
	public FedoraObject getObjectProperties(FedoraObject fedoraObject) throws ObjectTellerException {
		HashMap<String, Object> keyValue = new HashMap<String, Object>();
		if(fusekiServerURL != null ) {
			if(testIfFusekiIsRunning()) {

				String uri = fusekiSubjectBaseURI+fedoraObject.getURI();
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
					if(predicate.contains(FusekiConstants.DC_NAMESPACE) == true) {
						String actualPredicate = predicate.substring(FusekiConstants.DC_NAMESPACE.length());
						if(!keyValue.containsKey(actualPredicate))
							keyValue.put(actualPredicate, binding.get("o"));
						else {
							keyValue.replace(actualPredicate, binding.get("o"));
						}
						if("title".equals(actualPredicate)){
							fedoraObject.setTitle(binding.get("o").toString());
						}
					} else {
						if(predicate.contains(FusekiConstants.FEDORA_NAMESPACE) == true){
							String actualPredicate = predicate.substring(FusekiConstants.FEDORA_NAMESPACE.length());
							if(!keyValue.containsKey(actualPredicate))
								keyValue.put(actualPredicate, binding.get("o"));
							else {
								keyValue.replace(actualPredicate, binding.get("o"));
							}
							if("lastModified".equals(actualPredicate)  )
								fedoraObject.setLastModified(convertRDFNodetoDate(binding.get("o")));

							if( "created".equals(actualPredicate))
								fedoraObject.setCreatedOn(convertRDFNodetoDate(binding.get("o")));

						} else {
							if(predicate.contains(FusekiConstants.OT_NAMESPACE) == true) {
								String actualPredicate = predicate.substring(FusekiConstants.OT_NAMESPACE.length());
								if(!keyValue.containsKey(actualPredicate))
									keyValue.put(actualPredicate, binding.get("o"));
								else {
									keyValue.replace(actualPredicate, binding.get("o"));
								}
								if("keywords".equals(actualPredicate) )
									fedoraObject.setKeywords(binding.get("o").toString());

								if( "owner".equals(actualPredicate)) 
									fedoraObject.setOwner(binding.get("o").toString());

								if( "contributors".equals(actualPredicate) )
									fedoraObject.setContributors(binding.get("o").toString());

								if( "description".equals(actualPredicate))
									fedoraObject.setDescription(binding.get("o").toString());

								if( "published".equals(actualPredicate)) {
									if("YES".equals(binding.get("o").toString().toUpperCase()))
										fedoraObject.setPublished(true);
									else
										fedoraObject.setPublished(false);
								}
							}
						}
					}
				}
			}
		} else {
			logger.error("Fuseki Server URL is not configured");
			ObjectTellerException exception = new ObjectTellerException("Fuseki Server URL is not configured");
			throw exception;
		} 
		
		/*fedoraObject.setTitle(keyValue.get("title").toString());
		fedoraObject.setLastModified(convertRDFNodetoDate((RDFNode)keyValue.get("lastModified")));
		fedoraObject.setCreatedOn(convertRDFNodetoDate((RDFNode)keyValue.get("created")));
		fedoraObject.setKeywords(keyValue.get("keywords").toString());
		fedoraObject.setOwner(keyValue.get("owner").toString());
		fedoraObject.setContributors(keyValue.get("contributors").toString());
		fedoraObject.setDescription(keyValue.get("description").toString());
		if("YES".equals(keyValue.get("published").toString().toUpperCase()))
			fedoraObject.setPublished(true);
		else
			fedoraObject.setPublished(false);*/
		
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

				String uri = fusekiSubjectBaseURI+objectURI+"/"+ChildType.PAYLOAD.getChildType();
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

				String uri = fusekiSubjectBaseURI+objectURI;
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

}
