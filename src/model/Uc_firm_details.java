package model;

import java.sql.ResultSet;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import logs.LogManager;

public class Uc_firm_details {
	
	private Integer fd_email_port;
	private Long fd_id, fd_contact_one, fd_contact_two, fd_whatsapp;
	private String fd_name, fd_email_one, fd_email_two, fd_logo, fd_banner, fd_slogan, fd_address, fd_facebook, fd_twitter, fd_gplus, fd_linkedin, fd_instagram, fd_map_url, fd_about, fd_wa_grp_one, fd_wa_grp_two, fd_bank_acc_name, fd_bank_acc_no, fd_bank_ifsc, fd_bank_branch, fd_send_email, fd_send_email_pass, fd_email_host;
	
	public Uc_firm_details() {
		/*fd_id = -1; fd_contact_one = -1; fd_contact_two = -1;
		fd_name = "-1" ; fd_email_one = "-1"; fd_email_two = "-1"; fd_logo = "-1"; fd_banner = "-1"; fd_slogan = "-1";*/
		fd_id = null; fd_contact_one = null; fd_contact_two = null; fd_whatsapp = null;
		fd_name = null; fd_email_one = null; fd_email_two = null; fd_logo = null; fd_banner = null; fd_slogan = null; fd_address = null; fd_facebook = null; fd_twitter = null; fd_gplus = null; fd_linkedin = null; fd_instagram = null; fd_map_url = null; fd_about = null; fd_wa_grp_one = null; fd_wa_grp_two = null; fd_bank_acc_name = null; fd_bank_acc_no = null; fd_bank_ifsc = null; fd_bank_branch = null;
		fd_send_email = null; fd_send_email_pass = null; fd_email_host = null; fd_email_port = null;
	}
	
	public String getFd_send_email() {
		return fd_send_email;
	}

	public void setFd_send_email(String fd_send_email) {
		this.fd_send_email = fd_send_email;
	}

	public String getFd_send_email_pass() {
		return fd_send_email_pass;
	}

	public void setFd_send_email_pass(String fd_send_email_pass) {
		this.fd_send_email_pass = fd_send_email_pass;
	}

	public String getFd_email_host() {
		return fd_email_host;
	}

	public void setFd_email_host(String fd_email_host) {
		this.fd_email_host = fd_email_host;
	}

	public Integer getFd_email_port() {
		return fd_email_port;
	}

	public void setFd_email_port(Integer fd_email_port) {
		this.fd_email_port = fd_email_port;
	}

	public String getFd_wa_grp_one() {
		return fd_wa_grp_one;
	}

	public void setFd_wa_grp_one(String fd_wa_grp_one) {
		this.fd_wa_grp_one = fd_wa_grp_one;
	}

	public String getFd_wa_grp_two() {
		return fd_wa_grp_two;
	}

	public void setFd_wa_grp_two(String fd_wa_grp_two) {
		this.fd_wa_grp_two = fd_wa_grp_two;
	}

	public String getFd_bank_acc_name() {
		return fd_bank_acc_name;
	}

	public void setFd_bank_acc_name(String fd_bank_acc_name) {
		this.fd_bank_acc_name = fd_bank_acc_name;
	}

	public String getFd_bank_acc_no() {
		return fd_bank_acc_no;
	}

	public void setFd_bank_acc_no(String fd_bank_acc_no) {
		this.fd_bank_acc_no = fd_bank_acc_no;
	}

	public String getFd_bank_ifsc() {
		return fd_bank_ifsc;
	}

	public void setFd_bank_ifsc(String fd_bank_ifsc) {
		this.fd_bank_ifsc = fd_bank_ifsc;
	}

	public String getFd_bank_branch() {
		return fd_bank_branch;
	}

	public void setFd_bank_branch(String fd_bank_branch) {
		this.fd_bank_branch = fd_bank_branch;
	}

	public String getFd_instagram() {
		return fd_instagram;
	}

	public void setFd_instagram(String fd_instagram) {
		this.fd_instagram = fd_instagram;
	}

	public String getFd_about() {
		return fd_about;
	}

	public void setFd_about(String fd_about) {
		this.fd_about = fd_about;
	}

	public Long getFd_whatsapp() {
		return fd_whatsapp;
	}

	public void setFd_whatsapp(Long fd_whatsapp) {
		this.fd_whatsapp = fd_whatsapp;
	}

	public String getFd_address() {
		return fd_address;
	}

	public void setFd_address(String fd_address) {
		this.fd_address = fd_address;
	}

	public String getFd_facebook() {
		return fd_facebook;
	}

	public void setFd_facebook(String fd_facebook) {
		this.fd_facebook = fd_facebook;
	}

	public String getFd_twitter() {
		return fd_twitter;
	}

	public void setFd_twitter(String fd_twitter) {
		this.fd_twitter = fd_twitter;
	}

	public String getFd_gplus() {
		return fd_gplus;
	}

	public void setFd_gplus(String fd_gplus) {
		this.fd_gplus = fd_gplus;
	}

	public String getFd_linkedin() {
		return fd_linkedin;
	}

	public void setFd_linkedin(String fd_linkedin) {
		this.fd_linkedin = fd_linkedin;
	}

	public String getFd_map_url() {
		return fd_map_url;
	}

	public void setFd_map_url(String fd_map_url) {
		this.fd_map_url = fd_map_url;
	}

	public Long getFd_id() {
		return fd_id;
	}
	public void setFd_id(Long fd_id) {
		this.fd_id = fd_id;
	}
	public Long getFd_contact_one() {
		return fd_contact_one;
	}
	public void setFd_contact_one(Long fd_contact_one) {
		this.fd_contact_one = fd_contact_one;
	}
	public Long getFd_contact_two() {
		return fd_contact_two;
	}
	public void setFd_contact_two(Long fd_contact_two) {
		this.fd_contact_two = fd_contact_two;
	}
	public String getFd_name() {
		return fd_name;
	}
	public void setFd_name(String fd_name) {
		this.fd_name = fd_name;
	}
	public String getFd_email_one() {
		return fd_email_one;
	}
	public void setFd_email_one(String fd_email_one) {
		this.fd_email_one = fd_email_one;
	}
	public String getFd_email_two() {
		return fd_email_two;
	}
	public void setFd_email_two(String fd_email_two) {
		this.fd_email_two = fd_email_two;
	}
	public String getFd_logo() {
		return fd_logo;
	}
	public void setFd_logo(String fd_logo) {
		this.fd_logo = fd_logo;
	}
	public String getFd_banner() {
		return fd_banner;
	}
	public void setFd_banner(String fd_banner) {
		this.fd_banner = fd_banner;
	}
	public String getFd_slogan() {
		return fd_slogan;
	}
	public void setFd_slogan(String fd_slogan) {
		this.fd_slogan = fd_slogan;
	}
}
