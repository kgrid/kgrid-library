package org.uofm.ot.ui.util;

import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.apache.commons.lang3.StringUtils;
import org.apache.http.HttpHeaders;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpPatch;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.ContentType;
import org.apache.http.entity.InputStreamEntity;
import org.apache.http.entity.StringEntity;
import org.apache.http.entity.mime.FormBodyPart;
import org.apache.http.entity.mime.MultipartEntity;
import org.apache.http.entity.mime.content.ContentBody;
import org.apache.http.entity.mime.content.StringBody;
import org.apache.http.impl.client.DefaultHttpClient;
import org.uofm.ot.exception.ObjectTellerException;
import java.io.ByteArrayInputStream;

  

public class MainClient {

	
	public static void main(String[] args) {


		HttpPatch httpPatch = new HttpPatch("http://localhost:8087/rest/Test1234");

		boolean success = false;

		HttpClient client = new DefaultHttpClient() ;		
		HttpResponse response;
		String properties = "PREFIX ot: <http://uofm.org/objectteller/> \n"+
				"INSERT DATA \n"+
				"{ 	<http://localhost:8087/rest/Test1234> ot:subject  \"subject\" ; \n"+
				" \t ot:description  \" desc\" ; \n"+
				"\t  ot:contributors  \" cotributors \" ; \n"+
				" \t ot:keywords  \" keywords \" . \n"+"} ";

		System.out.println(properties);

		String binary = "some garbage -------START-------\n".concat("TEST &(*&)&)").concat("\n-------END-------");
		
		System.out.println(binary);
		
		String output = StringUtils.substringBetween(binary, "-------START-------\n", "\n-------END-------");
		System.out.println("output is  "+ output);
		
		/*try {
			MultipartEntity multiPartEntity = new MultipartEntity () ;
			ContentBody nameBody = new StringBody(properties);
			multiPartEntity.addPart(new FormBodyPart("my-text", nameBody)) ;

			httpPatch.setEntity(multiPartEntity) ;

			httpPatch.setHeader(HttpHeaders.CONTENT_TYPE, "application/sparql-update");
			
			 InputStream requestEntity = new ByteArrayInputStream(properties.getBytes(StandardCharsets.UTF_8));
			
			httpPatch.setEntity(new InputStreamEntity(requestEntity));
			
			StringEntity params = new StringEntity(properties, "application/sparql-update");
			httpPatch.setEntity(params);
			
			response = client.execute(httpPatch);	
			
			System.out.println(response.getStatusLine());
			
			success = true;
		} catch (IOException e) {
			ObjectTellerException exception = new ObjectTellerException(e);
			exception.setErrormessage("Exception occured while adding properties (triples) for object ");
			exception.printStackTrace();
		}*/
		
		
	}
}
