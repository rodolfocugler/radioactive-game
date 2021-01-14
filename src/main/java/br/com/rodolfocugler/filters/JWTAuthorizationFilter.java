package br.com.rodolfocugler.filters;

import br.com.rodolfocugler.domains.Account;
import br.com.rodolfocugler.domains.AccountGroup;
import br.com.rodolfocugler.domains.Environment;
import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.interfaces.DecodedJWT;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.www.BasicAuthenticationFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.ArrayList;

import static br.com.rodolfocugler.configs.AuthenticationConfig.*;
import static java.lang.Long.parseLong;

public class JWTAuthorizationFilter extends BasicAuthenticationFilter {

  public JWTAuthorizationFilter(AuthenticationManager authManager) {
    super(authManager);
  }

  @Override
  protected void doFilterInternal(HttpServletRequest req,
                                  HttpServletResponse res,
                                  FilterChain chain) throws IOException, ServletException {
    String header = req.getHeader(HEADER_STRING);

    if (header == null || !header.startsWith(TOKEN_PREFIX)) {
      chain.doFilter(req, res);
      return;
    }

    UsernamePasswordAuthenticationToken authentication = getAuthentication(req);

    SecurityContextHolder.getContext().setAuthentication(authentication);
    chain.doFilter(req, res);
  }

  private UsernamePasswordAuthenticationToken getAuthentication(HttpServletRequest request) {
    String token = request.getHeader(HEADER_STRING);

    if (token == null) {
      return null;
    }

    try {
      DecodedJWT decodedJWT = JWT.require(Algorithm.HMAC512(SECRET.getBytes()))
              .build()
              .verify(token.replace(TOKEN_PREFIX, ""));

      Account account = Account.builder().id(parseLong(decodedJWT.getSubject()))
              .name(decodedJWT.getClaim("user").asString())
              .email(decodedJWT.getAudience().get(0))
              .environment(Environment.builder().id(decodedJWT.getClaim("environment").asLong())
                      .build())
              .accountGroup(AccountGroup.builder().id(decodedJWT.getClaim("group").asLong())
                      .build())
              .isLeader(decodedJWT.getClaim("isLeader").asBoolean())
              .build();

      if (account != null) {
        return new UsernamePasswordAuthenticationToken(account, null, new ArrayList<>());
      }
    } catch (Exception ex) {
      return null;
    }

    return null;
  }
}