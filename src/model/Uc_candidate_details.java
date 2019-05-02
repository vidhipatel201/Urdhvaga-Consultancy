package model;

import java.sql.ResultSet;
import java.sql.Statement;
import java.util.ArrayList;
import java.sql.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import logs.LogManager;

public class Uc_candidate_details {
	
	private Integer cd_gender, cd_birthyear, cd_experience, cd_is_placed, cd_is_active, cd_payment;
	private Long cd_id, cd_city_id, cd_contact_num, cd_contact_num_secondary;
	private String cd_name, cd_email, cd_password, cd_resume, cd_expected_salary, payment_date;
	private Date cd_joining_date;
	
	public Uc_candidate_details() {
		/*cd_id = -1; cd_contact_num = -1; cd_gender = -1; cd_birthyear = -1; cd_experience = -1; cd_is_placed = -1; cd_city_id = -1; cd_is_active = -1;
		cd_name = "-1"; cd_email = "-1"; cd_password = "-1"; cd_resume = "-1";*/
		cd_id = null; cd_contact_num = null; cd_gender = null; cd_birthyear = null; cd_experience = null; cd_is_placed = null; cd_city_id = null; cd_is_active = null; cd_contact_num_secondary = null; cd_payment = null;
		cd_name = null; cd_email = null; cd_password = null; cd_resume = null; cd_joining_date = null; cd_expected_salary = null;
		payment_date = null;
	}
	
	public Integer getCd_payment() {
		return cd_payment;
	}

	public void setCd_payment(Integer cd_payment) {
		this.cd_payment = cd_payment;
	}

	public Long getCd_contact_num_secondary() {
		return cd_contact_num_secondary;
	}

	public void setCd_contact_num_secondary(Long cd_contact_num_secondary) {
		this.cd_contact_num_secondary = cd_contact_num_secondary;
	}

	public String getCd_expected_salary() {
		return cd_expected_salary;
	}

	public void setCd_expected_salary(String cd_expected_salary) {
		this.cd_expected_salary = cd_expected_salary;
	}

	public Date getCd_joining_date() {
		return cd_joining_date;
	}

	public void setCd_joining_date(Date cd_joining_date) {
		this.cd_joining_date = cd_joining_date;
	}

	public Integer getCd_is_active() {
		return cd_is_active;
	}

	public void setCd_is_active(Integer cd_is_active) {
		this.cd_is_active = cd_is_active;
	}

	public Long getCd_id() {
		return cd_id;
	}

	public void setCd_id(Long cd_id) {
		this.cd_id = cd_id;
	}

	public Long getCd_contact_num() {
		return cd_contact_num;
	}

	public void setCd_contact_num(Long cd_contact_num) {
		this.cd_contact_num = cd_contact_num;
	}

	public Integer getCd_gender() {
		return cd_gender;
	}

	public void setCd_gender(Integer cd_gender) {
		this.cd_gender = cd_gender;
	}

	public Integer getCd_birthyear() {
		return cd_birthyear;
	}

	public void setCd_birthyear(Integer cd_birthyear) {
		this.cd_birthyear = cd_birthyear;
	}

	public Integer getCd_experience() {
		return cd_experience;
	}

	public void setCd_experience(Integer cd_experience) {
		this.cd_experience = cd_experience;
	}

	public Integer getCd_is_placed() {
		return cd_is_placed;
	}

	public void setCd_is_placed(Integer cd_is_placed) {
		this.cd_is_placed = cd_is_placed;
	}

	public String getCd_name() {
		return cd_name;
	}

	public void setCd_name(String cd_name) {
		this.cd_name = cd_name;
	}

	public String getCd_email() {
		return cd_email;
	}

	public void setCd_email(String cd_email) {
		this.cd_email = cd_email;
	}

	public String getCd_password() {
		return cd_password;
	}

	public void setCd_password(String cd_password) {
		this.cd_password = cd_password;
	}

	public String getCd_resume() {
		return cd_resume;
	}

	public void setCd_resume(String cd_resume) {
		this.cd_resume = cd_resume;
	}

	public Long getCd_city_id() {
		return cd_city_id;
	}

	public void setCd_city_id(Long cd_city_id) {
		this.cd_city_id = cd_city_id;
	}

	public String getPayment_date() {
		return payment_date;
	}

	public void setPayment_date(String payment_date) {
		this.payment_date = payment_date;
	}
	
	
}
