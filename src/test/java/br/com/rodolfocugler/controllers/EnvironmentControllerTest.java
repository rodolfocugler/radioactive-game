package br.com.rodolfocugler.controllers;

import br.com.rodolfocugler.domains.*;
import com.fasterxml.jackson.databind.type.CollectionType;
import com.fasterxml.jackson.databind.type.TypeFactory;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.DirtiesContext;

import java.util.List;

import static br.com.rodolfocugler.configs.AuthenticationConfig.HEADER_STRING;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.springframework.http.MediaType.APPLICATION_JSON_VALUE;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
@DirtiesContext(classMode = DirtiesContext.ClassMode.BEFORE_EACH_TEST_METHOD)
class EnvironmentControllerTest extends BaseControllerTest {

  private static String urlPath = "/api/environments";

  @Test
  public void shouldReturnOneEnvironmentAfterSaveOne() throws Exception {
    String token = getToken();

    Environment environment = Environment.builder()
            .name("name").description("description").icon("icon")
            .build();

    mockMvc.perform(post(urlPath).contentType(APPLICATION_JSON_VALUE)
            .content(mapper.writeValueAsString(environment))
            .header(HEADER_STRING, token))
            .andExpect(status().isOk())
            .andReturn()
            .getResponse()
            .getContentAsString();

    List<Environment> environments = getEnvironments(token);

    assertEquals(1, environments.size());
    assertEquals(environment.getDescription(), environments.get(0).getDescription());
    assertEquals(environment.getName(), environments.get(0).getName());
  }

  @Test
  public void shouldReturnOneEnvironmentWithUserResponses() throws Exception {
    String token = getToken();

    Environment environment = Environment.builder()
            .id(1).name("name").description("description").icon("icon")
            .build();

    Question question = Question.builder().description("description").id(1)
            .environment(environment).build();

    Response response = Response.builder().question(question).text("text").build();

    postBase(environment, urlPath, token);
    postBase(question, "/api/questions", token);
    postBase(response, "/api/responses", token);

    String httpResponse = getBase(urlPath + "/getWithUserResponses", token);
    Environment actualEnvironment = mapper.readValue(httpResponse, Environment.class);

    assertEquals(environment.getDescription(), actualEnvironment.getDescription());
    assertEquals(environment.getName(), actualEnvironment.getName());

    assertEquals(1, actualEnvironment.getQuestions().size());
    Question actualQuestion = actualEnvironment.getQuestions().get(0);
    assertEquals(question.getDescription(), actualQuestion.getDescription());

    Response actualResponse = actualQuestion.getResponses().get(0);
    assertEquals(1, actualQuestion.getResponses().size());
    assertEquals(response.getText(), actualResponse.getText());
  }

  @Test
  public void shouldIgnoreResponseOfADifferentUser() throws Exception {
    String token = getToken();

    Environment environment = Environment.builder()
            .id(1).name("name").description("description").icon("icon").build();
    Question question = Question.builder().description("description").id(1)
            .environment(environment).build();
    Response response = Response.builder().question(question).text("text").build();

    postBase(environment, urlPath, token);
    postBase(question, "/api/questions", token);
    postBase(response, "/api/responses", token);

    Account account = Account.builder().email("email2@email2.com").name("name2").number("12345")
            .password("12345@2021").environment(environment).build();
    addAccount(account);
    String token2 = getToken(account);

    ChatMessage chatMessage = ChatMessage.builder().text("text").build();

    postBase(response, "/api/responses", token2);
    postBase(chatMessage, "/api/chatMessages", token2);

    String httpResponse = getBase(urlPath + "/getWithUserResponses", token);
    Environment actualEnvironment = mapper.readValue(httpResponse, Environment.class);

    assertEquals(environment.getDescription(), actualEnvironment.getDescription());
    assertEquals(environment.getName(), actualEnvironment.getName());
//    assertNull(environment.getAccounts());
//    assertNull(environment.getChatMessages());

    assertEquals(1, actualEnvironment.getQuestions().size());
    Question actualQuestion = actualEnvironment.getQuestions().get(0);
    assertEquals(question.getDescription(), actualQuestion.getDescription());

    Response actualResponse = actualQuestion.getResponses().get(0);
    assertEquals(1, actualQuestion.getResponses().size());
    assertEquals(response.getText(), actualResponse.getText());
  }

  private List<Environment> getEnvironments(String token) throws Exception {
    String response = getBase(urlPath, token);

    CollectionType typeReference =
            TypeFactory.defaultInstance().constructCollectionType(List.class, Environment.class);

    return mapper.readValue(response, typeReference);
  }
}