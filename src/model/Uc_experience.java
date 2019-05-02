package model;

import java.sql.ResultSet;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import logs.LogManager;

public class Uc_experience {
	private Long e_id, e_industry_id, e_position_id, e_candidate_id;
	private Integer e_years, e_is_current_job, e_salary_per_month;
	
	public Uc_experience() {
		//e_id = -1; e_industry_id = -1; e_position_id = -1; e_candidate_id = -1; e_is_current_job = -1;
		e_id = null; e_industry_id = null; e_position_id = null; e_candidate_id = null; e_is_current_job = null; e_salary_per_month = null;
	}
	
	public Integer getE_salary_per_month() {
		return e_salary_per_month;
	}

	public void setE_salary_per_month(Integer e_salary_per_month) {
		this.e_salary_per_month = e_salary_per_month;
	}

	public Long getE_id() {
		return e_id;
	}

	public void setE_id(Long e_id) {
		this.e_id = e_id;
	}

	public Long getE_industry_id() {
		return e_industry_id;
	}

	public void setE_industry_id(Long e_industry_id) {
		this.e_industry_id = e_industry_id;
	}

	public Long getE_position_id() {
		return e_position_id;
	}

	public void setE_position_id(Long e_position_id) {
		this.e_position_id = e_position_id;
	}

	public Long getE_candidate_id() {
		return e_candidate_id;
	}

	public void setE_candidate_id(Long e_candidate_id) {
		this.e_candidate_id = e_candidate_id;
	}

	public Integer getE_years() {
		return e_years;
	}

	public void setE_years(Integer e_years) {
		this.e_years = e_years;
	}

	public Integer getE_is_current_job() {
		return e_is_current_job;
	}

	public void setE_is_current_job(Integer e_is_current_job) {
		this.e_is_current_job = e_is_current_job;
	}
}
