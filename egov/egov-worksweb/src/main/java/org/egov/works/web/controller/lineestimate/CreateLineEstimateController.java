/*
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
 *         1) All versions of this program, verbatim or modified must carry this
 *            Legal Notice.
 *
 *         2) Any misrepresentation of the origin of the material is prohibited. It
 *            is required that all modified versions of this material be marked in
 *            reasonable ways as different from the original version.
 *
 *         3) This license does not grant any rights to any user of the program
 *            with regards to rights under trademark law for use of the trade names
 *            or trademarks of eGovernments Foundation.
 *
 *   In case of any queries, you can reach eGovernments Foundation at contact@egovernments.org.
 */
package org.egov.works.web.controller.lineestimate;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.List;

import javax.servlet.ServletContext;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.egov.commons.CChartOfAccountDetail;
import org.egov.commons.dao.EgwStatusHibernateDAO;
import org.egov.commons.dao.FunctionHibernateDAO;
import org.egov.commons.dao.FundHibernateDAO;
import org.egov.commons.service.TypeOfWorkService;
import org.egov.dao.budget.BudgetGroupDAO;
import org.egov.egf.budget.model.BudgetControlType;
import org.egov.egf.budget.service.BudgetControlTypeService;
import org.egov.eis.web.contract.WorkflowContainer;
import org.egov.eis.web.controller.workflow.GenericWorkFlowController;
import org.egov.infra.admin.master.entity.Department;
import org.egov.infra.admin.master.service.BoundaryService;
import org.egov.infra.admin.master.service.CityService;
import org.egov.infra.exception.ApplicationException;
import org.egov.infra.filestore.service.FileStoreService;
import org.egov.infra.security.utils.SecurityUtils;
import org.egov.infra.utils.ApplicationConstant;
import org.egov.services.masters.SchemeService;
import org.egov.works.lineestimate.entity.DocumentDetails;
import org.egov.works.lineestimate.entity.EstimateAppropriation;
import org.egov.works.lineestimate.entity.LineEstimate;
import org.egov.works.lineestimate.entity.LineEstimateDetails;
import org.egov.works.lineestimate.entity.enums.Beneficiary;
import org.egov.works.lineestimate.entity.enums.LineEstimateStatus;
import org.egov.works.lineestimate.entity.enums.WorkCategory;
import org.egov.works.lineestimate.service.EstimateAppropriationService;
import org.egov.works.lineestimate.service.LineEstimateService;
import org.egov.works.masters.service.LineEstimateUOMService;
import org.egov.works.masters.service.ModeOfAllotmentService;
import org.egov.works.masters.service.NatureOfWorkService;
import org.egov.works.utils.WorksConstants;
import org.egov.works.utils.WorksUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.MessageSource;
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
@RequestMapping(value = "/lineestimate")
public class CreateLineEstimateController extends GenericWorkFlowController {

    private static final int BUFFER_SIZE = 4096;

    @Autowired
    private LineEstimateService lineEstimateService;

    @Autowired
    private FundHibernateDAO fundHibernateDAO;

    @Autowired
    @Qualifier("schemeService")
    private SchemeService schemeService;

    @Autowired
    private FileStoreService fileStoreService;

    @Autowired
    private WorksUtils worksUtils;

    @Autowired
    private NatureOfWorkService natureOfWorkService;

    @Autowired
    @Qualifier("messageSource")
    private MessageSource messageSource;

    @Autowired
    private EgwStatusHibernateDAO egwStatusHibernateDAO;

    @Autowired
    private SecurityUtils securityUtils;

    @Autowired
    private EstimateAppropriationService estimateAppropriationService;

    @Autowired
    private BoundaryService boundaryService;

    @Autowired
    private ModeOfAllotmentService modeOfAllotmentService;

    @Autowired
    private LineEstimateUOMService lineEstimateUOMService;

    @Autowired
    private BudgetControlTypeService budgetControlTypeService;

    @Autowired
    private TypeOfWorkService typeOfWorkService;

    @Autowired
    private CityService cityService;

    @Autowired
    private FunctionHibernateDAO functionHibernateDAO;

    @Autowired
    private BudgetGroupDAO budgetGroupDAO;

    @RequestMapping(value = "/newform", method = RequestMethod.GET)
    public String showNewLineEstimateForm(@ModelAttribute("lineEstimate") final LineEstimate lineEstimate,
            final Model model) throws ApplicationException {
        model.addAttribute("hiddenfields", lineEstimateService.getLineEstimateHiddenFields());
        model.addAttribute("workdetailsadd",
                WorksConstants.YES.equalsIgnoreCase(lineEstimateService.getLineEstimateMultipleWorkDetailsAllowed())
                        ? Boolean.TRUE : Boolean.FALSE);
        setDropDownValues(model);
        model.addAttribute("lineEstimate", lineEstimate);

        final List<Department> departments = worksUtils.getUserDepartments(securityUtils.getCurrentUser());
        if (departments != null && !departments.isEmpty())
            lineEstimate.setExecutingDepartment(departments.get(0));
        if (lineEstimate.getState() != null && lineEstimate.getState().getNextAction() != null)
            model.addAttribute("nextAction", lineEstimate.getState().getNextAction());
        lineEstimate.setApprovalDepartment(worksUtils.getDefaultDepartmentId());

        model.addAttribute("stateType", lineEstimate.getClass().getSimpleName());
        model.addAttribute("documentDetails", lineEstimate.getDocumentDetails());
        lineEstimate.setTempLineEstimateDetails(lineEstimate.getLineEstimateDetails());
        model.addAttribute("additionalRule", cityService.cityDataAsMap().get(ApplicationConstant.CITY_CORP_GRADE_KEY));

        prepareWorkflow(model, lineEstimate, new WorkflowContainer());

        model.addAttribute("mode", null);
        lineEstimateService.loadModelValues(lineEstimate, model);

        return "newLineEstimate-form";
    }

    @RequestMapping(value = "/create", method = RequestMethod.POST)
    public String create(@ModelAttribute("lineEstimate") final LineEstimate lineEstimate,
            final RedirectAttributes redirectAttributes, final Model model, final BindingResult errors,
            @RequestParam("file") final MultipartFile[] files, final HttpServletRequest request,
            @RequestParam String workFlowAction) throws ApplicationException, IOException {
        setDropDownValues(model);
        validateLineEstimate(lineEstimate, errors);
        validateBudgetHead(lineEstimate, errors);
        lineEstimateService.validateLineEstimateDetails(lineEstimate, errors);

        if (errors.hasErrors()) {
            model.addAttribute("stateType", lineEstimate.getClass().getSimpleName());

            prepareWorkflow(model, lineEstimate, new WorkflowContainer());
            model.addAttribute("documentDetails", lineEstimate.getDocumentDetails());
            model.addAttribute("mode", null);
            model.addAttribute("approvalDesignation", request.getParameter("approvalDesignation"));
            model.addAttribute("approvalPosition", request.getParameter("approvalPosition"));
            model.addAttribute("additionalRule",
                    cityService.cityDataAsMap().get(ApplicationConstant.CITY_CORP_GRADE_KEY));
            model.addAttribute("hiddenfields", lineEstimateService.getLineEstimateHiddenFields());
            model.addAttribute("workdetailsadd",
                    WorksConstants.YES.equalsIgnoreCase(lineEstimateService.getLineEstimateMultipleWorkDetailsAllowed())
                            ? Boolean.TRUE : Boolean.FALSE);
            return "newLineEstimate-form";
        } else {

            if (lineEstimate.getState() == null)
                lineEstimate.setStatus(egwStatusHibernateDAO.getStatusByModuleAndCode(WorksConstants.MODULETYPE,
                        LineEstimateStatus.CREATED.toString()));

            Long approvalPosition = 0l;
            String approvalComment = "";
            String additionalRule = "";
            if (request.getParameter("approvalComent") != null)
                approvalComment = request.getParameter("approvalComent");
            if (request.getParameter("workFlowAction") != null)
                workFlowAction = request.getParameter("workFlowAction");
            if (request.getParameter("approvalPosition") != null && !request.getParameter("approvalPosition").isEmpty())
                approvalPosition = Long.valueOf(request.getParameter("approvalPosition"));
            if (request.getParameter("additionalRule") != null)
                additionalRule = request.getParameter("additionalRule");

            final LineEstimate newLineEstimate = lineEstimateService.create(lineEstimate, files, approvalPosition,
                    approvalComment, additionalRule, workFlowAction);
            model.addAttribute("lineEstimate", newLineEstimate);

            final String pathVars = worksUtils.getPathVars(newLineEstimate.getStatus(), newLineEstimate.getState(),
                    newLineEstimate.getId(), approvalPosition);

            return "redirect:/lineestimate/lineestimate-success?pathVars=" + pathVars;
        }
    }

    private void validateBudgetHead(final LineEstimate lineEstimate, final BindingResult errors) {
        if (lineEstimate.getBudgetHead() != null) {
            Boolean check = false;
            final List<CChartOfAccountDetail> accountDetails = new ArrayList<CChartOfAccountDetail>();
            accountDetails.addAll(lineEstimate.getBudgetHead().getMaxCode().getChartOfAccountDetails());
            for (final CChartOfAccountDetail detail : accountDetails)
                if (detail.getDetailTypeId() != null
                        && detail.getDetailTypeId().getName().equalsIgnoreCase(WorksConstants.PROJECTCODE))
                    check = true;
            if (!check)
                errors.reject("error.budgethead.validate", "error.budgethead.validate");

        }

    }

    private void setDropDownValues(final Model model) {
        model.addAttribute("funds", fundHibernateDAO.findAllActiveFunds());
        if (BudgetControlType.BudgetCheckOption.NONE.toString()
                .equalsIgnoreCase(budgetControlTypeService.getConfigValue())) {
            model.addAttribute("functions", functionHibernateDAO.getAllActiveFunctions());
            model.addAttribute("budgetHeads", budgetGroupDAO.getBudgetGroupList());
        }
        model.addAttribute("departments", worksUtils.getUserDepartments(securityUtils.getCurrentUser()));
        model.addAttribute("workCategory", WorkCategory.values());
        model.addAttribute("beneficiary", Beneficiary.values());
        model.addAttribute("modeOfAllotment", modeOfAllotmentService.findAll());
        model.addAttribute("lineEstimateUOMs", lineEstimateUOMService.findAll());
        model.addAttribute("typeOfWork",
                typeOfWorkService.getActiveTypeOfWorksByPartyType(WorksConstants.PARTY_TYPE_CONTRACTOR));
        model.addAttribute("natureOfWork", natureOfWorkService.findAll());
        model.addAttribute("locations", boundaryService.getActiveBoundariesByBndryTypeNameAndHierarchyTypeName(
                WorksConstants.LOCATION_BOUNDARYTYPE, WorksConstants.LOCATION_HIERARCHYTYPE));
        model.addAttribute("budgetControlType",budgetControlTypeService.getConfigValue());


    }

    @RequestMapping(value = "/downloadLineEstimateDoc", method = RequestMethod.GET)
    public void getLineEstimateDoc(final HttpServletRequest request, final HttpServletResponse response)
            throws IOException {
        final ServletContext context = request.getServletContext();
        final String fileStoreId = request.getParameter("fileStoreId");
        String fileName = "";
        final File downloadFile = fileStoreService.fetch(fileStoreId, WorksConstants.FILESTORE_MODULECODE);
        final FileInputStream inputStream = new FileInputStream(downloadFile);
        LineEstimate lineEstimate = lineEstimateService
                .getLineEstimateById(Long.parseLong(request.getParameter("lineEstimateId")));
        lineEstimate = getEstimateDocuments(lineEstimate);

        for (final DocumentDetails doc : lineEstimate.getDocumentDetails())
            if (doc.getFileStore().getFileStoreId().equalsIgnoreCase(fileStoreId))
                fileName = doc.getFileStore().getFileName();

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
        final String headerValue = String.format("attachment; filename=\"%s\"", fileName);
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

    private LineEstimate getEstimateDocuments(final LineEstimate lineEstimate) {
        List<DocumentDetails> documentDetailsList = new ArrayList<DocumentDetails>();
        documentDetailsList = worksUtils.findByObjectIdAndObjectType(lineEstimate.getId(),
                WorksConstants.MODULE_NAME_LINEESTIMATE);
        lineEstimate.setDocumentDetails(documentDetailsList);
        return lineEstimate;
    }

    @RequestMapping(value = "/lineestimate-success", method = RequestMethod.GET)
    public ModelAndView successView(@ModelAttribute LineEstimate lineEstimate, final HttpServletRequest request,
            final Model model, final ModelMap modelMap) {

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
            lineEstimate = lineEstimateService.getLineEstimateById(id);
        model.addAttribute("approverName", approverName);
        model.addAttribute("currentUserDesgn", currentUserDesgn);
        model.addAttribute("nextDesign", nextDesign);

        final String message = getMessageByStatus(lineEstimate, approverName, nextDesign);

        model.addAttribute("message", message);
        if (lineEstimate.getStatus().getCode().equals(LineEstimateStatus.ADMINISTRATIVE_SANCTIONED.toString())
                && !BudgetControlType.BudgetCheckOption.NONE.toString()
                        .equalsIgnoreCase(budgetControlTypeService.getConfigValue())) {
            final List<String> basMessages = new ArrayList<String>();
            Integer count = 1;
            for (final LineEstimateDetails led : lineEstimate.getLineEstimateDetails()) {
                final EstimateAppropriation lea = estimateAppropriationService
                        .findLatestByLineEstimateDetails(led);
                final String tempMessage = messageSource.getMessage("msg.lineestimatedetails.budgetsanction.success",
                        new String[] { count.toString(), led.getEstimateNumber(),
                                lea.getBudgetUsage().getAppropriationnumber() },
                        null);
                basMessages.add(tempMessage);
                count++;
            }
            model.addAttribute("basMessages", basMessages);
        }

        return new ModelAndView("lineestimate-success", "lineEstimate", lineEstimate);
    }

    private String getMessageByStatus(final LineEstimate lineEstimate, final String approverName,
            final String nextDesign) {
        String message = "";

        if (lineEstimate.getStatus().getCode().equals(LineEstimateStatus.CREATED.toString())
                && !lineEstimate.getState().getValue().equals(WorksConstants.WF_STATE_REJECTED))
            message = messageSource.getMessage("msg.lineestimate.create.success",
                    new String[] { approverName, nextDesign, lineEstimate.getLineEstimateNumber() }, null);
        if (lineEstimate.getStatus().getCode().equals(LineEstimateStatus.RESUBMITTED.toString())
                && !lineEstimate.getState().getValue().equals(WorksConstants.WF_STATE_REJECTED))
            message = messageSource.getMessage("msg.lineestimate.resubmit.success",
                    new String[] { approverName, nextDesign, lineEstimate.getLineEstimateNumber() }, null);
        else if (lineEstimate.getStatus().getCode().equals(LineEstimateStatus.CHECKED.toString()))
            message = messageSource.getMessage("msg.lineestimate.check.success",
                    new String[] { lineEstimate.getLineEstimateNumber(), approverName, nextDesign }, null);
        else if (lineEstimate.getStatus().getCode().equals(LineEstimateStatus.BUDGET_SANCTIONED.toString()))
            message = messageSource.getMessage("msg.lineestimate.budgetsanction.success",
                    new String[] { lineEstimate.getLineEstimateNumber(), approverName, nextDesign }, null);
        else if (lineEstimate.getStatus().getCode().equals(LineEstimateStatus.ADMINISTRATIVE_SANCTIONED.toString()))
            message = messageSource.getMessage("msg.lineestimate.adminsanction.success",
                    new String[] { lineEstimate.getLineEstimateNumber(), lineEstimate.getAdminSanctionNumber() }, null);
        else if (lineEstimate.getState().getValue().equals(WorksConstants.WF_STATE_REJECTED))
            message = messageSource.getMessage("msg.lineestimate.reject",
                    new String[] { lineEstimate.getLineEstimateNumber(), approverName, nextDesign }, null);
        else if (lineEstimate.getStatus().getCode().equals(LineEstimateStatus.CANCELLED.toString()))
            message = messageSource.getMessage("msg.lineestimate.cancel",
                    new String[] { lineEstimate.getLineEstimateNumber() }, null);
        else if (lineEstimate.getStatus().getCode().equals(LineEstimateStatus.TECHNICALLY_APPROVED.toString()))
            message = messageSource.getMessage("msg.lineestimate.technicalapprove.success",
                    new String[] { lineEstimate.getLineEstimateNumber(), approverName, nextDesign }, null);

        return message;
    }

    private void validateLineEstimate(final LineEstimate lineEstimate, final BindingResult errors) {
        if (!lineEstimateService.getLineEstimateHiddenFields().contains("subject") && lineEstimate.getSubject() == null)
            errors.reject("error.subject.required", "error.subject.required");
        if (!lineEstimateService.getLineEstimateHiddenFields().contains("description")
                && lineEstimate.getDescription() == null)
            errors.reject("error.description.required", "error.description.required");

    }
}
