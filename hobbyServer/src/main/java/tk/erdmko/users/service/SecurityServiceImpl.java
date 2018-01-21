package tk.erdmko.users.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.context.SecurityContextImpl;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.oauth2.common.OAuth2AccessToken;
import org.springframework.security.oauth2.provider.OAuth2Authentication;
import org.springframework.security.oauth2.provider.OAuth2Request;
import org.springframework.security.oauth2.provider.endpoint.TokenEndpoint;
import org.springframework.security.oauth2.provider.token.AuthorizationServerTokenServices;
import org.springframework.stereotype.Service;
import org.springframework.web.HttpRequestMethodNotSupportedException;

import java.io.Serializable;
import java.security.Principal;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Service
public class SecurityServiceImpl implements SecurityService  {

    @Autowired
    private AuthenticationManager authenticationManager;

    @Autowired
    private UserDetailsService userDetailsService;

    private TokenEndpoint tokenEndpoint;

    @Override
    public String findLoggedInUsername() {
        Object userDetails = SecurityContextHolder.getContext().getAuthentication().getDetails();
        if (userDetails instanceof UserDetails) {
            return ((UserDetails) userDetails).getUsername();
        }

        return null;
    }
    @Value("${security.jwt.client-id}")
    private String clientId;

    @Value("${security.jwt.client-secret}")
    private String clientSecret;

    @Value("${security.jwt.resource-ids}")
    private String resourceIds;

    @Autowired
    private AuthorizationServerTokenServices tokenServices;


    public ResponseEntity<OAuth2AccessToken> autoLoginToken(String username, String password) throws HttpRequestMethodNotSupportedException {
        ResponseEntity<OAuth2AccessToken> out = null;
        UserDetails userDetails = userDetailsService.loadUserByUsername(username);
        UsernamePasswordAuthenticationToken authenticationToken =
                new UsernamePasswordAuthenticationToken(userDetails, password, userDetails.getAuthorities());

        authenticationManager.authenticate(authenticationToken);

        if (authenticationToken.isAuthenticated()) {
            SecurityContext ctx = SecurityContextHolder.getContext();
            ctx.setAuthentication(authenticationToken);


            HashMap<String, String> parameters = new HashMap<String, String>();
            parameters.put("client_id", clientId);
            parameters.put("client_secret", clientSecret);
            parameters.put("grant_type", "password");
            parameters.put("password", username);
            parameters.put("scope", "read write");
            parameters.put("username", password);
            Set<String> responseTypes = new HashSet<String>();
            responseTypes.add("code");
            List<String> scopes = null;
            OAuth2Request oauth2Request = new OAuth2Request(
                    parameters, clientId,
                    new ArrayList<GrantedAuthority>(),
                    true,
                    null,
                    new HashSet<String>(Arrays.asList(resourceIds)),
                    null,
                    responseTypes,
                    null
            );
            OAuth2Authentication auth = new OAuth2Authentication(oauth2Request, authenticationToken);
            OAuth2AccessToken accessToken = tokenServices.createAccessToken(auth);
            HttpHeaders headers = new HttpHeaders();
            headers.set("Cache-Control", "no-store");
            headers.set("Pragma", "no-cache");
            out = new ResponseEntity<>(accessToken, headers, HttpStatus.OK);
        }
        return out;
    }

    @Override
    public void autoLogin(String username, String password) {
        UserDetails userDetails = userDetailsService.loadUserByUsername(username);
        UsernamePasswordAuthenticationToken authenticationToken =
                new UsernamePasswordAuthenticationToken(userDetails, password, userDetails.getAuthorities());

        authenticationManager.authenticate(authenticationToken);

        if (authenticationToken.isAuthenticated()) {
            SecurityContextHolder.getContext().setAuthentication(authenticationToken);
        }
    }
}
