package controller;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import database.Model;
import logs.LogManager;
import model.Uc_city;
import model.Uc_district;
import model.Uc_industry_sector;
import model.Uc_position;
import model.Uc_state;
import model.Uc_taluka;
import utils.SessionManager;
import utils.ViewManager;

public class CommonHelper {
	
	Model model;
	List<Uc_industry_sector> ucIndustry_list;
	Uc_industry_sector ucIndustry;
	List<Uc_state> ucState_list;
	Uc_city ucCity;
	Uc_district ucDistrict;
	List<Uc_position> ucPosition_list;
	Uc_position ucPosition;
	List<Uc_district> ucDistrict_list;
	List<Uc_city> ucCity_list;
	Uc_taluka ucTaluka;
	List<Uc_taluka> ucTaluka_list;
	public CommonHelper() {
		model = new Model();
	}
	
	public long saveIndustrySector(HttpServletRequest request) {
		long industry = -1;
		try {
			ucIndustry_list = new ArrayList<Uc_industry_sector>();
			ucIndustry_list = (List<Uc_industry_sector>)(Object) model.selectData("model.Uc_industry_sector", "is_name='" + request.getParameter("industry-sector").toString() + "' LIMIT 1");
			if(ucIndustry_list.isEmpty()) {
				ucIndustry = new Uc_industry_sector();
				ucIndustry.setIs_name(request.getParameter("industry-sector").toString());
				List<Long> keys = model.insertDataReturnGeneratedKeys(ucIndustry);
				if(keys != null) industry = keys.get(0);
				else SessionManager.setSession(request, "error", "Something went wrong while saving industry sector. Please try again.");
			} else {
				industry = ucIndustry_list.get(0).getIs_id();
			}
		} catch(Exception ex) {
			System.out.println("error");
			LogManager.appendToExceptionLogs(ex);
		}
		return industry;
	}
	
	public long saveIndustrySector(String indus) {
		long industry = -1;
		try {
			ucIndustry_list = new ArrayList<Uc_industry_sector>();
			ucIndustry_list = (List<Uc_industry_sector>)(Object) model.selectData("model.Uc_industry_sector", "is_name='" + indus + "' LIMIT 1");
			if(ucIndustry_list.isEmpty()) {
				ucIndustry = new Uc_industry_sector();
				ucIndustry.setIs_name(indus);
				List<Long> keys = model.insertDataReturnGeneratedKeys(ucIndustry);
				if(keys != null) industry = keys.get(0);
			} else {
				industry = ucIndustry_list.get(0).getIs_id();
			}
		} catch(Exception ex) {
			System.out.println("error");
			LogManager.appendToExceptionLogs(ex);
		}
		return industry;
	}
	
	public long saveCity(HttpServletRequest request) {
		long user_city = -1; 
		String user_district_other, user_taluka_other, user_city_other;
		try {
			long user_state = Long.parseLong(request.getParameter("user_state").toString());
			ucState_list = new ArrayList<Uc_state>();
			ucState_list = (List<Uc_state>)(Object) model.selectData("model.Uc_state", "s_id=" + user_state);
			String state_name = ucState_list.get(0).getS_name();
			if(state_name.toLowerCase().equals("gujarat")) {
				user_city = Long.parseLong(request.getParameter("user_city").toString());
			} else {
				user_district_other = request.getParameter("user_district_other").toString();
				user_taluka_other = request.getParameter("user_taluka_other").toString();
				user_city_other = request.getParameter("user_city_other").toString();
				ucDistrict_list = new ArrayList<Uc_district>();
				ucDistrict_list = (List<Uc_district>)(Object) model.selectData("model.Uc_district", "d_name='" + user_district_other + "' and d_s_id=" + user_state);
				if(!ucDistrict_list.isEmpty()) {
					
						long dId = ucDistrict_list.get(0).getD_id();
						
						ucTaluka_list = new ArrayList<Uc_taluka>();
						ucTaluka_list = (List<Uc_taluka>)(Object) model.selectData("model.Uc_taluka", "t_name='" + user_taluka_other + "' and t_district_id=" + dId);
						if(!ucTaluka_list.isEmpty()) {
								long tkey = ucTaluka_list.get(0).getT_id();
								ucCity_list = new ArrayList<Uc_city>();
								ucCity_list = (List<Uc_city>)(Object) model.selectData("model.Uc_city", "c_name='" + user_city_other + "' and c_taluka_id=" + tkey);
								if(!ucCity_list.isEmpty()) {
									user_city = ucCity_list.get(0).getC_id();
								} else {
									ucCity = new Uc_city();
									ucCity.setC_taluka_id(tkey);
									ucCity.setC_name(user_city_other);
									List<Long> citykeys = model.insertDataReturnGeneratedKeys(ucCity);
									if(citykeys != null) {
										user_city = citykeys.get(0);
									} else {
										SessionManager.setSession(request, "error", "Something went wrong while saving city. Please try again.");
									}
								}
							
						} else {
							ucTaluka = new Uc_taluka();
							ucTaluka.setT_district_id(dId);
							ucTaluka.setT_name(user_taluka_other);
							List<Long> talukaKey = model.insertDataReturnGeneratedKeys(ucTaluka);
							if(talukaKey != null) {
								long tkey = talukaKey.get(0);
								ucCity_list = new ArrayList<Uc_city>();
								ucCity_list = (List<Uc_city>)(Object) model.selectData("model.Uc_city", "c_name='" + user_city_other + "' and c_taluka_id=" + tkey);
								if(!ucCity_list.isEmpty()) {
									user_city = ucCity_list.get(0).getC_id();
								} else {
									ucCity = new Uc_city();
									ucCity.setC_taluka_id(tkey);
									ucCity.setC_name(user_city_other);
									List<Long> citykeys = model.insertDataReturnGeneratedKeys(ucCity);
									if(citykeys != null) {
										user_city = citykeys.get(0);
									} else {
										SessionManager.setSession(request, "error", "Something went wrong while saving city. Please try again.");
									}
								}
							} else {
								SessionManager.setSession(request, "error", "Something went wrong while saving city. Please try again.");
							}
						}
					
				} else {
					ucDistrict = new Uc_district();
					ucDistrict.setD_s_id(user_state);
					ucDistrict.setD_name(user_district_other);
					List<Long> keys = model.insertDataReturnGeneratedKeys(ucDistrict);
					if(keys != null) {
						long dId = keys.get(0);
						ucTaluka_list = new ArrayList<Uc_taluka>();
						ucTaluka_list = (List<Uc_taluka>)(Object) model.selectData("model.Uc_taluka", "t_name='" + user_taluka_other + "' and t_district_id=" + dId);
						if(!ucTaluka_list.isEmpty()) {
								long tkey = ucTaluka_list.get(0).getT_id();
								ucCity_list = new ArrayList<Uc_city>();
								ucCity_list = (List<Uc_city>)(Object) model.selectData("model.Uc_city", "c_name='" + user_city_other + "' and c_taluka_id=" + tkey);
								if(!ucCity_list.isEmpty()) {
									user_city = ucCity_list.get(0).getC_id();
								} else {
									ucCity = new Uc_city();
									ucCity.setC_taluka_id(tkey);
									ucCity.setC_name(user_city_other);
									List<Long> citykeys = model.insertDataReturnGeneratedKeys(ucCity);
									if(citykeys != null) {
										user_city = citykeys.get(0);
									} else {
										SessionManager.setSession(request, "error", "Something went wrong while saving city. Please try again.");
									}
								}
							
						} else {
							ucTaluka = new Uc_taluka();
							ucTaluka.setT_district_id(dId);
							ucTaluka.setT_name(user_taluka_other);
							List<Long> talukaKey = model.insertDataReturnGeneratedKeys(ucTaluka);
							if(talukaKey != null) {
								long tkey = talukaKey.get(0);
								ucCity_list = new ArrayList<Uc_city>();
								ucCity_list = (List<Uc_city>)(Object) model.selectData("model.Uc_city", "c_name='" + user_city_other + "' and c_taluka_id=" + tkey);
								if(!ucCity_list.isEmpty()) {
									user_city = ucCity_list.get(0).getC_id();
								} else {
									ucCity = new Uc_city();
									ucCity.setC_taluka_id(tkey);
									ucCity.setC_name(user_city_other);
									List<Long> citykeys = model.insertDataReturnGeneratedKeys(ucCity);
									if(citykeys != null) {
										user_city = citykeys.get(0);
									} else {
										SessionManager.setSession(request, "error", "Something went wrong while saving city. Please try again.");
									}
								}
							} else {
								SessionManager.setSession(request, "error", "Something went wrong while saving city. Please try again.");
							}
						}
					} else {
						SessionManager.setSession(request, "error", "Something went wrong while saving city. Please try again.");
					}
				}
			}
		} catch(Exception ex) {
			LogManager.appendToExceptionLogs(ex);
		}
		return user_city;
	}
	
	public long saveOtherCity(String districtName, String talukaName, String cityName) {
		long user_city = -1; 
		String user_district_other, user_taluka_other, user_city_other;
		try {
			long user_state = 0;
			ucState_list = new ArrayList<Uc_state>();
			ucState_list = (List<Uc_state>)(Object) model.selectData("model.Uc_state", "s_name='other'");
			user_state = ucState_list.get(0).getS_id();
				user_district_other = districtName;
				user_taluka_other = talukaName;
				user_city_other = cityName;
				
				ucDistrict_list = new ArrayList<Uc_district>();
				ucDistrict_list = (List<Uc_district>)(Object) model.selectData("model.Uc_district", "d_s_id=" + user_state + " and d_name='" + user_district_other + "'");
				if(ucDistrict_list.isEmpty()) {
					ucDistrict = new Uc_district();
					ucDistrict.setD_s_id(user_state);
					ucDistrict.setD_name(user_district_other);
					List<Long> keys = model.insertDataReturnGeneratedKeys(ucDistrict);
					if(keys != null) {
						long dId = keys.get(0);
						ucTaluka_list = new ArrayList<Uc_taluka>();
						ucTaluka_list = (List<Uc_taluka>)(Object) model.selectData("model.Uc_taluka", "t_district_id=" + dId + " and t_name='" + user_taluka_other + "'");
						if(ucTaluka_list.isEmpty()) {
							ucTaluka = new Uc_taluka();
							ucTaluka.setT_district_id(dId);
							ucTaluka.setT_name(user_taluka_other);
							List<Long> talukaKey = model.insertDataReturnGeneratedKeys(ucTaluka);
							if(talukaKey != null) {
								ucCity_list = new ArrayList<Uc_city>();
								ucCity_list = (List<Uc_city>)(Object) model.selectData("model.Uc_city", "c_taluka_id=" + talukaKey.get(0) + " and c_name='" + user_city_other + "'");
								if(ucCity_list.isEmpty()) {
									ucCity = new Uc_city();
									ucCity.setC_taluka_id(Long.parseLong(talukaKey.get(0).toString()));
									ucCity.setC_name(user_city_other);
									List<Long> citykeys = model.insertDataReturnGeneratedKeys(ucCity);
									if(citykeys != null) {
										user_city = citykeys.get(0);
									}
								} else {
									user_city = ucCity_list.get(0).getC_id();
								} 
							} 
						} else {
							ucCity_list = new ArrayList<Uc_city>();
							ucCity_list = (List<Uc_city>)(Object) model.selectData("model.Uc_city", "c_taluka_id=" + ucTaluka_list.get(0).getT_id() + " and c_name='" + user_city_other + "'");
							if(ucCity_list.isEmpty()) {
								ucCity = new Uc_city();
								ucCity.setC_taluka_id(ucTaluka_list.get(0).getT_id());
								ucCity.setC_name(user_city_other);
								List<Long> citykeys = model.insertDataReturnGeneratedKeys(ucCity);
								if(citykeys != null) {
									user_city = citykeys.get(0);
								}
							} else {
								user_city = ucCity_list.get(0).getC_id();
							} 
						}
					}
				} else {
					long dId = ucDistrict_list.get(0).getD_id();
					ucTaluka_list = new ArrayList<Uc_taluka>();
					ucTaluka_list = (List<Uc_taluka>)(Object) model.selectData("model.Uc_taluka", "t_district_id=" + dId + " and t_name='" + user_taluka_other + "'");
					if(ucTaluka_list.isEmpty()) {
						ucTaluka = new Uc_taluka();
						ucTaluka.setT_district_id(dId);
						ucTaluka.setT_name(user_taluka_other);
						List<Long> talukaKey = model.insertDataReturnGeneratedKeys(ucTaluka);
						if(talukaKey != null) {
							ucCity_list = new ArrayList<Uc_city>();
							ucCity_list = (List<Uc_city>)(Object) model.selectData("model.Uc_city", "c_taluka_id=" + talukaKey.get(0) + " and c_name='" + user_city_other + "'");
							if(ucCity_list.isEmpty()) {
								ucCity = new Uc_city();
								ucCity.setC_taluka_id(Long.parseLong(talukaKey.get(0).toString()));
								ucCity.setC_name(user_city_other);
								List<Long> citykeys = model.insertDataReturnGeneratedKeys(ucCity);
								if(citykeys != null) {
									user_city = citykeys.get(0);
								}
							} else {
								user_city = ucCity_list.get(0).getC_id();
							} 
						} 
					} else {
						ucCity_list = new ArrayList<Uc_city>();
						ucCity_list = (List<Uc_city>)(Object) model.selectData("model.Uc_city", "c_taluka_id=" + ucTaluka_list.get(0).getT_id() + " and c_name='" + user_city_other + "'");
						if(ucCity_list.isEmpty()) {
							ucCity = new Uc_city();
							ucCity.setC_taluka_id(ucTaluka_list.get(0).getT_id());
							ucCity.setC_name(user_city_other);
							List<Long> citykeys = model.insertDataReturnGeneratedKeys(ucCity);
							if(citykeys != null) {
								user_city = citykeys.get(0);
							}
						} else {
							user_city = ucCity_list.get(0).getC_id();
						} 
					}
				}
				
				
			
		} catch(Exception ex) {
			LogManager.appendToExceptionLogs(ex);
		}
		return user_city;
	}
	
	public long savePosition(HttpServletRequest request) {
		long position = -1;
		try {
			ucPosition_list = new ArrayList<Uc_position>();
			ucPosition_list = (List<Uc_position>)(Object) model.selectData("model.Uc_position", "p_name='" + request.getParameter("industry-post").toString() + "' LIMIT 1");
			if(ucPosition_list.isEmpty()) {
				ucPosition = new Uc_position();
				ucPosition.setP_name(request.getParameter("industry-post").toString());
				List<Long> keys = model.insertDataReturnGeneratedKeys(ucPosition);
				if(keys != null) position = keys.get(0);
				else SessionManager.setSession(request, "error", "Something went wrong while saving position. Please try again.");
			} else {
				position = ucPosition_list.get(0).getP_id();
			}
		} catch(Exception ex) {
			LogManager.appendToExceptionLogs(ex);
		}
		return position;
	}
	
	public long savePosition(String pos) {
		long position = -1;
		try {
			ucPosition_list = new ArrayList<Uc_position>();
			ucPosition_list = (List<Uc_position>)(Object) model.selectData("model.Uc_position", "p_name='" + pos + "' LIMIT 1");
			if(ucPosition_list.isEmpty()) {
				ucPosition = new Uc_position();
				ucPosition.setP_name(pos);
				List<Long> keys = model.insertDataReturnGeneratedKeys(ucPosition);
				if(keys != null) position = keys.get(0);
			} else {
				position = ucPosition_list.get(0).getP_id();
			}
		} catch(Exception ex) {
			LogManager.appendToExceptionLogs(ex);
		}
		return position;
	}
}
