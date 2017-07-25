package org.uofm.ot.FedoraAccessLayer;

import com.complexible.pinto.RDFMapper;
import org.apache.jena.arq.querybuilder.SelectBuilder;
import org.apache.jena.query.*;
import org.apache.jena.vocabulary.RDF;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;
import org.openrdf.model.Model;
import org.openrdf.model.Namespace;
import org.openrdf.model.impl.SimpleNamespace;
import org.uofm.ot.exception.ObjectTellerException;
import org.uofm.ot.fedoraGateway.FCRepoService;
import org.uofm.ot.knowledgeObject.ArkId;
import org.uofm.ot.knowledgeObject.KnowledgeObject;
import org.uofm.ot.services.FedoraConfiguration;

import java.net.URI;

import static org.junit.Assert.*;

/**
 * Created by nggittle on 4/19/17.
 */
public class FCRepoServiceIT {

  private FCRepoService fos;

  @Rule
  public ExpectedException expectedEx = ExpectedException.none();
  FedoraConfiguration fconfig;

  @Before
  public void setUp() {
    fconfig = new FedoraConfigurationBuilder()
        .fcRepoUrl("https://dlhs-fedora.med.umich.edu/fcrepo/rest/")
        .fcrepoUsername("fedoraAdmin")
        .fcrepoPassword("secret321")
        .fusekiUrl("https://dlhs-fedora.med.umich.edu/fuseki/test/query")
        .fusekiPrefix("https://dlhs-fedora.med.umich.edu/fcrepo/rest/")
        .build();
    fos = new FCRepoService();
    fos.setFedoraConfiguration(fconfig);
  }

  @Test
  public void testCheckObjectWhenObjectDoesntExist() throws Exception {
    assertFalse(fos.checkIfObjectExists(new URI("https://dlhs-fedora.med.umich.edu/fcrepo/rest/null")));
  }

  @Test
  public void serializeObject() throws Exception {
    ArkId arkId = new ArkId("ark:/99999/fk4TEST01");
    KnowledgeObject ko = new KnowledgeObject(arkId);
    ko.setInputMessage("TESTINPUT");
    ko.setOutputMessage("TESTOUTOUT");
    Namespace namespace = new SimpleNamespace("ot", "http://uofm.org/objectteller/");
     RDFMapper mapper = RDFMapper.builder()
        .namespace(namespace)
        .build();
    Model model = mapper.writeValue(ko);
    model.iterator();
    String test = model.toString();

    System.out.println(test);
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
}