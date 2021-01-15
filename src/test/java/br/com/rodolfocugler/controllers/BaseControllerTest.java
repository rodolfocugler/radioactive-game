package br.com.rodolfocugler.controllers;

import br.com.rodolfocugler.domains.Account;
import br.com.rodolfocugler.dtos.UserDTO;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;

import static br.com.rodolfocugler.configs.AuthenticationConfig.HEADER_STRING;
import static br.com.rodolfocugler.configs.AuthenticationConfig.SIGN_UP_URL;
import static org.springframework.http.MediaType.APPLICATION_JSON_VALUE;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

public class BaseControllerTest {

  @Autowired
  protected MockMvc mockMvc;

  protected static ObjectMapper mapper = new ObjectMapper();

  protected String getToken() throws Exception {
    Account account = Account.builder()
            .email("email@email.com")
            .name("name")
            .number("12345")
            .password("12345@2021")
            .build();

    addAccount(account);
    return getToken(account);
  }

  protected String getToken(Account account) throws Exception {
    UserDTO user = UserDTO.builder().email(account.getEmail()).password(account.getPassword()).build();

    String token = mockMvc.perform(post("/login").contentType(APPLICATION_JSON_VALUE)
            .content(mapper.writeValueAsString(user)))
            .andExpect(status().isOk())
            .andReturn()
            .getResponse()
            .getHeader(HEADER_STRING);
    return token;
  }

  protected ResultActions addAccount(Account account) throws Exception {
    return mockMvc.perform(post(SIGN_UP_URL)
            .contentType(APPLICATION_JSON_VALUE)
            .content(mapper.writeValueAsString(account)))
            .andExpect(status().isOk());
  }

  protected ResultActions postBase(Object object, String url, String token) throws Exception {
    return mockMvc.perform(post(url).contentType(APPLICATION_JSON_VALUE)
            .content(mapper.writeValueAsString(object))
            .header(HEADER_STRING, token))
            .andExpect(status().isOk());
  }

  protected String getBase(String urlPath, String token) throws Exception {
    return mockMvc.perform(get(urlPath)
            .header(HEADER_STRING, token))
            .andExpect(status().isOk())
            .andReturn()
            .getResponse()
            .getContentAsString();
  }
}
