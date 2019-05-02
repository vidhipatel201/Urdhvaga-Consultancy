package model;

public class Uc_cj_quantity {
	private Long cjq_id, cjq_job_id;
	private Integer cjq_male, cjq_female;
	
	public Uc_cj_quantity() {
		//cjq_id = -1; cjq_job_id = -1; cjq_male = -1; cjq_female = -1;
		cjq_id = null; cjq_job_id = null; cjq_male = null; cjq_female = null;
	}

	public Long getCjq_id() {
		return cjq_id;
	}

	public void setCjq_id(Long cjq_id) {
		this.cjq_id = cjq_id;
	}

	public Long getCjq_job_id() {
		return cjq_job_id;
	}

	public void setCjq_job_id(Long cjq_job_id) {
		this.cjq_job_id = cjq_job_id;
	}

	public Integer getCjq_male() {
		return cjq_male;
	}

	public void setCjq_male(Integer cjq_male) {
		this.cjq_male = cjq_male;
	}

	public Integer getCjq_female() {
		return cjq_female;
	}

	public void setCjq_female(Integer cjq_female) {
		this.cjq_female = cjq_female;
	}

	
}
