package controller;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Hashtable;
import java.util.List;

import javax.servlet.ServletException;
import javax.servlet.annotation.MultipartConfig;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.Part;

import database.Model;
import model.Uc_candidate_details;
import model.Uc_candidate_qualification;
import model.Uc_city;
import model.Uc_district;
import model.Uc_experience;
import model.Uc_industry_sector;
import model.Uc_interest_areas;
import model.Uc_position;
import model.Uc_qualifications;
import model.Uc_state;
import model.Uc_taluka;
import utils.EncryptionManager;
import utils.FileManager;
import utils.RouteManager;
import utils.SessionManager;
import utils.ViewManager;

@WebServlet("/profile")
@MultipartConfig(fileSizeThreshold=1024*1024*10, // 10MB
maxFileSize=1024*1024*20,      // 20MB
maxRequestSize=1024*1024*50)   // 50MB
public class UserProfile extends HttpServlet {
	private static final long serialVersionUID = 1L;
       
	private List<Uc_state> ucState_list;
	private Model model;
	private Uc_candidate_details ucCandidate;
	private Uc_city ucCity;
	private Uc_district ucDistrict;
	private Uc_state ucState;
	private List<Uc_candidate_qualification> ucCandidateQuali_list;
	private List<Uc_interest_areas> ucInterest_list;
	private List<Uc_experience> ucExperience_list;
	private Uc_industry_sector ucIndustry;
	private Uc_position ucPosition;
	private Uc_taluka ucTaluka;
	private List<Uc_candidate_details> ucCandidate_list;
	private List<Uc_qualifications> ucQuali_list;
	private Uc_qualifications ucQuali;
	private Uc_candidate_qualification ucCandiQuali;
	private Uc_experience ucExperience;
	private Uc_interest_areas ucInterest;
	private FileManager fileManager;
    public UserProfile() {
        super();
        model = new Model();
    }

	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		if(request.getParameter("action") == null || request.getParameter("action").toString().equals("")) {
			showProfile(request,response);
		} else {
			 if(request.getParameter("action").toString().toLowerCase().equals("change_password")) {
					ViewManager.showView(request, response, "changepassword.jsp");
				} else if(request.getParameter("action").toString().toLowerCase().equals("save_password")) {
					savePassword(request, response);
				}  else if(request.getParameter("action").toString().toLowerCase().equals("savepersonalprofile")) {
					savePersonalProfile(request,response);
				} else if(request.getParameter("action").toString().toLowerCase().equals("savequalifications")) {
					saveQualifications(request,response);
				} else if(request.getParameter("action").toString().toLowerCase().equals("saveinterests")) {
					saveInterests(request,response);
				} else if(request.getParameter("action").toString().toLowerCase().equals("download")) {
					downloadResume(request,response);
				} else if(request.getParameter("action").toString().toLowerCase().equals("saveresume")) {
					saveResume(request,response);
				} 
		}
	}

	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		doGet(request, response);
	}
	
	private void showProfile(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		Hashtable sessionAttributes = new Hashtable();
		sessionAttributes = (Hashtable) SessionManager.sessionExists(request, "uc_client_session");
		ucState_list = (List<Uc_state>)(Object) model.selectData("model.Uc_state", "1");
		request.setAttribute("states", ucState_list);
		
		ucCandidate = new Uc_candidate_details();
		ucCandidate = (Uc_candidate_details) model.selectData("model.Uc_candidate_details", "cd_id=" + sessionAttributes.get("id")).get(0);
		request.setAttribute("candidateDetails", ucCandidate);
		
		ucCity = new Uc_city();
		ucCity = (Uc_city) model.selectData("model.Uc_city", "c_id=" + ucCandidate.getCd_city_id()).get(0);
		ucTaluka = new Uc_taluka();
		ucTaluka = (Uc_taluka) model.selectData("model.Uc_taluka", "t_id=" + ucCity.getC_taluka_id()).get(0);
		ucDistrict = new Uc_district();
		ucDistrict = (Uc_district) model.selectData("model.Uc_district", "d_id=" + ucTaluka.getT_district_id()).get(0);
		ucState = new Uc_state();
		ucState = (Uc_state) model.selectData("model.Uc_state", "s_id=" + ucDistrict.getD_s_id()).get(0);
		String location = ucCity.getC_name() + ", " + ucTaluka.getT_name() + ", " + ucDistrict.getD_name() + ", " + ucState.getS_name();
		request.setAttribute("location",location);
		
		ucCandidateQuali_list = new ArrayList<Uc_candidate_qualification>();
		ucCandidateQuali_list = (List<Uc_candidate_qualification>)(Object) model.selectData("model.Uc_candidate_qualification", "cq_candidate_id=" + sessionAttributes.get("id"));
		List<String> candidateQuali = new ArrayList<String>();
		for(Uc_candidate_qualification cq : ucCandidateQuali_list) {
			Uc_qualifications ucQualification = new Uc_qualifications();
			ucQualification = (Uc_qualifications) model.selectData("model.Uc_qualifications", "q_id=" + cq.getCq_quali_id()).get(0);
			candidateQuali.add(ucQualification.getQ_name());
		}
		request.setAttribute("candidateQualifications", candidateQuali);
		request.setAttribute("qualificationCount", ucCandidateQuali_list.size());
		ucExperience_list = new ArrayList<Uc_experience>();
		ucExperience_list = (List<Uc_experience>)(Object) model.selectData("model.Uc_experience", "e_candidate_id=" + sessionAttributes.get("id"));
		StringBuilder experienceString = new StringBuilder();
		if(ucExperience_list.isEmpty()) {
			request.setAttribute("experienceAvail", "0");
			experienceString.append("No experience data added.");
		} else {
		for(int i=1 ; i<=ucExperience_list.size() ; i++) {
			ucIndustry = new Uc_industry_sector();
			ucPosition = new Uc_position();
			ucIndustry = (Uc_industry_sector) model.selectData("model.Uc_industry_sector", "is_id=" + ucExperience_list.get(i-1).getE_industry_id()).get(0);
			ucPosition = (Uc_position) model.selectData("model.Uc_position", "p_id=" + ucExperience_list.get(i-1).getE_position_id()).get(0);
			if(ucExperience_list.get(i-1).getE_is_current_job() == 1) {
				experienceString.append("<div class=\"experience_y_inner form-group col-lg-12\" id=\"exp"+i+"\"><div class=\"form-group col-lg-12\"><a title=\"Remove\" style=\"float:right;cursor:pointer\" onclick=\"remove_experience("+i+")\"><i class=\"fa fa-close\" style=\"color:#790000\"></i></a></div><div class=\"form-group col-lg-6\"><input type=\"text\" class=\"form-control\" value=\"" + ucIndustry.getIs_name() + "\" name=\"user_industry_sector"+i+"\" id=\"user_industry_sector"+i+"\" onkeyup=\"disableError('user_industry_sector"+i+"')\" placeholder=\"* Industry sector\"><label style=\"color:red;font-weight:normal;display:none\" id=\"user_industry_sector"+i+"_error\"></label></div><div class=\"form-group col-lg-6\"><input type=\"text\" value=\"" + ucPosition.getP_name() + "\" name=\"user_post"+i+"\" id=\"user_post"+i+"\" onkeyup=\"disableError('user_post"+i+"')\" placeholder=\"* Postion or post\" class=\"form-control\"><label style=\"color:red;font-weight:normal;display:none\" id=\"user_post"+i+"_error\"></label></div><div class=\"form-group col-lg-6\"><input type=\"number\" value=\"" + ucExperience_list.get(i-1).getE_years() + "\" name=\"user_year_exp"+i+"\" id=\"user_year_exp"+i+"\" onkeyup=\"disableError('user_year_exp"+i+"')\" class=\"form-control\" placeholder=\"* Years of experience\"><label style=\"color:red;font-weight:normal;display:none\" id=\"user_year_exp"+i+"_error\"></label></div><div class=\"form-group col-lg-6\"><input type=\"number\" name=\"user_exp_salary'+count+'\" id=\"user_exp_salary"+i+"\" class=\"form-control\" value=\"" + (ucExperience_list.get(i-1).getE_salary_per_month() == 0 ? "" : ucExperience_list.get(i-1).getE_salary_per_month()) + "\" placeholder=\"Salary (optional)\"></div><div class=\"form-group col-lg-6\"><label><input type=\"checkbox\" name=\"current_job\" onchange=\"currentChanged(this)\" value=\"current_job"+i+"\" checked> Current job</label></div></div>");
			} else {
				experienceString.append("<div class=\"experience_y_inner form-group col-lg-12\" id=\"exp"+i+"\"><div class=\"form-group col-lg-12\"><a title=\"Remove\" style=\"float:right;cursor:pointer\" onclick=\"remove_experience("+i+")\"><i class=\"fa fa-close\" style=\"color:#790000\"></i></a></div><div class=\"form-group col-lg-6\"><input type=\"text\" class=\"form-control\" value=\"" + ucIndustry.getIs_name() + "\" name=\"user_industry_sector"+i+"\" id=\"user_industry_sector"+i+"\" onkeyup=\"disableError('user_industry_sector"+i+"')\" placeholder=\"* Industry sector\"><label style=\"color:red;font-weight:normal;display:none\" id=\"user_industry_sector"+i+"_error\"></label></div><div class=\"form-group col-lg-6\"><input type=\"text\" value=\"" + ucPosition.getP_name() + "\" name=\"user_post"+i+"\" id=\"user_post"+i+"\" onkeyup=\"disableError('user_post"+i+"')\" placeholder=\"* Postion or post\" class=\"form-control\"><label style=\"color:red;font-weight:normal;display:none\" id=\"user_post"+i+"_error\"></label></div><div class=\"form-group col-lg-6\"><input type=\"number\" value=\"" + ucExperience_list.get(i-1).getE_years() + "\" name=\"user_year_exp"+i+"\" id=\"user_year_exp"+i+"\" onkeyup=\"disableError('user_year_exp"+i+"')\" class=\"form-control\" placeholder=\"* Years of experience\"><label style=\"color:red;font-weight:normal;display:none\" id=\"user_year_exp"+i+"_error\"></label></div><div class=\"form-group col-lg-6\"><input type=\"number\" name=\"user_exp_salary'+count+'\" id=\"user_exp_salary"+i+"\" class=\"form-control\" value=\"" + (ucExperience_list.get(i-1).getE_salary_per_month() == 0 ? "" : ucExperience_list.get(i-1).getE_salary_per_month()) + "\" placeholder=\"Salary (optional)\"></div><div class=\"form-group col-lg-6\"><label><input type=\"checkbox\" name=\"current_job\" onchange=\"currentChanged(this)\" value=\"current_job"+i+"\"> Current job</label></div></div>");
			}
		}
		request.setAttribute("experienceAvail", "1");
		}
		request.setAttribute("experienceData", experienceString);
		request.setAttribute("experienceCount", ucExperience_list.size());
		
		ucInterest_list = new ArrayList<Uc_interest_areas>();
		ucInterest_list = (List<Uc_interest_areas>)(Object) model.selectData("model.Uc_interest_areas", "ia_candidate=" + sessionAttributes.get("id"));
		StringBuilder interestString = new StringBuilder();
		for(int i=1 ; i<=ucInterest_list.size() ; i++) {
			ucIndustry = new Uc_industry_sector();
			ucPosition = new Uc_position();
			ucIndustry = (Uc_industry_sector) model.selectData("model.Uc_industry_sector", "is_id=" + ucInterest_list.get(i-1).getIa_industry()).get(0);
			ucPosition = (Uc_position) model.selectData("model.Uc_position", "p_id=" + ucInterest_list.get(i-1).getIa_position()).get(0);
			interestString.append("<div class=\"single-tag\" id=\"tag_count"+i+"\"> &nbsp;<a onclick=\"remove_tag("+i+")\" style=\"cursor:pointer\"><i class=\"fa fa-close\" style=\"color: #790000\"></i></a> &nbsp;<label>"+ ucIndustry.getIs_name() +"</label><input type=\"hidden\" id=\"interest-industry"+i+"\" name=\"interest-industry"+i+"\" value=\""+ ucIndustry.getIs_name() +"\"> - <label>"+ ucPosition.getP_name() +"</label><input type=\"hidden\" id=\"interest-post"+i+"\" name=\"interest-post"+i+"\" value=\""+ ucPosition.getP_name() +"\"></div>");
		}
		String downloadLink = RouteManager.getBasePath() + "profile?action=download&file=" + ucCandidate.getCd_resume();
		request.setAttribute("resume", downloadLink);
		request.setAttribute("interestAreas", interestString);
		request.setAttribute("interestCount", ucInterest_list.size());
		
		ViewManager.showView(request, response, "userprofile.jsp");
	}
	
	private void savePersonalProfile(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		Hashtable sessionAttributes = new Hashtable();
		sessionAttributes = (Hashtable) SessionManager.sessionExists(request, "uc_client_session");
		String name = request.getParameter("user_name").toString();
		name = name.replace("'", "\\'");
		String email = request.getParameter("user_email").toString();
		int gender = Integer.parseInt(request.getParameter("user_gender").toString());
		int birthyear = request.getParameter("user_birthyear").toString().equals("") ? 0 : Integer.parseInt(request.getParameter("user_birthyear").toString());
		long city = 0;
		if(!request.getParameter("user_city").toString().equals("N/A")) {
			if(request.getParameter("user_state").toString().toLowerCase().equals("other")) {
				CommonHelper ch = new CommonHelper();
				city = ch.saveOtherCity(request.getParameter("user_district").toString(), request.getParameter("user_taluka").toString(), request.getParameter("user_city").toString());
			} else {
				city = Integer.parseInt(request.getParameter("user_city").toString());
			}
		}
		Long secondaryContact = request.getParameter("user_secondarycontactno").toString().equals("") ? 0 : Long.parseLong(request.getParameter("user_secondarycontactno").toString());
		ucCandidate = new Uc_candidate_details();
		if(!name.equals("")) ucCandidate.setCd_name(name);
		if(!email.equals("")) ucCandidate.setCd_email(email);
		ucCandidate.setCd_gender(gender);
		if(birthyear != 0) ucCandidate.setCd_birthyear(birthyear);
		if(city != 0 && city != -1) ucCandidate.setCd_city_id(city);
		ucCandidate.setCd_contact_num_secondary(secondaryContact);
		response.setContentType("text/html;charset=UTF-8");
		
		if(!email.equals("")) {
			ucCandidate_list = new ArrayList<Uc_candidate_details>();
			ucCandidate_list = (List<Uc_candidate_details>)(Object) model.selectData("model.Uc_candidate_details", "cd_id!=" + sessionAttributes.get("id") + " and cd_email=BINARY'" + email +"'");
			if(!ucCandidate_list.isEmpty()) {
				response.getWriter().write("email_error");
			} else {
				if(model.updateData(ucCandidate, "cd_id=" + sessionAttributes.get("id"))) {
					response.getWriter().write("success");
				} else {
					response.getWriter().write("error");
				}
			}
		} else {
			if(model.updateData(ucCandidate, "cd_id=" + sessionAttributes.get("id"))) {
				response.getWriter().write("success");
			} else {
				response.getWriter().write("error");
			}
		}
		
	}
	
	private void saveQualifications(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		Hashtable sessionAttributes = new Hashtable();
		sessionAttributes = (Hashtable) SessionManager.sessionExists(request, "uc_client_session");
		String quali_avail = request.getParameter("quali_avail");
		String[] qualifications = request.getParameterValues("qualifications[]");
		String[] exp_industry = request.getParameterValues("exp_industry[]");
		String[] exp_position = request.getParameterValues("exp_position[]");
		String[] exp_years = request.getParameterValues("exp_years[]");
		String[] exp_salary = request.getParameterValues("exp_salary[]");
		String[] exp_current = request.getParameterValues("exp_current[]");
		String exp_avail = request.getParameter("exp_avail");
		String expected_salary = request.getParameter("expected_salary");
		boolean quali_flag = true;
		boolean exp_flag = true;
		if(quali_avail.equals("1")) {
			model.startTransaction();
			if(model.deleteData("model.Uc_candidate_qualification", "cq_candidate_id=" + sessionAttributes.get("id"))) {
				for(int i=0 ; i<qualifications.length ; i++) {
					ucQuali_list = new ArrayList<Uc_qualifications>();
					ucQuali_list = (List<Uc_qualifications>)(Object) model.selectData("model.Uc_qualifications", "q_name='" + qualifications[i] + "' LIMIT 1");
					if(ucQuali_list.isEmpty()) {
						ucQuali = new Uc_qualifications();
						ucQuali.setQ_name(qualifications[i]);
						List<Long> qualikeys = model.insertDataReturnGeneratedKeys(ucQuali);
						ucCandiQuali = new Uc_candidate_qualification();
						ucCandiQuali.setCq_candidate_id(Long.parseLong(sessionAttributes.get("id").toString()));
						ucCandiQuali.setCq_quali_id(Long.parseLong(qualikeys.get(0).toString()));
						if(!model.insertData(ucCandiQuali)) {
							//error
							quali_flag = false;
							break;
						}
					} else {
						ucCandiQuali = new Uc_candidate_qualification();
						ucCandiQuali.setCq_candidate_id(Long.parseLong(sessionAttributes.get("id").toString()));
						ucCandiQuali.setCq_quali_id(ucQuali_list.get(0).getQ_id());
						if(!model.insertData(ucCandiQuali)) {
							//error
							quali_flag = false;
							break;
						}
					}
				}
			} else {
				//error
				quali_flag = false;
			}
			model.endTransaction();
		}
		if(exp_avail.equals("1")) {
			model.startTransaction();
			ucCandidate = new Uc_candidate_details();
			ucCandidate.setCd_experience(1);
			if(model.updateData(ucCandidate, "cd_id=" + Integer.parseInt(sessionAttributes.get("id").toString()))) {
				if(model.deleteData("model.Uc_experience", "e_candidate_id=" + sessionAttributes.get("id"))) {
					for(int i=0 ; i<exp_industry.length ; i++) {
						CommonHelper ch = new CommonHelper();
						long industry = ch.saveIndustrySector(exp_industry[i]);
						long position = ch.savePosition(exp_position[i]);
						if(industry != -1 && position != -1) {
							ucExperience = new Uc_experience();
							ucExperience.setE_candidate_id(Long.parseLong(sessionAttributes.get("id").toString()));
							ucExperience.setE_industry_id(industry);
							ucExperience.setE_position_id(position);
							ucExperience.setE_is_current_job(Integer.parseInt(exp_current[i]));
							ucExperience.setE_years(Integer.parseInt(exp_years[i]));
							ucExperience.setE_salary_per_month(Integer.parseInt(exp_salary[i]));
							if(!model.insertData(ucExperience)) {
								//error
								exp_flag = false;
								break;
							}
						} else {
							//error
							exp_flag = false;
							break;
						}
					}
				} else {
					//error
					exp_flag = false;
				}
			} else {
				//error
				exp_flag = false;
			}
			model.endTransaction();
		} else {
			model.startTransaction();
			ucCandidate = new Uc_candidate_details();
			ucCandidate.setCd_experience(0);
			if(model.updateData(ucCandidate, "cd_id=" + Integer.parseInt(sessionAttributes.get("id").toString()))) {
				if(!model.deleteData("model.Uc_experience", "e_candidate_id=" + sessionAttributes.get("id"))) {
					//error
					exp_flag = false;
				}
			} else {
				//error
				exp_flag = false;
			}
			model.endTransaction();
		}
		response.setContentType("text/html;charset=UTF-8");
		if(quali_flag && exp_flag) {
			ucCandidate = new Uc_candidate_details();
			ucCandidate.setCd_expected_salary(expected_salary);
			if(model.updateData(ucCandidate, "cd_id=" + Integer.parseInt(sessionAttributes.get("id").toString()))) {
				response.getWriter().write("success");
			} else {
				response.getWriter().write("error");
			}
		} else {
			response.getWriter().write("error");
		}
	}
	private void saveInterests(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		Hashtable sessionAttributes = new Hashtable();
		sessionAttributes = (Hashtable) SessionManager.sessionExists(request, "uc_client_session");
		String[] industry = request.getParameterValues("industry[]");
		String[] post = request.getParameterValues("post[]");
		boolean flag = true;
		model.startTransaction();
		if(model.deleteData("model.Uc_interest_areas", "ia_candidate=" + Integer.parseInt(sessionAttributes.get("id").toString()))) {
			for(int i=0 ; i<industry.length ; i++) {
				CommonHelper ch = new CommonHelper();
				long indus = ch.saveIndustrySector(industry[i]);
				long position = ch.savePosition(post[i]);
				if(indus != -1 && position != -1) {
					ucInterest = new Uc_interest_areas();
					ucInterest.setIa_candidate(Long.parseLong(sessionAttributes.get("id").toString()));
					ucInterest.setIa_industry(indus);
					ucInterest.setIa_position(position);
					if(!model.insertData(ucInterest)) {
						//error
						flag = false;
						break;
					}
				} else {
					//error
					flag = false;
					break;
				}
			}
		} else {
			//error
			flag = false;
		}
		model.endTransaction();
		response.setContentType("text/html;charset=UTF-8");
		if(flag) {
			response.getWriter().write("success");
		} else {
			response.getWriter().write("error");
		}
	}
	
	private void downloadResume(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		//FileManager.downloadFile(request, response, "resumes", request.getParameter("file"));
		ViewManager.showView(request, response, "download.jsp?fileName=" + request.getParameter("file") + "&fileType=file");
	}
	
	private void saveResume(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		try {
			fileManager = new FileManager();
		} catch(Exception ex) {}
		Hashtable sessionAttributes = new Hashtable();
		sessionAttributes = (Hashtable) SessionManager.sessionExists(request, "uc_client_session");
		ucCandidate = new Uc_candidate_details();
		ucCandidate = (Uc_candidate_details) model.selectData("model.Uc_candidate_details", "cd_id=" + Integer.parseInt(sessionAttributes.get("id").toString())).get(0);
		boolean flag = true;
		Part userResume = null;
		String userResumeFile = null;
		userResume = request.getPart("resume");
		//String userResumeFile = userResume.getSubmittedFileName();
		String original = getFileName(userResume);
		userResumeFile = "uc" + ucCandidate.getCd_contact_num().toString() + original.substring(original.lastIndexOf('.'));
		try {
			fileManager.deleteFTPFile(UCConstants.UC_RESUMES + ucCandidate.getCd_resume());
		} catch(Exception ex) {}
		try {
			fileManager.uploadFTPFile(userResume.getInputStream(), userResumeFile, UCConstants.UC_RESUMES);
			ucCandidate = new Uc_candidate_details();
			ucCandidate.setCd_resume(userResumeFile);
			if(model.updateData(ucCandidate, "cd_id=" + Integer.parseInt(sessionAttributes.get("id").toString()))) {
				flag = true;
			} else flag = false;
		} catch(Exception ex) {
			flag = false;
		}
		/*if(FileManager.deleteFile(request, "resumes", ucCandidate.getCd_resume())) {
			if(!FileManager.upLoadFile(request, "resumes", userResume, userResumeFile)) {
				flag = false;
			}
		} else {
			flag = false;
		}*/
		fileManager.disconnect();
		response.setContentType("text/html;charset=UTF-8");
		if(flag) {
			response.getWriter().write("success");
		} else {
			response.getWriter().write("error");
		}
	}
	
	private void savePassword(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		Hashtable sessionAttributes = new Hashtable();
		sessionAttributes = (Hashtable) SessionManager.sessionExists(request, "uc_client_session");
		String old_password = request.getParameter("old_password").toString();
		String new_password = request.getParameter("new_password").toString();
		ucCandidate_list = new ArrayList<Uc_candidate_details>();
		ucCandidate_list = (List<Uc_candidate_details>)(Object) model.selectData("model.Uc_candidate_details", "cd_id=" + Long.parseLong(sessionAttributes.get("id").toString()));
		if(ucCandidate_list.size() > 0) {
			if(EncryptionManager.matchHash(old_password, ucCandidate_list.get(0).getCd_password())) {
				ucCandidate = new Uc_candidate_details();
				ucCandidate.setCd_password(EncryptionManager.generateHash(new_password));
				if(model.updateData(ucCandidate, "cd_id=" + Long.parseLong(sessionAttributes.get("id").toString()))) {
					//success
					SessionManager.setSession(request, "success", "Password successfully changed.");
					RouteManager.route(response, "profile?action=change_password");
				} else {
					//error
					SessionManager.setSession(request, "error", "Something went wrong. Please try again.");
					RouteManager.route(response, "profile?action=change_password");
				}
			} else {
				//wrong old password
				SessionManager.setSession(request, "error", "Incorrect old password.");
				RouteManager.route(response, "profile?action=change_password");
			}
		} else {
			//redirect to home
			RouteManager.route(response, "");
		}
		
	}
	
	private String getFileName(Part part) {
        String contentDisp = part.getHeader("content-disposition");
        String[] tokens = contentDisp.split(";");
        for (String token : tokens) {
            if (token.trim().startsWith("filename")) {
                return token.substring(token.indexOf("=") + 2, token.length()-1);
            }
        }
        return "";
    }
}
