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

package org.egov.stms.transactions.service.collection;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.apache.log4j.Logger;
import org.egov.collection.constants.CollectionConstants;
import org.egov.demand.dao.EgBillDao;
import org.egov.demand.dao.EgDemandDao;
import org.egov.demand.interfaces.Billable;
import org.egov.demand.model.AbstractBillable;
import org.egov.demand.model.EgBillType;
import org.egov.demand.model.EgDemand;
import org.egov.demand.model.EgDemandDetails;
import org.egov.infra.admin.master.entity.Module;
import org.egov.infra.admin.master.service.ModuleService;
import org.egov.infra.config.core.ApplicationThreadLocals;
import org.egov.infra.exception.ApplicationRuntimeException;
import org.egov.ptis.domain.model.AssessmentDetails;
import org.egov.ptis.domain.model.BoundaryDetails;
import org.egov.ptis.domain.model.OwnerName;
import org.egov.stms.masters.entity.enums.SewerageConnectionStatus;
import org.egov.stms.transactions.entity.SewerageApplicationDetails;
import org.egov.stms.utils.SewerageTaxUtils;
import org.egov.stms.utils.constants.SewerageTaxConstants;
import org.egov.wtms.utils.WaterTaxUtils;
import org.joda.time.DateTime;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.config.ConfigurableBeanFactory;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Scope(ConfigurableBeanFactory.SCOPE_PROTOTYPE)
@Transactional(readOnly = true)
public class SewerageBillable extends AbstractBillable implements Billable {
    private static final Logger LOGGER = Logger.getLogger(SewerageBillable.class);

    @Autowired
    private ModuleService moduleService;

    private SewerageApplicationDetails sewerageApplicationDetails;

 
    private AssessmentDetails assessmentDetails;
    private Long userId;
    private EgBillType billType;
    private Boolean isCallbackForApportion = Boolean.FALSE;
    private String referenceNumber;
    private String transanctionReferenceNumber;

    @Autowired
    private EgBillDao egBillDAO;
    @Autowired
    private EgDemandDao egDemandDAO;
   
    @Autowired
    private WaterTaxUtils waterTaxUtils;
    
    @Autowired
    private SewerageTaxUtils sewerageTaxUtils;
    
    @Override
    public String getBillPayee() {
        return buildOwnerFullName(getAssessmentDetails().getOwnerNames());
    }

    @Override
    public String getBillAddress() {
         return buildAddressDetails(assessmentDetails);
    }

    @Override
    public EgDemand getCurrentDemand() {
        if(getSewerageApplicationDetails()!=null && getSewerageApplicationDetails().getCurrentDemand()!=null)
            return getSewerageApplicationDetails().getCurrentDemand();
        
        return null;
    }

    @Override
    public String getConsumerType() {
        return "";
    }
    @Override
    public List<EgDemand> getAllDemands() {
        List<EgDemand> demands = null;
        if (getCurrentDemand() != null) {
            demands = new ArrayList<EgDemand>();
            demands.add(getCurrentDemand());
        }
        return demands;
    }

    @Override
    public EgBillType getBillType() {
        if (billType == null)
            billType = egBillDAO.getBillTypeByCode(SewerageTaxConstants.BILL_TYPE_AUTO);
           
        return billType;
    }

    @Override
    public Date getBillLastDueDate() {
        return new DateTime().plusMonths(1).toDate();
    }

    @Override
    public Long getBoundaryNum() {
        if (getAssessmentDetails() != null && getAssessmentDetails().getBoundaryDetails() != null)
            return getAssessmentDetails().getBoundaryDetails().getWardNumber();
        return null;
    }

    @Override
    public String getBoundaryType() {
        
        if (getAssessmentDetails() != null && getAssessmentDetails().getBoundaryDetails() != null)
            return getAssessmentDetails().getBoundaryDetails().getWardBoundaryType();
        
        return "Ward";
    }

    @Override
    public String getDepartmentCode() {
        return SewerageTaxConstants.STRING_DEPARTMENT_CODE;
    }

    @Override
    public BigDecimal getFunctionaryCode() {
        return new BigDecimal(SewerageTaxConstants.DEFAULT_FUNCTIONARY_CODE);
    }

    @Override
    public String getFundCode() {
        return SewerageTaxConstants.DEFAULT_FUND_CODE;
    }

    @Override
    public String getFundSourceCode() {
        return SewerageTaxConstants.DEFAULT_FUND_SRC_CODE;
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
        return moduleService.getModuleByName(SewerageTaxConstants.MODULE_NAME);
    }

    @Override
    public Boolean getOverrideAccountHeadsAllowed() {
        return false;
    }

    @Override
    public Boolean getPartPaymentAllowed() {
        if (getSewerageApplicationDetails()!=null && getSewerageApplicationDetails().getConnection().getStatus()!=null
                && getSewerageApplicationDetails().getConnection().getStatus().equals(SewerageConnectionStatus.ACTIVE))
            return true;
        else
            return false;
    }

    @Override
    public String getServiceCode() {
      /*  if (getSewerageApplicationDetails().getStatus().getCode().equalsIgnoreCase(SewerageTaxConstants.APPLICATION_STATUS_ESTIMATENOTICEGEN))
            return SewerageTaxConstants.EST_STRING_SERVICE_CODE;
        else*/
        return SewerageTaxConstants.STRING_SERVICE_CODE;
    }

    @Override
    public BigDecimal getTotalAmount() {
        BigDecimal balance = BigDecimal.ZERO;

        if (getCurrentDemand() != null)
            for (final EgDemandDetails det : getCurrentDemand().getEgDemandDetails()) {
                final BigDecimal dmdAmt = det.getAmount();
                final BigDecimal collAmt = det.getAmtCollected();
                balance = balance.add(dmdAmt.subtract(collAmt));
            }

        return balance;
    }

    @Override
    public Long getUserId() {
        return ApplicationThreadLocals.getUserId() == null ? null : Long.valueOf(ApplicationThreadLocals.getUserId()); 
    }

    @Override
    public String getDescription() {
        if (null != getSewerageApplicationDetails().getConnection().getShscNumber())
            return "Sewerage connection H.S.C No: " + getSewerageApplicationDetails().getConnection().getShscNumber();
        else
            return "Sewerage connection Application Number: " + getSewerageApplicationDetails().getApplicationNumber();
    }

    @Override
    public String getDisplayMessage() {
        return SewerageTaxConstants.DISPLAY_MESSAGE;
    }

    @Override
    public String getCollModesNotAllowed() {
            
            StringBuilder collectionModesNotAllowed = new StringBuilder();
            collectionModesNotAllowed.append(CollectionConstants.INSTRUMENTTYPE_BANK);
            return collectionModesNotAllowed.toString();
    }

    @Override
    public String getConsumerId() {
        return getSewerageApplicationDetails().getApplicationNumber();
    }

    @Override
    public Boolean isCallbackForApportion() {
        if (getSewerageApplicationDetails()!=null && getSewerageApplicationDetails().getConnection().getStatus()!=null
                && getSewerageApplicationDetails().getConnection().getStatus().equals(SewerageConnectionStatus.ACTIVE))
            return true;
        else
            return false;
    }

    @Override
    public void setCallbackForApportion(final Boolean b) {
        isCallbackForApportion = b;
    }

    public void setUserId(final Long userId) {
        this.userId = userId;
    }

    public void setBillType(final EgBillType billType) {
        this.billType = billType;
    }

    public String buildOwnerFullName(final Set<OwnerName> ownerSet) {
        if (ownerSet == null)
            throw new ApplicationRuntimeException("Property Owner set is null...");
        String ownerFullName = "";
        final Set<String> ownerNameSet = new HashSet<String>();
        for (final OwnerName propOwnerInfo : ownerSet)
            // User propOwner = propOwnerInfo.getOwner();
            if (propOwnerInfo.getOwnerName() != null && !propOwnerInfo.getOwnerName().trim().equals(""))
                if (!ownerNameSet.contains(propOwnerInfo.getOwnerName().trim())) {
                    if (!ownerFullName.trim().equals(""))
                        if (!ownerFullName.equals(""))
                            ownerFullName += ", ";
                    ownerNameSet.add(propOwnerInfo.getOwnerName().trim());
                    ownerFullName = propOwnerInfo.getOwnerName() == null ? "" : propOwnerInfo.getOwnerName();
                }
        return ownerFullName;
    }

    public String buildAddressDetails(final AssessmentDetails assessmentDetails) {
        final StringBuilder address = new StringBuilder();
         
       if(assessmentDetails!=null)  {
            final BoundaryDetails boundaryDetails = assessmentDetails.getBoundaryDetails();
            if(assessmentDetails.getPropertyAddress() !=null && !"".equals(assessmentDetails.getPropertyAddress())){
                address.append( assessmentDetails.getPropertyAddress() );
            }
            else{
                if (boundaryDetails.getZoneName() != null)
                    address.append(boundaryDetails.getZoneName());
                if (boundaryDetails.getWardName() != null)
                    address.append(", ").append(boundaryDetails.getWardName());
                if (boundaryDetails.getLocalityName() != null)
                    address.append(", ").append(boundaryDetails.getLocalityName());
                if (boundaryDetails.getBlockName() != null)
                    address.append(", ").append(boundaryDetails.getBlockName());
                if (boundaryDetails.getStreetName() != null)
                    address.append(", ").append(boundaryDetails.getStreetName());
            }
       }
        return address.toString();
    }

    public SewerageApplicationDetails getSewerageApplicationDetails() {
        return sewerageApplicationDetails;
    }

    public void setSewerageApplicationDetails(SewerageApplicationDetails sewerageApplicationDetails) {
        this.sewerageApplicationDetails = sewerageApplicationDetails;
    }

    public AssessmentDetails getAssessmentDetails() {
        return assessmentDetails;
    }

    public void setAssessmentDetails(final AssessmentDetails assessmentDetails) {
        this.assessmentDetails = assessmentDetails;
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

    @Override
    public String getEmailId() {

        return null;
    }    
}
