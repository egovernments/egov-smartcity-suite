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
import com.google.gson.JsonObject;
import org.apache.commons.io.IOUtils;
import org.egov.infra.exception.ApplicationException;
import org.egov.works.milestone.entity.Milestone;
import org.egov.works.milestone.entity.TrackMilestone;
import org.egov.works.milestone.entity.TrackMilestoneActivity;
import org.egov.works.milestone.entity.enums.MilestoneActivityStatus;
import org.egov.works.milestone.service.MilestoneService;
import org.egov.works.milestone.service.TrackMilestoneService;
import org.egov.works.web.adaptor.TrackMilestoneJsonAdaptor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.MessageSource;
import org.springframework.http.MediaType;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.Writer;

@RestController
@RequestMapping(value = "/milestone")
public class TrackMilestoneController {

    @Autowired
    private MilestoneService milestoneService;

    @Autowired
    @Qualifier("messageSource")
    private MessageSource messageSource;

    @Autowired
    private TrackMilestoneJsonAdaptor trackMilestoneJsonAdaptor;

    @Autowired
    private TrackMilestoneService trackMilestoneService;

    @ModelAttribute
    public Milestone getMilestone(@PathVariable final Long id) {
        final Milestone milestone = milestoneService.getMilestoneById(id);
        return milestone;
    }

    @RequestMapping(value = "/track/{id}", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
    public @ResponseBody String showNewMilestoneForm(@PathVariable final Long id) throws ApplicationException {
        final Milestone milestone = getMilestone(id);
        final String result = new StringBuilder().append(toSearchMilestoneTemplateJson(milestone)).toString();
        return result;
    }

    public Object toSearchMilestoneTemplateJson(final Object object) {
        final GsonBuilder gsonBuilder = new GsonBuilder();
        final Gson gson = gsonBuilder.registerTypeAdapter(Milestone.class, trackMilestoneJsonAdaptor).create();
        final String json = gson.toJson(object);
        return json;
    }

    @RequestMapping(value = "/track/{id}", method = RequestMethod.POST)
    public @ResponseBody String create(@ModelAttribute("milestone") final Milestone milestone,
            final Model model, final BindingResult errors, final HttpServletRequest request, final BindingResult resultBinder,
            final HttpServletResponse response)
            throws ApplicationException, IOException {

        final String mode = request.getParameter("mode");
        final JsonObject jsonObject = new JsonObject();
        validateTrackMilestone(milestone, jsonObject, mode);

        if (jsonObject.toString().length() > 2) {
            sendAJAXResponse(jsonObject.toString(), response);
            return "";
        }

        final Milestone newMilestone = milestoneService.update(milestone);

        return messageSource.getMessage("msg.trackmilestone.create.success",
                new String[] { newMilestone.getWorkOrderEstimate().getEstimate().getLineEstimateDetails().getEstimateNumber() },
                null);
    }

    private void validateTrackMilestone(final Milestone milestone, final JsonObject jsonObject, final String mode) {
        for (final TrackMilestone tm : milestone.getTrackMilestone()) {
            Integer count = 0;
            boolean flag = false;
            if ("create".equals(mode)) {
                final TrackMilestone fromDB = trackMilestoneService.getTrackMilestoneByMilestoneId(milestone.getId());
                if (fromDB != null) {
                    jsonObject.addProperty("alreadyCreated",
                            messageSource.getMessage("error.trackmilestone.already.created",
                                    new String[] {}, null));
                    flag = true;
                }
            }
            for (final TrackMilestoneActivity tma : tm.getActivities()) {
                if (tma.getStatus().equals(MilestoneActivityStatus.NOT_YET_STARTED.name()) && tma.getCompletedPercentage() != 0) {
                    jsonObject.addProperty("completedPercentage_" + count,
                            messageSource.getMessage("error.trackmilestone.notyetstarted.percentage.zero",
                                    new String[] {}, null));
                    flag = true;
                }
                if (tma.getStatus().equals(MilestoneActivityStatus.COMPLETED.name()) && tma.getCompletedPercentage() != 100) {
                    jsonObject.addProperty("completedPercentage_" + count,
                            messageSource.getMessage("error.trackmilestone.completed.percentage.hundred",
                                    new String[] {}, null));
                    flag = true;
                }
                if (tma.getStatus().equals(MilestoneActivityStatus.COMPLETED.name()) && tma.getCompletionDate() == null) {
                    jsonObject.addProperty("completionDate_" + count,
                            messageSource.getMessage("error.trackmilestone.completed.completiondate.mandatory",
                                    new String[] {}, null));
                    flag = true;
                }
                if (tma.getCompletionDate() != null)
                    if (tma.getCompletionDate().after(milestone.getActivities().get(count).getScheduleEndDate())
                            && tma.getRemarks() == null) {
                        jsonObject.addProperty("reasonForDelay_" + count,
                                messageSource.getMessage("error.trackmilestone.reasonfordelay.mandatory",
                                        new String[] {}, null));
                        flag = true;
                    }
                count++;
                if (flag)
                    break;
            }
        }
    }

    protected void sendAJAXResponse(final String msg, final HttpServletResponse response) {
        try {
            final Writer httpResponseWriter = response.getWriter();
            IOUtils.write(msg, httpResponseWriter);
            IOUtils.closeQuietly(httpResponseWriter);
        } catch (final IOException e) {

        }
    }
}
