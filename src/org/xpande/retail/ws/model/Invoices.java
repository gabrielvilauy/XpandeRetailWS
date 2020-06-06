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
@XmlRootElement(name = "invoices")
public class Invoices implements Serializable {

	
	private List<Invoice> invoices = new ArrayList<Invoice>();
	private int cantidad = 0;


	private static final long serialVersionUID = -6839777288208241683L;

	/**
	 * 
	 */
	public Invoices() {
	}

	public List<Invoice> getInvoices() {
		return invoices;
	}

	@XmlElement
	public void setInvoices(List<Invoice> invoices) {
		this.invoices = invoices;
	}

	public int getCantidad() {
		return cantidad;
	}

	@XmlElement
	public void setCantidad(int cantidad) {
		this.cantidad = cantidad;
	}

	
	
}
