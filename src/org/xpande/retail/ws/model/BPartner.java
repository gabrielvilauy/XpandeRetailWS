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
@XmlRootElement(name = "bpartner")
public class BPartner implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = -1144008327111723164L;
	
	private int ID = 0;
	private String RUT = "";
	private String RazonSocial = "";
	private String NomFantasia = "";
	private String DevolPendiente = "N";
	private Products products = null;
	private Orders orders = null;
	private LineasRecepcion lineasRecepcion = null;
	private Invoices invoices = null;	
	private String fechaUltCompra = null;
	private int paymentTermID = 0;
	
	
	/**
	 * 
	 */
	public BPartner() {
	}

	public int getID() {
		return ID;
	}

	public Products getProducts() {
		return products;
	}

	@XmlElement
	public void setProducts(Products products) {
		this.products = products;
	}
	
	public Orders getOrders() {
		return orders;
	}

	@XmlElement
	public void setOrders(Orders orders) {
		this.orders = orders;
	}

	@XmlElement
	public void setID(int iD) {
		ID = iD;
	}

	public String getRUT() {
		return RUT;
	}

	@XmlElement
	public void setRUT(String rUT) {
		RUT = rUT;
	}

	public String getRazonSocial() {
		return RazonSocial;
	}


	@XmlElement
	public void setRazonSocial(String razonSocial) {
		RazonSocial = razonSocial;
	}

	public String getDevolPendiente() {
		return DevolPendiente;
	}

	@XmlElement
	public void setDevolPendiente(String devolPendiente) {
		DevolPendiente = devolPendiente;
	}

	public LineasRecepcion getLineasRecepcion() {
		return lineasRecepcion;
	}

	@XmlElement
	public void setLineasRecepcion(LineasRecepcion lineasRecepcion) {
		this.lineasRecepcion = lineasRecepcion;
	}
	
	public Invoices getInvoices() {
		return invoices;
	}

	@XmlElement
	public void setInvoices(Invoices invoices) {
		this.invoices = invoices;
	}
	
	public String getFechaUltCompra() {
		return fechaUltCompra;
	}

	@XmlElement
	public void setFechaUltCompra(String fechaUltCompra) {
		this.fechaUltCompra = fechaUltCompra;
	}

	public static BPartner getByRut(String RUT) throws Exception {

		BPartner partner = null;

		Connection conn = null;
		String sql = "";
		ResultSet rs = null;
		PreparedStatement pstmt = null;

		try{
			conn = DBUtils.getConnection();
			
			sql = " select bp.c_bpartner_id, bp.taxid, bp.name, bp.po_paymentterm_id " + 
					" from c_bpartner bp " + 
					" where bp.taxid ='" + RUT + "' " + 
					" and bp.isactive='Y' " + 
					" and bp.isvendor='Y'"; 

			pstmt = conn.prepareStatement(sql);
			rs = pstmt.executeQuery ();

			if (rs.next()){
				partner = new BPartner();
				partner.setID(rs.getInt("c_bpartner_id"));
				partner.setRUT(RUT);
				partner.setRazonSocial(rs.getString("name"));
				
				if (rs.getInt("po_paymentterm_id") > 0) {
					partner.setPaymentTermID(rs.getInt("po_paymentterm_id"));	
				}
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

		
		return partner;
	}

	public void getPurchaseOrders() throws Exception {

		Connection conn = null;
		String sql = "";
		ResultSet rs = null;
		PreparedStatement pstmt = null;

		try{
			
			if (this.getPaymentTermID() >= 0) return;
			
			Orders orders = new Orders();
			List<Order> orderList = new ArrayList<Order>();
			
			SimpleDateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy");
			
			conn = DBUtils.getConnection();
			
			sql = " select a.c_order_id, a.documentno, b.name as comprador, a.dateordered, a.grandtotal " + 
					"from c_order a " + 
					"left outer join ad_user b on a.salesrep_id = b.ad_user_id " + 
					"where a.docstatus='CO' " + 
					"and a.created >='2017-11-20 00:00:00' " + 
					"and a.c_bpartner_id =" + this.getID(); 

			pstmt = conn.prepareStatement(sql);
			rs = pstmt.executeQuery ();

			int cont = 0;
			
			while (rs.next()){
				
				cont++;
				
				Order order = new Order();
				order.setID(rs.getInt("c_order_id"));
				order.setNumero(rs.getString("documentno"));
				order.setComprador(rs.getString("comprador"));
				order.setPartnerID(this.getID());
				order.setMonto(rs.getDouble("grandtotal"));
				
				Timestamp fechaOrdenTS = rs.getTimestamp("dateordered");
				String fechaOrden = dateFormat.format(fechaOrdenTS);
				order.setFecha(fechaOrden);
				order.getOrderProducts();
				orderList.add(order);
				
			}
			
			orders.setCantidad(cont);
			orders.setOrdenes(orderList);
			
			this.setOrders(orders);
			
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
		
	}

	
	public int getADUserID(String value) throws Exception {

		int adUserID = 0;
		
		Connection conn = null;
		String sql = "";
		ResultSet rs = null;
		PreparedStatement pstmt = null;

		try{

			conn = DBUtils.getConnection();
			
			sql = " select ad_user_id from ad_user where value='" + value + "'"; 

			pstmt = conn.prepareStatement(sql);
			rs = pstmt.executeQuery ();

			if (rs.next()){
				adUserID = rs.getInt("ad_user_id");
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
		
		return adUserID;
	}

	
	public int getOrgByUser(int adUserID) throws Exception {

		int adOrgID = 0;
		
		Connection conn = null;
		String sql = "";
		ResultSet rs = null;
		PreparedStatement pstmt = null;

		try{

			conn = DBUtils.getConnection();
			
			sql = " select ad_orgtrx_id from z_mobileuserorg where ad_user_id =" + adUserID; 

			pstmt = conn.prepareStatement(sql);
			rs = pstmt.executeQuery ();

			if (rs.next()){
				adOrgID = rs.getInt("ad_orgtrx_id");
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
		
		return adOrgID;
	}

	public int getPaymentTermID() {
		return paymentTermID;
	}

	@XmlElement
	public void setPaymentTermID(int paymentTermID) {
		this.paymentTermID = paymentTermID;
	}

	public String getNomFantasia() {
		return NomFantasia;
	}

	@XmlElement
	public void setNomFantasia(String nomFantasia) {
		NomFantasia = nomFantasia;
	}

}
