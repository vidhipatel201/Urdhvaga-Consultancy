package controller;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.Hashtable;
import java.util.List;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import database.Model;
import model.Uc_current_jobs;
import model.Uc_job_applications;
import model.Uc_position;
import utils.EncryptionManager;
import utils.SessionManager;
import utils.ViewManager;

@WebServlet("/apply")
public class JobApplication extends HttpServlet {
	private static final long serialVersionUID = 1L;
       
	private Model model;
	private Uc_job_applications ucJobApp;
	private List<Uc_current_jobs> ucCurrJobs_list;
	private List<Uc_job_applications> ucJobApp_list;
	private List<Uc_position> ucPosition_list;
	private Uc_current_jobs ucCurrJobs;
	
    public JobApplication() {
        super();
        model = new Model();
    }

	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		
		System.out.println(request.getRequestURI() + " " + request.getRequestURL());
		
		Hashtable sessionAttributes = (Hashtable) SessionManager.sessionExists(request, "uc_client_session");
		long id = Long.parseLong(sessionAttributes.get("id").toString());
		String job = EncryptionManager.decryptData(request.getParameter("job").toString());
		if(job.matches("^\\d+$") && !job.equals("")) {
			ucCurrJobs_list = new ArrayList<Uc_current_jobs>();
			ucCurrJobs_list = (List<Uc_current_jobs>)(Object) model.selectData("model.Uc_current_jobs", "cj_id=" + job + " and cj_is_active=1");
			if(ucCurrJobs_list != null && !ucCurrJobs_list.isEmpty()) {
				ucJobApp_list = new ArrayList<Uc_job_applications>();
				ucJobApp_list = (List<Uc_job_applications>)(Object) model.selectData("model.Uc_job_applications", "ja_job=" + job + " and ja_candidate=" + id);
				if(ucJobApp_list.isEmpty()) {
					ucJobApp = new Uc_job_applications();
					//ucJobApp.setJa_accepted(2);
					ucJobApp.setJa_candidate(id);
					ucJobApp.setJa_job(Long.parseLong(job));
					Date date = new Date();
					SimpleDateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy");
					//ucJobApp.setJa_date(dateFormat.format(date));
					ucJobApp.setJa_date(new java.sql.Date(Calendar.getInstance().getTime().getTime()));
					dateFormat = new SimpleDateFormat("HH:mm:ss");
					ucJobApp.setJa_time(dateFormat.format(date));
					ucPosition_list = new ArrayList<Uc_position>();
					ucPosition_list = (List<Uc_position>)(Object) model.selectData("model.Uc_position", "p_id=" + ucCurrJobs_list.get(0).getCj_position());
					model.startTransaction();
					if(model.insertData(ucJobApp)) {
						//successfully applied
						if(ucCurrJobs_list.get(0).getCj_has_applicant() == 0) {
							ucCurrJobs = new Uc_current_jobs();
							ucCurrJobs.setCj_has_applicant(1);
							ucCurrJobs.setCj_number_of_new_applicant(ucCurrJobs_list.get(0).getCj_number_of_new_applicant() + 1);
							if(model.updateData(ucCurrJobs, "cj_id=" + job)) {
								SessionManager.setSession(request, "apply_success", "<b>GRACIAS!!</b> Your application for the job of " + ucPosition_list.get(0).getP_name() + " has been successfully submitted. You will be notified soon by us for any further process.");
							} else {
								SessionManager.setSession(request, "apply_error", "Something went wrong while submitting your application for the job of" + ucPosition_list.get(0).getP_name() + ". Please try again.");
							}
						} else {
							SessionManager.setSession(request, "apply_success", "<b>GRACIAS!!</b> Your application for the job of " + ucPosition_list.get(0).getP_name() + " has been successfully submitted. You will be notified soon by us for any further process.");
						}
					} else {
						//something went wrong
						SessionManager.setSession(request, "apply_error", "Something went wrong while submitting your application for the job of" + ucPosition_list.get(0).getP_name() + ". Please try again.");
					}
					model.endTransaction();
				} else {
					//already applied
					SessionManager.setSession(request, "apply_error", "Your application for this job have already been submitted to us. Be patience, soon we will respond to your application.");
				}
			} else {
				//no such job
				SessionManager.setSession(request, "apply_error", "Opps!! Seems like there is no such job for which you want to apply.");
			}

		} else {
			//no such job
			SessionManager.setSession(request, "apply_error", "Opps!! Seems like there is no such job for which you want to apply.");

		}
		ViewManager.showView(request, response, "jobapplyresult.jsp");
	}

	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		doGet(request, response);
	}

}
