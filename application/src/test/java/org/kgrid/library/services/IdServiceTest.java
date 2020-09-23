package org.kgrid.library.services;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.kgrid.library.ezidGateway.DummyIdService;
import org.kgrid.library.ezidGateway.EzidService;
import org.kgrid.shelf.domain.ArkId;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.ArrayList;

import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

public class IdServiceTest {

  private static final String ARKID_STRING = "ark:/99999/fake";

  private IdService idService;

  @Mock private EzidService ezidService;

  @Mock private DummyIdService dummyIdService;

  public IdServiceTest() {
    MockitoAnnotations.initMocks(this);
  }

  @Before
  public void setUp() {
    idService = new IdService(ezidService, dummyIdService);
  }

  @After
  public void tearDown() throws Exception {}

  @Test
  public void testMethodDelegation() throws Exception {
    when(ezidService.mint()).thenReturn(ARKID_STRING);
    idService.mint();
    verify(ezidService).mint();
  }

  // TODO: reimplement ark id statuses
  @Test
  public void publishKnowledgeObject() {

    ArrayList<String> metadata = new ArrayList<String>();
    idService.publish(new ArkId("ark:/test/ark"), metadata);

    //		verify(ezidService).status(ARKID_STRING, metadata, ArkId.Status.PUBLIC);
  }

  @Test
  public void retractId() {
    ArrayList<String> metadata = new ArrayList<String>();
    idService.retract(new ArkId(ARKID_STRING), metadata);

    //		verify(ezidService).status(ARKID_STRING, metadata, ArkId.Status.UNAVAILABLE);
  }

  @Test
  public void testPing() throws Exception {
    when(ezidService.ping()).thenReturn(true);
    assertEquals(true, idService.ping());
  }
}
