/**
 * 
 */
package org.xpande.retail.ws.model;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

/**
 * @author gabriel
 *
 */
@XmlRootElement(name = "lineasRecepcion")
public class LineasRecepcion implements Serializable {

	private List<Product> productos = new ArrayList<Product>();
	private int cantidad = 0;

	
	private static final long serialVersionUID = -3092744526882245933L;

	/**
	 * 
	 */
	public LineasRecepcion() {
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

}
