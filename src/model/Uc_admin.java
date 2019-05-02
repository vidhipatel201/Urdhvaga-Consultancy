package model;

public class Uc_admin {
	/*private DatabaseManager dbManager;
	private static String tableName = "uc_admin";
	private Statement statement;*/
	
	private Long a_id, a_type;
	private Integer a_active;
	private String a_username, a_password;
	
	public Uc_admin() {
		/*a_id = -1; a_active = -1;
		a_username = "-1"; a_password = "-1";*/
		a_id = null; a_active = null; a_type = null;
		a_username = null; a_password = null;
	}
	
	public Long getA_type() {
		return a_type;
	}

	public void setA_type(Long a_type) {
		this.a_type = a_type;
	}

	public Integer getA_active() {
		return a_active;
	}
	public void setA_active(Integer a_active) {
		this.a_active = a_active;
	}
	public String getA_username() {
		return a_username;
	}
	public void setA_username(String a_username) {
		this.a_username = a_username;
	}
	public String getA_password() {
		return a_password;
	}
	public void setA_password(String a_password) {
		this.a_password = a_password;
	}
	public Long getA_id() {
		return a_id;
	}
	public void setA_id(Long a_id) {
		this.a_id = a_id;
	}
	
	/*public Integer getColumnCount() {
		return getClass().getDeclaredFields().length;
	}
	
	public boolean insertData() {
		DatabaseManager dbManager = null;
		try {
			String tableName = "uc_admin";
			Statement statement;
			dbManager = new DatabaseManager();
			String sql = "insert Integero " + tableName + " (a_username, a_password, a_active) values('" + a_username + "', '" + a_password + "', " + a_active + ")";
			statement = dbManager.conn.createStatement();
			statement.executeUpdate(sql);
			return true;
		} catch(Exception ex) {
			LogManager.appendToExceptionLogs(ex);
			return false;
		} finally {
			dbManager.closeConnection();
		}
	}
	
	public boolean updateData(String entries) {
		DatabaseManager dbManager = null;
		try {
			
			String tableName = "uc_admin";
			Statement statement;
			dbManager = new DatabaseManager();
			/*Iterator it = values.entrySet().iterator();
			String entries = "";
			while (it.hasNext()) {
				 Map.Entry pair = (Map.Entry)it.next();
				 entries += pair.getKey().toString() + " = " + pair.getValue();
			}*/
			/*String sql = "update " + tableName + " set " + entries + " where a_id=" + a_id;
			statement = dbManager.conn.createStatement();
			statement.executeUpdate(sql);
			return true;
		} catch(Exception ex) {
			LogManager.appendToExceptionLogs(ex);
			return false;
		} finally {
			dbManager.closeConnection();
		}
	}
	
	public boolean deleteData() {
		DatabaseManager dbManager = null;
		try {
			
			String tableName = "uc_admin";
			Statement statement;
			dbManager = new DatabaseManager();
			String sql = "Delete from " + tableName + " where a_id=" + a_id;
			statement = dbManager.conn.createStatement();
			statement.executeUpdate(sql);
			return true;
		} catch(Exception ex) {
			LogManager.appendToExceptionLogs(ex);
			return false;
		} finally {
			dbManager.closeConnection();
		}
	}
	
	public List<Uc_admin> selectData(String whereClause) {
		DatabaseManager dbManager = null;
		try {
			String tableName = "uc_admin";
			Statement statement;
			dbManager = new DatabaseManager();
			/* Iterator it = values.entrySet().iterator();
			 String entries = "";
			 while (it.hasNext()) {
				 Map.Entry pair = (Map.Entry)it.next();
				 entries += pair.getKey().toString() + " = " + pair.getValue();
			}*/
			/*String sql = "select * from " + tableName + " where " + whereClause;
			System.out.prIntegerln("Query: " + sql);
			statement = dbManager.conn.createStatement();
			ResultSet rs = statement.executeQuery(sql);
			List<Uc_admin> list = new ArrayList<Uc_admin>();
			while(rs.next()) {
				Uc_admin ucAdmin = new Uc_admin();
				ucAdmin.setA_id(rs.getInteger("a_id"));
				ucAdmin.setA_password(rs.getString("a_password"));
				ucAdmin.setA_username(rs.getString("a_username"));
				ucAdmin.setA_active(rs.getInteger("a_active"));
				list.add(ucAdmin);
			}
			return list;
		} catch(Exception ex) {
			LogManager.appendToExceptionLogs(ex);
			return null;
		} finally {
			dbManager.closeConnection();
		}
	}
	
	public List<Uc_admin> selectAllData() {
		DatabaseManager dbManager = null;
		try {
			String tableName = "uc_admin";
			Statement statement;
			dbManager = new DatabaseManager();
			String sql = "select * from " + tableName;
			statement = dbManager.conn.createStatement();
			ResultSet rs = statement.executeQuery(sql);
			List<Uc_admin> list = new ArrayList<Uc_admin>();
			while(rs.next()) {
				Uc_admin ucAdmin = new Uc_admin();
				ucAdmin.setA_id(rs.getInteger("a_id"));
				ucAdmin.setA_password(rs.getString("a_password"));
				ucAdmin.setA_username(rs.getString("a_username"));
				ucAdmin.setA_active(rs.getInteger("a_active"));
				list.add(ucAdmin);
			}
			return list;
		} catch(Exception ex) {
			LogManager.appendToExceptionLogs(ex);
			return null;
		} finally {
			dbManager.closeConnection();
		}
	}*/
}
