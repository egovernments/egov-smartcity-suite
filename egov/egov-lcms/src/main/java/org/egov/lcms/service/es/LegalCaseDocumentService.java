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

package org.egov.lcms.service.es;

import org.egov.infra.admin.master.service.CityService;
import org.egov.infra.config.core.ApplicationThreadLocals;
import org.egov.lcms.entity.es.LegalCaseDocument;
import org.egov.lcms.masters.entity.enums.JudgmentImplIsComplied;
import org.egov.lcms.repository.es.LegalCaseDocumentRepository;
import org.egov.lcms.transactions.entity.Judgment;
import org.egov.lcms.transactions.entity.JudgmentImpl;
import org.egov.lcms.transactions.entity.LegalCase;
import org.egov.lcms.transactions.entity.LegalCaseDisposal;
import org.egov.lcms.transactions.entity.LegalCaseInterimOrder;
import org.egov.lcms.utils.constants.LcmsConstants;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.text.ParseException;
import java.util.Map;

import static org.apache.commons.lang3.StringUtils.defaultString;
import static org.egov.infra.utils.ApplicationConstant.CITY_CORP_GRADE_KEY;
import static org.egov.infra.utils.ApplicationConstant.CITY_DIST_NAME_KEY;
import static org.egov.infra.utils.ApplicationConstant.CITY_REGION_NAME_KEY;

@Service
@Transactional(readOnly = true)
public class LegalCaseDocumentService {

    private final LegalCaseDocumentRepository legalCaseDocumentRepository;

    @Autowired
    private CityService cityService;

    @Autowired
    public LegalCaseDocumentService(final LegalCaseDocumentRepository legalCaseDocumentRepository) {
        this.legalCaseDocumentRepository = legalCaseDocumentRepository;
    }

    public LegalCaseDocument persistLegalCaseDocumentIndex(final LegalCase legalCase,
            final LegalCaseInterimOrder legalCaseInterimOrder, final Judgment judgment, final JudgmentImpl judgmentImpl,
            final LegalCaseDisposal legalCaseDisposal) throws ParseException {
        final Map<String, Object> cityInfo = cityService.cityDataAsMap();
        LegalCaseDocument legalCaseDocument = null;
        final String lcnumber = "\"" + legalCase.getLcNumber() + "\"";

        if (legalCase.getLcNumber() != null)
            legalCaseDocument = legalCaseDocumentRepository.findByLcNumberAndCityCode(lcnumber,
                    ApplicationThreadLocals.getCityCode());

        if (legalCaseDocument != null)
            updateLegalCaseIndex(legalCaseDocument, legalCase, legalCaseInterimOrder, judgment, judgmentImpl,
                    legalCaseDisposal);
        else {
            legalCaseDocument = LegalCaseDocument.builder().withCaseNumber(legalCase.getCaseNumber())
                    .withLcNumber(legalCase.getLcNumber()).withCaseTitle(legalCase.getCaseTitle())
                    .withCaseDate(legalCase.getCaseDate()).withCaseReceivingDate(legalCase.getCaseReceivingDate())
                    .withCaseType(legalCase.getCaseTypeMaster().getCaseType())
                    .withCourtName(legalCase.getCourtMaster().getName())
                    .withCourtType(legalCase.getCourtMaster().getCourtType().getCourtType())
                    .withPetitionType(legalCase.getPetitionTypeMaster().getPetitionType())
                    .withStatus(legalCase.getStatus().getDescription())
                    .withPetitionerNames(legalCase.getPetitionersNames())
                    .withRespondantNames(legalCase.getRespondantNames()).withNextDate(legalCase.getNextDate())
                    .withPreviousCaseNumber(legalCase.getAppealNum() != null ? legalCase.getAppealNum() : "")
                    .withOfficerIncharge(
                            legalCase.getOfficerIncharge() != null ? legalCase.getOfficerIncharge().getName() : "")
                    .withSubStatus(legalCase.getReportStatus() !=null ? legalCase.getReportStatus().getName() : "")
                    .withPwrDueDate(legalCase.getPwrList().get(0).getPwrDueDate())
                    .withCaDueDate(legalCase.getPwrList().get(0).getCaDueDate())
                    .withFiledByULB(legalCase.getIsFiledByCorporation())
                    .withCityName(ApplicationThreadLocals.getCityName())
                    .withCityCode(ApplicationThreadLocals.getCityCode())
                    .withCityGrade(defaultString((String) cityInfo.get(CITY_CORP_GRADE_KEY)))
                    .withRegionName(defaultString((String) cityInfo.get(CITY_REGION_NAME_KEY)))
                    .withDistrictName(defaultString((String) cityInfo.get(CITY_DIST_NAME_KEY)))

                    .withCreatedDate(legalCase.getCaseDate()).build();

            createLeglCaseDocument(legalCaseDocument);
        }

        return legalCaseDocument;
    }

    public LegalCaseDocument updateLegalCaseIndex(final LegalCaseDocument legalCaseDocument, final LegalCase legalCase,
            final LegalCaseInterimOrder legalCaseInterimOrder, final Judgment judgment, final JudgmentImpl judgmentImpl,
            final LegalCaseDisposal legalCaseDisposal) throws ParseException {
        legalCaseDocument.setCaseDate(legalCase.getCaseDate());
        legalCaseDocument.setCaseReceivingDate(legalCase.getCaseReceivingDate());
        legalCaseDocument.setCaseTitle(legalCase.getCaseTitle());
        legalCaseDocument.setCaseType(legalCase.getCaseTypeMaster().getCaseType());
        legalCaseDocument.setCourtName(legalCase.getCourtMaster().getName());
        legalCaseDocument.setCourtType(legalCase.getCourtMaster().getCourtType().getCourtType());
        legalCaseDocument.setPetitionType(legalCase.getPetitionTypeMaster().getPetitionType());
        legalCaseDocument.setFiledByULB(legalCase.getIsFiledByCorporation());
        legalCaseDocument.setNextDate(legalCase.getNextDate());
        legalCaseDocument.setOfficerIncharge(
                legalCase.getOfficerIncharge() != null ? legalCase.getOfficerIncharge().getName() : "");
        legalCaseDocument.setPetitionerNames(legalCase.getPetitionersNames());
        legalCaseDocument.setRespondantNames(legalCase.getRespondantNames());
        legalCaseDocument.setPreviousCaseNumber(legalCase.getAppealNum() != null ? legalCase.getAppealNum() : "");
        legalCaseDocument.setStatus(legalCase.getStatus().getDescription());
        legalCaseDocument
                .setSubStatus(legalCase.getReportStatus() != null ? legalCase.getReportStatus().getName() : "");
        if (legalCase.getPwrList() != null && !legalCase.getPwrList().isEmpty()) {
            legalCaseDocument.setPwrDueDate(legalCase.getPwrList().get(0).getPwrDueDate());
            legalCaseDocument.setCaFilingDate(legalCase.getPwrList().get(0).getCaFilingDate());
            legalCaseDocument.setCaDueDate(legalCase.getPwrList().get(0).getCaDueDate());
        }
        if (legalCase.getLegalCaseAdvocates() != null && !legalCase.getLegalCaseAdvocates().isEmpty()) {
            legalCaseDocument.setAdvocateName(legalCase.getLegalCaseAdvocates().get(0).getAdvocateMaster().getName());
            legalCaseDocument.setSeniorAdvocate(
                    legalCase.getLegalCaseAdvocates().get(0).getAdvocateMaster().getIsSenioradvocate());
        }
        if (LcmsConstants.LEGALCASE_INTERIMSTAY_STATUS.equalsIgnoreCase(legalCase.getStatus().getCode()))
            if (!legalCase.getLegalCaseInterimOrder().isEmpty())
                legalCaseDocument
                        .setInterimOrderType(legalCase.getLegalCaseInterimOrder().get(0).getInterimOrder().getInterimOrderType());
            else
                legalCaseDocument.setInterimOrderType(legalCaseInterimOrder.getInterimOrder().getInterimOrderType());
        if (LcmsConstants.LEGALCASE_STATUS_JUDGMENT.equalsIgnoreCase(legalCase.getStatus().getCode())) {
            legalCaseDocument.setJudgmentOutcome(judgment.getJudgmentType().getName());
            legalCaseDocument.setJudgmentDate(judgment.getOrderDate());
            legalCaseDocument.setOrderSentToDeptDate(judgment.getSentToDeptOn());
            legalCaseDocument.setDeadLineImplementByDate(judgment.getImplementByDate());
        }
        if (LcmsConstants.LEGALCASE_STATUS_JUDGMENT_IMPLIMENTED.equalsIgnoreCase(legalCase.getStatus().getCode())) {
            legalCaseDocument.setJudgmentImplDate(judgmentImpl.getDateOfCompliance());
            legalCaseDocument.setJudgmentImplemented(judgmentImpl.getJudgmentImplIsComplied().name());
            if (judgmentImpl.getJudgmentImplIsComplied().toString().equals(JudgmentImplIsComplied.NO.toString()))
                legalCaseDocument.setImplementationFailure(judgmentImpl.getImplementationFailure().name());
        }
        if (LcmsConstants.LEGALCASE_STATUS_CLOSED.equalsIgnoreCase(legalCase.getStatus().getCode()))
            legalCaseDocument.setDisposalDate(legalCaseDisposal.getDisposalDate());
        legalCaseDocument.setCreatedDate(legalCase.getCaseDate());

        createLeglCaseDocument(legalCaseDocument);
        return legalCaseDocument;

    }

    @Transactional
    public LegalCaseDocument createLeglCaseDocument(final LegalCaseDocument legalCaseIndex) {
        legalCaseDocumentRepository.save(legalCaseIndex);
        return legalCaseIndex;
    }
}
