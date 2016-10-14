package org.uofm.ot.services;

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
    
    private String modify(String id, String metadata){
        RestTemplate rt = new RestTemplate();

        rt.getInterceptors().add(new BasicAuthorizationInterceptor(EZID_USERNAME, EZID_PASSWORD));

        HttpHeaders headers = new HttpHeaders();
       headers.setContentType(MediaType.TEXT_PLAIN);
        
        HttpEntity<String> requestEntity = new HttpEntity<>(metadata, headers);

        String url = EZID_BASE_URL+"id/"+id ; 
        
        ResponseEntity<String> response = rt.postForEntity(
                url,
                requestEntity,
                String.class);

        return response.getBody();

    }
    
    public String bind(String id, String objectURL){
    	String metadata = "_target: " + objectURL ;
    	return  modify(id, metadata);
    }
    
    public String status(String id, ArkId.Status status){
    	String metadata = "_status: "+status.toString() ; 
    	return  modify(id, metadata);
    }
  
}
