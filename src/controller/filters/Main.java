package controller.filters;

import java.io.IOException;
import java.util.Hashtable;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.annotation.WebFilter;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import controller.UCConstants;
import utils.RouteManager;
import utils.SessionManager;
import utils.ViewManager;

/*@WebFilter({"/admin/admin-manager"})*/
public class Main implements Filter {

    public Main() {
    }

	public void destroy() {
	}

	public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws IOException, ServletException {
		HttpServletRequest httpRequest = (HttpServletRequest) request;
		HttpServletResponse httpResponse = (HttpServletResponse) response;
		Hashtable sessionAttributes = new Hashtable();
		sessionAttributes = (Hashtable) SessionManager.sessionExists(httpRequest, "admin");
		if(sessionAttributes != null) {
			if(Integer.parseInt(sessionAttributes.get("utype").toString()) == UCConstants.MAIN_ADMIN) {
				chain.doFilter(request, response);
			} else {
				ViewManager.showView(httpRequest, httpResponse, "admin/accessdenied.jsp");
			}
		} else {
			RouteManager.route(httpResponse, "admin");
		}
	}

	public void init(FilterConfig fConfig) throws ServletException {
	}

}
