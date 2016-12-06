package org.uofm.ot.knowledgeObject;

import org.junit.Test;

import static org.junit.Assert.assertNotNull;

public class KnowledgeObjectTest {

	@Test
	public void fedoraObjectHasArkId () {
		KnowledgeObject fedoraObject = new KnowledgeObject(ArkId.FAKE_ARKID());
		assertNotNull(fedoraObject.getArkId());
	}

}
