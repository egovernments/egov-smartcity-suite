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

import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.egov.infra.exception.ApplicationRuntimeException;
import org.egov.infra.filestore.entity.FileStoreMapper;
import org.egov.infra.filestore.repository.FileStoreMapperRepository;
import org.egov.infra.filestore.service.FileStoreService;
import org.egov.infra.reporting.engine.ReportOutput;
import org.egov.infra.security.utils.SecurityUtils;
import org.egov.infra.utils.EgovThreadLocals;
import org.egov.infra.workflow.entity.StateAware;
import org.egov.infra.workflow.entity.WorkflowTypes;
import org.egov.infra.workflow.inbox.InboxRenderServiceDeligate;
import org.egov.ptis.domain.model.AssessmentDetails;
import org.egov.ptis.domain.model.OwnerName;
import org.egov.ptis.domain.service.property.PropertyExternalService;
import org.egov.wtms.application.entity.WaterConnectionDetails;
import org.egov.wtms.application.service.ReportGenerationService;
import org.egov.wtms.application.service.WaterConnectionDetailsService;
import org.egov.wtms.masters.service.UsageTypeService;
import org.egov.wtms.utils.PropertyExtnUtils;
import org.egov.wtms.utils.WaterTaxNumberGenerator;
import org.egov.wtms.utils.constants.WaterTaxConstants;
import org.hibernate.Session;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

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
    
    @Autowired
    private InboxRenderServiceDeligate<StateAware> inboxRenderServiceDeligate;
    
    @Autowired
    private SecurityUtils securityUtils;
     
    @Autowired
    private WaterTaxNumberGenerator waterTaxNumberGenerator;
    
    @Autowired
    protected UsageTypeService usageTypeService;
    
    @Autowired
    private PropertyExtnUtils propertyExtnUtils;
    
    @Autowired
    private ReportGenerationService reportGenerationService;
    
    @Autowired
    private FileStoreMapperRepository fileStoreMapperRepository;
    
    @RequestMapping(value = "/waterTax/transitionWorkflow")
    public String transitionWorkflow(final HttpServletRequest request, final Model model) {
        final String fileStoreIds = request.getParameter("fileStoreId");
        final String[] fileStoreIdArr = fileStoreIds.split(",");
        HttpSession session = request.getSession();
        String sourceChannel = request.getParameter("Source");
        Long approvalPosition = (Long)session.getAttribute(WaterTaxConstants.APPROVAL_POSITION);
        String approvalComent = (String)session.getAttribute(WaterTaxConstants.APPROVAL_COMMENT);
        Map<String, String> appNoFileStoreIdsMap = (Map<String, String>)session.getAttribute(WaterTaxConstants.FILE_STORE_ID_APPLICATION_NUMBER);
        WaterConnectionDetails waterConnectionDetails = null;
        for(String fileStoreId : fileStoreIdArr) {
            String applicationNumber = appNoFileStoreIdsMap.get(fileStoreId);
            if(null != applicationNumber && !applicationNumber.isEmpty()) {
                waterConnectionDetails = waterConnectionDetailsService.findByApplicationNumber(applicationNumber);
                if(null == approvalPosition) {
                    String additionalRule = waterConnectionDetails.getApplicationType().getCode();
                    if(additionalRule.equalsIgnoreCase(WaterTaxConstants.CLOSINGCONNECTION)) {
                        additionalRule = WaterTaxConstants.CLOSECONNECTION;
                    }
                    approvalPosition = waterConnectionDetailsService.getApprovalPositionByMatrixDesignation(
                            waterConnectionDetails, approvalPosition, additionalRule, "", WaterTaxConstants.SIGNWORKFLOWACTION);
                }
                waterConnectionDetailsService.updateWaterConnection(waterConnectionDetails, approvalPosition,
                        approvalComent, waterConnectionDetails.getApplicationType().getCode(), WaterTaxConstants.SIGNWORKFLOWACTION, "",
                        null,sourceChannel);
            }
        }
        model.addAttribute("successMessage", "Digitally Signed Successfully");
        model.addAttribute("fileStoreId", fileStoreIdArr.length == 1 ? fileStoreIdArr[0] : "");
        return "digitalSignature-success";
    }
    
    @RequestMapping(value = "/waterTax/downloadSignedWorkOrderConnection")
    public void downloadSignedWorkOrderConnection(final HttpServletRequest request, final HttpServletResponse response) {
        String signedFileStoreId = request.getParameter("signedFileStoreId");
        File file = fileStoreService.fetch(signedFileStoreId, WaterTaxConstants.FILESTORE_MODULECODE);
        final FileStoreMapper fileStoreMapper = fileStoreMapperRepository.findByFileStoreId(signedFileStoreId);
        response.setContentType("application/pdf");  
        response.setContentType("application/octet-stream");
        response.setHeader("content-disposition", "attachment; filename=\"" + fileStoreMapper.getFileName() + "\"");
        try{
            FileInputStream inStream = new FileInputStream(file);
            PrintWriter outStream = response.getWriter();
            int bytesRead = -1;
            while ((bytesRead = inStream.read()) != -1) {
                outStream.write(bytesRead);
            }
            inStream.close();
            outStream.close(); 
        } catch(FileNotFoundException fileNotFoundExcep) {
            throw new ApplicationRuntimeException("Exception while loading file : " + fileNotFoundExcep);
        } catch(final IOException ioExcep) {
            throw new ApplicationRuntimeException("Exception while generating work order : " + ioExcep);
        }
    }

    @RequestMapping(value = "/digitalSignaturePending-form", method = RequestMethod.GET)
    public String searchForm(final HttpServletRequest request, final Model model) {
        String cityMunicipalityName = (String)request.getSession().getAttribute("citymunicipalityname");
        String districtName = (String)request.getSession().getAttribute("districtName");
        final List<HashMap<String, Object>> resultList = getRecordsForDigitalSignature();
        model.addAttribute("digitalSignatureReportList", resultList);
        model.addAttribute("noticeType", WaterTaxConstants.NOTICE_TYPE_SPECIAL_NOTICE);
        model.addAttribute("cityMunicipalityName", cityMunicipalityName);
        model.addAttribute("districtName", districtName);
        return "digitalSignaturePending-form";
    }
    
    @RequestMapping(value = "/waterTax/signWorkOrder", method = RequestMethod.POST)
    public String signWorkOrder(final HttpServletRequest request, final Model model) {
        WaterConnectionDetails waterConnectionDetails = null;
        String[] applicationNumbers = null;
        String[] appNumberConTypePair = null;
        Map<String, String> fileStoreIdsApplicationNoMap = new HashMap<String, String>();
        StringBuffer fileStoreIds = new StringBuffer();
        String pathVar = request.getParameter("pathVar");
        String applicationNoStatePair = request.getParameter("applicationNoStatePair");
        String currentState = request.getParameter("currentState");
        String signAll = request.getParameter("signAll");
        String fileName = "";
        if(pathVar != null) {
            applicationNumbers = request.getParameter("pathVar").split(",");
            if(applicationNoStatePair != null) {
                appNumberConTypePair = applicationNoStatePair.split(",");
            }
            for(int i = 0; i < applicationNumbers.length; i++) {
                waterConnectionDetails = waterConnectionDetailsService.findByApplicationNumber(applicationNumbers[i]);
                String cityMunicipalityName = (String) request.getSession().getAttribute("citymunicipalityname");
                String districtName = (String) request.getSession().getAttribute("districtName");
                waterConnectionDetails.setWorkOrderDate(new Date());
                waterConnectionDetails.setWorkOrderNumber(waterTaxNumberGenerator.generateWorkOrderNumber());
                ReportOutput reportOutput = null;
                if(signAll != null && signAll.equalsIgnoreCase(WaterTaxConstants.SIGN_ALL)) {
                    for(int j = 0; j < appNumberConTypePair.length; j++) {
                        String[] appNoStatePair = appNumberConTypePair[j].split(":");
                        if(applicationNumbers[i].equalsIgnoreCase(appNoStatePair[0])) {
                            currentState = appNoStatePair[1];
                        }
                    }
                }
                if(currentState.equalsIgnoreCase(WaterTaxConstants.CLOSECONNECTION)) {
                    reportOutput = reportGenerationService.generateClosureConnectionReport(waterConnectionDetails,
                            WaterTaxConstants.SIGNWORKFLOWACTION, cityMunicipalityName, districtName);
                    fileName = WaterTaxConstants.SIGNED_DOCUMENT_PREFIX + waterConnectionDetails.getApplicationNumber() + ".pdf";
                } else if(currentState.equalsIgnoreCase(WaterTaxConstants.RECONNECTIONCONNECTION)) {
                    reportOutput = reportGenerationService.generateReconnectionReport(waterConnectionDetails,
                            WaterTaxConstants.SIGNWORKFLOWACTION, cityMunicipalityName, districtName);
                    fileName = WaterTaxConstants.SIGNED_DOCUMENT_PREFIX + waterConnectionDetails.getApplicationNumber() + ".pdf";
                } else {
                    reportOutput = reportGenerationService.getReportOutput(waterConnectionDetails,
                            WaterTaxConstants.SIGNWORKFLOWACTION, cityMunicipalityName, districtName);
                    fileName = WaterTaxConstants.SIGNED_DOCUMENT_PREFIX + waterConnectionDetails.getWorkOrderNumber() + ".pdf";
                }
                //Setting FileStoreMap object while Commissioner Signs the document   
                if(reportOutput != null) {
                    InputStream fileStream = new ByteArrayInputStream(reportOutput.getReportOutputData());
                    final FileStoreMapper fileStore = fileStoreService.store(fileStream, fileName, "application/pdf",
                            WaterTaxConstants.FILESTORE_MODULECODE);
                    waterConnectionDetails.setFileStore(fileStore);
                    waterConnectionDetails = waterConnectionDetailsService.updateWaterConnectionDetailsWithFileStore(waterConnectionDetails);
                    fileStoreIdsApplicationNoMap.put(waterConnectionDetails.getFileStore().getFileStoreId(), applicationNumbers[i]);
                    fileStoreIds.append(waterConnectionDetails.getFileStore().getFileStoreId());
                    if(i < applicationNumbers.length - 1) {
                        fileStoreIds.append(",");
                    }
                }
            }
            request.getSession().setAttribute(WaterTaxConstants.FILE_STORE_ID_APPLICATION_NUMBER, fileStoreIdsApplicationNoMap);
            model.addAttribute("fileStoreIds", fileStoreIds.toString());
            model.addAttribute("ulbCode", EgovThreadLocals.getCityCode());
        }
        return "newConnection-digitalSignatureRedirection";
    }
    
    @SuppressWarnings("unchecked")
    public List<HashMap<String, Object>> getRecordsForDigitalSignature() {
        final List<HashMap<String, Object>> resultList = new ArrayList<HashMap<String, Object>>();
        final List<StateAware> stateAwareList = fetchItems();
        WaterConnectionDetails waterConnectionDetails = null;
        if (null != stateAwareList && !stateAwareList.isEmpty()) {
            HashMap<String, Object> tempMap = new HashMap<String, Object>();
            WorkflowTypes workflowTypes = null;
            List<WorkflowTypes> workflowTypesList = new ArrayList<WorkflowTypes>();
            for (final StateAware record : stateAwareList)
                if (record != null)
                    if (record.getState() != null && record.getState().getNextAction() != null && 
                    record.getState().getNextAction().equalsIgnoreCase(WaterTaxConstants.DIGITAL_SIGNATURE_PENDING)) {
                        tempMap = new HashMap<String, Object>();
                        workflowTypesList = getCurrentSession().getNamedQuery(WorkflowTypes.WF_TYPE_BY_TYPE_AND_RENDER_Y)
                                .setString(0, record.getStateType()).list();
                        
                        if (workflowTypesList != null && !workflowTypesList.isEmpty())
                            workflowTypes = workflowTypesList.get(0);
                        else
                            workflowTypes = null;
                        if (WaterTaxConstants.MODULE_NAME.equalsIgnoreCase(workflowTypes.getModule().getName())) {
                            waterConnectionDetails = (WaterConnectionDetails)record;
                            tempMap.put("objectId", ((WaterConnectionDetails)record).getApplicationNumber());
                            tempMap.put("type", record.getState().getNatureOfTask());
                            tempMap.put("module", workflowTypes != null ? workflowTypes.getModule().getDisplayName() : null);
                            tempMap.put("details", record.getStateDetails());
                            tempMap.put("hscNumber", waterConnectionDetails.getConnection().getConsumerCode());
                            tempMap.put("status", record.getCurrentState().getValue());
                            tempMap.put("applicationNumber", waterConnectionDetails.getApplicationNumber());
                            tempMap.put("waterConnectionDetails", waterConnectionDetails);
                            tempMap.put("ownerName", getOwnerName(waterConnectionDetails));
                            tempMap.put("propertyAddress", getPropertyAddress(waterConnectionDetails));
                            String additionalRule = waterConnectionDetails.getApplicationType().getCode();
                            if(additionalRule.equals("CLOSINGCONNECTION")) {
                                additionalRule = WaterTaxConstants.CLOSECONNECTION;
                            }
                            tempMap.put("state", additionalRule);
                            resultList.add(tempMap);
                        }
                    }
        }
        return resultList;
    }
    
    public List<StateAware> fetchItems() {
        final List<StateAware> digitalSignWFItems = new ArrayList<StateAware>();
        digitalSignWFItems.addAll(inboxRenderServiceDeligate.getInboxItems(securityUtils.getCurrentUser().getId()));
        return digitalSignWFItems;
    }
    
    private Session getCurrentSession() {
        return entityManager.unwrap(Session.class);
    }
    
    private String getOwnerName(final WaterConnectionDetails waterConnectionDetails) {
        String ownerName =  "";
        AssessmentDetails assessmentDetails = propertyExtnUtils.getAssessmentDetailsForFlag(
                waterConnectionDetails.getConnection().getPropertyIdentifier(),
                PropertyExternalService.FLAG_FULL_DETAILS);
        if(null != assessmentDetails && null != assessmentDetails.getOwnerNames()) {
            Iterator<OwnerName> ownerNameItr = assessmentDetails.getOwnerNames().iterator();
            if (ownerNameItr.hasNext()) {
                final OwnerName owner = ownerNameItr.next();
                ownerName = owner.getOwnerName() != null ? owner.getOwnerName() : ownerName;
            }
        }
        return ownerName;
    }
    
    private String getPropertyAddress(final WaterConnectionDetails waterConnectionDetails) {
        String propAddress =  "";
        AssessmentDetails assessmentDetails = propertyExtnUtils.getAssessmentDetailsForFlag(
                waterConnectionDetails.getConnection().getPropertyIdentifier(),
                PropertyExternalService.FLAG_FULL_DETAILS);
        if(null != assessmentDetails) {
            propAddress = assessmentDetails.getPropertyAddress() != null ? assessmentDetails.getPropertyAddress() : propAddress;
        }
        return propAddress;
    }
}
