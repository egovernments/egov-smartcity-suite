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

package org.egov.council.web.controller;

import org.apache.log4j.Logger;
import org.egov.commons.service.EducationalQualificationService;
import org.egov.council.entity.CouncilMember;
import org.egov.council.entity.CouncilMemberStatus;
import org.egov.council.service.CouncilCasteService;
import org.egov.council.service.CouncilDesignationService;
import org.egov.council.service.CouncilMemberService;
import org.egov.council.service.CouncilPartyService;
import org.egov.council.service.es.CouncilMemberIndexService;
import org.egov.council.utils.constants.CouncilConstants;
import org.egov.council.web.adaptor.CouncilMemberJsonAdaptor;
import org.egov.infra.admin.master.service.BoundaryService;
import org.egov.infra.filestore.service.FileStoreService;
import org.egov.infra.utils.FileStoreUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.MessageSource;
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

import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;
import java.io.IOException;
import java.util.List;

import static org.egov.infra.utils.JsonUtils.toJSON;

@Controller
@RequestMapping("/councilmember")
public class CouncilMemberController {
    private static final String COUNCIL_MEMBER = "councilMember";
    private static final String COUNCILMEMBER_NEW = "councilmember-new";
    private static final String COUNCILMEMBER_RESULT = "councilmember-result";
    private static final String COUNCILMEMBER_EDIT = "councilmember-edit";
    private static final String COUNCILMEMBER_VIEW = "councilmember-view";
    private static final String COUNCILMEMBER_SEARCH = "councilmember-search";
    private static final String MODULE_NAME = "COUNCIL";
    private static final Logger LOGGER = Logger.getLogger(CouncilMemberController.class);
    @Qualifier("fileStoreService")
    @Autowired
    protected FileStoreService fileStoreService;
    @Autowired
    protected FileStoreUtils fileStoreUtils;
    @Autowired
    private CouncilMemberService councilMemberService;
    @Autowired
    private MessageSource messageSource;
    @Autowired
    private BoundaryService boundaryService;
    @Autowired
    private CouncilDesignationService councilDesignationService;
    @Autowired
    private EducationalQualificationService educationalQualificationService;
    @Autowired
    private CouncilCasteService councilCasteService;
    @Autowired
    private CouncilPartyService councilPartyService;
    @Autowired
    private CouncilMemberIndexService councilMemberIndexService;
    @Autowired
    private CouncilMemberValidator councilMemberValidator;

    private void prepareNewForm(final Model model) {
        model.addAttribute("boundarys",
                boundaryService.getActiveBoundariesByBndryTypeNameAndHierarchyTypeName("Ward", "ADMINISTRATION"));// GET
        // ELECTION
        model.addAttribute("councilDesignations", councilDesignationService.getActiveDesignations());                                                                                                      // WARD.
        model.addAttribute("councilQualifications", educationalQualificationService.getActiveQualifications());
        model.addAttribute("councilCastes", councilCasteService.getActiveCastes());
        model.addAttribute("councilPartys", councilPartyService.getActiveParties());
        model.addAttribute("category", CouncilConstants.CATEGORY);
    }

    @RequestMapping(value = "/new", method = RequestMethod.GET)
    public String newForm(final Model model) {
        prepareNewForm(model);
        model.addAttribute("state", "NEW");
        model.addAttribute(COUNCIL_MEMBER, new CouncilMember());
        return COUNCILMEMBER_NEW;
    }

    @RequestMapping(value = "/create", method = RequestMethod.POST)
    public String create(@Valid @ModelAttribute final CouncilMember councilMember, final BindingResult errors,
                         @RequestParam final MultipartFile attachments, final Model model,
                         final RedirectAttributes redirectAttrs) {
        councilMemberValidator.validate(councilMember, errors);
        if (errors.hasErrors()) {
            prepareNewForm(model);
            return COUNCILMEMBER_NEW;
        }
        if (councilMember != null && councilMember.getStatus() == null)
            councilMember.setStatus(CouncilMemberStatus.ACTIVE);

        if (attachments.getSize() > 0) {
            try {
                councilMember.setPhoto(fileStoreService.store(attachments.getInputStream(),
                        attachments.getOriginalFilename(), attachments.getContentType(), MODULE_NAME));
            } catch (IOException e) {
                LOGGER.error("Error in loading Employee photo" + e.getMessage(), e);
            }
        }
        councilMemberService.create(councilMember);
        councilMemberIndexService.createCouncilMemberIndex(councilMember);
        redirectAttrs.addFlashAttribute("message", messageSource.getMessage("msg.councilMember.success", null, null));
        return "redirect:/councilmember/result/" + councilMember.getId();
    }

    @RequestMapping(value = "/edit/{id}", method = RequestMethod.GET)
    public String edit(@PathVariable("id") final Long id, final Model model, final HttpServletResponse response)
            throws IOException {
        CouncilMember councilMember = councilMemberService.findOne(id);
        prepareNewForm(model);
        model.addAttribute(COUNCIL_MEMBER, councilMember);

        return COUNCILMEMBER_EDIT;
    }

    @RequestMapping(value = "/update", method = RequestMethod.POST)
    public String update(@Valid @ModelAttribute final CouncilMember councilMember, @RequestParam final MultipartFile attachments,
                         final BindingResult errors,
                         final Model model, final RedirectAttributes redirectAttrs) {
        councilMemberValidator.validate(councilMember, errors);
        if (errors.hasErrors()) {
            prepareNewForm(model);
            return COUNCILMEMBER_EDIT;
        }
        if (attachments.getSize() > 0) {
            try {
                councilMember.setPhoto(fileStoreService.store(attachments.getInputStream(),
                        attachments.getOriginalFilename(), attachments.getContentType(), MODULE_NAME));
            } catch (IOException e) {
                LOGGER.error("Error in loading Employee photo" + e.getMessage(), e);
            }
        }
        councilMemberService.update(councilMember);
        councilMemberIndexService.createCouncilMemberIndex(councilMember);
        redirectAttrs.addFlashAttribute("message", messageSource.getMessage("msg.councilMember.success", null, null));
        return "redirect:/councilmember/result/" + councilMember.getId();
    }

    @RequestMapping(value = "/view/{id}", method = RequestMethod.GET)
    public String view(@PathVariable("id") final Long id, Model model) {
        CouncilMember councilMember = councilMemberService.findOne(id);
        prepareNewForm(model);
        model.addAttribute(COUNCIL_MEMBER, councilMember);

        return COUNCILMEMBER_VIEW;
    }

    @RequestMapping(value = "/result/{id}", method = RequestMethod.GET)
    public String result(@PathVariable("id") final Long id, Model model) {
        CouncilMember councilMember = councilMemberService.findOne(id);
        model.addAttribute(COUNCIL_MEMBER, councilMember);
        return COUNCILMEMBER_RESULT;
    }

    @RequestMapping(value = "/search/{mode}", method = RequestMethod.GET)
    public String search(@PathVariable("mode") final String mode, Model model) {
        CouncilMember councilMember = new CouncilMember();
        prepareNewForm(model);
        model.addAttribute(COUNCIL_MEMBER, councilMember);
        return COUNCILMEMBER_SEARCH;

    }

    @RequestMapping(value = "/ajaxsearch/{mode}", method = RequestMethod.POST, produces = MediaType.TEXT_PLAIN_VALUE)
    @ResponseBody
    public String ajaxsearch(@PathVariable("mode") final String mode, Model model,
                             @ModelAttribute final CouncilMember councilMember) {
        List<CouncilMember> searchResultList = councilMemberService.search(councilMember);
        return new StringBuilder("{ \"data\":")
                .append(toJSON(searchResultList, CouncilMember.class, CouncilMemberJsonAdaptor.class)).append("}")
                .toString();
    }

    @RequestMapping(value = "/downloadfile/{fileStoreId}")
    @ResponseBody
    public ResponseEntity download(@PathVariable final String fileStoreId) {
        return fileStoreUtils.fileAsResponseEntity(fileStoreId, MODULE_NAME, false);
    }

}