package controller;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.PrintWriter;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.Hashtable;
import java.util.List;
import java.util.StringTokenizer;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;


import org.apache.poi.xssf.usermodel.XSSFCell;
import org.apache.poi.xssf.usermodel.XSSFRow;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import database.Model;
import logs.LogManager;
import model.Uc_application_comments;
import model.Uc_candidate_comments;
import model.Uc_candidate_details;
import model.Uc_candidate_qualification;
import model.Uc_city;
import model.Uc_district;
import model.Uc_experience;
import model.Uc_industry_sector;
import model.Uc_interest_areas;
import model.Uc_job_applications;
import model.Uc_position;
import model.Uc_qualifications;
import model.Uc_state;
import model.Uc_taluka;
import utils.FileManager;
import utils.RouteManager;
import utils.SessionManager;
import utils.ViewManager;

@WebServlet("/admin/manage-users")
public class AdminRegisteredUsers extends HttpServlet {
	private static final long serialVersionUID = 1L;
       
	private Model model;
	private Uc_candidate_comments ucCandidate_comments;
	private List<Uc_candidate_comments> ucCandidateComments_list;
	private List<Uc_candidate_details> ucCandidate_list;
	private List<Uc_position> ucPosition_list;
	private List<Uc_experience> ucExperience_list;
	private List<Uc_interest_areas> ucInterest_list;
	private List<Uc_industry_sector> ucIndustry_list;
	private List<Uc_state> ucState_list;
	private List<Uc_district> ucDistrict_list;
	
	private List<Uc_city> ucCity_list;
	private List<Uc_candidate_qualification> ucQualification_list;
	private Uc_city ucCity;
	private List<Uc_qualifications> ucQuali_list;
	private List<Uc_job_applications> ucJobApp_list;
	private Uc_candidate_details ucCandidate;
	private Uc_district ucDistrict;
	private Uc_state ucState;
	private Uc_qualifications ucQualification;
	private Uc_industry_sector ucIndustry;
	private Uc_position ucPosition;
	private List<Uc_taluka> ucTaluka_list;
	private Uc_taluka ucTaluka;
	private FileManager fileManager;
	private Uc_interest_areas ucInterestAreas;
    public AdminRegisteredUsers() {
        super();
        model = new Model();
    }

	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		Hashtable sessionAttributes = new Hashtable();
		sessionAttributes = (Hashtable) SessionManager.sessionExists(request, "admin");
		if(request.getParameter("action") != null) {
			String action = request.getParameter("action").toLowerCase().toString();
			if(action.equals("getuserlist")) {
				if(Integer.parseInt(sessionAttributes.get("utype").toString()) == UCConstants.MAIN_ADMIN || Integer.parseInt(sessionAttributes.get("utype").toString()) == UCConstants.SUB_MAIN_ADMIN || Integer.parseInt(sessionAttributes.get("utype").toString()) == UCConstants.TELECALLER_ADMIN) {
					try {
						getUserList(request,response);
					} catch(Exception ex) {
						System.out.println(ex);
						LogManager.appendToExceptionLogs(ex);
					}
				} else {
					ViewManager.showView(request, response, "admin/accessdenied.jsp");
				}
			}
			if(action.equals("searchuser")) {
				if(Integer.parseInt(sessionAttributes.get("utype").toString()) == UCConstants.MAIN_ADMIN || Integer.parseInt(sessionAttributes.get("utype").toString()) == UCConstants.SUB_MAIN_ADMIN || Integer.parseInt(sessionAttributes.get("utype").toString()) == UCConstants.TELECALLER_ADMIN) {
					try {
						searchUser(request,response);
					} catch(Exception ex) {
						System.out.println(ex);
						LogManager.appendToExceptionLogs(ex);
					}
				} else {
					ViewManager.showView(request, response, "admin/accessdenied.jsp");
				}
			}
			if(action.equals("showdetails")) {
				if(Integer.parseInt(sessionAttributes.get("utype").toString()) == UCConstants.MAIN_ADMIN || Integer.parseInt(sessionAttributes.get("utype").toString()) == UCConstants.SUB_MAIN_ADMIN || Integer.parseInt(sessionAttributes.get("utype").toString()) == UCConstants.TELECALLER_ADMIN) {
					try {
						showDetails(request,response);
					} catch(Exception ex) {
						System.out.println(ex);
						LogManager.appendToExceptionLogs(ex);
					}
				} else {
					ViewManager.showView(request, response, "admin/accessdenied.jsp");
				}
			}
			if(action.equals("getcandidatecomments")) {
				if(Integer.parseInt(sessionAttributes.get("utype").toString()) == UCConstants.MAIN_ADMIN || Integer.parseInt(sessionAttributes.get("utype").toString()) == UCConstants.SUB_MAIN_ADMIN || Integer.parseInt(sessionAttributes.get("utype").toString()) == UCConstants.TELECALLER_ADMIN) {
					try {
						getCandidateComments(request,response);
					} catch(Exception ex) {
						System.out.println(ex);
						LogManager.appendToExceptionLogs(ex);
					}
				} else {
					ViewManager.showView(request, response, "admin/accessdenied.jsp");
				}
			}
			if(action.equals("savecommentchanges")) {
				if(Integer.parseInt(sessionAttributes.get("utype").toString()) == UCConstants.MAIN_ADMIN || Integer.parseInt(sessionAttributes.get("utype").toString()) == UCConstants.SUB_MAIN_ADMIN || Integer.parseInt(sessionAttributes.get("utype").toString()) == UCConstants.TELECALLER_ADMIN) {
					response.setContentType("text/html;charset=UTF-8");
					if(saveCommentChanges(request, response)) response.getWriter().write("success");
					else response.getWriter().write("error");
				} else {
					ViewManager.showView(request, response, "admin/accessdenied.jsp");
				}
			}
			if(action.equals("savecomment")) {
				if(Integer.parseInt(sessionAttributes.get("utype").toString()) == UCConstants.MAIN_ADMIN || Integer.parseInt(sessionAttributes.get("utype").toString()) == UCConstants.SUB_MAIN_ADMIN || Integer.parseInt(sessionAttributes.get("utype").toString()) == UCConstants.TELECALLER_ADMIN) {
					saveComment(request,response);
				} else {
					ViewManager.showView(request, response, "admin/accessdenied.jsp");
				}
			}
			if(action.equals("deletecomment")) {
				if(Integer.parseInt(sessionAttributes.get("utype").toString()) == UCConstants.MAIN_ADMIN || Integer.parseInt(sessionAttributes.get("utype").toString()) == UCConstants.SUB_MAIN_ADMIN || Integer.parseInt(sessionAttributes.get("utype").toString()) == UCConstants.TELECALLER_ADMIN) {
					deleteComment(request,response);
				} else {
					ViewManager.showView(request, response, "admin/accessdenied.jsp");
				}
			}
			if(action.equals("download")) {
				if(Integer.parseInt(sessionAttributes.get("utype").toString()) == UCConstants.MAIN_ADMIN || Integer.parseInt(sessionAttributes.get("utype").toString()) == UCConstants.SUB_MAIN_ADMIN || Integer.parseInt(sessionAttributes.get("utype").toString()) == UCConstants.TELECALLER_ADMIN) {
					downloadResume(request, response);
				} else {
					ViewManager.showView(request, response, "admin/accessdenied.jsp");
				}
			}
			if(action.equals("getstates")) {
				getStates(request,response);
			}
			if(action.equals("getdistricts")) {
				getDistricts(request,response);
			}
			if(action.equals("gettalukas")) {
				getTalukas(request,response);
			}
			if(action.equals("getcities")) {
				getCities(request,response);
			}
			if(action.equals("getpositions")) {
				getPositions(request,response);
			}
			if(action.equals("getqualifications")) {
				getQualifications(request,response);
			}
			if(action.equals("saveinterestindustry")) {
				saveInterestIndustry(request, response);
			}
			if(action.equals("saveinterestposition")) {
				saveInterestPosition(request, response);
			}
			if(action.equals("deleteinterest")) {
				deleteInterest(request, response);
			}
			if(action.equals("removeuser")) {
				if(Integer.parseInt(sessionAttributes.get("utype").toString()) == UCConstants.MAIN_ADMIN || Integer.parseInt(sessionAttributes.get("utype").toString()) == UCConstants.SUB_MAIN_ADMIN) {
					removeUser(request,response);
				} else {
					ViewManager.showView(request, response, "admin/accessdenied.jsp");
				}
			}
			if(action.equals("deactivateuser")) {
				if(Integer.parseInt(sessionAttributes.get("utype").toString()) == UCConstants.MAIN_ADMIN || Integer.parseInt(sessionAttributes.get("utype").toString()) == UCConstants.SUB_MAIN_ADMIN) {
					deactivateUser(request,response);
				} else {
					ViewManager.showView(request, response, "admin/accessdenied.jsp");
				}
			}
			if(action.equals("activateuser")) {
				if(Integer.parseInt(sessionAttributes.get("utype").toString()) == UCConstants.MAIN_ADMIN || Integer.parseInt(sessionAttributes.get("utype").toString()) == UCConstants.SUB_MAIN_ADMIN) {
					activateUser(request,response);
				} else {
					ViewManager.showView(request, response, "admin/accessdenied.jsp");
				}
			}
			if(action.equals("getinsights")) {
				if(Integer.parseInt(sessionAttributes.get("utype").toString()) == UCConstants.MAIN_ADMIN || Integer.parseInt(sessionAttributes.get("utype").toString()) == UCConstants.SUB_MAIN_ADMIN) {
					try {
						getInsights(request,response);
					} catch(Exception ex) {
						LogManager.appendToExceptionLogs(ex);
					}
				} else {
					ViewManager.showView(request, response, "admin/accessdenied.jsp");
				}
			}
			if(action.equals("searchshortlistuser")) {
				if(Integer.parseInt(sessionAttributes.get("utype").toString()) == UCConstants.MAIN_ADMIN || Integer.parseInt(sessionAttributes.get("utype").toString()) == UCConstants.SUB_MAIN_ADMIN || Integer.parseInt(sessionAttributes.get("utype").toString()) == UCConstants.TELECALLER_ADMIN) {
					try {
						searchShortlistUser(request,response);
					} catch(Exception ex) {
						LogManager.appendToExceptionLogs(ex);
					}
				} else {
					ViewManager.showView(request, response, "admin/accessdenied.jsp");
				}
			}
			if(action.equals("changeplaced")) {
				if(Integer.parseInt(sessionAttributes.get("utype").toString()) == UCConstants.MAIN_ADMIN || Integer.parseInt(sessionAttributes.get("utype").toString()) == UCConstants.SUB_MAIN_ADMIN || Integer.parseInt(sessionAttributes.get("utype").toString()) == UCConstants.TELECALLER_ADMIN) {
					try {
						changePlaced(request,response);
					} catch(Exception ex) {
						LogManager.appendToExceptionLogs(ex);
					}
				} else {
					ViewManager.showView(request, response, "admin/accessdenied.jsp");
				}
			}
			if(action.equals("changepayment")) {
				if(Integer.parseInt(sessionAttributes.get("utype").toString()) == UCConstants.MAIN_ADMIN || Integer.parseInt(sessionAttributes.get("utype").toString()) == UCConstants.SUB_MAIN_ADMIN || Integer.parseInt(sessionAttributes.get("utype").toString()) == UCConstants.TELECALLER_ADMIN) {
					try {
						changePayment(request,response);
					} catch(Exception ex) {
						LogManager.appendToExceptionLogs(ex);
					}
				} else {
					ViewManager.showView(request, response, "admin/accessdenied.jsp");
				}
			}
			if(action.equals("savemodifiedgender")) {
				if(Integer.parseInt(sessionAttributes.get("utype").toString()) == UCConstants.MAIN_ADMIN || Integer.parseInt(sessionAttributes.get("utype").toString()) == UCConstants.SUB_MAIN_ADMIN || Integer.parseInt(sessionAttributes.get("utype").toString()) == UCConstants.TELECALLER_ADMIN) {
					try {
						saveModifiedGender(request,response);
					} catch(Exception ex) {
						LogManager.appendToExceptionLogs(ex);
					}
				} else {
					ViewManager.showView(request, response, "admin/accessdenied.jsp");
				}
			}
			if(action.equals("savepaymentdate")) {
				if(Integer.parseInt(sessionAttributes.get("utype").toString()) == UCConstants.MAIN_ADMIN || Integer.parseInt(sessionAttributes.get("utype").toString()) == UCConstants.SUB_MAIN_ADMIN || Integer.parseInt(sessionAttributes.get("utype").toString()) == UCConstants.TELECALLER_ADMIN) {
					try {
						savePaymentDate(request,response);
					} catch(Exception ex) {
						LogManager.appendToExceptionLogs(ex);
					}
				} else {
					ViewManager.showView(request, response, "admin/accessdenied.jsp");
				}
			}
			if(action.equals("downloadcandidatedetails")) {
				if(Integer.parseInt(sessionAttributes.get("utype").toString()) == UCConstants.MAIN_ADMIN || Integer.parseInt(sessionAttributes.get("utype").toString()) == UCConstants.SUB_MAIN_ADMIN || Integer.parseInt(sessionAttributes.get("utype").toString()) == UCConstants.TELECALLER_ADMIN) {
					downloadCandidateDetails(request,response);
				} else {
					ViewManager.showView(request, response, "admin/accessdenied.jsp");
				}
			}
		} else {
			if(Integer.parseInt(sessionAttributes.get("utype").toString()) == UCConstants.MAIN_ADMIN || Integer.parseInt(sessionAttributes.get("utype").toString()) == UCConstants.SUB_MAIN_ADMIN || Integer.parseInt(sessionAttributes.get("utype").toString()) == UCConstants.TELECALLER_ADMIN) {
				ViewManager.showView(request, response, "admin/registeredcandidates.jsp");
			} else {
				ViewManager.showView(request, response, "admin/accessdenied.jsp");
			}
		}
	}

	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		doGet(request, response);
	}
	
	private void getUserList(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException, JSONException {
		/*Hashtable sessionAttributes = new Hashtable();
		sessionAttributes = (Hashtable) SessionManager.sessionExists(request, "pagination");
		int start = (Integer.parseInt(sessionAttributes.get("pageNo").toString())-1)*Integer.parseInt(sessionAttributes.get("limit").toString());*/
		
		Hashtable sessionAttributes = new Hashtable();
		sessionAttributes = (Hashtable) SessionManager.sessionExists(request, "admin");
		
		int order = Integer.parseInt(request.getParameter("order").toString());
		int pageNo = Integer.parseInt(request.getParameter("pageNo").toString());
		int limit = Integer.parseInt(request.getParameter("limit").toString());
		boolean begin = request.getParameter("begin").toLowerCase().equals("true") ? true : false;
		int start = (pageNo-1)*limit;
		String ordering = "cd_id desc";
		if(order == 1) {
			ordering = "cd_id asc";
		} else if(order == 2) {
			ordering = "cd_id desc";
		} else if(order == 3) {
			ordering = "cd_name asc";
		} else if(order == 4) {
			ordering = "cd_name desc";
		} 
		
		
		StringBuilder where = new StringBuilder();
		if(request.getParameter("isFiltered").toLowerCase().equals("true")) {
			String postfix = "";
			String[] filters = request.getParameterValues("filters[]");
			for(String filter : filters) {
				where.append(postfix);
				postfix = " or ";
				if(filter.equals("1")) {
					where.append("cd_gender=1");
				} else if(filter.equals("2")) {
					where.append("cd_gender=0");
				} else if(filter.equals("3")) {
					where.append("cd_is_placed=0");
				} else if(filter.equals("4")) {
					where.append("cd_is_placed=1");
				} else if(filter.equals("5")) {
					where.append("cd_payment=1");
				} else if(filter.equals("6")) {
					where.append("cd_payment=0");
				} else if(filter.equals("7")) {
					where.append("cd_is_active=1");
				} else if(filter.equals("8")) {
					where.append("cd_is_active=0");
				}
			}
		} else {
			where.append("1");
		}
		ucCandidate_list = new ArrayList<Uc_candidate_details>();
		ucCandidate_list = (List<Uc_candidate_details>)(Object) model.selectData("model.Uc_candidate_details", where.toString() + " order by " + ordering + " LIMIT " + start + "," + limit);
		if(ucCandidate_list.isEmpty()) {
			response.setContentType("application/json");
			JSONObject jsonResponse = new JSONObject();
			jsonResponse.put("table", "No users found.");
			jsonResponse.put("pageCount", 0);
			response.getWriter().write(jsonResponse.toString());
		} else {
			StringBuilder temp = new StringBuilder();
			List<Uc_interest_areas> ucInterest_list;
			Uc_candidate_details ucCandidate;
			Uc_industry_sector ucIndustry;
			Uc_position ucPosition;
			List<Uc_candidate_comments> ucCandidateComments_list;
			if(Integer.parseInt(sessionAttributes.get("utype").toString()) == UCConstants.MAIN_ADMIN) {
				temp.append("<table class=\"table table-striped table-bordered\" width=\"100%\"><tr><th>Sr. No.</th><th>Candidate Id</th><th>Name</th><th>Contact No.</th><th>Interest position</th><th>Exp. Salary</th><th>Curr. Salary</th><th>Exp. Years</th><th>Activate/Deactivate</th><th>Remove</th></tr>");
			} else {
				temp.append("<table class=\"table table-striped table-bordered\" width=\"100%\"><tr><th>Sr. No.</th><th>Candidate Id</th><th>Name</th><th>Contact No.</th><th>Interest position</th><th>Exp. Salary</th><th>Curr. Salary</th><th>Exp. Years</th></tr>");
			}
			for(int i = 0 ; i < ucCandidate_list.size() ; i++) {
				ucCandidate = new Uc_candidate_details();
				ucCandidate = ucCandidate_list.get(i);
				temp.append("<tr><td>" + (i+1) + "</td>");
				temp.append("<td>" + ucCandidate.getCd_id() + "</td>");
				temp.append("<td><a style=\"cursor:pointer\" onclick=\"showDetails(" + ucCandidate.getCd_id() + ")\">" + ucCandidate.getCd_name() + "</a></td>");
				temp.append("<td>" + ucCandidate.getCd_contact_num() + "</td>");
				ucInterest_list = (List<Uc_interest_areas>)(Object) model.selectData("model.Uc_interest_areas", "ia_candidate = " + ucCandidate.getCd_id());
				temp.append("<td>");
				String temppostfix = "";
				for(Uc_interest_areas single : ucInterest_list) {
					//ucIndustry = (Uc_industry_sector) model.selectData("model.Uc_industry_sector", "is_id=" + single.getIa_industry()).get(0);
					ucPosition = (Uc_position) model.selectData("model.Uc_position", "p_id=" + single.getIa_position()).get(0);
					temp.append(temppostfix);
					temppostfix = ", ";
					temp.append(ucPosition.getP_name());
				}
				temp.append("</td>");
				/*ucCandidateComments_list = (List<Uc_candidate_comments>)(Object) model.selectData("model.Uc_candidate_comments", "cc_candidate_id=" + ucCandidate.getCd_id());
				temp.append("<td><textarea style=\"width:100%\" maxlimit=\"500\" onblur=\"saveComment(this," + ucCandidate.getCd_id() + ")\">");
				if(!ucCandidateComments_list.isEmpty()) {
					temp.append(ucCandidateComments_list.get(0).getCc_comment());
				}
				temp.append("</textarea></td>");*/
				temp.append("<td>" + ucCandidate.getCd_expected_salary() + "</td>");
				if(ucCandidate.getCd_experience() == 1) {
					ucExperience_list = new ArrayList<Uc_experience>();
					ucExperience_list = (List<Uc_experience>)(Object) model.selectData("model.Uc_experience", "e_candidate_id=" + ucCandidate.getCd_id() + " order by e_id desc");
					if(!ucExperience_list.isEmpty()) {
						int tempSalary = 0;
						int tempYears = 0;
						for(Uc_experience exp : ucExperience_list) {
							if(exp.getE_is_current_job() == 1) {
								tempSalary = exp.getE_salary_per_month();
								tempYears = exp.getE_years();
								break;
							}
						}
						if(tempSalary == 0 && tempYears == 0) {
							for(Uc_experience exp : ucExperience_list) {
								if(exp.getE_salary_per_month() != 0) {
									tempSalary = exp.getE_salary_per_month();
									tempYears = exp.getE_years();
									break;
								}
							}
						}
						temp.append("<td>" + (tempSalary == 0 ? "N/A" : tempSalary) + "</td>");
						temp.append("<td>" + (tempYears == 0 ? "N/A" : tempYears) + "</td>");
					} else {
						temp.append("<td>N/A</td>");
						temp.append("<td>N/A</td>");
					}
				} else {
					temp.append("<td>Fresher</td>");
					temp.append("<td>Fresher</td>");
				}
				if(Integer.parseInt(sessionAttributes.get("utype").toString()) == UCConstants.MAIN_ADMIN) {
				if(ucCandidate.getCd_is_active() == 1) {
					temp.append("<td><button type=\"button\" onclick=\"deactivateUser(" + ucCandidate.getCd_id() + ")\" class=\"btn btn-warning\">Deactivate</button></td>");
				} else {
					temp.append("<td><button type=\"button\" onclick=\"activateUser(" + ucCandidate.getCd_id() + ")\" class=\"btn btn-warning\">Activate</button></td>");
				}
				temp.append("<td><button type=\"button\" onclick=\"removeUser(" + ucCandidate.getCd_id() + ")\" class=\"btn btn-danger\">Remove</button></td>");
				}
				temp.append("</tr>");
			}
			temp.append("</table>");
			response.setContentType("application/json");
			 JSONObject jsonResponse = new JSONObject();
			 jsonResponse.put("table", temp.toString());
			
			if(begin) {
			 ucCandidate_list = new ArrayList<Uc_candidate_details>();
				ucCandidate_list = (List<Uc_candidate_details>)(Object) model.selectData("model.Uc_candidate_details", where.toString());
				 int pageCount = (int) Math.ceil(ucCandidate_list.size()/(float)limit);
				if(pageCount == 0) pageCount = 1;
				 jsonResponse.put("pageCount", pageCount);
			}
			
			//response.setHeader("cache-control", "no-cache");
			//response.setCharacterEncoding("UTF-8");
			//response.setContentType("text/html;charset=UTF-8");
			
			//String result = "'{\"table\":\"" + temp.toString() + "\", \"pageCount\":\"" + pageCount + "\"}'";
			//PrintWriter out = response.getWriter();
			//out.println(jsonResponse.toString());
			//out.flush();
			response.getWriter().write(jsonResponse.toString());
		}
	}
	
	private void searchUser(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException, JSONException {
		
		Hashtable sessionAttributes = new Hashtable();
		sessionAttributes = (Hashtable) SessionManager.sessionExists(request, "admin");
		
		int order = Integer.parseInt(request.getParameter("order").toString());
		int pageNo = Integer.parseInt(request.getParameter("pageNo").toString());
		int limit = Integer.parseInt(request.getParameter("limit").toString());
		boolean begin = request.getParameter("begin").toLowerCase().equals("true") ? true : false;
		int start = (pageNo-1)*limit;
		String ordering = "cd_id desc";
		if(order == 1) {
			ordering = "cd_id asc";
		} else if(order == 2) {
			ordering = "cd_id desc";
		} else if(order == 3) {
			ordering = "cd_name asc";
		} else if(order == 4) {
			ordering = "cd_name desc";
		} 
		
		
		StringBuilder where = new StringBuilder();
		String postfix = "";
		
		if(request.getParameter("isFiltered").toLowerCase().equals("true")) {
			where.append("(");
			String[] filters = request.getParameterValues("filters[]");
			for(String filter : filters) {
				where.append(postfix);
				postfix = " and ";
				if(filter.equals("1")) {
					where.append("cd_gender=1");
				} else if(filter.equals("2")) {
					where.append("cd_gender=0");
				} else if(filter.equals("3")) {
					where.append("cd_is_placed=0");
				} else if(filter.equals("4")) {
					where.append("cd_is_placed=1");
				} else if(filter.equals("5")) {
					where.append("cd_payment=1");
				} else if(filter.equals("6")) {
					where.append("cd_payment=0");
				} else if(filter.equals("7")) {
					where.append("cd_is_active=1");
				} else if(filter.equals("8")) {
					where.append("cd_is_active=0");
				}
			}
			where.append(") and ");
		}
		
		postfix = "";
		int count = 0;
		ucPosition_list = new ArrayList<Uc_position>();
		ucInterest_list = new ArrayList<Uc_interest_areas>();
		ucExperience_list = new ArrayList<Uc_experience>();
		ucIndustry_list = new ArrayList<Uc_industry_sector>();
		List<Integer> positionCandidates = new ArrayList<Integer>();
		boolean extraFlag = false;
		
		while(true) {
			if(request.getParameterValues("extraParameters[" + count + "][]") == null) break;
			String val1 = request.getParameterValues("extraParameters[" + count + "][]")[0];
			String val2 = request.getParameterValues("extraParameters[" + count + "][]")[1];
			extraFlag = true;
			count++;
			where.append(postfix);
			postfix = " and ";
			if(val1.equals("1")) {
				where.append("cd_id=" + val2);
			} else if(val1.equals("2")) {
				where.append("cd_name like '%" + val2.trim() + "%'");
			} else if(val1.equals("3")) {
				where.append("cd_contact_num='" + val2 + "'");
			} else if(val1.equals("4")) {
				where.append("cd_email='" + val2 + "'");
			} else if(val1.equals("5")) {
				//position
				//StringTokenizer st = new StringTokenizer(val2,",");
				//StringBuilder temp = new StringBuilder();
				//String tempPostfix = "";
				//while(st.hasMoreTokens()) {
					//temp.append(tempPostfix);
					//tempPostfix = " or ";
					//temp.append("p_name LIKE '%"+st.nextToken().trim()+"%'");
				//}
				/*ucPosition_list = new ArrayList<Uc_position>();
				ucPosition_list = (List<Uc_position>)(Object) model.selectData("model.Uc_position", temp.toString());*/
				StringBuilder tempInterest = new StringBuilder();
				StringBuilder tempExperience = new StringBuilder();
				//tempPostfix = "";
				
				/*for(Uc_position pos : ucPosition_list) {
					tempInterest.append(tempPostfix);
					tempExperience.append(tempPostfix);
					tempPostfix = " or ";
					tempInterest.append("ia_position=" + pos.getP_id());
					tempExperience.append("e_position_id=" + pos.getP_id());
				}*/
				tempInterest.append("ia_position=" + val2);
				tempExperience.append("e_position_id=" + val2);
				ucInterest_list = new ArrayList<Uc_interest_areas>();
				ucExperience_list = new ArrayList<Uc_experience>();
				ucInterest_list = (List<Uc_interest_areas>)(Object) model.selectData("model.Uc_interest_areas", tempInterest.toString());
				ucExperience_list = (List<Uc_experience>)(Object) model.selectData("model.Uc_experience", tempExperience.toString());
				if(!ucInterest_list.isEmpty() || !ucExperience_list.isEmpty()) { 
					where.append("(");
				String tempPostfix = "";
				for(Uc_interest_areas interest : ucInterest_list) {
					//positionCandidates.add(interest.getIa_candidate());
					where.append(tempPostfix);
					tempPostfix = " or ";
					where.append("cd_id=" + interest.getIa_candidate());
				}
				for(Uc_experience exp : ucExperience_list) {
					//positionCandidates.add(exp.getE_candidate_id());
					where.append(tempPostfix);
					tempPostfix = " or ";
					where.append("cd_id=" + exp.getE_candidate_id());
				}
				 where.append(")");
				} else {
					where.append("0");
				}
				
			} else if(val1.equals("6")) {
				//industrySector
				StringTokenizer st = new StringTokenizer(val2,",");
				StringBuilder temp = new StringBuilder();
				String tempPostfix = "";
				while(st.hasMoreTokens()) {
					temp.append(tempPostfix);
					tempPostfix = " or ";
					temp.append("is_name LIKE '%"+st.nextToken().trim()+"%'");
				}
				ucIndustry_list = new ArrayList<Uc_industry_sector>();
				ucIndustry_list = (List<Uc_industry_sector>)(Object) model.selectData("model.Uc_industry_sector", temp.toString());
				StringBuilder tempInterest = new StringBuilder();
				StringBuilder tempExperience = new StringBuilder();
				tempPostfix = "";
				for(Uc_industry_sector indus : ucIndustry_list) {
					tempInterest.append(tempPostfix);
					tempExperience.append(tempPostfix);
					tempPostfix = " or ";
					tempInterest.append("ia_industry=" + indus.getIs_id());
					tempExperience.append("e_industry_id=" + indus.getIs_id());
				}
				ucInterest_list = new ArrayList<Uc_interest_areas>();
				ucExperience_list = new ArrayList<Uc_experience>();
				ucInterest_list = (List<Uc_interest_areas>)(Object) model.selectData("model.Uc_interest_areas", tempInterest.toString());
				ucExperience_list = (List<Uc_experience>)(Object) model.selectData("model.Uc_experience", tempExperience.toString());
				if(!ucInterest_list.isEmpty() || !ucExperience_list.isEmpty()) { 
					where.append("(");
				tempPostfix = "";
				for(Uc_interest_areas interest : ucInterest_list) {
					//positionCandidates.add(interest.getIa_candidate());
					where.append(tempPostfix);
					tempPostfix = " or ";
					where.append("cd_id=" + interest.getIa_candidate());
				}
				for(Uc_experience exp : ucExperience_list) {
					//positionCandidates.add(exp.getE_candidate_id());
					where.append(tempPostfix);
					tempPostfix = " or ";
					where.append("cd_id=" + exp.getE_candidate_id());
				}
				where.append(")");
				} else {
					where.append("0");
				}
				
			} else if(val1.equals("7")) {
				//experience
				StringTokenizer st = new StringTokenizer(val2, "-");
				String token1, token2, tempExp;
				token1 = st.nextToken();
				token2 = st.nextToken();
				if(token1.equals(token2)) {
					tempExp = "e_years=" + token1;
				} else {
					if(token2.equals("*")) {
						tempExp = "e_years>=" + token1;
					} else {
						tempExp = "(e_years>=" + token1 + " and e_years<=" + token2 + ")";
					}
				}
				ucExperience_list = new ArrayList<Uc_experience>();
				ucExperience_list = (List<Uc_experience>)(Object) model.selectData("model.Uc_experience", tempExp);
				if(!ucExperience_list.isEmpty()) { 
					where.append("(");
				String tempPostfix = "";
				for(Uc_experience exp : ucExperience_list) {
					where.append(tempPostfix);
					tempPostfix = " or ";
					where.append("cd_id=" + exp.getE_candidate_id());
				}
				 where.append(")");
				} else {
					where.append("0");
				}
				
				
			} else if(val1.equals("8")) {
				//location
				char c = val2.charAt(0);
				if(c == '*') {
					String str = val2.substring(1);
					StringTokenizer st = new StringTokenizer(str, "-");
					String state = st.nextToken().trim();
					String district = st.nextToken().trim();
					String taluka = st.nextToken().trim();
					String city = st.nextToken().trim();
					
					/*ucDistrict_list = new ArrayList<Uc_district>();
					ucDistrict_list = (List<Uc_district>)(Object) model.selectData("model.Uc_district", "d_name='" + district + "'");
					if(!ucDistrict_list.isEmpty()) {
						String tempPostfix = "(";
						for(Uc_district d : ucDistrict_list) {
							ucCity_list = new ArrayList<Uc_city>();
							ucCity_list = (List<Uc_city>)(Object) model.selectData("model.Uc_city", "c_district=" + d.getD_id() + " and c_name='" + city + "'");
							if(!ucCity_list.isEmpty()) {
								for(Uc_city ct : ucCity_list) {
									where.append(tempPostfix);
									tempPostfix = " or ";
									where.append("cd_city_id=" + ct.getC_id());
								}
							}
 						}
						if(!where.toString().equals("")) where.append(")");
					} else {
						where.append("0");
					}
				} else {
					where.append("cd_city_id=" + val2);
				}*/
					
					StringBuilder cityWhere = new StringBuilder();
					String temppostfix = "(";
					
					if(!city.equals("0")) {
						if(!taluka.equals("0")) {
							if(!district.equals("0")) {
								//city and taluka and district and state(other)
								ucDistrict_list = new ArrayList<Uc_district>();
								ucDistrict_list = (List<Uc_district>)(Object) model.selectData("model.Uc_district", "d_name like '%" + district + "%' and d_s_id=" + Integer.parseInt(state));
								if(!ucDistrict_list.isEmpty()) {
									for(Uc_district d : ucDistrict_list) {
										ucTaluka_list = new ArrayList<Uc_taluka>();
										ucTaluka_list = (List<Uc_taluka>)(Object) model.selectData("model.Uc_taluka", "t_name like '%" + taluka + "%' and t_district_id=" + d.getD_id());
										if(!ucTaluka_list.isEmpty()) {
											for(Uc_taluka t : ucTaluka_list) {
												ucCity_list = new ArrayList<Uc_city>();
												ucCity_list = (List<Uc_city>)(Object) model.selectData("model.Uc_city", "c_name like '%" + city + "%' and c_taluka_id=" + t.getT_id());
												if(!ucCity_list.isEmpty()) {
													for(Uc_city cty: ucCity_list) {
														cityWhere.append(temppostfix);
														temppostfix = " or ";
														cityWhere.append("cd_city_id=" + cty.getC_id());
													}
												}
											} 
										}
									}
								}
							} else {
								//city and taluka and state(other)
								ucDistrict_list = new ArrayList<Uc_district>();
								ucDistrict_list = (List<Uc_district>)(Object) model.selectData("model.Uc_district", "d_s_id=" + Integer.parseInt(state));
								if(!ucDistrict_list.isEmpty()) {
									for(Uc_district d : ucDistrict_list) {
										ucTaluka_list = new ArrayList<Uc_taluka>();
										ucTaluka_list = (List<Uc_taluka>)(Object) model.selectData("model.Uc_taluka", "t_name like '%" + taluka + "%' and t_district_id=" + d.getD_id());
										if(!ucTaluka_list.isEmpty()) {
											for(Uc_taluka t : ucTaluka_list) {
												ucCity_list = new ArrayList<Uc_city>();
												ucCity_list = (List<Uc_city>)(Object) model.selectData("model.Uc_city", "c_name like '%" + city + "%' and c_taluka_id=" + t.getT_id());
												if(!ucCity_list.isEmpty()) {
													for(Uc_city cty: ucCity_list) {
														cityWhere.append(temppostfix);
														temppostfix = " or ";
														cityWhere.append("cd_city_id=" + cty.getC_id());
													}
												}
											} 
										}
									}
								}
							}
						} else {
							if(!district.equals("0")) {
								//city and district and state(other)
								ucDistrict_list = new ArrayList<Uc_district>();
								ucDistrict_list = (List<Uc_district>)(Object) model.selectData("model.Uc_district", "d_name like '%" + district + "%' and d_s_id=" + Integer.parseInt(state));
								if(!ucDistrict_list.isEmpty()) {
									for(Uc_district d : ucDistrict_list) {
										ucTaluka_list = new ArrayList<Uc_taluka>();
										ucTaluka_list = (List<Uc_taluka>)(Object) model.selectData("model.Uc_taluka", "t_district_id=" + d.getD_id());
										if(!ucTaluka_list.isEmpty()) {
											for(Uc_taluka t : ucTaluka_list) {
												ucCity_list = new ArrayList<Uc_city>();
												ucCity_list = (List<Uc_city>)(Object) model.selectData("model.Uc_city", "c_name like '%" + city + "%' and c_taluka_id=" + t.getT_id());
												if(!ucCity_list.isEmpty()) {
													for(Uc_city cty: ucCity_list) {
														cityWhere.append(temppostfix);
														temppostfix = " or ";
														cityWhere.append("cd_city_id=" + cty.getC_id());
													}
												}
											} 
										}
									}
								}
							} else {
								//city and state(other)
								ucDistrict_list = new ArrayList<Uc_district>();
								ucDistrict_list = (List<Uc_district>)(Object) model.selectData("model.Uc_district", "d_s_id=" + Integer.parseInt(state));
								if(!ucDistrict_list.isEmpty()) {
									for(Uc_district d : ucDistrict_list) {
										ucTaluka_list = new ArrayList<Uc_taluka>();
										ucTaluka_list = (List<Uc_taluka>)(Object) model.selectData("model.Uc_taluka", "t_district_id=" + d.getD_id());
										if(!ucTaluka_list.isEmpty()) {
											for(Uc_taluka t : ucTaluka_list) {
												ucCity_list = new ArrayList<Uc_city>();
												ucCity_list = (List<Uc_city>)(Object) model.selectData("model.Uc_city", "c_name like '%" + city + "%' and c_taluka_id=" + t.getT_id());
												if(!ucCity_list.isEmpty()) {
													for(Uc_city cty: ucCity_list) {
														cityWhere.append(temppostfix);
														temppostfix = " or ";
														cityWhere.append("cd_city_id=" + cty.getC_id());
													}
												}
											} 
										}
									}
								}
							}
						}
					} else {
						if(!taluka.equals("0")) {
							if(!district.equals("0")) {
								//taluka and district and state(other)
								ucDistrict_list = new ArrayList<Uc_district>();
								ucDistrict_list = (List<Uc_district>)(Object) model.selectData("model.Uc_district", "d_name like '%" + district + "%' and d_s_id=" + Integer.parseInt(state));
								if(!ucDistrict_list.isEmpty()) {
									for(Uc_district d : ucDistrict_list) {
										ucTaluka_list = new ArrayList<Uc_taluka>();
										ucTaluka_list = (List<Uc_taluka>)(Object) model.selectData("model.Uc_taluka", "t_name like '%" + taluka + "%' and t_district_id=" + d.getD_id());
										if(!ucTaluka_list.isEmpty()) {
											for(Uc_taluka t : ucTaluka_list) {
												ucCity_list = new ArrayList<Uc_city>();
												ucCity_list = (List<Uc_city>)(Object) model.selectData("model.Uc_city", "c_taluka_id=" + t.getT_id());
												if(!ucCity_list.isEmpty()) {
													for(Uc_city cty: ucCity_list) {
														cityWhere.append(temppostfix);
														temppostfix = " or ";
														cityWhere.append("cd_city_id=" + cty.getC_id());
													}
												}
											} 
										}
									}
								}
							} else {
								//taluka and state(other)
								ucDistrict_list = new ArrayList<Uc_district>();
								ucDistrict_list = (List<Uc_district>)(Object) model.selectData("model.Uc_district", "d_s_id=" + Integer.parseInt(state));
								if(!ucDistrict_list.isEmpty()) {
									for(Uc_district d : ucDistrict_list) {
										ucTaluka_list = new ArrayList<Uc_taluka>();
										ucTaluka_list = (List<Uc_taluka>)(Object) model.selectData("model.Uc_taluka", "t_name like '%" + taluka + "%' and t_district_id=" + d.getD_id());
										if(!ucTaluka_list.isEmpty()) {
											for(Uc_taluka t : ucTaluka_list) {
												ucCity_list = new ArrayList<Uc_city>();
												ucCity_list = (List<Uc_city>)(Object) model.selectData("model.Uc_city", "c_taluka_id=" + t.getT_id());
												if(!ucCity_list.isEmpty()) {
													for(Uc_city cty: ucCity_list) {
														cityWhere.append(temppostfix);
														temppostfix = " or ";
														cityWhere.append("cd_city_id=" + cty.getC_id());
													}
												}
											} 
										}
									}
								}
							}
						} else {
							if(!district.equals("0")) {
								//district and state(other)
								ucDistrict_list = new ArrayList<Uc_district>();
								ucDistrict_list = (List<Uc_district>)(Object) model.selectData("model.Uc_district", "d_name like '%" + district + "%' and d_s_id=" + Integer.parseInt(state));
								if(!ucDistrict_list.isEmpty()) {
									for(Uc_district d : ucDistrict_list) {
										ucTaluka_list = new ArrayList<Uc_taluka>();
										ucTaluka_list = (List<Uc_taluka>)(Object) model.selectData("model.Uc_taluka", "t_district_id=" + d.getD_id());
										if(!ucTaluka_list.isEmpty()) {
											for(Uc_taluka t : ucTaluka_list) {
												ucCity_list = new ArrayList<Uc_city>();
												ucCity_list = (List<Uc_city>)(Object) model.selectData("model.Uc_city", "c_taluka_id=" + t.getT_id());
												if(!ucCity_list.isEmpty()) {
													for(Uc_city cty: ucCity_list) {
														cityWhere.append(temppostfix);
														temppostfix = " or ";
														cityWhere.append("cd_city_id=" + cty.getC_id());
													}
												}
											} 
										}
									}
								}
							} else {
								//state(other)
								ucDistrict_list = new ArrayList<Uc_district>();
								ucDistrict_list = (List<Uc_district>)(Object) model.selectData("model.Uc_district", "d_s_id=" + Integer.parseInt(state));
								if(!ucDistrict_list.isEmpty()) {
									for(Uc_district d : ucDistrict_list) {
										ucTaluka_list = new ArrayList<Uc_taluka>();
										ucTaluka_list = (List<Uc_taluka>)(Object) model.selectData("model.Uc_taluka", "t_district_id=" + d.getD_id());
										if(!ucTaluka_list.isEmpty()) {
											for(Uc_taluka t : ucTaluka_list) {
												ucCity_list = new ArrayList<Uc_city>();
												ucCity_list = (List<Uc_city>)(Object) model.selectData("model.Uc_city", "c_taluka_id=" + t.getT_id());
												if(!ucCity_list.isEmpty()) {
													for(Uc_city cty: ucCity_list) {
														cityWhere.append(temppostfix);
														temppostfix = " or ";
														cityWhere.append("cd_city_id=" + cty.getC_id());
													}
												}
											} 
										}
									}
								}
							}
						}
					}
					
					if(cityWhere.toString().equals("")) {
						where.append("0");
					} else {
						cityWhere.append(")");
						where.append(cityWhere);
					}
					
				}  else {
					StringTokenizer st = new StringTokenizer(val2, "-");
					String state = st.nextToken().trim();
					String district = st.nextToken().trim();
					String taluka = st.nextToken().trim();
					String city = st.nextToken().trim();
					
					StringBuilder cityWhere = new StringBuilder();
					String temppostfix = "(";
					
					if(!city.equals("0")) {
						if(!taluka.equals("0")) {
							if(!district.equals("0")) {
								//city and taluka and district and state(gujarat)
								ucDistrict_list = new ArrayList<Uc_district>();
								ucDistrict_list = (List<Uc_district>)(Object) model.selectData("model.Uc_district", "d_id=" + district + " and d_s_id=" + Integer.parseInt(state));
								if(!ucDistrict_list.isEmpty()) {
									for(Uc_district d : ucDistrict_list) {
										ucTaluka_list = new ArrayList<Uc_taluka>();
										ucTaluka_list = (List<Uc_taluka>)(Object) model.selectData("model.Uc_taluka", "t_id=" + taluka + " and t_district_id=" + d.getD_id());
										if(!ucTaluka_list.isEmpty()) {
											for(Uc_taluka t : ucTaluka_list) {
												ucCity_list = new ArrayList<Uc_city>();
												ucCity_list = (List<Uc_city>)(Object) model.selectData("model.Uc_city", "c_id=" + city + " and c_taluka_id=" + t.getT_id());
												if(!ucCity_list.isEmpty()) {
													for(Uc_city cty: ucCity_list) {
														cityWhere.append(temppostfix);
														temppostfix = " or ";
														cityWhere.append("cd_city_id=" + cty.getC_id());
													}
												}
											} 
										}
									}
								}
							} else {
								//city and taluka and state(gujarat)
								ucDistrict_list = new ArrayList<Uc_district>();
								ucDistrict_list = (List<Uc_district>)(Object) model.selectData("model.Uc_district", " d_s_id=" + Integer.parseInt(state));
								if(!ucDistrict_list.isEmpty()) {
									for(Uc_district d : ucDistrict_list) {
										ucTaluka_list = new ArrayList<Uc_taluka>();
										ucTaluka_list = (List<Uc_taluka>)(Object) model.selectData("model.Uc_taluka", "t_id=" + taluka + " and t_district_id=" + d.getD_id());
										if(!ucTaluka_list.isEmpty()) {
											for(Uc_taluka t : ucTaluka_list) {
												ucCity_list = new ArrayList<Uc_city>();
												ucCity_list = (List<Uc_city>)(Object) model.selectData("model.Uc_city", "c_id=" + city + " and c_taluka_id=" + t.getT_id());
												if(!ucCity_list.isEmpty()) {
													for(Uc_city cty: ucCity_list) {
														cityWhere.append(temppostfix);
														temppostfix = " or ";
														cityWhere.append("cd_city_id=" + cty.getC_id());
													}
												}
											} 
										}
									}
								}
							}
						} else {
							if(!district.equals("0")) {
								//city and district and state(gujarat)
								ucDistrict_list = new ArrayList<Uc_district>();
								ucDistrict_list = (List<Uc_district>)(Object) model.selectData("model.Uc_district", "d_id=" + district + " and d_s_id=" + Integer.parseInt(state));
								if(!ucDistrict_list.isEmpty()) {
									for(Uc_district d : ucDistrict_list) {
										ucTaluka_list = new ArrayList<Uc_taluka>();
										ucTaluka_list = (List<Uc_taluka>)(Object) model.selectData("model.Uc_taluka", "t_district_id=" + d.getD_id());
										if(!ucTaluka_list.isEmpty()) {
											for(Uc_taluka t : ucTaluka_list) {
												ucCity_list = new ArrayList<Uc_city>();
												ucCity_list = (List<Uc_city>)(Object) model.selectData("model.Uc_city", "c_id=" + city + " and c_taluka_id=" + t.getT_id());
												if(!ucCity_list.isEmpty()) {
													for(Uc_city cty: ucCity_list) {
														cityWhere.append(temppostfix);
														temppostfix = " or ";
														cityWhere.append("cd_city_id=" + cty.getC_id());
													}
												}
											} 
										}
									}
								}
								
							} else {
								//city and state(gujarat)
								ucDistrict_list = new ArrayList<Uc_district>();
								ucDistrict_list = (List<Uc_district>)(Object) model.selectData("model.Uc_district", "d_s_id=" + Integer.parseInt(state));
								if(!ucDistrict_list.isEmpty()) {
									for(Uc_district d : ucDistrict_list) {
										ucTaluka_list = new ArrayList<Uc_taluka>();
										ucTaluka_list = (List<Uc_taluka>)(Object) model.selectData("model.Uc_taluka", "t_district_id=" + d.getD_id());
										if(!ucTaluka_list.isEmpty()) {
											for(Uc_taluka t : ucTaluka_list) {
												ucCity_list = new ArrayList<Uc_city>();
												ucCity_list = (List<Uc_city>)(Object) model.selectData("model.Uc_city", "c_id=" + city + " and c_taluka_id=" + t.getT_id());
												if(!ucCity_list.isEmpty()) {
													for(Uc_city cty: ucCity_list) {
														cityWhere.append(temppostfix);
														temppostfix = " or ";
														cityWhere.append("cd_city_id=" + cty.getC_id());
													}
												}
											} 
										}
									}
								}
							}
						}
					} else {
						if(!taluka.equals("0")) {
							if(!district.equals("0")) {
								//taluka and district and state(gujarat)
								ucDistrict_list = new ArrayList<Uc_district>();
								ucDistrict_list = (List<Uc_district>)(Object) model.selectData("model.Uc_district", "d_id=" + district + " and d_s_id=" + Integer.parseInt(state));
								if(!ucDistrict_list.isEmpty()) {
									for(Uc_district d : ucDistrict_list) {
										ucTaluka_list = new ArrayList<Uc_taluka>();
										ucTaluka_list = (List<Uc_taluka>)(Object) model.selectData("model.Uc_taluka", "t_id=" + taluka + " and t_district_id=" + d.getD_id());
										if(!ucTaluka_list.isEmpty()) {
											for(Uc_taluka t : ucTaluka_list) {
												ucCity_list = new ArrayList<Uc_city>();
												ucCity_list = (List<Uc_city>)(Object) model.selectData("model.Uc_city", "c_taluka_id=" + t.getT_id());
												if(!ucCity_list.isEmpty()) {
													for(Uc_city cty: ucCity_list) {
														cityWhere.append(temppostfix);
														temppostfix = " or ";
														cityWhere.append("cd_city_id=" + cty.getC_id());
													}
												}
											} 
										}
									}
								}
							} else {
								//taluka and state(gujarat)
								ucDistrict_list = new ArrayList<Uc_district>();
								ucDistrict_list = (List<Uc_district>)(Object) model.selectData("model.Uc_district", "d_s_id=" + Integer.parseInt(state));
								if(!ucDistrict_list.isEmpty()) {
									for(Uc_district d : ucDistrict_list) {
										ucTaluka_list = new ArrayList<Uc_taluka>();
										ucTaluka_list = (List<Uc_taluka>)(Object) model.selectData("model.Uc_taluka", "t_id=" + taluka + " and t_district_id=" + d.getD_id());
										if(!ucTaluka_list.isEmpty()) {
											for(Uc_taluka t : ucTaluka_list) {
												ucCity_list = new ArrayList<Uc_city>();
												ucCity_list = (List<Uc_city>)(Object) model.selectData("model.Uc_city", "c_taluka_id=" + t.getT_id());
												if(!ucCity_list.isEmpty()) {
													for(Uc_city cty: ucCity_list) {
														cityWhere.append(temppostfix);
														temppostfix = " or ";
														cityWhere.append("cd_city_id=" + cty.getC_id());
													}
												}
											} 
										}
									}
								}
							}
						} else {
							if(!district.equals("0")) {
								//district and state(gujarat)
								ucDistrict_list = new ArrayList<Uc_district>();
								ucDistrict_list = (List<Uc_district>)(Object) model.selectData("model.Uc_district", "d_id=" + district + " and d_s_id=" + Integer.parseInt(state));
								if(!ucDistrict_list.isEmpty()) {
									for(Uc_district d : ucDistrict_list) {
										ucTaluka_list = new ArrayList<Uc_taluka>();
										ucTaluka_list = (List<Uc_taluka>)(Object) model.selectData("model.Uc_taluka", "t_district_id=" + d.getD_id());
										if(!ucTaluka_list.isEmpty()) {
											for(Uc_taluka t : ucTaluka_list) {
												ucCity_list = new ArrayList<Uc_city>();
												ucCity_list = (List<Uc_city>)(Object) model.selectData("model.Uc_city", "c_taluka_id=" + t.getT_id());
												if(!ucCity_list.isEmpty()) {
													for(Uc_city cty: ucCity_list) {
														cityWhere.append(temppostfix);
														temppostfix = " or ";
														cityWhere.append("cd_city_id=" + cty.getC_id());
													}
												}
											} 
										}
									}
								}
							} else {
								//state(gujarat)
								ucDistrict_list = new ArrayList<Uc_district>();
								ucDistrict_list = (List<Uc_district>)(Object) model.selectData("model.Uc_district", "d_s_id=" + Integer.parseInt(state));
								if(!ucDistrict_list.isEmpty()) {
									for(Uc_district d : ucDistrict_list) {
										ucTaluka_list = new ArrayList<Uc_taluka>();
										ucTaluka_list = (List<Uc_taluka>)(Object) model.selectData("model.Uc_taluka", "t_district_id=" + d.getD_id());
										if(!ucTaluka_list.isEmpty()) {
											for(Uc_taluka t : ucTaluka_list) {
												ucCity_list = new ArrayList<Uc_city>();
												ucCity_list = (List<Uc_city>)(Object) model.selectData("model.Uc_city", "c_taluka_id=" + t.getT_id());
												if(!ucCity_list.isEmpty()) {
													for(Uc_city cty: ucCity_list) {
														cityWhere.append(temppostfix);
														temppostfix = " or ";
														cityWhere.append("cd_city_id=" + cty.getC_id());
													}
												}
											} 
										}
									}
								}
							}
						}
					}
					
					if(cityWhere.toString().equals("")) {
						where.append("0");
					} else {
						cityWhere.append(")");
						where.append(cityWhere);
					}
				}
			} else if(val1.equals("9")) {
				//qualifications
				ucQualification_list = new ArrayList<Uc_candidate_qualification>();
				ucQualification_list = (List<Uc_candidate_qualification>)(Object) model.selectData("model.Uc_candidate_qualification", "cq_quali_id=" + val2);
				if(!ucQualification_list.isEmpty()) {
					where.append("(");
					String temppostfix = "";
					for(Uc_candidate_qualification q : ucQualification_list) {
						where.append(temppostfix);
						temppostfix = " or ";
						where.append("cd_id=" + q.getCq_candidate_id());
					}
					where.append(")");
				} else {
					where.append("0");
				}
			}
		}
		
		//error here
		//if(where.toString().equals("")) where.append("1");
		
		//edit here
		
		if(extraFlag && where.toString().equals("")) {
			response.setContentType("application/json");
			JSONObject jsonResponse = new JSONObject();
			jsonResponse.put("table", "No users found.");
			jsonResponse.put("pageCount", 0);
			response.getWriter().write(jsonResponse.toString());
		} else {
			ucCandidate_list = new ArrayList<Uc_candidate_details>();
			ucCandidate_list = (List<Uc_candidate_details>)(Object) model.selectData("model.Uc_candidate_details", where.toString() + " order by " + ordering + " LIMIT " + start + "," + limit);
			if(ucCandidate_list.isEmpty()) {
				response.setContentType("application/json");
				JSONObject jsonResponse = new JSONObject();
				jsonResponse.put("table", "No users found.");
				jsonResponse.put("pageCount", 0);
				response.getWriter().write(jsonResponse.toString());
			} else {
				StringBuilder temp = new StringBuilder();
				List<Uc_interest_areas> ucInterest_list;
				Uc_candidate_details ucCandidate;
				Uc_industry_sector ucIndustry;
				Uc_position ucPosition;
				List<Uc_candidate_comments> ucCandidateComments_list;
				if(Integer.parseInt(sessionAttributes.get("utype").toString()) == UCConstants.MAIN_ADMIN) {
					temp.append("<table class=\"table table-striped table-bordered\" width=\"100%\"><tr><th>Sr. No.</th><th>Candidate Id</th><th>Name</th><th>Contact No.</th><th>Interest position</th><th>Exp. Salary</th><th>Curr. Salary</th><th>Activate/Deactivate</th><th>Remove</th></tr>");
				} else {
					temp.append("<table class=\"table table-striped table-bordered\" width=\"100%\"><tr><th>Sr. No.</th><th>Candidate Id</th><th>Name</th><th>Contact No.</th><th>Interest position</th><th>Exp. Salary</th><th>Curr. Salary</th></tr>");
				}
				for(int i = 0 ; i < ucCandidate_list.size() ; i++) {
					ucCandidate = new Uc_candidate_details();
					ucCandidate = ucCandidate_list.get(i);
					temp.append("<tr><td>" + (i+1) + "</td>");
					temp.append("<td>" + ucCandidate.getCd_id() + "</td>");
					temp.append("<td><a style=\"cursor:pointer\" onclick=\"showDetails(" + ucCandidate.getCd_id() + ")\">" + ucCandidate.getCd_name() + "</a></td>");
					temp.append("<td>" + ucCandidate.getCd_contact_num() + "</td>");
					ucInterest_list = (List<Uc_interest_areas>)(Object) model.selectData("model.Uc_interest_areas", "ia_candidate = " + ucCandidate.getCd_id());
					temp.append("<td>");
					String temppostfix = "";
					for(Uc_interest_areas single : ucInterest_list) {
						//ucIndustry = (Uc_industry_sector) model.selectData("model.Uc_industry_sector", "is_id=" + single.getIa_industry()).get(0);
						ucPosition = (Uc_position) model.selectData("model.Uc_position", "p_id=" + single.getIa_position()).get(0);
						temp.append(temppostfix);
						temppostfix = ", ";
						temp.append(ucPosition.getP_name());
					}
					temp.append("</td>");
					/*ucCandidateComments_list = (List<Uc_candidate_comments>)(Object) model.selectData("model.Uc_candidate_comments", "cc_candidate_id=" + ucCandidate.getCd_id());
					temp.append("<td><textarea style=\"width:100%\" maxlimit=\"500\" onblur=\"saveComment(this," + ucCandidate.getCd_id() + ")\">");
					if(!ucCandidateComments_list.isEmpty()) {
						temp.append(ucCandidateComments_list.get(0).getCc_comment());
					}
					temp.append("</textarea></td>");*/
					
					temp.append("<td>" + ucCandidate.getCd_expected_salary() + "</td>");
					if(ucCandidate.getCd_experience() == 1) {
						ucExperience_list = new ArrayList<Uc_experience>();
						ucExperience_list = (List<Uc_experience>)(Object) model.selectData("model.Uc_experience", "e_candidate_id=" + ucCandidate.getCd_id() + " order by e_id desc");
						if(!ucExperience_list.isEmpty()) {
							int tempSalary = 0;
							for(Uc_experience exp : ucExperience_list) {
								if(exp.getE_is_current_job() == 1) {
									tempSalary = exp.getE_salary_per_month();
									break;
								}
							}
							if(tempSalary == 0) {
								for(Uc_experience exp : ucExperience_list) {
									if(exp.getE_salary_per_month() != 0) {
										tempSalary = exp.getE_salary_per_month();
										break;
									}
								}
							}
							temp.append("<td>" + (tempSalary == 0 ? "N/A" : tempSalary) + "</td>");
						} else {
							temp.append("<td>N/A</td>");
						}
					} else {
						temp.append("<td>Fresher</td>");
					}
					
					if(Integer.parseInt(sessionAttributes.get("utype").toString()) == UCConstants.MAIN_ADMIN) {
					if(ucCandidate.getCd_is_active() == 1) {
						temp.append("<td><button type=\"button\" onclick=\"deactivateUser(" + ucCandidate.getCd_id() + ")\" class=\"btn btn-warning\">Deactivate</button></td>");
					} else {
						temp.append("<td><button type=\"button\" onclick=\"activateUser(" + ucCandidate.getCd_id() + ")\" class=\"btn btn-warning\">Activate</button></td>");
					}
					temp.append("<td><button type=\"button\" onclick=\"removeUser(" + ucCandidate.getCd_id() + ")\" class=\"btn btn-danger\">Remove</button></td>");
					}
					temp.append("</tr>");
				}
				temp.append("</table>");
				response.setContentType("application/json");
				 JSONObject jsonResponse = new JSONObject();
				 jsonResponse.put("table", temp.toString());
				
				if(begin) {
					ucCandidate_list = new ArrayList<Uc_candidate_details>();
					ucCandidate_list = (List<Uc_candidate_details>)(Object) model.selectData("model.Uc_candidate_details", where.toString());
					 int pageCount = (int) Math.ceil(ucCandidate_list.size()/(float)limit);
					if(pageCount == 0) pageCount = 1;
					 jsonResponse.put("pageCount", pageCount);
				}
				
				//response.setHeader("cache-control", "no-cache");
				//response.setCharacterEncoding("UTF-8");
				//response.setContentType("text/html;charset=UTF-8");
				
				//String result = "'{\"table\":\"" + temp.toString() + "\", \"pageCount\":\"" + pageCount + "\"}'";
				//PrintWriter out = response.getWriter();
				//out.println(jsonResponse.toString());
				//out.flush();
				response.getWriter().write(jsonResponse.toString());
			}
		}
	}
	
	private void getCandidateComments(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException, JSONException {
		int candidateId = Integer.parseInt(request.getParameterValues("extraParameters[0][]")[0].toString());
		Hashtable sessionAttributes = new Hashtable();
		sessionAttributes = (Hashtable) SessionManager.sessionExists(request, "admin");
		int pageNo = Integer.parseInt(request.getParameter("pageNo").toString());
		int limit = Integer.parseInt(request.getParameter("limit").toString());
		boolean begin = request.getParameter("begin").toLowerCase().equals("true") ? true : false;
		int start = (pageNo-1)*limit;
		ucCandidateComments_list = new ArrayList<Uc_candidate_comments>();
		ucCandidateComments_list = (List<Uc_candidate_comments>)(Object) model.selectData("model.Uc_candidate_comments", "cc_candidate_id=" + candidateId + " order by cc_id desc LIMIT " + start + "," + limit);
		if(ucCandidateComments_list.isEmpty()) {
			response.setContentType("application/json");
			JSONObject jsonResponse = new JSONObject();
			jsonResponse.put("table", "No comments are submitted yet.");
			jsonResponse.put("pageCount", 0);
			response.getWriter().write(jsonResponse.toString());
		} else {
			StringBuilder temp = new StringBuilder();
			
			temp.append("<table class=\"table table-bordered table-striped\">");
			temp.append("<tr><th>Comment</th><th>Date - Time</th><th>Delete</th></tr>");
			for(Uc_candidate_comments cc : ucCandidateComments_list) {
				temp.append("<tr>");
				if(Integer.parseInt(sessionAttributes.get("uuid").toString()) == cc.getCc_added_by() || Integer.parseInt(sessionAttributes.get("utype").toString()) == UCConstants.MAIN_ADMIN) {
					temp.append("<td><textarea maxlimit=\"500\" onblur=\"saveCommentChanges(this," + cc.getCc_id() + ", " + UCConstants.CANDIDATE_COMMENT + ")\">" + cc.getCc_comment() + "</textarea></td>");
					temp.append("<td>" + cc.getCc_date() + " - " + cc.getCc_time() + "</td>");
					temp.append("<td><button type=\"button\" class=\"btn btn-danger\" onclick=\"deleteComment(" + candidateId + "," + cc.getCc_id() + ", " + UCConstants.CANDIDATE_COMMENT + ")\"><i class=\"fa fa-ban\"></i></button></td>");
				} else {
					temp.append("<td>" + cc.getCc_comment() + "</td>");
					temp.append("<td>" + cc.getCc_date() + " - " + cc.getCc_time() + "</td>");
					temp.append("<td>---</td>");
				}
				temp.append("</tr>");
			}
			temp.append("</table>");
			
			response.setContentType("application/json");
			 JSONObject jsonResponse = new JSONObject();
			 jsonResponse.put("table", temp.toString());
			 if(begin) {
				 ucCandidateComments_list = new ArrayList<Uc_candidate_comments>();
				 ucCandidateComments_list = (List<Uc_candidate_comments>)(Object) model.selectData("model.Uc_candidate_comments", "cc_candidate_id=" + candidateId);
				 int pageCount = (int) Math.ceil(ucCandidateComments_list.size()/(float)limit);
				 if(pageCount == 0) pageCount = 1;
				 jsonResponse.put("pageCount", pageCount);
			 }
			 response.getWriter().write(jsonResponse.toString());
		}
	}
	
	
	protected boolean saveCommentChanges(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		boolean successFlag;
		Hashtable sessionAttributes = new Hashtable();
		sessionAttributes = (Hashtable) SessionManager.sessionExists(request, "admin");
		String comment = request.getParameter("comment").toString();
		int id = Integer.parseInt(request.getParameter("id").toString());
			ucCandidate_comments = new Uc_candidate_comments();
			ucCandidate_comments.setCc_comment(comment);
			successFlag = model.updateData(ucCandidate_comments, "cc_id=" + id);
		return successFlag;
	}
	
	private void saveComment(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		String comment = request.getParameter("comment").toString();
		long candidateId = Long.parseLong(request.getParameter("id").toString());
		Hashtable sessionAttributes = new Hashtable();
		sessionAttributes = (Hashtable) SessionManager.sessionExists(request, "admin");
		ucCandidate_comments = new Uc_candidate_comments();
		ucCandidate_comments.setCc_added_by(Long.parseLong(sessionAttributes.get("uuid").toString()));
		ucCandidate_comments.setCc_candidate_id(candidateId);
		ucCandidate_comments.setCc_comment(comment);
		ucCandidate_comments.setCc_date(new java.sql.Date(Calendar.getInstance().getTime().getTime()));
		ucCandidate_comments.setCc_time(new SimpleDateFormat("HH:mm:ss").format(new Date()));
		response.setContentType("text/html;charset=UTF-8");
		if(model.insertData(ucCandidate_comments)) {
			response.getWriter().write("success");
		} else {
			response.getWriter().write("error");
		}
	}
	
	private void deleteComment(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		int commentId = Integer.parseInt(request.getParameter("commentId").toString());
		response.setContentType("text/html;charset=UTF-8");
		if(model.deleteData("model.Uc_candidate_comments", "cc_id=" + commentId)) {
			response.getWriter().write("success");
		} else {
			response.getWriter().write("error");
		}
	}
	
	private void getStates(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		int criteriaId = Integer.parseInt(request.getParameter("criteriaId").toString());
		ucState_list = new ArrayList<Uc_state>();
		ucState_list = (List<Uc_state>)(Object) model.selectData("model.Uc_state", "1");
		StringBuilder temp = new StringBuilder();
		temp.append("<select class=\"form-control\" id=\"state"+criteriaId+"\" onchange=\"stateChanged(this, "+criteriaId+")\">");
		temp.append("<option selected hidden disabled value=\"select\">Select state</option>");
		for(Uc_state state : ucState_list) {
			temp.append("<option value=\""+ state.getS_id() +"\">"+ state.getS_name() +"</option>");
		}
		temp.append("</select>");
		response.setContentType("text/html;charset=UTF-8");
		response.getWriter().write(temp.toString());
	}
	
	private void getDistricts(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		int criteriaId = Integer.parseInt(request.getParameter("criteriaId").toString());
		int state = Integer.parseInt(request.getParameter("state").toString());
		ucDistrict_list = new ArrayList<Uc_district>();
		ucDistrict_list = (List<Uc_district>)(Object) model.selectData("model.Uc_district", "d_s_id=" + state + " order by d_name");
		StringBuilder temp = new StringBuilder();
		temp.append("<option selected hidden disabled value=\"select\">Select district</option>");
		for(Uc_district district : ucDistrict_list) {
			temp.append("<option value=\""+ district.getD_id() +"\">"+ district.getD_name() +"</option>");
		}
		response.setContentType("text/html;charset=UTF-8");
		response.getWriter().write(temp.toString());
	}
	
	private void getTalukas(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		int criteriaId = Integer.parseInt(request.getParameter("criteriaId").toString());
		int district = Integer.parseInt(request.getParameter("district").toString());
		ucTaluka_list = new ArrayList<Uc_taluka>();
		ucTaluka_list = (List<Uc_taluka>)(Object) model.selectData("model.Uc_taluka", "t_district_id=" + district + " order by t_name");
		StringBuilder temp = new StringBuilder();
		temp.append("<option selected hidden disabled value=\"select\">Select taluka</option>");
		for(Uc_taluka taluka : ucTaluka_list) {
			temp.append("<option value=\""+ taluka.getT_id() +"\">"+ taluka.getT_name() +"</option>");
		}
		response.setContentType("text/html;charset=UTF-8");
		response.getWriter().write(temp.toString());
	}
	
	private void getCities(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		int criteriaId = Integer.parseInt(request.getParameter("criteriaId").toString());
		int taluka = Integer.parseInt(request.getParameter("taluka").toString());
		ucCity_list = new ArrayList<Uc_city>();
		ucCity_list = (List<Uc_city>)(Object) model.selectData("model.Uc_city", "c_taluka_id=" + taluka + " order by c_name");
		StringBuilder temp = new StringBuilder();
		temp.append("<option selected hidden disabled value=\"select\">Select city</option>");
		for(Uc_city city : ucCity_list) {
			temp.append("<option value=\""+ city.getC_id() +"\">"+ city.getC_name() +"</option>");
		}
		response.setContentType("text/html;charset=UTF-8");
		response.getWriter().write(temp.toString());
	}
	
	private void showDetails(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException, JSONException {
		int id = Integer.parseInt(request.getParameter("id").toString());
		Uc_candidate_details ucCandidate = new Uc_candidate_details();
		ucCandidate = (Uc_candidate_details) model.selectData("model.Uc_candidate_details", "cd_id=" + id).get(0);
		StringBuilder temp = new StringBuilder();
		temp.append("<table class=\"table table-bordered table-striped\">");
		temp.append("<tr><td>Name: </td><th><input type=\"text\" name=\"candidate_name\" id=\"candidate_name\" class=\"form-control\" value=\"" + ucCandidate.getCd_name() + "\"></th></tr>");
		if(ucCandidate.getCd_gender() == UCConstants.MALE) {
			temp.append("<tr><td>Gender: </td><th><label><input type=\"radio\" name=\"candidate_gender\" onChange=\"changeGender(this," + ucCandidate.getCd_id() + ", 1)\" id=\"gender_male\" value\"" + UCConstants.MALE + "\" checked> Male</label> <label><input type=\"radio\" name=\"candidate_gender\" id=\"gender_female\" onChange=\"changeGender(this," + ucCandidate.getCd_id() + ", 0)\" value\"" + UCConstants.FEMALE + "\"> Female</label></th></tr>");
		} else {
			temp.append("<tr><td>Gender: </td><th><label><input type=\"radio\" name=\"candidate_gender\" id=\"gender_male\" onChange=\"changeGender(this," + ucCandidate.getCd_id() + ", 1)\" value\"" + UCConstants.MALE + "\"> Male</label> <label><input type=\"radio\" name=\"candidate_gender\" id=\"gender_female\" onChange=\"changeGender(this," + ucCandidate.getCd_id() + ", 0)\" value\"" + UCConstants.FEMALE + "\" checked> Female</label></th></tr>");
		}
		//temp.append("<tr><td>Gender: </td><th> " + (ucCandidate.getCd_gender()==1?"Male":"Female") + "</th></tr>");
		Date date = new Date();
		SimpleDateFormat smf = new SimpleDateFormat("YYYY");
		temp.append("<tr><td>Age: </td><th> " + (Integer.parseInt(smf.format(date))-ucCandidate.getCd_birthyear()) + "  - <input type=\"number\" name=\"candidate_birthyear\" id=\"candidate_birthyear\" class\"form-control\" oninput=\"javascript: if (this.value.length > this.maxLength) this.value = this.value.slice(0, this.maxLength);\" maxlength=\"4\" value=\"" + ucCandidate.getCd_birthyear() + "\"></th></tr>");
		temp.append("<tr><td>Contact no.: </td><th> Primary:" + ucCandidate.getCd_contact_num() + "<br>Secondary: <input type=\"number\" name=\"candidate_secondary_number\" id=\"candidate_secondary_number\" class=\"form-control\" value=\"" + ucCandidate.getCd_contact_num_secondary() + "\" oninput=\"javascript: if (this.value.length > this.maxLength) this.value = this.value.slice(0, this.maxLength);\" maxlength=\"10\"></th></tr>");
		temp.append("<tr><td>Email Id: </td><th> " + ucCandidate.getCd_email() + "</th></tr>");
		Uc_city ucCity = new Uc_city();
		ucCity = (Uc_city) model.selectData("model.Uc_city", "c_id=" + ucCandidate.getCd_city_id()).get(0);
		Uc_taluka ucTaluka = new Uc_taluka();
		ucTaluka = (Uc_taluka) model.selectData("model.Uc_taluka", "t_id=" + ucCity.getC_taluka_id()).get(0);
		Uc_district ucDistrict = new Uc_district();
		ucDistrict = (Uc_district) model.selectData("model.Uc_district", "d_id=" + ucTaluka.getT_district_id()).get(0);
		Uc_state ucState = new Uc_state();
		ucState = (Uc_state) model.selectData("model.Uc_state", "s_id=" + ucDistrict.getD_s_id()).get(0);
		temp.append("<tr><td>Location: </td><th> " + ucCity.getC_name() + ", " + ucTaluka.getT_name() + ", " + ucDistrict.getD_name() + ", " + ucState.getS_name() + "</th></tr>");
		temp.append("<tr><td>Change Location: </td><th><div class=\"col-lg-6\">");
		temp.append("<select class=\"form-control\" name=\"candidate_state\" id=\"candidate_state\">");
		temp.append("</th></tr>");
		temp.append("<tr><td>Joining Date: </td><th>" + ucCandidate.getCd_joining_date() + "</th></tr>");
		temp.append("<tr><td><hr></td><td><hr></td></tr>");
		ucQualification_list = new ArrayList<Uc_candidate_qualification>();
		ucQualification_list = (List<Uc_candidate_qualification>)(Object) model.selectData("model.Uc_candidate_qualification", "cq_candidate_id=" + ucCandidate.getCd_id());
		StringBuilder qual = new StringBuilder();
		String tempPostfix = "";
		for(Uc_candidate_qualification q : ucQualification_list) {
			Uc_qualifications quali = new Uc_qualifications();
			quali = (Uc_qualifications) model.selectData("model.Uc_qualifications", "q_id=" + q.getCq_quali_id()).get(0);
			qual.append(tempPostfix);
			tempPostfix = ", ";
			qual.append(quali.getQ_name());
		}
		temp.append("<tr><td>Qualification: </td><th> " + qual.toString() + "</th></tr>");
		temp.append("<tr><td>Add qualification: </td>");
		temp.append("<th><input class=\"form-control\" id=\"user_qualification\" name=\"user_qualification\">");
		temp.append("<input type=\"hidden\" name=\"qualification_count\" id=\"qualification_count\">");
		temp.append("<div class=\"input-group-btn\"> " + 
				"		<button type=\"button\" onclick=\"add_qualification_tag()\" class=\"btn mybtn\"><i style=\"color:#FFF200\" class=\"fa fa-plus\"></i> &nbsp;Add</button>\n" + 
				"" + 
				"    </div><br><div class=\"form-group col-lg-12\" id=\"qualification-tags\">" + 
				"							</div>");
		temp.append("</th>");
		temp.append("</tr>");
		StringBuilder exp = new StringBuilder();
		tempPostfix = "";
		if(ucCandidate.getCd_experience() == 1) {
			ucExperience_list = new ArrayList<Uc_experience>();
			ucExperience_list = (List<Uc_experience>)(Object) model.selectData("model.Uc_experience", "e_candidate_id=" + ucCandidate.getCd_id());
			StringBuilder tempExp = new StringBuilder();
			tempExp.append("<table class=\"table table-striped table-bordered\"><tr><th>Industry sector</th><th>Position</th><th>Years</th><th>Is current job</th><th>Salary</th></tr>");
			tempPostfix = "";
			for(Uc_experience e : ucExperience_list) {
				/*Uc_industry_sector ucIndustry = new Uc_industry_sector();
				ucIndustry = (Uc_industry_sector) model.selectData("model.Uc_industry_sector", "is_id=" + e.getE_industry_id()).get(0);
				Uc_position ucPosition = new Uc_position();
				ucPosition = (Uc_position) model.selectData("model.Uc_position", "p_id=" + e.getE_position_id()).get(0);
				exp.append(tempPostfix);
				tempPostfix = ", ";
				exp.append(ucIndustry.getIs_name() + " - " + ucPosition.getP_name() + "(" + e.getE_years() + " years)");*/
				
				tempExp.append("<tr>");
				Uc_industry_sector ucIndustry = new Uc_industry_sector();
				ucIndustry = (Uc_industry_sector) model.selectData("model.Uc_industry_sector", "is_id=" + e.getE_industry_id()).get(0);
				Uc_position ucPosition = new Uc_position();
				ucPosition = (Uc_position) model.selectData("model.Uc_position", "p_id=" + e.getE_position_id()).get(0);
				tempExp.append("<td>" + ucIndustry.getIs_name() + "</td>");
				tempExp.append("<td>" + ucPosition.getP_name() + "</td>");
				tempExp.append("<td><input type=\"number\" value=\"" + e.getE_years() + "\" onblur=\"saveExperienceYears(this, " + e.getE_id() + ")\"></td>");
				tempExp.append("<td>" + (e.getE_is_current_job() == 1 ? "Yes" : "No") + "</td>");
				tempExp.append("<td>" + e.getE_salary_per_month() + "</td>");
				tempExp.append("</tr>");
			}
			tempExp.append("</table>");
			exp = tempExp;
		} else {
			exp.append("No experience");
		}
		temp.append("<tr><td>Experience: </td><th> " + exp.toString() + "</th></tr>");
		temp.append("<tr><td>Expected Salary: </td><th>" + ucCandidate.getCd_expected_salary() + "</th></tr>");
		ucInterest_list = new ArrayList<Uc_interest_areas>();
		ucInterest_list = (List<Uc_interest_areas>)(Object) model.selectData("model.Uc_interest_areas", "ia_candidate=" + ucCandidate.getCd_id());
		StringBuilder tempInterest = new StringBuilder();
		tempInterest.append("<table class=\"table table-striped table-bordered\"><tr><th>Industry sector</th><th>Position</th><th>Delete</th></tr>");
		tempPostfix = "";
		for(Uc_interest_areas i : ucInterest_list) {
			/*Uc_industry_sector ucIndustry = new Uc_industry_sector();
			ucIndustry = (Uc_industry_sector) model.selectData("model.Uc_industry_sector", "is_id=" + i.getIa_industry()).get(0);
			Uc_position ucPosition = new Uc_position();
			ucPosition = (Uc_position) model.selectData("model.Uc_position", "p_id=" + i.getIa_position()).get(0);
			interestString.append(tempPostfix);
			tempPostfix = ", ";
			interestString.append(ucIndustry.getIs_name() + " - " + ucPosition.getP_name());*/
			tempInterest.append("<tr id=\"interestRow" + i.getIa_id() + "\">");
			Uc_industry_sector ucIndustry = new Uc_industry_sector();
			ucIndustry = (Uc_industry_sector) model.selectData("model.Uc_industry_sector", "is_id=" + i.getIa_industry()).get(0);
			Uc_position ucPosition = new Uc_position();
			ucPosition = (Uc_position) model.selectData("model.Uc_position", "p_id=" + i.getIa_position()).get(0);
			tempInterest.append("<td><input type=\"text\" onblur=\"saveInterestIndustry(this, "+ i.getIa_id() +")\" value=\"" + ucIndustry.getIs_name() + "\"></td>");
			tempInterest.append("<td><input type=\"text\" onblur=\"saveInterestPosition(this, "+ i.getIa_id() +")\" value=\"" + ucPosition.getP_name() + "\"></td>");
			if(ucInterest_list.size() > 1) {
				tempInterest.append("<td><button class=\"btn btn-danger\" onclick=\"deleteInterest("+ i.getIa_id() +")\">Delete</button></td>");
			} else {
				tempInterest.append("<td></td>");
			}
			tempInterest.append("</tr>");
		}
		tempInterest.append("</table>");
		temp.append("<tr><td>Interest areas: </td><th> " + tempInterest.toString() + "</th></tr>");
		temp.append("<tr><td><hr></td><td><hr></td></tr>");
		if(ucCandidate.getCd_payment() == 1) {
			temp.append("<tr><td>Payment Done: </td><th><input type=\"checkbox\" value=\"yes\" onChange=\"changePayment(this," + ucCandidate.getCd_id() + ")\" checked> &nbsp; <input type=\"text\" placeholder=\"Payment Date\" value=\"" + (ucCandidate.getPayment_date() != null && !ucCandidate.getPayment_date().equals("") ? ucCandidate.getPayment_date() : "") + "\" onblur=\"savePaymentDate(this, " + ucCandidate.getCd_id() + ")\"></th></tr>");
		} else {
			temp.append("<tr><td>Payment Done: </td><th><input type=\"checkbox\" value=\"yes\" onChange=\"changePayment(this," + ucCandidate.getCd_id() + ")\"> &nbsp; <input type=\"text\" placeholder=\"Payment Date\" value=\"" + (ucCandidate.getPayment_date() != null && !ucCandidate.getPayment_date().equals("") ? ucCandidate.getPayment_date() : "") + "\" onblur=\"savePaymentDate(this, " + ucCandidate.getCd_id() + ")\"></th></tr>");
		}
		temp.append("<tr><td><hr></td><td><hr></td></tr>");
		if(ucCandidate.getCd_is_placed() == 1) {
			temp.append("<tr><td>Is placed: </td><th><input type=\"checkbox\" value=\"yes\" onChange=\"changePlaced(this," + ucCandidate.getCd_id() + ")\" checked></th></tr>");
		} else {
			temp.append("<tr><td>Is placed: </td><th><input type=\"checkbox\" value=\"yes\" onChange=\"changePlaced(this," + ucCandidate.getCd_id() + ")\"></th></tr>");
		}
		temp.append("<tr><td><hr></td><td><hr></td></tr>");
		
		/*List<Uc_candidate_comments> ucComments_list = (List<Uc_candidate_comments>)(Object) model.selectData("model.Uc_candidate_comments", "cc_candidate_id=" + ucCandidate.getCd_id());
		temp.append("<tr><td>Comments: </td><th><textarea style=\"width:100%\" maxlimit=\"500\" onblur=\"saveComment(this," + ucCandidate.getCd_id() + ")\">");
					if(!ucComments_list.isEmpty()) {
						temp.append(ucComments_list.get(0).getCc_comment());
					}
		temp.append("</textarea></th></tr>");*/
		temp.append("<tr><th>Comments: </th><td>");
		temp.append("<div id=\"candidate_comments\"></div><br>");
		temp.append("<hr>");
		temp.append("<textarea placeholder=\"Enter comment\" style=\"width:80%\" id=\"candidate_comment_entry\"></textarea><button type=\"button\" class=\"btn btn-primary\" onclick=\"saveComment(this," + ucCandidate.getCd_id() + ")\">Submit</button>");
		temp.append("</td></tr>");
		temp.append("<tr><td><hr></td><td><hr></td></tr>");
		String downloadLink = RouteManager.getBasePath() + "admin/manage-users?action=download&file=" + ucCandidate.getCd_resume();
		temp.append("<tr><td>Resume</td><th><a href=\"" + downloadLink + "\" target=\"_blank\">Download</a></th></tr>");
		temp.append("</table>");
		JSONObject json = new JSONObject();
		json.put("details", temp.toString());
		json.put("id", ucCandidate.getCd_id());
		response.setContentType("application/json");
		response.getWriter().write(json.toString());
	}
	
	private void downloadResume(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		//FileManager.downloadFile(request, response, "resumes", request.getParameter("file"));
		ViewManager.showView(request, response, "download.jsp?fileName=" + request.getParameter("file") + "&fileType=file");
	}
	
	private void getInsights(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException, JSONException {
		
		List<Hashtable> previousYearCandidates = new ArrayList<Hashtable>();
		List<Hashtable> currentYearCandidates = new ArrayList<Hashtable>();
		Date date = new Date();
		JSONObject json = new JSONObject();
		SimpleDateFormat smf_year = new SimpleDateFormat("yyyy");
		SimpleDateFormat smf_month = new SimpleDateFormat("MM");
		int filter = Integer.parseInt(request.getParameter("filter").toString());
		StringBuilder where = new StringBuilder();
		if(filter == 0) {
			where.append("1");
		} else {
			String filter_value = request.getParameter("filter_value").toString();
			if(filter == UCConstants.CITY) {
				if(filter_value.matches("^\\d+$")) {
					where.append("cd_city_id=" + Integer.parseInt(filter_value));
				} else {
					/*StringTokenizer st = new StringTokenizer(filter_value, ",");
					String city = st.nextToken().trim();
					String district = st.nextToken().trim();
					String state = st.nextToken().trim();
					ucCity_list = new ArrayList<Uc_city>();
					ucState_list = new ArrayList<Uc_state>();
					ucDistrict_list = new ArrayList<Uc_district>();
					ucState_list = (List<Uc_state>)(Object) model.selectData("model.Uc_state", "s_name='" + state + "'");
					if(!ucState_list.isEmpty()) {
						ucDistrict_list = (List<Uc_district>)(Object) model.selectData("model.Uc_district", "d_name='" + district + "' and d_s_id=" + ucState_list.get(0).getS_id());
						if(!ucDistrict_list.isEmpty()) {
							ucCity_list = (List<Uc_city>)(Object) model.selectData("model.Uc_city", "c_name='" + city + "' and c_district=" + ucDistrict_list.get(0).getD_id());
							if(!ucCity_list.isEmpty()) {
								where.append("cd_city_id=" + ucCity_list.get(0).getC_id());
							}
						}
					}*/
					
					ucCity_list = new ArrayList<Uc_city>();
					ucCity_list = (List<Uc_city>)(Object) model.selectData("model.Uc_city", "c_name like '%" + filter_value + "%'");
					if(!ucCity_list.isEmpty()) {
						String temp = "";
						where.append("(");
						for(Uc_city c : ucCity_list) {
							where.append(temp);
							temp = " or ";
							where.append("cd_city_id=" + c.getC_name());
						}
						where.append(")");
					}
				}
			} else if(filter == UCConstants.TALUKA) {
				if(filter_value.matches("^\\d+$")) {
					ucCity_list = new ArrayList<Uc_city>();
					ucCity_list = (List<Uc_city>)(Object) model.selectData("model.Uc_city", "c_taluka_id=" + Integer.parseInt(filter_value));
					String postfix = "";
					for(Uc_city city : ucCity_list) {
						where.append(postfix);
						postfix = " or ";
						where.append("cd_city_id=" + city.getC_id());
					}
				} else {
					/*StringTokenizer st = new StringTokenizer(filter_value, ",");
					String district = st.nextToken().trim();
					String state = st.nextToken().trim();
					ucCity_list = new ArrayList<Uc_city>();
					ucState_list = new ArrayList<Uc_state>();
					ucDistrict_list = new ArrayList<Uc_district>();
					ucState_list = (List<Uc_state>)(Object) model.selectData("model.Uc_state", "s_name='" + state + "'");
					if(!ucState_list.isEmpty()) {
						ucDistrict_list = (List<Uc_district>)(Object) model.selectData("model.Uc_district", "d_name='" + district + "' and d_s_id=" + ucState_list.get(0).getS_id());
						if(!ucDistrict_list.isEmpty()) {
							ucCity_list = (List<Uc_city>)(Object) model.selectData("model.Uc_city", "c_district=" + ucDistrict_list.get(0).getD_id());
							if(!ucCity_list.isEmpty()) {
								String postfix = "";
								for(Uc_city city : ucCity_list) {
									where.append(postfix);
									postfix = " or ";
									where.append("cd_city_id=" + city.getC_id());
								}
							}
						}
					}*/
					StringBuilder cityWhere = new StringBuilder();
					ucTaluka_list = new ArrayList<Uc_taluka>();
					ucTaluka_list = (List<Uc_taluka>)(Object) model.selectData("model.Uc_taluka", "t_name like '%" + filter_value + "%'");
					if(!ucTaluka_list.isEmpty()) {
						String temp = "(";
						for(Uc_taluka t : ucTaluka_list) {
							ucCity_list = new ArrayList<Uc_city>();
							ucCity_list = (List<Uc_city>)(Object) model.selectData("model.Uc_city", "c_taluka_id=" + t.getT_id());
							if(!ucCity_list.isEmpty()) {
								for(Uc_city c : ucCity_list) {
									cityWhere.append(temp);
									temp = " or ";
									cityWhere.append("cd_city_id=" + c.getC_id());
								}
							}
						}
					}
					if(!cityWhere.toString().equals("")) cityWhere.append(")");
					where.append(cityWhere);
				}
			} else if(filter == UCConstants.DISTRICT) {
				if(filter_value.matches("^\\d+$")) {
					ucTaluka_list = new ArrayList<Uc_taluka>();
					ucTaluka_list = (List<Uc_taluka>)(Object) model.selectData("model.Uc_taluka", "t_district_id=" + Integer.parseInt(filter_value));
					StringBuilder cityWhere = new StringBuilder();
					if(!ucTaluka_list.isEmpty()) {
						String temp = "(";
						for(Uc_taluka t : ucTaluka_list) {
							ucCity_list = new ArrayList<Uc_city>();
							ucCity_list = (List<Uc_city>)(Object) model.selectData("model.Uc_city", "c_taluka_id=" + t.getT_id());
							if(!ucCity_list.isEmpty()) {
								for(Uc_city c : ucCity_list) {
									cityWhere.append(temp);
									temp = " or ";
									cityWhere.append("cd_city_id=" + c.getC_id());
								}
							}
						}
					}
					if(!cityWhere.toString().equals("")) cityWhere.append(")");
					where.append(cityWhere);
				} else {
					/*StringTokenizer st = new StringTokenizer(filter_value, ",");
					String district = st.nextToken().trim();
					String state = st.nextToken().trim();
					ucCity_list = new ArrayList<Uc_city>();
					ucState_list = new ArrayList<Uc_state>();
					ucDistrict_list = new ArrayList<Uc_district>();
					ucState_list = (List<Uc_state>)(Object) model.selectData("model.Uc_state", "s_name='" + state + "'");
					if(!ucState_list.isEmpty()) {
						ucDistrict_list = (List<Uc_district>)(Object) model.selectData("model.Uc_district", "d_name='" + district + "' and d_s_id=" + ucState_list.get(0).getS_id());
						if(!ucDistrict_list.isEmpty()) {
							ucCity_list = (List<Uc_city>)(Object) model.selectData("model.Uc_city", "c_district=" + ucDistrict_list.get(0).getD_id());
							if(!ucCity_list.isEmpty()) {
								String postfix = "";
								for(Uc_city city : ucCity_list) {
									where.append(postfix);
									postfix = " or ";
									where.append("cd_city_id=" + city.getC_id());
								}
							}
						}
					}*/
					
					ucDistrict_list = new ArrayList<Uc_district>();
					ucDistrict_list = (List<Uc_district>)(Object) model.selectData("model.Uc_district", "d_name like '%" + filter_value + "%'");
					StringBuilder cityWhere = new StringBuilder();
					if(!ucDistrict_list.isEmpty()) {
						for(Uc_district d : ucDistrict_list) {
							ucTaluka_list = new ArrayList<Uc_taluka>();
							ucTaluka_list = (List<Uc_taluka>)(Object) model.selectData("model.Uc_taluka", "t_district_id=" + d.getD_id());
							if(!ucTaluka_list.isEmpty()) {
								String temp = "(";
								for(Uc_taluka t : ucTaluka_list) {
									ucCity_list = new ArrayList<Uc_city>();
									ucCity_list = (List<Uc_city>)(Object) model.selectData("model.Uc_city", "c_taluka_id=" + t.getT_id());
									if(!ucCity_list.isEmpty()) {
										for(Uc_city c : ucCity_list) {
											cityWhere.append(temp);
											temp = " or ";
											cityWhere.append("cd_city_id=" + c.getC_id());
										}
									}
								}
							}
						}
					}
					if(!cityWhere.toString().equals("")) cityWhere.append(")");
					where.append(cityWhere);
				}
			} else if(filter == UCConstants.INDUSTRY_SECTOR) {
				if(filter_value.matches("^\\d+$")) {
					ucIndustry_list = new ArrayList<Uc_industry_sector>();
					ucIndustry_list = (List<Uc_industry_sector>)(Object) model.selectData("model.Uc_industry_sector", "is_id=" + Integer.parseInt(filter_value));
					if(!ucIndustry_list.isEmpty()) {
						ucInterest_list = new ArrayList<Uc_interest_areas>();
						String postfix = "";
						ucInterest_list = (List<Uc_interest_areas>)(Object) model.selectData("model.Uc_interest_areas", "ia_industry=" + ucIndustry_list.get(0).getIs_id());
						for(Uc_interest_areas interest : ucInterest_list) {
							where.append(postfix);
							postfix = " or ";
							where.append("cd_id=" + interest.getIa_candidate());
						}
						ucExperience_list = new ArrayList<Uc_experience>();
						ucExperience_list = (List<Uc_experience>)(Object) model.selectData("model.Uc_experience", "e_industry_id=" + ucIndustry_list.get(0).getIs_id());
						for(Uc_experience exp : ucExperience_list) {
							where.append(postfix);
							postfix = " or ";
							where.append("cd_id=" + exp.getE_candidate_id());
						}
					}
				} else {
					ucIndustry_list = new ArrayList<Uc_industry_sector>();
					ucIndustry_list = (List<Uc_industry_sector>)(Object) model.selectData("model.Uc_industry_sector", "is_name='" + filter_value + "'");
					if(!ucIndustry_list.isEmpty()) {
						ucInterest_list = new ArrayList<Uc_interest_areas>();
						String postfix = "";
						ucInterest_list = (List<Uc_interest_areas>)(Object) model.selectData("model.Uc_interest_areas", "ia_industry=" + ucIndustry_list.get(0).getIs_id());
						for(Uc_interest_areas interest : ucInterest_list) {
							where.append(postfix);
							postfix = " or ";
							where.append("cd_id=" + interest.getIa_candidate());
						}
						ucExperience_list = new ArrayList<Uc_experience>();
						ucExperience_list = (List<Uc_experience>)(Object) model.selectData("model.Uc_experience", "e_industry_id=" + ucIndustry_list.get(0).getIs_id());
						for(Uc_experience exp : ucExperience_list) {
							where.append(postfix);
							postfix = " or ";
							where.append("cd_id=" + exp.getE_candidate_id());
						}
					}
				}
			} else if(filter == UCConstants.POSITION) {
				if(filter_value.matches("^\\d+$")) {
					ucPosition_list = new ArrayList<Uc_position>();
					ucPosition_list = (List<Uc_position>)(Object) model.selectData("model.Uc_position", "p_id=" + Integer.parseInt(filter_value));
					if(!ucPosition_list.isEmpty()) {
						ucInterest_list = new ArrayList<Uc_interest_areas>();
						String postfix = "";
						ucInterest_list = (List<Uc_interest_areas>)(Object) model.selectData("model.Uc_interest_areas", "ia_position=" + ucPosition_list.get(0).getP_id());
						for(Uc_interest_areas interest : ucInterest_list) {
							where.append(postfix);
							postfix = " or ";
							where.append("cd_id=" + interest.getIa_candidate());
						}
						ucExperience_list = new ArrayList<Uc_experience>();
						ucExperience_list = (List<Uc_experience>)(Object) model.selectData("model.Uc_experience", "e_position_id=" + ucPosition_list.get(0).getP_id());
						for(Uc_experience exp : ucExperience_list) {
							where.append(postfix);
							postfix = " or ";
							where.append("cd_id=" + exp.getE_candidate_id());
						}
					}
				} else {
					ucPosition_list = new ArrayList<Uc_position>();
					ucPosition_list = (List<Uc_position>)(Object) model.selectData("model.Uc_position", "p_name='" + filter_value + "'");
					if(!ucPosition_list.isEmpty()) {
						ucInterest_list = new ArrayList<Uc_interest_areas>();
						String postfix = "";
						ucInterest_list = (List<Uc_interest_areas>)(Object) model.selectData("model.Uc_interest_areas", "ia_position=" + ucPosition_list.get(0).getP_id());
						for(Uc_interest_areas interest : ucInterest_list) {
							where.append(postfix);
							postfix = " or ";
							where.append("cd_id=" + interest.getIa_candidate());
						}
						ucExperience_list = new ArrayList<Uc_experience>();
						ucExperience_list = (List<Uc_experience>)(Object) model.selectData("model.Uc_experience", "e_position_id=" + ucPosition_list.get(0).getP_id());
						for(Uc_experience exp : ucExperience_list) {
							where.append(postfix);
							postfix = " or ";
							where.append("cd_id=" + exp.getE_candidate_id());
						}
					}
				}
			} else if(filter == UCConstants.STATE) {
				if(filter_value.matches("^\\d+$")) {
					StringBuilder cityWhere = new StringBuilder();
					String temp = "(";
					ucDistrict_list = new ArrayList<Uc_district>();
					ucDistrict_list = (List<Uc_district>)(Object) model.selectData("model.Uc_district", "d_s_id=" + Integer.parseInt(filter_value));
					if(!ucDistrict_list.isEmpty()) {
						for(Uc_district d : ucDistrict_list) {
							ucTaluka_list = new ArrayList<Uc_taluka>();
							ucTaluka_list = (List<Uc_taluka>)(Object) model.selectData("model.Uc_taluka", "t_district_id=" + d.getD_id());
							if(!ucTaluka_list.isEmpty()) {
								for(Uc_taluka t : ucTaluka_list) {
									ucCity_list = new ArrayList<Uc_city>();
									ucCity_list = (List<Uc_city>)(Object)model.selectData("model.Uc_city", "c_taluka_id=" + t.getT_id());
									if(!ucCity_list.isEmpty()) {
										for(Uc_city c : ucCity_list) {
											cityWhere.append(temp);
											temp = " or ";
											cityWhere.append("cd_city_id=" + c.getC_id());
										}
									}
								}
							}
						}
					}
					if(!cityWhere.toString().equals("")) cityWhere.append(")");
					where.append(cityWhere);
				} else {
					/*ucState_list = new ArrayList<Uc_state>();
					String postfix = "";
					ucState_list = (List<Uc_state>)(Object) model.selectData("model.Uc_state", "s_name='" + filter_value + "'");
					if(!ucState_list.isEmpty()) {
						ucDistrict_list = new ArrayList<Uc_district>();
						ucDistrict_list = (List<Uc_district>)(Object) model.selectData("model.Uc_district", "d_s_id=" + ucState_list.get(0).getS_id());
						for(Uc_district dis : ucDistrict_list) {
							ucCity_list = (List<Uc_city>)(Object) model.selectData("model.Uc_city", "c_district=" + dis.getD_id());
							for(Uc_city city : ucCity_list) {
								where.append(postfix);
								postfix = " or ";
								where.append("cd_city_id=" + city.getC_id());
							}
						}
					}*/
					StringBuilder cityWhere = new StringBuilder();
					String temp = "(";
					ucState_list = new ArrayList<Uc_state>();
					ucState_list = (List<Uc_state>)(Object) model.selectData("model.Uc_state", "s_name like '%" + filter_value + "%'");
					if(!ucState_list.isEmpty()) {
						for(Uc_state s : ucState_list) {
							ucDistrict_list = new ArrayList<Uc_district>();
							ucDistrict_list = (List<Uc_district>)(Object) model.selectData("model.Uc_district", "d_s_id=" + s.getS_id());
							if(!ucDistrict_list.isEmpty()) {
								for(Uc_district d : ucDistrict_list) {
									ucTaluka_list = new ArrayList<Uc_taluka>();
									ucTaluka_list = (List<Uc_taluka>)(Object) model.selectData("model.Uc_taluka", "t_district_id=" + d.getD_id());
									if(!ucTaluka_list.isEmpty()) {
										for(Uc_taluka t : ucTaluka_list) {
											ucCity_list = new ArrayList<Uc_city>();
											ucCity_list = (List<Uc_city>)(Object)model.selectData("model.Uc_city", "c_taluka_id=" + t.getT_id());
											if(!ucCity_list.isEmpty()) {
												for(Uc_city c : ucCity_list) {
													cityWhere.append(temp);
													temp = " or ";
													cityWhere.append("cd_city_id=" + c.getC_id());
												}
											}
										}
									}
								}
							}
						}
					}
					if(!cityWhere.toString().equals("")) cityWhere.append(")");
					where.append(cityWhere);
				}
			}
		}
		if(where.toString().equals("")) {
			json.put("dataAvail", "0");
		} else {
			JSONArray jsonArray = new JSONArray();
			List<Integer> tempList = new ArrayList<Integer>();
			json.put("dataAvail", "1");
			json.put("previousYear", (Integer.parseInt(smf_year.format(date).toString()) - 1));
			json.put("currentYear", (Integer.parseInt(smf_year.format(date).toString())));
			previousYearCandidates = model.selectDataGroupBy("model.Uc_candidate_details", "count(*) cnt, MONTH(cd_joining_date) month", "YEAR(cd_joining_date)=" + (Integer.parseInt(smf_year.format(date).toString()) - 1) + " and (" + where.toString() + ")", "MONTH(cd_joining_date)");
			currentYearCandidates = model.selectDataGroupBy("model.Uc_candidate_details", "count(*) cnt, MONTH(cd_joining_date) month", "YEAR(cd_joining_date)=" + (Integer.parseInt(smf_year.format(date).toString())) + " and (" + where.toString() + ")", "MONTH(cd_joining_date)");
			if(!previousYearCandidates.isEmpty()) {
				int tempCount = 0;
				for(int i = 1 ; i <= 12 ; i++ ) {
					if(tempCount >= previousYearCandidates.size()) {
						jsonArray.put(0);
						continue;
					}
					if(previousYearCandidates.get(tempCount).get("month").equals(String.valueOf(i))) {
						jsonArray.put(Integer.parseInt(previousYearCandidates.get(tempCount++).get("cnt").toString()));
					} else {
						jsonArray.put(0);
					}
				}
				json.put("previousYearCandidates", jsonArray);
			} else {
				int[] noData= {0,0,0,0,0,0,0,0,0,0,0,0};
				json.put("previousYearCandidates", noData);
			}
			jsonArray = new JSONArray();
			if(!currentYearCandidates.isEmpty()) {
				int tempCount = 0;
				for(int i = 1 ; i <= Integer.parseInt(smf_month.format(date).toString()) ; i++ ) {
					if(tempCount >= currentYearCandidates.size()) {
						jsonArray.put(0);
						continue;
					}
					if(currentYearCandidates.get(tempCount).get("month").equals(String.valueOf(i))) {
						jsonArray.put(Integer.parseInt(currentYearCandidates.get(tempCount++).get("cnt").toString()));
					} else {
						jsonArray.put(0);
					}
				}
				json.put("currentYearCandidates", jsonArray);
			} else {
				int[] noData = {};
				json.put("currentYearCandidates", noData);
			}
			
		}
		response.setContentType("application/json");
		response.getWriter().write(json.toString());
	}
	
	private void searchShortlistUser(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException, JSONException {
		int order = Integer.parseInt(request.getParameter("order").toString());
		int pageNo = Integer.parseInt(request.getParameter("pageNo").toString());
		int limit = Integer.parseInt(request.getParameter("limit").toString());
		boolean begin = request.getParameter("begin").toLowerCase().equals("true") ? true : false;
		int start = (pageNo-1)*limit;
		
		String ordering = "cd_id desc";
		if(order == 1) {
			ordering = "cd_id asc";
		} else if(order == 2) {
			ordering = "cd_id desc";
		} else if(order == 3) {
			ordering = "cd_name asc";
		} else if(order == 4) {
			ordering = "cd_name desc";
		} 
		
		
		StringBuilder where = new StringBuilder();
		String postfix = "";
		
		if(request.getParameter("isFiltered").toLowerCase().equals("true")) {
			where.append("(");
			String[] filters = request.getParameterValues("filters[]");
			for(String filter : filters) {
				where.append(postfix);
				postfix = " and ";
				if(filter.equals("1")) {
					where.append("cd_gender=1");
				} else if(filter.equals("2")) {
					where.append("cd_gender=0");
				} else if(filter.equals("3")) {
					where.append("cd_is_placed=0");
				} else if(filter.equals("4")) {
					where.append("cd_is_placed=1");
				} else if(filter.equals("5")) {
					where.append("cd_payment=1");
				} else if(filter.equals("6")) {
					where.append("cd_payment=0");
				}
			}
			where.append(") and ");
		}
		
		postfix = "";
		int count = 0;
		ucPosition_list = new ArrayList<Uc_position>();
		ucInterest_list = new ArrayList<Uc_interest_areas>();
		ucExperience_list = new ArrayList<Uc_experience>();
		ucIndustry_list = new ArrayList<Uc_industry_sector>();
		List<Integer> positionCandidates = new ArrayList<Integer>();
		boolean extraFlag = false;
		int reqId = 0;
		while(true) {
			if(request.getParameterValues("extraParameters[" + count + "][]") == null) break;
			String val1 = request.getParameterValues("extraParameters[" + count + "][]")[0];
			String val2 = request.getParameterValues("extraParameters[" + count + "][]")[1];
			extraFlag = true;
			count++;
			where.append(postfix);
			postfix = " and ";
			if(val1.equals("1")) {
				where.append("cd_id=" + val2);
			} else if(val1.equals("2")) {
				where.append("cd_name like '%" + val2.trim() + "%'");
			} else if(val1.equals("3")) {
				where.append("cd_contact_num='" + val2 + "'");
			} else if(val1.equals("4")) {
				where.append("cd_email='" + val2 + "'");
			} else if(val1.equals("5")) {
				//position
				StringTokenizer st = new StringTokenizer(val2,",");
				StringBuilder temp = new StringBuilder();
				String tempPostfix = "";
				while(st.hasMoreTokens()) {
					temp.append(tempPostfix);
					tempPostfix = " or ";
					temp.append("p_name LIKE '%"+st.nextToken().trim()+"%'");
				}
				ucPosition_list = new ArrayList<Uc_position>();
				ucPosition_list = (List<Uc_position>)(Object) model.selectData("model.Uc_position", temp.toString());
				StringBuilder tempInterest = new StringBuilder();
				StringBuilder tempExperience = new StringBuilder();
				tempPostfix = "";
				
				for(Uc_position pos : ucPosition_list) {
					tempInterest.append(tempPostfix);
					tempExperience.append(tempPostfix);
					tempPostfix = " or ";
					tempInterest.append("ia_position=" + pos.getP_id());
					tempExperience.append("e_position_id=" + pos.getP_id());
				}
				ucInterest_list = new ArrayList<Uc_interest_areas>();
				ucExperience_list = new ArrayList<Uc_experience>();
				ucInterest_list = (List<Uc_interest_areas>)(Object) model.selectData("model.Uc_interest_areas", tempInterest.toString());
				ucExperience_list = (List<Uc_experience>)(Object) model.selectData("model.Uc_experience", tempExperience.toString());
				if(!ucInterest_list.isEmpty() || !ucExperience_list.isEmpty()) where.append("(");
				tempPostfix = "";
				for(Uc_interest_areas interest : ucInterest_list) {
					//positionCandidates.add(interest.getIa_candidate());
					where.append(tempPostfix);
					tempPostfix = " or ";
					where.append("cd_id=" + interest.getIa_candidate());
				}
				for(Uc_experience exp : ucExperience_list) {
					//positionCandidates.add(exp.getE_candidate_id());
					where.append(tempPostfix);
					tempPostfix = " or ";
					where.append("cd_id=" + exp.getE_candidate_id());
				}
				if(!ucInterest_list.isEmpty() || !ucExperience_list.isEmpty()) where.append(")");
			} else if(val1.equals("6")) {
				//industrySector
				StringTokenizer st = new StringTokenizer(val2,",");
				StringBuilder temp = new StringBuilder();
				String tempPostfix = "";
				while(st.hasMoreTokens()) {
					temp.append(tempPostfix);
					tempPostfix = " or ";
					temp.append("is_name LIKE '%"+st.nextToken().trim()+"%'");
				}
				ucIndustry_list = new ArrayList<Uc_industry_sector>();
				ucIndustry_list = (List<Uc_industry_sector>)(Object) model.selectData("model.Uc_industry_sector", temp.toString());
				StringBuilder tempInterest = new StringBuilder();
				StringBuilder tempExperience = new StringBuilder();
				tempPostfix = "";
				for(Uc_industry_sector indus : ucIndustry_list) {
					tempInterest.append(tempPostfix);
					tempExperience.append(tempPostfix);
					tempPostfix = " or ";
					tempInterest.append("ia_industry=" + indus.getIs_id());
					tempExperience.append("e_industry_id=" + indus.getIs_id());
				}
				ucInterest_list = new ArrayList<Uc_interest_areas>();
				ucExperience_list = new ArrayList<Uc_experience>();
				ucInterest_list = (List<Uc_interest_areas>)(Object) model.selectData("model.Uc_interest_areas", tempInterest.toString());
				ucExperience_list = (List<Uc_experience>)(Object) model.selectData("model.Uc_experience", tempExperience.toString());
				if(!ucInterest_list.isEmpty() || !ucExperience_list.isEmpty()) where.append("(");
				tempPostfix = "";
				for(Uc_interest_areas interest : ucInterest_list) {
					//positionCandidates.add(interest.getIa_candidate());
					where.append(tempPostfix);
					tempPostfix = " or ";
					where.append("cd_id=" + interest.getIa_candidate());
				}
				for(Uc_experience exp : ucExperience_list) {
					//positionCandidates.add(exp.getE_candidate_id());
					where.append(tempPostfix);
					tempPostfix = " or ";
					where.append("cd_id=" + exp.getE_candidate_id());
				}
				if(!ucInterest_list.isEmpty() || !ucExperience_list.isEmpty()) where.append(")");
			} else if(val1.equals("7")) {
				//experience
				StringTokenizer st = new StringTokenizer(val2, "-");
				String token1, token2, tempExp;
				token1 = st.nextToken();
				token2 = st.nextToken();
				if(token1.equals(token2)) {
					tempExp = "e_years=" + token1;
				} else {
					if(token2.equals("*")) {
						tempExp = "e_years>=" + token1;
					} else {
						tempExp = "(e_years>=" + token1 + " and e_years<=" + token2 + ")";
					}
				}
				ucExperience_list = new ArrayList<Uc_experience>();
				ucExperience_list = (List<Uc_experience>)(Object) model.selectData("model.Uc_experience", tempExp);
				if(!ucExperience_list.isEmpty()) where.append("(");
				String tempPostfix = "";
				for(Uc_experience exp : ucExperience_list) {
					where.append(tempPostfix);
					tempPostfix = " or ";
					where.append("cd_id=" + exp.getE_candidate_id());
				}
				if(!ucExperience_list.isEmpty()) where.append(")");
				
			} else if(val1.equals("8")) {
				//location
				char c = val2.charAt(0);
				if(c == '*') {
					/*String str = val2.substring(1);
					StringTokenizer st = new StringTokenizer(str, "-");
					String district = st.nextToken().trim();
					String city = st.nextToken().trim();
					ucDistrict_list = new ArrayList<Uc_district>();
					ucDistrict_list = (List<Uc_district>)(Object) model.selectData("model.Uc_district", "d_name='" + district + "'");
					if(!ucDistrict_list.isEmpty()) {
						String tempPostfix = "(";
						for(Uc_district d : ucDistrict_list) {
							ucCity_list = new ArrayList<Uc_city>();
							ucCity_list = (List<Uc_city>)(Object) model.selectData("model.Uc_city", "c_district=" + d.getD_id() + " and c_name='" + city + "'");
							if(!ucCity_list.isEmpty()) {
								for(Uc_city ct : ucCity_list) {
									where.append(tempPostfix);
									tempPostfix = " or ";
									where.append("cd_city_id=" + ct.getC_id());
								}
							}
 						}
						if(!where.toString().equals("")) where.append(")");
					}*/
					
					String str = val2.substring(1);
					StringTokenizer st = new StringTokenizer(str, "-");
					String state = st.nextToken().trim();
					String district = st.nextToken().trim();
					String taluka = st.nextToken().trim();
					String city = st.nextToken().trim();
					
					StringBuilder cityWhere = new StringBuilder();
					String temppostfix = "(";
					
					if(!city.equals("0")) {
						if(!taluka.equals("0")) {
							if(!district.equals("0")) {
								//city and taluka and district and state(other)
								ucDistrict_list = new ArrayList<Uc_district>();
								ucDistrict_list = (List<Uc_district>)(Object) model.selectData("model.Uc_district", "d_name like '%" + district + "%' and d_s_id=" + Integer.parseInt(state));
								if(!ucDistrict_list.isEmpty()) {
									for(Uc_district d : ucDistrict_list) {
										ucTaluka_list = new ArrayList<Uc_taluka>();
										ucTaluka_list = (List<Uc_taluka>)(Object) model.selectData("model.Uc_taluka", "t_name like '%" + taluka + "%' and t_district_id=" + d.getD_id());
										if(!ucTaluka_list.isEmpty()) {
											for(Uc_taluka t : ucTaluka_list) {
												ucCity_list = new ArrayList<Uc_city>();
												ucCity_list = (List<Uc_city>)(Object) model.selectData("model.Uc_city", "c_name like '%" + city + "%' and c_taluka_id=" + t.getT_id());
												if(!ucCity_list.isEmpty()) {
													for(Uc_city cty: ucCity_list) {
														cityWhere.append(temppostfix);
														temppostfix = " or ";
														cityWhere.append("cd_city_id=" + cty.getC_id());
													}
												}
											} 
										}
									}
								}
							} else {
								//city and taluka and state(other)
								ucDistrict_list = new ArrayList<Uc_district>();
								ucDistrict_list = (List<Uc_district>)(Object) model.selectData("model.Uc_district", "d_s_id=" + Integer.parseInt(state));
								if(!ucDistrict_list.isEmpty()) {
									for(Uc_district d : ucDistrict_list) {
										ucTaluka_list = new ArrayList<Uc_taluka>();
										ucTaluka_list = (List<Uc_taluka>)(Object) model.selectData("model.Uc_taluka", "t_name like '%" + taluka + "%' and t_district_id=" + d.getD_id());
										if(!ucTaluka_list.isEmpty()) {
											for(Uc_taluka t : ucTaluka_list) {
												ucCity_list = new ArrayList<Uc_city>();
												ucCity_list = (List<Uc_city>)(Object) model.selectData("model.Uc_city", "c_name like '%" + city + "%' and c_taluka_id=" + t.getT_id());
												if(!ucCity_list.isEmpty()) {
													for(Uc_city cty: ucCity_list) {
														cityWhere.append(temppostfix);
														temppostfix = " or ";
														cityWhere.append("cd_city_id=" + cty.getC_id());
													}
												}
											} 
										}
									}
								}
							}
						} else {
							if(!district.equals("0")) {
								//city and district and state(other)
								ucDistrict_list = new ArrayList<Uc_district>();
								ucDistrict_list = (List<Uc_district>)(Object) model.selectData("model.Uc_district", "d_name like '%" + district + "%' and d_s_id=" + Integer.parseInt(state));
								if(!ucDistrict_list.isEmpty()) {
									for(Uc_district d : ucDistrict_list) {
										ucTaluka_list = new ArrayList<Uc_taluka>();
										ucTaluka_list = (List<Uc_taluka>)(Object) model.selectData("model.Uc_taluka", "t_district_id=" + d.getD_id());
										if(!ucTaluka_list.isEmpty()) {
											for(Uc_taluka t : ucTaluka_list) {
												ucCity_list = new ArrayList<Uc_city>();
												ucCity_list = (List<Uc_city>)(Object) model.selectData("model.Uc_city", "c_name like '%" + city + "%' and c_taluka_id=" + t.getT_id());
												if(!ucCity_list.isEmpty()) {
													for(Uc_city cty: ucCity_list) {
														cityWhere.append(temppostfix);
														temppostfix = " or ";
														cityWhere.append("cd_city_id=" + cty.getC_id());
													}
												}
											} 
										}
									}
								}
							} else {
								//city and state(other)
								ucDistrict_list = new ArrayList<Uc_district>();
								ucDistrict_list = (List<Uc_district>)(Object) model.selectData("model.Uc_district", "d_s_id=" + Integer.parseInt(state));
								if(!ucDistrict_list.isEmpty()) {
									for(Uc_district d : ucDistrict_list) {
										ucTaluka_list = new ArrayList<Uc_taluka>();
										ucTaluka_list = (List<Uc_taluka>)(Object) model.selectData("model.Uc_taluka", "t_district_id=" + d.getD_id());
										if(!ucTaluka_list.isEmpty()) {
											for(Uc_taluka t : ucTaluka_list) {
												ucCity_list = new ArrayList<Uc_city>();
												ucCity_list = (List<Uc_city>)(Object) model.selectData("model.Uc_city", "c_name like '%" + city + "%' and c_taluka_id=" + t.getT_id());
												if(!ucCity_list.isEmpty()) {
													for(Uc_city cty: ucCity_list) {
														cityWhere.append(temppostfix);
														temppostfix = " or ";
														cityWhere.append("cd_city_id=" + cty.getC_id());
													}
												}
											} 
										}
									}
								}
							}
						}
					} else {
						if(!taluka.equals("0")) {
							if(!district.equals("0")) {
								//taluka and district and state(other)
								ucDistrict_list = new ArrayList<Uc_district>();
								ucDistrict_list = (List<Uc_district>)(Object) model.selectData("model.Uc_district", "d_name like '%" + district + "%' and d_s_id=" + Integer.parseInt(state));
								if(!ucDistrict_list.isEmpty()) {
									for(Uc_district d : ucDistrict_list) {
										ucTaluka_list = new ArrayList<Uc_taluka>();
										ucTaluka_list = (List<Uc_taluka>)(Object) model.selectData("model.Uc_taluka", "t_name like '%" + taluka + "%' and t_district_id=" + d.getD_id());
										if(!ucTaluka_list.isEmpty()) {
											for(Uc_taluka t : ucTaluka_list) {
												ucCity_list = new ArrayList<Uc_city>();
												ucCity_list = (List<Uc_city>)(Object) model.selectData("model.Uc_city", "c_taluka_id=" + t.getT_id());
												if(!ucCity_list.isEmpty()) {
													for(Uc_city cty: ucCity_list) {
														cityWhere.append(temppostfix);
														temppostfix = " or ";
														cityWhere.append("cd_city_id=" + cty.getC_id());
													}
												}
											} 
										}
									}
								}
							} else {
								//taluka and state(other)
								ucDistrict_list = new ArrayList<Uc_district>();
								ucDistrict_list = (List<Uc_district>)(Object) model.selectData("model.Uc_district", "d_s_id=" + Integer.parseInt(state));
								if(!ucDistrict_list.isEmpty()) {
									for(Uc_district d : ucDistrict_list) {
										ucTaluka_list = new ArrayList<Uc_taluka>();
										ucTaluka_list = (List<Uc_taluka>)(Object) model.selectData("model.Uc_taluka", "t_name like '%" + taluka + "%' and t_district_id=" + d.getD_id());
										if(!ucTaluka_list.isEmpty()) {
											for(Uc_taluka t : ucTaluka_list) {
												ucCity_list = new ArrayList<Uc_city>();
												ucCity_list = (List<Uc_city>)(Object) model.selectData("model.Uc_city", "c_taluka_id=" + t.getT_id());
												if(!ucCity_list.isEmpty()) {
													for(Uc_city cty: ucCity_list) {
														cityWhere.append(temppostfix);
														temppostfix = " or ";
														cityWhere.append("cd_city_id=" + cty.getC_id());
													}
												}
											} 
										}
									}
								}
							}
						} else {
							if(!district.equals("0")) {
								//district and state(other)
								ucDistrict_list = new ArrayList<Uc_district>();
								ucDistrict_list = (List<Uc_district>)(Object) model.selectData("model.Uc_district", "d_name like '%" + district + "%' and d_s_id=" + Integer.parseInt(state));
								if(!ucDistrict_list.isEmpty()) {
									for(Uc_district d : ucDistrict_list) {
										ucTaluka_list = new ArrayList<Uc_taluka>();
										ucTaluka_list = (List<Uc_taluka>)(Object) model.selectData("model.Uc_taluka", "t_district_id=" + d.getD_id());
										if(!ucTaluka_list.isEmpty()) {
											for(Uc_taluka t : ucTaluka_list) {
												ucCity_list = new ArrayList<Uc_city>();
												ucCity_list = (List<Uc_city>)(Object) model.selectData("model.Uc_city", "c_taluka_id=" + t.getT_id());
												if(!ucCity_list.isEmpty()) {
													for(Uc_city cty: ucCity_list) {
														cityWhere.append(temppostfix);
														temppostfix = " or ";
														cityWhere.append("cd_city_id=" + cty.getC_id());
													}
												}
											} 
										}
									}
								}
							} else {
								//state(other)
								ucDistrict_list = new ArrayList<Uc_district>();
								ucDistrict_list = (List<Uc_district>)(Object) model.selectData("model.Uc_district", "d_s_id=" + Integer.parseInt(state));
								if(!ucDistrict_list.isEmpty()) {
									for(Uc_district d : ucDistrict_list) {
										ucTaluka_list = new ArrayList<Uc_taluka>();
										ucTaluka_list = (List<Uc_taluka>)(Object) model.selectData("model.Uc_taluka", "t_district_id=" + d.getD_id());
										if(!ucTaluka_list.isEmpty()) {
											for(Uc_taluka t : ucTaluka_list) {
												ucCity_list = new ArrayList<Uc_city>();
												ucCity_list = (List<Uc_city>)(Object) model.selectData("model.Uc_city", "c_taluka_id=" + t.getT_id());
												if(!ucCity_list.isEmpty()) {
													for(Uc_city cty: ucCity_list) {
														cityWhere.append(temppostfix);
														temppostfix = " or ";
														cityWhere.append("cd_city_id=" + cty.getC_id());
													}
												}
											} 
										}
									}
								}
							}
						}
					}
					
					if(cityWhere.toString().equals("")) {
						where.append("0");
					} else {
						cityWhere.append(")");
						where.append(cityWhere);
					}
					
				} else {
					//where.append("cd_city_id=" + val2);
					
					StringTokenizer st = new StringTokenizer(val2, "-");
					String state = st.nextToken().trim();
					String district = st.nextToken().trim();
					String taluka = st.nextToken().trim();
					String city = st.nextToken().trim();
					
					StringBuilder cityWhere = new StringBuilder();
					String temppostfix = "(";
					
					if(!city.equals("0")) {
						if(!taluka.equals("0")) {
							if(!district.equals("0")) {
								//city and taluka and district and state(gujarat)
								ucDistrict_list = new ArrayList<Uc_district>();
								ucDistrict_list = (List<Uc_district>)(Object) model.selectData("model.Uc_district", "d_id=" + district + " and d_s_id=" + Integer.parseInt(state));
								if(!ucDistrict_list.isEmpty()) {
									for(Uc_district d : ucDistrict_list) {
										ucTaluka_list = new ArrayList<Uc_taluka>();
										ucTaluka_list = (List<Uc_taluka>)(Object) model.selectData("model.Uc_taluka", "t_id=" + taluka + " and t_district_id=" + d.getD_id());
										if(!ucTaluka_list.isEmpty()) {
											for(Uc_taluka t : ucTaluka_list) {
												ucCity_list = new ArrayList<Uc_city>();
												ucCity_list = (List<Uc_city>)(Object) model.selectData("model.Uc_city", "c_id=" + city + " and c_taluka_id=" + t.getT_id());
												if(!ucCity_list.isEmpty()) {
													for(Uc_city cty: ucCity_list) {
														cityWhere.append(temppostfix);
														temppostfix = " or ";
														cityWhere.append("cd_city_id=" + cty.getC_id());
													}
												}
											} 
										}
									}
								}
							} else {
								//city and taluka and state(gujarat)
								ucDistrict_list = new ArrayList<Uc_district>();
								ucDistrict_list = (List<Uc_district>)(Object) model.selectData("model.Uc_district", " d_s_id=" + Integer.parseInt(state));
								if(!ucDistrict_list.isEmpty()) {
									for(Uc_district d : ucDistrict_list) {
										ucTaluka_list = new ArrayList<Uc_taluka>();
										ucTaluka_list = (List<Uc_taluka>)(Object) model.selectData("model.Uc_taluka", "t_id=" + taluka + " and t_district_id=" + d.getD_id());
										if(!ucTaluka_list.isEmpty()) {
											for(Uc_taluka t : ucTaluka_list) {
												ucCity_list = new ArrayList<Uc_city>();
												ucCity_list = (List<Uc_city>)(Object) model.selectData("model.Uc_city", "c_id=" + city + " and c_taluka_id=" + t.getT_id());
												if(!ucCity_list.isEmpty()) {
													for(Uc_city cty: ucCity_list) {
														cityWhere.append(temppostfix);
														temppostfix = " or ";
														cityWhere.append("cd_city_id=" + cty.getC_id());
													}
												}
											} 
										}
									}
								}
							}
						} else {
							if(!district.equals("0")) {
								//city and district and state(gujarat)
								ucDistrict_list = new ArrayList<Uc_district>();
								ucDistrict_list = (List<Uc_district>)(Object) model.selectData("model.Uc_district", "d_id=" + district + " and d_s_id=" + Integer.parseInt(state));
								if(!ucDistrict_list.isEmpty()) {
									for(Uc_district d : ucDistrict_list) {
										ucTaluka_list = new ArrayList<Uc_taluka>();
										ucTaluka_list = (List<Uc_taluka>)(Object) model.selectData("model.Uc_taluka", "t_district_id=" + d.getD_id());
										if(!ucTaluka_list.isEmpty()) {
											for(Uc_taluka t : ucTaluka_list) {
												ucCity_list = new ArrayList<Uc_city>();
												ucCity_list = (List<Uc_city>)(Object) model.selectData("model.Uc_city", "c_id=" + city + " and c_taluka_id=" + t.getT_id());
												if(!ucCity_list.isEmpty()) {
													for(Uc_city cty: ucCity_list) {
														cityWhere.append(temppostfix);
														temppostfix = " or ";
														cityWhere.append("cd_city_id=" + cty.getC_id());
													}
												}
											} 
										}
									}
								}
								
							} else {
								//city and state(gujarat)
								ucDistrict_list = new ArrayList<Uc_district>();
								ucDistrict_list = (List<Uc_district>)(Object) model.selectData("model.Uc_district", "d_s_id=" + Integer.parseInt(state));
								if(!ucDistrict_list.isEmpty()) {
									for(Uc_district d : ucDistrict_list) {
										ucTaluka_list = new ArrayList<Uc_taluka>();
										ucTaluka_list = (List<Uc_taluka>)(Object) model.selectData("model.Uc_taluka", "t_district_id=" + d.getD_id());
										if(!ucTaluka_list.isEmpty()) {
											for(Uc_taluka t : ucTaluka_list) {
												ucCity_list = new ArrayList<Uc_city>();
												ucCity_list = (List<Uc_city>)(Object) model.selectData("model.Uc_city", "c_id=" + city + " and c_taluka_id=" + t.getT_id());
												if(!ucCity_list.isEmpty()) {
													for(Uc_city cty: ucCity_list) {
														cityWhere.append(temppostfix);
														temppostfix = " or ";
														cityWhere.append("cd_city_id=" + cty.getC_id());
													}
												}
											} 
										}
									}
								}
							}
						}
					} else {
						if(!taluka.equals("0")) {
							if(!district.equals("0")) {
								//taluka and district and state(gujarat)
								ucDistrict_list = new ArrayList<Uc_district>();
								ucDistrict_list = (List<Uc_district>)(Object) model.selectData("model.Uc_district", "d_id=" + district + " and d_s_id=" + Integer.parseInt(state));
								if(!ucDistrict_list.isEmpty()) {
									for(Uc_district d : ucDistrict_list) {
										ucTaluka_list = new ArrayList<Uc_taluka>();
										ucTaluka_list = (List<Uc_taluka>)(Object) model.selectData("model.Uc_taluka", "t_id=" + taluka + " and t_district_id=" + d.getD_id());
										if(!ucTaluka_list.isEmpty()) {
											for(Uc_taluka t : ucTaluka_list) {
												ucCity_list = new ArrayList<Uc_city>();
												ucCity_list = (List<Uc_city>)(Object) model.selectData("model.Uc_city", "c_taluka_id=" + t.getT_id());
												if(!ucCity_list.isEmpty()) {
													for(Uc_city cty: ucCity_list) {
														cityWhere.append(temppostfix);
														temppostfix = " or ";
														cityWhere.append("cd_city_id=" + cty.getC_id());
													}
												}
											} 
										}
									}
								}
							} else {
								//taluka and state(gujarat)
								ucDistrict_list = new ArrayList<Uc_district>();
								ucDistrict_list = (List<Uc_district>)(Object) model.selectData("model.Uc_district", "d_s_id=" + Integer.parseInt(state));
								if(!ucDistrict_list.isEmpty()) {
									for(Uc_district d : ucDistrict_list) {
										ucTaluka_list = new ArrayList<Uc_taluka>();
										ucTaluka_list = (List<Uc_taluka>)(Object) model.selectData("model.Uc_taluka", "t_id=" + taluka + " and t_district_id=" + d.getD_id());
										if(!ucTaluka_list.isEmpty()) {
											for(Uc_taluka t : ucTaluka_list) {
												ucCity_list = new ArrayList<Uc_city>();
												ucCity_list = (List<Uc_city>)(Object) model.selectData("model.Uc_city", "c_taluka_id=" + t.getT_id());
												if(!ucCity_list.isEmpty()) {
													for(Uc_city cty: ucCity_list) {
														cityWhere.append(temppostfix);
														temppostfix = " or ";
														cityWhere.append("cd_city_id=" + cty.getC_id());
													}
												}
											} 
										}
									}
								}
							}
						} else {
							if(!district.equals("0")) {
								//district and state(gujarat)
								ucDistrict_list = new ArrayList<Uc_district>();
								ucDistrict_list = (List<Uc_district>)(Object) model.selectData("model.Uc_district", "d_id=" + district + " and d_s_id=" + Integer.parseInt(state));
								if(!ucDistrict_list.isEmpty()) {
									for(Uc_district d : ucDistrict_list) {
										ucTaluka_list = new ArrayList<Uc_taluka>();
										ucTaluka_list = (List<Uc_taluka>)(Object) model.selectData("model.Uc_taluka", "t_district_id=" + d.getD_id());
										if(!ucTaluka_list.isEmpty()) {
											for(Uc_taluka t : ucTaluka_list) {
												ucCity_list = new ArrayList<Uc_city>();
												ucCity_list = (List<Uc_city>)(Object) model.selectData("model.Uc_city", "c_taluka_id=" + t.getT_id());
												if(!ucCity_list.isEmpty()) {
													for(Uc_city cty: ucCity_list) {
														cityWhere.append(temppostfix);
														temppostfix = " or ";
														cityWhere.append("cd_city_id=" + cty.getC_id());
													}
												}
											} 
										}
									}
								}
							} else {
								//state(gujarat)
								ucDistrict_list = new ArrayList<Uc_district>();
								ucDistrict_list = (List<Uc_district>)(Object) model.selectData("model.Uc_district", "d_s_id=" + Integer.parseInt(state));
								if(!ucDistrict_list.isEmpty()) {
									for(Uc_district d : ucDistrict_list) {
										ucTaluka_list = new ArrayList<Uc_taluka>();
										ucTaluka_list = (List<Uc_taluka>)(Object) model.selectData("model.Uc_taluka", "t_district_id=" + d.getD_id());
										if(!ucTaluka_list.isEmpty()) {
											for(Uc_taluka t : ucTaluka_list) {
												ucCity_list = new ArrayList<Uc_city>();
												ucCity_list = (List<Uc_city>)(Object) model.selectData("model.Uc_city", "c_taluka_id=" + t.getT_id());
												if(!ucCity_list.isEmpty()) {
													for(Uc_city cty: ucCity_list) {
														cityWhere.append(temppostfix);
														temppostfix = " or ";
														cityWhere.append("cd_city_id=" + cty.getC_id());
													}
												}
											} 
										}
									}
								}
							}
						}
					}
					
					if(cityWhere.toString().equals("")) {
						where.append("0");
					} else {
						cityWhere.append(")");
						where.append(cityWhere);
					}
					
				}
			} else if(val1.equals("9")) {
				//qualification
				StringTokenizer st = new StringTokenizer(val2,",");
				StringBuilder temp = new StringBuilder();
				String tempPostfix = "";
				while(st.hasMoreTokens()) {
					temp.append(tempPostfix);
					tempPostfix = " or ";
					temp.append("q_name LIKE '%"+st.nextToken().trim()+"%'");
				}
				ucQuali_list = new ArrayList<Uc_qualifications>();
				ucQuali_list = (List<Uc_qualifications>)(Object) model.selectData("model.Uc_qualifications", temp.toString());
				if(!ucQuali_list.isEmpty()) {
					temp = new StringBuilder();
					tempPostfix = "";
					for(Uc_qualifications q : ucQuali_list) {
						temp.append(tempPostfix);
						tempPostfix = " or ";
						temp.append("cq_quali_id=" + q.getQ_id());
					}
					ucQualification_list = new ArrayList<Uc_candidate_qualification>();
					ucQualification_list = (List<Uc_candidate_qualification>)(Object) model.selectData("model.Uc_candidate_qualification", temp.toString());
					if(!ucQualification_list.isEmpty()) {
						temp = new StringBuilder();
						tempPostfix = "(";
						for(Uc_candidate_qualification cq : ucQualification_list) {
							temp.append(tempPostfix);
							tempPostfix = " or ";
							temp.append("cd_id=" + cq.getCq_candidate_id());
						}
						temp.append(")");
						where.append(temp.toString());
					} else {
						where.append("0");
					}
				} else {
					where.append("0");
				}
			} else if(val1.equals("reqId")) {
				reqId = Integer.parseInt(val2);
				where.append(1);
			}
		}
		
		if(reqId != 0) {
			ucJobApp_list = new ArrayList<Uc_job_applications>();
			ucJobApp_list = (List<Uc_job_applications>)(Object) model.selectData("model.Uc_job_applications", "ja_job=" + reqId);
			if(!ucJobApp_list.isEmpty()) {
				postfix = " and (";
				for(Uc_job_applications ja : ucJobApp_list) {
					where.append(postfix);
					postfix = " and ";
					where.append("cd_id!=" + ja.getJa_candidate());
				}
				where.append(")");
			}
		}
		
		//error here
		//if(where.toString().equals("")) where.append("1");
		
		//edit here
		
		if(extraFlag && where.toString().equals("")) {
			response.setContentType("application/json");
			JSONObject jsonResponse = new JSONObject();
			jsonResponse.put("table", "No users found.");
			jsonResponse.put("pageCount", 0);
			response.getWriter().write(jsonResponse.toString());
		} else {
			ucCandidate_list = new ArrayList<Uc_candidate_details>();
			ucCandidate_list = (List<Uc_candidate_details>)(Object) model.selectData("model.Uc_candidate_details", where.toString() + " AND cd_is_active=1 order by " + ordering + " LIMIT " + start + "," + limit);
			if(ucCandidate_list.isEmpty()) {
				response.setContentType("application/json");
				JSONObject jsonResponse = new JSONObject();
				jsonResponse.put("table", "No users found.");
				jsonResponse.put("pageCount", 0);
				response.getWriter().write(jsonResponse.toString());
			} else {
				StringBuilder temp = new StringBuilder();
				List<Uc_interest_areas> ucInterest_list;
				Uc_candidate_details ucCandidate;
				Uc_industry_sector ucIndustry;
				Uc_position ucPosition;
				List<Uc_candidate_comments> ucCandidateComments_list;
				temp.append("<table class=\"table table-striped table-bordered\" width=\"100%\"><tr><th>Sr. No.</th><th>Candidate Id</th><th>Name</th><th>Contact No.</th><th>Interest Position</th><th>Exp. Salary</th><th>Curr. Salary</th><th>Exp. Years</th><th>Shortlist</th></tr>");
				for(int i = 0 ; i < ucCandidate_list.size() ; i++) {
					ucCandidate = new Uc_candidate_details();
					ucCandidate = ucCandidate_list.get(i);
					temp.append("<tr><td>" + (i+1) + "</td>");
					temp.append("<td>" + ucCandidate.getCd_id() + "</td>");
					temp.append("<td><a style=\"cursor:pointer\" onclick=\"showCandidateDetails(" + ucCandidate.getCd_id() + ")\">" + ucCandidate.getCd_name() + "</a></td>");
					temp.append("<td>" + ucCandidate.getCd_contact_num() + "</td>");
					ucInterest_list = (List<Uc_interest_areas>)(Object) model.selectData("model.Uc_interest_areas", "ia_candidate = " + ucCandidate.getCd_id());
					temp.append("<td>");
					String temppostfix = "";
					for(Uc_interest_areas single : ucInterest_list) {
						//ucIndustry = (Uc_industry_sector) model.selectData("model.Uc_industry_sector", "is_id=" + single.getIa_industry()).get(0);
						ucPosition = (Uc_position) model.selectData("model.Uc_position", "p_id=" + single.getIa_position()).get(0);
						temp.append(temppostfix);
						temppostfix = ", ";
						temp.append(ucPosition.getP_name());
					}
					temp.append("</td>");
					/*ucCandidateComments_list = (List<Uc_candidate_comments>)(Object) model.selectData("model.Uc_candidate_comments", "cc_candidate_id=" + ucCandidate.getCd_id());
					temp.append("<td><textarea style=\"width:100%\" maxlimit=\"500\" onblur=\"saveComment(this," + ucCandidate.getCd_id() + ")\">");
					if(!ucCandidateComments_list.isEmpty()) {
						temp.append(ucCandidateComments_list.get(0).getCc_comment());
					}
					temp.append("</textarea></td>");*/
					
					temp.append("<td>" + ucCandidate.getCd_expected_salary() + "</td>");
					if(ucCandidate.getCd_experience() == 1) {
						ucExperience_list = new ArrayList<Uc_experience>();
						ucExperience_list = (List<Uc_experience>)(Object) model.selectData("model.Uc_experience", "e_candidate_id=" + ucCandidate.getCd_id() + " order by e_id desc");
						if(!ucExperience_list.isEmpty()) {
							int tempSalary = 0;
							int tempYears = 0;
							for(Uc_experience exp : ucExperience_list) {
								if(exp.getE_is_current_job() == 1) {
									tempSalary = exp.getE_salary_per_month();
									tempYears = exp.getE_years();
									break;
								}
							}
							if(tempSalary == 0 && tempYears == 0) {
								for(Uc_experience exp : ucExperience_list) {
									if(exp.getE_salary_per_month() != 0) {
										tempSalary = exp.getE_salary_per_month();
										tempYears = exp.getE_years();
										break;
									}
								}
							}
							temp.append("<td>" + (tempSalary == 0 ? "N/A" : tempSalary) + "</td>");
							temp.append("<td>" + (tempYears == 0 ? "N/A" : tempYears) + "</td>");
						} else {
							temp.append("<td>N/A</td>");
							temp.append("<td>N/A</td>");
						}
					} else {
						temp.append("<td>Fresher</td>");
						temp.append("<td>Fresher</td>");
					}
					
					temp.append("<td><button type=\"button\" onclick=\"addToShortlist(" + ucCandidate.getCd_id() + ")\" class=\"btn btn-warning\">Add to shortlist</button></td>");
					temp.append("</tr>");
				}
				temp.append("</table>");
				response.setContentType("application/json");
				 JSONObject jsonResponse = new JSONObject();
				 jsonResponse.put("table", temp.toString());
				
				if(begin) {
					ucCandidate_list = new ArrayList<Uc_candidate_details>();
					ucCandidate_list = (List<Uc_candidate_details>)(Object) model.selectData("model.Uc_candidate_details", where.toString());
					 int pageCount = (int) Math.ceil(ucCandidate_list.size()/(float)limit);
					if(pageCount == 0) pageCount = 1;
					 jsonResponse.put("pageCount", pageCount);
				}
				
				//response.setHeader("cache-control", "no-cache");
				//response.setCharacterEncoding("UTF-8");
				//response.setContentType("text/html;charset=UTF-8");
				
				//String result = "'{\"table\":\"" + temp.toString() + "\", \"pageCount\":\"" + pageCount + "\"}'";
				//PrintWriter out = response.getWriter();
				//out.println(jsonResponse.toString());
				//out.flush();
				response.getWriter().write(jsonResponse.toString());
			}
		}
	}
	
	private void getPositions(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		int id = Integer.parseInt(request.getParameter("criteriaId").toString());
		ucPosition_list = new ArrayList<Uc_position>();
		ucPosition_list = (List<Uc_position>)(Object) model.selectData("model.Uc_position", "1");
		StringBuilder temp = new StringBuilder();
		temp.append("<select class=\"form-control\" id=\"positionValue" + id + "\">");
		temp.append("<option selected hidden disabled value=\"select\">Select position</option>");
		for(Uc_position pos : ucPosition_list) {
			temp.append("<option value=\"" + pos.getP_id() + "\">" + pos.getP_name() + "</option>");
		}
		temp.append("</select>");
		response.setContentType("text/html;charset=UTF-8");
		response.getWriter().write(temp.toString());
	}
	private void getQualifications(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		int id = Integer.parseInt(request.getParameter("criteriaId").toString());
		ucQuali_list = new ArrayList<Uc_qualifications>();
		ucQuali_list = (List<Uc_qualifications>)(Object) model.selectData("model.Uc_qualifications", "1");
		StringBuilder temp = new StringBuilder();
		temp.append("<select class=\"form-control\" id=\"qualificationValue" + id + "\">");
		temp.append("<option selected hidden disabled value=\"select\">Select qualification</option>");
		for(Uc_qualifications q : ucQuali_list) {
			if(q.getQ_name().toLowerCase().equals("any")) continue;
			temp.append("<option value=\"" + q.getQ_id() + "\">" + q.getQ_name() + "</option>");
		}
		temp.append("</select>");
		response.setContentType("text/html;charset=UTF-8");
		response.getWriter().write(temp.toString());
	}
	
	private void changePlaced(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		int id = Integer.parseInt(request.getParameter("id").toString());
		int placed = Integer.parseInt(request.getParameter("placed").toString());
		ucCandidate = new Uc_candidate_details();
		ucCandidate.setCd_is_placed(placed);
		if(model.updateData(ucCandidate, "cd_id=" + id)) {
			response.setContentType("text/html;charset=UTF-8");
			response.getWriter().write("success");
		} else {
			response.setContentType("text/html;charset=UTF-8");
			response.getWriter().write("error");
		}
	}
	private void changePayment(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		int id = Integer.parseInt(request.getParameter("id").toString());
		int payment = Integer.parseInt(request.getParameter("payment").toString());
		ucCandidate = new Uc_candidate_details();
		ucCandidate.setCd_payment(payment);
		if(model.updateData(ucCandidate, "cd_id=" + id)) {
			response.setContentType("text/html;charset=UTF-8");
			response.getWriter().write("success");
		} else {
			response.setContentType("text/html;charset=UTF-8");
			response.getWriter().write("error");
		}
	}
	private void downloadCandidateDetails(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		
		StringBuilder temp = new StringBuilder();
		if(request.getParameter("candidateStartRange") != null && !request.getParameter("candidateStartRange").toString().equals("")) {
			if(request.getParameter("candidateEndRange") != null && !request.getParameter("candidateEndRange").toString().equals("")) {
				String postfix = "";
				for(int i=Integer.parseInt(request.getParameter("candidateStartRange").toString().trim()) ; i<=Integer.parseInt(request.getParameter("candidateEndRange").toString().trim()) ; i++) {
					temp.append(postfix);
					postfix = " or ";
					temp.append("cd_id=" + i);
				}
			} else {
				StringTokenizer st = new StringTokenizer(request.getParameter("candidateStartRange").toString() ,",");
				String postfix = "";
				while(st.hasMoreTokens()) {
					int id = Integer.parseInt(st.nextToken().toString().trim());
					temp.append(postfix);
					postfix = " or ";
					temp.append("cd_id=" + id);
				}
			}
		}
		
		if(!temp.toString().equals("")) {
			
			ucCandidate_list = new ArrayList<Uc_candidate_details>();
	    	ucCandidate_list = (List<Uc_candidate_details>)(Object) model.selectData("model.Uc_candidate_details", temp.toString());
	    	
	    	if(!ucCandidate_list.isEmpty()) {
	    		response.setContentType("application/vnd.ms-excel");
				response.setHeader("Content-Disposition", "attachment; filename=candidateDetails.xls");
				XSSFWorkbook workbook = new XSSFWorkbook();
				XSSFSheet spreadsheet = workbook
					      .createSheet("CANDIDATES");
					      XSSFRow row=spreadsheet.createRow(0);
					      XSSFCell cell;
					      cell=row.createCell(0);
					      cell.setCellValue("CANDIDATE ID");
					      cell=row.createCell(1);
					      cell.setCellValue("NAME");
					      cell=row.createCell(2);
					      cell.setCellValue("CONTACT NUMBER");
					      cell=row.createCell(3);
					      cell.setCellValue("SECONDARY CONTACT NUMBER");
					      cell=row.createCell(4);
					      cell.setCellValue("EMAIL ID");
					      cell=row.createCell(5);
					      cell.setCellValue("GENDER");
					      cell=row.createCell(6);
					      cell.setCellValue("BIRTHYEAR");
					      cell=row.createCell(7);
					      cell.setCellValue("CITY, TALUKA, DISTRICT, STATE");
					      cell=row.createCell(8);
					      cell.setCellValue("IS PLACED");
					      cell=row.createCell(9);
					      cell.setCellValue("JOINING DATE");
					      cell=row.createCell(10);
					      cell.setCellValue("EXPECTED SALARY");
					      cell=row.createCell(11);
					      cell.setCellValue("QUALIFICATION");
					      cell=row.createCell(12);
					      cell.setCellValue("EXPERIENCE");
					      cell=row.createCell(13);
					      cell.setCellValue("INTEREST AREAS");
					      
					      int i = 1;
					      for(Uc_candidate_details cd : ucCandidate_list) {
					    	  row=spreadsheet.createRow(i);
					          cell=row.createCell(0);
					          cell.setCellValue(cd.getCd_id());
					          cell=row.createCell(1);
					          cell.setCellValue(cd.getCd_name());
					          cell=row.createCell(2);
					          cell.setCellValue(cd.getCd_contact_num());
					          cell=row.createCell(3);
					          cell.setCellValue(cd.getCd_contact_num_secondary());
					          cell=row.createCell(4);
					          cell.setCellValue(cd.getCd_email());
					          cell=row.createCell(5);
					          cell.setCellValue((cd.getCd_gender() == UCConstants.MALE ? "Male" : "Female"));
					          cell=row.createCell(6);
					          cell.setCellValue(cd.getCd_birthyear());
					          ucCity = new Uc_city();
					          ucCity = (Uc_city) model.selectData("model.Uc_city", "c_id=" + cd.getCd_city_id()).get(0);
					          ucTaluka = new Uc_taluka();
					          ucTaluka = (Uc_taluka) model.selectData("model.Uc_taluka", "t_id=" + ucCity.getC_taluka_id()).get(0);
					          ucDistrict = new Uc_district();
					          ucDistrict = (Uc_district) model.selectData("model.Uc_district", "d_id=" + ucTaluka.getT_district_id()).get(0);
					          ucState = new Uc_state();
					          ucState = (Uc_state) model.selectData("model.Uc_state", "s_id=" + ucDistrict.getD_s_id()).get(0);
					          cell=row.createCell(7);
					          cell.setCellValue(ucCity.getC_name() + ", " + ucTaluka.getT_name() + ", " + ucDistrict.getD_name() + ", " + ucState.getS_name());
					          cell=row.createCell(8);
					          cell.setCellValue((cd.getCd_is_placed() == 1 ? "Yes" : "No"));
					          cell=row.createCell(9);
					          cell.setCellValue(cd.getCd_joining_date().toString());
					          cell=row.createCell(10);
					          cell.setCellValue(cd.getCd_expected_salary());
					          ucQualification_list = new ArrayList<Uc_candidate_qualification>();
					          ucQualification_list = (List<Uc_candidate_qualification>)(Object) model.selectData("model.Uc_candidate_qualification", "cq_candidate_id=" + cd.getCd_id());
					          StringBuilder qualificationTemp = new StringBuilder();
					          String postfix = "";
					          for(Uc_candidate_qualification cq : ucQualification_list) {
					        	  ucQuali_list = new ArrayList<Uc_qualifications>();
					        	  ucQuali_list = (List<Uc_qualifications>)(Object) model.selectData("model.Uc_qualifications", "q_id=" + cq.getCq_quali_id());
					        	  if(!ucQuali_list.isEmpty()) {
					        		  ucQualification = new Uc_qualifications();
						        	  ucQualification = ucQuali_list.get(0);
					        		  qualificationTemp.append(postfix);
						        	  postfix = ", ";
						        	  qualificationTemp.append(ucQualification.getQ_name());
					        	  }
					          }
					          cell=row.createCell(11);
					          cell.setCellValue(qualificationTemp.toString());
					          ucExperience_list = new ArrayList<Uc_experience>();
					          ucExperience_list = (List<Uc_experience>)(Object) model.selectData("model.Uc_experience", "e_candidate_id=" + cd.getCd_id());
					          StringBuilder experienceTemp = new StringBuilder();
					          if(!ucExperience_list.isEmpty()) {
					        	  postfix = "";
					        	  for(Uc_experience e : ucExperience_list) {
					        		  ucIndustry_list = new ArrayList<Uc_industry_sector>();
					        		  ucIndustry_list = (List<Uc_industry_sector>)(Object) model.selectData("model.Uc_industry_sector", "is_id=" + e.getE_industry_id());
					        		  ucPosition_list = new ArrayList<Uc_position>();
					        		  ucPosition_list = (List<Uc_position>)(Object) model.selectData("model.Uc_position", "p_id=" + e.getE_position_id());
					        		  experienceTemp.append(postfix);
					        		  postfix = ", ";
					        		  experienceTemp.append("IS:" + (ucIndustry_list.isEmpty() ? "N/A" : ucIndustry_list.get(0).getIs_name()) + " * P:" + (ucPosition_list.isEmpty() ? "N/A" : ucPosition_list.get(0).getP_name()) + " * Y:" + e.getE_years() + " * S:" + e.getE_salary_per_month());
					        	  }
					          } else {
					        	  experienceTemp.append("N/A");
					          }
					          cell=row.createCell(12);
					          cell.setCellValue(experienceTemp.toString());
					          ucInterest_list = new ArrayList<Uc_interest_areas>();
					          ucInterest_list = (List<Uc_interest_areas>)(Object) model.selectData("model.Uc_interest_areas", "ia_candidate=" + cd.getCd_id());
					          StringBuilder interestTemp = new StringBuilder();
					          if(!ucInterest_list.isEmpty()) {
					        	  postfix = "";
					        	  for(Uc_interest_areas ia : ucInterest_list) {
					        		  ucIndustry_list = new ArrayList<Uc_industry_sector>();
					        		  ucIndustry_list = (List<Uc_industry_sector>)(Object) model.selectData("model.Uc_industry_sector", "is_id=" + ia.getIa_industry());
					        		  ucPosition_list = new ArrayList<Uc_position>();
					        		  ucPosition_list = (List<Uc_position>)(Object) model.selectData("model.Uc_position", "p_id=" + ia.getIa_position());
					        		  interestTemp.append(postfix);
					        		  postfix = ", ";
					        		  interestTemp.append("IS:" + (ucIndustry_list.isEmpty() ? "N/A" : ucIndustry_list.get(0).getIs_name()) + " * P:" + (ucPosition_list.isEmpty() ? "N/A" : ucPosition_list.get(0).getP_name()));
					        	  }
 					          } else {
					        	  interestTemp.append("N/A");
					          }
					          cell=row.createCell(13);
					          cell.setCellValue(interestTemp.toString());
					          i++;
					      }
				
				workbook.write(response.getOutputStream()); // Write workbook to response.
				workbook.close();
	    	} else {
	    		response.setContentType("text/html;charset=UTF-8");
				response.getWriter().write("<b>Error, Go back to <a href=\"" + RouteManager.getBasePath() + "/admin\">Home</a>");
	    	}
		} else {
			response.setContentType("text/html;charset=UTF-8");
			response.getWriter().write("<b>Error, Go back to <a href=\"" + RouteManager.getBasePath() + "/admin\">Home</a>");
		}
	}
	
	private void removeUser(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		try {
			fileManager = new FileManager();
		} catch(Exception ex) {
			
		}
		int candidateId = Integer.parseInt(request.getParameter("candidateId").toString());
		ucCandidate = new Uc_candidate_details();
		ucCandidate = (Uc_candidate_details) model.selectData("model.Uc_candidate_details", "cd_id=" + candidateId).get(0);
		response.setContentType("text/html;charset=UTF-8");
		//if(FileManager.deleteFile(request, "resumes", ucCandidate.getCd_resume())) {
			fileManager.deleteFTPFile(UCConstants.UC_FILES + ucCandidate.getCd_resume());
			fileManager.disconnect();
			if(model.deleteData("model.Uc_candidate_details", "cd_id=" + candidateId)) {
				response.getWriter().write("success");
			} else {
				response.getWriter().write("error");
			}
		/*} else {
			response.getWriter().write("error");
		}*/
	}
	
	private void deactivateUser(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		int candidateId = Integer.parseInt(request.getParameter("candidateId").toString());
		ucCandidate = new Uc_candidate_details();
		ucCandidate.setCd_is_active(0);
		response.setContentType("text/html;charset=UTF-8");
		if(model.updateData(ucCandidate, "cd_id=" + candidateId)) {
			response.getWriter().write("success");
		} else {
			response.getWriter().write("error");
		}
	}
	
	private void activateUser(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		int candidateId = Integer.parseInt(request.getParameter("candidateId").toString());
		ucCandidate = new Uc_candidate_details();
		ucCandidate.setCd_is_active(1);
		response.setContentType("text/html;charset=UTF-8");
		if(model.updateData(ucCandidate, "cd_id=" + candidateId)) {
			response.getWriter().write("success");
		} else {
			response.getWriter().write("error");
		}
	}
	
	private void saveInterestIndustry(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		boolean flag = true;
		String value = request.getParameter("value").toString();
		long id = Long.parseLong(request.getParameter("id").toString());
		long industryId = 0;
		if(!value.equals("")) {
			ucIndustry_list = new ArrayList<Uc_industry_sector>();
			ucIndustry_list = (List<Uc_industry_sector>)(Object) model.selectData("model.Uc_industry_sector", "is_name='"+ value +"' LIMIT 1");
			if(ucIndustry_list.isEmpty()) {
				ucIndustry = new Uc_industry_sector();
				ucIndustry.setIs_name(value);
				List<Long> industrykeys = model.insertDataReturnGeneratedKeys(ucIndustry);
				if(industrykeys.isEmpty()) {
					flag = false;
				} else {
					industryId = industrykeys.get(0);
				}
			} else {
				industryId = ucIndustry_list.get(0).getIs_id();
			}
			if(flag) {
				ucInterestAreas = new Uc_interest_areas();
				ucInterestAreas.setIa_industry(industryId);
				if(!model.updateData(ucInterestAreas, "ia_id=" + id)) {
					flag = false;
				}
			}
			response.setContentType("text/html;charset=UTF-8");
			if(flag) {
				response.getWriter().write("success");
			} else {
				//error
				response.getWriter().write("error");
			}
		}
	}
	
	private void saveInterestPosition(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		boolean flag = true;
		String value = request.getParameter("value").toString();
		long id = Long.parseLong(request.getParameter("id").toString());
		long positionId = 0;
		if(!value.equals("")) {
			ucPosition_list = new ArrayList<Uc_position>();
			ucPosition_list = (List<Uc_position>)(Object) model.selectData("model.Uc_position", "p_name='"+ value +"' LIMIT 1");
			if(ucPosition_list.isEmpty()) {
				ucPosition = new Uc_position();
				ucPosition.setP_name(value);
				List<Long> positionkeys = model.insertDataReturnGeneratedKeys(ucPosition);
				if(positionkeys.isEmpty()) {
					flag = false;
				} else {
					positionId = positionkeys.get(0);
				}
			} else {
				positionId = ucPosition_list.get(0).getP_id();
			}
			if(flag) {
				ucInterestAreas = new Uc_interest_areas();
				ucInterestAreas.setIa_position(positionId);
				if(!model.updateData(ucInterestAreas, "ia_id=" + id)) {
					flag = false;
				}
			}
			response.setContentType("text/html;charset=UTF-8");
			if(flag) {
				response.getWriter().write("success");
			} else {
				//error
				response.getWriter().write("error");
			}
		}
	}

	private void deleteInterest(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		long id = Long.parseLong(request.getParameter("id").toString());
		response.setContentType("text/html;charset=UTF-8");
		if(model.deleteData("model.Uc_interest_areas", "ia_id=" + id)) {
			response.getWriter().write("success");
		} else {
			//error
			response.getWriter().write("error");
		}
	}
	
	private void saveModifiedGender(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		long id = Long.parseLong(request.getParameter("id").toString());
		int gender = Integer.parseInt(request.getParameter("gender").toString());
		ucCandidate = new Uc_candidate_details();
		ucCandidate.setCd_gender(gender);
		response.setContentType("text/html;charset=UTF-8");
		if(model.updateData(ucCandidate, "cd_id=" + id)) {
			response.getWriter().write("success");
		} else {
			//error
			response.getWriter().write("error");
		}
	}
	
	private void savePaymentDate(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		long id = Long.parseLong(request.getParameter("id").toString());
		String date = request.getParameter("date").toString();
		ucCandidate = new Uc_candidate_details();
		ucCandidate.setPayment_date(date);
		response.setContentType("text/html;charset=UTF-8");
		if(model.updateData(ucCandidate, "cd_id=" + id)) {
			response.getWriter().write("success");
		} else {
			//error
			response.getWriter().write("error");
		}
	}
}
