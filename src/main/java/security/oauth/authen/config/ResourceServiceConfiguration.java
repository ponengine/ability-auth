package security.oauth.authen.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.oauth2.config.annotation.web.configuration.EnableResourceServer;
import org.springframework.security.oauth2.config.annotation.web.configuration.ResourceServerConfigurerAdapter;
//@Configuration
//@EnableResourceServer
public class ResourceServiceConfiguration extends ResourceServerConfigurerAdapter{
	@Override
	  public void configure(final HttpSecurity http) throws Exception {
	    // @formatter:off
	    http.csrf().disable().authorizeRequests()
	    // This is needed to enable swagger-ui interface.
	    .antMatchers("/swagger-ui.html","/swagger-resources/**", "/v2/api-docs/**").permitAll()
	    
	    ;

	  }
}
