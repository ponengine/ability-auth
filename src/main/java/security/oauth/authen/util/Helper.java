package security.oauth.authen.util;

import java.net.InetAddress;
import java.net.UnknownHostException;
import java.nio.charset.Charset;
import java.security.Principal;
import java.util.Locale;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;

import org.json.simple.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.core.env.Environment;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.converter.StringHttpMessageConverter;
import org.springframework.web.client.RestTemplate;

import security.oauth.authen.domain.BaseRestApi;
import security.oauth.authen.entity.Users;
import security.oauth.authen.repository.UserRepository;



public class Helper {

	public static String currentLocale() {
        Locale currentLocale = LocaleContextHolder.getLocale();
        if (currentLocale.getLanguage().equals("th")) {
            return "th";
        } else {
            return "en";
        }
    }

    public static String localizedField(String name) {
        if (Helper.currentLocale().equals("th")) {
            return name + "Th";
        } else {
            return name;
        }
    }

    public static String getMessage(String code) {
        MessageByLocaleService messageByLocaleService = SpringContextUtil.getApplicationContext().getBean(MessageByLocaleService.class);
        return messageByLocaleService.getMessage(code);
    }

    public static Locale currentLocale(HttpServletRequest request) {
        String lang = "en";
        if (request.getCookies() != null) {
            for (Cookie cookie : request.getCookies()) {
                if (cookie.getName().equals("myI18N_cookie")) {
                    lang = cookie.getValue();
                }
            }
        }

        if (lang.equals("th") || lang.equals("en")) {
            return new Locale(lang);
        } else {
            return new Locale("en");
        }
    }
    
    public static Users currentUser(HttpServletRequest request) {
        Principal principal = request.getUserPrincipal();
        if (principal == null)
          return null;

        UserRepository userRepo = SpringContextUtil.getApplicationContext().getBean(UserRepository.class);
        return userRepo.findByUsername(principal.getName());
      }
    
    public static String getComName(){
    	String hostname = "Unknown";
		try
		{
		    InetAddress addr;
		    addr = InetAddress.getLocalHost();
		    hostname = addr.getHostName();
		 
		}
		catch (UnknownHostException ex)
		{
		    System.out.println("Hostname can not be resolved");
		}
		return hostname;
    }
    

}
