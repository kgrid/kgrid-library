package org.uofm.ot.services;

import static org.junit.Assert.*;

import org.junit.Test;

public class ArkIDTest {

	@Test
	public void test() {
		ArkID arkId = new ArkID("ark:/99999/12345");
		assertNotNull(arkId.getArkId());
	}

	
	
}
