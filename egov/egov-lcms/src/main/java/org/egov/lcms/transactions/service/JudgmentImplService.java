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
package org.egov.lcms.transactions.service;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.egov.commons.EgwStatus;
import org.egov.infra.filestore.service.FileStoreService;
import org.egov.lcms.transactions.entity.Appeal;
import org.egov.lcms.transactions.entity.AppealDocuments;
import org.egov.lcms.transactions.entity.Contempt;
import org.egov.lcms.transactions.entity.JudgmentImpl;
import org.egov.lcms.transactions.entity.ReportStatus;
import org.egov.lcms.transactions.repository.AppealDocumentsRepository;
import org.egov.lcms.transactions.repository.JudgmentImplRepository;
import org.egov.lcms.utils.LegalCaseUtil;
import org.egov.lcms.utils.constants.LcmsConstants;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

@Service
@Transactional(readOnly = true)
public class JudgmentImplService {

    private final JudgmentImplRepository judgmentImplRepository;

    @Autowired
    private AppealDocumentsRepository appealDocumentsRepository;

    @Autowired
    private LegalCaseService legalCaseService;

    @Autowired
    private LegalCaseUtil legalCaseUtil;

    @Autowired
    private FileStoreService fileStoreService;
    
    @Autowired
    private LegalCaseSmsService legalCaseSmsService;

    @Autowired
    public JudgmentImplService(final JudgmentImplRepository judgmentImplRepository) {
        this.judgmentImplRepository = judgmentImplRepository;
    }

    @Transactional
    public JudgmentImpl persist(final JudgmentImpl judgmentImpl, final MultipartFile[] files) throws IOException {
        persistAppealOrContempt(judgmentImpl);
        final JudgmentImpl savedjudgmentImpl = judgmentImplRepository.save(judgmentImpl);
        if (judgmentImpl.getImplementationFailure() != null
                && judgmentImpl.getImplementationFailure().toString().equals("Appeal")) {
            final List<AppealDocuments> documentDetails = getDocumentDetails(savedjudgmentImpl, files);
            if (!documentDetails.isEmpty()) {
                savedjudgmentImpl.getAppeal().get(0).setAppealDocuments(documentDetails);
                persistDocuments(documentDetails);
            }
        }
        return savedjudgmentImpl;
    }

    @Transactional
    public void saveOrUpdate(final JudgmentImpl judgmentImpl, final MultipartFile[] files) throws IOException {
        persist(judgmentImpl, files);
        if (judgmentImpl.getJudgment().getImplementByDate() != null)
            judgmentImpl.getJudgment().getLegalCase().setNextDate(judgmentImpl.getJudgment().getImplementByDate());
        else
            judgmentImpl.getJudgment().getLegalCase().setNextDate(judgmentImpl.getJudgment().getOrderDate());
        final EgwStatus statusObj = legalCaseUtil.getStatusForModuleAndCode(LcmsConstants.MODULE_TYPE_LEGALCASE,
                LcmsConstants.LEGALCASE_STATUS_JUDGMENT_IMPLIMENTED);
        judgmentImpl.getJudgment().getLegalCase().setStatus(statusObj);
        final ReportStatus reportStatus=null;
        judgmentImpl.getJudgment().getLegalCase().setReportStatus(reportStatus);
        judgmentImpl.getJudgment().getLegalCase().setNextDate(judgmentImpl.getDateOfCompliance());
        legalCaseSmsService.sendSmsToOfficerInchargeForJudgmentImpl(judgmentImpl);
        legalCaseService.save(judgmentImpl.getJudgment().getLegalCase());

    }

    @Transactional
    public void persistAppealOrContempt(final JudgmentImpl judgmentImpl) {
        if (judgmentImpl.getContempt().get(0).getCaNumber() != null) {
            for (final Contempt contemptObj : judgmentImpl.getContempt())
                contemptObj.setJudgmentImpl(judgmentImpl);
            judgmentImpl.getJudgment().getLegalCase().setNextDate(judgmentImpl.getContempt().get(0).getReceivingDate());
            judgmentImpl.getAppeal().clear();
        } else if (judgmentImpl.getAppeal().get(0).getSrNumber() != null) {
            for (final Appeal appealObj : judgmentImpl.getAppeal())
                if (appealObj.getSrNumber() != null && !"".equals(appealObj.getSrNumber()))
                    appealObj.setJudgmentImpl(judgmentImpl);
            judgmentImpl.getJudgment().getLegalCase().setNextDate(judgmentImpl.getAppeal().get(0).getAppealFiledOn());
            judgmentImpl.getContempt().clear();
        } else {
            judgmentImpl.getAppeal().clear();
            judgmentImpl.getContempt().clear();
        }
    }

    public List<AppealDocuments> getAppealDocList(final JudgmentImpl judgmentImpl) {
        final List<AppealDocuments> judgmentImplAppealDOc = new ArrayList<AppealDocuments>();
        final Set<AppealDocuments> appealDOcSet = new HashSet<AppealDocuments>();
        if (!judgmentImpl.getAppeal().isEmpty() && judgmentImpl.getAppeal().get(0) != null) {
            for (final AppealDocuments appealDocs : judgmentImpl.getAppeal().get(0).getAppealDocuments())
                appealDOcSet.add(appealDocs);
            judgmentImplAppealDOc.addAll(appealDOcSet);
        }
        return judgmentImplAppealDOc;
    }

    public List<AppealDocuments> getDocumentDetails(final JudgmentImpl judgmentImpl, final MultipartFile[] files)
            throws IOException {
        final List<AppealDocuments> documentDetailsList = new ArrayList<AppealDocuments>();

        if (files != null)
            for (int i = 0; i < files.length; i++)
                if (!files[i].isEmpty()) {
                    final AppealDocuments applicationDocument = new AppealDocuments();
                    applicationDocument.setAppeal(judgmentImpl.getAppeal().get(0));
                    applicationDocument.setDocumentName(LcmsConstants.APPEAL_DOCUMENTNAME);
                    applicationDocument.setSupportDocs(
                            fileStoreService.store(files[i].getInputStream(), files[i].getOriginalFilename(),
                                    files[i].getContentType(), LcmsConstants.FILESTORE_MODULECODE));
                    documentDetailsList.add(applicationDocument);

                }
        return documentDetailsList;
    }

    public void persistDocuments(final List<AppealDocuments> documentDetailsList) {
        if (documentDetailsList != null && !documentDetailsList.isEmpty())
            for (final AppealDocuments doc : documentDetailsList)
                appealDocumentsRepository.save(doc);
    }

    public List<JudgmentImpl> findAll() {
        return judgmentImplRepository.findAll(new Sort(Sort.Direction.ASC, " "));
    }

    public JudgmentImpl findOne(final Long id) {
        return judgmentImplRepository.findOne(id);
    }

}