package model;

public class Uc_terms_conditions {
	private Long tc_id;
	private String tc_term;
	
	public Uc_terms_conditions() {
		tc_id = null;
		tc_term = null;
	}

	public Long getTc_id() {
		return tc_id;
	}

	public void setTc_id(Long tc_id) {
		this.tc_id = tc_id;
	}

	public String getTc_term() {
		return tc_term;
	}

	public void setTc_term(String tc_term) {
		this.tc_term = tc_term;
	}
}
