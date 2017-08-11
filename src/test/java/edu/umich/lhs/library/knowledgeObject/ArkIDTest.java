package edu.umich.lhs.library.knowledgeObject;

import org.junit.Test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

public class ArkIDTest {

	@Test
	public void givenArkIdPathThenNewArkIdHasCorrectIdAndFedoraId() {
		ArkId arkId = ArkId.FAKE_ARKID();

		assertEquals("Arkid should match scheme + naan + name", "ark:/" + arkId.getNaan() + "/" + arkId.getName(), arkId.getArkId());

		assertEquals("fedora pid should match naan + name", arkId.getNaan() + "-" + arkId.getName(), arkId.getFedoraPath());
	}

	@Test
	public void givenPlainPathThenNewArkIdHasCorrectIdAndFedoraPath() throws Exception {

		ArkId arkId = new ArkId("OT42");

		assertEquals("Arkid should match original path", "OT42", arkId.getArkId());

		assertEquals("Arkid should match name", arkId.getName(), arkId.getArkId());

		assertEquals("fedora pid should match name", "OT42", arkId.getFedoraPath());

	}

	@Test
	public void givenBadPathThrowsIllegalArgumentException() throws Exception {

        try {
            ArkId arkId = new ArkId(null);
            assertTrue("Failed to throw IllegalArgumentException", false);
        } catch (IllegalArgumentException e) {
            // good
        } catch (Exception e) {
            assertTrue("Should throw IllegalArgumentException, not " + e.getClass(), false);
        }

        try {
            ArkId arkId = new ArkId("foo/bar");
            assertTrue("Failed to throw IllegalArgumentException", false);
        } catch (IllegalArgumentException e) {
            // good
        } catch (Exception e) {
            assertTrue("Should throw IllegalArgumentException, not " + e.getClass(), false);
        }

    }
}
