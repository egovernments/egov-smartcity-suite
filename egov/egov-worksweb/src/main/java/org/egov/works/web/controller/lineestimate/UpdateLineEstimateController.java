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
package org.egov.works.web.controller.lineestimate;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;

import org.egov.commons.dao.EgwTypeOfWorkHibernateDAO;
import org.egov.commons.dao.FunctionHibernateDAO;
import org.egov.commons.dao.FundHibernateDAO;
import org.egov.dao.budget.BudgetGroupDAO;
import org.egov.infra.admin.master.service.BoundaryService;
import org.egov.infra.admin.master.service.DepartmentService;
import org.egov.infra.exception.ApplicationException;
import org.egov.services.masters.SchemeService;
import org.egov.works.lineestimate.entity.Beneficiary;
import org.egov.works.lineestimate.entity.DocumentDetails;
import org.egov.works.lineestimate.entity.LineEstimate;
import org.egov.works.lineestimate.entity.ModeOfAllotment;
import org.egov.works.lineestimate.entity.TypeOfSlum;
import org.egov.works.lineestimate.service.LineEstimateService;
import org.egov.works.master.services.NatureOfWorkService;
import org.egov.works.utils.WorksConstants;
import org.egov.works.utils.WorksUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
@RequestMapping(value = "/lineestimate")
public class UpdateLineEstimateController {

    @Autowired
    private LineEstimateService lineEstimateService;

    @Autowired
    private FundHibernateDAO fundHibernateDAO;

    @Autowired
    private FunctionHibernateDAO functionHibernateDAO;

    @Autowired
    private BudgetGroupDAO budgetGroupDAO;

    @Autowired
    private SchemeService schemeService;

    @Autowired
    private DepartmentService departmentService;

    @Autowired
    private WorksUtils worksUtils;

    @Autowired
    private NatureOfWorkService natureOfWorkService;

    @Autowired
    private EgwTypeOfWorkHibernateDAO egwTypeOfWorkHibernateDAO;

    @Autowired
    private BoundaryService boundaryService;

    @ModelAttribute
    public LineEstimate getLineEstimate(@PathVariable final String lineEstimateId) {
        final LineEstimate lineEstimate = lineEstimateService.getLineEstimateById(Long.parseLong(lineEstimateId));
        return lineEstimate;
    }

    @RequestMapping(value = "/update/{lineEstimateId}", method = RequestMethod.GET)
    public String viewLineEstimate(final Model model, @PathVariable final String lineEstimateId,
            final HttpServletRequest request)
            throws ApplicationException {
        setDropDownValues(model);
        final LineEstimate lineEstimate = getLineEstimate(lineEstimateId);
        model.addAttribute("message", WorksConstants.LINEESTIMATE_CREATE);
        return loadViewData(model, request, lineEstimate);
    }

    @RequestMapping(value = "/update/{lineEstimateId}", method = RequestMethod.POST)
    public String update(@Valid @ModelAttribute("lineEstimate") final LineEstimate lineEstimate, final BindingResult errors,
            final RedirectAttributes redirectAttributes, final Model model, final HttpServletRequest request,
            @RequestParam final String removedLineEstimateDetailsIds, @RequestParam("file") final MultipartFile[] files)
            throws ApplicationException, IOException {
        setDropDownValues(model);
        if (errors.hasErrors())
            return loadViewData(model, request, lineEstimate);
        else {
            final LineEstimate newLineEstimate = lineEstimateService.update(lineEstimate, removedLineEstimateDetailsIds, files);
            setDropDownValues(model);
            redirectAttributes.addFlashAttribute("lineEstimate", newLineEstimate);
            redirectAttributes.addAttribute("message", WorksConstants.LINEESTIMATE_UPDATE);
            return "redirect:/lineestimate/update/" + lineEstimate.getId();
        }
    }

    private void setDropDownValues(final Model model) {
        model.addAttribute("funds", fundHibernateDAO.findAllActiveFunds());
        model.addAttribute("functions", functionHibernateDAO.getAllActiveFunctions());
        model.addAttribute("budgetHeads", budgetGroupDAO.getBudgetGroupList());
        model.addAttribute("schemes", schemeService.findAll());
        model.addAttribute("departments", departmentService.getAllDepartments());
        model.addAttribute("typeOfSlum", TypeOfSlum.values());
        model.addAttribute("beneficiary", Beneficiary.values());
        model.addAttribute("modeOfAllotment", ModeOfAllotment.values());
        model.addAttribute("typeOfWork", egwTypeOfWorkHibernateDAO.getAllParentOrderByCode());
        model.addAttribute("natureOfWork", natureOfWorkService.findAll());
    }

    private String loadViewData(final Model model, final HttpServletRequest request,
            final LineEstimate lineEstimate) {
        final LineEstimate newLineEstimate = getEstimateDocuments(lineEstimate);
        model.addAttribute("lineEstimate", newLineEstimate);
        if (request != null && request.getParameter("message") != null && request.getParameter("message").equals("update"))
            model.addAttribute("message", WorksConstants.LINEESTIMATE_UPDATE);
        return "newLineEstimate-edit";
    }

    private LineEstimate getEstimateDocuments(final LineEstimate lineEstimate) {
        List<DocumentDetails> documentDetailsList = new ArrayList<DocumentDetails>();
        documentDetailsList = worksUtils.findByObjectIdAndObjectType(lineEstimate.getId(),
                WorksConstants.MODULE_NAME_LINEESTIMATE);
        lineEstimate.setDocumentDetails(documentDetailsList);
        return lineEstimate;
    }
}
