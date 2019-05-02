package controller;

import java.io.IOException;
import java.security.SecureRandom;
import java.util.ArrayList;
import java.util.List;
import java.util.Properties;

import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.PasswordAuthentication;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang3.RandomStringUtils;

import database.Model;
import model.Uc_candidate_details;
import model.Uc_firm_details;
import utils.EncryptionManager;
import utils.RouteManager;
import utils.SessionManager;
import utils.ViewManager;

@WebServlet("/forgot-password")
public class ForgotPassword extends HttpServlet {
	private static final long serialVersionUID = 1L;
       
	private Model model;
	private Uc_candidate_details ucCandidateDetails;
	private Uc_firm_details ucFirmDetails;
	private List<Uc_candidate_details> ucCandidateDetails_list;
    public ForgotPassword() {
        super();
        model = new Model();
    }

	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		if(SessionManager.sessionExists((HttpServletRequest)request, "uc_client_session") == null) {
			if(request.getParameter("action") == null) {
				ViewManager.showView(request, response, "forgotpassword.jsp");
			} else {
				if(request.getParameter("action").equals("")) {
					ViewManager.showView(request, response, "forgotpassword.jsp");
				} else {
					if(request.getParameter("action").equalsIgnoreCase("requestnewpassword")) {
						try {
							requestNewPassword(request, response);
						}
						catch(Exception ex) {
							ex.printStackTrace();
						}
					}
				}
			}
		} else {
			RouteManager.route(response, "");
		}
	}

	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		doGet(request, response);
	}
	
	private void requestNewPassword(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		String email = request.getParameter("user_email");
		ucFirmDetails = new Uc_firm_details();
		ucFirmDetails = (Uc_firm_details) model.selectData("model.Uc_firm_details", "1").get(0);
		ucCandidateDetails_list = new ArrayList<Uc_candidate_details>();
		ucCandidateDetails_list = (List<Uc_candidate_details>)(Object) model.selectData("model.Uc_candidate_details", "cd_email=BINARY'" + email + "'");
		char[] possibleCharacters = (new String("ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz0123456789~!@#$%^&*()-_=+?")).toCharArray();
		String newPassword = RandomStringUtils.random(10, 0, possibleCharacters.length-1, false, false, possibleCharacters, new SecureRandom() );
		if(!ucCandidateDetails_list.isEmpty()) {
			if(ucCandidateDetails_list.size() > 1) {
				SessionManager.setSession(request, "error", "Your account password cannot be reset. Please contact admin for this issue.");
			} else {
			String host = ucFirmDetails.getFd_email_host();  
			  String user = ucFirmDetails.getFd_send_email();//change accordingly  
			  String password = EncryptionManager.decryptData(ucFirmDetails.getFd_send_email_pass());//change accordingly  
			  int port = ucFirmDetails.getFd_email_port();  
			  String to = email;//change accordingly  
			  
			   //Get the session object  
			   Properties props = new Properties();
			   if(host.equals("smtp.gmail.com")) {
				   props.put("mail.smtp.host", "smtp.gmail.com");
				    props.put("mail.smtp.socketFactory.port", "465");
				    props.put("mail.smtp.socketFactory.class",
				            "javax.net.ssl.SSLSocketFactory");
				    props.put("mail.smtp.auth", "true");
				    props.put("mail.smtp.port", port); 
			   } else {
				   props.put("mail.smtp.host",host);
				   props.put("mail.smtp.port", port); 
				   props.put("mail.smtp.auth", "true");  
			   }
			     
			   Session session = Session.getDefaultInstance(props,  
			    new javax.mail.Authenticator() {  
			      protected PasswordAuthentication getPasswordAuthentication() {  
			    return new PasswordAuthentication(user,password);  
			      }  
			    });  
			  
			   //Compose the message  
			    try {  
			     MimeMessage message = new MimeMessage(session);  
			     message.setFrom(new InternetAddress(user));  
			     message.addRecipient(Message.RecipientType.TO,new InternetAddress(to));  
			     message.setSubject("Urdhvaga Account Password Recovery");  
			     message.setText("Your new Password for Urdhvaga Consultancy's account is: <b>" + newPassword + "</b><br>Please change your password after login.<br><br>Regards,<br>Urdhvaga Consultancy<br>http://urdhvaga.in", "utf-8", "html");  
			       
			    //send the message  
			     Transport.send(message);
			     
			     
			     ucCandidateDetails = new Uc_candidate_details();
			     ucCandidateDetails.setCd_password(EncryptionManager.generateHash(newPassword));
			     if(model.updateData(ucCandidateDetails, "cd_email=BINARY'" + to + "'")) {
			    	 SessionManager.setSession(request, "success", "Email with new password is sent to you. Please check your inbox."); 
			     } else {
			    	 SessionManager.setSession(request, "error", "Something went wrong. Please try again.");
			     }  
			     
			   
			     } catch (MessagingException e) {
			    	 e.printStackTrace();
			    	 SessionManager.setSession(request, "error", "Something went wrong. Please try again.");
			       }   
			}
		} else {
			SessionManager.setSession(request, "error", "Entered Email ID is not registered.");
		}
		RouteManager.route(response, "forgot-password");
	}

}
