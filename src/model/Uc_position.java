package model;

import java.sql.ResultSet;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import logs.LogManager;

public class Uc_position {
	
	private Long p_id;
	private String p_name;
	
	public Uc_position() {
		/*p_id = -1;
		p_name = "-1";*/
		p_id = null;
		p_name = null;
	}

	public Long getP_id() {
		return p_id;
	}

	public void setP_id(Long p_id) {
		this.p_id = p_id;
	}

	public String getP_name() {
		return p_name;
	}

	public void setP_name(String p_name) {
		this.p_name = p_name;
	}
}
