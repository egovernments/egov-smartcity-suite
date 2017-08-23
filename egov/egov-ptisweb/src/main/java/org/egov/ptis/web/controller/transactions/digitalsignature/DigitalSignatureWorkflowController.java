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
package org.egov.ptis.web.controller.transactions.digitalsignature;

import static org.egov.ptis.constants.PropertyTaxConstants.ANONYMOUS_USER;
import static org.egov.ptis.constants.PropertyTaxConstants.APPLICATION_TYPE_ALTER_ASSESSENT;
import static org.egov.ptis.constants.PropertyTaxConstants.APPLICATION_TYPE_AMALGAMATION;
import static org.egov.ptis.constants.PropertyTaxConstants.APPLICATION_TYPE_BIFURCATE_ASSESSENT;
import static org.egov.ptis.constants.PropertyTaxConstants.APPLICATION_TYPE_DEMOLITION;
import static org.egov.ptis.constants.PropertyTaxConstants.APPLICATION_TYPE_GRP;
import static org.egov.ptis.constants.PropertyTaxConstants.APPLICATION_TYPE_NEW_ASSESSENT;
import static org.egov.ptis.constants.PropertyTaxConstants.APPLICATION_TYPE_TAX_EXEMTION;
import static org.egov.ptis.constants.PropertyTaxConstants.APPLICATION_TYPE_TRANSFER_OF_OWNERSHIP;
import static org.egov.ptis.constants.PropertyTaxConstants.APPLICATION_TYPE_VACANCY_REMISSION;
import static org.egov.ptis.constants.PropertyTaxConstants.APPLICATION_TYPE_VACANCY_REMISSION_APPROVAL;
import static org.egov.ptis.constants.PropertyTaxConstants.STATUS_ISACTIVE;
import static org.egov.ptis.constants.PropertyTaxConstants.STATUS_ISHISTORY;
import static org.egov.ptis.constants.PropertyTaxConstants.WFLOW_ACTION_NAME_EXEMPTION;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.OutputStream;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.io.FileUtils;
import org.egov.commons.entity.Source;
import org.egov.eis.entity.Assignment;
import org.egov.eis.service.AssignmentService;
import org.egov.infra.exception.ApplicationRuntimeException;
import org.egov.infra.filestore.entity.FileStoreMapper;
import org.egov.infra.filestore.repository.FileStoreMapperRepository;
import org.egov.infra.filestore.service.FileStoreService;
import org.egov.infra.security.utils.SecurityUtils;
import org.egov.infra.workflow.entity.StateAware;
import org.egov.infra.workflow.service.SimpleWorkflowService;
import org.egov.pims.commons.Position;
import org.egov.ptis.constants.PropertyTaxConstants;
import org.egov.ptis.domain.dao.property.PropertyStatusDAO;
import org.egov.ptis.domain.entity.objection.RevisionPetition;
import org.egov.ptis.domain.entity.property.BasicProperty;
import org.egov.ptis.domain.entity.property.PropertyImpl;
import org.egov.ptis.domain.entity.property.PropertyMutation;
import org.egov.ptis.domain.entity.property.VacancyRemission;
import org.egov.ptis.domain.entity.property.VacancyRemissionApproval;
import org.egov.ptis.domain.repository.vacancyremission.VacancyRemissionApprovalRepository;
import org.egov.ptis.domain.service.property.PropertyPersistenceService;
import org.egov.ptis.domain.service.property.PropertyService;
import org.egov.ptis.domain.service.revisionPetition.RevisionPetitionService;
import org.egov.ptis.service.utils.PropertyTaxCommonUtils;
import org.hibernate.Session;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;

/**
 * @author subhash
 */
@Controller
@RequestMapping(value = "/digitalSignature")
public class DigitalSignatureWorkflowController {

    private static final String SUCCESS_MESSAGE = "successMessage";

    private static final String FALSE = "false";

    private static final String APPLICATION_NO = "applicationNo";

    private static final String DIGISIGN_SUCCESS_MESSAGE = "Digitally Signed Successfully";

    private static final String NOTICE_SUCCESS_MESSAGE = "Notice Generated Successfully";

    private static final String STR_DEMOLITION = "Demolition";

    private static final String BIFURCATE = "Bifurcate";

    private static final String ALTER = "Alter";

    private static final String CREATE = "Create";

    private static final String GRP = "GRP";

    private static final String DIGITAL_SIGNATURE_SUCCESS = "digitalSignature-success";

    private static final String AMALG = "Amalgamation";

    private static final String INITIATOR_INACTIVE_MESSAGE = "Digital signature can not be completed as initiator assignment is inactive for the applicatoin number: ";

    @PersistenceContext
    private EntityManager entityManager;

    @Autowired
    private SecurityUtils securityUtils;

    @Autowired
    private AssignmentService assignmentService;

    @Autowired
    private PropertyService propertyService;

    @Autowired
    @Qualifier("workflowService")
    private SimpleWorkflowService<PropertyImpl> propertyWorkflowService;

    @Autowired
    private PropertyPersistenceService basicPropertyService;

    @Autowired
    private RevisionPetitionService revisionPetitionService;

    @Autowired
    @Qualifier("workflowService")
    private SimpleWorkflowService<PropertyMutation> transferWorkflowService;

    @Autowired
    @Qualifier("workflowService")
    protected SimpleWorkflowService<RevisionPetition> revisionPetitionWorkFlowService;

    @Autowired
    private PropertyStatusDAO propertyStatusDAO;

    @Autowired
    @Qualifier("fileStoreService")
    protected FileStoreService fileStoreService;

    @Autowired
    private FileStoreMapperRepository fileStoreMapperRepository;

    @Autowired
    private VacancyRemissionApprovalRepository vacancyRemissionApprovalRepository;
    
    @Autowired
    private PropertyTaxCommonUtils propertyTaxCommonUtils;

    @RequestMapping(value = "/propertyTax/transitionWorkflow")
    public String transitionWorkflow(final HttpServletRequest request, final Model model) {
        final String fileStoreIds = request.getParameter("fileStoreId");
        final String isDigiEnabled = request.getParameter("isDigiEnabled");
        final String[] fileStoreId = fileStoreIds.split(",");
        String applicationNumber = null;
        for (final String id : fileStoreId) {
            applicationNumber = (String) getCurrentSession()
                    .createQuery(
                            "select notice.applicationNumber from PtNotice notice where notice.fileStore.fileStoreId = :id")
                    .setParameter("id", id).uniqueResult();
            final PropertyImpl property = getPropertyByApplicationNo(applicationNumber);
            if (property != null)
                updateProperty(property);
            else
                updateOthers(applicationNumber);
        }
        if (isDigiEnabled != null && isDigiEnabled.equals(FALSE))
            model.addAttribute(SUCCESS_MESSAGE, NOTICE_SUCCESS_MESSAGE);
        else
            model.addAttribute(SUCCESS_MESSAGE, DIGISIGN_SUCCESS_MESSAGE);
        model.addAttribute("fileStoreId", fileStoreId.length == 1 ? fileStoreId[0] : "");
        return DIGITAL_SIGNATURE_SUCCESS;
    }

    private void updateProperty(final PropertyImpl property) {
        final BasicProperty basicProperty = property.getBasicProperty();
        final String applicationType = transition(property);
        propertyService.updateIndexes(property, getApplicationTypes().get(applicationType));
        basicPropertyService.update(basicProperty);
        propertyTaxCommonUtils.buildMailAndSMS(property);
    }

    private void updateOthers(final String applicationNumber) {
        final RevisionPetition revisionPetition = getRevisionPetitionByApplicationNo(applicationNumber);
        if (revisionPetition != null)
             updateRevisionPetition(revisionPetition);
        else {
            final PropertyMutation propertyMutation = getPropertyMutationByApplicationNo(applicationNumber);
            if (propertyMutation != null)
                 updatePropertyMutation(propertyMutation);
            else
                 updateVacanyRemission(applicationNumber);
        }
    }

    private void updateRevisionPetition(final RevisionPetition revisionPetition) {
        transition(revisionPetition);
        propertyService.updateIndexes(revisionPetition, "RP".equalsIgnoreCase(revisionPetition.getType())
                ? PropertyTaxConstants.APPLICATION_TYPE_REVISION_PETITION : APPLICATION_TYPE_GRP);
        revisionPetitionService.updateRevisionPetition(revisionPetition);
        if (Source.CITIZENPORTAL.toString().equalsIgnoreCase(revisionPetition.getSource())) {
            propertyService.updatePortal(revisionPetition, "RP".equalsIgnoreCase(revisionPetition.getType())
                        ? PropertyTaxConstants.APPLICATION_TYPE_REVISION_PETITION : APPLICATION_TYPE_GRP);
        }
        propertyTaxCommonUtils.buildMailAndSMS(revisionPetition);
    }

    private void updatePropertyMutation(final PropertyMutation propertyMutation) {
        final BasicProperty basicProperty = propertyMutation.getBasicProperty();
        transition(propertyMutation);
        propertyService.updateIndexes(propertyMutation, APPLICATION_TYPE_TRANSFER_OF_OWNERSHIP);
        if (Source.CITIZENPORTAL.toString().equalsIgnoreCase(propertyMutation.getSource()))
            propertyService.updatePortal(propertyMutation, APPLICATION_TYPE_TRANSFER_OF_OWNERSHIP);
        basicPropertyService.persist(basicProperty);
        propertyTaxCommonUtils.buildMailAndSMS(propertyMutation);
    }

    private void updateVacanyRemission(final String applicationNumber) {
        final VacancyRemission vacancyRemission = getVacancyRemissionByApplicationNo(applicationNumber);
        if (vacancyRemission != null) {
            final BasicProperty basicProperty = vacancyRemission.getBasicProperty();
            transition(vacancyRemission);
            propertyService.updateIndexes(vacancyRemission.getVacancyRemissionApproval().get(0), APPLICATION_TYPE_VACANCY_REMISSION_APPROVAL);
            vacancyRemissionApprovalRepository.save(vacancyRemission.getVacancyRemissionApproval().get(0));
            if (Source.CITIZENPORTAL.toString().equalsIgnoreCase(vacancyRemission.getSource()))
                propertyService.updatePortal(vacancyRemission, APPLICATION_TYPE_VACANCY_REMISSION);
            basicPropertyService.persist(basicProperty);
            propertyTaxCommonUtils.buildMailAndSMS(getVacancyRemissionByApplicationNo(applicationNumber));
        }
    }

    private List<Assignment> getCurrentOwnerAssignments(final StateAware stateAware) {
        return assignmentService.getAssignmentsForPosition(
                stateAware.getState().getOwnerPosition().getId(),
                new Date());
    }

    private VacancyRemission getVacancyRemissionByApplicationNo(final String applicationNumber) {
        return (VacancyRemission) getCurrentSession()
                .createQuery("from VacancyRemission where applicationNumber = :applicationNo")
                .setParameter(APPLICATION_NO, applicationNumber).uniqueResult();
    }

    private PropertyMutation getPropertyMutationByApplicationNo(final String applicationNumber) {
        return (PropertyMutation) getCurrentSession()
                .createQuery("from PropertyMutation where applicationNo = :applicationNo")
                .setParameter(APPLICATION_NO, applicationNumber).uniqueResult();
    }

    private RevisionPetition getRevisionPetitionByApplicationNo(final String applicationNumber) {
        return (RevisionPetition) getCurrentSession()
                .createQuery("from RevisionPetition where objectionNumber = :applicationNo")
                .setParameter(APPLICATION_NO, applicationNumber).uniqueResult();
    }

    private PropertyImpl getPropertyByApplicationNo(final String applicationNumber) {
        return (PropertyImpl) getCurrentSession()
                .createQuery("from PropertyImpl where applicationNo = :applicationNo")
                .setParameter(APPLICATION_NO, applicationNumber).uniqueResult();
    }

    private Map<String, String> getApplicationTypes() {
        final Map<String, String> applicationTypes = new HashMap<>();
        applicationTypes.put(CREATE, APPLICATION_TYPE_NEW_ASSESSENT);
        applicationTypes.put(ALTER, APPLICATION_TYPE_ALTER_ASSESSENT);
        applicationTypes.put(BIFURCATE, APPLICATION_TYPE_BIFURCATE_ASSESSENT);
        applicationTypes.put(STR_DEMOLITION, APPLICATION_TYPE_DEMOLITION);
        applicationTypes.put(GRP, APPLICATION_TYPE_GRP);
        applicationTypes.put(WFLOW_ACTION_NAME_EXEMPTION, APPLICATION_TYPE_TAX_EXEMTION);
        applicationTypes.put(AMALG, APPLICATION_TYPE_AMALGAMATION);
        return applicationTypes;
    }

    private String transition(final PropertyImpl property) {
        final String applicationType = property.getCurrentState().getValue().split(":")[0];
            property.transition().end().withOwner((Position)null).withNextAction(null);
            property.getBasicProperty().setUnderWorkflow(false);
        return applicationType;
    }

    private void transition(final RevisionPetition revPetition) {
            revPetition.getBasicProperty().setStatus(
                    propertyStatusDAO.getPropertyStatusByCode(PropertyTaxConstants.STATUS_CODE_ASSESSED));
            revPetition.getBasicProperty().getProperty().setStatus(STATUS_ISHISTORY);
            revPetition.getBasicProperty().setUnderWorkflow(Boolean.FALSE);
            revPetition.getProperty().setStatus(STATUS_ISACTIVE);
            revPetition.transition().end().withOwner((Position)null).withNextAction(null);
    }

    public void transition(final PropertyMutation propertyMutation) {
            propertyMutation.transition().end().withOwner((Position)null).withNextAction(null);
            propertyMutation.getBasicProperty().setUnderWorkflow(false);
    }

    private void transition(final VacancyRemission vacancyRemission) {
        final VacancyRemissionApproval vacancyRemissionApproval = vacancyRemission.getVacancyRemissionApproval().get(0);
            vacancyRemissionApproval.transition().end().withOwner((Position)null).withNextAction(null);
            vacancyRemission.getBasicProperty().setUnderWorkflow(false);
    }

    private Assignment getWorkflowInitiator(final StateAware state, final BasicProperty basicProperty) {
        Assignment wfInitiator = null;
        if (basicProperty.getSource().equals(PropertyTaxConstants.SOURCEOFDATA_ONLINE)) {
            if (!state.getStateHistory().isEmpty())
                wfInitiator = assignmentService.getAssignmentsForPosition(state.getStateHistory().get(0)
                        .getOwnerPosition().getId(), new Date()).get(0);
        } else if (propertyService.isEmployee(state.getCreatedBy())
                && !ANONYMOUS_USER.equalsIgnoreCase(state.getCreatedBy().getName())
                && !propertyService.isCitizenPortalUser(state.getCreatedBy()))
            wfInitiator = assignmentService.getAllActiveEmployeeAssignmentsByEmpId(state.getCreatedBy().getId()).get(0);
        else if (!state.getStateHistory().isEmpty())
            wfInitiator = assignmentService.getAssignmentsForPosition(
                    state.getStateHistory().get(0).getOwnerPosition().getId(), new Date()).get(0);
        else
            wfInitiator = assignmentService.getAssignmentsForPosition(state.getState().getOwnerPosition().getId(),
                    new Date()).get(0);
        return wfInitiator;
    }

    private Session getCurrentSession() {
        return entityManager.unwrap(Session.class);
    }

    @RequestMapping(value = "/propertyTax/downloadSignedNotice")
    public void downloadSignedNotice(final HttpServletRequest request, final HttpServletResponse response) {
        final String signedFileStoreId = request.getParameter("signedFileStoreId");
        final File file = fileStoreService.fetch(signedFileStoreId, PropertyTaxConstants.FILESTORE_MODULE_NAME);
        final FileStoreMapper fileStoreMapper = fileStoreMapperRepository.findByFileStoreId(signedFileStoreId);
        response.setContentType("application/pdf");
        response.setContentType("application/octet-stream");
        response.setHeader("content-disposition", "attachment; filename=\"" + fileStoreMapper.getFileName() + "\"");
        try (FileInputStream inStream = new FileInputStream(file)) {
            final OutputStream outStream = response.getOutputStream();
            int bytesRead = -1;
            final byte[] buffer = FileUtils.readFileToByteArray(file);
            while ((bytesRead = inStream.read(buffer)) != -1)
                outStream.write(buffer, 0, bytesRead);
        } catch (final FileNotFoundException fileNotFoundExcep) {
            throw new ApplicationRuntimeException("Exception while loading file : " + fileNotFoundExcep);
        } catch (final IOException ioExcep) {
            throw new ApplicationRuntimeException("Exception while downloading notice : " + ioExcep);
        }
    }
}
