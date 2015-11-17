package org.egov.wtms.integration;

import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import org.apache.commons.io.FileUtils;
import org.apache.commons.lang3.StringUtils;
import org.egov.infra.filestore.entity.FileStoreMapper;
import org.egov.infra.filestore.service.FileStoreService;
import org.egov.wtms.application.entity.WaterConnectionDetails;
import org.egov.wtms.application.service.WaterConnectionDetailsService;
import org.egov.wtms.utils.constants.WaterTaxConstants;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;


public class WaterChargesThirdPartyService {

    public WaterConnectionDetails waterConnectionDetails;

    @Autowired
    @Qualifier("fileStoreService")
    protected FileStoreService fileStoreService;

    private final WaterConnectionDetailsService waterConnectionDetailsService;

    @Autowired
    public WaterChargesThirdPartyService(final WaterConnectionDetailsService waterConnectionDetailsService) {
        this.waterConnectionDetailsService = waterConnectionDetailsService;
    }

    public byte[] getEstimationNotice(final String applicationNo) throws IOException {
        if (StringUtils.isNotBlank(applicationNo))
            waterConnectionDetails = waterConnectionDetailsService.findByApplicationNumber(applicationNo);
        if (waterConnectionDetails != null && waterConnectionDetails.getFileStore() != null) {
            final FileStoreMapper fsm = waterConnectionDetails.getFileStore();
            final File file = fileStoreService.fetch(fsm, WaterTaxConstants.FILESTORE_MODULECODE);
            return FileUtils.readFileToByteArray(file);
        } else
            return null;
    }

    public Map<String, String> validateWaterConnectionStatus(final String applicationNo) {
        waterConnectionDetails = waterConnectionDetailsService.findByApplicationNumber(applicationNo);
        final Map<String, String> statusCommentsMap = new HashMap<String, String>();
        if (waterConnectionDetails.getStatus().getCode().equals(WaterTaxConstants.APPLICATION_STATUS_CREATED)
                || waterConnectionDetails.getStatus().getCode().equals(WaterTaxConstants.APPLICATION_STATUS_VERIFIED)
                || waterConnectionDetails.getStatus().getCode()
                        .equals(WaterTaxConstants.APPLICATION_STATUS_ESTIMATENOTICEGEN)
                || waterConnectionDetails.getStatus().getCode().equals(WaterTaxConstants.APPLICATION_STATUS_FEEPAID)) {
            statusCommentsMap.put("status", WaterTaxConstants.OPEN);
            statusCommentsMap.put("comments", waterConnectionDetails.getState().getComments());
            statusCommentsMap.put("updatedBy", waterConnectionDetails.getState().getLastModifiedBy().getName());
        } else if (waterConnectionDetails.getStatus().getCode().equals(WaterTaxConstants.APPLICATION_STATUS_SANCTIONED)) {
            statusCommentsMap.put("status", WaterTaxConstants.APPROVED);
            statusCommentsMap.put("comments", waterConnectionDetails.getState().getComments());
            statusCommentsMap.put("updatedBy", waterConnectionDetails.getState().getLastModifiedBy().getName());
        } else if (waterConnectionDetails.getStatus().getCode().equals(WaterTaxConstants.APPLICATION_STATUS_CANCELLED)) {
            statusCommentsMap.put("status", WaterTaxConstants.WF_STATE_REJECTED);
            statusCommentsMap.put("comments", waterConnectionDetails.getState().getComments());
            statusCommentsMap.put("updatedBy", waterConnectionDetails.getState().getLastModifiedBy().getName());
        }
        return statusCommentsMap;
    }
}
