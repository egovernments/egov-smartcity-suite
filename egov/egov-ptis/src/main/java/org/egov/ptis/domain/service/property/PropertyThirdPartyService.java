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

import org.apache.commons.io.FileUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.log4j.Logger;
import org.egov.infra.filestore.entity.FileStoreMapper;
import org.egov.infra.filestore.service.FileStoreService;
import org.egov.infra.workflow.entity.StateHistory;
import org.egov.infstr.services.PersistenceService;
import org.egov.ptis.domain.entity.objection.RevisionPetition;
import org.egov.ptis.domain.entity.property.PropertyImpl;
import org.egov.ptis.domain.entity.property.PropertyMutation;
import org.egov.ptis.domain.entity.property.VacancyRemission;
import org.egov.ptis.domain.service.transfer.PropertyTransferService;
import org.egov.ptis.notice.PtNotice;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;

import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import static org.egov.ptis.constants.PropertyTaxConstants.*;

public class PropertyThirdPartyService {

    private static final Logger LOGGER = Logger.getLogger(PropertyThirdPartyService.class);
    public PersistenceService persistenceService;

    @Autowired
    @Qualifier("fileStoreService")
    protected FileStoreService fileStoreService;

    @Autowired
    private PropertyTransferService transferOwnerService;

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
        RevisionPetition revisionPetition = null;
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
                revisionPetition = (RevisionPetition) persistenceService.find(
                        "From RevisionPetition where objectionNumber = ? ", applicationNo);
            }
            if (null != revisionPetition) {
                if (LOGGER.isDebugEnabled())
                    LOGGER.debug("Inside applicationType:" + applicationType + "for property revision petition"
                            + revisionPetition);
                if (!revisionPetition.getState().getHistory().isEmpty()) {
                    int size = revisionPetition.getState().getHistory().size();
                    stateHistory = revisionPetition.getStateHistory().get(size - 1);
                }
                if (revisionPetition.getState().getValue().equals(WFLOW_ACTION_END)
                        && (stateHistory.getValue().endsWith(WF_STATE_DIGITALLY_SIGNED) || stateHistory.getValue()
                                .endsWith(WF_STATE_COMMISSIONER_APPROVED))) {
                    statusCommentsMap.put("status", STATUS_APPROVED);
                    statusCommentsMap.put("comments", stateHistory.getComments());
                    statusCommentsMap.put("updatedBy", stateHistory.getLastModifiedBy().getName());
                } else if (revisionPetition.getState().getValue().equals(WFLOW_ACTION_END)) {
                    statusCommentsMap.put("status", STATUS_REJECTED);
                    statusCommentsMap.put("comments", stateHistory.getComments());
                    statusCommentsMap.put("updatedBy", stateHistory.getLastModifiedBy().getName());
                } else {
                    statusCommentsMap.put("status", STATUS_OPEN);
                    statusCommentsMap.put("comments", revisionPetition.getState().getComments());
                    statusCommentsMap.put("updatedBy", revisionPetition.getState().getLastModifiedBy().getName());
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

}
