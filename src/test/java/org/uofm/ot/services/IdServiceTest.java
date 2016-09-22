package org.uofm.ot.services;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.uofm.ot.knowledgeObject.FedoraObject;

import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration({"file:src/main/webapp/WEB-INF/ObjectTellerServlet-servlet.xml" })
public class IdServiceTest {

	private static final String ARKID = IdService.ARKID;

	private IdService idService;
	
	@Mock
	private EzidService ezidService; 
	
	public IdServiceTest() {
		MockitoAnnotations.initMocks(this);
	}
	
	@Before
	public void setUp() {
		idService = new IdService(ezidService);
	}

	@After
	public void tearDown() throws Exception {

	}

	
	@Test
	public void testMethodDelegation() throws Exception {
		idService.mint();
		verify(ezidService).mint();
	}

	@Test
	public void publishKnowledgeObject(){

		FedoraObject ko = new FedoraObject();
		ko.setURI(ARKID);
		idService.publish(ko);
		verify(ezidService).status(ARKID,IDStatus.PUBLIC);
	}
	
	@Test
	public void retractId(){
		FedoraObject ko = new FedoraObject();
		ko.setURI(ARKID);
		idService.retract(ko);
		verify(ezidService).status(ARKID,IDStatus.UNAVAILABLE);
	}

	@Test
	public void givenNewKoCanBindNewArkId() throws Exception {

		when(ezidService.mint()).thenReturn(ARKID);

		FedoraObject ko = new FedoraObject(new ArkID(ARKID));

		idService.bind(ko, "http://dev.umich.edu/"+ARKID);

		assertEquals(ARKID, ko.getURI());

		verify(ezidService).bind(ARKID, "http://dev.umich.edu/"+ARKID );

		}
}