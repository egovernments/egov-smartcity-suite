/*
 * eGov suite of products aim to improve the internal efficiency,transparency,
 *    accountability and the service delivery of the government  organizations.
 *
 *     Copyright (C) <2016>  eGovernments Foundation
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
package org.egov.council.web.controller;

import static org.egov.council.utils.constants.CouncilConstants.AGENDA_MODULENAME;
import static org.egov.council.utils.constants.CouncilConstants.AGENDA_STATUS_APPROVED;
import static org.egov.council.utils.constants.CouncilConstants.PREAMBLEUSEDINAGENDA;
import static org.egov.council.utils.constants.CouncilConstants.PREAMBLE_MODULENAME;
import static org.egov.council.utils.constants.CouncilConstants.PREAMBLE_STATUS_APPROVED;
import static org.egov.council.utils.constants.CouncilConstants.REVENUE_HIERARCHY_TYPE;
import static org.egov.council.utils.constants.CouncilConstants.WARD;
import static org.egov.infra.utils.JsonUtils.toJSON;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;

import org.egov.commons.dao.EgwStatusHibernateDAO;
import org.egov.council.autonumber.AgendaNumberGenerator;
import org.egov.council.entity.CommitteeType;
import org.egov.council.entity.CouncilAgenda;
import org.egov.council.entity.CouncilAgendaDetails;
import org.egov.council.entity.CouncilPreamble;
import org.egov.council.service.CommitteeTypeService;
import org.egov.council.service.CouncilAgendaService;
import org.egov.council.service.CouncilPreambleService;
import org.egov.council.web.adaptor.CouncilAgendaJsonAdaptor;
import org.egov.council.web.adaptor.CouncilPreambleJsonAdaptor;
import org.egov.infra.admin.master.entity.Boundary;
import org.egov.infra.admin.master.entity.Department;
import org.egov.infra.admin.master.service.BoundaryService;
import org.egov.infra.admin.master.service.DepartmentService;
import org.egov.infra.utils.autonumber.AutonumberServiceBeanResolver;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
@RequestMapping("/agenda")
public class CouncilAgendaController {

    private static final String DATA = "{\"data\":";
    private static final String COUNCIL_AGENDA = "councilAgenda";
    private static final String COUNCILAGENDA_NEW = "create-agenda";
    private static final String COUNCILAGENDA_RESULT = "agenda-result";
    private static final String COUNCILAGENDA_EDIT = "agenda-edit";
    private static final String COUNCILAGENDA_VIEW = "agenda-view";
    private static final String COUNCILAGENDA_SEARCH = "agenda-search";
    private static final String COUNCILAGENDA_SEARCH_APPROVED = "council-agendaSearch";

    @Autowired
    protected DepartmentService departmentService;

    @Autowired
    protected CommitteeTypeService committeeTypeService;

    @Autowired
    protected CouncilAgendaService councilAgendaService;

    @Autowired
    protected CouncilPreambleService councilPreambleService;

    @Autowired
    private EgwStatusHibernateDAO egwStatusHibernateDAO;

    @Autowired
    private AutonumberServiceBeanResolver autonumberServiceBeanResolver;

    @Autowired
    private MessageSource messageSource;

    @Autowired
    private BoundaryService boundaryService;

    @ModelAttribute("departments")
    public List<Department> getDepartmentList() {
        return departmentService.getAllDepartments();
    }

    @ModelAttribute("wards")
    public List<Boundary> getWardsList() {
        return boundaryService.getActiveBoundariesByBndryTypeNameAndHierarchyTypeName(WARD,
                REVENUE_HIERARCHY_TYPE);
    }

    @ModelAttribute("committeeType")
    public List<CommitteeType> getCommitteTypeList() {
        return committeeTypeService.getActiveCommiteeType();
    }

    @RequestMapping(value = "/new", method = RequestMethod.GET)
    public String newForm(final Model model) {
        model.addAttribute(COUNCIL_AGENDA, new CouncilAgenda());
        model.addAttribute("councilPreamble", new CouncilPreamble());
        return COUNCILAGENDA_NEW;
    }

    @RequestMapping(value = "/create", method = RequestMethod.POST)
    public String create(
            @Valid @ModelAttribute final CouncilAgenda councilAgenda,
            final BindingResult errors, final Model model,
            final RedirectAttributes redirectAttrs) {
        if (errors.hasErrors()) {
            return COUNCILAGENDA_NEW;
        }
        List<CouncilAgendaDetails> preambleList = new ArrayList<>();

        Long itemNumber = Long.valueOf(1);
        for (CouncilAgendaDetails councilAgendaDetails : councilAgenda
                .getCouncilAgendaDetailsForUpdate()) {
            if (councilAgendaDetails != null
                    && councilAgendaDetails.getPreamble() != null) {
                itemNumber = buildCouncilAgendaDetails(councilAgenda,
                        itemNumber, councilAgendaDetails);
                preambleList.add(councilAgendaDetails);
            }
        }

        AgendaNumberGenerator agendaNumberGenerator = autonumberServiceBeanResolver
                .getAutoNumberServiceFor(AgendaNumberGenerator.class);
        councilAgenda.setAgendaNumber(agendaNumberGenerator
                .getNextNumber(councilAgenda));

        councilAgenda.setStatus(egwStatusHibernateDAO.getStatusByModuleAndCode(
                AGENDA_MODULENAME, AGENDA_STATUS_APPROVED));
        councilAgenda.setAgendaDetails(preambleList);
        councilAgendaService.create(councilAgenda);

        redirectAttrs.addFlashAttribute("message",
                messageSource.getMessage("msg.agenda.success", null, null));
        return "redirect:/agenda/result/" + councilAgenda.getId();
    }

    private Long buildCouncilAgendaDetails(final CouncilAgenda councilAgenda,
            Long itemNumber, CouncilAgendaDetails councilAgendaDetails) {
        councilAgendaDetails.setPreamble(councilPreambleService
                .findOne(councilAgendaDetails.getPreamble().getId()));
        councilAgendaDetails.setAgenda(councilAgenda);
        councilAgendaDetails.setItemNumber(itemNumber.toString());
        councilAgendaDetails.setOrder(itemNumber);
        itemNumber++;
        councilAgendaDetails.getPreamble().setStatus(
                egwStatusHibernateDAO.getStatusByModuleAndCode(
                        PREAMBLE_MODULENAME, PREAMBLEUSEDINAGENDA));
        return itemNumber;
    }

    @RequestMapping(value = "/result/{id}", method = RequestMethod.GET)
    public String result(@PathVariable("id") final Long id, Model model) {
        CouncilAgenda councilagenda = councilAgendaService.findOne(id);
        model.addAttribute(COUNCIL_AGENDA, councilagenda);
        return COUNCILAGENDA_RESULT;
    }

    @RequestMapping(value = "/view/{id}", method = RequestMethod.GET)
    public String view(@PathVariable("id") final Long id, Model model) {
        CouncilAgenda councilAgenda = councilAgendaService.findOne(id);
        model.addAttribute(COUNCIL_AGENDA, councilAgenda);

        return COUNCILAGENDA_VIEW;
    }

    @RequestMapping(value = "/search/{mode}", method = RequestMethod.GET)
    public String search(@PathVariable("mode") final String mode, Model model) {
        model.addAttribute(COUNCIL_AGENDA, new CouncilAgenda());
        return COUNCILAGENDA_SEARCH;

    }

    @RequestMapping(value = "/searchagenda/{mode}", method = RequestMethod.GET)
    public String editAgenda(@PathVariable("mode") final String mode,
            Model model) {
        model.addAttribute(COUNCIL_AGENDA, new CouncilAgenda());
        return COUNCILAGENDA_SEARCH_APPROVED;

    }

    @RequestMapping(value = "/edit/{id}", method = RequestMethod.GET)
    public String edit(@PathVariable("id") final Long id, final Model model,
            final HttpServletResponse response) throws IOException {
        CouncilAgenda councilAgenda = councilAgendaService.findOne(id);
        councilAgenda.setCouncilAgendaDetailsForUpdate(councilAgenda
                .getAgendaDetails());
        model.addAttribute(COUNCIL_AGENDA, councilAgenda);
        model.addAttribute("councilPreamble", new CouncilPreamble());

        return COUNCILAGENDA_EDIT;
    }

    @RequestMapping(value = "/update", method = RequestMethod.POST)
    public String update(@ModelAttribute final CouncilAgenda councilAgenda,
            final Model model, final BindingResult errors,
            final RedirectAttributes redirectAttrs) {
        if (errors.hasErrors()) {
            return COUNCILAGENDA_EDIT;
        }
        final List<CouncilAgendaDetails> existingPreambleList = new ArrayList<>();
        List<CouncilAgendaDetails> preambleList = new ArrayList<>();
        if (!councilAgenda.getCouncilAgendaDetailsForUpdate().isEmpty()) {
            for (final CouncilAgendaDetails councilAgendaDetails : councilAgenda
                    .getAgendaDetails()) {
                councilAgendaDetails.getPreamble().setStatus(
                        egwStatusHibernateDAO.getStatusByModuleAndCode(
                                PREAMBLE_MODULENAME, PREAMBLE_STATUS_APPROVED));
                existingPreambleList.add(councilAgendaDetails);
            }
        }

        Long itemNumber = Long.valueOf(1);
        for (CouncilAgendaDetails councilAgendaDetails : councilAgenda
                .getCouncilAgendaDetailsForUpdate()) {
            if (councilAgendaDetails != null
                    && councilAgendaDetails.getPreamble() != null) {
                itemNumber = buildCouncilAgendaDetails(councilAgenda,
                        itemNumber, councilAgendaDetails);
                preambleList.add(councilAgendaDetails);
            }
        }
        councilAgenda.setAgendaDetails(preambleList);
        councilAgendaService.update(councilAgenda);
        if (!existingPreambleList.isEmpty())
            councilAgendaService.deleteAllInBatch(existingPreambleList); // UPDATE
                                                                         // STATUS
        redirectAttrs.addFlashAttribute("message",
                messageSource.getMessage("msg.agenda.success", null, null));
        return "redirect:/agenda/result/" + councilAgenda.getId();
    }

    @RequestMapping(value = "/ajaxsearch/{mode}", method = RequestMethod.POST, produces = MediaType.TEXT_PLAIN_VALUE)
    @ResponseBody
    public String searchPreamble(
            @PathVariable("mode") final String mode, Model model,
            @ModelAttribute final CouncilAgenda councilAgenda) {
        List<CouncilAgenda> searchResultList = councilAgendaService
                .search(councilAgenda);
        return new StringBuilder(DATA)
                .append(toJSON(searchResultList, CouncilAgenda.class,
                        CouncilAgendaJsonAdaptor.class))
                .append("}").toString();
    }

    @RequestMapping(value = "/ajaxsearch", method = RequestMethod.POST, produces = MediaType.TEXT_PLAIN_VALUE)
    @ResponseBody
    public String searchPreamble(Model model,
            @ModelAttribute final CouncilPreamble councilPreamble) {
        List<CouncilPreamble> searchResultList = councilPreambleService
                .searchForPreamble(councilPreamble);
        return new StringBuilder(DATA)
                .append(toJSON(searchResultList, CouncilPreamble.class,
                        CouncilPreambleJsonAdaptor.class))
                .append("}")
                .toString();
    }

    @RequestMapping(value = "/searchagenda-tocreatemeeting", method = RequestMethod.POST, produces = MediaType.TEXT_PLAIN_VALUE)
    @ResponseBody
    public String searchAgendaToCreateMeeting(Model model,
            @ModelAttribute final CouncilAgenda councilAgenda) {
        List<CouncilAgenda> searchResultList = councilAgendaService
                .searchForAgendaToCreateMeeting(councilAgenda);
        return new StringBuilder(DATA)
                .append(toJSON(searchResultList, CouncilAgenda.class,
                        CouncilAgendaJsonAdaptor.class))
                .append("}").toString();
    }

}
