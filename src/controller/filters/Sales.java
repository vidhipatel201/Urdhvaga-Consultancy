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

/*@WebFilter({"/admin/employer?action=save", "/admin/current-openings?action=downloadjobs", "/admin/current-openings?action=getCurrentOpenings", "/admin/current-openings?action=modifySingle", "/admin/manage-users?action=getStates", "/admin/manage-users?action=getDistricts", "/admin/manage-users?action=getTalukas", "/admin/manage-users?action=getCities", "/admin/current-openings?action=getCurrentOpenings", "/admin/employer?action=getEmployerList", "/admin/employer?action=getType", "/admin/employer?action=modifySingle", "/admin/employer?action=changeFirmName", "/admin/employer?action=changeIndustrySector"})*/
public class Sales implements Filter {

    public Sales() {
    }

	public void destroy() {
	}

	public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws IOException, ServletException {
		HttpServletRequest httpRequest = (HttpServletRequest) request;
		HttpServletResponse httpResponse = (HttpServletResponse) response;
		Hashtable sessionAttributes = new Hashtable();
		sessionAttributes = (Hashtable) SessionManager.sessionExists(httpRequest, "admin");
		if(sessionAttributes != null) {
			if(Integer.parseInt(sessionAttributes.get("utype").toString()) == UCConstants.MAIN_ADMIN || Integer.parseInt(sessionAttributes.get("utype").toString()) == UCConstants.SUB_MAIN_ADMIN || Integer.parseInt(sessionAttributes.get("utype").toString()) == UCConstants.SALES_ADMIN) {
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
