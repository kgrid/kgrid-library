package org.kgrid.library.FedoraAccessLayer;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

import java.net.URI;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;
import org.junit.runner.RunWith;
import org.kgrid.library.exception.LibraryException;
import org.kgrid.library.fedoraGateway.FCRepoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

/**
 * Created by nggittle on 4/19/17.
 */
@RunWith(SpringRunner.class)
@SpringBootTest
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
    expectedEx.expect(LibraryException.class);
    fos.commitTransaction(null);
  }

  @Test
  public void rollbackTransaction() throws Exception {
    fos.rollbackTransaction(fos.createTransaction());
  }

  @Test
  public void rollbackIncorrectTransaction() throws Exception {
    expectedEx.expect(LibraryException.class);
    fos.rollbackTransaction(null);
  }

  @Test
  public void testPing() throws Exception {
    assertTrue(fos.ping());
  }
}