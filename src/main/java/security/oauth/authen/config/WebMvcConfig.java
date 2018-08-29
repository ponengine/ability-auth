package security.oauth.authen.config;

import java.util.Locale;
import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.context.MessageSource;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.support.ReloadableResourceBundleMessageSource;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.web.filter.CharacterEncodingFilter;
import org.springframework.web.servlet.LocaleResolver;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurerAdapter;
import org.springframework.web.servlet.i18n.CookieLocaleResolver;
import org.springframework.web.servlet.i18n.LocaleChangeInterceptor;
@Configuration
public class WebMvcConfig extends WebMvcConfigurerAdapter{
	@Bean
	public BCryptPasswordEncoder passwordEncoder() {
		BCryptPasswordEncoder bCryptPasswordEncoder = new BCryptPasswordEncoder();
		return bCryptPasswordEncoder;
	}
        
        @Bean
	  public LocaleResolver localeResolver() {
	    CookieLocaleResolver resolver = new CookieLocaleResolver();
	    resolver.setDefaultLocale(new Locale("en"));
	    resolver.setCookieName("myI18N_cookie");
	    return resolver;
	  }
	  
	  @Bean
	  public LocaleChangeInterceptor localeChangeInterceptor() {
	    LocaleChangeInterceptor lci = new LocaleChangeInterceptor();
	    lci.setParamName("lang");
	    return lci;
	  }
	  
	  @Override
	  public void addInterceptors(InterceptorRegistry registry) {
	    registry.addInterceptor(localeChangeInterceptor());
	    registry.addInterceptor(new AllInterceptor()).addPathPatterns("/**");
	  }

	  @Override
	  public void addCorsMappings(CorsRegistry registry) {
	    registry.addMapping("/**").allowedMethods("HEAD", "GET", "PUT", "POST", "DELETE", "PATCH");
	  }

	  @Bean
	  public MessageSource messageSource() {
	    ReloadableResourceBundleMessageSource msgSrc = new ReloadableResourceBundleMessageSource();
	    msgSrc.setBasename("classpath:i18n/messages");
	    msgSrc.setDefaultEncoding("UTF-8");
	    return msgSrc;
	  }
	  

	  @Bean
	  public FilterRegistrationBean filterRegistrationBean() {
	    FilterRegistrationBean registrationBean = new FilterRegistrationBean();
	    CharacterEncodingFilter characterEncodingFilter = new CharacterEncodingFilter();
	    characterEncodingFilter.setForceEncoding(true);
	    characterEncodingFilter.setEncoding("UTF-8");
	    registrationBean.setFilter(characterEncodingFilter);
	    return registrationBean;
	  }
}
