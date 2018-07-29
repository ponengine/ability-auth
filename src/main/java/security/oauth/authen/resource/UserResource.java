package security.oauth.authen.resource;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

import javax.servlet.ServletRequest;
import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import security.oauth.authen.domain.BaseResponse;
import security.oauth.authen.domain.BaseRestApi;
import security.oauth.authen.dto.LoginDTO;
import security.oauth.authen.entity.Users;
import security.oauth.authen.repository.UserRepository;
import security.oauth.authen.util.Helper;



@RestController
@RequestMapping("/user")
@PreAuthorize("hasRole('ROLE_USER')")
public class UserResource {

	@Autowired
	private UserRepository userRepository;

//	@Autowired
//	private EmailService emailService;

	@Autowired
	private BCryptPasswordEncoder bCryptPasswordEncoder;

	@GetMapping("/message")
	public String msg() {
		return "This user";
	}

	@RequestMapping(value = "/forgotpassword", method = RequestMethod.POST)
	public BaseRestApi forgotPassword(@RequestBody @Valid Users user, BindingResult bindingResult,
			ServletRequest request) {
		BaseRestApi brapi = new BaseRestApi();
		BaseResponse<Map<String, Object>> resp = new BaseResponse<Map<String, Object>>();

		user.setConfirmationToken(UUID.randomUUID().toString());
		userRepository.save(user);
		String appUrl = request.getScheme() + "://" + request.getServerName() + ":" + request.getLocalPort()
				+ "/confirm?token=" + user.getConfirmationToken();
		String from = "OreoMaster";
		String to = user.getUsername();
		String subject = "Meeting Oreo";

//		EmailTemplate template = new EmailTemplate("email.html");

		Map<String, String> replacements = new HashMap<String, String>();
		replacements.put("user", user.getUsername());
		replacements.put("today", String.valueOf(new Date()));
		replacements.put("verify", appUrl);

//		String message = template.getTemplate(replacements);
//		Email email = new Email(from, to, subject, message);
//		email.setHtml(true);
//		emailService.send(email);
		brapi.setSuccess(true);
		return brapi;
	}

	@RequestMapping(value = "/confirmforgot", method = RequestMethod.GET)
	public BaseRestApi confirmForgot(ModelAndView modelAndView, @RequestParam("token") String token) {
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

	@RequestMapping(value = "/confirmforgot", method = RequestMethod.POST)
	public BaseRestApi confirmForgotPost(@RequestBody LoginDTO logindto, BindingResult bindingResult,
			@RequestParam("token") String token, RedirectAttributes redir) {
		BaseRestApi brapi = new BaseRestApi();
		BaseResponse<Map<String, Object>> resp = new BaseResponse<Map<String, Object>>();
		Users user = userRepository.findByConfirmationToken(token);
		user.setPassword(bCryptPasswordEncoder.encode(logindto.getPassword()));
		user.setEnabled(true);
		userRepository.save(user);
		brapi.setSuccess(true);
		return brapi;
	}

	@PostMapping("/changepassword")
	public BaseRestApi changePassword(@RequestBody LoginDTO logindto) {
		BaseRestApi brapi = new BaseRestApi();
		BaseResponse<Map<String, Object>> resp = new BaseResponse<Map<String, Object>>();
		Users user = userRepository.findByUsername(logindto.getUsername());
		if (bCryptPasswordEncoder.matches(logindto.getNewPassword(), user.getPassword())) {
			brapi.setSuccess(false);
			resp.setErrorMessage(Helper.getMessage("duplicate_password"));
			brapi.setResponse(resp);
			return brapi;
		}
		user.setPassword(bCryptPasswordEncoder.encode(logindto.getNewPassword()));
		userRepository.save(user);
		return brapi;
	}

}
