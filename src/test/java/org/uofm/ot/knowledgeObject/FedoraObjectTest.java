package org.uofm.ot.knowledgeObject;

import org.junit.Test;

import static org.junit.Assert.assertNotNull;

public class FedoraObjectTest {

	@Test
	public void fedoraObjectHasArkId () {
		FedoraObject fedoraObject = new FedoraObject(ArkId.FAKE_ARKID());
		assertNotNull(fedoraObject.getArkId());
	}

}