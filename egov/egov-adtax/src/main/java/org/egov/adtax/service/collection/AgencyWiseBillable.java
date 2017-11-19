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

package org.egov.adtax.service.collection;

import org.apache.log4j.Logger;
import org.egov.adtax.entity.AgencyWiseCollection;
import org.egov.adtax.utils.constants.AdvertisementTaxConstants;
import org.egov.demand.dao.EgBillDao;
import org.egov.demand.interfaces.Billable;
import org.egov.demand.model.AbstractBillable;
import org.egov.demand.model.EgBillType;
import org.egov.demand.model.EgDemand;
import org.egov.infra.admin.master.entity.Boundary;
import org.egov.infra.admin.master.entity.BoundaryType;
import org.egov.infra.admin.master.entity.HierarchyType;
import org.egov.infra.admin.master.entity.Module;
import org.egov.infra.admin.master.service.BoundaryService;
import org.egov.infra.admin.master.service.BoundaryTypeService;
import org.egov.infra.admin.master.service.HierarchyTypeService;
import org.egov.infra.admin.master.service.ModuleService;
import org.egov.infra.config.core.ApplicationThreadLocals;
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
public class AgencyWiseBillable extends AbstractBillable implements Billable {
    private static final Logger LOGGER = Logger.getLogger(AgencyWiseBillable.class);
    public static final String FEECOLLECTIONMESSAGE = "Fee Collection : Agency Name-";
    private static final String CITY_BOUNDARY_TYPE = "City";
    private String referenceNumber;
    private String transanctionReferenceNumber;

    @Autowired
    private ModuleService moduleService;

    private AgencyWiseCollection agencyWiseCollection;

    @Autowired
    private HierarchyTypeService hierarchyTypeService;
    @Autowired
    private BoundaryTypeService boundaryTypeService;
    @Autowired
    private BoundaryService boundaryService;

    @Autowired
    private EgBillDao egBillDAO;

    @Override
    public String getBillPayee() {
        if (agencyWiseCollection != null && agencyWiseCollection.getAgency() != null)
            return agencyWiseCollection.getAgency().getName();
        return null;
    }

    @Override
    public String getBillAddress() {
        if (agencyWiseCollection != null && agencyWiseCollection.getAgency() != null && agencyWiseCollection.getAgency().getAddress()!=null)
            return agencyWiseCollection.getAgency().getAddress();

        return "";
    }

    @Override
    public EgDemand getCurrentDemand() {

        if (agencyWiseCollection != null && agencyWiseCollection.getAgencyWiseDemand() != null)
            return agencyWiseCollection.getAgencyWiseDemand();

        return null;
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
        final Boundary boundary = getBoundaryAsCity();
        if (boundary != null)
            return boundary.getBoundaryNum();
        return null;
    }

    private Boundary getBoundaryAsCity() {
        HierarchyType hType = null;
        hType = hierarchyTypeService.getHierarchyTypeByName(AdvertisementTaxConstants.ELECTION_HIERARCHY_TYPE);

        List<Boundary> locationList = null;
        if (hType != null) {
            final BoundaryType bType = boundaryTypeService.getBoundaryTypeByNameAndHierarchyType(CITY_BOUNDARY_TYPE,
                    hType);
            if (bType != null)
                locationList = boundaryService.getActiveBoundariesByBoundaryTypeId(bType.getId());

            if (locationList != null && locationList.size() > 0)
                return locationList.get(0);

        }
        return null;
    }

    @Override
    public String getBoundaryType() {
        final Boundary boundary = getBoundaryAsCity();
        if (boundary != null)
            return boundary.getBoundaryType().getName();
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
        return AdvertisementTaxConstants.DEFAULT_FUND_SRC_CODE;
    }

    @Override
    public Date getIssueDate() {
        return new Date();
    }

    @Override
    public Date getLastDate() {
        return new Date();
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

        if (agencyWiseCollection != null && agencyWiseCollection.getTotalAmount() != null)
            balance = agencyWiseCollection.getTotalAmount();
        return balance;
    }

    @Override
    public Long getUserId() {
        return ApplicationThreadLocals.getUserId() == null ? null : Long.valueOf(ApplicationThreadLocals.getUserId());
    }

    @Override
    public String getDescription() {
        final StringBuffer description = new StringBuffer();

        if (agencyWiseCollection != null && agencyWiseCollection.getAgency() != null) {
            description.append(FEECOLLECTIONMESSAGE);
            description.append(agencyWiseCollection.getAgency() != null ? agencyWiseCollection.getAgency().getName()
                    : "");
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
        if (agencyWiseCollection != null)
            return AdvertisementTaxConstants.AGENCY_PREFIX_CONSUMERCODE.concat(agencyWiseCollection.getId().toString());
        return null;
    }
    @Override
    public String getConsumerType() {
        return "";
    }
    @Override
    public Boolean isCallbackForApportion() {
        return Boolean.FALSE;
    }

    @Override
    public void setCallbackForApportion(final Boolean b) {
        throw new IllegalArgumentException("Apportioning is always TRUE and shouldn't be changed");

    }

    @Override
    public String getReferenceNumber() {
        return referenceNumber;
    }

    public void setReferenceNumber(final String referenceNumber) {
        this.referenceNumber = referenceNumber;
    }

    public AgencyWiseCollection getAgencyWiseCollection() {
        return agencyWiseCollection;
    }

    public void setAgencyWiseCollection(final AgencyWiseCollection agencyWiseCollection) {
        this.agencyWiseCollection = agencyWiseCollection;
    }

    @Override
    public String getTransanctionReferenceNumber() {
        return transanctionReferenceNumber;
    }

    public void setTransanctionReferenceNumber(String transanctionReferenceNumber) {
        this.transanctionReferenceNumber = transanctionReferenceNumber;
    }
    
}
