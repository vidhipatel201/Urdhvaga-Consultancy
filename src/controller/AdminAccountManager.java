package controller;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Hashtable;
import java.util.List;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import database.Model;
import model.Uc_admin;
import model.Uc_admin_types;
import utils.EncryptionManager;
import utils.RouteManager;
import utils.SessionManager;
import utils.ViewManager;

@WebServlet("/admin/admin-manager")
public class AdminAccountManager extends HttpServlet {
	private static final long serialVersionUID = 1L;
       
	private List<Uc_admin_types> ucAdminTypes_list;
	private Model model;
	private List<Uc_admin> ucAdmin_list;
	private Uc_admin ucAdmin;
    public AdminAccountManager() {
        super();
        model = new Model();
    }

	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		Hashtable sessionAttributes = new Hashtable();
		sessionAttributes = (Hashtable) SessionManager.sessionExists(request, "admin");
		if(request.getParameter("action") == null) {
			if(Integer.parseInt(sessionAttributes.get("utype").toString()) == UCConstants.MAIN_ADMIN) {
				adminManager(request,response);
			} else {
				ViewManager.showView(request, response, "admin/accessdenied.jsp");
			}
		} else {
			if(request.getParameter("action").equals("")) {
				if(Integer.parseInt(sessionAttributes.get("utype").toString()) == UCConstants.MAIN_ADMIN) {
					adminManager(request,response);
				} else {
					ViewManager.showView(request, response, "admin/accessdenied.jsp");
				}
			} else if(request.getParameter("action").toLowerCase().equals("add")) {
				if(Integer.parseInt(sessionAttributes.get("utype").toString()) == UCConstants.MAIN_ADMIN) {
					addNewAdmin(request, response);
				} else {
					ViewManager.showView(request, response, "admin/accessdenied.jsp");
				}
			} else if(request.getParameter("action").toLowerCase().equals("savetypechanges")) {
				if(Integer.parseInt(sessionAttributes.get("utype").toString()) == UCConstants.MAIN_ADMIN) {
					saveTypeChanges(request, response);
				} else {
					ViewManager.showView(request, response, "admin/accessdenied.jsp");
				}
			} else if(request.getParameter("action").toLowerCase().equals("activateadmin")) {
				if(Integer.parseInt(sessionAttributes.get("utype").toString()) == UCConstants.MAIN_ADMIN) {
					activateAdmin(request, response);
				} else {
					ViewManager.showView(request, response, "admin/accessdenied.jsp");
				}
			} else if(request.getParameter("action").toLowerCase().equals("deactivateadmin")) {
				if(Integer.parseInt(sessionAttributes.get("utype").toString()) == UCConstants.MAIN_ADMIN) {
					deactivateAdmin(request, response);
				} else {
					ViewManager.showView(request, response, "admin/accessdenied.jsp");
				}
			} else if(request.getParameter("action").toLowerCase().equals("deleteadmin")) {
				if(Integer.parseInt(sessionAttributes.get("utype").toString()) == UCConstants.MAIN_ADMIN) {
					deleteAdmin(request, response);
				} else {
					ViewManager.showView(request, response, "admin/accessdenied.jsp");
				}
			}
		}
	}

	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		doGet(request, response);
	}
	
	private void adminManager(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		Hashtable sessionAttributes = new Hashtable();
		StringBuilder temp = new StringBuilder();
		sessionAttributes = (Hashtable) SessionManager.sessionExists(request, "admin");
		ucAdmin_list = new ArrayList<Uc_admin>();
		ucAdmin_list = (List<Uc_admin>)(Object) model.selectData("model.Uc_admin", "a_id!=" + sessionAttributes.get("uuid"));
		int count = 1;
		for(Uc_admin a : ucAdmin_list) {
			temp.append("<tr>");
			temp.append("<td>" + count++);
			temp.append("<td>" + a.getA_username());
			temp.append("<td>" + generateAdminTypeDropForEdit("adminType" + a.getA_id(), a.getA_type(), a.getA_id()));
			if(a.getA_active() == 1) {
				temp.append("<td><button type=\"button\" onclick=\"deactivateAdmin(this," + a.getA_id() + ")\" class=\"btn btn-warning\">Deactivate</button>");
			} else {
				temp.append("<td><button type=\"button\" onclick=\"activateAdmin(this," + a.getA_id() + ")\" class=\"btn btn-success\">Activate</button>");
			}
			
			temp.append("<td><button type=\"button\" onclick=\"deleteAdmin(this," + a.getA_id() + ")\" class=\"btn btn-danger\">Remove</button>");
			temp.append("</tr>");
		}
		if(ucAdmin_list.isEmpty()) request.setAttribute("adminList", null);
		else request.setAttribute("adminList", temp.toString());
		request.setAttribute("adminTypes", generateAdminTypeDrop("adminTypes", Long.valueOf(0)));
		ViewManager.showView(request, response, "admin/adminmanager.jsp");
	}
	
	private String generateAdminTypeDrop(String id, Long selectedId) {
		StringBuilder temp = new StringBuilder();
		ucAdminTypes_list = new ArrayList<Uc_admin_types>();
		ucAdminTypes_list = (List<Uc_admin_types>)(Object) model.selectData("model.Uc_admin_types", "1");
		temp.append("<select id=\"" + id + "\" name=\"" + id + "\" onchange=\"disableError('" + id + "')\" class=\"form-control\">");
		temp.append("<option value=\"select\" disabled selected hidden>Select admin type</option>");
		for(Uc_admin_types at : ucAdminTypes_list) {
			if(at.getAt_id() == selectedId) temp.append("<option value=\"" + at.getAt_id() + "\" selected>" + at.getAt_name() + "</option>");
			else temp.append("<option value=\"" + at.getAt_id() + "\">" + at.getAt_name() + "</option>");
		}
		temp.append("</select>");
		temp.append("<label style=\"color:red;font-weight:normal;display:none\" id=\"" + id + "_error\"></label>");
		return temp.toString();
	}
	
	private String generateAdminTypeDropForEdit(String id, Long selectedId, Long adminId) {
		StringBuilder temp = new StringBuilder();
		ucAdminTypes_list = new ArrayList<Uc_admin_types>();
		ucAdminTypes_list = (List<Uc_admin_types>)(Object) model.selectData("model.Uc_admin_types", "1");
		temp.append("<select id=\"" + id + "\" name=\"" + id + "\" onchange=\"saveChanges(this," + adminId + ")\" class=\"form-control\">");
		temp.append("<option value=\"select\" disabled selected hidden>Select admin type</option>");
		for(Uc_admin_types at : ucAdminTypes_list) {
			if(at.getAt_id() == selectedId) temp.append("<option value=\"" + at.getAt_id() + "\" selected>" + at.getAt_name() + "</option>");
			else temp.append("<option value=\"" + at.getAt_id() + "\">" + at.getAt_name() + "</option>");
		}
		temp.append("</select>");
		temp.append("<label style=\"color:red;font-weight:normal;display:none\" id=\"" + id + "_error\"></label>");
		return temp.toString();
	}
	
	private void addNewAdmin(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		Long adminType = Long.parseLong(request.getParameter("adminTypes").toString());
		String adminUsername = request.getParameter("adminUsername").toString();
		String password = EncryptionManager.generateHash("123456");
		String whereClause = "a_username='" + adminUsername + "'";
		ucAdmin_list = new ArrayList<Uc_admin>();
		ucAdmin_list = (List<Uc_admin>)(Object) model.selectData("model.Uc_admin", whereClause);
		if(ucAdmin_list.size() > 0) {
			SessionManager.setSession(request, "error", "Username you entered is already registered. Please enter another username.");
			RouteManager.route(response, "admin/admin-manager");
		} else {
			ucAdmin = new Uc_admin();
			ucAdmin.setA_password(password);
			ucAdmin.setA_type(adminType);
			ucAdmin.setA_username(adminUsername);
			if(model.insertData(ucAdmin)) {
				SessionManager.setSession(request, "success", "Account successfully created.");
				RouteManager.route(response, "admin/admin-manager");
			} else {
				SessionManager.setSession(request, "error", "Something went wrong. Please try again");
				RouteManager.route(response, "admin/admin-manager");
			}
		}
	}
	
	private void saveTypeChanges(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		Long typeId = Long.parseLong(request.getParameter("typeId").toString());
		Long adminId = Long.parseLong(request.getParameter("adminId").toString());
		ucAdmin = new Uc_admin();
		ucAdmin.setA_type(typeId);
		if(!model.updateData(ucAdmin, "a_id=" + adminId)) {
			response.setContentType("text/html;charset=UTF-8");
			response.getWriter().write("error");
		}
	}
	
	private void activateAdmin(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		int adminId = Integer.parseInt(request.getParameter("adminId").toString());
		ucAdmin = new Uc_admin();
		ucAdmin.setA_active(1);
		if(!model.updateData(ucAdmin, "a_id=" + adminId)) {
			response.setContentType("text/html;charset=UTF-8");
			response.getWriter().write("error");
		} else {
			response.setContentType("text/html;charset=UTF-8");
			response.getWriter().write("success");
		}
	}
	
	private void deactivateAdmin(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		int adminId = Integer.parseInt(request.getParameter("adminId").toString());
		ucAdmin = new Uc_admin();
		ucAdmin.setA_active(0);
		if(!model.updateData(ucAdmin, "a_id=" + adminId)) {
			response.setContentType("text/html;charset=UTF-8");
			response.getWriter().write("error");
		} else {
			response.setContentType("text/html;charset=UTF-8");
			response.getWriter().write("success");
		}
	}
	
	private void deleteAdmin(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		int adminId = Integer.parseInt(request.getParameter("adminId").toString());
		try {
			if(model.deleteData("model.Uc_admin", "a_id=" + adminId)) {
				response.setContentType("text/html;charset=UTF-8");
				response.getWriter().write("success");
			} else {
				response.setContentType("text/html;charset=UTF-8");
				response.getWriter().write("error");
			}
		} catch(Exception ex) {
			response.setContentType("text/html;charset=UTF-8");
			response.getWriter().write("dependency");
		}
	}

}
