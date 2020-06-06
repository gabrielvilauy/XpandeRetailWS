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
public class Partner implements Serializable{

	private int ID = 0;
	private String RUT = "";
	private String RazonSocial = "";
	private String NomFantasia = "";
	private String DevolPendiente = "N";

	private String fechaUltCompra = null;
	private int paymentTermID = 0;
	private String user="";

	private Products products = null;
	private Orders orders = null;
	private LineasRecepcion lineasRecepcion = null;
	private Invoices invoices = null;
	
	private static final long serialVersionUID = 4591211767645645883L;

	/**
	 * 
	 */
	public Partner() {
	}

	public int getID() {
		return ID;
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

	public String getNomFantasia() {
		return NomFantasia;
	}

	@XmlElement
	public void setNomFantasia(String nomFantasia) {
		NomFantasia = nomFantasia;
	}

	public String getDevolPendiente() {
		return DevolPendiente;
	}

	@XmlElement
	public void setDevolPendiente(String devolPendiente) {
		DevolPendiente = devolPendiente;
	}

	public String getFechaUltCompra() {
		return fechaUltCompra;
	}

	@XmlElement
	public void setFechaUltCompra(String fechaUltCompra) {
		this.fechaUltCompra = fechaUltCompra;
	}

	public int getPaymentTermID() {
		return paymentTermID;
	}

	@XmlElement
	public void setPaymentTermID(int paymentTermID) {
		this.paymentTermID = paymentTermID;
	}

	public String getUser() {
		return user;
	}

	@XmlElement
	public void setUser(String user) {
		this.user = user;
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
	
	private void obtenerByRut(String RUT) throws Exception {

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
				this.setID(rs.getInt("c_bpartner_id"));
				this.setRUT(RUT);
				this.setRazonSocial(rs.getString("name"));
				if (rs.getInt("po_paymentterm_id") > 0) {
					this.setPaymentTermID(rs.getInt("po_paymentterm_id"));	
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
	}

	
	public void initByRut(String RUT, String forzarSinOc) throws Exception{

		Connection conn = null;
		String sql = "";
		ResultSet rs = null;
		PreparedStatement pstmt = null;

		try{
			this.obtenerByRut(RUT);
			
			if ((this.getRazonSocial() == null) || (this.getRazonSocial().trim().equalsIgnoreCase(""))){
				return;
			}

			this.setProducts(new Products());			
			this.setOrders(new Orders());
			
			if (forzarSinOc.equalsIgnoreCase("N")) {
				this.getPurchaseOrders();
				if (this.getOrders() !=  null) {
					if (this.getOrders().getOrdenes() != null){
						if (this.getOrders().getOrdenes().size() > 0){
							return;
						}
					}
				}
			}
			

			List<Product> prods = new ArrayList<Product>();
			
			conn = DBUtils.getConnection();
			
			sql = " select bpp.m_product_id, p.value, p.name, bpp.vendorproductno, " +
					" s.name as seccion, r.name as rubro, f.name as familia, sf.name as subfamilia " +
					" from z_productosocio bpp " + 
					" inner join m_product p on bpp.m_product_id = p.m_product_id " + 
					" left outer join z_productoseccion s on s.z_productoseccion_id = p.z_productoseccion_id " +
					" left outer join z_productorubro r on r.z_productorubro_id = p.z_productorubro_id " +
					" left outer join z_productofamilia f on f.z_productofamilia_id = p.z_productofamilia_id " +
					" left outer join z_productosubfamilia sf on sf.z_productosubfamilia_id = p.z_productosubfamilia_id " +
					" where bpp.c_bpartner_id =" + this.getID() + 
					" and p.isactive='Y' " + 
					" order by p.name"; 

			pstmt = conn.prepareStatement(sql);
			rs = pstmt.executeQuery ();
		
			int cont = 0;
			while (rs.next()){
				
				cont++;
				
				Product prod = new Product();
				prod.setID(rs.getInt("m_product_id"));
				prod.setCodigoInterno(rs.getString("value"));
				prod.setNombre(rs.getString("name"));
				prod.setCodigoProdProv(rs.getString("vendorproductno"));
				prod.setSeccion(rs.getString("seccion"));
				prod.setRubro(rs.getString("rubro"));
				prod.setFamilia(rs.getString("familia"));
				prod.setSubfamilia(rs.getString("subfamilia"));
				prod.getUpcsProducto();
				prods.add(prod);
			}
			
			this.setProducts(new Products());
			this.getProducts().setCantidad(cont);
			this.getProducts().setProductos(prods);
			
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

	public int obtenerADUserID(String value) throws Exception {

		int adUserID = 0;
		
		Connection conn = null;
		String sql = "";
		ResultSet rs = null;
		PreparedStatement pstmt = null;

		try{

			conn = DBUtils.getConnection();
			
			sql = " select ad_user_id from ad_user where lower(value)='" + value.toLowerCase() + "'"; 

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

	
	public int obtenerOrgByUser(int adUserID) throws Exception {

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

}
