package controller;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Hashtable;
import java.sql.Date;
import java.util.List;
import java.util.StringTokenizer;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import database.Model;
import logs.LogManager;
import model.Uc_admin;
import model.Uc_admin_employer_permissions;
import model.Uc_admin_types;
import model.Uc_city;
import model.Uc_district;
import model.Uc_employer_details;
import model.Uc_employer_type;
import model.Uc_industry_sector;
import model.Uc_state;
import model.Uc_taluka;
import utils.RouteManager;
import utils.SessionManager;
import utils.ViewManager;

@WebServlet("/admin/employer")
public class AdminEmployerManager extends HttpServlet {
	private static final long serialVersionUID = 1L;
    
	private List<Uc_state> ucState_list;
	private Uc_employer_details ucEmployer;
	private List<Uc_employer_details> ucEmployer_list;
	private Model model;
	private Uc_industry_sector ucIndustry;
	private List<Uc_industry_sector> ucIndustry_list;
	private Uc_district ucDistrict;
	private Uc_city ucCity;
	private CommonHelper commonHelper;
	private List<Uc_employer_type> ucEmployerType_list;
	private Uc_state ucState;
	private List<Uc_city> ucCity_list;
	private List<Uc_district> ucDistrict_list;
	private List<Uc_taluka> ucTaluka_list;
	private Uc_admin_employer_permissions ucAdminEmployerPermissions;
	private List<Uc_admin_employer_permissions> ucAdminEmployerPermissions_list;
	private Uc_admin ucAdmin;
	private List<Uc_admin> ucAdmin_list;
	private List<Uc_admin_types> ucAdminTypes_list;
	private Uc_taluka ucTaluka;
    public AdminEmployerManager() {
        super();
        model = new Model();
    }

	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		Hashtable sessionAttributes = new Hashtable();
		sessionAttributes = (Hashtable) SessionManager.sessionExists(request, "admin");
		if(request.getParameter("action") != null) {
			if(request.getParameter("action").toLowerCase().equals("add")) {
				if(Integer.parseInt(sessionAttributes.get("utype").toString()) == UCConstants.MAIN_ADMIN || Integer.parseInt(sessionAttributes.get("utype").toString()) == UCConstants.SUB_MAIN_ADMIN || Integer.parseInt(sessionAttributes.get("utype").toString()) == UCConstants.SALES_ADMIN) {
					add(request,response);
					ViewManager.showView(request, response, "admin/addemployer.jsp");
				} else {
					ViewManager.showView(request, response, "admin/accessdenied.jsp");
				}
			} else if(request.getParameter("action").toLowerCase().equals("save")) {
				if(Integer.parseInt(sessionAttributes.get("utype").toString()) == UCConstants.MAIN_ADMIN || Integer.parseInt(sessionAttributes.get("utype").toString()) == UCConstants.SUB_MAIN_ADMIN || Integer.parseInt(sessionAttributes.get("utype").toString()) == UCConstants.SALES_ADMIN) {
					saveEmployer(request,response);
					RouteManager.route(response, "admin/employer?action=add");
				} else {
					ViewManager.showView(request, response, "admin/accessdenied.jsp");
				}
			} else if(request.getParameter("action").toLowerCase().equals("modify")) {
				if(Integer.parseInt(sessionAttributes.get("utype").toString()) == UCConstants.MAIN_ADMIN || Integer.parseInt(sessionAttributes.get("utype").toString()) == UCConstants.SUB_MAIN_ADMIN || Integer.parseInt(sessionAttributes.get("utype").toString()) == UCConstants.SALES_ADMIN) {
					ViewManager.showView(request, response, "admin/modifyemployer.jsp");
				} else {
					ViewManager.showView(request, response, "admin/accessdenied.jsp");
				}
			} else if(request.getParameter("action").toLowerCase().equals("gettype")) {
				if(Integer.parseInt(sessionAttributes.get("utype").toString()) == UCConstants.MAIN_ADMIN || Integer.parseInt(sessionAttributes.get("utype").toString()) == UCConstants.SUB_MAIN_ADMIN || Integer.parseInt(sessionAttributes.get("utype").toString()) == UCConstants.SALES_ADMIN) {
					getType(request,response);
				} else {
					ViewManager.showView(request, response, "admin/accessdenied.jsp");
				}
			} else if(request.getParameter("action").toLowerCase().equals("modifysingle")) {
				if(Integer.parseInt(sessionAttributes.get("utype").toString()) == UCConstants.MAIN_ADMIN || Integer.parseInt(sessionAttributes.get("utype").toString()) == UCConstants.SUB_MAIN_ADMIN || Integer.parseInt(sessionAttributes.get("utype").toString()) == UCConstants.SALES_ADMIN) {
					try {
						modifySingle(request,response);
					} catch(Exception ex) {
						LogManager.appendToExceptionLogs(ex);
					}
				} else {
					ViewManager.showView(request, response, "admin/accessdenied.jsp");
				}
			} else if(request.getParameter("action").toLowerCase().equals("getemployerlist")) {
				if(Integer.parseInt(sessionAttributes.get("utype").toString()) == UCConstants.MAIN_ADMIN || Integer.parseInt(sessionAttributes.get("utype").toString()) == UCConstants.SUB_MAIN_ADMIN || Integer.parseInt(sessionAttributes.get("utype").toString()) == UCConstants.SALES_ADMIN) {
					try {
						getEmployerList(request,response);
					} catch(Exception ex) {
						LogManager.appendToExceptionLogs(ex);
					}
				} else {
					ViewManager.showView(request, response, "admin/accessdenied.jsp");
				}
			} else if(request.getParameter("action").toLowerCase().equals("savechanges")) {
				if(Integer.parseInt(sessionAttributes.get("utype").toString()) == UCConstants.MAIN_ADMIN || Integer.parseInt(sessionAttributes.get("utype").toString()) == UCConstants.SUB_MAIN_ADMIN || Integer.parseInt(sessionAttributes.get("utype").toString()) == UCConstants.SALES_ADMIN) {
					saveChanges(request,response);
				} else {
					ViewManager.showView(request, response, "admin/accessdenied.jsp");
				}
			} else if(request.getParameter("action").toLowerCase().equals("getinsights")) {
				if(Integer.parseInt(sessionAttributes.get("utype").toString()) == UCConstants.MAIN_ADMIN || Integer.parseInt(sessionAttributes.get("utype").toString()) == UCConstants.SUB_MAIN_ADMIN) {
					try {
						getInsights(request,response);
					} catch(Exception ex) {
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
						LogManager.appendToExceptionLogs(ex);
					}
				} else {
					ViewManager.showView(request, response, "admin/accessdenied.jsp");
				}
			} else if(request.getParameter("action").toLowerCase().equals("removepermission")) {
				if(Integer.parseInt(sessionAttributes.get("utype").toString()) == UCConstants.MAIN_ADMIN || Integer.parseInt(sessionAttributes.get("utype").toString()) == UCConstants.SUB_MAIN_ADMIN) {
					try {
						removePermission(request,response);
					} catch(Exception ex) {
						LogManager.appendToExceptionLogs(ex);
					}
				} else {
					ViewManager.showView(request, response, "admin/accessdenied.jsp");
				}
			} else if(request.getParameter("action").toLowerCase().equals("deleteemployers")) {
				if(Integer.parseInt(sessionAttributes.get("utype").toString()) == UCConstants.MAIN_ADMIN || Integer.parseInt(sessionAttributes.get("utype").toString()) == UCConstants.SUB_MAIN_ADMIN || Integer.parseInt(sessionAttributes.get("utype").toString()) == UCConstants.SALES_ADMIN) {
					try {
						deleteEmployers(request,response);
					} catch(Exception ex) {
						LogManager.appendToExceptionLogs(ex);
					}
				} else {
					ViewManager.showView(request, response, "admin/accessdenied.jsp");
				}
			}   
			
			/* else if(request.getParameter("action").toLowerCase().equals("changefirmname")) {
				response.setContentType("text/html;charset=UTF-8");
				if(changeFirmName(request,response)) response.getWriter().write("success");
				else response.getWriter().write("error");
			} else if(request.getParameter("action").toLowerCase().equals("changeindustrysector")) {
				response.setContentType("text/html;charset=UTF-8");
				if(changeIndustrySector(request,response)) response.getWriter().write("success");
				else response.getWriter().write("error");
			}*/ else {
				if(Integer.parseInt(sessionAttributes.get("utype").toString()) == UCConstants.MAIN_ADMIN || Integer.parseInt(sessionAttributes.get("utype").toString()) == UCConstants.SUB_MAIN_ADMIN || Integer.parseInt(sessionAttributes.get("utype").toString()) == UCConstants.SALES_ADMIN) {
					RouteManager.route(response, "admin/employer?action=add");
				} else {
					ViewManager.showView(request, response, "admin/accessdenied.jsp");
				}
			}
		} else {
			if(Integer.parseInt(sessionAttributes.get("utype").toString()) == UCConstants.MAIN_ADMIN || Integer.parseInt(sessionAttributes.get("utype").toString()) == UCConstants.SUB_MAIN_ADMIN || Integer.parseInt(sessionAttributes.get("utype").toString()) == UCConstants.SALES_ADMIN) {
				RouteManager.route(response, "admin/employer?action=add");
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
		ucEmployerType_list = new ArrayList<Uc_employer_type>();
		ucEmployerType_list = (List<Uc_employer_type>)(Object) model.selectData("model.Uc_employer_type", "1");
		request.setAttribute("types", ucEmployerType_list);
		request.setAttribute("states", ucState_list);
	}
	
	private void saveEmployer(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		commonHelper = new CommonHelper();
		ucEmployer = new Uc_employer_details();
		ucEmployer.setEd_firm_name(request.getParameter("firm_name").toString());
		long industry = -1;
		long user_city = -1;
		Hashtable sessionAttributes = new Hashtable();
		sessionAttributes = (Hashtable) SessionManager.sessionExists(request, "admin");
		ucEmployer.setEd_added_by(Long.parseLong(sessionAttributes.get("uuid").toString()));
		if(Integer.parseInt(sessionAttributes.get("utype").toString()) == UCConstants.MAIN_ADMIN) {
			ucEmployer.setEd_active(1);
			ucEmployer.setEd_approved(1);
		}
		/*ucIndustry_list = new ArrayList<Uc_industry_sector>();
		ucIndustry_list = (List<Uc_industry_sector>)(Object) model.selectData("model.Uc_industry_sector", "is_name='" + request.getParameter("industry-sector").toString() + "' LIMIT 1");
		if(ucIndustry_list.isEmpty()) {
			ucIndustry = new Uc_industry_sector();
			ucIndustry.setIs_name(request.getParameter("industry-sector").toString());
			List<Long> keys = model.insertDataReturnGeneratedKeys(ucIndustry);
			if(keys != null) industry = keys.get(0).intValue();
			else SessionManager.setSession(request, "error", "Something went wrong while employer details. Please try again.");
		} else {
			industry = ucIndustry_list.get(0).getIs_id();
		}*/
		
		industry = commonHelper.saveIndustrySector(request);
		if(industry != -1) {
			ucEmployer.setEd_industry_sector_id(industry);
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
						SessionManager.setSession(request, "error", "Something went wrong while saving employer details. Please try again.");
					}
				} else {
					SessionManager.setSession(request, "error", "Something went wrong while saving employer details. Please try again.");
				}
			}*/
			user_city = commonHelper.saveCity(request);
		}
		if(user_city != -1) {
			ucEmployer.setEd_city_id(user_city);
			ucEmployer.setEd_type_id(Long.parseLong(request.getParameter("employer_type").toString()));
			String address = request.getParameter("employer_address").toString();
			address = address.replace("'", "\\'");
			ucEmployer.setEd_address(address);
			ucEmployer.setEd_contact_person(request.getParameter("contact_person_name").toString());
			ucEmployer.setEd_contact_no(Long.parseLong(request.getParameter("contact_person_no").toString()));
			ucEmployer.setEd_email(request.getParameter("contact_person_email").toString());
			Date date = null;
			if(request.getParameter("employer_date") != null) {
				if(request.getParameter("employer_date").equals("")) {
					/*SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
					date = sdf.format(new Date());*/
					date = new Date(Calendar.getInstance().getTime().getTime());
				} else {
					try {
						date = new Date(new SimpleDateFormat("yyyy-MM-dd").parse(request.getParameter("employer_date").toString()).getTime());
					} catch(Exception ex) {}
				}
			} else {
				/*SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
				date = sdf.format(new Date());*/
				date = new Date(Calendar.getInstance().getTime().getTime());
			}
			ucEmployer.setEd_date_of_joining(date);
			model.startTransaction();
			List<Long> keys = model.insertDataReturnGeneratedKeys(ucEmployer);
			if(!keys.isEmpty()) {
				if(Integer.parseInt(sessionAttributes.get("utype").toString()) == UCConstants.SALES_ADMIN) {
					ucAdminEmployerPermissions = new Uc_admin_employer_permissions();
					ucAdminEmployerPermissions.setAep_admin_id(Long.parseLong(sessionAttributes.get("uuid").toString()));
					ucAdminEmployerPermissions.setAep_employer_id(keys.get(0));
					if(model.insertData(ucAdminEmployerPermissions)) {
						SessionManager.setSession(request, "success", "Employer details successfully saved.");
					} else {
						SessionManager.setSession(request, "error", "Something went wrong while saving employer details. Please try again.");
					}
				}
			} else {
				SessionManager.setSession(request, "error", "Something went wrong while saving employer details. Please try again.");
			}
			model.endTransaction();
		}
	}
	
	private void getEmployerList(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException, JSONException {
		int pageNo = Integer.parseInt(request.getParameter("pageNo").toString());
		int limit = Integer.parseInt(request.getParameter("limit").toString());
		boolean begin = request.getParameter("begin").toLowerCase().equals("true") ? true : false;
		int start = (pageNo-1)*limit;
		StringBuilder where = new StringBuilder();
		boolean hasExtra = false;
		int count = 0;
		String postfix = "";
		Hashtable sessionAttributes = new Hashtable();
		sessionAttributes = (Hashtable) SessionManager.sessionExists(request, "admin");
		
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
					where.append("ed_id=" + Integer.parseInt(val2));
				} else if(val1.equals("2")) {
					where.append("ed_firm_name like '%" + val2 +"%'");
				} else if(val1.equals("3")) {
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
															cityWhere.append("ed_city_id=" + cty.getC_id());
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
															cityWhere.append("ed_city_id=" + cty.getC_id());
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
															cityWhere.append("ed_city_id=" + cty.getC_id());
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
															cityWhere.append("ed_city_id=" + cty.getC_id());
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
															cityWhere.append("ed_city_id=" + cty.getC_id());
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
															cityWhere.append("ed_city_id=" + cty.getC_id());
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
															cityWhere.append("ed_city_id=" + cty.getC_id());
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
															cityWhere.append("ed_city_id=" + cty.getC_id());
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
															cityWhere.append("ed_city_id=" + cty.getC_id());
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
															cityWhere.append("ed_city_id=" + cty.getC_id());
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
															cityWhere.append("ed_city_id=" + cty.getC_id());
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
															cityWhere.append("ed_city_id=" + cty.getC_id());
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
															cityWhere.append("ed_city_id=" + cty.getC_id());
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
															cityWhere.append("ed_city_id=" + cty.getC_id());
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
															cityWhere.append("ed_city_id=" + cty.getC_id());
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
															cityWhere.append("ed_city_id=" + cty.getC_id());
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
				} else if(val1.equals("4")) {
					where.append("ed_industry_sector=" + Integer.parseInt(val2));
				} else if(val1.equals("5")) {
					where.append("ed_type_id=" + Integer.parseInt(val2));
				} else if(val1.equals("6")) {
					where.append("ed_contact_person like '%" + val2 + "%'");
				} else if(val1.equals("7")) {
					where.append("ed_contact_no=" + val2);
				}
			}
		} else {
			where.append("1");
		}
		
		if(hasExtra && where.toString().equals("")) {
			response.setContentType("application/json");
			JSONObject jsonResponse = new JSONObject();
			jsonResponse.put("table", "No employers are registered yet.");
			jsonResponse.put("pageCount", 0);
			response.getWriter().write(jsonResponse.toString());
		} else {
		ucEmployer_list = new ArrayList<Uc_employer_details>();
		if(Integer.parseInt(sessionAttributes.get("utype").toString()) == UCConstants.SALES_ADMIN) {
			ucAdminEmployerPermissions_list = new ArrayList<Uc_admin_employer_permissions>();
			ucAdminEmployerPermissions_list = (List<Uc_admin_employer_permissions>)(Object) model.selectData("model.Uc_admin_employer_permissions", "aep_admin_id=" + Integer.parseInt(sessionAttributes.get("uuid").toString()));
			if(!ucAdminEmployerPermissions_list.isEmpty()) {
				where.append(" and (");
				String temppostfix = "";
				for(Uc_admin_employer_permissions aep : ucAdminEmployerPermissions_list) {
					where.append(temppostfix);
					temppostfix = " or ";
					where.append("ed_id=" + aep.getAep_employer_id());
				}
				where.append(")");
			} else {
				where.append(" and 0");
			}
		}
		ucEmployer_list = (List<Uc_employer_details>)(Object) model.selectData("model.Uc_employer_details", where.toString() + " order by ed_id desc LIMIT " + start + "," + limit);
		if(ucEmployer_list.isEmpty()) {
			response.setContentType("application/json");
			JSONObject jsonResponse = new JSONObject();
			jsonResponse.put("table", "No employers are registered yet.");
			jsonResponse.put("pageCount", 0);
			response.getWriter().write(jsonResponse.toString());
		} else {
			StringBuilder temp = new StringBuilder();
			if(Integer.parseInt(sessionAttributes.get("utype").toString()) == UCConstants.MAIN_ADMIN) {
				temp.append("<table class=\"table table-bordered\" width=\"100%\"><tr><th>Sr. No.</th><th>Employer Id</th><th>Firm name</th><th>Industry sector</th><th>City (State)</th><th>Contact person</th><th>Contact number</th><th>Added by</th><th>View / Modify</th><th>Delete</th></tr>");
			} else {
				temp.append("<table class=\"table table-bordered\" width=\"100%\"><tr><th>Sr. No.</th><th>Employer Id</th><th>Firm name</th><th>Industry sector</th><th>City (State)</th><th>Contact person</th><th>Contact number</th><th>View / Modify</th><th>Delete</th></tr>");
			}
			for(int i = 0 ; i < ucEmployer_list.size() ; i++) {
				ucEmployer = new Uc_employer_details();
				ucEmployer = ucEmployer_list.get(i);
				String bgColor = "#000";
				if(ucEmployer.getEd_approved() == 1 && ucEmployer.getEd_active() == 1) {
					bgColor = UCConstants.APPROVED_ACTIVE;
				} else if(ucEmployer.getEd_approved() == 1 && ucEmployer.getEd_active() == 0) {
					bgColor = UCConstants.APPROVED_NOTACTIVE;
				} else if(ucEmployer.getEd_approved() == 0 && ucEmployer.getEd_active() == 0) {
					bgColor = UCConstants.NOTAPPROVED_NOTACTIVE;
				} else if(ucEmployer.getEd_approved() == 2 && ucEmployer.getEd_active() == 0) {
					bgColor = UCConstants.PENDING_NOTACTIVE;
				}
				temp.append("<tr id=\"employer_row" + ucEmployer.getEd_id() + "\" style=\"background:" + bgColor + ";color:#fff\">");
				if(Integer.parseInt(sessionAttributes.get("utype").toString()) == UCConstants.MAIN_ADMIN) {
					temp.append("<td><label><input type=\"checkbox\" name=\"employer_check\" value=\"" + ucEmployer.getEd_id() + "\"> &nbsp;" + (i+1) + "</label></td>");
				} else {
					temp.append("<td>" + (i+1) + "</td>");
				}
				temp.append("<td>" + ucEmployer.getEd_id() + "</td>");
				temp.append("<td>" + ucEmployer.getEd_firm_name() + "</td>");
				ucIndustry = new Uc_industry_sector();
				ucIndustry = (Uc_industry_sector) model.selectData("model.Uc_industry_sector", "is_id=" + ucEmployer.getEd_industry_sector_id()).get(0);
				temp.append("<td>" + ucIndustry.getIs_name() + "</td>");
				ucCity = new Uc_city();
				ucCity = (Uc_city) model.selectData("model.Uc_city", "c_id=" + ucEmployer.getEd_city_id()).get(0);
				ucTaluka = new Uc_taluka();
				ucTaluka = (Uc_taluka) model.selectData("model.Uc_taluka", "t_id=" + ucCity.getC_taluka_id()).get(0);
				ucDistrict = new Uc_district();
				ucDistrict = (Uc_district) model.selectData("model.Uc_district", "d_id=" + ucTaluka.getT_district_id()).get(0);
				ucState = new Uc_state();
				ucState = (Uc_state) model.selectData("model.Uc_state", "s_id=" + ucDistrict.getD_s_id()).get(0);
				temp.append("<td>" + ucCity.getC_name() + " (" + ucState.getS_name() + ")</td>");
				temp.append("<td>" + ucEmployer.getEd_contact_person() + "</td>");
				temp.append("<td>" + ucEmployer.getEd_contact_no() + "</td>");
				
				if(Integer.parseInt(sessionAttributes.get("utype").toString()) == UCConstants.MAIN_ADMIN) {
				ucAdmin = new Uc_admin();
				ucAdmin = (Uc_admin) model.selectData("model.Uc_admin", "a_id=" + ucEmployer.getEd_added_by()).get(0);
				temp.append("<td>" + ucAdmin.getA_username() + "</td>");
				}
				
				temp.append("<td><button type=\"button\" class=\"btn btn-info\" onclick=\"modifyEmployer(this," + ucEmployer.getEd_id() + ")\">Modify</button></td>");
				temp.append("<td><button type=\"button\" class=\"btn btn-danger\">Delete</button></td>");
				temp.append("</tr>");
			}
			temp.append("</table><br>");
			if(Integer.parseInt(sessionAttributes.get("utype").toString()) == UCConstants.MAIN_ADMIN) {
			temp.append("<button type=\"button\" onclick=\"activateEmployers(this)\" class=\"btn btn-success\" style=\"margin:5px\">Activate</button>");
			temp.append("<button type=\"button\" onclick=\"deactivateEmployers(this)\" class=\"btn btn-danger\" style=\"margin:5px\">De-Activate</button>");
			temp.append("<button type=\"button\" onclick=\"approveEmployers(this)\" class=\"btn btn-success\" style=\"margin:5px\">Approve</button>");
			temp.append("<button type=\"button\" onclick=\"disapproveEmployers(this)\" class=\"btn btn-danger\" style=\"margin:5px\">Dis-Approve</button>");
			temp.append("<button type=\"button\" onclick=\"deleteEmployers(this)\" class=\"btn btn-danger\" style=\"margin:5px\">Delete</button>");
			}
			response.setContentType("application/json");
			 JSONObject jsonResponse = new JSONObject();
			 jsonResponse.put("table", temp.toString());
			
			if(begin) {
				ucEmployer_list = new ArrayList<Uc_employer_details>();
				ucEmployer_list = (List<Uc_employer_details>)(Object) model.selectData("model.Uc_employer_details", where.toString());
				 int pageCount = (int) Math.ceil(ucEmployer_list.size()/(float)limit);
				if(pageCount == 0) pageCount = 1;
				 jsonResponse.put("pageCount", pageCount);

			}
			response.getWriter().write(jsonResponse.toString());
		}
	}
	}
	
/*	private boolean changeFirmName(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		String firmName = request.getParameter("firmName").toString();
		int id = Integer.parseInt(request.getParameter("id").toString());
		ucEmployer = new Uc_employer_details();
		ucEmployer.setEd_firm_name(firmName);
		return model.updateData(ucEmployer, "ed_id=" + id);
	}
	
	private boolean changeIndustrySector(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		commonHelper = new CommonHelper();
		int industrySector = commonHelper.saveIndustrySector(request);
		if(industrySector != -1) {
			int id = Integer.parseInt(request.getParameter("id").toString());
			ucEmployer = new Uc_employer_details();
			ucEmployer.setEd_industry_sector_id(industrySector);
			return model.updateData(ucEmployer, "ed_id=" + id);
		} else {
			return false;
		}
	} */
	
	private void modifySingle(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException, JSONException {
		int id = Integer.parseInt(request.getParameter("id"));
		Hashtable sessionAttributes = new Hashtable();
		sessionAttributes = (Hashtable) SessionManager.sessionExists(request, "admin");
		StringBuilder temp = new StringBuilder();
		ucEmployer = new Uc_employer_details();
		ucEmployer = (Uc_employer_details) model.selectData("model.Uc_employer_details", "ed_id=" + id).get(0);
		temp.append("<form action=\""+ RouteManager.getBasePath() +"admin/employer?action=saveChanges\" method=\"POST\">");
		temp.append("<input type=\"hidden\" value=\"" + id + "\" name=\"employer_id\">");
		temp.append("<div class=\"form-group col-lg-12\">");
			temp.append("<div class=\"col-lg-12\">");
				temp.append("Employer name:<input type=\"text\" class=\"form-control\" value=\"" + ucEmployer.getEd_firm_name() + "\" name=\"firm_name\" id=\"firm_name\" placeholder=\"Firm Name\">");
			temp.append("</div>");
		temp.append("</div>");
		temp.append("<div class=\"form-group col-lg-12\">");
			temp.append("<div class=\"col-lg-12\">");
				temp.append("Firm type:<select name=\"employer_type\" class=\"form-control\" id=\"employer_type\">");
					ucEmployerType_list = new ArrayList<Uc_employer_type>();
					ucEmployerType_list = (List<Uc_employer_type>)(Object) model.selectData("model.Uc_employer_type", "1");
					for(Uc_employer_type type : ucEmployerType_list) {
						if(type.getEt_id() == ucEmployer.getEd_type_id()) {
							temp.append("<option value=" + type.getEt_id() + " selected>" + type.getEt_name() + "</option>");
						} else {
							temp.append("<option value=" + type.getEt_id() + ">" + type.getEt_name() + "</option>");
						}
					 }
				temp.append("</select>");
			temp.append("</div>");
		temp.append("</div>");
		temp.append("<div class=\"form-group col-lg-12\">");
		temp.append("<div class=\"col-lg-12\">");
		ucIndustry = new Uc_industry_sector();
		ucIndustry = (Uc_industry_sector) model.selectData("model.Uc_industry_sector", "is_id=" + ucEmployer.getEd_industry_sector_id()).get(0);
			temp.append("Industry sector:<input type=\"text\" value=\"" + ucIndustry.getIs_name() + "\" class=\"form-control\" name=\"industry-sector\" id=\"industry-sector\" placeholder=\"Industry sector\">");
		temp.append("</div>");
		temp.append("</div>");
		
		ucCity = new Uc_city();
		ucCity = (Uc_city) model.selectData("model.Uc_city", "c_id=" + ucEmployer.getEd_city_id()).get(0);
		ucTaluka = new Uc_taluka();
		ucTaluka = (Uc_taluka) model.selectData("model.Uc_taluka", "t_id=" + ucCity.getC_taluka_id()).get(0);
		ucDistrict = new Uc_district();
		ucDistrict = (Uc_district) model.selectData("model.Uc_district", "d_id=" + ucTaluka.getT_district_id()).get(0);
		ucState = new Uc_state();
		ucState = (Uc_state) model.selectData("model.Uc_state", "s_id=" + ucDistrict.getD_s_id()).get(0);
		temp.append("<div class=\"col-lg-12 form-group\">");
			temp.append("Location: " + ucCity.getC_name() + ", " + ucTaluka.getT_name() + ", " + ucDistrict.getD_name() + ", " + ucState.getS_name());
		temp.append("</div>");
		
		temp.append("<div class=\"col-lg-12 form-group\">");
			temp.append("<div class=\"col-lg-6\">");
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
		temp.append("<div class=\"form-group col-lg-12\">");
			temp.append("<div class=\"col-lg-12\">");
				temp.append("Full address of employer:<textarea rows=\"3\" class=\"form-control\" id=\"employer_address\" name=\"employer_address\" placeholder=\"Full address of employer\">" + ucEmployer.getEd_address() + "</textarea>");
			temp.append("</div>");
		temp.append("</div>");
		temp.append("<div class=\"form-group col-lg-12\">");
			temp.append("<div class=\"col-lg-6\">");
				temp.append("Contact person name:<input type=\"text\" class=\"form-control\" value=\"" + ucEmployer.getEd_contact_person() + "\" placeholder=\"Contact person name\" id=\"contact_person_name\" name=\"contact_person_name\">");
			temp.append("</div>");
			temp.append("<div class=\"col-lg-6\">");
				temp.append("Contact person phone number:<input type=\"number\" value=\"" + ucEmployer.getEd_contact_no() + "\" oninput=\"javascript: if (this.value.length > this.maxLength) this.value = this.value.slice(0, this.maxLength);\" maxlength=\"10\" class=\"form-control\" placeholder=\"Contact person phone number\" id=\"contact_person_no\" name=\"contact_person_no\">");
			temp.append("</div>");
		temp.append("</div>");
		temp.append("<div class=\"form-group col-lg-12\">");
			temp.append("<div class=\"col-lg-6\">");
				temp.append("Contact person email-Id:<input type=\"text\" value=\"" + ucEmployer.getEd_email() + "\" class=\"form-control\" placeholder=\"Contact person email-ID\" id=\"contact_person_email\" name=\"contact_person_email\">");
			temp.append("</div>");
		temp.append("</div>");
		temp.append("<div class=\"form-group col-lg-12\">");
			temp.append("<div class=\"col-lg-6\">");
				temp.append("Joining date of employer:<input type=\"text\" class=\"form-control\" value=\"" + ucEmployer.getEd_date_of_joining() + "\" placeholder=\"Joining date of employer\" id=\"employer_date\" name=\"employer_date\">");
			temp.append("</div>");
		temp.append("</div>");
		
		if(Integer.parseInt(sessionAttributes.get("utype").toString()) == UCConstants.MAIN_ADMIN) {
		
		temp.append("<div class=\"form-group col-lg-12\">");
		ucAdmin_list = new ArrayList<Uc_admin>();
		ucAdmin_list = (List<Uc_admin>)(Object) model.selectData("model.Uc_admin", "a_type=" + UCConstants.SALES_ADMIN);
		ucAdminTypes_list = new ArrayList<Uc_admin_types>();
		ucAdminTypes_list = (List<Uc_admin_types>)(Object) model.selectData("model.Uc_admin_types", "1");
		temp.append("<div class=\"col-lg-6\">");
		temp.append("Admins to view this employer details:");
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
		temp.append("<button type=\"button\" class=\"btn btn-primary\" onclick=\"add_admin_permission(" + ucEmployer.getEd_id() + ")\"><i class=\"fa fa-plus\"></i></button>");
		temp.append("</div>");
		temp.append("</div>");
		temp.append("<div class=\"col-lg-6\" id=\"admin-tags\">");
		
		ucAdminEmployerPermissions_list = new ArrayList<Uc_admin_employer_permissions>();
		ucAdminEmployerPermissions_list = (List<Uc_admin_employer_permissions>)(Object) model.selectData("model.Uc_admin_employer_permissions", "aep_employer_id=" + ucEmployer.getEd_id());
		for(int i=1 ; i<=ucAdminEmployerPermissions_list.size() ; i++) {
			ucAdmin = new Uc_admin();
			ucAdmin = (Uc_admin) model.selectData("model.Uc_admin", "a_id=" + ucAdminEmployerPermissions_list.get(i-1).getAep_admin_id()).get(0);
			temp.append("<div class=\"single-tag\" style=\"display:inline-block;margin-left:10px\" id=\"a_tag_count" + i + "\"> &nbsp;<a onclick=\"remove_a_tag(" + i + ", " + ucEmployer.getEd_id() + ")\" style=\"cursor:pointer\"><i class=\"fa fa-close\" style=\"color: #790000\"></i></a> &nbsp;<label id=\"permitted_admin" + i + "\">" + ucAdmin.getA_username() + "</label><input type=\"hidden\" id=\"permitted_admin_hidden" + i + "\" name=\"permitted_admin" + i + "\" value=\"" + ucAdmin.getA_id() + "\"></div>");
		}
		
		temp.append("</div>");
		temp.append("<input type=\"hidden\" name=\"admin_count\" value=\"" + ucAdminEmployerPermissions_list.size() + "\" id=\"admin_count\">");
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
		jsonResponse.put("employerId", id);
		response.getWriter().write(jsonResponse.toString());
	}
	
	private void saveChanges(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		long id = Long.parseLong(request.getParameter("employer_id").toString());
		commonHelper = new CommonHelper();
		ucEmployer = new Uc_employer_details();
		String firm_name = request.getParameter("firm_name").toString();
		if(!firm_name.equals("")) ucEmployer.setEd_firm_name(firm_name);
		ucEmployer.setEd_type_id(Long.parseLong(request.getParameter("employer_type").toString()));
		String indusSector = request.getParameter("industry-sector").toString();
		if(!indusSector.equals("")) {
			long industry = commonHelper.saveIndustrySector(request);
			if(industry != -1) ucEmployer.setEd_industry_sector_id(industry);
		}
		String state = request.getParameter("user_state").toString();
		if(!state.toLowerCase().equals("select")) {
			String stateName = ((List<Uc_state>)(Object)model.selectData("model.Uc_state", "s_id=" + Long.parseLong(state))).get(0).getS_name();
			if(stateName.toLowerCase().equals("other")) {
				String userDistrict = request.getParameter("user_district_other").toString();
				String userCity = request.getParameter("user_city_other").toString();
				if(!userDistrict.equals("") && !userCity.equals("")) {
					long city = commonHelper.saveCity(request);
					ucEmployer.setEd_city_id(city);
				}
			} else {
				String district = request.getParameter("user_district").toString();
				if(!district.toLowerCase().equals("select")) {
					String city = request.getParameter("user_city").toString();
					if(!city.toLowerCase().equals("select")) {
						ucEmployer.setEd_city_id(Long.parseLong(city));
					}
				}
			}
		}
		String address = request.getParameter("employer_address").toString();
		address = address.replace("'", "\\'");
		if(!address.equals("")) ucEmployer.setEd_address(address);
		String contactPerson = request.getParameter("contact_person_name").toString();
		if(!contactPerson.equals("")) ucEmployer.setEd_contact_person(contactPerson);
		String contactNo = request.getParameter("contact_person_no").toString();
		if(!contactNo.equals("")) ucEmployer.setEd_contact_no(Long.parseLong(contactNo));
		String email = request.getParameter("contact_person_email").toString();
		if(!email.equals("")) ucEmployer.setEd_email(email);
		String date = request.getParameter("employer_date").toString();
		if(!date.equals("")) {
			try {
				Date dt = new Date(new SimpleDateFormat("yyyy-MM-dd").parse(date).getTime());
				ucEmployer.setEd_date_of_joining(dt);
			} catch(Exception ex) {}
		}
		
		if(model.updateData(ucEmployer, "ed_id=" + id)) {
			SessionManager.setSession(request, "success", "Employer details successfully saved.");
		} else {
			SessionManager.setSession(request, "error", "Something went wrong which saving employer details. Please try again.");
		}
		RouteManager.route(response, "admin/employer?action=modify");
	}
	
	private void getType(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		int id = Integer.parseInt(request.getParameter("criteriaId").toString());
		ucEmployerType_list = new ArrayList<Uc_employer_type>();
		ucEmployerType_list = (List<Uc_employer_type>)(Object) model.selectData("model.Uc_employer_type", "1");
		StringBuilder temp = new StringBuilder();
		temp.append("<select class=\"form-control\" id=\"employer_type" + id + "\">");
		temp.append("<option selected hidden value=\"select\">Select employer type</option>");
		for(Uc_employer_type type : ucEmployerType_list) {
			temp.append("<option value=\"" + type.getEt_id() + "\">" + type.getEt_name() + "</option>");
		}
		temp.append("</select>");
		response.setContentType("text/html;charset=UTF-8");
		response.getWriter().write(temp.toString());
	}
	
	private void getInsights(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException, JSONException {
		List<Hashtable> previousYearEmployers = new ArrayList<Hashtable>();
		List<Hashtable> currentYearEmployers = new ArrayList<Hashtable>();
		java.util.Date date = new java.util.Date();
		JSONArray jsonArray = new JSONArray();
		JSONObject json = new JSONObject();
		SimpleDateFormat smf_year = new SimpleDateFormat("yyyy");
		SimpleDateFormat smf_month = new SimpleDateFormat("MM");
		json.put("previousYear", (Integer.parseInt(smf_year.format(date).toString()) - 1));
		json.put("currentYear", (Integer.parseInt(smf_year.format(date).toString())));
		previousYearEmployers = model.selectDataGroupBy("model.Uc_employer_details", "count(*) cnt, MONTH(ed_date_of_joining) month", "YEAR(ed_date_of_joining)=" + (Integer.parseInt(smf_year.format(date).toString()) - 1) , "MONTH(ed_date_of_joining)");
		currentYearEmployers = model.selectDataGroupBy("model.Uc_employer_details", "count(*) cnt, MONTH(ed_date_of_joining) month", "YEAR(ed_date_of_joining)=" + (Integer.parseInt(smf_year.format(date).toString())) , "MONTH(ed_date_of_joining)");
		if(!previousYearEmployers.isEmpty()) {
			StringBuilder temp = new StringBuilder();
			String postfix = "[";
			int tempCount = 0;
			for(int i = 1 ; i <= 12 ; i++ ) {
				temp.append(postfix);
				postfix = ", ";
				if(tempCount >= previousYearEmployers.size()) {
					//temp.append("0");
					jsonArray.put(0);
					continue;
				}
				if(previousYearEmployers.get(tempCount).get("month").equals(String.valueOf(i))) {
					//temp.append(previousYearEmployers.get(tempCount++).get("cnt"));
					jsonArray.put(Integer.parseInt(previousYearEmployers.get(tempCount++).get("cnt").toString()));
				} else {
					//temp.append("0");
					jsonArray.put(0);
				}
			}
			//temp.append("]");
			//request.setAttribute("previousYearEmployers", temp);
			json.put("previousYearEmployers", jsonArray);
		} else {
			//String noData = "[0,0,0,0,0,0,0,0,0,0,0,0]";
			//request.setAttribute("previousYearEmployers", noData);
			int[] noData = {0,0,0,0,0,0,0,0,0,0,0,0};
			json.put("previousYearEmployers", noData);
		}
		jsonArray = new JSONArray();
		if(!currentYearEmployers.isEmpty()) {
			StringBuilder temp = new StringBuilder();
			String postfix = "[";
			int tempCount = 0;
			for(int i = 1 ; i <= Integer.parseInt(smf_month.format(date).toString()) ; i++ ) {
				temp.append(postfix);
				postfix = ", ";
				if(tempCount >= currentYearEmployers.size()) {
					//temp.append("0");
					jsonArray.put(0);
					continue;
				}
					if(currentYearEmployers.get(tempCount).get("month").equals(String.valueOf(i))) {
						//temp.append(currentYearEmployers.get(tempCount++).get("cnt"));
						jsonArray.put(Integer.parseInt(currentYearEmployers.get(tempCount++).get("cnt").toString()));
					} else {
						//temp.append("0");
						jsonArray.put(0);
					}
			}
			//temp.append("]");
			//request.setAttribute("currentYearEmployers", temp);
			json.put("currentYearEmployers", jsonArray);
		} else {
			/*String noData = "[]";
			request.setAttribute("currentYearEmployers", noData);*/
			int[] noData = {};
			json.put("currentYearEmployers", noData);
		}
		
		response.setContentType("application/json");
		response.getWriter().write(json.toString());
	}
	
	private void activate(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException, JSONException {
		JSONArray json = new JSONArray(request.getParameter("employerIds").toString());
		boolean flag = true;
		model.startTransaction();
		for(int i=0 ; i<json.length() ; i++) {
			ucEmployer = new Uc_employer_details();
			ucEmployer.setEd_active(1);
			ucEmployer.setEd_approved(1);
			if(!model.updateData(ucEmployer, "ed_id=" + json.getInt(i))) {
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
		JSONArray json = new JSONArray(request.getParameter("employerIds").toString());
		boolean flag = true;
		model.startTransaction();
		for(int i=0 ; i<json.length() ; i++) {
			ucEmployer = new Uc_employer_details();
			ucEmployer.setEd_active(0);
			if(!model.updateData(ucEmployer, "ed_id=" + json.getInt(i))) {
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
		JSONArray json = new JSONArray(request.getParameter("employerIds").toString());
		boolean flag = true;
		model.startTransaction();
		for(int i=0 ; i<json.length() ; i++) {
			ucEmployer = new Uc_employer_details();
			ucEmployer.setEd_approved(1);
			if(!model.updateData(ucEmployer, "ed_id=" + json.getInt(i))) {
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
		JSONArray json = new JSONArray(request.getParameter("employerIds").toString());
		boolean flag = true;
		model.startTransaction();
		for(int i=0 ; i<json.length() ; i++) {
			ucEmployer = new Uc_employer_details();
			ucEmployer.setEd_approved(0);
			ucEmployer.setEd_active(0);
			if(!model.updateData(ucEmployer, "ed_id=" + json.getInt(i))) {
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
	
	private void deleteEmployers(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException, JSONException {
		JSONArray json = new JSONArray(request.getParameter("employerIds").toString());
		boolean flag = true;
		for(int i=0 ; i<json.length() ; i++) {
			if(!model.deleteData("model.Uc_employer_details", "ed_id=" + json.getInt(i))) {
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
		long employerId = Long.parseLong(request.getParameter("employerId").toString());
		long adminId = Long.parseLong(request.getParameter("adminId").toString());
		ucAdminEmployerPermissions = new Uc_admin_employer_permissions();
		ucAdminEmployerPermissions.setAep_admin_id(adminId);
		ucAdminEmployerPermissions.setAep_employer_id(employerId);
		if(model.insertData(ucAdminEmployerPermissions)) {
			response.setContentType("text/html;charset=UTF-8");
			response.getWriter().write("success");
		} else {
			response.setContentType("text/html;charset=UTF-8");
			response.getWriter().write("error");
		}
	}
	
	private void removePermission(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException, JSONException {
		int employerId = Integer.parseInt(request.getParameter("employerId").toString());
		int adminId = Integer.parseInt(request.getParameter("adminId").toString());
		if(model.deleteData("model.Uc_admin_employer_permissions", "aep_admin_id=" + adminId + " and aep_employer_id=" + employerId)) {
			response.setContentType("text/html;charset=UTF-8");
			response.getWriter().write("success");
		} else {
			response.setContentType("text/html;charset=UTF-8");
			response.getWriter().write("error");
		}
	}
}
