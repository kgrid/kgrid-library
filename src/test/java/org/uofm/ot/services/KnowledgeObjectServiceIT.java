package org.uofm.ot.services;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.junit4.SpringRunner;
import org.uofm.ot.ObjectTellerApplication;
import org.uofm.ot.knowledgeObject.ArkId;
import org.uofm.ot.knowledgeObject.KnowledgeObject;
import org.uofm.ot.knowledgeObject.Metadata;
import org.uofm.ot.knowledgeObject.Payload;
import org.uofm.ot.model.OTUser;
import org.uofm.ot.model.UserProfile;

import java.util.ArrayList;

import static org.junit.Assert.assertNull;


/**
 * Created by nggittle on 4/27/17.
 */
@RunWith(SpringRunner.class)
@ContextConfiguration(classes = {ObjectTellerApplication.class})
@TestPropertySource("classpath:test.properties")  // Careful: loads test ezid service by default
public class KnowledgeObjectServiceIT {

  @Autowired
  private KnowledgeObjectService koService;

  private KnowledgeObject dummyKO;
  private OTUser loggedInUser;
  private String libraryURL = "http://localhost:8080/knowledgeObject";

  @Before
  public void setUp() throws Exception {
    dummyKO = new KnowledgeObject();
    dummyKO.setArkId(new ArkId());
    dummyKO.setInputMessage("Input test");
    dummyKO.setOutputMessage("Output test");
    Payload payload = new Payload();
    payload.setEngineType("Python");
    payload.setFunctionName("test");
    payload.setContent("test");
    dummyKO.setPayload(payload);
    Metadata metadata = new Metadata();
    metadata.setTitle("test");
    metadata.setContributors("test");
    metadata.setKeywords("test");
    metadata.setDescription("test");
    dummyKO.setMetadata(metadata);

    ArrayList<GrantedAuthority> auth = new ArrayList<>();
    auth.add(new SimpleGrantedAuthority("ADMIN"));

    loggedInUser = new OTUser("test", "test", auth);
    loggedInUser.setProfile(new UserProfile("ftest", "ltest"));
  }

  @After
  public void tearDown() throws Exception {
  }

  @Test
  public void tryToCreateEditAndDeleteKO() throws Exception {

    KnowledgeObject createdKO = koService.createNewKnowledgeObject(dummyKO, loggedInUser);
    // Need to wait for the fedora repository to finish creating the KO before getting it
    Thread.sleep(1000);
    koService.deleteObject(dummyKO.getArkId());
    assertNull(koService.getKnowledgeObject(dummyKO.getArkId()));

  }

}