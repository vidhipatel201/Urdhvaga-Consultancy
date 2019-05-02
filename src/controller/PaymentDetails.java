package controller;

import java.io.IOException;
import java.util.List;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import database.Model;
import model.Uc_firm_details;
import utils.ViewManager;

@WebServlet("/bank-details")
public class PaymentDetails extends HttpServlet {
	private static final long serialVersionUID = 1L;
       
	private Uc_firm_details ucFirm;
	private Model model;
    public PaymentDetails() {
        super();
        model = new Model();
    }

	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		ucFirm = new Uc_firm_details();
		ucFirm = (Uc_firm_details) model.selectData("model.Uc_firm_details", "1").get(0);
		StringBuilder temp = new StringBuilder();
		temp.append("<table class=\"table table-striped\">");
		temp.append("<tr><td>Bank Branch Location: </td><th>" + ucFirm.getFd_bank_branch() + "</th></tr>");
		temp.append("<tr><td>Account Name: </td><th>" + ucFirm.getFd_bank_acc_name() + "</th></tr>");
		temp.append("<tr><td>Account Number: </td><th>" + ucFirm.getFd_bank_acc_no() + "</th></tr>");
		temp.append("<tr><td>Bank IFSC Code: </td><th>" + ucFirm.getFd_bank_ifsc() + "</th></tr>");
		temp.append("</table>");
		request.setAttribute("bankDetails", temp.toString());
		ViewManager.showView(request, response, "accountdetails.jsp");
	}

	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		doGet(request, response);
	}

}
