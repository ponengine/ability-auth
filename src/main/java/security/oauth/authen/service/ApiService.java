package security.oauth.authen.service;

import java.nio.charset.Charset;
import java.util.Optional;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.json.simple.JSONObject;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.converter.StringHttpMessageConverter;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import security.oauth.authen.domain.BaseRestApi;
import security.oauth.authen.dto.PromotionDTO;
import security.oauth.authen.modelapi.Rewards;
import security.oauth.authen.modelapi.TransactionReport;

@Service
public class ApiService {
	@Value("${base.service}")
	private static String pathuri;
	private static String pathwallet = "/api/walletpocket";
	private static String pathpromotion="/api/promotions";
	private static String pathreward="api/rewards";
	private static String pathtransaction="/api/transaction";
	private static String pathredeem="/api/redeem";

	public static BaseRestApi configdata(JSONObject json,MediaType media,String path,HttpServletRequest request) {
		String uri = pathuri+path;
		RestTemplate rt = new RestTemplate();
		rt.getMessageConverters().add(0, new StringHttpMessageConverter(Charset.forName("UTF-8")));
		HttpHeaders headers = new HttpHeaders();
		headers.set("lang", request.getHeader("lang"));
		headers.setContentType(media);
		HttpEntity<String> entity = new HttpEntity<String>(json.toString() ,headers);
		BaseRestApi response = request.getMethod().equals("POST")?rt.postForObject(uri,entity, BaseRestApi.class):rt.getForObject(uri, BaseRestApi.class);
		return response;	
	}
	public BaseRestApi getwallet(String username,HttpServletRequest request) {
		JSONObject json = new JSONObject();
		return configdata(json, MediaType.APPLICATION_JSON, pathwallet+"/"+username, request);
	}
	public BaseRestApi calceltransaction(Long id, String adminname,HttpServletRequest request) {
		JSONObject json = new JSONObject();
		json.put("usernameAdmin", adminname);
		return configdata(json, MediaType.APPLICATION_JSON, pathwallet+"/"+id, request);
	}
	public BaseRestApi newpromotion(PromotionDTO promotionDto, HttpServletRequest request) {
		JSONObject json = new JSONObject();
		json.put("startDateST", promotionDto.getStartDateST());
		json.put("startTimeST", promotionDto.getStartTimeST());
		json.put("endDateST", promotionDto.getEndDateST());
		json.put("endTimeST", promotionDto.getEndTimeST());
		json.put("point", promotionDto.getPoint());
		json.put("createby", promotionDto.getCreateby());
		json.put("promoName", promotionDto.getPromoName());
		json.put("detail", promotionDto.getDetail());
		json.put("uploadPic", promotionDto.getUploadPic());
		json.put("policy", promotionDto.getPolicy());
		return configdata(json, MediaType.MULTIPART_FORM_DATA, pathpromotion+"/newpromotion", request);
	}
	public BaseRestApi getpromotion(Long id, HttpServletRequest request) {
		JSONObject json = new JSONObject();
		return configdata(json, MediaType.APPLICATION_JSON, pathpromotion+"/geteditpromotion?id="+id, request);
	}
	public BaseRestApi getallpromotions(HttpServletRequest request) {
		JSONObject json = new JSONObject();
		return configdata(json, MediaType.APPLICATION_JSON, pathpromotion+"/getallpromotion", request);
	}
	public BaseRestApi deletepromotion(Long id, HttpServletRequest request) {
		JSONObject json = new JSONObject();
		return configdata(json, MediaType.APPLICATION_JSON, pathpromotion+"/delete?id="+id, request);
	}
	public BaseRestApi updatepromotion(PromotionDTO promotionDto, HttpServletRequest request, Long id) {
		Optional<PromotionDTO> optionalreward = Optional.ofNullable(promotionDto);
		JSONObject json = new JSONObject();
		json.put("startDateST", optionalreward.map(PromotionDTO::getStartDateST).orElse(""));
		json.put("startTimeST", optionalreward.map(PromotionDTO::getStartTimeST).orElse(""));
		json.put("endDateST", optionalreward.map(PromotionDTO::getEndDateST).orElse(""));
		json.put("endTimeST", optionalreward.map(PromotionDTO::getEndTimeST).orElse(""));
		json.put("uploadPic", promotionDto.getUploadPic());
		json.put("rewardName", optionalreward.map(PromotionDTO::getPromoName).orElse(""));
		json.put("point", promotionDto.getPoint());
		json.put("detail", optionalreward.map(PromotionDTO::getDetail).orElse(""));
		json.put("policy", optionalreward.map(PromotionDTO::getPolicy).orElse(""));
		return configdata(json, MediaType.MULTIPART_FORM_DATA, pathreward+"/updatepromotion?id"+id, request);
	}
	public BaseRestApi newreward(Rewards reward, HttpServletRequest request) {
		JSONObject json = new JSONObject();
		json.put("startDateST", reward.getStartDateST());
		json.put("startTimeST", reward.getStartTimeST());
		json.put("endDateST", reward.getEndDateST());
		json.put("endTimeST", reward.getEndTimeST());
		json.put("uploadPic", reward.getUploadPic());
		json.put("typeTime", reward.getTypeTime());
		json.put("amountTime", reward.getAmountTime());
		return configdata(json, MediaType.MULTIPART_FORM_DATA, pathreward+"/newreward", request);
	}
	public BaseRestApi getreward(Long id, HttpServletRequest request) {
		JSONObject json = new JSONObject();
		return configdata(json, MediaType.APPLICATION_JSON, pathreward+"/getreward?id="+id, request);
	}
	public BaseRestApi getallrewards(HttpServletRequest request) {
		JSONObject json = new JSONObject();
		return configdata(json, MediaType.APPLICATION_JSON, pathreward+"/getallreward", request);
	}
	public BaseRestApi updatereward(Rewards reward, HttpServletRequest request, Long id) {
		Optional<Rewards> optionalreward = Optional.ofNullable(reward);
		JSONObject json = new JSONObject();
		json.put("startDateST", optionalreward.map(Rewards::getStartDateST).orElse(""));
		json.put("startTimeST", optionalreward.map(Rewards::getStartTimeST).orElse(""));
		json.put("endDateST", optionalreward.map(Rewards::getEndDateST).orElse(""));
		json.put("endTimeST", optionalreward.map(Rewards::getEndTimeST).orElse(""));
		json.put("uploadPic", reward.getUploadPic());
		json.put("typeTime", optionalreward.map(Rewards::getTypeTime).orElse(""));
		json.put("amountTime", optionalreward.map(Rewards::getAmountTime).orElse(""));
		json.put("rewardName", optionalreward.map(Rewards::getRewardName).orElse(""));
		json.put("point", reward.getPoint());
		json.put("detail", optionalreward.map(Rewards::getDetail).orElse(""));
		json.put("policy", optionalreward.map(Rewards::getPolicy).orElse(""));
		json.put("location", optionalreward.map(Rewards::getLocation).orElse(""));
		return configdata(json, MediaType.MULTIPART_FORM_DATA, pathreward+"/updatereward", request);
	}
	public BaseRestApi deletereward(Long id, HttpServletRequest request) {
		JSONObject json = new JSONObject();
		return configdata(json, MediaType.APPLICATION_JSON, pathreward+"/delete?id="+id, request);
	}
	public BaseRestApi getAllTransaction(HttpServletRequest request) {
		JSONObject json = new JSONObject();
		return configdata(json, MediaType.APPLICATION_JSON, pathtransaction+"/admin/getall", request);
	}
	public BaseRestApi gettranToday(HttpServletRequest request) {
		JSONObject json = new JSONObject();
		return configdata(json, MediaType.APPLICATION_JSON, pathtransaction+"/admin/gettoday", request);
	}
	public BaseRestApi getbysearch(TransactionReport searchDate,HttpServletRequest request) {
		JSONObject json = new JSONObject();
		json.put("status", searchDate.getStatus());
		json.put("stasrtDate", searchDate.getStasrtDate());
		json.put("endDate", searchDate.getEndDate());
		return configdata(json, MediaType.APPLICATION_JSON, pathtransaction+"/admin/getbysearch", request);
	}
	public BaseRestApi gettranByUser(String username,HttpServletRequest request) {
		JSONObject json = new JSONObject();
		return configdata(json, MediaType.APPLICATION_JSON, pathtransaction+"/user/getallbyuser/"+username, request);
	}
	public BaseRestApi getByUserToday(String username, HttpServletRequest request) {
		JSONObject json = new JSONObject();
		return configdata(json, MediaType.APPLICATION_JSON, pathtransaction+"/user/getusertrantoday/"+username, request);
	}
	public BaseRestApi getbysearchwithuser(TransactionReport transReport, HttpServletRequest request) {
		JSONObject json = new JSONObject();
		json.put("payer", transReport.getPayer());
		json.put("status", transReport.getStatus());
		json.put("stasrtDate", transReport.getStasrtDate());
		json.put("endDate", transReport.getEndDate());
		return configdata(json, MediaType.APPLICATION_JSON, pathtransaction+"/user/getbysearchwithuser", request);
	}
	public BaseRestApi myreward(String username, HttpServletRequest request) {
		JSONObject json = new JSONObject();
		return configdata(json, MediaType.APPLICATION_JSON, pathredeem+"/myreward/"+username, request);
	}
	
	
	

}
