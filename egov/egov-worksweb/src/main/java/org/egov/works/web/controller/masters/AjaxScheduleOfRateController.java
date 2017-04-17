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
package org.egov.works.web.controller.masters;

import java.util.List;

import org.egov.works.masters.entity.ScheduleOfRate;
import org.egov.works.masters.entity.ScheduleOfRateSearchRequest;
import org.egov.works.masters.service.ScheduleOfRateService;
import org.egov.works.web.adaptor.ScheduleOfRateSearchJsonAdaptor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

@Controller
@RequestMapping(value = "/masters")
public class AjaxScheduleOfRateController {

    @Autowired
    private ScheduleOfRateService scheduleOfRateService;

    @Autowired
    private ScheduleOfRateSearchJsonAdaptor scheduleOfRateSearchJsonAdaptor;

    @RequestMapping(value = "/scheduleofrate-searchdetails", method = RequestMethod.POST, produces = MediaType.TEXT_PLAIN_VALUE)
    @ResponseBody
    public String ajaxScheduleOfRateSearch(final Model model,
            @ModelAttribute final ScheduleOfRateSearchRequest scheduleOfRateSearchRequest) {
        final List<ScheduleOfRate> searchScheduleOfRateList = scheduleOfRateService
                .searchScheduleOfRate(scheduleOfRateSearchRequest);
        return new StringBuilder("{ \"data\":").append(toSearchScheduleOfRateJson(searchScheduleOfRateList)).append("}")
                .toString();
    }

    public Object toSearchScheduleOfRateJson(final Object object) {
        final GsonBuilder gsonBuilder = new GsonBuilder();
        final Gson gson = gsonBuilder.registerTypeAdapter(ScheduleOfRate.class, scheduleOfRateSearchJsonAdaptor)
                .create();
        return gson.toJson(object);
    }
}