package org.uofm.ot.FedoraAccessLayer;

import static org.junit.Assert.*;

import org.apache.jena.arq.querybuilder.SelectBuilder;
import org.apache.jena.query.Query;
import org.apache.jena.query.QueryExecution;
import org.apache.jena.query.QueryExecutionFactory;
import org.apache.jena.query.QuerySolution;
import org.apache.jena.query.ResultSet;
import org.apache.jena.vocabulary.RDF;
import org.junit.Before;
import org.junit.Test;
import org.uofm.ot.fedoraAccessLayer.FedoraObjectService;
import org.uofm.ot.services.FedoraConfiguration;

/**
 * Created by nggittle on 4/19/17.
 */
public class FedoraObjectServiceTest {

  private FedoraObjectService fos;

  @Before
  public void setUp() {
    FedoraConfiguration config = new FedoraConfigurationBuilder()
        .fcRepoUrl("https://dlhs-fedora.med.umich.edu/fcrepo/rest/")
        .fcrepoUsername("fedoraAdmin")
        .fcrepoPassword("secret321")
        .fusekiUrl("https://dlhs-fedora.med.umich.edu/fuseki/test/query")
        .fusekiPrefix("https://dlhs-fedora.med.umich.edu/fcrepo/rest/")
        .build();
    fos = new FedoraObjectService();
    fos.setFedoraConfiguration(config);
  }

  @Test
  public void testCheckObjectWhenObjectExists() throws Exception {
    assertTrue(fos.checkIfObjectExists("99999-fk41265c2w"));
  }

  @Test
  public void testCheckObjectWhenObjectDoesntExist() throws Exception {
    assertFalse(fos.checkIfObjectExists("null"));
  }

  @Test
  public void testGetNumberOfObjects() throws Exception {
    SelectBuilder builder = new SelectBuilder();
    Query query = builder
        .addPrefix("ot", "http://uofm.org/objectteller/")
        .addVar("*")
        .addWhere("?s", "?p", "?o")
        .addWhere("?s", RDF.type, "<https://dlhs-fedora.med.umich.edu/fuseki/test/query>")
        .build();

    QueryExecution execution = QueryExecutionFactory
        .sparqlService("https://dlhs-fedora.med.umich.edu/fuseki/test/query", query);
    ResultSet resultSet = execution.execSelect();
    while(resultSet.hasNext()) {
      QuerySolution binding = resultSet.nextSolution();


    }

  }



}