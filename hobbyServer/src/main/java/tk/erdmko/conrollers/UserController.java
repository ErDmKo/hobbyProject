package tk.erdmko.conrollers;

import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.security.oauth2.common.OAuth2AccessToken;
import org.springframework.security.oauth2.provider.endpoint.TokenEndpoint;
import org.springframework.security.oauth2.provider.token.AuthorizationServerTokenServices;
import org.springframework.security.oauth2.provider.token.DefaultTokenServices;
import org.springframework.security.oauth2.provider.token.TokenStore;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.HttpRequestMethodNotSupportedException;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import java.util.Collection;

import javax.validation.Valid;

import tk.erdmko.models.OkResponse;
import tk.erdmko.models.SimpleJsonResponse;
import tk.erdmko.users.model.User;
import tk.erdmko.users.service.SecurityService;
import tk.erdmko.users.service.UserService;

@Validated
@RestController
public class UserController {

    private static final transient org.slf4j.Logger LOG = LoggerFactory.getLogger(UserController.class.getName());

    @Autowired
    private SecurityService securityService;

    @Autowired
    private UserService userService;


    @Value("${security.jwt.client-id}")
    private String clientId;


    @RequestMapping(value = "/users/register", method = RequestMethod.POST)
    public ResponseEntity<OAuth2AccessToken>  registration(@Valid @RequestBody User input) throws HttpRequestMethodNotSupportedException {
        input.setEnabled(true);
        String pass = input.getPassword();
        userService.save(input);
        ResponseEntity<OAuth2AccessToken> info = securityService.autoLoginToken(input.getUsername(), pass);
        if (info == null) {
            return null;
        }
        return info;
    }
    @RequestMapping(value = "/users/authenticate", method = RequestMethod.POST)
    public ResponseEntity<OAuth2AccessToken>  auth(@Valid @RequestBody User input) throws HttpRequestMethodNotSupportedException {
        return securityService.autoLoginToken(input.getUsername(), input.getPassword());
    }

}
