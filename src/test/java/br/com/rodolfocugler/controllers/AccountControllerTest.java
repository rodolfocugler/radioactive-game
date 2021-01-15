package br.com.rodolfocugler.controllers;

import br.com.rodolfocugler.domains.Account;
import br.com.rodolfocugler.domains.Environment;
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
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
@DirtiesContext(classMode = DirtiesContext.ClassMode.BEFORE_EACH_TEST_METHOD)
class AccountControllerTest extends BaseControllerTest {

  private static String urlPath = "/api/accounts";

  @Test
  public void shouldReturn200OkWhenAddAnAccount() throws Exception {
    Account account = Account.builder()
            .email("email@email.com")
            .name("name")
            .number("12345")
            .build();

    addAccount(account)
            .andExpect(status().isOk());
  }

  @Test
  public void shouldReturn403WhenRequestHasNoToken() throws Exception {
    mockMvc.perform(get(urlPath))
            .andExpect(status().isForbidden());
  }

  @Test
  public void shouldReturnOneAccountAfterSaveOneAccount() throws Exception {
    Account account = Account.builder()
            .email("email@email.com")
            .name("name")
            .number("12345")
            .password("12345@2021")
            .build();

    addAccount(account);

    String token = getToken(account);

    List<Account> accounts = getAccounts(token);

    assertEquals(1, accounts.size());
    assertEquals(account.getEmail(), accounts.get(0).getEmail());
    assertEquals(account.getName(), accounts.get(0).getName());
  }

  @Test
  public void shouldReturnOneAccountAfterSaveAndEditOneAccount() throws Exception {
    Account account = Account.builder()
            .email("email2@email2.com")
            .name("name2")
            .number("123456")
            .password("12345@2021")
            .build();

    String token = getToken();

    mockMvc.perform(put(urlPath + "/1").contentType(APPLICATION_JSON_VALUE)
            .content(mapper.writeValueAsString(account))
            .header(HEADER_STRING, token))
            .andExpect(status().isOk())
            .andReturn()
            .getResponse()
            .getContentAsString();

    List<Account> accounts = getAccounts(token);

    assertEquals(1, accounts.size());
    assertEquals(account.getEmail(), accounts.get(0).getEmail());
    assertEquals(account.getName(), accounts.get(0).getName());
  }

  @Test
  public void shouldReturn403ForAInvalidToken() throws Exception {
    String token = getToken();
    token = token.replaceFirst(".$", "B");

    mockMvc.perform(get(urlPath)
            .header(HEADER_STRING, token))
            .andExpect(status().isForbidden());
  }


  private List<Account> getAccounts(String token) throws Exception {
    String response = getBase(urlPath, token);

    CollectionType typeReference =
            TypeFactory.defaultInstance().constructCollectionType(List.class, Account.class);

    return mapper.readValue(response, typeReference);
  }
}