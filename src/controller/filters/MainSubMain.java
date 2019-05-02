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

/*@WebFilter({"/admin/firm", "/admin/manage-users", "/admin/current-openings?action=activate", "/admin/current-openings?action=deactivate", "/admin/current-openings?action=approve", "/admin/current-openings?action=disapprove", "/admin/current-openings?action=deleteJobs", "/admin/current-openings?action=addPermission", "/admin/current-openings?action=removePermission", "/admin/employer?action=activate", "/admin/employer?action=deactivate", "/admin/employer?action=approve", "/admin/employer?action=disapprove", "/admin/employer?action=deleteEmployers", "/admin/employer?action=addPermission", "/admin/employer?action=removePermission"})*/
public class MainSubMain implements Filter {

    public MainSubMain() {
    }

	public void destroy() {
	}

	public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws IOException, ServletException {
		HttpServletRequest httpRequest = (HttpServletRequest) request;
		HttpServletResponse httpResponse = (HttpServletResponse) response;
		Hashtable sessionAttributes = new Hashtable();
		sessionAttributes = (Hashtable) SessionManager.sessionExists(httpRequest, "admin");
		if(sessionAttributes != null) {
			if(Integer.parseInt(sessionAttributes.get("utype").toString()) == UCConstants.MAIN_ADMIN || Integer.parseInt(sessionAttributes.get("utype").toString()) == UCConstants.SUB_MAIN_ADMIN) {
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
