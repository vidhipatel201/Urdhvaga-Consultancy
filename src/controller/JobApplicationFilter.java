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

@WebFilter("/apply")
public class JobApplicationFilter implements Filter {

    public JobApplicationFilter() {
    }

	public void destroy() {
	}

	public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws IOException, ServletException {
		if(SessionManager.sessionExists((HttpServletRequest)request, "uc_client_session") != null) {
			chain.doFilter(request, response);
		} else {
			SessionManager.setSession((HttpServletRequest)request, "success", "Please login before you can apply for any job.");
			RouteManager.route((HttpServletResponse)response, "user-accounts?job=" + request.getParameter("job"));
		}
	}

	public void init(FilterConfig fConfig) throws ServletException {
	}

}
