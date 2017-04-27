package org.uofm.ot.services;

import java.net.URI;
import java.util.List;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.http.client.support.BasicAuthorizationInterceptor;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import org.uofm.ot.knowledgeObject.ArkId;

/**
 * Created by pboisver on 9/15/16.
 */

@Service
public class EzidService {
	
	@Value(value = "${NAAN}")
	private String NAAN;
	
	@Value(value = "${EZID_BASE_URL}")
	private String EZID_BASE_URL;
	
	@Value(value = "${EZID_USERNAME}")
	private String EZID_USERNAME;
	
	@Value(value = "${EZID_PASSWORD}")
	private String EZID_PASSWORD;
	
	@Value(value = "${EZID_SHOULDER}")
	private String EZID_SHOULDER;

    public String get(String id) {

        RestTemplate rt = new RestTemplate();
        
        String url = EZID_BASE_URL+"id/" + id  ; 

        ResponseEntity<String> response = rt.getForEntity(url, String.class);

        return response.getBody();

    }

    public String mint() {

        RestTemplate rt = new RestTemplate();

        rt.getInterceptors().add(new BasicAuthorizationInterceptor(EZID_USERNAME, EZID_PASSWORD));

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.TEXT_PLAIN);
        
        HttpEntity<String> requestEntity = new HttpEntity<>("_status: reserved", headers);

        ResponseEntity<String> response = rt.postForEntity(
                EZID_BASE_URL+"shoulder/ark:/"+NAAN+"/"+EZID_SHOULDER,
                requestEntity,
                String.class);

        String arkId = response.getBody() ; 
        arkId = arkId.substring("success: ".length());
        return arkId;

    }
    
    private String modify(String id, List<String> metadata){
        RestTemplate rt = new RestTemplate();

        rt.getInterceptors().add(new BasicAuthorizationInterceptor(EZID_USERNAME, EZID_PASSWORD));

        String metadataRequestBody = "" ;
        
        for (String string : metadata) {
			metadataRequestBody += string + "\n";
		}
        
        HttpHeaders headers = new HttpHeaders();
       headers.setContentType(MediaType.TEXT_PLAIN);
        
        HttpEntity<String> requestEntity = new HttpEntity<>(metadataRequestBody, headers);

        String url = EZID_BASE_URL+"id/"+id ; 
        
        ResponseEntity<String> response = rt.postForEntity(
                url,
                requestEntity,
                String.class);

        return response.getBody();

    }
    
    public String bind(String id, List<String> metadata, URI objectURL){
    	String target = "_target: " + objectURL ;
    	metadata.add(target);
    	return  modify(id, metadata);
    }
    
    public String status(String id, List<String> metadata, ArkId.Status status){
    	String statusAttribute = "_status: "+status.toString() ;
    	metadata.add(statusAttribute);
    	return  modify(id, metadata);
    }
  
}
