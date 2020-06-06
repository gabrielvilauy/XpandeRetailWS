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
@XmlRootElement(name = "product")
public class Product implements Serializable {

	private static final long serialVersionUID = -8042638774742022508L;

	private int ID = 0;
	private String codigoInterno = "";
	private String codigoProdProv = "";
	private String nombre = "";
	private String seccion = "";
	private String rubro = "";
	private String familia = "";
	private String subfamilia = "";
	private double cantRecepcion = 0;
	private double cantFactura = 0;
	private double cantOrden = 0;
	private String numeroFactura = "";
	private String numeroOrden = "";
	private Upcs upcs = null;
	
	
	/**
	 * 
	 */
	public Product() {
	}

	public int getID() {
		return ID;
	}

	@XmlElement
	public void setID(int iD) {
		ID = iD;
	}

	public String getCodigoInterno() {
		return codigoInterno;
	}

	@XmlElement
	public void setCodigoInterno(String codigoInterno) {
		this.codigoInterno = codigoInterno;
	}

	public String getCodigoProdProv() {
		return codigoProdProv;
	}

	@XmlElement
	public void setCodigoProdProv(String codigoProdProv) {
		this.codigoProdProv = codigoProdProv;
	}

	public String getNombre() {
		return nombre;
	}

	@XmlElement
	public void setNombre(String nombre) {
		this.nombre = nombre;
	}

	public Upcs getUpcs() {
		return upcs;
	}
	
	@XmlElement
	public void setUpcs(Upcs upcs) {
		this.upcs = upcs;
	}
	
	public String getSeccion() {
		return seccion;
	}

	@XmlElement
	public void setSeccion(String seccion) {
		this.seccion = seccion;
	}

	public String getRubro() {
		return rubro;
	}

	@XmlElement
	public void setRubro(String rubro) {
		this.rubro = rubro;
	}

	public String getFamilia() {
		return familia;
	}

	@XmlElement
	public void setFamilia(String familia) {
		this.familia = familia;
	}

	public String getSubfamilia() {
		return subfamilia;
	}

	@XmlElement
	public void setSubfamilia(String subfamilia) {
		this.subfamilia = subfamilia;
	}
	
	public double getCantRecepcion() {
		return cantRecepcion;
	}

	@XmlElement
	public void setCantRecepcion(double cantRecepcion) {
		this.cantRecepcion = cantRecepcion;
	}

	public double getCantFactura() {
		return cantFactura;
	}

	@XmlElement
	public void setCantFactura(double cantFactura) {
		this.cantFactura = cantFactura;
	}

	public double getCantOrden() {
		return cantOrden;
	}

	@XmlElement
	public void setCantOrden(double cantOrden) {
		this.cantOrden = cantOrden;
	}

	public String getNumeroFactura() {
		return numeroFactura;
	}

	@XmlElement
	public void setNumeroFactura(String numeroFactura) {
		this.numeroFactura = numeroFactura;
	}

	public String getNumeroOrden() {
		return numeroOrden;
	}

	@XmlElement
	public void setNumeroOrden(String numeroOrden) {
		this.numeroOrden = numeroOrden;
	}

	public void getUpcsProducto() throws Exception{

		List<Upc> upcList = new ArrayList<Upc>();
		
		Connection conn = null;
		String sql = "";
		ResultSet rs = null;
		PreparedStatement pstmt = null;

		try{
			conn = DBUtils.getConnection();
			
			sql = " select z_productoupc_id, upc "
					+ " from z_productoupc "
					+ " where m_product_id =" + this.getID(); 

			pstmt = conn.prepareStatement(sql);
			rs = pstmt.executeQuery ();
		
			int cont = 0;
			while (rs.next()){
				
				cont++;
				
				Upc upc = new Upc();
				upc.setID(rs.getInt("z_productoupc_id"));
				upc.setCodigoBarra(rs.getString("upc"));
				
				upcList.add(upc);
			}
			
			this.setUpcs(new Upcs());
			this.upcs.setCantidad(cont);
			this.upcs.setUpcs(upcList);
			
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

	public static Product getByUpc(String UPC) throws Exception {
		
		Product prod = null;

		Connection conn = null;
		String sql = "";
		ResultSet rs = null;
		PreparedStatement pstmt = null;

		try{
			conn = DBUtils.getConnection();
			
			sql = " select p.m_product_id, p.value, p.name " + 
					" from m_product p " +
					" inner join z_productoupc pu on p.m_product_id = pu.m_product_id " +
					" where pu.upc='" + UPC + "'";

			pstmt = conn.prepareStatement(sql);
			rs = pstmt.executeQuery ();

			if (rs.next()){
				prod = new Product();
				prod.setID(rs.getInt("m_product_id"));
				prod.setCodigoInterno(rs.getString("value"));
				prod.setNombre(rs.getString("name"));
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

		
		return prod;
	}
	
	
}
