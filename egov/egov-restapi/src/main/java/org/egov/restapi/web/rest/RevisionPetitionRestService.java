/**
 * eGov suite of products aim to improve the internal efficiency,transparency, 
   accountability and the service delivery of the government  organizations.

    Copyright (C) <2015>  eGovernments Foundation

    The updated version of eGov suite of products as by eGovernments Foundation 
    is available at http://www.egovernments.org

    This program is free software: you can redistribute it and/or modify
    it under the terms of the GNU General Public License as published by
    the Free Software Foundation, either version 3 of the License, or
    any later version.

    This program is distributed in the hope that it will be useful,
    but WITHOUT ANY WARRANTY; without even the implied warranty of
    MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
    GNU General Public License for more details.

    You should have received a copy of the GNU General Public License
    along with this program. If not, see http://www.gnu.org/licenses/ or 
    http://www.gnu.org/licenses/gpl.html .

    In addition to the terms of the GPL license to be adhered to in using this
    program, the following additional terms are to be complied with:

	1) All versions of this program, verbatim or modified must carry this 
	   Legal Notice.

	2) Any misrepresentation of the origin of the material is prohibited. It 
	   is required that all modified versions of this material be marked in 
	   reasonable ways as different from the original version.

	3) This license does not grant any rights to any user of the program 
	   with regards to rights under trademark law for use of the trade names 
	   or trademarks of eGovernments Foundation.

  In case of any queries, you can reach eGovernments Foundation at contact@egovernments.org.
 */
package org.egov.restapi.web.rest;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import org.codehaus.jackson.JsonGenerationException;
import org.codehaus.jackson.annotate.JsonAutoDetect.Visibility;
import org.codehaus.jackson.annotate.JsonMethod;
import org.codehaus.jackson.map.JsonMappingException;
import org.codehaus.jackson.map.ObjectMapper;
import org.codehaus.jackson.map.SerializationConfig;
import org.egov.infra.utils.EgovThreadLocals;
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
import org.springframework.web.bind.annotation.RestController;

import javax.ws.rs.FormParam;
import javax.ws.rs.core.MediaType;
import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;

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
    String USER_NAME = "mahesh";
    String PASSWORD = "demo";
    String LOGIN_USERID = "16";

    /**
     * This method is used for handling user request for revision petition
     * details.
     * 
     * @param applicationNumber
     *            - applicationNumber number i.e. revision peatition application
     *            number
     * @return
     * @throws JsonGenerationException
     * @throws JsonMappingException
     * @throws IOException
     */
    @RequestMapping(value = "/property/revisionPetition", method = RequestMethod.POST, produces = MediaType.APPLICATION_JSON)
    public String getRevisionPetitionDetails(@FormParam("applicationNo") String applicationNo)
            throws JsonGenerationException, JsonMappingException, IOException {
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
     * @param accessmentnumber
     * @param username
     * @param password
     * @param details
     * @param receivedon
     * @param recievedBy
     * @return
     * @throws JsonGenerationException
     * @throws JsonMappingException
     * @throws IOException
     */
	@RequestMapping(value = "/property/createRevisionPetition", method = RequestMethod.POST, consumes = MediaType.APPLICATION_JSON, produces = MediaType.APPLICATION_JSON)
	public String createRevisionPetitionFromRest(@RequestBody String createRevionPetitionDetails)
			throws JsonGenerationException, JsonMappingException, IOException {
		String responseJson = new String();
		EgovThreadLocals.setUserId(Long.valueOf(LOGIN_USERID));
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
				e.printStackTrace();
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

    private String getJSONResponse(Object obj) throws JsonGenerationException, JsonMappingException, IOException {
        ObjectMapper objectMapper = new ObjectMapper();
        objectMapper.setVisibility(JsonMethod.FIELD, Visibility.ANY);
        String jsonResponse = objectMapper.writeValueAsString(obj);
        return jsonResponse;
    }

    /**
     * This method is used to get the error details for invalid credentials.
     * 
     * @return
     */
    private ErrorDetails getInvalidCredentialsErrorDetails() {
        ErrorDetails errorDetails = new ErrorDetails();
        errorDetails.setErrorCode(PropertyTaxConstants.THIRD_PARTY_ERR_CODE_INVALIDCREDENTIALS);
        errorDetails.setErrorMessage(PropertyTaxConstants.THIRD_PARTY_ERR_MSG_INVALIDCREDENTIALS);
        return errorDetails;
    }

    public RevisionPetitionService getRevisionPetitionService() {
        return revisionPetitionService;
    }

    public void setRevisionPetitionService(RevisionPetitionService revisionPetitionService) {
        this.revisionPetitionService = revisionPetitionService;
    }

    public Boolean authenticateUser(String username, String password) {
        Boolean isAuthenticated = false;

        if (username != null && password != null && username.equals(USER_NAME) && password.equals(PASSWORD)) {
            isAuthenticated = true;
        }
        return isAuthenticated;
    }

}