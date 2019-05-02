package controller;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.sql.Date;
import java.util.Hashtable;
import java.util.List;

import javax.servlet.ServletException;
import javax.servlet.annotation.MultipartConfig;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import javax.servlet.http.Part;

import org.json.JSONArray;

import database.Model;
import model.Uc_admin;
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
import utils.CookieManager;
import utils.EncryptionManager;
import utils.FileManager;
import utils.RouteManager;
import utils.SessionManager;
import utils.ViewManager;

@WebServlet("/user-accounts")
@MultipartConfig(fileSizeThreshold=1024*1024*10, // 10MB
maxFileSize=1024*1024*20,      // 20MB
maxRequestSize=1024*1024*50)   // 50MB
public class UserAccounts extends HttpServlet {
	private static final long serialVersionUID = 1L;
	private Model model;
	private List<Uc_state> ucState_list;
	private List<Uc_district> ucDistrict_list;
	private List<Uc_taluka> ucTaluka_list;
	private List<Uc_city> ucCity_list;
	private List<Uc_candidate_details> ucCandidate_list;
	private Uc_candidate_details ucCandidate;
	private Uc_district ucDistrict;
	private Uc_taluka ucTaluka;
	private Uc_state ucState;
	private Uc_city ucCity;
	private List<Uc_qualifications> ucQuali_list;
	private Uc_qualifications ucQuali;
	private List<Uc_industry_sector> ucIndustry_list;
	private List<Uc_position> ucPosition_list;
	private Uc_industry_sector ucIndustry;
	private Uc_position ucPosition;
	private List<Uc_candidate_qualification> ucCandiQuali_list;
	private Uc_candidate_qualification ucCandiQuali;
	private List<Uc_experience> ucExperience_list;
	private Uc_experience ucExperience;
	private List<Uc_interest_areas> ucInterest_list;
	private Uc_interest_areas ucInterest;
	private FileManager fileManager;
	private CommonHelper ch;
	
	public UserAccounts() {
        super();
        model = new Model();
        ucState_list = new ArrayList<Uc_state>();
        ucDistrict_list = new ArrayList<Uc_district>();
        ucCity_list = new ArrayList<Uc_city>();
        ucCandidate_list = new ArrayList<Uc_candidate_details>();
    }

	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		/*if(request.getAttribute("restricted") != null && Integer.parseInt(request.getAttribute("restricted").toString()) == 1) {
			request.removeAttribute("restricted");
			showSignupPage(request, response);
		} else {*/
		if(request.getParameter("action") == null) {
			showSignupPage(request, response);
		} else if(request.getParameter("action").toString().equals("login")) {
			login(request,response);
		} else if(request.getParameter("action").toString().equals("signup")) {
			signup(request,response);
			if(request.getParameter("job") != null) RouteManager.route(response, "user-accounts?job=" + request.getParameter("job"));
			else RouteManager.route(response, "user-accounts");
		} else if(request.getParameter("action").toString().equals("getDistrict")) {
			getDistrict(request,response);
		} else if(request.getParameter("action").toString().equals("getTaluka")) {
			getTaluka(request,response);
		} else if(request.getParameter("action").toString().equals("getCity")) {
			getCity(request,response);
		} else if(request.getParameter("action").toString().equals("verifyContact")) {
			verifyContact(request,response);
		} else if(request.getParameter("action").toString().equals("logout")) {
			logout(request,response);
		} else {
			showSignupPage(request, response);
		}
		//}
	}

	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		doGet(request, response);
	}

	protected void showSignupPage(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		if(SessionManager.sessionExists(request, "uc_client_session") == null) {
			ucState_list = (List<Uc_state>)(Object) model.selectData("model.Uc_state", "1");
			request.setAttribute("states", ucState_list);
			//request.getRequestDispatcher("/WEB-INF/signin.jsp").forward(request, response);
			ViewManager.showView(request, response, "signin.jsp");
		} else {
			RouteManager.route(response, "");
		}
	}
	
	protected void login(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		String username = request.getParameter("signin_username").toString();
		String password = request.getParameter("signin_password").toString();
		username = username.replace("'", "\\'");
		password = password.replace("'", "\\'");
		String whereClause = "(cd_contact_num='" + username + "' or cd_email='" + username + "') and cd_is_active=1";
		ucCandidate_list = new ArrayList<Uc_candidate_details>();
		ucCandidate_list = (List<Uc_candidate_details>)(Object) model.selectData("model.Uc_candidate_details", whereClause);
		if(ucCandidate_list.size() > 0) {
			if(EncryptionManager.matchHash(password, ucCandidate_list.get(0).getCd_password())) {
				//success
				ucCandidate = new Uc_candidate_details();
				ucCandidate = ucCandidate_list.get(0);
				if(request.getParameter("remember") != null) {
					CookieManager.setCookie(response, "uc_client_cookie", String.valueOf(ucCandidate.getCd_contact_num()), (60*60*24*5));
				}
				Hashtable sessionAttributes = new Hashtable();
				sessionAttributes.put("contact_no", ucCandidate.getCd_contact_num());
				sessionAttributes.put("name", ucCandidate.getCd_name());
				sessionAttributes.put("id", ucCandidate.getCd_id());
				sessionAttributes.put("payment", ucCandidate.getCd_payment());
				SessionManager.setSession(request, "uc_client_session", sessionAttributes);
				if(request.getParameter("job") != null) {
					RouteManager.route(response, "apply?job=" + request.getParameter("job").toString());
				} else {
					RouteManager.route(response, "");
				}
			} else {
				SessionManager.setSession(request, "error", "Invalid password.");
				if(request.getParameter("job") != null) {
					RouteManager.route(response, "user-accounts?job=" + request.getParameter("job").toString());
				} else {
					RouteManager.route(response, "user-accounts");
				}
			}
		} else {
			SessionManager.setSession(request, "error", "Invalid username or you might be deactivated by the admin.");
			if(request.getParameter("job") != null) {
				RouteManager.route(response, "user-accounts?job=" + request.getParameter("job").toString());
			} else {
				RouteManager.route(response, "user-accounts");
			}
		}
	}
	

	protected void signup(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		
		boolean firstStep = true;
		boolean secondStep = true;
		boolean thirdStep = true;
		boolean fourthStep = true;
		ucCandidate = new Uc_candidate_details();
		//SimpleDateFormat smf = new SimpleDateFormat("yyyy/MM/dd");
		Date date = new Date(Calendar.getInstance().getTime().getTime());
		
		/*----------- FIRST STEP -----------*/
		String user_fullname = request.getParameter("user_fullname").toString();
		user_fullname = user_fullname.replace("'", "\\'");
		ucCandidate.setCd_name(user_fullname);
		//long user_contactno = Long.parseLong(request.getParameter("user_contactno").toString());
		ucCandidate.setCd_contact_num(Long.parseLong(request.getParameter("user_contactno").toString()));
		//String user_email = request.getParameter("user_email").toString();
		ucCandidate.setCd_email(request.getParameter("user_email").toString());
		//String user_password = request.getParameter("user_password").toString();
		ucCandidate.setCd_password(EncryptionManager.generateHash(request.getParameter("user_password").toString()));
		//int user_gender = Integer.parseInt(request.getParameter("user_gender").toString());
		ucCandidate.setCd_gender(Integer.parseInt(request.getParameter("user_gender").toString()));
		//int user_birthyear = Integer.parseInt(request.getParameter("user_birthyear").toString());
		ucCandidate.setCd_birthyear(Integer.parseInt(request.getParameter("user_birthyear").toString()));
		
		if(request.getParameter("user_expected_salary") != null && !request.getParameter("user_expected_salary").toString().equals("")) {
			ucCandidate.setCd_expected_salary(request.getParameter("user_expected_salary").toString());
		}
		
		if(request.getParameter("user_secondarycontactno") != null && !request.getParameter("user_secondarycontactno").toString().equals("")) {
			ucCandidate.setCd_contact_num_secondary(Long.parseLong(request.getParameter("user_secondarycontactno").toString()));
		}
		
		//ucCandidate.setCd_joining_date(smf.format(date));
		ucCandidate.setCd_joining_date(date);
		long user_state = Long.parseLong(request.getParameter("user_state").toString());
		ucState_list = (List<Uc_state>)(Object) model.selectData("model.Uc_state", "s_id=" + user_state);
		String state_name = ucState_list.get(0).getS_name();
		long user_city = 0;
		String user_district_other = "";
		String user_taluka_other = "";
		String user_city_other = "";
		if(state_name.toLowerCase().equals("gujarat")) {
			firstStep = true;
			user_city = Long.parseLong(request.getParameter("user_city").toString());
		} else {
			ch = new CommonHelper();
			user_city = ch.saveCity(request);
			if(user_city != -1) {
				firstStep = true;
			} else {
				firstStep = false;
			}
		}
			/*user_district_other = request.getParameter("user_district_other").toString();
			user_taluka_other = request.getParameter("user_taluka_other").toString();
			user_city_other = request.getParameter("user_city_other").toString();
			ucDistrict = new Uc_district();
			ucDistrict.setD_s_id(user_state);
			ucDistrict.setD_name(user_district_other);
			// transaction starts from here //
			model.startTransaction();
			List<Long> keys = model.insertDataReturnGeneratedKeys(ucDistrict);
			if(keys != null) {
				long dId = Long.parseLong(keys.get(0).toString());
				ucTaluka = new Uc_taluka();
				ucTaluka.setT_district_id(dId);
				ucTaluka.setT_name(user_taluka_other);
				List<Long> talukaKey = model.insertDataReturnGeneratedKeys(ucTaluka);
				if(talukaKey != null) {
					firstStep = true;
					ucCity = new Uc_city();
					ucCity.setC_taluka_id(Long.parseLong(talukaKey.get(0).toString()));
					ucCity.setC_name(user_city_other);
					List<Long> citykeys = model.insertDataReturnGeneratedKeys(ucCity);
					if(citykeys != null) {
						firstStep = true;
						user_city = Long.parseLong(citykeys.get(0).toString());
					} else {
						firstStep = false;
						SessionManager.setSession(request, "error", "Something went wrong while saving your details. Please try again.");
					}
				} else {
					firstStep = false;
					SessionManager.setSession(request, "error", "Something went wrong while saving your details. Please try again.");
				}
			} else {
				firstStep = false;
				SessionManager.setSession(request, "error", "Something went wrong while saving your details. Please try again.");
			}*/
			//model.endTransaction();
			// transaction ends here //
		
		ucCandidate.setCd_city_id(user_city);
		ucCandidate.setCd_is_placed(0);
		/*----------- /FIRST STEP -----------*/
		
		/*----------- SECOND STEP ------------*/
		int experience_count = 1;
		int qualification_count = 1;
		List<Integer> user_qualification = new ArrayList<Integer>();
		List<Integer> user_experience_industry = new ArrayList<Integer>();
		List<Integer> user_experience_position = new ArrayList<Integer>();
		List<Integer> user_experience_years = new ArrayList<Integer>();
		ucExperience_list = new ArrayList<Uc_experience>();
		ucCandiQuali_list = new ArrayList<Uc_candidate_qualification>();
		int currentJob = -1;
		if(firstStep) {
			qualification_count = Integer.parseInt(request.getParameter("qualification_count").toString());
			for(int i=1 ; i<=qualification_count ; i++) {
				ucCandiQuali = new Uc_candidate_qualification();
				if(request.getParameter("user_qualification" + i) != null) {
					ucQuali_list = new ArrayList<Uc_qualifications>();
					ucQuali_list = (List<Uc_qualifications>)(Object) model.selectData("model.Uc_qualifications", "q_name='" + request.getParameter("user_qualification" + i).toString() + "' LIMIT 1");
					if(ucQuali_list.isEmpty()) {
						ucQuali = new Uc_qualifications();
						ucQuali.setQ_name(request.getParameter("user_qualification" + i).toString());
						List<Long> qualikeys = model.insertDataReturnGeneratedKeys(ucQuali);
						if(qualikeys != null) {
							secondStep = true;
							//user_qualification.add(model.getLastInsertedID());
							ucCandiQuali.setCq_quali_id(Long.parseLong(qualikeys.get(0).toString()));
							
						} else {
							secondStep = false;
							SessionManager.setSession(request, "error", "Something went wrong while saving your details. Please try again.");
						}
					} else {
						//user_qualification.add(ucQuali_list.get(0).getQ_id());
						ucCandiQuali.setCq_quali_id(ucQuali_list.get(0).getQ_id());
					}
					ucCandiQuali_list.add(ucCandiQuali);
				}
			}
			if(secondStep) {
			if(request.getParameter("user_experience").toString().equals("y")) {
				ucCandidate.setCd_experience(1);
				experience_count = Integer.parseInt(request.getParameter("experience_count").toString());
				for(int i=1 ; i<=experience_count ; i++) {
					ucExperience = new Uc_experience();
					if(request.getParameter("user_industry_sector" + i) != null) {
						if(request.getParameter("current_job") != null && request.getParameter("current_job").equals("current_job" + i)) {
							ucExperience.setE_is_current_job(1);
						} else {
							ucExperience.setE_is_current_job(0);
						}
						if(request.getParameter("user_exp_salary" + i) != null && !request.getParameter("user_exp_salary" + i).toString().equals("")) {
							ucExperience.setE_salary_per_month((int)Double.parseDouble(request.getParameter("user_exp_salary" + i).toString()));
						}
						//user_experience_years.add(Integer.parseInt(request.getParameter("user_year_exp" + i).toString()));
						ucExperience.setE_years((int)Double.parseDouble(request.getParameter("user_year_exp" + i).toString()));
						ucIndustry_list = new ArrayList<Uc_industry_sector>();
						ucPosition_list = new ArrayList<Uc_position>();
						ucIndustry_list = (List<Uc_industry_sector>)(Object) model.selectData("model.Uc_industry_sector", "is_name='" + request.getParameter("user_industry_sector" + i).toString() + "' LIMIT 1");
						if(ucIndustry_list.isEmpty()) {
							ucIndustry = new Uc_industry_sector();
							ucIndustry.setIs_name(request.getParameter("user_industry_sector" + i).toString());
							List<Long> industrykeys = model.insertDataReturnGeneratedKeys(ucIndustry);
							if(industrykeys != null) {
								secondStep = true;
								//user_experience_industry.add(model.getLastInsertedID());
								ucExperience.setE_industry_id(Long.parseLong(industrykeys.get(0).toString()));
							} else {
								secondStep = false;
								SessionManager.setSession(request, "error", "Something went wrong while saving your details. Please try again.");
							}
						} else {
							//user_experience_industry.add(ucIndustry_list.get(0).getIs_id());
							ucExperience.setE_industry_id(ucIndustry_list.get(0).getIs_id());
						}
						ucPosition_list = (List<Uc_position>)(Object) model.selectData("model.Uc_position", "p_name='" + request.getParameter("user_post" + i).toString() + "' LIMIT 1");
						if(ucPosition_list.isEmpty()) {
							ucPosition = new Uc_position();
							ucPosition.setP_name(request.getParameter("user_post" + i).toString());
							List<Long> poskeys = model.insertDataReturnGeneratedKeys(ucPosition);
							if(poskeys != null) {
								secondStep = true;
								//user_experience_position.add(model.getLastInsertedID());
								ucExperience.setE_position_id(Long.parseLong(poskeys.get(0).toString()));
							} else {
								secondStep = false;
								SessionManager.setSession(request, "error", "Something went wrong while saving your details. Please try again.");
							}
						} else {
							//user_experience_position.add(ucPosition_list.get(0).getP_id());
							ucExperience.setE_position_id(ucPosition_list.get(0).getP_id());
						}
						ucExperience_list.add(ucExperience);
					}
				}
			} else {
				//no experience
				ucCandidate.setCd_experience(0);
			}
			}
		} else {
			secondStep = false;
		}
		/*----------- /SECOND STEP ------------*/
		
		/*----------- THIRD STEP ------------*/
		int interest_count = 1;
		List<Integer> interest_industry = new ArrayList<Integer>();
		List<Integer> interest_post = new ArrayList<Integer>();
		ucInterest_list = new ArrayList<Uc_interest_areas>();
		if(secondStep) {
			interest_count = Integer.parseInt(request.getParameter("interest_count"));
			for(int i=1 ; i<interest_count ; i++) {
				ucInterest = new Uc_interest_areas();
				if(request.getParameter("interest-industry" + i) != null) {
					ucIndustry_list = new ArrayList<Uc_industry_sector>();
					ucIndustry_list = (List<Uc_industry_sector>)(Object) model.selectData("model.Uc_industry_sector", "is_name='" + request.getParameter("interest-industry" + i).toString() + "' LIMIT 1");
					if(ucIndustry_list.isEmpty()) {
						ucIndustry = new Uc_industry_sector();
						ucIndustry.setIs_name(request.getParameter("interest-industry" + i).toString());
						List<Long> industrykeys = model.insertDataReturnGeneratedKeys(ucIndustry);
						if(industrykeys != null) {
							thirdStep = true;
							//interest_industry.add(model.getLastInsertedID());
							ucInterest.setIa_industry(Long.parseLong(industrykeys.get(0).toString()));
						} else {
							thirdStep = false;
							SessionManager.setSession(request, "error", "Something went wrong while saving your details. Please try again.");
							break;
						}
					} else {
						//interest_industry.add(ucIndustry_list.get(0).getIs_id());
						ucInterest.setIa_industry(ucIndustry_list.get(0).getIs_id());
					}
				}
				
				if(request.getParameter("interest-post" + i) != null && thirdStep) {
					ucPosition_list = new ArrayList<Uc_position>();
					ucPosition_list = (List<Uc_position>)(Object) model.selectData("model.Uc_position", "p_name='" + request.getParameter("interest-post" + i).toString() + "' LIMIT 1");
					if(ucPosition_list.isEmpty()) {
						ucPosition = new Uc_position();
						ucPosition.setP_name(request.getParameter("interest-post" + i).toString());
						List<Long> poskeys = model.insertDataReturnGeneratedKeys(ucPosition);
						if(poskeys != null) {
							thirdStep = true;
							//interest_post.add(model.getLastInsertedID());
							ucInterest.setIa_position(Long.parseLong(poskeys.get(0).toString()));
						} else {
							thirdStep = false;
							SessionManager.setSession(request, "error", "Something went wrong while saving your details. Please try again.");
							break;
						}
					} else {
						//interest_post.add(ucPosition_list.get(0).getP_id());
						ucInterest.setIa_position(ucPosition_list.get(0).getP_id());
					}
					ucInterest_list.add(ucInterest);
				}
			}
		} else {
			thirdStep = false;
		}
		/*----------- /THIRD STEP ------------*/
		
		/*----------- FOURTH STEP ------------*/
		Part userResume = null;
		String userResumeFile = null;
		if(thirdStep) {
			userResume = request.getPart("user_resume");
			//String userResumeFile = userResume.getSubmittedFileName();
			String original = getFileName(userResume);
			userResumeFile = "uc" + request.getParameter("user_contactno").toString() + original.substring(original.lastIndexOf('.'));
			ucCandidate.setCd_resume(userResumeFile);
		} else {
			fourthStep = false;
		}
		/*----------- /FOURTH STEP ------------*/
		
		if(fourthStep) {
			model.startTransaction();
				List<Long> keys = model.insertDataReturnGeneratedKeys(ucCandidate);
				if(keys != null) {
				long candiId = keys.get(0);
				//System.out.println(candiId);
				for(int i=0 ; i<ucCandiQuali_list.size() ; i++) {
					ucCandiQuali_list.get(i).setCq_candidate_id(candiId);
					model.insertData(ucCandiQuali_list.get(i));
				}
				for(int i=0 ; i<ucExperience_list.size() ; i++) {
					ucExperience_list.get(i).setE_candidate_id(candiId);
					model.insertData(ucExperience_list.get(i));
				}
				for(int i=0 ; i<ucInterest_list.size() ; i++) {
					ucInterest_list.get(i).setIa_candidate(candiId);
					model.insertData(ucInterest_list.get(i));
				}
				//FileManager.upLoadFile(request, "resumes", userResume, userResumeFile);
				try{
					fileManager = new FileManager();
					fileManager.uploadFTPFile(userResume.getInputStream(), userResumeFile, UCConstants.UC_RESUMES);
				} catch(Exception ex) {
					
				} finally {
					fileManager.disconnect();
				}
				} else {
					SessionManager.setSession(request, "error", "Something went wrong while creating your account. Please try again.");
				}
			model.endTransaction();
			SessionManager.setSession(request, "success", "Your account is successfully created. You can now sign in and apply for the jobs you want.");
		} else {
			SessionManager.setSession(request, "error", "Something went wrong while creating your account. Please try again.");
		}
	}
	
	protected void getDistrict(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException  {
		int stateId = Integer.parseInt(request.getParameter("stateId").toString());
		ucDistrict_list = (List<Uc_district>)(Object) model.selectData("model.Uc_district", "d_s_id=" + stateId + " order by d_name");
		StringBuilder temp = new StringBuilder();
		temp.append("<option selected hidden value=\"select\">* Select District</option>");
		for(Uc_district district : ucDistrict_list) {
			temp.append("<option value=\""+ district.getD_id() +"\">"+district.getD_name()+"</option>");
		}
		response.setContentType("text/html;charset=UTF-8");
		response.getWriter().write(temp.toString());
	}
	
	protected void getTaluka(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException  {
		int districtId = Integer.parseInt(request.getParameter("districtId").toString());
		ucTaluka_list = (List<Uc_taluka>)(Object) model.selectData("model.Uc_taluka", "t_district_id=" + districtId + " order by t_name");
		StringBuilder temp = new StringBuilder();
		temp.append("<option selected hidden value=\"select\">* Select Taluka</option>");
		for(Uc_taluka taluka : ucTaluka_list) {
			temp.append("<option value=\""+ taluka.getT_id() +"\">"+taluka.getT_name()+"</option>");
		}
		response.setContentType("text/html;charset=UTF-8");
		response.getWriter().write(temp.toString());
	}
	
	protected void getCity(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException  {
		int talukaId = Integer.parseInt(request.getParameter("talukaId").toString());
		ucCity_list = (List<Uc_city>)(Object) model.selectData("model.Uc_city", "c_taluka_id=" + talukaId + " order by c_name");
		StringBuilder temp = new StringBuilder();
		temp.append("<option selected hidden value=\"select\">* Select City / Village</option>");
		for(Uc_city city : ucCity_list) {
			temp.append("<option value=\""+ city.getC_id() +"\">"+city.getC_name()+"</option>");
		}
		response.setContentType("text/html;charset=UTF-8");
		response.getWriter().write(temp.toString());
	}
	
	protected void verifyContact(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		long contactNo = Long.parseLong(request.getParameter("user_contact").toString());
		String email = request.getParameter("user_email").toString();
		ucCandidate_list = new ArrayList<Uc_candidate_details>();
		ucCandidate_list = (List<Uc_candidate_details>)(Object) model.selectData("model.Uc_candidate_details", "cd_contact_num=" + contactNo);
		response.setContentType("text/html;charset=UTF-8");
		if(ucCandidate_list.isEmpty()) {
			ucCandidate_list = new ArrayList<Uc_candidate_details>();
			ucCandidate_list = (List<Uc_candidate_details>)(Object) model.selectData("model.Uc_candidate_details", "cd_email=BINARY'" + email + "'");
			if(ucCandidate_list.isEmpty()) {
				response.getWriter().write("success");
			} else {
				response.getWriter().write("email");
			}
		} else {
			response.getWriter().write("contact");
		}
	}
	protected void logout(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		SessionManager.unsetSession(request, "uc_client_session");
		CookieManager.unsetCookie(request, response, "uc_client_cookie");
		RouteManager.route(response, "");
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
