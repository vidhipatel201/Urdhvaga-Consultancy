package controller;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import database.Model;
import model.Uc_terms_conditions;
import utils.ViewManager;

@WebServlet("/terms-conditions")
public class Terms extends HttpServlet {
	private static final long serialVersionUID = 1L;
       
	private List<Uc_terms_conditions> ucTerms_list;
	private Model model;
    public Terms() {
        super();
        model = new Model();
    }

	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		if(request.getParameter("action") == null) {
			ucTerms_list = new ArrayList<Uc_terms_conditions>();
			ucTerms_list = (List<Uc_terms_conditions>)(Object) model.selectData("model.Uc_terms_conditions", "1");
			StringBuilder temp = new StringBuilder();
			temp.append("<ul>");
			for(Uc_terms_conditions tc : ucTerms_list) {
				temp.append("<li>" + tc.getTc_term());
			}
			temp.append("</ul>");
			request.setAttribute("terms", temp.toString());
			ViewManager.showView(request, response, "terms.jsp");
		} else {
			if(request.getParameter("action").equals("")) {
				ucTerms_list = new ArrayList<Uc_terms_conditions>();
				ucTerms_list = (List<Uc_terms_conditions>)(Object) model.selectData("model.Uc_terms_conditions", "1");
				StringBuilder temp = new StringBuilder();
				temp.append("<ul>");
				for(Uc_terms_conditions tc : ucTerms_list) {
					temp.append("<li>" + tc.getTc_term());
				}
				temp.append("</ul>");
				request.setAttribute("terms", temp.toString());
				ViewManager.showView(request, response, "terms.jsp");
			} else {
				if(request.getParameter("action").equalsIgnoreCase("getTC")) {
					getTC(request, response);
				}
			}
		}
	}

	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		doGet(request, response);
	}
	
	private void getTC(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		ucTerms_list = new ArrayList<Uc_terms_conditions>();
		ucTerms_list = (List<Uc_terms_conditions>)(Object) model.selectData("model.Uc_terms_conditions", "1");
		StringBuilder temp = new StringBuilder();
		temp.append("<ul>");
		for(Uc_terms_conditions tc : ucTerms_list) {
			temp.append("<li>" + tc.getTc_term());
		}
		temp.append("</ul>");
		response.setContentType("text/html;charset=UTF-8");
		response.getWriter().write(temp.toString());
	}

}
