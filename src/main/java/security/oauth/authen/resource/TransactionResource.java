package security.oauth.authen.resource;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import security.oauth.authen.domain.BaseResponse;
import security.oauth.authen.domain.BaseRestApi;
import security.oauth.authen.dto.ProfileDTO;
import security.oauth.authen.entity.DateAndTime;
import security.oauth.authen.entity.Users;
import security.oauth.authen.repository.DateAndTimeRepository;
import security.oauth.authen.repository.UserInfoRepository;
import security.oauth.authen.repository.UserRepository;



@RestController
@PreAuthorize("hasRole('ROLE_ADMIN')")
@RequestMapping("/admin")
public class TransactionResource {

	@Autowired
	private UserInfoRepository userinfoRepository;
	
	@Autowired
	private UserRepository userrepository;
	
	@Autowired
	private DateAndTimeRepository dateAndTimeRepository;
	
	@GetMapping("/findtransaction_all_true")
	public BaseRestApi findAllAccess(){
		  BaseRestApi brapi = new BaseRestApi();
          BaseResponse<List<ProfileDTO>> resp = new BaseResponse<List<ProfileDTO>>();
          List<Users> list = userrepository.findByEnabled(true);
          List<ProfileDTO> listreturn = new ArrayList<>();
          for(Users userinfo:list){
        	  ProfileDTO profile = new ProfileDTO();
//        	  profile.setFirstName(userinfo.getUserinfo().getFirstName());
//        	  profile.setLastName(userinfo.getUserinfo().getLastName());
        	  profile.setUsername(userinfo.getUsername());
        	  profile.setPhone(userinfo.getUserinfo().getPhone());
//        	  profile.setAddress(userinfo.getUserinfo().getAddress());
        	  listreturn.add(profile);
          }
          resp.setData(listreturn);
          brapi.setResponse(resp);
          brapi.setSuccess(true);
          return brapi;
	}
	
	@GetMapping("/findtransaction_all_false")
	public BaseRestApi findAllNotAccess(){
		  BaseRestApi brapi = new BaseRestApi();
          BaseResponse<List<ProfileDTO>> resp = new BaseResponse<List<ProfileDTO>>();
          List<Users> list = userrepository.findByEnabled(false);
          List<ProfileDTO> listreturn = new ArrayList<>();
          for(Users userinfo:list){
        	  ProfileDTO profile = new ProfileDTO();
//        	  profile.setFirstName(userinfo.getUserinfo().getFirstName());
//        	  profile.setLastName(userinfo.getUserinfo().getLastName());
        	  profile.setUsername(userinfo.getUsername());
        	  profile.setPhone(userinfo.getUserinfo().getPhone());
//        	  profile.setAddress(userinfo.getUserinfo().getAddress());
        	  listreturn.add(profile);
          }
          resp.setData(listreturn);
          brapi.setResponse(resp);
          brapi.setSuccess(true);
          return brapi;
	}
	
	@GetMapping("/findtransaction_today")
	public BaseRestApi findTransactionToday(){
		  BaseRestApi brapi = new BaseRestApi();
          BaseResponse<List<ProfileDTO>> resp = new BaseResponse<List<ProfileDTO>>();
          List<DateAndTime> list = dateAndTimeRepository.findUserToday();
          List<ProfileDTO> listreturn = new ArrayList<>();
          for(DateAndTime userinfo:list){
        	  if(userinfo.getUserinfo().getUsers().isEnabled()){
        	  ProfileDTO profile = new ProfileDTO();
//        	  profile.setFirstName(userinfo.getUserinfo().getFirstName());
//        	  profile.setLastName(userinfo.getUserinfo().getLastName());
        	  profile.setUsername(userinfo.getUserinfo().getUsers().getUsername());
        	  profile.setPhone(userinfo.getUserinfo().getPhone());
//        	  profile.setAddress(userinfo.getUserinfo().getAddress());
        	  listreturn.add(profile);
        	  }
          }
          resp.setData(listreturn);
          brapi.setResponse(resp);
          brapi.setSuccess(true);
          return brapi;
	}
}
