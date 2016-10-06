package org.uofm.ot.services;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.uofm.ot.knowledgeObject.ArkId;
import org.uofm.ot.knowledgeObject.FedoraObject;

import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration({"file:src/main/webapp/WEB-INF/ObjectTellerServlet-servlet.xml" })
public class IdServiceTest {

	private static final String ARKID_STRING = ArkId.FAKE_ARKID().toString();

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
		when(ezidService.mint()).thenReturn(ARKID_STRING);
		idService.mint();
		verify(ezidService).mint();
	}

	@Test
	public void publishKnowledgeObject(){

		FedoraObject ko = new FedoraObject();
		ko.setArkId(ArkId.FAKE_ARKID());
		idService.publish(ko);
		verify(ezidService).status(ARKID_STRING, ArkId.Status.PUBLIC);
	}
	
	@Test
	public void retractId(){
		FedoraObject ko = new FedoraObject();
		ko.setArkId(ArkId.FAKE_ARKID());
		idService.retract(ko);
		verify(ezidService).status(ARKID_STRING, ArkId.Status.UNAVAILABLE);
	}

	@Test
	public void givenNewKoCanBindNewArkId() throws Exception {

		when(ezidService.mint()).thenReturn(ARKID_STRING);

		FedoraObject ko = new FedoraObject(new ArkId(ARKID_STRING));

		idService.bind(ko, "http://dev.umich.edu/"+ ARKID_STRING);

		assertEquals(ARKID_STRING, ko.getURI());

		verify(ezidService).bind(ARKID_STRING, "http://dev.umich.edu/"+ ARKID_STRING);

		}
}