package org.uofm.ot.fedoraAccessLayer;

import java.io.BufferedInputStream;
import java.io.IOException;
import java.io.InputStream;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.auth.UsernamePasswordCredentials;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.auth.BasicScheme;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.log4j.Logger;
import org.uofm.ot.exception.ObjectTellerException;


public class GetFedoraObjectService extends FedoraObjectService {
	
	private static final Logger logger = Logger.getLogger(GetFedoraObjectService.class);
	
	
	
	public String getObjectContent(String objectId, String datastremId) throws ObjectTellerException  {

		HttpClient httpClient = new DefaultHttpClient();

		HttpGet httpGetRequest = new HttpGet(baseURI+objectId+"/"+datastremId+"/");
		httpGetRequest.addHeader(BasicScheme.authenticate(
				new UsernamePasswordCredentials(userName, password),
				"UTF-8", false));

		String chunk = null;
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
						if(chunk == null)
							chunk = new String(buffer, 0, bytesRead);
						else
							chunk = chunk + new String(buffer, 0, bytesRead);

					}

				}
			} else {
				ObjectTellerException exception = new ObjectTellerException();
				logger.error("Exception occured while retrieving object content for object "+objectId+"/"+datastremId+". Request status code is "+httpResponse.getStatusLine().getStatusCode());
				exception.setErrormessage("Exception occured while retrieving object content for object "+objectId+"/"+datastremId+". Request status code is "+httpResponse.getStatusLine().getStatusCode());
				throw exception;
			} 

		} catch (IOException e) {
			ObjectTellerException exception = new ObjectTellerException(e);
			logger.error("Exception occured while retrieving object content for object "+objectId+"/"+datastremId + e.getMessage());
			exception.setErrormessage("Exception occured while retrieving object content for object "+objectId+"/"+datastremId + e.getMessage());
			throw exception;
		}
		return chunk;
	}
	
}
