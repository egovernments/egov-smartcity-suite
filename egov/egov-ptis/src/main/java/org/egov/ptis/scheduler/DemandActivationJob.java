package org.egov.ptis.scheduler;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.apache.log4j.Logger;
import org.egov.commons.Installment;
import org.egov.demand.model.EgDemandDetails;
import org.egov.infra.scheduler.quartz.AbstractQuartzJob;
import org.egov.infstr.services.PersistenceService;
import org.egov.infstr.utils.DateUtils;
import org.egov.infstr.utils.HibernateUtil;
import org.egov.ptis.client.util.PropertyTaxUtil;
import org.egov.ptis.constants.PropertyTaxConstants;
import org.egov.ptis.domain.entity.demand.Ptdemand;
import org.egov.ptis.domain.entity.property.BasicProperty;
import org.egov.ptis.domain.entity.property.PropertyImpl;
import org.egov.ptis.domain.service.property.PropertyService;
import org.quartz.DisallowConcurrentExecution;
import org.quartz.StatefulJob;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;

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

	private PersistenceService basicPrpertyService;
    @Autowired
    private PropertyService propertyService;
    @Autowired
    private PropertyTaxUtil propertyTaxUtil;

	@SuppressWarnings("unchecked")
	@Override
	public void executeJob() {
		LOGGER.debug("Entered into DemandActivationJob.execute");

		Long currentTimeMillis = System.currentTimeMillis();

		List<Ptdemand> properties = getInactiveDemandNotObjectedProperties();
		BasicProperty basicProperty = null;

		for (Ptdemand demand : properties) {
			try {
				basicProperty = demand.getEgptProperty().getBasicProperty();
				// adjustAdvancePayment(demand); -- as of now we do not have
				// rules to adjust advances
				activateDemand(basicProperty);
			} catch (Exception e) {
				LOGGER.error("Error while activating the demand for " + basicProperty.getUpicNo(), e);
			}
		}

		LOGGER.info("Demand activation for " + properties.size() + " properties is completed in "
				+ ((System.currentTimeMillis() - currentTimeMillis) / 1000) + " sec(s)");

		LOGGER.debug("Exting from DemandActivationJob.execute");
	}

	private void adjustAdvancePayment(Ptdemand ptDemand) {
		LOGGER.debug("Entered into adjustAdvancePayment");

		EgDemandDetails advanceDemandDetail = getAdvanceDemandDetail(ptDemand);

		BigDecimal advanceAmount = advanceDemandDetail.getAmtCollected();
		BigDecimal balanceAmountToBePaid = BigDecimal.ZERO;
		boolean thereIsAdvanceCollection = advanceAmount.compareTo(BigDecimal.ZERO) > 0 ? true : false;

		List<Installment> installments = propertyTaxUtil.getInstallmentListByStartDate(ptDemand.getEgptProperty()
				.getPropertyDetail().getEffectiveDate());

		Map<Installment, Set<EgDemandDetails>> installmentWiseDemandDetails = propertyService
				.getEgDemandDetailsSetAsMap(new ArrayList<EgDemandDetails>(ptDemand.getEgDemandDetails()), installments);

		EgDemandDetails demandDetail = null;

		if (thereIsAdvanceCollection) {
			for (Installment installment : installments) {

				Map<String, EgDemandDetails> demandDetailsAndReason = propertyTaxUtil
						.getEgDemandDetailsAndReasonAsMap(installmentWiseDemandDetails.get(installment));

				for (String reason : PropertyTaxConstants.DEMAND_RSNS_LIST) {

					demandDetail = demandDetailsAndReason.get(reason);

					if (demandDetail != null
							&& !demandDetail.getEgDemandReason().getEgDemandReasonMaster().getCode()
									.equalsIgnoreCase(PropertyTaxConstants.DEMANDRSN_CODE_ADVANCE)) {

						balanceAmountToBePaid = demandDetail.getAmount().subtract(demandDetail.getAmtCollected());

						if (advanceAmount.compareTo(BigDecimal.ZERO) > 0
								&& balanceAmountToBePaid.compareTo(BigDecimal.ZERO) > 0) {
							if (advanceAmount.compareTo(balanceAmountToBePaid) > 0) {
								demandDetail.setAmtCollected(demandDetail.getAmtCollected().add(balanceAmountToBePaid));
								advanceAmount = advanceAmount.subtract(balanceAmountToBePaid);
							} else {
								demandDetail.setAmtCollected(demandDetail.getAmtCollected().add(advanceAmount));
								advanceAmount = BigDecimal.ZERO;
							}
						}
					}
				}
			}
			advanceDemandDetail.setAmtCollected(advanceAmount);
		}

		LOGGER.debug("Exiting from adjustAdvancePayment");
	}

	/**
	 * @param ptDemand
	 */
	private EgDemandDetails getAdvanceDemandDetail(Ptdemand ptDemand) {
		EgDemandDetails advanceDemandDetail = null;

		for (EgDemandDetails demandDetail : ptDemand.getEgDemandDetails()) {
			if (demandDetail.getEgDemandReason().getEgDemandReasonMaster().getCode()
					.equalsIgnoreCase(PropertyTaxConstants.DEMANDRSN_CODE_ADVANCE)) {
				advanceDemandDetail = demandDetail;
				break;
			}
		}

		return advanceDemandDetail;
	}

	/**
	 * @param basicProperty
	 */
	private void activateDemand(BasicProperty basicProperty) {
		LOGGER.debug("Entered into activateDemand");

		PropertyImpl inactiveProperty = basicProperty.getInactiveProperty();

		inactiveProperty.setStatus(PropertyTaxConstants.STATUS_ISACTIVE);
		inactiveProperty.setRemarks(inactiveProperty.getRemarks() == null ? STR_REMARKS_DEMAND_ACTIVATION
				: inactiveProperty.getRemarks().concat(", ").concat(STR_REMARKS_DEMAND_ACTIVATION));
		inactiveProperty.setLastModifiedDate(new Date());

		basicPrpertyService.update(basicProperty);

		LOGGER.debug("Exiting from activateDemand");
	}

	@SuppressWarnings("unchecked")
	private List<Ptdemand> getInactiveDemandNotObjectedProperties() {
		LOGGER.debug("Entered into getQueryString");

		Date date15DaysPast = DateUtils.add(new Date(), Calendar.DAY_OF_MONTH, -15);

		String stringQuery = "SELECT ptd FROM PtNotice n, PtNotice pvr, Ptdemand ptd LEFT JOIN FETCH ptd.egptProperty p "
				+ "LEFT JOIN FETCH p.basicProperty bp WHERE n.basicProperty = bp AND pvr.basicProperty = bp AND bp.active = true "
				+ "AND bp.status.statusCode <> :bpStatus AND p.status = 'I' AND ptd.egInstallmentMaster = :currInstallment "
				+ "AND pvr.noticeType = :noticeType AND n.noticeDate > p.createdDate AND pvr.noticeDate > p.createdDate "
				+ "AND n.noticeDate < :pastDate AND pvr.noticeDate < :pastDate ";

		LOGGER.debug("getQueryString, query=" + stringQuery);

		List<Ptdemand> properties = HibernateUtil.getCurrentSession().createQuery(stringQuery)
				.setString("bpStatus", PropertyTaxConstants.STATUS_OBJECTED_STR)
				.setParameter("pastDate", date15DaysPast)
				.setString("noticeType", PropertyTaxConstants.NOTICE_TYPE_SPECIAL_NOTICE)
				.setEntity("currInstallment", PropertyTaxUtil.getCurrentInstallment()).list();

		LOGGER.debug("Exting from getQueryString");
		return properties;
	}

	public void setBasicPrpertyService(PersistenceService basicPrpertyService) {
		this.basicPrpertyService = basicPrpertyService;
	}

	public void setPropertyService(PropertyService propertyService) {
		this.propertyService = propertyService;
	}

	public void setPropertyTaxUtil(PropertyTaxUtil propertyTaxUtil) {
		this.propertyTaxUtil = propertyTaxUtil;
	}
}