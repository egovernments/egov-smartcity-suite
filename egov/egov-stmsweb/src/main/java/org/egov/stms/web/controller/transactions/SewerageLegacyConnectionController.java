/**
 * eGov suite of products aim to improve the internal efficiency,transparency,
   accountability and the service delivery of the government  organizations.

    Copyright (C) <2015>  eGovernments Foundation

    The updated version of eGov suite of products as by eGovernments Foundation
    is available at http://www.egovernments.org

    This program is free software: you can redistribute it and/or modify
    it under the terms of the GNU General Public License as published by
    the Free Software Foundation, either version 3 of the License, or
    any later version.

    This program is distributed in the hope that it will be useful,
    but WITHOUT ANY WARRANTY; without even the implied warranty of
    MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
    GNU General Public License for more details.

    You should have received a copy of the GNU General Public License
    along with this program. If not, see http://www.gnu.org/licenses/ or
    http://www.gnu.org/licenses/gpl.html .

    In addition to the terms of the GPL license to be adhered to in using this
    program, the following additional terms are to be complied with:

        1) All versions of this program, verbatim or modified must carry this
           Legal Notice.

        2) Any misrepresentation of the origin of the material is prohibited. It
           is required that all modified versions of this material be marked in
           reasonable ways as different from the original version.

        3) This license does not grant any rights to any user of the program
           with regards to rights under trademark law for use of the trade names
           or trademarks of eGovernments Foundation.

  In case of any queries, you can reach eGovernments Foundation at contact@egovernments.org.
 */
package org.egov.stms.web.controller.transactions;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;

import org.egov.eis.service.AssignmentService;
import org.egov.eis.web.controller.workflow.GenericWorkFlowController;
import org.egov.infra.filestore.service.FileStoreService;
import org.egov.infra.security.utils.SecurityUtils;
import org.egov.stms.masters.entity.DocumentTypeMaster;
import org.egov.stms.masters.entity.FeesDetailMaster;
import org.egov.stms.masters.entity.SewerageApplicationType;
import org.egov.stms.masters.entity.enums.PropertyType;
import org.egov.stms.masters.entity.enums.SewerageConnectionStatus;
import org.egov.stms.masters.service.DocumentTypeMasterService;
import org.egov.stms.masters.service.FeesDetailMasterService;
import org.egov.stms.masters.service.SewerageApplicationTypeService;
import org.egov.stms.transactions.entity.SewerageApplicationDetails;
import org.egov.stms.transactions.entity.SewerageApplicationDetailsDocument;
import org.egov.stms.transactions.entity.SewerageConnection;
import org.egov.stms.transactions.entity.SewerageConnectionFee;
import org.egov.stms.transactions.service.SewerageApplicationDetailsService;
import org.egov.stms.transactions.service.SewerageConnectionService;
import org.egov.stms.utils.SewerageTaxUtils;
import org.egov.stms.utils.constants.SewerageTaxConstants;
import org.egov.stms.web.controller.utils.SewerageApplicationValidator;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.ui.ModelMap;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
@RequestMapping(value = "/transactions")
public class SewerageLegacyConnectionController extends GenericWorkFlowController {

    private static final Logger LOG = LoggerFactory.getLogger(SewerageLegacyConnectionController.class);
    private final SewerageTaxUtils sewerageTaxUtils;

    private final SewerageApplicationDetailsService sewerageApplicationDetailsService;

    @Autowired
    private SewerageConnectionService sewerageConnectionService;

    @Autowired
    private SewerageApplicationTypeService sewerageApplicationTypeService;

    @Autowired
    private SecurityUtils securityUtils;

    @Autowired
    protected AssignmentService assignmentService;

    @Autowired
    private FeesDetailMasterService feesDetailMasterService;

    @Autowired
    @Qualifier("fileStoreService")
    protected FileStoreService fileStoreService;

    @Autowired
    private DocumentTypeMasterService documentTypeMasterService;

    @Autowired
    private SewerageApplicationValidator sewerageApplicationValidator;

    @Autowired
    public SewerageLegacyConnectionController(final SewerageTaxUtils sewerageTaxUtils,
            final SewerageApplicationDetailsService sewerageApplicationDetailsService) {
        this.sewerageTaxUtils = sewerageTaxUtils;
        this.sewerageApplicationDetailsService = sewerageApplicationDetailsService;
    }

    @ModelAttribute("documentNamesList")
    public List<SewerageApplicationDetailsDocument> documentTypeMasterList(
            @ModelAttribute final SewerageApplicationDetails sewerageApplicationDetails) {
        final List<SewerageApplicationDetailsDocument> tempDocList = new ArrayList<>(
                0);
        final SewerageApplicationType applicationType = sewerageApplicationTypeService
                .findByCode(SewerageTaxConstants.NEWSEWERAGECONNECTION);
        final List<DocumentTypeMaster> documentTypeMasterList = documentTypeMasterService
                .getAllActiveDocumentTypeMasterByApplicationType(applicationType);
        if (sewerageApplicationDetails != null)
            for (final DocumentTypeMaster dtm : documentTypeMasterList) {
                final SewerageApplicationDetailsDocument sad = new SewerageApplicationDetailsDocument();
                if (dtm != null && dtm.getDescription().equalsIgnoreCase(SewerageTaxConstants.DOCTYPE_OTHERS)) {
                    sad.setDocumentTypeMaster(dtm);
                    tempDocList.add(sad);
                }
            }
        return tempDocList;
    }

    @RequestMapping(value = "/legacyConnection-newform", method = RequestMethod.GET)
    public String showLegacyApplicationForm(@ModelAttribute final SewerageApplicationDetails sewerageApplicationDetails,
            final Model model, final HttpServletRequest request) {
        LOG.debug("Inside showLegacyApplicationForm method");
        final SewerageApplicationType applicationType = sewerageApplicationTypeService
                .findByCode(SewerageTaxConstants.NEWSEWERAGECONNECTION);
        sewerageApplicationDetails.setApplicationType(applicationType);
        sewerageApplicationDetails.setApplicationDate(new Date());
        final SewerageConnection connection = new SewerageConnection();
        connection.setStatus(SewerageConnectionStatus.ACTIVE);
        sewerageApplicationDetails.setConnection(connection);
        model.addAttribute("propertyTypes", PropertyType.values());

        model.addAttribute("additionalRule", sewerageApplicationDetails.getApplicationType().getCode());
        model.addAttribute("currentUser", sewerageTaxUtils.getCurrentUserRole(securityUtils.getCurrentUser()));
        model.addAttribute("stateType", sewerageApplicationDetails.getClass().getSimpleName());
        model.addAttribute("typeOfConnection", SewerageTaxConstants.NEWSEWERAGECONNECTION);
        model.addAttribute("legacy", true);
        model.addAttribute("isDonationChargeCollectionRequired",sewerageTaxUtils.isDonationChargeCollectionRequiredForLegacy());
        createSewerageConnectionFee(sewerageApplicationDetails,
                SewerageTaxConstants.FEES_ESTIMATIONCHARGES_CODE);
        createSewerageConnectionFee(sewerageApplicationDetails,
                SewerageTaxConstants.FEES_SEWERAGETAX_CODE);
        model.addAttribute("mode", null);
        return "legacySewerageConnection-form";
    }

    private void createSewerageConnectionFee(final SewerageApplicationDetails sewerageApplicationDetails, final String feeCode) {
        final List<FeesDetailMaster> inspectionFeeList = feesDetailMasterService.findAllActiveFeesDetailByFeesCode(feeCode);
        for (final FeesDetailMaster feeDetailMaster : inspectionFeeList)
            if (!feeDetailMaster.getCode().equalsIgnoreCase(SewerageTaxConstants.FEES_SEWERAGETAX_CODE)) {
                final SewerageConnectionFee connectionFee = new SewerageConnectionFee();
                connectionFee.setFeesDetail(feeDetailMaster);
                if (feeDetailMaster.getIsFixedRate())
                    connectionFee.setAmount(feeDetailMaster.getAmount().doubleValue());
                connectionFee.setApplicationDetails(sewerageApplicationDetails);
                sewerageApplicationDetails.getConnectionFees().add(connectionFee);
            }
    }

    @RequestMapping(value = "/legacyConnection-create", method = RequestMethod.POST)
    public String create(@Valid @ModelAttribute final SewerageApplicationDetails sewerageApplicationDetails,
            final BindingResult resultBinder, final RedirectAttributes redirectAttributes,
            final HttpServletRequest request, final Model model,
            @RequestParam("files") final MultipartFile[] files) {

        final List<SewerageApplicationDetailsDocument> applicationDocs = new ArrayList<>();
        int i = 0;
        if (!sewerageApplicationDetails.getAppDetailsDocument().isEmpty())
            for (final SewerageApplicationDetailsDocument applicationDocument : sewerageApplicationDetails
                    .getAppDetailsDocument()) {
                sewerageConnectionService.validateDocuments(applicationDocs, applicationDocument, i, resultBinder);
                i++;
            }

        sewerageApplicationValidator.validateLegacyData(sewerageApplicationDetails, resultBinder, request);
        if (LOG.isDebugEnabled())
            LOG.error("Model Level Validation occurs = " + resultBinder);

        if (resultBinder.hasErrors()) {
            sewerageApplicationDetails.setApplicationDate(new Date());
            model.addAttribute("sewerageApplicationDetails", sewerageApplicationDetails);
            model.addAttribute("demandDetailList", sewerageApplicationDetails.getDemandDetailBeanList());
            model.addAttribute("propertyTypes", PropertyType.values());
            model.addAttribute("executionDate", sewerageApplicationDetails.getConnection().getExecutionDate());
            return "legacySewerageConnection-form";
        }

        sewerageApplicationDetails.setStatus(sewerageTaxUtils.getStatusByCodeAndModuleType(
                SewerageTaxConstants.APPLICATION_STATUS_SANCTIONED, SewerageTaxConstants.MODULETYPE));
        sewerageApplicationDetails.getAppDetailsDocument().clear();
        sewerageApplicationDetails.setAppDetailsDocument(applicationDocs);
        sewerageConnectionService.processAndStoreApplicationDocuments(sewerageApplicationDetails);
        populateFeesDetails(sewerageApplicationDetails);
        final SewerageApplicationDetails newSewerageApplicationDetails = sewerageApplicationDetailsService.createLegacySewerageConnection(sewerageApplicationDetails, request);
        final String pathVars = newSewerageApplicationDetails.getApplicationNumber();
        return "redirect:/transactions/sewerageLegacyApplication-success?pathVars=" + pathVars;
    }

    private void populateFeesDetails(final SewerageApplicationDetails sewerageApplicationDetails) {
        if (sewerageApplicationDetails.getConnectionFees() != null
                && !sewerageApplicationDetails.getConnectionFees().isEmpty())
            for (final SewerageConnectionFee scf : sewerageApplicationDetails.getConnectionFees())
                scf.setApplicationDetails(sewerageApplicationDetails);
        // Create Sewerage Tax connection fee entry for current installment
        final FeesDetailMaster sewerageTax = feesDetailMasterService.findByCodeAndIsActive(
                SewerageTaxConstants.FEES_SEWERAGETAX_CODE, true);
        if (sewerageTax != null && !sewerageApplicationDetails.getDemandDetailBeanList().isEmpty()) {
            final SewerageConnectionFee connectionFee = new SewerageConnectionFee();
            connectionFee.setFeesDetail(sewerageTax);
            // Last entry in demanddetailbean is always current installment
            connectionFee.setAmount(sewerageApplicationDetails.getDemandDetailBeanList()
                    .get(sewerageApplicationDetails.getDemandDetailBeanList().size() - 1).getActualAmount().doubleValue());
            connectionFee.setApplicationDetails(sewerageApplicationDetails);
            sewerageApplicationDetails.getConnectionFees().add(connectionFee);
        }
    }

    @RequestMapping(value = "/sewerageLegacyApplication-success", method = RequestMethod.GET)
    public ModelAndView successView(@ModelAttribute final SewerageApplicationDetails sewerageApplicationDetails,
            final HttpServletRequest request, final Model model, final ModelMap modelMap) {
        SewerageApplicationDetails applicationDetails = null;
        final String[] keyNameArray = request.getParameter("pathVars").split(",");
        String applicationNumber = "";
        if (keyNameArray.length != 0 && keyNameArray.length > 0 && keyNameArray.length == 1)
            applicationNumber = keyNameArray[0];

        if (applicationNumber != null)
            applicationDetails = sewerageApplicationDetailsService.findByApplicationNumber(applicationNumber);

        return new ModelAndView("sewerageLegacyApplication-success", "sewerageApplicationDetails", applicationDetails);
    }

}