package model;

import java.sql.Date;

public class Uc_employer_details {
	private Integer ed_approved, ed_active;

	private String ed_firm_name, ed_contact_person, ed_email, ed_address;
	private Long ed_contact_no, ed_id, ed_city_id, ed_industry_sector_id, ed_type_id, ed_added_by;
	private Date ed_date_of_joining;
	
	public Uc_employer_details() {
		/*ed_id = -1; ed_city_id = -1; ed_industry_sector_id = -1; ed_type_id = -1;
		ed_firm_name = "-1"; ed_contact_person = "-1"; ed_email = "-1"; ed_address = "-1"; ed_date_of_joining = "-1";
		ed_contact_no = -1;*/
		ed_id = null; ed_city_id = null; ed_industry_sector_id = null; ed_type_id = null; ed_approved = null; ed_added_by = null; ed_active = null;
		ed_firm_name = null; ed_contact_person = null; ed_email =  null; ed_address = null; ed_date_of_joining = null;
		ed_contact_no = null;
	}

	public Integer getEd_approved() {
		return ed_approved;
	}

	public void setEd_approved(Integer ed_approved) {
		this.ed_approved = ed_approved;
	}

	public Long getEd_added_by() {
		return ed_added_by;
	}

	public void setEd_added_by(Long ed_added_by) {
		this.ed_added_by = ed_added_by;
	}

	public Long getEd_contact_no() {
		return ed_contact_no;
	}

	public void setEd_contact_no(Long ed_contact_no) {
		this.ed_contact_no = ed_contact_no;
	}

	public Long getEd_type_id() {
		return ed_type_id;
	}

	public void setEd_type_id(Long ed_type_id) {
		this.ed_type_id = ed_type_id;
	}

	public String getEd_contact_person() {
		return ed_contact_person;
	}

	public void setEd_contact_person(String ed_contact_person) {
		this.ed_contact_person = ed_contact_person;
	}

	public String getEd_email() {
		return ed_email;
	}

	public void setEd_email(String ed_email) {
		this.ed_email = ed_email;
	}

	public String getEd_address() {
		return ed_address;
	}

	public void setEd_address(String ed_address) {
		this.ed_address = ed_address;
	}

	public Date getEd_date_of_joining() {
		return ed_date_of_joining;
	}

	public void setEd_date_of_joining(Date ed_date_of_joining) {
		this.ed_date_of_joining = ed_date_of_joining;
	}

	public Long getEd_id() {
		return ed_id;
	}

	public void setEd_id(Long ed_id) {
		this.ed_id = ed_id;
	}

	public Long getEd_city_id() {
		return ed_city_id;
	}

	public void setEd_city_id(Long ed_city_id) {
		this.ed_city_id = ed_city_id;
	}

	public Long getEd_industry_sector_id() {
		return ed_industry_sector_id;
	}

	public void setEd_industry_sector_id(Long ed_industry_sector_id) {
		this.ed_industry_sector_id = ed_industry_sector_id;
	}

	public String getEd_firm_name() {
		return ed_firm_name;
	}

	public void setEd_firm_name(String ed_firm_name) {
		this.ed_firm_name = ed_firm_name;
	}
	
	public Integer getEd_active() {
		return ed_active;
	}

	public void setEd_active(Integer ed_active) {
		this.ed_active = ed_active;
	}
	
}
