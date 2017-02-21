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
package org.egov.mrs.application.service.collection;

import static org.egov.mrs.application.MarriageConstants.BOUNDARY_TYPE;
import static org.egov.mrs.application.MarriageConstants.MARRIAGE_DEFAULT_FUNCTIONARY_CODE;
import static org.egov.mrs.application.MarriageConstants.MARRIAGE_DEFAULT_FUND_CODE;
import static org.egov.mrs.application.MarriageConstants.MARRIAGE_DEFAULT_FUND_SRC_CODE;
import static org.egov.mrs.application.MarriageConstants.MARRIAGE_DEPARTMENT_CODE;
import static org.egov.mrs.application.MarriageConstants.MODULE_NAME;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.egov.collection.constants.CollectionConstants;
import org.egov.demand.dao.EgDemandDao;
import org.egov.demand.interfaces.Billable;
import org.egov.demand.model.AbstractBillable;
import org.egov.demand.model.EgBillType;
import org.egov.demand.model.EgDemand;
import org.egov.infra.admin.master.entity.AppConfigValues;
import org.egov.infra.admin.master.entity.Module;
import org.egov.infra.admin.master.service.AppConfigValueService;
import org.egov.infra.admin.master.service.ModuleService;
import org.egov.mrs.application.MarriageConstants;
import org.egov.mrs.domain.entity.ReIssue;
import org.joda.time.DateTime;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.config.ConfigurableBeanFactory;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Extension service of <code> AbstractBillable </code>, which implements billing features
 *
 * @author nayeem
 *
 */
@Service
@Transactional(readOnly = true)
@Scope(ConfigurableBeanFactory.SCOPE_PROTOTYPE)
public class ReIssueBillable extends AbstractBillable implements Billable {

    private static final String STRING_SERVICE_CODE = "MRR";
    public static final String DISPLAY_MESSAGE = "Marriage Certificate fee Collection";
    public static final String BILLTYPE_AUTO = "AUTO";
    public static final String MARRIAGE_REISSUE_MESSAGE = "Marriage_Certificate_Issue";

    private Long userId;
    private EgBillType billType;
    private boolean callbackForApportion = Boolean.FALSE;
    private String referenceNumber;
    private String transanctionReferenceNumber;

    private ReIssue reIssue;

    @Autowired
    private EgDemandDao egDemandDAO;

    @Autowired
    private ModuleService moduleService;
    
    @Autowired
    private AppConfigValueService appConfigValuesService;

    @Override
    public String getBillPayee() {
        return reIssue.getApplicant().getFullName();
    }

    @Override
    public String getBillAddress() {
        return reIssue.getApplicant().getContactInfo().getResidenceAddress();
    }

    @Override
    public EgDemand getCurrentDemand() {
        return reIssue.getDemand();
    }

    @Override
    public List<EgDemand> getAllDemands() {
        List<EgDemand> demands = null;
        final Long demandIds = getCurrentDemand().getId();
        if (demandIds != null) {
            demands = new ArrayList<>();

            demands.add(egDemandDAO.findById(Long.valueOf(demandIds.toString()), false));
        }
        return demands;
    }

    @Override
    public EgBillType getBillType() {
        return billType;
    }

    @Override
    public Date getBillLastDueDate() {
        return new DateTime().plusMonths(1).toDate();
    }

    @Override
    public Long getBoundaryNum() {
        return reIssue.getRegistration().getZone().getBoundaryNum();
    }

    @Override
    public String getBoundaryType() {
        final AppConfigValues boundaryType = appConfigValuesService.getConfigValuesByModuleAndKey(
                MarriageConstants.MODULE_NAME, MarriageConstants.MARRIAGE_REGISTRATIONUNIT_BOUNDARYYTYPE).get(0);
        return boundaryType != null && !"".equals(boundaryType) ? boundaryType.getValue() : BOUNDARY_TYPE;

    }

    @Override
    public String getDepartmentCode() {
        final AppConfigValues marriageDeptCode = getAppConfigValue(MARRIAGE_DEPARTMENT_CODE);
        return marriageDeptCode != null ? marriageDeptCode.getValue() : "";
    }

    @Override
    public BigDecimal getFunctionaryCode() {
        final AppConfigValues marriageFunctionaryCode = getAppConfigValue(MARRIAGE_DEFAULT_FUNCTIONARY_CODE);
        return marriageFunctionaryCode != null ? new BigDecimal(marriageFunctionaryCode.getValue()) : BigDecimal.ZERO;
    }

    @Override
    public String getFundCode() {
        final AppConfigValues marriageFundCode = getAppConfigValue(MARRIAGE_DEFAULT_FUND_CODE);
        return marriageFundCode != null ? marriageFundCode.getValue() : "";
    }

    @Override
    public String getFundSourceCode() {
        final AppConfigValues marriageFundSrcCode = getAppConfigValue(MARRIAGE_DEFAULT_FUND_SRC_CODE);
        return marriageFundSrcCode != null ? marriageFundSrcCode.getValue() : "";
    }

    public AppConfigValues getAppConfigValue(String code) {
        List<AppConfigValues> appConfigResult = appConfigValuesService.getConfigValuesByModuleAndKey(
                MODULE_NAME, code);
        return !appConfigResult.isEmpty() ? appConfigResult.get(0) : null;
    }
    
    @Override
    public Date getIssueDate() {
        return new Date();
    }

    @Override
    public Date getLastDate() {
        return getBillLastDueDate();
    }

    @Override
    public Module getModule() {
        return moduleService.getModuleByName(MODULE_NAME);
    }

    @Override
    public Boolean getOverrideAccountHeadsAllowed() {
        return false;
    }

    @Override
    public Boolean getPartPaymentAllowed() {
        return false;
    }

    @Override
    public String getServiceCode() {
        return STRING_SERVICE_CODE;
    }

    @Override
    public BigDecimal getTotalAmount() {
        return new BigDecimal(reIssue.getFeePaid());
    }

    @Override
    public Long getUserId() {
        return userId;
    }

    @Override
    public String getDescription() {
        return "Marriage Certificate Re-Issue with application No. : " + reIssue.getApplicationNo();
    }

    @Override
    public String getDisplayMessage() {
        return DISPLAY_MESSAGE;
    }

    @Override
    public String getCollModesNotAllowed() {
        return new StringBuilder()
                .append(CollectionConstants.INSTRUMENTTYPE_CHEQUE)
                .append(CollectionConstants.INSTRUMENTTYPE_CHEQUEORDD)
                .append(CollectionConstants.INSTRUMENTTYPE_DD)
                .append(CollectionConstants.INSTRUMENTTYPE_CARD)
                .append(CollectionConstants.INSTRUMENTTYPE_BANK)
                .append(CollectionConstants.INSTRUMENTTYPE_ONLINE).toString();
    }

    @Override
    public String getConsumerId() {
        return reIssue.getApplicationNo();
    }

    @Override
    public Boolean isCallbackForApportion() {
        return callbackForApportion;
    }

    @Override
    public void setCallbackForApportion(final Boolean b) {
        callbackForApportion = b;
    }

    @Override
    public String getReferenceNumber() {
        return referenceNumber;
    }

    @Override
    public String getTransanctionReferenceNumber() {
        return transanctionReferenceNumber;
    }

    public void setUserId(final Long userId) {
        this.userId = userId;
    }

    public void setBillType(final EgBillType billType) {
        this.billType = billType;
    }

    public void setReferenceNumber(final String referenceNumber) {
        this.referenceNumber = referenceNumber;
    }

    public void setTransanctionReferenceNumber(final String transanctionReferenceNumber) {
        this.transanctionReferenceNumber = transanctionReferenceNumber;
    }

    public ReIssue getReIssue() {
        return reIssue;
    }

    public void setReIssue(final ReIssue reIssue) {
        this.reIssue = reIssue;
    }

    @Override
    public String getEmailId() {
        return null;
    }

    @Override
    public String getConsumerType() {
        return MARRIAGE_REISSUE_MESSAGE;
    }
}
