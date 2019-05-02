package controller;

import java.io.File;

import java.io.IOException;
import java.net.InetAddress;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.Hashtable;
import java.util.List;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import com.maxmind.geoip2.DatabaseReader;
import com.maxmind.geoip2.exception.GeoIp2Exception;
import com.maxmind.geoip2.model.CityResponse;

import database.Model;
import model.Uc_candidate_details;
import model.Uc_city;
import model.Uc_cj_quantity;
import model.Uc_current_jobs;
import model.Uc_district;
import model.Uc_employer_details;
import model.Uc_experience;
import model.Uc_firm_details;
import model.Uc_interest_areas;
import model.Uc_position;
import model.Uc_state;
import model.Uc_taluka;
import utils.EncryptionManager;
import utils.RouteManager;
import utils.SessionManager;
import utils.ViewManager;

@WebServlet("")
public class Index extends HttpServlet {
	private static final long serialVersionUID = 1L;
       
	private List<Uc_current_jobs> ucCurrJobs_list;
	private List<Uc_employer_details> ucEmployer_list;
	private List<Uc_candidate_details> ucCandidate_list;
	private Model model;
	private Uc_candidate_details ucCandidate;
	private List<Uc_experience> ucExperience_list;
	private List<Uc_cj_quantity> ucCjQuantity_list;
	private List<Uc_interest_areas> ucInterest_list;
	
    public Index() {
        super();   
    }

	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		getFeaturedJobs(request, response);
		Hashtable sessionAttributes = new Hashtable();
		sessionAttributes = SessionManager.sessionExists(request, "uc_client_session") == null ? null : (Hashtable)SessionManager.sessionExists(request, "uc_client_session");
		if(sessionAttributes != null) {
			getLoginRecommendedJobs(request,response, sessionAttributes);
		}
		try {
			getRecommendedJobs(request,response);
		} catch(Exception ex) {
			System.out.println(ex);
		}
		ViewManager.showView(request, response, "index.jsp");
		//request.getRequestDispatcher("/WEB-INF/index.jsp").forward(request, response);
	}

	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		doGet(request, response);
	}
	
	private void getFeaturedJobs(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		model = new Model();
		//ucCandidate_list = new ArrayList<Uc_candidate_details>();
		//ucCandidate_list = (List<Uc_candidate_details>)(Object) model.selectData("model.Uc_candidate_details", "cd_is_placed=1");
		//ucEmployer_list = new ArrayList<Uc_employer_details>();
		//ucEmployer_list = (List<Uc_employer_details>)(Object) model.selectData("model.Uc_employer_details","ed_active=1");
		ucCurrJobs_list = new ArrayList<Uc_current_jobs>();
		ucCurrJobs_list = (List<Uc_current_jobs>)(Object) model.selectData("model.Uc_current_jobs", "cj_is_active=1");
		//request.setAttribute("candidateCount", ucCandidate_list.size());
		if(ucCurrJobs_list == null) {
			ucCurrJobs_list = new ArrayList<>();
		}
		request.setAttribute("jobCount", ucCurrJobs_list.size());
		//request.setAttribute("employerCount", ucEmployer_list.size());
		if(ucCurrJobs_list.isEmpty()) {
			request.setAttribute("featuredAvail", false);
			request.setAttribute("featured", null);
		} else {
			StringBuilder temp = new StringBuilder();
			int i = 0;
			for(Uc_current_jobs job : ucCurrJobs_list) {
				if(job.getCj_is_featured() == 1 && job.getCj_is_active() == 1) {
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
					//Uc_cj_quantity q = new Uc_cj_quantity();
					//q = (Uc_cj_quantity) model.selectData("model.Uc_cj_quantity", "cjq_job_id=" + job.getCj_id()).get(0);
					ucCjQuantity_list = new ArrayList<Uc_cj_quantity>();
					ucCjQuantity_list = (List<Uc_cj_quantity>)(Object) model.selectData("model.Uc_cj_quantity", "cjq_job_id=" + job.getCj_id());
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
				temp.append("<button class=\"btn mybtn\" style=\"margin-left:10px;\" onclick=\"showJobDetails('" + EncryptionManager.encryptData(String.valueOf(job.getCj_id())) + "')\">Details</button>");
				temp.append("<button class=\"btn mybtn\" onclick=\"showTCDirect('" + EncryptionManager.encryptData(String.valueOf(job.getCj_id())) +"')\" style=\"margin-left:10px;\"><i style=\"color:#FFC300\" class=\"fa fa-check\"></i> &nbsp;Apply</button>");
			temp.append("</div>");
		temp.append("</div>");
			}
			}
			
			if(temp.toString().equals("")) {
				request.setAttribute("featuredAvail", false);
				request.setAttribute("featured", null);
			} else {
				if(i>0) {
					temp.append("</div>");
				}
				request.setAttribute("featuredAvail", true);
				request.setAttribute("featured", temp);
			}
			
		}
	}
	private void getLoginRecommendedJobs(HttpServletRequest request, HttpServletResponse response, Hashtable sessionAttributes) throws ServletException, IOException {
		int uid = Integer.parseInt(sessionAttributes.get("id").toString());
		StringBuilder jobWhere = new StringBuilder();
		StringBuilder genderWhere = new StringBuilder();
		StringBuilder expWhere = new StringBuilder();
		StringBuilder expPosWhere = new StringBuilder();
		StringBuilder expExpWhere = new StringBuilder();
		StringBuilder interestWhere = new StringBuilder();
		StringBuilder interestPosWhere = new StringBuilder();
		//StringBuilder interestWhere = new StringBuilder();
		StringBuilder cityWhere = new StringBuilder();
		
		ucCandidate = (Uc_candidate_details) model.selectData("model.Uc_candidate_details", "cd_id=" + uid).get(0);
		String genderWhereCondition = ((ucCandidate.getCd_gender() == UCConstants.MALE) ? "cjq_male > 0" : "cjq_female > 0");
		ucCjQuantity_list = new ArrayList<Uc_cj_quantity>();
		ucCjQuantity_list = (List<Uc_cj_quantity>)(Object) model.selectData("model.Uc_cj_quantity", genderWhereCondition);
		if(!ucCjQuantity_list.isEmpty()) {
			String postfix = "(";
			for(Uc_cj_quantity cjq : ucCjQuantity_list) {
				//jobWhere.append(postfix);
				genderWhere.append(postfix);
				postfix = " or ";
				//jobWhere.append("cj_id=" + cjq.getCjq_job_id());
				genderWhere.append("cj_id=" + cjq.getCjq_job_id());
			}
			//jobWhere.append(")");
			genderWhere.append(")");
		}
		
			ucExperience_list = new ArrayList<Uc_experience>();
			ucExperience_list = (List<Uc_experience>)(Object) model.selectData("model.Uc_experience", "e_candidate_id=" + uid + " order by e_years desc");
			if(!ucExperience_list.isEmpty()) {
				//jobWhere.append(" and (");
				expWhere.append("(");
				String posPostfix = "(";
				String postfix = "(";
				for(Uc_experience exp : ucExperience_list) {
					//jobWhere.append(postfix);
					expWhere.append(postfix);
					expPosWhere.append(posPostfix);
					expExpWhere.append(posPostfix);
					posPostfix = " or ";
					postfix = ") or (";
					ucEmployer_list = new ArrayList<Uc_employer_details>();
					ucEmployer_list = (List<Uc_employer_details>)(Object) model.selectData("model.Uc_employer_details", "ed_industry_sector_id=" + exp.getE_industry_id());
					if(!ucEmployer_list.isEmpty()) {
						String temppostfix = "(";
						for(Uc_employer_details ed : ucEmployer_list) {
							//jobWhere.append(temppostfix);
							expWhere.append(temppostfix);
							temppostfix = " or ";
							//jobWhere.append("cj_employer_id=" + ed.getEd_id());
							expWhere.append("cj_employer_id=" + ed.getEd_id());
						}
						//jobWhere.append(") and ");
						expWhere.append(") and ");
					}
					//jobWhere.append("cj_position=" + exp.getE_position_id() + " and (cj_experience_start <=" + exp.getE_years() + "<= cj_experience_end)");
					expPosWhere.append("cj_position=" + exp.getE_position_id());
					expExpWhere.append("(cj_experience_start <=" + exp.getE_years() + "<= cj_experience_end)");
					expWhere.append("cj_position=" + exp.getE_position_id() + " and (cj_experience_start <=" + exp.getE_years() + "<= cj_experience_end)");
				}
				//jobWhere.append(")) ");
				expWhere.append("))");
				expPosWhere.append(")");
				expExpWhere.append(")");
			} else {
				expWhere.append("1");
				expExpWhere.append("1");
				expPosWhere.append("1");
			}
			
			ucInterest_list = new ArrayList<Uc_interest_areas>();
			ucInterest_list = (List<Uc_interest_areas>)(Object) model.selectData("model.Uc_interest_areas", "ia_candidate=" + ucCandidate.getCd_id());
			if(!ucInterest_list.isEmpty()) {
				//jobWhere.append("and (");
				interestWhere.append("(");
				String postfix = "(";
				String posPostfix = "(";
				for(Uc_interest_areas ia : ucInterest_list) {
					//jobWhere.append(postfix);
					interestWhere.append(postfix);
					interestPosWhere.append(posPostfix);
					postfix = ") or (";
					posPostfix = " or ";
					ucEmployer_list = new ArrayList<Uc_employer_details>();
					ucEmployer_list = (List<Uc_employer_details>)(Object) model.selectData("model.Uc_employer_details", "ed_industry_sector_id=" + ia.getIa_industry());
					if(!ucEmployer_list.isEmpty()) {
						String temppostfix = "(";
						for(Uc_employer_details ed : ucEmployer_list) {
							//jobWhere.append(temppostfix);
							interestWhere.append(temppostfix);
							temppostfix = " or ";
							//jobWhere.append("cj_employer_id=" + ed.getEd_id());
							interestWhere.append("cj_employer_id=" + ed.getEd_id());
						}
						//jobWhere.append(") and ");
						interestWhere.append(") and ");
					}
					//jobWhere.append("cj_position=" + ia.getIa_position());
					interestWhere.append("cj_position=" + ia.getIa_position());
					interestPosWhere.append("cj_position=" + ia.getIa_position());
				}
				//jobWhere.append(")) and ");
				interestWhere.append("))");
				interestPosWhere.append(")");
			}
			//jobWhere.append("cj_city=" + ucCandidate.getCd_city_id());
			cityWhere.append("cj_city=" + ucCandidate.getCd_city_id());
			//ucCurrJobs_list = new ArrayList<Uc_current_jobs>();
			//ucCurrJobs_list = (List<Uc_current_jobs>)(Object) model.selectData("model.Uc_current_jobs", jobWhere.toString() + " LIMIT 6");
			//System.out.println(ucCurrJobs_list.size());
		
		List<Uc_current_jobs> ucCurrJobs_list_temp;
		StringBuilder omitJob;
		
		ucCurrJobs_list = new ArrayList<Uc_current_jobs>();
		ucCurrJobs_list = (List<Uc_current_jobs>)(Object) model.selectData("model.Uc_current_jobs", "cj_is_active=1 and (cj_quantity!=0" + (genderWhere.toString().equals("") ? "" : " or " + genderWhere.toString()) + ")" + (expWhere.toString().equals("") ? "" : " and " + expWhere.toString()) + "" + (interestWhere.toString().equals("") ? "" : " and " + interestWhere.toString()) + "" + (cityWhere.toString().equals("") ? "" : " and " + cityWhere.toString()) + " LIMIT 6");
		if(!ucCurrJobs_list.isEmpty() && ucCurrJobs_list.size() < 6) {
			int tempLimit = (6 - ucCurrJobs_list.size());
			omitJob = new StringBuilder();
			if(!ucCurrJobs_list.isEmpty()) {
				String postfix = "(";
				for(Uc_current_jobs j : ucCurrJobs_list) {
					omitJob.append(postfix);
					postfix = " and ";
					omitJob.append("cj_id!=" + j.getCj_id());
				}
				omitJob.append(") and ");
			}
			ucCurrJobs_list_temp = new ArrayList<Uc_current_jobs>();
			ucCurrJobs_list_temp = (List<Uc_current_jobs>)(Object) model.selectData("model.Uc_current_jobs", omitJob.toString() + "(cj_quantity!=0 or " + genderWhere.toString() + ") and cj_is_active=1 and " + expWhere.toString() + " and " + cityWhere.toString() + " LIMIT " + tempLimit);
			ucCurrJobs_list.addAll(ucCurrJobs_list_temp);
			if(ucCurrJobs_list.size() < 6) {
				tempLimit = (6 - ucCurrJobs_list.size());
				omitJob = new StringBuilder();
				if(!ucCurrJobs_list.isEmpty()) {
					String postfix = "(";
					for(Uc_current_jobs j : ucCurrJobs_list) {
						omitJob.append(postfix);
						postfix = " and ";
						omitJob.append("cj_id!=" + j.getCj_id());
					}
					omitJob.append(") and ");
				}
				ucCurrJobs_list_temp = new ArrayList<Uc_current_jobs>();
				ucCurrJobs_list_temp = (List<Uc_current_jobs>)(Object) model.selectData("model.Uc_current_jobs", omitJob.toString() + "(cj_quantity!=0 or " + genderWhere.toString() + ") and cj_is_active=1 and " + expExpWhere.toString() + " and " + cityWhere.toString() + " and " + expPosWhere.toString() + " LIMIT " + tempLimit);
				ucCurrJobs_list.addAll(ucCurrJobs_list_temp);
				if(ucCurrJobs_list.size() < 6) {
					tempLimit = (6 - ucCurrJobs_list.size());
					omitJob = new StringBuilder();
					if(!ucCurrJobs_list.isEmpty()) {
						String postfix = "(";
						for(Uc_current_jobs j : ucCurrJobs_list) {
							omitJob.append(postfix);
							postfix = " and ";
							omitJob.append("cj_id!=" + j.getCj_id());
						}
						omitJob.append(") and ");
					}
					ucCurrJobs_list_temp = new ArrayList<Uc_current_jobs>();
					ucCurrJobs_list_temp = (List<Uc_current_jobs>)(Object) model.selectData("model.Uc_current_jobs", omitJob.toString() + "(cj_quantity!=0 or " + genderWhere.toString() + ") and cj_is_active=1 and " + interestWhere.toString() + " and " + cityWhere.toString() + " LIMIT " + tempLimit);
					ucCurrJobs_list.addAll(ucCurrJobs_list_temp);
					if(ucCurrJobs_list.size() < 6) {
						tempLimit = (6 - ucCurrJobs_list.size());
						omitJob = new StringBuilder();
						if(!ucCurrJobs_list.isEmpty()) {
							String postfix = "(";
							for(Uc_current_jobs j : ucCurrJobs_list) {
								omitJob.append(postfix);
								postfix = " and ";
								omitJob.append("cj_id!=" + j.getCj_id());
							}
							omitJob.append(") and ");
						}
						ucCurrJobs_list_temp = new ArrayList<Uc_current_jobs>();
						ucCurrJobs_list_temp = (List<Uc_current_jobs>)(Object) model.selectData("model.Uc_current_jobs", omitJob.toString() + "(cj_quantity!=0 or " + genderWhere.toString() + ") and cj_is_active=1 and " + interestPosWhere.toString() + " and " + cityWhere.toString() + " LIMIT " + tempLimit);
						ucCurrJobs_list.addAll(ucCurrJobs_list_temp);
						if(ucCurrJobs_list.size() < 6) {
							tempLimit = (6 - ucCurrJobs_list.size());
							omitJob = new StringBuilder();
							if(!ucCurrJobs_list.isEmpty()) {
								String postfix = "(";
								for(Uc_current_jobs j : ucCurrJobs_list) {
									omitJob.append(postfix);
									postfix = " and ";
									omitJob.append("cj_id!=" + j.getCj_id());
								}
								omitJob.append(") and ");
							}
							ucCurrJobs_list_temp = new ArrayList<Uc_current_jobs>();
							ucCurrJobs_list_temp = (List<Uc_current_jobs>)(Object) model.selectData("model.Uc_current_jobs", omitJob.toString() + "(cj_quantity!=0 or " + genderWhere.toString() + ") and cj_is_active=1 and " + expWhere.toString() + " LIMIT " + tempLimit);
							ucCurrJobs_list.addAll(ucCurrJobs_list_temp);
							if(ucCurrJobs_list.size() < 6) {
								tempLimit = (6 - ucCurrJobs_list.size());
								omitJob = new StringBuilder();
								if(!ucCurrJobs_list.isEmpty()) {
									String postfix = "(";
									for(Uc_current_jobs j : ucCurrJobs_list) {
										omitJob.append(postfix);
										postfix = " and ";
										omitJob.append("cj_id!=" + j.getCj_id());
									}
									omitJob.append(") and ");
								}
								ucCurrJobs_list_temp = new ArrayList<Uc_current_jobs>();
								ucCurrJobs_list_temp = (List<Uc_current_jobs>)(Object) model.selectData("model.Uc_current_jobs", omitJob.toString() + "(cj_quantity!=0 or " + genderWhere.toString() + ") and cj_is_active=1 and " + expExpWhere.toString() + " and " +  expPosWhere.toString() + " LIMIT " + tempLimit);
								ucCurrJobs_list.addAll(ucCurrJobs_list_temp);
								if(ucCurrJobs_list.size() < 6) {
									tempLimit = (6 - ucCurrJobs_list.size());
									omitJob = new StringBuilder();
									if(!ucCurrJobs_list.isEmpty()) {
										String postfix = "(";
										for(Uc_current_jobs j : ucCurrJobs_list) {
											omitJob.append(postfix);
											postfix = " and ";
											omitJob.append("cj_id!=" + j.getCj_id());
										}
										omitJob.append(") and ");
									}
									ucCurrJobs_list_temp = new ArrayList<Uc_current_jobs>();
									ucCurrJobs_list_temp = (List<Uc_current_jobs>)(Object) model.selectData("model.Uc_current_jobs", omitJob.toString() + "(cj_quantity!=0 or " + genderWhere.toString() + ") and cj_is_active=1 and " + interestWhere.toString() + " LIMIT " + tempLimit);
									ucCurrJobs_list.addAll(ucCurrJobs_list_temp);
									if(ucCurrJobs_list.size() < 6) {
										tempLimit = (6 - ucCurrJobs_list.size());
										omitJob = new StringBuilder();
										if(!ucCurrJobs_list.isEmpty()) {
											String postfix = "(";
											for(Uc_current_jobs j : ucCurrJobs_list) {
												omitJob.append(postfix);
												postfix = " and ";
												omitJob.append("cj_id!=" + j.getCj_id());
											}
											omitJob.append(") and ");
										}
										ucCurrJobs_list_temp = new ArrayList<Uc_current_jobs>();
										ucCurrJobs_list_temp = (List<Uc_current_jobs>)(Object) model.selectData("model.Uc_current_jobs", omitJob.toString() + "(cj_quantity!=0 or " + genderWhere.toString() + ") and cj_is_active=1 and " + interestPosWhere.toString() + " LIMIT " + tempLimit);
										ucCurrJobs_list.addAll(ucCurrJobs_list_temp);
										if(ucCurrJobs_list.size() < 6) {
											tempLimit = (6 - ucCurrJobs_list.size());
											omitJob = new StringBuilder();
											if(!ucCurrJobs_list.isEmpty()) {
												String postfix = "(";
												for(Uc_current_jobs j : ucCurrJobs_list) {
													omitJob.append(postfix);
													postfix = " and ";
													omitJob.append("cj_id!=" + j.getCj_id());
												}
												omitJob.append(") and ");
											}
											ucCurrJobs_list_temp = new ArrayList<Uc_current_jobs>();
											ucCurrJobs_list_temp = (List<Uc_current_jobs>)(Object) model.selectData("model.Uc_current_jobs", omitJob.toString() + "(cj_quantity!=0 or " + genderWhere.toString() + ") and cj_is_active=1 and " + cityWhere.toString() + " LIMIT " + tempLimit);
											ucCurrJobs_list.addAll(ucCurrJobs_list_temp);
											if(ucCurrJobs_list.size() < 6) {
												tempLimit = (6 - ucCurrJobs_list.size());
												omitJob = new StringBuilder();
												if(!ucCurrJobs_list.isEmpty()) {
													String postfix = "(";
													for(Uc_current_jobs j : ucCurrJobs_list) {
														omitJob.append(postfix);
														postfix = " and ";
														omitJob.append("cj_id!=" + j.getCj_id());
													}
													omitJob.append(") and ");
												}
												ucCurrJobs_list_temp = new ArrayList<Uc_current_jobs>();
												ucCurrJobs_list_temp = (List<Uc_current_jobs>)(Object) model.selectData("model.Uc_current_jobs", omitJob.toString() + "(cj_quantity!=0 or " + genderWhere.toString() + ") and cj_is_active=1 LIMIT " + tempLimit);
												ucCurrJobs_list.addAll(ucCurrJobs_list_temp);
											}
										}
									}
								}
							}
						}
					}
				}
			}
		}
		if(ucCurrJobs_list.size() > 0) {
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
					//Uc_cj_quantity q = new Uc_cj_quantity();
					//q = (Uc_cj_quantity) model.selectData("model.Uc_cj_quantity", "cjq_job_id=" + job.getCj_id()).get(0);
					ucCjQuantity_list = new ArrayList<Uc_cj_quantity>();
					ucCjQuantity_list = (List<Uc_cj_quantity>)(Object) model.selectData("model.Uc_cj_quantity", "cjq_job_id=" + job.getCj_id());
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
				temp.append("<div class=\"font-secondary\" style=\"margin:10px;margin-bottom:0;margin-top:20px;color:#515151\">" + job.getCj_work_profile() + "</div>");
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
				temp.append("<button class=\"btn mybtn\" onclick=\"showJobDetails('" + EncryptionManager.encryptData(String.valueOf(job.getCj_id())) + "')\" style=\"margin-left:10px;\">Details</button>");
				temp.append("<button class=\"btn mybtn\" onclick=\"showTCDirect('" + EncryptionManager.encryptData(String.valueOf(job.getCj_id())) +"')\" style=\"margin-left:10px;\"><i style=\"color:#FFC300\" class=\"fa fa-check\"></i> &nbsp;Apply</button>");
			temp.append("</div>");
		temp.append("</div>");
			
			}
			if(i>0) {
				temp.append("</div>");
			}
				//request.setAttribute("featuredAvail", true);
				request.setAttribute("recommended", temp);
			
			
		} 
	}
	
	private void getRecommendedJobs(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException, GeoIp2Exception {
		System.out.println(getServletContext().getRealPath("/"));
		File database = new File(getServletContext().getRealPath("/assets/GeoLite2-City.mmdb"));
		System.out.println(getClientIpAddr(request));
		DatabaseReader reader = new DatabaseReader.Builder(database).build();
		InetAddress ipAddress = InetAddress.getByName(request.getRemoteAddr());
		CityResponse res = reader.city(ipAddress);
		System.out.println("city" + res.getCity().getName());
		
	}
	private String getClientIpAddr(HttpServletRequest request) {  
        String ip = request.getHeader("X-Forwarded-For");  
        if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {  
            ip = request.getHeader("Proxy-Client-IP");  
        }  
        if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {  
            ip = request.getHeader("WL-Proxy-Client-IP");  
        }  
        if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {  
            ip = request.getHeader("HTTP_CLIENT_IP");  
        }  
        if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {  
            ip = request.getHeader("HTTP_X_FORWARDED_FOR");  
        }  
        if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {  
            ip = request.getRemoteAddr();  
        }  
        return ip;  
    }  
	
}
