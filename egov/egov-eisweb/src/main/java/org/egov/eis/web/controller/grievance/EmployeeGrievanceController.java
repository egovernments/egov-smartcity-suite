/*
 *    eGov  SmartCity eGovernance suite aims to improve the internal efficiency,transparency,
 *    accountability and the service delivery of the government  organizations.
 *
 *     Copyright (C) 2017  eGovernments Foundation
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
 *            Further, all user interfaces, including but not limited to citizen facing interfaces,
 *            Urban Local Bodies interfaces, dashboards, mobile applications, of the program and any
 *            derived works should carry eGovernments Foundation logo on the top right corner.
 *
 *            For the logo, please refer http://egovernments.org/html/logo/egov_logo.png.
 *            For any further queries on attribution, including queries on brand guidelines,
 *            please contact contact@egovernments.org
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
 *
 */
package org.egov.eis.web.controller.grievance;

import java.io.IOException;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.apache.log4j.Logger;
import org.egov.eis.autonumber.EmployeeGrievanceNumberGenerator;
import org.egov.eis.entity.EmployeeGrievance;
import org.egov.eis.entity.enums.EmployeeGrievanceStatus;
import org.egov.eis.service.EmployeeGrievanceService;
import org.egov.eis.service.EmployeeGrievanceTypeService;
import org.egov.eis.service.EmployeeService;
import org.egov.eis.utils.EisUtils;
import org.egov.eis.web.adaptor.EmployeeGrievanceJsonAdaptor;
import org.egov.infra.filestore.entity.FileStoreMapper;
import org.egov.infra.filestore.service.FileStoreService;
import org.egov.infra.security.utils.SecurityUtils;
import org.egov.infra.utils.FileStoreUtils;
import org.egov.infra.utils.StringUtils;
import org.egov.infra.utils.autonumber.AutonumberServiceBeanResolver;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.MessageSource;
import org.springframework.core.io.InputStreamResource;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

@Controller
@RequestMapping("/employeegrievance")
public class EmployeeGrievanceController {
    private static final String EMPLOYEE_GRIEVANCE = "employeeGrievance";
    private static final String EMPLOYEEGRIEVANCE_NEW = "employeegrievance-new";
    private static final String EMPLOYEEGRIEVANCE_RESULT = "employeegrievance-result";
    private static final String EMPLOYEEGRIEVANCE_EDIT = "employeegrievance-edit";
    private static final String EMPLOYEEGRIEVANCE_VIEW = "employeegrievance-view";
    private static final String EMPLOYEEGRIEVANCE_SEARCH = "employeegrievance-search";
    private static final Logger LOGGER = Logger
            .getLogger(EmployeeGrievanceController.class);
    @Autowired
    private EmployeeGrievanceService employeeGrievanceService;
    @Autowired
    private MessageSource messageSource;
    @Autowired
    private EmployeeGrievanceTypeService employeeGrievanceTypeService;
    @Autowired
    private EmployeeService employeeService;
    @Autowired
    private SecurityUtils securityUtils;
    @Qualifier("fileStoreService")
    @Autowired
    protected FileStoreService fileStoreService;
    @Autowired
    private AutonumberServiceBeanResolver autonumberServiceBeanResolver;
    @Autowired
    protected FileStoreUtils fileStoreUtils;
    @Autowired
    private EisUtils eisUtils;
    
    private void prepareNewForm(Model model) {
        model.addAttribute("employeeGrievanceTypes", employeeGrievanceTypeService.findAll());

    }

    @RequestMapping(value = "/new", method = RequestMethod.GET)
    public String newForm(final Model model) {
        prepareNewForm(model);
        EmployeeGrievance employeeGrievance = new EmployeeGrievance();
        model.addAttribute("stateType", employeeGrievance.getClass().getSimpleName());
        employeeGrievance.setEmployee(employeeService.getEmployeeById(securityUtils.getCurrentUser().getId()));
        model.addAttribute(EMPLOYEE_GRIEVANCE, employeeGrievance);
        return EMPLOYEEGRIEVANCE_NEW;
    }

    @RequestMapping(value = "/create", method = RequestMethod.POST)
    public String create(@ModelAttribute final EmployeeGrievance employeeGrievance, final BindingResult errors,
            final Model model, final RedirectAttributes redirectAttrs, @RequestParam("file") final MultipartFile[] files) {
        validateGrievanceData(employeeGrievance, errors);
        if (errors.hasErrors()) {
            prepareNewForm(model);
            return EMPLOYEEGRIEVANCE_NEW;
        }
        if (files != null && files.length > 0) {
            try {

                Set<FileStoreMapper> fileStore = new HashSet<>();
                for (MultipartFile file : files)
                    if (!StringUtils.EMPTY.equals(file.getOriginalFilename())) {
                        FileStoreMapper fileStoreMapper = fileStoreService.store(
                                file.getInputStream(),
                                file.getOriginalFilename(),
                                file.getContentType(),
                                "EIS");
                        fileStore.add(fileStoreMapper);
                    }

                employeeGrievance.setGrievanceDocs(fileStore);
            } catch (IOException e) {
                LOGGER.error("Error in loading documents" + e.getMessage(), e);
            }
        }
        employeeGrievance.setStatus(EmployeeGrievanceStatus.REGISTERED);
        EmployeeGrievanceNumberGenerator employeeGrievancenumbergenerator = autonumberServiceBeanResolver
                .getAutoNumberServiceFor(EmployeeGrievanceNumberGenerator.class);
        employeeGrievance.setGrievanceNumber(employeeGrievancenumbergenerator
                .getNextNumber(employeeGrievance));
        employeeGrievanceService.prepareWorkFlowTransition(employeeGrievance);
        EmployeeGrievance savedEmployeeGrievance = employeeGrievanceService.create(employeeGrievance);
        return "redirect:/employeegrievance/result/" + savedEmployeeGrievance.getId();
    }

    @RequestMapping(value = "/edit/{id}", method = RequestMethod.GET)
    public String edit(@PathVariable("id") final Long id, Model model) {
        EmployeeGrievance employeeGrievance = employeeGrievanceService.findOne(id);
        prepareNewForm(model);
        model.addAttribute(EMPLOYEE_GRIEVANCE, employeeGrievance);
        model.addAttribute("employeeGrievanceStatus", EmployeeGrievanceStatus.values());
        return EMPLOYEEGRIEVANCE_EDIT;
    }

    @RequestMapping(value = "/update", method = RequestMethod.POST)
    public String update(@ModelAttribute final EmployeeGrievance employeeGrievance, final BindingResult errors,
            final Model model, final RedirectAttributes redirectAttrs) {
        validateGrievanceData(employeeGrievance, errors);
        if (errors.hasErrors()) {
            model.addAttribute("employeeGrievanceStatus", EmployeeGrievanceStatus.values());
            prepareNewForm(model);
            return EMPLOYEEGRIEVANCE_EDIT;
        }
        employeeGrievanceService.prepareWorkFlowTransition(employeeGrievance);
        employeeGrievanceService.update(employeeGrievance);
        redirectAttrs.addFlashAttribute("message", messageSource.getMessage("msg.employeegrievance.success", null, null));

        return "redirect:/employeegrievance/result/" + employeeGrievance.getId();
    }

    @RequestMapping(value = "/view/{id}", method = RequestMethod.GET)
    public String view(@PathVariable("id") final Long id, Model model) {
        EmployeeGrievance employeeGrievance = employeeGrievanceService.findOne(id);
        prepareNewForm(model);
        model.addAttribute(EMPLOYEE_GRIEVANCE, employeeGrievance);
        return EMPLOYEEGRIEVANCE_VIEW;
    }

    @RequestMapping(value = "/result/{id}", method = RequestMethod.GET)
    public String result(@PathVariable("id") final Long id, Model model) {
        EmployeeGrievance employeeGrievance = employeeGrievanceService.findOne(id);
        final String message = getMessageByStatus(employeeGrievance);
        model.addAttribute("message", message);
        model.addAttribute(EMPLOYEE_GRIEVANCE, employeeGrievance);

        return EMPLOYEEGRIEVANCE_RESULT;
    }

    @RequestMapping(value = "/search/{mode}", method = RequestMethod.GET)
    public String search(@PathVariable("mode") final String mode, Model model) {
        EmployeeGrievance employeeGrievance = new EmployeeGrievance();
        prepareNewForm(model);
        model.addAttribute(EMPLOYEE_GRIEVANCE, employeeGrievance);
        return EMPLOYEEGRIEVANCE_SEARCH;

    }

    @RequestMapping(value = "/ajaxsearch/{mode}", method = RequestMethod.POST, produces = MediaType.TEXT_PLAIN_VALUE)
    @ResponseBody
    public String ajaxsearch(@PathVariable("mode") final String mode, Model model,
            @ModelAttribute final EmployeeGrievance employeeGrievance) {
        List<EmployeeGrievance> searchResultList = employeeGrievanceService.search(employeeGrievance);
        return new StringBuilder("{ \"data\":").append(toSearchResultJson(searchResultList)).append("}").toString();
    }

    public Object toSearchResultJson(final Object object) {
        final GsonBuilder gsonBuilder = new GsonBuilder();
        final Gson gson = gsonBuilder.registerTypeAdapter(EmployeeGrievance.class, new EmployeeGrievanceJsonAdaptor()).create();
        return gson.toJson(object);
    }

    @RequestMapping(value = "/downloadfile/{fileStoreId}")
    @ResponseBody
    public ResponseEntity<InputStreamResource> download(@PathVariable final String fileStoreId) {
        return fileStoreUtils.fileAsResponseEntity(fileStoreId,
                "EIS", false);
    }

    private String getMessageByStatus(final EmployeeGrievance employeeGrievance) {
        String message = "";
        String ownerName = employeeGrievanceService.getApproverName(employeeGrievance.getState().getOwnerPosition().getId());
        if (employeeGrievance.getStatus().equals(EmployeeGrievanceStatus.REGISTERED)) {

            message = messageSource.getMessage("msg.employeegrievance.success",
                    new String[] { employeeGrievance.getGrievanceNumber(), ownerName }, null);

        } else if (employeeGrievance.getStatus().equals(EmployeeGrievanceStatus.INPROCESS)) {
            message = messageSource.getMessage("msg.employeegrievance.inprocess",
                    new String[] { employeeGrievance.getGrievanceNumber(), ownerName }, null);
        } else if (employeeGrievance.getStatus().equals(EmployeeGrievanceStatus.REDRESSED)) {
            message = messageSource.getMessage("msg.employeegrievance.redressed",
                    new String[] { employeeGrievance.getGrievanceNumber() }, null);
        } else if (employeeGrievance.getStatus().equals(EmployeeGrievanceStatus.REJECTED)) {
            message = messageSource.getMessage("msg.employeegrievance.rejected",
                    new String[] { employeeGrievance.getGrievanceNumber() }, null);
        }

        return message;
    }
    
    private void validateGrievanceData(final EmployeeGrievance employeeGrievance, final BindingResult errors) {

        if (StringUtils.isNotBlank(employeeGrievance.getDetails())
                && eisUtils.hasHtmlTags(employeeGrievance.getDetails())) {
            errors.rejectValue("details", "invalid.input");
        } else if (StringUtils.isNotBlank(employeeGrievance.getGrievanceResolution())
                && eisUtils.hasHtmlTags(employeeGrievance.getGrievanceResolution())) {
            errors.rejectValue("grievanceResolution", "invalid.input");
        }
    }

}
