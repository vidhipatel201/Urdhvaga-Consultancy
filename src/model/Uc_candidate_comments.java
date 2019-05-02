package model;

import java.sql.Date;

public class Uc_candidate_comments {
	private Long cc_id, cc_candidate_id, cc_added_by;
	private String cc_comment, cc_time;
	private Date cc_date;
	
	public Uc_candidate_comments() {
		cc_id = null; cc_candidate_id = null; cc_added_by = null;
		cc_comment = null; cc_time = null;
		cc_date = null;
	}

	public String getCc_time() {
		return cc_time;
	}

	public void setCc_time(String cc_time) {
		this.cc_time = cc_time;
	}

	public Date getCc_date() {
		return cc_date;
	}

	public void setCc_date(Date cc_date) {
		this.cc_date = cc_date;
	}

	public Long getCc_added_by() {
		return cc_added_by;
	}

	public void setCc_added_by(Long cc_added_by) {
		this.cc_added_by = cc_added_by;
	}

	public Long getCc_id() {
		return cc_id;
	}

	public void setCc_id(Long cc_id) {
		this.cc_id = cc_id;
	}

	public Long getCc_candidate_id() {
		return cc_candidate_id;
	}

	public void setCc_candidate_id(Long cc_candidate_id) {
		this.cc_candidate_id = cc_candidate_id;
	}

	public String getCc_comment() {
		return cc_comment;
	}

	public void setCc_comment(String cc_comment) {
		this.cc_comment = cc_comment;
	}
	
	
}
