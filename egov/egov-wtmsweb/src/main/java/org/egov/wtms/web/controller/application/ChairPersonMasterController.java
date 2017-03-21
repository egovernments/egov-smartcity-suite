/*
 * eGov suite of products aim to improve the internal efficiency,transparency,
 *    accountability and the service delivery of the government  organizations.
 *
 *     Copyright (C) <2017>  eGovernments Foundation
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

import static org.springframework.web.bind.annotation.RequestMethod.GET;
import static org.springframework.web.bind.annotation.RequestMethod.POST;

import java.util.Date;
import java.util.List;

import org.egov.commons.entity.ChairPerson;
import org.egov.commons.service.ChairPersonService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.google.gson.JsonObject;

@Controller
@RequestMapping(value = "/application")
public class ChairPersonMasterController {

    @Autowired
    private ChairPersonService chairPersonService;
    public static final String CONTENTTYPE_JSON = "application/json";

    @RequestMapping(value = "/chairperson-create", method = GET)
    public String createForm(@ModelAttribute final ChairPerson chairPerson) {
        return "chairperson-create";
    }

    @RequestMapping(value = "/chairperson-create", method = POST)
    public String createChairPerson(@ModelAttribute final ChairPerson chairPerson, final Model model) {
        final ChairPerson chairPersonFromDb = chairPersonService.getActiveChairPerson();
        chairPersonService.createChairPerson(chairPerson);

        if (chairPersonFromDb != null) {
            chairPersonFromDb.setActive(false);
            chairPersonService.updateChairPerson(chairPersonFromDb);
        }
        model.addAttribute("mode", "create");
        model.addAttribute("message", "Chair Person Created Successfully");
        return "chairperson-success";
    }

    @RequestMapping(value = "/ajax-activeChairPersonExistsAsOnCurrentDate", method = GET)
    @ResponseBody
    public ChairPerson getChairPersonName(@RequestParam final String name) {
        if (chairPersonService.getActiveChairPerson() != null)
            return chairPersonService.getActiveChairPerson();
        return new ChairPerson();
    }

    @RequestMapping(value = "/chairperson-view", method = GET)
    public String getChairPersonMasterList(final Model model) {
        final List<ChairPerson> chairPersonList = chairPersonService.findAll();
        model.addAttribute("chairPersonList", chairPersonList);
        return "chairperson-view";
    }

    @RequestMapping(value = "/chairperson-edit", method = GET)
    public String getChairPersonMaster(final Model model) {
        model.addAttribute("mode", "edit");
        return getChairPersonMasterList(model);
    }

    @RequestMapping(value = "/chairperson-edit/{chairpersonid}", method = GET)
    public String modifyChairPerson(@ModelAttribute final ChairPerson chairPerson, @PathVariable final Long chairpersonid,
            final Model model) {
        final ChairPerson chairPersonFromDb = chairPersonService.findById(chairpersonid);
        if (chairPersonFromDb != null)
            model.addAttribute("chairPerson", chairPersonFromDb);
        return "chairpersondetails-edit";
    }

    @RequestMapping(value = "/chairperson-edit/{chairpersonid}", method = POST)
    public String saveModifiedChairPerson(@ModelAttribute final ChairPerson chairPerson, final Model model,
            final RedirectAttributes redirectAttrs,
            @PathVariable("chairpersonid") final Long chairpersonid) {

        final ChairPerson oldActiveChairPerson = chairPersonService.getActiveChairPerson();
        if (oldActiveChairPerson != null && oldActiveChairPerson.getId() != chairpersonid) {
            oldActiveChairPerson.setActive(false);
            chairPersonService.updateChairPerson(oldActiveChairPerson);
        }

        final ChairPerson chairPersonFromDB = chairPersonService.findById(chairpersonid);
        if (chairPersonFromDB != null) {
            chairPersonFromDB.setActive(chairPerson.isActive());
            chairPersonFromDB.setName(chairPerson.getName());
            chairPersonFromDB.setFromDate(chairPerson.getFromDate());
            chairPersonFromDB.setToDate(chairPerson.getToDate());
            chairPersonService.updateChairPerson(chairPersonFromDB);
        }

        model.addAttribute("message", "Chair Person Updated Successfully");
        return "chairperson-success";
    }

    @RequestMapping(value = "/ajax-activeChairPersonExistsAsOnGivenDate", method = GET)
    @ResponseBody
    public String getChairPersonNameOnGivenDate(@RequestParam final Date fromdate, @RequestParam final Date toDate) {
        final JsonObject jsonObj = new JsonObject();
        final ChairPerson chairperson = chairPersonService.getActiveChairPersonByGivenDate(fromdate, toDate);
        if (chairperson != null) {
            jsonObj.addProperty("fromDate", chairperson.getFromDate().toString());
            jsonObj.addProperty("toDate", chairperson.getToDate().toString());
        }
        return jsonObj.toString();
    }
}
