package model;

import java.sql.Date;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import logs.LogManager;

public class Uc_job_applications {
	
	private Long ja_id, ja_candidate, ja_job;
	private Integer ja_source, ja_confirm_pre_final, ja_confirm_final;
	private String ja_time, ja_reject_reason;
	private Date ja_date;

	public Uc_job_applications() {
		/*ja_id = -1; ja_candidate = -1; ja_job = -1; ja_accepted = -1;
		ja_date = "-1"; ja_time = "-1";*/
		ja_id = null; ja_candidate = null; ja_job = null; ja_source = null; ja_confirm_pre_final = null; ja_confirm_final = null; ja_reject_reason = null;
		ja_date = null; ja_time = null;
	}
	
	public String getJa_reject_reason() {
		return ja_reject_reason;
	}

	public void setJa_reject_reason(String ja_reject_reason) {
		this.ja_reject_reason = ja_reject_reason;
	}

	public Integer getJa_confirm_pre_final() {
		return ja_confirm_pre_final;
	}

	public void setJa_confirm_pre_final(Integer ja_confirm_pre_final) {
		this.ja_confirm_pre_final = ja_confirm_pre_final;
	}

	public Integer getJa_confirm_final() {
		return ja_confirm_final;
	}

	public void setJa_confirm_final(Integer ja_confirm_final) {
		this.ja_confirm_final = ja_confirm_final;
	}

	public Integer getJa_source() {
		return ja_source;
	}

	public void setJa_source(Integer ja_source) {
		this.ja_source = ja_source;
	}

	public Date getJa_date() {
		return ja_date;
	}

	public void setJa_date(Date ja_date) {
		this.ja_date = ja_date;
	}

	public String getJa_time() {
		return ja_time;
	}

	public void setJa_time(String ja_time) {
		this.ja_time = ja_time;
	}

	public Long getJa_id() {
		return ja_id;
	}

	public void setJa_id(Long ja_id) {
		this.ja_id = ja_id;
	}

	public Long getJa_candidate() {
		return ja_candidate;
	}

	public void setJa_candidate(Long ja_candidate) {
		this.ja_candidate = ja_candidate;
	}

	public Long getJa_job() {
		return ja_job;
	}

	public void setJa_job(Long ja_job) {
		this.ja_job = ja_job;
	}
}
