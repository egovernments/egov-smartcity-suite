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
package org.egov.lcms.transactions.service;

import java.io.IOException;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.Iterator;
import java.util.List;

import org.egov.commons.EgwStatus;
import org.egov.infra.filestore.service.FileStoreService;
import org.egov.infra.utils.DateUtils;
import org.egov.lcms.transactions.entity.LcInterimOrderDocuments;
import org.egov.lcms.transactions.entity.LegalCase;
import org.egov.lcms.transactions.entity.LegalCaseInterimOrder;
import org.egov.lcms.transactions.entity.ReportStatus;
import org.egov.lcms.transactions.repository.LCInterimOrderDocumentsRepository;
import org.egov.lcms.transactions.repository.LegalCaseInterimOrderRepository;
import org.egov.lcms.utils.LegalCaseUtil;
import org.egov.lcms.utils.constants.LcmsConstants;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

@Service
@Transactional(readOnly = true)
public class LegalCaseInterimOrderService {

    private final LegalCaseInterimOrderRepository legalCaseInterimOrderRepository;

    @Autowired
    private LegalCaseUtil legalCaseUtil;

    @Autowired
    private FileStoreService fileStoreService;

    @Autowired
    private LegalCaseSmsService legalCaseSmsService;

    @Autowired
    private LegalCaseService legalCaseService;

    @Autowired
    private LCInterimOrderDocumentsRepository lCInterimOrderDocumentsRepository;

    @Autowired
    public LegalCaseInterimOrderService(final LegalCaseInterimOrderRepository legalCaseInterimOrderRepository) {
        this.legalCaseInterimOrderRepository = legalCaseInterimOrderRepository;
    }

    @Transactional
    public LegalCaseInterimOrder persist(final LegalCaseInterimOrder legalCaseInterimOrder, final MultipartFile[] files)
            throws IOException, ParseException {
        final EgwStatus statusObj = legalCaseUtil.getStatusForModuleAndCode(LcmsConstants.MODULE_TYPE_LEGALCASE,
                LcmsConstants.LEGALCASE_INTERIMSTAY_STATUS);
        legalCaseInterimOrder.getLegalCase().setStatus(statusObj);
        final ReportStatus reportStatus = null;
        legalCaseInterimOrder.getLegalCase().setReportStatus(reportStatus);
        updateNextDate(legalCaseInterimOrder, legalCaseInterimOrder.getLegalCase());
        final LegalCaseInterimOrder savedlcInterimOrder = legalCaseInterimOrderRepository.save(legalCaseInterimOrder);
        legalCaseSmsService.sendSmsToOfficerInchargeInterimOrder(legalCaseInterimOrder);
        legalCaseSmsService.sendSmsToStandingCounselForInterimOrder(legalCaseInterimOrder);
        legalCaseService.persistLegalCaseIndex(legalCaseInterimOrder.getLegalCase(), legalCaseInterimOrder, null, null,
                null);
        final List<LcInterimOrderDocuments> documentDetails = getDocumentDetails(savedlcInterimOrder, files);
        if (!documentDetails.isEmpty()) {
            savedlcInterimOrder.setLcInterimOrderDocuments(documentDetails);
            persistDocuments(documentDetails);
        }

        return savedlcInterimOrder;
        /* legalCaseRepository.save(legalCaseInterimOrder.getLegalCase()); */
    }

    public List<LegalCaseInterimOrder> findAll() {
        return legalCaseInterimOrderRepository.findAll(new Sort(Sort.Direction.DESC, "id"));
    }

    public LegalCaseInterimOrder findById(final Long id) {
        return legalCaseInterimOrderRepository.findById(id);
    }

    public List<LcInterimOrderDocuments> getLcInterimOrderDocList(final LegalCaseInterimOrder legalCaseInterimOrder) {
        return legalCaseInterimOrder.getLcInterimOrderDocuments();
    }

    public List<LegalCaseInterimOrder> findByLCNumber(final String lcNumber) {
        return legalCaseInterimOrderRepository.findByLegalCase_lcNumber(lcNumber);
    }

    public void updateNextDate(final LegalCaseInterimOrder legalCaseInterimOrder, final LegalCase legalCase) {

        if (!DateUtils.compareDates(legalCase.getNextDate(), legalCaseInterimOrder.getIoDate()))
            legalCase.setNextDate(legalCaseInterimOrder.getIoDate());
        else {
            final List<Date> ioDateList = new ArrayList<Date>(0);
            ioDateList.add(legalCaseInterimOrder.getIoDate());
            final Iterator<LegalCaseInterimOrder> iteratorInterimOrder = legalCase.getLegalCaseInterimOrder()
                    .iterator();
            while (iteratorInterimOrder.hasNext()) {
                final LegalCaseInterimOrder lcinterimorderObj = iteratorInterimOrder.next();
                if (!lcinterimorderObj.getId().equals(legalCaseInterimOrder.getId()))
                    ioDateList.add(lcinterimorderObj.getIoDate());
            }

            legalCase.setNextDate(Collections.max(ioDateList));
        }
    }

    public List<LcInterimOrderDocuments> getDocumentDetails(final LegalCaseInterimOrder legalCaseInterimOrder,
            final MultipartFile[] files) throws IOException {
        final List<LcInterimOrderDocuments> documentDetailsList = new ArrayList<LcInterimOrderDocuments>();

        if (files != null)
            for (int i = 0; i < files.length; i++)
                if (!files[i].isEmpty()) {
                    final LcInterimOrderDocuments applicationDocument = new LcInterimOrderDocuments();
                    applicationDocument.setLegalCaseInterimOrder(legalCaseInterimOrder);
                    applicationDocument.setDocumentName(LcmsConstants.LCINTERIOMORDER_DOCUMENTNAME);
                    applicationDocument.setSupportDocs(
                            fileStoreService.store(files[i].getInputStream(), files[i].getOriginalFilename(),
                                    files[i].getContentType(), LcmsConstants.FILESTORE_MODULECODE));
                    documentDetailsList.add(applicationDocument);

                }
        return documentDetailsList;
    }

    public void persistDocuments(final List<LcInterimOrderDocuments> documentDetailsList) {
        if (documentDetailsList != null && !documentDetailsList.isEmpty())
            for (final LcInterimOrderDocuments doc : documentDetailsList)
                lCInterimOrderDocumentsRepository.save(doc);
    }
}