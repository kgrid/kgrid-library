package org.uofm.ot.FedoraAccessLayer;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

import java.net.URI;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.web.WebAppConfiguration;
import org.uofm.ot.ObjectTellerApplication;
import org.uofm.ot.exception.ObjectTellerException;
import org.uofm.ot.fedoraGateway.FCRepoService;

/**
 * Created by nggittle on 4/19/17.
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(classes= {ObjectTellerApplication.class})
@WebAppConfiguration
@TestPropertySource("classpath:application.properties")
public class FCRepoServiceIT {

  @Autowired
  private FCRepoService fos;

  @Rule
  public ExpectedException expectedEx = ExpectedException.none();

  @Test
  public void testCheckObjectWhenObjectDoesntExist() throws Exception {
    assertFalse(fos.checkIfObjectExists(new URI(fos.getBaseURI() + "null")));
  }

  @Test
  public void createTransactionID() throws Exception {
    assertNotNull(fos.createTransaction());
  }

  @Test
  public void commitCorrectTransaction() throws Exception {
    fos.commitTransaction(fos.createTransaction());
  }

  @Test
  public void commitIncorrectTransaction() throws Exception {
    expectedEx.expect(ObjectTellerException.class);
    fos.commitTransaction(null);
  }

  @Test
  public void rollbackTransaction() throws Exception {
    fos.rollbackTransaction(fos.createTransaction());
  }

  @Test
  public void rollbackIncorrectTransaction() throws Exception {
    expectedEx.expect(ObjectTellerException.class);
    fos.rollbackTransaction(null);
  }

  @Test
  public void testPing() throws Exception {
    assertTrue(fos.ping());
  }
}