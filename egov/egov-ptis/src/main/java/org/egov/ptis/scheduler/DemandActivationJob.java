package org.egov.ptis.scheduler;

import org.apache.log4j.Logger;
import org.egov.demand.model.EgDemandDetails;
import org.egov.infra.scheduler.quartz.AbstractQuartzJob;
import org.egov.infra.utils.DateUtils;
import org.egov.infstr.utils.HibernateUtil;
import org.egov.ptis.client.util.PropertyTaxUtil;
import org.egov.ptis.constants.PropertyTaxConstants;
import org.egov.ptis.domain.entity.demand.Ptdemand;
import org.egov.ptis.domain.entity.property.BasicProperty;
import org.egov.ptis.domain.entity.property.PropertyImpl;
import org.egov.ptis.domain.service.property.PropertyPersistenceService;
import org.quartz.DisallowConcurrentExecution;
import org.quartz.StatefulJob;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;

import java.util.Calendar;
import java.util.Date;
import java.util.List;

/**
 * This job activates the demand after 21 days for the properties.
 *
 * This job will fire everyday at 12:15 AM
 *
 * @author Ramki
 *
 */
@Transactional
@SuppressWarnings("unchecked")
@DisallowConcurrentExecution
public class DemandActivationJob extends AbstractQuartzJob implements StatefulJob {

    private static final long serialVersionUID = 1L;
    private static final Logger LOGGER = Logger.getLogger(DemandActivationJob.class);
    private static final String STR_REMARKS_DEMAND_ACTIVATION = "Demand activated by system on 15thd day after notice generation";

    @Autowired
    private PropertyTaxUtil propertyTaxUtil;
    @Autowired
    private PropertyPersistenceService basicPropertyService;

    @SuppressWarnings("unchecked")
    @Override
    public void executeJob() {
        LOGGER.debug("Entered into DemandActivationJob.execute");
 
        final Long currentTimeMillis = System.currentTimeMillis();

        final List<Ptdemand> properties = getInactiveDemandNotObjectedProperties();
        BasicProperty basicProperty = null;

        for (final Ptdemand demand : properties)
            try {
                basicProperty = demand.getEgptProperty().getBasicProperty();
                // adjustAdvancePayment(demand); -- as of now we do not have
                // rules to adjust advances
                activateDemand(basicProperty);
            } catch (final Exception e) {
                LOGGER.error("Error while activating the demand for " + basicProperty.getUpicNo(), e);
            }

        LOGGER.info("Demand activation for " + properties.size() + " properties is completed in "
                + (System.currentTimeMillis() - currentTimeMillis) / 1000 + " sec(s)");

        LOGGER.debug("Exting from DemandActivationJob.execute");
    }

    /**
     * @param ptDemand
     */
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

    /**
     * @param basicProperty
     */
    private void activateDemand(final BasicProperty basicProperty) {
        LOGGER.debug("Entered into activateDemand");

        final PropertyImpl inactiveProperty = basicProperty.getInactiveProperty();

        inactiveProperty.setStatus(PropertyTaxConstants.STATUS_ISACTIVE);
        inactiveProperty.setRemarks(inactiveProperty.getRemarks() == null ? STR_REMARKS_DEMAND_ACTIVATION
                : inactiveProperty.getRemarks().concat(", ").concat(STR_REMARKS_DEMAND_ACTIVATION));
        inactiveProperty.setLastModifiedDate(new Date());
        basicPropertyService.merge(basicProperty);

        LOGGER.debug("Exiting from activateDemand");
    }

    @SuppressWarnings("unchecked")
    private List<Ptdemand> getInactiveDemandNotObjectedProperties() {
        LOGGER.debug("Entered into getQueryString");

        final Date date15DaysPast = DateUtils.add(new Date(), Calendar.DAY_OF_MONTH, -15);

        final String stringQuery = "SELECT ptd FROM PtNotice n, PtNotice pvr, Ptdemand ptd LEFT JOIN FETCH ptd.egptProperty p "
                + "LEFT JOIN FETCH p.basicProperty bp WHERE n.basicProperty = bp AND pvr.basicProperty = bp AND bp.active = true "
                + "AND bp.status.statusCode <> :bpStatus AND p.status = 'I' AND ptd.egInstallmentMaster = :currInstallment "
                + "AND pvr.noticeType = :noticeType AND n.noticeDate > p.createdDate AND pvr.noticeDate > p.createdDate "
                + "AND n.noticeDate < :pastDate AND pvr.noticeDate < :pastDate ";

        LOGGER.debug("getQueryString, query=" + stringQuery);

        final List<Ptdemand> properties = HibernateUtil.getCurrentSession().createQuery(stringQuery)
                .setString("bpStatus", PropertyTaxConstants.STATUS_OBJECTED_STR)
                .setParameter("pastDate", date15DaysPast)
                .setString("noticeType", PropertyTaxConstants.NOTICE_TYPE_SPECIAL_NOTICE)
                .setEntity("currInstallment", PropertyTaxUtil.getCurrentInstallment()).list();

        LOGGER.debug("Exting from getQueryString");
        return properties;
    }

}