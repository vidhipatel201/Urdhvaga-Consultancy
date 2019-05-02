package controller;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Hashtable;
import java.util.List;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import database.Model;
import model.Uc_candidate_qualification;
import model.Uc_city;
import model.Uc_cj_quantity;
import model.Uc_current_jobs;
import model.Uc_district;
import model.Uc_employer_details;
import model.Uc_interviews;
import model.Uc_job_facilities;
import model.Uc_job_facilities_mapping;
import model.Uc_job_qualifications;
import model.Uc_position;
import model.Uc_qualifications;
import model.Uc_state;
import model.Uc_taluka;
import utils.EncryptionManager;
import utils.SessionManager;

@WebServlet("/job")
public class JobDetails extends HttpServlet {
	private static final long serialVersionUID = 1L;

	private List<Uc_current_jobs> ucCurrJob_list;
	private Model model;
	private Uc_current_jobs ucCurrJob;
	private Uc_position ucPosition;
	private Uc_cj_quantity ucCjQuantity;
	private List<Uc_cj_quantity> ucCjQuantity_list;
	private List<Uc_job_qualifications> ucJobQualification_list;
	private Uc_qualifications ucQualification;
	private Uc_city ucCity;
	private Uc_taluka ucTaluka;
	private Uc_district ucDistrict;
	private Uc_state ucState;
	private List<Uc_interviews> ucInterview_list;
	private Uc_interviews ucInterview;
	private Uc_employer_details ucEmployer;
	
	public JobDetails() {
        super();
        model = new Model();
    }

	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		if(request.getParameter("action") == null || request.getParameter("action").toString().equals("")) {
			generateJobDetails(request, response);
		} else {
			if(request.getParameter("action").toLowerCase().equals("getinterviewschedule")) {
				getInterviewSchedule(request, response);
			} 
		}
	}

	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		doGet(request, response);
	}
	
	private void generateJobDetails(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		int jobId = Integer.parseInt(EncryptionManager.decryptData(request.getParameter("jobId").toString()));
		ucCurrJob_list = new ArrayList<Uc_current_jobs>();
		ucCurrJob_list = (List<Uc_current_jobs>)(Object) model.selectData("model.Uc_current_jobs", "cj_id=" + jobId);
		response.setContentType("text/html;charset=UTF-8");
		if(!ucCurrJob_list.isEmpty()) {
			StringBuilder temp = new StringBuilder();
			ucCurrJob = new Uc_current_jobs();
			ucCurrJob = ucCurrJob_list.get(0);
			temp.append("<table>");
			ucPosition = new Uc_position();
			ucPosition = (Uc_position) model.selectData("model.Uc_position", "p_id=" + ucCurrJob.getCj_position()).get(0);
			temp.append("<tr><th>Position: </th><td>" + ucPosition.getP_name() + "</td></tr>");
			if(ucCurrJob.getCj_quantity() == 0) {
				//ucCjQuantity = new Uc_cj_quantity();
				//ucCjQuantity = (Uc_cj_quantity) model.selectData("model.Uc_cj_quantity", "cjq_job_id=" + ucCurrJob.getCj_id()).get(0);
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
			temp.append("<tr><th>Qualification Required: </th><td>");
			ucJobQualification_list = new ArrayList<Uc_job_qualifications>();
			ucJobQualification_list = (List<Uc_job_qualifications>)(Object) model.selectData("model.Uc_job_qualifications", "jq_job_id=" + ucCurrJob.getCj_id());
			String postfix = "";
			for(Uc_job_qualifications jq : ucJobQualification_list) {
				ucQualification = new Uc_qualifications();
				ucQualification = (Uc_qualifications) model.selectData("model.Uc_qualifications", "q_id=" + jq.getJq_qualification_id()).get(0);
				temp.append(postfix);
				postfix = ", ";
				temp.append(ucQualification.getQ_name());
			}
			temp.append("</td></tr>");
			temp.append("<tr><th>Experience Required: </th><td>" + (ucCurrJob.getCj_experience_start() == ucCurrJob.getCj_experience_end() ? ucCurrJob.getCj_experience_start() : ucCurrJob.getCj_experience_start() + "-" + ucCurrJob.getCj_experience_end()) + " years</td></tr>");
			temp.append("<tr><th>Job duration: </th><td>" + (ucCurrJob.getCj_duty_hours().equals("0") ? "N/A" : ucCurrJob.getCj_duty_hours() + " hours") + "</td></tr>");
			temp.append("<tr><th>Salary: </th><td>" + (ucCurrJob.getCj_salary().equals("0") ? "N/A" : "INR " + ucCurrJob.getCj_salary()) + "</td></tr>");
			ucCity = new Uc_city();
			ucCity = (Uc_city) model.selectData("model.Uc_city", "c_id=" + ucCurrJob.getCj_city()).get(0);
			ucTaluka = new Uc_taluka();
			ucTaluka = (Uc_taluka) model.selectData("model.Uc_taluka", "t_id=" + ucCity.getC_taluka_id()).get(0);
			ucDistrict = new Uc_district();
			ucDistrict = (Uc_district) model.selectData("model.Uc_district", "d_id=" + ucTaluka.getT_district_id()).get(0);
			ucState = new Uc_state();
			ucState = (Uc_state) model.selectData("model.Uc_state", "s_id=" + ucDistrict.getD_s_id()).get(0);
			temp.append("<tr><th>Job Location: </th><td>" + ucCity.getC_name() + ", " + ucTaluka.getT_name() + ", " + ucDistrict.getD_name() + ", " + ucState.getS_name() + "</td></tr>");
			temp.append("<tr><th>Work Profile: </th><td>" + ucCurrJob.getCj_work_profile() + "</td></tr>");
			List<Uc_job_facilities> ucJobFacilities_list = new ArrayList<>();
			List<Uc_job_facilities_mapping> ucJobFacilitiesMapping_list = new ArrayList<>();
			ucJobFacilitiesMapping_list  = (List<Uc_job_facilities_mapping>)(Object) model.selectData("model.Uc_job_facilities_mapping", "jfm_current_opening_id=" + ucCurrJob.getCj_id());
			temp.append("<tr><th>Facilities Included: </th><td>");
			if(ucJobFacilitiesMapping_list != null && !ucJobFacilitiesMapping_list.isEmpty()) {
				ucJobFacilities_list  = (List<Uc_job_facilities>)(Object) model.selectData("model.Uc_job_facilities", "1");
				String prefix = "";
				for(Uc_job_facilities facility : ucJobFacilities_list) {
					if(ucJobFacilitiesMapping_list != null && !ucJobFacilitiesMapping_list.isEmpty()) {
						for(Uc_job_facilities_mapping facilityMapping : ucJobFacilitiesMapping_list) {
							if(facilityMapping.getJfm_job_facilities_id() == facility.getJf_id()) {
								temp.append(prefix);
								prefix = ", ";
								temp.append(facility.getJf_name());
							}
						}	
					}
				}
			} else {
				temp.append(" - ");
			}
			temp.append("</td></tr>");
			temp.append("</table>");
			response.getWriter().write(temp.toString());
		} else {
			response.getWriter().write("<b>Sorry! No such job found. Please refresh the page and try again.</b>");
		}
	}
	
	private void getInterviewSchedule(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		int jobId = Integer.parseInt(EncryptionManager.decryptData(request.getParameter("jobId").toString()));
		ucInterview_list = new ArrayList<Uc_interviews>();
		ucInterview_list = (List<Uc_interviews>)(Object) model.selectData("model.Uc_interviews", "i_job_id=" + jobId);
		response.setContentType("text/html;charset=UTF-8");
		if(!ucInterview_list.isEmpty()) {
			ucInterview = new Uc_interviews();
			ucInterview = ucInterview_list.get(0);
			StringBuilder temp = new StringBuilder();
			temp.append("<table>");
			temp.append("<tr><th>Date: </th><td>" + ucInterview.getI_date() + "</td></tr>");
			temp.append("<tr><th>Time: </th><td>" + ucInterview.getI_time() + "</td></tr>");
			String loc, contactPerson;
			if(ucInterview.getI_location().equals("same")) {
				ucCurrJob = new Uc_current_jobs();
				ucCurrJob = (Uc_current_jobs) model.selectData("model.Uc_current_jobs", "cj_id=" + jobId).get(0);
				ucEmployer = new Uc_employer_details();
				ucEmployer = (Uc_employer_details) model.selectData("model.Uc_employer_details", "ed_id=" + ucCurrJob.getCj_employer_id()).get(0);
				loc = ucEmployer.getEd_address();
				if(ucInterview.getI_contact_person().equals("N/A")) {
					contactPerson = ucEmployer.getEd_contact_person();
				} else {
					contactPerson = ucInterview.getI_contact_person();
				}
			} else {
				loc = ucInterview.getI_location();
				if(ucInterview.getI_contact_person().equals("N/A")) {
					ucCurrJob = new Uc_current_jobs();
					ucCurrJob = (Uc_current_jobs) model.selectData("model.Uc_current_jobs", "cj_id=" + jobId).get(0);
					ucEmployer = new Uc_employer_details();
					ucEmployer = (Uc_employer_details) model.selectData("model.Uc_employer_details", "ed_id=" + ucCurrJob.getCj_employer_id()).get(0);
					contactPerson = ucEmployer.getEd_contact_person();
				} else {
					contactPerson = ucInterview.getI_contact_person();
				}
			}
			temp.append("<tr><th>Location: </th><td>" + loc + "</td></tr>");
			temp.append("<tr><th>Contact Person: </th><td>" + contactPerson + "</td></tr>");
			temp.append("</table>");
			response.getWriter().write(temp.toString());
		} else {
			response.getWriter().write("<b>Sorry! No interview schedule found. Please refresh the page and try again.</b>");
		}
	}

}
