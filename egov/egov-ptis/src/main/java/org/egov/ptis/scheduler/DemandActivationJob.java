/*******************************************************************************
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
 * 	1) All versions of this program, verbatim or modified must carry this 
 * 	   Legal Notice.
 * 
 * 	2) Any misrepresentation of the origin of the material is prohibited. It 
 * 	   is required that all modified versions of this material be marked in 
 * 	   reasonable ways as different from the original version.
 * 
 * 	3) This license does not grant any rights to any user of the program 
 * 	   with regards to rights under trademark law for use of the trade names 
 * 	   or trademarks of eGovernments Foundation.
 * 
 *   In case of any queries, you can reach eGovernments Foundation at contact@egovernments.org
 ******************************************************************************/
package org.egov.ptis.scheduler;

import static org.egov.ptis.nmc.constants.NMCPTISConstants.DEMANDRSN_CODE_ADVANCE;

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
import org.egov.infstr.scheduler.quartz.AbstractQuartzJob;
import org.egov.infstr.services.PersistenceService;
import org.egov.infstr.utils.DateUtils;
import org.egov.infstr.utils.HibernateUtil;
import org.egov.ptis.constants.PropertyTaxConstants;
import org.egov.ptis.domain.entity.demand.Ptdemand;
import org.egov.ptis.domain.entity.property.BasicProperty;
import org.egov.ptis.domain.entity.property.PropertyImpl;
import org.egov.ptis.domain.service.property.PropertyService;
import org.egov.ptis.nmc.constants.NMCPTISConstants;
import org.egov.ptis.nmc.util.PropertyTaxUtil;
import org.joda.time.DateTime;
import org.quartz.StatefulJob;

/**
 * This job activates the demand after 21 days for the properties.
 *
 * This job will fire everyday at 12:15 AM
 *
 * @author nayeem
 *
 */
public class DemandActivationJob extends AbstractQuartzJob implements
		StatefulJob {

	private static final long serialVersionUID = 1L;
	private static final Logger LOGGER = Logger
			.getLogger(DemandActivationJob.class);
	private static final String STR_REMARKS_DEMAND_ACTIVATION = "Demand activated by system on 22nd day after notice generation";

	private PersistenceService basicPrpertyService;
	private PropertyService propertyService;
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
				adjustAdvancePayment(demand);
				activateDemand(basicProperty);

			} catch (Exception e) {
				LOGGER.error("Error while activating the demand for "
						+ basicProperty.getUpicNo(), e);
			}
		}

		LOGGER.info("Demand activation for " + properties.size()
				+ " properties is completed in "
				+ ((System.currentTimeMillis() - currentTimeMillis) / 1000)
				+ " sec(s)");

		LOGGER.debug("Exting from DemandActivationJob.execute");
	}

	private void adjustAdvancePayment(Ptdemand ptDemand) {
		LOGGER.debug("Entered into adjustAdvancePayment");

		EgDemandDetails advanceDemandDetail = getAdvanceDemandDetail(ptDemand);

		BigDecimal advanceAmount = advanceDemandDetail.getAmtCollected();
		BigDecimal balanceAmountToBePaid = BigDecimal.ZERO;
		boolean thereIsAdvanceCollection = advanceAmount
				.compareTo(BigDecimal.ZERO) > 0 ? true : false;

		List<Installment> installments = propertyTaxUtil
				.getInstallmentListByStartDate(ptDemand.getEgptProperty()
						.getPropertyDetail().getEffective_date());

		Map<Installment, Set<EgDemandDetails>> installmentWiseDemandDetails = propertyService
				.getEgDemandDetailsSetAsMap(new ArrayList<EgDemandDetails>(
						ptDemand.getEgDemandDetails()), installments);

		EgDemandDetails demandDetail = null;

		if (thereIsAdvanceCollection) {
			for (Installment installment : installments) {

				Map<String, EgDemandDetails> demandDetailsAndReason = propertyTaxUtil
						.getEgDemandDetailsAndReasonAsMap(installmentWiseDemandDetails
								.get(installment));

				for (String reason : NMCPTISConstants.DEMAND_RSNS_LIST) {

					demandDetail = demandDetailsAndReason.get(reason);

					if (demandDetail != null
							&& !demandDetail.getEgDemandReason()
									.getEgDemandReasonMaster().getCode()
									.equalsIgnoreCase(DEMANDRSN_CODE_ADVANCE)) {

						balanceAmountToBePaid = demandDetail.getAmount()
								.subtract(demandDetail.getAmtCollected());

						if (advanceAmount.compareTo(BigDecimal.ZERO) > 0
								&& balanceAmountToBePaid
										.compareTo(BigDecimal.ZERO) > 0) {
							if (advanceAmount.compareTo(balanceAmountToBePaid) > 0) {
								demandDetail.setAmtCollected(demandDetail
										.getAmtCollected().add(
												balanceAmountToBePaid));
								advanceAmount = advanceAmount
										.subtract(balanceAmountToBePaid);
							} else {
								demandDetail.setAmtCollected(demandDetail
										.getAmtCollected().add(advanceAmount));
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
			if (demandDetail.getEgDemandReason().getEgDemandReasonMaster()
					.getCode()
					.equalsIgnoreCase(NMCPTISConstants.DEMANDRSN_CODE_ADVANCE)) {
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
		inactiveProperty
				.setRemarks(inactiveProperty.getRemarks() == null ? STR_REMARKS_DEMAND_ACTIVATION
						: inactiveProperty.getRemarks().concat(", ")
								.concat(STR_REMARKS_DEMAND_ACTIVATION));
		inactiveProperty.setLastModifiedDate(new DateTime());

		basicPrpertyService.update(basicProperty);

		LOGGER.debug("Exiting from activateDemand");
	}

	@SuppressWarnings("unchecked")
	private List<Ptdemand> getInactiveDemandNotObjectedProperties() {
		LOGGER.debug("Entered into getQueryString");

		Date date21DaysPast = DateUtils.add(new Date(), Calendar.DAY_OF_MONTH,
				-21);

		StringBuilder noticeTypeBuilder = new StringBuilder().append("'")
				.append(NMCPTISConstants.NOTICE127).append("'").append(",")
				.append(" '").append(NMCPTISConstants.NOTICE134).append("'");

		StringBuilder noticePVR = new StringBuilder().append("'")
				.append(NMCPTISConstants.NOTICE_PRATIVRUTTA).append("'");

		String stringQuery = "SELECT ptd FROM PtNotice n, PtNotice pvr, Ptdemand ptd "
				+ "LEFT JOIN FETCH ptd.egptProperty p "
				+ "LEFT JOIN FETCH p.basicProperty bp "
				+ "WHERE n.basicProperty = bp "
				+ "AND pvr.basicProperty = bp "
				+ "AND bp.active = true "
				+ "AND bp.status.statusCode <> :bpStatus "
				+ "AND p.status = 'I' "
				+ "AND ptd.egInstallmentMaster = :currInstallment "
				+ "AND n.noticeType in ("
				+ noticeTypeBuilder.toString()
				+ ") "
				+ "AND pvr.noticeType = "
				+ noticePVR
				+ "AND n.noticeDate > p.createdDate "
				+ "AND pvr.noticeDate > p.createdDate "
				+ " AND n.noticeDate < :pastDate "
				+ "AND pvr.noticeDate < :pastDate ";

		LOGGER.debug("getQueryString, query=" + stringQuery);

		List<Ptdemand> properties = HibernateUtil
				.getCurrentSession()
				.createQuery(stringQuery)
				.setString("bpStatus", PropertyTaxConstants.STATUS_OBJECTED_STR)
				.setParameter("pastDate", date21DaysPast)
				.setEntity("currInstallment",
						PropertyTaxUtil.getCurrentInstallment()).list();

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
