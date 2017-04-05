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
package org.egov.bpa.application.service.collection;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.egov.bpa.application.entity.BpaApplication;
import org.egov.bpa.application.service.ApplicationBpaBillService;
import org.egov.bpa.utils.BpaConstants;
import org.egov.collection.constants.CollectionConstants;
import org.egov.demand.dao.EgBillDao;
import org.egov.demand.interfaces.Billable;
import org.egov.demand.model.AbstractBillable;
import org.egov.demand.model.EgBillType;
import org.egov.demand.model.EgDemand;
import org.egov.infra.admin.master.entity.Module;
import org.egov.infra.admin.master.service.ModuleService;
import org.joda.time.DateTime;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.config.ConfigurableBeanFactory;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional(readOnly = true)
@Scope(ConfigurableBeanFactory.SCOPE_PROTOTYPE)
public class BpaApplicationBillable extends AbstractBillable implements Billable {

    private static final String DISPLAY_MESSAGE = "Bpa Admission Fee Collection";
    private BpaApplication application;
    private Long userId;
    private Boolean isCallbackForApportion = Boolean.FALSE;
    private EgBillType billType;
    private String referenceNumber;
    private String transanctionReferenceNumber;

    @Autowired
    private EgBillDao egBillDAO;
    @Autowired
    private ModuleService moduleService;

    @Autowired
    private ApplicationBpaBillService applicationBpaBillService;

    @Override
    public String getBillPayee() {
        return application.getOwner().getApplicantName();
    }

    @Override
    public String getBillAddress() {
        return application.getOwner() != null ? application.getOwner().getAddress() : "";
    }

    @Override
    public EgDemand getCurrentDemand() {
        return application.getDemand();
    }

    @Override
    public String getEmailId() {
        return application.getOwner().getEmailid();
    }

    @Override
    public List<EgDemand> getAllDemands() {
        final List<EgDemand> demandList = new ArrayList<>();
        demandList.add(application.getDemand());
        return demandList;
    }

    @Override
    public EgBillType getBillType() {
        if (billType == null)
            billType = egBillDAO.getBillTypeByCode("AUTO");
        return billType;
    }

    @Override
    public Date getBillLastDueDate() {
        return new DateTime().plusMonths(1).toDate();
    }

    @Override
    public Long getBoundaryNum() {
        return application.getSiteDetail().get(0) != null && application.getSiteDetail().get(0).getAdminBoundary() != null
                ? application.getSiteDetail().get(0).getAdminBoundary().getBoundaryNum() : 0l;
    }

    @Override
    public String getBoundaryType() {
        return "Ward";
    }

    @Override
    public String getDepartmentCode() {
        return "REV";
    }

    @Override
    public BigDecimal getFunctionaryCode() {
        return new BigDecimal(0);
    }

    @Override
    public String getFundCode() {
        return "01";
    }

    @Override
    public String getFundSourceCode() {
        return "01";
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
        return moduleService.getModuleByName(BpaConstants.EGMODULE_NAME);
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
        return "BPA";

    }

    @Override
    public BigDecimal getTotalAmount() {
        final EgDemand currentDemand = getCurrentDemand();
        final List instVsAmt = applicationBpaBillService.getDmdCollAmtInstallmentWise(currentDemand);
        BigDecimal balance = BigDecimal.ZERO;
        for (final Object object : instVsAmt) {
            final Object[] ddObject = (Object[]) object;
            final BigDecimal dmdAmt = new BigDecimal((Double) ddObject[2]);
            BigDecimal collAmt = BigDecimal.ZERO;
            if (ddObject[2] != null)
                collAmt = new BigDecimal((Double) ddObject[3]);
            balance = balance.add(dmdAmt.subtract(collAmt));

        }
        return balance;
    }

    @Override
    public Long getUserId() {
        return userId;
    }

    @Override
    public String getDescription() {

        return "Bpa Application Number: " + getApplication().getApplicationNumber();
    }

    @Override
    public String getDisplayMessage() {
        return DISPLAY_MESSAGE;
    }

    @Override
    public String getCollModesNotAllowed() {
        return CollectionConstants.INSTRUMENTTYPE_BANK;
    }

    @Override
    public String getConsumerId() {
        return application.getApplicationNumber();

    }

    @Override
    public String getConsumerType() {
        return application.getServiceType().getCode();
    }

    @Override
    public Boolean isCallbackForApportion() {
        return isCallbackForApportion;
    }

    @Override
    public void setCallbackForApportion(final Boolean b) {
        isCallbackForApportion = b;
    }

    public BpaApplication getApplication() {
        return application;
    }

    public void setApplication(final BpaApplication application) {
        this.application = application;
    }

    public void setUserId(final Long userId) {
        this.userId = userId;
    }

    public void setBillType(final EgBillType billType) {
        this.billType = billType;
    }

    public String buildAddressDetails(final BpaApplication application) {

        final StringBuilder address = new StringBuilder();
        if (application.getSiteDetail().get(0) != null)
            address.append(application.getSiteDetail().get(0).getArea());
        return address.toString();
    }

    @Override
    public String getReferenceNumber() {
        return referenceNumber;
    }

    public void setReferenceNumber(final String referenceNumber) {
        this.referenceNumber = referenceNumber;
    }

    @Override
    public String getTransanctionReferenceNumber() {
        return transanctionReferenceNumber;
    }

    public void setTransanctionReferenceNumber(final String transanctionReferenceNumber) {
        this.transanctionReferenceNumber = transanctionReferenceNumber;
    }
}