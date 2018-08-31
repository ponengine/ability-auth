package security.oauth.authen.resource;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import security.oauth.authen.domain.BaseRestApi;
import security.oauth.authen.dto.ProfileDTO;
import security.oauth.authen.dto.PromotionDTO;
import security.oauth.authen.modelapi.Rewards;
import security.oauth.authen.modelapi.TransactionReport;
import security.oauth.authen.service.ApiService;

@RestController
@RequestMapping("/api/rest")
public class ApiRestResource {

	private final ApiService apiservice;

	@Autowired
	public ApiRestResource(ApiService apiservice) {
		this.apiservice = apiservice;
	}

	@RequestMapping("/wallet")
	class WalletCon {
		@GetMapping("/getwallet/{username}")
		public BaseRestApi getwallet(@PathVariable("username") String username, HttpServletRequest request) {
			return apiservice.getwallet(username, request);
		}

		@PostMapping("/calcelwallet/{id}")
		public BaseRestApi calcelTransaction(@PathVariable("id") Long id, @RequestParam("adminname") String adminname,
				HttpServletRequest request) {
			return apiservice.calceltransaction(id, adminname, request);
		}
	}

	@RequestMapping("/promotion")
	class PromotionCon {
		@PostMapping("/newpromotion")
		public BaseRestApi newPromotion(@ModelAttribute @Valid PromotionDTO promotionDto, HttpServletRequest request,
				BindingResult result) {
			return apiservice.newpromotion(promotionDto, request);
		}

		@PostMapping("/updatepromotion")
		public BaseRestApi updatePromotion(@ModelAttribute @Valid PromotionDTO promotionDto, HttpServletRequest request,
				@RequestParam("id") Long id, BindingResult result) {
			return apiservice.updatepromotion(promotionDto, request, id);
		}

		@RequestMapping("/deletepromotion")
		public BaseRestApi deletePromotion(@RequestParam("id") Long id, HttpServletRequest request) {
			return apiservice.deletepromotion(id, request);
		}

		@GetMapping("/getpromotion")
		public BaseRestApi getPromotion(@RequestParam("id") Long id, HttpServletRequest request) {
			return apiservice.getpromotion(id, request);
		}

		@GetMapping("/getallpromotion")
		public BaseRestApi geAlltPromotions(HttpServletRequest request) {
			return apiservice.getallpromotions(request);

		}
	}

	@RequestMapping("/reward")
	class RewardCon {
		@PostMapping("/newreward")
		public BaseRestApi newReward(@ModelAttribute @Valid Rewards reward, HttpServletRequest request) {
			return apiservice.newreward(reward, request);
		}

		@GetMapping("/getreward")
		public BaseRestApi getReward(@RequestParam("id") Long id, HttpServletRequest request) {
			return apiservice.getreward(id, request);
		}

		@GetMapping("/getallreward")
		public BaseRestApi getRewards(HttpServletRequest request) {
			return apiservice.getallrewards(request);
		}

		@PostMapping("/updatereward")
		public BaseRestApi editReward(@ModelAttribute @Valid Rewards rewardDto, HttpServletRequest request,
				@RequestParam("id") Long id, BindingResult result) {
			return apiservice.updatereward(rewardDto, request, id);
		}

		@RequestMapping("/deletereward")
		public BaseRestApi deleteReward(@RequestParam("id") Long id, HttpServletRequest request) {
			return apiservice.deletereward(id, request);
		}
	}

	@RequestMapping("/transaction")
	class TransactionCon {
		@GetMapping("/admin/getall")
		public BaseRestApi getalltransaction(HttpServletRequest request) {
			return apiservice.getAllTransaction(request);
		}

		@GetMapping("/admin/gettoday")
		public BaseRestApi gettran_today(HttpServletRequest request) {
			return apiservice.gettranToday(request);
		}

		@PostMapping("/admin/getbysearch")
		public BaseRestApi getbysearch(@RequestBody TransactionReport searchDate, HttpServletRequest request) {
			return apiservice.getbysearch(searchDate, request);
		}

		@GetMapping("/user/getallbyuser/{username}")
		public BaseRestApi getTransaction(@PathVariable("username") String username,HttpServletRequest request) {
			return apiservice.gettranByUser(username,request);
		}

		@PostMapping("/user/getusertrantoday/{username}")
		public BaseRestApi getusertrantoday(@PathVariable("username") String username,HttpServletRequest request) {
			return apiservice.getByUserToday(username,request);
		}

		@PostMapping("/user/getbysearchwithuser")
		public BaseRestApi getbysearchwithuser(@RequestBody TransactionReport transReport,HttpServletRequest request) {
			return apiservice.getbysearchwithuser(transReport,request);
		}
	}

	@RequestMapping("/redeem")
	class RedeemCon {
		@PostMapping("/myreward/{username}")
		public BaseRestApi myReward(@PathVariable("username")String username,HttpServletRequest request) {
			/* username and enable */return apiservice.myreward(username,request);
		}
	}

}
