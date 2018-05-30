package org.kgrid.library.ezidGateway;

import java.net.URI;
import java.util.List;

import org.kgrid.library.exception.LibraryException;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.http.client.support.BasicAuthorizationInterceptor;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import org.kgrid.library.knowledgeObject.ArkId;

/**
 * Created by pboisver on 9/15/16.
 */

@Service
public class EzidService {
	
	@Value(value = "${naan}")
	private String naan;
	
	@Value(value = "${ezid.base.url}")
	private String ezidBaseUrl;
	
	@Value(value = "${ezid.username}")
	private String ezidUsername;
	
	@Value(value = "${ezid.password}")
	private String ezidPassword;
	
	@Value(value = "${ezid.shoulder}")
	private String ezidShoulder;

	public boolean ping() throws LibraryException {

    RestTemplate rt = new RestTemplate();

    String url = ezidBaseUrl + "login";

    rt.getInterceptors().add(new BasicAuthorizationInterceptor(ezidUsername, ezidPassword));
    ResponseEntity<String> response = rt.getForEntity(url, String.class);

    if(response.getStatusCode().value() == HttpStatus.OK.value()) {
      return true;
    }
    throw new LibraryException("Cannot connect to ezid service " + response.getStatusCode().toString());
  }

  public String get(String id) {

      RestTemplate rt = new RestTemplate();

      String url = ezidBaseUrl +"id/" + id  ;

      ResponseEntity<String> response = rt.getForEntity(url, String.class);

      return response.getBody();

  }

  public String mint() {

      RestTemplate rt = new RestTemplate();

      rt.getInterceptors().add(new BasicAuthorizationInterceptor(ezidUsername, ezidPassword));

      HttpHeaders headers = new HttpHeaders();
      headers.setContentType(MediaType.TEXT_PLAIN);


      HttpEntity<String> requestEntity = new HttpEntity<>("_status: reserved", headers);

      ResponseEntity<String> response = rt.postForEntity(
              ezidBaseUrl +"shoulder/ark:/"+ naan +"/"+ ezidShoulder,
              requestEntity,
              String.class);

      String arkId = response.getBody() ;
      arkId = arkId.substring("success: ".length());
      return arkId;

  }

  public void create(String arkId) {
    RestTemplate rt = new RestTemplate();

    rt.getInterceptors().add(new BasicAuthorizationInterceptor(ezidUsername, ezidPassword));
    HttpHeaders headers = new HttpHeaders();
    headers.setContentType(MediaType.TEXT_PLAIN);

    HttpEntity<String> requestEntity = new HttpEntity<>("_status: reserved", headers);
    String ezidURL = ezidBaseUrl + "id/" + arkId;

    ResponseEntity response = rt.exchange(ezidURL, HttpMethod.PUT, requestEntity, String.class);
  }

  String modify(String id, List<String> metadata){
    RestTemplate rt = new RestTemplate();

    rt.getInterceptors().add(new BasicAuthorizationInterceptor(ezidUsername, ezidPassword));

    StringBuilder metadataRequestBody = new StringBuilder("") ;

    for (String string : metadata) {
      metadataRequestBody.append(string).append("\n");
    }

    HttpHeaders headers = new HttpHeaders();
    headers.setContentType(MediaType.TEXT_PLAIN);

    HttpEntity<String> requestEntity = new HttpEntity<>(metadataRequestBody.toString(), headers);

    String url = ezidBaseUrl +"id/"+id ;

    ResponseEntity<String> response = rt.postForEntity(url, requestEntity, String.class);

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
