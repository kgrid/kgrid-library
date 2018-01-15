package edu.umich.lhs.library.controller;

import static org.springframework.restdocs.hypermedia.HypermediaDocumentation.links;
import static org.springframework.restdocs.mockmvc.MockMvcRestDocumentation.document;
import static org.springframework.restdocs.mockmvc.MockMvcRestDocumentation.documentationConfiguration;
import static org.springframework.restdocs.mockmvc.RestDocumentationRequestBuilders.get;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.user;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.security.test.web.servlet.setup.SecurityMockMvcConfigurers.*;
import static org.springframework.restdocs.payload.PayloadDocumentation.*;

import edu.umich.lhs.library.knowledgeObject.KnowledgeObject;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.restdocs.JUnitRestDocumentation;
import org.springframework.restdocs.payload.JsonFieldType;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;
import com.fasterxml.jackson.databind.ObjectMapper;


@RunWith(SpringRunner.class)
@SpringBootTest
public class KnowledgeObjectControllerTest {

  @Rule
  public final JUnitRestDocumentation restDocumentation = new JUnitRestDocumentation("target/generated-snippets");

  @Autowired
  private KnowledgeObjectController koController;

  @Autowired
  private ObjectMapper objectMapper;

  @Autowired
  private WebApplicationContext context;

  private MockMvc mockMvc;

  @Before
  public void setUp() {
    this.mockMvc = MockMvcBuilders.webAppContextSetup(this.context)
        .apply(springSecurity())
        .apply(documentationConfiguration(this.restDocumentation))
        .alwaysDo(document("{method-name}/{step}/"))
        .build();
  }

  @Test
  public void getKOList() throws Exception {
    this.mockMvc.perform(get("/knowledgeObject").accept(MediaType.APPLICATION_JSON_VALUE))
        .andExpect(status().isOk());
  }

  /**
   * Creates a simple knowledge object with a title, version and owner.
   * @throws Exception
   */
  @Test
  public void createGetAndDeleteKO() throws Exception {
    String title = "testKO" + UUID.randomUUID();
    Map<String, Object> metadata = new HashMap<>();
    metadata.put("title", title);
    metadata.put("version", "0.0.1");
    metadata.put("owner", "testing");
    Map<String, Object> ko = new HashMap<>();
    ko.put("metadata", metadata);

    String createdKO = this.mockMvc
        .perform(
            post("/knowledgeObject")
                .with(user("admin@kgrid.org").password("test").roles("ADMIN"))
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .content(objectMapper.writeValueAsString(ko)))
        .andExpect(status().isCreated())
        .andExpect(content().string(org.hamcrest.Matchers.containsString("\"title\":\"" + title + "\"")))
        .andDo(document("index",
            responseFields(fieldWithPath("metadata.arkId").description("The object's unique id"),
                fieldWithPath("metadata.title").description("The title for the ko"),
                fieldWithPath("metadata.version").description("The ko's version"),
                fieldWithPath("metadata.owner").description("The creator of the ko"),
                fieldWithPath("metadata.description").description("A description of the ko").optional(),
                fieldWithPath("metadata.contributors").description("Contributors to the ko"),
                fieldWithPath("metadata.keywords").description("Tags for the ko"),
                fieldWithPath("metadata.published").description("Indicates if the object has been published or is still a draft")
                    .type(JsonFieldType.BOOLEAN).optional(),
                fieldWithPath("metadata.lastModified").description("The date the ko was last modified").type(JsonFieldType.OBJECT).optional(),
                fieldWithPath("metadata.createdOn").description("The date of the ko's creation").type(JsonFieldType.OBJECT).optional(),
                fieldWithPath("metadata.objectType").ignored(),
                fieldWithPath("metadata.citations").description("A list of citations").type(JsonFieldType.ARRAY).optional(),
                fieldWithPath("metadata.license").description("A link to a license specifying use permissions").type(JsonFieldType.OBJECT).optional(),
                fieldWithPath("inputMessage").description("Validates the inputs supplied when invoking the ko").type(JsonFieldType.OBJECT).optional(),
                fieldWithPath("outputMessage").description("Validates the output received from the ko").type(JsonFieldType.OBJECT).optional(),
                fieldWithPath("payload.content").description("The model representation heart of the ko").type(JsonFieldType.STRING).optional(),
                fieldWithPath("payload.functionName").description("The name of function to be invoked in the payload").type(JsonFieldType.STRING).optional(),
                fieldWithPath("payload.engineType").description("The adapter to use when invoking the payload").type(JsonFieldType.STRING).optional(),
                fieldWithPath("logData").description("A log of changes to the ko").type(JsonFieldType.OBJECT).optional(),
                fieldWithPath("uri").description("The location of the ko"))))
        .andReturn().getResponse().getContentAsString();

    ObjectMapper mapper = new ObjectMapper();
    KnowledgeObject knowledgeObject = mapper.readValue(createdKO, KnowledgeObject.class);

    System.out.println("ArkId: " + knowledgeObject.getMetadata().getArkId());

    this.mockMvc.perform(
        get("/knowledgeObject/" + knowledgeObject.getMetadata().getArkId()))
        .andExpect(status().isOk())
        .andExpect(content().string(org.hamcrest.Matchers.containsString("\"title\":\"" + title + "\"")));

    this.mockMvc.perform(
        delete("/knowledgeObject/" + knowledgeObject.getMetadata().getArkId()))
        .andExpect(status().isNoContent());

  }

  @Test
  public void setKOPayload() throws Exception {
    String title = "testKO" + UUID.randomUUID();

    Map<String, Object> metadata = new HashMap<>();
  }

}