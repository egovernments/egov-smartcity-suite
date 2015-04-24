package org.egov.erpcollection.web.xml.converter;

import org.egov.erpcollection.integration.models.ReceiptAccountInfo;
import org.egov.erpcollection.integration.models.ReceiptAccountInfoImpl;

import com.thoughtworks.xstream.converters.Converter;
import com.thoughtworks.xstream.converters.MarshallingContext;
import com.thoughtworks.xstream.converters.UnmarshallingContext;
import com.thoughtworks.xstream.io.HierarchicalStreamReader;
import com.thoughtworks.xstream.io.HierarchicalStreamWriter;

public class ReceiptAccountInfoConverter implements Converter{

	@Override
	public void marshal(Object value, HierarchicalStreamWriter writer,
			MarshallingContext context) {
		
		ReceiptAccountInfo receiptInfo = (ReceiptAccountInfo) value;
		ConverterUtil.createNode(writer, "glCode", receiptInfo.getGlCode());   
		ConverterUtil.createNode(writer, "creditAmt", String.valueOf(receiptInfo.getCrAmount()));
		ConverterUtil.createNode(writer, "debitAmt", String.valueOf(receiptInfo.getDrAmount()));
		ConverterUtil.createNode(writer, "Function", receiptInfo.getFunction());
	}

	@Override
	public Object unmarshal(HierarchicalStreamReader arg0,
			UnmarshallingContext arg1) {
		return null;
	}

	@Override
	public boolean canConvert(Class clazz) {
		return clazz.equals(ReceiptAccountInfoImpl.class);
	}
	
}
