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
package org.egov.works.web.controller.milestone;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import org.egov.works.contractorbill.entity.enums.BillTypes;
import org.egov.works.master.service.MilestoneTemplateService;
import org.egov.works.milestone.entity.Milestone;
import org.egov.works.milestone.entity.SearchRequestMilestone;
import org.egov.works.milestone.entity.SearchRequestMilestoneTemplate;
import org.egov.works.milestone.entity.TrackMilestone;
import org.egov.works.milestone.service.MilestoneService;
import org.egov.works.milestone.service.TrackMilestoneService;
import org.egov.works.models.masters.MilestoneTemplate;
import org.egov.works.web.adaptor.SearchCancelMilestoneJsonAdaptor;
import org.egov.works.web.adaptor.SearchMilestoneJsonAdaptor;
import org.egov.works.web.adaptor.SearchMilestoneTemplateJsonAdaptor;
import org.egov.works.web.adaptor.SearchTrackMilestoneJsonAdaptor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.MessageSource;
import org.springframework.http.MediaType;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping(value = "/milestone")
public class AjaxMilestoneController {

    @Autowired
    private MilestoneService milestoneService;

    @Autowired
    private SearchMilestoneJsonAdaptor searchMilestoneJsonAdaptor;

    @Autowired
    private MilestoneTemplateService milestoneTemplateService;

    @Autowired
    private SearchMilestoneTemplateJsonAdaptor searchMilestoneTemplateJsonAdaptor;

    @Autowired
    private TrackMilestoneService trackMilestoneService;

    @Autowired
    private SearchTrackMilestoneJsonAdaptor searchTrackMilestoneJsonAdaptor;

    @Autowired
    private SearchCancelMilestoneJsonAdaptor searchCancelMilestoneJsonAdaptor;

    @Autowired
    @Qualifier("messageSource")
    private MessageSource messageSource;

    @RequestMapping(value = "/ajax-search", method = RequestMethod.POST, produces = MediaType.TEXT_PLAIN_VALUE)
    public @ResponseBody String searchMilestones(@ModelAttribute final SearchRequestMilestone searchRequestMilestone) {
        final List<Milestone> searchMilestoneList = milestoneService
                .searchMilestone(searchRequestMilestone);
        final String result = new StringBuilder("{ \"data\":").append(toSearchMilestone(searchMilestoneList))
                .append("}").toString();
        return result;
    }

    public Object toSearchMilestone(final Object object) {
        final GsonBuilder gsonBuilder = new GsonBuilder();
        final Gson gson = gsonBuilder.registerTypeAdapter(Milestone.class, searchMilestoneJsonAdaptor).create();
        final String json = gson.toJson(object);
        return json;
    }

    @RequestMapping(value = "/ajaxmilestonetemplatecode-milestone", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
    public @ResponseBody List<MilestoneTemplate> findMilestoneTemplateCodeForMilestone(@RequestParam final String code) {
        return milestoneTemplateService.findMilestoneTemplateCodeForMilestone(code);
    }

    @RequestMapping(value = "/ajaxsearchmilestonetemplate", method = RequestMethod.POST, produces = MediaType.TEXT_PLAIN_VALUE)
    public @ResponseBody String ajaxMilestoneTemplateSearch(final Model model,
            @ModelAttribute final SearchRequestMilestoneTemplate searchRequestMilestoneTemplate) {
        final List<MilestoneTemplate> searchMilestoneTemplateList = milestoneTemplateService
                .searchMilestoneTemplate(searchRequestMilestoneTemplate);
        final String result = new StringBuilder("{ \"data\":").append(toSearchMilestoneTemplateJson(searchMilestoneTemplateList))
                .append("}").toString();
        return result;
    }

    public Object toSearchMilestoneTemplateJson(final Object object) {
        final GsonBuilder gsonBuilder = new GsonBuilder();
        final Gson gson = gsonBuilder.registerTypeAdapter(MilestoneTemplate.class, searchMilestoneTemplateJsonAdaptor).create();
        final String json = gson.toJson(object);
        return json;
    }

    @RequestMapping(value = "/ajaxtrackmilestone-search", method = RequestMethod.POST, produces = MediaType.TEXT_PLAIN_VALUE)
    public @ResponseBody String searchTrackMilestones(final Model model,
            @ModelAttribute final SearchRequestMilestone searchRequestMilestone) {
        final List<TrackMilestone> searchTrackMilestoneList = trackMilestoneService
                .searchTrackMilestone(searchRequestMilestone);
        final String result = new StringBuilder("{ \"data\":").append(toSearchTrackMilestone(searchTrackMilestoneList))
                .append("}").toString();
        return result;
    }

    public Object toSearchTrackMilestone(final Object object) {
        final GsonBuilder gsonBuilder = new GsonBuilder();
        final Gson gson = gsonBuilder.registerTypeAdapter(TrackMilestone.class, searchTrackMilestoneJsonAdaptor).create();
        final String json = gson.toJson(object);
        return json;
    }

    @RequestMapping(value = "/ajaxworkidentificationnumbers-trackmilestone", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
    public @ResponseBody List<String> findWorkIdNumbersToTrackMilestone(@RequestParam final String code) {
        return trackMilestoneService.findWorkIdentificationNumbersTrackMilestone(code);
    }

    @RequestMapping(value = "/cancel/ajax-search", method = RequestMethod.POST, produces = MediaType.TEXT_PLAIN_VALUE)
    public @ResponseBody String searchMilestonesToCancel(final Model model,
            @ModelAttribute final SearchRequestMilestone searchRequestMilestone) {
        final List<Milestone> milestones = milestoneService
                .searchMilestonesToCancel(searchRequestMilestone);
        final String result = new StringBuilder("{ \"data\":")
                .append(toSearchMilestonesToCancelJson(milestones))
                .append("}").toString();
        return result;
    }

    public Object toSearchMilestonesToCancelJson(final Object object) {
        final GsonBuilder gsonBuilder = new GsonBuilder();
        final Gson gson = gsonBuilder.registerTypeAdapter(Milestone.class, searchCancelMilestoneJsonAdaptor)
                .create();
        final String json = gson.toJson(object);
        return json;
    }

    @RequestMapping(value = "/ajaxloanumbers-milestonetocancel", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
    public @ResponseBody List<String> findLOAsToCancelMilestone(@RequestParam final String code) {
        return milestoneService.findLoaNumbersToCancelMilestone(code);
    }

    @RequestMapping(value = "/ajaxcontractors-milestonetocancel", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
    public @ResponseBody List<String> findContractorsToCancelMilestone(@RequestParam final String code) {
        return milestoneService.findContractorsToCancelMilestone(code);
    }

    @RequestMapping(value = "/ajax-searchmilestoneforview", method = RequestMethod.POST, produces = MediaType.TEXT_PLAIN_VALUE)
    public @ResponseBody String searchMilestonesForView(@ModelAttribute final SearchRequestMilestone searchRequestMilestone) {
        final List<Milestone> searchMilestoneList = milestoneService
                .searchMilestoneForView(searchRequestMilestone);
        final String result = new StringBuilder("{ \"data\":").append(toSearchMilestone(searchMilestoneList))
                .append("}").toString();
        return result;
    }

    @RequestMapping(value = "/validate-milestonepercentagetocreatecontractorbill", method = RequestMethod.POST, produces = MediaType.APPLICATION_JSON_VALUE)
    public @ResponseBody String validateCreateContractorPartBill(
            @RequestParam("workOrderEstimateId") final Long workOrderEstimateId,
            @RequestParam("billType") final String billType) {
        String message = "";
        TrackMilestone trackMileStone = null;
        if (billType.equalsIgnoreCase(BillTypes.Final_Bill.toString())) {
            trackMileStone = trackMilestoneService.getCompletionPercentageToCreateContractorFinalBill(workOrderEstimateId);
            if (trackMileStone == null)
                message = messageSource.getMessage("error.contractor.finalbill.milestonepercentage", null, null);
        } else {
            trackMileStone = trackMilestoneService.getMinimumPercentageToCreateContractorBill(workOrderEstimateId);
            if (trackMileStone == null)
                message = messageSource.getMessage("error.contractorbil.milestone.percentage", null, null);
        }
        return message;
    }

}
