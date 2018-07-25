package org.kgrid.library.services;

import org.kgrid.library.ezidGateway.DummyIdService;
import org.kgrid.library.ezidGateway.EzidService;
import java.net.URI;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.kgrid.shelf.domain.ArkId;
import org.kgrid.shelf.domain.KnowledgeObject;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.ArrayList;

public class IdServiceTest {

	private static final String ARKID_STRING = "ark:/99999/fake";

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

	// TODO: reimplement ark id statuses
	@Test
	public void publishKnowledgeObject(){

		ArrayList<String> metadata = new ArrayList<String>();
		idService.publish(new ArkId("ark:/test/ark"), metadata);
		
//		verify(ezidService).status(ARKID_STRING, metadata, ArkId.Status.PUBLIC);
	}
	
	@Test
	public void retractId(){
		ArrayList<String> metadata = new ArrayList<String>(); 
		idService.retract(new ArkId(ARKID_STRING), metadata);
		
//		verify(ezidService).status(ARKID_STRING, metadata, ArkId.Status.UNAVAILABLE);
	}

	// TODO: Fix tests by setting ko to a real knowledge object
	@Test
	public void givenNewKoCanBindNewArkId() throws Exception {

		when(ezidService.mint()).thenReturn(ARKID_STRING);

		KnowledgeObject ko = null;
		
		ArrayList<String> metadata = new ArrayList<String>();

//		idService.bind(ko.getArkId(), metadata, new URI("http://dev.umich.edu/"+ ARKID_STRING));

//		assertEquals(ARKID_STRING, ko.getArkId().toString());

//		verify(ezidService).bind(ARKID_STRING, metadata, new URI("http://dev.umich.edu/"+ ARKID_STRING));

	}

	@Test
	public void testPing() throws Exception {
		when(ezidService.ping()).thenReturn(true);
		assertEquals(true, idService.ping());
	}
}