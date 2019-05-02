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

public class Uc_current_jobs {
	
	private Long cj_id, cj_position, cj_city, cj_employer_id,cj_added_by;
	private Integer cj_is_featured, cj_is_active, cj_quantity, cj_experience_start, cj_experience_end, cj_approved, cj_has_applicant, cj_approved_by_telecaller, cj_approved_by_sales, cj_approved_by_telecaller_final, cj_number_of_new_applicant;
	private String cj_experience, cj_duty_hours, cj_work_profile, cj_salary;
	private Date cj_posted_at;
	
	public Uc_current_jobs() {
		/*cj_id = -1; cj_position = -1; cj_city = -1; cj_is_featured = -1; cj_is_active = -1; cj_quantity = -1; cj_employer_id = -1;
		cj_experience = "-1"; cj_duty_hours = "-1"; cj_work_profile = "-1"; cj_salary = "-1"; cj_posted_at = "-1";*/
		cj_id = null; cj_position = null; cj_city = null; cj_is_featured = null; cj_is_active = null; cj_quantity = null; cj_employer_id = null; cj_experience_start = null; cj_experience_end = null; cj_approved = null; cj_added_by = null; cj_has_applicant = null; cj_approved_by_telecaller = null; cj_approved_by_sales = null; cj_approved_by_telecaller_final = null; cj_number_of_new_applicant = null;
		cj_experience = null; cj_duty_hours = null; cj_work_profile = null; cj_salary = null; cj_posted_at = null;
	}
	
	public Integer getCj_number_of_new_applicant() {
		return cj_number_of_new_applicant;
	}

	public void setCj_number_of_new_applicant(Integer cj_number_of_new_applicant) {
		this.cj_number_of_new_applicant = cj_number_of_new_applicant;
	}

	public Integer getCj_approved_by_telecaller_final() {
		return cj_approved_by_telecaller_final;
	}

	public void setCj_approved_by_telecaller_final(Integer cj_approved_by_telecaller_final) {
		this.cj_approved_by_telecaller_final = cj_approved_by_telecaller_final;
	}

	public Integer getCj_approved_by_sales() {
		return cj_approved_by_sales;
	}

	public void setCj_approved_by_sales(Integer cj_approved_by_sales) {
		this.cj_approved_by_sales = cj_approved_by_sales;
	}

	public Integer getCj_approved_by_telecaller() {
		return cj_approved_by_telecaller;
	}

	public void setCj_approved_by_telecaller(Integer cj_approved_by_telecaller) {
		this.cj_approved_by_telecaller = cj_approved_by_telecaller;
	}

	public Integer getCj_has_applicant() {
		return cj_has_applicant;
	}

	public void setCj_has_applicant(Integer cj_has_applicant) {
		this.cj_has_applicant = cj_has_applicant;
	}

	public Integer getCj_approved() {
		return cj_approved;
	}

	public void setCj_approved(Integer cj_approved) {
		this.cj_approved = cj_approved;
	}

	public Long getCj_added_by() {
		return cj_added_by;
	}

	public void setCj_added_by(Long cj_added_by) {
		this.cj_added_by = cj_added_by;
	}

	public Integer getCj_experience_start() {
		return cj_experience_start;
	}

	public void setCj_experience_start(Integer cj_experience_start) {
		this.cj_experience_start = cj_experience_start;
	}

	public Integer getCj_experience_end() {
		return cj_experience_end;
	}

	public void setCj_experience_end(Integer cj_experience_end) {
		this.cj_experience_end = cj_experience_end;
	}

	public Long getCj_employer_id() {
		return cj_employer_id;
	}

	public void setCj_employer_id(Long cj_employer_id) {
		this.cj_employer_id = cj_employer_id;
	}

	public Integer getCj_quantity() {
		return cj_quantity;
	}

	public void setCj_quantity(Integer cj_quantity) {
		this.cj_quantity = cj_quantity;
	}

	public Long getCj_id() {
		return cj_id;
	}
	public void setCj_id(Long cj_id) {
		this.cj_id = cj_id;
	}
	public Long getCj_position() {
		return cj_position;
	}
	public void setCj_position(Long cj_position) {
		this.cj_position = cj_position;
	}
	public Long getCj_city() {
		return cj_city;
	}
	public void setCj_city(Long cj_city) {
		this.cj_city = cj_city;
	}
	public Integer getCj_is_featured() {
		return cj_is_featured;
	}
	public void setCj_is_featured(Integer cj_is_featured) {
		this.cj_is_featured = cj_is_featured;
	}
	public Integer getCj_is_active() {
		return cj_is_active;
	}
	public void setCj_is_active(Integer cj_is_active) {
		this.cj_is_active = cj_is_active;
	}
	public String getCj_experience() {
		return cj_experience;
	}
	public void setCj_experience(String cj_experience) {
		this.cj_experience = cj_experience;
	}
	public String getCj_duty_hours() {
		return cj_duty_hours;
	}
	public void setCj_duty_hours(String cj_duty_hours) {
		this.cj_duty_hours = cj_duty_hours;
	}
	public String getCj_work_profile() {
		return cj_work_profile;
	}
	public void setCj_work_profile(String cj_work_profile) {
		this.cj_work_profile = cj_work_profile;
	}
	public String getCj_salary() {
		return cj_salary;
	}
	public void setCj_salary(String cj_salary) {
		this.cj_salary = cj_salary;
	}
	public Date getCj_posted_at() {
		return cj_posted_at;
	}
	public void setCj_posted_at(Date cj_posted_at) {
		this.cj_posted_at = cj_posted_at;
	}
}
