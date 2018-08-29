package security.oauth.authen.resource;

import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.oauth2.common.OAuth2AccessToken;
import org.springframework.security.oauth2.provider.token.TokenStore;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import security.oauth.authen.constant.UserType;
import security.oauth.authen.domain.BaseResponse;
import security.oauth.authen.domain.BaseRestApi;
import security.oauth.authen.dto.LoginDTO;
import security.oauth.authen.dto.ProfileDTO;
import security.oauth.authen.entity.Users;
import security.oauth.authen.repository.UserRepository;
import security.oauth.authen.util.Helper;



@RestController
@RequestMapping("/admin")
@PreAuthorize("hasRole('ROLE_ADMIN')")
public class AdminResource {
	
	@Autowired
	private UserRepository userRepository;
	
	 @Autowired
	private TokenStore tokenStore;
	 
	@GetMapping("/message")
	public String msg(){
		return "This user";
	}
	@PostMapping("/changerole")
	public BaseRestApi adminChangeStatus(LoginDTO loginDTO){
		BaseRestApi brapi = new BaseRestApi();
		BaseResponse<Map<String, Object>> resp = new BaseResponse<Map<String, Object>>();
		Users user = userRepository.findByUsername(loginDTO.getUsername());
		 if(user==null){
             brapi.setSuccess(false);
             resp.setErrorMessage(Helper.getMessage("user_not_found"));
             brapi.setResponse(resp);
            return brapi;
            }
		 if(user.equals("ROLE_USER")){
			 user.getUserinfo().getAuthorities().setAuthority("ROLE_USER");
			 userRepository.save(user);
			 brapi.setSuccess(true);
            return brapi;
		 }else{
			 brapi.setSuccess(false);
             resp.setErrorMessage(Helper.getMessage("not_user_role"));
             brapi.setResponse(resp);
            return brapi;
		 }
	}
		@PostMapping("/setstatususer")
		public BaseRestApi setStatusUser(ProfileDTO profile){
			 BaseRestApi brapi = new BaseRestApi();
			 BaseResponse<Map<String, Object>> resp = new BaseResponse<Map<String, Object>>();
			 Users user = userRepository.findByUsername(profile.getUsername());
			 if(user==null){
	             brapi.setSuccess(false);
	             resp.setErrorMessage(Helper.getMessage("user_not_found"));
	             brapi.setResponse(resp);
	            return brapi;
	           }
			 String userType =profile.getUserType();
			 user.getUserinfo().setUserType(userType);
			 brapi.setSuccess(true);
			 return brapi;
		}
		
		@PostMapping("/destroytoken")
		public BaseRestApi destroytoken(LoginDTO logindto,HttpServletRequest request){
			 BaseRestApi brapi = new BaseRestApi();
			 BaseResponse<Map<String, Object>> resp = new BaseResponse<Map<String, Object>>();
			 Users user = userRepository.findByUsername(logindto.getUsername());
			 if(user==null){
	             brapi.setSuccess(false);
	             resp.setErrorMessage(Helper.getMessage("user_not_found"));
	             brapi.setResponse(resp);
	            return brapi;
	           }
			 String authHeader = request.getHeader("Authorization");
		        if (authHeader != null) {
		            String tokenValue = authHeader.replace("Bearer", "").trim();
		            OAuth2AccessToken accessToken = tokenStore.readAccessToken(tokenValue);
		            tokenStore.removeAccessToken(accessToken);
		            user.setToken(null);
		            userRepository.save(user);
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
