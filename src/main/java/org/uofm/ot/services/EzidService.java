package org.uofm.ot.services;

import org.springframework.http.ResponseEntity;
import org.springframework.web.client.RestTemplate;

/**
 * Created by pboisver on 9/15/16.
 */
public class EzidService {

    public String get(String id) {

        RestTemplate rt = new RestTemplate();

        ResponseEntity<String> response = rt.getForEntity("https://ezid.cdlib.org/id/ark:/99999/fk45x2kt6t/", String.class);

        return response.getBody();

    }
}
