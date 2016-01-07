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
package org.egov.adtax.service.collection;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.apache.log4j.Logger;
import org.egov.adtax.entity.Hoarding;
import org.egov.adtax.utils.constants.AdvertisementTaxConstants;
import org.egov.demand.dao.EgBillDao;
import org.egov.demand.interfaces.Billable;
import org.egov.demand.model.AbstractBillable;
import org.egov.demand.model.EgBillType;
import org.egov.demand.model.EgDemand;
import org.egov.demand.model.EgDemandDetails;
import org.egov.infra.admin.master.entity.Module;
import org.egov.infra.admin.master.service.ModuleService;
import org.egov.infra.utils.EgovThreadLocals;
import org.egov.infstr.utils.DateUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.config.ConfigurableBeanFactory;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Scope(ConfigurableBeanFactory.SCOPE_PROTOTYPE)
@Transactional(readOnly = true)
public class AdvertisementBillable extends AbstractBillable implements Billable {
    private static final Logger LOGGER = Logger.getLogger(AdvertisementBillable.class);
    public static final String collectionTypeHoarding = "hoarding";
    public static final String FEECOLLECTIONMESSAGE = "Fee Collection : Hoarding Number -";
    private static final String STRING_DEPARTMENT_CODE = "REV";
    public static final String DEFAULT_FUNCTIONARY_CODE = "1";
    public static final String DEFAULT_FUND_SRC_CODE = "01";
    public static final String DEFAULT_FUND_CODE = "01";

    public static final String FEECOLLECTION = "Fee Collection";
    public static final String AUTO = "AUTO";
    public static final String WARD = "Ward";
    public static final String ADDRESSTYPEASOWNER = "OWNER";
    private String referenceNumber;
    private String transanctionReferenceNumber;

    @Autowired
    private ModuleService moduleService;

    private Hoarding hoarding;
    private String collectionType;

    @Autowired
    private EgBillDao egBillDAO;

    @Override
    public String getBillPayee() {
        if (hoarding != null)
            /*
             * if (collectionType != null &&
             * collectionTypeHoarding.equalsIgnoreCase(collectionType)) { return
             * hoarding.getOwnerDetail(); } else
             */
            return hoarding.getAgency() != null ? hoarding.getAgency().getName() : " ";
        return null;
    }

    @Override
    public String getBillAddress() {
        if (hoarding != null)
            if (collectionType != null && collectionTypeHoarding.equalsIgnoreCase(collectionType))
                return " ";
            else
                return hoarding.getAgency() != null ? hoarding.getAgency().getAddress() : " ";
        return null;
    }

    @Override
    public EgDemand getCurrentDemand() {
        return hoarding != null ? hoarding.getDemandId() : null;
    }

    @Override
    public List<EgDemand> getAllDemands() {
        final List<EgDemand> demands = new ArrayList<EgDemand>();
        if (getCurrentDemand() != null)
            demands.add(getCurrentDemand());
        return demands;
    }

    @Override
    public EgBillType getBillType() {
        return egBillDAO.getBillTypeByCode(AUTO);

    }

    @Override
    public Date getBillLastDueDate() {
        return DateUtils.today();
    }

    @Override
    public Long getBoundaryNum() {
        if (hoarding != null && hoarding.getWard() != null)
            return hoarding.getWard().getBoundaryNum();
        return null;
    }

    @Override
    public String getBoundaryType() {

        if (hoarding != null && hoarding.getWard() != null)
            return hoarding.getWard().getBoundaryType().getName();
        return null;
    }

    @Override
    public String getDepartmentCode() {
        return STRING_DEPARTMENT_CODE;
    }

    @Override
    public BigDecimal getFunctionaryCode() {
        return new BigDecimal(DEFAULT_FUNCTIONARY_CODE);
    }

    @Override
    public String getFundCode() {
        return DEFAULT_FUND_CODE;
    }

    @Override
    public String getFundSourceCode() {
        return DEFAULT_FUNCTIONARY_CODE;
    }

    @Override
    public Date getIssueDate() {
        return new Date();
    }

    @Override
    public Date getLastDate() {
        return hoarding.getPenaltyCalculationDate() != null ? hoarding.getPenaltyCalculationDate() : null;
    }

    @Override
    public Module getModule() {
        return moduleService.getModuleByName(AdvertisementTaxConstants.MODULE_NAME);
    }

    @Override
    public Boolean getOverrideAccountHeadsAllowed() {
        return Boolean.FALSE;
    }

    @Override
    public Boolean getPartPaymentAllowed() {
        return Boolean.FALSE;
    }

    @Override
    public String getServiceCode() {
        return AdvertisementTaxConstants.SERVICE_CODE;
    }

    @Override
    public BigDecimal getTotalAmount() {
        BigDecimal balance = BigDecimal.ZERO;

        if (hoarding != null && hoarding.getDemandId() != null)
            for (final EgDemandDetails det : hoarding.getDemandId().getEgDemandDetails()) {
                final BigDecimal dmdAmt = det.getAmount();
                final BigDecimal collAmt = det.getAmtCollected();
                balance = balance.add(dmdAmt.subtract(collAmt));
            }
        return balance;
    }

    @Override
    public Long getUserId() {
        return EgovThreadLocals.getUserId() == null ? null : Long.valueOf(EgovThreadLocals.getUserId());
    }

    @Override
    public String getDescription() {
        final StringBuffer description = new StringBuffer();

        if (hoarding != null && hoarding.getHoardingNumber() != null) {
            description.append(FEECOLLECTIONMESSAGE);
            description.append(hoarding.getHoardingNumber() != null ? hoarding.getHoardingNumber() : "");
        }
        return description.toString();
    }

    @Override
    public String getDisplayMessage() {
        return FEECOLLECTION;
    }

    @Override
    public String getCollModesNotAllowed() {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public String getConsumerId() {
        if (hoarding != null)
            return hoarding.getId().toString();
        return null;
    }

    @Override
    public Boolean isCallbackForApportion() {
        return Boolean.FALSE;
    }

    @Override
    public void setCallbackForApportion(final Boolean b) {
        throw new IllegalArgumentException("Apportioning is always TRUE and shouldn't be changed");

    }

    public Hoarding getHoarding() {
        return hoarding;
    }

    public void setHoarding(final Hoarding hoarding) {
        this.hoarding = hoarding;
    }

    public String getCollectionType() {
        return collectionType;
    }

    public void setCollectionType(final String collectionType) {
        this.collectionType = collectionType;
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

    public void setTransanctionReferenceNumber(String transanctionReferenceNumber) {
        this.transanctionReferenceNumber = transanctionReferenceNumber;
    }
    
}
