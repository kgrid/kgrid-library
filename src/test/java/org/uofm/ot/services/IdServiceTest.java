package org.uofm.ot.services;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.*;


public class IdServiceTest {

    private IdService idService;

    @Before
    public void setUp() {

    idService = new IdService();

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