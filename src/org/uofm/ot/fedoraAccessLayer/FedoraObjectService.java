package org.uofm.ot.fedoraAccessLayer;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;

import org.apache.http.HttpResponse;
import org.apache.http.auth.UsernamePasswordCredentials;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpPatch;
import org.apache.http.client.methods.HttpPut;
import org.apache.http.entity.InputStreamEntity;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.auth.BasicScheme;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.log4j.Logger;
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

	

	public void putBinary(String binary, String objIdentifier, String type) throws ObjectTellerException {

		HttpPut httpPutRequestPayload = new HttpPut(baseURI + objIdentifier + "/" + type);
		httpPutRequestPayload.addHeader(BasicScheme.authenticate(
				new UsernamePasswordCredentials(userName, password),
				"UTF-8", false));

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


	protected void sendPatchRequestForUpdatingTriples(String data, String ObjectURI) throws ObjectTellerException {

		HttpPatch httpPatch = new HttpPatch(baseURI + ObjectURI );
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
		

	public void createContainer(String uri) throws ObjectTellerException {
		HttpClient httpClient = new DefaultHttpClient();

		HttpPut httpPutRequest = new HttpPut(baseURI+uri);
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


}
