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

package org.egov.adtax.service.collection;

import org.apache.log4j.Logger;
import org.egov.adtax.entity.Advertisement;
import org.egov.adtax.entity.AdvertisementPermitDetail;
import org.egov.adtax.utils.constants.AdvertisementTaxConstants;
import org.egov.demand.dao.EgBillDao;
import org.egov.demand.interfaces.Billable;
import org.egov.demand.model.AbstractBillable;
import org.egov.demand.model.EgBillType;
import org.egov.demand.model.EgDemand;
import org.egov.demand.model.EgDemandDetails;
import org.egov.infra.admin.master.entity.Module;
import org.egov.infra.admin.master.service.ModuleService;
import org.egov.infra.config.core.ApplicationThreadLocals;
import org.egov.infra.security.utils.SecurityUtils;
import org.egov.infra.utils.DateUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.config.ConfigurableBeanFactory;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Service
@Scope(ConfigurableBeanFactory.SCOPE_PROTOTYPE)
@Transactional(readOnly = true)
public class AdvertisementBillable extends AbstractBillable implements Billable {
    private static final Logger LOGGER = Logger.getLogger(AdvertisementBillable.class);
    private String referenceNumber;
    private String transanctionReferenceNumber;

    @Autowired
    private ModuleService moduleService;

    private Advertisement advertisement;
    private String collectionType;
    @Autowired
    private EgBillDao egBillDAO;
    @Autowired
    private SecurityUtils securityUtils;
    @Override
    public String getBillPayee() {
        AdvertisementPermitDetail advPermit=advertisement.getActiveAdvertisementPermit();
        if (advertisement != null)
        { 
            if (collectionType != null && advPermit != null
                    && AdvertisementTaxConstants.ADVERTISEMENT_COLLECTION_TYPE.equalsIgnoreCase(collectionType)) {
                
                return (advPermit.getAgency() != null && advPermit.getAgency().getName()!=null? advPermit.getAgency().getName():(advPermit.getOwnerDetail()!=null ? advPermit.getOwnerDetail(): " ") );
            } else
            {
                return advPermit != null
                        && advPermit.getAgency() != null && advPermit.getAgency().getName()!=null ? advPermit.getAgency().getName() : " ";
            }
        }
        return null;
    }

    @Override
    public String getBillAddress() {
        AdvertisementPermitDetail advPermit=advertisement.getActiveAdvertisementPermit();
        if (advertisement != null){
            if (collectionType != null && AdvertisementTaxConstants.ADVERTISEMENT_COLLECTION_TYPE.equalsIgnoreCase(collectionType))
                return (advPermit!=null && advPermit.getAgency() != null && advPermit.getAgency().getAddress()!=null) ? advPermit.getAgency().getAddress() : ( advPermit != null && advPermit.getOwnerDetail()!=null?advPermit.getOwnerDetail():" ");
            else
            {
            //     advPermit=  advertisement.getActiveAdvertisementPermit();
                return advPermit!=null && advPermit.getAgency() != null && advPermit.getAgency().getAddress()!=null ? advPermit.getAgency().getAddress() : " ";
            }
        }
        return null;
    }

    @Override
    public EgDemand getCurrentDemand() {
        return advertisement != null ? advertisement.getDemandId() : null;
    }
   
    @Override
    public String getEmailId() {
        return "";
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
        return egBillDAO.getBillTypeByCode(AdvertisementTaxConstants.BILL_TYPE_AUTO);

    }

    @Override
    public Date getBillLastDueDate() {
        return DateUtils.today();
    }

    @Override
    public Long getBoundaryNum() {
        if (advertisement != null && advertisement.getWard() != null)
            return advertisement.getWard().getBoundaryNum();
        return null;
    }

    @Override
    public String getBoundaryType() {

        if (advertisement != null  &&  advertisement.getWard() != null && advertisement.getWard().getBoundaryType()!=null)
            return advertisement.getWard().getBoundaryType().getName();
        return null;
    }

    @Override
    public String getDepartmentCode() {
        return AdvertisementTaxConstants.STRING_DEPARTMENT_CODE;
    }

    @Override
    public BigDecimal getFunctionaryCode() {
        return new BigDecimal(AdvertisementTaxConstants.DEFAULT_FUNCTIONARY_CODE);
    }

    @Override
    public String getFundCode() {
        return AdvertisementTaxConstants.DEFAULT_FUND_CODE;
    }

    @Override
    public String getFundSourceCode() {
        return AdvertisementTaxConstants.DEFAULT_FUNCTIONARY_CODE;
    }

    @Override
    public Date getIssueDate() {
        return new Date();
    }

    @Override
    public Date getLastDate() {
        return advertisement!=null && 
                advertisement.getPenaltyCalculationDate() != null ? advertisement.getPenaltyCalculationDate() : null;
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

        if (advertisement != null && advertisement.getDemandId() != null)
            for (final EgDemandDetails det : advertisement.getDemandId().getEgDemandDetails()) {
                final BigDecimal dmdAmt = det.getAmount();
                final BigDecimal collAmt = det.getAmtCollected();
                balance = balance.add(dmdAmt.subtract(collAmt));
            }
        return balance;
    }

    @Override
    public Long getUserId() {
        return ApplicationThreadLocals.getUserId() == null ? securityUtils.getCurrentUser().getId() : Long.valueOf(ApplicationThreadLocals.getUserId());
    }
    
    
    @Override
    public String getDescription() {
        final StringBuffer description = new StringBuffer();

        if (advertisement != null ) {
            description.append(AdvertisementTaxConstants.FEECOLLECTIONMESSAGE);
            description.append(advertisement.getAdvertisementNumber() != null ? advertisement.getAdvertisementNumber() : "");
        }
        return description.toString();
    }

    @Override
    public String getDisplayMessage() {
        return AdvertisementTaxConstants.FEECOLLECTION;
    }

    @Override
    public String getCollModesNotAllowed() {

        return null;
    }

    @Override
    public String getConsumerId() {
        if (advertisement != null)
            return advertisement.getId().toString();
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
  

    public Advertisement getAdvertisement() {
        return advertisement;
    }

    public void setAdvertisement(Advertisement advertisement) {
        this.advertisement = advertisement;
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
    public String getConsumerType() {
        return "";
    }
    @Override
    public String getTransanctionReferenceNumber() {
        return transanctionReferenceNumber;
    }

    public void setTransanctionReferenceNumber(String transanctionReferenceNumber) {
        this.transanctionReferenceNumber = transanctionReferenceNumber;
    }
    
}
