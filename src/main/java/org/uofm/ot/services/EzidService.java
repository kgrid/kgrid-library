package org.uofm.ot.services;

import java.io.IOException;
import java.net.URISyntaxException;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.http.client.support.BasicAuthorizationInterceptor;
import org.springframework.stereotype.Service;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.web.client.RestTemplate;

/**
 * Created by pboisver on 9/15/16.
 */

@Service
public class EzidService {
	
	@Value(value = "${NAAN}")
	private String NAAN;

    public String get(String id) {

        RestTemplate rt = new RestTemplate();
        
        String url = "https://ezid.cdlib.org/id/" + id  ; 

        ResponseEntity<String> response = rt.getForEntity(url, String.class);

        return response.getBody();

    }

    public String mint() {

        RestTemplate rt = new RestTemplate();

        rt.getInterceptors().add(new BasicAuthorizationInterceptor("apitest", "apitest"));

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.TEXT_PLAIN);

        HttpEntity<String> requestEntity = new HttpEntity<>("_status: reserved", headers);

        ResponseEntity<String> response = rt.postForEntity(
                "https://ezid.cdlib.org/shoulder/ark:/"+NAAN+"/fk4",
                requestEntity,
                String.class);

        String arkId = response.getBody() ; 
        arkId = arkId.substring("success: ".length());
        return arkId;

    }
    
    private String modify(String id, String metadata){
        RestTemplate rt = new RestTemplate();

        rt.getInterceptors().add(new BasicAuthorizationInterceptor("apitest", "apitest"));

        HttpHeaders headers = new HttpHeaders();
       headers.setContentType(MediaType.TEXT_PLAIN);
        
        HttpEntity<String> requestEntity = new HttpEntity<>(metadata, headers);

        String url = "https://ezid.cdlib.org/id/"+id ; 
        
        ResponseEntity<String> response = rt.postForEntity(
                url,
                requestEntity,
                String.class);

        return response.getBody();

    }
    
    public String bind(String id, String objectURL){
    	String metadata = "_target: "+objectURL ; 
    	return  modify(id, metadata);
    }
    
    public String status(String id, String status){
    	String metadata = "_status: "+status ; 
    	return  modify(id, metadata);
    }
  
}
