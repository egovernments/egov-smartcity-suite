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

package org.egov.wtms.web.controller.application;

import static org.egov.infra.utils.JsonUtils.toJSON;
import static org.springframework.web.bind.annotation.RequestMethod.GET;

import java.io.IOException;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.io.IOUtils;
import org.egov.commons.entity.ChairPerson;
import org.egov.commons.service.ChairPersonService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

@Controller
@RequestMapping(value = "/application")
public class CreateChairPersonMasterController {

    @Autowired
    private ChairPersonService chairPersonService;
    public static final String CONTENTTYPE_JSON = "application/json";

    @RequestMapping(value = "/chairPersonDetails", method = GET)
    public String viewForm() {
        return "chairperson-details";
    }

    @RequestMapping(value = "/ajax-activeChairPersonExistsAsOnCurrentDate", method = GET)
    public @ResponseBody boolean getChairPersonName(@RequestParam final String name) {
        if (chairPersonService.getActiveChairPersonByCurrentDate() != null)
            return false;
        else
            return true;
    }

    @RequestMapping(value = "/ajax-chairpersontable", method = GET)
    public @ResponseBody void springPaginationDataTables(final HttpServletRequest request,
            final HttpServletResponse response) throws IOException {
        final int pageStart = Integer.valueOf(request.getParameter("start"));
        int pageSize = Integer.valueOf(request.getParameter("length"));
        final int pageNumber = pageStart / pageSize + 1;
        final List<ChairPerson> totalRecords = chairPersonService.findAll();

        if (pageSize == -1)
            pageSize = totalRecords.size();
        final List<ChairPerson> chairPersonsList = chairPersonService.getListOfChairPersons(pageNumber, pageSize)
                .getContent();
        final StringBuilder chairPersonJSONData = new StringBuilder();
        chairPersonJSONData.append("{\"draw\": ").append("0");
        chairPersonJSONData.append(",\"recordsTotal\":").append(totalRecords.size());
        chairPersonJSONData.append(",\"totalDisplayRecords\":").append(chairPersonsList.size());
        chairPersonJSONData.append(",\"recordsFiltered\":").append(totalRecords.size());
        chairPersonJSONData.append(",\"data\":")
                .append(toJSON(chairPersonsList, ChairPerson.class, ChairPersonAdaptor.class)).append("}");
        response.setContentType(CONTENTTYPE_JSON);
        IOUtils.write(chairPersonJSONData, response.getWriter());
    }

    @RequestMapping(value = "/ajax-addChairPersonName", method = GET)
    public @ResponseBody void addChairPersonName(@RequestParam final String name) {
        final Calendar cal = Calendar.getInstance();
        if (chairPersonService.getActiveChairPersonByCurrentDate() != null) {
            ChairPerson chairPerson = new ChairPerson();
            chairPerson = chairPersonService.getActiveChairPersonByCurrentDate();
            chairPerson.setName(name);
            chairPersonService.updateChairPerson(chairPerson);

        } else {
            final ChairPerson chairPersonDetails = chairPersonService.getActiveChairPerson();
            if (chairPersonDetails != null) {
                chairPersonDetails.setActive(false);
                cal.add(Calendar.DATE, -1);
                chairPersonDetails.setToDate(cal.getTime());
                final ChairPerson chairPerson = new ChairPerson();
                chairPerson.setName(name);
                chairPerson.setFromDate(new Date());
                chairPerson.setToDate(null);
                chairPerson.setActive(true);
                chairPersonService.updateChairPerson(chairPersonDetails);
                chairPersonService.createChairPerson(chairPerson);
            } else {
                final ChairPerson chairPerson = new ChairPerson();
                chairPerson.setActive(true);
                chairPerson.setName(name);
                chairPerson.setFromDate(new Date());
                chairPerson.setToDate(null);
                chairPersonService.createChairPerson(chairPerson);

            }
        }
    }
}
