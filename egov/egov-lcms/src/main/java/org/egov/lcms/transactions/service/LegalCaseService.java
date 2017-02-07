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
import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

import org.egov.eis.repository.PositionMasterRepository;
import org.egov.infra.filestore.service.FileStoreService;
import org.egov.lcms.masters.entity.AdvocateMaster;
import org.egov.lcms.masters.service.AdvocateMasterService;
import org.egov.lcms.transactions.entity.BipartisanDetails;
import org.egov.lcms.transactions.entity.CounterAffidavit;
import org.egov.lcms.transactions.entity.LegalCase;
import org.egov.lcms.transactions.entity.LegalCaseAdvocate;
import org.egov.lcms.transactions.entity.LegalCaseUploadDocuments;
import org.egov.lcms.transactions.entity.Pwr;
import org.egov.lcms.transactions.entity.PwrDocuments;
import org.egov.lcms.transactions.entity.ReportStatus;
import org.egov.lcms.transactions.repository.LegalCaseRepository;
import org.egov.lcms.transactions.repository.LegalCaseUploadDocumentsRepository;
import org.egov.lcms.transactions.repository.PwrDocumentsRepository;
import org.egov.lcms.transactions.repository.ReportStatusRepository;
import org.egov.lcms.utils.LegalCaseUtil;
import org.egov.lcms.utils.constants.LcmsConstants;
import org.egov.pims.commons.Position;
import org.hibernate.Session;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

@Service
@Transactional(readOnly = true)
public class LegalCaseService {

    private final LegalCaseRepository legalCaseRepository;

    @Autowired
    private PwrDocumentsRepository pwrDocumentsRepository;

    @Autowired
    private PositionMasterRepository positionMasterRepository;
    @Autowired
    private AdvocateMasterService advocateMasterService;

    @Autowired
    private LegalCaseUtil legalCaseUtil;

    @Autowired
    private FileStoreService fileStoreService;

    @Autowired
    private LegalCaseUploadDocumentsRepository legalCaseUploadDocumentsRepository;

    @Autowired
    private ReportStatusRepository reportStatusRepository;
    
    @PersistenceContext
    private EntityManager entityManager;
    
    public Session getCurrentSession() {
        return entityManager.unwrap(Session.class);
    }

    @Autowired
    public LegalCaseService(final LegalCaseRepository legalCaseRepository) {
        this.legalCaseRepository = legalCaseRepository;

    }

    public LegalCase findById(final Long Id) {
        return legalCaseRepository.findOne(Id);
    }

    public LegalCase findByLcNumber(final String lcnumber) {
        return legalCaseRepository.findByLcNumber(lcnumber);
    }

    public LegalCase getLegalCaseByCaseNumber(final String caseNumber) {
        return legalCaseRepository.findByCaseNumber(caseNumber);
    }

    @Transactional
    public LegalCase persist(LegalCase legalcase, final MultipartFile[] files) throws IOException {
        if (null != legalcase.getOfficerIncharge() && legalcase.getOfficerIncharge().getId() == null)
            legalcase.setOfficerIncharge(null);
        else {
            Position position = positionMasterRepository.findOne(legalcase.getOfficerIncharge().getId());
            legalcase.setOfficerIncharge(position);
            getCurrentSession().evict(position);
        }
        legalcase.setCaseNumber(
                legalcase.getCaseNumber() + (legalcase.getWpYear() != null ? "/" + legalcase.getWpYear() : ""));
        legalcase.setStatus(legalCaseUtil.getStatusForModuleAndCode(LcmsConstants.MODULE_TYPE_LEGALCASE,
                LcmsConstants.LEGALCASE_STATUS_CREATED));
        legalcase = prepareChildEntities(legalcase);
        updateNextDate(legalcase, legalcase.getPwrList());
        setLegalCaseReportStatus(legalcase, legalcase.getPwrList());
        final LegalCase savedlegalcase = legalCaseRepository.save(legalcase);
        final List<LegalCaseUploadDocuments> documentDetails = getLegalcaseUploadDocumentDetails(savedlegalcase, files);
        if (!documentDetails.isEmpty()) {
            savedlegalcase.setLegalCaseUploadDocuments(documentDetails);
            persistLegalcaseUploadDocuments(documentDetails);
        }
        return savedlegalcase;
    }

    @Transactional
    public LegalCase update(final LegalCase legalcase, final MultipartFile[] files) throws IOException {
        updateCounterAffidavitAndPwr(legalcase, legalcase.getPwrList());
        final LegalCase savedCaAndPwr = legalCaseRepository.save(legalcase);
        final List<PwrDocuments> documentDetails = getPwrDocumentDetails(savedCaAndPwr, files);
        if (!documentDetails.isEmpty()) {
            savedCaAndPwr.getPwrList().get(0).setPwrDocuments(documentDetails);
            persistPwrDocuments(documentDetails);
        }
        return savedCaAndPwr;

    }

    @Transactional
    public void updateCounterAffidavitAndPwr(final LegalCase legalcase, final List<Pwr> pwrList) {
        /*
         * final List<LegalCaseDepartment> legalcaseDetails = new ArrayList<LegalCaseDepartment>(0);
         */
        final List<Pwr> pwrListtemp = new ArrayList<Pwr>(0);
        final List<CounterAffidavit> caListtemp = new ArrayList<CounterAffidavit>(0);
        for (final Pwr legalpwr : pwrList) {
            legalpwr.setLegalCase(legalcase);
            pwrListtemp.add(legalpwr);
        }
        legalcase.getPwrList().clear();
        legalcase.setPwrList(pwrListtemp);
        for (final CounterAffidavit counterAffidavit : legalcase.getCounterAffidavits()) {
            counterAffidavit.setLegalCase(legalcase);
            caListtemp.add(counterAffidavit);
        }
        legalcase.getCounterAffidavits().clear();
        legalcase.setCounterAffidavits(caListtemp);
        /*
         * for (final LegalCaseDepartment legaldeptObj : legalDept) { String[] stremp = null;
         * legaldeptObj.setLegalCase(legalcase); if (legaldeptObj.getPosition().getName() != null &&
         * legaldeptObj.getPosition().getName().contains("@")) { stremp = legaldeptObj.getPosition().getName().split("@");
         * legaldeptObj.setPosition(legalCaseUtil.getPositionByName(stremp[0])); } else {
         * legaldeptObj.setPosition(legalCaseUtil.getPositionByName(legaldeptObj .getPosition().getName())); }
         * legaldeptObj.setDepartment(legalCaseUtil.getDepartmentByName( legaldeptObj.getDepartment().getName()));
         * legalcaseDetails.add(legaldeptObj); } legalcase.getLegalCaseDepartment().clear();
         * legalcase.setLegalCaseDepartment(legalcaseDetails);
         */

    }

    public List<LegalCaseUploadDocuments> getLegalCaseDocList(final LegalCase legalCase) {
        return legalCase.getLegalCaseUploadDocuments();
    }

    public List<PwrDocuments> getPwrDocList(final LegalCase legalCase) {
        return legalCase.getPwrList().get(0).getPwrDocuments();
    }

    public LegalCase prepareChildEntities(final LegalCase legalcase) {
        final List<Pwr> pwrListtemp = new ArrayList<Pwr>(0);
        int serialNumberPetitioner = 1;
        int serialNumberRespondent = 1;

        if (legalcase != null) {
            if (legalcase.getBipartisanPetitionerDetailsList() != null
                    || legalcase.getBipartisanRespondentDetailsList() != null) {
                legalcase.getBipartisanDetails().clear();
                legalCaseRepository.flush();
            }
            for (final BipartisanDetails petitioner : legalcase.getBipartisanPetitionerDetailsList()) {
                if (petitioner.getName() != null && !petitioner.getName().trim().isEmpty()) {
                    petitioner.setSerialNumber(petitioner.getSerialNumber() != null ? petitioner.getSerialNumber()
                            : serialNumberPetitioner);
                    petitioner.setIsRepondent(Boolean.FALSE);
                    if (petitioner.getIsRespondentGovernment() == null)
                        petitioner.setIsRespondentGovernment(Boolean.FALSE);
                    petitioner.setLegalCase(legalcase);
                    legalcase.getBipartisanDetails().add(petitioner);
                }
                serialNumberPetitioner++;
            }

            for (final BipartisanDetails respondent : legalcase.getBipartisanRespondentDetailsList()) {
                if (respondent.getName() != null && !respondent.getName().trim().isEmpty()) {

                    respondent.setSerialNumber(respondent.getSerialNumber() != null ? respondent.getSerialNumber()
                            : serialNumberRespondent);
                    respondent.setLegalCase(legalcase);
                    if (respondent.getIsRespondentGovernment() == null)
                        respondent.setIsRespondentGovernment(Boolean.FALSE);
                    respondent.setIsRepondent(Boolean.TRUE);
                    legalcase.getBipartisanDetails().add(respondent);
                }
                serialNumberRespondent++;
            }
        }
        if (!legalcase.getPwrList().isEmpty()) {
            for (final Pwr legalpwr : legalcase.getPwrList()) {
                legalpwr.setLegalCase(legalcase);
                pwrListtemp.add(legalpwr);
            }
            legalcase.getPwrList().clear();
            legalcase.setPwrList(pwrListtemp);
        }

        return legalcase;
    }

    @Transactional
    public LegalCase saveStandingCouncilEntity(final LegalCaseAdvocate legalCaseAdvocate) {
        LegalCaseAdvocate legalCaseAdvocatetemp = null;
        AdvocateMaster seniorLegalMaster = null;
        final AdvocateMaster advocateName = advocateMasterService
                .findByName(legalCaseAdvocate.getAdvocateMaster().getName());
        if (legalCaseAdvocate.getSeniorAdvocate().getName() != null)
            seniorLegalMaster = advocateMasterService.findByName(legalCaseAdvocate.getSeniorAdvocate().getName());
        if (!legalCaseAdvocate.getLegalCase().getLegalCaseAdvocates().isEmpty()) {
            legalCaseAdvocatetemp = legalCaseAdvocate.getLegalCase().getLegalCaseAdvocates().get(0);
            legalCaseAdvocatetemp.setAdvocateMaster(advocateName);
            legalCaseAdvocatetemp.setAssignedToDate(legalCaseAdvocate.getAssignedToDate());
            legalCaseAdvocatetemp.setVakalatDate(legalCaseAdvocate.getVakalatDate());
            legalCaseAdvocatetemp.getLegalCase().setIsSenioradvrequired(legalCaseAdvocate.getIsSeniorAdvocate());
            legalCaseAdvocatetemp.setIsActive(Boolean.TRUE);
            legalCaseAdvocatetemp.setChangeAdvocate(legalCaseAdvocate.getChangeAdvocate());
            legalCaseAdvocatetemp.setChangeSeniorAdvocate(legalCaseAdvocate.getChangeSeniorAdvocate());
            legalCaseAdvocatetemp.setSeniorAdvocate(seniorLegalMaster);
            legalCaseAdvocatetemp.setAssignedToDateForSenior(legalCaseAdvocate.getAssignedToDateForSenior());
            legalCaseAdvocatetemp.setOrderDate(legalCaseAdvocate.getOrderDate());
            legalCaseAdvocatetemp.setOrderNumber(legalCaseAdvocate.getOrderNumber());
            legalCaseAdvocatetemp.setOrderDateJunior(legalCaseAdvocate.getOrderDateJunior());
            legalCaseAdvocatetemp.setOrderNumberJunior(legalCaseAdvocate.getOrderNumberJunior());
            legalCaseAdvocate.getLegalCase().getLegalCaseAdvocates().add(legalCaseAdvocatetemp);

        } else {
            legalCaseAdvocate.setAdvocateMaster(advocateName);
            legalCaseAdvocate.getLegalCase().setIsSenioradvrequired(legalCaseAdvocate.getIsSeniorAdvocate());
            legalCaseAdvocate.setSeniorAdvocate(seniorLegalMaster);
            legalCaseAdvocate.setIsActive(Boolean.TRUE);
            legalCaseAdvocate.getLegalCase().getLegalCaseAdvocates().add(legalCaseAdvocate);
        }
        return legalCaseRepository.save(legalCaseAdvocate.getLegalCase());

    }

    @Transactional
    public LegalCase save(final LegalCase legalcase) {
        return legalCaseRepository.save(legalcase);
    }

    public void updateNextDate(final LegalCase legalCase, final List<Pwr> pwr) {

        if (pwr.get(0).getCaFilingDate() != null)
            legalCase.setNextDate(pwr.get(0).getCaFilingDate());
        else if (pwr.get(0).getCaDueDate() != null)
            legalCase.setNextDate(pwr.get(0).getCaDueDate());
        else if (pwr.get(0).getPwrDueDate() != null)
            legalCase.setNextDate(pwr.get(0).getPwrDueDate());
        else
            legalCase.setNextDate(legalCase.getCaseDate());

    }

    public List<PwrDocuments> getPwrDocumentDetails(final LegalCase legalCase, final MultipartFile[] files)
            throws IOException {
        final List<PwrDocuments> documentDetailsList = new ArrayList<PwrDocuments>();

        if (files != null)
            for (int i = 0; i < files.length; i++)
                if (!files[i].isEmpty()) {
                    final PwrDocuments applicationDocument = new PwrDocuments();
                    applicationDocument.setPwr(legalCase.getPwrList().get(0));
                    applicationDocument.setDocumentName(LcmsConstants.PWR_DOCUMENTNAME);
                    applicationDocument.setSupportDocs(
                            fileStoreService.store(files[i].getInputStream(), files[i].getOriginalFilename(),
                                    files[i].getContentType(), LcmsConstants.FILESTORE_MODULECODE));
                    documentDetailsList.add(applicationDocument);

                }
        return documentDetailsList;
    }

    public void persistPwrDocuments(final List<PwrDocuments> documentDetailsList) {
        if (documentDetailsList != null && !documentDetailsList.isEmpty())
            for (final PwrDocuments doc : documentDetailsList)
                pwrDocumentsRepository.save(doc);
    }

    public List<LegalCaseUploadDocuments> getLegalcaseUploadDocumentDetails(final LegalCase legalCase,
            final MultipartFile[] files) throws IOException {
        final List<LegalCaseUploadDocuments> documentDetailsList = new ArrayList<LegalCaseUploadDocuments>();

        if (files != null)
            for (int i = 0; i < files.length; i++)
                if (!files[i].isEmpty()) {
                    final LegalCaseUploadDocuments applicationDocument = new LegalCaseUploadDocuments();
                    applicationDocument.setLegalCase(legalCase);
                    applicationDocument.setDocumentName(LcmsConstants.LEGALCASE_DOCUMENTNAME);
                    applicationDocument.setSupportDocs(
                            fileStoreService.store(files[i].getInputStream(), files[i].getOriginalFilename(),
                                    files[i].getContentType(), LcmsConstants.FILESTORE_MODULECODE));
                    documentDetailsList.add(applicationDocument);

                }
        return documentDetailsList;
    }

    public void persistLegalcaseUploadDocuments(final List<LegalCaseUploadDocuments> documentDetailsList) {
        if (documentDetailsList != null && !documentDetailsList.isEmpty())
            for (final LegalCaseUploadDocuments doc : documentDetailsList)
                legalCaseUploadDocumentsRepository.save(doc);
    }

    public void setLegalCaseReportStatus(final LegalCase legalCase, final List<Pwr> pwr) {
        final String caseStatus = legalCase.getStatus().getCode();
        ReportStatus reportStatus = null;
        if (caseStatus.equalsIgnoreCase(LcmsConstants.LEGALCASE_STATUS_CREATED))
            if (reportStatus == null && pwr != null && !pwr.isEmpty())
                if (pwr.get(0).getCaFilingDate() != null)
                    reportStatus = getReportStatusByCode(LcmsConstants.CODE_REPORTSTATUS_COUNTERFILED);
                else if (pwr.get(0).getPwrApprovalDate() == null)
                    legalCase.setReportStatus(getReportStatusByCode(LcmsConstants.CODE_REPORTSTATUS_PWRPENDING));
                else
                    reportStatus = getReportStatusByCode(LcmsConstants.CODE_REPORTSTATUS_DCAPENDING);

        if (reportStatus != null)
            legalCase.setReportStatus(reportStatus);

    }

    public ReportStatus getReportStatusByCode(final String reportStatusCode) {
        final ReportStatus reportStatus = reportStatusRepository.findByCode(reportStatusCode);
        return reportStatus;
    }

/*    public Position getPostion(final Long positionId) {
        final Position pos = positionMasterRepository.findOne(positionId);
        return pos;
    }*/
}