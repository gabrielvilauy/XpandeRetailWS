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
@XmlRootElement(name = "upcs")
public class Upcs implements Serializable {

	private List<Upc> upcs = new ArrayList<Upc>();
	private int cantidad = 0;	
	
	private static final long serialVersionUID = 6029739273193885036L;

	/**
	 * 
	 */
	public Upcs() {
		
	}

	public List<Upc> getUpcs() {
		return upcs;
	}

	@XmlElement
	public void setUpcs(List<Upc> upcs) {
		this.upcs = upcs;
	}

	public int getCantidad() {
		return cantidad;
	}

	@XmlElement
	public void setCantidad(int cantidad) {
		this.cantidad = cantidad;
	}

	
}
