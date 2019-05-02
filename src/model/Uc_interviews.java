package model;

import java.sql.Date;

public class Uc_interviews {
	private Long i_id, i_job_id;
	private Date i_date;
	private String i_time, i_location, i_contact_person;
	
	public Uc_interviews() {
		i_id = null; i_job_id = null;
		i_date = null;
		i_time = null;
		i_location = null;
	}

	public String getI_contact_person() {
		return i_contact_person;
	}

	public void setI_contact_person(String i_contact_person) {
		this.i_contact_person = i_contact_person;
	}

	public String getI_location() {
		return i_location;
	}

	public void setI_location(String i_location) {
		this.i_location = i_location;
	}

	public Long getI_id() {
		return i_id;
	}

	public void setI_id(Long i_id) {
		this.i_id = i_id;
	}

	public Long getI_job_id() {
		return i_job_id;
	}

	public void setI_job_id(Long i_job_id) {
		this.i_job_id = i_job_id;
	}

	public Date getI_date() {
		return i_date;
	}

	public void setI_date(Date i_date) {
		this.i_date = i_date;
	}

	public String getI_time() {
		return i_time;
	}

	public void setI_time(String i_time) {
		this.i_time = i_time;
	}
}
