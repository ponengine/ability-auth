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

import security.oauth.authen.domain.BaseResponse;
import security.oauth.authen.domain.BaseRestApi;
import security.oauth.authen.dto.LoginDTO;
import security.oauth.authen.entity.Authorities;
import security.oauth.authen.entity.DateAndTime;
import security.oauth.authen.entity.UserInfo;
import security.oauth.authen.entity.Users;
import security.oauth.authen.repository.UserInfoRepository;
import security.oauth.authen.repository.UserRepository;
import security.oauth.authen.service.UserService;
import security.oauth.authen.util.Helper;

import java.sql.Timestamp;

@RestController
@RequestMapping("/api/resource")
public class LoginResource {

    @Autowired
    private UserService userservice;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private UserInfoRepository userinforepository;

    @Autowired
    private BCryptPasswordEncoder bCryptPasswordEncoder;

//    @Autowired
//    private EmailService emailService;

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

        String token = userservice.getToken(loginDTO.getUsername(), loginDTO.getPassword()).getValue();
        Map<String, String> gettoken = new HashMap<String, String>();
        gettoken.put("token", "Bearer " + token);
        gettoken.put("role", user.getUserinfo().getAuthorities().getAuthority());
        gettoken.put("username", user.getUsername());
        resp.setData(gettoken);
        user.setToken(token);
        userRepository.save(user);
        brapi.setResponse(resp);
        brapi.setSuccess(true);
        return brapi;
    }

    @RequestMapping(value = "/registration", method = RequestMethod.POST)
    //,@RequestParam(name="g-recaptcha-response") String recaptchaResponse
    public BaseRestApi createNewUser(@RequestBody @Valid UserInfo userinfo, BindingResult bindingResult, ServletRequest request) {
//		String ip = request.getRemoteAddr();
//		  String captchaVerifyMessage = 
//				  recaptchaservice.verifyRecaptcha(ip, recaptchaResponse);
//		  if ( StringUtils.isNotEmpty(captchaVerifyMessage)) {
//		    Map<String, Object> response = new HashMap<>();
//		    response.put("message", captchaVerifyMessage);
//		    System.out.println("Error");
//		  }

        BaseRestApi brapi = new BaseRestApi();
        BaseResponse<Map<String, Object>> resp = new BaseResponse<Map<String, Object>>();
        String uri = env.getProperty("base.wallet") + "/user/add";
        Timestamp stamp = new Timestamp(System.currentTimeMillis());
        Users userExists = userRepository.findByUsername(userinfo.getUsers().getUsername());
        if (userExists != null) {
            brapi.setSuccess(false);
            resp.setErrorMessage(Helper.getMessage("user_already_in_system"));
            brapi.setResponse(resp);
            return brapi;

        }
        userinfo.getUsers().setPassword(bCryptPasswordEncoder.encode(userinfo.getUsers().getPassword()));
        userinfo.getUsers().setEnabled(false);
        userinfo.getUsers().setConfirmationToken(UUID.randomUUID().toString());
        System.out.println("Date: " + stamp);
        DateAndTime dt = new DateAndTime();
        dt.setCreateDate(stamp);
        userinfo.setDateandtime(dt);
        Authorities ar = new Authorities();
        ar.setAuthority("ROLE_USER");
        ar.setUsername(userinfo.getUsers().getUsername());
        userinfo.setAuthorities(ar);
        userinforepository.save(userinfo);
        String appUrl = request.getScheme() + "://" + request.getServerName() + ":" + request.getLocalPort() + "/api/login/confirm?token=" + userinfo.getUsers().getConfirmationToken();
        String from = "OreoMaster";
        String to = userinfo.getUsers().getUsername();
        String subject = "Meeting Oreo";

//        EmailTemplate template = new EmailTemplate("email.html");

        Map<String, String> replacements = new HashMap<String, String>();
        replacements.put("user", userinfo.getUsers().getUsername());
        replacements.put("today", String.valueOf(new Date()));
        replacements.put("verify", appUrl);

//        String message = template.getTemplate(replacements);
//
//        Email email = new Email(from, to, subject, message);
//        email.setHtml(true);
//        emailService.send(email);
        RestTemplate rt = new RestTemplate();
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        JSONObject obj = new JSONObject();
        obj.put("payer", userinfo.getUsers().getUsername());
        obj.put("phone", userinfo.getPhoneNumber());
        HttpEntity<String> entity = new HttpEntity<String>(obj.toString(), headers);
//			BaseRestApi response = rt.postForObject( uri, entity , BaseRestApi.class );
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
        userRepository.save(user);
        brapi.setSuccess(true);
        return brapi;
    }

}
