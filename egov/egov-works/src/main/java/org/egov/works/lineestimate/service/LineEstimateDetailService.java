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
package org.egov.works.lineestimate.service;

import java.util.List;

import org.egov.commons.Accountdetailkey;
import org.egov.commons.Accountdetailtype;
import org.egov.commons.dao.AccountdetailkeyHibernateDAO;
import org.egov.commons.dao.AccountdetailtypeHibernateDAO;
import org.egov.works.abstractestimate.entity.ProjectCode;
import org.egov.works.abstractestimate.service.ProjectCodeService;
import org.egov.works.lineestimate.entity.LineEstimateDetails;
import org.egov.works.lineestimate.repository.LineEstimateDetailsRepository;
import org.egov.works.utils.WorksConstants;
import org.egov.works.utils.WorksUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional(readOnly = true)
public class LineEstimateDetailService {

    private final LineEstimateDetailsRepository lineEstimateDetailsRepository;

    @Autowired
    private AccountdetailtypeHibernateDAO accountdetailtypeHibernateDAO;

    @Autowired
    private AccountdetailkeyHibernateDAO accountdetailkeyHibernateDAO;

    @Autowired
    @Qualifier("projectCodeService")
    private ProjectCodeService projectCodeService;

    @Autowired
    private WorkIdentificationNumberGenerator workIdentificationNumberGenerator;

    @Autowired
    private WorksUtils worksUtils;

    @Autowired
    public LineEstimateDetailService(final LineEstimateDetailsRepository lineEstimateDetailsRepository) {
        this.lineEstimateDetailsRepository = lineEstimateDetailsRepository;
    }

    @Transactional
    public void save(final LineEstimateDetails lineEstimateDetails) {
        lineEstimateDetailsRepository.save(lineEstimateDetails);
    }

    @Transactional
    public void update(final LineEstimateDetails lineEstimateDetails) {
        lineEstimateDetailsRepository.saveAndFlush(lineEstimateDetails);
    }

    @Transactional
    public void delete(final LineEstimateDetails lineEstimateDetails) {
        lineEstimateDetailsRepository.delete(lineEstimateDetails);
    }

    @Transactional
    public void delete(final List<LineEstimateDetails> lineEstimateDetailsList) {
        lineEstimateDetailsRepository.delete(lineEstimateDetailsList);
    }

    public LineEstimateDetails getById(final Long id) {
        return lineEstimateDetailsRepository.findOne(id);
    }

    @Transactional(propagation = Propagation.REQUIRES_NEW)
    public void setProjectCode(final LineEstimateDetails lineEstimateDetails) {
        ProjectCode projectCode = null;
        if (lineEstimateDetails.getProjectCode() != null && lineEstimateDetails.getLineEstimate().isSpillOverFlag()) {
            projectCode = lineEstimateDetails.getProjectCode();
            projectCode.setCode(lineEstimateDetails.getProjectCode().getCode());
        } else {
            projectCode = new ProjectCode();
            projectCode
                    .setCode(workIdentificationNumberGenerator.generateWorkOrderIdentificationNumber(lineEstimateDetails));
            lineEstimateDetails.setProjectCode(projectCode);
        }
        projectCode.setCodeName(lineEstimateDetails.getNameOfWork());
        projectCode.setDescription(lineEstimateDetails.getNameOfWork());
        projectCode.setActive(true);
        projectCode.setEgwStatus(worksUtils.getStatusByModuleAndCode(
                ProjectCode.class.getSimpleName(), WorksConstants.DEFAULT_PROJECTCODE_STATUS));
        projectCodeService.applyAuditing(projectCode);
        projectCodeService.persist(projectCode);
        createAccountDetailKey(projectCode);
    }

    protected void createAccountDetailKey(final ProjectCode proj) {
        final Accountdetailtype accountdetailtype = accountdetailtypeHibernateDAO.getAccountdetailtypeByName("PROJECTCODE");
        final Accountdetailkey adk = new Accountdetailkey();
        adk.setGroupid(1);
        adk.setDetailkey(proj.getId().intValue());
        adk.setDetailname(accountdetailtype.getAttributename());
        adk.setAccountdetailtype(accountdetailtype);
        accountdetailkeyHibernateDAO.create(adk);

    }

    public LineEstimateDetails findLineEstimateByEstimateNumber(final String estimatenumber, final String status) {
        return lineEstimateDetailsRepository.findByEstimateNumberAndLineEstimate_Status_CodeEquals(estimatenumber, status);
    }

    public LineEstimateDetails getLineEstimateDetailsByProjectCode(final String workIdentificationNumber) {
        return lineEstimateDetailsRepository.findByProjectCode_codeAndLineEstimate_Status_CodeNotLike(workIdentificationNumber,
                WorksConstants.CANCELLED_STATUS);
    }

    public LineEstimateDetails getLineEstimateDetailsByEstimateNumber(final String estimateNumber) {
        return lineEstimateDetailsRepository.findByEstimateNumberAndLineEstimate_Status_CodeNot(estimateNumber,
                WorksConstants.CANCELLED_STATUS);
    }

}
