package org.uofm.ot;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.authentication.SavedRequestAwareAuthenticationSuccessHandler;
import org.springframework.stereotype.Component;
import org.uofm.ot.model.OTUser;

import com.fasterxml.jackson.databind.ObjectMapper;



@Component
public class AuthenticationSuccessHandler extends SavedRequestAwareAuthenticationSuccessHandler {
	
	@Autowired
    private MappingJackson2HttpMessageConverter converter;
	
	@Override
    public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response, Authentication authentication) throws ServletException, IOException {
        // Instead of handle(request, response, authentication),
        // the statements below are introduced
		
        response.setStatus(HttpServletResponse.SC_OK);
        response.setContentType(MediaType.APPLICATION_JSON_VALUE);

        OTUser principal = (OTUser) authentication.getPrincipal();
        
        writeUserToResponse(response,principal);
        // as done in the base class
        clearAuthenticationAttributes(request);

    }
	
	private void writeUserToResponse(HttpServletResponse response, OTUser principal) throws IOException {
	     ObjectMapper mapper = converter.getObjectMapper();

	     ServletOutputStream os = response.getOutputStream();

	     mapper.writeValue(os, principal);
	    }
}

