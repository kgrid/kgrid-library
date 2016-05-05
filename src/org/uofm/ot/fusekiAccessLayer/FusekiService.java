package org.uofm.ot.fusekiAccessLayer;

import java.io.IOException;
import java.net.ConnectException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Locale;

import org.apache.http.HttpResponse;
import org.apache.http.auth.UsernamePasswordCredentials;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.auth.BasicScheme;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.jena.query.Query;
import org.apache.jena.query.QueryExecution;
import org.apache.jena.query.QueryExecutionFactory;
import org.apache.jena.query.QueryFactory;
import org.apache.jena.query.QuerySolution;
import org.apache.jena.query.ResultSet;
import org.uofm.ot.dao.SystemConfigurationDAO;
import org.uofm.ot.exception.ObjectTellerException;
import org.uofm.ot.fedoraAccessLayer.FedoraObject;
import org.uofm.ot.model.Server_details;
import org.apache.log4j.Logger;

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
	

	public ArrayList<FedoraObject> getPublicFedoraObjects() throws ObjectTellerException {
		ArrayList< FedoraObject> list = new ArrayList<FedoraObject>();

		try {
			if(fusekiServerURL != null ) {
				if(testIfFusekiIsRunning()) {
					String queryString = FusekiConstants.PREFIX_DC + "\n"+
							FusekiConstants.PREFIX_FEDORA+ "\n"+
							FusekiConstants.PREFIX_OT+"\n"+

					"SELECT  ?x ?title ?published ?lastModified ?created \n"+

					"WHERE \n"+
					"{ \n"+ 
					"?x  dc:title  ?title. \n"+
					"?x  ot:published \"yes\". \n"+
					"?x  fedora:lastModified  ?lastModified. \n"+
					"?x  fedora:created ?created. \n"+

					"} \n";


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
			// #TODO User error : Check if fuseki server up and running. 
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

					"SELECT  ?x ?title ?published ?lastModified ?created \n"+

					"WHERE \n"+
					"{ \n"+ 
					"?x  dc:title  ?title. \n"+
					"?x  ot:published ?published. \n"+
					"?x  fedora:lastModified  ?lastModified. \n"+
					"?x  fedora:created ?created. \n"+

					"} \n";


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
			// #TODO User error : Check if fuseki server up and running. 
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
			fedoraObject.setURI(binding.get("x").toString());
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
			String createdOn = binding.get("created").toString();
			String lastModified = binding.get("lastModified").toString();
			lastModified = lastModified.substring(0, 10);
			createdOn = createdOn.substring(0,10);
			try {
				Date dateLastModified = new SimpleDateFormat("yyyy-MM-dd", Locale.ENGLISH).parse(lastModified);
				fedoraObject.setLastModified(dateLastModified);
				
				Date datecreatedOn = new SimpleDateFormat("yyyy-MM-dd", Locale.ENGLISH).parse(createdOn);
				fedoraObject.setCreatedOn(datecreatedOn);
				
			} catch (ParseException e) {
				ObjectTellerException exception = new ObjectTellerException(e);
				exception.setErrormessage("Unable to parse created on date or last updated date");
				throw exception;
			}
			
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
			ObjectTellerException exception = new ObjectTellerException(e);
			exception.setErrormessage("Not able to connect to the Fuseki with url "+fusekiURL);
			throw exception;
		} catch (IOException e) {
			ObjectTellerException exception = new ObjectTellerException(e);
			exception.setErrormessage("Not able to connect to the Fuseki with url "+fusekiURL);
			throw exception;
		}
		
		return result;
	}
	
	/*public FedoraObject getObjectProperties(FedoraObject fedoraObject) throws ObjectTellerException {
		if(fusekiServerURL != null ) {
			if(testIfFusekiIsRunning()) {
			String queryString = FusekiConstants.PREFIX_OT+"\n"+

			 "SELECT  ?p ?o \n"+

			 "WHERE {  \n"+
			 "<"+fedoraObject.getURI()+">  ?p ?o.\n"+ 
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
		return null;
	} */
}
