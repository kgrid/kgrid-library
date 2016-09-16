package org.uofm.ot.services;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import java.io.IOException;
import java.net.URISyntaxException;

import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

public class EzidServiceTest {
    private EzidService ezidService;

    @Before
    public void setUp() throws Exception {

        ezidService = new EzidService();

    }

    @After
    public void tearDown() throws Exception {

    }

//    @Test
    public void canConnectToEzidService() throws IOException, URISyntaxException {

        String response = ezidService.get("foo");

        assertTrue(response.contains("_target"));
    }

    @Test
    public void mintReturnsArkId() {

        String arkId = ezidService.mint();

        System.out.println("arkId: " + arkId);

        assertNotNull(arkId);

    }


}