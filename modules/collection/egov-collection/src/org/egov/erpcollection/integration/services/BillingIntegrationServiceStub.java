package org.egov.erpcollection.integration.services;
import java.io.File;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Set;

import org.apache.log4j.Logger;
import org.egov.erpcollection.integration.models.BillReceiptInfo;
import org.egov.erpcollection.integration.models.BillReceiptInfoImpl;
import org.egov.erpcollection.models.ReceiptDetail;
import org.egov.erpcollection.web.xml.converter.BillReceiptInfoConverter;
import org.egov.erpcollection.web.xml.converter.ReceiptAccountInfoConverter;
import org.egov.erpcollection.web.xml.converter.ReceiptInstrumentInfoConverter;

import com.thoughtworks.xstream.XStream;
import com.thoughtworks.xstream.io.xml.DomDriver;

/**
* This interface needs to be implemented by any billing application that integrates
* with the eGov collection system. 
* For internal applications, the methods can use direct API calls.
* For external applications, the integration can be through web-service/REST calls.
* The convention to be followed: a bean named "<servicename>collectionsInterface" 
* needs to be available in the spring context. Service name is the name provided for 
* the billing service in <ServiceDetails> class.
*/
public class BillingIntegrationServiceStub implements BillingIntegrationService{
	
	private static final Logger LOGGER = Logger.getLogger(
			BillingIntegrationServiceStub.class);
	

	@Override
	public Boolean updateReceiptDetails(Set<BillReceiptInfo> billReceipts) {
		//FileOutputStream fos = null;
		try {
			String xml = null;
			//fos = new FileOutputStream(getOutputFile("BillReceiptOutput.xml"));

			xml = convertToXML(billReceipts);
			//fos.write(xml.getBytes());
			
			LOGGER.debug("Written bill details to file successfully " + xml);
			
			//fos.close(); 
			
		} /*catch (FileNotFoundException e) {
			LOGGER.error("Error occrured while updating dishonored cheque status to billing system : " + e.getMessage());
			return false;
		} catch (IOException e) {
			LOGGER.error("Error occrured while updating dishonored cheque status to billing system : " + e.getMessage());
			return false;
		} */catch (Exception e){
			LOGGER.error("Error occrured while updating dishonored cheque status to billing system : " + e.getMessage());
		}
		return true;
	}
	
	@Override
	public void apportionPaidAmount(String billReferenceNumber,
			BigDecimal actualAmountPaid,
			ArrayList<ReceiptDetail> receiptDetailsArray) {

	}
	
	/**
	 * This method converts the given bill receipt object into an XML
	 * 
	 * @param billReceipt an instance of <code>BillReceiptInfo</code>
	 * 
	 * @return a <code>String</code> representing the XML format of the 
	 * <code>BillReceiptInfo</code> object
	 */
	private String convertToXML(Set<BillReceiptInfo> billReceipts){
		XStream xStream = new XStream(new DomDriver());
        xStream.registerConverter(new BillReceiptInfoConverter());
        xStream.registerConverter(new ReceiptAccountInfoConverter());
        xStream.registerConverter(new ReceiptInstrumentInfoConverter());
        xStream.alias("Bill-Receipt", BillReceiptInfoImpl.class);
        return xStream.toXML(billReceipts);
	}
	
	private File getOutputFile(String fileName){
		return new File(fileName);
	}
}
