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

import static org.egov.council.utils.constants.CouncilConstants.PREAMBLE_MODULENAME;
import static org.egov.council.utils.constants.CouncilConstants.PREAMBLE_STATUS_APPROVED;
import static org.egov.council.utils.constants.CouncilConstants.PREAMBLEUSEDINAGENDA;
import static org.egov.infra.web.utils.WebUtils.toJSON;

import java.io.IOException;
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
import org.egov.infra.admin.master.entity.Department;
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

    private final static String COUNCILAGENDA_NEW = "create-agenda";
    private final static String COUNCILAGENDA_RESULT = "agenda-result";
    private final static String COUNCILAGENDA_EDIT = "agenda-edit";
    private final static String COUNCILAGENDA_VIEW = "agenda-view";
    private final static String COUNCILAGENDA_SEARCH = "agenda-search";
    private final static String COUNCILAGENDA_SEARCH_APPROVED = "council-agendaSearch";

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

    public @ModelAttribute("departmentList") List<Department> getDepartmentList() {
        return departmentService.getAllDepartments();
    }
    
    public @ModelAttribute("committeeType") List<CommitteeType> getCommitteTypeList() {
        return committeeTypeService.getActiveCommiteeType();
    }

    @RequestMapping(value = "/new", method = RequestMethod.GET)
    public String newForm(final Model model) {
        model.addAttribute("councilAgenda", new CouncilAgenda());
        model.addAttribute("councilPreamble", new CouncilPreamble());
        return COUNCILAGENDA_NEW;
    }

    @RequestMapping(value = "/create", method = RequestMethod.POST)
    public String create(@Valid @ModelAttribute final CouncilAgenda councilAgenda, final BindingResult errors,
            final Model model, final RedirectAttributes redirectAttrs) {
         if (errors.hasErrors()) {
            return COUNCILAGENDA_NEW;
        }
        Long itemNumber = Long.valueOf(1);
        for (CouncilAgendaDetails councilAgendaDetails : councilAgenda.getAgendaDetails()) {
            if (councilAgendaDetails != null && councilAgendaDetails.getPreamble() != null) {
                councilAgendaDetails.setPreamble(councilPreambleService.findOne(councilAgendaDetails.getPreamble()
                        .getId()));
                councilAgendaDetails.setAgenda(councilAgenda);
                councilAgendaDetails.setItemNumber(itemNumber.toString());
                councilAgendaDetails.setOrder(itemNumber);
                councilAgendaDetails.getPreamble().setStatus(egwStatusHibernateDAO.getStatusByModuleAndCode(PREAMBLE_MODULENAME,
                		PREAMBLEUSEDINAGENDA));
                itemNumber++;
            }
        }

        AgendaNumberGenerator agendaNumberGenerator = autonumberServiceBeanResolver
                .getAutoNumberServiceFor(AgendaNumberGenerator.class);
        councilAgenda.setAgendaNumber(agendaNumberGenerator.getNextNumber(councilAgenda));

        councilAgenda.setStatus(egwStatusHibernateDAO.getStatusByModuleAndCode(PREAMBLE_MODULENAME,
                PREAMBLE_STATUS_APPROVED));

        councilAgendaService.create(councilAgenda);
        redirectAttrs.addFlashAttribute("message", messageSource.getMessage("msg.agenda.success", null, null));
        return "redirect:/agenda/result/" + councilAgenda.getId();
    }

    @RequestMapping(value = "/result/{id}", method = RequestMethod.GET)
    public String result(@PathVariable("id") final Long id, Model model) {
        CouncilAgenda councilagenda = councilAgendaService.findOne(id);
        model.addAttribute("councilAgenda", councilagenda);
        return COUNCILAGENDA_RESULT;
    }

    @RequestMapping(value = "/view/{id}", method = RequestMethod.GET)
    public String view(@PathVariable("id") final Long id, Model model) {
        CouncilAgenda councilAgenda = councilAgendaService.findOne(id);
        model.addAttribute("councilAgenda", councilAgenda);

        return COUNCILAGENDA_VIEW;
    }

    @RequestMapping(value = "/search/{mode}", method = RequestMethod.GET)
    public String search(@PathVariable("mode") final String mode, Model model) {
        model.addAttribute("councilAgenda", new CouncilAgenda());
        return COUNCILAGENDA_SEARCH;

    }
    
    @RequestMapping(value = "/searchagenda/{mode}", method = RequestMethod.GET)
    public String editAgenda(@PathVariable("mode") final String mode, Model model) {
        model.addAttribute("councilAgenda", new CouncilAgenda());
        return COUNCILAGENDA_SEARCH_APPROVED;

    }
    

    @RequestMapping(value = "/edit/{id}", method = RequestMethod.GET)
    public String edit(@PathVariable("id") final Long id, final Model model, final HttpServletResponse response)
            throws IOException {
        CouncilAgenda councilAgenda = councilAgendaService.findOne(id);
        model.addAttribute("councilAgenda", councilAgenda);
        model.addAttribute("councilPreamble", new CouncilPreamble());
        
        return COUNCILAGENDA_EDIT;
    }

    @RequestMapping(value = "/update", method = RequestMethod.POST)
    public String update(@ModelAttribute final CouncilAgenda councilAgenda, final Model model,
            final BindingResult errors, final RedirectAttributes redirectAttrs) {
        if (errors.hasErrors()) {
            return COUNCILAGENDA_EDIT;
        }
        Long itemNumber = Long.valueOf(1);
        for (CouncilAgendaDetails councilAgendaDetails : councilAgenda.getAgendaDetails()) {
            if (councilAgendaDetails != null && councilAgendaDetails.getPreamble() != null) {
                councilAgendaDetails.setPreamble(councilPreambleService.findOne(councilAgendaDetails.getPreamble()
                        .getId()));
                councilAgendaDetails.setAgenda(councilAgenda);
                councilAgendaDetails.setItemNumber(itemNumber.toString());
                councilAgendaDetails.setOrder(itemNumber);
                itemNumber++;

            }
        }

        councilAgendaService.update(councilAgenda);
        redirectAttrs.addFlashAttribute("message", messageSource.getMessage("msg.agenda.success", null, null));
        return "redirect:/agenda/result/" + councilAgenda.getId();
    }

    @RequestMapping(value = "/ajaxsearch/{mode}", method = RequestMethod.POST, produces = MediaType.TEXT_PLAIN_VALUE)
    public @ResponseBody String searchPreamble(@PathVariable("mode") final String mode, Model model,
            @ModelAttribute final CouncilAgenda councilAgenda) {
        List<CouncilAgenda> searchResultList = councilAgendaService.search(councilAgenda);
        final String result = new StringBuilder("{\"data\":")
                .append(toJSON(searchResultList, CouncilAgenda.class, CouncilAgendaJsonAdaptor.class)).append("}")
                .toString();
        return result;
    }

    @RequestMapping(value = "/ajaxsearch", method = RequestMethod.POST, produces = MediaType.TEXT_PLAIN_VALUE)
    public @ResponseBody String searchPreamble(Model model, @ModelAttribute final CouncilPreamble councilPreamble) {
        List<CouncilPreamble> searchResultList = councilPreambleService.searchForPreamble(councilPreamble);
        final String result = new StringBuilder("{\"data\":")
                .append(toJSON(searchResultList, CouncilPreamble.class, CouncilPreambleJsonAdaptor.class)).append("}")
                .toString();
        return result;
    }

    @RequestMapping(value = "/searchagenda-tocreatemeeting", method = RequestMethod.POST, produces = MediaType.TEXT_PLAIN_VALUE)
    public @ResponseBody String searchAgendaToCreateMeeting( Model model,
            @ModelAttribute final CouncilAgenda councilAgenda) {
        List<CouncilAgenda> searchResultList = councilAgendaService.searchForAgendaToCreateMeeting(councilAgenda);
        final String result = new StringBuilder("{\"data\":")
                .append(toJSON(searchResultList, CouncilAgenda.class, CouncilAgendaJsonAdaptor.class)).append("}")
                .toString();
        return result;
    }

}
