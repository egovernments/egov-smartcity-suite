/*
 *    eGov  SmartCity eGovernance suite aims to improve the internal efficiency,transparency,
 *    accountability and the service delivery of the government  organizations.
 *
 *     Copyright (C) 2017  eGovernments Foundation
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
 *            Further, all user interfaces, including but not limited to citizen facing interfaces,
 *            Urban Local Bodies interfaces, dashboards, mobile applications, of the program and any
 *            derived works should carry eGovernments Foundation logo on the top right corner.
 *
 *            For the logo, please refer http://egovernments.org/html/logo/egov_logo.png.
 *            For any further queries on attribution, including queries on brand guidelines,
 *            please contact contact@egovernments.org
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
 *
 */
package org.egov.restapi.web.rest;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import org.codehaus.jackson.annotate.JsonAutoDetect.Visibility;
import org.codehaus.jackson.annotate.JsonMethod;
import org.codehaus.jackson.map.ObjectMapper;
import org.codehaus.jackson.map.SerializationConfig;
import org.egov.infra.config.core.ApplicationThreadLocals;
import org.egov.ptis.constants.PropertyTaxConstants;
import org.egov.ptis.domain.dao.property.BasicPropertyDAO;
import org.egov.ptis.domain.entity.objection.RevisionPetition;
import org.egov.ptis.domain.entity.property.BasicProperty;
import org.egov.ptis.domain.model.ErrorDetails;
import org.egov.ptis.domain.service.revisionPetition.RevisionPetitionService;
import org.egov.restapi.model.CreateRevisionPetitionDetails;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;

import static org.springframework.http.MediaType.APPLICATION_JSON_VALUE;

/**
 * The AssessmentService class is used as the RESTFul service to handle user
 * request and response.
 * 
 * @author Pradeep
 */
@RestController
public class RevisionPetitionRestService {
    @Autowired
    private RevisionPetitionService revisionPetitionService;
    @Autowired
    private BasicPropertyDAO basicPropertyDAO;

    String LOGIN_USERID = "16";

    /**
     * This method is used for handling user request for revision petition
     * details.
     * 
     * @param applicationNo
     * @return
     * @throws IOException
     */
    @RequestMapping(value = "/property/revisionPetition", method = RequestMethod.POST, produces = APPLICATION_JSON_VALUE)
    public String getRevisionPetitionDetails(@RequestParam String applicationNo)
            throws IOException {
        RevisionPetition revisionPetition = revisionPetitionService.getRevisionPetitionByApplicationNumber(applicationNo);
        if (revisionPetition != null)
            return convertRevisionPetitionObjectToJson(revisionPetition);
        else {
            ErrorDetails errorDetails = new ErrorDetails();
            errorDetails.setErrorCode(PropertyTaxConstants.THIRD_PARTY_ERR_CODE_REVISIONPETITION_INVALID);
            errorDetails.setErrorMessage(PropertyTaxConstants.THIRD_PARTY_ERR_MSG_REVISIONPETITION_INVALID);
            return getJSONResponse(errorDetails);
        }

    }

    /**
     * @param createRevionPetitionDetails
     * @return
     * @throws IOException
     */
	@RequestMapping(value = "/property/createRevisionPetition", method = RequestMethod.POST, consumes = APPLICATION_JSON_VALUE, produces = APPLICATION_JSON_VALUE)
	public String createRevisionPetitionFromRest(@RequestBody String createRevionPetitionDetails)
			throws IOException {
		String responseJson = new String();
        //FIXME hardcode user id ?
		ApplicationThreadLocals.setUserId(Long.valueOf(LOGIN_USERID));
		ObjectMapper mapper = new ObjectMapper();
		mapper.setVisibility(JsonMethod.FIELD, Visibility.ANY);
		mapper.configure(SerializationConfig.Feature.AUTO_DETECT_FIELDS, true);
		CreateRevisionPetitionDetails revionPetitionDetails = mapper.readValue(createRevionPetitionDetails,
				CreateRevisionPetitionDetails.class);
		String assessmentNo = revionPetitionDetails.getAssessmentNo();
		String details = revionPetitionDetails.getDetails();
		String receivedOn = revionPetitionDetails.getReceivedOn();
		String receivedBy = revionPetitionDetails.getReceivedBy();

		ErrorDetails errorDetails = validateRevisionPetitionForm(assessmentNo, details, receivedOn, receivedBy);
		if (null != errorDetails) {
			responseJson = getJSONResponse(errorDetails);
		} else {
			try {
				BasicProperty basicProperty = basicPropertyDAO.getBasicPropertyByPropertyID(assessmentNo);
				if (basicProperty != null && basicProperty.isUnderWorkflow()) {
					errorDetails = new ErrorDetails();
					errorDetails
							.setErrorCode(PropertyTaxConstants.THIRD_PARTY_ERR_CODE_REVISIONPETITION_ALREADYINWORKFLOW);
					errorDetails.setErrorMessage(
							PropertyTaxConstants.THIRD_PARTY_ERR_MSG_REVISIONPETITION_ALREADYINWORKFLOW);
					return getJSONResponse(errorDetails);
				}
				RevisionPetition revPetition = new RevisionPetition();
				revPetition.setBasicProperty(basicProperty);
				revPetition.setRecievedBy(receivedBy);
				revPetition.setDetails(details);
				SimpleDateFormat sdf = new SimpleDateFormat("dd-MM-yyyy");
				revPetition.setRecievedOn(sdf.parse(receivedOn));
				responseJson = convertRevisionPetitionObjectToJson(
						revisionPetitionService.createRevisionPetitionForRest(revPetition));
			} catch (ParseException e) {

			}

		}
		return responseJson;
	}

    /**
     * @param accessmentnumber
     * @param details
     * @param receivedon
     * @param recievedBy
     * @return
     */
    private ErrorDetails validateRevisionPetitionForm(String accessmentnumber, String details, String receivedon,
            String recievedBy) {
        ErrorDetails errorDetails = null;
        if (accessmentnumber == null || accessmentnumber.trim().length() == 0) {
            errorDetails = new ErrorDetails();
            errorDetails.setErrorCode(PropertyTaxConstants.THIRD_PARTY_ERR_CODE_ASSESSMENT_NO_REQUIRED);
            errorDetails.setErrorMessage(PropertyTaxConstants.THIRD_PARTY_ERR_MSG_ASSESSMENT_NO_REQUIRED);
        } else {
            if (accessmentnumber.trim().length() > 0 && accessmentnumber.trim().length() < 10) {
                errorDetails = new ErrorDetails();
                errorDetails.setErrorCode(PropertyTaxConstants.THIRD_PARTY_ERR_CODE_ASSESSMENT_NO_LEN);
                errorDetails.setErrorMessage(PropertyTaxConstants.THIRD_PARTY_ERR_MSG_ASSESSMENT_NO_LEN);
            }
            if (!basicPropertyDAO.isAssessmentNoExist(accessmentnumber)) {
                errorDetails = new ErrorDetails();
                errorDetails.setErrorCode(PropertyTaxConstants.THIRD_PARTY_ERR_CODE_ASSESSMENT_NO_NOT_FOUND);
                errorDetails.setErrorMessage(PropertyTaxConstants.THIRD_PARTY_ERR_MSG_ASSESSMENT_NO_NOT_FOUND);
            }
        }

        if (receivedon == null || receivedon.trim().length() == 0) {
            errorDetails = new ErrorDetails();
            errorDetails.setErrorCode(PropertyTaxConstants.THIRD_PARTY_ERR_CODE_REVISIONPETITION_RECEIVEDON);
            errorDetails.setErrorMessage(PropertyTaxConstants.THIRD_PARTY_ERR_MSG_REVISIONPETITION_RECEIVEDON);
        } else if (recievedBy == null || recievedBy.trim().length() == 0) {
            errorDetails = new ErrorDetails();
            errorDetails.setErrorCode(PropertyTaxConstants.THIRD_PARTY_ERR_CODE_REVISIONPETITION_RECEIVEDBY);
            errorDetails.setErrorMessage(PropertyTaxConstants.THIRD_PARTY_ERR_MSG_REVISIONPETITION_RECEIVEDBY);
        } else if (details == null || details.trim().length() == 0) {
            errorDetails = new ErrorDetails();
            errorDetails.setErrorCode(PropertyTaxConstants.THIRD_PARTY_ERR_CODE_REVISIONPETITION_RECEIVEDDETAIL);
            errorDetails.setErrorMessage(PropertyTaxConstants.THIRD_PARTY_ERR_MSG_REVISIONPETITION_RECEIVEDDETAIL);
        }

        return errorDetails;
    }

    /**
     * @param object
     * @return
     */
    private String convertRevisionPetitionObjectToJson(final Object object) {
        GsonBuilder gsonBuilder = new GsonBuilder();
        Gson gson = gsonBuilder.registerTypeAdapter(RevisionPetition.class, new RevisionPetitionAdaptor()).create();
        String json = gson.toJson(object);
        return json;
    }

    private String getJSONResponse(Object obj) throws IOException {
        ObjectMapper objectMapper = new ObjectMapper();
        objectMapper.setVisibility(JsonMethod.FIELD, Visibility.ANY);
        String jsonResponse = objectMapper.writeValueAsString(obj);
        return jsonResponse;
    }
}