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
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import org.apache.commons.lang3.ArrayUtils;
import org.egov.eis.service.PositionMasterService;
import org.egov.infra.admin.master.service.DepartmentService;
import org.egov.infra.exception.ApplicationRuntimeException;
import org.egov.infra.filestore.entity.FileStoreMapper;
import org.egov.infra.filestore.service.FileStoreService;
import org.egov.lcms.masters.entity.AdvocateMaster;
import org.egov.lcms.masters.service.AdvocateMasterService;
import org.egov.lcms.transactions.entity.BipartisanDetails;
import org.egov.lcms.transactions.entity.LegalCase;
import org.egov.lcms.transactions.entity.LegalCaseAdvocate;
import org.egov.lcms.transactions.entity.LegalCaseDepartment;
import org.egov.lcms.transactions.entity.LegalCaseDocuments;
import org.egov.lcms.transactions.entity.Pwr;
import org.egov.lcms.transactions.entity.PwrDocuments;
import org.egov.lcms.transactions.repository.BipartisanDetailsRepository;
import org.egov.lcms.transactions.repository.LegalCaseRepository;
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

	private final LegalCaseRepository legalCaseRepository;

	@Autowired
	@Qualifier("fileStoreService")
	protected FileStoreService fileStoreService;

	@Autowired
	private DepartmentService departmentService;

	@Autowired
	private PositionMasterService positionMasterService;

	@Autowired
	private AdvocateMasterService advocateMasterService;

	@Autowired
	private LegalCaseUtil legalCaseUtil;

	@Autowired
	private BipartisanDetailsRepository bipartisanDetailsRepository;

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
	public LegalCase persist(final LegalCase legalcase) {
		legalcase.setCaseNumber(
				legalcase.getCaseNumber() + (legalcase.getWpYear() != null ? "/" + legalcase.getWpYear() : ""));
		legalcase.setStatus(legalCaseUtil.getStatusForModuleAndCode(LcmsConstants.MODULE_TYPE_LEGALCASE,
				LcmsConstants.LEGALCASE_STATUS_CREATED));
		final List<LegalCaseDocuments> legalDoc = legalCaseRepository.getLegalCaseDocumentList(legalcase.getId());
		prepareChildEntities(legalcase);
		processAndStoreApplicationDocuments(legalcase, legalDoc);
		return legalCaseRepository.save(legalcase);
	}

	@Transactional
	public LegalCase update(final LegalCase legalcase) {
		updateLegalCaseDeptAndPwr(legalcase, legalcase.getEglcPwrs(), legalcase.getLegalCaseDepartment());
		List<PwrDocuments> legalDoc = null;
		if (!legalcase.getEglcPwrs().isEmpty()) {
			legalDoc = legalCaseRepository.getPwrDocumentList(legalcase.getEglcPwrs().get(0).getId());
		}
		processAndStorePwrDocuments(legalcase, legalDoc);
		return legalCaseRepository.save(legalcase);
	}

	@Transactional
	public void updateLegalCaseDeptAndPwr(final LegalCase legalcase, List<Pwr> pwrList,
			List<LegalCaseDepartment> legalDept) {
		final List<LegalCaseDepartment> legalcaseDetails = new ArrayList<LegalCaseDepartment>();
		final List<Pwr> pwrListtemp = new ArrayList<Pwr>();
		for (final Pwr legalpwr : pwrList) {
			legalpwr.setLegalCase(legalcase);
			legalpwr.setCaFilingdate(new Date());
			pwrListtemp.add(legalpwr);
		}
		legalcase.getEglcPwrs().clear();
		legalcase.setEglcPwrs(pwrListtemp);
		for (final LegalCaseDepartment legaldeptObj : legalDept) {
			legaldeptObj.setLegalCase(legalcase);
			legaldeptObj.setPosition(positionMasterService.getPositionByName(legaldeptObj.getPosition().getName()));
			legaldeptObj.setDepartment(departmentService.getDepartmentByName(legaldeptObj.getDepartment().getName()));
			legalcaseDetails.add(legaldeptObj);
		}
		legalcase.getLegalCaseDepartment().clear();
		legalcase.setLegalCaseDepartment(legalcaseDetails);

	}

	public List<LegalCaseDocuments> getLegalCaseDocList(final LegalCase legalCase) {
		List<LegalCaseDocuments> legalDOc = null;
		final Set<LegalCaseDocuments> legalDOcSet = new HashSet<LegalCaseDocuments>();
		for (final LegalCaseDocuments legalDoc : legalCase.getLegalCaseDocuments())
			legalDOcSet.add(legalDoc);
		legalDOc = new ArrayList<LegalCaseDocuments>(legalDOcSet);
		return legalDOc;
	}

	@Transactional
	public void prepareChildEntities(final LegalCase legalcase) {
		final List<BipartisanDetails> partitionDetails = new ArrayList<BipartisanDetails>();
		List<BipartisanDetails> biparttionerList = legalCaseRepository.getBipartitionDetList(legalcase.getId());
		legalcase.getBipartisanDetails().addAll(biparttionerList);
		final List<Pwr> pwrListtemp = new ArrayList<Pwr>();
		legalcase.getBipartisanDetails().clear();
		// legalCaseRepository.save(legalcase);
		// bipartisanDetailsRepository.delete(legalcase.getBipartisanDetails());
		for (final BipartisanDetails bipartObjtemp : legalcase.getBipartisanDetails()) {
			bipartisanDetailsRepository.delete(bipartObjtemp);
		}
		if (legalcase != null && legalcase.getId() != null) {
			for (final BipartisanDetails bipartObj : legalcase.getBipartisanPetitionDetailsList())
				if (bipartObj.getName() != null && !"".equals(bipartObj.getName())) {
					bipartObj.setSerialNumber(bipartObj.getSerialNumber() != null ? bipartObj.getSerialNumber() : 111l);
					bipartObj.setIsRepondent(Boolean.FALSE);
					if (bipartObj.getIsRespondentGovernment() == null) {
						bipartObj.setIsRespondentGovernment(Boolean.FALSE);
					}
					bipartObj.setLegalCase(legalcase);
					partitionDetails.add(bipartObj);
				}
		} else {
			for (final BipartisanDetails bipartObj : legalcase.getBipartisanPetitionDetailsList())
				if (bipartObj.getName() != null && bipartObj.getIsRepondent().equals(Boolean.FALSE)
						&& !"".equals(bipartObj.getName())) {
					bipartObj.setSerialNumber(bipartObj.getSerialNumber() != null ? bipartObj.getSerialNumber() : 111l);
					bipartObj.setIsRepondent(Boolean.FALSE);
					if (bipartObj.getIsRespondentGovernment() == null) {
						bipartObj.setIsRespondentGovernment(Boolean.FALSE);
					}
					bipartObj.setLegalCase(legalcase);
					partitionDetails.add(bipartObj);
				}
		}
		for (final BipartisanDetails bipartObjtemp : legalcase.getBipartisanDetailsBeanList())
			if ((bipartObjtemp.getId() == null || bipartObjtemp.getId() != null)
					&& (bipartObjtemp.getName() != null && !"".equals(bipartObjtemp.getName()))) {
				bipartObjtemp.setSerialNumber(
						bipartObjtemp.getSerialNumber() != null ? bipartObjtemp.getSerialNumber() : 111l);
				bipartObjtemp.setLegalCase(legalcase);
				if (bipartObjtemp.getIsRespondentGovernment() == null) {
					bipartObjtemp.setIsRespondentGovernment(Boolean.FALSE);
				}
				bipartObjtemp.setIsRepondent(Boolean.TRUE);
				partitionDetails.add(bipartObjtemp);

			}
		legalcase.getBipartisanDetails().clear();
		legalcase.getBipartisanDetails().addAll(partitionDetails);
		if(!legalcase.getEglcPwrs().isEmpty() ){
		for (final Pwr legalpwr : legalcase.getEglcPwrs()) {
			legalpwr.setLegalCase(legalcase);
			legalpwr.setCaFilingdate(new Date());
			pwrListtemp.add(legalpwr);
		}
		legalcase.getEglcPwrs().clear();
		legalcase.setEglcPwrs(pwrListtemp);
		}
	}

	@Transactional
	public LegalCase saveStandingCouncilEntity(final LegalCaseAdvocate legalCaseAdvocate) {
		LegalCaseAdvocate legalCaseAdvocatetemp = null;
		AdvocateMaster seniorLegalMaster = null;
		final AdvocateMaster advocateName = advocateMasterService
				.findByName(legalCaseAdvocate.getAdvocateMaster().getName());
		if (legalCaseAdvocate.getEglcSeniorAdvocateMaster().getName() != null)
			seniorLegalMaster = advocateMasterService
					.findByName(legalCaseAdvocate.getEglcSeniorAdvocateMaster().getName());
		if (!legalCaseAdvocate.getLegalCase().getEglcLegalcaseAdvocates().isEmpty()) {
			legalCaseAdvocatetemp = legalCaseAdvocate.getLegalCase().getEglcLegalcaseAdvocates().get(0);
			legalCaseAdvocatetemp.setAdvocateMaster(advocateName);
			legalCaseAdvocatetemp.setAssignedtodate(legalCaseAdvocate.getAssignedtodate());
			legalCaseAdvocatetemp.setVakalatdate(legalCaseAdvocate.getVakalatdate());
			legalCaseAdvocatetemp.getLegalCase().setIsSenioradvrequired(legalCaseAdvocate.getIsSeniorAdvocate());
			legalCaseAdvocatetemp.setIsActive(Boolean.TRUE);
			legalCaseAdvocatetemp.setChangeAdvocate(legalCaseAdvocate.getChangeAdvocate());
			legalCaseAdvocatetemp.setChangeSeniorAdvocate(legalCaseAdvocate.getChangeSeniorAdvocate());
			legalCaseAdvocatetemp.setEglcSeniorAdvocateMaster(seniorLegalMaster);
			legalCaseAdvocatetemp.setAssignedtodateForsenior(legalCaseAdvocate.getAssignedtodateForsenior());
			legalCaseAdvocatetemp.setOrderdate(legalCaseAdvocate.getOrderdate());
			legalCaseAdvocatetemp.setOrdernumber(legalCaseAdvocate.getOrdernumber());
			legalCaseAdvocatetemp.setOrderdateJunior(legalCaseAdvocate.getOrderdateJunior());
			legalCaseAdvocatetemp.setOrdernumberJunior(legalCaseAdvocate.getOrdernumberJunior());
			legalCaseAdvocate.getLegalCase().getEglcLegalcaseAdvocates().add(legalCaseAdvocatetemp);

		} else {
			legalCaseAdvocate.setAdvocateMaster(advocateName);
			legalCaseAdvocate.getLegalCase().setIsSenioradvrequired(legalCaseAdvocate.getIsSeniorAdvocate());
			legalCaseAdvocate.setEglcSeniorAdvocateMaster(seniorLegalMaster);
			legalCaseAdvocate.setIsActive(Boolean.TRUE);
			legalCaseAdvocate.getLegalCase().getEglcLegalcaseAdvocates().add(legalCaseAdvocate);
		}
		return legalCaseRepository.save(legalCaseAdvocate.getLegalCase());

	}

	@Transactional
	public void processAndStoreApplicationDocuments(final LegalCase legalcase,
			final List<LegalCaseDocuments> legalDoc) {
		if (legalcase.getId() == null) {
			if (!legalcase.getLegalCaseDocuments().isEmpty())
				for (final LegalCaseDocuments applicationDocument : legalcase.getLegalCaseDocuments()) {
					applicationDocument.setLegalCase(legalcase);
					applicationDocument.setDocumentName("LegalCase");
					applicationDocument.setSupportDocs(addToFileStore(applicationDocument.getFiles()));
				}
		} else {
			for (final LegalCaseDocuments applicationDocument : legalcase.getLegalCaseDocuments()) {
				applicationDocument.setLegalCase(legalcase);
				applicationDocument.setDocumentName("LegalCase");
				applicationDocument.getSupportDocs().addAll(addToFileStore(applicationDocument.getFiles()));
				legalcase.getLegalCaseDocuments().clear();
				legalcase.getLegalCaseDocuments().add(applicationDocument);
			}
			legalcase.getLegalCaseDocuments().addAll(legalDoc);

		}
	}

	@Transactional
	public void processAndStorePwrDocuments(final LegalCase legalcase, final List<PwrDocuments> pwrDoc) {
		if (legalcase.getId() == null) {
			if (!legalcase.getEglcPwrs().get(0).getPwrDocuments().isEmpty())
				for (final PwrDocuments pwr : legalcase.getEglcPwrs().get(0).getPwrDocuments()) {
					pwr.setPwr(legalcase.getEglcPwrs().get(0));
					pwr.setDocumentName("Pwr");
					pwr.setSupportDocs(addToFileStore(pwr.getFiles()));
				}
		} else {
			for (final PwrDocuments pwr : legalcase.getEglcPwrs().get(0).getPwrDocuments()) {
				pwr.setPwr(legalcase.getEglcPwrs().get(0));
				pwr.setDocumentName("Pwr");
				pwr.getSupportDocs().addAll(addToFileStore(pwr.getFiles()));
				legalcase.getEglcPwrs().get(0).getPwrDocuments().clear();
				legalcase.getEglcPwrs().get(0).getPwrDocuments().add(pwr);
			}
			legalcase.getEglcPwrs().get(0).getPwrDocuments().addAll(pwrDoc);

		}
	}

	protected Set<FileStoreMapper> addToFileStore(final MultipartFile[] files) {
		if (ArrayUtils.isNotEmpty(files))
			return Arrays.asList(files).stream().filter(file -> !file.isEmpty()).map(file -> {
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
