package model;

public class Uc_admin_employer_permissions {
	private Long aep_id, aep_admin_id, aep_employer_id;
	
	public Uc_admin_employer_permissions() {
		aep_id = null; aep_admin_id = null; aep_employer_id = null;
	}

	public Long getAep_id() {
		return aep_id;
	}

	public void setAep_id(Long aep_id) {
		this.aep_id = aep_id;
	}

	public Long getAep_admin_id() {
		return aep_admin_id;
	}

	public void setAep_admin_id(Long aep_admin_id) {
		this.aep_admin_id = aep_admin_id;
	}

	public Long getAep_employer_id() {
		return aep_employer_id;
	}

	public void setAep_employer_id(Long aep_employer_id) {
		this.aep_employer_id = aep_employer_id;
	}
}
