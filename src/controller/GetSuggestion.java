package controller;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.List;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.json.JSONArray;

import database.Model;
import model.Uc_city;
import model.Uc_district;
import model.Uc_industry_sector;
import model.Uc_position;
import model.Uc_qualifications;
import model.Uc_state;
import model.Uc_taluka;

@WebServlet("/GetSuggestion")
public class GetSuggestion extends HttpServlet {
	private static final long serialVersionUID = 1L;
       
	private Model model;
	private List<Uc_qualifications> ucQualiList;
	private List<Uc_industry_sector> ucIndustryList;
	private List<Uc_position> ucPositionList;
	private List<Uc_city> ucCityList;
	private List<Uc_district> ucDistrictList;
	private List<Uc_taluka> ucTalukaList;
	private List<Uc_state> ucStateList;
	
    public GetSuggestion() {
        super();
        model = new Model();
    }

	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		if(request.getParameter("autocomplete") == null) {
			//no parameter found
			System.out.println("reached");
		} else {
			int id = Integer.parseInt(request.getParameter("autocomplete").toString());
			if(id == UCConstants.CITY) {
				generateCity(request,response);
			} else if(id == UCConstants.DISTRICT) {
				generateDistrict(request,response);
			} else if(id == UCConstants.INDUSTRY_SECTOR) {
				generateIndustrySector(request,response);
			} else if(id == UCConstants.POSITION) {
				generatePosition(request,response);
			} else if(id == UCConstants.QUALIFICATION) {
				generateQualification(request,response);
			} else if(id == UCConstants.STATE) {
				generateState(request,response);
			} else {
				//wrong parameter value
				System.out.println("reached");
			}
		}
		/*String query = request.getParameter("term");
		String tag = request.getParameter("tag");
		query = query.toLowerCase();
		JSONArray jArray = new JSONArray();
		if(tag.equals("qualification")) {
			ucQualiList = (List<Uc_qualifications>)(Object) m.selectData("model.Uc_qualifications", "1");
			if(!ucQualiList.isEmpty()) {
				for(int i=0 ; i<ucQualiList.size() ; i++) {
					if(ucQualiList.get(i).getQ_name().toString().toLowerCase().startsWith(query)) {
						jArray.put(ucQualiList.get(i).getQ_name().toString());
					} else {
						jArray.put("No suggestions found");
					}
				}
			} else {
				jArray.put("No suggestions found");
			}
		} else if(tag.equals("industry")) {
			ucIndustryList = (List<Uc_industry_sector>)(Object) m.selectData("model.Uc_industry_sector", "1");
			if(!ucIndustryList.isEmpty()) {
				for(int i=0 ; i<ucIndustryList.size() ; i++) {
					if(ucIndustryList.get(i).getIs_name().toString().toLowerCase().startsWith(query)) {
						jArray.put(ucIndustryList.get(i).getIs_name().toString());
					} else {
						jArray.put("No suggestions found");
					}
				}
			} else {
				jArray.put("No suggestions found");
			}
		} else if(tag.equals("post")) {
			ucPositionList = (List<Uc_position>)(Object) m.selectData("model.Uc_position", "1");
			if(!ucPositionList.isEmpty()) {
				for(int i=0 ; i<ucPositionList.size() ; i++) {
					if(ucPositionList.get(i).getP_name().toString().toLowerCase().startsWith(query)) {
						jArray.put(ucPositionList.get(i).getP_name().toString());
					} else {
						jArray.put("No suggestions found");
					}
				}
			} else {
				jArray.put("No suggestions found");
			}
		}
		 PrintWriter out = response.getWriter();
		   out.println(jArray);*/
	}

	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		doGet(request, response);
	}
	
	private void generateCity(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		ucCityList = new ArrayList<Uc_city>();
		ucDistrictList = new ArrayList<Uc_district>();
		ucStateList = new ArrayList<Uc_state>();
		JSONArray json = new JSONArray();
		ucCityList = (List<Uc_city>)(Object) model.selectData("model.Uc_city", "1");
		for(Uc_city city : ucCityList) {
			ucTalukaList = (List<Uc_taluka>)(Object) model.selectData("model.Uc_taluka", "t_id=" + city.getC_taluka_id());
			for(Uc_taluka tal : ucTalukaList) {
				ucDistrictList = (List<Uc_district>)(Object) model.selectData("model.Uc_district", "d_id=" + tal.getT_district_id());
				for(Uc_district dis : ucDistrictList) {
					ucStateList = (List<Uc_state>)(Object) model.selectData("model.Uc_state", "s_id=" + dis.getD_s_id());
					for(Uc_state state : ucStateList) {
						json.put(city.getC_name() + "," + dis.getD_name() + "," + state.getS_name());
					}
				}
			}
		}
		/*for(Uc_city obj : ucCityList) {
			json.put(obj.getC_name());
		}*/
		response.setContentType("application/json");
		response.getWriter().write(json.toString());
	}
	private void generateDistrict(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		ucDistrictList = new ArrayList<Uc_district>();
		ucStateList = new ArrayList<Uc_state>();
		ucDistrictList = (List<Uc_district>)(Object) model.selectData("model.Uc_district", "1");
		JSONArray json = new JSONArray();
		for(Uc_district dis : ucDistrictList) {
			ucStateList = (List<Uc_state>)(Object) model.selectData("model.Uc_state", "s_id=" + dis.getD_s_id());
			for(Uc_state state : ucStateList) {
				json.put(dis.getD_name() + "," + state.getS_name());
			}
		}
		response.setContentType("application/json");
		response.getWriter().write(json.toString());
	}
	private void generateIndustrySector(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		ucIndustryList = new ArrayList<Uc_industry_sector>();
		ucIndustryList = (List<Uc_industry_sector>)(Object) model.selectData("model.Uc_industry_sector", "1");
		JSONArray json = new JSONArray();
		for(Uc_industry_sector obj : ucIndustryList) {
			json.put(obj.getIs_name());
		}
		response.setContentType("application/json");
		response.getWriter().write(json.toString());
	}
	private void generatePosition(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		ucPositionList = new ArrayList<Uc_position>();
		ucPositionList = (List<Uc_position>)(Object) model.selectData("model.Uc_position", "1");
		JSONArray json = new JSONArray();
		for(Uc_position obj : ucPositionList) {
			json.put(obj.getP_name());
		}
		response.setContentType("application/json");
		response.getWriter().write(json.toString());
	}
	private void generateQualification(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		ucQualiList = new ArrayList<Uc_qualifications>();
		ucQualiList = (List<Uc_qualifications>)(Object) model.selectData("model.Uc_qualifications", "1");
		JSONArray json = new JSONArray();
		for(Uc_qualifications obj : ucQualiList) {
			json.put(obj.getQ_name());
		}
		response.setContentType("application/json");
		response.getWriter().write(json.toString());
	}
	private void generateState(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		ucStateList = new ArrayList<Uc_state>();
		ucStateList = (List<Uc_state>)(Object) model.selectData("model.Uc_state", "1");
		JSONArray json = new JSONArray();
		for(Uc_state obj : ucStateList) {
			json.put(obj.getS_name());
		}
		response.setContentType("application/json");
		response.getWriter().write(json.toString());
	}

}
