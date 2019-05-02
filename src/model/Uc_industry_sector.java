package model;

import java.sql.ResultSet;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import logs.LogManager;

public class Uc_industry_sector {
	
	private Long is_id;
	private String is_name;
	
	public Uc_industry_sector() {
		/*is_id = -1;
		is_name = "-1";*/
		is_id = null;
		is_name = null;
	}
	
	public Long getIs_id() {
		return is_id;
	}
	public void setIs_id(Long is_id) {
		this.is_id = is_id;
	}
	public String getIs_name() {
		return is_name;
	}
	public void setIs_name(String is_name) {
		this.is_name = is_name;
	}
}
