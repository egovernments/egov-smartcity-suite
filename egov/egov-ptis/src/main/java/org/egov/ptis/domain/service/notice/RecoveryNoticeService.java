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
import static org.egov.ptis.constants.PropertyTaxConstants.PTMODULENAME;
import static org.egov.ptis.constants.PropertyTaxConstants.REPORT_INVENTORY_NOTICE_CORPORATION;
import static org.egov.ptis.constants.PropertyTaxConstants.REPORT_INVENTORY_NOTICE_MUNICIPALITY;

import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.math.BigDecimal;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;

import org.apache.commons.io.FileUtils;
import org.apache.commons.lang3.StringUtils;
import org.egov.commons.Installment;
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
import org.egov.ptis.client.util.PropertyTaxNumberGenerator;
import org.egov.ptis.constants.PropertyTaxConstants;
import org.egov.ptis.domain.dao.property.BasicPropertyDAO;
import org.egov.ptis.domain.entity.property.BasicProperty;
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

@Service
public class RecoveryNoticeService {

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
    private static final String DOOR_NO = "doorNo";
    private static final String CITY_NAME = "cityName";
    private static final String INST_MON_YEAR = "instMonYear";
    private static final String INST_LAST_DATE = "instLastDate";
    private static final String REPORT_MON_YEAR = "reportMonYear";
    private static final String REPORT_DATE = "reportDate";
    private static final String OWNER_NAME = "ownerName";

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

    public Session getCurrentSession() {
        return entityManager.unwrap(Session.class);
    }

    public boolean getDemandBillByAssessmentNo(final BasicProperty basicProperty) {
        boolean billExists = false;
        final AppConfigValues appConfigValues = appConfigValuesService.getAppConfigValueByDate(PTMODULENAME,
                APPCONFIG_CLIENT_SPECIFIC_DMD_BILL, new Date());
        final String value = appConfigValues != null ? appConfigValues.getValue() : "";
        if ("Y".equalsIgnoreCase(value)) {
            final DemandBillService demandBillService = (DemandBillService) beanProvider.getBean("demandBillService");
            billExists = demandBillService.getDemandBillByAssessmentNumber(basicProperty.getUpicNo());
        } else {
            final EgBill egBill = getBillByAssessmentNumber(basicProperty);
            if (egBill != null)
                billExists = true;
        }

        return billExists;
    }
    
	public List<String> validateRecoveryNotices(String assessmentNo, String noticeType) {
		List<String> errors = new ArrayList<String>();
		final BasicProperty basicProperty = basicPropertyDAO.getBasicPropertyByPropertyID(assessmentNo);
		if (basicProperty == null)
			errors.add("property.invalid");
		else {
			if (NOTICE_TYPE_ESD.equals(noticeType)) {
				validateDemandBill(basicProperty);
			} else if (NOTICE_TYPE_DISTRESS.equals(noticeType)) {
				final BigDecimal totalDue = getTotalPropertyTaxDueIncludingPenalty(basicProperty);
				if (totalDue.compareTo(BigDecimal.ZERO) == 0)
					errors.add("invalid.no.due");
				else if (basicProperty.getProperty().getIsExemptedFromTax()) {
					errors.add("invalid.exempted");
				} else {
					final PtNotice esdNotice = noticeService.getNoticeByNoticeTypeAndAssessmentNumner(NOTICE_TYPE_ESD,
							basicProperty.getUpicNo());
					if (esdNotice == null)
						errors.add("invalid.esd.not.generated");
					else if ((DateUtils.noOfDays(esdNotice.getNoticeDate(), new Date())) < 15)
						errors.add("invalid.time.not.lapsed");
				}
			} else if (NOTICE_TYPE_INVENTORY.equals(noticeType)) {
				validateDemandBill(basicProperty);
				PtNotice distressNotice = noticeService.getNoticeByNoticeTypeAndAssessmentNumner(NOTICE_TYPE_DISTRESS,
						basicProperty.getUpicNo());
				if (distressNotice != null) {
					DateTime noticeDate = new DateTime(distressNotice.getNoticeDate());
					DateTime currDate = new DateTime();
					if (!currDate.isAfter(noticeDate.plusDays(16))) {
						errors.add("invntry.distress.notice.not.exists");
					}
				} else {
					errors.add("invntry.distress.notice.not.exists");
				}
			}
		}
		return errors;
	}

	private List<String> validateDemandBill(final BasicProperty basicProperty) {
		List<String> errors = new ArrayList<String>();
		final BigDecimal totalDue = getTotalPropertyTaxDue(basicProperty);
		if (totalDue.compareTo(BigDecimal.ZERO) == 0)
			errors.add("common.no.property.due");
		else {
			boolean billExists = false;
			billExists = getDemandBillByAssessmentNo(basicProperty);
			if (!billExists)
				errors.add("common.demandbill.not.exists");
		}
		return errors;
	}

    public EgBill getBillByAssessmentNumber(final BasicProperty basicProperty) {
        final StringBuilder queryStr = new StringBuilder();
        queryStr.append(
                "FROM EgBill WHERE module =:module AND egBillType.code =:billType AND consumerId =:assessmentNo AND is_history = 'N'");
        final Query qry = entityManager.createQuery(queryStr.toString());
        qry.setParameter("module", moduleDao.getModuleByName(PTMODULENAME));
        qry.setParameter("billType", BILLTYPE_MANUAL);
        qry.setParameter("assessmentNo", basicProperty.getUpicNo());
        final EgBill egBill = qry.getResultList() != null && !qry.getResultList().isEmpty() ? (EgBill) qry.getResultList().get(0)
                : null;
        return egBill;
    }

    public BigDecimal getTotalPropertyTaxDue(final BasicProperty basicProperty) {
        return propertyService.getTotalPropertyTaxDue(basicProperty);
    }
    
    private Map<String, Object> populateReportParams(Map<String, Object> reportParams ,City city ,BasicProperty basicProperty) {
        reportParams.put(CITY_NAME, city.getPreferences().getMunicipalityName());
        reportParams.put(OWNER_NAME, basicProperty.getFullOwnerName());
        return reportParams;
    }
    
    private ReportOutput getNotice(PtNotice notice , String noticeType) {
        ReportOutput reportOutput = new ReportOutput();
        final FileStoreMapper fsm = notice.getFileStore();
        final File file = fileStoreService.fetch(fsm, FILESTORE_MODULE_NAME);
        byte[] bFile;
        try {
            bFile = FileUtils.readFileToByteArray(file);
        } catch (final IOException e) {
            throw new ApplicationRuntimeException("Exception while retrieving "+ noticeType +" : " + e);
        }
        reportOutput.setReportOutputData(bFile);
        reportOutput.setReportFormat(FileFormat.PDF);
        return reportOutput;
    }
    
    public BigDecimal getTotalPropertyTaxDueIncludingPenalty(final BasicProperty basicProperty) {
        return propertyService.getTotalPropertyTaxDueIncludingPenalty(basicProperty);
    }

	public ResponseEntity<byte[]> generateNotice(final String assessmentNo, String noticeType) {
		ReportOutput reportOutput = new ReportOutput();
		BasicProperty basicProperty = basicPropertyDAO.getBasicPropertyByPropertyID(assessmentNo);
		final PtNotice notice = noticeService.getNoticeByNoticeTypeAndAssessmentNumner(noticeType,
				basicProperty.getUpicNo());
		if (notice == null) {
			final Map<String, Object> reportParams = new HashMap<String, Object>();
			InputStream noticePDF = null;
			ReportRequest reportInput = null;
			final StringBuilder queryString = new StringBuilder();
			queryString.append("from City");
			final Query query = entityManager.createQuery(queryString.toString());
			final City city = (City) query.getSingleResult();
			populateReportParams(reportParams, city, basicProperty);
			final String noticeNo = propertyTaxNumberGenerator.generateNoticeNumber(noticeType);
			final SimpleDateFormat formatter = new SimpleDateFormat("MMM yyyy");
			if (NOTICE_TYPE_ESD.equals(noticeType)) {
				final Address ownerAddress = basicProperty.getAddress();
				reportParams.put(DOOR_NO, StringUtils.isNotBlank(ownerAddress.getHouseNoBldgApt())
						? ownerAddress.getHouseNoBldgApt() : "N/A");
				reportParams.put(FIN_YEAR, formatter.format(new Date()));
				final DateTime noticeDate = new DateTime(new Date());
				reportParams.put(TOTAL_TAX_DUE, getTotalPropertyTaxDue(basicProperty));
				reportParams.put(FUTURE_DATE, DateUtils.getDefaultFormattedDate(noticeDate.plusDays(2).toDate()));
				reportParams.put(ESD_NOTICE_NUMBER, noticeNo);
				reportParams.put(ESD_NOTICE_DATE, DateUtils.getDefaultFormattedDate(new Date()));
				final AppConfigValues appConfigValues = appConfigValuesService.getAppConfigValueByDate(PTMODULENAME,
						APPCONFIG_CLIENT_SPECIFIC_DMD_BILL, new Date());
				final String value = appConfigValues != null ? appConfigValues.getValue() : "";
				if ("Y".equalsIgnoreCase(value)) {
					final DemandBillService demandBillService = (DemandBillService) beanProvider
							.getBean("demandBillService");
					reportParams.putAll(demandBillService.getDemandBillDetails(basicProperty));
				} else {
					final EgBill egBill = getBillByAssessmentNumber(basicProperty);
					reportParams.put(BILL_DATE, DateUtils.getDefaultFormattedDate(egBill.getCreateDate()));
					reportParams.put(BILL_NUMBER, egBill.getBillNo());
				}
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
			} else if (NOTICE_TYPE_INVENTORY.equals(noticeType)) {
				Installment currentInstall = propertyTaxCommonUtils.getCurrentPeriodInstallment();
				DateTime dateTime = new DateTime();
				DateTime currInstToDate = new DateTime(currentInstall.getToDate());
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

			} else if (NOTICE_TYPE_DISTRESS.equals(noticeType)) {
				reportParams.put(TOTAL_TAX_DUE, getTotalPropertyTaxDueIncludingPenalty(basicProperty));
				DateTime noticeDate = new DateTime();
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
				if (cityGrade != null && cityGrade != ""
						&& cityGrade.equalsIgnoreCase(PropertyTaxConstants.CITY_GRADE_CORPORATION)) {
					reportParams.put(SECTION_ACT, PropertyTaxConstants.CORPORATION_ESD_NOTICE_SECTION_ACT);
					reportInput = new ReportRequest(PropertyTaxConstants.REPORT_DISTRESS_CORPORATION, reportParams,
							reportParams);
				} else {
					reportParams.put(SECTION_ACT, PropertyTaxConstants.MUNICIPALITY_DISTRESS_NOTICE_SECTION_ACT);
					reportInput = new ReportRequest(PropertyTaxConstants.REPORT_DISTRESS_MUNICIPALITY, reportParams,
							reportParams);
				}
			}
			reportInput.setPrintDialogOnOpenReport(true);
			reportInput.setReportFormat(FileFormat.PDF);
			reportOutput = reportService.createReport(reportInput);
			if (reportOutput != null && reportOutput.getReportOutputData() != null)
				noticePDF = new ByteArrayInputStream(reportOutput.getReportOutputData());
			noticeService.saveNotice(basicProperty.getPropertyForBasicProperty().getApplicationNo(), noticeNo,
					noticeType, basicProperty, noticePDF);
		} else {
			reportOutput = getNotice(notice, noticeType);
		}
		final HttpHeaders headers = new HttpHeaders();
		headers.setContentType(MediaType.parseMediaType("application/pdf"));
		headers.add("content-disposition", "inline;filename=ESDNotice_" + basicProperty.getUpicNo() + ".pdf");
		return new ResponseEntity<byte[]>(reportOutput.getReportOutputData(), headers, HttpStatus.CREATED);
	}
}
