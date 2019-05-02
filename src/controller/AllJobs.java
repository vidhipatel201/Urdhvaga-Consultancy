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
import model.Uc_city;
import model.Uc_cj_quantity;
import model.Uc_current_jobs;
import model.Uc_district;
import model.Uc_position;
import model.Uc_state;
import model.Uc_taluka;
import utils.EncryptionManager;
import utils.RouteManager;
import utils.ViewManager;

@WebServlet("/jobs")
public class AllJobs extends HttpServlet {
	private static final long serialVersionUID = 1L;
       
	private Model model;
	private List<Uc_current_jobs> ucCurrJobs_list;
	private List<Uc_cj_quantity> ucCjQuantity_list;
    public AllJobs() {
        super();
        model = new Model();
    }

	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		if(request.getParameter("action") == null) {
			ViewManager.showView(request, response, "alljobs.jsp");
		} else {
			if(request.getParameter("action").equals("")) {
				ViewManager.showView(request, response, "alljobs.jsp");
			} else {
				if(request.getParameter("action").equals("all")) {
					try {
						showAllJobs(request, response);
					} catch(Exception ex) {}
				}
			}
		}
	}

	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		doGet(request, response);
	}
	
	private void showAllJobs(HttpServletRequest request, HttpServletResponse response) throws ServletException, JSONException, IOException {
		int pageNo = Integer.parseInt(request.getParameter("pageNo").toString());
		int limit = Integer.parseInt(request.getParameter("limit").toString());
		boolean begin = request.getParameter("begin").toLowerCase().equals("true") ? true : false;
		int start = (pageNo-1)*limit;
		ucCurrJobs_list = new ArrayList<Uc_current_jobs>();
		ucCurrJobs_list = (List<Uc_current_jobs>)(Object) model.selectData("model.Uc_current_jobs", "cj_is_active=1 order by cj_id desc LIMIT " + start + "," + limit);
		if(ucCurrJobs_list.isEmpty()) {
			response.setContentType("application/json");
			JSONObject jsonResponse = new JSONObject();
			String noResult = "<div class=\"col-lg-12\"><div class=\"alert alert-danger\">No job found.</div></div>";
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
				//Uc_cj_quantity q = new Uc_cj_quantity();
				//q = (Uc_cj_quantity) model.selectData("model.Uc_cj_quantity", "cjq_job_id=" + job.getCj_id()).get(0);
				ucCjQuantity_list = new ArrayList<Uc_cj_quantity>();
				ucCjQuantity_list = (List<Uc_cj_quantity>)(Object)model.selectData("model.Uc_cj_quantity", "cjq_job_id=" + job.getCj_id());
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
			temp.append("<button class=\"btn mybtn\" onclick=\"showTCDirect('" + EncryptionManager.encryptData(String.valueOf(job.getCj_id())) +"')\" style=\"margin-left:10px;\"><i style=\"color:#FFC300\" class=\"fa fa-check\"></i> &nbsp;Apply</button>");
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
			ucCurrJobs_list = (List<Uc_current_jobs>)(Object) model.selectData("model.Uc_current_jobs", "cj_is_active=1");
			int pageCount = (int) Math.ceil(ucCurrJobs_list.size()/(float)limit);
			if(pageCount == 0) pageCount = 1;
			 jsonResponse.put("pageCount", pageCount);
		}
		response.getWriter().write(jsonResponse.toString());
		}
	}

}
