package org.egov.pgr.web.controller.complaint;

import java.util.Arrays;
import java.util.Set;
import java.util.stream.Collectors;

import org.apache.commons.lang3.ArrayUtils;
import org.egov.exceptions.EGOVRuntimeException;
import org.egov.infra.filestore.FileStoreMapper;
import org.egov.infra.filestore.service.FileStoreService;
import org.egov.infra.utils.SecurityUtils;
import org.egov.pgr.service.ComplaintService;
import org.egov.pgr.service.ComplaintTypeService;
import org.egov.pgr.utils.constants.CommonConstants;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.MessageSource;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.web.multipart.MultipartFile;

public abstract class GenericComplaintController {

    public static final String ERROR = "error";
    public static final String MESSAGE = "message";

    @Autowired
    protected ComplaintTypeService complaintTypeService;

    @Autowired
    protected ComplaintService complaintService;

    @Autowired
    protected MessageSource messageSource;

    @Autowired
    protected SecurityUtils securityUtils;
    
    @Autowired
    @Qualifier("localDiskFileStoreService")
    protected FileStoreService fileStoreService;

    protected String getMessage(final String messageKey, final Object... args) {
        return messageSource.getMessage(messageKey, args, LocaleContextHolder.getLocale());
    }

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