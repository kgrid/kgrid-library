package org.uofm.ot.services;

import org.junit.After;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.web.client.HttpClientErrorException;
import org.uofm.ot.knowledgeObject.ArkId;

import java.io.IOException;
import java.net.URISyntaxException;

import static org.junit.Assert.*;

 
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

    	String arkId = ezidService.mint();
    	
        String response = ezidService.get(arkId);

        assertTrue(response.contains("_target"));
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
    
	@Test
	public void canMintTwoNewArkIdsThatAreDifferent() {
		
		String arkId = ezidService.mint();

		String arkId2 = ezidService.mint();
		
		assertNotNull(arkId);

		assertNotEquals("Should be different", arkId, arkId2);

	}

	@Test
	public void arkIdsStartWithStringArkColonAndHave3Parts() {
		
		String arkId = ezidService.mint();
		
		String[] idParts = arkId.split("/");

		assertEquals(3, idParts.length);

		assertEquals("Should be 'ark:'", "ark:", idParts[0]);
	}

	 @Test
	 public void givenReservedStatusCanChangeToPublic() {
		 // Create
		 String arkId = ezidService.mint(); 	 

		 //Modify
		 ezidService.status(arkId, ArkId.Status.PUBLIC);

		 //GET
		 String modified = ezidService.get(arkId);
		 assertTrue(modified.contains("_status: public"));

	 }
	 
	 @Test
	 public void changePublicStatusToReservedThrowsException() {
		 // Create
		 String arkId = ezidService.mint(); 	 

		 //Modify
		 ezidService.status(arkId, ArkId.Status.PUBLIC);
		 
		 try {
			 ezidService.status(arkId, ArkId.Status.RESERVED);
			 assertTrue(false);
		 } catch(HttpClientErrorException e){
			//GET
			 assertTrue(e.getMessage().contains("400 BAD REQUEST"));
			 assertTrue(e.getResponseBodyAsString().contains("invalid identifier status change"));
			 String modified = ezidService.get(arkId);
			 assertTrue(modified.contains("_status: public"));
			 
		 }		 

	 }
}