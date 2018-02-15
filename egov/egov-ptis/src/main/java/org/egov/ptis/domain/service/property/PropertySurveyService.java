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
import static org.egov.ptis.constants.PropertyTaxConstants.WF_STATE_COMMISSIONER_APPROVED;
import static org.egov.ptis.constants.PropertyTaxConstants.WF_STATE_REJECTED;

import java.math.BigDecimal;
import java.util.Date;
import java.util.Map;

import org.apache.commons.lang3.StringUtils;
import org.egov.eis.service.AssignmentService;
import org.egov.infra.admin.master.service.CityService;
import org.egov.infra.config.core.ApplicationThreadLocals;
import org.egov.ptis.constants.PropertyTaxConstants;
import org.egov.ptis.domain.entity.es.PTGISIndex;
import org.egov.ptis.domain.entity.property.BasicPropertyImpl;
import org.egov.ptis.domain.entity.property.PropertyID;
import org.egov.ptis.domain.entity.property.view.SurveyBean;
import org.egov.ptis.repository.es.PTGISIndexRepository;
import org.egov.ptis.service.utils.PropertyTaxCommonUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class PropertySurveyService {
    @Autowired
    private PTGISIndexRepository surveyRepository;

    @Autowired
    private CityService cityService;
    @Autowired
    private ApplicationContext beanProvider;

    @Autowired
    private PropertyTaxCommonUtils propertyTaxCommonUtils;
    @Autowired
    protected AssignmentService assignmentService;

    private static final String RI_APPROVED = "UD Revenue Inspector Approved";
    private static final String ASSISTANT_APPROVED = "Assistant Approved";

    public void updateSurveyIndex(final String applicationType, final SurveyBean surveyBean) {
        final PropertyService propService = beanProvider.getBean("propService", PropertyService.class);
        if (!applicationType.isEmpty() && propService.propertyApplicationTypes().contains(applicationType))
            updateIndex(applicationType, surveyBean);
    }
    @Transactional
    public void updateIndex(final String applicationType, final SurveyBean surveyBean) {
        PTGISIndex ptGisIndex = findByApplicationNo(surveyBean.getProperty().getApplicationNo());
        if (ptGisIndex == null)
            createPropertySurveyindex(applicationType, surveyBean);
        else
            updatePropertySurveyIndex(applicationType, surveyBean, ptGisIndex);

    }

    @Transactional
    public void createPropertySurveyindex(final String applicationType, final SurveyBean surveyBean) {
        PTGISIndex ptGisIndex;
        Double taxVar;
        final String state;
        taxVar = calculatetaxvariance(applicationType, surveyBean);
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
        final boolean isCancelled = state.contains(WF_STATE_REJECTED) ? true : false;

        ptGisIndex = PTGISIndex.builder().withApplicationNo(surveyBean.getProperty().getApplicationNo())
                .withApplicationdate(applicationDate)
                .withApplicationStatus(state).withApplicationType(applicationType)
                .withAssessmentNo(assessmentNo)
                .withSource(source)
                .withRevenueWard(propId.getWard().getName())
                .withGisTax(surveyBean.getGisTax().doubleValue()).withApplicationTax(surveyBean.getApplicationTax().doubleValue())
                .withSystemTax(surveyBean.getSystemTax().doubleValue()).withApprovedTax(surveyBean.getApprovedTax().doubleValue())
                .withTaxVariance(taxVar)
                .withAgeOfCompletion(surveyBean.getAgeOfCompletion()).withTaxVariance(0.0)
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
                .withToReferedToThrdPrty(false)
                .withAssistantName(null)
                .withRiName(null)
                .build();
        createPTGISIndex(ptGisIndex);
    }

    private Double calculatetaxvariance(final String applicationType, final SurveyBean surveyBean) {
        Double taxVar;
        if (applicationType.equalsIgnoreCase(PropertyTaxConstants.APPLICATION_TYPE_ALTER_ASSESSENT)
                && surveyBean.getSystemTax().compareTo(BigDecimal.ZERO) > 0)
            taxVar = ((surveyBean.getApprovedTax().subtract(surveyBean.getSystemTax())).multiply(BigDecimal.valueOf(100.0)))
                    .divide(surveyBean.getSystemTax(), BigDecimal.ROUND_HALF_UP).doubleValue();
        else
            taxVar = Double.valueOf(100);
        return taxVar;
    }
   @Transactional
    public void updatePropertySurveyIndex(final String applicationType, final SurveyBean surveyBean,
            final PTGISIndex ptGisIndex) {

        Double taxVar;
        final Date applicationDate = surveyBean.getProperty().getCreatedDate() == null ? new Date()
                : surveyBean.getProperty().getCreatedDate();
        String stateValue = surveyBean.getProperty().getState().getValue();
        final Date completionDate = stateValue.contains(PropertyTaxConstants.WF_STATE_CLOSED)
                ? surveyBean.getProperty().getState().getLastModifiedDate() : null;
        taxVar = calculateVariance(applicationType, surveyBean, ptGisIndex);

        Double applicationTax = stateValue.endsWith(ASSISTANT_APPROVED) || stateValue.endsWith(RI_APPROVED)
                ? surveyBean.getApplicationTax().doubleValue() : ptGisIndex.getApplicationTax();
        Double approvedTax = stateValue.endsWith(WF_STATE_COMMISSIONER_APPROVED) ? surveyBean.getApprovedTax().doubleValue()
                : ptGisIndex.getApprovedTax();

        String riName = stateValue.endsWith(PropertyTaxConstants.WF_STATE_UD_REVENUE_INSPECTOR_APPROVED)
                ? surveyBean.getProperty().getState().getLastModifiedBy().getName() : ptGisIndex.getRiName();
        String assistantName = stateValue.endsWith(PropertyTaxConstants.WF_STATE_ASSISTANT_APPROVED)
                ? surveyBean.getProperty().getState().getLastModifiedBy().getName() : ptGisIndex.getAssistantName();
        prepareApplicationDetails(applicationType, surveyBean, ptGisIndex, applicationDate, completionDate, stateValue);
        prepareTax(ptGisIndex, taxVar, applicationTax, approvedTax);
        prepareAddress(surveyBean, ptGisIndex);
        ptGisIndex.setAssistantName(assistantName);
        ptGisIndex.setRiName(riName);
        ptGisIndex.setToReferedToThrdPrty(surveyBean.isToBeRefferdThirdParty());
        ptGisIndex.setThirdPrtyFlag(surveyBean.getProperty().isThirdPartyVerified());
        createPTGISIndex(ptGisIndex);
    }

    private Double calculateVariance(final String applicationType, final SurveyBean surveyBean, final PTGISIndex ptGisIndex) {
        Double taxVar;
        if (applicationType.equalsIgnoreCase(PropertyTaxConstants.APPLICATION_TYPE_ALTER_ASSESSENT))
            taxVar = ((surveyBean.getApprovedTax().doubleValue() - (ptGisIndex.getSystemTax())) * 100)
                    / ptGisIndex.getSystemTax();
        else
            taxVar = Double.valueOf(100);
        return taxVar;
    }

    private void prepareAddress(final SurveyBean surveyBean, final PTGISIndex ptGisIndex) {
        String doorNo = surveyBean.getProperty().getBasicProperty().getAddress().getHouseNoBldgApt();
        ptGisIndex.setElectionWard(ptGisIndex.getElectionWard());
        ptGisIndex.setBlockName(ptGisIndex.getBlockName());
        ptGisIndex.setLocalityName(ptGisIndex.getLocalityName());
        ptGisIndex.setDoorNo(doorNo == null ? ptGisIndex.getDoorNo() : doorNo);
    }

    private void prepareApplicationDetails(final String applicationType, final SurveyBean surveyBean, final PTGISIndex ptGisIndex,
            final Date applicationDate, final Date completionDate, String stateValue) {
        ptGisIndex.setApplicationDate(applicationDate);
        ptGisIndex.setApplicationNo(surveyBean.getProperty().getApplicationNo());
        ptGisIndex.setApplicationStatus(surveyBean.getProperty().getState().getValue());
        ptGisIndex.setApplicationType(applicationType);
        ptGisIndex.setAssessmentNo(StringUtils.isBlank(surveyBean.getProperty().getBasicProperty().getUpicNo())
                ? StringUtils.EMPTY
                : surveyBean.getProperty().getBasicProperty().getUpicNo());
        ptGisIndex.setSource(ptGisIndex.getSource());
        ptGisIndex.setIsApproved(
                stateValue.contains(WF_STATE_COMMISSIONER_APPROVED)
                        || stateValue.contains(PropertyTaxConstants.WF_STATE_CLOSED) ? true
                                : false);
        ptGisIndex.setIsCancelled(stateValue.contains(WF_STATE_REJECTED) ? true : false);
        ptGisIndex.setCompletionDate(completionDate);
        ptGisIndex.setAgeOfCompletion(ptGisIndex.getAgeOfCompletion());
    }

    private void prepareTax(final PTGISIndex ptGisIndex, Double taxVar, Double applicationTax, Double approvedTax) {
        ptGisIndex.setApplicationTax(applicationTax);
        ptGisIndex.setApprovedTax(approvedTax);
        ptGisIndex.setGisTax(ptGisIndex.getGisTax());
        ptGisIndex.setSystemTax(ptGisIndex.getSystemTax());
        ptGisIndex.setTaxVariance(taxVar);
    }

    public PTGISIndex createPTGISIndex(PTGISIndex surveyIndex) {
        Map<String, Object> cityInfo = cityService.cityDataAsMap();
        surveyIndex.setCityCode(defaultString((String) cityInfo.get(CITY_CODE_KEY)));
        surveyIndex.setCityName(defaultString((String) cityInfo.get(CITY_NAME_KEY)));
        surveyIndex.setRegionName(defaultString((String) cityInfo.get(CITY_REGION_NAME_KEY)));
        surveyIndex.setDistrictName(defaultString((String) cityInfo.get(CITY_DIST_NAME_KEY)));
        surveyRepository.save(surveyIndex);
        return surveyIndex;
    }

    public PTGISIndex updatePTGISIndex(PTGISIndex surveyIndex) {
        surveyRepository.save(surveyIndex);
        return surveyIndex;
    }

    public PTGISIndex findByApplicationNo(String applicationNo) {
        return surveyRepository.findByApplicationNoAndCityName(applicationNo, ApplicationThreadLocals.getCityName());

    }

}
