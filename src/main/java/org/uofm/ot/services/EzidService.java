package org.uofm.ot.services;

import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.http.client.support.BasicAuthorizationInterceptor;
import org.springframework.web.client.RestTemplate;

import java.io.IOException;
import java.net.URISyntaxException;

/**
 * Created by pboisver on 9/15/16.
 */
public class EzidService {

    public String get(String id) throws URISyntaxException, IOException {

        RestTemplate rt = new RestTemplate();

        rt.getInterceptors().add(new BasicAuthorizationInterceptor("apitest", "apitest"));

//        ClientHttpRequest request = rt.getRequestFactory().createRequest(new URI("https://ezid.cdlib.org/id/ark:/99999/fk45x2kt6t/"), HttpMethod.GET);

        ResponseEntity<String> response = rt.getForEntity("https://ezid.cdlib.org/id/ark:/99999/fk45x2kt6t/", String.class);

        return response.getBody();

    }

    public String mint() {

        RestTemplate rt = new RestTemplate();

        rt.getInterceptors().add(new BasicAuthorizationInterceptor("apitest", "apitest"));

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.TEXT_PLAIN);

        HttpEntity<String> requestEntity = new HttpEntity<>("_status: reserved", headers);

        ResponseEntity<String> response = rt.postForEntity(
                "https://ezid.cdlib.org/shoulder/ark:/99999/fk4",
                requestEntity,
                String.class);

        return response.getBody();

    }
}
