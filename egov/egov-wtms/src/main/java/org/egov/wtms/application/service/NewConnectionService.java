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
package org.egov.wtms.application.service;

import static org.apache.commons.lang.StringUtils.isNotBlank;
import static org.egov.wtms.masters.entity.enums.ConnectionStatus.ACTIVE;
import static org.egov.wtms.masters.entity.enums.ConnectionStatus.CLOSED;
import static org.egov.wtms.masters.entity.enums.ConnectionStatus.DISCONNECTED;
import static org.egov.wtms.masters.entity.enums.ConnectionStatus.HOLDING;
import static org.egov.wtms.masters.entity.enums.ConnectionStatus.INPROGRESS;
import static org.egov.wtms.utils.constants.WaterTaxConstants.CATEGORY_BPL;
import static org.egov.wtms.utils.constants.WaterTaxConstants.NEWCONNECTION;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Iterator;
import java.util.List;

import org.apache.commons.lang3.ArrayUtils;
import org.egov.ptis.domain.model.AssessmentDetails;
import org.egov.ptis.domain.model.enums.BasicPropertyStatus;
import org.egov.ptis.domain.service.property.PropertyExternalService;
import org.egov.wtms.application.entity.ApplicationDocuments;
import org.egov.wtms.application.entity.WaterConnection;
import org.egov.wtms.application.entity.WaterConnectionDetails;
import org.egov.wtms.masters.entity.ConnectionCategory;
import org.egov.wtms.masters.entity.enums.ConnectionType;
import org.egov.wtms.masters.service.ConnectionCategoryService;
import org.egov.wtms.utils.PropertyExtnUtils;
import org.egov.wtms.utils.WaterTaxUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.MessageSource;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.validation.BindingResult;
import org.springframework.web.multipart.MultipartFile;

@Service
@Transactional(readOnly = true)
public class NewConnectionService {

    private static final String ERROR_REQUIRED = "err.required";
    private static final String CONNECTION_PROPERTYID = "connection.propertyIdentifier";

    @Autowired
    private WaterConnectionDetailsService waterConnectionDetailsService;

    @Autowired
    @Qualifier("parentMessageSource")
    private MessageSource wcmsMessageSource;

    @Autowired
    private WaterTaxUtils waterTaxUtils;

    @Autowired
    private PropertyExtnUtils propertyExtnUtils;

    @Autowired
    private ConnectionDetailService connectionDetailService;

    @Autowired
    private ConnectionCategoryService connectionCategoryService;

    @Autowired
    private WaterConnectionService waterConnectionService;

    public String checkConnectionPresentForProperty(final String propertyID) {
        String validationMessage = "";
        /**
         * Validate only if configuration value is 'NO' for multiple new connection per property allowed or not. If configuration
         * value is 'YES' then multiple new connections are allowed. This will impact on the Additional connection feature.
         **/
        if (!waterTaxUtils.isMultipleNewConnectionAllowedForPID()) {
            final WaterConnectionDetails waterConnectionDetails = waterConnectionDetailsService
                    .getPrimaryConnectionDetailsByPropertyIdentifier(propertyID);
            if (waterConnectionDetails == null)
                return validationMessage;
            else if (ACTIVE.equals(waterConnectionDetails.getConnectionStatus()))
                validationMessage = wcmsMessageSource.getMessage("err.validate.newconnection.active", new String[] {
                        waterConnectionDetails.getConnection().getConsumerCode(), propertyID }, null);
            else if (INPROGRESS.equals(waterConnectionDetails.getConnectionStatus()))
                validationMessage = wcmsMessageSource.getMessage("err.validate.newconnection.application.inprocess",
                        new String[] { propertyID, waterConnectionDetails.getApplicationNumber() }, null);
            else if (DISCONNECTED.equals(waterConnectionDetails.getConnectionStatus()))
                validationMessage = wcmsMessageSource
                        .getMessage("err.validate.newconnection.disconnected", new String[] {
                                waterConnectionDetails.getConnection().getConsumerCode(), propertyID }, null);
            else if (CLOSED.equals(waterConnectionDetails.getConnectionStatus()))
                validationMessage = wcmsMessageSource
                        .getMessage("err.validate.newconnection.closed", new String[] {
                                waterConnectionDetails.getConnection().getConsumerCode(), propertyID }, null);
            else if (HOLDING.equals(waterConnectionDetails.getConnectionStatus()))
                validationMessage = wcmsMessageSource.getMessage("err.validate.newconnection.holding", new String[] {
                        waterConnectionDetails.getConnection().getConsumerCode(), propertyID }, null);
        }
        return validationMessage;
    }

    public String checkValidPropertyAssessmentNumber(final String asessmentNumber) {
        String errorMessage = "";
        final AssessmentDetails assessmentDetails = propertyExtnUtils.getAssessmentDetailsForFlag(asessmentNumber,
                PropertyExternalService.FLAG_FULL_DETAILS, BasicPropertyStatus.ACTIVE);
        errorMessage = validateProperty(assessmentDetails);
        if (errorMessage.isEmpty())
            errorMessage = validatePTDue(asessmentNumber, assessmentDetails);
        return errorMessage;
    }

    public String checkValidPropertyForDataEntry(final String asessmentNumber) {
        String errorMessage = "";
        final AssessmentDetails assessmentDetails = propertyExtnUtils.getAssessmentDetailsForFlag(asessmentNumber,
                PropertyExternalService.FLAG_FULL_DETAILS, BasicPropertyStatus.ACTIVE);
        errorMessage = validateProperty(assessmentDetails);
        return errorMessage;
    }

    /**
     * @param assessmentDetails
     * @return ErrorMessage If PropertyId is Not Valid
     */
    private String validateProperty(final AssessmentDetails assessmentDetails) {
        String errorMessage = "";
        if (assessmentDetails.getErrorDetails() != null && assessmentDetails.getErrorDetails().getErrorCode() != null)
            errorMessage = assessmentDetails.getErrorDetails().getErrorMessage();
        return errorMessage;
    }

    private String validatePTDue(final String asessmentNumber, final AssessmentDetails assessmentDetails) {
        String errorMessage = "";
        if (assessmentDetails.getPropertyDetails() != null
                && assessmentDetails.getPropertyDetails().getTaxDue() != null
                && assessmentDetails.getPropertyDetails().getTaxDue().doubleValue() > 0
                && !waterTaxUtils.isNewConnectionAllowedIfPTDuePresent())

            /**
             * If property tax due present and configuration value is 'NO' then restrict not to allow new water tap connection
             * application. If configuration value is 'YES' then new water tap connection can be created even though there is
             * Property Tax Due present.
             **/
            errorMessage = wcmsMessageSource.getMessage("err.validate.property.taxdue", new String[] {
                    assessmentDetails.getPropertyDetails().getTaxDue().toString(), asessmentNumber, "new" }, null);
        return errorMessage;
    }

    public void validateDocuments(final List<ApplicationDocuments> applicationDocs,
            final ApplicationDocuments applicationDocument, final int i, final BindingResult resultBinder,
            final Long categoryId, final String documentRequired) {

        final ConnectionCategory connectionCategory = connectionCategoryService.findOne(categoryId);
        if (connectionCategory != null && documentRequired != null
                && connectionCategory.getCode().equalsIgnoreCase(CATEGORY_BPL)
                && documentRequired.equalsIgnoreCase(applicationDocument.getDocumentNames().getDocumentName()))
            validateDocumentsForBPLCategory(applicationDocs, applicationDocument, resultBinder, i);
        else {
            validateDocumentsRequired(applicationDocument, resultBinder, i);
            if (connectionDetailService.validApplicationDocument(applicationDocument))
                applicationDocs.add(applicationDocument);
        }
    }

    public void validateDocumentsRequired(final ApplicationDocuments applicationDocument, final BindingResult resultBinder,
            final int i) {
        if (applicationDocument.getDocumentNumber() == null && applicationDocument.getDocumentDate() != null) {
            final String fieldError = "applicationDocs[" + i + "].documentNumber";
            resultBinder.rejectValue(fieldError, "documentNumber.required");
        }
        if (applicationDocument.getDocumentNumber() != null && applicationDocument.getDocumentDate() == null) {
            final String fieldError = "applicationDocs[" + i + "].documentDate";
            resultBinder.rejectValue(fieldError, "documentDate.required");
        }
        if (applicationDocument.getDocumentNumber() != null && applicationDocument.getDocumentDate() != null) {
            Iterator<MultipartFile> stream = null;
            if (ArrayUtils.isNotEmpty(applicationDocument.getFiles()))
                stream = Arrays.asList(applicationDocument.getFiles()).stream().filter(file -> !file.isEmpty())
                        .iterator();
            if (stream == null) {
                final String fieldError = "applicationDocs[" + i + "].files";
                resultBinder.rejectValue(fieldError, "files.required");
            }
        }
    }

    public void validateDocumentsForBPLCategory(final List<ApplicationDocuments> applicationDocs,
            final ApplicationDocuments applicationDocument, final BindingResult resultBinder, final int i) {
        if (applicationDocument.getDocumentNumber() == null) {
            final String fieldError = "applicationDocs[" + i + "].documentNumber";
            resultBinder.rejectValue(fieldError, "documentNumber.required");
        }
        if (applicationDocument.getDocumentDate() == null) {
            final String fieldError = "applicationDocs[" + i + "].documentDate";
            resultBinder.rejectValue(fieldError, "documentDate.required");
        }

        Iterator<MultipartFile> stream;
        if (ArrayUtils.isNotEmpty(applicationDocument.getFiles())) {
            stream = Arrays.asList(applicationDocument.getFiles()).stream().filter(file -> !file.isEmpty())
                    .iterator();

            if (stream != null && connectionDetailService.validApplicationDocument(applicationDocument))
                applicationDocs.add(applicationDocument);
        } else {
            final String fieldError = "applicationDocs[" + i + "].files";
            resultBinder.rejectValue(fieldError, "files.required");
        }
    }

    public void validateExisting(final WaterConnectionDetails waterConnectionDetails, final BindingResult errors) {
        if (waterConnectionDetails.getConnection() != null) {
            List<WaterConnection> waterConnectionList = new ArrayList<>();
            if (waterConnectionDetails.getConnection().getOldConsumerNumber() != null)
                waterConnectionList = waterConnectionService
                        .findByOldConsumerNumber(waterConnectionDetails.getConnection().getOldConsumerNumber());
            if (!waterConnectionList.isEmpty())
                errors.rejectValue("connection.oldConsumerNumber", "err.oldConsumerCode.already.exists");
        }
        if (waterConnectionDetails.getConnectionType() != null
                && waterConnectionDetails.getConnectionType() == ConnectionType.METERED)
            validateMeterConnectionDetails(waterConnectionDetails, errors);
    }

    public void validateMeterConnectionDetails(final WaterConnectionDetails waterConnectionDetails, final BindingResult errors) {
        if (waterConnectionDetails.getExecutionDate() == null)
            errors.rejectValue("executionDate", ERROR_REQUIRED);
        if (waterConnectionDetails.getExistingConnection().getMeterNo() == null)
            errors.rejectValue("existingConnection.meterNo", ERROR_REQUIRED);
        if (waterConnectionDetails.getExistingConnection().getPreviousReading() == null)
            errors.rejectValue("existingConnection.previousReading", ERROR_REQUIRED);
        if (waterConnectionDetails.getExistingConnection().getReadingDate() == null)
            errors.rejectValue("existingConnection.readingDate", ERROR_REQUIRED);
        if (waterConnectionDetails.getExistingConnection().getCurrentReading() == null)
            errors.rejectValue("existingConnection.currentReading", ERROR_REQUIRED);
    }

    public void validatePropertyIDForDataEntry(final WaterConnectionDetails waterConnectionDetails,
            final BindingResult errors) {
        if (waterConnectionDetails.getConnection() != null
                && waterConnectionDetails.getConnection().getPropertyIdentifier() != null
                && !"".equals(waterConnectionDetails.getConnection().getPropertyIdentifier())) {
            String errorMessage = checkValidPropertyForDataEntry(waterConnectionDetails.getConnection().getPropertyIdentifier());
            if (errorMessage != null && !"".equals(errorMessage))
                errors.rejectValue(CONNECTION_PROPERTYID, errorMessage, errorMessage);
            if (waterConnectionDetails.getId() == null && waterConnectionDetails.getApplicationType().getCode()
                    .equalsIgnoreCase(NEWCONNECTION))
                errorMessage = checkConnectionPresentForProperty(
                        waterConnectionDetails.getConnection().getPropertyIdentifier());
            if (errorMessage != null && !"".equals(errorMessage))
                errors.rejectValue(CONNECTION_PROPERTYID, errorMessage, errorMessage);
        }
    }

    public void validatePropertyID(final WaterConnectionDetails waterConnectionDetails, final BindingResult errors) {
        if (waterConnectionDetails.getConnection() != null
                && isNotBlank(waterConnectionDetails.getConnection().getPropertyIdentifier())) {
            String errorMessage = checkValidPropertyAssessmentNumber(
                    waterConnectionDetails.getConnection().getPropertyIdentifier());
            if (isNotBlank(errorMessage))
                errors.rejectValue(CONNECTION_PROPERTYID, errorMessage, errorMessage);
            else {
                errorMessage = checkConnectionPresentForProperty(
                        waterConnectionDetails.getConnection().getPropertyIdentifier());
                if (isNotBlank(errorMessage))
                    errors.rejectValue(CONNECTION_PROPERTYID, errorMessage, errorMessage);
            }
        }
    }

}
