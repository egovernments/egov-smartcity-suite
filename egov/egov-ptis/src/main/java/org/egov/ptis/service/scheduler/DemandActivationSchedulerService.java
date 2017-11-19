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

package org.egov.ptis.service.scheduler;

import org.apache.log4j.Logger;
import org.egov.demand.model.EgDemandDetails;
import org.egov.infra.utils.DateUtils;
import org.egov.ptis.constants.PropertyTaxConstants;
import org.egov.ptis.domain.entity.demand.Ptdemand;
import org.egov.ptis.domain.entity.property.BasicProperty;
import org.egov.ptis.domain.entity.property.PropertyImpl;
import org.egov.ptis.domain.service.property.PropertyPersistenceService;
import org.egov.ptis.scheduler.DemandActivationJob;
import org.egov.ptis.service.utils.PropertyTaxCommonUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

@Service
@Transactional(readOnly = true)
public class DemandActivationSchedulerService {

    private static final Logger LOGGER = Logger.getLogger(DemandActivationJob.class);
    private static final String STR_REMARKS_DEMAND_ACTIVATION_CORP = "Demand activated by system on 15th day after notice generation";
    private static final String STR_REMARKS_DEMAND_ACTIVATION_MNCP_AND_NP = "Demand activated by system on 30th day after notice generation";
    
    @Autowired
    private PropertyPersistenceService basicPropertyService;

    @Autowired
    private PropertyTaxCommonUtils propertyTaxCommonUtils;

    @PersistenceContext
    private EntityManager entityManager;

    @Transactional
    public void demandActivation() {

        final Long currentTimeMillis = System.currentTimeMillis();
        String assessmentNo = null;
        final List<Ptdemand> properties = getInactiveDemandNotObjectedProperties();
        BasicProperty basicProperty;

        for (final Ptdemand demand : properties)
            try {
                basicProperty = demand.getEgptProperty().getBasicProperty();
                // adjustAdvancePayment(demand); -- as of now we do not have
                // rules to adjust advances
                assessmentNo = basicProperty != null ? basicProperty.getUpicNo() : null;
                activateDemand(basicProperty);
            } catch (final Exception e) {
                LOGGER.error("Error while activating the demand for " + assessmentNo, e);
            }

        LOGGER.info("Demand activation for " + properties.size() + " properties is completed in "
                + (System.currentTimeMillis() - currentTimeMillis) / 1000 + " sec(s)");

    }

    /**
     * @param basicProperty
     */
    private void activateDemand(final BasicProperty basicProperty) {
        LOGGER.debug("Entered into activateDemand");

        final PropertyImpl inactiveProperty = basicProperty.getInactiveProperty();

        inactiveProperty.setStatus(PropertyTaxConstants.STATUS_ISACTIVE);
        inactiveProperty.setRemarks(getRemarksByCityGrade(inactiveProperty));
        inactiveProperty.setLastModifiedDate(new Date());
        basicPropertyService.merge(basicProperty);

        LOGGER.debug("Exiting from activateDemand");
    }

    public List<Ptdemand> getInactiveDemandNotObjectedProperties() {
        LOGGER.debug("Entered into getQueryString");
        final Date dateEffectiveDaysPast; // Effective days based on city grade
        if (propertyTaxCommonUtils.isCorporation())
            dateEffectiveDaysPast = DateUtils.add(new Date(), Calendar.DAY_OF_MONTH, -15);
        else
            dateEffectiveDaysPast = DateUtils.add(new Date(), Calendar.DAY_OF_MONTH, -30);

        final String stringQuery = "SELECT ptd FROM PtNotice n, PtNotice pvr, Ptdemand ptd LEFT JOIN FETCH ptd.egptProperty p "
                + "LEFT JOIN FETCH p.basicProperty bp WHERE n.basicProperty = bp AND pvr.basicProperty = bp AND bp.active = true "
                + "AND bp.status.statusCode <> :bpStatus AND p.status = 'I' AND ptd.egInstallmentMaster = :currInstallment "
                + "AND pvr.noticeType = :noticeType AND n.noticeDate > p.createdDate AND pvr.noticeDate > p.createdDate "
                + "AND n.noticeDate < :pastDate AND pvr.noticeDate < :pastDate ";

        LOGGER.debug("getQueryString, query=" + stringQuery);

        @SuppressWarnings("unchecked")
        final List<Ptdemand> properties = basicPropertyService.getSession().createQuery(stringQuery)
                .setString("bpStatus", PropertyTaxConstants.STATUS_OBJECTED_STR)
                .setParameter("pastDate", dateEffectiveDaysPast)
                .setString("noticeType", PropertyTaxConstants.NOTICE_TYPE_SPECIAL_NOTICE)
                .setEntity("currInstallment", propertyTaxCommonUtils.getCurrentInstallment()).list();

        LOGGER.debug("Exting from getQueryString");
        return properties;
    }

    /**
     * @param ptDemand
     */
    @SuppressWarnings("unused")
    private EgDemandDetails getAdvanceDemandDetail(final Ptdemand ptDemand) {
        EgDemandDetails advanceDemandDetail = null;

        for (final EgDemandDetails demandDetail : ptDemand.getEgDemandDetails())
            if (demandDetail.getEgDemandReason().getEgDemandReasonMaster().getCode()
                    .equalsIgnoreCase(PropertyTaxConstants.DEMANDRSN_CODE_ADVANCE)) {
                advanceDemandDetail = demandDetail;
                break;
            }

        return advanceDemandDetail;
    }
    
    private String getRemarksByCityGrade(PropertyImpl inactiveProperty) {

        String remarks = propertyTaxCommonUtils.isCorporation() ? STR_REMARKS_DEMAND_ACTIVATION_CORP
                : STR_REMARKS_DEMAND_ACTIVATION_MNCP_AND_NP;
        return inactiveProperty.getRemarks() == null ? remarks
                : inactiveProperty.getRemarks().concat(", ").concat(remarks);
    }
}
