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
@XmlRootElement(name = "vendorreturns")
public class VendorReturns implements Serializable {

	private List<VendorReturn> devoluciones = new ArrayList<VendorReturn>();
	private int cantidad = 0;

	
	private static final long serialVersionUID = -8477673306804429866L;

	
	/**
	 * 
	 */
	public VendorReturns() {
	}

	public List<VendorReturn> getDevoluciones() {
		return devoluciones;
	}

	@XmlElement
	public void setDevoluciones(List<VendorReturn> devoluciones) {
		this.devoluciones = devoluciones;
	}

	public int getCantidad() {
		return cantidad;
	}

	@XmlElement
	public void setCantidad(int cantidad) {
		this.cantidad = cantidad;
	}

	
}
