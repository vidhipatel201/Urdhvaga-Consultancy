package model;

public class Uc_employer_type {
	private Long et_id;
	private String et_name;
	
	public Uc_employer_type() {
		/*et_id = -1;
		et_name = "-1";*/
		et_id = null;
		et_name = null;
	}

	public Long getEt_id() {
		return et_id;
	}

	public void setEt_id(Long et_id) {
		this.et_id = et_id;
	}

	public String getEt_name() {
		return et_name;
	}

	public void setEt_name(String et_name) {
		this.et_name = et_name;
	}
}
