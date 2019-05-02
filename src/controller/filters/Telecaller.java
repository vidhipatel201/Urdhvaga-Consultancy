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


/*@WebFilter({"/admin/manage-job-applications?action=getApplications", "/admin/manage-job-applications?action=acceptApplication", "/admin/manage-job-applications?action=rejectApplication", "/admin/manage-job-applications?action=showDetails", "/admin/manage-job-applications?action=getApplicationList", "/admin/manage-job-applications?action=getCandidateDetails", "/admin/manage-job-applications?action=getApplicationComments", "/admin/manage-users?action=getCandidateComments", "/admin/manage-users?action=savecomment", "/admin/manage-users?action=savecommentchanges", "/admin/manage-job-applications?action=savecommentchanges", "/admin/manage-users?action=deleteComment", "/admin/manage-job-applications?action=deleteComment", "/admin/manage-job-applications?action=acceptJob", "/admin/manage-job-applications?action=rejectJob", "/admin/manage-job-applications?action=saveExperienceYears", "/admin/manage-job-applications?action=deleteApplications"})*/
public class Telecaller implements Filter {

    public Telecaller() {
    }

	public void destroy() {
	}

	public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws IOException, ServletException {
		HttpServletRequest httpRequest = (HttpServletRequest) request;
		HttpServletResponse httpResponse = (HttpServletResponse) response;
		Hashtable sessionAttributes = new Hashtable();
		sessionAttributes = (Hashtable) SessionManager.sessionExists(httpRequest, "admin");
		if(sessionAttributes != null) {
			if(Integer.parseInt(sessionAttributes.get("utype").toString()) == UCConstants.MAIN_ADMIN || Integer.parseInt(sessionAttributes.get("utype").toString()) == UCConstants.SUB_MAIN_ADMIN || Integer.parseInt(sessionAttributes.get("utype").toString()) == UCConstants.TELECALLER_ADMIN) {
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
