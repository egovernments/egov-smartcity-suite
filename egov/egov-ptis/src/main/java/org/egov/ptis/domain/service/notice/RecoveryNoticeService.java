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
import static org.egov.ptis.constants.PropertyTaxConstants.PTMODULENAME;

import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.math.BigDecimal;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;

import org.apache.commons.io.FileUtils;
import org.apache.commons.lang3.StringUtils;
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
import org.egov.ptis.domain.entity.property.BasicProperty;
import org.egov.ptis.domain.service.property.PropertyService;
import org.egov.ptis.notice.PtNotice;
import org.egov.ptis.service.DemandBill.DemandBillService;
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

    public ResponseEntity<byte[]> generateESDNotice(final BasicProperty basicProperty) {
        ReportOutput reportOutput = new ReportOutput();
        final PtNotice notice = noticeService.getNoticeByNoticeTypeAndAssessmentNumner(PropertyTaxConstants.NOTICE_TYPE_ESD,
                basicProperty.getUpicNo());
        final Map<String, Object> reportParams = new HashMap<String, Object>();
        final SimpleDateFormat formatter = new SimpleDateFormat("MMM yyyy");
        if (notice == null) {
            InputStream noticePDF = null;
            ReportRequest reportInput = null;
            final StringBuilder queryString = new StringBuilder();
            queryString.append("from City");
            final Query query = entityManager.createQuery(queryString.toString());
            final City city = (City) query.getSingleResult();
            reportParams.put("cityName", city.getPreferences().getMunicipalityName());
            final Address ownerAddress = basicProperty.getAddress();
            reportParams.put("doorNo",
                    StringUtils.isNotBlank(ownerAddress.getHouseNoBldgApt()) ? ownerAddress.getHouseNoBldgApt() : "N/A");
            reportParams.put("totalTaxDue", getTotalPropertyTaxDue(basicProperty));
            reportParams.put("finYear", formatter.format(new Date()));
            reportParams.put("ownerName", basicProperty.getFullOwnerName());
            final DateTime noticeDate = new DateTime(new Date());
            reportParams.put("futureDate", DateUtils.getDefaultFormattedDate(noticeDate.plusDays(2).toDate()));
            final String noticeNo = propertyTaxNumberGenerator.generateNoticeNumber(PropertyTaxConstants.NOTICE_TYPE_ESD);
            reportParams.put("eSDNoticeNumber", noticeNo);
            reportParams.put("eSDNoticeDate", DateUtils.getDefaultFormattedDate(new Date()));
            final AppConfigValues appConfigValues = appConfigValuesService.getAppConfigValueByDate(PTMODULENAME,
                    APPCONFIG_CLIENT_SPECIFIC_DMD_BILL, new Date());
            final String value = appConfigValues != null ? appConfigValues.getValue() : "";
            if ("Y".equalsIgnoreCase(value)) {
                final DemandBillService demandBillService = (DemandBillService) beanProvider.getBean("demandBillService");
                reportParams.putAll(demandBillService.getDemandBillDetails(basicProperty));
            } else {
                final EgBill egBill = getBillByAssessmentNumber(basicProperty);
                reportParams.put("billDate", DateUtils.getDefaultFormattedDate(egBill.getCreateDate()));
                reportParams.put("billNumber", egBill.getBillNo());
            }
            final String cityGrade = city.getGrade();
            if (cityGrade != null && cityGrade != ""
                    && cityGrade.equalsIgnoreCase(PropertyTaxConstants.CITY_GRADE_CORPORATION)) {
                reportParams.put("sectionAct", PropertyTaxConstants.CORPORATION_ESD_NOTICE_SECTION_ACT);
                reportInput = new ReportRequest(PropertyTaxConstants.REPORT_ESD_NOTICE_CORPORATION, reportParams,
                        reportParams);
            } else {
                reportParams.put("sectionAct", PropertyTaxConstants.MUNICIPALITY_ESD_NOTICE_SECTION_ACT);
                reportInput = new ReportRequest(PropertyTaxConstants.REPORT_ESD_NOTICE_MUNICIPALITY, reportParams,
                        reportParams);
            }
            reportInput.setPrintDialogOnOpenReport(true);
            reportInput.setReportFormat(FileFormat.PDF);
            reportOutput = reportService.createReport(reportInput);
            if (reportOutput != null && reportOutput.getReportOutputData() != null)
                noticePDF = new ByteArrayInputStream(reportOutput.getReportOutputData());
            noticeService.saveNotice(basicProperty.getPropertyForBasicProperty().getApplicationNo(),
                    noticeNo, PropertyTaxConstants.NOTICE_TYPE_ESD, basicProperty, noticePDF);

        } else {
            final FileStoreMapper fsm = notice.getFileStore();
            final File file = fileStoreService.fetch(fsm, FILESTORE_MODULE_NAME);
            byte[] bFile;
            try {
                bFile = FileUtils.readFileToByteArray(file);
            } catch (final IOException e) {
                throw new ApplicationRuntimeException("Exception while retrieving ESD Notice: " + e);
            }
            reportOutput.setReportOutputData(bFile);
            reportOutput.setReportFormat(FileFormat.PDF);
        }
        final HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.parseMediaType("application/pdf"));
        headers.add("content-disposition", "inline;filename=ESDNotice_" + basicProperty.getUpicNo() + ".pdf");
        return new ResponseEntity<byte[]>(reportOutput.getReportOutputData(), headers, HttpStatus.CREATED);
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

}
