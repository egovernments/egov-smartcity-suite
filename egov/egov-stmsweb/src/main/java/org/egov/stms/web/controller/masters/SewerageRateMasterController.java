/**
 * eGov suite of products aim to improve the internal efficiency,transparency,
   accountability and the service delivery of the government  organizations.

    Copyright (C) <2015>  eGovernments Foundation

    The updated version of eGov suite of products as by eGovernments Foundation
    is available at http://www.egovernments.org
    This program is free software: you can redistribute it and/or modify
    it under the terms of the GNU General Public License as published by
    the Free Software Foundation, either version 3 of the License, or
    any later version.

    This program is distributed in the hope that it will be useful,
    but WITHOUT ANY WARRANTY; without even the implied warranty of
    MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
    GNU General Public License for more details.

    You should have received a copy of the GNU General Public License
    along with this program. If not, see http://www.gnu.org/licenses/ or
    http://www.gnu.org/licenses/gpl.html .

    In addition to the terms of the GPL license to be adhered to in using this
    program, the following additional terms are to be complied with:

        1) All versions of this program, verbatim or modified must carry this
           Legal Notice.

        2) Any misrepresentation of the origin of the material is prohibited. It
           is required that all modified versions of this material be marked in
           reasonable ways as different from the original version.

        3) This license does not grant any rights to any user of the program
           with regards to rights under trademark law for use of the trade names
           or trademarks of eGovernments Foundation.

  In case of any queries, you can reach eGovernments Foundation at contact@egovernments.org.
 */
package org.egov.stms.web.controller.masters;

import static org.springframework.http.MediaType.APPLICATION_JSON_VALUE;
import static org.springframework.web.bind.annotation.RequestMethod.GET;
import static org.springframework.web.bind.annotation.RequestMethod.POST;

import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

import javax.servlet.http.HttpServletResponse;

import org.apache.commons.io.IOUtils;
import org.egov.commons.CFinancialYear;
import org.egov.commons.service.FinancialYearService;
import org.egov.infra.config.properties.ApplicationProperties;
import org.egov.infra.utils.DateUtils;
import org.egov.stms.masters.entity.SewerageRatesMaster;
import org.egov.stms.masters.entity.enums.PropertyType;
import org.egov.stms.masters.entity.enums.SewerageRateStatus;
import org.egov.stms.masters.pojo.SewerageRatesSearch;
import org.egov.stms.masters.service.SewerageRatesMasterService;
import org.egov.stms.web.controller.utils.SewerageMasterDataValidator;
import org.joda.time.DateTime;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.google.gson.GsonBuilder;

@Controller
@RequestMapping(value = "/masters")
public class SewerageRateMasterController {
    private static final Logger LOG = LoggerFactory.getLogger(SewerageRateMasterController.class);
    private static final String SEWERAGE_RATES_MASTER = "sewerageRatesMaster";
    private static final String SEWERAGE_RATES_SUCCESS_PAGE = "redirect:/masters/getseweragerates/";
    private static final String MESSAGE = "message";

    @Autowired
    private SewerageRatesMasterService sewerageRatesMasterService;

    @Autowired
    private FinancialYearService financialYearService;

    @Autowired
    private ApplicationProperties applicationProperties;

    @Autowired
    private SewerageMasterDataValidator sewerageMasterDataValidator;

    @RequestMapping(value = "/seweragerates", method = RequestMethod.GET)
    public String showForm(
            @ModelAttribute final SewerageRatesMaster sewerageRatesMaster,
            final Model model) {
        final CFinancialYear financialYear = financialYearService.getCurrentFinancialYear();
        if (financialYear != null)
            model.addAttribute("endDate", financialYear.getEndingDate());
        model.addAttribute("propertyTypes", PropertyType.values());

        return "sewerageRates-master";
    }

    @RequestMapping(value = "/seweragerates", method = RequestMethod.POST)
    public String create(
            @ModelAttribute final SewerageRatesMaster sewerageRatesMaster,
            final RedirectAttributes redirectAttrs, final Model model,
            final BindingResult resultBinder) {
        sewerageMasterDataValidator.validateMonthlyRate(sewerageRatesMaster, resultBinder);
        if (resultBinder.hasErrors()) {
            model.addAttribute("monthlyRate", sewerageRatesMaster.getMonthlyRate());
            model.addAttribute(SEWERAGE_RATES_MASTER, sewerageRatesMaster);
            return "sewerageRates-master";
        }

        final List<SewerageRatesMaster> existingsewerageRatesMasterList = sewerageRatesMasterService
                .getLatestActiveRecord(sewerageRatesMaster.getPropertyType(), true);

        SewerageRatesMaster sewerageRatesMasterExisting;
        sewerageRatesMasterExisting = sewerageRatesMasterService
                .findByPropertyTypeAndFromDateAndActive(
                        sewerageRatesMaster.getPropertyType(),
                        sewerageRatesMaster.getFromDate(), true);
        if (sewerageRatesMasterExisting != null) {
            model.addAttribute("existingMonthlyRate", sewerageRatesMasterExisting.getMonthlyRate());
            sewerageRatesMasterExisting.setActive(false);
            /* append the end time of given date as 23:59:59 */
            final DateTime dateValue = DateUtils.endOfGivenDate(new DateTime(sewerageRatesMaster.getFromDate()));
            final Date formattedToDate = dateValue.toDate();
            sewerageRatesMasterExisting.setToDate(formattedToDate);
            sewerageRatesMasterService.update(sewerageRatesMasterExisting);

            sewerageRatesMaster.setActive(true);
            sewerageRatesMasterService.create(sewerageRatesMaster);
        } else {
            SewerageRatesMaster sewerageRatesMasterOld = null;
            if (!existingsewerageRatesMasterList.isEmpty())
                sewerageRatesMasterOld = existingsewerageRatesMasterList.get(0);
            if (sewerageRatesMasterOld != null) {
                if (sewerageRatesMaster.getFromDate().compareTo(new Date()) < 0)
                    sewerageRatesMasterOld.setActive(false);

                final DateTime oldDate = DateUtils.endOfGivenDate(new DateTime(sewerageRatesMaster.getFromDate()).minusDays(1));
                final Date formattedToDate = oldDate.toDate();
                sewerageRatesMasterOld.setToDate(formattedToDate);
                sewerageRatesMasterService.update(sewerageRatesMasterOld);
            }
            sewerageRatesMaster.setActive(true);
            sewerageRatesMasterService.create(sewerageRatesMaster);
        }
        redirectAttrs.addFlashAttribute(MESSAGE, "msg.seweragemonthlyrate.creation.success");
        return SEWERAGE_RATES_SUCCESS_PAGE + sewerageRatesMaster.getId();

    }

    @RequestMapping(value = "/getseweragerates/{id}", method = RequestMethod.GET)
    public String getSeweragerates(@PathVariable("id") final Long id, final Model model) {
        final SewerageRatesMaster sewerageRatesMaster = sewerageRatesMasterService.findBy(id);
        model.addAttribute(SEWERAGE_RATES_MASTER, sewerageRatesMaster);
        return "sewerageRates-success";
    }

    @RequestMapping(value = "/ajaxexistingseweragevalidate", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
    @ResponseBody
    public double getSewerageRatesByAllCombinatons(@RequestParam("propertyType") final PropertyType propertyType,
            @RequestParam("fromDate") final Date fromDate) {
        SewerageRatesMaster sewerageRatesMasterExist;
        sewerageRatesMasterExist = sewerageRatesMasterService
                .findByPropertyTypeAndFromDateAndActive(propertyType, fromDate, true);
        if (sewerageRatesMasterExist != null)
            return sewerageRatesMasterExist.getMonthlyRate();
        else
            return 0;
    }

    @RequestMapping(value = "/fromDateValidationWithLatestActiveRecord", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
    @ResponseBody
    public String getLatestActiveFromDate(@RequestParam("propertyType") final PropertyType propertyType,
            @RequestParam("fromDate") final Date fromDate) {
        SimpleDateFormat newFormat = new SimpleDateFormat("dd-MM-yyyy");
     
        final List<SewerageRatesMaster> existingsewerageRatesMasterList = sewerageRatesMasterService
                .getLatestActiveRecord(propertyType, true);
        if (!existingsewerageRatesMasterList.isEmpty() && fromDate != null) {
            final SewerageRatesMaster existingActiveSewerageRatesObject = existingsewerageRatesMasterList.get(0);

            if (fromDate.compareTo(existingActiveSewerageRatesObject.getFromDate()) < 0)
                return newFormat.format(existingActiveSewerageRatesObject.getFromDate());
        }
        return "true";

    }

    @RequestMapping(value = "/viewSewerageRate", method = RequestMethod.GET)
    public String view(@ModelAttribute final SewerageRatesSearch sewerageRatesSearch, final Model model) {
        model.addAttribute("propertyTypes", PropertyType.values());
        model.addAttribute("statusValues", SewerageRateStatus.values());
        return "sewerageRates-view";
    }

    @RequestMapping(value = "/rateView/{id}", method = RequestMethod.GET)
    public String view(@ModelAttribute final SewerageRatesSearch sewerageRatesSearch, final Model model,
            @PathVariable("id") final Long id, final RedirectAttributes redirectAttrs) {
        final SewerageRatesMaster sewerageRatesMaster = sewerageRatesMasterService.findBy(id);
        model.addAttribute("sewerageRatesSearch", sewerageRatesMaster);
        return SEWERAGE_RATES_SUCCESS_PAGE + id;
    }

    @RequestMapping(value = "/search-sewerage-rates", method = GET)
    @ResponseBody
    public void sewerageRatesSearch(final Model model,
            @ModelAttribute final SewerageRatesSearch sewerageRatesSearch, final HttpServletResponse response)
            throws IOException {
        SimpleDateFormat formatter = new SimpleDateFormat("dd/MM/yyyy");
        SimpleDateFormat newFormat = new SimpleDateFormat("dd-MM-yyyy");
     
        PropertyType type = null;
        String effectivefromDate = null;
        if (sewerageRatesSearch.getPropertyType() != null)
            type = PropertyType.valueOf(sewerageRatesSearch.getPropertyType());

        if (sewerageRatesSearch.getFromDate() != null)
            try {
                effectivefromDate = newFormat.format(formatter.parse(sewerageRatesSearch.getFromDate()));
            } catch (final ParseException e) {
                LOG.error("Parse Exception " + e);
            }

        IOUtils.write("{ \"data\":" + new GsonBuilder().setDateFormat(applicationProperties.defaultDatePattern()).create()
                .toJson(sewerageRatesMasterService.getSewerageMasters(type, effectivefromDate, sewerageRatesSearch.getStatus()))
                + "}", response.getWriter());
    }

    @RequestMapping(value = "update/{id}", method = GET)
    public String updateSewerageRates(@ModelAttribute final SewerageRatesMaster sewerageRatesMaster, @PathVariable final Long id,
            final Model model) {
        final SewerageRatesMaster existingratesMaster = sewerageRatesMasterService.findBy(id);
        model.addAttribute(SEWERAGE_RATES_MASTER, existingratesMaster);
        return "sewerageRates-update";
    }

    @RequestMapping(value = "update/{id}", method = POST)
    public String update(@ModelAttribute final SewerageRatesMaster sewerageRatesMaster, @PathVariable final Long id,
            final Model model, final RedirectAttributes redirectAttrs, final BindingResult errors) throws ParseException {
        SimpleDateFormat newFormat = new SimpleDateFormat("dd-MM-yyyy");
     
        final SewerageRatesMaster ratesMaster = sewerageRatesMasterService.findBy(id);

        final String todaysdate = newFormat.format(new Date());
        final String effectiveFromDate = newFormat.format(ratesMaster.getFromDate());
        final Date currentDate = newFormat.parse(todaysdate);
        final Date effectiveDate = newFormat.parse(effectiveFromDate);
        sewerageMasterDataValidator.validateSewerageMonthlyRateUpdate(sewerageRatesMaster, errors);
        if (errors.hasErrors()) {
            model.addAttribute("sewerageRatesMaster", sewerageRatesMaster);
            return "sewerageRates-update";
        }

        if (effectiveDate.compareTo(currentDate) < 0) {
            model.addAttribute(MESSAGE, "msg.seweragerate.modification.rejected");
            return "sewerageRates-update";
        }
        ratesMaster.setMonthlyRate(sewerageRatesMaster.getMonthlyRate());
        sewerageRatesMasterService.update(ratesMaster);
        redirectAttrs.addFlashAttribute(MESSAGE, "msg.seweragemonthlyrate.update.success");
        return SEWERAGE_RATES_SUCCESS_PAGE + id;
    }

    @RequestMapping(value = "/fromDateValues-by-propertyType", method = GET, produces = APPLICATION_JSON_VALUE)
    @ResponseBody
    public List<Date> effectiveFromDates(@RequestParam final PropertyType propertyType) {
        return sewerageRatesMasterService.findFromDateByPropertyType(propertyType);
    }

}