package controller;

import java.io.IOException;
import java.util.ArrayList;
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
import model.Uc_cj_quantity;
import model.Uc_current_jobs;
import model.Uc_district;
import model.Uc_job_qualifications;
import model.Uc_position;
import model.Uc_qualifications;
import model.Uc_state;
import model.Uc_taluka;
import utils.EncryptionManager;
import utils.RouteManager;
import utils.SessionManager;
import utils.ViewManager;

@WebServlet("/search")
public class JobSearch extends HttpServlet {
	private static final long serialVersionUID = 1L;
       
	Model model;
	List<Uc_position> ucPosition_list;
	List<Uc_city> ucCity_list;
	List<Uc_district> ucDistrict_list;
	List<Uc_current_jobs> ucCurrJobs_list;
	List<Uc_qualifications> ucQualification_list;
	List<Uc_job_qualifications> ucJobQualification_list;
	List<Uc_taluka> ucTaluka_list;
	List<Uc_cj_quantity> ucCjQuantity_list;
	
    public JobSearch() {
        super();
        model = new Model();
    }

	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		if(request.getParameter("position") == null || request.getParameter("location") == null) {
			 
				SessionManager.setSession(request, "searchError", "No or wrong parameters submitted. Please search again.");
				request.setAttribute("positions", generatePositions());
				ViewManager.showView(request, response, "searchresults.jsp");
			
		} else {
			if(request.getParameter("action") == null) {
				request.setAttribute("positions", generatePositions());
				ViewManager.showView(request, response, "searchresults.jsp");
			} else if(request.getParameter("action").toLowerCase().equals("find")) {
				request.setAttribute("positions", generatePositions());
				generateResults(request, response);
			} else {
				//show error by setting session
				request.setAttribute("positions", generatePositions());
				SessionManager.setSession(request, "searchError", "Wrong parameters submitted. Please search again.");
				ViewManager.showView(request, response, "searchresults.jsp");
			}
		}
	}

	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		doGet(request, response);
	}
	
	private void generateResults(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		if(request.getParameter("position") == null && request.getParameter("location") == null) {
			//wrong or null parameters submitted
			try {
				response.setContentType("application/json");
				JSONObject jsonResponse = new JSONObject();
				String noResult = "<div class=\"col-lg-12\"><div class=\"alert alert-danger\">Wrong parameters submitted. Please search again.</div></div>";
				jsonResponse.put("table", noResult);
				jsonResponse.put("pageCount", 0);
				response.getWriter().write(jsonResponse.toString());
				} catch(Exception ex) {
					LogManager.appendToExceptionLogs(ex);
				}
		} else {
			String position = request.getParameter("position").toString();
			String location = request.getParameter("location").toString();
			position = position.replace("'", "\\'");
			location = location.replace("'", "\\'");
			if(position.equals("") && location.equals("")) {
				//empty parameters submitted
				try {
					response.setContentType("application/json");
					JSONObject jsonResponse = new JSONObject();
					String noResult = "<div class=\"col-lg-12\"><div class=\"alert alert-danger\">Please provide either position or location or both to search for.</div></div>";
					jsonResponse.put("table", noResult);
					jsonResponse.put("pageCount", 0);
					response.getWriter().write(jsonResponse.toString());
					} catch(Exception ex) {
						LogManager.appendToExceptionLogs(ex);
					}
			} else {
				if(!position.equals("") && !location.equals("")) {
					//got both parameters
					
					ucDistrict_list = new ArrayList<Uc_district>();
					ucDistrict_list = (List<Uc_district>)(Object) model.selectData("model.Uc_district", "d_name like '%" + location + "%'");
					StringBuilder talukaWhere = new StringBuilder();
					if(!ucDistrict_list.isEmpty()) {
						String temp = "(";
						for(Uc_district d : ucDistrict_list) {
							talukaWhere.append(temp);
							temp = " or ";
							talukaWhere.append("t_district_id=" + d.getD_id());
						}
						talukaWhere.append(")");
					}
					
					ucTaluka_list = new ArrayList<Uc_taluka>();
					if(!talukaWhere.toString().equals("")) {
						ucTaluka_list = (List<Uc_taluka>)(Object) model.selectData("model.Uc_taluka", "t_name like '%" + location + "%' or " + talukaWhere.toString());
					} else {
						ucTaluka_list = (List<Uc_taluka>)(Object) model.selectData("model.Uc_taluka", "t_name like '%" + location + "%'");
					}
					StringBuilder cityWhere = new StringBuilder();
					if(!ucTaluka_list.isEmpty()) {
						String temp = "(";
						for(Uc_taluka t : ucTaluka_list) {
							cityWhere.append(temp);
							temp = " or ";
							cityWhere.append("c_taluka_id=" + t.getT_id());
						}
						cityWhere.append(")");
					}
					
					ucCity_list = new ArrayList<Uc_city>();
				
					if(!cityWhere.toString().equals("")) {
						ucCity_list = (List<Uc_city>)(Object) model.selectData("model.Uc_city", "c_name like '%" + location + "%' or " + cityWhere.toString());
					} else {
						ucCity_list = (List<Uc_city>)(Object) model.selectData("model.Uc_city", "c_name like '%" + location + "%'");
					}
					if(ucCity_list.isEmpty()) {
						//no such location found
						try {
						response.setContentType("application/json");
						JSONObject jsonResponse = new JSONObject();
						String noResult = "<div class=\"col-lg-12\"><div class=\"alert alert-danger\">No job found for the entered keywords. Please try another keywords.</div></div>";
						jsonResponse.put("table", noResult);
						jsonResponse.put("pageCount", 0);
						response.getWriter().write(jsonResponse.toString());
						} catch(Exception ex) {
							LogManager.appendToExceptionLogs(ex);
						}
					} else {
						ucPosition_list = new ArrayList<Uc_position>();
						ucPosition_list = (List<Uc_position>)(Object) model.selectData("model.Uc_position", "p_name like '%" + position + "%'");
						ucQualification_list = new ArrayList<Uc_qualifications>();
						ucQualification_list = (List<Uc_qualifications>)(Object) model.selectData("model.Uc_qualifications", "q_name like '%" + position + "%'");
						if(ucPosition_list.isEmpty() && ucQualification_list.isEmpty()) {
							//no position found
							try {
								response.setContentType("application/json");
								JSONObject jsonResponse = new JSONObject();
								String noResult = "<div class=\"col-lg-12\"><div class=\"alert alert-danger\">No job found for the entered keywords. Please try another keywords.</div></div>";
								jsonResponse.put("table", noResult);
								jsonResponse.put("pageCount", 0);
								response.getWriter().write(jsonResponse.toString());
								} catch(Exception ex) {
									LogManager.appendToExceptionLogs(ex);
								}
						} else {
							StringBuilder jobWhere = new StringBuilder();
							String temp = "(";
							for(Uc_city c : ucCity_list) {
								jobWhere.append(temp);
								temp = " or ";
								jobWhere.append("cj_city=" + c.getC_id());
							}
							jobWhere.append(")");
							temp = "";
							if(!ucPosition_list.isEmpty()) {
								jobWhere.append(" and (");
								for(Uc_position p : ucPosition_list) {
									jobWhere.append(temp);
									temp = " or ";
									jobWhere.append("cj_position=" + p.getP_id());
								}
								jobWhere.append(")");
							}
							if(!ucQualification_list.isEmpty()) {
								jobWhere.append("and 1 and (");
								String temppostfix = "";
								for(Uc_qualifications q : ucQualification_list) {
									ucJobQualification_list = new ArrayList<Uc_job_qualifications>();
									ucJobQualification_list = (List<Uc_job_qualifications>)(Object) model.selectData("model.Uc_job_qualifications", "jq_qualification_id=" + q.getQ_id());
									if(!ucJobQualification_list.isEmpty()) {
										for(Uc_job_qualifications jq : ucJobQualification_list) {
											jobWhere.append(temppostfix);
											temppostfix = " or ";
											jobWhere.append("cj_id=" + jq.getJq_job_id());
										}
									} else {
										jobWhere.append(temppostfix);
										temppostfix = " or ";
										jobWhere.append("0");
									}
								}
								jobWhere.append(")");
							}
							jobWhere.append(" and cj_is_active=1 order by cj_id desc");
							try{
								createJobs(request, response, jobWhere);
							} catch(Exception ex) {
								LogManager.appendToExceptionLogs(ex);
							}
						}
					}
				} else if(!position.equals("") && location.equals("")) {
					//got only position
					ucPosition_list = new ArrayList<Uc_position>();
					ucPosition_list = (List<Uc_position>)(Object) model.selectData("model.Uc_position", "p_name like '%" + position +"%'");
					ucQualification_list = new ArrayList<Uc_qualifications>();
					ucQualification_list = (List<Uc_qualifications>)(Object) model.selectData("model.Uc_qualifications", "q_name like '%" + position + "%'");
					if(ucPosition_list.isEmpty() && ucQualification_list.isEmpty()) {
						try {
							response.setContentType("application/json");
							JSONObject jsonResponse = new JSONObject();
							String noResult = "<div class=\"col-lg-12\"><div class=\"alert alert-danger\">No job found for the entered keywords. Please try another keywords.</div></div>";
							jsonResponse.put("table", noResult);
							jsonResponse.put("pageCount", 0);
							response.getWriter().write(jsonResponse.toString());
							} catch(Exception ex) {
								LogManager.appendToExceptionLogs(ex);
							}
					} else {
						StringBuilder jobWhere = new StringBuilder();
						if(!ucPosition_list.isEmpty()) {
							String temp = "(";
							for(Uc_position p : ucPosition_list) {
								jobWhere.append(temp);
								temp = " or ";
								jobWhere.append("cj_position=" + p.getP_id());
							}
							jobWhere.append(")");
						}
						if(!ucQualification_list.isEmpty()) {
							if(!jobWhere.toString().equals("")) {
								jobWhere.append(" and ");
							}
							jobWhere.append("1 and  (");
							String temppostfix = "";
							for(Uc_qualifications q : ucQualification_list) {
								ucJobQualification_list = new ArrayList<Uc_job_qualifications>();
								ucJobQualification_list = (List<Uc_job_qualifications>)(Object) model.selectData("model.Uc_job_qualifications", "jq_qualification_id=" + q.getQ_id());
								if(!ucJobQualification_list.isEmpty()) {
									for(Uc_job_qualifications jq : ucJobQualification_list) {
										jobWhere.append(temppostfix);
										temppostfix = " or ";
										jobWhere.append("cj_id=" + jq.getJq_job_id());
									}
								} else {
									jobWhere.append(temppostfix);
									temppostfix = " or ";
									jobWhere.append("0");
								}
							}
							jobWhere.append(")");
						}
						jobWhere.append(" and cj_is_active=1 order by cj_id desc");
						try{
							createJobs(request, response, jobWhere);
						} catch(Exception ex) {
							LogManager.appendToExceptionLogs(ex);
						}
					}
				} else if(position.equals("") && !location.equals("")) {
					//got only location
					ucDistrict_list = new ArrayList<Uc_district>();
					ucDistrict_list = (List<Uc_district>)(Object) model.selectData("model.Uc_district", "d_name like '%" + location + "%'");
					StringBuilder talukaWhere = new StringBuilder();
					if(!ucDistrict_list.isEmpty()) {
						String temp = "(";
						for(Uc_district d : ucDistrict_list) {
							talukaWhere.append(temp);
							temp = " or ";
							talukaWhere.append("t_district_id=" + d.getD_id());
						}
						talukaWhere.append(")");
					}
					
					
					ucTaluka_list = new ArrayList<Uc_taluka>();
					if(!talukaWhere.toString().equals("")) {
						ucTaluka_list = (List<Uc_taluka>)(Object) model.selectData("model.Uc_taluka", "t_name like '%" + location + "%' or " + talukaWhere.toString());
					} else {
						ucTaluka_list = (List<Uc_taluka>)(Object) model.selectData("model.Uc_taluka", "t_name like '%" + location + "%'");
					}
					StringBuilder cityWhere = new StringBuilder();
					if(!ucTaluka_list.isEmpty()) {
						String temp = "(";
						for(Uc_taluka t : ucTaluka_list) {
							cityWhere.append(temp);
							temp = " or ";
							cityWhere.append("c_taluka_id=" + t.getT_id());
						}
						cityWhere.append(")");
					}
					
					ucCity_list = new ArrayList<Uc_city>();
				
					if(!cityWhere.toString().equals("")) {
						ucCity_list = (List<Uc_city>)(Object) model.selectData("model.Uc_city", "c_name like '%" + location + "%' or " + cityWhere.toString());
					} else {
						ucCity_list = (List<Uc_city>)(Object) model.selectData("model.Uc_city", "c_name like '%" + location + "%'");
					}
					if(ucCity_list.isEmpty()) {
						//no such location found
						try {
						response.setContentType("application/json");
						JSONObject jsonResponse = new JSONObject();
						String noResult = "<div class=\"col-lg-12\"><div class=\"alert alert-danger\">No job found for the entered keywords. Please try another keywords.</div></div>";
						jsonResponse.put("table", noResult);
						jsonResponse.put("pageCount", 0);
						response.getWriter().write(jsonResponse.toString());
						} catch(Exception ex) {
							LogManager.appendToExceptionLogs(ex);
						}
					} else {
						StringBuilder jobWhere = new StringBuilder();
						String temp = "(";
						for(Uc_city c : ucCity_list) {
							jobWhere.append(temp);
							temp = " or ";
							jobWhere.append("cj_city=" + c.getC_id());
						}
						jobWhere.append(") and cj_is_active=1 order by cj_id desc");
						try{
							createJobs(request, response, jobWhere);
						} catch(Exception ex) {
							LogManager.appendToExceptionLogs(ex);
						}
					}
				}
			}
		}
	}
	
	private void createJobs(HttpServletRequest request, HttpServletResponse response, StringBuilder jobWhere) throws JSONException, IOException {
		int pageNo = Integer.parseInt(request.getParameter("pageNo").toString());
		int limit = Integer.parseInt(request.getParameter("limit").toString());
		boolean begin = request.getParameter("begin").toLowerCase().equals("true") ? true : false;
		int start = (pageNo-1)*limit;
		ucCurrJobs_list = new ArrayList<Uc_current_jobs>();
		ucCurrJobs_list = (List<Uc_current_jobs>)(Object) model.selectData("model.Uc_current_jobs", jobWhere.toString() + " LIMIT " + start + "," + limit);
		if(ucCurrJobs_list.isEmpty()) {
			response.setContentType("application/json");
			JSONObject jsonResponse = new JSONObject();
			String noResult = "<div class=\"col-lg-12\"><div class=\"alert alert-danger\">No job found for the entered keywords. Please try another keywords.</div></div>";
			jsonResponse.put("table", noResult);
			jsonResponse.put("pageCount", 0);
			response.getWriter().write(jsonResponse.toString());
		} else {
		StringBuilder temp = new StringBuilder();
		int i = 0;
		for(Uc_current_jobs job : ucCurrJobs_list) {
			Uc_position position = new Uc_position();
			position = (Uc_position) model.selectData("model.Uc_position", "p_id=" + job.getCj_position()).get(0);
			Uc_city city = new Uc_city();
			Uc_district district = new Uc_district();
			Uc_taluka taluka = new Uc_taluka();
			Uc_state state = new Uc_state();
			city = (Uc_city) model.selectData("model.Uc_city", "c_id=" + job.getCj_city()).get(0);
			taluka = (Uc_taluka) model.selectData("model.Uc_taluka", "t_id=" + city.getC_taluka_id()).get(0);
			district = (Uc_district) model.selectData("model.Uc_district", "d_id=" + taluka.getT_district_id()).get(0);
			state = (Uc_state) model.selectData("model.Uc_state", "s_id=" + district.getD_s_id()).get(0);
			String loc;
			if(state.getS_name().toLowerCase().equals("gujarat")) {
				loc = city.getC_name() + ", " + taluka.getT_name() + ", " + district.getD_name() + ", " + state.getS_name();
			} else {
				loc = city.getC_name() + ", " + taluka.getT_name() + ", " + district.getD_name();
			}
			String gender = "";
			if(job.getCj_quantity() != 0) {
				gender = "Male or female";
			} else {
				ucCjQuantity_list = new ArrayList<Uc_cj_quantity>();
				ucCjQuantity_list = (List<Uc_cj_quantity>)(Object) model.selectData("model.Uc_cj_quantity", "cjq_job_id=" + job.getCj_id());
				//Uc_cj_quantity q = new Uc_cj_quantity();
				//q = (Uc_cj_quantity) model.selectData("model.Uc_cj_quantity", "cjq_job_id=" + job.getCj_id()).get(0);
				if(!ucCjQuantity_list.isEmpty()) {
					Uc_cj_quantity q = new Uc_cj_quantity();
					q = ucCjQuantity_list.get(0);
					if(q.getCjq_male() != 0 && q.getCjq_female() == 0) gender = "Male only";
					else if(q.getCjq_female() != 0 && q.getCjq_male() == 0) gender = "Female only";
					else if(q.getCjq_female() != 0 && q.getCjq_male() != 0) gender = "Male and female";
				} else {
					gender = "N/A";
				}
			}
			if(i%3==0) {
				if(i!=0) temp.append("</div>");
				temp.append("<div class=\"row\">");
			}
			i++;
		temp.append("<div class=\"col-lg-4 single-job-outer\">");
		temp.append("<div class=\"single-job-inner\">");
		temp.append("<div class=\"job-title\"><h3 style=\"padding:0;margin:0;\">" + position.getP_name() + "</h3></div>");
		temp.append("<div class=\"font-secondary\" style=\"margin:10px;margin-bottom:0;margin-top:20px;color:#515151;height:40px\">" + (job.getCj_work_profile().length() > 100 ? job.getCj_work_profile().substring(0,95) + " <a class=\"red-link\" style=\"cursor:pointer\" onclick=\"showJobDetails('" + EncryptionManager.encryptData(String.valueOf(job.getCj_id())) + "')\">Continue</a>" : job.getCj_work_profile()) + "</div>");
			temp.append("<hr>");
			temp.append("<table style=\"margin:10px;margin-top:0;\">");
				temp.append("<tr>");
				temp.append("<td style=\"border:none;color:#818181;\">Location </td><td class=\"font-secondary\" style=\"border:none;\">" + loc + "</td>");
				temp.append("</tr>");
				temp.append("<tr>");
				temp.append("<td style=\"border:none;color:#818181; padding-right:10px\">Experience </td><td class=\"font-secondary\" style=\"border:none;\">" + (job.getCj_experience_start() == job.getCj_experience_end() ? job.getCj_experience_start() : (job.getCj_experience_start() + "-" + job.getCj_experience_end())) + " years</td>");
				temp.append("</tr>");
				temp.append("<tr>");
				temp.append("<td style=\"border:none;color:#818181;\">Gender </td><td class=\"font-secondary\" style=\"border:none;\">" + gender + "</td>");
				temp.append("</tr>");
			temp.append("</table>");
			temp.append("<hr>");
			temp.append("<button class=\"btn mybtn\"  onclick=\"showJobDetails('" + EncryptionManager.encryptData(String.valueOf(job.getCj_id())) + "')\" style=\"margin-left:10px;\">Details</button>");
			temp.append("<button class=\"btn mybtn\" onclick=\"showTCDirect('"+ EncryptionManager.encryptData(String.valueOf(job.getCj_id())) +"')\" style=\"margin-left:10px;\"><i style=\"color:#FFC300\" class=\"fa fa-check\"></i> &nbsp;Apply</button>");
		temp.append("</div>");
	temp.append("</div>");
		
		}
		if(i>0) {
			temp.append("</div>");
		}
		response.setContentType("application/json");
		 JSONObject jsonResponse = new JSONObject();
		 jsonResponse.put("table", temp.toString());
		
		if(begin) {
			ucCurrJobs_list = new ArrayList<Uc_current_jobs>();
			ucCurrJobs_list = (List<Uc_current_jobs>)(Object) model.selectData("model.Uc_current_jobs", jobWhere.toString());
			int pageCount = (int) Math.ceil(ucCurrJobs_list.size()/(float)limit);
			if(pageCount == 0) pageCount = 1;
			 jsonResponse.put("pageCount", pageCount);
		}
		response.getWriter().write(jsonResponse.toString());
		}
	}
	
	
	private List<Uc_position> generatePositions() {
		ucPosition_list = new ArrayList<Uc_position>();
		ucPosition_list = (List<Uc_position>)(Object) model.selectData("model.Uc_position", "1");
		return ucPosition_list;
	}
}
