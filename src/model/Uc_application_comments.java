package model;

import java.sql.Date;

public class Uc_application_comments {
	private Long ac_id, ac_application_id, ac_added_by;
	private String ac_comment, ac_time;
	private Date ac_date;
	
	public Uc_application_comments() {
		ac_id = null; ac_application_id = null; ac_added_by = null;
		ac_comment = null; ac_time = null;
		ac_date = null;
	}

	public String getAc_time() {
		return ac_time;
	}

	public void setAc_time(String ac_time) {
		this.ac_time = ac_time;
	}

	public Date getAc_date() {
		return ac_date;
	}

	public void setAc_date(Date ac_date) {
		this.ac_date = ac_date;
	}

	public Long getAc_added_by() {
		return ac_added_by;
	}

	public void setAc_added_by(Long ac_added_by) {
		this.ac_added_by = ac_added_by;
	}

	public Long getAc_id() {
		return ac_id;
	}

	public void setAc_id(Long ac_id) {
		this.ac_id = ac_id;
	}

	public Long getAc_application_id() {
		return ac_application_id;
	}

	public void setAc_application_id(Long ac_application_id) {
		this.ac_application_id = ac_application_id;
	}

	public String getAc_comment() {
		return ac_comment;
	}

	public void setAc_comment(String ac_comment) {
		this.ac_comment = ac_comment;
	}
	
}
