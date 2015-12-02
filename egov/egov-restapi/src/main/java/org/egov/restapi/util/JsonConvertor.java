package org.egov.restapi.util;

import org.codehaus.jackson.annotate.JsonAutoDetect.Visibility;
import org.codehaus.jackson.annotate.JsonMethod;
import org.codehaus.jackson.map.ObjectMapper;
import org.egov.dcb.bean.ChequePayment;
import org.egov.restapi.constants.RestApiConstants;

public class JsonConvertor {
	
	 public static String convert(final Object obj)  {
	        String jsonResponse;
			try {
				final ObjectMapper objectMapper = new ObjectMapper();
				objectMapper.setVisibility(JsonMethod.FIELD, Visibility.DEFAULT);
				objectMapper.setDateFormat(ChequePayment.CHEQUE_DATE_FORMAT);
				jsonResponse = objectMapper.writeValueAsString(obj);
			} catch (Exception e) {
				
				StringBuilder sb=new StringBuilder("200");
				sb.append("{ \"errorCode\":\"").append(RestApiConstants.JSON_CONVERSION_ERROR_CODE).append("\"")
				.append(" \"errorCode\":\"").append(RestApiConstants.JSON_CONVERSION_ERROR_MESSAGE).append("\"}");
				jsonResponse=sb.toString();
					
				}
	        return jsonResponse;
	    }

}
