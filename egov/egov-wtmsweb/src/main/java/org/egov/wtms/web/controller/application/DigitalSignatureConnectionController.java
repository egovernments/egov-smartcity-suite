/*******************************************************************************
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
 *      1) All versions of this program, verbatim or modified must carry this
 *         Legal Notice.
 *
 *      2) Any misrepresentation of the origin of the material is prohibited. It
 *         is required that all modified versions of this material be marked in
 *         reasonable ways as different from the original version.
 *
 *      3) This license does not grant any rights to any user of the program
 *         with regards to rights under trademark law for use of the trade names
 *         or trademarks of eGovernments Foundation.
 *
 *   In case of any queries, you can reach eGovernments Foundation at contact@egovernments.org.
 ******************************************************************************/
package org.egov.wtms.web.controller.application;

import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Map;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.apache.commons.io.FileUtils;
import org.egov.infra.cache.impl.LRUCache;
import org.egov.infra.exception.ApplicationRuntimeException;
import org.egov.infra.filestore.service.FileStoreService;
import org.egov.infra.reporting.engine.ReportConstants;
import org.egov.infra.reporting.engine.ReportConstants.FileFormat;
import org.egov.infra.reporting.engine.ReportOutput;
import org.egov.infra.reporting.viewer.ReportViewerUtil;
import org.egov.wtms.application.entity.WaterConnectionDetails;
import org.egov.wtms.application.service.WaterConnectionDetailsService;
import org.egov.wtms.utils.constants.WaterTaxConstants;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

/**
 *
 * @author ranjit
 *
 */
@Controller
@RequestMapping(value = "/digitalSignature")
public class DigitalSignatureConnectionController {

    @PersistenceContext
    private EntityManager entityManager;
    
    @Autowired
    private WaterConnectionDetailsService waterConnectionDetailsService;

    @Autowired
    @Qualifier("fileStoreService")
    protected FileStoreService fileStoreService;
    
    @RequestMapping(value = "/waterTax/transitionWorkflow")
    public String transitionWorkflow(final HttpServletRequest request, final Model model) {
        final String fileStoreIds = request.getParameter("fileStoreId");
        final String[] fileStoreId = fileStoreIds.split(",");
        HttpSession session = request.getSession();
        Long approvalPosition = (Long)session.getAttribute(WaterTaxConstants.APPROVAL_POSITION);
        String approvalComent = (String)session.getAttribute(WaterTaxConstants.APPROVAL_COMMENT);
        String workFlowAction = (String)session.getAttribute(WaterTaxConstants.WORKFLOW_ACTION);
        String mode = (String)session.getAttribute(WaterTaxConstants.MODE);
        String applicationNumber = (String)session.getAttribute(WaterTaxConstants.APPLICATION_NUMBER);
        WaterConnectionDetails waterConnectionDetails = waterConnectionDetailsService.findByApplicationNumber(applicationNumber);
        waterConnectionDetailsService.updateWaterConnection(waterConnectionDetails, approvalPosition,
                approvalComent, waterConnectionDetails.getApplicationType().getCode(), workFlowAction, mode,
                null);
        model.addAttribute("successMessage", "Digitally Signed Successfully");
        model.addAttribute("fileStoreId", fileStoreId.length == 1 ? fileStoreId[0] : "");
        return "digitalSignature-success";
    }
    
    @RequestMapping(value = "/waterTax/previewSignedWorkOrderConnection")
    public @ResponseBody ResponseEntity<byte[]> previewSignedWorkOrderConnection(final HttpServletRequest request, final Model model) {
        String signedFileStoreId = request.getParameter("signedFileStoreId");
        File file = fileStoreService.fetch(signedFileStoreId, WaterTaxConstants.FILESTORE_MODULECODE);
        byte[] bFile;
        try {
            bFile = FileUtils.readFileToByteArray(file);
        } catch (final IOException e) {
            throw new ApplicationRuntimeException("Exception while generating work order for New Connection : " + e);
        }
        ReportOutput reportOutput = new ReportOutput();
        reportOutput.setReportOutputData(bFile);
        reportOutput.setReportFormat(FileFormat.PDF);
        
        final HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.parseMediaType("application/pdf"));
        headers.add("content-disposition", "inline;filename=WorkOrderConnection.pdf");
        return new ResponseEntity<byte[]>(reportOutput.getReportOutputData(), headers, HttpStatus.CREATED);
    }
}
