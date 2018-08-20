package security.oauth.authen.resource;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

import javax.servlet.ServletRequest;
import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;

import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.fasterxml.jackson.core.JsonProcessingException;

import security.oauth.authen.constant.UserType;
import security.oauth.authen.domain.BaseResponse;
import security.oauth.authen.domain.BaseRestApi;
import security.oauth.authen.dto.LoginDTO;
import security.oauth.authen.dto.ProfileDTO;
import security.oauth.authen.entity.Authorities;
import security.oauth.authen.entity.PhoneAuthen;
import security.oauth.authen.entity.UserInfo;
import security.oauth.authen.entity.Users;
import security.oauth.authen.model.Email;
import security.oauth.authen.repository.PhoneAuthenRepository;
import security.oauth.authen.repository.UserInfoRepository;
import security.oauth.authen.repository.UserRepository;
import security.oauth.authen.service.EmailService;
import security.oauth.authen.service.UserService;
import security.oauth.authen.template.EmailTemplate;
import security.oauth.authen.util.Helper;

import java.security.SecureRandom;
import java.sql.Timestamp;
import java.time.LocalDate;
import java.time.LocalTime;

@RestController
@RequestMapping("/api/resource")
public class LoginResource {

    @Autowired
    private UserService userservice;

    @Autowired
    private UserRepository userRepository;
    
    @Autowired
    private PhoneAuthenRepository phoneauthenrepository;

    @Autowired
    private UserInfoRepository userinforepository;

    @Autowired
    private BCryptPasswordEncoder bCryptPasswordEncoder;

    @Autowired
    private EmailService emailService;

//    @Autowired
//    private RecaptchaService recaptchaservice;

    @Autowired
    private Environment env;

    @GetMapping("/message")
    public String message(HttpServletRequest request) {
        return Helper.getMessage("token_error");
    }
    @PostMapping("/login")
    public BaseRestApi gettoken(@RequestBody LoginDTO loginDTO) throws JsonProcessingException {
        BaseRestApi brapi = new BaseRestApi();
        BaseResponse<Map<String, String>> resp = new BaseResponse<Map<String, String>>();
        Users user = userRepository.findByUsername(loginDTO.getUsername());
        if (user == null) {
            brapi.setSuccess(false);
            resp.setErrorMessage(Helper.getMessage("user_not_found"));
            brapi.setResponse(resp);
            return brapi;
        }
        if (!bCryptPasswordEncoder.matches(loginDTO.getPassword(), user.getPassword())) {
            brapi.setSuccess(false);
            resp.setErrorMessage(Helper.getMessage("password_not_true"));
            brapi.setResponse(resp);
            return brapi;
        }
        if(!user.getUserinfo().getUserType().equals("ACTIVE")){
        	 brapi.setSuccess(false);
             resp.setErrorMessage(Helper.getMessage("user_not_active"));
             brapi.setResponse(resp);
             return brapi;
        }

        String token = userservice.getToken(loginDTO.getUsername(), loginDTO.getPassword()).getValue();
        Map<String, String> gettoken = new HashMap<String, String>();
        gettoken.put("token", "Bearer " + token);
        gettoken.put("role", user.getUserinfo().getAuthorities().getAuthority());
        gettoken.put("username", user.getUsername());
        resp.setData(gettoken);
        user.setToken("Bearer " + token);
        userRepository.save(user);
        brapi.setResponse(resp);
        brapi.setSuccess(true);
        return brapi;
    }
    @PostMapping("/genotp")
    public BaseRestApi genByPhone(@RequestBody ProfileDTO phoneprofile){
    	 BaseRestApi brapi = new BaseRestApi();
    	 BaseResponse<Map<String, Object>> resp = new BaseResponse<Map<String, Object>>();
    	 LocalDate ld = LocalDate.now();
         LocalTime lt = LocalTime.now().plusMinutes(10);
    	 PhoneAuthen phoneauthen = new PhoneAuthen();
    	 PhoneAuthen checkphone =phoneauthenrepository.findByPhone(phoneprofile.getPhone());
    	 if(checkphone!=null){
         if(!checkphone.isEnabled()){
        	 checkphone.setPhone(phoneprofile.getPhone());
        	 checkphone.setTimeExpire(lt);
        	 checkphone.setDateExpire(ld);
        	 checkphone.setRandomNum(userservice.randomString(6));
             phoneauthenrepository.save(checkphone);
             brapi.setSuccess(true);
             return brapi;
             }
             if(checkphone.isEnabled()){
            	 brapi.setSuccess(false);
                 resp.setErrorMessage(Helper.getMessage("user_already_in_system"));
                 brapi.setResponse(resp);
                 return brapi;
         }
             }
         phoneauthen.setPhone(phoneprofile.getPhone());
         phoneauthen.setTimeExpire(lt);
         phoneauthen.setDateExpire(ld);
         phoneauthen.setRandomNum(userservice.randomString(6));
         phoneauthenrepository.save(phoneauthen);
//         phoneauthenrepository.insertPhone(phoneprofile.getPhoneNumber(), userservice.randomString(6), ld, lt);
         brapi.setSuccess(true);
         return brapi;
    }
    @PostMapping("/checkotp")
    public BaseRestApi checkotp(@RequestBody ProfileDTO phoneprofile){
    	Map<String, Object> model = new HashMap<>();
    	BaseRestApi brapi = new BaseRestApi();
        BaseResponse<Map<String, Object>> resp = new BaseResponse<Map<String, Object>>();
        if(phoneprofile.getOtp().isEmpty()){
        	resp.setErrorMessage(Helper.getMessage("otp_message"));
        	brapi.setResponse(resp);
        	brapi.setSuccess(false);	
        	return brapi;
        }
        PhoneAuthen checkphone =phoneauthenrepository.findByPhone(phoneprofile.getPhone());
        if(checkphone==null){
        	resp.setErrorMessage(Helper.getMessage("user_not_found"));
        	brapi.setResponse(resp);
        	brapi.setSuccess(false);	
        	return brapi;
        }

        PhoneAuthen phoneauthen =phoneauthenrepository.findByPhoneAndRandomNum(phoneprofile.getPhone(),phoneprofile.getOtp());
        if(phoneauthen==null){
        	resp.setErrorMessage(Helper.getMessage("otp_recheck"));
        	brapi.setResponse(resp);
        	brapi.setSuccess(false);	
        	return brapi;
        }
        phoneauthen.setEnabled(true);
        phoneauthenrepository.save(phoneauthen);
        UserInfo userinfo = new UserInfo();
        userinfo.setPhone(phoneprofile.getPhone());
        userinforepository.save(userinfo);
        model.put("phone" ,userinfo.getPhone());
        resp.setData(model);
        brapi.setResponse(resp);
        brapi.setSuccess(true);	
        return brapi;
    }
    @RequestMapping(value = "/registration", method = RequestMethod.POST)
    //,@RequestParam(name="g-recaptcha-response") String recaptchaResponse
    public BaseRestApi createNewUser(@RequestBody @Valid ProfileDTO profile, BindingResult bindingResult, ServletRequest request) {
//		String ip = request.getRemoteAddr();
//		  String captchaVerifyMessage = 
//				  recaptchaservice.verifyRecaptcha(ip, recaptchaResponse);
//		  if ( StringUtils.isNotEmpty(captchaVerifyMessage)) {
//		    Map<String, Object> response = new HashMap<>();
//		    response.put("message", captchaVerifyMessage);
//		    System.out.println("Error");
//		  }
    	 LocalDate ld = LocalDate.now();
         LocalTime lt = LocalTime.now();
        BaseRestApi brapi = new BaseRestApi();
        BaseResponse<Map<String, Object>> resp = new BaseResponse<Map<String, Object>>();
        String uri = env.getProperty("base.wallet") + "/user/add";
      
        Users userExists = userRepository.findByUsername(profile.getUsername());
        if (userExists != null) {
            brapi.setSuccess(false);
            resp.setErrorMessage(Helper.getMessage("user_already_in_system"));
            brapi.setResponse(resp);
            return brapi;

        }
   
        UserInfo userregis=userinforepository.findByPhone(profile.getPhone());

        Users setUsers = new Users();
        setUsers.setUsername(profile.getUsername());
        setUsers.setPassword(bCryptPasswordEncoder.encode(profile.getPassword()));
        setUsers.setEnabled(true);
        userregis.setUsers(setUsers);
        //userinfo.getUsers().setConfirmationToken(UUID.randomUUID().toString());
        userregis.setCreateDate(ld);
        userregis.setCreateTime(lt);
        userregis.setUserType(UserType.ACTIVE.toString());
        Authorities ar = new Authorities();
        ar.setAuthority("ROLE_USER");
        ar.setUsername(profile.getUsername());
        userregis.setAuthorities(ar);

        userinforepository.save(userregis);

//        String appUrl = request.getScheme() + "://" + request.getServerName() + ":" + request.getLocalPort() + "/api/login/confirm?token=" + userinfo.getUsers().getConfirmationToken();
//        String from = "OreoMaster";
//        String to = userinfo.getUsers().getUsername();
//        String subject = "Meeting Oreo";
//        EmailTemplate template = new EmailTemplate("email.html");
//        Map<String, String> replacements = new HashMap<String, String>();
//        replacements.put("user", userinfo.getUsers().getUsername());
//        replacements.put("today", String.valueOf(new Date()));
//        .put("verify", appUrl);
//        String message = template.getTemplate(replacements);

//        Email email = new Email(from, to, subject, message);
//        email.setHtml(true);
//        emailService.send(email);
        RestTemplate rt = new RestTemplate();
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
//        JSONObject obj = new JSONObject();
//        obj.put("username", userinfo.getUsers().getUsername());
//        obj.put("phone", userinfo.getPhoneNumber());
//        HttpEntity<String> entity = new HttpEntity<String>(obj.toString(), headers);
//		BaseRestApi response = rt.postForObject( uri, entity , BaseRestApi.class );
//		if(!response.isSuccess()){
//        return brapi;
//        } 
        brapi.setSuccess(true);
        return brapi;
    }

    @RequestMapping(value = "/confirm", method = RequestMethod.GET)
    public BaseRestApi confirmRegistration(@RequestParam("token") String token) {
        BaseRestApi brapi = new BaseRestApi();
        BaseResponse<Map<String, Object>> resp = new BaseResponse<Map<String, Object>>();
        Users tokencheck = userRepository.findByConfirmationToken(token);
        if (tokencheck == null) {
            resp.setErrorMessage(Helper.getMessage("token_error"));
            brapi.setResponse(resp);
            return brapi;
        }
        brapi.setSuccess(true);
        return brapi;
    }

    @RequestMapping(value = "/confirmafter/checktoken", method = RequestMethod.POST)
    public BaseRestApi confirmRegistrationPost(@RequestBody LoginDTO logindto, BindingResult bindingResult, @RequestParam("token") String token, RedirectAttributes redir) {
        BaseRestApi brapi = new BaseRestApi();
        BaseResponse<Map<String, Object>> resp = new BaseResponse<Map<String, Object>>();
        Users user = userRepository.findByConfirmationToken(token);
        user.setEnabled(true);
        user.getUserinfo().setUserType(UserType.ACTIVE.toString());
        userRepository.save(user);
        brapi.setSuccess(true);
        return brapi;
    }

}
