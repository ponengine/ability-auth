package security.oauth.authen.resource;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import security.oauth.authen.domain.BaseResponse;
import security.oauth.authen.domain.BaseRestApi;
import security.oauth.authen.dto.ProfileDTO;
import security.oauth.authen.entity.UserInfo;
import security.oauth.authen.entity.Users;
import security.oauth.authen.repository.UserInfoRepository;
import security.oauth.authen.repository.UserRepository;
import security.oauth.authen.util.Helper;



@RestController
//@PreAuthorize("hasRole('ROLE_ADMIN')")
@RequestMapping("/admin")
public class TransactionResource {

	@Autowired
	private UserInfoRepository userinfoRepository;
	
	@Autowired
	private UserRepository userrepository;

	
	@PostMapping("/findtransaction/user")
	public BaseRestApi findAll(@RequestBody UserInfo userinfo){//get type typetran
		  BaseRestApi brapi = new BaseRestApi();
          BaseResponse<List<ProfileDTO>> resp = new BaseResponse<List<ProfileDTO>>();
          List<UserInfo> list = new ArrayList<>();
          if("today".equalsIgnoreCase(userinfo.getTypetran())){
        	  list = userinfoRepository.findByUserTypeToday(userinfo.getUserType());
          }else{
        	  list = userinfoRepository.findByUserType(userinfo.getUserType());
          }
          
          List<ProfileDTO> listreturn = new ArrayList<>();
          for(UserInfo user:list){
        	  ProfileDTO profile = new ProfileDTO();
        	  profile.setUsername(user.getUsers().getUsername());
        	  profile.setPhone(user.getPhone());
        	  profile.setRole(user.getAuthorities().getAuthority());
        	  listreturn.add(profile);
          }
          resp.setData(listreturn);
          brapi.setResponse(resp);
          brapi.setSuccess(true);
          return brapi;
	}
	
	
	@PostMapping("/findtransactionuserone")
	public BaseRestApi findOnePhone(@RequestBody ProfileDTO profledto){ //username phone
		  BaseRestApi brapi = new BaseRestApi();
          BaseResponse<Map<String, Object>> resp = new BaseResponse<Map<String, Object>>();
          UserInfo userinfo = new UserInfo();
          if(null!=profledto.getPhone()){
        	  userinfo= userinfoRepository.findByPhone(profledto.getPhone());
          }else if(null!=profledto.getUsername()){
        	  Users user = userrepository.findByUsername(profledto.getUsername());
        	  userinfo=user.getUserinfo();
          }else{
        	  brapi.setSuccess(false);
        	  resp.setErrorMessage(Helper.getMessage("user_not_found"));
        	  brapi.setResponse(resp);
        	  return brapi;
          }
          
          Map<String, Object> model =  new HashMap<>();
        	  ProfileDTO profile = new ProfileDTO();
        	  profile.setUsername(userinfo.getUsers().getUsername());
        	  profile.setPhone(userinfo.getPhone());
        	  profile.setRole(userinfo.getAuthorities().getAuthority());
        	  model.put("user", profile);
          resp.setData(model);
          brapi.setResponse(resp);
          brapi.setSuccess(true);
          return brapi;
	}
	
	

}
