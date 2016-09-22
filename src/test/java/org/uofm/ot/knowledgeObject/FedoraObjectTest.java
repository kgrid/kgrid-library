package org.uofm.ot.knowledgeObject;

import static org.junit.Assert.*;

import org.junit.Test;
import org.uofm.ot.services.ArkID;
import org.uofm.ot.services.IdService;

public class FedoraObjectTest {

	@Test
	public void fedoraObjectHasArkId () {
		FedoraObject fedoraObject = new FedoraObject(new ArkID(IdService.ARKID));
		assertNotNull(fedoraObject.getArkID());
	}

}
