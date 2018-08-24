package security.oauth.authen.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.annotation.Order;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.builders.WebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import javax.sql.DataSource;

@Configuration
@Order(100)
public class WebSecurityConfig extends WebSecurityConfigurerAdapter {
	

    @Override
    @Bean
    public AuthenticationManager authenticationManagerBean() throws Exception {
        return super.authenticationManagerBean();
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Autowired
    @Qualifier("dataSource")
    private DataSource dataSource;

    @Override
    public void configure(WebSecurity web) throws Exception {
        web.ignoring()
            .antMatchers("/h2-console/**","/login","/api/resource/**");
    }

    @Override
    protected void configure(HttpSecurity http) throws Exception {
    	  	
        // @formatter:off
        http.authorizeRequests()
        	.antMatchers(HttpMethod.OPTIONS, "/oauth/token").permitAll()
            .anyRequest().authenticated()
            .and().formLogin()
            .and().csrf().disable();
        // @formatter:on
    }

    @Override
    protected void configure(AuthenticationManagerBuilder auth) throws Exception {
        auth.jdbcAuthentication()
            .dataSource(dataSource)
            .usersByUsernameQuery("select username,password,enabled from w_users where username=?")
            .authoritiesByUsernameQuery("select username,authority from w_authorities where username=?")
            .passwordEncoder(passwordEncoder());
        
//        auth.
//		jdbcAuthentication()
//			.usersByUsernameQuery("select username,password,enabled from merchant where username=?")
//			.authoritiesByUsernameQuery("select username,authority from auth_merchant where username=?")
//			.dataSource(dataSource)
//			.passwordEncoder(passwordEncoder());
    }
    
   
}
