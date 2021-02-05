package br.com.rodolfocugler.filters;

import br.com.rodolfocugler.dtos.UserDTO;
import com.auth0.jwt.JWT;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import javax.servlet.FilterChain;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Date;

import static br.com.rodolfocugler.configs.AuthenticationConfig.*;
import static com.auth0.jwt.algorithms.Algorithm.HMAC512;
import static java.lang.Boolean.parseBoolean;
import static java.lang.Integer.parseInt;
import static java.lang.Long.parseLong;

public class JWTAuthenticationFilter extends UsernamePasswordAuthenticationFilter {
  private AuthenticationManager authenticationManager;

  public JWTAuthenticationFilter(AuthenticationManager authenticationManager) {
    this.authenticationManager = authenticationManager;
  }

  @Override
  public Authentication attemptAuthentication(HttpServletRequest req,
                                              HttpServletResponse res) throws AuthenticationException {
    try {
      UserDTO creds = new ObjectMapper()
              .readValue(req.getInputStream(), UserDTO.class);

      return authenticationManager.authenticate(
              new UsernamePasswordAuthenticationToken(
                      creds.getEmail().trim(),
                      creds.getPassword().trim(),
                      new ArrayList<>())
      );
    } catch (IOException e) {
      throw new RuntimeException(e);
    }
  }

  @Override
  protected void successfulAuthentication(HttpServletRequest req,
                                          HttpServletResponse res,
                                          FilterChain chain,
                                          Authentication auth) {

    String username = ((User) auth.getPrincipal()).getUsername();
    String[] claims = username.split(";");
    String email = claims[0];
    String id = claims[1];
    String name = claims[2];
    String environment = claims[3];
    String group = claims[4];
    String isLeader = claims[5];

    String token = JWT.create()
            .withSubject(id)
            .withAudience(email)
            .withClaim("user", name)
            .withClaim("environment", parseLong(environment))
            .withClaim("group", parseLong(group))
            .withClaim("isLeader", parseBoolean(isLeader))
            .withExpiresAt(new Date(System.currentTimeMillis() + EXPIRATION_TIME))
            .sign(HMAC512(SECRET.getBytes()));
    res.addHeader(HEADER_STRING, TOKEN_PREFIX + token);
  }
}
