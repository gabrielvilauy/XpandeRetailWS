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
@XmlRootElement(name = "orders")
public class Orders implements Serializable {

	private List<Order> ordenes = new ArrayList<Order>();
	private int cantidad = 0;

	private static final long serialVersionUID = -7174387852836520627L;

	/**
	 * 
	 */
	public Orders() {
	}

	public List<Order> getOrdenes() {
		return ordenes;
	}

	@XmlElement
	public void setOrdenes(List<Order> ordenes) {
		this.ordenes = ordenes;
	}

	public int getCantidad() {
		return cantidad;
	}

	@XmlElement
	public void setCantidad(int cantidad) {
		this.cantidad = cantidad;
	}

}
