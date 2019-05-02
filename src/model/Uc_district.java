package model;

import java.sql.ResultSet;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import logs.LogManager;

public class Uc_district {
	
	private Long d_id, d_s_id;
	private String d_name;
	
	public Uc_district() {
		/*d_id = -1; d_s_id = -1;
		d_name = "-1";*/
		d_id = null; d_s_id = null;
		d_name = null;
	}
	
	public Long getD_id() {
		return d_id;
	}
	public void setD_id(Long d_id) {
		this.d_id = d_id;
	}
	public Long getD_s_id() {
		return d_s_id;
	}
	public void setD_s_id(Long d_s_id) {
		this.d_s_id = d_s_id;
	}
	public String getD_name() {
		return d_name;
	}
	public void setD_name(String d_name) {
		this.d_name = d_name;
	}
}
