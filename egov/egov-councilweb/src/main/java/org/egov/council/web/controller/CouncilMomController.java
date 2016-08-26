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

import java.lang.reflect.Type;
import java.util.List;

import javax.validation.Valid;

import org.egov.commons.EgwStatus;
import org.egov.commons.dao.EgwStatusHibernateDAO;
import org.egov.council.autonumber.PreambleNumberGenerator;
import org.egov.council.entity.CouncilMeeting;
import org.egov.council.entity.CouncilPreamble;
import org.egov.council.entity.MeetingMOM;
import org.egov.council.entity.enums.PreambleType;
import org.egov.council.service.CommitteeTypeService;
import org.egov.council.service.CouncilAgendaService;
import org.egov.council.service.CouncilMeetingService;
import org.egov.council.service.CouncilPreambleService;
import org.egov.council.utils.constants.CouncilConstants;
import org.egov.council.web.adaptor.CouncilDepartmentJsonAdaptor;
import org.egov.infra.admin.master.entity.Boundary;
import org.egov.infra.admin.master.entity.Department;
import org.egov.infra.admin.master.service.BoundaryService;
import org.egov.infra.admin.master.service.DepartmentService;
import org.egov.infra.utils.autonumber.AutonumberServiceBeanResolver;
import org.egov.infra.web.support.json.adapter.BoundaryAdapter;
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

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;

@Controller
@RequestMapping("/councilmom")
public class CouncilMomController {

	private final static String COUNCIL_MOM_MEETING_SEARCH = "councilmomMeeting-search";
	private final static String COUNCILMOM_NEW = "councilMom-new";
	private final static String MODULE_NAME = "COUNCIL";
	private static final String COMMONERRORPAGE = null;
	private final static String COUNCILMEETING_EDIT = "councilmeeting-edit";
	private final static String COUNCILMOM_RESULT = "councilmom-result";
	private final static String COUNCILMOM_SEARCH = "councilmom-search";
	private final static String COUNCILMOM_VIEW = "councilmom-view";

	@Autowired
	private EgwStatusHibernateDAO egwStatusHibernateDAO;

	@Autowired
	private MessageSource messageSource;

	@Autowired
	private CommitteeTypeService committeeTypeService;

	@Autowired
	private CouncilMeetingService councilMeetingService;

	@Autowired
	private CouncilAgendaService councilAgendaService;

	@Autowired
	private EgwStatusHibernateDAO egwStatusService;

	@Autowired
	private DepartmentService departmentService;

	@Autowired
	private BoundaryService boundaryService;

	@Autowired
	private AutonumberServiceBeanResolver autonumberServiceBeanResolver;

	@Autowired
	private CouncilPreambleService councilPreambleService;

	private void prepareNewForm(final Model model) {
		model.addAttribute("committeeType",
				committeeTypeService.getActiveCommiteeType());
		model.addAttribute("meetingTimingMap", CouncilConstants.MEETING_TIMINGS);
		model.addAttribute("resolutionStatus", egwStatusService
				.getStatusByModule(CouncilConstants.COUNCIL_RESOLUTION));

	}

	@RequestMapping(value = "/new/{id}", method = RequestMethod.GET)
	public String newForm(@PathVariable("id") final Long id, Model model) {
		CouncilMeeting councilMeeting = councilMeetingService.findOne(id);
		prepareNewForm(model);
		model.addAttribute("councilMeeting", councilMeeting);
		return COUNCILMOM_NEW;
	}

	@RequestMapping(value = "/update", method = RequestMethod.POST)
	public String update(
			@Valid @ModelAttribute final CouncilMeeting councilMeeting,
			final BindingResult errors, final Model model,
			final RedirectAttributes redirectAttrs) {
		if (errors.hasErrors()) {
			prepareNewForm(model);
			return COUNCILMEETING_EDIT;
		}
		for (MeetingMOM meetingMOM : councilMeeting.getMeetingMOMs()) {
			if (meetingMOM.getPreamble().getId() == null) {
				CouncilPreamble councilPreamble = new CouncilPreamble();
				PreambleNumberGenerator preamblenumbergenerator = autonumberServiceBeanResolver
						.getAutoNumberServiceFor(PreambleNumberGenerator.class);

				councilPreamble.setPreambleNumber(preamblenumbergenerator
						.getNextNumber(councilPreamble));
				councilPreamble.setStatus(egwStatusHibernateDAO
						.getStatusByModuleAndCode(
								CouncilConstants.PREAMBLE_MODULENAME,
								CouncilConstants.PREAMBLE_STATUS_CREATED));
				councilPreamble.setDepartment(meetingMOM.getPreamble()
						.getDepartment());
				councilPreamble.setGistOfPreamble(meetingMOM.getPreamble()
						.getGistOfPreamble());
				councilPreamble.setSanctionAmount(meetingMOM.getPreamble()
						.getSanctionAmount());
				councilPreamble.setType(PreambleType.SUMOTO);
				meetingMOM.setPreamble(councilPreamble);
				meetingMOM.setMeeting(councilMeeting);
			}
		}

		councilMeetingService.update(councilMeeting);

		redirectAttrs.addFlashAttribute("message", messageSource.getMessage(
				"msg.councilMeeting.success", null, null));
		return "redirect:/councilmom/result/" + councilMeeting.getId();
	}

	@RequestMapping(value = "/result/{id}", method = RequestMethod.GET)
	public String result(@PathVariable("id") final Long id, Model model) {
		CouncilMeeting councilMeeting = councilMeetingService.findOne(id);
		model.addAttribute("councilMeeting", councilMeeting);
		return COUNCILMOM_RESULT;
	}

	@RequestMapping(value = "/meetingsearch/{mode}", method = RequestMethod.GET)
	public String searchMeeting(@PathVariable("mode") final String mode,
			Model model) {
		prepareNewForm(model);
		model.addAttribute("councilMeeting", new CouncilMeeting());
		return COUNCIL_MOM_MEETING_SEARCH;

	}

	@RequestMapping(value = "/view/{id}", method = RequestMethod.GET)
	public String view(@PathVariable("id") final Long id, Model model) {
		CouncilMeeting councilMeeting = councilMeetingService.findOne(id);
		prepareNewForm(model);
		model.addAttribute("councilMeeting", councilMeeting);

		return COUNCILMOM_VIEW;
	}

	@RequestMapping(value = "/search/{mode}", method = RequestMethod.GET)
	public String search(@PathVariable("mode") final String mode, Model model) {
		CouncilMeeting councilMeeting = new CouncilMeeting();
		prepareNewForm(model);
		model.addAttribute("councilMeeting", councilMeeting);
		return COUNCILMOM_SEARCH;

	}

	@RequestMapping(value = "/departmentlist", method = RequestMethod.GET, produces = MediaType.TEXT_PLAIN_VALUE)
	public @ResponseBody String ajaxsearch(final String mode, Model model,
			@ModelAttribute final CouncilMeeting councilMeeting) {
		List<Department> departmentList = departmentService.getAllDepartments();
		String result = new StringBuilder("{ \"departmentLists\":")
				.append(toSearchResultJson(departmentList)).append("}")
				.toString();
		return result;
	}

	@RequestMapping(value = "/resolutionlist", method = RequestMethod.GET, produces = MediaType.TEXT_PLAIN_VALUE)
	public @ResponseBody String ajaxsearchResolutionlist(final String mode,
			Model model, @ModelAttribute final CouncilMeeting councilMeeting) {
		List<EgwStatus> resolutionList = egwStatusService
				.getStatusByModule(CouncilConstants.COUNCIL_RESOLUTION);
		Gson gson = new Gson();
		Type type = new TypeToken<List<EgwStatus>>() {
		}.getType();
		String json = gson.toJson(resolutionList, type);
		String result = new StringBuilder("{ \"resolutionLists\":")
				.append(json).append("}").toString();
		return result;
	}

	@RequestMapping(value = "/wardlist", method = RequestMethod.GET, produces = MediaType.TEXT_PLAIN_VALUE)
	public @ResponseBody String ajaxsearchWardlist(final String mode,
			Model model, @ModelAttribute final CouncilMeeting councilMeeting) {
		List<Boundary> wardList = boundaryService
				.getActiveBoundariesByBndryTypeNameAndHierarchyTypeName(
						CouncilConstants.WARD,
						CouncilConstants.REVENUE_HIERARCHY_TYPE);
		String result = new StringBuilder("{ \"wardLists\":")
				.append(toGetboundaryResultJson(wardList)).append("}")
				.toString();
		return result;
	}

	public Object toSearchResultJson(final Object object) {
		final GsonBuilder gsonBuilder = new GsonBuilder();
		final Gson gson = gsonBuilder.registerTypeAdapter(Department.class,
				new CouncilDepartmentJsonAdaptor()).create();
		final String json = gson.toJson(object);
		return json;
	}

	public Object toGetboundaryResultJson(final Object object) {
		final GsonBuilder gsonBuilder = new GsonBuilder();
		final Gson gson = gsonBuilder.registerTypeAdapter(Boundary.class,
				new BoundaryAdapter()).create();
		final String json = gson.toJson(object);
		return json;
	}
}
