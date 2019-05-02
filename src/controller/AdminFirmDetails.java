package controller;

import java.io.IOException;
import java.util.Hashtable;

import javax.servlet.ServletException;
import javax.servlet.annotation.MultipartConfig;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.Part;

import database.Model;
import model.Uc_firm_details;
import utils.EncryptionManager;
import utils.FileManager;
import utils.RouteManager;
import utils.SessionManager;
import utils.ViewManager;

@WebServlet("/admin/firm")
@MultipartConfig(fileSizeThreshold=1024*1024*2, // 2MB
maxFileSize=1024*1024*10,      // 10MB
maxRequestSize=1024*1024*50)   // 50MB
public class AdminFirmDetails extends HttpServlet {
	private static final long serialVersionUID = 1L;
       
	private Uc_firm_details ucFirm;
	private Model model;
	private FileManager fileManager;
	
    public AdminFirmDetails() {
        super();
        model = new Model();
    }

	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		Hashtable sessionAttributes = new Hashtable();
		sessionAttributes = (Hashtable) SessionManager.sessionExists(request, "admin");
		if(request.getParameter("action") == null || request.getParameter("action").toString().equals("")) {
			if(Integer.parseInt(sessionAttributes.get("utype").toString()) == UCConstants.MAIN_ADMIN) {
				firmDetails(request, response);
			} else {
				ViewManager.showView(request, response, "admin/accessdenied.jsp");
			}
		} else {
			if(request.getParameter("action").toString().toLowerCase().equals("save")) {
				if(Integer.parseInt(sessionAttributes.get("utype").toString()) == UCConstants.MAIN_ADMIN) {
					saveChanges(request, response);
				} else {
					ViewManager.showView(request, response, "admin/accessdenied.jsp");
				}
			}
			if(request.getParameter("action").toString().toLowerCase().equals("img")) {
				showImage(request, response);
			}
		}
	}

	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		doGet(request, response);
	}
	
	private void showImage(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		//FileManager.downloadFile(request, response, "resumes", request.getParameter("file"));
		ViewManager.showView(request, response, "download.jsp?fileName=" + request.getParameter("fileName") + "&fileType=image");
	}
	
	private void firmDetails(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		ucFirm = new Uc_firm_details();
		ucFirm = (Uc_firm_details) model.selectData("model.Uc_firm_details", "1").get(0);
		if(!ucFirm.getFd_logo().equals("N/A")) {
			//String logoImg = FileManager.getFileUrl(request, "ucFirm", ucFirm.getFd_logo());
			request.setAttribute("logoUrl", ucFirm.getFd_logo());
		} else {
			request.setAttribute("logoUrl", "");
		}
		if(!ucFirm.getFd_banner().equals("N/A")) {
			//String bannerImg = FileManager.getFileUrl(request, "ucFirm", ucFirm.getFd_banner());
			request.setAttribute("bannerUrl", ucFirm.getFd_banner());
		} else {
			request.setAttribute("bannerUrl", "");
		}
		request.setAttribute("firm", ucFirm);
		
		ViewManager.showView(request, response, "admin/firmdetails.jsp");
	}
	
	private void saveChanges(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		try {
			fileManager = new FileManager();
		} catch(Exception ex) {
			
		}
		ucFirm = new Uc_firm_details();
		ucFirm = (Uc_firm_details) model.selectData("model.Uc_firm_details", "1").get(0);
		String oldLogo = ucFirm.getFd_logo();
		String oldBanner = ucFirm.getFd_banner();
		
		String firmName = request.getParameter("firm_name");
		Long contactOne = request.getParameter("firm_contact_one").toString().equals("") ? 010: Long.parseLong(request.getParameter("firm_contact_one").toString());
		Long contactTwo = request.getParameter("firm_contact_two").toString().equals("") ? 010 : Long.parseLong(request.getParameter("firm_contact_two").toString());
		String emailOne = request.getParameter("firm_email_one");
		String emailTwo = request.getParameter("firm_email_two");
		String slogan = request.getParameter("firm_slogan");
		String address = request.getParameter("firm_address");
		String about = request.getParameter("firm_about");
		String bank_acc_name = request.getParameter("firm_bank_acc_name");
		String bank_acc_no = request.getParameter("firm_bank_acc_no");
		String bank_ifsc = request.getParameter("firm_bank_ifsc");
		String bank_branch = request.getParameter("firm_bank_branch");
		String facebook = request.getParameter("firm_facebook");
		String twitter = request.getParameter("firm_twitter");
		Long whatsapp = request.getParameter("firm_whatsapp").toString().equals("") ? 010 : Long.parseLong(request.getParameter("firm_whatsapp").toString());
		String whatsapp_grp1 = request.getParameter("firm_whatsapp_grp_one");
		String whatsapp_grp2 = request.getParameter("firm_whatsapp_grp_two");
		String gplus = request.getParameter("firm_gplus");
		String linkedin = request.getParameter("firm_linkedin");
		String instagram = request.getParameter("firm_instagram");
		String map = request.getParameter("firm_map");
		Part logo = request.getPart("firm_logo");
		Part banner = request.getPart("firm_banner");
		
		String send_email = request.getParameter("firm_send_email");
		String email_pass = request.getParameter("firm_send_email_pass");
		String email_host = request.getParameter("firm_email_host");
		int email_port = request.getParameter("firm_email_port").toString().equals("") ? 010 : Integer.parseInt(request.getParameter("firm_email_port").toString());
		
		ucFirm = new Uc_firm_details();
		if(!firmName.equals("")) ucFirm.setFd_name(firmName);
		if(contactOne != 010) ucFirm.setFd_contact_one(contactOne);
		if(contactTwo != 010) ucFirm.setFd_contact_two(contactTwo);
		if(!emailOne.equals("")) ucFirm.setFd_email_one(emailOne);
		if(!emailTwo.equals("")) ucFirm.setFd_email_two(emailTwo);
		if(!slogan.equals("")) {
			slogan = slogan.replace("'", "\\'");
			ucFirm.setFd_slogan(slogan);
		}
		if(!address.equals("")) {
			address = address.replace("'", "\\'");
			ucFirm.setFd_address(address);
		}
		if(!about.equals("")) {
			about = about.replace("'", "\\'");
			ucFirm.setFd_about(about);
		}
		if(!bank_acc_name.equals("")) ucFirm.setFd_bank_acc_name(bank_acc_name);
		if(!bank_acc_no.equals("")) ucFirm.setFd_bank_acc_no(bank_acc_no);
		if(!bank_ifsc.equals("")) ucFirm.setFd_bank_ifsc(bank_ifsc);
		if(!bank_branch.equals("")) ucFirm.setFd_bank_branch(bank_branch);
		if(!facebook.equals("")) ucFirm.setFd_facebook(facebook);
		if(!twitter.equals("")) ucFirm.setFd_twitter(twitter);
		if(whatsapp != 010) ucFirm.setFd_whatsapp(whatsapp);
		if(!whatsapp_grp1.equals("")) ucFirm.setFd_wa_grp_one(whatsapp_grp1);
		if(!whatsapp_grp2.equals("")) ucFirm.setFd_wa_grp_two(whatsapp_grp2);
		if(!gplus.equals("")) ucFirm.setFd_gplus(gplus);
		if(!linkedin.equals("")) ucFirm.setFd_linkedin(linkedin);
		if(!instagram.equals("")) ucFirm.setFd_instagram(instagram);
		if(!map.equals("")) ucFirm.setFd_map_url(map);
		if(!send_email.equals("")) ucFirm.setFd_send_email(send_email);
		if(!email_pass.equals("")) ucFirm.setFd_send_email_pass(EncryptionManager.encryptData(email_pass));
		if(!email_host.equals("")) ucFirm.setFd_email_host(email_host);
		if(email_port != 010) ucFirm.setFd_email_port(email_port);
		String logoName = getFileName(logo);
		String bannerName = getFileName(banner);
		if(!logoName.equals("")) ucFirm.setFd_logo("ucLogo" + logoName.substring(logoName.lastIndexOf(".")));
		if(!bannerName.equals("")) ucFirm.setFd_banner("ucBanner" + bannerName.substring(bannerName.lastIndexOf(".")));
		
		boolean flag = true;
		
		if(model.updateData(ucFirm, "fd_id=1")) {
			if(!logoName.equals("")) {
				/*if(!oldLogo.equals("N/A")) FileManager.deleteFile(request, "ucFirm", oldLogo);
				if(!FileManager.upLoadFile(request, "ucFirm", logo, "ucLogo" + logo.getSubmittedFileName().substring(logo.getSubmittedFileName().lastIndexOf(".")))) flag = false;*/
				try {
					if(!oldLogo.equals("N/A")) fileManager.deleteFTPFile(UCConstants.UC_FILES + oldLogo);
					fileManager.uploadFTPFile(logo.getInputStream(), "ucLogo" + logoName.substring(logoName.lastIndexOf(".")), UCConstants.UC_FILES);
				} catch(Exception ex) {
					flag = false;
				}
			}
			if(!bannerName.equals("")) {
				/*if(!oldBanner.equals("N/A")) FileManager.deleteFile(request, "ucFirm", oldBanner);
				if(!FileManager.upLoadFile(request, "ucFirm", banner, "ucBanner" + banner.getSubmittedFileName().substring(banner.getSubmittedFileName().lastIndexOf(".")))) flag = false;*/
				try {
					if(!oldBanner.equals("N/A")) fileManager.deleteFTPFile(UCConstants.UC_FILES + oldBanner);
					fileManager.uploadFTPFile(banner.getInputStream(), "ucBanner" + bannerName.substring(bannerName.lastIndexOf(".")), UCConstants.UC_FILES);
				} catch(Exception ex) {
					flag = false;
				}
			}
		} else {
			flag = false;
		}
		
		if(flag) {
			SessionManager.setSession(request, "success", "Changes are successfully saved!");
		} else {
			SessionManager.setSession(request, "error", "Something went wrong. Please try again.");
		}
		fileManager.disconnect();
		RouteManager.route(response, "admin/firm");
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
