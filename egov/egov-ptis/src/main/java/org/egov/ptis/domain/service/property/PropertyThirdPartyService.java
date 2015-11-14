package org.egov.ptis.domain.service.property;

import static org.egov.ptis.constants.PropertyTaxConstants.APPLICATION_TYPE_ALTER_ASSESSENT;
import static org.egov.ptis.constants.PropertyTaxConstants.APPLICATION_TYPE_BIFURCATE_ASSESSENT;
import static org.egov.ptis.constants.PropertyTaxConstants.APPLICATION_TYPE_DEMOLITION;
import static org.egov.ptis.constants.PropertyTaxConstants.APPLICATION_TYPE_NEW_ASSESSENT;
import static org.egov.ptis.constants.PropertyTaxConstants.APPLICATION_TYPE_TRANSFER_OF_OWNERSHIP;
import static org.egov.ptis.constants.PropertyTaxConstants.APPLICATION_TYPE_VACANCY_REMISSION;
import static org.egov.ptis.constants.PropertyTaxConstants.FILESTORE_MODULE_NAME;
import static org.egov.ptis.constants.PropertyTaxConstants.NOTICE_TYPE_SPECIAL_NOTICE;
import static org.egov.ptis.constants.PropertyTaxConstants.STATUS_APPROVED;
import static org.egov.ptis.constants.PropertyTaxConstants.STATUS_OPEN;
import static org.egov.ptis.constants.PropertyTaxConstants.STATUS_REJECTED;
import static org.egov.ptis.constants.PropertyTaxConstants.WF_STATE_CLOSED;
import static org.egov.ptis.constants.PropertyTaxConstants.WF_STATE_COMMISSIONER_APPROVED;

import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import org.apache.commons.io.FileUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.log4j.Logger;
import org.egov.infra.filestore.entity.FileStoreMapper;
import org.egov.infra.filestore.service.FileStoreService;
import org.egov.infstr.services.PersistenceService;
import org.egov.ptis.domain.dao.property.BasicPropertyDAO;
import org.egov.ptis.domain.entity.property.PropertyImpl;
import org.egov.ptis.domain.entity.property.PropertyMutation;
import org.egov.ptis.domain.entity.property.VacancyRemission;
import org.egov.ptis.domain.service.transfer.PropertyTransferService;
import org.egov.ptis.notice.PtNotice;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;

public class PropertyThirdPartyService {

    private static final Logger LOGGER = Logger.getLogger(PropertyThirdPartyService.class);
    public PersistenceService persistenceService;

    @Autowired
    @Qualifier("fileStoreService")
    protected FileStoreService fileStoreService;

    @Autowired
    private PropertyTransferService transferOwnerService;

    @Autowired
    private BasicPropertyDAO basicPropertyDAO;

    public byte[] getSpecialNotice(String assessmentNo, String applicationNo, String applicationType)
            throws IOException {
        PtNotice ptNotice = null;
        if (StringUtils.isNotBlank(applicationNo)) {
            ptNotice = (PtNotice) persistenceService.find(
                    "from PtNotice where applicationNumber = ? and noticeType = ?", applicationNo,
                    NOTICE_TYPE_SPECIAL_NOTICE);
        } else if (StringUtils.isNotBlank(assessmentNo)) {
            ptNotice = (PtNotice) persistenceService.find("from PtNotice where basicProperty.upicNo = ?", assessmentNo);
        }
        if (ptNotice != null && ptNotice.getFileStore() != null) {
            final FileStoreMapper fsm = ptNotice.getFileStore();
            final File file = fileStoreService.fetch(fsm, FILESTORE_MODULE_NAME);
            return FileUtils.readFileToByteArray(file);
        } else
            return null;
    }

    public Map<String, String> validatePropertyStatus(String applicationNo, String applicationType) {
        PropertyImpl property = null;
        PropertyMutation mutation = null;
        VacancyRemission vacancyRemission = null;
        Map<String, String> statusCommentsMap = new HashMap<String, String>();
        if (applicationType.equals(APPLICATION_TYPE_NEW_ASSESSENT)
                || applicationType.equals(APPLICATION_TYPE_ALTER_ASSESSENT)
                || applicationType.equals(APPLICATION_TYPE_BIFURCATE_ASSESSENT)
                || applicationType.equals(APPLICATION_TYPE_DEMOLITION)) {
            if (StringUtils.isNotBlank(applicationNo)) {
                property = (PropertyImpl) persistenceService.find("From PropertyImpl where applicationNo = ? ",
                        applicationNo);
            }
            if (property.getState().getValue().endsWith(WF_STATE_COMMISSIONER_APPROVED))
                statusCommentsMap.put(STATUS_APPROVED, property.getState().getComments());
            else if (property.getState().getValue().equals(WF_STATE_CLOSED))
                statusCommentsMap.put(STATUS_REJECTED, property.getState().getComments());
            else
                statusCommentsMap.put(STATUS_OPEN, property.getState().getComments());
        } else if (applicationType.equals(APPLICATION_TYPE_TRANSFER_OF_OWNERSHIP)) {
            if (StringUtils.isNotBlank(applicationNo)) {
                mutation = transferOwnerService.getPropertyMutationByApplicationNo(applicationNo);
            }
            if (mutation.getState().getValue().equals(WF_STATE_COMMISSIONER_APPROVED))
                statusCommentsMap.put(STATUS_APPROVED, mutation.getState().getComments());
            else if (mutation.getState().getValue().equals(WF_STATE_CLOSED))
                statusCommentsMap.put(STATUS_REJECTED, mutation.getState().getComments());
            else
                statusCommentsMap.put(STATUS_OPEN, mutation.getState().getComments());
        } else if (applicationType.equals(APPLICATION_TYPE_VACANCY_REMISSION)) {
            if (StringUtils.isNotBlank(applicationNo)) {
                vacancyRemission = (VacancyRemission) persistenceService.find(
                        "From VacancyRemission where applicationNumber = ? ", applicationNo);
            }
            if (vacancyRemission.getState().getValue().equals(WF_STATE_COMMISSIONER_APPROVED))
                statusCommentsMap.put(STATUS_APPROVED, vacancyRemission.getState().getComments());
            else if (vacancyRemission.getState().getValue().equals(WF_STATE_CLOSED))
                statusCommentsMap.put(STATUS_REJECTED, vacancyRemission.getState().getComments());
            else
                statusCommentsMap.put(STATUS_OPEN, vacancyRemission.getState().getComments());
        }
        return statusCommentsMap;
    }

    public PersistenceService getPersistenceService() {
        return persistenceService;
    }

    public void setPersistenceService(PersistenceService persistenceService) {
        this.persistenceService = persistenceService;
    }

}
