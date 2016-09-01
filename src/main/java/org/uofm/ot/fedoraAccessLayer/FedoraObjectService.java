package org.uofm.ot.fedoraAccessLayer;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;

import org.apache.http.HttpResponse;
import org.apache.http.auth.UsernamePasswordCredentials;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpDelete;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPatch;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.methods.HttpPut;
import org.apache.http.entity.InputStreamEntity;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.auth.BasicScheme;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.log4j.Logger;
import org.springframework.http.HttpStatus;
import org.uofm.ot.dao.SystemConfigurationDAO;
import org.uofm.ot.exception.ObjectTellerException;
import org.uofm.ot.model.Server_details;


public class FedoraObjectService {

	private SystemConfigurationDAO sysConfDao;

	protected String baseURI;

	protected String userName;

	protected String password;

	private static final Logger logger = Logger.getLogger(FedoraObjectService.class);

	public void setSysConfDao(SystemConfigurationDAO sysConfDao) {
		this.sysConfDao = sysConfDao;
		configureBaseURI();
	}

	

	public void putBinary(String binary, String objIdentifier, String type, String transactionID) throws ObjectTellerException {

		HttpPut httpPutRequestPayload ;
		if(transactionID == null)
			httpPutRequestPayload = new HttpPut(baseURI + objIdentifier + "/" + type);
		else
			httpPutRequestPayload = new HttpPut(baseURI + transactionID + "/"+ objIdentifier + "/" + type);
			
		httpPutRequestPayload.addHeader(BasicScheme.authenticate(
				new UsernamePasswordCredentials(userName, password),
				"UTF-8", false));

		if(binary == null)
			binary = "";
		
		HttpClient httpClient = new DefaultHttpClient();

		HttpResponse response;

		
		try {
			StringEntity entity = new StringEntity(binary);
			httpPutRequestPayload.setEntity(entity );
			response = httpClient.execute(httpPutRequestPayload) ;
			if (response != null) {
				if(response.getStatusLine().getStatusCode() == 201 || response.getStatusLine().getStatusCode() == 204) {
					logger.info("Binary added successfully in the Object "+baseURI + objIdentifier + "/" + type);
				} else {
					ObjectTellerException exception = new ObjectTellerException();
					logger.error("Exception occured while creating binary of type"+type+" for object "+objIdentifier+". HTTPResponse is "+response);
					exception.setErrormessage("Exception occured while creating binary of type"+type+" for object "+objIdentifier+". HTTPResponse is "+response);
					throw exception;
				}
			}

		} catch (ClientProtocolException e) {
			ObjectTellerException exception = new ObjectTellerException(e);
			logger.error("Exception occured while creating binary of type"+type+" for object "+objIdentifier+"."+e.getMessage());
			exception.setErrormessage("Exception occured while creating binary of type"+type+" for object "+objIdentifier+"."+e.getMessage());
			throw exception;
		} catch (IOException e) {
			ObjectTellerException exception = new ObjectTellerException(e);
			logger.error("Exception occured while creating binary of type"+type+" for object "+objIdentifier+"."+e.getMessage());
			exception.setErrormessage("Exception occured while creating binary of type"+type+" for object "+objIdentifier+"."+e.getMessage());
			throw exception;
		}
	}

	private void configureBaseURI(){
		Server_details configuration =sysConfDao.getFedoraServerConfiguration();
		if(configuration != null){
			baseURI = configuration.getComplete_url() ;
			userName = configuration.getSvr_username();
			password= configuration.getSvr_passwd();

		} else{
			baseURI = null;
		}
	}


	protected void sendPatchRequestForUpdatingTriples(String data, String ObjectURI, String transactionId) throws ObjectTellerException {

		HttpPatch httpPatch ;
		if(transactionId == null)
			httpPatch = new HttpPatch(baseURI + ObjectURI );
		else
			httpPatch = new HttpPatch(baseURI + transactionId+"/"+ObjectURI );
		
		httpPatch.addHeader(BasicScheme.authenticate(
				new UsernamePasswordCredentials(userName, password),
				"UTF-8", false));


		HttpClient client = new DefaultHttpClient() ;		
		HttpResponse response;

		try {
			InputStream requestEntity = new ByteArrayInputStream(data.getBytes(StandardCharsets.UTF_8));

			httpPatch.setEntity(new InputStreamEntity(requestEntity));
			response = client.execute(httpPatch);	
			if(response != null) {
				if(response.getStatusLine().getStatusCode() == 204) {
					logger.info("Successfully added triples for object "+baseURI + ObjectURI + "and query "+ data);
				} else {
					ObjectTellerException exception = new ObjectTellerException();
					logger.error("Exception occured while adding properties (triples) for object "+ baseURI + ObjectURI + "and query "+ data);
					exception.setErrormessage("Exception occured while adding properties (triples) for object "+ baseURI + ObjectURI + "and query "+ data);
					throw exception;
				}
			}
		} catch (IOException e) {
			ObjectTellerException exception = new ObjectTellerException(e);
			logger.error("Exception occured while adding properties (triples) for object "+ baseURI + ObjectURI + "and query "+ data);
			exception.setErrormessage("Exception occured while adding properties (triples) for object "+ baseURI + ObjectURI + "and query "+ data);
			throw exception;
		}

	}
		

	public void createContainer(String uri, String transactionID) throws ObjectTellerException {
		HttpClient httpClient = new DefaultHttpClient();

		HttpPut httpPutRequest = null;
		if(transactionID == null) 
			httpPutRequest = new HttpPut(baseURI+uri);
		else 
			httpPutRequest = new HttpPut(baseURI+transactionID+"/"+uri);
		
		httpPutRequest.addHeader(BasicScheme.authenticate(
				new UsernamePasswordCredentials(userName, password),
				"UTF-8", false));
		try {
			HttpResponse httpResponse = httpClient.execute(httpPutRequest);
			if(httpResponse != null && httpResponse.getStatusLine().getStatusCode() == 201) {
				logger.info("Successfully added new container "+ baseURI+uri);
			} else {
				logger.error("Error occured while creating object "+baseURI+uri +"HttpResonse is "+httpResponse);
				throw new ObjectTellerException("Error occured while creating object "+baseURI+uri +"HttpResonse is "+httpResponse);
			}
		} catch (IOException e) {
			logger.error("Exception occured while creating the container "+uri+"."+e.getMessage()+" caused by "+e.getCause());
			ObjectTellerException exception = new ObjectTellerException(e);
			exception.setErrormessage("Exception occured while creating the container "+uri+"."+e.getMessage()+" caused by "+e.getCause());
			throw exception;
		}
	}

	public String createTransaction() throws ObjectTellerException {
		String transactionId = null;
		String transactionURL = baseURI+"fcr:tx";
		
		HttpPost httpPost = new HttpPost(transactionURL);
		httpPost.addHeader(BasicScheme.authenticate(
				new UsernamePasswordCredentials(userName, password),
				"UTF-8", false));

		HttpClient httpClient = new DefaultHttpClient();

		HttpResponse response;
		
		try {
			response = httpClient.execute(httpPost) ;
			if(response.getStatusLine().getStatusCode() == HttpStatus.CREATED.value()) {
				String location = response.getFirstHeader("Location").getValue();
				if(location!= null ){
					
					transactionId = location.substring(baseURI.length());
					logger.info("Transaction created with id "+transactionId);
				} else {
					ObjectTellerException exception = new ObjectTellerException("Transaction ID Not found. ");
					logger.error("Transaction ID Not found. ");
					throw exception;
				}
			} else {
				ObjectTellerException exception = new ObjectTellerException("Unable to create transaction .");
				logger.error("Unable to create transaction .");
				throw exception;
			}
			
		} catch (ClientProtocolException e) {
			ObjectTellerException exception = new ObjectTellerException(e);
			logger.error("Exception occured while creating transaction "+e.getMessage());
			exception.setErrormessage("Exception occured while creating transaction "+e.getMessage());
			throw exception;
		} catch (IOException e) {
			ObjectTellerException exception = new ObjectTellerException(e);
			logger.error("Exception occured while creating transaction "+e.getMessage());
			exception.setErrormessage("Exception occured while creating transaction "+e.getMessage());
			throw exception;
		}
		
		return transactionId;
	}

	public void commitTransaction(String transactionId) throws ObjectTellerException {
		String transactionCommitURL = baseURI + transactionId + "/fcr:tx/fcr:commit";
		HttpPost httpPost = new HttpPost(transactionCommitURL);
		httpPost.addHeader(BasicScheme.authenticate(
				new UsernamePasswordCredentials(userName, password),
				"UTF-8", false));

		HttpClient httpClient = new DefaultHttpClient();

		HttpResponse response;
		
		try {
			response = httpClient.execute(httpPost) ;
			if(response.getStatusLine().getStatusCode() == HttpStatus.NO_CONTENT.value()) {
				
			} else {
				ObjectTellerException exception = new ObjectTellerException("Unable to commit transaction with Id "+transactionId);
				logger.error("Unable to commit transaction with Id "+transactionId);
				throw exception;
			}
			
		} catch (ClientProtocolException e) {
			ObjectTellerException exception = new ObjectTellerException(e);
			logger.error("Exception occured while committing the transaction with id "+transactionId+"."+e.getMessage());
			exception.setErrormessage("Exception occured while committing the transaction with id "+transactionId+"."+e.getMessage());
			throw exception;
		} catch (IOException e) {
			ObjectTellerException exception = new ObjectTellerException(e);
			logger.error("Exception occured while committing the transaction with id "+transactionId+"."+e.getMessage());
			exception.setErrormessage("Exception occured while committing the transaction with id "+transactionId+"."+e.getMessage());
			throw exception;
		}
	}

	public void rollbackTransaction(String transactionId) throws ObjectTellerException {
		String transactionRollBackURL = baseURI + transactionId + "/fcr:tx/fcr:rollback";
		HttpPost httpPost = new HttpPost(transactionRollBackURL);
		httpPost.addHeader(BasicScheme.authenticate(
				new UsernamePasswordCredentials(userName, password),
				"UTF-8", false));

		HttpClient httpClient = new DefaultHttpClient();

		HttpResponse response;
		
		try {
			response = httpClient.execute(httpPost) ;
			if(response.getStatusLine().getStatusCode() == HttpStatus.NO_CONTENT.value()) {
				
			} else {
				ObjectTellerException exception = new ObjectTellerException("Unable to roll back transaction with Id "+transactionId);
				logger.error("Unable to roll back transaction with Id "+transactionId);
				throw exception;
			}
			
		} catch (ClientProtocolException e) {
			ObjectTellerException exception = new ObjectTellerException(e);
			logger.error("Exception occured while rolling back the transaction with id "+transactionId+"."+e.getMessage());
			exception.setErrormessage("Exception occured while rolling back the transaction with id "+transactionId+"."+e.getMessage());
			throw exception;
		} catch (IOException e) {
			ObjectTellerException exception = new ObjectTellerException(e);
			logger.error("Exception occured while rolling back the transaction with id "+transactionId+"."+e.getMessage());
			exception.setErrormessage("Exception occured while rolling back the transaction with id "+transactionId+"."+e.getMessage());
			throw exception;
		}
	}
	
	public String createContainerWithAutoGeneratedName(String parent, String transactionID) throws ObjectTellerException {
		String location = null ;
		HttpPost httpPostRequest = null;
		
		if(transactionID == null) 
			httpPostRequest = new HttpPost (baseURI + parent + "/");
		else 
			httpPostRequest = new HttpPost (baseURI+transactionID+"/"+parent + "/");
		
		
		httpPostRequest.addHeader(BasicScheme.authenticate(
				new UsernamePasswordCredentials(userName, password),
				"UTF-8", false));

		HttpClient httpClient = new DefaultHttpClient();

		HttpResponse response;
		
		try {
			response = httpClient.execute(httpPostRequest) ;
			if(response.getStatusLine().getStatusCode() == HttpStatus.CREATED.value()) {
				location = response.getFirstHeader("Location").getValue();
				if(transactionID == null)
					location = location.substring((baseURI + parent + "/").length());
				else
					location = location.substring((baseURI+transactionID+"/"+parent + "/").length());
			} else {
				ObjectTellerException exception = new ObjectTellerException("Unable to create child resourse for parent "+parent);
				logger.error("Unable to create child resourse for parent "+parent);
				throw exception;
			}
			
		} catch (ClientProtocolException e) {
			ObjectTellerException exception = new ObjectTellerException(e);
			logger.error("Exception occured while creating child resource for parent "+parent+"."+e.getMessage());
			exception.setErrormessage("Exception occured while creating child resource for parent "+parent+"."+e.getMessage());
			throw exception;
		} catch (IOException e) {
			ObjectTellerException exception = new ObjectTellerException(e);
			logger.error("Exception occured while creating child resource for parent "+parent+"."+e.getMessage());
			exception.setErrormessage("Exception occured while creating child resource for parent "+parent+"."+e.getMessage());
			throw exception;
		}
		 return location;
		
	}
	
	public void deleteFedoraResource(String uri) throws ObjectTellerException {
		String deleteResourceURI = baseURI +  uri ;
		HttpDelete httpDelete = new HttpDelete(deleteResourceURI);
		httpDelete.addHeader(BasicScheme.authenticate(
				new UsernamePasswordCredentials(userName, password),
				"UTF-8", false));

		HttpClient httpClient = new DefaultHttpClient();

		HttpResponse response;
		
		try {
			response = httpClient.execute(httpDelete) ;
			if(response.getStatusLine().getStatusCode() == HttpStatus.GONE.value() || response.getStatusLine().getStatusCode()  == HttpStatus.NO_CONTENT.value()) {
				
			} else {
				ObjectTellerException exception = new ObjectTellerException("Unable to delete fedora resource "+deleteResourceURI);
				logger.error("Unable to delete fedora resource "+deleteResourceURI);
				throw exception;
			}
			
		} catch (ClientProtocolException e) {
			ObjectTellerException exception = new ObjectTellerException(e);
			logger.error("Exception occured while deleting fedora resource with URI "+deleteResourceURI+"."+e.getMessage());
			exception.setErrormessage("Exception occured while deleting fedora resource with URI "+deleteResourceURI+"."+e.getMessage());
			throw exception;
		} catch (IOException e) {
			ObjectTellerException exception = new ObjectTellerException(e);
			logger.error("Exception occured while deleting fedora resource with URI "+deleteResourceURI+"."+e.getMessage());
			exception.setErrormessage("Exception occured while deleting fedora resource with URI "+deleteResourceURI+"."+e.getMessage());
			throw exception;
		}
	}
	
	public boolean checkIfObjectExists(String objectId) throws ObjectTellerException{

		boolean result = false;

		HttpClient httpClient = new DefaultHttpClient();

		HttpGet httpGetRequest = new HttpGet(baseURI+objectId+"/");
		httpGetRequest.addHeader(BasicScheme.authenticate(
				new UsernamePasswordCredentials(userName, password),
				"UTF-8", false));

		HttpResponse httpResponse;
		try {
			httpResponse = httpClient.execute(httpGetRequest);
			if ( 200 == httpResponse.getStatusLine().getStatusCode())
				result = true;
		} catch (ClientProtocolException e) {
			ObjectTellerException exception = new ObjectTellerException(e);
			logger.error("Exception occured while verifying object id "+objectId +"."+ e.getMessage());
			exception.setErrormessage("Exception occured while verifying object id "+objectId +"."+ e.getMessage());
			throw exception;
		} catch (IOException e) {
			ObjectTellerException exception = new ObjectTellerException(e);
			logger.error("Exception occured while verifying object id "+objectId+"."+e.getMessage());
			exception.setErrormessage("Exception occured while verifying object id "+objectId+"."+e.getMessage());
			throw exception;
		}
		return result;
	}
}
