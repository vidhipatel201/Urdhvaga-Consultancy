package model;

public class Uc_qualifications {
	private Long q_id;
	private String q_name;
	public Uc_qualifications() {
		/*q_id = -1;
		q_name = "-1";*/
		q_id = null;
		q_name = null;
	}
	public Long getQ_id() {
		return q_id;
	}
	public void setQ_id(Long q_id) {
		this.q_id = q_id;
	}
	public String getQ_name() {
		return q_name;
	}
	public void setQ_name(String q_name) {
		this.q_name = q_name;
	}
	
}
