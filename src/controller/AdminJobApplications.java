package controller;

import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.Instant;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.Hashtable;
import java.util.List;
import java.util.StringTokenizer;

import logs.LogManager;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import database.Model;
import model.Uc_application_comments;
import model.Uc_candidate_comments;
import model.Uc_candidate_details;
import model.Uc_candidate_qualification;
import model.Uc_city;
import model.Uc_cj_quantity;
import model.Uc_current_jobs;
import model.Uc_district;
import model.Uc_employer_details;
import model.Uc_experience;
import model.Uc_industry_sector;
import model.Uc_interest_areas;
import model.Uc_interviews;
import model.Uc_job_applications;
import model.Uc_job_qualifications;
import model.Uc_position;
import model.Uc_qualifications;
import model.Uc_state;
import model.Uc_taluka;
import utils.SessionManager;
import utils.ViewManager;

@WebServlet("/admin/manage-job-applications")
public class AdminJobApplications extends HttpServlet {
	private static final long serialVersionUID = 1L;
       
	private Uc_job_applications ucJobApps;
	private Model model;
	private List<Uc_job_applications> ucJobApp_list;
	private Uc_job_applications ucJobApp;
	private Uc_candidate_details ucCandidate;
	private Uc_current_jobs ucCurrJob;
	private List<Uc_current_jobs> ucCurrJobs_list;
	private Uc_position ucPosition;
	private Uc_employer_details ucEmployer;
	private Uc_city ucCity;
	private Uc_district ucDistrict;
	private Uc_state ucState;
	private Uc_cj_quantity ucCjQuantity;
	private List<Uc_interest_areas> ucInterest_list;
	private Uc_industry_sector ucIndustry;
	private List<Uc_experience> ucExperience_list;
	private List<Uc_district> ucDistrict_list;
	private List<Uc_state> ucState_list;
	private List<Uc_city> ucCity_list;
	private List<Uc_industry_sector> ucIndustry_list;
	private List<Uc_position> ucPosition_list;
	private List<Uc_candidate_details> ucCandidate_list;
	private Uc_experience ucExperience;
	private List<Uc_application_comments> ucApplicationComments_list;
	private Uc_application_comments ucApplicationComment;
	private List<Uc_candidate_comments> ucCandidateComments_list;
	private Uc_candidate_qualification ucCandiQuali;
	private List<Uc_candidate_qualification> ucCandiQuali_list;
	private Uc_qualifications ucQualifications;
	private List<Uc_qualifications> ucQuali_list;
	private Uc_interviews ucInterviews;
	private List<Uc_interviews> ucInterviews_list;
	private List<Uc_job_qualifications> ucJobQualifications_list;
	private List<Uc_cj_quantity> ucCjQuantity_list;
	private Uc_taluka ucTaluka;
    public AdminJobApplications() {
        super();
        model = new Model();
    }

	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		Hashtable sessionAttributes = new Hashtable();
		sessionAttributes = (Hashtable) SessionManager.sessionExists(request, "admin");
		if(request.getParameter("action") != null) {
			if(request.getParameter("action").toLowerCase().equals("getapplications")) {
				if(Integer.parseInt(sessionAttributes.get("utype").toString()) == UCConstants.MAIN_ADMIN || Integer.parseInt(sessionAttributes.get("utype").toString()) == UCConstants.SUB_MAIN_ADMIN || Integer.parseInt(sessionAttributes.get("utype").toString()) == UCConstants.TELECALLER_ADMIN) {
					try {
						getJobApps(request, response);
					} catch(Exception ex) {
						LogManager.appendToExceptionLogs(ex);
					}
				} else {
					ViewManager.showView(request, response, "admin/accessdenied.jsp");
				}
			} /*else if(request.getParameter("action").toLowerCase().equals("acceptapplication")) {
				acceptApplication(request,response);
			} else if(request.getParameter("action").toLowerCase().equals("rejectapplication")) {
				rejectApplication(request,response);
			} */else if(request.getParameter("action").toLowerCase().equals("showdetails")) {
				if(Integer.parseInt(sessionAttributes.get("utype").toString()) == UCConstants.MAIN_ADMIN || Integer.parseInt(sessionAttributes.get("utype").toString()) == UCConstants.SUB_MAIN_ADMIN || Integer.parseInt(sessionAttributes.get("utype").toString()) == UCConstants.TELECALLER_ADMIN || Integer.parseInt(sessionAttributes.get("utype").toString()) == UCConstants.SALES_ADMIN) {
					try {
						showDetails(request,response);
					} catch(Exception ex) {
						LogManager.appendToExceptionLogs(ex);
					}
				} else {
					ViewManager.showView(request, response, "admin/accessdenied.jsp");
				}
			} else if(request.getParameter("action").toLowerCase().equals("getapplicationlist")) {
				if(Integer.parseInt(sessionAttributes.get("utype").toString()) == UCConstants.MAIN_ADMIN || Integer.parseInt(sessionAttributes.get("utype").toString()) == UCConstants.SUB_MAIN_ADMIN || Integer.parseInt(sessionAttributes.get("utype").toString()) == UCConstants.TELECALLER_ADMIN) {
					try {
						getApplicationList(request,response);
					} catch(Exception ex) {
						LogManager.appendToExceptionLogs(ex);
					}
				} else {
					ViewManager.showView(request, response, "admin/accessdenied.jsp");
				}
			} else if(request.getParameter("action").toLowerCase().equals("getcandidatedetails")) {
				if(Integer.parseInt(sessionAttributes.get("utype").toString()) == UCConstants.MAIN_ADMIN || Integer.parseInt(sessionAttributes.get("utype").toString()) == UCConstants.SUB_MAIN_ADMIN || Integer.parseInt(sessionAttributes.get("utype").toString()) == UCConstants.TELECALLER_ADMIN) {
					try {
						getCandidateDetails(request,response);
					} catch(Exception ex) {
						LogManager.appendToExceptionLogs(ex);
					}
				} else {
					ViewManager.showView(request, response, "admin/accessdenied.jsp");
				}
			} else if(request.getParameter("action").toLowerCase().equals("acceptapplication")) {
				if(Integer.parseInt(sessionAttributes.get("utype").toString()) == UCConstants.MAIN_ADMIN || Integer.parseInt(sessionAttributes.get("utype").toString()) == UCConstants.SUB_MAIN_ADMIN || Integer.parseInt(sessionAttributes.get("utype").toString()) == UCConstants.TELECALLER_ADMIN) {
					try {
						acceptApplication(request,response);
					} catch(Exception ex) {
						LogManager.appendToExceptionLogs(ex);
					}
				} else {
					ViewManager.showView(request, response, "admin/accessdenied.jsp");
				}
			} else if(request.getParameter("action").toLowerCase().equals("rejectapplication")) {
				if(Integer.parseInt(sessionAttributes.get("utype").toString()) == UCConstants.MAIN_ADMIN || Integer.parseInt(sessionAttributes.get("utype").toString()) == UCConstants.SUB_MAIN_ADMIN || Integer.parseInt(sessionAttributes.get("utype").toString()) == UCConstants.TELECALLER_ADMIN) {
					try {
						rejectApplication(request,response);
					} catch(Exception ex) {
						LogManager.appendToExceptionLogs(ex);
					}
				} else {
					ViewManager.showView(request, response, "admin/accessdenied.jsp");
				}
			} else if(request.getParameter("action").toLowerCase().equals("savecommentchanges")) {
				if(Integer.parseInt(sessionAttributes.get("utype").toString()) == UCConstants.MAIN_ADMIN || Integer.parseInt(sessionAttributes.get("utype").toString()) == UCConstants.SUB_MAIN_ADMIN || Integer.parseInt(sessionAttributes.get("utype").toString()) == UCConstants.TELECALLER_ADMIN) {
					try {
						saveCommentChanges(request,response);
					} catch(Exception ex) {
						LogManager.appendToExceptionLogs(ex);
					}
				} else {
					ViewManager.showView(request, response, "admin/accessdenied.jsp");
				}
			} else if(request.getParameter("action").toLowerCase().equals("savecomment")) {
				if(Integer.parseInt(sessionAttributes.get("utype").toString()) == UCConstants.MAIN_ADMIN || Integer.parseInt(sessionAttributes.get("utype").toString()) == UCConstants.SUB_MAIN_ADMIN || Integer.parseInt(sessionAttributes.get("utype").toString()) == UCConstants.TELECALLER_ADMIN) {
					try {
						saveComment(request,response);
					} catch(Exception ex) {
						LogManager.appendToExceptionLogs(ex);
					}
				} else {
					ViewManager.showView(request, response, "admin/accessdenied.jsp");
				}
			} else if(request.getParameter("action").toLowerCase().equals("getapplicationcomments")) {
				if(Integer.parseInt(sessionAttributes.get("utype").toString()) == UCConstants.MAIN_ADMIN || Integer.parseInt(sessionAttributes.get("utype").toString()) == UCConstants.SUB_MAIN_ADMIN || Integer.parseInt(sessionAttributes.get("utype").toString()) == UCConstants.TELECALLER_ADMIN) {
					try {
						getApplicationComments(request,response);
					} catch(Exception ex) {
						LogManager.appendToExceptionLogs(ex);
					}
				} else {
					ViewManager.showView(request, response, "admin/accessdenied.jsp");
				}
			} else if(request.getParameter("action").toLowerCase().equals("deletecomment")) {
				if(Integer.parseInt(sessionAttributes.get("utype").toString()) == UCConstants.MAIN_ADMIN || Integer.parseInt(sessionAttributes.get("utype").toString()) == UCConstants.SUB_MAIN_ADMIN || Integer.parseInt(sessionAttributes.get("utype").toString()) == UCConstants.TELECALLER_ADMIN) {
					try {
						deleteComment(request,response);
					} catch(Exception ex) {
						LogManager.appendToExceptionLogs(ex);
					}
				} else {
					ViewManager.showView(request, response, "admin/accessdenied.jsp");
				}
			} else if(request.getParameter("action").toLowerCase().equals("acceptjob")) {
				if(Integer.parseInt(sessionAttributes.get("utype").toString()) == UCConstants.MAIN_ADMIN || Integer.parseInt(sessionAttributes.get("utype").toString()) == UCConstants.SUB_MAIN_ADMIN || Integer.parseInt(sessionAttributes.get("utype").toString()) == UCConstants.TELECALLER_ADMIN) {
					try {
						acceptJob(request,response);
					} catch(Exception ex) {
						LogManager.appendToExceptionLogs(ex);
					}
				} else {
					ViewManager.showView(request, response, "admin/accessdenied.jsp");
				}
			} else if(request.getParameter("action").toLowerCase().equals("rejectjob")) {
				if(Integer.parseInt(sessionAttributes.get("utype").toString()) == UCConstants.MAIN_ADMIN || Integer.parseInt(sessionAttributes.get("utype").toString()) == UCConstants.SUB_MAIN_ADMIN || Integer.parseInt(sessionAttributes.get("utype").toString()) == UCConstants.TELECALLER_ADMIN) {
					try {
						rejectJob(request,response);
					} catch(Exception ex) {
						LogManager.appendToExceptionLogs(ex);
					}
				} else {
					ViewManager.showView(request, response, "admin/accessdenied.jsp");
				}
			} else if(request.getParameter("action").toLowerCase().equals("gettelecallerapprovedjobs")) {
				if(Integer.parseInt(sessionAttributes.get("utype").toString()) == UCConstants.MAIN_ADMIN || Integer.parseInt(sessionAttributes.get("utype").toString()) == UCConstants.SUB_MAIN_ADMIN || Integer.parseInt(sessionAttributes.get("utype").toString()) == UCConstants.SALES_ADMIN) {
					try {
						getTelecallerApprovedJobs(request,response);
					} catch(Exception ex) {
						LogManager.appendToExceptionLogs(ex);
					}
				} else {
					ViewManager.showView(request, response, "admin/accessdenied.jsp");
				}
			} else if(request.getParameter("action").toLowerCase().equals("telecallerapprovedapplications")) {
				if(Integer.parseInt(sessionAttributes.get("utype").toString()) == UCConstants.MAIN_ADMIN || Integer.parseInt(sessionAttributes.get("utype").toString()) == UCConstants.SUB_MAIN_ADMIN || Integer.parseInt(sessionAttributes.get("utype").toString()) == UCConstants.SALES_ADMIN) {
					try {
						ViewManager.showView(request, response, "admin/telecallerapprovedapplications.jsp");
					} catch(Exception ex) {
						LogManager.appendToExceptionLogs(ex);
					}
				} else {
					ViewManager.showView(request, response, "admin/accessdenied.jsp");
				}
			} else if(request.getParameter("action").toLowerCase().equals("getacceptedapplications")) {
				if(Integer.parseInt(sessionAttributes.get("utype").toString()) == UCConstants.MAIN_ADMIN || Integer.parseInt(sessionAttributes.get("utype").toString()) == UCConstants.SUB_MAIN_ADMIN || Integer.parseInt(sessionAttributes.get("utype").toString()) == UCConstants.SALES_ADMIN) {
					try {
						getAcceptedApplications(request,response);
					} catch(Exception ex) {
						LogManager.appendToExceptionLogs(ex);
					}
				} else {
					ViewManager.showView(request, response, "admin/accessdenied.jsp");
				}
			} else if(request.getParameter("action").toLowerCase().equals("acceptjobfinal")) {
				if(Integer.parseInt(sessionAttributes.get("utype").toString()) == UCConstants.MAIN_ADMIN || Integer.parseInt(sessionAttributes.get("utype").toString()) == UCConstants.SUB_MAIN_ADMIN || Integer.parseInt(sessionAttributes.get("utype").toString()) == UCConstants.SALES_ADMIN) {
					try {
						acceptJobFinal(request,response);
					} catch(Exception ex) {
						LogManager.appendToExceptionLogs(ex);
					}
				} else {
					ViewManager.showView(request, response, "admin/accessdenied.jsp");
				}
			} else if(request.getParameter("action").toLowerCase().equals("rejectjobfinal")) {
				if(Integer.parseInt(sessionAttributes.get("utype").toString()) == UCConstants.MAIN_ADMIN || Integer.parseInt(sessionAttributes.get("utype").toString()) == UCConstants.SUB_MAIN_ADMIN || Integer.parseInt(sessionAttributes.get("utype").toString()) == UCConstants.SALES_ADMIN) {
					try {
						rejectJobFinal(request,response);
					} catch(Exception ex) {
						LogManager.appendToExceptionLogs(ex);
					}
				} else {
					ViewManager.showView(request, response, "admin/accessdenied.jsp");
				}
			}  else if(request.getParameter("action").toLowerCase().equals("saveinterviewschedule")) {
				if(Integer.parseInt(sessionAttributes.get("utype").toString()) == UCConstants.MAIN_ADMIN || Integer.parseInt(sessionAttributes.get("utype").toString()) == UCConstants.SUB_MAIN_ADMIN || Integer.parseInt(sessionAttributes.get("utype").toString()) == UCConstants.SALES_ADMIN) {
					try {
						saveInterviewSchedule(request,response);
					} catch(Exception ex) {
						LogManager.appendToExceptionLogs(ex);
					}
				} else {
					ViewManager.showView(request, response, "admin/accessdenied.jsp");
				}
			} else if(request.getParameter("action").toLowerCase().equals("salesapprovedapplications")) {
				if(Integer.parseInt(sessionAttributes.get("utype").toString()) == UCConstants.MAIN_ADMIN || Integer.parseInt(sessionAttributes.get("utype").toString()) == UCConstants.SUB_MAIN_ADMIN || Integer.parseInt(sessionAttributes.get("utype").toString()) == UCConstants.TELECALLER_ADMIN) {
					try {
						ViewManager.showView(request, response, "admin/salesapprovedjobs.jsp");
					} catch(Exception ex) {
						LogManager.appendToExceptionLogs(ex);
					}
				} else {
					ViewManager.showView(request, response, "admin/accessdenied.jsp");
				}
			} else if(request.getParameter("action").toLowerCase().equals("getsalesapprovedjobs")) {
				if(Integer.parseInt(sessionAttributes.get("utype").toString()) == UCConstants.MAIN_ADMIN || Integer.parseInt(sessionAttributes.get("utype").toString()) == UCConstants.SUB_MAIN_ADMIN || Integer.parseInt(sessionAttributes.get("utype").toString()) == UCConstants.TELECALLER_ADMIN) {
					try {
						getSalesApprovedJobs(request,response);
					} catch(Exception ex) {
						LogManager.appendToExceptionLogs(ex);
					}
				} else {
					ViewManager.showView(request, response, "admin/accessdenied.jsp");
				}
			} else if(request.getParameter("action").toLowerCase().equals("showfinaldetails")) {
				if(Integer.parseInt(sessionAttributes.get("utype").toString()) == UCConstants.MAIN_ADMIN || Integer.parseInt(sessionAttributes.get("utype").toString()) == UCConstants.SUB_MAIN_ADMIN || Integer.parseInt(sessionAttributes.get("utype").toString()) == UCConstants.TELECALLER_ADMIN) {
					try {
						showFinalDetails(request,response);
					} catch(Exception ex) {
						LogManager.appendToExceptionLogs(ex);
					}
				} else {
					ViewManager.showView(request, response, "admin/accessdenied.jsp");
				}
			} else if(request.getParameter("action").toLowerCase().equals("getfinalapplicationlist")) {
				if(Integer.parseInt(sessionAttributes.get("utype").toString()) == UCConstants.MAIN_ADMIN || Integer.parseInt(sessionAttributes.get("utype").toString()) == UCConstants.SUB_MAIN_ADMIN || Integer.parseInt(sessionAttributes.get("utype").toString()) == UCConstants.TELECALLER_ADMIN) {
					try {
						getFinalApplicationList(request,response);
					} catch(Exception ex) {
						LogManager.appendToExceptionLogs(ex);
					}
				} else {
					ViewManager.showView(request, response, "admin/accessdenied.jsp");
				}
			} else if(request.getParameter("action").toLowerCase().equals("acceptapplicationfinal")) {
				if(Integer.parseInt(sessionAttributes.get("utype").toString()) == UCConstants.MAIN_ADMIN || Integer.parseInt(sessionAttributes.get("utype").toString()) == UCConstants.SUB_MAIN_ADMIN || Integer.parseInt(sessionAttributes.get("utype").toString()) == UCConstants.TELECALLER_ADMIN) {
					try {
						acceptApplicationFinal(request,response);
					} catch(Exception ex) {
						LogManager.appendToExceptionLogs(ex);
					}
				} else {
					ViewManager.showView(request, response, "admin/accessdenied.jsp");
				}
			} else if(request.getParameter("action").toLowerCase().equals("rejectapplicationfinal")) {
				if(Integer.parseInt(sessionAttributes.get("utype").toString()) == UCConstants.MAIN_ADMIN || Integer.parseInt(sessionAttributes.get("utype").toString()) == UCConstants.SUB_MAIN_ADMIN || Integer.parseInt(sessionAttributes.get("utype").toString()) == UCConstants.TELECALLER_ADMIN) {
					try {
						rejectApplicationFinal(request,response);
					} catch(Exception ex) {
						LogManager.appendToExceptionLogs(ex);
					}
				} else {
					ViewManager.showView(request, response, "admin/accessdenied.jsp");
				}
			} else if(request.getParameter("action").toLowerCase().equals("acceptjobtelecallerfinal")) {
				if(Integer.parseInt(sessionAttributes.get("utype").toString()) == UCConstants.MAIN_ADMIN || Integer.parseInt(sessionAttributes.get("utype").toString()) == UCConstants.SUB_MAIN_ADMIN || Integer.parseInt(sessionAttributes.get("utype").toString()) == UCConstants.TELECALLER_ADMIN) {
					try {
						acceptJobTelecallerFinal(request,response);
					} catch(Exception ex) {
						LogManager.appendToExceptionLogs(ex);
					}
				} else {
					ViewManager.showView(request, response, "admin/accessdenied.jsp");
				}
			} else if(request.getParameter("action").toLowerCase().equals("rejectjobtelecallerfinal")) {
				if(Integer.parseInt(sessionAttributes.get("utype").toString()) == UCConstants.MAIN_ADMIN || Integer.parseInt(sessionAttributes.get("utype").toString()) == UCConstants.SUB_MAIN_ADMIN || Integer.parseInt(sessionAttributes.get("utype").toString()) == UCConstants.TELECALLER_ADMIN) {
					try {
						rejectJobTelecallerFinal(request,response);
					} catch(Exception ex) {
						LogManager.appendToExceptionLogs(ex);
					}
				} else {
					ViewManager.showView(request, response, "admin/accessdenied.jsp");
				}
			} else if(request.getParameter("action").toLowerCase().equals("getpositions")) {
				try {
					getPositions(request,response);
				} catch(Exception ex) {
					LogManager.appendToExceptionLogs(ex);
				}
			} else if(request.getParameter("action").toLowerCase().equals("showprefinaldetails")) {
				if(Integer.parseInt(sessionAttributes.get("utype").toString()) == UCConstants.MAIN_ADMIN || Integer.parseInt(sessionAttributes.get("utype").toString()) == UCConstants.SUB_MAIN_ADMIN || Integer.parseInt(sessionAttributes.get("utype").toString()) == UCConstants.SALES_ADMIN) {
					try {
						showPreFinalDetails(request,response);
					} catch(Exception ex) {
						LogManager.appendToExceptionLogs(ex);
					}
				} else {
					ViewManager.showView(request, response, "admin/accessdenied.jsp");
				}
			} else if(request.getParameter("action").toLowerCase().equals("shortlistcandidates")) {
				if(Integer.parseInt(sessionAttributes.get("utype").toString()) == UCConstants.MAIN_ADMIN || Integer.parseInt(sessionAttributes.get("utype").toString()) == UCConstants.SUB_MAIN_ADMIN || Integer.parseInt(sessionAttributes.get("utype").toString()) == UCConstants.TELECALLER_ADMIN) {
					try {
						ViewManager.showView(request, response, "admin/shortlistcandidates.jsp");
					} catch(Exception ex) {
						LogManager.appendToExceptionLogs(ex);
					}
				} else {
					ViewManager.showView(request, response, "admin/accessdenied.jsp");
				}
			} else if(request.getParameter("action").toLowerCase().equals("searchrequirements")) {
				if(Integer.parseInt(sessionAttributes.get("utype").toString()) == UCConstants.MAIN_ADMIN || Integer.parseInt(sessionAttributes.get("utype").toString()) == UCConstants.SUB_MAIN_ADMIN || Integer.parseInt(sessionAttributes.get("utype").toString()) == UCConstants.TELECALLER_ADMIN) {
					try {
						searchRequirements(request,response);
					} catch(Exception ex) {
						LogManager.appendToExceptionLogs(ex);
					}
				} else {
					ViewManager.showView(request, response, "admin/accessdenied.jsp");
				}
			} else if(request.getParameter("action").toLowerCase().equals("saveshortlist")) {
				if(Integer.parseInt(sessionAttributes.get("utype").toString()) == UCConstants.MAIN_ADMIN || Integer.parseInt(sessionAttributes.get("utype").toString()) == UCConstants.SUB_MAIN_ADMIN || Integer.parseInt(sessionAttributes.get("utype").toString()) == UCConstants.TELECALLER_ADMIN) {
					try {
						saveShortlist(request,response);
					} catch(Exception ex) {
						LogManager.appendToExceptionLogs(ex);
					}
				} else {
					ViewManager.showView(request, response, "admin/accessdenied.jsp");
				}
			} else if(request.getParameter("action").toLowerCase().equals("getapplicationlistforshortlist")) {
				if(Integer.parseInt(sessionAttributes.get("utype").toString()) == UCConstants.MAIN_ADMIN || Integer.parseInt(sessionAttributes.get("utype").toString()) == UCConstants.SUB_MAIN_ADMIN) {
					try {
						getApplicationListForShortlist(request,response);
					} catch(Exception ex) {
						LogManager.appendToExceptionLogs(ex);
					}
				} else {
					ViewManager.showView(request, response, "admin/accessdenied.jsp");
				}
			} else if(request.getParameter("action").toLowerCase().equals("candidateappliedjobs")) {
				if(Integer.parseInt(sessionAttributes.get("utype").toString()) == UCConstants.MAIN_ADMIN || Integer.parseInt(sessionAttributes.get("utype").toString()) == UCConstants.SUB_MAIN_ADMIN) {
					try {
						ViewManager.showView(request, response, "admin/candidateappliedjobs.jsp");
					} catch(Exception ex) {
						LogManager.appendToExceptionLogs(ex);
					}
				} else {
					ViewManager.showView(request, response, "admin/accessdenied.jsp");
				}
			}  else if(request.getParameter("action").toLowerCase().equals("searchcandidateappliedjobs")) {
				if(Integer.parseInt(sessionAttributes.get("utype").toString()) == UCConstants.MAIN_ADMIN || Integer.parseInt(sessionAttributes.get("utype").toString()) == UCConstants.SUB_MAIN_ADMIN) {
					try {
						searchCandidateAppliedJobs(request,response);
					} catch(Exception ex) {
						LogManager.appendToExceptionLogs(ex);
					}
				} else {
					ViewManager.showView(request, response, "admin/accessdenied.jsp");
				}
			}  else if(request.getParameter("action").toLowerCase().equals("saveexperienceyears")) {
				if(Integer.parseInt(sessionAttributes.get("utype").toString()) == UCConstants.MAIN_ADMIN || Integer.parseInt(sessionAttributes.get("utype").toString()) == UCConstants.SUB_MAIN_ADMIN || Integer.parseInt(sessionAttributes.get("utype").toString()) == UCConstants.TELECALLER_ADMIN) {
					try {
						saveExperienceYears(request,response);
					} catch(Exception ex) {
						LogManager.appendToExceptionLogs(ex);
					}
				} else {
					ViewManager.showView(request, response, "admin/accessdenied.jsp");
				}
			} else if(request.getParameter("action").toLowerCase().equals("savesalary")) {
				if(Integer.parseInt(sessionAttributes.get("utype").toString()) == UCConstants.MAIN_ADMIN || Integer.parseInt(sessionAttributes.get("utype").toString()) == UCConstants.SUB_MAIN_ADMIN || Integer.parseInt(sessionAttributes.get("utype").toString()) == UCConstants.TELECALLER_ADMIN) {
					try {
						saveSalary(request,response);
					} catch(Exception ex) {
						LogManager.appendToExceptionLogs(ex);
					}
				} else {
					ViewManager.showView(request, response, "admin/accessdenied.jsp");
				}
			} else if(request.getParameter("action").toLowerCase().equals("saveexpectedsalary")) {
				if(Integer.parseInt(sessionAttributes.get("utype").toString()) == UCConstants.MAIN_ADMIN || Integer.parseInt(sessionAttributes.get("utype").toString()) == UCConstants.SUB_MAIN_ADMIN || Integer.parseInt(sessionAttributes.get("utype").toString()) == UCConstants.TELECALLER_ADMIN) {
					try {
						saveExpectedSalary(request,response);
					} catch(Exception ex) {
						LogManager.appendToExceptionLogs(ex);
					}
				} else {
					ViewManager.showView(request, response, "admin/accessdenied.jsp");
				}
			} else if(request.getParameter("action").toLowerCase().equals("deleteapplications")) {
				if(Integer.parseInt(sessionAttributes.get("utype").toString()) == UCConstants.MAIN_ADMIN || Integer.parseInt(sessionAttributes.get("utype").toString()) == UCConstants.SUB_MAIN_ADMIN || Integer.parseInt(sessionAttributes.get("utype").toString()) == UCConstants.TELECALLER_ADMIN) {
					try {
						deleteApplications(request, response);
					} catch(Exception ex) {
						LogManager.appendToExceptionLogs(ex);
					}
				} else {
					ViewManager.showView(request, response, "admin/accessdenied.jsp");
				}
			} else if(request.getParameter("action").toLowerCase().equals("savecandidatequalification")) {
				if(Integer.parseInt(sessionAttributes.get("utype").toString()) == UCConstants.MAIN_ADMIN || Integer.parseInt(sessionAttributes.get("utype").toString()) == UCConstants.SUB_MAIN_ADMIN || Integer.parseInt(sessionAttributes.get("utype").toString()) == UCConstants.TELECALLER_ADMIN) {
					try {
						saveCandidateQualification(request,response);
					} catch(Exception ex) {
						LogManager.appendToExceptionLogs(ex);
					}
				} else {
					ViewManager.showView(request, response, "admin/accessdenied.jsp");
				}
			} 
		} else {
			if(Integer.parseInt(sessionAttributes.get("utype").toString()) == UCConstants.MAIN_ADMIN || Integer.parseInt(sessionAttributes.get("utype").toString()) == UCConstants.SUB_MAIN_ADMIN || Integer.parseInt(sessionAttributes.get("utype").toString()) == UCConstants.TELECALLER_ADMIN || Integer.parseInt(sessionAttributes.get("utype").toString()) == UCConstants.SALES_ADMIN) {
				ViewManager.showView(request, response, "admin/jobapplications.jsp");
			} else {
				ViewManager.showView(request, response, "admin/accessdenied.jsp");
			}
		}
	}

	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		doGet(request, response);
	}
	
	/*private void acceptApplication(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		int id = Integer.parseInt(request.getParameter("id").toString());
		ucJobApps = new Uc_job_applications();
		ucJobApps.setJa_accepted(1);
		response.setContentType("text/html;charset=UTF-8");
		if(model.updateData(ucJobApps, "ja_id=" + id)) {
			response.getWriter().write("success");
		} else {
			response.getWriter().write("error");
		}
	}
	
	private void rejectApplication(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		int id = Integer.parseInt(request.getParameter("id").toString());
		ucJobApps = new Uc_job_applications();
		ucJobApps.setJa_accepted(0);
		response.setContentType("text/html;charset=UTF-8");
		if(model.updateData(ucJobApps, "ja_id=" + id)) {
			response.getWriter().write("success");
		} else {
			response.getWriter().write("error");
		}
	}*/
	
	private void getJobApps(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException, JSONException {
		int pageNo = Integer.parseInt(request.getParameter("pageNo").toString());
		int limit = Integer.parseInt(request.getParameter("limit").toString());
		boolean begin = request.getParameter("begin").toLowerCase().equals("true") ? true : false;
		int start = (pageNo-1)*limit;
		boolean hasExtra = false;
		int count = 0;
		
		StringBuilder where = new StringBuilder();
		if(request.getParameter("isFiltered").toLowerCase().equals("true")) {
			String temppostfix = "(";
			String[] filters = request.getParameterValues("filters[]");
			for(String filter : filters) {
				where.append(temppostfix);
				temppostfix = " and ";
				if(filter.equals("1")) {
					where.append("cj_approved_by_telecaller=2");
				} else if(filter.equals("2")) {
					where.append("cj_approved_by_telecaller=1");
				} else if(filter.equals("3")) {
					where.append("cj_approved_by_telecaller=0");
				} 
			}
			where.append(")");
		} else {
			where.append("1");
		}
		
		if(request.getParameter("extraParameters") != "" && request.getParameter("extraParameters") == null) {
				String postfix = " and ";
				while(true) {
				
					if(request.getParameterValues("extraParameters[" + count + "][]") == null) break;
					hasExtra = true;
					String val1 = request.getParameterValues("extraParameters[" + count + "][]")[0];
					String val2 = request.getParameterValues("extraParameters[" + count + "][]")[1];
					count++;
				
					where.append(postfix);
					postfix = " and ";
					
					if(val1.equals("1")) {
						where.append("cj_id=" + Integer.parseInt(val2));
					} else if(val1.equals("2")) {
						where.append("cj_position=" + Integer.parseInt(val2));
					} 
				}
		}
		
		where.append(" and cj_has_applicant=1 and cj_is_active=1");
		
		ucCurrJobs_list = new ArrayList<Uc_current_jobs>();
		ucCurrJobs_list = (List<Uc_current_jobs>)(Object) model.selectData("model.Uc_current_jobs", where.toString() + " order by cj_id desc LIMIT " + start + "," + limit);
		if(ucCurrJobs_list.isEmpty()) {
			response.setContentType("application/json");
			JSONObject jsonResponse = new JSONObject();
			jsonResponse.put("table", "No job applications are submitted yet.");
			jsonResponse.put("pageCount", 0);
			response.getWriter().write(jsonResponse.toString());
		} else {
			StringBuilder temp = new StringBuilder();
			
			temp.append("<table class=\"table table-bordered\" width=\"100%\"><tr><th>Sr. No.</th><th>Job Id</th><th>Employer Name (Id)</th><th>Position</th><th>Number of applicants</th><th>Number of new applicants</th><th>Details</th><th>Accept</th><th>Reject</th></tr>");
			for(int i = 0 ; i < ucCurrJobs_list.size() ; i++) {
				ucCurrJob = new Uc_current_jobs();
				ucCurrJob = ucCurrJobs_list.get(i);
				String bgColor = "";
				if(ucCurrJob.getCj_approved_by_telecaller() == 2) bgColor = UCConstants.PENDING_APPLICATION;
				else if(ucCurrJob.getCj_approved_by_telecaller() == 1) bgColor = UCConstants.ACCEPTED_APPLICATION;
				else bgColor = UCConstants.REJECTED_APPLICATION;
				temp.append("<tr id=\"requirement_row" + ucCurrJob.getCj_id() + "\" style=\"background:" + bgColor + ";color:#fff\">");
				temp.append("<td>" + (i+1) + "</td>");
				temp.append("<td>" + ucCurrJob.getCj_id() + "</td>");
				
				ucEmployer = new Uc_employer_details();
				ucEmployer = (Uc_employer_details) model.selectData("model.Uc_employer_details", "ed_id=" + ucCurrJob.getCj_employer_id()).get(0);
				temp.append("<td>" + ucEmployer.getEd_firm_name() + " ("  + ucEmployer.getEd_id() + ")</td>");
				
				ucPosition = new Uc_position();
				ucPosition = (Uc_position) model.selectData("model.Uc_position", "p_id=" + ucCurrJob.getCj_position()).get(0);
				temp.append("<td>" + ucPosition.getP_name() + "</td>");
				
				ucJobApp_list = new ArrayList<Uc_job_applications>();
				ucJobApp_list = (List<Uc_job_applications>)(Object) model.selectData("model.Uc_job_applications", "ja_job=" + ucCurrJob.getCj_id());
				temp.append("<td>" + ucJobApp_list.size() + "</td>");
				
				temp.append("<td>" + ucCurrJob.getCj_number_of_new_applicant() + "</td>");
				temp.append("<td><button type=\"button\" onclick=\"showDetails(this," + ucCurrJob.getCj_id() + ")\" class=\"btn btn-info\">Details</button></td>");
				temp.append("<td><button type=\"button\" onclick=\"acceptJob(this," + ucCurrJob.getCj_id() + ")\" class=\"btn btn-success\">Accept</button></td>");
				temp.append("<td><button type=\"button\" data-id=\"" + ucCurrJob.getCj_id() + "\" data-toggle=\"modal\" data-target=\"#rejectJobModal\" id=\"rejectJobButton\" class=\"btn btn-danger\">Reject</button></td>");
				temp.append("</tr>");
			}
			temp.append("</table>");
			response.setContentType("application/json");
			 JSONObject jsonResponse = new JSONObject();
			 jsonResponse.put("table", temp.toString());
			 if(begin) {
				 ucCurrJobs_list = new ArrayList<Uc_current_jobs>();
				 ucCurrJobs_list = (List<Uc_current_jobs>)(Object) model.selectData("model.Uc_current_jobs", where.toString());
				 int pageCount = (int) Math.ceil(ucCurrJobs_list.size()/(float)limit);
				 if(pageCount == 0) pageCount = 1;
				 jsonResponse.put("pageCount", pageCount);
			 }
				 response.getWriter().write(jsonResponse.toString());
		}
		
		/*ucJobApp_list = new ArrayList<Uc_job_applications>();
		ucJobApp_list = (List<Uc_job_applications>)(Object) model.selectData("model.Uc_job_applications", where.toString() + " order by ja_id desc LIMIT " + start + "," + limit);
		if(ucJobApp_list.isEmpty()) {
			response.setContentType("application/json");
			JSONObject jsonResponse = new JSONObject();
			jsonResponse.put("table", "No job applications are submitted yet.");
			jsonResponse.put("pageCount", 0);
			response.getWriter().write(jsonResponse.toString());
		} else {
			StringBuilder temp = new StringBuilder();
			temp.append("<table class=\"table table-bordered\" width=\"100%\"><tr><th>Sr. No.</th><th>Application Id</th><th>Job Id</th><th>Candidate Id</th><th>Candidate Name</th><th>Position</th><th>Applied at</th><th>Details</th></tr>");
			for(int i = 0 ; i < ucJobApp_list.size() ; i++) {
				ucJobApp = new Uc_job_applications();
				ucJobApp = ucJobApp_list.get(i);
				
				temp.append("<tr id=\"appRow" + ucJobApp.getJa_id() + "\">");
				temp.append("<td>" + (i+1) + "</td>");
				temp.append("<td>" + ucJobApp.getJa_id() + "</td>");
				temp.append("<td>" + ucJobApp.getJa_job() + "</td>");
				temp.append("<td>" + ucJobApp.getJa_candidate() + "</td>");
				ucCandidate = new Uc_candidate_details();
				ucCandidate = (Uc_candidate_details) model.selectData("model.Uc_candidate_details", "cd_id=" + ucJobApp.getJa_candidate()).get(0);
				temp.append("<td>" + ucCandidate.getCd_name() + " (" + (ucCandidate.getCd_gender()==1?"Male":"Female") + ")</td>");
				ucCurrJob = new Uc_current_jobs();
				ucCurrJob = (Uc_current_jobs) model.selectData("model.Uc_current_jobs", "cj_id=" + ucJobApp.getJa_job()).get(0);
				ucPosition = new Uc_position();
				ucPosition = (Uc_position) model.selectData("model.Uc_position", "p_id=" + ucCurrJob.getCj_position()).get(0);
				temp.append("<td>" + ucPosition.getP_name() + "</td>");
				temp.append("<td>" + ucJobApp.getJa_date() + " - " + ucJobApp.getJa_time() + "</td>");
				temp.append("<td><button type=\"button\" onclick=\"showDetails(this," + ucJobApp.getJa_id() + ")\" class=\"btn btn-info\">Details</button></td>");
				
				temp.append("</tr>");
			}
			temp.append("</table>");
			response.setContentType("application/json");
			 JSONObject jsonResponse = new JSONObject();
			 jsonResponse.put("table", temp.toString());
			
		//	if(begin) {
		//		ucJobApp_list = new ArrayList<Uc_job_applications>();
		//		ucJobApp_list = (List<Uc_job_applications>)(Object) model.selectData("model.Uc_job_applications", where.toString());
				int pageCount = ucJobApp_list.size()/limit;
				if(pageCount == 0) pageCount = 1;
				 jsonResponse.put("pageCount", pageCount);
		//	}
			response.getWriter().write(jsonResponse.toString());
		}*/
	}
	
	private void showDetails(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException, JSONException {
		int jobId = Integer.parseInt(request.getParameter("jobId").toString());
		Hashtable sessionAttributes = new Hashtable();
		sessionAttributes = (Hashtable) SessionManager.sessionExists(request, "admin");
		ucCurrJob = new Uc_current_jobs();
		ucCurrJob = (Uc_current_jobs) model.selectData("model.Uc_current_jobs", "cj_id=" + jobId).get(0);
		ucPosition = new Uc_position();
		ucPosition = (Uc_position) model.selectData("model.Uc_position", "p_id=" + ucCurrJob.getCj_position()).get(0);
		ucEmployer = (Uc_employer_details) model.selectData("model.Uc_employer_details", "ed_id=" + ucCurrJob.getCj_employer_id()).get(0);
		
		StringBuilder temp = new StringBuilder();
		temp.append("<table class=\"table table-striped table-bordered\">");
		ucCity = new Uc_city();
		ucCity = (Uc_city) model.selectData("model.Uc_city", "c_id=" + ucCurrJob.getCj_city()).get(0);
		ucTaluka = new Uc_taluka();
		ucTaluka = (Uc_taluka) model.selectData("model.Uc_taluka", "t_id=" + ucCity.getC_taluka_id()).get(0);
		ucDistrict = new Uc_district();
		ucDistrict = (Uc_district) model.selectData("model.Uc_district", "d_id=" + ucTaluka.getT_district_id()).get(0);
		ucState = new Uc_state();
		ucState = (Uc_state) model.selectData("model.Uc_state", "s_id=" + ucDistrict.getD_s_id()).get(0);
		temp.append("<tr><th>Job Location: </th><td>" + ucCity.getC_name() + ", " + ucTaluka.getT_name() + ", " + ucDistrict.getD_name() + ", " + ucState.getS_name() + "</td></tr>");
		temp.append("<tr><th>Employer: </th><td>" + ucEmployer.getEd_firm_name() + " (" + ucEmployer.getEd_id() + ")</td></tr>");
		if(Integer.parseInt(sessionAttributes.get("utype").toString()) == UCConstants.SALES_ADMIN || Integer.parseInt(sessionAttributes.get("utype").toString()) == UCConstants.MAIN_ADMIN || Integer.parseInt(sessionAttributes.get("utype").toString()) == UCConstants.SUB_MAIN_ADMIN) {
			temp.append("<tr><th>Employer contact person: </th><td>" + ucEmployer.getEd_contact_person() + "</td></tr>");
			temp.append("<tr><th>Employer contact number: </th><td>" + ucEmployer.getEd_contact_no() + "</td></tr>");
			temp.append("<tr><th>Employer email: </th><td>" + ucEmployer.getEd_email() + "</td></tr>");
		}
		temp.append("<tr><th>Employer Address: </th><td>" + ucEmployer.getEd_address() + "</td></tr>");
		if(ucCurrJob.getCj_quantity() == 0) {
			ucCjQuantity_list = new ArrayList<Uc_cj_quantity>();
			ucCjQuantity_list = (List<Uc_cj_quantity>)(Object) model.selectData("model.Uc_cj_quantity", "cjq_job_id=" + ucCurrJob.getCj_id());
			if(!ucCjQuantity_list.isEmpty()) {
				ucCjQuantity = new Uc_cj_quantity();
				ucCjQuantity = ucCjQuantity_list.get(0);
				temp.append("<tr><th>Requirement: </th><td>" + ucCjQuantity.getCjq_male() + " Male and " + ucCjQuantity.getCjq_female() + " Female</td></tr>");
			} else {
				temp.append("<tr><th>Requirement: </th><td>N/A</td></tr>");
			}
		} else {
			temp.append("<tr><th>Requirement: </th><td>" + ucCurrJob.getCj_quantity() + " Male or Female</td></tr>");
		}
		temp.append("<tr><th>Position: </th><td>" + ucPosition.getP_name() + "</td></tr>");
		temp.append("<tr><th>Experience: </th><td>" + (ucCurrJob.getCj_experience_start()==ucCurrJob.getCj_experience_end() ? (ucCurrJob.getCj_experience_start() + " Years"):(ucCurrJob.getCj_experience_start() + "-" + ucCurrJob.getCj_experience_end() + " Years")) + "</td></tr>");
		ucJobQualifications_list = new ArrayList<Uc_job_qualifications>();
		ucJobQualifications_list = (List<Uc_job_qualifications>)(Object) model.selectData("model.Uc_job_qualifications", "jq_job_id=" + ucCurrJob.getCj_id());
		if(!ucJobQualifications_list.isEmpty()) {
			temp.append("<tr><th>Qualification required: </th><td>");
			String temppostfix = "";
			for(Uc_job_qualifications jq : ucJobQualifications_list) {
				ucQualifications = new Uc_qualifications();
				ucQualifications = (Uc_qualifications) model.selectData("model.Uc_qualifications", "q_id=" + jq.getJq_qualification_id()).get(0);
				temp.append(temppostfix);
				temppostfix = ", ";
				temp.append(ucQualifications.getQ_name());
			}
			temp.append("</td></tr>");
		}
		temp.append("<tr><th>Duty hours: </th><td>" + ucCurrJob.getCj_duty_hours() + "</td></tr>");
		temp.append("<tr><th>Expected salary: </th><td>" + ucCurrJob.getCj_salary() + "</td></tr>");
		temp.append("<tr><th>Work profile: </th><td>" + ucCurrJob.getCj_work_profile() + "</td></tr>");
		
		temp.append("</table>");
		ucCurrJob = new Uc_current_jobs();
		ucCurrJob.setCj_number_of_new_applicant(0);
		model.updateData(ucCurrJob, "cj_id=" + jobId);
		response.setContentType("application/json");
		 JSONObject jsonResponse = new JSONObject();
		 jsonResponse.put("table", temp.toString());
		 response.getWriter().write(jsonResponse.toString());
	}
	
	private void showPreFinalDetails(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException, JSONException {
		int jobId = Integer.parseInt(request.getParameter("jobId").toString());
		Hashtable sessionAttributes = new Hashtable();
		sessionAttributes = (Hashtable) SessionManager.sessionExists(request, "admin");
		ucCurrJob = new Uc_current_jobs();
		ucCurrJob = (Uc_current_jobs) model.selectData("model.Uc_current_jobs", "cj_id=" + jobId).get(0);
		ucPosition = new Uc_position();
		ucPosition = (Uc_position) model.selectData("model.Uc_position", "p_id=" + ucCurrJob.getCj_position()).get(0);
		ucEmployer = (Uc_employer_details) model.selectData("model.Uc_employer_details", "ed_id=" + ucCurrJob.getCj_employer_id()).get(0);
		
		StringBuilder temp = new StringBuilder();
		temp.append("<table class=\"table table-striped table-bordered\">");
		temp.append("<tr><th>Employer: </th><td>" + ucEmployer.getEd_firm_name() + " (" + ucEmployer.getEd_id() + ")</td></tr>");
		if(Integer.parseInt(sessionAttributes.get("utype").toString()) == UCConstants.SALES_ADMIN || Integer.parseInt(sessionAttributes.get("utype").toString()) == UCConstants.MAIN_ADMIN || Integer.parseInt(sessionAttributes.get("utype").toString()) == UCConstants.SUB_MAIN_ADMIN) {
			temp.append("<tr><th>Employer contact person: </th><td>" + ucEmployer.getEd_contact_person() + "</td></tr>");
			temp.append("<tr><th>Employer contact number: </th><td>" + ucEmployer.getEd_contact_no() + "</td></tr>");
			temp.append("<tr><th>Employer email: </th><td>" + ucEmployer.getEd_email() + "</td></tr>");
		}
		temp.append("<tr><th>Position: </th><td>" + ucPosition.getP_name() + "</td></tr>");
		temp.append("<tr><th>Experience: </th><td>" + (ucCurrJob.getCj_experience_start()==ucCurrJob.getCj_experience_end() ? (ucCurrJob.getCj_experience_start() + " Years"):(ucCurrJob.getCj_experience_start() + "-" + ucCurrJob.getCj_experience_end() + " Years")) + "</td></tr>");
		temp.append("<tr><th>Duty hours: </th><td>" + ucCurrJob.getCj_duty_hours() + "</td></tr>");
		temp.append("<tr><th>Expected salary: </th><td>" + ucCurrJob.getCj_salary() + "</td></tr>");
		temp.append("<tr><th>Work profile: </th><td>" + ucCurrJob.getCj_work_profile() + "</td></tr>");
		
			ucInterviews_list = new ArrayList<Uc_interviews>();
			ucInterviews_list = (List<Uc_interviews>)(Object) model.selectData("model.Uc_interviews", "i_job_id=" + ucCurrJob.getCj_id());
			if(ucInterviews_list.isEmpty()) {
				temp.append("<tr><th>Interview Date: </th><td><input placeholder=\"Enter\" onclick=\"disableError('interview-date')\" onkeyup=\"disableError('interview-date')\" type=\"text\" id=\"interview-date\" class=\"form-control\"><label style=\"color:red;font-weight:normal;display:none\" id=\"interview-date_error\"></label></td></tr>");
				temp.append("<tr><th>Interview Time: </th><td><input placeholder=\"Enter\" onkeyup=\"disableError('interview-time')\" type=\"text\" id=\"interview-time\" class=\"form-control\"><label style=\"color:red;font-weight:normal;display:none\" id=\"interview-time_error\"></label></td></tr>");
				temp.append("<tr><th>Interview Contact Person (optional): </th><td><input placeholder=\"Enter\" type=\"text\" id=\"interview-contact-person\" class=\"form-control\"></td></tr>");
				temp.append("<tr><th>Interview Location Address: </th><td><table class=\"table\"><tr><td><label><input onchange=\"disableError('interview-location')\" id=\"interview-location\" value=\"same\" type=\"checkbox\"> Same as employer office address</label><label style=\"color:red;font-weight:normal;display:none\" id=\"interview-location_error\"></label></td></tr><tr><th>OR</th></tr><tr><td><textarea placeholder=\"Enter\" id=\"interview-location_other\" onkeyup=\"disableError('interview-location')\" class=\"form-control\"></textarea></td></tr></table></td></tr>");
			} else {
				ucInterviews = new Uc_interviews();
				ucInterviews = ucInterviews_list.get(0);
				temp.append("<tr><th>Interview Date (yyyy-MM-dd): </th><td><input value=\"" + ucInterviews.getI_date() + "\" placeholder=\"Enter\" onclick=\"disableError('interview-date')\" onkeyup=\"disableError('interview-date')\" type=\"text\" id=\"interview-date\" class=\"form-control\"><label style=\"color:red;font-weight:normal;display:none\" id=\"interview-date_error\"></label></td></tr>");
				temp.append("<tr><th>Interview Time: </th><td><input value=\"" + ucInterviews.getI_time() + "\" placeholder=\"Enter\" onkeyup=\"disableError('interview-time')\" type=\"text\" id=\"interview-time\" class=\"form-control\"><label style=\"color:red;font-weight:normal;display:none\" id=\"interview-time_error\"></label></td></tr>");
				temp.append("<tr><th>Interview Contact Person (optional): </th><td><input value=\"" + ucInterviews.getI_contact_person() + "\" placeholder=\"Enter\" type=\"text\" id=\"interview-contact-person\" class=\"form-control\"></td></tr>");
				if(ucInterviews.getI_location().equals("same")) {
					temp.append("<tr><th>Interview Location Address: </th><td><table class=\"table\"><tr><td><label><input onchange=\"disableError('interview-location')\" id=\"interview-location\" value=\"same\" checked type=\"checkbox\"> Same as employer office address</label><label style=\"color:red;font-weight:normal;display:none\" id=\"interview-location_error\"></label></td></tr><tr><th>OR</th></tr><tr><td><textarea placeholder=\"Enter\" id=\"interview-location_other\" onkeyup=\"disableError('interview-location')\" class=\"form-control\"></textarea></td></tr></table></td></tr>");
				} else {
					temp.append("<tr><th>Interview Location Address: </th><td><table class=\"table\"><tr><td><label><input onchange=\"disableError('interview-location')\" id=\"interview-location\" value=\"same\" type=\"checkbox\"> Same as employer office address</label><label style=\"color:red;font-weight:normal;display:none\" id=\"interview-location_error\"></label></td></tr><tr><th>OR</th></tr><tr><td><textarea placeholder=\"Enter\" id=\"interview-location_other\" onkeyup=\"disableError('interview-location')\" class=\"form-control\">" + ucInterviews.getI_location() + "</textarea></td></tr></table></td></tr>");
				}
			}
			temp.append("<tr><th></th><td><button id=\"saveBtn\" type=\"button\" onclick=\"validate(" + ucCurrJob.getCj_id() + ")\" class=\"btn btn-primary\">Save interview shedule</button></td></tr>");
		
		temp.append("</table>");
		response.setContentType("application/json");
		 JSONObject jsonResponse = new JSONObject();
		 jsonResponse.put("table", temp.toString());
		 response.getWriter().write(jsonResponse.toString());
	}
	
	private void getApplicationList(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException, JSONException {
		int pageNo = Integer.parseInt(request.getParameter("pageNo").toString());
		int limit = Integer.parseInt(request.getParameter("limit").toString());
		boolean begin = request.getParameter("begin").toLowerCase().equals("true") ? true : false;
		int start = (pageNo-1)*limit;
		StringBuilder where = new StringBuilder();
		if(request.getParameter("isFiltered").toLowerCase().equals("true")) {
			String postfix = "(";
			String[] filters = request.getParameterValues("filters[]");
			for(String filter : filters) {
				where.append(postfix);
				postfix = " and ";
				if(filter.equals("1")) {
					where.append("ja_confirm_pre_final=2");
				} else if(filter.equals("2")) {
					where.append("ja_confirm_pre_final=1");
				} else if(filter.equals("3")) {
					where.append("ja_confirm_pre_final=0");
				} 
			}
			where.append(")");
		} else {
			where.append("1");
		}
		int jobId = Integer.parseInt(request.getParameterValues("extraParameters[0][]")[0].toString());
		where.append(" and ja_job=" + jobId);
		ucJobApp_list = new ArrayList<Uc_job_applications>();
		ucJobApp_list = (List<Uc_job_applications>)(Object) model.selectData("model.Uc_job_applications", where.toString() + " order by ja_id desc LIMIT " + start + "," + limit);
		if(ucJobApp_list.isEmpty()) {
			response.setContentType("application/json");
			JSONObject jsonResponse = new JSONObject();
			jsonResponse.put("table", "No job applications found.");
			jsonResponse.put("pageCount", 0);
			response.getWriter().write(jsonResponse.toString());
		} else {
		StringBuilder temp = new StringBuilder();
		temp.append("<table class=\"table table-bordered\" width=\"100%\"><tr><th>Sr. No.</th><th>Application Id (Type)</th><th>Candidate Id</th><th>Candidate Name</th><th>Candidate contact no.</th><th>Applied at</th><th>Details</th><th>Accept</th><th>Reject</th></tr>");
		for(int i = 0 ; i < ucJobApp_list.size() ; i++) {
			ucJobApp = new Uc_job_applications();
			ucJobApp = ucJobApp_list.get(i);
			String bgColor = "#000";
			if(ucJobApp.getJa_confirm_pre_final() == 1 && ucJobApp.getJa_confirm_final() != 0) bgColor = UCConstants.ACCEPTED_APPLICATION;
			else if(ucJobApp.getJa_confirm_pre_final() == 2 && ucJobApp.getJa_confirm_final() != 0) bgColor = UCConstants.PENDING_APPLICATION;
			else bgColor = UCConstants.REJECTED_APPLICATION;
			temp.append("<tr id=\"application_row" + ucJobApp.getJa_id() + "\" style=\"color:#fff;background:" + bgColor + "\">");
			temp.append("<td><label><input type=\"checkbox\" name=\"application_check\" value=\"" + ucJobApp.getJa_id() + "\"> &nbsp;" + (i+1) + "</label></td>");
			temp.append("<td>" + ucJobApp.getJa_id() + " (" + (ucJobApp.getJa_source()==0 ? "Online" : "Short-listed") + ")</td>");
			temp.append("<td>" + ucJobApp.getJa_candidate() + "</td>");
			ucCandidate = new Uc_candidate_details();
			ucCandidate = (Uc_candidate_details) model.selectData("model.Uc_candidate_details", "cd_id=" + ucJobApp.getJa_candidate()).get(0);
			temp.append("<td>" + ucCandidate.getCd_name() + " (" + (ucCandidate.getCd_gender()==1?"Male":"Female") + ")</td>");
			temp.append("<td>" + ucCandidate.getCd_contact_num() + "</td>");
			ucCurrJob = new Uc_current_jobs();
			ucCurrJob = (Uc_current_jobs) model.selectData("model.Uc_current_jobs", "cj_id=" + ucJobApp.getJa_job()).get(0);
			temp.append("<td>" + ucJobApp.getJa_date() + " - " + ucJobApp.getJa_time() + "</td>");
			temp.append("<td><button type=\"button\" onclick=\"showCandidateDetails(this," + ucJobApp.getJa_candidate() + ", " + ucJobApp.getJa_id() + ")\" class=\"btn btn-warning\">Details</button></td>");
			temp.append("<td><button type=\"button\" onclick=\"acceptApplication(this," + ucJobApp.getJa_id() + ")\" class=\"btn btn-success\">Accept</button></td>");
			temp.append("<td><button type=\"button\" data-id=\"" + ucJobApp.getJa_id() + "\" data-toggle=\"modal\" data-target=\"#myModal\" id=\"rejectButton\" class=\"btn btn-danger\">Reject</button></td>");
			/*onclick=\"rejectApplication(this," + ucJobApp.getJa_id() + ")\"*/
			temp.append("</tr>");
		}
		temp.append("</table>");
		temp.append("<button type=\"button\" onclick=\"deleteApplication(this)\" class=\"btn btn-danger\" style=\"margin:5px\">Delete</button>");
		response.setContentType("application/json");
		 JSONObject jsonResponse = new JSONObject();
		 jsonResponse.put("table", temp.toString());
		 if(begin) {
			 ucJobApp_list = new ArrayList<Uc_job_applications>();
			 ucJobApp_list = (List<Uc_job_applications>)(Object) model.selectData("model.Uc_job_applications", where.toString());
			 int pageCount = (int) Math.ceil(ucJobApp_list.size()/(float)limit);
			 if(pageCount == 0) pageCount = 1;
			 jsonResponse.put("pageCount", pageCount);
		 	}
		jsonResponse.put("id", jobId);
		response.getWriter().write(jsonResponse.toString());
		}
		/*ucJobApp_list = new ArrayList<Uc_job_applications>();
		ucJobApp_list = (List<Uc_job_applications>)(Object) model.selectData("model.Uc_job_applications", "ja_id=" + id);
		if(!ucJobApp_list.isEmpty()) {
			ucJobApp = new Uc_job_applications();
			ucJobApp = ucJobApp_list.get(0);
			StringBuilder temp = new StringBuilder();
			temp.append("<table>");
			ucCurrJob = new Uc_current_jobs();
			ucCurrJob = (Uc_current_jobs) model.selectData("model.Uc_current_jobs", "cj_id=" + ucJobApp.getJa_job()).get(0);
			ucEmployer = new Uc_employer_details();
			ucEmployer = (Uc_employer_details) model.selectData("model.Uc_employer_details", "ed_id=" + ucCurrJob.getCj_employer_id()).get(0);
			ucPosition = new Uc_position();
			ucPosition = (Uc_position) model.selectData("model.Uc_position", "p_id=" + ucCurrJob.getCj_position()).get(0);
			ucCity = new Uc_city();
			ucCity = (Uc_city) model.selectData("model.Uc_city", "c_id=" + ucCurrJob.getCj_city()).get(0);
			ucDistrict = new Uc_district();
			ucDistrict = (Uc_district) model.selectData("model.Uc_district", "d_id=" + ucCity.getC_district()).get(0);
			ucState = new Uc_state();
			ucState = (Uc_state) model.selectData("model.Uc_state", "s_id=" + ucDistrict.getD_s_id()).get(0);
			String quantity = "";
			if(ucCurrJob.getCj_quantity() == 0) {
				ucCjQuantity = new Uc_cj_quantity();
				ucCjQuantity = (Uc_cj_quantity) model.selectData("model.Uc_cj_quantity", "cjq_job_id=" + ucCurrJob.getCj_id()).get(0);
				quantity = ucCjQuantity.getCjq_male() + " males and " + ucCjQuantity.getCjq_female() + " females";
			} else {
				quantity = ucCurrJob.getCj_quantity() + " males or females";
			}
			
			temp.append("<tr><td>Job posted on: </td><th> " + ucCurrJob.getCj_posted_at() + "</th></tr>");
			temp.append("<tr><td>Candidate applied on: </td><th> " + ucJobApp.getJa_date() + " , " + ucJobApp.getJa_time() + "</th></tr>");
			temp.append("<tr><td>Job employer: </td><th> " + ucEmployer.getEd_id() + " - " + ucEmployer.getEd_firm_name() + "</th></tr>");
			temp.append("<tr><td>Job position: </td><th> " + ucPosition.getP_name() + "</th></tr>");
			temp.append("<tr><td>Job location: </td><th> " + ucCity.getC_name() + ", " + ucDistrict.getD_name() + ", " + ucState.getS_name() + "</th></tr>");
			temp.append("<tr><td>Requirement: </td><th> " + quantity + "</th></tr>");
			temp.append("<tr><td><hr></td><td><hr></td></tr>");
			
			ucCandidate = new Uc_candidate_details(); 
			ucCandidate = (Uc_candidate_details) model.selectData("model.Uc_candidate_details", "cd_id=" + ucJobApp.getJa_candidate()).get(0);
			ucCity = new Uc_city();
			ucCity = (Uc_city) model.selectData("model.Uc_city", "c_id=" + ucCandidate.getCd_city_id()).get(0);
			ucDistrict = new Uc_district();
			ucDistrict = (Uc_district) model.selectData("model.Uc_district", "d_id=" + ucCity.getC_district()).get(0);
			ucState = new Uc_state();
			ucState = (Uc_state) model.selectData("model.Uc_state", "s_id=" + ucDistrict.getD_s_id()).get(0);
			ucInterest_list = new ArrayList<Uc_interest_areas>();
			ucInterest_list = (List<Uc_interest_areas>)(Object) model.selectData("model.Uc_interest_areas", "ia_candidate=" + ucCandidate.getCd_id());
			StringBuilder interestString = new StringBuilder();
			String tempPostfix = "";
			for(Uc_interest_areas i : ucInterest_list) {
				ucIndustry = new Uc_industry_sector();
				ucIndustry = (Uc_industry_sector) model.selectData("model.Uc_industry_sector", "is_id=" + i.getIa_industry()).get(0);
				ucPosition = new Uc_position();
				ucPosition = (Uc_position) model.selectData("model.Uc_position", "p_id=" + i.getIa_position()).get(0);
				interestString.append(tempPostfix);
				tempPostfix = ", ";
				interestString.append(ucIndustry.getIs_name() + " - " + ucPosition.getP_name());
			}
			StringBuilder expString = new StringBuilder();
			if(ucCandidate.getCd_experience() == 1) {
				ucExperience_list = new ArrayList<Uc_experience>();
				ucExperience_list = (List<Uc_experience>)(Object) model.selectData("model.Uc_experience", "e_candidate_id=" + ucCandidate.getCd_id());
				tempPostfix = "";
				for(Uc_experience e : ucExperience_list) {
					ucIndustry = new Uc_industry_sector();
					ucIndustry = (Uc_industry_sector) model.selectData("model.Uc_industry_sector", "is_id=" + e.getE_industry_id()).get(0);
					ucPosition = new Uc_position();
					ucPosition = (Uc_position) model.selectData("model.Uc_position", "p_id=" + e.getE_position_id()).get(0);
					expString.append(tempPostfix);
					tempPostfix = ", ";
					expString.append(ucIndustry.getIs_name() + " - " + ucPosition.getP_name() + "(" + e.getE_years() + " years)");
				}
			} else {
				expString.append("No experience");
			}
			temp.append("<tr><td>Candidate: </td><th> "+ ucCandidate.getCd_id() + " - " + ucCandidate.getCd_name() + " (" + (ucCandidate.getCd_is_placed()==0?"Not placed":"Placed") + ")</th></tr>");
			temp.append("<tr><td>Contact: </td><th> " + ucCandidate.getCd_contact_num() + ", " + ucCandidate.getCd_email() + "</th></tr>");
			temp.append("<tr><td>Candidate location: </td><th> " + ucCity.getC_name() + ", " + ucDistrict.getD_name() + ", " + ucState.getS_name() + "</th></tr>");
			temp.append("<tr><td>Candidate interest areas: </td><th> " + interestString.toString() + "</th></tr>");
			temp.append("<tr><td>Candidate experience: </td><th> " + expString.toString() + "</th></tr>");
			temp.append("<tr><td><button type=\"button\" onclick=\"accept_application(this," + ucJobApp.getJa_id() + ")\" class=\"btn btn-success\"><i class=\"fa fa-check\"></i> &nbsp; Accept</button> &nbsp;&nbsp; <button type=\"button\" onclick=\"reject_application(this," + ucJobApp.getJa_id() + ")\" class=\"btn btn-danger\"><i class=\"fa fa-close\"></i> &nbsp; Reject</button> &nbsp;&nbsp; <button type=\"button\" onclick=\"close_details()\" class=\"btn btn-warning\">Close</button></td><th></th></tr>");
			temp.append("</table>");
			
			JSONObject json = new JSONObject();
			json.put("id", ucJobApp.getJa_id());
			json.put("details", temp.toString());
			response.setContentType("application/json");
			response.getWriter().write(json.toString());
		} else {
			//no such job application found
		}*/
	}
	
	private void getCandidateDetails(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException, JSONException {
		int candidateId = Integer.parseInt(request.getParameter("candidateId").toString());
		int applicationId = Integer.parseInt(request.getParameter("applicationId").toString());
		ucCandidate = new Uc_candidate_details();
		ucCandidate = (Uc_candidate_details) model.selectData("model.Uc_candidate_details", "cd_id=" + candidateId).get(0);
		
		StringBuilder temp = new StringBuilder();
		temp.append("<table class=\"table table-striped table-bordered\">");
		temp.append("<tr><th>Name: </th><td>" + ucCandidate.getCd_name() + " (" + (ucCandidate.getCd_gender() == UCConstants.MALE ? "Male" : "Female") + ")</td></tr>");
		temp.append("<tr><th>Contact Number: </th><td>" + ucCandidate.getCd_contact_num() + "</td></tr>");
		temp.append("<tr><th>Email Id: </th><td>" + ucCandidate.getCd_email() + "</td></tr>");
		
		ucCity = new Uc_city();
		ucCity = (Uc_city) model.selectData("model.Uc_city", "c_id=" + ucCandidate.getCd_city_id()).get(0);
		ucTaluka = new Uc_taluka();
		ucTaluka = (Uc_taluka) model.selectData("model.Uc_taluka", "t_id=" + ucCity.getC_taluka_id()).get(0);
		ucDistrict = new Uc_district();
		ucDistrict = (Uc_district) model.selectData("model.Uc_district", "d_id=" + ucTaluka.getT_district_id()).get(0);
		ucState = new Uc_state();
		ucState = (Uc_state) model.selectData("model.Uc_state", "s_id=" + ucDistrict.getD_s_id()).get(0);
		temp.append("<tr><th>Location: </th><td>" + ucCity.getC_name() + ", " + ucTaluka.getT_name() + ", " + ucDistrict.getD_name() + ", " + ucState.getS_name() + "</td></tr>");
		temp.append("<tr><th>Payment done: </th><td>" + (ucCandidate.getCd_payment() == 1 ? "Yes" : "No") + "</td></tr>");
		ucCandiQuali_list = new ArrayList<Uc_candidate_qualification>();
		ucCandiQuali_list = (List<Uc_candidate_qualification>)(Object) model.selectData("model.Uc_candidate_qualification", "cq_candidate_id=" + ucCandidate.getCd_id());
		temp.append("<tr><th>Qualification: </th>");
		if(ucCandiQuali_list.isEmpty()) {
			temp.append("<td>No qualification entered.</td>");
		} else {
			temp.append("<td>");
			//String postfix = "";
			StringBuilder tempQuali = new StringBuilder();
			tempQuali.append("<table class=\"table table-striped table-bordered\"><tr><th>Qualification</th></tr>");
			for(Uc_candidate_qualification cq : ucCandiQuali_list) {
				ucQualifications = new Uc_qualifications();
				ucQualifications = (Uc_qualifications) model.selectData("model.Uc_qualifications", "q_id=" + cq.getCq_quali_id()).get(0);
				/*temp.append(postfix);
				postfix = ", ";
				temp.append(ucQualifications.getQ_name());*/
				tempQuali.append("<tr><td><input type=\"text\" value=\"" + ucQualifications.getQ_name() + "\" onblur=\"saveCandidateQualification(this," + cq.getCq_id() + ")\"></td></tr>");
			}
			tempQuali.append("</table>");
			temp.append(tempQuali);
			temp.append("</td>");
		}
		temp.append("</tr>");
		if(ucCandidate.getCd_experience() == 1) {
			ucExperience_list = new ArrayList<Uc_experience>();
			ucExperience_list = (List<Uc_experience>)(Object) model.selectData("model.Uc_experience", "e_candidate_id=" + ucCandidate.getCd_id());
			StringBuilder tempExp = new StringBuilder();
			tempExp.append("<table class=\"table table-striped table-bordered\"><tr><th>Industry sector</th><th>Position</th><th>Years</th><th>Is current job</th><th>Salary</th></tr>");
			for(Uc_experience exp : ucExperience_list) {
				tempExp.append("<tr>");
				ucIndustry = new Uc_industry_sector();
				ucIndustry = (Uc_industry_sector) model.selectData("model.Uc_industry_sector", "is_id=" + exp.getE_industry_id()).get(0);
				ucPosition = new Uc_position();
				ucPosition = (Uc_position) model.selectData("model.Uc_position", "p_id=" + exp.getE_position_id()).get(0);
				tempExp.append("<td>" + ucIndustry.getIs_name() + "</td>");
				tempExp.append("<td>" + ucPosition.getP_name() + "</td>");
				tempExp.append("<td><input type=\"number\" value=\"" + exp.getE_years() + "\" onblur=\"saveExperienceYears(this, " + exp.getE_id() + ", " + exp.getE_candidate_id() + ")\"></td>");
				tempExp.append("<td>" + (exp.getE_is_current_job() == 1 ? "Yes" : "No") + "</td>");
				tempExp.append("<td><input type=\"number\" value=\"" + exp.getE_salary_per_month() + "\" onblur=\"saveSalary(this, " + exp.getE_id() + ", " + exp.getE_candidate_id() + ")\"></td>");
				tempExp.append("</tr>");
			}
			tempExp.append("</table>");
			temp.append("<tr><th>Experience: </th><td>" + tempExp.toString() + "</td></tr>");
		} else {
			temp.append("<tr><th>Experience: </th><td>N/A</td></tr>");
		}
		temp.append("<tr><th>Expected Salary: </th><td><input type=\"text\" value=\"" + ucCandidate.getCd_expected_salary() + "\" onblur=\"saveExpectedSalary(this, " + ucCandidate.getCd_id() + ")\"></td></tr>");
		StringBuilder tempInterest = new StringBuilder();
		ucInterest_list = new ArrayList<Uc_interest_areas>();
		ucInterest_list = (List<Uc_interest_areas>)(Object) model.selectData("model.Uc_interest_areas", "ia_candidate=" + ucCandidate.getCd_id());
		if(ucInterest_list.isEmpty()) {
			temp.append("<tr><th>Interest areas: </th><td>N/A</td></tr>");
		} else {
		tempInterest.append("<table class=\"table table-striped table-bordered\"><tr><th>Industry sector</th><th>Position</th></tr>");
		for(Uc_interest_areas interest : ucInterest_list) {
			tempInterest.append("<tr>");
			ucIndustry = new Uc_industry_sector();
			ucIndustry = (Uc_industry_sector) model.selectData("model.Uc_industry_sector", "is_id=" + interest.getIa_industry()).get(0);
			ucPosition = new Uc_position();
			ucPosition = (Uc_position) model.selectData("model.Uc_position", "p_id=" + interest.getIa_position()).get(0);
			tempInterest.append("<td>" + ucIndustry.getIs_name() + "</td>");
			tempInterest.append("<td>" + ucPosition.getP_name() + "</td>");
			tempInterest.append("</tr>");
		}
		tempInterest.append("</table>");
		temp.append("<tr><th>Interest areas: </th><td>" + tempInterest + "</td></tr>");
		}
		
		/*
		ucApplicationComments_list = new ArrayList<Uc_application_comments>();
		ucApplicationComments_list = (List<Uc_application_comments>)(Object) model.selectData("model.Uc_application_comments", "ac_application_id=" + ucJobApp.getJa_id());
		if(!ucApplicationComments_list.isEmpty()) {
			temp.append("<tr><th>Job application comments: </th><td><textarea style=\"width:100%\" maxlimit=\"500\" onblur=\"saveComment(this," + ucJobApp.getJa_id() + "," + UCConstants.JOB_APPLICATION_COMMENT + ")\">" + ucApplicationComments_list.get(0).getAc_comment() + "</textarea></td></tr>");
		} else {
			temp.append("<tr><th>Job application comments: </th><td><textarea style=\"width:100%\" maxlimit=\"500\" onblur=\"saveComment(this," + ucJobApp.getJa_id() + "," + UCConstants.JOB_APPLICATION_COMMENT + ")\"></textarea></td></tr>");
		}
		
		ucCandidateComments_list = new ArrayList<Uc_candidate_comments>();
		ucCandidateComments_list = (List<Uc_candidate_comments>)(Object) model.selectData("model.Uc_candidate_comments", "cc_candidate_id=" + ucCandidate.getCd_id());
		if(!ucCandidateComments_list.isEmpty()) {
			temp.append("<tr><th>Candidate comments: </th><td><textarea style=\"width:100%\" maxlimit=\"500\" onblur=\"saveComment(this," + ucCandidate.getCd_id() + "," + UCConstants.CANDIDATE_COMMENT + ")\">" + ucCandidateComments_list.get(0).getCc_comment() + "</textarea></td></tr>");
		} else {
			temp.append("<tr><th>Candidate comments: </th><td><textarea style=\"width:100%\" maxlimit=\"500\" onblur=\"saveComment(this," + ucCandidate.getCd_id() + "," + UCConstants.CANDIDATE_COMMENT + ")\"></textarea></td></tr>");
		}
		*/
		
		temp.append("<tr><th>Job application comments: </th><td>");
		temp.append("<div id=\"application_comments\"></div>");
		temp.append("<hr>");
		temp.append("<textarea placeholder=\"Enter comment\" style=\"width:80%\" id=\"application_comment_entry\"></textarea><button type=\"button\" class=\"btn btn-primary\" onclick=\"saveComment(this," + applicationId + "," + UCConstants.JOB_APPLICATION_COMMENT + ")\">Submit</button>");
		temp.append("</td></tr>");
		temp.append("<tr><th>Candidate comments: </th><td>");
		temp.append("<div id=\"candidate_comments\"></div><br>");
		temp.append("<hr>");
		temp.append("<textarea placeholder=\"Enter comment\" style=\"width:80%\" id=\"candidate_comment_entry\"></textarea><button type=\"button\" class=\"btn btn-primary\" onclick=\"saveComment(this," + ucCandidate.getCd_id() + ", " + UCConstants.CANDIDATE_COMMENT + ")\">Submit</button>");
		temp.append("</td></tr>");
		
		temp.append("<tr><th></th><td>");
		temp.append("<button type=\"button\" onclick=\"acceptApplication(this," + applicationId + ")\" class=\"btn btn-success\">Accept</button> &nbsp;&nbsp; ");
		temp.append("<button type=\"button\" onclick=\"rejectApplication(this," + applicationId + ")\" class=\"btn btn-danger\">Reject</button>");
		temp.append("</td></tr>");
		temp.append("</table>");
		response.setContentType("application/json");
		 JSONObject jsonResponse = new JSONObject();
		 jsonResponse.put("table", temp.toString());
		 response.getWriter().write(jsonResponse.toString());
	}
	
	private void acceptApplication(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException, JSONException {
		int jobId = Integer.parseInt(request.getParameter("jobId").toString());
		ucJobApp = new Uc_job_applications();
		ucJobApp.setJa_confirm_pre_final(1);
		ucJobApp.setJa_confirm_final(2);
		response.setContentType("text/html;charset=UTF-8");
		if(model.updateData(ucJobApp, "ja_id=" + jobId)) {
			response.getWriter().write("success");
		} else {
			response.getWriter().write("error");
		}
	}
	
	private void rejectApplication(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException, JSONException {
		int jobId = Integer.parseInt(request.getParameter("jobId").toString());
		String reason = request.getParameter("reason").toString();
		ucJobApp = new Uc_job_applications();
		ucJobApp.setJa_confirm_pre_final(0);
		ucJobApp.setJa_confirm_final(0);
		ucJobApp.setJa_reject_reason(reason);
		response.setContentType("text/html;charset=UTF-8");
		if(model.updateData(ucJobApp, "ja_id=" + jobId)) {
			response.getWriter().write("success");
		} else {
			response.getWriter().write("error");
		}
	}
	
	private void getApplicationComments(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException, JSONException {
		int applicationId = Integer.parseInt(request.getParameterValues("extraParameters[0][]")[0].toString());
		Hashtable sessionAttributes = new Hashtable();
		sessionAttributes = (Hashtable) SessionManager.sessionExists(request, "admin");
		int pageNo = Integer.parseInt(request.getParameter("pageNo").toString());
		int limit = Integer.parseInt(request.getParameter("limit").toString());
		boolean begin = request.getParameter("begin").toLowerCase().equals("true") ? true : false;
		int start = (pageNo-1)*limit;
		ucApplicationComments_list = new ArrayList<Uc_application_comments>();
		ucApplicationComments_list = (List<Uc_application_comments>)(Object) model.selectData("model.Uc_application_comments", "ac_application_id=" + applicationId + " order by ac_id desc LIMIT " + start + "," + limit);
		if(ucApplicationComments_list.isEmpty()) {
			response.setContentType("application/json");
			JSONObject jsonResponse = new JSONObject();
			jsonResponse.put("table", "No comments are submitted yet.");
			jsonResponse.put("pageCount", 0);
			response.getWriter().write(jsonResponse.toString());
		} else {
			StringBuilder temp = new StringBuilder();
			
			temp.append("<table class=\"table table-bordered table-striped\">");
			temp.append("<tr><th>Comment</th><th>Date - Time</th><th>Delete</th></tr>");
			for(Uc_application_comments ac : ucApplicationComments_list) {
				temp.append("<tr>");
				if(Integer.parseInt(sessionAttributes.get("uuid").toString()) == ac.getAc_added_by() || Integer.parseInt(sessionAttributes.get("utype").toString()) == UCConstants.MAIN_ADMIN) {
					temp.append("<td><textarea maxlimit=\"500\" onblur=\"saveCommentChanges(this," + ac.getAc_id() + "," + UCConstants.JOB_APPLICATION_COMMENT + ")\">" + ac.getAc_comment() + "</textarea></td>");
					temp.append("<td>" + ac.getAc_date() + " - " + ac.getAc_time() + "</td>");
					temp.append("<td><button type=\"button\" class=\"btn btn-danger\" onclick=\"deleteComment(" + applicationId + "," + ac.getAc_id() + "," + UCConstants.JOB_APPLICATION_COMMENT + ")\"><i class=\"fa fa-ban\"></i></button></td>");
				} else {
					temp.append("<td>" + ac.getAc_comment() + "</td>");
					temp.append("<td>" + ac.getAc_date() + " - " + ac.getAc_time() + "</td>");
					temp.append("<td>---</td>");
				}
				temp.append("</tr>");
			}
			temp.append("</table>");
			
			response.setContentType("application/json");
			 JSONObject jsonResponse = new JSONObject();
			 jsonResponse.put("table", temp.toString());
			 if(begin) {
				 ucApplicationComments_list = new ArrayList<Uc_application_comments>();
				 ucApplicationComments_list = (List<Uc_application_comments>)(Object) model.selectData("model.Uc_application_comments", "ac_application_id=" + applicationId);
				 int pageCount = (int) Math.ceil(ucApplicationComments_list.size()/(float)limit);
				 if(pageCount == 0) pageCount = 1;
				 jsonResponse.put("pageCount", pageCount);
			 }
			 response.getWriter().write(jsonResponse.toString());
		}
	}
	
	private void saveCommentChanges(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		boolean successFlag;
		String comment = request.getParameter("comment").toString();
		int id = Integer.parseInt(request.getParameter("id").toString());
			ucApplicationComment = new Uc_application_comments();
			ucApplicationComment.setAc_comment(comment);
			successFlag = model.updateData(ucApplicationComment, "ac_id=" + id);
		response.setContentType("text/html;charset=UTF-8");
		if(successFlag) {
			response.getWriter().write("success");
		} else {
			response.getWriter().write("error");
		}
	}
	
	private void saveComment(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		String comment = request.getParameter("comment").toString();
		long applicationId = Long.parseLong(request.getParameter("id").toString());
		Hashtable sessionAttributes = new Hashtable();
		sessionAttributes = (Hashtable) SessionManager.sessionExists(request, "admin");
		ucApplicationComment = new Uc_application_comments();
		ucApplicationComment.setAc_added_by(Long.parseLong(sessionAttributes.get("uuid").toString()));
		ucApplicationComment.setAc_application_id(applicationId);
		ucApplicationComment.setAc_comment(comment);
		ucApplicationComment.setAc_date(new java.sql.Date(Calendar.getInstance().getTime().getTime()));
		ucApplicationComment.setAc_time(new SimpleDateFormat("HH:mm:ss").format(new Date()));
		response.setContentType("text/html;charset=UTF-8");
		if(model.insertData(ucApplicationComment)) {
			response.getWriter().write("success");
		} else {
			response.getWriter().write("error");
		}
	}
	
	private void deleteComment(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		int commentId = Integer.parseInt(request.getParameter("commentId").toString());
		response.setContentType("text/html;charset=UTF-8");
		if(model.deleteData("model.Uc_application_comments", "ac_id=" + commentId)) {
			response.getWriter().write("success");
		} else {
			response.getWriter().write("error");
		}
	}
	
	private void acceptJob(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		int jobId = Integer.parseInt(request.getParameter("jobId").toString());
		ucJobApp_list = new ArrayList<Uc_job_applications>();
		ucJobApp_list = (List<Uc_job_applications>)(Object) model.selectData("model.Uc_job_applications", "ja_job=" + jobId + " and ja_confirm_pre_final=1");
		if(ucJobApp_list.isEmpty()) {
			response.setContentType("text/html;charset=UTF-8");
			response.getWriter().write("set");
		} else {
		ucCurrJob = new Uc_current_jobs();
		ucCurrJob.setCj_approved_by_telecaller(1);
		ucCurrJob.setCj_approved_by_sales(2);
		ucCurrJob.setCj_approved_by_telecaller_final(2);
		response.setContentType("text/html;charset=UTF-8");
		if(model.updateData(ucCurrJob, "cj_id=" + jobId)) {
			response.getWriter().write("success");
		} else {
			response.getWriter().write("error");
		}
		}
	}
	
	private void rejectJob(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		int jobId = Integer.parseInt(request.getParameter("jobId").toString());
		String reason = request.getParameter("reason").toString();
		ucCurrJob = new Uc_current_jobs();
		ucCurrJob.setCj_approved_by_telecaller(0);
		ucCurrJob.setCj_approved_by_telecaller_final(0);
		ucCurrJob.setCj_approved_by_sales(0);
		ucJobApps = new Uc_job_applications();
		//ucJobApps.setJa_confirm_final(0);
		//ucJobApps.setJa_confirm_pre_final(0);
		ucJobApps.setJa_reject_reason(reason);
		response.setContentType("text/html;charset=UTF-8");
		if(model.updateData(ucCurrJob, "cj_id=" + jobId)) {
			if(model.updateData(ucJobApps, "ja_job=" + jobId)) {
				response.getWriter().write("success");
			} else {
				response.getWriter().write("error");
			}
		} else {
			response.getWriter().write("error");
		}
	}
	
	private void acceptJobFinal(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		int jobId = Integer.parseInt(request.getParameter("jobId").toString());
		ucInterviews_list = new ArrayList<Uc_interviews>();
		ucInterviews_list = (List<Uc_interviews>)(Object) model.selectData("model.Uc_interviews", "i_job_id=" + jobId);
		if(ucInterviews_list.isEmpty()) {
			response.setContentType("text/html;charset=UTF-8");
			response.getWriter().write("set");
		} else {
			ucCurrJob = new Uc_current_jobs();
			ucCurrJob.setCj_approved_by_sales(1);
			ucCurrJob.setCj_approved_by_telecaller_final(2);
			ucCurrJob.setCj_approved_by_telecaller(1);
			response.setContentType("text/html;charset=UTF-8");
			if(model.updateData(ucCurrJob, "cj_id=" + jobId)) {
				response.getWriter().write("success");
			} else {
				response.getWriter().write("error");
			}
		}
	}
	
	private void rejectJobFinal(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		int jobId = Integer.parseInt(request.getParameter("jobId").toString());
		String reason = request.getParameter("reason").toString();
		//ucInterviews_list = new ArrayList<Uc_interviews>();
	//	ucInterviews_list = (List<Uc_interviews>)(Object) model.selectData("model.Uc_interviews", "i_job_id=" + jobId);
		//if(!ucInterviews_list.isEmpty()) {
			//model.deleteData("model.Uc_interviews", "i_job_id=" + jobId);
		//}
		ucCurrJob = new Uc_current_jobs();
		ucCurrJob.setCj_approved_by_sales(0);
		//ucCurrJob.setCj_approved_by_telecaller(0);
		//ucCurrJob.setCj_approved_by_telecaller_final(0);
		ucJobApps = new Uc_job_applications();
		//ucJobApps.setJa_confirm_final(0);
		//ucJobApps.setJa_confirm_pre_final(0);
		ucJobApps.setJa_reject_reason(reason);
		response.setContentType("text/html;charset=UTF-8");
		if(model.updateData(ucCurrJob, "cj_id=" + jobId)) {
			if(model.updateData(ucJobApps, "ja_job=" + jobId)) {
				response.getWriter().write("success");
			} else {
				response.getWriter().write("error");
			}
		} else {
			response.getWriter().write("error");
		}
	}
	
	
	private void getTelecallerApprovedJobs(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException, JSONException {
		int pageNo = Integer.parseInt(request.getParameter("pageNo").toString());
		int limit = Integer.parseInt(request.getParameter("limit").toString());
		boolean begin = request.getParameter("begin").toLowerCase().equals("true") ? true : false;
		int start = (pageNo-1)*limit;
		int count = 0;
		boolean hasExtra = false;
		StringBuilder where = new StringBuilder();
		if(request.getParameter("isFiltered").toLowerCase().equals("true")) {
			String postfix = "(";
			String[] filters = request.getParameterValues("filters[]");
			for(String filter : filters) {
				where.append(postfix);
				postfix = " and ";
				if(filter.equals("1")) {
					where.append("cj_approved_by_sales=2");
				} else if(filter.equals("2")) {
					where.append("cj_approved_by_sales=1");
				} else if(filter.equals("3")) {
					where.append("cj_approved_by_sales=0");
				} 
			}
			where.append(")");
		} else {
			where.append("1");
		}
		
		if(request.getParameter("extraParameters") != "" && request.getParameter("extraParameters") == null) {
			String postfix = " and ";
			while(true) {
			
				if(request.getParameterValues("extraParameters[" + count + "][]") == null) break;
				hasExtra = true;
				String val1 = request.getParameterValues("extraParameters[" + count + "][]")[0];
				String val2 = request.getParameterValues("extraParameters[" + count + "][]")[1];
				count++;
			
				where.append(postfix);
				postfix = " and ";
				
				if(val1.equals("1")) {
					where.append("cj_id=" + Integer.parseInt(val2));
				} else if(val1.equals("2")) {
					where.append("cj_position=" + Integer.parseInt(val2));
				} 
			}
	}
		
		where.append(" and cj_approved_by_telecaller=1 and cj_is_active=1");
		ucCurrJobs_list = new ArrayList<Uc_current_jobs>();
		ucCurrJobs_list = (List<Uc_current_jobs>)(Object) model.selectData("model.Uc_current_jobs", where.toString() + " order by cj_id desc LIMIT " + start + "," + limit);
		if(ucCurrJobs_list.isEmpty()) {
			response.setContentType("application/json");
			JSONObject jsonResponse = new JSONObject();
			jsonResponse.put("table", "No job applications are submitted yet.");
			jsonResponse.put("pageCount", 0);
			response.getWriter().write(jsonResponse.toString());
		} else {
			StringBuilder temp = new StringBuilder();
			temp.append("<table class=\"table table-bordered\" width=\"100%\"><tr><th>Sr. No.</th><th>Job Id</th><th>Employer Name (Id)</th><th>Position</th><th>Number of applicants</th><th>Details</th><th>Accept</th><th>Reject</th></tr>");
			for(int i = 0 ; i < ucCurrJobs_list.size() ; i++) {
				ucCurrJob = new Uc_current_jobs();
				ucCurrJob = ucCurrJobs_list.get(i);
				String bgColor = "";
				if(ucCurrJob.getCj_approved_by_sales() == 2) bgColor = UCConstants.PENDING_APPLICATION;
				else if(ucCurrJob.getCj_approved_by_sales() == 1) bgColor = UCConstants.ACCEPTED_APPLICATION;
				else bgColor = UCConstants.REJECTED_APPLICATION;
				temp.append("<tr id=\"requirement_row" + ucCurrJob.getCj_id() + "\" style=\"background:" + bgColor + ";color:#fff\">");
				temp.append("<td>" + (i+1) + "</td>");
				temp.append("<td>" + ucCurrJob.getCj_id() + "</td>");
				
				ucEmployer = new Uc_employer_details();
				ucEmployer = (Uc_employer_details) model.selectData("model.Uc_employer_details", "ed_id=" + ucCurrJob.getCj_employer_id()).get(0);
				temp.append("<td>" + ucEmployer.getEd_firm_name() + " ("  + ucEmployer.getEd_id() + ")</td>");
				
				ucPosition = new Uc_position();
				ucPosition = (Uc_position) model.selectData("model.Uc_position", "p_id=" + ucCurrJob.getCj_position()).get(0);
				temp.append("<td>" + ucPosition.getP_name() + "</td>");
				
				ucJobApp_list = new ArrayList<Uc_job_applications>();
				ucJobApp_list = (List<Uc_job_applications>)(Object) model.selectData("model.Uc_job_applications", "ja_job=" + ucCurrJob.getCj_id() + " and ja_confirm_pre_final=1");
				temp.append("<td>" + ucJobApp_list.size() + "</td>");
				
				temp.append("<td><button type=\"button\" onclick=\"showDetails(this," + ucCurrJob.getCj_id() + ")\" class=\"btn btn-info\">Details</button></td>");
				temp.append("<td><button type=\"button\" onclick=\"acceptJob(this," + ucCurrJob.getCj_id() + ")\" class=\"btn btn-success\">Accept</button></td>");
				temp.append("<td><button type=\"button\" data-id=\"" + ucCurrJob.getCj_id() + "\" data-toggle=\"modal\" data-target=\"#rejectJobModal\" id=\"rejectJobButton\" class=\"btn btn-danger\">Reject</button></td>");
				temp.append("</tr>");
			}
			temp.append("</table>");
			response.setContentType("application/json");
			 JSONObject jsonResponse = new JSONObject();
			 jsonResponse.put("table", temp.toString());
			 if(begin) {
				 ucCurrJobs_list = new ArrayList<Uc_current_jobs>();
				 ucCurrJobs_list = (List<Uc_current_jobs>)(Object) model.selectData("model.Uc_current_jobs", where.toString());
				 int pageCount = (int) Math.ceil(ucCurrJobs_list.size()/(float)limit);
				 if(pageCount == 0) pageCount = 1;
				 jsonResponse.put("pageCount", pageCount);
			 }
				 response.getWriter().write(jsonResponse.toString());
		}
	}
	
	private void getAcceptedApplications(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException, JSONException {
		int pageNo = Integer.parseInt(request.getParameter("pageNo").toString());
		int limit = Integer.parseInt(request.getParameter("limit").toString());
		boolean begin = request.getParameter("begin").toLowerCase().equals("true") ? true : false;
		int start = (pageNo-1)*limit;
		StringBuilder where = new StringBuilder();
		int jobId = Integer.parseInt(request.getParameterValues("extraParameters[0][]")[0].toString());
		where.append("ja_job=" + jobId + " and ja_confirm_pre_final=1");
		ucJobApp_list = new ArrayList<Uc_job_applications>();
		ucJobApp_list = (List<Uc_job_applications>)(Object) model.selectData("model.Uc_job_applications", where.toString() + " order by ja_id desc LIMIT " + start + "," + limit);
		if(ucJobApp_list.isEmpty()) {
			response.setContentType("application/json");
			JSONObject jsonResponse = new JSONObject();
			jsonResponse.put("table", "No job applications are submitted yet.");
			jsonResponse.put("pageCount", 0);
			response.getWriter().write(jsonResponse.toString());
		} else {
			StringBuilder temp = new StringBuilder();
			temp.append("<div class=\"row\"><div class=\"col-lg-12\">");
			for(Uc_job_applications ja : ucJobApp_list) {
				temp.append("<div class=\"col-lg-4 home_boxes\" style=\"padding-top:10px\">");
				ucCandidate = new Uc_candidate_details();
				ucCandidate = (Uc_candidate_details) model.selectData("model.Uc_candidate_details", "cd_id=" + ja.getJa_candidate()).get(0);
				temp.append("<table>");
				temp.append("<tr><th>Name: </th><td>" + ucCandidate.getCd_name() + " (" + (ucCandidate.getCd_gender()==UCConstants.MALE ? "Male" : "Female") + ")</td></tr>");
				ucCity = new Uc_city();
				ucCity = (Uc_city) model.selectData("model.Uc_city", "c_id=" + ucCandidate.getCd_city_id()).get(0);
				ucTaluka = new Uc_taluka();
				ucTaluka = (Uc_taluka) model.selectData("model.Uc_taluka", "t_id=" + ucCity.getC_taluka_id()).get(0);
				ucDistrict = new Uc_district();
				ucDistrict = (Uc_district) model.selectData("model.Uc_district", "d_id=" + ucTaluka.getT_district_id()).get(0);
				ucState = new Uc_state();
				ucState = (Uc_state) model.selectData("model.Uc_state", "s_id=" + ucDistrict.getD_s_id()).get(0);
				temp.append("<tr><th>Location: </th><td>" + ucCity.getC_name() + ", " + ucTaluka.getT_name() + ", " + ucDistrict.getD_name() + ", " + ucState.getS_name() + "</td></tr>");
				temp.append("<tr><th>Expected Salary: </th><td>" + ucCandidate.getCd_expected_salary() + "</td></tr>");
				ucCandiQuali_list = new ArrayList<Uc_candidate_qualification>();
				ucCandiQuali_list = (List<Uc_candidate_qualification>)(Object) model.selectData("model.Uc_candidate_qualification", "cq_candidate_id=" + ucCandidate.getCd_id());
				temp.append("<tr><th>Qualification: </th>");
				if(ucCandiQuali_list.isEmpty()) {
					temp.append("<td>No qualification entered.</td>");
				} else {
					temp.append("<td>");
					String postfix = "";
					for(Uc_candidate_qualification cq : ucCandiQuali_list) {
						ucQualifications = new Uc_qualifications();
						ucQualifications = (Uc_qualifications) model.selectData("model.Uc_qualifications", "q_id=" + cq.getCq_quali_id()).get(0);
						temp.append(postfix);
						postfix = ", ";
						temp.append(ucQualifications.getQ_name());
					}
					temp.append("</td>");
				}
				temp.append("</tr>");
				if(ucCandidate.getCd_experience() == 1) {
					ucExperience_list = new ArrayList<Uc_experience>();
					ucExperience_list = (List<Uc_experience>)(Object) model.selectData("model.Uc_experience", "e_candidate_id=" + ucCandidate.getCd_id());
					StringBuilder tempExp = new StringBuilder();
					tempExp.append("<table class=\"table table-striped table-bordered\"><tr><th>Experience Industry</th><th>Position</th><th>Years</th><th>Is current job</th></tr>");
					for(Uc_experience exp : ucExperience_list) {
						tempExp.append("<tr>");
						ucIndustry = new Uc_industry_sector();
						ucIndustry = (Uc_industry_sector) model.selectData("model.Uc_industry_sector", "is_id=" + exp.getE_industry_id()).get(0);
						ucPosition = new Uc_position();
						ucPosition = (Uc_position) model.selectData("model.Uc_position", "p_id=" + exp.getE_position_id()).get(0);
						tempExp.append("<td>" + ucIndustry.getIs_name() + "</td>");
						tempExp.append("<td>" + ucPosition.getP_name() + "</td>");
						tempExp.append("<td>" + exp.getE_years() + "</td>");
						tempExp.append("<td>" + (exp.getE_is_current_job() == 1 ? "Yes" : "No") + "</td>");
						tempExp.append("</tr>");
					}
					tempExp.append("</table>");
					temp.append("<tr><td colspan=2>" + tempExp.toString() + "</td></tr>");
				} else {
					temp.append("<tr><th>Experience: </th><td>N/A</td></tr>");
				}
				StringBuilder tempInterest = new StringBuilder();
				ucInterest_list = new ArrayList<Uc_interest_areas>();
				ucInterest_list = (List<Uc_interest_areas>)(Object) model.selectData("model.Uc_interest_areas", "ia_candidate=" + ucCandidate.getCd_id());
				if(ucInterest_list.isEmpty()) {
					temp.append("<tr><th>Interest areas: </th><td>N/A</td></tr>");
				} else {
				tempInterest.append("<table class=\"table table-striped table-bordered\"><tr><th>Interest Industry</th><th>Position</th></tr>");
				for(Uc_interest_areas interest : ucInterest_list) {
					tempInterest.append("<tr>");
					ucIndustry = new Uc_industry_sector();
					ucIndustry = (Uc_industry_sector) model.selectData("model.Uc_industry_sector", "is_id=" + interest.getIa_industry()).get(0);
					ucPosition = new Uc_position();
					ucPosition = (Uc_position) model.selectData("model.Uc_position", "p_id=" + interest.getIa_position()).get(0);
					tempInterest.append("<td>" + ucIndustry.getIs_name() + "</td>");
					tempInterest.append("<td>" + ucPosition.getP_name() + "</td>");
					tempInterest.append("</tr>");
				}
				tempInterest.append("</table>");
				temp.append("<tr><td colspan=2>" + tempInterest + "</td></tr>");
				}
				
				temp.append("</table>");
				temp.append("</div>");
			}
			temp.append("</div></div>");
			response.setContentType("application/json");
			 JSONObject jsonResponse = new JSONObject();
			 jsonResponse.put("table", temp.toString());
			 if(begin) {
				 ucJobApp_list = new ArrayList<Uc_job_applications>();
					ucJobApp_list = (List<Uc_job_applications>)(Object) model.selectData("model.Uc_job_applications", where.toString());
				 int pageCount = (int) Math.ceil(ucCurrJobs_list.size()/(float)limit);
				 if(pageCount == 0) pageCount = 1;
				 jsonResponse.put("pageCount", pageCount);
			 }
				 response.getWriter().write(jsonResponse.toString());
		}
	}
	private void saveInterviewSchedule(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException, JSONException, ParseException {
		long jobId = Long.parseLong(request.getParameter("jobId").toString());
		String interviewDate = request.getParameter("interviewDate").toString();
		String interviewTime = request.getParameter("interviewTime").toString();
		String interviewContact = request.getParameter("interviewContact").toString();
		String interviewLocation = request.getParameter("interviewLocation").toString();
		interviewLocation = interviewLocation.replace("'", "\\'");
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
		ucInterviews_list = new ArrayList<Uc_interviews>();
		ucInterviews_list = (List<Uc_interviews>)(Object) model.selectData("model.Uc_interviews", "i_job_id=" + jobId);
			ucInterviews = new Uc_interviews();
			Date date = sdf.parse(interviewDate);
			ucInterviews.setI_date(new java.sql.Date(date.getTime()));
			ucInterviews.setI_job_id(jobId);
			ucInterviews.setI_location(interviewLocation);
			ucInterviews.setI_time(interviewTime);
			ucInterviews.setI_contact_person(interviewContact);
			response.setContentType("text/html;charset=UTF-8");
			if(ucInterviews_list.isEmpty()) {
				if(model.insertData(ucInterviews)) {
					//success
					response.getWriter().write("success");
				} else {
					//error
					response.getWriter().write("error");
				}
			} else {
				if(model.updateData(ucInterviews, "i_id=" + ucInterviews_list.get(0).getI_id())) {
					//success
					response.getWriter().write("success");
				} else {
					//error
					response.getWriter().write("error");
				}
			}
	}
	
	private void getSalesApprovedJobs(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException, JSONException {
		int pageNo = Integer.parseInt(request.getParameter("pageNo").toString());
		int limit = Integer.parseInt(request.getParameter("limit").toString());
		boolean begin = request.getParameter("begin").toLowerCase().equals("true") ? true : false;
		int start = (pageNo-1)*limit;
		int count = 0;
		boolean hasExtra = false;
		StringBuilder where = new StringBuilder();
		if(request.getParameter("isFiltered").toLowerCase().equals("true")) {
			String postfix = "(";
			String[] filters = request.getParameterValues("filters[]");
			for(String filter : filters) {
				where.append(postfix);
				postfix = " and ";
				if(filter.equals("1")) {
					where.append("cj_approved_by_telecaller_final=2");
				} else if(filter.equals("2")) {
					where.append("cj_approved_by_telecaller_final=1");
				} else if(filter.equals("3")) {
					where.append("cj_approved_by_telecaller_final=0");
				} 
			}
			where.append(")");
		} else {
			where.append("1");
		}
		
		if(request.getParameter("extraParameters") != "" && request.getParameter("extraParameters") == null) {
			String postfix = " and ";
			while(true) {
			
				if(request.getParameterValues("extraParameters[" + count + "][]") == null) break;
				hasExtra = true;
				String val1 = request.getParameterValues("extraParameters[" + count + "][]")[0];
				String val2 = request.getParameterValues("extraParameters[" + count + "][]")[1];
				count++;
			
				where.append(postfix);
				postfix = " and ";
				
				if(val1.equals("1")) {
					where.append("cj_id=" + Integer.parseInt(val2));
				} else if(val1.equals("2")) {
					where.append("cj_position=" + Integer.parseInt(val2));
				} 
			}
	}
		
		
		where.append(" and cj_approved_by_telecaller=1 and cj_is_active=1 and cj_approved_by_sales=1");
		ucCurrJobs_list = new ArrayList<Uc_current_jobs>();
		ucCurrJobs_list = (List<Uc_current_jobs>)(Object) model.selectData("model.Uc_current_jobs", where.toString() + " order by cj_id desc LIMIT " + start + "," + limit);
		if(ucCurrJobs_list.isEmpty()) {
			response.setContentType("application/json");
			JSONObject jsonResponse = new JSONObject();
			jsonResponse.put("table", "No job applications are submitted yet.");
			jsonResponse.put("pageCount", 0);
			response.getWriter().write(jsonResponse.toString());
		} else {
			StringBuilder temp = new StringBuilder();
			temp.append("<table class=\"table table-bordered\" width=\"100%\"><tr><th>Sr. No.</th><th>Job Id</th><th>Employer Name (Id)</th><th>Position</th><th>Number of applicants</th><th>Details</th><th>Accept</th><th>Reject</th></tr>");
			for(int i = 0 ; i < ucCurrJobs_list.size() ; i++) {
				ucCurrJob = new Uc_current_jobs();
				ucCurrJob = ucCurrJobs_list.get(i);
				String bgColor = "";
				if(ucCurrJob.getCj_approved_by_telecaller_final() == 2) bgColor = UCConstants.PENDING_APPLICATION;
				else if(ucCurrJob.getCj_approved_by_telecaller_final() == 1) bgColor = UCConstants.ACCEPTED_APPLICATION;
				else bgColor = UCConstants.REJECTED_APPLICATION;
				temp.append("<tr id=\"requirement_row" + ucCurrJob.getCj_id() + "\" style=\"background:" + bgColor + ";color:#fff\">");
				temp.append("<td>" + (i+1) + "</td>");
				temp.append("<td>" + ucCurrJob.getCj_id() + "</td>");
				
				ucEmployer = new Uc_employer_details();
				ucEmployer = (Uc_employer_details) model.selectData("model.Uc_employer_details", "ed_id=" + ucCurrJob.getCj_employer_id()).get(0);
				temp.append("<td>" + ucEmployer.getEd_firm_name() + " ("  + ucEmployer.getEd_id() + ")</td>");
				
				ucPosition = new Uc_position();
				ucPosition = (Uc_position) model.selectData("model.Uc_position", "p_id=" + ucCurrJob.getCj_position()).get(0);
				temp.append("<td>" + ucPosition.getP_name() + "</td>");
				
				ucJobApp_list = new ArrayList<Uc_job_applications>();
				ucJobApp_list = (List<Uc_job_applications>)(Object) model.selectData("model.Uc_job_applications", "ja_job=" + ucCurrJob.getCj_id() + " and ja_confirm_pre_final=1");
				temp.append("<td>" + ucJobApp_list.size() + "</td>");
				
				temp.append("<td><button type=\"button\" onclick=\"showDetails(this," + ucCurrJob.getCj_id() + ")\" class=\"btn btn-info\">Details</button></td>");
				temp.append("<td><button type=\"button\" onclick=\"acceptJob(this," + ucCurrJob.getCj_id() + ")\" class=\"btn btn-success\">Accept</button></td>");
				temp.append("<td><button type=\"button\" data-id=\"" + ucCurrJob.getCj_id() + "\" data-toggle=\"modal\" data-target=\"#rejectJobModal\" id=\"rejectJobButton\"  class=\"btn btn-danger\">Reject</button></td>");
				temp.append("</tr>");
			}
			temp.append("</table>");
			response.setContentType("application/json");
			 JSONObject jsonResponse = new JSONObject();
			 jsonResponse.put("table", temp.toString());
			 if(begin) {
				 ucCurrJobs_list = new ArrayList<Uc_current_jobs>();
				 ucCurrJobs_list = (List<Uc_current_jobs>)(Object) model.selectData("model.Uc_current_jobs", where.toString());
				 int pageCount = (int) Math.ceil(ucCurrJobs_list.size()/(float)limit);
				 if(pageCount == 0) pageCount = 1;
				 jsonResponse.put("pageCount", pageCount);
			 }
				 response.getWriter().write(jsonResponse.toString());
		}
	}
	private void showFinalDetails(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException, JSONException {
		int jobId = Integer.parseInt(request.getParameter("jobId").toString());
		Hashtable sessionAttributes = new Hashtable();
		sessionAttributes = (Hashtable) SessionManager.sessionExists(request, "admin");
		ucCurrJob = new Uc_current_jobs();
		ucCurrJob = (Uc_current_jobs) model.selectData("model.Uc_current_jobs", "cj_id=" + jobId).get(0);
		ucPosition = new Uc_position();
		ucPosition = (Uc_position) model.selectData("model.Uc_position", "p_id=" + ucCurrJob.getCj_position()).get(0);
		ucEmployer = (Uc_employer_details) model.selectData("model.Uc_employer_details", "ed_id=" + ucCurrJob.getCj_employer_id()).get(0);
		
		StringBuilder temp = new StringBuilder();
		temp.append("<table class=\"table table-bordered\">");
		temp.append("<tr><th>Employer: </th><td>" + ucEmployer.getEd_firm_name() + " (" + ucEmployer.getEd_id() + ")</td></tr>");
		temp.append("<tr><th>Position: </th><td>" + ucPosition.getP_name() + "</td></tr>");
		temp.append("<tr><th>Experience: </th><td>" + (ucCurrJob.getCj_experience_start()==ucCurrJob.getCj_experience_end() ? (ucCurrJob.getCj_experience_start() + " Years"):(ucCurrJob.getCj_experience_start() + "-" + ucCurrJob.getCj_experience_end() + " Years")) + "</td></tr>");
		temp.append("<tr><th>Duty hours: </th><td>" + ucCurrJob.getCj_duty_hours() + "</td></tr>");
		temp.append("<tr><th>Expected salary: </th><td>" + ucCurrJob.getCj_salary() + "</td></tr>");
		temp.append("<tr><th>Work profile: </th><td>" + ucCurrJob.getCj_work_profile() + "</td></tr>");
		if(Integer.parseInt(sessionAttributes.get("utype").toString()) == UCConstants.TELECALLER_ADMIN || Integer.parseInt(sessionAttributes.get("utype").toString()) == UCConstants.MAIN_ADMIN || Integer.parseInt(sessionAttributes.get("utype").toString()) == UCConstants.SUB_MAIN_ADMIN) {
			ucInterviews_list = new ArrayList<Uc_interviews>();
			ucInterviews_list = (List<Uc_interviews>)(Object) model.selectData("model.Uc_interviews", "i_job_id=" + ucCurrJob.getCj_id());
			if(!ucInterviews_list.isEmpty()) {
				ucInterviews = new Uc_interviews();
				ucInterviews = ucInterviews_list.get(0);
				temp.append("<tr style=\"background:" + UCConstants.ACCEPTED_APPLICATION + " !important;color:#fff\"><th>Interview Date: </th><td>" + ucInterviews.getI_date() + "</td></tr>");
				temp.append("<tr style=\"background:" + UCConstants.ACCEPTED_APPLICATION + ";color:#fff\"><th>Interview Time: </th><td>" + ucInterviews.getI_time() + "</td></tr>");
				temp.append("<tr style=\"background:" + UCConstants.ACCEPTED_APPLICATION + ";color:#fff\"><th>Interview Contact Person (optional): </th><td>" + (ucInterviews.getI_contact_person().toLowerCase().equals("n/a") ? ucEmployer.getEd_contact_person() : ucInterviews.getI_contact_person()) + "</td></tr>");
				temp.append("<tr style=\"background:" + UCConstants.ACCEPTED_APPLICATION + ";color:#fff\"><th>Interview Location Address: </th><td>" + (ucInterviews.getI_location().equals("same") ? ucEmployer.getEd_address() + "<br><b>(Employer default office address)</b>" : ucInterviews.getI_location()) + "</td></tr>");
			} else {
				temp.append("<tr><th></th><td>No interview schedule available. Please contact sales admin to update interview schedule for this job requirement.</td></tr>");
			}
		}
		temp.append("</table>");
		response.setContentType("application/json");
		 JSONObject jsonResponse = new JSONObject();
		 jsonResponse.put("table", temp.toString());
		 response.getWriter().write(jsonResponse.toString());
	}
	
	private void getFinalApplicationList(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException, JSONException {
		int pageNo = Integer.parseInt(request.getParameter("pageNo").toString());
		int limit = Integer.parseInt(request.getParameter("limit").toString());
		boolean begin = request.getParameter("begin").toLowerCase().equals("true") ? true : false;
		int start = (pageNo-1)*limit;
		StringBuilder where = new StringBuilder();
		if(request.getParameter("isFiltered").toLowerCase().equals("true")) {
			String postfix = "(";
			String[] filters = request.getParameterValues("filters[]");
			for(String filter : filters) {
				where.append(postfix);
				postfix = " and ";
				if(filter.equals("1")) {
					where.append("ja_confirm_final=2");
				} else if(filter.equals("2")) {
					where.append("ja_confirm_final=1");
				} else if(filter.equals("3")) {
					where.append("ja_confirm_final=0");
				} 
			}
			where.append(")");
		} else {
			where.append("1");
		}
		int jobId = Integer.parseInt(request.getParameterValues("extraParameters[0][]")[0].toString());
		where.append(" and ja_confirm_pre_final=1 and ja_job=" + jobId);
		ucJobApp_list = new ArrayList<Uc_job_applications>();
		ucJobApp_list = (List<Uc_job_applications>)(Object) model.selectData("model.Uc_job_applications", where.toString() + " order by ja_id desc LIMIT " + start + "," + limit);
		if(ucJobApp_list.isEmpty()) {
			response.setContentType("application/json");
			JSONObject jsonResponse = new JSONObject();
			jsonResponse.put("table", "No job applications found.");
			jsonResponse.put("pageCount", 0);
			response.getWriter().write(jsonResponse.toString());
		} else {
		StringBuilder temp = new StringBuilder();
		temp.append("<table class=\"table table-bordered\" width=\"100%\"><tr><th>Sr. No.</th><th>Application Id (Type)</th><th>Candidate Id</th><th>Candidate Name</th><th>Candidate contact no.</th><th>Applied at</th><th>Details</th><th>Accept</th><th>Reject</th></tr>");
		for(int i = 0 ; i < ucJobApp_list.size() ; i++) {
			ucJobApp = new Uc_job_applications();
			ucJobApp = ucJobApp_list.get(i);
			String bgColor = "#000";
			if(ucJobApp.getJa_confirm_final() == 1) bgColor = UCConstants.ACCEPTED_APPLICATION;
			else if(ucJobApp.getJa_confirm_final() == 2) bgColor = UCConstants.PENDING_APPLICATION;
			else bgColor = UCConstants.REJECTED_APPLICATION;
			temp.append("<tr id=\"application_row" + ucJobApp.getJa_id() + "\" style=\"color:#fff;background:" + bgColor + "\">");
			temp.append("<td>" + (i+1) + "</td>");
			temp.append("<td>" + ucJobApp.getJa_id() + " (" + (ucJobApp.getJa_source()==0 ? "Online" : "Short-listed") + ")</td>");
			temp.append("<td>" + ucJobApp.getJa_candidate() + "</td>");
			ucCandidate = new Uc_candidate_details();
			ucCandidate = (Uc_candidate_details) model.selectData("model.Uc_candidate_details", "cd_id=" + ucJobApp.getJa_candidate()).get(0);
			temp.append("<td>" + ucCandidate.getCd_name() + " (" + (ucCandidate.getCd_gender()==1?"Male":"Female") + ")</td>");
			temp.append("<td>" + ucCandidate.getCd_contact_num() + "</td>");
			ucCurrJob = new Uc_current_jobs();
			ucCurrJob = (Uc_current_jobs) model.selectData("model.Uc_current_jobs", "cj_id=" + ucJobApp.getJa_job()).get(0);
			temp.append("<td>" + ucJobApp.getJa_date() + " - " + ucJobApp.getJa_time() + "</td>");
			temp.append("<td><button type=\"button\" onclick=\"showCandidateDetails(this," + ucJobApp.getJa_candidate() + ", " + ucJobApp.getJa_id() + ")\" class=\"btn btn-warning\">Details</button></td>");
			temp.append("<td><button type=\"button\" onclick=\"acceptApplication(this," + ucJobApp.getJa_id() + ")\" class=\"btn btn-success\">Accept</button></td>");
			temp.append("<td><button type=\"button\" data-id=\"" + ucJobApp.getJa_id() + "\" data-toggle=\"modal\" data-target=\"#myModal\" id=\"rejectButton\" class=\"btn btn-danger\">Reject</button></td>");
			/*add reason in reject button*/
			temp.append("</tr>");
		}
		temp.append("</table>");
		response.setContentType("application/json");
		 JSONObject jsonResponse = new JSONObject();
		 jsonResponse.put("table", temp.toString());
		 if(begin) {
			 ucJobApp_list = new ArrayList<Uc_job_applications>();
			 ucJobApp_list = (List<Uc_job_applications>)(Object) model.selectData("model.Uc_job_applications", where.toString());
			 int pageCount =(int) Math.ceil(ucJobApp_list.size()/(float)limit);;
			 if(pageCount == 0) pageCount = 1;
			 jsonResponse.put("pageCount", pageCount);
		 }
		jsonResponse.put("id", jobId);
		response.getWriter().write(jsonResponse.toString());
		}
	}
	
	private void acceptApplicationFinal(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException, JSONException {
		int jobId = Integer.parseInt(request.getParameter("jobId").toString());
		ucJobApp = new Uc_job_applications();
		ucJobApp.setJa_confirm_final(1);
		ucJobApp.setJa_confirm_pre_final(1);
		response.setContentType("text/html;charset=UTF-8");
		if(model.updateData(ucJobApp, "ja_id=" + jobId)) {
			response.getWriter().write("success");
		} else {
			response.getWriter().write("error");
		}
	}
	
	private void rejectApplicationFinal(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException, JSONException {
		int jobId = Integer.parseInt(request.getParameter("jobId").toString());
		String reason = request.getParameter("reason").toString();
		ucJobApp = new Uc_job_applications();
		ucJobApp.setJa_confirm_final(0);
		//ucJobApp.setJa_confirm_pre_final(0);
		ucJobApp.setJa_reject_reason(reason);
		response.setContentType("text/html;charset=UTF-8");
		if(model.updateData(ucJobApp, "ja_id=" + jobId)) {
			response.getWriter().write("success");
		} else {
			response.getWriter().write("error");
		}
	}
	
	private void acceptJobTelecallerFinal(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		int jobId = Integer.parseInt(request.getParameter("jobId").toString());
		ucJobApp_list = new ArrayList<Uc_job_applications>();
		ucJobApp_list = (List<Uc_job_applications>)(Object) model.selectData("model.Uc_job_applications", "ja_job=" + jobId + " and ja_confirm_final=1");
		if(ucJobApp_list.isEmpty()) {
			response.setContentType("text/html;charset=UTF-8");
			response.getWriter().write("set");
		} else {
		ucCurrJob = new Uc_current_jobs();
		ucCurrJob.setCj_approved_by_telecaller_final(1);
		ucCurrJob.setCj_approved_by_sales(1);
		ucCurrJob.setCj_approved_by_telecaller(1);
		response.setContentType("text/html;charset=UTF-8");
		if(model.updateData(ucCurrJob, "cj_id=" + jobId)) {
			response.getWriter().write("success");
		} else {
			response.getWriter().write("error");
		}
		}
	}
	
	private void rejectJobTelecallerFinal(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		int jobId = Integer.parseInt(request.getParameter("jobId").toString());
		String reason = request.getParameter("reason").toString();
		ucCurrJob = new Uc_current_jobs();
		ucCurrJob.setCj_approved_by_telecaller_final(0);
		//ucCurrJob.setCj_approved_by_sales(0);
		//ucCurrJob.setCj_approved_by_telecaller(0);
		ucJobApps = new Uc_job_applications();
		//ucJobApps.setJa_confirm_final(0);
		//ucJobApps.setJa_confirm_pre_final(0);
		ucJobApps.setJa_reject_reason(reason);
		response.setContentType("text/html;charset=UTF-8");
		if(model.updateData(ucCurrJob, "cj_id=" + jobId)) {
			if(model.updateData(ucJobApps, "ja_job=" + jobId)) {
				response.getWriter().write("success");
			} else {
				response.getWriter().write("error");
			}
		} else {
			response.getWriter().write("error");
		}
	}
	
	private void getPositions(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		int criteriaId = Integer.parseInt(request.getParameter("criteriaId").toString());
		ucPosition_list = new ArrayList<Uc_position>();
		ucPosition_list = (List<Uc_position>)(Object) model.selectData("model.Uc_position", "1 order by p_name");
		StringBuilder temp = new StringBuilder();
		temp.append("<select name=\"position" + criteriaId + "\" class=\"form-control\" id=\"position"+criteriaId+"\">");
		temp.append("<option selected hidden disabled value=\"select\">Select position</option>");
		for(Uc_position position : ucPosition_list) {
			temp.append("<option value=\""+ position.getP_id() +"\">"+ position.getP_name() +"</option>");
		}
		temp.append("</select>");
		response.setContentType("text/html;charset=UTF-8");
		response.getWriter().write(temp.toString());
	}
	
	private void searchRequirements(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException, JSONException {
		int pageNo = Integer.parseInt(request.getParameter("pageNo").toString());
		int limit = Integer.parseInt(request.getParameter("limit").toString());
		boolean begin = request.getParameter("begin").toLowerCase().equals("true") ? true : false;
		int start = (pageNo-1)*limit;
		boolean hasExtra = false;
		int count = 0;
		
		StringBuilder where = new StringBuilder();
			where.append("1");
		
		
		if(request.getParameter("extraParameters") != "" && request.getParameter("extraParameters") == null) {
				String postfix = " and ";
				while(true) {
				
					if(request.getParameterValues("extraParameters[" + count + "][]") == null) break;
					hasExtra = true;
					String val1 = request.getParameterValues("extraParameters[" + count + "][]")[0];
					String val2 = request.getParameterValues("extraParameters[" + count + "][]")[1];
					count++;
				
					where.append(postfix);
					postfix = " and ";
					
					if(val1.equals("1")) {
						where.append("cj_id=" + Integer.parseInt(val2));
					} else if(val1.equals("2")) {
						where.append("cj_position=" + Integer.parseInt(val2));
					} 
				}
		}
		
		where.append(" and cj_is_active=1");
		
		ucCurrJobs_list = new ArrayList<Uc_current_jobs>();
		ucCurrJobs_list = (List<Uc_current_jobs>)(Object) model.selectData("model.Uc_current_jobs", where.toString() + " order by cj_id desc LIMIT " + start + "," + limit);
		if(ucCurrJobs_list.isEmpty()) {
			response.setContentType("application/json");
			JSONObject jsonResponse = new JSONObject();
			jsonResponse.put("table", "No job applications are submitted yet.");
			jsonResponse.put("pageCount", 0);
			response.getWriter().write(jsonResponse.toString());
		} else {
			StringBuilder temp = new StringBuilder();
			temp.append("<table class=\"table table-striped table-bordered\" width=\"100%\"><tr><th>Sr. No.</th><th>Job Id</th><th>Employer Name (Id)</th><th>Position</th><th>Number of applicants</th><th>Details</th><th>Short</th></tr>");
			for(int i = 0 ; i < ucCurrJobs_list.size() ; i++) {
				ucCurrJob = new Uc_current_jobs();
				ucCurrJob = ucCurrJobs_list.get(i);
				temp.append("<tr id=\"requirement_row" + ucCurrJob.getCj_id() + "\">");
				temp.append("<td><input type=\"radio\" name=\"requirement_check\" value=\"" + ucCurrJob.getCj_id() + "\"> &nbsp;" + (i+1) + "</td>");
				temp.append("<td>" + ucCurrJob.getCj_id() + "</td>");
				
				ucEmployer = new Uc_employer_details();
				ucEmployer = (Uc_employer_details) model.selectData("model.Uc_employer_details", "ed_id=" + ucCurrJob.getCj_employer_id()).get(0);
				temp.append("<td>" + ucEmployer.getEd_firm_name() + " ("  + ucEmployer.getEd_id() + ")</td>");
				
				ucPosition = new Uc_position();
				ucPosition = (Uc_position) model.selectData("model.Uc_position", "p_id=" + ucCurrJob.getCj_position()).get(0);
				temp.append("<td>" + ucPosition.getP_name() + "</td>");
				
				ucJobApp_list = new ArrayList<Uc_job_applications>();
				ucJobApp_list = (List<Uc_job_applications>)(Object) model.selectData("model.Uc_job_applications", "ja_job=" + ucCurrJob.getCj_id());
				temp.append("<td>" + ucJobApp_list.size() + "</td>");
				
				temp.append("<td><button type=\"button\" onclick=\"showDetails(this," + ucCurrJob.getCj_id() + ")\" class=\"btn btn-info\">Details</button></td>");
				temp.append("<td><button type=\"button\" onclick=\"shortlist(this," + ucCurrJob.getCj_id() + ")\" class=\"btn btn-success\">Auto shortlist</button></td>");
				temp.append("</tr>");
			}
			temp.append("</table>");
			response.setContentType("application/json");
			 JSONObject jsonResponse = new JSONObject();
			 jsonResponse.put("table", temp.toString());
			 if(begin) {
				 ucCurrJobs_list = new ArrayList<Uc_current_jobs>();
				 ucCurrJobs_list = (List<Uc_current_jobs>)(Object) model.selectData("model.Uc_current_jobs", where.toString());
				 int pageCount = (int) Math.ceil(ucCurrJobs_list.size()/(float)limit);;
				 if(pageCount == 0) pageCount = 1;
				 jsonResponse.put("pageCount", pageCount);
			 }
				 response.getWriter().write(jsonResponse.toString());
		}
	}
	private void saveShortlist(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException, JSONException {
		int count = 0;
		java.sql.Date date = new java.sql.Date(Calendar.getInstance().getTime().getTime());
		String time = new SimpleDateFormat("HH:mm:ss").format(new Date());
		boolean flag = true;
		while(true) {
			if(request.getParameterValues("shortlist[" + count + "][]") == null) break;
			long reqId = Long.parseLong(request.getParameterValues("shortlist[" + count + "][]")[0].toString());
			long candidateId = Long.parseLong(request.getParameterValues("shortlist[" + count + "][]")[1].toString());
			count++;
			ucJobApp = new Uc_job_applications();
			ucJobApp.setJa_candidate(candidateId);
			ucJobApp.setJa_date(date);
			ucJobApp.setJa_job(reqId);
			ucJobApp.setJa_source(1);
			ucJobApp.setJa_time(time);
			if(!model.insertData(ucJobApp)) {
				flag = false;
				break;
			} else {
				ucCurrJob = new Uc_current_jobs();
				ucCurrJob = (Uc_current_jobs) model.selectData("model.Uc_current_jobs", "cj_id=" + reqId).get(0);
				int tempNumber = ucCurrJob.getCj_number_of_new_applicant();
				ucCurrJob = new Uc_current_jobs();
				ucCurrJob.setCj_has_applicant(1);
				ucCurrJob.setCj_number_of_new_applicant(tempNumber+1);
				if(!model.updateData(ucCurrJob, "cj_id=" + reqId)) {
					flag = false;
					break;
				}
			}
		}
		response.setContentType("text/html;charset=UTF-8");
		if(flag) {
			response.getWriter().write("success");
		} else {
			response.getWriter().write("error");
		}
	}
	
	private void getApplicationListForShortlist(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException, JSONException {
		int pageNo = Integer.parseInt(request.getParameter("pageNo").toString());
		int limit = Integer.parseInt(request.getParameter("limit").toString());
		boolean begin = request.getParameter("begin").toLowerCase().equals("true") ? true : false;
		int start = (pageNo-1)*limit;
		StringBuilder where = new StringBuilder();
	
			where.append("1");
			
		int jobId = Integer.parseInt(request.getParameterValues("extraParameters[0][]")[0].toString());
		where.append(" and ja_job=" + jobId);
		ucJobApp_list = new ArrayList<Uc_job_applications>();
		ucJobApp_list = (List<Uc_job_applications>)(Object) model.selectData("model.Uc_job_applications", where.toString() + " order by ja_id desc LIMIT " + start + "," + limit);
		if(ucJobApp_list.isEmpty()) {
			response.setContentType("application/json");
			JSONObject jsonResponse = new JSONObject();
			jsonResponse.put("table", "No job applications found.");
			jsonResponse.put("pageCount", 0);
			response.getWriter().write(jsonResponse.toString());
		} else {
		StringBuilder temp = new StringBuilder();
		temp.append("<table class=\"table table-bordered table-striped\" width=\"100%\"><tr><th>Sr. No.</th><th>Application Id (Type)</th><th>Candidate Id</th><th>Candidate Name</th><th>Applied at</th></tr>");
		for(int i = 0 ; i < ucJobApp_list.size() ; i++) {
			ucJobApp = new Uc_job_applications();
			ucJobApp = ucJobApp_list.get(i);
			temp.append("<tr id=\"application_row" + ucJobApp.getJa_id() + "\">");
			temp.append("<td>" + (i+1) + "</td>");
			temp.append("<td>" + ucJobApp.getJa_id() + " (" + (ucJobApp.getJa_source()==0 ? "Online" : "Short-listed") + ")</td>");
			temp.append("<td>" + ucJobApp.getJa_candidate() + "</td>");
			ucCandidate = new Uc_candidate_details();
			ucCandidate = (Uc_candidate_details) model.selectData("model.Uc_candidate_details", "cd_id=" + ucJobApp.getJa_candidate()).get(0);
			temp.append("<td>" + ucCandidate.getCd_name() + " (" + (ucCandidate.getCd_gender()==1?"Male":"Female") + ")</td>");
			ucCurrJob = new Uc_current_jobs();
			ucCurrJob = (Uc_current_jobs) model.selectData("model.Uc_current_jobs", "cj_id=" + ucJobApp.getJa_job()).get(0);
			temp.append("<td>" + ucJobApp.getJa_date() + " - " + ucJobApp.getJa_time() + "</td>");
			
			
			temp.append("</tr>");
		}
		temp.append("</table>");
		response.setContentType("application/json");
		 JSONObject jsonResponse = new JSONObject();
		 jsonResponse.put("table", temp.toString());
		 if(begin) {
			 ucJobApp_list = new ArrayList<Uc_job_applications>();
			 ucJobApp_list = (List<Uc_job_applications>)(Object) model.selectData("model.Uc_job_applications", where.toString());
			 int pageCount = (int) Math.ceil(ucJobApp_list.size()/(float)limit);;
			 if(pageCount == 0) pageCount = 1;
			 jsonResponse.put("pageCount", pageCount);
		 }
		jsonResponse.put("id", jobId);
		response.getWriter().write(jsonResponse.toString());
		}
	}
	
	private void saveExperienceYears(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException, JSONException {
		int expId = Integer.parseInt(request.getParameter("expId").toString());
		int years = Integer.parseInt(request.getParameter("years").toString());
		int candidateId = Integer.parseInt(request.getParameter("candidateId").toString());
		
		ucExperience = new Uc_experience();
		ucExperience.setE_is_current_job(0);
		if(!model.updateData(ucExperience, "e_candidate_id=" + candidateId)) {
			response.setContentType("text/html;charset=UTF-8");
			response.getWriter().write("error");
		} else {
			ucExperience = new Uc_experience();
			ucExperience.setE_years(years);
			ucExperience.setE_is_current_job(1);
			if(model.updateData(ucExperience, "e_id=" + expId)) {
				response.setContentType("text/html;charset=UTF-8");
				response.getWriter().write("success");
			} else {
				response.setContentType("text/html;charset=UTF-8");
				response.getWriter().write("error");
			}
		}
	}
	
	private void saveExpectedSalary(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException, JSONException {
		int candidateId = Integer.parseInt(request.getParameter("candidateId").toString());
		String salary = request.getParameter("salary").toString();
		ucCandidate = new Uc_candidate_details();
		ucCandidate.setCd_expected_salary(salary);
		if(model.updateData(ucCandidate, "cd_id=" + candidateId)) {
			response.setContentType("text/html;charset=UTF-8");
			response.getWriter().write("success");
		} else {
			response.setContentType("text/html;charset=UTF-8");
			response.getWriter().write("error");
		}
	}
	
	private void saveSalary(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException, JSONException {
		int expId = Integer.parseInt(request.getParameter("expId").toString());
		int salary = Integer.parseInt(request.getParameter("salary").toString());
		int candidateId = Integer.parseInt(request.getParameter("candidateId").toString());
		
		ucExperience = new Uc_experience();
		ucExperience.setE_is_current_job(0);
		if(!model.updateData(ucExperience, "e_candidate_id=" + candidateId)) {
			response.setContentType("text/html;charset=UTF-8");
			response.getWriter().write("error");
		} else {
			ucExperience = new Uc_experience();
			ucExperience.setE_is_current_job(1);
			ucExperience.setE_salary_per_month(salary);
			if(model.updateData(ucExperience, "e_id=" + expId)) {
				response.setContentType("text/html;charset=UTF-8");
				response.getWriter().write("success");
			} else {
				response.setContentType("text/html;charset=UTF-8");
				response.getWriter().write("error");
			}
		}
	}
	
	private void deleteApplications(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException, JSONException {
		JSONArray json = new JSONArray(request.getParameter("applicationIds").toString());
		boolean flag = true;
		for(int i=0 ; i<json.length() ; i++) {
			if(!model.deleteData("model.Uc_job_applications", "ja_id=" + json.getInt(i))) {
				flag = false;
			}
		}
		if(flag) {
			response.setContentType("text/html;charset=UTF-8");
			response.getWriter().write("success");
		} else {
			response.setContentType("text/html;charset=UTF-8");
			response.getWriter().write("error");
		} 
	}
	
	private void searchCandidateAppliedJobs(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException, JSONException {
		int pageNo = Integer.parseInt(request.getParameter("pageNo").toString());
		int limit = Integer.parseInt(request.getParameter("limit").toString());
		boolean begin = request.getParameter("begin").toLowerCase().equals("true") ? true : false;
		int start = (pageNo-1)*limit;
		StringBuilder where = new StringBuilder();
	
			where.append("1");
			
		int candidateId = Integer.parseInt(request.getParameterValues("extraParameters[0][]")[0].toString());
		where.append(" and ja_candidate=" + candidateId);
		ucJobApp_list = new ArrayList<Uc_job_applications>();
		ucJobApp_list = (List<Uc_job_applications>)(Object) model.selectData("model.Uc_job_applications", where.toString() + " order by ja_id desc LIMIT " + start + "," + limit);
		if(ucJobApp_list.isEmpty()) {
			response.setContentType("application/json");
			JSONObject jsonResponse = new JSONObject();
			jsonResponse.put("table", "No applications found for entered candidate.");
			jsonResponse.put("pageCount", 0);
			response.getWriter().write(jsonResponse.toString());
		} else {
			StringBuilder temp = new StringBuilder();
			temp.append("<table class=\"table table-bordered table-striped\"><tr><th>Sr. No.</th><th>Application Id</th><th>Job Id</th><th>Applied At</th><th>Online/Short-listed</th></tr>");
			for(int i = 0 ; i < ucJobApp_list.size() ; i++) {
				ucJobApp = new Uc_job_applications();
				ucJobApp = ucJobApp_list.get(i);
				if(ucJobApp.getJa_confirm_pre_final() == 2) {
					//pending
					temp.append("<tr style=\"color:#fff;background:" + UCConstants.PENDING_APPLICATION + "\">");
				} else if(ucJobApp.getJa_confirm_pre_final() == 1) {
					//accepted
					temp.append("<tr style=\"color:#fff;background:" + UCConstants.ACCEPTED_APPLICATION + "\">");
				} else {
					//rejected
					temp.append("<tr style=\"color:#fff;background:" + UCConstants.REJECTED_APPLICATION + "\">");
				}
				temp.append("<td>" + (i+1) + "</td>");
				temp.append("<td>" + ucJobApp.getJa_id() + "</td>");
				temp.append("<td>" + ucJobApp.getJa_job() + "</td>");
				temp.append("<td>" + ucJobApp.getJa_date() + "   " + ucJobApp.getJa_time() + "</td>");
				temp.append("<td>" + (ucJobApp.getJa_source()==0 ? "Online" : "Short-listed") + "</td>");
				temp.append("</tr>");
			}
			temp.append("</table>");
			response.setContentType("application/json");
			 JSONObject jsonResponse = new JSONObject();
			 jsonResponse.put("table", temp.toString());
			 if(begin) {
				 ucJobApp_list = new ArrayList<Uc_job_applications>();
				 ucJobApp_list = (List<Uc_job_applications>)(Object) model.selectData("model.Uc_job_applications", where.toString());
				 int pageCount = (int) Math.ceil(ucJobApp_list.size()/(float)limit);;
				 if(pageCount == 0) pageCount = 1;
				 jsonResponse.put("pageCount", pageCount);
			 }
			response.getWriter().write(jsonResponse.toString());
		}
	}
	
	private void saveCandidateQualification(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException, JSONException {
		long qualiId = 0;
		boolean flag = false;
		String qualification = request.getParameter("qualification").toString();
		long candiQualiId = Long.parseLong(request.getParameter("candiQualiId").toString());
		ucQuali_list = new ArrayList<Uc_qualifications>();
		ucQuali_list = (List<Uc_qualifications>)(Object) model.selectData("model.Uc_qualifications", "q_name='" + qualification + "' LIMIT 1");
		if(ucQuali_list.isEmpty()) {
			ucQualifications = new Uc_qualifications();
			ucQualifications.setQ_name(qualification);
			List<Long> qualikeys = model.insertDataReturnGeneratedKeys(ucQualifications);
			if(qualikeys != null) {
				qualiId = Long.parseLong(qualikeys.get(0).toString());
				flag = true;
			}
		} else {
			qualiId = ucQuali_list.get(0).getQ_id();
			flag = true;
		}
		if(flag && qualiId != 0) {
			ucCandiQuali = new Uc_candidate_qualification();
			ucCandiQuali.setCq_quali_id(qualiId);
			if(!model.updateData(ucCandiQuali, "cq_id=" + candiQualiId)) {
				flag = false;
			}
		}
		if(flag) {
			response.setContentType("text/html;charset=UTF-8");
			response.getWriter().write("success");
		} else {
			// error
			response.setContentType("text/html;charset=UTF-8");
			response.getWriter().write("error");
		}
	}
}
