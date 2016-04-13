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
package org.egov.works.web.controller.contractorbill;

import java.io.IOException;
import java.math.BigDecimal;
import java.util.Date;
import java.util.List;

import javax.servlet.http.HttpServletRequest;

import org.apache.commons.lang3.StringUtils;
import org.egov.commons.CChartOfAccounts;
import org.egov.commons.dao.ChartOfAccountsHibernateDAO;
import org.egov.eis.web.contract.WorkflowContainer;
import org.egov.eis.web.controller.workflow.GenericWorkFlowController;
import org.egov.works.contractorbill.entity.ContractorBillRegister;
import org.egov.works.contractorbill.entity.enums.BillTypes;
import org.egov.works.contractorbill.service.ContractorBillNumberGenerator;
import org.egov.works.contractorbill.service.ContractorBillRegisterService;
import org.egov.works.letterofacceptance.service.LetterOfAcceptanceService;
import org.egov.works.lineestimate.entity.LineEstimateDetails;
import org.egov.works.lineestimate.service.LineEstimateService;
import org.egov.works.models.workorder.WorkOrder;
import org.egov.works.utils.WorksConstants;
import org.egov.works.utils.WorksUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.support.ResourceBundleMessageSource;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;

@Controller
@RequestMapping(value = "/contractorbill")
public class CreateContractorBillController extends GenericWorkFlowController{

    @Autowired
    private LineEstimateService lineEstimateService;

    @Autowired
    private LetterOfAcceptanceService letterOfAcceptanceService;

    @Autowired
    private ContractorBillRegisterService contractorBillRegisterService;

    @Autowired
    private ContractorBillNumberGenerator contractorBillNumberGenerator;
    
    @Autowired
    private WorksUtils worksUtils;
    
    @Autowired
    private ResourceBundleMessageSource messageSource;

    @Autowired
    private ChartOfAccountsHibernateDAO chartOfAccountsHibernateDAO;

    @RequestMapping(value = "/newform", method = RequestMethod.GET)
    public String showNewForm(
            @ModelAttribute("contractorBillRegister") final ContractorBillRegister contractorBillRegister,
            final Model model, final HttpServletRequest request) {
        final String loaNumber = request.getParameter("loaNumber");
        final WorkOrder workOrder = letterOfAcceptanceService.getApprovedWorkOrder(loaNumber);
        final LineEstimateDetails lineEstimateDetails = lineEstimateService.findByEstimateNumber(workOrder.getEstimateNumber());
        setDropDownValues(model);
        
        model.addAttribute("stateType", contractorBillRegister.getClass().getSimpleName());

        prepareWorkflow(model, contractorBillRegister, new WorkflowContainer());

        model.addAttribute("mode", "edit");
        
        contractorBillRegister.setBilldate(new Date());
        // TODO:Fixme - Hardcoded 26 as purposeId for Creditors-Contractor Payable for time being
        final List<CChartOfAccounts> contractorPayableAccountList = chartOfAccountsHibernateDAO.getAccountCodeByPurpose(26);
        model.addAttribute("netPayableAccounCodes", contractorPayableAccountList);
        model.addAttribute("workOrder", workOrder);
        model.addAttribute("lineEstimateDetails", lineEstimateDetails);
        model.addAttribute("contractorBillRegister", contractorBillRegister);
        return "contractorBill-form";
    }

    private void setDropDownValues(final Model model) {
        model.addAttribute("billTypes", BillTypes.values());
    }

    @RequestMapping(value = "/contractorbill-save", method = RequestMethod.POST)
    public String create(@ModelAttribute("contractorBillRegister") final ContractorBillRegister contractorBillRegister,
            final Model model, final BindingResult resultBinder, final HttpServletRequest request,
            @RequestParam String workFlowAction, @RequestParam("file") final MultipartFile[] files) throws IOException {
        
        final String loaNumber = request.getParameter("loaNumber");
        final WorkOrder workOrder = letterOfAcceptanceService.getApprovedWorkOrder(loaNumber);
        final LineEstimateDetails lineEstimateDetails = lineEstimateService.findByEstimateNumber(workOrder.getEstimateNumber());
        contractorBillRegister.setWorkOrder(workOrder);

        validateInput(contractorBillRegister, resultBinder);

        if (resultBinder.hasErrors()) {
            setDropDownValues(model);
            model.addAttribute("lineEstimateDetails", lineEstimateDetails);
            model.addAttribute("workOrder", workOrder);
            
            model.addAttribute("stateType", contractorBillRegister.getClass().getSimpleName());

            prepareWorkflow(model, contractorBillRegister, new WorkflowContainer());

            model.addAttribute("mode", "edit");
            
            return "contractorBill-form";
        } else {
            
            Long approvalPosition = 0l;
            String approvalComment = "";
            if (request.getParameter("approvalComment") != null)
                approvalComment = request.getParameter("approvalComent");
            if (request.getParameter("workFlowAction") != null)
                workFlowAction = request.getParameter("workFlowAction");
            if (request.getParameter("approvalPosition") != null && !request.getParameter("approvalPosition").isEmpty())
                approvalPosition = Long.valueOf(request.getParameter("approvalPosition"));

            Integer partBillCount = contractorBillRegisterService
                    .getMaxSequenceNumberByWorkOrder(workOrder);
            if (partBillCount == null || partBillCount == 0)
                partBillCount = 1;
            else
                partBillCount++;
            contractorBillRegister.setBillSequenceNumber(partBillCount);
            contractorBillRegister.setBillnumber(
                    contractorBillNumberGenerator.generateContractorBillNumber(contractorBillRegister));

            // TODO:Fixme Replace with proper bill value from UI
            contractorBillRegister.setBillamount(new BigDecimal(workOrder.getWorkOrderAmount()));
            contractorBillRegister.setBillamount(contractorBillRegister.getBillamount());

            final ContractorBillRegister savedContractorBillRegister = contractorBillRegisterService
                    .create(contractorBillRegister, lineEstimateDetails, files, approvalPosition,
                            approvalComment, null, workFlowAction);
            
            final String pathVars = worksUtils.getPathVars(savedContractorBillRegister.getStatus(),
                    savedContractorBillRegister.getState(), savedContractorBillRegister.getId(), approvalPosition);
            
            return "redirect:/contractorbill/contractorbill-success?pathVars=" + pathVars + "&billNumber=" + savedContractorBillRegister.getBillnumber();
        }
    }

    @RequestMapping(value = "/contractorbill-success", method = RequestMethod.GET)
    public String showContractorBillSuccessPage(@RequestParam("billNumber") final String billNumber, final Model model,
            final HttpServletRequest request) {
        
        final String[] keyNameArray = request.getParameter("pathVars").split(",");
        Long id = 0L;
        String approverName = "";
        String currentUserDesgn = "";
        String nextDesign = "";
        if (keyNameArray.length != 0 && keyNameArray.length > 0)
            if (keyNameArray.length == 1)
                id = Long.parseLong(keyNameArray[0]);
            else if (keyNameArray.length == 3) {
                id = Long.parseLong(keyNameArray[0]);
                approverName = keyNameArray[1];
                currentUserDesgn = keyNameArray[2];
            } else {
                id = Long.parseLong(keyNameArray[0]);
                approverName = keyNameArray[1];
                currentUserDesgn = keyNameArray[2];
                nextDesign = keyNameArray[3];
            }

        if (id != null)
        model.addAttribute("approverName", approverName);
        model.addAttribute("currentUserDesgn", currentUserDesgn);
        model.addAttribute("nextDesign", nextDesign);
        
        final ContractorBillRegister contractorBillRegister = contractorBillRegisterService
                .getContractorBillByBillNumber(billNumber);
        
        final String message = getMessageByStatus(contractorBillRegister, approverName, nextDesign);
        
        model.addAttribute("message", message);
        
        model.addAttribute("contractorBillRegister", contractorBillRegister);
        return "contractorBill-success";
    }

    private void validateInput(final ContractorBillRegister contractorBillRegister, final BindingResult resultBinder) {
        if (StringUtils.isBlank(contractorBillRegister.getBilltype()))
            resultBinder.rejectValue("billtype", "error.billtype.required");
        if (contractorBillRegister.getEgBillregistermis() != null
                && contractorBillRegister.getEgBillregistermis().getPartyBillDate() != null
                && contractorBillRegister.getEgBillregistermis().getPartyBillDate()
                        .before(contractorBillRegister.getWorkOrder().getWorkOrderDate()))
            resultBinder.rejectValue("egBillregistermis.partyBillDate", "error.validate.partybilldate.lessthan.loadate");

    }
    
    private String getMessageByStatus(final ContractorBillRegister contractorBillRegister, final String approverName, final String nextDesign) {
        String message = "";

        if (contractorBillRegister.getStatus().getCode().equals(ContractorBillRegister.BillStatus.CREATED.toString())
                && !contractorBillRegister.getState().getValue().equals(WorksConstants.WF_STATE_REJECTED))
            message = messageSource.getMessage("msg.contractorbill.create.success",
                    new String[] { contractorBillRegister.getBillnumber(), approverName, nextDesign }, null);
        else if (contractorBillRegister.getStatus().getCode().equals(ContractorBillRegister.BillStatus.APPROVED.toString()))
            message = messageSource.getMessage("msg.contractorbill.approved.success",
                    new String[] { contractorBillRegister.getBillnumber() }, null);
        else if (contractorBillRegister.getState().getValue().equals(WorksConstants.WF_STATE_REJECTED))
            message = messageSource.getMessage("msg.contractorbill.reject",
                    new String[] { contractorBillRegister.getBillnumber(), approverName, nextDesign }, null);
        else if (contractorBillRegister.getState().getValue().equals(WorksConstants.WF_STATE_CANCELLED))
            message = messageSource.getMessage("msg.contractorbill.cancel",
                    new String[] { contractorBillRegister.getBillnumber() }, null);

        return message;
    }

}
