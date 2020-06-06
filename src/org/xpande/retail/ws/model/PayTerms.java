package org.xpande.retail.ws.model;

import java.io.Serializable;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;

import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

import org.xpande.retail.ws.util.DBUtils;

@XmlRootElement(name = "payterms")
public class PayTerms implements Serializable {

	
	private List<PayTerm> terminospago = new ArrayList<PayTerm>();
	private int cantidad = 0;
	

	private static final long serialVersionUID = 4415108410588858145L;

	public PayTerms() {
	}

	
	public List<PayTerm> getTerminospago() {
		return terminospago;
	}

	@XmlElement
	public void setTerminospago(List<PayTerm> terminospago) {
		this.terminospago = terminospago;
	}

	public int getCantidad() {
		return cantidad;
	}

	@XmlElement
	public void setCantidad(int cantidad) {
		this.cantidad = cantidad;
	}

	
	public static PayTerms getPayTerms() throws Exception {
		
		PayTerms terms = new PayTerms();

		Connection conn = null;
		String sql = "";
		ResultSet rs = null;
		PreparedStatement pstmt = null;

		try{
			conn = DBUtils.getConnection();

			sql = " select c_paymentterm_id, name " + 
					" from c_paymentterm " +
					" where isactive='Y' and c_paymentterm_id >= 1000000 "; 

			pstmt = conn.prepareStatement(sql);
			rs = pstmt.executeQuery ();

			int cont = 0;
			List<PayTerm> termlist = new ArrayList<PayTerm>();
			
			while (rs.next()){
				
				cont++;
				
				PayTerm t = new PayTerm();
				t.setID(rs.getInt("c_paymentterm_id"));
				t.setNomTermino(rs.getString("name"));

				termlist.add(t);
			}
			
			terms.setCantidad(cont);
			
			if (termlist.size() > 0) {
				terms.setTerminospago(termlist);	
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
		
		return terms;

	}

	
}
