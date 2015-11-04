package org.egov.restapi.util;

import java.io.IOException;

import org.codehaus.jackson.JsonGenerationException;
import org.codehaus.jackson.annotate.JsonMethod;
import org.codehaus.jackson.annotate.JsonAutoDetect.Visibility;
import org.codehaus.jackson.map.JsonMappingException;
import org.codehaus.jackson.map.ObjectMapper;
import org.egov.dcb.bean.ChequePayment;

import ch.qos.logback.classic.Logger;

public class JsonConvertor {
	
	 public static String convert(final Object obj)  {
	        String jsonResponse;
			try {
				final ObjectMapper objectMapper = new ObjectMapper();
				objectMapper.setVisibility(JsonMethod.FIELD, Visibility.DEFAULT);
				objectMapper.setDateFormat(ChequePayment.CHEQUE_DATE_FORMAT);
				jsonResponse = objectMapper.writeValueAsString(obj);
			} catch (Exception e) {
				jsonResponse="ERROR";
			}
	        return jsonResponse;
	    }

}
