package org.uofm.ot.knowledgeObject;

import org.junit.Test;

import static org.junit.Assert.assertNotNull;

public class ArkIDTest {

	@Test
	public void test() {
		ArkID arkId = new ArkID("ark:/99999/12345");
		assertNotNull(arkId.getArkId());
	}

	
	
}
