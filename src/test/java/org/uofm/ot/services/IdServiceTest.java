package org.uofm.ot.services;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotEquals;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.PropertySource;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration({"file:src/main/webapp/WEB-INF/ObjectTellerServlet-servlet.xml" })
@PropertySource("classpath:application.properties")
public class IdServiceTest {
	
	@Autowired
	private IdService idService;
	
	@Before
	public void setUp() {
		
	}

	@After
	public void tearDown() throws Exception {

	}

	
	@Test
	public void canMintTwoNewArkIdsThatAreDifferent() {
		
	
		
		String arkId = idService.mint();

		String arkId2 = idService.mint();

		assertNotEquals("Should be different", arkId, arkId2);

	}

	@Test
	public void arkIdsStartWithStringArkColonAndHave3Parts() {

		String arkId = idService.mint();


		String[] idParts = arkId.split("/");

		assertEquals(3, idParts.length);

		assertEquals("Should be 'ark:'", "ark:", idParts[0]);
	}


}