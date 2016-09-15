package org.uofm.ot.services;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

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

    @Test
    public void canConnectToEzidService()  {

        String response = ezidService.get("foo");

        assertTrue(response.contains("_target"));
    }


}