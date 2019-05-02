package model;

public class Uc_taluka {
	private Long t_id, t_district_id;
	private String t_name;
	
	public Uc_taluka() {
		t_id = null; t_district_id = null;
		t_name = null;
	}

	public Long getT_id() {
		return t_id;
	}

	public void setT_id(Long t_id) {
		this.t_id = t_id;
	}

	public Long getT_district_id() {
		return t_district_id;
	}

	public void setT_district_id(Long t_district_id) {
		this.t_district_id = t_district_id;
	}

	public String getT_name() {
		return t_name;
	}

	public void setT_name(String t_name) {
		this.t_name = t_name;
	}
}
