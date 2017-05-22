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

import java.math.BigDecimal;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Set;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;

import org.egov.commons.Installment;
import org.egov.demand.model.BillReceipt;
import org.egov.demand.model.EgDemandDetails;
import org.egov.demand.model.EgDemandReason;
import org.egov.eis.service.AssignmentService;
import org.egov.eis.web.controller.workflow.GenericWorkFlowController;
import org.egov.infra.exception.ApplicationRuntimeException;
import org.egov.infra.filestore.service.FileStoreService;
import org.egov.infra.security.utils.SecurityUtils;
import org.egov.ptis.constants.PropertyTaxConstants;
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
import org.egov.stms.transactions.entity.SewerageDemandDetail;
import org.egov.stms.transactions.service.SewerageApplicationDetailsService;
import org.egov.stms.transactions.service.SewerageConnectionService;
import org.egov.stms.transactions.service.SewerageDemandService;
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
import org.springframework.web.bind.annotation.PathVariable;
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
    private static final String MESSAGE = "message";
    private static final String COMMON_ERROR_PAGE = "common-error";
    private static final String SEWERAGEAPPLICATIONDETAILS = "sewerageApplicationDetails";
    private static final String PROPERTYTYPES =  "propertyTypes";

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
    private SewerageDemandService sewerageDemandService;

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
        model.addAttribute(PROPERTYTYPES, PropertyType.values());

        model.addAttribute("additionalRule", sewerageApplicationDetails.getApplicationType().getCode());
        model.addAttribute("currentUser", sewerageTaxUtils.getCurrentUserRole(securityUtils.getCurrentUser()));
        model.addAttribute("stateType", sewerageApplicationDetails.getClass().getSimpleName());
        model.addAttribute("typeOfConnection", SewerageTaxConstants.NEWSEWERAGECONNECTION);
        model.addAttribute("legacy", true);
        model.addAttribute("isDonationChargeCollectionRequired", sewerageTaxUtils.isDonationChargeCollectionRequiredForLegacy());
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
            model.addAttribute(SEWERAGEAPPLICATIONDETAILS, sewerageApplicationDetails);
            model.addAttribute("demandDetailList", sewerageApplicationDetails.getDemandDetailBeanList());
            model.addAttribute(PROPERTYTYPES, PropertyType.values());
            model.addAttribute("executionDate", sewerageApplicationDetails.getConnection().getExecutionDate());
            return "legacySewerageConnection-form";
        }

        sewerageApplicationDetails.setStatus(sewerageTaxUtils.getStatusByCodeAndModuleType(
                SewerageTaxConstants.APPLICATION_STATUS_SANCTIONED, SewerageTaxConstants.MODULETYPE));
        sewerageApplicationDetails.getAppDetailsDocument().clear();
        sewerageApplicationDetails.setAppDetailsDocument(applicationDocs);
        sewerageConnectionService.processAndStoreApplicationDocuments(sewerageApplicationDetails);
        populateFeesDetails(sewerageApplicationDetails);
        final SewerageApplicationDetails newSewerageApplicationDetails = sewerageApplicationDetailsService
                .createLegacySewerageConnection(sewerageApplicationDetails, request);
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

        return new ModelAndView("sewerageLegacyApplication-success", SEWERAGEAPPLICATIONDETAILS, applicationDetails);
    }

    @RequestMapping(value = "/sewerage/sewerageLegacyApplication-updateForm/{consumernumber}/{assessmentnumber}", method = RequestMethod.GET)
    public ModelAndView updateLegacy(@PathVariable final String consumernumber, @PathVariable final String assessmentnumber,
            final Model model, final ModelMap modelMap,
            final HttpServletRequest request) {
        SewerageApplicationDetails sewerageApplicationDetails = null;
        if (consumernumber != null)
            sewerageApplicationDetails = sewerageApplicationDetailsService.findByApplicationNumber(consumernumber);

        if (sewerageApplicationDetails != null) {
            List<BillReceipt> sewerageReceipts = sewerageDemandService
                    .getBilReceiptsByDemand(sewerageApplicationDetails.getCurrentDemand());
            if (sewerageReceipts != null && sewerageReceipts.isEmpty()) {
                model.addAttribute(MESSAGE, "msg.validate.modification.notallowed");
                return new ModelAndView(COMMON_ERROR_PAGE, SEWERAGEAPPLICATIONDETAILS, sewerageApplicationDetails);
            }
        }
        model.addAttribute(PROPERTYTYPES, PropertyType.values());
        model.addAttribute("legacy", true);
        model.addAttribute("isDonationChargeCollectionRequired", sewerageTaxUtils.isDonationChargeCollectionRequiredForLegacy());
        model.addAttribute("demandDetailList", loadDemandDetails(sewerageApplicationDetails));
        return new ModelAndView("edit-legacySewerageConnection-form", SEWERAGEAPPLICATIONDETAILS, sewerageApplicationDetails);

    }

    @RequestMapping(value = "/sewerageLegacyApplication-update", method = RequestMethod.POST)
    public String updateLegacyConnection(
            SewerageApplicationDetails sewerageApplicationDetails, final BindingResult errors,
            final RedirectAttributes redirectAttributes, final BindingResult resultBinder,
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
        sewerageApplicationDetails.getAppDetailsDocument().clear();
        sewerageApplicationDetails.setAppDetailsDocument(applicationDocs);
        sewerageConnectionService.processAndStoreApplicationDocuments(sewerageApplicationDetails);

        final String pathVars = sewerageApplicationDetails.getApplicationNumber();
        sewerageApplicationDetailsService.updateLegacySewerageConnection(sewerageApplicationDetails, request);

        return "redirect:/transactions/sewerageLegacyApplication-success?pathVars=" + pathVars;

    }

    private List<SewerageDemandDetail> loadDemandDetails(
            final SewerageApplicationDetails sewerageApplicationDetails) {
        final List<SewerageDemandDetail> demandDetailBeanList = new ArrayList<>();
        final Set<SewerageDemandDetail> tempDemandDetail = new LinkedHashSet<>();
        List<Installment> allInstallments = new ArrayList<>();
        final DateFormat dateFormat = new SimpleDateFormat(PropertyTaxConstants.DATE_FORMAT_DDMMYYY);
        try {

            allInstallments = sewerageTaxUtils
                    .getInstallmentsForCurrYear(
                            dateFormat.parse(dateFormat.format(sewerageApplicationDetails.getConnection().getExecutionDate())));

        } catch (final ParseException e) {
            throw new ApplicationRuntimeException("Error while getting all installments from start date", e);
        }
        SewerageDemandDetail dmdDtl = null;

        for (final Installment installObj : allInstallments) {
            if (installObj.getFromDate().compareTo(new Date()) < 0) {
                final EgDemandReason demandReasonObj = sewerageDemandService
                        .getDemandReasonByCodeAndInstallment(SewerageTaxConstants.FEES_SEWERAGETAX_CODE, installObj.getId());
                if (demandReasonObj != null) {
                    EgDemandDetails demanddet = null;
                    if (sewerageApplicationDetails.getCurrentDemand() != null)
                        demanddet = getDemandDetailsExist(sewerageApplicationDetails, demandReasonObj);
                    if (demanddet != null)
                        dmdDtl = createDemandDetailBean(installObj, demandReasonObj, demanddet.getAmount(),
                                demanddet.getAmtCollected(), demanddet.getId());
                    else
                        dmdDtl = createDemandDetailBean(installObj, demandReasonObj, BigDecimal.ZERO, BigDecimal.ZERO, null);

                }
                tempDemandDetail.add(dmdDtl);
            }
        }
        for (final SewerageDemandDetail demandDetList : tempDemandDetail)
            if (demandDetList != null)
                demandDetailBeanList.add(demandDetList);

        return demandDetailBeanList;
    }

    private EgDemandDetails getDemandDetailsExist(final SewerageApplicationDetails sewerageApplicationDetails,
            final EgDemandReason demandReasonObj) {
        EgDemandDetails demandDet = null;
        for (final EgDemandDetails dd : sewerageApplicationDetails.getCurrentDemand()
                .getEgDemandDetails())
            if (dd.getEgDemandReason().equals(demandReasonObj)) {
                demandDet = dd;
                break;
            }
        return demandDet;

    }

    private SewerageDemandDetail createDemandDetailBean(final Installment installment, final EgDemandReason demandReasonObj,
            final BigDecimal amount, final BigDecimal amountCollected, final Long demanddetailId) {
        final SewerageDemandDetail demandDetail = new SewerageDemandDetail();
        demandDetail.setInstallment(installment.getDescription());
        demandDetail.setReasonMaster(demandReasonObj.getEgDemandReasonMaster().getCode());
        demandDetail.setInstallmentId(installment.getId());
        demandDetail.setDemandReasonId(demanddetailId);
        demandDetail.setActualAmount(amount);
        demandDetail.setActualCollection(amountCollected);
        demandDetail.setReasonMasterDesc(demandReasonObj.getEgDemandReasonMaster().getReasonMaster());
        return demandDetail;
    }

}