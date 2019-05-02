package controller;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Hashtable;
import java.util.List;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import database.Model;
import model.Uc_current_jobs;
import model.Uc_interviews;
import model.Uc_job_applications;
import model.Uc_position;
import utils.EncryptionManager;
import utils.SessionManager;
import utils.ViewManager;

@WebServlet("/applied")
public class AppliedJobs extends HttpServlet {
	private static final long serialVersionUID = 1L;

	private List<Uc_job_applications> ucJobApps_list;
	private Model model;
	private Uc_current_jobs ucCurrJob;
	private Uc_interviews ucInterview;
	private List<Uc_interviews> ucInterview_list;
	private List<Uc_current_jobs> ucCurrJob_list;
	private Uc_position ucPosition;
	public AppliedJobs() {
        super();
        model = new Model();
    }

	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		if(request.getParameter("action") == null || request.getParameter("action").toString().equals("")) {
			showAppliedJobs(request,response);
		} else {
			if(request.getParameter("action").toLowerCase().equals("cancelapplication")) {
				cancelApplication(request, response);
			}
		}
	}

	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		doGet(request, response);
	}
	
	private void showAppliedJobs(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		Hashtable sessionAttributes = new Hashtable();
		sessionAttributes = (Hashtable) SessionManager.sessionExists(request, "uc_client_session");
		StringBuilder temp = new StringBuilder();
		ucJobApps_list = new ArrayList<Uc_job_applications>();
		ucJobApps_list = (List<Uc_job_applications>)(Object) model.selectData("model.Uc_job_applications", "ja_candidate=" + Integer.parseInt(sessionAttributes.get("id").toString()) + " order by ja_id desc");
		if(!ucJobApps_list.isEmpty()) {
			for(Uc_job_applications ja : ucJobApps_list) {
				ucCurrJob_list = new ArrayList<Uc_current_jobs>();
				ucCurrJob_list = (List<Uc_current_jobs>)(Object) model.selectData("model.Uc_current_jobs", "cj_id=" + ja.getJa_job() + " and cj_is_active=1");
				if(!ucCurrJob_list.isEmpty()) {
					ucCurrJob = new Uc_current_jobs();
					ucCurrJob = ucCurrJob_list.get(0);
					ucInterview_list = new ArrayList<Uc_interviews>();
					ucInterview_list = (List<Uc_interviews>)(Object) model.selectData("model.Uc_interviews", "i_job_id=" + ucCurrJob.getCj_id());
					if(!ucInterview_list.isEmpty()) {
						ucInterview = new Uc_interviews();
						ucInterview = ucInterview_list.get(0);
						java.sql.Date interviewDate = ucInterview.getI_date();
						Calendar c = Calendar.getInstance();
						c.setTime(interviewDate);
						c.add(Calendar.DATE, 10);
						interviewDate = new java.sql.Date(c.getTime().getTime());
						if(interviewDate.equals(new java.sql.Date(Calendar.getInstance().getTime().getTime())) || new java.sql.Date(Calendar.getInstance().getTime().getTime()).before(interviewDate)) {
							//data available with interview schedule
							
							if(ja.getJa_confirm_pre_final() == 2 && ucCurrJob.getCj_approved_by_sales() !=0) {
								//pending
								temp.append("<tr style=\"color:#fff;background:" + UCConstants.PENDING_APPLICATION + "\">");
							} else if(ja.getJa_confirm_pre_final() == 1 && ucCurrJob.getCj_approved_by_sales() !=0) {
								//accepted
								temp.append("<tr style=\"color:#fff;background:" + UCConstants.ACCEPTED_APPLICATION + "\">");
							} else {
								//rejected
								temp.append("<tr style=\"color:#fff;background:" + UCConstants.REJECTED_APPLICATION + "\">");
							}
							ucPosition = new Uc_position();
							ucPosition = (Uc_position) model.selectData("model.Uc_position", "p_id=" + ucCurrJob.getCj_position()).get(0);
							temp.append("<td>" + ucPosition.getP_name() + "</td>");
							temp.append("<td>" + ja.getJa_date() + "</td>");
							if(ja.getJa_confirm_pre_final() == 2 && ucCurrJob.getCj_approved_by_sales() !=0) {
								//pending
								temp.append("<td>Pending</td>");
							} else if(ja.getJa_confirm_pre_final() == 1 && ucCurrJob.getCj_approved_by_sales() !=0) {
								//accepted
								temp.append("<td>Accepted</td>");
							} else {
								//rejected
								temp.append("<td>Rejected (<a style=\"cursor:pointer\" onclick=\"showReason('" + ja.getJa_reject_reason() + "')\">Reason</a>)</td>");
							}
							temp.append("<td><button type=\"button\" onclick=\"showJobDetails('" + EncryptionManager.encryptData(String.valueOf(ucCurrJob.getCj_id())) + "')\" class=\"btn btn-primary\">Details</button></td>");
							if(ja.getJa_confirm_final() == 1 && ucCurrJob.getCj_approved_by_sales() !=0) {
								temp.append("<td><button type=\"button\" onclick=\"viewSchedule('" + EncryptionManager.encryptData(String.valueOf(ucCurrJob.getCj_id())) + "')\" class=\"btn btn-warning\">View Schedule</button></td>");
							} else {
								temp.append("<td><button type=\"button\" class=\"btn btn-warning disabled\">No schedule available</button></td>");
							}
							
							if(ja.getJa_confirm_pre_final() == 2 && ucCurrJob.getCj_approved_by_sales() !=0) {
								temp.append("<td><button type=\"button\" onclick=\"cancelApplication('" + EncryptionManager.encryptData(String.valueOf(ucCurrJob.getCj_id())) + "')\" class=\"btn btn-danger\"><i class=\"fa fa-times\"></i></button></td>");
							} else {
								temp.append("<td><button type=\"button\" class=\"btn btn-danger disabled\"><i class=\"fa fa-times\"></i></button></td>");
							}
							temp.append("</tr>");
						}
					} else {
						//data available without interview schedule
						
						if(ja.getJa_confirm_pre_final() == 2 && ucCurrJob.getCj_approved_by_sales() !=0) {
							//pending
							temp.append("<tr style=\"color:#fff;background:" + UCConstants.PENDING_APPLICATION + "\">");
						} else if(ja.getJa_confirm_pre_final() == 1 && ucCurrJob.getCj_approved_by_sales() !=0) {
							//accepted
							temp.append("<tr style=\"color:#fff;background:" + UCConstants.ACCEPTED_APPLICATION + "\">");
						} else {
							//rejected
							temp.append("<tr style=\"color:#fff;background:" + UCConstants.REJECTED_APPLICATION + "\">");
						}
						ucPosition = new Uc_position();
						ucPosition = (Uc_position) model.selectData("model.Uc_position", "p_id=" + ucCurrJob.getCj_position()).get(0);
						temp.append("<td>" + ucPosition.getP_name() + "</td>");
						temp.append("<td>" + ja.getJa_date() + "</td>");
						if(ja.getJa_confirm_pre_final() == 2 && ucCurrJob.getCj_approved_by_sales() !=0) {
							//pending
							temp.append("<td>Pending</td>");
						} else if(ja.getJa_confirm_pre_final() == 1 && ucCurrJob.getCj_approved_by_sales() !=0) {
							//accepted
							temp.append("<td>Accepted</td>");
						} else {
							//rejected
							temp.append("<td>Rejected (<a style=\"cursor:pointer\" onclick=\"showReason('" + ja.getJa_reject_reason() + "')\">Reason</a>)</td>");
						}
						temp.append("<td><button type=\"button\" onclick=\"showJobDetails('" + EncryptionManager.encryptData(String.valueOf(ucCurrJob.getCj_id())) + "')\" class=\"btn btn-primary\">Details</button></td>");
						temp.append("<td><button type=\"button\" class=\"btn btn-warning disabled\">No schedule available</button></td>");
						if(ja.getJa_confirm_pre_final() == 2 && ucCurrJob.getCj_approved_by_sales() !=0) {
							temp.append("<td><button type=\"button\" onclick=\"cancelApplication('" + EncryptionManager.encryptData(String.valueOf(ucCurrJob.getCj_id())) + "')\" class=\"btn btn-danger\"><i class=\"fa fa-times\"></i></button></td>");
						} else {
							temp.append("<td><button type=\"button\" class=\"btn btn-danger disabled\"><i class=\"fa fa-times\"></i></button></td>");
						}
						temp.append("</tr>");
					}
				}
			}
		}
		if(temp.toString().equals("")) {
			request.setAttribute("jobsAvail", "0");
		} else {
			request.setAttribute("jobsAvail", "1");
		}
		request.setAttribute("jobs", temp.toString());
		ViewManager.showView(request, response, "appliedjobs.jsp");
	}
	
	private void cancelApplication(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		int jobId = Integer.parseInt(EncryptionManager.decryptData(request.getParameter("jobId").toString()));
		Hashtable sessionAttributes = new Hashtable();
		sessionAttributes = (Hashtable) SessionManager.sessionExists(request, "uc_client_session");
		ucJobApps_list = new ArrayList<Uc_job_applications>();
		ucJobApps_list = (List<Uc_job_applications>)(Object) model.selectData("model.Uc_job_applications", "ja_job=" + jobId + " and ja_confirm_pre_final=2 and ja_candidate=" + Integer.parseInt(sessionAttributes.get("id").toString()));
		response.setContentType("text/html;charset=UTF-8");
		if(!ucJobApps_list.isEmpty()) {
			if(model.deleteData("model.Uc_job_applications", "ja_id=" + ucJobApps_list.get(0).getJa_id())) {
				response.getWriter().write("success");
			} else {
				response.getWriter().write("error");
			}
		} else {
			response.getWriter().write("error");
		}
	}

}
