package org.egov.erpcollection.web.xml.converter;

import org.egov.erpcollection.integration.models.ReceiptInstrumentInfo;
import org.egov.erpcollection.integration.models.ReceiptInstrumentInfoImpl;

import com.thoughtworks.xstream.converters.Converter;
import com.thoughtworks.xstream.converters.MarshallingContext;
import com.thoughtworks.xstream.converters.UnmarshallingContext;
import com.thoughtworks.xstream.io.HierarchicalStreamReader;
import com.thoughtworks.xstream.io.HierarchicalStreamWriter;

public class ReceiptInstrumentInfoConverter implements Converter{

	@Override
	public void marshal(Object value, HierarchicalStreamWriter writer,
			MarshallingContext context) {
		
		ReceiptInstrumentInfo receiptInstrInfo = (ReceiptInstrumentInfo) value;
		
		ConverterUtil.createNode(writer, "instrumentNo", receiptInstrInfo.getInstrumentNumber());
		ConverterUtil.createNode(writer, "instrumentDate", String.valueOf(receiptInstrInfo.getInstrumentDate()));
		ConverterUtil.createNode(writer, "instrumentAmount", String.valueOf(receiptInstrInfo.getInstrumentAmount()));
		ConverterUtil.createNode(writer, "instrumentType", receiptInstrInfo.getInstrumentType());
	}

	@Override
	public Object unmarshal(HierarchicalStreamReader arg0,
			UnmarshallingContext context) {
		return null;
	}

	@Override
	public boolean canConvert(Class clazz) {
		return clazz.equals(ReceiptInstrumentInfoImpl.class);
	}
	
}
