package security.oauth.authen.resource;

import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import security.oauth.authen.domain.BaseResponse;
import security.oauth.authen.domain.BaseRestApi;
import security.oauth.authen.dto.ProfileDTO;
import security.oauth.authen.entity.UserInfo;
import security.oauth.authen.entity.Users;
import security.oauth.authen.repository.UserRepository;
import security.oauth.authen.util.Helper;

@RestController
@RequestMapping("/api/profile")
public class ProfileResource {
	@Autowired
	private UserRepository userRepository;
	
	@PostMapping("/updateprofile")
	public BaseRestApi updateProfile(ProfileDTO profile){
		
		BaseRestApi brapi = new BaseRestApi();
		BaseResponse<Map<String, Object>> resp = new BaseResponse<Map<String, Object>>();
		Users user = userRepository.findByUsername(profile.getUsername());
		 if(user==null){
             brapi.setSuccess(false);
             resp.setErrorMessage(Helper.getMessage("user_not_found"));
             brapi.setResponse(resp);
            return brapi;
            }
		if(profile.getFirstName().length()!=0){
			user.getUserinfo().setFirstName(profile.getFirstName());
		}
		if(profile.getLastName().length()!=0){
			user.getUserinfo().setLastName(profile.getLastName());
		}
		if(profile.getPhone().length()!=0){
			user.getUserinfo().setPhone(profile.getPhone());
		}
		userRepository.save(user);
		 brapi.setSuccess(true);
		 return brapi;
	}
	@GetMapping("/getprofile")
	public BaseRestApi getProfile(HttpServletRequest request){
		BaseRestApi brapi = new BaseRestApi();
		BaseResponse<ProfileDTO> resp = new BaseResponse<ProfileDTO>();
		Users user = Helper.currentUser(request);
		 if(user==null){
             brapi.setSuccess(false);
             resp.setErrorMessage(Helper.getMessage("user_not_found"));
             brapi.setResponse(resp);
            return brapi;
            }
		 ProfileDTO profile = new ProfileDTO();
		 profile.setFirstName(user.getUserinfo().getFirstName());
		 profile.setLastName(user.getUserinfo().getLastName());
		 profile.setUsername(user.getUsername());
		 profile.setPhone(user.getUserinfo().getPhone());
   	  	 profile.setAddress(user.getUserinfo().getAddress());
   	  	 
		 resp.setData(profile);
		 brapi.setResponse(resp);
		 brapi.setSuccess(true);
		 return brapi;
		 
	}
}
