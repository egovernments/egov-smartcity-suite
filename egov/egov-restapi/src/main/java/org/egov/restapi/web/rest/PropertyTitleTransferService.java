/*
 * eGov suite of products aim to improve the internal efficiency,transparency,
 *    accountability and the service delivery of the government  organizations.
 *
 *     Copyright (C) <2015>  eGovernments Foundation
 *
 *     The updated version of eGov suite of products as by eGovernments Foundation
 *     is available at http://www.egovernments.org
 *
 *     This program is free software: you can redistribute it and/or modify
 *     it under the terms of the GNU General Public License as published by
 *     the Free Software Foundation, either version 3 of the License, or
 *     any later version.
 *
 *     This program is distributed in the hope that it will be useful,
 *     but WITHOUT ANY WARRANTY; without even the implied warranty of
 *     MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 *     GNU General Public License for more details.
 *
 *     You should have received a copy of the GNU General Public License
 *     along with this program. If not, see http://www.gnu.org/licenses/ or
 *     http://www.gnu.org/licenses/gpl.html .
 *
 *     In addition to the terms of the GPL license to be adhered to in using this
 *     program, the following additional terms are to be complied with:
 *
 *         1) All versions of this program, verbatim or modified must carry this
 *            Legal Notice.
 *
 *         2) Any misrepresentation of the origin of the material is prohibited. It
 *            is required that all modified versions of this material be marked in
 *            reasonable ways as different from the original version.
 *
 *         3) This license does not grant any rights to any user of the program
 *            with regards to rights under trademark law for use of the trade names
 *            or trademarks of eGovernments Foundation.
 *
 *   In case of any queries, you can reach eGovernments Foundation at contact@egovernments.org.
 */
package org.egov.restapi.web.rest;

import java.io.IOException;
import java.text.ParseException;
import java.util.List;

import javax.ws.rs.core.MediaType;

import org.codehaus.jackson.JsonGenerationException;
import org.codehaus.jackson.JsonParseException;
import org.codehaus.jackson.annotate.JsonAutoDetect.Visibility;
import org.codehaus.jackson.annotate.JsonMethod;
import org.codehaus.jackson.map.JsonMappingException;
import org.codehaus.jackson.map.ObjectMapper;
import org.codehaus.jackson.map.SerializationConfig;
import org.egov.dcb.bean.ChequePayment;
import org.egov.infra.config.core.ApplicationThreadLocals;
import org.egov.ptis.domain.model.ErrorDetails;
import org.egov.ptis.domain.model.NewPropertyDetails;
import org.egov.ptis.domain.model.OwnerDetails;
import org.egov.ptis.domain.service.transfer.PropertyTransferService;
import org.egov.restapi.model.PropertyTransferDetails;
import org.egov.restapi.util.JsonConvertor;
import org.egov.restapi.util.ValidationUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class PropertyTitleTransferService {

	@Autowired
	private ValidationUtil validationUtil;
	
	@Autowired
	private PropertyTransferService transferOwnerService;
	 
	/**
	 * Initiates property transfer
	 * @param titleTransferDetails
	 * @return
	 * @throws JsonGenerationException
	 * @throws JsonMappingException
	 * @throws IOException
	 * @throws ParseException
	 */
	@RequestMapping(value = "/property/titletransfer", method = RequestMethod.POST, consumes = MediaType.APPLICATION_JSON, produces = MediaType.APPLICATION_JSON)
    public String transferProperty(@RequestBody String titleTransferDetails)
            throws JsonGenerationException, JsonMappingException, IOException, ParseException {
		String responseJson = new String();
		ApplicationThreadLocals.setUserId(2L);
		
		PropertyTransferDetails propertyTransferDetails = (PropertyTransferDetails) getObjectFromJSONRequest(
				titleTransferDetails, PropertyTransferDetails.class);
		
		ErrorDetails errorDetails = validationUtil.validatePropertyTransferRequest(propertyTransferDetails);
		if (errorDetails != null) {
            responseJson = JsonConvertor.convert(errorDetails);
        } else {
        	String assessmentNo = propertyTransferDetails.getAssessmentNo();
        	String mutationReasonCode = propertyTransferDetails.getMutationReasonCode();
        	String saleDetails = propertyTransferDetails.getSaleDetail();
        	String deedNo = propertyTransferDetails.getDeedNo();
        	String deedDate = propertyTransferDetails.getDeedDate();
        	List<OwnerDetails> ownerDetailsList = propertyTransferDetails.getOwnerDetails();
        	
        	NewPropertyDetails newPropertyDetails = transferOwnerService.createPropertyMutation(assessmentNo, mutationReasonCode,
        			saleDetails, deedNo, deedDate, ownerDetailsList);
        	
        	responseJson = JsonConvertor.convert(newPropertyDetails);
        }
		
		return responseJson;
	}
	
	/**
     * This method is used to get POJO object from JSON request.
     * 
     * @param jsonString - request JSON string
     * @return
     * @throws JsonParseException
     * @throws JsonMappingException
     * @throws IOException
     */
    private Object getObjectFromJSONRequest(String jsonString, Class cls)
            throws JsonParseException, JsonMappingException, IOException {
        ObjectMapper mapper = new ObjectMapper();
        mapper.setVisibility(JsonMethod.FIELD, Visibility.ANY);
        mapper.configure(SerializationConfig.Feature.AUTO_DETECT_FIELDS, true);
        mapper.setDateFormat(ChequePayment.CHEQUE_DATE_FORMAT);
        return mapper.readValue(jsonString, cls);
    }
    
}
