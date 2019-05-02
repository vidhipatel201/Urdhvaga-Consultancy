package model;

public class Uc_candidate_qualification {
	private Long cq_id, cq_quali_id, cq_candidate_id;

	public Uc_candidate_qualification() {
		//cq_id = -1; cq_quali_id = -1; cq_candidate_id = -1;
		cq_id = null; cq_quali_id = null; cq_candidate_id = null;
	}
	public Long getCq_id() {
		return cq_id;
	}

	public void setCq_id(Long cq_id) {
		this.cq_id = cq_id;
	}

	public Long getCq_quali_id() {
		return cq_quali_id;
	}

	public void setCq_quali_id(Long cq_quali_id) {
		this.cq_quali_id = cq_quali_id;
	}

	public Long getCq_candidate_id() {
		return cq_candidate_id;
	}

	public void setCq_candidate_id(Long cq_candidate_id) {
		this.cq_candidate_id = cq_candidate_id;
	}
	
}
