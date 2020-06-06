/**
 * 
 */
package org.xpande.retail.ws.service;

import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import org.xpande.retail.ws.model.BPartners;
import org.xpande.retail.ws.model.Partner;
import org.xpande.retail.ws.model.PayTerms;
import org.xpande.retail.ws.model.Product;
import org.xpande.retail.ws.model.Products;

/**
 * @author gabriel
 *
 */
@Path("RetailService")
public class RetailService {

	/**
	 * 
	 */
	public RetailService() {
	}
	
	@GET 
	@Path("getvendorlist/{rut}/{forzarsinoc}")
	@Produces(MediaType.APPLICATION_JSON) 
	public Partner getPartnerByRut(@PathParam("rut")String RUT, @PathParam("forzarsinoc")String forzarSinOc ){

		Partner model = new Partner();
		
		try {
		
			//RetailServiceImpl impl = new RetailServiceImpl();
			//bpartner = impl.getPartnerByRut(RUT, forzarSinOc);
			
			model.initByRut(RUT, forzarSinOc);
			
			if ((model.getRazonSocial() == null) || (model.getRazonSocial().trim().equalsIgnoreCase(""))) {
				return null;
			}
		} 
		catch (Exception e) {
			e.printStackTrace();
		}
		
		return model;
	}

	@GET 
	@Path("getsociolist/{rut}/{forzarsinoc}")
	@Produces(MediaType.APPLICATION_JSON) 
	public Partner getSocioByRut(@PathParam("rut")String RUT, @PathParam("forzarsinoc")String forzarSinOc ){

		Partner model = new Partner();
		
		try {
			model.initByRut(RUT, forzarSinOc);
			
			if ((model.getRazonSocial() == null) || (model.getRazonSocial().trim().equalsIgnoreCase(""))) {
				return null;
			}
			
			//RetailServiceImpl impl = new RetailServiceImpl();
			//bpartner = impl.getPartnerByRut(RUT, forzarSinOc);
			
		} 
		catch (Exception e) {
			e.printStackTrace();
		}
		
		return model;
	}

	
	@GET 
	@Path("getvendors")
	@Produces(MediaType.APPLICATION_JSON) 
	public BPartners getVendorPartners(){

		BPartners bpartners = new BPartners();
		
		try {
		
			RetailServiceImpl impl = new RetailServiceImpl();
			bpartners = impl.getVendorPartners();
			
		} 
		catch (Exception e) {
			e.printStackTrace();
		}
		
		return bpartners;
	}

	@GET 
	@Path("getpayterms")
	@Produces(MediaType.APPLICATION_JSON) 
	public PayTerms getPayTermns(){

		PayTerms terms = new PayTerms();
		
		try {
		
			RetailServiceImpl impl = new RetailServiceImpl();
			terms = impl.getPayTerms();
			
		} 
		catch (Exception e) {
			e.printStackTrace();
		}
		
		return terms;
	}

	
	@GET 
	@Path("getprodbyupc/{upc}")
	@Produces(MediaType.APPLICATION_JSON) 
	public Product getProdByUpc(@PathParam("upc")String UPC){

		Product product = new Product();
		
		try {
		
			RetailServiceImpl impl = new RetailServiceImpl();
			product = impl.getProdByUpc(UPC);
			
		} 
		catch (Exception e) {
			e.printStackTrace();
		}
		
		return product;
	}

	@GET 
	@Path("getprodsbyname/{name}")
	@Produces(MediaType.APPLICATION_JSON) 
	public Products getProdsByName(@PathParam("name")String Name){

		Products prods = new Products();
		
		try {
		
			String nombreProd = Name;
			if (nombreProd != null) {
				nombreProd = nombreProd.replace(" ", "%");
				
			}
			
			RetailServiceImpl impl = new RetailServiceImpl();
			prods = impl.getProdsByName(nombreProd);
			
		} 
		catch (Exception e) {
			e.printStackTrace();
		}
		
		return prods;
	}

	@GET 
	@Path("getvendorsbyprod/{prodid}")
	@Produces(MediaType.APPLICATION_JSON) 
	public BPartners getPartnersByProd(@PathParam("prodid")Integer mProductID){

		BPartners bpartners = new BPartners();
		
		try {
		
			RetailServiceImpl impl = new RetailServiceImpl();
			bpartners = impl.getPartnersByProd(mProductID);
			
			
		} 
		catch (Exception e) {
			e.printStackTrace();
		}
		
		return bpartners;
	}
	
	
	@POST 
	@Path("setreception")
	@Consumes(MediaType.APPLICATION_JSON)
	public Response setPartnerReception(String reception){

		int respuesta = -1;
		
		try {
			RetailServiceImpl impl = new RetailServiceImpl();
			respuesta = impl.setPartnerReception(reception);

		} catch (Exception e) {
			e.printStackTrace();
		}
		
		return Response.status(201).entity(respuesta).build();
		
	}	

	@POST 
	@Path("setreturns")
	@Consumes(MediaType.APPLICATION_JSON)
	public Response setVendorReturns(String vendorreturns){

		int respuesta = -1;
		
		try {
			RetailServiceImpl impl = new RetailServiceImpl();
			respuesta = impl.setVendorReturns(vendorreturns);

		} catch (Exception e) {
			e.printStackTrace();
		}
		
		return Response.status(201).entity(respuesta).build();
		
	}	
	
	
	@GET 
	@Path("hello")
	@Produces(MediaType.TEXT_PLAIN)
	public String hello() {
		return "holaaaa";
	}
	
	
}
