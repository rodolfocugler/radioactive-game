package br.com.rodolfocugler.controllers;

import br.com.rodolfocugler.domains.Account;
import br.com.rodolfocugler.dtos.UserDTO;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.type.CollectionType;
import com.fasterxml.jackson.databind.type.TypeFactory;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;

import java.util.List;

import static br.com.rodolfocugler.configs.AuthenticationConfig.HEADER_STRING;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.springframework.http.MediaType.APPLICATION_JSON_VALUE;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
@DirtiesContext(classMode = DirtiesContext.ClassMode.BEFORE_EACH_TEST_METHOD)
class AccountControllerTest {
  @Autowired
  private MockMvc mockMvc;

  private static ObjectMapper mapper = new ObjectMapper();
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
  public void shouldReturnOneAccountAfterSaveAndEditOneAccount() throws Exception {
    Account account = Account.builder()
            .email("email@email.com")
            .name("name")
            .number("12345")
            .build();

    addAccount(account);

    String token = getToken(account);

    List<Account> accounts = getAccounts(token);

    assertEquals(1, accounts.size());
    assertEquals(account.getEmail(), accounts.get(0).getEmail());
    assertEquals(account.getName(), accounts.get(0).getName());
  }

  //@Test
  public void shouldReturnOneAccountAfterSaveOneAccount() throws Exception {
    Account account = Account.builder()
            .email("email2@email2.com")
            .name("name2")
            .number("123456")
            .build();

    String token = getToken();

    mockMvc.perform(put("$urlPath/1").contentType(APPLICATION_JSON_VALUE)
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

  private String getToken() throws Exception {
    Account account = Account.builder()
            .email("email@email.com")
            .name("name")
            .number("12345")
            .build();

    addAccount(account);
    return getToken(account);
  }

  private String getToken(Account account) throws Exception {
    UserDTO user = UserDTO.builder().email(account.getEmail()).password(account.getNumber()).build();

    String token = mockMvc.perform(post("/login").contentType(APPLICATION_JSON_VALUE)
            .content(mapper.writeValueAsString(user)))
            .andExpect(status().isOk())
            .andReturn()
            .getResponse()
            .getHeader(HEADER_STRING);
    return token;
  }

  private ResultActions addAccount(Account account) throws Exception {
    return mockMvc.perform(post(urlPath)
            .contentType(APPLICATION_JSON_VALUE)
            .content(mapper.writeValueAsString(account)))
            .andExpect(status().isOk());
  }

  private List<Account> getAccounts(String token) throws Exception {
    String response = mockMvc.perform(get(urlPath)
            .header(HEADER_STRING, token))
            .andExpect(status().isOk())
            .andReturn()
            .getResponse()
            .getContentAsString();

    CollectionType typeReference =
            TypeFactory.defaultInstance().constructCollectionType(List.class, Account.class);

    return mapper.readValue(response, typeReference);
  }
}