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
package org.egov.ptis.client.service;

import org.egov.commons.Installment;
import org.egov.demand.model.EgDemand;
import org.egov.demand.model.EgDemandDetails;
import org.egov.infra.exception.ApplicationRuntimeException;
import org.egov.infra.utils.DateUtils;
import org.egov.infra.utils.MoneyUtils;
import org.egov.infra.validation.exception.ValidationException;
import org.egov.ptis.client.bill.PenaltyBill;
import org.egov.ptis.client.util.PropertyTaxUtil;
import org.egov.ptis.constants.PropertyTaxConstants;
import org.egov.ptis.domain.entity.demand.Ptdemand;
import org.egov.ptis.domain.entity.property.BasicProperty;
import org.egov.ptis.domain.entity.property.Property;
import org.egov.ptis.domain.entity.property.RebatePeriod;
import org.egov.ptis.domain.service.property.RebatePeriodService;
import org.egov.ptis.notice.PtNotice;
import org.egov.ptis.service.utils.PropertyTaxCommonUtils;
import org.hibernate.Session;
import org.joda.time.DateTime;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

import java.math.BigDecimal;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

import static org.egov.ptis.constants.PropertyTaxConstants.ARR_LP_DATE_BREAKUP;
import static org.egov.ptis.constants.PropertyTaxConstants.ARR_LP_DATE_CONSTANT;
import static org.egov.ptis.constants.PropertyTaxConstants.BILLTYPE_MANUAL;
import static org.egov.ptis.constants.PropertyTaxConstants.DEMANDRSN_CODE_PENALTY_FINES;
import static org.egov.ptis.constants.PropertyTaxConstants.LP_PERCENTAGE_CONSTANT;
import static org.egov.ptis.constants.PropertyTaxConstants.NOTICE127;
import static org.egov.ptis.constants.PropertyTaxConstants.NOTICE134;
import static org.egov.ptis.constants.PropertyTaxConstants.NOTICE_PRATIVRUTTA;
import static org.egov.ptis.constants.PropertyTaxConstants.NOTICE_TYPE_BILL;

/**
 * Provieds api's for penalty calculation
 * 
 * @author nayeem
 */
@Service
@Transactional(readOnly = true)
public class PenaltyCalculationService {

    private Logger LOGGER = LoggerFactory.getLogger(PenaltyCalculationService.class);

    @Autowired
    private PropertyTaxUtil propertyTaxUtil;
    private SimpleDateFormat dateFormatter = new SimpleDateFormat("dd/MM/yyyy");
    private static final BigDecimal VALUE_HUNDRED = new BigDecimal(100);

    private BasicProperty basicProperty;
    private Map<Installment, BigDecimal> installmentWiseDemand;
    private Map<Installment, BigDecimal> installmentWiseCollection;
    private Map<String, Date> installmentAndLatestCollDate;
    private Map<Installment, EgDemandDetails> installmentWisePenaltyDemandDetail;

    @Autowired
    private RebatePeriodService rebatePeriodService;

    @PersistenceContext
    private EntityManager entityManager;

    @Autowired
    private PropertyTaxCommonUtils propertyTaxCommonUtils;

    public PenaltyCalculationService() {
    }

    public PenaltyCalculationService(BasicProperty basicProperty, Map<Installment, BigDecimal> installmentWiseDemand,
            Map<Installment, BigDecimal> installmentWiseCollection, Map<String, Date> installmentAndLatestCollDate,
            Map<Installment, EgDemandDetails> installmentWisePenaltyDemandDetail) {

        this.basicProperty = basicProperty;
        this.installmentWiseDemand = installmentWiseDemand;
        this.installmentWiseCollection = installmentWiseCollection;
        this.installmentAndLatestCollDate = installmentAndLatestCollDate;
        this.installmentWisePenaltyDemandDetail = installmentWisePenaltyDemandDetail;
    }

    public Map<Installment, BigDecimal> getInstallmentWisePenalty() throws ValidationException {

        LOGGER.debug("Entered into getInstallmentWisePenalty, basicProperty={}", basicProperty);

        Map<Installment, BigDecimal> installmentWisePenalty = new TreeMap<Installment, BigDecimal>();

        Map<Installment, Date> installmentWisePenaltyEffectiveDates = getPenaltyEffectiveDates(basicProperty,
                new ArrayList<Installment>(installmentWiseDemand.keySet()));

        if (installmentWisePenaltyEffectiveDates.isEmpty()) {
            LOGGER.debug("getInstallmentWisePenalty - installmentWisePenaltyEffectiveDates is empty");
            return null;
        }

        LOGGER.debug("getInstallmentWisePenalty - installmentWisePenaltyEffectiveDates={}",
                installmentWisePenaltyEffectiveDates);

        Installment installment = null;
        BigDecimal penalty = BigDecimal.ZERO;
        BigDecimal balancePenalty = BigDecimal.ZERO;
        EgDemandDetails penaltyDemandDetail = null;

        Amount balance = null;
        Amount installmentDemand = null;
        Amount installmentCollection = null;

        DateTime installmentLatestCollectionDate = null;
        DateTime today = new DateTime();

        for (Map.Entry<Installment, Date> mapEntry : installmentWisePenaltyEffectiveDates.entrySet()) {

            installment = mapEntry.getKey();
            penalty = BigDecimal.ZERO;
            installmentDemand = new Amount(installmentWiseDemand.get(installment));
            installmentCollection = new Amount(installmentWiseCollection.get(installment));
            installmentLatestCollectionDate = new DateTime(installmentAndLatestCollDate.get(installment));

            if (!installmentWisePenaltyDemandDetail.isEmpty()) {
                penaltyDemandDetail = installmentWisePenaltyDemandDetail.get(installment);
            }

            if (mapEntry.getValue() != null) {

                balance = installmentDemand.minus(installmentWiseCollection.get(installment));

                if (balance.isGreaterThanZero()) {

                    /*
                     * if (installmentCollection.isZero()) { penalty =
                     * calculatePenalty(mapEntry.getValue(),
                     * balance.getAmount()); } else {
                     */

                    if (penaltyDemandDetail == null || installmentLatestCollectionDate == null) {

                        LOGGER.debug(
                                "getInstallmentWisePenalty - Penalty demand detail / collection date is null for {} ",
                                installment);
                        penalty = calculatePenalty(mapEntry.getValue(), balance.getAmount());

                    } else {

                        if (penaltyDemandDetail.getAmtCollected().compareTo(BigDecimal.ZERO) > 0) {
                            balancePenalty = penaltyDemandDetail.getAmount().subtract(
                                    penaltyDemandDetail.getAmtCollected());
                        }

                        if (installmentLatestCollectionDate.getMonthOfYear() == today.getMonthOfYear()
                                && installmentLatestCollectionDate.getYear() == today.getYear()) {

                            penalty = balancePenalty.compareTo(BigDecimal.ZERO) > 0 ? balancePenalty
                                    : calculatePenalty(mapEntry.getValue(), balance.getAmount());

                        } else {
                            penalty = calculatePenalty(mapEntry.getValue(), balance.getAmount()).add(balancePenalty);
                        }

                    }
                    // }
                }
            }

            installmentWisePenalty.put(mapEntry.getKey(), penalty);
        }

        LOGGER.debug("getInstallmentWisePenalty, installmentWisePenalty={}", installmentWisePenalty);
        LOGGER.debug("Exiting from getInstallmentWisePenalty");

        return installmentWisePenalty;
    }

    /**
     * Excludes if there is water tax before the installment 2011-12
     * (exclusive).
     * <p>
     * Water tax must be considered only after 01/01/2012 for penalty
     * calculation
     * </p>
     * 
     * @param installment
     * @param balanceDemand
     * @param waterTax
     * @return
     */
    private BigDecimal excludeWaterTax(Installment installment, BigDecimal balanceDemand, BigDecimal waterTax) {
        BigDecimal demand = balanceDemand;
        Date waterTaxEffectiveDate = PropertyTaxUtil.getWaterTaxEffectiveDateForPenalty();

        if (installment.getFromDate().before(waterTaxEffectiveDate)
                && !propertyTaxUtil.between(waterTaxEffectiveDate, installment.getFromDate(), installment.getToDate())) {
            demand = demand.subtract(waterTax);
        }

        return demand;
    }

    /**
     * Calculates the penalty from the effective date to the current date
     * 
     * @param effectiveFrom
     * @param tax
     * @return
     */
    public BigDecimal calculatePenalty(Date effectiveFrom, BigDecimal tax) {
        LOGGER.debug("Entered into calculatePenalty, effectiveFrom={} , tax={} ", effectiveFrom, tax);

        Integer noOfMonths = PropertyTaxUtil.getMonthsBetweenDates(effectiveFrom, new Date());

        BigDecimal penalty = BigDecimal.ZERO;

        if (noOfMonths > 0) {
            penalty = tax.multiply(LP_PERCENTAGE_CONSTANT).divide(VALUE_HUNDRED)
                    .multiply(BigDecimal.valueOf(noOfMonths));
        }

        LOGGER.debug("calcPanalty - before rounding -  noOfMonths={}, penalty={}", noOfMonths, penalty);

        penalty = MoneyUtils.roundOff(penalty);

        LOGGER.debug("calcPanalty - after rounding -  noOfMonths={}, penalty={}", noOfMonths, penalty);

        LOGGER.debug("Exiting from calculatePenalty");
        return penalty;

    }

    /**
     * True if the 90 days notice after bill generation is over
     * 
     * @return
     */
    private Map<Installment, Date> getPenaltyEffectiveDates(BasicProperty basicProperty, List<Installment> installments)
            throws ValidationException {
        LOGGER.debug("Entered into getPenaltyEffectiveDates, basicProperty={}, installments={}", basicProperty,
                installments);

        Map<Installment, Date> installmentWisePenaltyEffectiveDate = new TreeMap<Installment, Date>();

        // get the 22nd day after expiry of 21 days notice for basicProperty
        Date date22ndDay = get22ndDayDateAfter21DaysNoticeExpiry(basicProperty.getUpicNo());

        if (date22ndDay == null) {
            LOGGER.debug(" getPenaltyEffectiveDates, date22ndDay is null ");
        }

        Date earliestModificationDate = null;

        if (!basicProperty.getSource().equals(PropertyTaxConstants.SOURCEOFDATA_APPLICATION)) {
            earliestModificationDate = propertyTaxUtil.getEarliestModificationDate(basicProperty.getUpicNo());
            LOGGER.debug(" getPenaltyEffectiveDates, earliestModificationDate={}", earliestModificationDate);
        }

        // get the bill dates installment wise
        Map<Installment, Date> installmentAndBillDate = getAllBillGenerationDates(basicProperty, installments);

        if (installmentAndBillDate != null && !installmentAndBillDate.isEmpty()) {
            // take the later date of 22nd date or bill date
            Map<Installment, Date> installmentAnd90DaysDate = get90DaysDate(installmentAndBillDate, date22ndDay);

            LOGGER.debug("getPenaltyEffectiveDates - installmentAndLatestCollDate={}", installmentAndLatestCollDate);

            // payment date should not be between the 90 days period
            Date latestCollectionDate = null;

            Installment installment = null;
            Date dateOn91stDay = null;
            Date penaltyEffectiveDate = null;

            for (Map.Entry<Installment, Date> mapEntry : installmentAnd90DaysDate.entrySet()) {

                installment = mapEntry.getKey();
                dateOn91stDay = mapEntry.getValue();
                penaltyEffectiveDate = null;

                if (earliestModificationDate != null && installment.getFromDate().before(earliestModificationDate)
                        && installment.getToDate().before(earliestModificationDate)) {
                    continue;
                }

                latestCollectionDate = installmentAndLatestCollDate.get(installment.getDescription());

                if (dateOn91stDay == null) {
                    if (isRolloverInstallment(installment)) {
                        penaltyEffectiveDate = getPenaltyEffectiveForRolloverInstallment(latestCollectionDate,
                                installment.getFromDate());
                    }
                } else {
                    if (latestCollectionDate != null && latestCollectionDate.after(dateOn91stDay)) {
                        penaltyEffectiveDate = getPenaltyEffectiveForRolloverInstallment(latestCollectionDate,
                                installment.getFromDate());
                    } else {
                        penaltyEffectiveDate = getPenaltyEffectiveDate(dateOn91stDay);
                    }
                }

                installmentWisePenaltyEffectiveDate.put(installment, penaltyEffectiveDate);
            }
        }

        LOGGER.debug("getPenaltyEffectiveDates - installmentWisePenaltyEffectiveDate={}",
                installmentWisePenaltyEffectiveDate);
        LOGGER.debug("Exiting from getPenaltyEffectiveDates");

        return installmentWisePenaltyEffectiveDate;
    }

    /**
     * Gives installment wise bill generation date
     * 
     * @param basicProperty
     * @return map of installment and bill generation date pair
     */
    @SuppressWarnings("unchecked")
    public Map<Installment, Date> getAllBillGenerationDates(BasicProperty basicProperty, List<Installment> installments)
            throws ValidationException {
        Map<Installment, Date> installmentAndBillDate = new TreeMap<Installment, Date>();
        String upicNo = basicProperty.getUpicNo();
        String noBillMessage = "Bill is not available penalty calculation for " + upicNo;

        String query = "select notice from EgBill bill, PtNotice notice left join notice.basicProperty bp "
                + "where bill.is_Cancelled = 'N' and bill.egBillType.code = :billTypeCode and bill.billNo = notice.noticeNo "
                + "and notice.noticeType = :noticeType and bp.upicNo = :upicNo order by notice.noticeDate";

        List<PtNotice> demandBills = entityManager.unwrap(Session.class).createQuery(query)
                .setString("billTypeCode", BILLTYPE_MANUAL).setString("noticeType", NOTICE_TYPE_BILL)
                .setString("upicNo", upicNo).list();

        if (demandBills.isEmpty()) {
            LOGGER.debug("getAllBillGenerationDates - {}", noBillMessage);
            // throw new ValidationException(Arrays.asList(new
            // ValidationError(noBillMessage , noBillMessage)));
        }

        Map<Date, Date> propertyCreatedAndOccupancyDate = new TreeMap<Date, Date>();
        List<Date> propertySystemCreatedDates = new ArrayList<Date>();
        List<Date> occupancyDates = new ArrayList<Date>();

        for (Map.Entry<Date, Property> entry : propertyTaxUtil.getPropertiesForPenlatyCalculation(basicProperty)
                .entrySet()) {
            propertySystemCreatedDates.add(entry.getValue().getCreatedDate());
            propertyCreatedAndOccupancyDate.put(entry.getValue().getCreatedDate(), entry.getKey());
        }

        occupancyDates = new ArrayList<Date>(propertyCreatedAndOccupancyDate.values());

        // Collections.sort(propertySystemCreatedDates);
        // Collections.sort(occupancyDates);

        LOGGER.info("getAllBillGenerationDates - propertySystemCreatedDates=" + propertySystemCreatedDates
                + ", occupancyDates=" + occupancyDates);

        int noOfEffects = propertySystemCreatedDates.size();
        Date createdDate = null;
        Date occupancyDate = null;
        Date nextOccupancyDate = null;
        Date nextCreatedDate = null;
        Date billDate = null;

        int j = 0;

        List<PenaltyBill> billDetails = new ArrayList<PenaltyBill>();

        Calendar fromDateCalendar = Calendar.getInstance();
        Calendar billDateCalendar = Calendar.getInstance();

        fromDateCalendar.set(Calendar.DAY_OF_MONTH, 1);
        fromDateCalendar.set(Calendar.MONTH, 3);
        fromDateCalendar.set(Calendar.HOUR_OF_DAY, 00);
        fromDateCalendar.set(Calendar.MINUTE, 00);
        fromDateCalendar.set(Calendar.SECOND, 00);
        fromDateCalendar.set(Calendar.MILLISECOND, 0);

        if (!demandBills.isEmpty()) {
            for (int i = 0; i < noOfEffects; i++) {
                createdDate = propertySystemCreatedDates.get(i);
                occupancyDate = occupancyDates.get(i);
                nextCreatedDate = i != (noOfEffects - 1) ? propertySystemCreatedDates.get(i + 1) : null;

                if (demandBills.isEmpty()) {
                    break;
                } else {
                    billDate = demandBills.get(j).getNoticeDate();
                }

                if (billDate.before(createdDate)) {
                    // bill needs to be skipped as there was modification
                    // effective from same previous modification effective
                    // date
                    demandBills.remove(j);
                    i--;
                    continue;
                } else if (PropertyTaxUtil.afterOrEqual(billDate, createdDate)
                        && (nextCreatedDate == null || billDate.before(nextCreatedDate))) {
                    billDetails.add(new PenaltyBill(createdDate, occupancyDate, billDate, false));
                    demandBills.remove(j);
                }
            }
        }

        j = 0;

        // if there are more bills as compared to bills generated after
        // modification
        if (!demandBills.isEmpty()) {
            for (j = 0; j < demandBills.size(); j++) {
                billDate = demandBills.get(j).getNoticeDate();
                billDateCalendar.setTime(billDate);
                fromDateCalendar.set(Calendar.YEAR, billDateCalendar.get(Calendar.YEAR));
                billDetails
                        .add(new PenaltyBill(fromDateCalendar.getTime(), fromDateCalendar.getTime(), billDate, true));
            }
        }

        Collections.sort(billDetails, new Comparator<PenaltyBill>() {

            @Override
            public int compare(PenaltyBill bill1, PenaltyBill bill2) {
                return bill1.getBillDate().compareTo(bill2.getBillDate());
            }

        });

        int totalBills = billDetails.size();

        DateFormat dateFormat = new SimpleDateFormat(PropertyTaxConstants.DATE_FORMAT_DDMMYYY);
        for (int i = 0; i < totalBills; i++) {

            createdDate = billDetails.get(i).getCreatedDate();
            occupancyDate = billDetails.get(i).getOccupancyDate();
            nextOccupancyDate = i == (totalBills - 1) ? null : billDetails.get(i + 1).getOccupancyDate();

            for (Installment installment : installments) {

                if (billDetails.get(i).getIsBillGeneratedAfterRollover()) {
                    if (dateFormat.format(installment.getFromDate()).equalsIgnoreCase(dateFormat.format(createdDate))) {
                        installmentAndBillDate.put(installment, null);
                    }
                } else {
                    if (nextOccupancyDate != null
                            && propertyTaxUtil.between(nextOccupancyDate, installment.getFromDate(),
                                    installment.getToDate())) {
                        break;
                    }

                    if (PropertyTaxUtil.afterOrEqual(createdDate, installment.getFromDate())
                            && !billDetails.get(i).getIsBillGeneratedAfterRollover()) {
                        if ((propertyTaxUtil.between(occupancyDate, installment.getFromDate(), installment.getToDate()) || PropertyTaxUtil
                                .afterOrEqual(installment.getFromDate(), occupancyDate))
                                && (nextOccupancyDate == null || installment.getFromDate().before(nextOccupancyDate))) {
                            installmentAndBillDate.put(installment, billDetails.get(i).getBillDate());
                        }
                    }
                }
            }
        }

        // this is the for the installments for which bills not there, ex. after
        // rollover, if no bill is generated
        for (Installment installment : installments) {
            if (installmentAndBillDate.get(installment) == null) {
                installmentAndBillDate.put(installment, null);
            }
        }

        LOGGER.info("getAllBillGenerationDates - installmentAndBillDate={}", installmentAndBillDate);
        return installmentAndBillDate;

    }

    /**
     * Gives the applicable demand bill for penalty calculation
     * 
     * @param demandBills
     * @param createdDate
     * @param nextCreatedDate
     * @param billToRemoved
     * @param billIndex
     * @param bill
     * @return
     */
    private Date getApplicableBillDate(int i, int j, Installment installment, List<Date> createdDates,
            List<Date> occupancyDates, List<PtNotice> demandBills) {

        Date applicableBillDate = null;
        Date createdDate = createdDates.get(i);
        Date nextCreatedDate = i != (createdDates.size() - 1) ? createdDates.get(i + 1) : null;
        Date occupancyDate = occupancyDates.get(i);
        Date nextOccupancyDate = nextCreatedDate == null ? null : occupancyDates.get(i + 1);
        Date billDate = demandBills.get(j).getNoticeDate();
        Date nextBillDate = j != (demandBills.size() - 1) ? demandBills.get(j + 1).getNoticeDate() : null;

        if (nextBillDate == null) {
            applicableBillDate = billDate;
        } else {
            if (!propertyTaxUtil.between(nextBillDate, installment.getFromDate(), installment.getToDate())) {

                if (PropertyTaxUtil.afterOrEqual(billDate, createdDate) && billDate.before(nextCreatedDate)) {

                    if (installment.getFromDate().before(billDate)
                            || propertyTaxUtil.between(billDate, installment.getFromDate(), installment.getToDate())) {

                        if (PropertyTaxUtil.afterOrEqual(installment.getFromDate(), occupancyDate)
                                && (nextOccupancyDate == null || installment.getFromDate().before(nextOccupancyDate))) {
                            applicableBillDate = billDate;
                            createdDates.remove(i);
                            occupancyDates.remove(i);
                            demandBills.remove(j);
                        }

                    }
                }
            }
        }

        return applicableBillDate;

    }

    public Map<Installment, Date> get90DaysDate(Map<Installment, Date> installmentAndBillDate, Date dateAfter21Days) {
        Map<Installment, Date> installmentAnd90DaysDate = new TreeMap<Installment, Date>();

        Date date = null;
        Date dateOn91stDay = null;

        for (Map.Entry<Installment, Date> mapEntry : installmentAndBillDate.entrySet()) {

            date = mapEntry.getValue();
            dateOn91stDay = null;

            if (date != null) {
                date = dateAfter21Days == null ? date : dateAfter21Days.after(date) ? dateAfter21Days : date;
                dateOn91stDay = DateUtils.add(date, Calendar.DAY_OF_MONTH, 91);
            }

            installmentAnd90DaysDate.put(mapEntry.getKey(), dateOn91stDay);
        }

        return installmentAnd90DaysDate;
    }

    /**
     * @param basicProperty
     * @return
     */
    public Date get22ndDayDateAfter21DaysNoticeExpiry(String propertyId) {
        LOGGER.debug("Entered into get22ndDayDateAfter21DaysNoticeExpiry, propertyId={}", propertyId);

        String notice127And134 = "'" + NOTICE127 + "', '" + NOTICE134 + "'";

        String stringQuery = "select to_char(n.noticeDate, 'dd/mm/yyyy'), to_char(pvr.noticeDate, 'dd/mm/yyyy'), to_char(p.createdDate, 'dd/mm/yyyy') "
                + "from PtNotice n, PtNotice pvr, PropertyImpl p left join p.basicProperty bp "
                + "where n.basicProperty = bp "
                + "and pvr.basicProperty = bp "
                + "and (p.status = 'A' or p.status = 'I') "
                + "and bp.upicNo = :upicNo "
                + "and bp.status.statusCode <> :bpStatus "
                + "and n.noticeType in ("
                + notice127And134
                + ") "
                + "and pvr.noticeType = :noticePVR "
                + "and n.noticeDate > p.createdDate "
                + "and pvr.noticeDate > p.createdDate "
                + " and n.noticeDate is not null "
                + "and pvr.noticeDate is not null ";

        List result = entityManager.unwrap(Session.class).createQuery(stringQuery).setString("upicNo", propertyId)
                .setString("bpStatus", PropertyTaxConstants.STATUS_OBJECTED_STR)
                .setString("noticePVR", NOTICE_PRATIVRUTTA).list();

        if (result.isEmpty()) {
            return null;
        }

        Date noticeDate = null;
        Date pvrDate = null;

        Object row = result.get(0);
        Object[] columnValues = (Object[]) row;

        try {
            noticeDate = dateFormatter.parse((String) columnValues[0]);
            pvrDate = dateFormatter.parse((String) columnValues[1]);
        } catch (ParseException pe) {
            LOGGER.error("Error while parsing notice/pvr date", pe);
            throw new ApplicationRuntimeException("Error while parsing notice/pvr date", pe);
        }

        Date dateOn22ndDay = DateUtils.add((noticeDate.before(pvrDate) ? pvrDate : noticeDate), Calendar.DAY_OF_MONTH,
                21);

        LOGGER.debug("get22ndDayDateAfter21DaysNoticeExpiry, dateOn22ndDay={}", dateOn22ndDay);
        LOGGER.debug("Exiting from get22ndDayDateAfter21DaysNoticeExpiry");
        return dateOn22ndDay;
    }

    public Date getPenaltyEffectiveDate(Date dateAfterNoticeDaysExpiry) {
        LOGGER.debug("Entered into getPenaltyEffectiveDate, dateAfterNoticeDaysExpiry={}", dateAfterNoticeDaysExpiry);

        Date penaltyEffectiveDate = null;

        Calendar calendar = Calendar.getInstance();
        calendar.setTime(dateAfterNoticeDaysExpiry);

        if (calendar.get(Calendar.DAY_OF_MONTH) <= 15) {
            penaltyEffectiveDate = dateAfterNoticeDaysExpiry;
        } else {
            calendar.add(Calendar.MONTH, 1);
            penaltyEffectiveDate = calendar.getTime();
        }

        LOGGER.debug("getPenaltyEffectiveDate, penaltyEffectiveDate={}", penaltyEffectiveDate);
        LOGGER.debug("Exting from getPenaltyEffectiveDate");

        return penaltyEffectiveDate;
    }

    /**
     * Gives the date to calculate the penalty from.
     * 
     * @param latestCollReceiptDate
     * @param fromDate
     * @return date from which penalty will be applied
     */
    public Date getPenaltyEffectiveForRolloverInstallment(Date latestCollReceiptDate, Date fromDate) {
        LOGGER.info("Enter into calcPanalty - latestCollReceiptDate: " + latestCollReceiptDate + ", fromDate "
                + fromDate);
        Calendar fromDateCalendar = Calendar.getInstance();
        fromDateCalendar.setTime(fromDate);
        Calendar arrearsPenaltyApplicableDate = Calendar.getInstance();
        arrearsPenaltyApplicableDate.set(Calendar.DAY_OF_MONTH, 1);
        arrearsPenaltyApplicableDate.set(Calendar.MONTH, Calendar.JANUARY);
        arrearsPenaltyApplicableDate.set(Calendar.HOUR_OF_DAY, 00);
        arrearsPenaltyApplicableDate.set(Calendar.MINUTE, 00);
        arrearsPenaltyApplicableDate.set(Calendar.SECOND, 00);

        Calendar latestCollRcptCalendar = Calendar.getInstance();
        if (latestCollReceiptDate != null) {
            latestCollRcptCalendar.setTime(latestCollReceiptDate);
        }

        DateFormat dateFormat = new SimpleDateFormat(PropertyTaxConstants.DATE_FORMAT_DDMMYYY);
        Date arrearlpDate = null;
        Date arrearlpDateBreakup = null;
        Date frmDate = null;
        try {
            arrearlpDate = dateFormat.parse(ARR_LP_DATE_CONSTANT);
            arrearlpDateBreakup = dateFormat.parse(ARR_LP_DATE_BREAKUP);
            frmDate = dateFormat.parse(dateFormat.format(fromDate));
        } catch (ParseException e) {
            LOGGER.error("Error while parsing Arrear Late Payment penalty dates", e);
        }

        Date penaltyFromDate = null;

        if (latestCollReceiptDate != null && latestCollReceiptDate.after(frmDate)
                && latestCollReceiptDate.after(arrearlpDateBreakup)) {

            latestCollRcptCalendar.add(Calendar.MONTH, 1);
            arrearsPenaltyApplicableDate.setTime(latestCollRcptCalendar.getTime());
            penaltyFromDate = arrearsPenaltyApplicableDate.getTime();

        } else if (frmDate.after(arrearlpDateBreakup) || frmDate.equals(arrearlpDateBreakup)) {
            arrearsPenaltyApplicableDate.set(Calendar.YEAR, (fromDateCalendar.get(Calendar.YEAR) + 1));
            penaltyFromDate = arrearsPenaltyApplicableDate.getTime();
        } else {
            penaltyFromDate = arrearlpDate;
        }

        LOGGER.info("calcPanalty - penaltyFromDate: " + penaltyFromDate);
        return penaltyFromDate;
    }

    /**
     * Returns true if the installment passed is a rollover installment else
     * returns false
     * 
     * @param installment
     * @return true if the installment passed is a rollover installment
     */
    private boolean isRolloverInstallment(Installment installment) {
        Date systemCreatedDate = basicProperty.getProperty().getCreatedDate();
        return propertyTaxUtil.between(new Date(), installment.getFromDate(), installment.getToDate())
                && !propertyTaxUtil.between(systemCreatedDate, installment.getFromDate(), installment.getToDate());
    }

    public Map<String, Map<Installment, BigDecimal>> getInstallmentDemandAndCollection(BasicProperty basicProperty,
            EgDemand currentDemand) {

        Map<String, Map<Installment, BigDecimal>> installmentDemandAndCollection = new TreeMap<String, Map<Installment, BigDecimal>>();
        Property property = null;
        property = basicProperty.getProperty();
        final Installment currentInstall = currentDemand.getEgInstallmentMaster();
        installmentDemandAndCollection = propertyTaxUtil.prepareReasonWiseDenandAndCollection(property, currentInstall);
        return installmentDemandAndCollection;
    }

    public Map<Installment, EgDemandDetails> getInstallmentWisePenaltyDemandDetails(final Property property,
            EgDemand currentDemand) {
        final Map<Installment, EgDemandDetails> installmentWisePenaltyDemandDetails = new TreeMap<Installment, EgDemandDetails>();
        for (final EgDemandDetails dmdDet : currentDemand.getEgDemandDetails()) {
            if (dmdDet.getEgDemandReason().getEgDemandReasonMaster().getCode()
                    .equalsIgnoreCase(DEMANDRSN_CODE_PENALTY_FINES)
                    && dmdDet.getAmount().compareTo(BigDecimal.ZERO) > 0)
                installmentWisePenaltyDemandDetails.put(dmdDet.getEgDemandReason().getEgInstallmentMaster(), dmdDet);
        }
        return installmentWisePenaltyDemandDetails;
    }

    public void setPropertyTaxUtil(PropertyTaxUtil propertyTaxUtil) {
        this.propertyTaxUtil = propertyTaxUtil;
    }

}
