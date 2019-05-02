package controller;

import java.io.IOException;
import java.io.PrintWriter;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Hashtable;
import java.util.List;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.json.JSONException;
import org.json.JSONObject;

import database.Model;
import logs.LogManager;
import model.Uc_admin_types;
import model.Uc_candidate_details;
import model.Uc_industry_sector;
import model.Uc_interest_areas;
import model.Uc_job_applications;
import model.Uc_position;
import utils.RouteManager;
import utils.SessionManager;
import utils.ViewManager;

@WebServlet("/admin/home")
public class AdminHome extends HttpServlet {
	private static final long serialVersionUID = 1L;

	private Uc_admin_types ucAdminTypes;
	private Model model;
	public AdminHome() {
        super();
        model = new Model();
    }

	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		Hashtable sessionAttributes = new Hashtable();
		sessionAttributes = (Hashtable) SessionManager.sessionExists(request, "admin");
		if(Integer.parseInt(sessionAttributes.get("utype").toString()) == UCConstants.MAIN_ADMIN) {
			ViewManager.showView(request, response, "admin/home.jsp");
		} else if(Integer.parseInt(sessionAttributes.get("utype").toString()) == UCConstants.SALES_ADMIN) {
			ViewManager.showView(request, response, "admin/saleshome.jsp");
		} else if(Integer.parseInt(sessionAttributes.get("utype").toString()) == UCConstants.TELECALLER_ADMIN) {
			ViewManager.showView(request, response, "admin/telecallerhome.jsp");
		} else if(Integer.parseInt(sessionAttributes.get("utype").toString()) == UCConstants.SUB_MAIN_ADMIN) {
			ViewManager.showView(request, response, "admin/home.jsp");
		} else if(Integer.parseInt(sessionAttributes.get("utype").toString()) == UCConstants.CANDIDATE_REGISTER_ADMIN) {
			ViewManager.showView(request, response, "admin/candidateregisterhome.jsp");
		} else if(Integer.parseInt(sessionAttributes.get("utype").toString()) == UCConstants.ADD_REQUIREMENT_ADMIN) {
			ViewManager.showView(request, response, "admin/addrequirementhome.jsp");
		}		
	}
	
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		doGet(request, response);
	}
}
