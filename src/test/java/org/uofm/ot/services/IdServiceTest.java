package org.uofm.ot.services;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.uofm.ot.knowledgeObject.FedoraObject;

import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration({"file:src/main/webapp/WEB-INF/ObjectTellerServlet-servlet.xml" })
public class IdServiceTest {

	@Autowired

	public static final String ARKID = "ark:/99999/12345";
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
	public void makeIdPublic(){
		idService.publish(ARKID);
		verify(ezidService).status(ARKID,IDStatus.PUBLIC);
	}
	
	@Test
	public void retractId(){
		idService.retract(ARKID);
		verify(ezidService).status(ARKID,IDStatus.UNAVAILABLE);
	}

	@Test
	public void givenNewKoCanBindNewArkId() throws Exception {

		when(ezidService.mint()).thenReturn(ARKID);

		FedoraObject ko = new FedoraObject();

		ko.setLibraryURL("http://kgrid.umich.edu/ko/");

		idService.bind(ko);

		assertEquals(ARKID, ko.getURI());

		verify(ezidService).bind(ARKID, ko.getURL());

		}
}