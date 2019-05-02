package controller;

import java.io.IOException;
import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.annotation.WebFilter;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import utils.RouteManager;
import utils.SessionManager;

@WebFilter({"/profile", "/applied"})
public class UserProfileFilter implements Filter {

    public UserProfileFilter() {
    }

	public void destroy() {
	}

	public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws IOException, ServletException {
		if(SessionManager.sessionExists((HttpServletRequest)request, "uc_client_session") != null) {
			chain.doFilter(request, response);
		} else {
			
			RouteManager.route((HttpServletResponse)response, "");
		}
	}
	public void init(FilterConfig fConfig) throws ServletException {
	}

}
