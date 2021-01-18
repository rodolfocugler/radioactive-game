package br.com.rodolfocugler.controllers;

import br.com.rodolfocugler.domains.*;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.DirtiesContext;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertNotNull;

@SpringBootTest
@AutoConfigureMockMvc
@DirtiesContext(classMode = DirtiesContext.ClassMode.BEFORE_EACH_TEST_METHOD)
class ReportControllerTest extends BaseControllerTest {
  private static String urlPath = "/api/report";

  @Test
  public void shouldReturnSimpleReport() throws Exception {
    String firstUserToken = getToken();

    Environment environment1 = Environment.builder()
            .id(1).name("name1").description("description1").icon("icon").build();
    Environment environment2 = Environment.builder()
            .id(2).name("name2").description("description2").icon("icon").build();

    postBase(environment1, "/api/environments", firstUserToken);
    postBase(environment2, "/api/environments", firstUserToken);

    Account studentAccount = Account.builder().id(2).email("email2@email2.com").name("name2").number("12345")
            .password("12345@2021").environment(environment1).build();
    addAccount(studentAccount);
    String studentToken = getToken(studentAccount);

    List<Account> accounts = new ArrayList() {{
      add(studentAccount);
    }};

    AccountGroup group = AccountGroup.builder().id(1).accounts(accounts).name("name").build();
    postBase(group, "/api/accountGroups", firstUserToken);

    Question question = Question.builder().description("description").id(1)
            .environment(environment1).build();
    Response response = Response.builder().question(question).text("text").build();
    postBase(question, "/api/questions", firstUserToken);
    postBase(response, "/api/responses", firstUserToken);
    postBase(response, "/api/responses", studentToken);

    Transport transport = Transport.builder().accounts(accounts).accountGroup(group).carIndex(1)
            .fromEnvironment(environment1).toEnvironment(environment2).tools(new ArrayList<>())
            .build();
    postBase(transport, "/api/transports", studentToken);

    ChatMessage msg = ChatMessage.builder().text("text").build();
    postBase(msg, "/api/chatMessages", studentToken);

    String httpResponse = getBase(urlPath + "/getByAccountGroupId/1", firstUserToken);
    assertNotNull(httpResponse);
  }
}