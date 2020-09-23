package org.kgrid.library.fusekiGateway;

import org.apache.jena.arq.querybuilder.SelectBuilder;
import org.apache.jena.query.*;
import org.apache.jena.rdf.model.RDFNode;
import org.apache.jena.sparql.engine.http.QueryExceptionHTTP;
import org.apache.log4j.Logger;
import org.kgrid.library.exception.LibraryException;
import org.kgrid.library.model.ServerDetails;
import org.kgrid.library.services.FedoraConfiguration;
import org.kgrid.shelf.domain.KnowledgeObjectWrapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.net.ConnectException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Locale;

@Component
public class FusekiService {

  private FedoraConfiguration fedoraConfiguration;

  private String fusekiServerURL;

  private static final Logger logger = Logger.getLogger(FusekiService.class);

  public FusekiService() {}

  @Autowired
  public FusekiService(FedoraConfiguration fedoraConfiguration) {
    setFedoraConfiguration(fedoraConfiguration);
  }

  private void setFedoraConfiguration(FedoraConfiguration fedoraConfiguration) {
    this.fedoraConfiguration = fedoraConfiguration;
    initFusekiUrl();
  }

  private void initFusekiUrl() {
    ServerDetails fusekiServer = fedoraConfiguration.getFusekiServerConfiguration();
    if (fusekiServer != null) {
      fusekiServerURL = fusekiServer.getUrl();
    }
  }

  public ArrayList<KnowledgeObjectWrapper> getKnowledgeObjects(boolean published)
      throws LibraryException {
    try {
      if (fedoraConfiguration.getFusekiServerConfiguration().getUrl() != null) {
        Query query = initQuery(published);

        return getFedoraObjects(query);
      } else {
        logger.error("Fuseki Server URL is not configured");
        throw new LibraryException("Fuseki Server URL is not configured");
      }
    } catch (ConnectException ex) {
      logger.error("Check if fuseki server up and running. " + ex);
      throw new LibraryException(ex);
    }
  }

  public int countUniqueArkIds() throws LibraryException {
    try {
      SelectBuilder countArkIds =
          new SelectBuilder()
              .addVar("count(*)", "?count")
              .addWhere("?s", "<http://uofm.org/objectteller/arkId>", "?o");
      QueryExecution queryExecution =
          QueryExecutionFactory.sparqlService(fusekiServerURL, countArkIds.build());
      ResultSet resultSet = queryExecution.execSelect();
      return resultSet.next().get("count").asLiteral().getInt();
    } catch (org.apache.jena.sparql.lang.sparql_11.ParseException e) {
      throw new LibraryException("Cannot create query for count of ark ids" + e);
    }
  }

  private Query initQuery(boolean published) {

    String subject = "?x";

    SelectBuilder getAllObjects =
        new SelectBuilder()
            .addPrefix(NamespaceConstants.OT_NAMESPACE_PREFIX, NamespaceConstants.OT_NAMESPACE_URL)
            .addPrefix(NamespaceConstants.DC_NAMESPACE_PREFIX, NamespaceConstants.DC_NAMESPACE_URL)
            .addPrefix(
                NamespaceConstants.FEDORA_NAMESPACE_PREFIX, NamespaceConstants.FEDORA_NAMESPACE_URL)
            .addVar("*")
            .addWhere(subject, "dc:title", "?title")
            .addWhere(subject, "fedora:created", "?created")
            .addWhere(subject, "fedora:lastModified", "?lastModified")
            .addOptional(subject, "ot:published", "?published")
            .addOptional(subject, "ot:keywords", "?keywords")
            .addOptional(subject, "ot:owner", "?owner")
            .addOptional(subject, "ot:contributors", "?contributors")
            .addOptional(subject, "ot:description", "?description")
            .addOptional(subject, "ot:arkId", "?arkId")
            .addOptional(subject, "ot:licenseName", "?licenseName")
            .addOptional(subject, "ot:licenseLink", "?licenseLink");

    if (published) {
      try {
        getAllObjects.addFilter("?published = \"yes\" || ?published = true");
      } catch (org.apache.jena.sparql.lang.sparql_11.ParseException e) {
        logger.error("Invalid filter for published objects " + e);
      }
    }
    return getAllObjects.build();
  }

  private ArrayList<KnowledgeObjectWrapper> getFedoraObjects(Query query)
      throws ConnectException, LibraryException {
    ArrayList<KnowledgeObjectWrapper> list = new ArrayList<>();
    QueryExecution execution = QueryExecutionFactory.sparqlService(fusekiServerURL, query);

    ResultSet resultSet;
    try {
      resultSet = execution.execSelect();
    } catch (QueryExceptionHTTP e) {
      throw new ConnectException("Cannot fetch object list from fuseki. " + e);
    }

    while (resultSet.hasNext()) {
      QuerySolution binding = resultSet.nextSolution();

      KnowledgeObjectWrapper knowledgeObject = null;
      list.add(knowledgeObject);
    }

    return list;
  }

  private Date convertRDFNodetoDate(RDFNode o) throws LibraryException {
    Date convertedDate;
    try {
      String date = o.toString();
      date = date.substring(0, 10);
      convertedDate = new SimpleDateFormat("yyyy-MM-dd", Locale.ENGLISH).parse(date);
    } catch (ParseException e) {
      logger.error("Unable to parse created on date or last updated date" + e.getCause());
      throw new LibraryException(
          "Unable to parse created on date or last updated date" + e.getCause(), e);
    }
    return convertedDate;
  }
}
