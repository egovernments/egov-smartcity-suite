package org.egov.stms.web.controller.application;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

import javax.servlet.ServletContext;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.egov.infra.filestore.service.FileStoreService;
import org.egov.ptis.domain.model.AssessmentDetails;
import org.egov.stms.masters.entity.DocumentTypeMaster;
import org.egov.stms.masters.entity.SewerageApplicationType;
import org.egov.stms.masters.entity.enums.SewerageConnectionStatus;
import org.egov.stms.masters.service.DocumentTypeMasterService;
import org.egov.stms.masters.service.SewerageApplicationTypeService;
import org.egov.stms.transactions.entity.SewerageApplicationDetails;
import org.egov.stms.transactions.entity.SewerageApplicationDetailsDocument;
import org.egov.stms.transactions.service.SewerageApplicationDetailsService;
import org.egov.stms.transactions.service.SewerageThirdPartyServices;
import org.egov.stms.utils.constants.SewerageTaxConstants;
import org.jboss.logging.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;

@Controller
@RequestMapping(value="/application")
public class CloseSewerageConnectionController {

    @Autowired
    private SewerageApplicationDetailsService  sewerageApplicationDetailsService;
    
    @Autowired
    private SewerageApplicationTypeService sewerageApplicationTypeService;
    
    @Autowired
    private SewerageThirdPartyServices sewerageThirdPartyServices;
    
    @Autowired
    private DocumentTypeMasterService documentTypeMasterService;
    
    @Autowired
    @Qualifier("fileStoreService")
    protected FileStoreService fileStoreService;
    
    private static final int BUFFER_SIZE=4096;
    private static final Logger logger = Logger.getLogger(CloseSewerageConnectionController.class);

    
    @RequestMapping(value = "/close/{applicationNumber}/{shscnumber}", method = RequestMethod.GET)
    public String view(@ModelAttribute SewerageApplicationDetails SewerageApplicationDetails, final Model model, @PathVariable final String applicationNumber,@PathVariable final String shscnumber, final HttpServletRequest request) {
        SewerageApplicationDetails details = null;
        if(applicationNumber !=null){
        details = sewerageApplicationDetailsService.findByApplicationNumber(applicationNumber );
        }
        
        if(shscnumber != null){
            final AssessmentDetails propertyOwnerDetails = sewerageThirdPartyServices.getPropertyDetails(shscnumber, request);
            if(propertyOwnerDetails!=null){
               model.addAttribute("propertyOwnerDetails", propertyOwnerDetails); 
            }
        }
        model.addAttribute("sewerageApplicationDetails", details);
        return "closeSewerageConnection";
    }
    
    @RequestMapping(value="/close", method=RequestMethod.POST)
    public void closeSewerageConnection(@ModelAttribute SewerageApplicationDetails sewerageApplicationDetails,@RequestParam("file") final MultipartFile[] files){
    if(logger.isDebugEnabled())
        logger.debug(sewerageApplicationDetails.getConnection().getClosingRemarks());
        
    }
    
    @RequestMapping(value = "/downloadFile", method = RequestMethod.GET)
    public void downLoadFieldInspectionAttachment(final HttpServletRequest request, final HttpServletResponse response)
            throws IOException {
        final SewerageApplicationDetails sewerageApplicationDetails = sewerageApplicationDetailsService
                .findByApplicationNumber(request.getParameter("applicationNumber"));
        final ServletContext context = request.getServletContext();
        final File downloadFile = fileStoreService.fetch(sewerageApplicationDetails.getFileStore().getFileStoreId(), SewerageTaxConstants.FILESTORE_MODULECODE);
        final FileInputStream inputStream = new FileInputStream(downloadFile);

        // get MIME type of the file
        String mimeType = context.getMimeType(downloadFile.getAbsolutePath());
        if (mimeType == null)
            // set to binary type if MIME mapping not found
            mimeType = "application/octet-stream";

        // set content attributes for the response
        response.setContentType(mimeType);
        response.setContentLength((int) downloadFile.length());

        // set headers for the response
        final String headerKey = "Content-Disposition";
        final String headerValue = String.format("attachment; filename=\"%s\"", sewerageApplicationDetails
                .getFileStore().getFileName());
        response.setHeader(headerKey, headerValue);

        // get output stream of the response
        final OutputStream outStream = response.getOutputStream();

        final byte[] buffer = new byte[BUFFER_SIZE];
        int bytesRead = -1;

        // write bytes read from the input stream into the output stream
        while ((bytesRead = inputStream.read(buffer)) != -1)
            outStream.write(buffer, 0, bytesRead);

        inputStream.close();
        outStream.close();
    }
    
}
