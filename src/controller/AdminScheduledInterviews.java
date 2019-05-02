package controller;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
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
import model.Uc_candidate_details;
import model.Uc_city;
import model.Uc_current_jobs;
import model.Uc_district;
import model.Uc_employer_details;
import model.Uc_interviews;
import model.Uc_job_applications;
import model.Uc_position;
import model.Uc_state;
import model.Uc_taluka;
import utils.SessionManager;
import utils.ViewManager;

@WebServlet("/admin/interviews")
public class AdminScheduledInterviews extends HttpServlet {

	private Model model;
	private List<Uc_interviews> ucInterviews_list;
	private List<Uc_current_jobs> ucCurrJobs_list;
	private List<Uc_employer_details> ucEmployer_list;
	private Uc_interviews ucInterview;
	private Uc_current_jobs ucCurrJob;
	private Uc_position ucPosition;
	private Uc_employer_details ucEmployer;
	private List<Uc_job_applications> ucJobApps_list;
	private Uc_city ucCity;
	private Uc_district ucDistrict;
	private Uc_taluka ucTaluka;
	private Uc_state ucState;
	private Uc_candidate_details ucCandidate;
	private Uc_job_applications ucJobApp;
	private List<Uc_candidate_details> ucCandidate_list;
    public AdminScheduledInterviews() {
        super();
        model = new Model();
    }

	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		Hashtable sessionAttributes = new Hashtable();
		sessionAttributes = (Hashtable) SessionManager.sessionExists(request, "admin");
		if(request.getParameter("action") == null || request.getParameter("action").equals("")) {
			if(Integer.parseInt(sessionAttributes.get("utype").toString()) == UCConstants.MAIN_ADMIN || Integer.parseInt(sessionAttributes.get("utype").toString()) == UCConstants.SUB_MAIN_ADMIN || Integer.parseInt(sessionAttributes.get("utype").toString()) == UCConstants.SALES_ADMIN || Integer.parseInt(sessionAttributes.get("utype").toString()) == UCConstants.TELECALLER_ADMIN) {
				ViewManager.showView(request, response, "admin/interviews.jsp");
			} else {
				ViewManager.showView(request, response, "admin/accessdenied.jsp");
			}
		} else {
			if(request.getParameter("action").toLowerCase().equals("getinterviewlist")) {
				if(Integer.parseInt(sessionAttributes.get("utype").toString()) == UCConstants.MAIN_ADMIN || Integer.parseInt(sessionAttributes.get("utype").toString()) == UCConstants.SUB_MAIN_ADMIN || Integer.parseInt(sessionAttributes.get("utype").toString()) == UCConstants.SALES_ADMIN || Integer.parseInt(sessionAttributes.get("utype").toString()) == UCConstants.TELECALLER_ADMIN) {
					try {
						getInterviewList(request,response);
					} catch(Exception ex) {
						LogManager.appendToExceptionLogs(ex);
					}
				} else {
					ViewManager.showView(request, response, "admin/accessdenied.jsp");
				}
			} else if(request.getParameter("action").toLowerCase().equals("showcandidatedetails")) {
				if(Integer.parseInt(sessionAttributes.get("utype").toString()) == UCConstants.MAIN_ADMIN || Integer.parseInt(sessionAttributes.get("utype").toString()) == UCConstants.SUB_MAIN_ADMIN || Integer.parseInt(sessionAttributes.get("utype").toString()) == UCConstants.SALES_ADMIN || Integer.parseInt(sessionAttributes.get("utype").toString()) == UCConstants.TELECALLER_ADMIN) {
					try {
						showCandidateDetails(request,response);
					} catch(Exception ex) {
						LogManager.appendToExceptionLogs(ex);
					}
				} else {
					ViewManager.showView(request, response, "admin/accessdenied.jsp");
				}
			} else if(request.getParameter("action").toLowerCase().equals("showemployerdetails")) {
				if(Integer.parseInt(sessionAttributes.get("utype").toString()) == UCConstants.MAIN_ADMIN || Integer.parseInt(sessionAttributes.get("utype").toString()) == UCConstants.SUB_MAIN_ADMIN || Integer.parseInt(sessionAttributes.get("utype").toString()) == UCConstants.SALES_ADMIN || Integer.parseInt(sessionAttributes.get("utype").toString()) == UCConstants.TELECALLER_ADMIN) {
					try {
						showEmployerDetails(request,response);
					} catch(Exception ex) {
						LogManager.appendToExceptionLogs(ex);
					}
				} else {
					ViewManager.showView(request, response, "admin/accessdenied.jsp");
				}
			}
		}
	}

	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		doGet(request, response);
	}
	
	private void getInterviewList(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException, JSONException {
		int pageNo = Integer.parseInt(request.getParameter("pageNo").toString());
		int limit = Integer.parseInt(request.getParameter("limit").toString());
		boolean begin = request.getParameter("begin").toLowerCase().equals("true") ? true : false;
		int start = (pageNo-1)*limit;
		boolean hasExtra = false;
		int count = 0;
		Hashtable sessionAttributes = new Hashtable();
		sessionAttributes = (Hashtable) SessionManager.sessionExists(request, "admin");
		
		Date date = new Date();
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
		
		StringBuilder where = new StringBuilder();
		if(request.getParameter("isFiltered").toLowerCase().equals("true")) {
			String temppostfix = "(";
			String[] filters = request.getParameterValues("filters[]");
			for(String filter : filters) {
				where.append(temppostfix);
				temppostfix = " and ";
				if(filter.equals("1")) {
					where.append("i_date='" + sdf.format(date) + "'");
				} else if(filter.equals("2")) {
					where.append("i_date>'" + sdf.format(date) + "'");
				} else if(filter.equals("3")) {
					where.append("i_date<'" + sdf.format(date) + "'");
				} 
			}
			where.append(")");
		}
		
		if(request.getParameter("extraParameters") != "" && request.getParameter("extraParameters") == null) {
				String postfix = "";
				while(true) {
				
					if(request.getParameterValues("extraParameters[" + count + "][]") == null) break;
					hasExtra = true;
					String val1 = request.getParameterValues("extraParameters[" + count + "][]")[0];
					String val2 = request.getParameterValues("extraParameters[" + count + "][]")[1];
					count++;
				
					where.append(postfix);
					postfix = " and ";
					
					if(val1.equals("1")) {
						ucCurrJobs_list = new ArrayList<Uc_current_jobs>();
						ucCurrJobs_list = (List<Uc_current_jobs>)(Object) model.selectData("model.Uc_current_jobs", "cj_employer_id=" + Integer.parseInt(val2));
						if(!ucCurrJobs_list.isEmpty()) {
							String temppostfix = "(";
							for(Uc_current_jobs cj : ucCurrJobs_list) {
								where.append(temppostfix);
								temppostfix = " or ";
								where.append("i_job_id=" + cj.getCj_id());
							}
							where.append(")");
						} else {
							where.append("0");
						}
					} else if(val1.equals("2")) {
						ucEmployer_list = new ArrayList<Uc_employer_details>();
						ucEmployer_list = (List<Uc_employer_details>)(Object) model.selectData("model.Uc_employer_details", "ed_firm_name like '%" + val2 + "%'");
						if(!ucEmployer_list.isEmpty()) {
							String temptemppostfix = "(";
							for(Uc_employer_details ed : ucEmployer_list) {
								where.append(temptemppostfix);
								temptemppostfix = " or ";
								ucCurrJobs_list = new ArrayList<Uc_current_jobs>();
								ucCurrJobs_list = (List<Uc_current_jobs>)(Object) model.selectData("model.Uc_current_jobs", "cj_employer_id=" + ed.getEd_id());
								if(!ucCurrJobs_list.isEmpty()) {
									String temppostfix = "(";
									for(Uc_current_jobs cj : ucCurrJobs_list) {
										where.append(temppostfix);
										temppostfix = " or ";
										where.append("i_job_id=" + cj.getCj_id());
									}
									where.append(")");
								} else {
									where.append("0");
								}
							}
							where.append(")");
						} else {
							where.append("0");
						}
					} else if(val1.equals("3")) {
						where.append("i_id=" + Integer.parseInt(val2));
					} else if(val1.equals("4")) {
						where.append("i_job_id=" + Integer.parseInt(val2));
					} else if(val1.equals("5")) {
						ucJobApps_list = new ArrayList<Uc_job_applications>();
						ucJobApps_list = (List<Uc_job_applications>)(Object) model.selectData("model.Uc_job_applications", "ja_candidate=" + Integer.parseInt(val2) + " and ja_confirm_final=1");
						if(!ucJobApps_list.isEmpty()) {
							String temppostfix = "(";
							for(Uc_job_applications ja : ucJobApps_list) {
								where.append(temppostfix);
								temppostfix = " or ";
								where.append("i_job_id=" + ja.getJa_job());
							}
							where.append(")");
						} else {
							where.append("0");
						}
					}  else if(val1.equals("6")) {
						ucCandidate_list = new ArrayList<Uc_candidate_details>();
						ucCandidate_list = (List<Uc_candidate_details>)(Object) model.selectData("model.Uc_candidate_details", "cd_name like '%" + val2 + "%'");
						if(!ucCandidate_list.isEmpty()) {
							String temppostfix = "(";
							for(Uc_candidate_details cd : ucCandidate_list) {
								where.append(temppostfix);
								temppostfix = " or ";
								ucJobApps_list = new ArrayList<Uc_job_applications>();
								ucJobApps_list = (List<Uc_job_applications>)(Object) model.selectData("model.Uc_job_applications", "ja_candidate=" + cd.getCd_id() + " and ja_confirm_final=1");
								if(!ucJobApps_list.isEmpty()) {
									String temptemppostfix = "(";
									for(Uc_job_applications ja : ucJobApps_list) {
										where.append(temptemppostfix);
										temppostfix = " or ";
										where.append("i_job_id=" + ja.getJa_job());
									}
									where.append(")");
								} else {
									where.append("0");
								}
							}
							where.append(")");
						} else {
							where.append("0");
						}
					}
				}
		}
		
		if(where.toString().equals("")) where.append("1");
		//false result.. error in where building
		
		ucInterviews_list = new ArrayList<Uc_interviews>();
		ucInterviews_list = (List<Uc_interviews>)(Object) model.selectData("model.Uc_interviews", where.toString() + " order by i_id desc LIMIT " + start + "," + limit);
		if(ucInterviews_list.isEmpty()) {
			response.setContentType("application/json");
			JSONObject jsonResponse = new JSONObject();
			jsonResponse.put("table", "No interviews are scheduled yet.");
			jsonResponse.put("pageCount", 0);
			response.getWriter().write(jsonResponse.toString());
		} else {
			StringBuilder temp = new StringBuilder();
			
				temp.append("<table class=\"table table-bordered\" width=\"100%\"><tr><th>Sr. No.</th><th>Interview Id</th><th>Job Id</th><th>Position</th><th>Employer name (Id)</th><th>Location</th><th>Date & Time</th><th>Number of candidates</th><th>Details</th></tr>");
			
			
			for(int i = 0 ; i < ucInterviews_list.size() ; i++) {
				ucInterview = new Uc_interviews();
				ucInterview = ucInterviews_list.get(i);
				String bgColor = "";
				
				if(ucInterview.getI_date().equals(new Date(Calendar.getInstance().getTime().getTime()))) bgColor = UCConstants.TODAY_INTERVIEWS;
				else if(ucInterview.getI_date().after(new Date(Calendar.getInstance().getTime().getTime()))) bgColor = UCConstants.FUTURE_INTERVIEWS;
				else if(ucInterview.getI_date().before(new Date(Calendar.getInstance().getTime().getTime()))) bgColor = UCConstants.PAST_INTERVIEWS;
				temp.append("<tr style=\"background:" + bgColor + ";color:#fff\">");
				temp.append("<td>" + (i+1) + "</td>");
				temp.append("<td>" + ucInterview.getI_id() + "</td>");
				temp.append("<td>" + ucInterview.getI_job_id() + "</td>");
				ucCurrJob = new Uc_current_jobs();
				ucCurrJob = (Uc_current_jobs) model.selectData("model.Uc_current_jobs", "cj_id=" + ucInterview.getI_job_id()).get(0);
				ucPosition = new Uc_position();
				ucPosition = (Uc_position) model.selectData("model.Uc_position", "p_id=" + ucCurrJob.getCj_position()).get(0);
				
				temp.append("<td>" + ucPosition.getP_name() + "</td>");
				
				ucEmployer = new Uc_employer_details();
				ucEmployer = (Uc_employer_details) model.selectData("model.Uc_employer_details", "ed_id=" + ucCurrJob.getCj_employer_id()).get(0);
				if(Integer.parseInt(sessionAttributes.get("utype").toString()) == UCConstants.SALES_ADMIN || Integer.parseInt(sessionAttributes.get("utype").toString()) == UCConstants.MAIN_ADMIN || Integer.parseInt(sessionAttributes.get("utype").toString()) == UCConstants.SUB_MAIN_ADMIN) {
					temp.append("<td>" + ucEmployer.getEd_firm_name() + " ("  + ucEmployer.getEd_id() + ")</td>");
				} else {
					temp.append("<td>" + ucEmployer.getEd_firm_name() + "</td>");
				}
				
				if(ucInterview.getI_location().equals("same")) 
					temp.append("<td>At employer's office (same address)</td>");
				else 
					temp.append("<td>" + ucInterview.getI_location() + "</td>");
				
				temp.append("<td>" + ucInterview.getI_date() + " - " + ucInterview.getI_time() + "</td>");
				
				ucJobApps_list = new ArrayList<Uc_job_applications>();
				ucJobApps_list = (List<Uc_job_applications>)(Object) model.selectData("model.Uc_job_applications", "ja_job=" + ucInterview.getI_job_id() + " and ja_confirm_final=1");
				temp.append("<td>" + ucJobApps_list.size() + "</td>");
				
					temp.append("<td><button type=\"button\" onclick=\"showDetails(this," + ucInterview.getI_id() + ")\" class=\"btn btn-info\">Details</button></td>");
				
				temp.append("</tr>");
			}
			temp.append("</table>");
			response.setContentType("application/json");
			 JSONObject jsonResponse = new JSONObject();
			 jsonResponse.put("table", temp.toString());
			 if(begin) {
				 ucInterviews_list = new ArrayList<Uc_interviews>();
				 ucInterviews_list = (List<Uc_interviews>)(Object) model.selectData("model.Uc_interviews", where.toString());
				 int pageCount = (int) Math.ceil(ucInterviews_list.size()/(float)limit);
				 if(pageCount == 0) pageCount = 1;
				 jsonResponse.put("pageCount", pageCount);
			 }
				 response.getWriter().write(jsonResponse.toString());
		}
		
	}
	
	private void showEmployerDetails(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException, JSONException {
		Hashtable sessionAttributes = new Hashtable();
		sessionAttributes = (Hashtable) SessionManager.sessionExists(request, "admin");
		int interviewId = Integer.parseInt(request.getParameter("interviewId"));
		ucInterview = new Uc_interviews();
		ucInterview = (Uc_interviews) model.selectData("model.Uc_interviews", "i_id=" + interviewId).get(0);
		ucCurrJob = new Uc_current_jobs();
		ucCurrJob = (Uc_current_jobs) model.selectData("model.Uc_current_jobs", "cj_id=" + ucInterview.getI_job_id()).get(0);
		ucEmployer = new Uc_employer_details();
		ucEmployer = (Uc_employer_details) model.selectData("model.Uc_employer_details", "ed_id=" + ucCurrJob.getCj_employer_id()).get(0);
		StringBuilder temp = new StringBuilder();
		temp.append("<table class=\"table table-striped table-bordered\">");
			//if(Integer.parseInt(sessionAttributes.get("utype").toString()) == UCConstants.SALES_ADMIN || Integer.parseInt(sessionAttributes.get("utype").toString()) == UCConstants.MAIN_ADMIN || Integer.parseInt(sessionAttributes.get("utype").toString()) == UCConstants.SUB_MAIN_ADMIN) {
				temp.append("<tr><th>Employer Name (Id)</th><td>" + ucEmployer.getEd_firm_name() + " (" + ucEmployer.getEd_id() + ")</td></tr>");
				/*ucCity = new Uc_city();
				ucCity = (Uc_city) model.selectData("model.Uc_city", "c_id=" + ucEmployer.getEd_city_id()).get(0);
				ucTaluka = new Uc_taluka();
				ucTaluka = (Uc_taluka) model.selectData("model.Uc_taluka", "t_id=" + ucCity.getC_taluka_id()).get(0);
				ucDistrict = new Uc_district();
				ucDistrict = (Uc_district) model.selectData("model.Uc_district", "d_id=" + ucTaluka.getT_district_id()).get(0);
				ucState = new Uc_state();
				ucState = (Uc_state) model.selectData("model.Uc_state", "s_id=" + ucDistrict.getD_s_id()).get(0);*/
				/*temp.append("<tr><th>City</th><td>" + ucCity.getC_name() + ", " + ucTaluka.getT_name() + ", " + ucDistrict.getD_name() + ", " + ucState.getS_name() + "</td></tr>");
				temp.append("<tr><th>Address</th><td>" + ucEmployer.getEd_address() + "</td></tr>");
				temp.append("<tr><th>Contact person</th><td>" + ucEmployer.getEd_contact_person() + "</td></tr>");
				temp.append("<tr><th>Contact number</th><td>" + ucEmployer.getEd_contact_no() + "</td></tr>");*/
				if(ucInterview.getI_location().equals("same")) 
					temp.append("<tr><th>Interview Location</th><td>" + ucEmployer.getEd_address() + "</td></tr>");
				else 
					temp.append("<tr><th>Interview Location</th><td>" + ucInterview.getI_location() + "</td></tr>");
				
				temp.append("<tr><th>Interview Date</th><td>" + ucInterview.getI_date() + "</td></tr>");
				temp.append("<tr><th>Interview Time</th><td>" + ucInterview.getI_time() + "</td></tr>");
				temp.append("<tr><th>Contact Person</th><td>" + (ucInterview.getI_contact_person().toLowerCase().equals("n/a") ? ucEmployer.getEd_contact_person() : ucInterview.getI_contact_person()) + "</td></tr>");
				
			
		temp.append("</table>");
		JSONObject jsonResponse = new JSONObject();
		jsonResponse.put("details", temp.toString());
		response.setContentType("application/json");
		response.getWriter().write(jsonResponse.toString());
	}
	
	private void showCandidateDetails(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException, JSONException {
		int pageNo = Integer.parseInt(request.getParameter("pageNo").toString());
		int limit = Integer.parseInt(request.getParameter("limit").toString());
		boolean begin = request.getParameter("begin").toLowerCase().equals("true") ? true : false;
		int start = (pageNo-1)*limit;
		
		Hashtable sessionAttributes = new Hashtable();
		sessionAttributes = (Hashtable) SessionManager.sessionExists(request, "admin");
		int interviewId = Integer.parseInt(request.getParameterValues("extraParameters[0][]")[0].toString());
		ucInterview = new Uc_interviews();
		ucInterview = (Uc_interviews) model.selectData("model.Uc_interviews", "i_id=" + interviewId).get(0);
		ucJobApps_list = new ArrayList<Uc_job_applications>();
		ucJobApps_list = (List<Uc_job_applications>)(Object) model.selectData("model.Uc_job_applications", "ja_job=" + ucInterview.getI_job_id() + " and ja_confirm_final=1 order by ja_id desc LIMIT " + start + "," + limit);
		int count = 1;
		if(ucJobApps_list.isEmpty()) {
			response.setContentType("application/json");
			JSONObject jsonResponse = new JSONObject();
			jsonResponse.put("table", "No job applications found.");
			jsonResponse.put("pageCount", 0);
			response.getWriter().write(jsonResponse.toString());
		} else {
		StringBuilder temp = new StringBuilder();
		if(Integer.parseInt(sessionAttributes.get("utype").toString()) == UCConstants.TELECALLER_ADMIN || Integer.parseInt(sessionAttributes.get("utype").toString()) == UCConstants.MAIN_ADMIN || Integer.parseInt(sessionAttributes.get("utype").toString()) == UCConstants.SUB_MAIN_ADMIN) {
			temp.append("<table class=\"table table-striped table-bordered\"><tr><th>Sr. No.</th><th>Candidate name (Id)</th><th>Contact number</th><th>Email Id</th></tr>");
			for(Uc_job_applications ja : ucJobApps_list) {
				ucCandidate = new Uc_candidate_details();
				ucCandidate = (Uc_candidate_details) model.selectData("model.Uc_candidate_details", "cd_id=" + ja.getJa_candidate()).get(0);
				temp.append("<tr>");
				temp.append("<td>" + (count++) + "</td>");
				temp.append("<td>" + ucCandidate.getCd_name() + " (" + ucCandidate.getCd_id() + ")</td>");
				temp.append("<td>" + ucCandidate.getCd_contact_num() + "</td>");
				temp.append("<td>" + ucCandidate.getCd_email() + "</td>");
				temp.append("</tr>");
			}
		} else {
			temp.append("<table class=\"table table-striped table-bordered\"><tr><th>Sr. No.</th><th>Candidate name</th></tr>");
			for(Uc_job_applications ja : ucJobApps_list) {
				ucCandidate = new Uc_candidate_details();
				ucCandidate = (Uc_candidate_details) model.selectData("model.Uc_candidate_details", "cd_id=" + ja.getJa_candidate()).get(0);
				temp.append("<tr>");
				temp.append("<td>" + (count++) + "</td>");
				temp.append("<td>" + ucCandidate.getCd_name() + "</td>");
				temp.append("</tr>");
			}
		}
		temp.append("</table>");
		response.setContentType("application/json");
		 JSONObject jsonResponse = new JSONObject();
		 jsonResponse.put("table", temp.toString());
		 if(begin) {
			 ucJobApps_list = new ArrayList<Uc_job_applications>();
				ucJobApps_list = (List<Uc_job_applications>)(Object) model.selectData("model.Uc_job_applications", "ja_id=" + ucInterview.getI_job_id() + " order by ja_id desc");
			 int pageCount = (int) Math.ceil(ucJobApps_list.size()/(float)limit);
			 if(pageCount == 0) pageCount = 1;
			 jsonResponse.put("pageCount", pageCount);
		 }
			 response.getWriter().write(jsonResponse.toString());
		}
	}

}
