package org.egov.ptis.nmc.handler;

import org.egov.ptis.nmc.model.TaxCalculationInfo;

import com.thoughtworks.xstream.XStream;
import com.thoughtworks.xstream.converters.basic.BooleanConverter;
import com.thoughtworks.xstream.converters.basic.DateConverter;

public class TaxCalculationInfoXmlHandler {
	
    private static final String DATE_FORMAT_DDMMYYY = "dd/MM/yyyy";
    
	public String toXML (Object obj) {
		XStream xStream = createXStream();
		String[] array = {DATE_FORMAT_DDMMYYY};
        xStream.registerConverter(new DateConverter(DATE_FORMAT_DDMMYYY,array));
        xStream.registerConverter(BooleanConverter.BINARY);
        return xStream.toXML(obj);
	}
	
	protected XStream createXStream() {
        XStream xstream = new XStream();
        xstream.autodetectAnnotations(true);
        return xstream;
    }
	
	
	public Object toObject (String xml) {
		XStream xStream = createXStream();
		xStream.alias("taxcalculationinfo", TaxCalculationInfo.class);
        String[] array = {DATE_FORMAT_DDMMYYY};
        xStream.registerConverter(new DateConverter(DATE_FORMAT_DDMMYYY,array));
        xStream.registerConverter(BooleanConverter.BINARY);
		return (TaxCalculationInfo)xStream.fromXML(xml);
	}
}
