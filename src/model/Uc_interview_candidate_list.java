package model;

public class Uc_interview_candidate_list {
	private Long icl_id, icl_candidate_id, icl_interview_id;
	
	public Uc_interview_candidate_list() {
		icl_id = null; icl_candidate_id = null; icl_interview_id = null;
	}

	public Long getIcl_id() {
		return icl_id;
	}

	public void setIcl_id(Long icl_id) {
		this.icl_id = icl_id;
	}

	public Long getIcl_candidate_id() {
		return icl_candidate_id;
	}

	public void setIcl_candidate_id(Long icl_candidate_id) {
		this.icl_candidate_id = icl_candidate_id;
	}

	public Long getIcl_interview_id() {
		return icl_interview_id;
	}

	public void setIcl_interview_id(Long icl_interview_id) {
		this.icl_interview_id = icl_interview_id;
	}
}
