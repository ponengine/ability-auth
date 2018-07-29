package security.oauth.authen.resource;

import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import security.oauth.authen.domain.BaseResponse;
import security.oauth.authen.domain.BaseRestApi;
import security.oauth.authen.dto.LoginDTO;
import security.oauth.authen.entity.Users;
import security.oauth.authen.repository.UserRepository;
import security.oauth.authen.util.Helper;



@RestController
@RequestMapping("/admin")
@PreAuthorize("hasRole('ROLE_ADMIN')")
public class AdminResource {
	
	@Autowired
	private UserRepository userRepository;
	
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
}
