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

package org.egov.ptis.domain.service.property;

import static org.apache.commons.lang3.StringUtils.defaultString;
import static org.egov.infra.utils.ApplicationConstant.CITY_CODE_KEY;
import static org.egov.infra.utils.ApplicationConstant.CITY_DIST_NAME_KEY;
import static org.egov.infra.utils.ApplicationConstant.CITY_NAME_KEY;
import static org.egov.infra.utils.ApplicationConstant.CITY_REGION_NAME_KEY;
import static org.egov.ptis.constants.PropertyTaxConstants.STATUS_CANCELLED;
import static org.egov.ptis.constants.PropertyTaxConstants.STATUS_DEMAND_INACTIVE;
import static org.egov.ptis.constants.PropertyTaxConstants.WF_STATE_CLOSED;
import static org.egov.ptis.constants.PropertyTaxConstants.WF_STATE_COMMISSIONER_APPROVED;
import static org.egov.ptis.constants.PropertyTaxConstants.WF_STATE_REJECTED;

import java.math.BigDecimal;
import java.util.Date;
import java.util.Map;

import org.apache.commons.lang3.StringUtils;
import org.egov.eis.service.AssignmentService;
import org.egov.infra.admin.master.entity.User;
import org.egov.infra.admin.master.service.CityService;
import org.egov.infra.config.core.ApplicationThreadLocals;
import org.egov.ptis.constants.PropertyTaxConstants;
import org.egov.ptis.domain.entity.es.PTGISIndex;
import org.egov.ptis.domain.entity.property.BasicProperty;
import org.egov.ptis.domain.entity.property.BasicPropertyImpl;
import org.egov.ptis.domain.entity.property.PropertyID;
import org.egov.ptis.domain.entity.property.PropertyImpl;
import org.egov.ptis.domain.entity.property.view.SurveyBean;
import org.egov.ptis.repository.es.PTGISIndexRepository;
import org.egov.ptis.service.utils.PropertyTaxCommonUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional(readOnly = true)

public class PropertySurveyService {

    @Autowired
    private PTGISIndexRepository surveyRepository;
    @Autowired
    private CityService cityService;
    @Autowired
    private PropertyTaxCommonUtils propertyTaxCommonUtils;
    @Autowired
    protected AssignmentService assignmentService;
    @Autowired
    private PropertyService propService;

    private static final String RI_APPROVED = "UD Revenue Inspector Approved";
    private static final String ASSISTANT_APPROVED = "Assistant Approved";

    @Transactional
    public void updateSurveyIndex(final String applicationType, final SurveyBean surveyBean) {
        if (!applicationType.isEmpty() && propService.propertyApplicationTypes().contains(applicationType)) {
            updateIndex(applicationType, surveyBean);
        }
    }

    @Transactional
    public void updateIndex(final String applicationType, final SurveyBean surveyBean) {
        PTGISIndex ptGisIndex = findByApplicationNo(surveyBean.getProperty().getApplicationNo());

        if (ptGisIndex == null) {
            createPropertySurveyindex(applicationType, surveyBean);
        } else {
            updatePropertySurveyIndex(surveyBean, ptGisIndex);
        }

    }

    @Transactional
    public PTGISIndex createPropertySurveyindex(final String applicationType, final SurveyBean surveyBean) {
        PTGISIndex ptGisIndex = new PTGISIndex();
        final String state;
        state = surveyBean.getProperty().getState().getValue();
        final Date applicationDate = surveyBean.getProperty().getCreatedDate() == null ? new Date()
                : surveyBean.getProperty().getCreatedDate();
        final Date completionDate = state.contains(PropertyTaxConstants.WF_STATE_CLOSED)
                ? surveyBean.getProperty().getState().getLastModifiedDate() : null;
        final String source = propertyTaxCommonUtils.getPropertySource(surveyBean.getProperty());
        final String assessmentNo = StringUtils.isBlank(surveyBean.getProperty().getBasicProperty().getUpicNo())
                ? StringUtils.EMPTY
                : surveyBean.getProperty().getBasicProperty().getUpicNo();

        final PropertyID propId = surveyBean.getProperty().getBasicProperty().getPropertyID();
        final BasicPropertyImpl basicProp = (BasicPropertyImpl) surveyBean.getProperty().getBasicProperty();
        final boolean isApproved = state.contains(WF_STATE_COMMISSIONER_APPROVED) ? true : false;
        final boolean isCancelled = (state.contains(WF_STATE_REJECTED)
                && surveyBean.getProperty().getStatus().equals(STATUS_CANCELLED)) ? true : false;

        ptGisIndex = PTGISIndex.builder().withApplicationNo(surveyBean.getProperty().getApplicationNo())
                .withApplicationdate(applicationDate)
                .withApplicationStatus(state).withApplicationType(applicationType)
                .withAssessmentNo(assessmentNo)
                .withSource(source)
                .withRevenueWard(propId.getWard().getName())
                .withGisTax(surveyBean.getGisTax().doubleValue()).withApplicationTax(surveyBean.getApplicationTax().doubleValue())
                .withSystemTax(surveyBean.getSystemTax().doubleValue()).withApprovedTax(surveyBean.getApprovedTax().doubleValue())
                .withAgeOfCompletion(surveyBean.getAgeOfCompletion())
                .withCompletionDate(completionDate)
                .withIsApproved(isApproved)
                .withIsCancelled(isCancelled)
                .withThirdPrtyFlag(false).withAgeOfCompletion(surveyBean.getAgeOfCompletion())
                .withElectionWard(propId.getElectionBoundary().getName())
                .withBlockName(propId.getArea().getName())
                .withLocalityName(propId.getLocality().getName())
                .withDoorNo(basicProp.getAddress().getHouseNoBldgApt())
                .withAssistantName(surveyBean.getProperty().getState().getValue()
                        .endsWith(PropertyTaxConstants.WF_STATE_ASSISTANT_APPROVED)
                                ? surveyBean.getProperty().getState().getLastModifiedBy().getName() : null)
                .withRiName(surveyBean.getProperty().getState().getValue()
                        .endsWith(PropertyTaxConstants.WF_STATE_UD_REVENUE_INSPECTOR_APPROVED)
                                ? surveyBean.getProperty().getState().getLastModifiedBy().getName() : null)
                .withSentToThirdParty(false)
                .withAssistantName(null)
                .withRiName(null)
                .withFunctionaryName(getFunctionaryDetail(surveyBean))
                .build();
        return createPTGISIndex(ptGisIndex);
    }

    private String getFunctionaryDetail(final SurveyBean surveyBean) {
        final User functionary = propService.getOwnerName(surveyBean.getProperty());
        return functionary.getUsername().concat("::").concat(functionary.getName().trim());
    } 

    private Double calculatetaxvariance(final String applicationType, final SurveyBean surveyBean, final PTGISIndex index) {
        Double taxVar;
        BigDecimal sysTax = index.getSystemTax() == null ? surveyBean.getSystemTax() : new BigDecimal(index.getSystemTax());

        if (applicationType.equalsIgnoreCase(PropertyTaxConstants.APPLICATION_TYPE_ALTER_ASSESSENT)
                && sysTax.compareTo(BigDecimal.ZERO) > 0)
            taxVar = ((BigDecimal.valueOf(index.getApprovedTax()).subtract(sysTax)).multiply(BigDecimal.valueOf(100.0)))
                    .divide(sysTax, BigDecimal.ROUND_HALF_UP).doubleValue();
        else
            taxVar = Double.valueOf(100);
        return taxVar;
    }

    @Transactional
    public PTGISIndex updatePropertySurveyIndex(final SurveyBean surveyBean,
            final PTGISIndex ptGisIndex) {

        String stateValue = surveyBean.getProperty().getState().getValue();
        String riName = stateValue.endsWith(PropertyTaxConstants.WF_STATE_UD_REVENUE_INSPECTOR_APPROVED)
                ? surveyBean.getProperty().getState().getLastModifiedBy().getName() : ptGisIndex.getRiName();
        String assistantName = stateValue.endsWith(PropertyTaxConstants.WF_STATE_ASSISTANT_APPROVED)
                ? surveyBean.getProperty().getState().getLastModifiedBy().getName() : ptGisIndex.getAssistantName();
        updateApplicationDetails(surveyBean, ptGisIndex);

        ptGisIndex.setAssistantName(assistantName);
        ptGisIndex.setRiName(riName);
        return updatePTGISIndex(ptGisIndex);
    }

    private void updateApplicationDetails(final SurveyBean surveyBean, final PTGISIndex ptGisIndex) {
        PropertyImpl propertyImpl = surveyBean.getProperty();
        BasicProperty basicProperty = propertyImpl.getBasicProperty();
        String stateValue = propertyImpl.getState().getValue();
        String doorNo = basicProperty.getAddress().getHouseNoBldgApt();

        if (stateValue.endsWith(ASSISTANT_APPROVED) || stateValue.endsWith(RI_APPROVED)) {
            ptGisIndex.setApplicationTax(surveyBean.getApplicationTax().doubleValue());
        }

        if (stateValue.endsWith(WF_STATE_COMMISSIONER_APPROVED)) {
            ptGisIndex.setApprovedTax(ptGisIndex.getApplicationTax());
            ptGisIndex.setIsApproved(true);
            ptGisIndex.setTaxVariance(calculatetaxvariance(ptGisIndex.getApplicationType(), surveyBean, ptGisIndex));
        }

        if (stateValue.endsWith(WF_STATE_CLOSED) && propertyImpl.getStatus().equals(STATUS_DEMAND_INACTIVE)) {
            ptGisIndex.setCompletionDate(propertyImpl.getState().getLastModifiedDate());
            ptGisIndex.setIsApproved(true);
        }

        ptGisIndex.setApplicationStatus(stateValue);
        ptGisIndex.setAssessmentNo(StringUtils.isBlank(basicProperty.getUpicNo())
                ? StringUtils.EMPTY
                : basicProperty.getUpicNo());

        ptGisIndex.setIsCancelled(
                (stateValue.contains(WF_STATE_CLOSED) && propertyImpl.getStatus().equals(STATUS_CANCELLED)) ? true : false);
        ptGisIndex.setThirdPrtyFlag(propertyImpl.isThirdPartyVerified());
        ptGisIndex.setDoorNo(doorNo == null ? ptGisIndex.getDoorNo() : doorNo);
        ptGisIndex.setSentToThirdParty(propertyImpl.isSentToThirdParty());
        ptGisIndex.setFunctionaryName(getFunctionaryDetail(surveyBean));
    }

    @Transactional
    public PTGISIndex createPTGISIndex(PTGISIndex surveyIndex) {
        Map<String, Object> cityInfo = cityService.cityDataAsMap();
        surveyIndex.setCityCode(defaultString((String) cityInfo.get(CITY_CODE_KEY)));
        surveyIndex.setCityName(defaultString((String) cityInfo.get(CITY_NAME_KEY)));
        surveyIndex.setRegionName(defaultString((String) cityInfo.get(CITY_REGION_NAME_KEY)));
        surveyIndex.setDistrictName(defaultString((String) cityInfo.get(CITY_DIST_NAME_KEY)));
        surveyRepository.save(surveyIndex);
        return surveyIndex;
    }

    @Transactional
    public PTGISIndex updatePTGISIndex(PTGISIndex surveyIndex) {
        surveyRepository.save(surveyIndex);
        return surveyIndex;
    }

    public PTGISIndex findByApplicationNo(String applicationNo) {
        return surveyRepository.findByApplicationNoAndCityCode(applicationNo, ApplicationThreadLocals.getCityCode());

    }

}