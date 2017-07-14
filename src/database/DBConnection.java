package database;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

import core.Gallery;
import core.StatusEvent;


public class DBConnection {
	
	public static final String DB_FILE = "sqlitedb.db3";
	private Connection conn;
	private Statement stm;
	
	public DBConnection() throws ClassNotFoundException, SQLException {
		Class.forName("org.sqlite.JDBC");
		this.conn = DriverManager.getConnection("jdbc:sqlite:" + DB_FILE);
		this.stm = conn.createStatement();
		if(stm == null)
			Gallery.getInstance().notifyStatus(this, StatusEvent.ERROR_MSG, "Connection to DB not established");
	}
	
	// TODO
	void initDB() {
	}
	
	public void update(String sql) throws SQLException {
		stm.executeUpdate(sql);
	}
	
	public ResultSet query(String sql) throws SQLException {
		System.out.println(sql);
		return stm.executeQuery(sql);
	}
}
