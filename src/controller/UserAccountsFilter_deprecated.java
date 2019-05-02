package controller;

import java.io.IOException;
import java.util.Enumeration;
import java.util.List;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.annotation.WebFilter;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import model.Uc_state;
import utils.RouteManager;
import utils.SessionManager;
import utils.ViewManager;


@WebFilter("/not-in-use")
public class UserAccountsFilter_deprecated implements Filter {

    public UserAccountsFilter_deprecated() {

    }

	public void destroy() {
	
	}

	public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws IOException, ServletException {
		HttpServletRequest servletRequest = (HttpServletRequest) request;
		HttpServletResponse servletResponse = (HttpServletResponse) response;
		boolean flag = true;
		if(servletRequest.getMethod().equals("GET") || servletRequest.getMethod().equals("get")) {
			Enumeration<String> params = servletRequest.getParameterNames();
			while(params.hasMoreElements()) {
				if(params.nextElement().toLowerCase().toString().equals("action") || params.nextElement().toLowerCase().toString().equals("job")) {
					
				} else {
					SessionManager.setSession(servletRequest, "error", "You made a bad request. Please try again.");
					flag = false;
					break;
				}
			}
		}
		if(!flag) {
			servletRequest.setAttribute("restricted", 1);
		}
		chain.doFilter(servletRequest, servletResponse);
	}

	public void init(FilterConfig fConfig) throws ServletException {
	
	}

}
