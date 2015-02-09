package org.egov.pgr.web.controller.complaint;

import org.egov.infra.filestore.service.FileStoreService;
import org.egov.infra.utils.SecurityUtils;
import org.egov.pgr.service.ComplaintService;
import org.egov.pgr.service.ComplaintTypeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.MessageSource;
import org.springframework.context.i18n.LocaleContextHolder;

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
}