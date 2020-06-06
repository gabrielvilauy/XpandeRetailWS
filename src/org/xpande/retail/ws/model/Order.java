/**
 * 
 */
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

/**
 * @author gabriel
 *
 */
@XmlRootElement(name = "order")
public class Order implements Serializable {

	private int ID = 0;
	private String numero = "";
	private String fecha = "";
	private String comprador = "";
	private double monto = 0;
	private Products products = null;
	private int partnerID = 0;
	
	/**
	 * 
	 */
	private static final long serialVersionUID = -7174387852836520627L;

	/**
	 * 
	 */
	public Order() {
	}

	
	public int getID() {
		return ID;
	}

	@XmlElement
	public void setID(int iD) {
		ID = iD;
	}

	public String getNumero() {
		return numero;
	}

	@XmlElement
	public void setNumero(String numero) {
		this.numero = numero;
	}

	public String getFecha() {
		return fecha;
	}

	@XmlElement
	public void setFecha(String fecha) {
		this.fecha = fecha;
	}

	public String getComprador() {
		return comprador;
	}

	@XmlElement
	public void setComprador(String comprador) {
		this.comprador = comprador;
	}

	public double getMonto() {
		return monto;
	}

	@XmlElement
	public void setMonto(double monto) {
		this.monto = monto;
	}

	public Products getProducts() {
		return products;
	}

	@XmlElement
	public void setProducts(Products products) {
		this.products = products;
	}

	
	
	public int getPartnerID() {
		return partnerID;
	}


	public void setPartnerID(int partnerID) {
		this.partnerID = partnerID;
	}


	public void getOrderProducts() throws Exception {
		
		Connection conn = null;
		String sql = "";
		ResultSet rs = null;
		PreparedStatement pstmt = null;

		try{
			
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
					" where bpp.c_bpartner_id =" + this.getPartnerID() + 
					" and p.isactive='Y' " +
					" and p.m_product_id in (select m_product_id from c_orderline where c_order_id =" + this.getID() + ") " +
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
	
}
