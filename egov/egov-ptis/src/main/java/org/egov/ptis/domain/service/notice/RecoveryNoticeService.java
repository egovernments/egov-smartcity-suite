/*
 * eGov suite of products aim to improve the internal efficiency,transparency,
 *    accountability and the service delivery of the government  organizations.
 *
 *     Copyright (C) <2016>  eGovernments Foundation
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
package org.egov.ptis.domain.service.notice;

import static org.egov.ptis.constants.PropertyTaxConstants.APPCONFIG_CLIENT_SPECIFIC_DMD_BILL;
import static org.egov.ptis.constants.PropertyTaxConstants.BILLTYPE_MANUAL;
import static org.egov.ptis.constants.PropertyTaxConstants.FILESTORE_MODULE_NAME;
import static org.egov.ptis.constants.PropertyTaxConstants.NOTICE_TYPE_DISTRESS;
import static org.egov.ptis.constants.PropertyTaxConstants.NOTICE_TYPE_ESD;
import static org.egov.ptis.constants.PropertyTaxConstants.NOTICE_TYPE_INVENTORY;
import static org.egov.ptis.constants.PropertyTaxConstants.NOTICE_TYPE_OC;
import static org.egov.ptis.constants.PropertyTaxConstants.NOTICE_TYPE_OCCUPIER;
import static org.egov.ptis.constants.PropertyTaxConstants.NOT_AVAILABLE;
import static org.egov.ptis.constants.PropertyTaxConstants.OCC_TENANT;
import static org.egov.ptis.constants.PropertyTaxConstants.PTMODULENAME;
import static org.egov.ptis.constants.PropertyTaxConstants.REPORT_INVENTORY_NOTICE_CORPORATION;
import static org.egov.ptis.constants.PropertyTaxConstants.REPORT_INVENTORY_NOTICE_MUNICIPALITY;
import static org.egov.ptis.constants.PropertyTaxConstants.VALUATION_CERTIFICATE;
import static org.egov.ptis.constants.PropertyTaxConstants.VALUATION_CERTIFICATE_CORPORATION;
import static org.egov.ptis.constants.PropertyTaxConstants.VALUATION_CERTIFICATE_MUNICIPALITY;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.math.BigDecimal;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;

import org.apache.commons.io.FileUtils;
import org.apache.commons.lang3.StringUtils;
import org.egov.commons.CFinancialYear;
import org.egov.commons.Installment;
import org.egov.commons.dao.FinancialYearDAO;
import org.egov.demand.model.EgBill;
import org.egov.infra.admin.master.entity.AppConfigValues;
import org.egov.infra.admin.master.entity.City;
import org.egov.infra.admin.master.service.AppConfigValueService;
import org.egov.infra.admin.master.service.ModuleService;
import org.egov.infra.exception.ApplicationRuntimeException;
import org.egov.infra.filestore.entity.FileStoreMapper;
import org.egov.infra.filestore.service.FileStoreService;
import org.egov.infra.persistence.entity.Address;
import org.egov.infra.reporting.engine.ReportConstants.FileFormat;
import org.egov.infra.reporting.engine.ReportOutput;
import org.egov.infra.reporting.engine.ReportRequest;
import org.egov.infra.reporting.engine.ReportService;
import org.egov.infra.utils.DateUtils;
import org.egov.infra.utils.NumberUtil;
import org.egov.ptis.bean.NoticeRequest;
import org.egov.ptis.client.util.PropertyTaxNumberGenerator;
import org.egov.ptis.constants.PropertyTaxConstants;
import org.egov.ptis.domain.dao.property.BasicPropertyDAO;
import org.egov.ptis.domain.entity.notice.RecoveryNoticesInfo;
import org.egov.ptis.domain.entity.property.BasicProperty;
import org.egov.ptis.domain.entity.property.Floor;
import org.egov.ptis.domain.repository.notice.RecoveryNoticesInfoRepository;
import org.egov.ptis.domain.service.property.PropertyService;
import org.egov.ptis.notice.PtNotice;
import org.egov.ptis.service.DemandBill.DemandBillService;
import org.egov.ptis.service.utils.PropertyTaxCommonUtils;
import org.hibernate.Session;
import org.joda.time.DateTime;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import com.lowagie.text.Document;
import com.lowagie.text.DocumentException;
import com.lowagie.text.pdf.PdfContentByte;
import com.lowagie.text.pdf.PdfImportedPage;
import com.lowagie.text.pdf.PdfReader;
import com.lowagie.text.pdf.PdfWriter;

@Service
public class RecoveryNoticeService {

    private static final String DEMAND_BILL_SERVICE = "demandBillService";
    private static final String CONSUMER_ID = "consumerId";
    private static final String ADDRESS = "address";
    private static final String LOCALITY = "locality";
    private static final String UPIC_NO = "upicNo";
    private static final String NOTICE_NUMBER = "noticeNumber";
    private static final String DOOR_NO = "doorNo";
    private static final String LOCATION = "Location";
    private static final String FINANCIAL_YEAR = "financialYear";
    private static final String DISTRESS_NOTICE_DATE = "distressNoticeDate";
    private static final String DISTRESS_NOTICE_NUMBER = "distressNoticeNumber";
    private static final String FIN_HALF_STRAT_MONTH = "FinHalfStratMonth";
    private static final String NOTICE_YEAR = "noticeYear";
    private static final String NOTICE_MONTH = "noticeMonth";
    private static final String NOTICE_DAY = "noticeDay";
    private static final String TOTAL_TAX_DUE = "totalTaxDue";
    private static final String SECTION_ACT = "sectionAct";
    private static final String BILL_NUMBER = "billNumber";
    private static final String BILL_DATE = "billDate";
    private static final String ESD_NOTICE_DATE = "eSDNoticeDate";
    private static final String ESD_NOTICE_NUMBER = "eSDNoticeNumber";
    private static final String FUTURE_DATE = "futureDate";
    private static final String FIN_YEAR = "finYear";
    private static final String CITY_NAME = "cityName";
    private static final String INST_MON_YEAR = "instMonYear";
    private static final String INST_LAST_DATE = "instLastDate";
    private static final String REPORT_MON_YEAR = "reportMonYear";
    private static final String REPORT_DATE = "reportDate";
    private static final String OWNER_NAME = "ownerName";
    private static final String OWNERSHIP_CERTIFICATE_DATE = "ownershipCertificateDate";
    private static final String OWNERSHIP_CERTIFICATE_NO = "ownershipCertificateNo";
    private final SimpleDateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy");
    private static final String OCCUPIER_NOTICE_NAGAR_PANCHAYET = "OccupierNotice_NagarPanchayet";
    private static final String OCCUPIER_NOTICE_CORPORATION = "OccupierNotice_Corporation";
    private static final String TAX_DUE_IN_WORDS = "taxDueInWords";

    @PersistenceContext
    private EntityManager entityManager;

    @Autowired
    private ModuleService moduleDao;

    @Autowired
    private ReportService reportService;

    @Autowired
    private NoticeService noticeService;

    @Autowired
    private PropertyTaxNumberGenerator propertyTaxNumberGenerator;

    @Autowired
    private AppConfigValueService appConfigValuesService;

    @Autowired
    private ApplicationContext beanProvider;

    @Autowired
    private FileStoreService fileStoreService;

    @Autowired
    private PropertyService propertyService;

    @Autowired
    private BasicPropertyDAO basicPropertyDAO;

    @Autowired
    private PropertyTaxCommonUtils propertyTaxCommonUtils;

    @Autowired
    private RecoveryNoticesInfoRepository recoveryNoticesInfoRepository;

    @Autowired
    private FinancialYearDAO financialYearDAO;

    public Session getCurrentSession() {
        return entityManager.unwrap(Session.class);
    }

    public boolean getDemandBillByAssessmentNo(final BasicProperty basicProperty) {
        boolean billExists = false;
        final AppConfigValues appConfigValues = appConfigValuesService.getAppConfigValueByDate(PTMODULENAME,
                APPCONFIG_CLIENT_SPECIFIC_DMD_BILL, new Date());
        final String value = appConfigValues != null ? appConfigValues.getValue() : "";
        if ("Y".equalsIgnoreCase(value)) {
            final DemandBillService demandBillService = (DemandBillService) beanProvider.getBean(DEMAND_BILL_SERVICE);
            billExists = demandBillService.getDemandBillByAssessmentNumber(basicProperty.getUpicNo());
        } else {
            final EgBill egBill = getBillByAssessmentNumber(basicProperty);
            if (egBill != null)
                billExists = true;
        }

        return billExists;
    }

    public List<String> validateRecoveryNotices(final String assessmentNo, final String noticeType) {
        final List<String> errors = new ArrayList<>();
        final BasicProperty basicProperty = basicPropertyDAO.getBasicPropertyByPropertyID(assessmentNo);
        if (basicProperty == null)
            errors.add("property.invalid");
        else if (NOTICE_TYPE_ESD.equals(noticeType))
            validateDemandBill(basicProperty, errors);
        else if (NOTICE_TYPE_DISTRESS.equals(noticeType))
            validateDistressNotice(errors, basicProperty);
        else if (NOTICE_TYPE_INVENTORY.equals(noticeType))
            validateInventoryNotice(errors, basicProperty);
        else if (VALUATION_CERTIFICATE.equals(noticeType) || NOTICE_TYPE_OC.equals(noticeType))
            validateCertificate(errors, basicProperty);
        else
            validateOccupierNotice(errors, basicProperty);
        return errors;
    }

    public EgBill getBillByAssessmentNumber(final BasicProperty basicProperty) {
        final StringBuilder queryStr = new StringBuilder(200);
        queryStr.append(
                "FROM EgBill WHERE module =:module AND egBillType.code =:billType AND consumerId =:assessmentNo AND is_history = 'N'");
        final Query qry = entityManager.createQuery(queryStr.toString());
        qry.setParameter("module", moduleDao.getModuleByName(PTMODULENAME));
        qry.setParameter("billType", BILLTYPE_MANUAL);
        qry.setParameter("assessmentNo", basicProperty.getUpicNo());
        return qry.getResultList() != null && !qry.getResultList().isEmpty() ? (EgBill) qry.getResultList().get(0)
                : null;
    }

    public BigDecimal getTotalPropertyTaxDue(final BasicProperty basicProperty) {
        return propertyService.getTotalPropertyTaxDue(basicProperty);
    }

    public BigDecimal getTotalPropertyTaxDueIncludingPenalty(final BasicProperty basicProperty) {
        return propertyService.getTotalPropertyTaxDueIncludingPenalty(basicProperty);
    }

    public ResponseEntity<byte[]> generateNotice(final String assessmentNo, final String noticeType) {
        ReportOutput reportOutput;
        final BasicProperty basicProperty = basicPropertyDAO.getBasicPropertyByPropertyID(assessmentNo);
        final PtNotice notice = noticeService.getNoticeByTypeUpicNoAndFinYear(noticeType, basicProperty.getUpicNo());
        if (notice == null)
            reportOutput = generateNotice(noticeType, basicProperty);
        else
            reportOutput = getNotice(notice, noticeType);
        final HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.parseMediaType("application/pdf"));
        headers.add("content-disposition", "inline;filename=" + noticeType + "_" + basicProperty.getUpicNo() + ".pdf");
        return new ResponseEntity<>(reportOutput.getReportOutputData(), headers, HttpStatus.CREATED);
    }

    public ReportOutput generateNotice(final String noticeType, final BasicProperty basicProperty) {
        ReportOutput reportOutput = null;
        final Map<String, Object> reportParams = new HashMap<>();
        InputStream noticePDF = null;
        ReportRequest reportInput = null;
        final StringBuilder queryString = new StringBuilder();
        queryString.append("from City");
        final Query query = entityManager.createQuery(queryString.toString());
        final City city = (City) query.getSingleResult();
        populateReportParams(reportParams, city, basicProperty);
        final String noticeNo = propertyTaxNumberGenerator.generateNoticeNumber(noticeType);
        final SimpleDateFormat formatter = new SimpleDateFormat("MMM yyyy");
        if (NOTICE_TYPE_ESD.equals(noticeType))
            reportInput = generateEsdNotice(basicProperty, reportParams, city, noticeNo, formatter);
        else if (NOTICE_TYPE_INVENTORY.equals(noticeType))
            reportInput = generateInventoryNotice(basicProperty, reportParams, city, formatter);
        else if (VALUATION_CERTIFICATE.equals(noticeType))
            reportInput = generateValuationCertificate(basicProperty, reportParams, city, noticeNo);
        else if (NOTICE_TYPE_OC.equals(noticeType))
            reportInput = generateOwnershipCertificate(basicProperty, reportParams, city, noticeNo);
        else if (NOTICE_TYPE_OCCUPIER.equals(noticeType))
            reportOutput = prepareOccupierNotice(basicProperty, reportParams, city, noticeNo);
        else
            reportInput = generateDistressNotice(basicProperty, reportParams, city, noticeNo);

        if (!NOTICE_TYPE_OCCUPIER.equals(noticeType)) {
            reportInput.setPrintDialogOnOpenReport(true);
            reportInput.setReportFormat(FileFormat.PDF);
            reportOutput = reportService.createReport(reportInput);
        }
        if (reportOutput != null && reportOutput.getReportOutputData() != null)
            noticePDF = new ByteArrayInputStream(reportOutput.getReportOutputData());
        noticeService.saveNotice(basicProperty.getPropertyForBasicProperty().getApplicationNo(), noticeNo, noticeType,
                basicProperty, noticePDF);
        return reportOutput;
    }

    public ReportOutput prepareOccupierNotice(final BasicProperty basicProperty, final Map<String, Object> reportParams,
            final City city, final String noticeNo) {
        ReportRequest reportInput;
        ReportOutput reportOutput = null;
        final List<InputStream> pdfs = new ArrayList<>();
        reportInput = generateOccupierNotice(basicProperty, reportParams, city, noticeNo);
        reportInput.setPrintDialogOnOpenReport(true);
        reportInput.setReportFormat(FileFormat.PDF);
        for (final Floor floor : basicProperty.getProperty().getPropertyDetail().getFloorDetails())
            if (OCC_TENANT.equalsIgnoreCase(floor.getPropertyOccupation().getOccupancyCode())) {
                reportOutput = reportService.createReport(reportInput);
                if (reportOutput != null && reportOutput.getReportOutputData() != null)
                    pdfs.add(new ByteArrayInputStream(reportOutput.getReportOutputData()));
            }
        if (!pdfs.isEmpty()) {
            byte[] mergedPdfs = null;
            final ByteArrayOutputStream output = new ByteArrayOutputStream();
            try {
                mergedPdfs = mergePdfFiles(pdfs, output);
                if (reportOutput != null)
                    reportOutput.setReportOutputData(mergedPdfs);
            } catch (final DocumentException | IOException e) {
                throw new ApplicationRuntimeException(e.getMessage(), e);
            }
        }
        return reportOutput;
    }

    @SuppressWarnings("unchecked")
    public List<String> generateRecoveryNotices(final NoticeRequest noticeRequest) {
        final Query qry = getSearchQuery(noticeRequest);
        final List<String> properties = qry.getResultList();
        final List<RecoveryNoticesInfo> noticesInfos = new ArrayList<>();

        Long jobNumber = getLatestJobNumber();
        if (jobNumber == null)
            jobNumber = 0l;
        jobNumber = jobNumber + 1;
        for (final String propertyId : properties) {
            final RecoveryNoticesInfo info = new RecoveryNoticesInfo();
            info.setPropertyId(propertyId);
            info.setNoticeType(noticeRequest.getNoticeType());
            info.setGenerated(Boolean.FALSE);
            info.setJobNumber(jobNumber);
            noticesInfos.add(info);
        }
        recoveryNoticesInfoRepository.save(noticesInfos);
        return properties;
    }

    public Long getLatestJobNumber() {
        return recoveryNoticesInfoRepository.getLatestJobNumber();
    }

    public RecoveryNoticesInfo getRecoveryNoticeInfoByAssessmentAndNoticeType(final String propertyId, final String noticeType) {
        return recoveryNoticesInfoRepository.findByPropertyIdAndNoticeType(propertyId, noticeType);
    }

    public void saveRecoveryNoticeInfo(final RecoveryNoticesInfo noticeInfo) {
        recoveryNoticesInfoRepository.save(noticeInfo);
    }

    private Map<String, Object> populateReportParams(final Map<String, Object> reportParams, final City city,
            final BasicProperty basicProperty) {
        reportParams.put(CITY_NAME, city.getPreferences().getMunicipalityName());
        reportParams.put(OWNER_NAME, basicProperty.getFullOwnerName());
        return reportParams;
    }

    private ReportOutput getNotice(final PtNotice notice, final String noticeType) {
        final ReportOutput reportOutput = new ReportOutput();
        final FileStoreMapper fsm = notice.getFileStore();
        final File file = fileStoreService.fetch(fsm, FILESTORE_MODULE_NAME);
        byte[] bFile;
        try {
            bFile = FileUtils.readFileToByteArray(file);
        } catch (final IOException e) {
            throw new ApplicationRuntimeException("Exception while retrieving " + noticeType + " : " + e);
        }
        reportOutput.setReportOutputData(bFile);
        reportOutput.setReportFormat(FileFormat.PDF);
        return reportOutput;
    }

    private void validateInventoryNotice(final List<String> errors, final BasicProperty basicProperty) {
        validateDemandBill(basicProperty, errors);
        final PtNotice distressNotice = noticeService.getNoticeByTypeUpicNoAndFinYear(NOTICE_TYPE_DISTRESS,
                basicProperty.getUpicNo());
        if (distressNotice != null) {
            final DateTime noticeDate = new DateTime(distressNotice.getNoticeDate());
            final DateTime currDate = new DateTime();
            if (!currDate.isAfter(noticeDate.plusDays(16)))
                errors.add("invntry.distress.notice.not.exists");
        } else
            errors.add("invntry.distress.notice.not.exists");
    }

    private void validateDistressNotice(final List<String> errors, final BasicProperty basicProperty) {
        final BigDecimal totalDue = getTotalPropertyTaxDueIncludingPenalty(basicProperty);
        if (totalDue.compareTo(BigDecimal.ZERO) == 0)
            errors.add("invalid.no.due");
        else if (basicProperty.getProperty().getIsExemptedFromTax())
            errors.add("invalid.exempted");
        else {
            final PtNotice esdNotice = noticeService.getNoticeByTypeUpicNoAndFinYear(NOTICE_TYPE_ESD,
                    basicProperty.getUpicNo());
            if (esdNotice == null)
                errors.add("invalid.esd.not.generated");
            else if (DateUtils.noOfDays(esdNotice.getNoticeDate(), new Date()) < 15)
                errors.add("invalid.time.not.lapsed");
        }
    }

    private void validateCertificate(final List<String> errors, final BasicProperty basicProperty) {
        final BigDecimal totalDue = getTotalPropertyTaxDue(basicProperty);
        if (totalDue.compareTo(BigDecimal.ZERO) > 0)
            errors.add("assessment.has.pt.due");
    }

    private List<String> validateDemandBill(final BasicProperty basicProperty, final List<String> errors) {
        final BigDecimal totalDue = getTotalPropertyTaxDue(basicProperty);
        if (totalDue.compareTo(BigDecimal.ZERO) == 0)
            errors.add("common.no.property.due");
        else {
            final boolean billExists = getDemandBillByAssessmentNo(basicProperty);
            if (!billExists)
                errors.add("common.demandbill.not.exists");
        }
        return errors;
    }

    private List<String> validateOccupierNotice(final List<String> errors, final BasicProperty basicProperty) {
        Boolean hasTenant = Boolean.FALSE;
        for (final Floor floor : basicProperty.getProperty().getPropertyDetail().getFloorDetails())
            if (OCC_TENANT.equalsIgnoreCase(floor.getPropertyOccupation().getOccupancyCode()))
                hasTenant = Boolean.TRUE;
        if (!hasTenant)
            errors.add("error.tenant.not.exist");
        return errors;
    }

    private ReportRequest generateDistressNotice(final BasicProperty basicProperty, final Map<String, Object> reportParams,
            final City city, final String noticeNo) {
        ReportRequest reportInput;
        reportParams.put(TOTAL_TAX_DUE, getTotalPropertyTaxDueIncludingPenalty(basicProperty));
        final DateTime noticeDate = new DateTime();
        reportParams.put(NOTICE_DAY, propertyTaxCommonUtils.getDateWithSufix(noticeDate.getDayOfMonth()));
        reportParams.put(NOTICE_MONTH, noticeDate.monthOfYear().getAsShortText());
        reportParams.put(NOTICE_YEAR, noticeDate.getYear());
        if (noticeDate.getMonthOfYear() >= 4 && noticeDate.getMonthOfYear() <= 10)
            reportParams.put(FIN_HALF_STRAT_MONTH, "April");
        else
            reportParams.put(FIN_HALF_STRAT_MONTH, "October");
        reportParams.put(DISTRESS_NOTICE_NUMBER, noticeNo);
        reportParams.put(DISTRESS_NOTICE_DATE, DateUtils.getDefaultFormattedDate(new Date()));
        final String cityGrade = city.getGrade();
        if (org.apache.commons.lang.StringUtils.isNotEmpty(cityGrade)
                && cityGrade.equalsIgnoreCase(PropertyTaxConstants.CITY_GRADE_CORPORATION)) {
            reportParams.put(SECTION_ACT, PropertyTaxConstants.CORPORATION_ESD_NOTICE_SECTION_ACT);
            reportInput = new ReportRequest(PropertyTaxConstants.REPORT_DISTRESS_CORPORATION, reportParams,
                    reportParams);
        } else {
            reportParams.put(SECTION_ACT, PropertyTaxConstants.MUNICIPALITY_DISTRESS_NOTICE_SECTION_ACT);
            reportInput = new ReportRequest(PropertyTaxConstants.REPORT_DISTRESS_MUNICIPALITY, reportParams,
                    reportParams);
        }
        return reportInput;
    }

    private ReportRequest generateInventoryNotice(final BasicProperty basicProperty, final Map<String, Object> reportParams,
            final City city, final SimpleDateFormat formatter) {
        ReportRequest reportInput;
        final Installment currentInstall = propertyTaxCommonUtils.getCurrentPeriodInstallment();
        final DateTime dateTime = new DateTime();
        final DateTime currInstToDate = new DateTime(currentInstall.getToDate());
        reportParams.put(TOTAL_TAX_DUE, String.valueOf(getTotalPropertyTaxDue(basicProperty)));
        reportParams.put(REPORT_DATE, propertyTaxCommonUtils.getDateWithSufix(dateTime.getDayOfMonth()));
        reportParams.put(REPORT_MON_YEAR, dateTime.monthOfYear().getAsShortText() + "," + dateTime.getYear());

        final String cityGrade = city.getGrade();
        if (StringUtils.isNotBlank(cityGrade)
                && cityGrade.equalsIgnoreCase(PropertyTaxConstants.CITY_GRADE_CORPORATION)) {
            reportParams.put(INST_LAST_DATE,
                    propertyTaxCommonUtils.getDateWithSufix(currInstToDate.getDayOfMonth()));
            reportParams.put(INST_MON_YEAR,
                    currInstToDate.monthOfYear().getAsShortText() + "," + currInstToDate.getYear());
            reportInput = new ReportRequest(REPORT_INVENTORY_NOTICE_CORPORATION, reportParams, reportParams);
        } else {
            reportParams.put(INST_LAST_DATE, formatter.format(currentInstall.getToDate()));
            reportInput = new ReportRequest(REPORT_INVENTORY_NOTICE_MUNICIPALITY, reportParams, reportParams);
        }
        return reportInput;
    }

    private ReportRequest generateEsdNotice(final BasicProperty basicProperty, final Map<String, Object> reportParams,
            final City city, final String noticeNo, final SimpleDateFormat formatter) {
        ReportRequest reportInput;
        prepareEsdReportParams(basicProperty, reportParams, noticeNo, formatter);
        final String cityGrade = city.getGrade();
        if (cityGrade != null && cityGrade != ""
                && cityGrade.equalsIgnoreCase(PropertyTaxConstants.CITY_GRADE_CORPORATION)) {
            reportParams.put(SECTION_ACT, PropertyTaxConstants.CORPORATION_ESD_NOTICE_SECTION_ACT);
            reportInput = new ReportRequest(PropertyTaxConstants.REPORT_ESD_NOTICE_CORPORATION, reportParams,
                    reportParams);
        } else {
            reportParams.put(SECTION_ACT, PropertyTaxConstants.MUNICIPALITY_ESD_NOTICE_SECTION_ACT);
            reportInput = new ReportRequest(PropertyTaxConstants.REPORT_ESD_NOTICE_MUNICIPALITY, reportParams,
                    reportParams);
        }
        return reportInput;
    }

    private void prepareEsdReportParams(final BasicProperty basicProperty, final Map<String, Object> reportParams,
            final String noticeNo, final SimpleDateFormat formatter) {
        final Address ownerAddress = basicProperty.getAddress();
        final DateTime noticeDate = new DateTime(new Date());
        final AppConfigValues appConfigValues = appConfigValuesService.getAppConfigValueByDate(PTMODULENAME,
                APPCONFIG_CLIENT_SPECIFIC_DMD_BILL, new Date());
        reportParams.put(DOOR_NO, StringUtils.isNotBlank(ownerAddress.getHouseNoBldgApt())
                ? ownerAddress.getHouseNoBldgApt() : "N/A");
        reportParams.put(FIN_YEAR, formatter.format(new Date()));
        reportParams.put(TOTAL_TAX_DUE, getTotalPropertyTaxDueIncludingPenalty(basicProperty));
        reportParams.put(FUTURE_DATE, DateUtils.getDefaultFormattedDate(noticeDate.plusDays(2).toDate()));
        reportParams.put(ESD_NOTICE_NUMBER, noticeNo);
        reportParams.put(ESD_NOTICE_DATE, DateUtils.getDefaultFormattedDate(new Date()));
        final String value = appConfigValues != null ? appConfigValues.getValue() : "";
        if ("Y".equalsIgnoreCase(value)) {
            final DemandBillService demandBillService = (DemandBillService) beanProvider
                    .getBean(DEMAND_BILL_SERVICE);
            reportParams.putAll(demandBillService.getDemandBillDetails(basicProperty));
        } else {
            final EgBill egBill = getBillByAssessmentNumber(basicProperty);
            reportParams.put(BILL_DATE, DateUtils.getDefaultFormattedDate(egBill.getCreateDate()));
            reportParams.put(BILL_NUMBER, egBill.getBillNo());
        }
    }

    private ReportRequest generateOwnershipCertificate(final BasicProperty basicProperty, final Map<String, Object> reportParams,
            final City city, final String noticeNo) {
        ReportRequest reportInput;
        prepareOwnershipCertificateParams(basicProperty, reportParams, city, noticeNo);
        final String[] ownerName = basicProperty.getFullOwnerName().split(",");
        if (ownerName.length > 1)
            reportInput = new ReportRequest(PropertyTaxConstants.REPORT_OWNERSHIP_CERTIFICATE_MULTIPLE, reportParams,
                    reportParams);
        else
            reportInput = new ReportRequest(PropertyTaxConstants.REPORT_OWNERSHIP_CERTIFICATE_SINGLE, reportParams,
                    reportParams);

        return reportInput;
    }

    private void prepareOwnershipCertificateParams(final BasicProperty basicProperty, final Map<String, Object> reportParams,
            final City city, final String noticeNo) {
        final Address ownerAddress = basicProperty.getAddress();
        reportParams.put(DOOR_NO, StringUtils.isNotBlank(ownerAddress.getHouseNoBldgApt())
                ? ownerAddress.getHouseNoBldgApt() : "N/A");
        reportParams.put(OWNERSHIP_CERTIFICATE_NO, noticeNo);
        reportParams.put(OWNERSHIP_CERTIFICATE_DATE, DateUtils.getDefaultFormattedDate(new Date()));
        reportParams.put(CONSUMER_ID, basicProperty.getUpicNo());
        reportParams.put("locality", ownerAddress.getAreaLocalitySector());
        reportParams.put(OWNER_NAME, basicProperty.getFullOwnerName());
        reportParams.put("address", basicProperty.getAddress().toString());
        final String cityGrade = city.getGrade();
        if (org.apache.commons.lang.StringUtils.isNotEmpty(cityGrade)
                && cityGrade.equalsIgnoreCase(PropertyTaxConstants.CITY_GRADE_CORPORATION))
            reportParams.put("cityGrade", "Municipal Corporation");
        else
            reportParams.put("cityGrade", "Municipality/NP");

        final StringBuilder ownerList = new StringBuilder(500);
        final String[] ownerName = basicProperty.getFullOwnerName().split(",");
        if (ownerName.length > 1) {
            for (int i = 0; i < ownerName.length; i++)
                ownerList.append(i + 1 + "." + ownerName[i] + "\n");
            reportParams.put("ownerNameList", ownerList.toString());
        } else
            reportParams.put(OWNER_NAME, basicProperty.getFullOwnerName());

        prepareDemandBillDetails(reportParams, basicProperty);
    }

    private Query getSearchQuery(final NoticeRequest noticeRequest) {
        final Map<String, Object> params = new HashMap<>();
        final StringBuilder query = new StringBuilder(500);
        query.append(
                "select mv.propertyId from PropertyMaterlizeView mv where mv.propertyId is not null ");
        appendWard(noticeRequest, params, query);
        appendBlock(noticeRequest, params, query);
        appendPropertyType(noticeRequest, params, query);
        appendCategoryType(noticeRequest, params, query);
        appendPropertyId(noticeRequest, params, query);
        appendFinancialYear(noticeRequest, params, query);
        return getQuery(params, query);
    }

    private void appendFinancialYear(final NoticeRequest noticeRequest, final Map<String, Object> params,
            final StringBuilder query) {
        final CFinancialYear currFinYear = financialYearDAO.getFinancialYearByDate(new Date());
        query.append(
                " and mv.propertyId not in (select propertyId from RecoveryNoticesInfo where noticeType = :noticeType and createdDate between :startDate and :endDate )");
        params.put("noticeType", noticeRequest.getNoticeType());
        params.put("startDate", currFinYear.getStartingDate());
        params.put("endDate", currFinYear.getEndingDate());
    }

    private void appendPropertyId(final NoticeRequest noticeRequest, final Map<String, Object> params,
            final StringBuilder query) {
        if (StringUtils.isNotEmpty(noticeRequest.getPropertyId())) {
            query.append(" and mv.propertyId = :propertyId");
            params.put("propertyId", noticeRequest.getPropertyId());
        }
    }

    private void appendCategoryType(final NoticeRequest noticeRequest, final Map<String, Object> params,
            final StringBuilder query) {
        if (!(noticeRequest.getCategoryType() == null || "-1".equals(noticeRequest.getCategoryType()))) {
            query.append(" and mv.categoryType = :categoryType");
            params.put("categoryType", noticeRequest.getCategoryType());
        }
    }

    private void appendPropertyType(final NoticeRequest noticeRequest, final Map<String, Object> params,
            final StringBuilder query) {
        if (!(noticeRequest.getPropertyType() == null || noticeRequest.getPropertyType().equals(-1l))) {
            query.append(" and mv.propTypeMstrID.id = :propertyType");
            params.put("propertyType", noticeRequest.getPropertyType());
        }
    }

    private void appendBlock(final NoticeRequest noticeRequest, final Map<String, Object> params, final StringBuilder query) {
        if (!(noticeRequest.getBlock() == null || noticeRequest.getBlock().equals(-1l))) {
            query.append(" and mv.block.id = :blockId");
            params.put("blockId", noticeRequest.getBlock());
        }
    }

    private void appendWard(final NoticeRequest noticeRequest, final Map<String, Object> params, final StringBuilder query) {
        if (!(noticeRequest.getWard() == null || noticeRequest.getWard().equals(-1l))) {
            query.append(" and mv.ward.id = :wardId");
            params.put("wardId", noticeRequest.getWard());
        }
    }

    private Query getQuery(final Map<String, Object> params, final StringBuilder query) {
        final Query qry = entityManager.createQuery(query.toString());
        for (final Entry<String, Object> param : params.entrySet())
            qry.setParameter(param.getKey(), param.getValue());
        return qry;
    }

    private ReportRequest generateValuationCertificate(final BasicProperty basicProperty, final Map<String, Object> reportParams,
            final City city, final String noticeNo) {
        ReportRequest reportInput;
        final SimpleDateFormat formatter = new SimpleDateFormat("dd/MM/yyyy");
        final Address ownerAddress = basicProperty.getAddress();
        reportParams.put(NOTICE_NUMBER, noticeNo);
        reportParams.put(REPORT_DATE, formatter.format(new Date()));
        reportParams.put(DOOR_NO, StringUtils.isNotBlank(ownerAddress.getHouseNoBldgApt())
                ? ownerAddress.getHouseNoBldgApt() : "N/A");
        reportParams.put(UPIC_NO, basicProperty.getUpicNo());
        reportParams.put(LOCALITY, ownerAddress.getAreaLocalitySector());
        reportParams.put(ADDRESS, ownerAddress.toString());
        final String cityGrade = city.getGrade();
        if (StringUtils.isNotBlank(cityGrade)
                && cityGrade.equalsIgnoreCase(PropertyTaxConstants.CITY_GRADE_CORPORATION))
            reportInput = new ReportRequest(VALUATION_CERTIFICATE_CORPORATION, reportParams, reportParams);
        else
            reportInput = new ReportRequest(VALUATION_CERTIFICATE_MUNICIPALITY, reportParams, reportParams);
        return reportInput;
    }

    private ReportRequest generateOccupierNotice(final BasicProperty basicProperty, final Map<String, Object> reportParams,
            final City city, final String noticeNo) {
        ReportRequest reportInput;
        final Date asOnDate = new Date();
        final CFinancialYear currFinYear = financialYearDAO.getFinancialYearByDate(asOnDate);
        reportParams.put(DOOR_NO, basicProperty.getAddress().getHouseNoBldgApt() == null ? NOT_AVAILABLE
                : basicProperty.getAddress().getHouseNoBldgApt());
        reportParams.put(LOCATION, basicProperty.getAddress().getAreaLocalitySector() == null ? NOT_AVAILABLE
                : basicProperty.getAddress().getAreaLocalitySector());
        reportParams.put(CITY_NAME, city.getPreferences().getMunicipalityName() == null ? NOT_AVAILABLE
                : city.getPreferences().getMunicipalityName());
        reportParams.put(FINANCIAL_YEAR, currFinYear.getFinYearRange());
        final BigDecimal totalTaxDue = getTotalPropertyTaxDue(basicProperty);
        reportParams.put(TOTAL_TAX_DUE, String.valueOf(totalTaxDue));
        reportParams.put(REPORT_DATE, dateFormat.format(asOnDate));
        reportParams.put(NOTICE_NUMBER, noticeNo);
        reportParams.put(TAX_DUE_IN_WORDS, NumberUtil.amountInWords(totalTaxDue));
        prepareDemandBillDetails(reportParams, basicProperty);
        final String cityGrade = city.getGrade();
        if (StringUtils.isNotBlank(cityGrade)
                && cityGrade.equalsIgnoreCase(PropertyTaxConstants.CITY_GRADE_CORPORATION))
            reportInput = new ReportRequest(OCCUPIER_NOTICE_CORPORATION, reportParams, reportParams);
        else
            reportInput = new ReportRequest(OCCUPIER_NOTICE_NAGAR_PANCHAYET, reportParams, reportParams);
        return reportInput;

    }

    private byte[] mergePdfFiles(final List<InputStream> inputPdfList, final OutputStream outputStream)
            throws DocumentException, IOException {
        final Document document = new Document();
        final List<PdfReader> readers = new ArrayList<>();
        int totalPages = 0;
        final Iterator<InputStream> pdfIterator = inputPdfList.iterator();
        while (pdfIterator.hasNext()) {
            final InputStream pdf = pdfIterator.next();
            if (pdf != null) {
                final PdfReader pdfReader = new PdfReader(pdf);
                readers.add(pdfReader);
                totalPages = totalPages + pdfReader.getNumberOfPages();
            } else
                break;
        }
        final PdfWriter writer = PdfWriter.getInstance(document, outputStream);
        document.open();
        final PdfContentByte pageContentByte = writer.getDirectContent();
        PdfImportedPage pdfImportedPage;
        int currentPdfReaderPage = 1;
        final Iterator<PdfReader> iteratorPDFReader = readers.iterator();
        while (iteratorPDFReader.hasNext()) {
            final PdfReader pdfReader = iteratorPDFReader.next();
            while (currentPdfReaderPage <= pdfReader.getNumberOfPages()) {
                document.newPage();
                pdfImportedPage = writer.getImportedPage(pdfReader, currentPdfReaderPage);
                pageContentByte.addTemplate(pdfImportedPage, 0, 0);
                currentPdfReaderPage++;
            }
            currentPdfReaderPage = 1;
        }
        outputStream.flush();
        document.close();
        outputStream.close();
        return ((ByteArrayOutputStream) outputStream).toByteArray();
    }

    public Map<String, Object> prepareDemandBillDetails(final Map<String, Object> reportParams,
            final BasicProperty basicProperty) {
        final AppConfigValues appConfigValues = appConfigValuesService.getAppConfigValueByDate(PTMODULENAME,
                APPCONFIG_CLIENT_SPECIFIC_DMD_BILL, new Date());
        final String value = appConfigValues != null ? appConfigValues.getValue() : "";
        if ("Y".equalsIgnoreCase(value)) {
            final DemandBillService demandBillService = (DemandBillService) beanProvider
                    .getBean(DEMAND_BILL_SERVICE);
            reportParams.putAll(demandBillService.getDemandBillDetails(basicProperty));
        } else {
            final EgBill egBill = getBillByAssessmentNumber(basicProperty);
            reportParams.put(BILL_DATE, DateUtils.getDefaultFormattedDate(egBill.getCreateDate()));
            reportParams.put(BILL_NUMBER, egBill.getBillNo());
        }
        return reportParams;
    }

}
