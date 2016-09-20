package org.uofm.ot.services;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotEquals;
import static org.junit.Assert.assertNotNull;

import javax.validation.constraints.AssertTrue;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import static org.mockito.Mockito.*;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration({"file:src/main/webapp/WEB-INF/ObjectTellerServlet-servlet.xml" })
public class IdServiceTest {
	
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
		idService.publish("ark:/99999/12345");
		verify(ezidService).status("ark:/99999/12345",IDStatus.PUBLIC);
	}
	
	@Test
	public void retractId(){
		idService.retract("ark:/99999/12345");
		verify(ezidService).status("ark:/99999/12345",IDStatus.UNAVAILABLE);
	}
}