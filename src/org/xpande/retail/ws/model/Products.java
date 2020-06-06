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
@XmlRootElement(name = "products")
public class Products implements Serializable {

	private List<Product> productos = new ArrayList<Product>();
	private int cantidad = 0;
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 8503413032850889950L;

	
	/**
	 * 
	 */
	public Products() {
	}

	public List<Product> getProductos() {
		return productos;
	}

	@XmlElement
	public void setProductos(List<Product> productos) {
		this.productos = productos;
	}

	public int getCantidad() {
		return cantidad;
	}

	@XmlElement
	public void setCantidad(int cantidad) {
		this.cantidad = cantidad;
	}


	public static Products getProdsByName(String Name) throws Exception {
		
		Products prods = new Products();

		Connection conn = null;
		String sql = "";
		ResultSet rs = null;
		PreparedStatement pstmt = null;

		try{
			conn = DBUtils.getConnection();

			sql = " select count(*) as contador " + 
					" from m_product " +
					" where lower(name) like '%" + Name.toLowerCase() + "%'";

			pstmt = conn.prepareStatement(sql);
			rs = pstmt.executeQuery ();

			if (rs.next()){
				if (rs.getInt("contador") > 300){
					prods.setCantidad(300);
					return prods;
				}
			}

			if (rs != null) rs.close();
			if (pstmt != null) pstmt.close();
			
			sql = " select m_product_id, value, name " + 
					" from m_product " +
					" where lower(name) like '%" + Name.toLowerCase() + "%' " +
					" order by name";

			pstmt = conn.prepareStatement(sql);
			rs = pstmt.executeQuery ();

			int cont = 0;
			List<Product> prodList = new ArrayList<Product>();
			
			while (rs.next()){
				
				cont++;
				
				Product prod = new Product();
				prod.setID(rs.getInt("m_product_id"));
				prod.setCodigoInterno(rs.getString("value"));
				prod.setNombre(rs.getString("name"));
				prod.getUpcsProducto();
				prodList.add(prod);
			}
			
			prods.setCantidad(cont);
			
			if (prodList.size() > 0) {
				prods.setProductos(prodList);	
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

		
		return prods;
	}

	
}
