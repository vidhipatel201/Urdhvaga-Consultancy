package model;

import java.sql.ResultSet;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import logs.LogManager;

public class Uc_interest_areas {
	
	private Long ia_id, ia_industry, ia_position, ia_candidate;

	public Uc_interest_areas() {
		//ia_id = -1; ia_industry = -1; ia_position = -1; ia_candidate = -1;
		ia_id = null; ia_industry = null; ia_position = null; ia_candidate = null;
	}
	
	public Long getIa_id() {
		return ia_id;
	}

	public void setIa_id(Long ia_id) {
		this.ia_id = ia_id;
	}

	public Long getIa_industry() {
		return ia_industry;
	}

	public void setIa_industry(Long ia_industry) {
		this.ia_industry = ia_industry;
	}

	public Long getIa_position() {
		return ia_position;
	}

	public void setIa_position(Long ia_position) {
		this.ia_position = ia_position;
	}

	public Long getIa_candidate() {
		return ia_candidate;
	}

	public void setIa_candidate(Long ia_candidate) {
		this.ia_candidate = ia_candidate;
	}
}
