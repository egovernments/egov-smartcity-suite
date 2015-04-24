package org.egov.erpcollection.web.xml.converter;

import org.egov.erpcollection.integration.models.BillReceiptInfo;
import org.egov.erpcollection.integration.models.BillReceiptInfoImpl;
import org.egov.erpcollection.integration.models.ReceiptAccountInfo;
import org.egov.erpcollection.integration.models.ReceiptInstrumentInfo;

import com.thoughtworks.xstream.converters.Converter;
import com.thoughtworks.xstream.converters.MarshallingContext;
import com.thoughtworks.xstream.converters.UnmarshallingContext;
import com.thoughtworks.xstream.io.HierarchicalStreamReader;
import com.thoughtworks.xstream.io.HierarchicalStreamWriter;

public class BillReceiptInfoConverter implements Converter{
	
	@Override
	public void marshal(Object value, HierarchicalStreamWriter writer,
			MarshallingContext context) {
		
		BillReceiptInfo receiptInfo = (BillReceiptInfo) value;
		ConverterUtil.createNode(writer, "event", receiptInfo.getEvent());
		ConverterUtil.createNode(writer, "referenceNumber", receiptInfo.getBillReferenceNum());    
		ConverterUtil.createNode(writer, "receiptNumber", receiptInfo.getReceiptNum());  
		ConverterUtil.createNode(writer, "receiptDate", String.valueOf(receiptInfo.getReceiptDate()));
		ConverterUtil.createNode(writer, "location", receiptInfo.getReceiptLocation()==null?
				"":receiptInfo.getReceiptLocation().getName());
		ConverterUtil.createNode(writer, "status", receiptInfo.getReceiptStatus().getCode());
		ConverterUtil.createNode(writer, "PayeeName", receiptInfo.getPayeeName());
		ConverterUtil.createNode(writer, "PayeeAddress", receiptInfo.getPayeeAddress());
		writer.startNode("Receipt-Accounts");
		for(ReceiptAccountInfo receiptAccInfo : receiptInfo.getAccountDetails()){
			context.convertAnother(receiptAccInfo);
		}
		writer.endNode(); 
		
		writer.startNode("Receipt-Instruments");
		for(ReceiptInstrumentInfo receiptAccInfo : receiptInfo.getInstrumentDetails()){
			context.convertAnother(receiptAccInfo);
		}
		writer.endNode(); 
	}

	@Override
	public Object unmarshal(HierarchicalStreamReader arg0,
			UnmarshallingContext arg1) {
		return null;
	}

	@Override
	public boolean canConvert(Class clazz) {
		return clazz.equals(BillReceiptInfoImpl.class);
	}
}
