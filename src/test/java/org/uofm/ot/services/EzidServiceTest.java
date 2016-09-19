package org.uofm.ot.services;

import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

import java.io.IOException;
import java.net.URISyntaxException;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.python.antlr.base.mod;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

 
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration({"file:src/main/webapp/WEB-INF/ObjectTellerServlet-servlet.xml" })
public class EzidServiceTest {

    @Autowired
	private EzidService ezidService;

    @Before
    public void setUp() throws Exception {
    }

    @After
    public void tearDown() throws Exception {

    }

   @Test
    public void canConnectToEzidService() throws IOException, URISyntaxException {

        String response = ezidService.get("ark:/99999/fk4n303p0d");

        assertTrue(response.contains("_target"));
    }

    @Test
    public void mintReturnsArkId() {

        String arkId = ezidService.mint();

        System.out.println("arkId: " + arkId);

        assertNotNull(arkId);

    }


    @Test
    public void createIDandBind() {
    	 // Create
    	 String arkId = ezidService.mint();
    	 
    	 
    	 //Modify
    	 ezidService.bind(arkId, "http://foo.com");
    	
    	 //GET
    	 String modified = ezidService.get(arkId);
    	 assertTrue(modified.contains("_target: http://foo.com"));
    	 
    }
}