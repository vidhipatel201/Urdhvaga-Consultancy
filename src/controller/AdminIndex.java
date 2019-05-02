package controller;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Hashtable;
import java.util.List;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import database.Model;
import model.Uc_admin;
import utils.CookieManager;
import utils.RouteManager;
import utils.SessionManager;
import utils.ViewManager;

@WebServlet("/admin")
public class AdminIndex extends HttpServlet {
	private static final long serialVersionUID = 1L;
    private Model model;
    private List<Uc_admin> ucAdmin_list;
    private Uc_admin ucAdmin;
    private Cookie cookie = null;
    public AdminIndex() {
        super();
        model = new Model();
        ucAdmin_list = new ArrayList<Uc_admin>();
        ucAdmin = new Uc_admin();
        
    }

	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		String cookieValue;
		if((cookieValue = CookieManager.cookieExists(request, "ucAdmin")) != null) {
			 String whereClause = "a_username='" + cookieValue + "'";
			  ucAdmin_list = (List<Uc_admin>)(Object) model.selectData("model.Uc_admin", whereClause);
			  ucAdmin = ucAdmin_list.get(0);
			  if(ucAdmin.getA_active() == 1) {
				  Hashtable sessionAttributes = new Hashtable();
				  sessionAttributes.put("utype", ucAdmin.getA_type());
				  sessionAttributes.put("uusername", ucAdmin.getA_username());
				  sessionAttributes.put("uuid", ucAdmin.getA_id());
				  SessionManager.setSession(request, "admin", sessionAttributes);
				  CookieManager.renewCookie(request, response, "ucAdmin", (60*60*24*5));
				 // response.sendRedirect(request.getContextPath() + "/admin/home");
				  RouteManager.route(response, "admin/home");
			  } else {
				  //response.sendRedirect(request.getContextPath() + "/AdminUserAccounts?action=logout");
				  RouteManager.route(response, "AdminUserAccounts?action=logout");
			  }
		} else if(SessionManager.sessionExists(request, "admin") != null) {
			//response.sendRedirect(request.getContextPath() + "/admin/home");
			 RouteManager.route(response, "admin/home");
		}
		else {
			ucAdmin_list = (List<Uc_admin>)(Object) model.selectData("model.Uc_admin", "1");
			if(ucAdmin_list.size() > 0) {
				request.setAttribute("registered", 1);
			} else {
				request.setAttribute("registered", 0);
			}
			//request.getRequestDispatcher("/admin/index.jsp").forward(request, response);
			ViewManager.showView(request, response, "admin/index.jsp");
		}
	}

	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		doGet(request, response);
	}

}
