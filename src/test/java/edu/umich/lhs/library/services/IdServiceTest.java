package edu.umich.lhs.library.services;

import edu.umich.lhs.library.ezidGateway.DummyIdService;
import edu.umich.lhs.library.ezidGateway.EzidService;
import edu.umich.lhs.library.knowledgeObject.ArkId;
import edu.umich.lhs.library.knowledgeObject.KnowledgeObject;
import java.net.URI;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.ArrayList;

public class IdServiceTest {

	private static final String ARKID_STRING = ArkId.FAKE_ARKID().toString();

	private IdService idService;
	
	@Mock
	private EzidService ezidService;

	@Mock
	private DummyIdService dummyIdService;
	
	public IdServiceTest() {
		MockitoAnnotations.initMocks(this);
	}
	
	@Before
	public void setUp() {
		idService = new IdService(ezidService, dummyIdService);
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

		ArrayList<String> metadata = new ArrayList<String>();
		idService.publish(ArkId.FAKE_ARKID(), metadata);
		
		verify(ezidService).status(ARKID_STRING, metadata, ArkId.Status.PUBLIC);
	}
	
	@Test
	public void retractId(){
		ArrayList<String> metadata = new ArrayList<String>(); 
		idService.retract(ArkId.FAKE_ARKID(), metadata);
		
		verify(ezidService).status(ARKID_STRING, metadata, ArkId.Status.UNAVAILABLE);
	}

	@Test
	public void givenNewKoCanBindNewArkId() throws Exception {

		when(ezidService.mint()).thenReturn(ARKID_STRING);

		KnowledgeObject ko = new KnowledgeObject(new ArkId(ARKID_STRING));
		
		ArrayList<String> metadata = new ArrayList<String>();

		idService.bind(ko.getArkId(), metadata, new URI("http://dev.umich.edu/"+ ARKID_STRING));

		assertEquals(ARKID_STRING, ko.getURI());

		verify(ezidService).bind(ARKID_STRING, metadata, new URI("http://dev.umich.edu/"+ ARKID_STRING));

	}

	@Test
	public void testPing() throws Exception {
		when(ezidService.ping()).thenReturn(true);
		assertEquals(true, idService.ping());
	}
}