package edu.umich.lhs.library.services;

import static org.junit.Assert.assertEquals;
import static org.mockito.Matchers.any;
import static org.mockito.Matchers.eq;

import edu.umich.lhs.library.exception.LibraryException;
import edu.umich.lhs.library.fedoraGateway.ChildType;
import edu.umich.lhs.library.fedoraGateway.FCRepoService;
import edu.umich.lhs.library.fusekiGateway.FusekiService;
import edu.umich.lhs.library.knowledgeObject.ArkId;
import edu.umich.lhs.library.knowledgeObject.Citation;
import edu.umich.lhs.library.knowledgeObject.KnowledgeObject;
import edu.umich.lhs.library.knowledgeObject.License;
import edu.umich.lhs.library.knowledgeObject.Metadata;
import edu.umich.lhs.library.knowledgeObject.Payload;
import edu.umich.lhs.library.knowledgeObject.Version;
import edu.umich.lhs.library.model.LibraryUser;
import edu.umich.lhs.library.model.UserProfile;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.openrdf.model.Model;
import org.openrdf.model.impl.LinkedHashModel;
import org.openrdf.model.impl.SimpleValueFactory;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.test.context.junit4.SpringRunner;

@RunWith(SpringRunner.class)

public class KnowledgeObjectServiceTest {

  @Mock
  private IdService idService;

  @Mock
  private FusekiService fusekiService;

  @Mock
  private FCRepoService fcRepoService;

  @Mock
  private VersioningService versioningService;

  private ArkId arkId;

  private LibraryUser user;

  private KnowledgeObjectService koService;

  @Before
  public void setUp() throws URISyntaxException, LibraryException {
    arkId = new ArkId("ark:/99999/fk4test");
    URI baseURI = new URI("http://localhost:8080/fcrepo/rest/");
    URI transactionURI = new URI(baseURI + "transactionURI/");
    URI containerURI = new URI(transactionURI + arkId.getFedoraPath());
    URI provURI = new URI (containerURI + "/" + ChildType.LOG.getChildType() + "/" + ChildType.CREATEACTIVITY.getChildType());
    URI citationParentURI = new URI(containerURI + "/" + ChildType.CITATIONS.getChildType());
    URI citationURI = new URI(citationParentURI + "/1");

    // Mocking inserting RDF data for a new knowledge object into fedora:
    Mockito.when(idService.mint()).thenReturn(arkId);
    Mockito.when(idService.createBasicMetadata("test", "ADMIN")).thenReturn(new ArrayList<>());
    Mockito.when(fcRepoService.getBaseURI()).thenReturn(baseURI);
    Mockito.when(fcRepoService.createTransaction()).thenReturn(transactionURI);
    Mockito.when(fcRepoService.createContainer(containerURI)).thenReturn(containerURI);
    Mockito.when(fcRepoService.createContainer(provURI)).thenReturn(provURI);
    Mockito.when(fcRepoService.createContainerWithAutoGeneratedName(citationParentURI)).thenReturn(citationURI);
    Mockito.when(fcRepoService.putRDFData(any(), eq(citationURI))).thenReturn(citationURI);

    // Setting up a user to create a knowledge object
    Collection<GrantedAuthority> auth = new ArrayList<>();
    auth.add(new SimpleGrantedAuthority("ADMIN"));
    user = new LibraryUser("test", "test", auth);
    user.setProfile(new UserProfile("first", "last"));

    koService = new KnowledgeObjectService(idService, fusekiService, fcRepoService, versioningService);
  }

  @Test
  public void testCreateBlankKnowledgeObject() throws Exception {

    KnowledgeObject ko = new KnowledgeObject();
    ko = koService.createNewKnowledgeObject(ko, user);

    assertEquals("ark:/99999/fk4test", ko.getMetadata().getArkId());
    assertEquals("v0.1.0", ko.getMetadata().getVersion());
  }

  @Test
  public void testCreateCompleteKnowledgeObject() throws Exception {
    License license = new License();
    license.setLicenseLink("http://example.com/license");
    license.setLicenseName("example");

    Citation citation1 = new Citation();
    citation1.setCitation_at("http://example.com/citation1");
    citation1.setCitation_title("citation1");
    Citation citation2 = new Citation();
    citation2.setCitation_at("http://example.com/citation2");
    citation2.setCitation_title("citation2");
    List<Citation> citations = Arrays.asList(citation1, citation2);

    Metadata metadata = new Metadata();
    metadata.setVersion(new Version("1.0.0"));
    metadata.setPublished(true);
    metadata.setLicense(license);
    metadata.setCitations(citations);
    metadata.setContributors("Alice, Bob, Carol, Dave");
    metadata.setDescription("Description of this test knowledge object");
    metadata.setOwner("Owner");
    metadata.setKeywords("Key, Word");
    metadata.setTitle("Title of Amazing Test Knowledge Object");

    Payload payload = new Payload();
    payload.setContent("Payload content");
    payload.setEngineType("Python");
    payload.setFunctionName("FUNction");

    KnowledgeObject ko = new KnowledgeObject();
    ko.setMetadata(metadata);
    ko.setPayload(payload);
    ko.setInputMessage("Input message");
    ko.setOutputMessage("Output message");
    ko.setLogData("Log data");

    ko = koService.createNewKnowledgeObject(ko, user);

    assertEquals("ark:/99999/fk4test", ko.getMetadata().getArkId());
    assertEquals("v1.0.0", ko.getMetadata().getVersion());
    assertEquals(null, ko.getMetadata().getCitations());
    assertEquals(null, ko.getMetadata().getLicense());
  }

  @Test
  public void testGetBasicKnowledgeObject() throws Exception {

    Model metadataModel = new LinkedHashModel();
    URI baseURI = new URI("http://localhost:8080/fcrepo/rest/");
    String objectLoc = baseURI + arkId.getFedoraPath();

    SimpleValueFactory factory = SimpleValueFactory.getInstance();
    metadataModel.add(factory.createIRI(objectLoc),
        factory.createIRI("http://purl.org/dc/elements/1.1/title"),
        factory.createLiteral("test object"));
    metadataModel.add(factory.createIRI(objectLoc),
        factory.createIRI("http://uofm.org/objectteller/arkId"),
        factory.createLiteral("ark:/99999/fk4test"));

    // Mocking retrieving a knowledge object from fedora
    Mockito.when(fcRepoService.getRDFData(new URI(objectLoc))).thenReturn(metadataModel);


    KnowledgeObject ko = koService.getKnowledgeObject(arkId);
    assertEquals(arkId.getArkId(), ko.getArkId().getArkId());
    assertEquals("test object", ko.getMetadata().getTitle());
  }



}