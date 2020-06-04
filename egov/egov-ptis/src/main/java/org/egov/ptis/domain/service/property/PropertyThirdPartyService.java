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

package org.egov.ptis.domain.service.property;

import static org.egov.ptis.constants.PropertyTaxConstants.APPLICATION_TYPE_ALTER_ASSESSENT;
import static org.egov.ptis.constants.PropertyTaxConstants.APPLICATION_TYPE_AMALGAMATION;
import static org.egov.ptis.constants.PropertyTaxConstants.APPLICATION_TYPE_BIFURCATE_ASSESSENT;
import static org.egov.ptis.constants.PropertyTaxConstants.APPLICATION_TYPE_DEMOLITION;
import static org.egov.ptis.constants.PropertyTaxConstants.APPLICATION_TYPE_NEW_ASSESSENT;
import static org.egov.ptis.constants.PropertyTaxConstants.APPLICATION_TYPE_REVISION_PETITION;
import static org.egov.ptis.constants.PropertyTaxConstants.APPLICATION_TYPE_TAX_EXEMTION;
import static org.egov.ptis.constants.PropertyTaxConstants.APPLICATION_TYPE_TRANSFER_OF_OWNERSHIP;
import static org.egov.ptis.constants.PropertyTaxConstants.APPLICATION_TYPE_VACANCY_REMISSION;
import static org.egov.ptis.constants.PropertyTaxConstants.FILESTORE_MODULE_NAME;
import static org.egov.ptis.constants.PropertyTaxConstants.NOTICE_TYPE_EXEMPTION;
import static org.egov.ptis.constants.PropertyTaxConstants.NOTICE_TYPE_MUTATION_CERTIFICATE;
import static org.egov.ptis.constants.PropertyTaxConstants.NOTICE_TYPE_SPECIAL_NOTICE;
import static org.egov.ptis.constants.PropertyTaxConstants.PROPERTY_MODIFY_REASON_ADD_OR_ALTER;
import static org.egov.ptis.constants.PropertyTaxConstants.PROPERTY_MODIFY_REASON_BIFURCATE;
import static org.egov.ptis.constants.PropertyTaxConstants.STATUS_APPROVED;
import static org.egov.ptis.constants.PropertyTaxConstants.STATUS_OPEN;
import static org.egov.ptis.constants.PropertyTaxConstants.STATUS_REJECTED;
import static org.egov.ptis.constants.PropertyTaxConstants.WFLOW_ACTION_END;
import static org.egov.ptis.constants.PropertyTaxConstants.WF_STATE_BILL_COLLECTOR_APPROVED;
import static org.egov.ptis.constants.PropertyTaxConstants.WF_STATE_CLOSED;
import static org.egov.ptis.constants.PropertyTaxConstants.WF_STATE_COMMISSIONER_APPROVED;
import static org.egov.ptis.constants.PropertyTaxConstants.WF_STATE_DIGITALLY_SIGNED;
import static org.egov.ptis.constants.PropertyTaxConstants.WF_STATE_REJECTED;
import static org.egov.ptis.constants.PropertyTaxConstants.WS_VIEW_PROPERT_BY_APP_NO_URL;
import static org.egov.ptis.constants.PropertyTaxConstants.PROPERTY_MODIFY_REASON_AMALG;
import static org.egov.ptis.constants.PropertyTaxConstants.WFLOW_ACTION_STEP_APPROVE;
import static org.egov.ptis.constants.PropertyTaxConstants.WFLOW_ACTION_STEP_REJECT_TO_CANCEL;
import static org.egov.ptis.constants.PropertyTaxConstants.PROP_CREATE_RSN_BIFUR;

import static java.lang.String.format;

import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.apache.commons.io.FileUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.log4j.Logger;
import org.apache.struts2.ServletActionContext;
import org.egov.infra.filestore.entity.FileStoreMapper;
import org.egov.infra.filestore.service.FileStoreService;
import org.egov.infra.integration.event.model.enums.ApplicationStatus;
import org.egov.infra.integration.event.model.enums.TransactionStatus;
import org.egov.infra.web.utils.WebUtils;
import org.egov.infra.workflow.entity.StateHistory;
import org.egov.infstr.services.PersistenceService;
import org.egov.ptis.domain.entity.objection.Petition;
import org.egov.ptis.domain.entity.property.BasicProperty;
import org.egov.ptis.domain.entity.property.PropertyImpl;
import org.egov.ptis.domain.entity.property.PropertyMutation;
import org.egov.ptis.domain.entity.property.VacancyRemission;
import org.egov.ptis.domain.service.transfer.PropertyTransferService;
import org.egov.ptis.event.EventPublisher;
import org.egov.ptis.notice.PtNotice;
import org.hibernate.Query;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.transaction.annotation.Transactional;

public class PropertyThirdPartyService {

    private static final Logger LOGGER = Logger.getLogger(PropertyThirdPartyService.class);
    public PersistenceService persistenceService;

    @Autowired
    @Qualifier("fileStoreService")
    protected FileStoreService fileStoreService;

    @Autowired
    private PropertyTransferService transferOwnerService;

    @Autowired
    private PropertyPersistenceService basicPropertyService;

    @Autowired
    private EventPublisher eventPublisher;

    // For Exemption and vacancyremission is in progess
    public byte[] getSpecialNotice(String assessmentNo, String applicationNo, String applicationType)
            throws IOException {
        PtNotice ptNotice = null;
        if (LOGGER.isDebugEnabled())
            LOGGER.debug("Entered into getSpecialNotice application type:" + applicationType + ",application no:"
                    + applicationNo);
        if (applicationType.equals(APPLICATION_TYPE_NEW_ASSESSENT)
                || applicationType.equals(APPLICATION_TYPE_ALTER_ASSESSENT)
                || applicationType.equals(APPLICATION_TYPE_BIFURCATE_ASSESSENT)
                || applicationType.equals(APPLICATION_TYPE_DEMOLITION)
                || applicationType.equals(APPLICATION_TYPE_AMALGAMATION)) {
            if (StringUtils.isNotBlank(applicationNo)) {
                ptNotice = (PtNotice) persistenceService.find(
                        "from PtNotice where applicationNumber = ? and noticeType = ?", applicationNo,
                        NOTICE_TYPE_SPECIAL_NOTICE);
            } else if (StringUtils.isNotBlank(assessmentNo)) {
                ptNotice = (PtNotice) persistenceService.find("from PtNotice where basicProperty.upicNo = ?",
                        assessmentNo);
            }
        } else if (applicationType.equals(APPLICATION_TYPE_TRANSFER_OF_OWNERSHIP)) {
            if (StringUtils.isNotBlank(applicationNo)) {
                ptNotice = (PtNotice) persistenceService.find(
                        "from PtNotice where applicationNumber = ? and noticeType = ?", applicationNo,
                        NOTICE_TYPE_MUTATION_CERTIFICATE);
            }

        } else if ((applicationType.equals(APPLICATION_TYPE_TAX_EXEMTION)) && (StringUtils.isNotBlank(applicationNo))) {
            ptNotice = (PtNotice) persistenceService.find(
                    "from PtNotice where applicationNumber = ? and noticeType = ?", applicationNo,
                    NOTICE_TYPE_EXEMPTION);
        }

        if (ptNotice != null && ptNotice.getFileStore() != null) {
            if (LOGGER.isDebugEnabled())
                LOGGER.debug("Property notice : " + ptNotice.getNoticeNo());
            final FileStoreMapper fsm = ptNotice.getFileStore();
            final File file = fileStoreService.fetch(fsm, FILESTORE_MODULE_NAME);
            return FileUtils.readFileToByteArray(file);
        } else
            return null;

    }

    public Map<String, String> validatePropertyStatus(String applicationNo, String applicationType) {
        if (LOGGER.isDebugEnabled())
            LOGGER.debug("Entered into validatePropertyStatus method,applicationType:" + applicationType
                    + ",application no:" + applicationNo);
        PropertyImpl property = null;
        PropertyMutation mutation = null;
        VacancyRemission vacancyRemission = null;
        Petition petition = null;
        StateHistory stateHistory = null;
        Map<String, String> statusCommentsMap = new HashMap<String, String>();
        if (applicationType.equals(APPLICATION_TYPE_NEW_ASSESSENT)
                || applicationType.equals(APPLICATION_TYPE_ALTER_ASSESSENT)
                || applicationType.equals(APPLICATION_TYPE_BIFURCATE_ASSESSENT)
                || applicationType.equals(APPLICATION_TYPE_TAX_EXEMTION)
                || applicationType.equals(APPLICATION_TYPE_DEMOLITION)
                || applicationType.equals(APPLICATION_TYPE_AMALGAMATION)) {
            if (StringUtils.isNotBlank(applicationNo)) {
                property = (PropertyImpl) persistenceService.find("From PropertyImpl where applicationNo = ? ",
                        applicationNo);
            }
            if (null != property) {
                if (LOGGER.isDebugEnabled())
                    LOGGER.debug("Inside applicationType:" + applicationType + "for property" + property);
                if (!property.getState().getHistory().isEmpty()) {
                    int size = property.getState().getHistory().size();
                    stateHistory = property.getStateHistory().get(size - 1);
                }
                if (property.getState().getValue().equals(WF_STATE_CLOSED)
                        && (stateHistory.getValue().endsWith(WF_STATE_DIGITALLY_SIGNED) || stateHistory.getValue()
                                .endsWith(WF_STATE_COMMISSIONER_APPROVED))) {
                    statusCommentsMap.put("status", STATUS_APPROVED);
                    statusCommentsMap.put("comments", stateHistory.getComments());
                    statusCommentsMap.put("updatedBy", stateHistory.getLastModifiedBy().getName());
                } else if (property.getState().getValue().equals(WF_STATE_CLOSED)
                        && stateHistory.getValue().endsWith(WF_STATE_REJECTED)) {
                    statusCommentsMap.put("status", STATUS_REJECTED);
                    statusCommentsMap.put("comments", property.getState().getComments());
                    statusCommentsMap.put("updatedBy", property.getState().getLastModifiedBy().getName());
                } else {
                    statusCommentsMap.put("status", STATUS_OPEN);
                    statusCommentsMap.put("comments", property.getState().getComments());
                    statusCommentsMap.put("updatedBy", property.getState().getLastModifiedBy().getName());
                }
            }

        } else if (applicationType.equals(APPLICATION_TYPE_TRANSFER_OF_OWNERSHIP)) {
            if (StringUtils.isNotBlank(applicationNo)) {
                mutation = transferOwnerService.getPropertyMutationByApplicationNo(applicationNo);
            }
            if (null != mutation) {
                if (LOGGER.isDebugEnabled())
                    LOGGER.debug("Inside applicationType:" + applicationType + "for property mutation" + mutation);
                if (!mutation.getState().getHistory().isEmpty()) {
                    int size = mutation.getState().getHistory().size();
                    stateHistory = mutation.getStateHistory().get(size - 1);
                }
                if (mutation.getState().getValue().equals(WF_STATE_CLOSED)
                        && (stateHistory.getValue().equals(WF_STATE_DIGITALLY_SIGNED) || stateHistory.getValue()
                                .equals(WF_STATE_COMMISSIONER_APPROVED))) {
                    statusCommentsMap.put("status", STATUS_APPROVED);
                    statusCommentsMap.put("comments", stateHistory.getComments());
                    statusCommentsMap.put("updatedBy", stateHistory.getLastModifiedBy().getName());
                } else if (mutation.getState().getValue().equals(WF_STATE_CLOSED)
                        && stateHistory.getValue().equals(WF_STATE_REJECTED)) {
                    statusCommentsMap.put("status", STATUS_REJECTED);
                    statusCommentsMap.put("comments", mutation.getState().getComments());
                    statusCommentsMap.put("updatedBy", mutation.getState().getLastModifiedBy().getName());
                } else {
                    statusCommentsMap.put("status", STATUS_OPEN);
                    statusCommentsMap.put("comments", mutation.getState().getComments());
                    statusCommentsMap.put("updatedBy", mutation.getState().getLastModifiedBy().getName());
                }
            }
        } else if (applicationType.equals(APPLICATION_TYPE_VACANCY_REMISSION)) {
            if (StringUtils.isNotBlank(applicationNo)) {
                vacancyRemission = (VacancyRemission) persistenceService.find(
                        "From VacancyRemission where applicationNumber = ? ", applicationNo);
            }
            if (null != vacancyRemission) {
                if (LOGGER.isDebugEnabled())
                    LOGGER.debug("Inside applicationType:" + applicationType + "for property vacancy remission"
                            + vacancyRemission);
                if (!vacancyRemission.getState().getHistory().isEmpty()) {
                    int size = vacancyRemission.getState().getHistory().size();
                    stateHistory = vacancyRemission.getStateHistory().get(size - 1);
                }
                if (vacancyRemission.getState().getValue().equals(WF_STATE_CLOSED)
                        && stateHistory.getValue().endsWith(WF_STATE_BILL_COLLECTOR_APPROVED)) {
                    statusCommentsMap.put("status", STATUS_APPROVED);
                    statusCommentsMap.put("comments", vacancyRemission.getState().getComments());
                    statusCommentsMap.put("updatedBy", vacancyRemission.getState().getLastModifiedBy().getName());
                } else if (vacancyRemission.getState().getValue().equals(WF_STATE_CLOSED)
                        && stateHistory.getValue().endsWith(WF_STATE_REJECTED)) {
                    statusCommentsMap.put("status", STATUS_REJECTED);
                    statusCommentsMap.put("comments", stateHistory.getComments());
                    statusCommentsMap.put("updatedBy", stateHistory.getLastModifiedBy().getName());
                } else {
                    statusCommentsMap.put("status", STATUS_OPEN);
                    statusCommentsMap.put("comments", vacancyRemission.getState().getComments());
                    statusCommentsMap.put("updatedBy", vacancyRemission.getState().getLastModifiedBy().getName());
                }
            }
        } else if (applicationType.equals(APPLICATION_TYPE_REVISION_PETITION)) {
            if (StringUtils.isNotBlank(applicationNo)) {
                petition = (Petition) persistenceService.find(
                        "From Petition where objectionNumber = ? ", applicationNo);
            }
            if (null != petition) {
                if (LOGGER.isDebugEnabled())
                    LOGGER.debug("Inside applicationType:" + applicationType + "for property revision petition"
                            + petition);
                if (!petition.getState().getHistory().isEmpty()) {
                    int size = petition.getState().getHistory().size();
                    stateHistory = petition.getStateHistory().get(size - 1);
                }
                if (petition.getState().getValue().equals(WFLOW_ACTION_END)
                        && (stateHistory.getValue().endsWith(WF_STATE_DIGITALLY_SIGNED) || stateHistory.getValue()
                                .endsWith(WF_STATE_COMMISSIONER_APPROVED))) {
                    statusCommentsMap.put("status", STATUS_APPROVED);
                    statusCommentsMap.put("comments", stateHistory.getComments());
                    statusCommentsMap.put("updatedBy", stateHistory.getLastModifiedBy().getName());
                } else if (petition.getState().getValue().equals(WFLOW_ACTION_END)) {
                    statusCommentsMap.put("status", STATUS_REJECTED);
                    statusCommentsMap.put("comments", stateHistory.getComments());
                    statusCommentsMap.put("updatedBy", stateHistory.getLastModifiedBy().getName());
                } else {
                    statusCommentsMap.put("status", STATUS_OPEN);
                    statusCommentsMap.put("comments", petition.getState().getComments());
                    statusCommentsMap.put("updatedBy", petition.getState().getLastModifiedBy().getName());
                }
            }
        }
        if (LOGGER.isDebugEnabled())
            LOGGER.debug("Exiting from validatePropertyStatus method");
        return statusCommentsMap;
    }

    public PersistenceService getPersistenceService() {
        return persistenceService;
    }

    public void setPersistenceService(PersistenceService persistenceService) {
        this.persistenceService = persistenceService;
    }

    @Transactional
    public void saveBasicPropertyAndPublishEvent(final BasicProperty basicProperty, final PropertyImpl property,
            final HttpServletRequest request, final String transactionId) {
        String remarks = null;
        try {
            basicPropertyService.persist(basicProperty);
            if (PROP_CREATE_RSN_BIFUR.equalsIgnoreCase(basicProperty.getPropertyMutationMaster().getCode())) {
                remarks = "Child Property for Bifurcation Initiated";
            } else {
                remarks = "New Property Created";
            }

            String viewURL = format(WS_VIEW_PROPERT_BY_APP_NO_URL, WebUtils.extractRequestDomainURL(request, false),
                    property.getApplicationNo(), APPLICATION_TYPE_NEW_ASSESSENT);

            eventPublisher.publishWSEvent(transactionId, TransactionStatus.SUCCESS, property.getApplicationNo(),
                    ApplicationStatus.INPROGRESS, viewURL, remarks);

        } catch (Exception ex) {
            if (PROP_CREATE_RSN_BIFUR.equalsIgnoreCase(basicProperty.getPropertyMutationMaster().getCode())) {
                remarks = "Child Property for Bifurcation Failed";
            } else
                remarks = "Property Creation Failed";
 
            LOGGER.error("exception while saving basic proeprty", ex);
            eventPublisher.publishWSEvent(transactionId, TransactionStatus.FAILED,
                    property.getApplicationNo(), null, null,remarks );
        }
    }
    
    /*
     * api to update basic property and publishing the event for ward secretary
     */
    @Transactional
    public void updateBasicPropertyAndPublishEvent(final BasicProperty basicProperty, final PropertyImpl property,
            final String modifyReason, final String transactionId) {

        String viewURL = null;
        String succeessMsg = null;
        String failureMsg = null;

        if (PROPERTY_MODIFY_REASON_ADD_OR_ALTER.equalsIgnoreCase(modifyReason)) {
            viewURL = format(WS_VIEW_PROPERT_BY_APP_NO_URL,
                    WebUtils.extractRequestDomainURL(ServletActionContext.getRequest(), false),
                    property.getApplicationNo(), APPLICATION_TYPE_ALTER_ASSESSENT);
            succeessMsg = "Property Addition/Alteration Initiated";
            failureMsg = "Property Addition/Alteration Failed";
        } else if (PROPERTY_MODIFY_REASON_BIFURCATE.equalsIgnoreCase(modifyReason)) {
            viewURL = format(WS_VIEW_PROPERT_BY_APP_NO_URL,
                    WebUtils.extractRequestDomainURL(ServletActionContext.getRequest(), false),
                    property.getApplicationNo(), APPLICATION_TYPE_BIFURCATE_ASSESSENT);
            succeessMsg = "Property Bifurcation Initiated";
            failureMsg = "Property Bifurcation Failed";
        } else if (PROPERTY_MODIFY_REASON_AMALG.equalsIgnoreCase(modifyReason)) {
            viewURL = format(WS_VIEW_PROPERT_BY_APP_NO_URL,
                    WebUtils.extractRequestDomainURL(ServletActionContext.getRequest(), false),
                    property.getApplicationNo(), APPLICATION_TYPE_AMALGAMATION);
            succeessMsg = "Property Amalgamation Initiated";
            failureMsg = "Property Amalgamation Failed";

        }
        try {
            basicPropertyService.update(basicProperty);

            eventPublisher.publishWSEvent(transactionId, TransactionStatus.SUCCESS,
                    property.getApplicationNo(), ApplicationStatus.INPROGRESS, viewURL, succeessMsg);

        } catch (Exception ex) {
            LOGGER.error("exception while updating basic property.", ex);
            eventPublisher.publishWSEvent(transactionId, TransactionStatus.FAILED,
                    property.getApplicationNo(), null, null, failureMsg);
        }
    }
    
    public void publishEventForAppurTenant(final String transactionId, final String nonVacantPropAppNo,
            final String vacantPropAppNo, final boolean isCreated) {
        String viewURL = format(WS_VIEW_PROPERT_BY_APP_NO_URL,
                WebUtils.extractRequestDomainURL(ServletActionContext.getRequest(), false),
                nonVacantPropAppNo, APPLICATION_TYPE_NEW_ASSESSENT);
        if (isCreated) {
            eventPublisher.publishWSEvent(transactionId, TransactionStatus.SUCCESS,
                    nonVacantPropAppNo, ApplicationStatus.INPROGRESS, viewURL,
                    "Created Appurtenant Property – VLT Application Number:" + vacantPropAppNo);
        } else {

            eventPublisher.publishWSEvent(transactionId, TransactionStatus.FAILED,
                    nonVacantPropAppNo, null, null, "Appurtenant Property Creation Failed.");
        }

    } 
    
    public void publishPropertyUpdateEvent(final BasicProperty basicProperty, final String applicationNumber, final String action,
            final boolean isCancelled) {
        PropertyImpl appurtenantProperty = getAppurtenantPropertyByBasicProperty(basicProperty);
        String remarks;
        if (appurtenantProperty == null) {
            if (isCancelled) {

                if (PROP_CREATE_RSN_BIFUR.equalsIgnoreCase(basicProperty.getPropertyMutationMaster().getCode())) {
                    remarks = WFLOW_ACTION_STEP_REJECT_TO_CANCEL.equalsIgnoreCase(action)
                            ? "Child Property for Bifurcation Rejected to Cancel"
                            : "Child Property for Bifurcation Cancelled";
                } else
                    remarks = WFLOW_ACTION_STEP_REJECT_TO_CANCEL.equalsIgnoreCase(action)
                            ? "New Property Rejected to Cancel" : "New Property Cancelled";
            } else {
                if (PROP_CREATE_RSN_BIFUR.equalsIgnoreCase(basicProperty.getPropertyMutationMaster().getCode())) {
                    remarks = WFLOW_ACTION_STEP_APPROVE.equalsIgnoreCase(action)
                            ? "Child Property for Bifurcation Approved" : "Child Property for Bifurcation Rejected";
                } else
                    remarks = WFLOW_ACTION_STEP_APPROVE.equalsIgnoreCase(action) ? "New Property Approved"
                            : "New Property Rejected";
            }

        } else {
            if (isCancelled)
                remarks = WFLOW_ACTION_STEP_REJECT_TO_CANCEL.equalsIgnoreCase(action)
                        ? "Appurtenant Property Rejected to Cancel.VLT ApplicationNo:" + appurtenantProperty.getApplicationNo()
                        : "Appurtenant Property Cancelled.VLT ApplicationNo:" + appurtenantProperty.getApplicationNo();
            else
                remarks = WFLOW_ACTION_STEP_APPROVE.equalsIgnoreCase(action)
                        ? "Appurtenant Property Approved.VLT ApplicationNo:" + appurtenantProperty.getApplicationNo()
                        : "Appurtenant Property Rejected.VLT ApplicationNo:" + appurtenantProperty.getApplicationNo();
        }
        publishUpdateEvent(applicationNumber, action, remarks);
    }

    /*
     * api to publish update event
     */
    public void publishUpdateEvent(final String applicationNumber, final String action, final String remarks) {
        ApplicationStatus applicationStatus;
        String applicationRemarks = remarks;
        if (WFLOW_ACTION_STEP_APPROVE.equalsIgnoreCase(action)) {
            applicationStatus = ApplicationStatus.APPROVED;
        } else {
            applicationStatus = ApplicationStatus.REJECTED;
        }
        try {
            eventPublisher.publishWSUpdateEvent(applicationNumber, applicationStatus, applicationRemarks);
        } catch (Exception ex) {
            LOGGER.info("exception while publishing update event.Application No:" + applicationNumber, ex);
        }
    }

    private PropertyImpl getAppurtenantPropertyByBasicProperty(BasicProperty basicProperty) {
        List<PropertyImpl> appurtenantPropertyList;
        final Query qry = persistenceService.getSession()
                .createQuery(
                        "select P from PropertyStatusValues PSV,PropertyImpl P  where PSV.referenceBasicProperty.id =:BasicPropertyId and P.basicProperty = PSV.basicProperty.id and PSV.isActive='Y' ");
        qry.setParameter("BasicPropertyId", basicProperty.getId());
        appurtenantPropertyList = qry.list();
        if (appurtenantPropertyList.isEmpty()) {
            return null;
        } else {
            return appurtenantPropertyList.get(0);
        }

    }

}
