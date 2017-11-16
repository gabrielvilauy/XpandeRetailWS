/**
 * 
 */
package org.xpande.retail.ws.service;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;

import org.xpande.retail.ws.model.BPartner;

/**
 * @author gabriel
 *
 */
public class RetailServiceImpl {

	/**
	 * 
	 */
	public RetailServiceImpl() {
	}

	public List<BPartner> getRecepcionPartners(){

		List<BPartner> lines = new ArrayList<BPartner>();
		
		BPartner model = new BPartner();
		model.setID(1000000);
		model.setRUT("1111");
		model.setRazonSocial("GABRIEL VILA");
		
		lines.add(model);
		
		
		String sql = "";
		ResultSet rs = null;
		PreparedStatement pstmt = null;

		try{
			
			sql = "";

			//pstmt = DB.prepareStatement (sql, trxName);
			
			rs = pstmt.executeQuery ();
		
			while (rs.next()){

				
			}
		}
		catch (Exception e)
		{
			
		}
		finally
		{
			//DB.close(rs, pstmt);
			rs = null; pstmt = null;
		}
		
		
		return lines;
	}

}
