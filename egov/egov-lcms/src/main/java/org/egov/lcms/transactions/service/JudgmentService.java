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

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.egov.commons.EgwStatus;
import org.egov.infra.utils.DateUtils;
import org.egov.lcms.transactions.entity.Judgment;
import org.egov.lcms.transactions.entity.JudgmentDocuments;
import org.egov.lcms.transactions.repository.JudgmentRepository;
import org.egov.lcms.transactions.repository.LegalCaseRepository;
import org.egov.lcms.utils.LegalCaseUtil;
import org.egov.lcms.utils.constants.LcmsConstants;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional(readOnly = true)
public class JudgmentService {

    @Autowired
    private final JudgmentRepository judgmentRepository;

    @Autowired
    private LegalCaseRepository legalCaseRepository;

    @Autowired
    private LegalCaseUtil legalCaseUtil;

    @Autowired
    public JudgmentService(final JudgmentRepository judgmentRepository) {
        this.judgmentRepository = judgmentRepository;
    }

    @Transactional
    public Judgment persist(final Judgment judgment) {
        final List<JudgmentDocuments> judgmentDoc = legalCaseUtil.getJudgmentDocumentList(judgment);
        processAndStoreApplicationDocuments(judgment, judgmentDoc);
        final EgwStatus statusObj = legalCaseUtil.getStatusForModuleAndCode(LcmsConstants.MODULE_TYPE_LEGALCASE,
                LcmsConstants.LEGALCASE_STATUS_JUDGMENT);
        judgment.getLegalCase().setStatus(statusObj);
        legalCaseRepository.save(judgment.getLegalCase());
        return judgmentRepository.save(judgment);

    }

    public List<Judgment> findAll() {
        return judgmentRepository.findAll(new Sort(Sort.Direction.ASC, ""));
    }

    public Judgment findById(final Long id) {
        return judgmentRepository.findOne(id);
    }

    public List<JudgmentDocuments> getJudgmentDocList(final Judgment judgment) {
        return judgment.getJudgmentDocuments();
    }

    protected void processAndStoreApplicationDocuments(final Judgment judgment,
            final List<JudgmentDocuments> judgmentDoc) {
        if (judgment.getId() == null) {
            if (!judgment.getJudgmentDocuments().isEmpty())
                for (final JudgmentDocuments applicationDocument : judgment.getJudgmentDocuments()) {
                    applicationDocument.setJudgment(judgment);
                    applicationDocument.setDocumentName("Judgment");
                    applicationDocument.setSupportDocs(legalCaseUtil.addToFileStore(applicationDocument.getFiles()));
                }
        } else {
            final List<JudgmentDocuments> tempJudgmentDoc = new ArrayList<JudgmentDocuments>(
                    judgment.getJudgmentDocuments());
            for (final JudgmentDocuments applicationDocument : tempJudgmentDoc) {
                applicationDocument.setJudgment(judgment);
                applicationDocument.setDocumentName("Judgment");
                applicationDocument.getSupportDocs()
                        .addAll(legalCaseUtil.addToFileStore(applicationDocument.getFiles()));
                judgment.getJudgmentDocuments().add(applicationDocument);
            }
            judgment.getJudgmentDocuments().addAll(judgmentDoc);
        }

    }

    @Transactional(propagation = Propagation.REQUIRES_NEW, readOnly = true)
    public Judgment findByLCNumber(final String lcNumber) {
        return judgmentRepository.findByLegalCase_lcNumber(lcNumber);
    }

    public void updateNextDate(final Judgment judgment) {
        final Date nextDate = judgment.getEnquiryDate() != null ? judgment.getEnquiryDate() : judgment.getOrderDate();
        if (!DateUtils.compareDates(judgment.getLegalCase().getNextDate(), nextDate))
            judgment.getLegalCase().setNextDate(nextDate);
        else if (judgment.getEnquiryDate() != null)
            judgment.getLegalCase().setNextDate(judgment.getEnquiryDate());
        else
            judgment.getLegalCase().setNextDate(judgment.getOrderDate());
    }

}