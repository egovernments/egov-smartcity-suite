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
package org.egov.ptis.domain.service.bill;

import org.apache.log4j.Logger;
import org.egov.commons.CFinancialYear;
import org.egov.commons.Installment;
import org.egov.commons.dao.FinancialYearDAO;
import org.egov.demand.model.EgBill;
import org.egov.infra.admin.master.entity.City;
import org.egov.infra.admin.master.entity.Module;
import org.egov.infra.admin.master.service.BoundaryService;
import org.egov.infra.admin.master.service.CityService;
import org.egov.infra.admin.master.service.ModuleService;
import org.egov.infra.config.core.ApplicationThreadLocals;
import org.egov.infra.exception.ApplicationRuntimeException;
import org.egov.infra.reporting.engine.ReportOutput;
import org.egov.infra.reporting.engine.ReportRequest;
import org.egov.infra.reporting.engine.ReportService;
import org.egov.infstr.services.PersistenceService;
import org.egov.ptis.client.bill.PTBillServiceImpl;
import org.egov.ptis.client.model.calculator.DemandNoticeInfo;
import org.egov.ptis.client.util.PropertyTaxNumberGenerator;
import org.egov.ptis.client.util.PropertyTaxUtil;
import org.egov.ptis.constants.PropertyTaxConstants;
import org.egov.ptis.domain.bill.PropertyTaxBillable;
import org.egov.ptis.domain.dao.property.BasicPropertyDAO;
import org.egov.ptis.domain.entity.demand.BulkBillGeneration;
import org.egov.ptis.domain.entity.property.BasicProperty;
import org.egov.ptis.domain.service.notice.NoticeService;
import org.egov.ptis.service.utils.PropertyTaxCommonUtils;
import org.egov.ptis.wtms.WaterChargesIntegrationService;
import org.hibernate.Query;
import org.hibernate.Session;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.math.BigDecimal;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static org.egov.ptis.constants.PropertyTaxConstants.BILLTYPE_MANUAL;
import static org.egov.ptis.constants.PropertyTaxConstants.NOTICE_TYPE_BILL;
import static org.egov.ptis.constants.PropertyTaxConstants.QUARTZ_BULKBILL_JOBS;
import static org.egov.ptis.constants.PropertyTaxConstants.REPORT_TEMPLATENAME_BILL_GENERATION;
import static org.egov.ptis.constants.PropertyTaxConstants.STATUS_BILL_CREATED;
import static org.egov.ptis.constants.PropertyTaxConstants.STRING_EMPTY;

/**
 * Provides API to Generate a Demand Notice or the Bill giving the break up of
 * the tax amounts
 */
@Transactional(readOnly = true)
public class BillService {

    private static final Logger LOGGER = Logger.getLogger(BillService.class);
    private static final String STR_BILL_SHORTCUT = "B";

    private ReportService reportService;
    private NoticeService noticeService;
    @Autowired
    private PropertyTaxUtil propertyTaxUtil;
    private PTBillServiceImpl ptBillServiceImpl;
    private PropertyTaxNumberGenerator propertyTaxNumberGenerator;
    private Map<String, Map<String, BigDecimal>> reasonwiseDues;
    private String billNo;
    InputStream billPDF;
    @Autowired
    private ModuleService moduleDao;
    @PersistenceContext
    private EntityManager entityManager;
    @Autowired
    private PropertyTaxBillable propertyTaxBillable;
    private PersistenceService persistenceService;
    @Autowired
    private BasicPropertyDAO basicPropertyDAO;
    @Autowired
    private WaterChargesIntegrationService waterChargesIntegrationService;
    @Autowired
    private BoundaryService boundaryService;
    @Autowired
    private CityService cityService;
    @Autowired
    private PropertyTaxCommonUtils propertyTaxCommonUtils;

    @Autowired
    @Qualifier("bulkBillGenerationPersistenceService")
    private PersistenceService bulkBillGenerationPersistenceService;
    @Autowired
    private FinancialYearDAO financialYearDAO;

    /**
     * Generates a Demand Notice or the Bill giving the break up of the tax
     * amounts and the <code>EgBill</code>
     *
     * @see EgBill
     * @param basicProperty
     * @return
     */
    public ReportOutput generateBill(final BasicProperty basicProperty, final Integer userId) {
        if (LOGGER.isDebugEnabled())
            LOGGER.debug("Entered into generateBill BasicProperty : " + basicProperty);
        ReportOutput reportOutput = null;
        try {
            setBillNo(propertyTaxNumberGenerator.generateManualBillNumber(basicProperty.getPropertyID()));
            final int noOfBillGenerated = getNumberOfBills(basicProperty);
            if (noOfBillGenerated > 0)
                setBillNo(getBillNo() + "/" + STR_BILL_SHORTCUT + noOfBillGenerated);
            DemandNoticeInfo demandNoticeInfo = new DemandNoticeInfo();
            demandNoticeInfo.setCityService(cityService);
            demandNoticeInfo.setBasicProperty(basicProperty);
            demandNoticeInfo.setOldAssessmentNo(basicProperty.getOldMuncipalNum());
            demandNoticeInfo.setBillNo(getBillNo());
            demandNoticeInfo.setLocality(basicProperty.getPropertyID().getLocality().getName());
            demandNoticeInfo.setBillPeriod(propertyTaxCommonUtils.getCurrentInstallment().getDescription());
            if (basicProperty.getVacancyRemissions().isEmpty()) {
                demandNoticeInfo.setIsVacancyRemissionDone(false);
            } else {
                demandNoticeInfo.setIsVacancyRemissionDone(true);
            }
            Map<String, Object> reprortParams = prepareReportParams(basicProperty);
            ReportRequest reportRequest = null;
            reportRequest = new ReportRequest(REPORT_TEMPLATENAME_BILL_GENERATION, demandNoticeInfo,
                    reprortParams);
            reportOutput = getReportService().createReport(reportRequest);
            if (reportOutput != null && reportOutput.getReportOutputData() != null)
                billPDF = new ByteArrayInputStream(reportOutput.getReportOutputData());
            saveEgBill(basicProperty, userId);// saving eg_bill
            basicProperty.setIsBillCreated(STATUS_BILL_CREATED);
            basicProperty.setBillCrtError(STRING_EMPTY);
            final boolean flag = waterChargesIntegrationService.updateBillNo(basicProperty.getId().toString(),
                    getBillNo());
            if (flag) {
                if (LOGGER.isDebugEnabled())
                    LOGGER.debug("Billno updated successfully in water tax");
            } else {
                if (LOGGER.isDebugEnabled())
                    LOGGER.debug("Failed to updated billno in water tax");
            }
            noticeService.saveNotice(basicProperty.getPropertyForBasicProperty().getApplicationNo(), getBillNo(),
                    NOTICE_TYPE_BILL, basicProperty, billPDF);// Save
            noticeService.getSession().flush(); // Added since notice was not
                                                // getting saved
        } catch (final Exception e) {

            throw new ApplicationRuntimeException("Bill Generation Exception : " + e);
        }
        if (LOGGER.isDebugEnabled())
            LOGGER.debug("Exiting from generateBill");
        return reportOutput;
    }

    private Map<String, Object> prepareReportParams(BasicProperty basicProperty) {
        final Map<String, Object> reportParams = new HashMap<String, Object>();
        City city = cityService.getCityByCode(cityService.getCityCode());
        String owner = basicProperty.getProperty().getPropertyDetail().getPropertyTypeMaster().getType();
        String ownerType = "";
        if (owner.equalsIgnoreCase("Vacant Land")) {
            ownerType = "(On Land)";
        }
        final String cityName = city.getPreferences().getMunicipalityName();
        final String districtName = city.getDistrictName();
        final String date = org.egov.infra.utils.DateUtils.getDefaultFormattedDate(new Date());
        final CFinancialYear financialYear = financialYearDAO.getFinancialYearByDate(new Date());
        reportParams.put("cityName", cityName);
        reportParams.put("cityUrl", cityService.findAll().get(0).getName().toLowerCase());
        reportParams.put("districtName", districtName);
        reportParams.put("currDate", date);
        reportParams.put("financialYear", financialYear.getFinYearRange());
        reportParams.put("ownerType", ownerType);
        return reportParams;
    }

    /**
     * Gives the count of generated bills
     *
     * @param basicProperty
     * @return
     */
    private int getNumberOfBills(final BasicProperty basicProperty) {
        final Installment currentInstallment = propertyTaxCommonUtils.getCurrentInstallment();

        final Long count = (Long) entityManager.unwrap(Session.class)
                .createQuery(
                        "SELECT COUNT (*) FROM EgBill WHERE module = ? "
                                + "AND egBillType.code = ? AND consumerId = ? AND is_Cancelled = 'N' "
                                + "AND issueDate between ? and ? ").setEntity(0, currentInstallment.getModule())
                .setString(1, BILLTYPE_MANUAL).setString(2, basicProperty.getUpicNo())
                .setDate(3, currentInstallment.getFromDate()).setDate(4, currentInstallment.getToDate()).list().get(0);
        return count.intValue();
    }

    private void saveEgBill(final BasicProperty basicProperty, final Integer userId) {
        LOGGER.debug("Entered into saveEgBill");
        LOGGER.debug("saveEgBill : BasicProperty: " + basicProperty);
        propertyTaxBillable.setBasicProperty(basicProperty);
        propertyTaxBillable.setReferenceNumber(getBillNo());
        propertyTaxBillable.setBillType(propertyTaxUtil.getBillTypeByCode(BILLTYPE_MANUAL));
        propertyTaxBillable.setLevyPenalty(Boolean.TRUE);
        final EgBill egBill = ptBillServiceImpl.generateBill(propertyTaxBillable);
        LOGGER.debug("Exit from saveEgBill, EgBill: " + egBill);
    }

    /**
     * @description Called from ptisSchedular for bulk bill generation
     * @param modulo
     * @param billsCount
     */
    @SuppressWarnings("unchecked")
    public void bulkBillGeneration(final Integer modulo, final Integer billsCount) {
        LOGGER.debug("Entered into executeJob" + modulo);

        Long currentTime = System.currentTimeMillis();

        // returns all the property for which bill is created or cancelled
        final Query query = getQuery(modulo, billsCount);

        final List<String> assessmentNumbers = query.list();

        LOGGER.info("executeJob" + modulo + " - got " + assessmentNumbers + "indexNumbers for bill generation");
        Long timeTaken = currentTime - System.currentTimeMillis();
        LOGGER.debug("executeJob" + modulo + " took " + timeTaken / 1000 + " secs for BasicProperty selection");
        LOGGER.debug("executeJob" + modulo + " - BasicProperties = " + assessmentNumbers.size());
        LOGGER.info("executeJob" + modulo + " - Generating bills.....");

        currentTime = System.currentTimeMillis();
        int noOfBillsGenerated = 0;

        for (final String assessmentNumber : assessmentNumbers) {
            BasicProperty basicProperty = null;
            try {
                basicProperty = basicPropertyDAO.getBasicPropertyByPropertyID(assessmentNumber);
                generateBill(basicProperty, ApplicationThreadLocals.getUserId().intValue());
                noOfBillsGenerated++;
            } catch (final Exception e) {
                basicProperty.setIsBillCreated('F');
                basicProperty.setBillCrtError(e.getMessage());
                final String msg = " Error while generating Demand bill via BulkBillGeneration Job "
                        + modulo.toString();
                final String propertyType = " for "
                        + (basicProperty.getSource().equals(PropertyTaxConstants.SOURCEOFDATA_APPLICATION) ? "  non-migrated property "
                                : " migrated property");
                LOGGER.error(msg + propertyType + basicProperty.getUpicNo(), e);
            }
        }

        timeTaken = currentTime - System.currentTimeMillis();

        LOGGER.info("executeJob" + modulo + " - " + noOfBillsGenerated + "/" + assessmentNumbers.size()
                + " Bill(s) generated in " + timeTaken / 1000 + " (secs)");

        LOGGER.debug("Exiting from executeJob" + modulo);
    }

    /**
     * @description returns list of property assestment number(Zone and ward
     *              wise). properties for which bill is not created or
     *              cancelled.
     * @param modulo
     * @param billsCount
     * @return
     */
    @SuppressWarnings({ "unchecked", "unused" })
    private Query getQuery(final Integer modulo, final Integer billsCount) {

        StringBuilder queryString = new StringBuilder(200);
        final StringBuilder zoneParamString = new StringBuilder();
        final StringBuilder wardParamString = new StringBuilder();
        final Installment currentInstallment = propertyTaxCommonUtils.getCurrentInstallment();
        final Module ptModule = moduleDao.getModuleByName(PropertyTaxConstants.PTMODULENAME);
        // read zone and ward saved in bulkbillgeneration table.
        final List<BulkBillGeneration> bulkBillGeneration = getPersistenceService().findAllBy(
                "from BulkBillGeneration where zone.id is not null and installment.id = ? order by id",
                currentInstallment.getId());
        queryString = queryString.append("select bp.upicNo ").append("from BasicPropertyImpl bp ")
                .append("where bp.active = true ").append("and bp.upicNo IS not NULL ")
                .append("and (bp.isBillCreated is NULL or bp.isBillCreated='N' or bp.isBillCreated='false') ")
                .append("and MOD(bp.id, ").append(QUARTZ_BULKBILL_JOBS).append(") = :modulo ");
        if (bulkBillGeneration != null && !bulkBillGeneration.isEmpty()) {
            wardParamString.append("(");
            zoneParamString.append("(");
            int count = 1;
            for (final BulkBillGeneration bbg : bulkBillGeneration) {
                if (bbg.getWard() != null) {
                    if (count == bulkBillGeneration.size())
                        wardParamString.append("'").append(bbg.getZone().getId()).append('-')
                                .append(bbg.getWard().getId()).append("')");
                    else
                        wardParamString.append("'").append(bbg.getZone().getId()).append('-')
                                .append(bbg.getWard().getId()).append("', ");

                } else if (count == bulkBillGeneration.size())
                    zoneParamString.append(bbg.getZone().getId()).append(")");
                else
                    zoneParamString.append(bbg.getZone().getId()).append(", ");
                count++;
            }
            if (wardParamString != null)
                if (wardParamString.charAt(wardParamString.length() - 2) == ',')
                    wardParamString.setCharAt(wardParamString.length() - 2, ')');
            if (zoneParamString != null)
                if (zoneParamString.charAt(zoneParamString.length() - 2) == ',')
                    zoneParamString.setCharAt(zoneParamString.length() - 2, ')');

            if (wardParamString != null && zoneParamString == null)
                queryString.append(" AND ").append("bp.propertyID.ward.parent.id||'-'||bp.propertyID.ward.id")
                        .append(" IN ").append(wardParamString.toString());
            else if (zoneParamString != null && wardParamString == null)
                queryString.append(" AND ").append("bp.propertyID.zone.id").append(" IN ")
                        .append(zoneParamString.toString());
            else if (wardParamString != null && zoneParamString != null)
                queryString.append(" AND ").append("(bp.propertyID.ward.parent.id||'-'||bp.propertyID.ward.id")
                        .append(" IN ").append(wardParamString.toString()).append(" OR ")
                        .append("bp.propertyID.zone.id").append(" IN ").append(zoneParamString.toString()).append(')');
        }
        queryString = queryString.append(" AND bp NOT IN (SELECT bp FROM BasicPropertyImpl bp, EgBill b ")
                .append("WHERE bp.active = true ")
                .append("AND bp.upicNo = substring(b.consumerId, 1, strpos(b.consumerId,'(')-1) ")
                .append("AND b.module = :ptModule ").append("AND b.egBillType = :billType ")
                .append("AND b.is_History = 'N' ").append("AND b.is_Cancelled = 'N' ")
                .append("AND (b.issueDate BETWEEN :fromDate AND :toDate)) ");
        final Query query = getPersistenceService().getSession().createQuery(queryString.toString())
                .setInteger("modulo", modulo).setEntity("ptModule", ptModule)
                .setEntity("billType", propertyTaxUtil.getBillTypeByCode(PropertyTaxConstants.BILLTYPE_MANUAL))
                .setDate("fromDate", currentInstallment.getFromDate())
                .setDate("toDate", currentInstallment.getToDate());
        query.setMaxResults(billsCount);

        return query;
    }

    /**
     * @param zoneId
     * @param wardId
     * @param currentInstallment
     * @return
     */
    @SuppressWarnings("unchecked")
    public List<BulkBillGeneration> getBulkBill(Long zoneId, Long wardId, Installment currentInstallment) {
        final StringBuilder queryStr = new StringBuilder();
        queryStr.append("select bbg from BulkBillGeneration bbg ").append(
                " where bbg.zone.id=:zoneid and bbg.installment.id=:installment ");
        if (wardId != null && wardId != -1)
            queryStr.append("and bbg.ward.id=:wardid ");
        final Query query = getPersistenceService().getSession().createQuery(queryStr.toString());
        query.setLong("zoneid", zoneId);
        query.setLong("installment", currentInstallment.getId());
        if (wardId != null && wardId != -1)
            query.setLong("wardid", wardId);

        final List<BulkBillGeneration> bbgList = query.list();
        return bbgList;
    }

    /**
     * @param zoneId
     * @param wardId
     * @param currentInstallment
     * @return
     */
    @SuppressWarnings("unchecked")
    public BulkBillGeneration saveBulkBill(Long zoneId, Long wardId, Installment currentInstallment) {
        BulkBillGeneration bulkBill = new BulkBillGeneration();
        bulkBill.setZone(boundaryService.getBoundaryById(zoneId));
        bulkBill.setWard(boundaryService.getBoundaryById(wardId));
        bulkBill.setInstallment(currentInstallment);
        bulkBillGenerationPersistenceService.persist(bulkBill);
        return bulkBill;
    }

    public ReportService getReportService() {
        return reportService;
    }

    public void setReportService(final ReportService reportService) {
        this.reportService = reportService;
    }

    public Map<String, Map<String, BigDecimal>> getReasonwiseDues() {
        return reasonwiseDues;
    }

    public void setReasonwiseDues(final Map<String, Map<String, BigDecimal>> reasonwiseDues) {
        this.reasonwiseDues = reasonwiseDues;
    }

    public String getBillNo() {
        return billNo;
    }

    public void setBillNo(final String billNo) {
        this.billNo = billNo;
    }

    public InputStream getBillPDF() {
        return billPDF;
    }

    public void setBillPDF(final InputStream billPDF) {
        this.billPDF = billPDF;
    }

    public PropertyTaxNumberGenerator getPropertyTaxNumberGenerator() {
        return propertyTaxNumberGenerator;
    }

    public void setPropertyTaxNumberGenerator(final PropertyTaxNumberGenerator propertyTaxNumberGenerator) {
        this.propertyTaxNumberGenerator = propertyTaxNumberGenerator;
    }

    public PropertyTaxUtil getPropertyTaxUtil() {
        return propertyTaxUtil;
    }

    public void setPropertyTaxUtil(final PropertyTaxUtil propertyTaxUtil) {
        this.propertyTaxUtil = propertyTaxUtil;
    }

    public void setNoticeService(final NoticeService noticeService) {
        this.noticeService = noticeService;
    }

    public PTBillServiceImpl getPtBillServiceImpl() {
        return ptBillServiceImpl;
    }

    public void setPtBillServiceImpl(final PTBillServiceImpl ptBillServiceImpl) {
        this.ptBillServiceImpl = ptBillServiceImpl;
    }

    public PersistenceService getPersistenceService() {
        return persistenceService;
    }

    public void setPersistenceService(final PersistenceService persistenceService) {
        this.persistenceService = persistenceService;
    }

}