package tk.erdmko.users.service;


import org.springframework.http.ResponseEntity;
import org.springframework.security.oauth2.common.OAuth2AccessToken;
import org.springframework.web.HttpRequestMethodNotSupportedException;

public interface SecurityService {
    String findLoggedInUsername();

    void autoLogin(String username, String password);
    ResponseEntity<OAuth2AccessToken> autoLoginToken(String username, String password) throws HttpRequestMethodNotSupportedException;
}
