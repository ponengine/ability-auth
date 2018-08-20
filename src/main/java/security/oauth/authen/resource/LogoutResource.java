package security.oauth.authen.resource;

import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.oauth2.common.OAuth2AccessToken;
import org.springframework.security.oauth2.provider.token.TokenStore;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import security.oauth.authen.domain.BaseResponse;
import security.oauth.authen.domain.BaseRestApi;
import security.oauth.authen.dto.LoginDTO;
import security.oauth.authen.entity.Users;
import security.oauth.authen.repository.UserRepository;
import security.oauth.authen.util.Helper;



@RestController
@PreAuthorize("hasRole('ROLE_USER') or hasRole('ROLE_ADMIN')")
@RequestMapping("/api/logoutresource")
public class LogoutResource {

    @Autowired
    private TokenStore tokenStore;
    
    @Autowired
    private UserRepository userrepository;

    @RequestMapping(value = "/logout", method = RequestMethod.GET)
    public BaseRestApi logout(HttpServletRequest request,LoginDTO logindto) {
        BaseRestApi brapi = new BaseRestApi();
        BaseResponse<Map<String, Object>> resp = new BaseResponse<Map<String, Object>>();
        String authHeader = request.getHeader("Authorization");
        if (authHeader != null) {
            String tokenValue = authHeader.replace("Bearer", "").trim();
            OAuth2AccessToken accessToken = tokenStore.readAccessToken(tokenValue);
            tokenStore.removeAccessToken(accessToken);
//            Users user = userrepository.findByUsername(logindto.getUsername());
            Users user = Helper.currentUser(request);
            user.setToken(null);
            userrepository.save(user);
            brapi.setSuccess(true);
            return brapi;
        } else {
            resp.setErrorMessage(Helper.getMessage("token_error"));
            brapi.setResponse(resp);
            brapi.setSuccess(false);
            return brapi;
        }
    }

}
