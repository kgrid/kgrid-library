package org.uofm.ot.services;

import static org.junit.Assert.*;

import java.util.ArrayList;
import java.util.List;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.junit4.SpringRunner;
import org.uofm.ot.ObjectTellerApplication;
import org.uofm.ot.fedoraAccessLayer.FCRepoService;
import org.uofm.ot.knowledgeObject.ArkId;
import org.uofm.ot.knowledgeObject.KnowledgeObject;
import org.uofm.ot.knowledgeObject.Metadata;
import org.uofm.ot.knowledgeObject.Payload;
import org.uofm.ot.model.OTUser;
import org.uofm.ot.model.UserProfile;


/**
 * Created by nggittle on 4/27/17.
 */
@RunWith(SpringRunner.class)
@ContextConfiguration(classes = {ObjectTellerApplication.class})
@TestPropertySource("classpath:test.properties")  // Careful: loads test ezid service by default
public class KnowledgeObjectServiceTest {

  @Autowired
  KnowledgeObjectService koService;

  @Before
  public void setUp() throws Exception {
  }

  @After
  public void tearDown() throws Exception {
  }

  @Test
  public void tryToCreateEditAndDeleteKO() throws Exception {
    KnowledgeObject dummyKO = new KnowledgeObject();
    dummyKO.setArkId(new ArkId());
    dummyKO.setInputMessage("Input test");
    dummyKO.setOutputMessage("Output test");
    dummyKO.setPayload(new Payload());
    dummyKO.setMetadata(new Metadata());

    ArrayList<GrantedAuthority> auth = new ArrayList<>();
    auth.add(new SimpleGrantedAuthority("ADMIN"));

    OTUser loggedInUser = new OTUser("test", "test", auth);
    loggedInUser.setProfile(new UserProfile("ftest", "ltest"));
    dummyKO = koService.createObject(dummyKO, loggedInUser, "http://localhost:8080/knowledgeObject", null);
    assertNotNull(dummyKO.getPayload());
    // Need to wait for the fedora repository to finish creating the KO before getting it
    Thread.sleep(1000);
    KnowledgeObject retrievedKO = koService.getKnowledgeObject(dummyKO.getArkId());
    assertNotNull(retrievedKO);
    KnowledgeObject editedKO = koService.editObject(retrievedKO, dummyKO.getArkId());
    Thread.sleep(1000);
    assertNotEquals(retrievedKO, editedKO);
    koService.deleteObject(dummyKO.getArkId());
    Thread.sleep(1000);
    assertNull(koService.getKnowledgeObject(dummyKO.getArkId()));

  }

  @Test
  public void addAndEditMetadataToKO() throws Exception {
   KnowledgeObject dummyKO = new KnowledgeObject();

  }

  @Test
  public void getKnowledgeObject() throws Exception {
    ArkId arkId = new ArkId("ark:/test/test");
    KnowledgeObject ko = koService.getKnowledgeObject(arkId);
    ko.getURI();
  }



}