/**
 * 
 */
package org.xpande.retail.ws.service;

import java.sql.Connection;
import java.util.Date;
import java.util.HashMap;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Statement;
import java.sql.Timestamp;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;

import org.xpande.retail.ws.model.Partner;
import org.xpande.retail.ws.model.BPartners;
import org.xpande.retail.ws.model.Invoice;
import org.xpande.retail.ws.model.PayTerms;
import org.xpande.retail.ws.model.Product;
import org.xpande.retail.ws.model.Products;
import org.xpande.retail.ws.model.Upc;
import org.xpande.retail.ws.model.Upcs;
import org.xpande.retail.ws.model.VendorReturn;
import org.xpande.retail.ws.model.VendorReturns;
import org.xpande.retail.ws.util.DBUtils;

import com.google.gson.Gson;

/**
 * @author gabriel
 *
 */
public class RetailServiceImpl {

	/**
	 * 
	 */
	public RetailServiceImpl() {
	}


	
	public Upcs getUpcs(int mProductID) throws Exception{

		Upcs upcs = new Upcs();
		
		List<Upc> upcList = new ArrayList<Upc>();
		
		Connection conn = null;
		String sql = "";
		ResultSet rs = null;
		PreparedStatement pstmt = null;

		try{
			conn = DBUtils.getConnection();
			
			sql = " select z_productoupc_id, upc "
					+ " from z_productoupc "
					+ " where m_product_id =" + mProductID; 

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
			
			upcs.setCantidad(cont);
			upcs.setUpcs(upcList);
			
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
		return upcs;
	}
	

	public int setPartnerReception(String reception) throws Exception {
		
		int resultado = -1;
		
		try {
			
			Partner bPartnerObj= new Gson().fromJson(reception, Partner.class);
			if (bPartnerObj != null) {
				resultado = this.nuevaRecepcion(bPartnerObj, reception);
			}
		} 
		catch (Exception e) {
			throw e;
		}
		
		return resultado;
	}


	private int nuevaRecepcion(Partner bPartnerObj, String jsonBody) throws Exception {

		int resultado = -1;

		Connection conn = null;
		Statement stmt = null;
		
		String sql = "";
		ResultSet rs = null;
		PreparedStatement pstmt = null;
		
		try {
			
			int adSequenceUPC = DBUtils.getSequenceID("z_mb_inoutupc");
			
			String insert = "", values = "";
			int ID_MB_InOut = 0;
		
			conn = DBUtils.getConnection();
		
			sql = " select nextid(1000556,'N') as id ";	
			pstmt = conn.prepareStatement(sql);
			rs = pstmt.executeQuery();
		
			if (rs.next()){
				ID_MB_InOut = rs.getInt("id");
			}
			
			int adUserID = 0;
			
			String userName = bPartnerObj.getUser();			
			
			if (userName == null) {
				userName = "";
			}
			else {
				userName = userName.trim();
			}
			
			if (!userName.equalsIgnoreCase("")) {
				adUserID = bPartnerObj.obtenerADUserID(userName);				
			}

			if (adUserID <= 0) adUserID = 1000002;
			
			int adOrgID = bPartnerObj.obtenerOrgByUser(adUserID);
						
			if (adOrgID <= 0) adOrgID = 1000001;
			
			// Inserto cabezal de recepcion móvil
			insert = " insert into z_mb_inout(z_mb_inout_id, ad_client_id, ad_org_id, created, updated, createdby, updatedby, issotrx, " + 
					"  description, movementtype, movementdate, c_bpartner_id, jsonbody, m_warehouse_id, isexecuted, processing) ";
			
			values = " select " + ID_MB_InOut + ", 1000000, " + adOrgID + ", now(), now(), " + adUserID + ", " + adUserID + ", 'N', " +
						"'Generada desde Movilidad', 'V+', date_trunc('day',now()), " + bPartnerObj.getID() + ",'" + jsonBody + "', " +
						"1000000, 'N', 'N' ";
			
			stmt = conn.createStatement();
			stmt.executeUpdate(insert + values);
			
			// Recorro lineas de recepcion e inserto
			for (Product prod: bPartnerObj.getLineasRecepcion().getProductos()) {

				String upc = "";
				List<Upc> upcList = prod.getUpcs().getUpcs();
				if (upcList != null) {
					if (upcList.size() > 0) {
						upc = upcList.get(0).getCodigoBarra();
						if (upc == null) upc = "";
					}
				}
				
				insert = " insert into z_mb_inoutline (z_mb_inoutline_id, ad_client_id, ad_org_id, created, updated, createdby, updatedby, " + 
						"  z_mb_inout_id, m_product_id, movementqty, documentnoref, qtyinvoiced, upc) ";			
				
				values = " select nextid(1000557,'N'), 1000000, " + adOrgID + ", now(), now(), " + adUserID + ", " + adUserID + "," + ID_MB_InOut + ", " +
						prod.getID() + ", " + prod.getCantRecepcion() + ", '" + prod.getNumeroFactura() + "', " + prod.getCantFactura() + ",'" + upc + "'";

				stmt.executeUpdate(insert + values);
				
				// Guardo codigos de barra recibidos para el producto de esta linea
				if (upcList != null) {
					for (Upc upcModel: upcList) {
						
						insert = " INSERT INTO z_mb_inoutupc (z_mb_inoutupc_id, ad_client_id, ad_org_id, created, createdby, isactive, " +
								 " updated, updatedby, m_product_id, upc, z_mb_inout_id) ";
						values = " select nextid(" + adSequenceUPC + ",'N'), 1000000, " + adOrgID + ", now(), " + adUserID + ", 'Y', " +
								 " now(), " + adUserID + ", " + prod.getID() + ", '" + upcModel.getCodigoBarra() + "', " + ID_MB_InOut;
						stmt.executeUpdate(insert + values);
					}
				}
				
			}

			// Recorro información de facturas e inserto
			for (Invoice invoice: bPartnerObj.getInvoices().getInvoices()) {

				int cCurrencyID = 142;
				if (invoice.getMoneda().equalsIgnoreCase("USD")) {
					cCurrencyID = 100;
				}
				
	            DateFormat formatter;
	            formatter = new SimpleDateFormat("yyyy-MM-dd");
	            Date dateFec = formatter.parse(invoice.getFecha());
	            Timestamp dateInvoiced = new Timestamp(dateFec.getTime());
				
				insert = " insert into z_mb_inoutfact (z_mb_inoutfact_id, ad_client_id, ad_org_id, created, updated, createdby, updatedby, " + 
						"  z_mb_inout_id, documentnoref, documentserie, c_currency_id, dateinvoiced) ";

				values = " select nextid(1000558,'N'), 1000000, " + adOrgID + ", now(), now(), " + adUserID + ", " + adUserID + "," + ID_MB_InOut + ", '" +
						invoice.getNumero() + "', '" + invoice.getSerie() + "', " + cCurrencyID + ", '" + dateInvoiced + "' ";

				stmt.executeUpdate(insert + values);
			}
			
			resultado = ID_MB_InOut;
			
		} 
		catch (Exception e) {
			resultado = -1;
		}
		finally
		{
			if (stmt != null) stmt.close();
			if (rs != null) rs.close();
			if (pstmt != null) pstmt.close();
			if (conn != null) conn.close();
		}
		
		return resultado;
	}


	private void nuevaDevolucion(Partner bPartnerObj, String jsonBody) throws Exception {

		Connection conn = null;
		Statement stmt = null;
		
		String sql = "";
		ResultSet rs = null;
		PreparedStatement pstmt = null;
		
		try {
					
			String insert = "", values = "";
			int ID_MB_OrdenDevol = 0;
		
			conn = DBUtils.getConnection();
		
			sql = " select nextid(1000559,'N') as id ";	
			pstmt = conn.prepareStatement(sql);
			rs = pstmt.executeQuery();
		
			if (rs.next()){
				ID_MB_OrdenDevol = rs.getInt("id");
			}
			
			// Inserto cabezal de devolución móvil
			insert = " insert into z_mb_ordendevol(z_mb_ordendevol_id, ad_client_id, ad_org_id, created, updated, createdby, updatedby, issotrx, " + 
					"  description, movementtype, movementdate, c_bpartner_id, jsonbody, m_warehouse_id, isexecuted, processing) ";
			
			values = " select " + ID_MB_OrdenDevol + ", 1000000, 1000000, now(), now(), 1000002, 1000002, 'N', " +
						"'Generada desde Movilidad', 'V-', date_trunc('day',now()), " + bPartnerObj.getID() + ",'" + jsonBody + "', " +
						"1000000, 'N', 'N' ";
			
			stmt = conn.createStatement();
			stmt.executeUpdate(insert + values);
			
			// Recorro lineas de orden de devolución e inserto
			for (Product prod: bPartnerObj.getProducts().getProductos()) {

				String upc = "";
				if (prod.getUpcs() != null) {
					List<Upc> upcList = prod.getUpcs().getUpcs();
					if (upcList != null) {
						if (upcList.size() > 0) {
							upc = upcList.get(0).getCodigoBarra();
							if (upc == null) upc = "";
						}
					}
				}
				
				insert = " insert into z_mb_ordendevolline (z_mb_ordendevolline_id, ad_client_id, ad_org_id, created, updated, createdby, updatedby, " + 
						"  z_mb_ordendevol_id, m_product_id, qtyentered, upc) ";

				values = " select nextid(1000560,'N'), 1000000, 1000000, now(), now(), 1000002, 1000002," + ID_MB_OrdenDevol + ", " +
						prod.getID() + ", " + prod.getCantRecepcion() + ",'" + upc + "'";

				stmt.executeUpdate(insert + values);
			}
			
		} 
		catch (Exception e) {
			throw e;
		}
		finally
		{
			if (stmt != null) stmt.close();
			if (rs != null) rs.close();
			if (pstmt != null) pstmt.close();
			if (conn != null) conn.close();
		}
	}
	
	
	public int setVendorReturns(String vendorreturns) {
		
		int respuesta = -1;
		
		try {
			
			VendorReturns returnsObj= new Gson().fromJson(vendorreturns, VendorReturns.class);
			if (returnsObj != null) {
				respuesta = this.getDevoluciones(returnsObj, vendorreturns);
			}
			
		} 
		catch (Exception e) {
			throw e;
		}
		
		return respuesta;
	}
	
	
	private int getDevoluciones(VendorReturns returnsObj, String vendorreturns) {
	
		int resultado = -1;

		try {
		
			HashMap<Integer, Partner> hashBPs = new HashMap<Integer, Partner>();
			
			// Recorro lineas de devoluciones y agrupo productos de cada socio de negocio.
			for (VendorReturn vendorReturn: returnsObj.getDevoluciones()){
				
				Partner partner = null;
				
				if (!hashBPs.containsKey(vendorReturn.getPartnerID())){
					partner = new Partner();
					partner.setID(vendorReturn.getPartnerID());
					partner.setProducts(new Products());
					hashBPs.put(partner.getID(), partner);
					
				}
				else {
					partner = hashBPs.get(vendorReturn.getPartnerID());
				}
				
				Product product = new Product();
				product.setCantRecepcion(vendorReturn.getCantidad());
				product.setID(vendorReturn.getProductID());
				product.setNumeroOrden(vendorReturn.getMotivo());
				
				partner.getProducts().getProductos().add(product);
				
			}

			int contador = 0;

			// Recorro socios de negocio a procesar
			for (Partner partner: hashBPs.values()) {
				contador++;
				this.nuevaDevolucion(partner, vendorreturns);
			}
			
			if (contador > 0) {
				resultado = contador;
			}
			
		} 
		catch (Exception e) {
			resultado = -1;
		}

		return resultado;
	}

	public Product getProdByUpc(String UPC) throws Exception{

		Product prod = null;

		try{

			prod = Product.getByUpc(UPC);
			
		}
		catch (Exception e)
		{
			throw e;
		}
		return prod;
	}
	
	
	public Products getProdsByName(String Name) throws Exception{

		Products prods = null;

		try{

			prods = Products.getProdsByName(Name);
			
		}
		catch (Exception e)
		{
			throw e;
		}
		return prods;
	}

	public BPartners getPartnersByProd(Integer mProductID) throws Exception {

		BPartners bps = null;

		try{

			bps = BPartners.getPartnersByProd(mProductID);
			
		}
		catch (Exception e)
		{
			throw e;
		}
		return bps;

	}

	public BPartners getVendorPartners() throws Exception {

		BPartners bps = null;

		try{

			bps = BPartners.getVendorPartners();
			
		}
		catch (Exception e)
		{
			throw e;
		}
		return bps;

	}

	public PayTerms getPayTerms() throws Exception {

		PayTerms terms = null;

		try{

			terms = PayTerms.getPayTerms();
			
		}
		catch (Exception e)
		{
			throw e;
		}
		return terms;

	}

}
