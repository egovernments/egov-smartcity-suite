package org.egov.pgr.web.controller.complaint;

import java.util.Arrays;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import org.apache.commons.lang3.ArrayUtils;
import org.egov.exceptions.EGOVRuntimeException;
import org.egov.infra.filestore.entity.FileStoreMapper;
import org.egov.infra.filestore.service.FileStoreService;
import org.egov.pgr.entity.ComplaintType;
import org.egov.pgr.service.ComplaintService;
import org.egov.pgr.service.ComplaintTypeService;
import org.egov.pgr.service.ReceivingCenterService;
import org.egov.pgr.utils.constants.CommonConstants;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.multipart.MultipartFile;

public abstract class GenericComplaintController {

    public static final String ERROR = "error";
    public static final String MESSAGE = "message";

    @Autowired
    protected ComplaintTypeService complaintTypeService;

    @Autowired(required=true)
    protected ComplaintService complaintService;
    
    @Autowired
    protected ReceivingCenterService receivingCenterService;

    public @ModelAttribute("complaintTypes") List<ComplaintType> frequentlyFiledComplaintTypes() {
        return complaintTypeService.getFrequentlyFiledComplaints();
    }
    
    @Autowired
    @Qualifier("fileStoreService")
    protected FileStoreService fileStoreService;


    protected Set<FileStoreMapper> addToFileStore(final MultipartFile[] files) {
        if(ArrayUtils.isNotEmpty(files)) {
            return fileStoreService.storeStreams(Arrays.asList(files)
                    .stream()
                    .filter(file -> !file.isEmpty())
                    .map(file -> {
                        try {
                            return file.getInputStream();
                        } catch (Exception e) {
                            throw new EGOVRuntimeException("Error occurred while getting inputstream",e);
                        }
                    })
                    .collect(Collectors.toSet()), CommonConstants.MODULE_NAME);
        } else {
            return null;
        }
    }
}