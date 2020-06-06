/**
 * 
 */
package org.xpande.retail.ws.model;

import java.io.Serializable;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;

import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

import org.xpande.retail.ws.util.DBUtils;

/**
 * @author gabriel
 *
 */
@XmlRootElement(name = "bpartners")
public class BPartners implements Serializable {

	private List<Partner> socios = new ArrayList<Partner>();
	private int cantidad = 0;

	private static final long serialVersionUID = -1876696243602760021L;

	/**
	 * 
	 */
	public BPartners() {
	}

	public List<Partner> getSocios() {
		return socios;
	}

	@XmlElement
	public void setSocios(List<Partner> socios) {
		this.socios = socios;
	}

	public int getCantidad() {
		return cantidad;
	}

	@XmlElement
	public void setCantidad(int cantidad) {
		this.cantidad = cantidad;
	}

	
	public static BPartners getPartnersByProd(Integer mProductID) throws Exception {
		
		BPartners bps = new BPartners();

		Connection conn = null;
		String sql = "";
		ResultSet rs = null;
		PreparedStatement pstmt = null;

		try{
			conn = DBUtils.getConnection();

			sql = " select distinct bpp.c_bpartner_id, bp.taxid, bp.name, bpp.dateinvoiced " + 
					" from z_productosocio bpp " + 
					" inner join c_bpartner bp on bpp.c_bpartner_id = bp.c_bpartner_id " +	
					" where bpp.m_product_id =" + mProductID.intValue(); 

			pstmt = conn.prepareStatement(sql);
			rs = pstmt.executeQuery ();

			int cont = 0;
			List<Partner> bpList = new ArrayList<Partner>();
			
			while (rs.next()){
				
				cont++;
				
				Partner bp = new Partner();
				bp.setID(rs.getInt("c_bpartner_id"));
				bp.setRUT(rs.getString("taxid"));
				bp.setRazonSocial(rs.getString("name"));
				
				Timestamp dateInvoiced = rs.getTimestamp("dateinvoiced");
				if (dateInvoiced != null){
					SimpleDateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy");
					String fecCompra  = dateFormat.format(dateInvoiced);
					bp.setFechaUltCompra(fecCompra);
				}
				
				bpList.add(bp);
			}
			
			bps.setCantidad(cont);
			
			if (bpList.size() > 0) {
				bps.setSocios(bpList);	
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

		
		return bps;

	}

	
	public static BPartners getVendorPartners() throws Exception {
		
		BPartners bps = new BPartners();

		Connection conn = null;
		String sql = "";
		ResultSet rs = null;
		PreparedStatement pstmt = null;

		try{
			conn = DBUtils.getConnection();

			sql = " select c_bpartner_id, taxid, name, name2, po_paymentterm_id " + 
					" from c_bpartner " +
					" where isactive='Y' and isvendor ='Y' "; 

			pstmt = conn.prepareStatement(sql);
			rs = pstmt.executeQuery ();

			int cont = 0;
			List<Partner> bpList = new ArrayList<Partner>();
			
			while (rs.next()){
				
				cont++;
				
				Partner bp = new Partner();
				bp.setID(rs.getInt("c_bpartner_id"));
				bp.setRUT(rs.getString("taxid"));
				bp.setRazonSocial(rs.getString("name"));
				
				bp.setNomFantasia(rs.getString("name2"));
				if (rs.getInt("po_paymentterm_id") > 0) {
					bp.setPaymentTermID(rs.getInt("po_paymentterm_id"));	
				}
				bpList.add(bp);
			}
			
			bps.setCantidad(cont);
			
			if (bpList.size() > 0) {
				bps.setSocios(bpList);	
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

		
		return bps;

	}

	
}
