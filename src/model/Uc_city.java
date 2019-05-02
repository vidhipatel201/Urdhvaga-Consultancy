package model;

import java.sql.ResultSet;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import logs.LogManager;

public class Uc_city {
	
	private Long c_id, c_taluka_id;
	private String c_name;
	
	public Uc_city() {
		/*c_id = -1; c_district = -1;
		c_name = "-1";*/
		c_id = null; c_taluka_id = null;
		c_name = null;
	}
	
	public Long getC_id() {
		return c_id;
	}

	public void setC_id(Long c_id) {
		this.c_id = c_id;
	}

	
	public Long getC_taluka_id() {
		return c_taluka_id;
	}

	public void setC_taluka_id(Long c_taluka_id) {
		this.c_taluka_id = c_taluka_id;
	}

	public String getC_name() {
		return c_name;
	}

	public void setC_name(String c_name) {
		this.c_name = c_name;
	}
}
