package controller;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
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
import model.Uc_candidate_details;
import utils.CookieManager;
import utils.EncryptionManager;
import utils.RouteManager;
import utils.SessionManager;
import utils.ViewManager;

@WebServlet("/AdminUserAccounts")
public class AdminUserAccounts extends HttpServlet {
	private static final long serialVersionUID = 1L;
	HttpSession session;
	String username, password;
	List<Uc_admin> ucAdmin_list;
	Uc_admin ucAdmin;
	Model m;
	public AdminUserAccounts() {
        super();
        ucAdmin_list = new ArrayList<Uc_admin>();
        ucAdmin = new Uc_admin();
        m = new Model();
    }

	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		if(request.getParameter("action").toString().equals("login")) {
			login(request,response);
		} else if(request.getParameter("action").toString().equals("signup")) {
			signup(request,response);
		} else if(request.getParameter("action").toString().equals("logout")) {
			logout(request,response);
		} else if(request.getParameter("action").toString().toLowerCase().equals("changepassword")) {
			ViewManager.showView(request, response, "admin/changepassword.jsp");
		} else if(request.getParameter("action").toString().toLowerCase().equals("save_password")) {
			savePassword(request, response);
		} else {
			session = request.getSession();
			session.setAttribute("error", "Something went wrong. Please refresh this page and try again.");
			RouteManager.route(response, "admin");
		}
	}

	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		doGet(request, response);
	}
	
	protected void login(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		username = request.getParameter("username_login").toString();
		password = request.getParameter("password_login").toString();
		String whereClause = "a_username=BINARY '" + username + "' and a_active=1";
		ucAdmin_list = (List<Uc_admin>)(Object) m.selectData("model.Uc_admin", whereClause);
		if(ucAdmin_list.size() > 0) {
			if(EncryptionManager.matchHash(password, ucAdmin_list.get(0).getA_password())) {
				//success
				Uc_admin ucAdmin = new Uc_admin();
				ucAdmin = ucAdmin_list.get(0);
				if(request.getParameter("remember") != null) {
					CookieManager.setCookie(response, "ucAdmin", ucAdmin.getA_username(), (60*60*24*5));
				}
				Hashtable sessionAttributes = new Hashtable();
				sessionAttributes.put("uusername", ucAdmin.getA_username());
				sessionAttributes.put("utype", ucAdmin.getA_type());
				sessionAttributes.put("uuid", ucAdmin.getA_id());
				SessionManager.setSession(request, "admin", sessionAttributes);
				RouteManager.route(response, "admin/home");
			} else {
				SessionManager.setSession(request, "login_error", "Invalid password.");
				RouteManager.route(response, "admin");
			}
		} else {
			SessionManager.setSession(request, "login_error", "Invalid username.");
			RouteManager.route(response, "admin");
		}
	}
	
	protected void signup(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		session = request.getSession();
		username = request.getParameter("username_signup").toString();
		password = EncryptionManager.generateHash(request.getParameter("password_signup").toString());
		String whereClause = "a_username='" + username + "'";
		ucAdmin_list = (List<Uc_admin>)(Object) m.selectData("model.Uc_admin", whereClause);
		if(ucAdmin_list.size() > 0) {
			SessionManager.setSession(request, "error", "Username you entered is already registered. Please enter another username.");
			RouteManager.route(response, "admin");
		} else {
			ucAdmin.setA_active(1);
			ucAdmin.setA_password(password);
			ucAdmin.setA_username(username);
			ucAdmin.setA_type(Long.valueOf(UCConstants.MAIN_ADMIN));
			if(m.insertData(ucAdmin)) {
				SessionManager.setSession(request, "login_success", "Account successfully created. Now please login.");
				RouteManager.route(response, "admin");
			} else {
				SessionManager.setSession(request, "signup_error", "Something went wrong. Please try again.");
				RouteManager.route(response, "admin");
			}
		}
	}
	
	protected void logout(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		SessionManager.unsetSession(request, "admin");
		CookieManager.unsetCookie(request, response, "ucAdmin");
		RouteManager.route(response, "admin");
	}
	
	private void savePassword(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		Hashtable sessionAttributes = new Hashtable();
		sessionAttributes = (Hashtable) SessionManager.sessionExists(request, "admin");
		String old_password = request.getParameter("old_password").toString();
		String new_password = request.getParameter("new_password").toString();
		ucAdmin_list = new ArrayList<Uc_admin>();
		ucAdmin_list = (List<Uc_admin>)(Object) m.selectData("model.Uc_admin", "a_id=" + Long.parseLong(sessionAttributes.get("uuid").toString())); 
		if(ucAdmin_list.size() > 0) {
			if(EncryptionManager.matchHash(old_password, ucAdmin_list.get(0).getA_password())) {
				ucAdmin = new Uc_admin();
				ucAdmin.setA_password(EncryptionManager.generateHash(new_password));
				if(m.updateData(ucAdmin, "a_id=" + Long.parseLong(sessionAttributes.get("uuid").toString()))) {
					//success
					SessionManager.setSession(request, "success", "Password successfully changed.");
					RouteManager.route(response, "AdminUserAccounts?action=changePassword");
				} else {
					//error
					SessionManager.setSession(request, "error", "Something went wrong. Please try again.");
					RouteManager.route(response, "AdminUserAccounts?action=changePassword");
				}
			} else {
				//wrong old password
				SessionManager.setSession(request, "error", "Incorrect old password.");
				RouteManager.route(response, "AdminUserAccounts?action=changePassword");
			}
		} else {
			//redirect to home
			RouteManager.route(response, "");
		}
	}
}
