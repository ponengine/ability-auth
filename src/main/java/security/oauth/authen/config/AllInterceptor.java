package security.oauth.authen.config;

import com.google.common.base.Strings;
import java.util.Locale;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;


import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.handler.HandlerInterceptorAdapter;

@Component
public class AllInterceptor extends HandlerInterceptorAdapter{
	 @Override
	  public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
	    String lang = request.getHeader("lang");
	    if (!Strings.isNullOrEmpty(lang))
	      LocaleContextHolder.setLocale(new Locale(lang));

	    return super.preHandle(request, response, handler);
	  }

	  @Override
	  public void postHandle(HttpServletRequest request, HttpServletResponse response, Object handler, ModelAndView modelAndView) throws Exception {
	    LocaleContextHolder.setLocale(new Locale("th"));
	    
	    super.postHandle(request, response, handler, modelAndView);
	  }
}
