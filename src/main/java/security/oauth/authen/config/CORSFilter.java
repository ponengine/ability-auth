package security.oauth.authen.config;

import java.io.IOException;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.context.annotation.Configuration;
@Configuration
public class CORSFilter implements Filter{

	@Override
	public void destroy() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void doFilter(ServletRequest req, ServletResponse resp, FilterChain chain)
			throws IOException, ServletException {
			    HttpServletResponse response = (HttpServletResponse) resp;
			    HttpServletRequest request = (HttpServletRequest) req;
			    response.setHeader("Access-Control-Allow-Origin", "*");
			    response.setHeader("Access-Control-Allow-Methods", "POST, GET, OPTIONS, DELETE");
			    response.setHeader("Access-Control-Max-Age", "3600");
			    response.setHeader("Access-Control-Allow-Headers", "x-requested-with, authorization, Content-Type, Authorization, credential, X-XSRF-TOKEN, lang");

			    if ("OPTIONS".equalsIgnoreCase(request.getMethod())) {
			        response.setStatus(HttpServletResponse.SC_OK);
			    } else {
			        chain.doFilter(req, resp);
			    }

		
	}

	@Override
	public void init(FilterConfig arg0) throws ServletException {
		// TODO Auto-generated method stub
		
	}

}
