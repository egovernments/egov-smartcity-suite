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

package org.egov.ptis.domain.service.intgdemandbill;

import static org.egov.ptis.constants.PropertyTaxConstants.APPCONFIG_CLIENT_SPECIFIC_DMD_BILL;
import static org.egov.ptis.constants.PropertyTaxConstants.BILLTYPE_MANUAL;
import static org.egov.ptis.constants.PropertyTaxConstants.FILESTORE_MODULE_NAME;
import static org.egov.ptis.constants.PropertyTaxConstants.INTEGRATED_BILL;
import static org.egov.ptis.constants.PropertyTaxConstants.NOTICE_TYPE_BILL;
import static org.egov.ptis.constants.PropertyTaxConstants.PTMODULENAME;

import java.io.File;
import java.io.IOException;

import org.apache.commons.io.FileUtils;
import org.egov.demand.model.EgBill;
import org.egov.infra.admin.master.service.ModuleService;
import org.egov.infra.config.core.ApplicationThreadLocals;
import org.egov.infra.exception.ApplicationRuntimeException;
import org.egov.infra.filestore.entity.FileStoreMapper;
import org.egov.infra.filestore.service.FileStoreService;
import org.egov.infra.reporting.engine.ReportFormat;
import org.egov.infra.reporting.engine.ReportOutput;
import org.egov.infstr.services.PersistenceService;
import org.egov.ptis.domain.dao.property.BasicPropertyDAO;
import org.egov.ptis.domain.entity.property.BasicProperty;
import org.egov.ptis.domain.service.bill.BillService;
import org.egov.ptis.notice.PtNotice;
import org.egov.ptis.service.DemandBill.DemandBillService;
import org.egov.ptis.service.utils.PropertyTaxCommonUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.validation.BindingResult;

@Service
public class IntgDemandBillService {

    @Autowired
    private BasicPropertyDAO basicPropertyDAO;
    @Autowired
    private ApplicationContext beanProvider;
    @Autowired
    private PropertyTaxCommonUtils propertyTaxCommonUtils;
    @Autowired
    private BillService billService;
    @Autowired
    private ModuleService moduleDao;
    @Autowired
    protected transient PersistenceService persistenceService;
    @Autowired
    protected FileStoreService fileStoreService;

    public void validate(final EgBill egBill, final BindingResult errors) {
        BasicProperty basicProperty = basicPropertyDAO.getBasicPropertyByPropertyID(egBill.getConsumerId());
        if (basicProperty == null)
            errors.reject("property.invalid", "property.invalid");
        else if (basicProperty.getProperty().getIsExemptedFromTax())
            errors.reject("error.msg.taxExempted", "error.msg.taxExempted");
    }

    public ResponseEntity<byte[]> generateIntgDemandBill(final String assessment) {
        ReportOutput reportOutput;
        String clientSpecificDmdBill = propertyTaxCommonUtils.getAppConfigValue(APPCONFIG_CLIENT_SPECIFIC_DMD_BILL, PTMODULENAME);
        if ("Y".equalsIgnoreCase(clientSpecificDmdBill)) {
            DemandBillService demandBillService = (DemandBillService) beanProvider.getBean("demandBillService");
            reportOutput = demandBillService.generateDemandBill(assessment, INTEGRATED_BILL);
        } else {
            reportOutput = getBill(assessment);
        }
        final HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.parseMediaType("application/pdf"));
        headers.add("content-disposition", "inline;filename=" + INTEGRATED_BILL + "_" + assessment + ".pdf");
        return new ResponseEntity<>(reportOutput.getReportOutputData(), headers, HttpStatus.CREATED);
    }

    private ReportOutput getBill(final String assessment) {
        ReportOutput reportOutput;
        BasicProperty basicProperty = basicPropertyDAO.getBasicPropertyByPropertyID(assessment);
        final EgBill egBill = (EgBill) persistenceService.find("FROM EgBill WHERE module = ? "
                + "AND egBillType.code = ? AND consumerId = ? AND is_history = 'N'",
                moduleDao.getModuleByName(PTMODULENAME), BILLTYPE_MANUAL, basicProperty.getUpicNo());
        if (egBill == null)
            reportOutput = billService.generateBill(basicProperty, ApplicationThreadLocals.getUserId().intValue());
        else {
            final String query = "SELECT notice FROM EgBill bill, PtNotice notice left join notice.basicProperty bp "
                    + "WHERE bill.is_History = 'N' "
                    + "AND bill.egBillType.code = ? "
                    + "AND bill.billNo = notice.noticeNo " + "AND notice.noticeType = ? " + "AND bp = ?";
            final PtNotice ptNotice = (PtNotice) persistenceService.find(query, BILLTYPE_MANUAL, NOTICE_TYPE_BILL,
                    basicProperty);
            reportOutput = new ReportOutput();
            if (ptNotice != null && ptNotice.getFileStore() != null) {
                final FileStoreMapper fsm = ptNotice.getFileStore();
                final File file = fileStoreService.fetch(fsm, FILESTORE_MODULE_NAME);
                byte[] bFile;
                try {
                    bFile = FileUtils.readFileToByteArray(file);
                    reportOutput.setReportOutputData(bFile);
                    reportOutput.setReportFormat(ReportFormat.PDF);
                } catch (IOException e) {
                    throw new ApplicationRuntimeException(
                            "IntgDemandBillService.getBill() : Integrated Bill Generation Exception : " + e);
                }
            }
        }
        return reportOutput;
    }

}
