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
import java.util.Arrays;
import java.util.Date;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import org.apache.commons.lang3.ArrayUtils;
import org.egov.commons.Functionary;
import org.egov.commons.dao.FunctionaryHibernateDAO;
import org.egov.eis.service.PositionMasterService;
import org.egov.infra.admin.master.service.DepartmentService;
import org.egov.infra.exception.ApplicationRuntimeException;
import org.egov.infra.filestore.entity.FileStoreMapper;
import org.egov.infra.filestore.service.FileStoreService;
import org.egov.lcms.masters.service.AdvocateMasterService;
import org.egov.lcms.transactions.entity.BipartisanDetails;
import org.egov.lcms.transactions.entity.Legalcase;
import org.egov.lcms.transactions.entity.LegalcaseAdvocate;
import org.egov.lcms.transactions.entity.LegalcaseDepartment;
import org.egov.lcms.transactions.entity.LegalcaseDocuments;
import org.egov.lcms.transactions.entity.Pwr;
import org.egov.lcms.transactions.repository.LegalcaseRepository;
import org.egov.lcms.utils.LegalCaseUtil;
import org.egov.lcms.utils.constants.LcmsConstants;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

@Service
@Transactional(readOnly = true)
public class LegalCaseService {

    private final LegalcaseRepository legalCaseRepository;

    @Autowired
    @Qualifier("fileStoreService")
    protected FileStoreService fileStoreService;

    @Autowired
    private DepartmentService departmentService;

    @Autowired
    private PositionMasterService positionMasterService;

    @Autowired
    private FunctionaryHibernateDAO functionaryDAO;

    @Autowired
    private AdvocateMasterService advocateMasterService;

    @Autowired
    private LegalCaseUtil legalCaseUtil;

    @Autowired
    public LegalCaseService(final LegalcaseRepository legalCaseRepository) {
        this.legalCaseRepository = legalCaseRepository;

    }

    public Legalcase findBy(final Long Id) {
        return legalCaseRepository.findOne(Id);
    }

    public Legalcase getLegalCaseByLcNumber(final String lcnumber) {
        return legalCaseRepository.findByLcNumber(lcnumber);
    }

    public Legalcase getLegalCaseByCaseNumber(final String caseNumber) {
        return legalCaseRepository.findByCasenumber(caseNumber);
    }

    @Transactional
    public Legalcase createLegalCase(final Legalcase legalcase) {
        legalcase.setCasenumber(legalcase.getCasenumber() + (legalcase.getWpYear()!=null ?"/" + legalcase.getWpYear() :""));
        final String[] funcString = legalcase.getFunctionaryCode().split("LC");
        final Functionary funcObj = legalCaseUtil.getFunctionaryByCode(funcString);
        legalcase.setFunctionary(funcObj);
        legalcase.setStatus(legalCaseUtil.getStatusForModuleAndCode(LcmsConstants.MODULE_TYPE_LEGALCASE,(LcmsConstants.LEGALCASE_STATUS_CREATED)));
        prepareChildEntities(legalcase);
        processAndStoreApplicationDocuments(legalcase);
        return legalCaseRepository.save(legalcase);
    }

    private void prepareChildEntities(final Legalcase legalcase) {
        final List<BipartisanDetails> partitionDetails = new ArrayList<BipartisanDetails>();
        final List<LegalcaseDepartment> legalcaseDetails = new ArrayList<LegalcaseDepartment>();
        final List<LegalcaseAdvocate> legalAdvocateDetails = new ArrayList<LegalcaseAdvocate>();
        final List<Pwr> pwrList = new ArrayList<Pwr>();

        for (final BipartisanDetails bipartObj : legalcase.getBipartisanDetails()) {
            bipartObj.setSerialNumber(bipartObj.getSerialNumber() != null ? bipartObj.getSerialNumber() : 111l);
            bipartObj.setLegalcase(legalcase);
            partitionDetails.add(bipartObj);
        }
        legalcase.getBipartisanDetails().clear();
        legalcase.setBipartisanDetails(partitionDetails);

        for (final BipartisanDetails bipartObjtemp : legalcase.getBipartisanDetailsBeanList()) {
            bipartObjtemp.setSerialNumber(bipartObjtemp.getSerialNumber() != null ? bipartObjtemp.getSerialNumber() : 111l);
            bipartObjtemp.setLegalcase(legalcase);
            legalcase.getBipartisanDetails().add(bipartObjtemp);
        }

        for (final LegalcaseDepartment legaldeptObj : legalcase.getLegalcaseDepartment()) {

            legaldeptObj.setLegalcase(legalcase);
            legaldeptObj.setDepartment(departmentService.getDepartmentByName(legaldeptObj.getDepartment().getName()));
            legaldeptObj.setPosition(positionMasterService.getPositionByName(legaldeptObj.getPosition().getName()));
            legalcaseDetails.add(legaldeptObj);
        }
        legalcase.getLegalcaseDepartment().clear();
        legalcase.setLegalcaseDepartment(legalcaseDetails);

        for (final LegalcaseAdvocate legalAdvocateObj : legalcase.getEglcLegalcaseAdvocates()) {
            legalAdvocateObj.setLegalcase(legalcase);
            legalAdvocateObj.setAdvocateMaster(
                    advocateMasterService.findByName(legalAdvocateObj.getAdvocateMaster().getName()));
            legalAdvocateObj.setEglcSeniorAdvocateMaster(
                    advocateMasterService.findByName(legalAdvocateObj.getEglcSeniorAdvocateMaster().getName()));
            legalAdvocateObj.setIsActive(Boolean.TRUE);
            legalAdvocateDetails.add(legalAdvocateObj);
        }
        legalcase.getEglcLegalcaseAdvocates().clear();
        legalcase.setEglcLegalcaseAdvocates(legalAdvocateDetails);

        for (final Pwr legalpwr : legalcase.getEglcPwrs()) {
            legalpwr.setLegalcase(legalcase);
            legalpwr.setCaFilingdate(new Date());
            pwrList.add(legalpwr);
        }
        legalcase.getEglcPwrs().clear();
        legalcase.setEglcPwrs(pwrList);

    }

    protected void processAndStoreApplicationDocuments(final Legalcase legalcase) {
        if (!legalcase.getLegalcaseDocuments().isEmpty())
            for (final LegalcaseDocuments applicationDocument : legalcase.getLegalcaseDocuments()) {
                applicationDocument.setLegalcase(legalcase);
                applicationDocument.setDocumentName("LegalCase");
                applicationDocument.setSupportDocs(addToFileStore(applicationDocument.getFiles()));
            }
    }

    @Transactional
    public Legalcase updateLegalCase(final Legalcase legalCase) {
        return legalCaseRepository.save(legalCase);
    }

    protected Set<FileStoreMapper> addToFileStore(final MultipartFile[] files) {
        if (ArrayUtils.isNotEmpty(files))
            return Arrays
                    .asList(files)
                    .stream()
                    .filter(file -> !file.isEmpty())
                    .map(file -> {
                        try {
                            return fileStoreService.store(file.getInputStream(), file.getOriginalFilename(),
                                    file.getContentType(), LcmsConstants.FILESTORE_MODULECODE);
                        } catch (final Exception e) {
                            throw new ApplicationRuntimeException("Error occurred while getting inputstream", e);
                        }
                    }).collect(Collectors.toSet());
        else
            return null;
    }
}
