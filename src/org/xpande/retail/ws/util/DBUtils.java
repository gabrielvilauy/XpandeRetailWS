/**
 * 
 */
package org.xpande.retail.ws.util;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

/**
 * @author gabriel
 *
 */
public final class DBUtils {

	public static Connection getConnection() throws Exception {
		
		
		Connection conn = null;

		org.postgresql.Driver driver = new org.postgresql.Driver();
		DriverManager.registerDriver (driver);

		
		String dbURL = "jdbc:postgresql://10.64.0.2:5432/adempiere";
		//String dbURL = "jdbc:postgresql://10.0.0.254:5432/adempiere";
        String user = "adempiere";
        String pass = "xpande8080";
        conn = DriverManager.getConnection(dbURL, user, pass);
	
		return conn;
	}
	
	
	public static int getSequenceID(String name) throws Exception {
		
		int adSequenceID = 0;
		
		Connection conn = null;
		String sql = "";
		ResultSet rs = null;
		PreparedStatement pstmt = null;

		try{

			conn = DBUtils.getConnection();
			
			sql = " select ad_sequence_id from ad_sequence where lower(name)='" + name + "' "; 

			pstmt = conn.prepareStatement(sql);
			rs = pstmt.executeQuery ();

			if (rs.next()){
				adSequenceID = rs.getInt("ad_sequence_id");
			}
		}
		catch (Exception e)
		{
			throw e;
		}
		finally
		{
			if (rs != null) rs.close();
			if (pstmt != null) pstmt.close();
			if (conn != null) conn.close();
		}
		
		return adSequenceID;
		
	}
	
}
