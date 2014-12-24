package org.egov.erpcollection.web.handler;

import org.egov.erpcollection.integration.models.BillDetails;
import org.egov.erpcollection.integration.models.BillInfoImpl;

import com.thoughtworks.xstream.XStream;
import com.thoughtworks.xstream.converters.basic.BooleanConverter;
import com.thoughtworks.xstream.converters.basic.DateConverter;

public class BillCollectXmlHandler {
	private static final String DATE_FORMAT_DDMMYYY = "dd/MM/yyyy";
	
	public String toXML (Object obj) {
		XStream xStream = createXStream();
		String[] array = {DATE_FORMAT_DDMMYYY};
		xStream.registerConverter(new DateConverter(DATE_FORMAT_DDMMYYY,array));
		xStream.registerConverter(BooleanConverter.BINARY);
		xStream.aliasAttribute(BillDetails.class,"billDate", "billDate");
		return xStream.toXML(obj);
	}
	
	protected XStream createXStream() {
        XStream xstream = new XStream();
        xstream.autodetectAnnotations(true);
        return xstream;
    }
	
	
	public Object toObject (String xml) {
		XStream xStream = createXStream();
		xStream.alias("bill-collect", BillInfoImpl.class);
		String[] array = {DATE_FORMAT_DDMMYYY};
		xStream.registerConverter(new DateConverter(DATE_FORMAT_DDMMYYY,array));
		xStream.registerConverter(BooleanConverter.BINARY);
		return (BillInfoImpl)xStream.fromXML(xml);
	}
}
