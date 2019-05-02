package controller;

import java.io.IOException;
import java.sql.Date;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
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
import model.Uc_admin;
import model.Uc_admin_cj_permissions;
import model.Uc_admin_employer_permissions;
import model.Uc_admin_types;
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
import model.Uc_job_facilities;
import model.Uc_job_facilities_mapping;
import model.Uc_job_qualifications;
import model.Uc_position;
import model.Uc_qualifications;
import model.Uc_state;
import model.Uc_taluka;
import utils.RouteManager;
import utils.SessionManager;
import utils.ViewManager;

@WebServlet("/admin/current-openings")
public class AdminCurrentOpenings extends HttpServlet {
	private static final long serialVersionUID = 1L;

	private List<Uc_state> ucState_list;
	private Model model;
	private List<Uc_current_jobs> ucCurrJobs_list;
	private List<Uc_industry_sector> ucIndustry_list;
	private List<Uc_position> ucPosition_list;
	private Uc_industry_sector ucIndustry;
	private Uc_position ucPosition;
	private Uc_current_jobs ucCurrJobs;
	private Uc_cj_quantity ucCjQuantity;
	private Uc_district ucDistrict;
	private Uc_city ucCity;
	private List<Uc_employer_details> ucEmployer_list;
	private CommonHelper commonHelper;
	private Uc_employer_details ucEmployer;
	private List<Uc_qualifications> ucQuali_list;
	private Uc_qualifications ucQuali;
	private Uc_job_qualifications ucJobQuali;
	private List<Uc_job_qualifications> ucJobQuali_list;
	private Uc_admin_cj_permissions ucAdminCjPermissions;
	private List<Uc_admin> ucAdmin_list;
	private List<Uc_admin_cj_permissions> ucAdminCjPermissions_list;
	private List<Uc_admin_types> ucAdminTypes_list;
	private Uc_admin ucAdmin;
	private Uc_state ucState;
	private Uc_taluka ucTaluka;
	private List<Uc_district> ucDistrict_list;
	private List<Uc_taluka> ucTaluka_list;
	private List<Uc_city> ucCity_list;
	private List<Uc_cj_quantity> ucCjQuantity_list;
	public AdminCurrentOpenings() {
        super();
        model = new Model();
    }

	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		Hashtable sessionAttributes = new Hashtable();
		sessionAttributes = (Hashtable) SessionManager.sessionExists(request, "admin");
		if(request.getParameter("action") != null) {
			if(request.getParameter("action").toLowerCase().equals("add")) {
				if(Integer.parseInt(sessionAttributes.get("utype").toString()) == UCConstants.MAIN_ADMIN || Integer.parseInt(sessionAttributes.get("utype").toString()) == UCConstants.SUB_MAIN_ADMIN || Integer.parseInt(sessionAttributes.get("utype").toString()) == UCConstants.SALES_ADMIN || Integer.parseInt(sessionAttributes.get("utype").toString()) == UCConstants.ADD_REQUIREMENT_ADMIN) {
					add(request, response);
					ViewManager.showView(request, response, "admin/addcurrentopenings.jsp");
				} else {
					ViewManager.showView(request, response, "admin/accessdenied.jsp");
				}
			} else if(request.getParameter("action").toLowerCase().equals("modify")) {
				if(Integer.parseInt(sessionAttributes.get("utype").toString()) == UCConstants.MAIN_ADMIN || Integer.parseInt(sessionAttributes.get("utype").toString()) == UCConstants.SUB_MAIN_ADMIN || Integer.parseInt(sessionAttributes.get("utype").toString()) == UCConstants.SALES_ADMIN) {
					ViewManager.showView(request, response, "admin/modifycurrentopenings.jsp");
				} else {
					ViewManager.showView(request, response, "admin/accessdenied.jsp");
				}
			} else if(request.getParameter("action").toLowerCase().equals("save")) {
				if(Integer.parseInt(sessionAttributes.get("utype").toString()) == UCConstants.MAIN_ADMIN || Integer.parseInt(sessionAttributes.get("utype").toString()) == UCConstants.SUB_MAIN_ADMIN || Integer.parseInt(sessionAttributes.get("utype").toString()) == UCConstants.SALES_ADMIN || Integer.parseInt(sessionAttributes.get("utype").toString()) == UCConstants.ADD_REQUIREMENT_ADMIN) {
					saveNewJob(request, response);
					RouteManager.route(response, "admin/current-openings?action=add");
				} else {
					ViewManager.showView(request, response, "admin/accessdenied.jsp");
				}
			} else if(request.getParameter("action").toLowerCase().equals("savechanges")) {
				if(Integer.parseInt(sessionAttributes.get("utype").toString()) == UCConstants.MAIN_ADMIN || Integer.parseInt(sessionAttributes.get("utype").toString()) == UCConstants.SUB_MAIN_ADMIN || Integer.parseInt(sessionAttributes.get("utype").toString()) == UCConstants.SALES_ADMIN) {
					saveChanges(request, response);
				} else {
					ViewManager.showView(request, response, "admin/accessdenied.jsp");
				}
			} else if(request.getParameter("action").toLowerCase().equals("getcurrentopenings")) {
				if(Integer.parseInt(sessionAttributes.get("utype").toString()) == UCConstants.MAIN_ADMIN || Integer.parseInt(sessionAttributes.get("utype").toString()) == UCConstants.SUB_MAIN_ADMIN || Integer.parseInt(sessionAttributes.get("utype").toString()) == UCConstants.SALES_ADMIN) {
					try {
						getCurrentOpenings(request,response);
					} catch(Exception ex) {
						System.out.println(ex);
						LogManager.appendToExceptionLogs(ex);
					}
				} else {
					ViewManager.showView(request, response, "admin/accessdenied.jsp");
				}
			} else if(request.getParameter("action").toLowerCase().equals("modifysingle")) {
				if(Integer.parseInt(sessionAttributes.get("utype").toString()) == UCConstants.MAIN_ADMIN || Integer.parseInt(sessionAttributes.get("utype").toString()) == UCConstants.SUB_MAIN_ADMIN || Integer.parseInt(sessionAttributes.get("utype").toString()) == UCConstants.SALES_ADMIN) {
					try {
						modifySingle(request,response);
					} catch(Exception ex) {
						System.out.println(ex);
						LogManager.appendToExceptionLogs(ex);
					}
				} else {
					ViewManager.showView(request, response, "admin/accessdenied.jsp");
				}
			} else if(request.getParameter("action").toLowerCase().equals("activate")) {
				if(Integer.parseInt(sessionAttributes.get("utype").toString()) == UCConstants.MAIN_ADMIN || Integer.parseInt(sessionAttributes.get("utype").toString()) == UCConstants.SUB_MAIN_ADMIN) {
					try {
						activate(request,response);
					} catch(Exception ex) {
						System.out.println(ex);
						LogManager.appendToExceptionLogs(ex);
					}
				} else {
					ViewManager.showView(request, response, "admin/accessdenied.jsp");
				}
			} else if(request.getParameter("action").toLowerCase().equals("deactivate")) {
				if(Integer.parseInt(sessionAttributes.get("utype").toString()) == UCConstants.MAIN_ADMIN || Integer.parseInt(sessionAttributes.get("utype").toString()) == UCConstants.SUB_MAIN_ADMIN) {
					try {
						deactivate(request,response);
					} catch(Exception ex) {
						System.out.println(ex);
						LogManager.appendToExceptionLogs(ex);
					}
				} else {
					ViewManager.showView(request, response, "admin/accessdenied.jsp");
				}
			} else if(request.getParameter("action").toLowerCase().equals("approve")) {
				if(Integer.parseInt(sessionAttributes.get("utype").toString()) == UCConstants.MAIN_ADMIN || Integer.parseInt(sessionAttributes.get("utype").toString()) == UCConstants.SUB_MAIN_ADMIN) {
					try {
						approve(request,response);
					} catch(Exception ex) {
						System.out.println(ex);
						LogManager.appendToExceptionLogs(ex);
					}
				} else {
					ViewManager.showView(request, response, "admin/accessdenied.jsp");
				}
			} else if(request.getParameter("action").toLowerCase().equals("disapprove")) {
				if(Integer.parseInt(sessionAttributes.get("utype").toString()) == UCConstants.MAIN_ADMIN || Integer.parseInt(sessionAttributes.get("utype").toString()) == UCConstants.SUB_MAIN_ADMIN) {
					try {
						disapprove(request,response);
					} catch(Exception ex) {
						System.out.println(ex);
						LogManager.appendToExceptionLogs(ex);
					}
				} else {
					ViewManager.showView(request, response, "admin/accessdenied.jsp");
				}
			} else if(request.getParameter("action").toLowerCase().equals("addpermission")) {
				if(Integer.parseInt(sessionAttributes.get("utype").toString()) == UCConstants.MAIN_ADMIN || Integer.parseInt(sessionAttributes.get("utype").toString()) == UCConstants.SUB_MAIN_ADMIN) {
					try {
						addPermission(request,response);
					} catch(Exception ex) {
						System.out.println(ex);
						LogManager.appendToExceptionLogs(ex);
					}
				} else {
					ViewManager.showView(request, response, "admin/accessdenied.jsp");
				}
			}  else if(request.getParameter("action").toLowerCase().equals("removepermission")) {
				if(Integer.parseInt(sessionAttributes.get("utype").toString()) == UCConstants.MAIN_ADMIN || Integer.parseInt(sessionAttributes.get("utype").toString()) == UCConstants.SUB_MAIN_ADMIN) {
					try {
						removePermission(request,response);
					} catch(Exception ex) {
						System.out.println(ex);
						LogManager.appendToExceptionLogs(ex);
					}
				} else {
					ViewManager.showView(request, response, "admin/accessdenied.jsp");
				}
			} else if(request.getParameter("action").toLowerCase().equals("downloadjobs")) {
				if(Integer.parseInt(sessionAttributes.get("utype").toString()) == UCConstants.MAIN_ADMIN || Integer.parseInt(sessionAttributes.get("utype").toString()) == UCConstants.SUB_MAIN_ADMIN || Integer.parseInt(sessionAttributes.get("utype").toString()) == UCConstants.SALES_ADMIN) {
					downloadJobs(request, response);
				} else {
					ViewManager.showView(request, response, "admin/accessdenied.jsp");
				}
			} else if(request.getParameter("action").toLowerCase().equals("deletejobs")) {
				if(Integer.parseInt(sessionAttributes.get("utype").toString()) == UCConstants.MAIN_ADMIN || Integer.parseInt(sessionAttributes.get("utype").toString()) == UCConstants.SUB_MAIN_ADMIN || Integer.parseInt(sessionAttributes.get("utype").toString()) == UCConstants.SALES_ADMIN) {
					try {
						deleteJobs(request, response);
					} catch(Exception ex) {
						LogManager.appendToExceptionLogs(ex);
					}
				} else {
					ViewManager.showView(request, response, "admin/accessdenied.jsp");
				}
			} 
		} else {
			if(Integer.parseInt(sessionAttributes.get("utype").toString()) == UCConstants.MAIN_ADMIN || Integer.parseInt(sessionAttributes.get("utype").toString()) == UCConstants.SUB_MAIN_ADMIN || Integer.parseInt(sessionAttributes.get("utype").toString()) == UCConstants.SALES_ADMIN) {
				RouteManager.route(response, "admin/current-openings?action=modify");
			} else {
				ViewManager.showView(request, response, "admin/accessdenied.jsp");
			}
		}
	}

	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		doGet(request, response);
	}
	
	private void add(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		ucState_list = new ArrayList<Uc_state>();
		ucState_list = (List<Uc_state>)(Object) model.selectData("model.Uc_state", "1");
		request.setAttribute("states", ucState_list);
		ucCurrJobs_list = new ArrayList<Uc_current_jobs>();
		ucCurrJobs_list = (List<Uc_current_jobs>)(Object) model.selectData("model.Uc_current_jobs", "cj_is_featured=1");
		if(ucCurrJobs_list.size() >=6) request.setAttribute("featured", false);
		else request.setAttribute("featured", true);
		ucEmployer_list = new ArrayList<Uc_employer_details>();
		ucEmployer_list = (List<Uc_employer_details>)(Object) model.selectData("model.Uc_employer_details", "ed_active=1");
		if(ucEmployer_list.isEmpty()) {
			request.setAttribute("firmsAvail", false);
			request.setAttribute("employers", null);
		} else {
			request.setAttribute("firmsAvail", true);
			request.setAttribute("employers", ucEmployer_list);
		}
		List<Uc_job_facilities> ucJobFacilities_list = new ArrayList<>();
		ucJobFacilities_list  = (List<Uc_job_facilities>)(Object) model.selectData("model.Uc_job_facilities", "1");
		request.setAttribute("jobFacilities", ucJobFacilities_list);
	}
	
	private void saveNewJob(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		commonHelper = new CommonHelper();
		ucCurrJobs = new Uc_current_jobs();
		ucCjQuantity = new Uc_cj_quantity();
		long position = -1;
		long user_city = -1;
		int noGender = 0;
		long employer = 0;
		String[] facilities = request.getParameterValues("facilities");
		
		
		if(request.getParameter("employer") != null && !request.getParameter("employer").toString().equals("select")) {
			employer = Long.parseLong(request.getParameter("employer").toString());
		} else {
			employer = Long.parseLong(request.getParameter("employer_id").toString());
			ucEmployer_list = new ArrayList<Uc_employer_details>();
			ucEmployer_list = (List<Uc_employer_details>)(Object) model.selectData("model.Uc_employer_details", "ed_active=1 and ed_id=" + employer);
			if(ucEmployer_list.isEmpty()) {
				SessionManager.setSession(request, "error", "Something went wrong while saving job details. Please try again.");
				employer = 0;
			}
		}
		if(employer != 0) {
		ucCurrJobs.setCj_employer_id(employer);
	//	ucCurrJobs.setCj_experience(request.getParameter("experience").toString().equals("") ? "N/A" : request.getParameter("experience").toString());
		ucCurrJobs.setCj_experience("N/A");
		ucCurrJobs.setCj_experience_start(Integer.parseInt(request.getParameter("experience_start").toString()));
		ucCurrJobs.setCj_experience_end(Integer.parseInt(request.getParameter("experience_end").toString()));
		ucCurrJobs.setCj_salary((request.getParameter("salary").equals("")) ? "0" : request.getParameter("salary").toString());
		ucCurrJobs.setCj_duty_hours((request.getParameter("duty-hours").equals("")) ? "0" : request.getParameter("duty-hours").toString());
		
		Hashtable sessionAttributes = new Hashtable();
		sessionAttributes = (Hashtable) SessionManager.sessionExists(request, "admin");
		ucCurrJobs.setCj_added_by(Long.parseLong(sessionAttributes.get("uuid").toString()));
		if(Integer.parseInt(sessionAttributes.get("utype").toString()) == UCConstants.MAIN_ADMIN) {
			ucCurrJobs.setCj_is_active(1);
			ucCurrJobs.setCj_approved(1);
		}
		
		
		
		if(request.getParameter("no_gender") == null) {
			noGender = 0;
			if(!request.getParameter("male_quantity").equals("")) {
				ucCjQuantity.setCjq_male(Integer.parseInt(request.getParameter("male_quantity").toString()));
			} else {
				ucCjQuantity.setCjq_male(0);
			}
			if(!request.getParameter("female_quantity").equals("")) {
				ucCjQuantity.setCjq_female(Integer.parseInt(request.getParameter("female_quantity").toString()));
			} else {
				ucCjQuantity.setCjq_female(0);
			}
			ucCurrJobs.setCj_quantity(0);
		} else {
			noGender = 1;
			if(!request.getParameter("total_quantity").equals("")) {
				ucCurrJobs.setCj_quantity(Integer.parseInt(request.getParameter("total_quantity").toString()));
			} else {
				ucCurrJobs.setCj_quantity(0);
			}
		}
		
		/*ucIndustry_list = new ArrayList<Uc_industry_sector>();
		ucIndustry_list = (List<Uc_industry_sector>)(Object) model.selectData("model.Uc_industry_sector", "is_name='" + request.getParameter("industry-sector").toString() + "' LIMIT 1");
		if(ucIndustry_list.isEmpty()) {
			ucIndustry = new Uc_industry_sector();
			ucIndustry.setIs_name(request.getParameter("industry-sector").toString());
			List<Long> keys = model.insertDataReturnGeneratedKeys(ucIndustry);
			if(keys != null) industry = keys.get(0).intValue();
			else SessionManager.setSession(request, "error", "Something went wrong while saving job details. Please try again.");
		} else {
			industry = ucIndustry_list.get(0).getIs_id();
		}*/
		
		
			/*ucPosition_list = new ArrayList<Uc_position>();
			ucPosition_list = (List<Uc_position>)(Object) model.selectData("model.Uc_position", "p_name='" + request.getParameter("industry-post").toString() + "' LIMIT 1");
			if(ucPosition_list.isEmpty()) {
				ucPosition = new Uc_position();
				ucPosition.setP_name(request.getParameter("industry-post").toString());
				List<Long> keys = model.insertDataReturnGeneratedKeys(ucPosition);
				if(keys != null) position = keys.get(0).intValue();
				else SessionManager.setSession(request, "error", "Something went wrong while saving job details. Please try again.");
			} else {
				position = ucPosition_list.get(0).getP_id();
			}*/
			
		
		position = commonHelper.savePosition(request);
		if(position != -1) {
		
			ucCurrJobs.setCj_position(position);
			
			/*int user_state = Integer.parseInt(request.getParameter("user_state").toString());
			String user_district_other, user_city_other;
			ucState_list = new ArrayList<Uc_state>();
			ucState_list = (List<Uc_state>)(Object) model.selectData("model.Uc_state", "s_id=" + user_state);
			String state_name = ucState_list.get(0).getS_name();
			if(state_name.toLowerCase().equals("gujarat")) {
				user_city = Integer.parseInt(request.getParameter("user_city").toString());
			} else {
				user_district_other = request.getParameter("user_district_other").toString();
				user_city_other = request.getParameter("user_city_other").toString();
				ucDistrict = new Uc_district();
				ucDistrict.setD_s_id(user_state);
				ucDistrict.setD_name(user_district_other);
				List<Long> keys = model.insertDataReturnGeneratedKeys(ucDistrict);
				if(keys != null) {
					int dId = keys.get(0).intValue();
					ucCity = new Uc_city();
					ucCity.setC_district(dId);
					ucCity.setC_name(user_city_other);
					List<Long> citykeys = model.insertDataReturnGeneratedKeys(ucCity);
					if(citykeys != null) {
						user_city = citykeys.get(0).intValue();
					} else {
						SessionManager.setSession(request, "error", "Something went wrong while saving job details. Please try again.");
					}
				} else {
					SessionManager.setSession(request, "error", "Something went wrong while saving job details. Please try again.");
				}
			}*/
			
			user_city = commonHelper.saveCity(request);
		}
		
		if(user_city != -1) {
			ucCurrJobs.setCj_city(user_city);
			if(request.getParameter("featured_avail").toLowerCase().equals("true")) {
				if(request.getParameter("featured") != null) ucCurrJobs.setCj_is_featured(1);
				else ucCurrJobs.setCj_is_featured(0);
			} else {
				ucCurrJobs.setCj_is_featured(0);
			}
			ucCurrJobs.setCj_work_profile(request.getParameter("workprofile").toString());
			Date date = new Date(Calendar.getInstance().getTime().getTime());
			//SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
			//ucCurrJobs.setCj_posted_at(dateFormat.format(date));
			ucCurrJobs.setCj_posted_at(date);
			model.startTransaction();
			
				List<Long> keys = model.insertDataReturnGeneratedKeys(ucCurrJobs);
				if(keys != null) {
					if(facilities != null && facilities.length > 0) {
						for(String facility : facilities) {
							Uc_job_facilities_mapping ucJobFacilitiesMapping = new Uc_job_facilities_mapping();
							ucJobFacilitiesMapping.setJfm_current_opening_id(keys.get(0));
							ucJobFacilitiesMapping.setJfm_job_facilities_id(Long.parseLong(facility));
							model.insertData(ucJobFacilitiesMapping);
						}
					}
					int quali_count = Integer.parseInt(request.getParameter("qualification_count"));
					for(int i=1 ; i<=quali_count ; i++) {
						if(request.getParameter("qualification_required" + i) != null) {
							ucJobQuali = new Uc_job_qualifications();
							ucQuali_list = new ArrayList<Uc_qualifications>();
							ucQuali_list = (List<Uc_qualifications>)(Object) model.selectData("model.Uc_qualifications", "q_name='" + request.getParameter("qualification_required" + i).toString() + "' LIMIT 1");
							if(ucQuali_list.isEmpty()) {
								ucQuali = new Uc_qualifications();
								ucQuali.setQ_name(request.getParameter("qualification_required" + i).toString());
								List<Long> qualikeys = model.insertDataReturnGeneratedKeys(ucQuali);
								if(qualikeys != null) {
									ucJobQuali.setJq_qualification_id(Long.parseLong(qualikeys.get(0).toString()));
									ucJobQuali.setJq_job_id(keys.get(0));
								} else {
									SessionManager.setSession(request, "error", "Something went wrong while saving your details. Please try again.");
								}
							} else {
								ucJobQuali.setJq_qualification_id(ucQuali_list.get(0).getQ_id());
								ucJobQuali.setJq_job_id(keys.get(0));
							}
							if(!model.insertData(ucJobQuali)) {
								SessionManager.setSession(request, "error", "Something went wrong while saving job details. Please try again.");
							}
						}
					}
					if(Integer.parseInt(sessionAttributes.get("utype").toString()) == UCConstants.SALES_ADMIN) {
						ucAdminCjPermissions = new Uc_admin_cj_permissions();
						ucAdminCjPermissions.setAcjp_admin_id(Long.parseLong(sessionAttributes.get("uuid").toString()));
						ucAdminCjPermissions.setAcjp_cj_id(keys.get(0));
						if(!model.insertData(ucAdminCjPermissions)) {
							SessionManager.setSession(request, "error", "Something went wrong while saving employer details. Please try again.");
						}
					}
					if(noGender == 0) {
						ucCjQuantity.setCjq_job_id(keys.get(0));
						if(model.insertData(ucCjQuantity)) {
							SessionManager.setSession(request, "success", "Job details successfully saved.");
						} else {
							SessionManager.setSession(request, "error", "Something went wrong while saving job details. Please try again.");
						}
					} else {
						SessionManager.setSession(request, "success", "Job details successfully saved.");
					}
				} else {
					SessionManager.setSession(request, "error", "Something went wrong while saving job details. Please try again.");
				}
			model.endTransaction();
		}
		}
	}
	
	private void getCurrentOpenings(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException, JSONException {
		int pageNo = Integer.parseInt(request.getParameter("pageNo").toString());
		int limit = Integer.parseInt(request.getParameter("limit").toString());
		boolean begin = request.getParameter("begin").toLowerCase().equals("true") ? true : false;
		int start = (pageNo-1)*limit;
		Hashtable sessionAttributes = new Hashtable();
		sessionAttributes = (Hashtable) SessionManager.sessionExists(request, "admin");
		StringBuilder where = new StringBuilder();
		boolean hasExtra = false;
		int count = 0;
		String postfix = "";
		
		if(request.getParameter("extraParameters") != "" && request.getParameter("extraParameters") == null) {
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
					/*ucPosition_list = new ArrayList<Uc_position>();
					ucPosition_list = (List<Uc_position>)(Object) model.selectData("model.Uc_position", "p_name like '%" + val2 + "%'");
					if(!ucPosition_list.isEmpty()) {
						String temppostfix = "(";
						for(Uc_position pos : ucPosition_list) {
							where.append(temppostfix);
							temppostfix = " or ";
							where.append("cj_position=" + pos.getP_id());
						}
						where.append(")");
					}*/
					where.append("cj_position=" + val2);
				} else if(val1.equals("3")) {
					where.append("cj_employer_id=" + Integer.parseInt(val2));
				} else if(val1.equals("4")) {
					ucEmployer_list = new ArrayList<Uc_employer_details>();
					ucEmployer_list = (List<Uc_employer_details>)(Object) model.selectData("model.Uc_employer_details", "ed_firm_name like '%" + val2 + "%'");
					if(!ucEmployer_list.isEmpty()) {
						String temppostfix = "(";
						for(Uc_employer_details emp : ucEmployer_list) {
							where.append(temppostfix);
							temppostfix = " or ";
							where.append("cj_employer_id=" + emp.getEd_id());
						}
						where.append(")");
					}
				} else if(val1.equals("5")) {
					//location
					
					char c = val2.charAt(0);
					if(c == '*') {
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
															cityWhere.append("cj_city=" + cty.getC_id());
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
															cityWhere.append("cj_city=" + cty.getC_id());
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
															cityWhere.append("cj_city=" + cty.getC_id());
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
															cityWhere.append("cj_city=" + cty.getC_id());
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
															cityWhere.append("cj_city=" + cty.getC_id());
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
															cityWhere.append("cj_city=" + cty.getC_id());
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
															cityWhere.append("cj_city=" + cty.getC_id());
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
															cityWhere.append("cj_city=" + cty.getC_id());
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
						
						/*if(!city.equals("0")) {
							ucCity_list = new ArrayList<Uc_city>();
							if(!district.equals("0")) {
								ucDistrict_list = new ArrayList<Uc_district>();
								ucDistrict_list = (List<Uc_district>)(Object) model.selectData("model.Uc_district", "d_name like '%" + district + "%' and d_s_id=" + Integer.parseInt(state));
								if(!ucDistrict_list.isEmpty()) {
									StringBuilder ds = new StringBuilder();
									String temppostfix = "(";
									for(Uc_district d : ucDistrict_list) {
										ds.append(temppostfix);
										temppostfix = " or ";
										ds.append("c_district=" + d.getD_id());
									}
									ds.append(")");
									ucCity_list = (List<Uc_city>)(Object) model.selectData("model.Uc_city", "c_name like '%" + city + "%' and " + ds.toString());
								}
							} else {
								ucCity_list = (List<Uc_city>)(Object) model.selectData("model.Uc_city", "c_name like '%" + city + "%'");
							}
							if(!ucCity_list.isEmpty()) {
								String temppostfix = "(";
								for(Uc_city ct : ucCity_list) {
									where.append(temppostfix);
									temppostfix = " or ";
									where.append("ed_city_id=" + ct.getC_id());
								}
								where.append(")");
							}
						} else if(!district.equals("0")) {
							ucDistrict_list = new ArrayList<Uc_district>();
							ucDistrict_list = (List<Uc_district>)(Object) model.selectData("model.Uc_district", "d_name like '%" + district + "%' and d_s_id=" + Integer.parseInt(state));
							if(!ucDistrict_list.isEmpty()) {
								StringBuilder ds = new StringBuilder();
								String temppostfix = "(";
								for(Uc_district d : ucDistrict_list) {
									ds.append(temppostfix);
									temppostfix = " or ";
									ds.append("c_district=" + d.getD_id());
								}
								ds.append(")");
								ucCity_list = new ArrayList<Uc_city>();
								ucCity_list = (List<Uc_city>)(Object) model.selectData("model.Uc_city", ds.toString());
								if(!ucCity_list.isEmpty()) {
									temppostfix = "(";
									for(Uc_city ct : ucCity_list) {
										where.append(temppostfix);
										temppostfix = " or ";
										where.append("ed_city_id=" + ct.getC_id());
									}
									where.append(")");
								}
							}
						} else {
						
							ucDistrict_list = new ArrayList<Uc_district>();
							ucDistrict_list = (List<Uc_district>)(Object) model.selectData("model.Uc_district", "d_s_id=" + Integer.parseInt(state));
							if(!ucDistrict_list.isEmpty()) {
								String temppostfix = "(";
								StringBuilder ds = new StringBuilder();
								for(Uc_district d : ucDistrict_list) {
									ds.append(temppostfix);
									temppostfix = " or ";
									ds.append("c_district=" + d.getD_id());
								}
								ds.append(")");
								ucCity_list = new ArrayList<Uc_city>();
								ucCity_list = (List<Uc_city>)(Object) model.selectData("model.Uc_city", ds.toString());
								if(!ucCity_list.isEmpty()) {
									temppostfix = "(";
									for(Uc_city ct : ucCity_list) {
										where.append(temppostfix);
										temppostfix = " or ";
										where.append("ed_city_id=" + ct.getC_id());
									}
									where.append(")");
								}
							}
						}*/
					} else {
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
															cityWhere.append("cj_city=" + cty.getC_id());
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
															cityWhere.append("cj_city=" + cty.getC_id());
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
															cityWhere.append("cj_city=" + cty.getC_id());
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
															cityWhere.append("cj_city=" + cty.getC_id());
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
															cityWhere.append("cj_city=" + cty.getC_id());
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
															cityWhere.append("cj_city=" + cty.getC_id());
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
															cityWhere.append("cj_city=" + cty.getC_id());
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
															cityWhere.append("cj_city=" + cty.getC_id());
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
						
						/*if(!city.equals("0")) {
							where.append("ed_city_id=" + Integer.parseInt(city));
						} else if(!district.equals("0")) {
							ucCity_list = new ArrayList<Uc_city>();
							ucCity_list = (List<Uc_city>)(Object) model.selectData("model.Uc_city", "c_district=" + Integer.parseInt(district));
							if(!ucCity_list.isEmpty()) {
								String temppostfix = "(";
								for(Uc_city ct : ucCity_list) {
									where.append(temppostfix);
									temppostfix = " or ";
									where.append("ed_city_id=" + ct.getC_id());
								}
								where.append(")");
							}
						} else {
							ucDistrict_list = new ArrayList<Uc_district>();
							ucDistrict_list = (List<Uc_district>)(Object) model.selectData("model.Uc_district", "d_s_id=" + Integer.parseInt(state));
							if(!ucDistrict_list.isEmpty()) {
								String temppostfix = "(";
								StringBuilder ds = new StringBuilder();
								for(Uc_district d : ucDistrict_list) {
									ds.append(temppostfix);
									temppostfix = " or ";
									ds.append("c_district=" + d.getD_id());
								}
								ds.append(")");
								ucCity_list = new ArrayList<Uc_city>();
								ucCity_list = (List<Uc_city>)(Object) model.selectData("model.Uc_city", ds.toString());
								if(!ucCity_list.isEmpty()) {
									temppostfix = "(";
									for(Uc_city ct : ucCity_list) {
										where.append(temppostfix);
										temppostfix = " or ";
										where.append("ed_city_id=" + ct.getC_id());
									}
									where.append(")");
								}
							}
						}*/
					}
				}
			}
		} else {
			where.append("1");
		}
		
		where.append(" and ");
		
		if(request.getParameter("isFiltered").toLowerCase().equals("true")) {
			postfix = "";
			String[] filters = request.getParameterValues("filters[]");
			for(String filter : filters) {
				where.append(postfix);
				postfix = " or ";
				if(filter.equals("1")) {
					where.append("cj_is_active=1");
				} else if(filter.equals("2")) {
					where.append("cj_is_active=0");
				}
			}
		} else {
			where.append("1");
		}
		
		if(hasExtra && where.toString().equals("")) {
			response.setContentType("application/json");
			JSONObject jsonResponse = new JSONObject();
			jsonResponse.put("table", "No current openings are added yet.");
			jsonResponse.put("pageCount", 0);
			response.getWriter().write(jsonResponse.toString());
		} else {
			if(Integer.parseInt(sessionAttributes.get("utype").toString()) == UCConstants.SALES_ADMIN) {
				ucAdminCjPermissions_list = new ArrayList<Uc_admin_cj_permissions>();
				ucAdminCjPermissions_list = (List<Uc_admin_cj_permissions>)(Object) model.selectData("model.Uc_admin_cj_permissions", "acjp_admin_id=" + Integer.parseInt(sessionAttributes.get("uuid").toString()));
				if(!ucAdminCjPermissions_list.isEmpty()) {
					where.append(" and (");
					String temppostfix = "";
					for(Uc_admin_cj_permissions acjp : ucAdminCjPermissions_list) {
						where.append(temppostfix);
						temppostfix = " or ";
						where.append("cj_id=" + acjp.getAcjp_cj_id());
					}
					where.append(")");
				} else {
					where.append(" and 0");
				}
			}
			ucCurrJobs_list = new ArrayList<Uc_current_jobs>();
			ucCurrJobs_list = (List<Uc_current_jobs>)(Object) model.selectData("model.Uc_current_jobs", where.toString() + " order by cj_id desc LIMIT " + start + "," + limit);
			if(ucCurrJobs_list.isEmpty()) {
				response.setContentType("application/json");
				JSONObject jsonResponse = new JSONObject();
				jsonResponse.put("table", "No current openings are added yet.");
				jsonResponse.put("pageCount", 0);
				response.getWriter().write(jsonResponse.toString());
			} else {
				StringBuilder temp = new StringBuilder();
				if(Integer.parseInt(sessionAttributes.get("utype").toString()) == UCConstants.MAIN_ADMIN) {
					temp.append("<table class=\"table table-bordered\" width=\"100%\"><tr><th>Sr. No.</th><th>Job Id</th><th>Employer Id</th><th>Employer</th><th>Position</th><th>Requirement</th><th>Posted at</th><th>Added by</th><th>Modify</th></tr>");
				} else {
					temp.append("<table class=\"table table-bordered\" width=\"100%\"><tr><th>Sr. No.</th><th>Job Id</th><th>Employer Id</th><th>Employer</th><th>Position</th><th>Requirement</th><th>Posted at</th><th>Modify</th></tr>");
				}
				for(int i = 0 ; i < ucCurrJobs_list.size() ; i++) {
					ucEmployer = new Uc_employer_details();
					ucEmployer = ((Uc_employer_details) model.selectData("model.Uc_employer_details", "ed_id=" + ucCurrJobs_list.get(i).getCj_employer_id()).get(0));
					String position = ((Uc_position) model.selectData("model.Uc_position", "p_id=" + ucCurrJobs_list.get(i).getCj_position()).get(0)).getP_name();
					String postedAt = ucCurrJobs_list.get(i).getCj_posted_at().toString();
					String bgColor = "#000";
					if(ucCurrJobs_list.get(i).getCj_approved() == 1 && ucCurrJobs_list.get(i).getCj_is_active() == 1) {
						bgColor = UCConstants.APPROVED_ACTIVE;
					} else if(ucCurrJobs_list.get(i).getCj_approved() == 1 && ucCurrJobs_list.get(i).getCj_is_active() == 0) {
						bgColor = UCConstants.APPROVED_NOTACTIVE;
					} else if(ucCurrJobs_list.get(i).getCj_approved() == 0 && ucCurrJobs_list.get(i).getCj_is_active() == 0) {
						bgColor = UCConstants.NOTAPPROVED_NOTACTIVE;
					} else if(ucCurrJobs_list.get(i).getCj_approved() == 2 && ucCurrJobs_list.get(i).getCj_is_active() == 0) {
						bgColor = UCConstants.PENDING_NOTACTIVE;
					}
					temp.append("<tr id=\"job_row" + ucCurrJobs_list.get(i).getCj_id() + "\" style=\"background:" + bgColor + ";color:#fff\">");
					if(Integer.parseInt(sessionAttributes.get("utype").toString()) == UCConstants.MAIN_ADMIN) {
						temp.append("<td><label><input type=\"checkbox\" name=\"job_check\" value=\"" + ucCurrJobs_list.get(i).getCj_id() + "\"> &nbsp;" + (i+1) + "</label></td>");
					} else {
						temp.append("<td>" + (i+1) + "</td>");
					}
					temp.append("<td>" + ucCurrJobs_list.get(i).getCj_id() + "</td>");
					temp.append("<td>" + ucEmployer.getEd_id() + "</td>");
					temp.append("<td>" + ucEmployer.getEd_firm_name() + "</td>");
					temp.append("<td>" + position + "</td>");
					if(ucCurrJobs_list.get(i).getCj_quantity() == 0) {
						//ucCjQuantity = new Uc_cj_quantity();
						//ucCjQuantity = ((Uc_cj_quantity) model.selectData("model.Uc_cj_quantity", "cjq_job_id=" + ucCurrJobs_list.get(i).getCj_id()).get(0));
						ucCjQuantity_list = new ArrayList<Uc_cj_quantity>();
						ucCjQuantity_list = (List<Uc_cj_quantity>)(Object) model.selectData("model.Uc_cj_quantity", "cjq_job_id=" + ucCurrJobs_list.get(i).getCj_id());
						if(!ucCjQuantity_list.isEmpty()) {
							ucCjQuantity = new Uc_cj_quantity();
							ucCjQuantity = ucCjQuantity_list.get(0);
							temp.append("<td>" + ucCjQuantity.getCjq_male() + " M / " + ucCjQuantity.getCjq_female() + " F</td>");
						} else {
							temp.append("<td>N/A</td>");
						}
					} else {
						temp.append("<td>" + ucCurrJobs_list.get(i).getCj_quantity() + " (M or F)</td>");
					}
					temp.append("<td>" + postedAt + "</td>");
					if(Integer.parseInt(sessionAttributes.get("utype").toString()) == UCConstants.MAIN_ADMIN) {
						ucAdmin = new Uc_admin();
						ucAdmin = (Uc_admin) model.selectData("model.Uc_admin", "a_id=" + ucCurrJobs_list.get(i).getCj_added_by()).get(0);
						temp.append("<td>" + ucAdmin.getA_username() + "</td>");
						}
					temp.append("<td><button type=\"button\" onclick=\"modifyCurrentJob(this," + ucCurrJobs_list.get(i).getCj_id() + ")\" class=\"btn btn-info\">Modify</button></td>");
					
					temp.append("</tr>");
					//remaining from here
				}
				temp.append("</table>");
				if(Integer.parseInt(sessionAttributes.get("utype").toString()) == UCConstants.MAIN_ADMIN) {
					temp.append("<button type=\"button\" onclick=\"activateJobs(this)\" class=\"btn btn-success\" style=\"margin:5px;\">Activate</button>");
					temp.append("<button type=\"button\" onclick=\"deactivateJobs(this)\" class=\"btn btn-danger\" style=\"margin:5px\">De-Activate</button>");
					temp.append("<button type=\"button\" onclick=\"approveJobs(this)\" class=\"btn btn-success\" style=\"margin:5px\">Approve</button>");
					temp.append("<button type=\"button\" onclick=\"disapproveJobs(this)\" class=\"btn btn-danger\" style=\"margin:5px\">Dis-Approve</button>");
					temp.append("<button type=\"button\" onclick=\"deleteJobs(this)\" class=\"btn btn-danger\" style=\"margin:5px\">Delete</button>");
				}
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
		
		
	}
	
	private void modifySingle(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException, JSONException {
		int id = Integer.parseInt(request.getParameter("id"));
		Hashtable sessionAttributes = new Hashtable();
		sessionAttributes = (Hashtable) SessionManager.sessionExists(request, "admin");
		ucCurrJobs = new Uc_current_jobs();
		ucCurrJobs = (Uc_current_jobs) model.selectData("model.Uc_current_jobs", "cj_id=" + id).get(0);
		StringBuilder temp = new StringBuilder();
		temp.append("<form action=\""+ RouteManager.getBasePath() +"admin/current-openings?action=saveChanges\" method=\"POST\">");
		temp.append("<input type=\"hidden\" name=\"currJobId\" value=\"" + ucCurrJobs.getCj_id() + "\">");
		temp.append("<div class=\"form-group col-lg-12\">");
			
			temp.append("<div class=\"col-lg-6\">");
			ucPosition = new Uc_position();
			ucPosition = (Uc_position) model.selectData("model.Uc_position", "p_id=" + ucCurrJobs.getCj_position()).get(0);
				temp.append("Position:<input type=\"text\" class=\"form-control\" value=\"" + ucPosition.getP_name() + "\" name=\"industry-post\" id=\"industry-post\" placeholder=\"Post or position\">");
			temp.append("</div>");
			temp.append("<div class=\"col-lg-3\">");
				temp.append("Experience start point:<input type=\"number\" class=\"form-control\" value=\"" + ucCurrJobs.getCj_experience_start() + "\" id=\"experience_start\" name=\"experience_start\" placeholder=\"Experience starting point\">");
			temp.append("</div>");
			temp.append("<div class=\"col-lg-3\">");
			temp.append("Experience end point:<input type=\"number\" class=\"form-control\" value=\"" + ucCurrJobs.getCj_experience_end() + "\" id=\"experience_end\" name=\"experience_end\" placeholder=\"Experience ending point\">");
		temp.append("</div>");
		temp.append("</div>");
		temp.append("<div class=\"form-group col-lg-12\">");
			temp.append("<div class=\"col-lg-6\">");
				temp.append("Duty hours:<input type=\"text\" value=\"" + ucCurrJobs.getCj_duty_hours() + "\" class=\"form-control\" name=\"duty-hours\" placeholder=\"Duty hours\">");
			temp.append("</div>");
			temp.append("<div class=\"col-lg-6\">");
				temp.append("Salary:<input type=\"text\" value=\"" + ucCurrJobs.getCj_salary() + "\" class=\"form-control\" name=\"salary\" placeholder=\"Expected salary\">");
			temp.append("</div>");
		temp.append("</div>");
		
		temp.append("<div class=\"form-group col-lg-12\">");
			temp.append("<div class=\"col-lg-6\">");
			int males = 0, females = 0, total;
			total = ucCurrJobs.getCj_quantity();
			if(total == 0) {
				//ucCjQuantity = new Uc_cj_quantity();
				//ucCjQuantity = (Uc_cj_quantity) model.selectData("model.Uc_cj_quantity", "cjq_job_id=" + ucCurrJobs.getCj_id()).get(0);
				ucCjQuantity_list = new ArrayList<Uc_cj_quantity>();
				ucCjQuantity_list = (List<Uc_cj_quantity>)(Object) model.selectData("model.Uc_cj_quantity", "cjq_job_id=" + ucCurrJobs.getCj_id());
				if(!ucCjQuantity_list.isEmpty()) {
					ucCjQuantity = new Uc_cj_quantity();
					ucCjQuantity = ucCjQuantity_list.get(0);
					males = ucCjQuantity.getCjq_male();
					females = ucCjQuantity.getCjq_female();
				}
			}
				temp.append("Males required:<input type=\"number\" value=\"" + (males==0 ? "" : males) + "\" class=\"form-control\" name=\"male_quantity\" id=\"male_quantity\" placeholder=\"Number of males required\">");
				
			temp.append("</div>");
			temp.append("<div class=\"col-lg-6\">");
				temp.append("Females required:<input type=\"number\" class=\"form-control\" value=\"" + (females==0 ? "" : females) + "\" name=\"female_quantity\" id=\"female_quantity\" placeholder=\"Number of females required\">");
			temp.append("</div>");
		temp.append("</div>");
		
		ucJobQuali_list = new ArrayList<Uc_job_qualifications>();
		ucJobQuali_list = (List<Uc_job_qualifications>)(Object) model.selectData("model.Uc_job_qualifications", "jq_job_id=" + ucCurrJobs.getCj_id());
		
		temp.append("<div class=\"form-group col-lg-12\">");
			temp.append("<div class=\"col-lg-6\">");
				temp.append("<div class=\"col-lg-10\">");
					temp.append("<input type=\"text\" class=\"form-control\" autocomplete=\"off\" spellcheck=\"false\" id=\"qualification_required\" name=\"qualification_required\" placeholder=\"Qualification Required\">");
				temp.append("</div>");
				temp.append("<div class=\"col-lg-2\">");
					temp.append("<button type=\"button\" onclick=\"add_qualification_tag()\" class=\"btn btn-primary\"><i class=\"fa fa-plus\"></i></button>");
				temp.append("</div>");
			temp.append("</div>");
			temp.append("<div class=\"col-lg-6\" id=\"qualification-tags\">");	
			for(int i = 1 ; i<=ucJobQuali_list.size() ; i++) {
				ucQuali = new Uc_qualifications();
				ucQuali = (Uc_qualifications) model.selectData("model.Uc_qualifications", "q_id=" + ucJobQuali_list.get((i-1)).getJq_qualification_id() + " LIMIT 1").get(0);
				temp.append("<div class=\"single-tag\" style=\"display:inline-block;margin-left:10px\" id=\"q_tag_count" + i + "\"> &nbsp;<a onclick=\"remove_q_tag(" + i + ")\" style=\"cursor:pointer\"><i class=\"fa fa-close\" style=\"color: #790000\"></i></a> &nbsp;<label id=\"qualification_required" + i + "\">" + ucQuali.getQ_name() + "</label><input type=\"hidden\" id=\"qualification_required_hidden" + i + "\" name=\"qualification_required" + i + "\" value=\"" + ucQuali.getQ_name() + "\"></div>");
			}
			temp.append("</div>");
			temp.append("<input type=\"hidden\" name=\"qualification_count\" value=\"" + ucJobQuali_list.size() + "\" id=\"qualification_count\">");
		temp.append("</div>");
		
		temp.append("<div class=\"form-group col-lg-12\">");
			temp.append("<div class=\"col-lg-6\">");
			if(total != 0) {
				temp.append("<label><input type=\"checkbox\" checked name=\"no_gender\" id=\"no_gender\" value=\"0\"> Check this if no gender information available</label>");
			} else {
				temp.append("<label><input type=\"checkbox\" name=\"no_gender\" id=\"no_gender\" value=\"0\"> Check this if no gender information available</label>");
			}
			temp.append("</div>");
			temp.append("<div class=\"col-lg-6\">");
				temp.append("Total number of persons required:<input type=\"number\" value=\"" + ((total == 0) ? "" : total) + "\" class=\"form-control\" id=\"total_quantity\" name=\"total_quantity\" placeholder=\"Total number of persons required\">");
			temp.append("</div>");
		temp.append("</div>");
		temp.append("<div class=\"col-lg-12 form-group\">");
			temp.append("<div class=\"col-lg-6\">");
			ucCity = new Uc_city();
			ucCity = (Uc_city) model.selectData("model.Uc_city", "c_id=" + ucCurrJobs.getCj_city()).get(0);
			ucTaluka = new Uc_taluka();
			ucTaluka = (Uc_taluka) model.selectData("model.Uc_taluka", "t_id=" + ucCity.getC_taluka_id()).get(0);
			ucDistrict = new Uc_district();
			ucDistrict = (Uc_district) model.selectData("model.Uc_district", "d_id=" + ucTaluka.getT_district_id()).get(0);
			ucState = new Uc_state();
			ucState = (Uc_state) model.selectData("model.Uc_state", "s_id=" + ucDistrict.getD_s_id()).get(0);
			temp.append("<b>Location: </b> " + ucCity.getC_name() + ", " + ucTaluka.getT_name() + ", " + ucDistrict.getD_name() + ", " + ucState.getS_name());
			temp.append("<select class=\"form-control\" name=\"user_state\" id=\"user_state\" onchange=\"state_selected(this)\">");
			temp.append("<option selected hidden value=\"select\">Select state</option>");
			ucState_list = new ArrayList<Uc_state>();
			ucState_list = (List<Uc_state>)(Object) model.selectData("model.Uc_state", "1");
			for(Uc_state state : ucState_list) { 
				temp.append("<option value=" + state.getS_id() + ">" + state.getS_name() + "</option>");
			}
		temp.append("</select>");
			temp.append("</div>");
		temp.append("</div>");
		temp.append("<div class=\"gujarat-selected form-group col-lg-12\">");
			temp.append("<div class=\"col-lg-6\">");
				temp.append("<select class=\"form-control\" id=\"district_options\" name=\"user_district\" onchange=\"district_selected(this)\">");
					
				temp.append("</select>");
			temp.append("</div>");
			temp.append("<div class=\"col-lg-6\">");
			temp.append("<select class=\"form-control\" id=\"taluka_options\" name=\"user_taluka\" onchange=\"taluka_selected(this); disableError('taluka_options');\">");
				
			temp.append("</select>");
			temp.append("<label style=\"color:red;font-weight:normal;display:none\" id=\"taluka_options_error\"></label>");
		temp.append("</div>");
			temp.append("<div class=\"col-lg-6\">");
				temp.append("<select class=\"form-control\" id=\"city_options\" name=\"user_city\">");
			
				temp.append("</select>");
			temp.append("</div>");
		temp.append("</div>");
		temp.append("<div class=\"other-selected form-group col-lg-12\">");
			temp.append("<div class=\"col-lg-6\">");
				temp.append("<input type=\"text\" placeholder=\"District\" id=\"user_district_other\" name=\"user_district_other\" class=\"form-control\">");
			temp.append("</div>");
			temp.append("<div class=\"col-lg-6\">");
			temp.append("<input type=\"text\" placeholder=\"Taluka\" id=\"user_taluka_other\" name=\"user_taluka_other\" class=\"form-control\">");
		temp.append("</div>");
			temp.append("<div class=\"col-lg-6\">");
				temp.append("<input type=\"text\" placeholder=\"City / Village\" id=\"user_city_other\" name=\"user_city_other\" class=\"form-control\">");
			temp.append("</div>");
		temp.append("</div>");
		
		List<Uc_job_facilities> ucJobFacilities_list = new ArrayList<>();
		ucJobFacilities_list  = (List<Uc_job_facilities>)(Object) model.selectData("model.Uc_job_facilities", "1");
		List<Uc_job_facilities_mapping> ucJobFacilitiesMapping_list = new ArrayList<>();
		ucJobFacilitiesMapping_list  = (List<Uc_job_facilities_mapping>)(Object) model.selectData("model.Uc_job_facilities_mapping", "jfm_current_opening_id=" + ucCurrJobs.getCj_id());
		temp.append("<div class=\"form-group col-lg-12\">");
		temp.append("<div class=\"col-lg-12\">");
		boolean facilityFlag = false;
		for(Uc_job_facilities facility : ucJobFacilities_list) {
			if(ucJobFacilitiesMapping_list != null && !ucJobFacilitiesMapping_list.isEmpty()) {
				facilityFlag = false;
				for(Uc_job_facilities_mapping facilityMapping : ucJobFacilitiesMapping_list) {
					if(facilityMapping.getJfm_job_facilities_id() == facility.getJf_id()) {
						temp.append("&nbsp; <label><input type=\"checkbox\" name=\"facilities\" value=\"" + facility.getJf_id() + "\" checked> " + facility.getJf_name() + "</label>");
						facilityFlag = true;
						break;
					}
				}
				if(!facilityFlag) {
					temp.append("&nbsp; <label><input type=\"checkbox\" name=\"facilities\" value=\"" + facility.getJf_id() + "\"> " + facility.getJf_name() + "</label>");
				}
			} else {
				temp.append("&nbsp; <label><input type=\"checkbox\" name=\"facilities\" value=\"" + facility.getJf_id() + "\"> " + facility.getJf_name() + "</label>");
			}
		}
		temp.append("</div></div>");
		
		temp.append("<div class=\"form-group col-lg-12\">");
			temp.append("<div class=\"col-lg-12\">");
				temp.append("<span id=\"message_msg\" style=\"color:red\"></span>");
				temp.append("Workprofile:<textarea id=\"workprofile\" name=\"workprofile\" class=\"form-control\" rows=\"3\" maxlength=\"300\" placeholder=\"Work-profile\">" + ucCurrJobs.getCj_work_profile() + "</textarea>");
			temp.append("</div>");
		temp.append("</div>");
		ucCurrJobs_list = new ArrayList<Uc_current_jobs>();
		ucCurrJobs_list = (List<Uc_current_jobs>)(Object) model.selectData("model.Uc_current_jobs", "cj_is_featured=1");
		if(ucCurrJobs.getCj_is_featured() == 1 || ucCurrJobs_list.size() < 6) {
		temp.append("<div class=\"form-group col-lg-12\">");
			temp.append("<div class=\"col-lg-12\">");
			if(ucCurrJobs.getCj_is_featured() == 1) {
				temp.append("<label><input name=\"featured\" value=\"1\" type=\"checkbox\" checked> Show this job as featured job on home page</label>");
			} else {
				temp.append("<label><input name=\"featured\" value=\"1\" type=\"checkbox\"> Show this job as featured job on home page</label>");
			}
			temp.append("</div>");
		temp.append("</div>");
		}
		/*if(ucCurrJobs_list.size() >= 6) {
			temp.append("<input type=\"hidden\" name=\"featured_avail\" value=\"false\">");
		} else {
			temp.append("<input type=\"hidden\" name=\"featured_avail\" value=\"true\">");
		}*/
		
		
		if(Integer.parseInt(sessionAttributes.get("utype").toString()) == UCConstants.MAIN_ADMIN) {
			
			temp.append("<div class=\"form-group col-lg-12\">");
			ucAdmin_list = new ArrayList<Uc_admin>();
			ucAdmin_list = (List<Uc_admin>)(Object) model.selectData("model.Uc_admin", "a_type=" + UCConstants.SALES_ADMIN);
			ucAdminTypes_list = new ArrayList<Uc_admin_types>();
			ucAdminTypes_list = (List<Uc_admin_types>)(Object) model.selectData("model.Uc_admin_types", "1");
			temp.append("<div class=\"col-lg-6\">");
			temp.append("Admins to view this job requirement:");
			temp.append("<div class=\"col-lg-11\">");
			temp.append("<select class=\"form-control\" id=\"admin_list\">");
			temp.append("<option value=\"select\" hidden selected disabled>Select admin</option>");
			for(Uc_admin ad : ucAdmin_list) {
				String adminType = "";
				for(Uc_admin_types adt : ucAdminTypes_list) {
					if(adt.getAt_id() == ad.getA_type()) {
						adminType = adt.getAt_name();
						break;
					}
				}
				temp.append("<option value=\"" + ad.getA_id() + "\">" + ad.getA_username() + " (" + adminType + ")</option>");
			}
			temp.append("</select>");
			temp.append("</div>");
			temp.append("<div class=\"col-lg-1\">");
			temp.append("<button type=\"button\" class=\"btn btn-primary\" onclick=\"add_admin_permission(" + ucCurrJobs.getCj_id() + ")\"><i class=\"fa fa-plus\"></i></button>");
			temp.append("</div>");
			temp.append("</div>");
			temp.append("<div class=\"col-lg-6\" id=\"admin-tags\">");
			
			ucAdminCjPermissions_list = new ArrayList<Uc_admin_cj_permissions>();
			ucAdminCjPermissions_list = (List<Uc_admin_cj_permissions>)(Object) model.selectData("model.Uc_admin_cj_permissions", "acjp_cj_id=" + ucCurrJobs.getCj_id());
			for(int i=1 ; i<=ucAdminCjPermissions_list.size() ; i++) {
				ucAdmin = new Uc_admin();
				ucAdmin = (Uc_admin) model.selectData("model.Uc_admin", "a_id=" + ucAdminCjPermissions_list.get(i-1).getAcjp_admin_id()).get(0);
				temp.append("<div class=\"single-tag\" style=\"display:inline-block;margin-left:10px\" id=\"a_tag_count" + i + "\"> &nbsp;<a onclick=\"remove_a_tag(" + i + ", " + ucCurrJobs.getCj_id() + ")\" style=\"cursor:pointer\"><i class=\"fa fa-close\" style=\"color: #790000\"></i></a> &nbsp;<label id=\"permitted_admin" + i + "\">" + ucAdmin.getA_username() + "</label><input type=\"hidden\" id=\"permitted_admin_hidden" + i + "\" name=\"permitted_admin" + i + "\" value=\"" + ucAdmin.getA_id() + "\"></div>");
			}
			
			temp.append("</div>");
			temp.append("<input type=\"hidden\" name=\"admin_count\" value=\"" + ucAdminCjPermissions_list.size() + "\" id=\"admin_count\">");
			temp.append("</div>");
			}
		
		
		
		temp.append("<div class=\"form-group col-lg-12\">");
			temp.append("<div class=\"col-lg-3\">");
				temp.append("<button type=\"submit\" class=\"btn btn-primary\">Save changes</button>");
			temp.append("</div>");
			temp.append("<div class=\"col-lg-3\">");
			temp.append("<button type=\"button\" onclick=\"javascript: location.reload();\" class=\"btn btn-warning\">Discard changes</button>");
		temp.append("</div>");
		temp.append("</div>");
		temp.append("</form>");
		response.setContentType("application/json");
		JSONObject jsonResponse = new JSONObject();
		jsonResponse.put("form", temp.toString());
		jsonResponse.put("currJobId", id);
		response.getWriter().write(jsonResponse.toString());
	}
	
	private void saveChanges(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		long currJobId = Long.parseLong(request.getParameter("currJobId").toString());
		commonHelper = new CommonHelper();
		ucCurrJobs = new Uc_current_jobs();
		String[] facilities = request.getParameterValues("facilities");
			long position = 0;
			if(!request.getParameter("industry-post").equals("")) {
				position = commonHelper.savePosition(request);
				if(position != -1) ucCurrJobs.setCj_position(position); 
			}
			//String experience = request.getParameter("experience").toString();
			//if(!experience.equals("")) ucCurrJobs.setCj_experience(experience);
			String experience_start = request.getParameter("experience_start");
			String experience_end = request.getParameter("experience_end");
			if(!experience_start.equals("") && !experience_end.equals("")) {
				if(Integer.parseInt(experience_end) >= Integer.parseInt(experience_start)) {
					ucCurrJobs.setCj_experience_start(Integer.parseInt(experience_start));
					ucCurrJobs.setCj_experience_end(Integer.parseInt(experience_end));
				}
			} else if(experience_start.equals("") && !experience_end.equals("")) {
				ucCurrJobs.setCj_experience_start(0);
				ucCurrJobs.setCj_experience_end(Integer.parseInt(experience_end));
			} else if(!experience_start.equals("") && experience_end.equals("")) {
				ucCurrJobs.setCj_experience_end(Integer.parseInt(experience_start));
				ucCurrJobs.setCj_experience_start(Integer.parseInt(experience_start));
			}
			String dutyHours = request.getParameter("duty-hours").toString();
			if(!dutyHours.equals("")) ucCurrJobs.setCj_duty_hours(dutyHours);
			String salary = request.getParameter("salary").toString();
			if(!salary.equals("")) ucCurrJobs.setCj_salary(salary);
			int noGender = 0;
			ucCjQuantity = new Uc_cj_quantity();
			if(request.getParameter("no_gender") == null) {
				noGender = 0;
				if(!request.getParameter("male_quantity").equals("")) {
					ucCjQuantity.setCjq_male(Integer.parseInt(request.getParameter("male_quantity").toString()));
				}
				if(!request.getParameter("female_quantity").equals("")) {
					ucCjQuantity.setCjq_female(Integer.parseInt(request.getParameter("female_quantity").toString()));
				}
				ucCurrJobs.setCj_quantity(0);
			} else {
				noGender = 1;
				if(!request.getParameter("total_quantity").equals("")) {
					ucCurrJobs.setCj_quantity(Integer.parseInt(request.getParameter("total_quantity").toString()));
				}
			}
			String state = request.getParameter("user_state").toString();
			if(!state.toLowerCase().equals("select")) {
				String stateName = ((List<Uc_state>)(Object)model.selectData("model.Uc_state", "s_id=" + Integer.parseInt(state))).get(0).getS_name();
				if(stateName.toLowerCase().equals("other")) {
					String userDistrict = request.getParameter("user_district_other").toString();
					String userTaluka = request.getParameter("user_taluka_other").toString();
					String userCity = request.getParameter("user_city_other").toString();
					if(!userDistrict.equals("") && !userTaluka.equals("") && !userCity.equals("")) {
						long city = commonHelper.saveCity(request);
						ucCurrJobs.setCj_city(city);
					}
				} else {
					String district = request.getParameter("user_district").toString();
					if(!district.toLowerCase().equals("select")) {
						String taluka = request.getParameter("user_taluka").toString();
						if(!taluka.toLowerCase().equals("select")) {
							String city = request.getParameter("user_city").toString();
							if(!city.toLowerCase().equals("select")) {
								ucCurrJobs.setCj_city(Long.parseLong(city));
							}
						}
					}
				}
			}
			String workProfile = request.getParameter("workprofile").toString();
			if(!workProfile.equals("")) ucCurrJobs.setCj_work_profile(workProfile);
			//if(request.getParameter("featured_avail").toLowerCase().equals("true")) {
				if(request.getParameter("featured") != null) ucCurrJobs.setCj_is_featured(1);
				else ucCurrJobs.setCj_is_featured(0);
			//}
		model.startTransaction();
				if(model.updateData(ucCurrJobs, "cj_id=" + currJobId)) {
					if(model.deleteData("model.Uc_job_facilities_mapping", "jfm_current_opening_id=" + currJobId)) {
						if(facilities != null && facilities.length > 0) {
							for(String facility : facilities) {
								Uc_job_facilities_mapping ucJobFacilitiesMapping = new Uc_job_facilities_mapping();
								ucJobFacilitiesMapping.setJfm_current_opening_id(currJobId);
								ucJobFacilitiesMapping.setJfm_job_facilities_id(Long.parseLong(facility));
								model.insertData(ucJobFacilitiesMapping);
							}
						}
					}
					int count = Integer.parseInt(request.getParameter("qualification_count").toString());
					List<String> changedQualifications = new ArrayList<String>();
					for(int i=1 ; i<=count ; i++) {
						if(request.getParameter("qualification_required" + i) != null) {
							changedQualifications.add(request.getParameter("qualification_required" + i).toString());
						}
					}
					if(!changedQualifications.isEmpty()) {
						if(model.deleteData("model.Uc_job_qualifications", "jq_job_id=" + currJobId)) {
							for(String q : changedQualifications) {
								ucJobQuali = new Uc_job_qualifications();
								ucQuali_list = new ArrayList<Uc_qualifications>();
								ucQuali_list = (List<Uc_qualifications>)(Object) model.selectData("model.Uc_qualifications", "q_name='" + q + "' LIMIT 1");
								if(ucQuali_list.isEmpty()) {
									ucQuali = new Uc_qualifications();
									ucQuali.setQ_name(q);
									List<Long> qualikeys = model.insertDataReturnGeneratedKeys(ucQuali);
									if(qualikeys != null) {
										ucJobQuali.setJq_qualification_id(Long.parseLong(qualikeys.get(0).toString()));
										ucJobQuali.setJq_job_id(currJobId);
									} else {
										SessionManager.setSession(request, "error", "Something went wrong while saving your details. Please try again.");
									}
								} else {
									ucJobQuali.setJq_qualification_id(ucQuali_list.get(0).getQ_id());
									ucJobQuali.setJq_job_id(currJobId);
								}
								model.insertData(ucJobQuali);
							}
						} else {
							SessionManager.setSession(request, "error", "Something went wrong while saving changes. Please try again.");
						}
					}
					if(noGender == 0) {
						ucCjQuantity_list = new ArrayList<Uc_cj_quantity>();
						ucCjQuantity_list = (List<Uc_cj_quantity>)(Object) model.selectData("model.Uc_cj_quantity", "cjq_job_id=" + currJobId);
						if(!ucCjQuantity_list.isEmpty()) {
							if(model.updateData(ucCjQuantity, "cjq_job_id=" + currJobId)) {
								SessionManager.setSession(request, "success", "Job details successfully saved.");
							} else {
								SessionManager.setSession(request, "error", "Something went wrong while saving changes. Please try again.");
							}
						} else {
							ucCjQuantity.setCjq_job_id(currJobId);
							if(model.insertData(ucCjQuantity)) {
								SessionManager.setSession(request, "success", "Job details successfully saved.");
							} else {
								SessionManager.setSession(request, "error", "Something went wrong while saving changes. Please try again.");
							}
						}
					} else {
						SessionManager.setSession(request, "success", "Job details successfully saved.");
					}
				} else {
					SessionManager.setSession(request, "error", "Something went wrong while saving changes. Please try again.");
				}
		model.endTransaction();
		RouteManager.route(response, "admin/current-openings?action=modify");
	}
	
	private void activate(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException, JSONException {
		JSONArray json = new JSONArray(request.getParameter("jobIds").toString());
		boolean flag = true;
		model.startTransaction();
		for(int i=0 ; i<json.length() ; i++) {
			ucCurrJobs = new Uc_current_jobs();
			ucCurrJobs.setCj_is_active(1);
			ucCurrJobs.setCj_approved(1);
			if(!model.updateData(ucCurrJobs, "cj_id=" + json.getInt(i))) {
				flag = false;
				break;
			}
		}
		model.endTransaction();
		if(flag) {
			response.setContentType("text/html;charset=UTF-8");
			response.getWriter().write("success");
		} else {
			response.setContentType("text/html;charset=UTF-8");
			response.getWriter().write("error");
		}
	}
	
	private void deactivate(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException, JSONException {
		JSONArray json = new JSONArray(request.getParameter("jobIds").toString());
		boolean flag = true;
		model.startTransaction();
		for(int i=0 ; i<json.length() ; i++) {
			ucCurrJobs = new Uc_current_jobs();
			ucCurrJobs.setCj_is_active(0);
			if(!model.updateData(ucCurrJobs, "cj_id=" + json.getInt(i))) {
				flag = false;
				break;
			}
		}
		model.endTransaction();
		if(flag) {
			response.setContentType("text/html;charset=UTF-8");
			response.getWriter().write("success");
		} else {
			response.setContentType("text/html;charset=UTF-8");
			response.getWriter().write("error");
		}
	}
	
	private void approve(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException, JSONException {
		JSONArray json = new JSONArray(request.getParameter("jobIds").toString());
		boolean flag = true;
		model.startTransaction();
		for(int i=0 ; i<json.length() ; i++) {
			ucCurrJobs = new Uc_current_jobs();
			ucCurrJobs.setCj_approved(1);
			if(!model.updateData(ucCurrJobs, "cj_id=" + json.getInt(i))) {
				flag = false;
				break;
			}
		}
		model.endTransaction();
		if(flag) {
			response.setContentType("text/html;charset=UTF-8");
			response.getWriter().write("success");
		} else {
			response.setContentType("text/html;charset=UTF-8");
			response.getWriter().write("error");
		}
	}
	
	private void disapprove(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException, JSONException {
		JSONArray json = new JSONArray(request.getParameter("jobIds").toString());
		boolean flag = true;
		model.startTransaction();
		for(int i=0 ; i<json.length() ; i++) {
			ucCurrJobs = new Uc_current_jobs();
			ucCurrJobs.setCj_approved(0);
			ucCurrJobs.setCj_is_active(0);
			if(!model.updateData(ucCurrJobs, "cj_id=" + json.getInt(i))) {
				flag = false;
				break;
			}
		}
		model.endTransaction();
		if(flag) {
			response.setContentType("text/html;charset=UTF-8");
			response.getWriter().write("success");
		} else {
			response.setContentType("text/html;charset=UTF-8");
			response.getWriter().write("error");
		}
	}
	
	private void deleteJobs(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException, JSONException {
		JSONArray json = new JSONArray(request.getParameter("jobIds").toString());
		boolean flag = true;
		for(int i=0 ; i<json.length() ; i++) {
			if(!model.deleteData("model.Uc_current_jobs", "cj_id=" + json.getInt(i))) {
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
	
	private void addPermission(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException, JSONException {
		long jobId = Long.parseLong(request.getParameter("jobId").toString());
		long adminId = Long.parseLong(request.getParameter("adminId").toString());
		ucAdminCjPermissions = new Uc_admin_cj_permissions();
		ucAdminCjPermissions.setAcjp_admin_id(adminId);
		ucAdminCjPermissions.setAcjp_cj_id(jobId);
		if(model.insertData(ucAdminCjPermissions)) {
			response.setContentType("text/html;charset=UTF-8");
			response.getWriter().write("success");
		} else {
			response.setContentType("text/html;charset=UTF-8");
			response.getWriter().write("error");
		}
	}
	
	private void removePermission(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException, JSONException {
		int jobId = Integer.parseInt(request.getParameter("jobId").toString());
		int adminId = Integer.parseInt(request.getParameter("adminId").toString());
		if(model.deleteData("model.Uc_admin_cj_permissions", "acjp_admin_id=" + adminId + " and acjp_cj_id=" + jobId)) {
			response.setContentType("text/html;charset=UTF-8");
			response.getWriter().write("success");
		} else {
			response.setContentType("text/html;charset=UTF-8");
			response.getWriter().write("error");
		}
	}

	private void downloadJobs(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		
		StringBuilder temp = new StringBuilder();
		if(request.getParameter("jobStartRange") != null && !request.getParameter("jobStartRange").toString().equals("")) {
			if(request.getParameter("jobEndRange") != null && !request.getParameter("jobEndRange").toString().equals("")) {
				String postfix = "";
				for(int i=Integer.parseInt(request.getParameter("jobStartRange").toString().trim()) ; i<=Integer.parseInt(request.getParameter("jobEndRange").toString().trim()) ; i++) {
					temp.append(postfix);
					postfix = " or ";
					temp.append("cj_id=" + i);
				}
			} else {
				StringTokenizer st = new StringTokenizer(request.getParameter("jobStartRange").toString() ,",");
				String postfix = "";
				while(st.hasMoreTokens()) {
					int id = Integer.parseInt(st.nextToken().toString().trim());
					temp.append(postfix);
					postfix = " or ";
					temp.append("cj_id=" + id);
				}
			}
		}
		
		if(!temp.toString().equals("")) {
			
			ucCurrJobs_list = new ArrayList<Uc_current_jobs>();
	    	ucCurrJobs_list = (List<Uc_current_jobs>)(Object) model.selectData("model.Uc_current_jobs", temp.toString());
	    	
	    	if(!ucCurrJobs_list.isEmpty()) {
	    		response.setContentType("application/vnd.ms-excel");
				response.setHeader("Content-Disposition", "attachment; filename=requirementDetails.xls");
				XSSFWorkbook workbook = new XSSFWorkbook();
				XSSFSheet spreadsheet = workbook
					      .createSheet("REQUIREMENTS");
					      XSSFRow row=spreadsheet.createRow(0);
					      XSSFCell cell;
					      cell=row.createCell(0);
					      cell.setCellValue("REQUIREMENT ID");
					      cell=row.createCell(1);
					      cell.setCellValue("POSITION");
					      cell=row.createCell(2);
					      cell.setCellValue("EMPLOYER NAME");
					      cell=row.createCell(3);
					      cell.setCellValue("EMPLOYER CONTACT PERSON");
					      cell=row.createCell(4);
					      cell.setCellValue("EMPLOYER CONTACT NUMBER");
					      cell=row.createCell(5);
					      cell.setCellValue("EMPLOYER EMAIL ID");
					      cell=row.createCell(6);
					      cell.setCellValue("EXPERIENCE REQUIRED");
					      cell=row.createCell(7);
					      cell.setCellValue("DUTY HOURS");
					      cell=row.createCell(8);
					      cell.setCellValue("SALARY");
					      cell=row.createCell(9);
					      cell.setCellValue("CANDIDATE REQUIRED");
					      cell=row.createCell(10);
					      cell.setCellValue("QUALIFICATION");
					      cell=row.createCell(11);
					      cell.setCellValue("JOB LOCATION");
					      cell=row.createCell(12);
					      cell.setCellValue("WORKPROFILE");
					      
					      int i = 1;
					      for(Uc_current_jobs cj : ucCurrJobs_list) {
					    	  row=spreadsheet.createRow(i);
					          cell=row.createCell(0);
					          cell.setCellValue(cj.getCj_id());
					          ucPosition_list = new ArrayList<Uc_position>();
					          ucPosition_list = (List<Uc_position>)(Object) model.selectData("model.Uc_position", "p_id=" + cj.getCj_position());
					          cell=row.createCell(1);
					          cell.setCellValue(ucPosition_list.isEmpty() ? "N/A" : ucPosition_list.get(0).getP_name());
					          ucEmployer = new Uc_employer_details();
					          ucEmployer = (Uc_employer_details) model.selectData("model.Uc_employer_details", "ed_id=" + cj.getCj_employer_id()).get(0);
					          cell=row.createCell(2);
					          cell.setCellValue(ucEmployer==null ? "N/A" : ucEmployer.getEd_firm_name());
					          cell=row.createCell(3);
					          cell.setCellValue(ucEmployer==null ? "N/A" : ucEmployer.getEd_contact_person());
					          cell=row.createCell(4);
					          cell.setCellValue(ucEmployer==null ? 0 : ucEmployer.getEd_contact_no());
					          cell=row.createCell(5);
					          cell.setCellValue(ucEmployer==null ? "N/A" : ucEmployer.getEd_email());
					          cell=row.createCell(6);
					          cell.setCellValue((cj.getCj_experience_start() == cj.getCj_experience_end() ? cj.getCj_experience_start() + " years" : cj.getCj_experience_start() + "-" + cj.getCj_experience_end() + " years"));
					          cell=row.createCell(7);
					          cell.setCellValue(cj.getCj_duty_hours());
					          cell=row.createCell(8);
					          cell.setCellValue(cj.getCj_salary());
					          if(cj.getCj_quantity() == 0) {
					        	  ucCjQuantity_list = new ArrayList<Uc_cj_quantity>();
					        	  ucCjQuantity_list = (List<Uc_cj_quantity>)(Object) model.selectData("model.Uc_cj_quantity", "cjq_job_id=" + cj.getCj_id());
					        	  if(!ucCjQuantity_list.isEmpty()) {
					        		  ucCjQuantity = new Uc_cj_quantity();
					        		  ucCjQuantity = ucCjQuantity_list.get(0);
					        		  cell=row.createCell(9);
					        		  cell.setCellValue((ucCjQuantity.getCjq_male()) + " M & " + (ucCjQuantity.getCjq_female()) + " F");
					        	  }
					          } else {
					        	  cell=row.createCell(9);
					        	  cell.setCellValue(cj.getCj_quantity() + "M/F");
					          }
					          ucJobQuali_list = new ArrayList<Uc_job_qualifications>();
					          ucJobQuali_list = (List<Uc_job_qualifications>)(Object) model.selectData("model.Uc_job_qualifications", "jq_job_id=" + cj.getCj_id());
					          StringBuilder jqTemp = new StringBuilder();
					          if(!ucJobQuali_list.isEmpty()) {
					        	  String postfix = "";
					        	  for(Uc_job_qualifications jq : ucJobQuali_list) {
					        		  jqTemp.append(postfix);
					        		  postfix = ", ";
					        		  ucQuali_list = new ArrayList<Uc_qualifications>();
					        		  ucQuali_list = (List<Uc_qualifications>)(Object) model.selectData("model.Uc_qualifications", "q_id=" + jq.getJq_qualification_id());
					        		  jqTemp.append(ucQuali_list.isEmpty() ? "N/A" : ucQuali_list.get(0).getQ_name());
					        	  }
					          } else {
					        	  jqTemp.append("N/A");
					          }
					          cell=row.createCell(10);
					          cell.setCellValue(jqTemp.toString());
					          ucCity = new Uc_city();
					          ucCity = (Uc_city) model.selectData("model.Uc_city", "c_id=" + cj.getCj_city()).get(0);
					          ucTaluka = new Uc_taluka();
					          ucTaluka = (Uc_taluka) model.selectData("model.Uc_taluka", "t_id=" + ucCity.getC_taluka_id()).get(0);
					          ucDistrict = new Uc_district();
					          ucDistrict = (Uc_district) model.selectData("model.Uc_district", "d_id=" + ucTaluka.getT_district_id()).get(0);
					          ucState = new Uc_state();
					          ucState = (Uc_state) model.selectData("model.Uc_state", "s_id=" + ucDistrict.getD_s_id()).get(0);
					          cell=row.createCell(11);
					          cell.setCellValue(ucCity.getC_name() + ", " + ucTaluka.getT_name() + ", " + ucDistrict.getD_name() + ", " + ucState.getS_name());
					          cell=row.createCell(12);
					          cell.setCellValue(cj.getCj_work_profile());
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
}
