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

import org.apache.commons.io.FileUtils;
import org.egov.eis.entity.Assignment;
import org.egov.eis.service.AssignmentService;
import org.egov.infra.admin.master.entity.User;
import org.egov.infra.exception.ApplicationRuntimeException;
import org.egov.infra.filestore.entity.FileStoreMapper;
import org.egov.infra.filestore.repository.FileStoreMapperRepository;
import org.egov.infra.filestore.service.FileStoreService;
import org.egov.infra.security.utils.SecurityUtils;
import org.egov.infra.workflow.entity.StateAware;
import org.egov.infra.workflow.matrix.entity.WorkFlowMatrix;
import org.egov.infra.workflow.service.SimpleWorkflowService;
import org.egov.pims.commons.Position;
import org.egov.ptis.constants.PropertyTaxConstants;
import org.egov.ptis.domain.dao.property.PropertyStatusDAO;
import org.egov.ptis.domain.entity.objection.RevisionPetition;
import org.egov.ptis.domain.entity.property.BasicProperty;
import org.egov.ptis.domain.entity.property.PropertyImpl;
import org.egov.ptis.domain.entity.property.PropertyMutation;
import org.egov.ptis.domain.service.property.PropertyPersistenceService;
import org.egov.ptis.domain.service.property.PropertyService;
import org.egov.ptis.domain.service.revisionPetition.RevisionPetitionService;
import org.hibernate.Session;
import org.joda.time.DateTime;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.OutputStream;
import java.util.Date;

import static org.egov.ptis.constants.PropertyTaxConstants.ADDTIONAL_RULE_ALTER_ASSESSMENT;
import static org.egov.ptis.constants.PropertyTaxConstants.ADDTIONAL_RULE_BIFURCATE_ASSESSMENT;
import static org.egov.ptis.constants.PropertyTaxConstants.APPLICATION_TYPE_ALTER_ASSESSENT;
import static org.egov.ptis.constants.PropertyTaxConstants.APPLICATION_TYPE_BIFURCATE_ASSESSENT;
import static org.egov.ptis.constants.PropertyTaxConstants.APPLICATION_TYPE_GRP;
import static org.egov.ptis.constants.PropertyTaxConstants.APPLICATION_TYPE_NEW_ASSESSENT;
import static org.egov.ptis.constants.PropertyTaxConstants.APPLICATION_TYPE_TRANSFER_OF_OWNERSHIP;
import static org.egov.ptis.constants.PropertyTaxConstants.DEMOLITION;
import static org.egov.ptis.constants.PropertyTaxConstants.GENERAL_REVISION_PETITION;
import static org.egov.ptis.constants.PropertyTaxConstants.NEW_ASSESSMENT;
import static org.egov.ptis.constants.PropertyTaxConstants.STATUS_ISACTIVE;
import static org.egov.ptis.constants.PropertyTaxConstants.STATUS_ISHISTORY;

/**
 * @author subhash
 */
@Controller
@RequestMapping(value = "/digitalSignature")
public class DigitalSignatureWorkflowController {

    private static final String DIGISIGN_SUCCESS_MESSAGE = "Digitally Signed Successfully";

    private static final String NOTICE_SUCCESS_MESSAGE = "Notice Generated Successfully";

    private static final String STR_DEMOLITION = "Demolition";

    private static final String BIFURCATE = "Bifurcate";

    private static final String ALTER = "Alter";

    private static final String CREATE = "Create";

    private static final String GRP = "GRP";

    private static final String DIGITAL_SIGNATURE_SUCCESS = "digitalSignature-success";

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

    @RequestMapping(value = "/propertyTax/transitionWorkflow")
    public String transitionWorkflow(final HttpServletRequest request, final Model model) {
        final String fileStoreIds = request.getParameter("fileStoreId");
        final String isDigiEnabled = request.getParameter("isDigiEnabled");
        final String[] fileStoreId = fileStoreIds.split(",");
        for (final String id : fileStoreId) {
            final String applicationNumber = (String) getCurrentSession()
                    .createQuery(
                            "select notice.applicationNumber from PtNotice notice where notice.fileStore.fileStoreId = :id")
                    .setParameter("id", id).uniqueResult();
            final PropertyImpl property = (PropertyImpl) getCurrentSession()
                    .createQuery("from PropertyImpl where applicationNo = :applicationNo")
                    .setParameter("applicationNo", applicationNumber).uniqueResult();
            if (property != null) {
                final BasicProperty basicProperty = property.getBasicProperty();
                final String applicationType = transitionWorkFlow(property);
                propertyService.updateIndexes(property, getTypeForUpdateIndexes(applicationType));
                basicPropertyService.update(basicProperty);
            } else {
                final RevisionPetition revisionPetition = (RevisionPetition) getCurrentSession()
                        .createQuery("from RevisionPetition where objectionNumber = :applicationNo")
                        .setParameter("applicationNo", applicationNumber).uniqueResult();
                if (revisionPetition != null) {
                    transitionWorkFlow(revisionPetition);
                    propertyService.updateIndexes(revisionPetition,
                            PropertyTaxConstants.APPLICATION_TYPE_REVISION_PETITION);
                    revisionPetitionService.updateRevisionPetition(revisionPetition);
                } else {
                    final PropertyMutation propertyMutation = (PropertyMutation) getCurrentSession()
                            .createQuery("from PropertyMutation where applicationNo = :applicationNo")
                            .setParameter("applicationNo", applicationNumber).uniqueResult();
                    if (propertyMutation != null) {
                        final BasicProperty basicProperty = propertyMutation.getBasicProperty();
                        transitionWorkFlow(propertyMutation);
                        propertyService.updateIndexes(propertyMutation, APPLICATION_TYPE_TRANSFER_OF_OWNERSHIP);
                        basicPropertyService.persist(basicProperty);
                    }
                }
            }
        }
        if (isDigiEnabled != null && isDigiEnabled.equals("false")) {
            model.addAttribute("successMessage", NOTICE_SUCCESS_MESSAGE);
        } else {
            model.addAttribute("successMessage", DIGISIGN_SUCCESS_MESSAGE);
        }
        model.addAttribute("fileStoreId", fileStoreId.length == 1 ? fileStoreId[0] : "");
        return DIGITAL_SIGNATURE_SUCCESS;
    }

    private String getTypeForUpdateIndexes(final String applicationType) {
        return applicationType.equals(NEW_ASSESSMENT) ? APPLICATION_TYPE_NEW_ASSESSENT : applicationType
                .equals(ADDTIONAL_RULE_ALTER_ASSESSMENT) ? APPLICATION_TYPE_ALTER_ASSESSENT : applicationType
                .equals(ADDTIONAL_RULE_BIFURCATE_ASSESSMENT) ? APPLICATION_TYPE_BIFURCATE_ASSESSENT : applicationType
                .equals(DEMOLITION) ? PropertyTaxConstants.APPLICATION_TYPE_DEMOLITION : applicationType
                .equals(GENERAL_REVISION_PETITION) ? APPLICATION_TYPE_GRP : null;
    }

    private String transitionWorkFlow(final PropertyImpl property) {
        final String applicationType = property.getCurrentState().getValue().startsWith(CREATE) ? NEW_ASSESSMENT
                : property.getCurrentState().getValue().startsWith(ALTER) ? ADDTIONAL_RULE_ALTER_ASSESSMENT : property
                        .getCurrentState().getValue().startsWith(BIFURCATE) ? ADDTIONAL_RULE_BIFURCATE_ASSESSMENT
                        : property.getCurrentState().getValue().startsWith(STR_DEMOLITION) ? DEMOLITION : property
                                .getCurrentState().getValue().startsWith(GRP) ? GENERAL_REVISION_PETITION : null;
        if (propertyService.isMeesevaUser(property.getCreatedBy())) {
            property.transition().end();
            property.getBasicProperty().setUnderWorkflow(false);
        } else {
            final User user = securityUtils.getCurrentUser();
            final DateTime currentDate = new DateTime();
            final Assignment wfInitiator = getWorkflowInitiator(property,property.getBasicProperty());
            final WorkFlowMatrix wfmatrix = propertyWorkflowService.getWfMatrix(property.getStateType(), null, null,
                    applicationType, property.getCurrentState().getValue(), null);
            property.transition(true).withSenderName(user.getUsername() + "::" + user.getName())
                    .withStateValue(wfmatrix.getNextState()).withDateInfo(currentDate.toDate())
                    .withOwner(property.getCurrentState().getInitiatorPosition()!=null?property.getCurrentState().getInitiatorPosition():wfInitiator.getPosition())
                    .withNextAction(wfmatrix.getNextAction());
        }
        return applicationType;
    }

    private void transitionWorkFlow(final RevisionPetition revPetition) {
        if (propertyService.isMeesevaUser(revPetition.getCreatedBy())) {
            revPetition.getBasicProperty().setStatus(
                    propertyStatusDAO.getPropertyStatusByCode(PropertyTaxConstants.STATUS_CODE_ASSESSED));
            revPetition.getBasicProperty().getProperty().setStatus(STATUS_ISHISTORY);
            revPetition.getBasicProperty().setUnderWorkflow(Boolean.FALSE);
            revPetition.getProperty().setStatus(STATUS_ISACTIVE);
            revPetition.transition().end();
        } else {
            final User user = securityUtils.getCurrentUser();
            final Assignment wfInitiator = getWorkflowInitiator(revPetition,revPetition.getBasicProperty());
            final WorkFlowMatrix wfmatrix = revisionPetitionWorkFlowService.getWfMatrix(revPetition.getStateType(),
                    null, null, null, revPetition.getCurrentState().getValue(), null);
            revPetition.transition(true).withStateValue(wfmatrix.getNextState())
            .withOwner(revPetition.getCurrentState().getInitiatorPosition()!=null?revPetition.getCurrentState().getInitiatorPosition():wfInitiator.getPosition())
                    .withSenderName(user.getUsername() + "::" + user.getName()).withDateInfo(new DateTime().toDate())
                    .withNextAction(wfmatrix.getNextAction());
        }
    }

    public void transitionWorkFlow(final PropertyMutation propertyMutation) {
        if (propertyService.isMeesevaUser(propertyMutation.getCreatedBy())) {
            propertyMutation.transition().end();
            propertyMutation.getBasicProperty().setUnderWorkflow(false);
        } else {
            final DateTime currentDate = new DateTime();
            final User user = securityUtils.getCurrentUser();
            final Assignment wfInitiator = getWorkflowInitiator(propertyMutation,propertyMutation.getBasicProperty());
            final WorkFlowMatrix wfmatrix = transferWorkflowService.getWfMatrix(propertyMutation.getStateType(), null,
                    null, propertyMutation.getType(), propertyMutation.getCurrentState().getValue(), null);
            propertyMutation.transition(true).withSenderName(user.getUsername() + "::" + user.getName())
                    .withStateValue(wfmatrix.getNextState()).withDateInfo(currentDate.toDate())
                    .withOwner(propertyMutation.getCurrentState().getInitiatorPosition()!=null?propertyMutation.getCurrentState().getInitiatorPosition():wfInitiator.getPosition())
                    .withNextAction(wfmatrix.getNextAction());
        }
    }

    private Assignment getWorkflowInitiator(final StateAware state, final BasicProperty basicProperty) {
        Assignment wfInitiator = null;
        if(basicProperty.getSource().equals(PropertyTaxConstants.SOURCEOFDATA_ONLINE)){
        	if(!state.getStateHistory().isEmpty())
        		wfInitiator = assignmentService.getPrimaryAssignmentForPositon(state.getStateHistory().get(0)
                    .getOwnerPosition().getId());
        } else{
	        if (propertyService.isEmployee(state.getCreatedBy()))
	            wfInitiator = assignmentService.getPrimaryAssignmentForUser(state.getCreatedBy().getId());
	        else if (!state.getStateHistory().isEmpty())
	            wfInitiator = assignmentService.getAssignmentsForPosition(
	                    state.getStateHistory().get(0).getOwnerPosition().getId(), new Date()).get(0);
	        else
	            wfInitiator = assignmentService.getAssignmentsForPosition(state.getState().getOwnerPosition().getId(),
	                    new Date()).get(0);
        }
        return wfInitiator;
    }

    private Session getCurrentSession() {
        return entityManager.unwrap(Session.class);
    }

    @RequestMapping(value = "/propertyTax/downloadSignedNotice")
    public void downloadSignedNotice(final HttpServletRequest request, final HttpServletResponse response) {
        String signedFileStoreId = request.getParameter("signedFileStoreId");
        File file = fileStoreService.fetch(signedFileStoreId, PropertyTaxConstants.FILESTORE_MODULE_NAME);
        final FileStoreMapper fileStoreMapper = fileStoreMapperRepository.findByFileStoreId(signedFileStoreId);
        response.setContentType("application/pdf");
        response.setContentType("application/octet-stream");
        response.setHeader("content-disposition", "attachment; filename=\"" + fileStoreMapper.getFileName() + "\"");
        try (FileInputStream inStream = new FileInputStream(file)){
            OutputStream outStream = response.getOutputStream();
            int bytesRead = -1;
            byte[] buffer = FileUtils.readFileToByteArray(file);
            while ((bytesRead = inStream.read(buffer)) != -1) {
                outStream.write(buffer, 0, bytesRead);
            }
        } catch (FileNotFoundException fileNotFoundExcep) {
            throw new ApplicationRuntimeException("Exception while loading file : " + fileNotFoundExcep);
        } catch (final IOException ioExcep) {
            throw new ApplicationRuntimeException("Exception while downloading notice : " + ioExcep);
        }
    }
}
