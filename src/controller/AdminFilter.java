package controller;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Hashtable;
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

import database.Model;
import model.Uc_admin;
import utils.RouteManager;
import utils.SessionManager;

@WebFilter({ "/admin/manage-users", "/admin/home", "/admin/manage-job-applications", "/admin/current-openings", "/admin/employer", "/admin/admin-manager", "/admin/firm", "/admin/interviews", "/admin/terms"  })
public class AdminFilter implements Filter {

	private List<Uc_admin> ucAdmin_list;
	private Model model;
	
    public AdminFilter() {
    }

	public void destroy() {
	}

	public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws IOException, ServletException {
		HttpServletRequest httprequest = (HttpServletRequest) request;
		HttpServletResponse httpresponse = (HttpServletResponse) response;
		if(SessionManager.sessionExists(httprequest, "admin") != null) {
			chain.doFilter(request, response);
		} else {
			RouteManager.route(httpresponse, "admin");
		}
	}

	public void init(FilterConfig fConfig) throws ServletException {
	}

}
