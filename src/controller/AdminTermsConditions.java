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
import model.Uc_terms_conditions;
import utils.RouteManager;
import utils.SessionManager;
import utils.ViewManager;

@WebServlet("/admin/terms")
public class AdminTermsConditions extends HttpServlet {
	private static final long serialVersionUID = 1L;
       
	private Uc_terms_conditions ucTerms;
	private List<Uc_terms_conditions> ucTerms_list;
	private Model model;
    public AdminTermsConditions() {
        super();
        model = new Model();
    }

	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		Hashtable sessionAttributes = new Hashtable();
		sessionAttributes = (Hashtable) SessionManager.sessionExists(request, "admin");
		if(request.getParameter("action") == null) {
			if(Integer.parseInt(sessionAttributes.get("utype").toString()) == UCConstants.MAIN_ADMIN) {
				
			} else {
				ViewManager.showView(request, response, "admin/accessdenied.jsp");
			}
			RouteManager.route(response, "admin/terms?action=add");
		} else {
			if(request.getParameter("action").equals("add")) {
				if(Integer.parseInt(sessionAttributes.get("utype").toString()) == UCConstants.MAIN_ADMIN) {
					ViewManager.showView(request, response, "admin/addtc.jsp");
				} else {
					ViewManager.showView(request, response, "admin/accessdenied.jsp");
				}
			} 
			if(request.getParameter("action").equals("modify")) {
				if(Integer.parseInt(sessionAttributes.get("utype").toString()) == UCConstants.MAIN_ADMIN) {
					modify(request, response);
				} else {
					ViewManager.showView(request, response, "admin/accessdenied.jsp");
				}
			}
			if(request.getParameter("action").equals("save")) {
				if(Integer.parseInt(sessionAttributes.get("utype").toString()) == UCConstants.MAIN_ADMIN) {
					
				} else {
					ViewManager.showView(request, response, "admin/accessdenied.jsp");
				}
				save(request, response);
			}
			if(request.getParameter("action").toLowerCase().equals("savechanges")) {
				if(Integer.parseInt(sessionAttributes.get("utype").toString()) == UCConstants.MAIN_ADMIN) {
					
				} else {
					ViewManager.showView(request, response, "admin/accessdenied.jsp");
				}
				saveChanges(request, response);
			}
		}
	}

	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		doGet(request, response);
	}
	
	private void save(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		int count = Integer.parseInt(request.getParameter("count").toString());
		boolean flag = true;
		model.startTransaction();
		for(int i=1 ; i<=count ; i++) {
			String term = request.getParameter("tc" + i);
			term = term.replace("'", "\\'");
			if(!term.toString().equals("")) {
				ucTerms = new Uc_terms_conditions();
				ucTerms.setTc_term(term.toString());
				if(!model.insertData(ucTerms)) {
					flag = false;
					break;
				}
			}
		}
		model.endTransaction();
		if(flag) {
			SessionManager.setSession(request, "success", "Terms and Conditions successfully saved.");
		} else {
			SessionManager.setSession(request, "error", "Something went wrong. Please try again.");
		}
		RouteManager.route(response, "admin/terms?action=add");
	}
	
	private void modify(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		ucTerms_list = new ArrayList<Uc_terms_conditions>();
		ucTerms_list = (List<Uc_terms_conditions>)(Object) model.selectData("model.Uc_terms_conditions", "1");
		StringBuilder temp = new StringBuilder();
		if(ucTerms_list.isEmpty()) {
			temp.append("No terms & conditions are added yet.");
		} else {
			int i = 1;
			for(Uc_terms_conditions tc : ucTerms_list) {
				temp.append("<div class=\"form-group col-lg-12\">");
				temp.append("<textarea class=\"form-control\" onblur=\"saveChanges(this," + tc.getTc_id() + ")\" rows=\"2\" name=\"tc" + i + "\" id=\"tc" + i++ + "\">" + tc.getTc_term() + "</textarea>");
				temp.append("</div>");
			}
		}
		request.setAttribute("terms", temp.toString());
		ViewManager.showView(request, response, "admin/modifytc.jsp");
	}
	
	private void saveChanges(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		long id = Long.parseLong(request.getParameter("id").toString());
		String term = request.getParameter("term").toString();
		term = term.replace("'", "\\'");
		response.setContentType("text/html;charset=UTF-8");
		if(term.equals("")) {
			if(!model.deleteData("model.Uc_terms_conditions", "tc_id=" + id)) response.getWriter().write("error");
			else response.getWriter().write("success");
		} else {
			ucTerms = new Uc_terms_conditions();
			ucTerms.setTc_term(term);
			if(!model.updateData(ucTerms, "tc_id=" + id)) response.getWriter().write("error");
			else response.getWriter().write("success");
		}
	}

}
