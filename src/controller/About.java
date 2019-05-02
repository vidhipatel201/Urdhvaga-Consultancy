package controller;

import java.io.IOException;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import database.Model;
import model.Uc_firm_details;
import utils.ViewManager;

@WebServlet("/about")
public class About extends HttpServlet {
	private static final long serialVersionUID = 1L;
       
	private Uc_firm_details ucFirm;
	private Model model;
    public About() {
        super();
        model = new Model();
    }

	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		ucFirm = new Uc_firm_details();
		ucFirm = (Uc_firm_details) model.selectData("model.Uc_firm_details", "1").get(0);
		request.setAttribute("ucFirm", ucFirm);
		ViewManager.showView(request, response, "about.jsp");
	}

	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		doGet(request, response);
	}

}
