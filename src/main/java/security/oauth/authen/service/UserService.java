package security.oauth.authen.service;


import java.security.SecureRandom;
import java.util.Base64;
import java.util.HashMap;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.security.oauth2.common.OAuth2AccessToken;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import security.oauth.authen.repository.UserRepository;

import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;



@Service
public class UserService {
	static String CLIENT_ID = "client";
    static String SECRET = "secret";
    static final String AB = "0123456789ABCDEFGHIJKLMNOPQRSTUVWXYZ";
	static SecureRandom rnd = new SecureRandom();
    @Autowired
    private Environment env;
     
    public OAuth2AccessToken getToken(String username, String password) {
        RestTemplate rest = new RestTemplate();
		Map<String,Object> uri = new HashMap<String,Object>();
		HttpEntity<String> entity = new HttpEntity<String>(getHeaders(CLIENT_ID,SECRET));
	    ResponseEntity<OAuth2AccessToken> response = rest.exchange(env.getProperty("base.uri")+"?username="+username+"&password="+password+"&grant_type=password",HttpMethod.POST,entity,OAuth2AccessToken.class,uri);
		return response.getBody();
    }

    private HttpHeaders getHeaders(String user,String password) {
		String encodedAuthorization = Base64.getEncoder().encodeToString(user.concat(":").concat(password).getBytes());
		HttpHeaders httpHeaders = new HttpHeaders();
		httpHeaders.add("Content-Type","application/x-www-form-urlencoded");
		httpHeaders.add(HttpHeaders.AUTHORIZATION, "Basic " + encodedAuthorization);
		return httpHeaders;
	}
    
	public String randomString( int len ){
		   StringBuilder sb = new StringBuilder( len );
		   for( int i = 0; i < len; i++ ) 
		      sb.append( AB.charAt( rnd.nextInt(AB.length()) ) );
		   return sb.toString();
		}
    
   
}
