/**
 * eGov suite of products aim to improve the internal efficiency,transparency,
 * accountability and the service delivery of the government  organizations.
 * <p>
 * Copyright (C) <2015>  eGovernments Foundation
 * <p>
 * The updated version of eGov suite of products as by eGovernments Foundation
 * is available at http://www.egovernments.org
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * any later version.
 * <p>
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 * <p>
 * You should have received a copy of the GNU General Public License
 * along with this program. If not, see http://www.gnu.org/licenses/ or
 * http://www.gnu.org/licenses/gpl.html .
 * <p>
 * In addition to the terms of the GPL license to be adhered to in using this
 * program, the following additional terms are to be complied with:
 * <p>
 * 1) All versions of this program, verbatim or modified must carry this
 * Legal Notice.
 * <p>
 * 2) Any misrepresentation of the origin of the material is prohibited. It
 * is required that all modified versions of this material be marked in
 * reasonable ways as different from the original version.
 * <p>
 * 3) This license does not grant any rights to any user of the program
 * with regards to rights under trademark law for use of the trade names
 * or trademarks of eGovernments Foundation.
 * <p>
 * In case of any queries, you can reach eGovernments Foundation at contact@egovernments.org.
 */
package org.egov.stms.web.controller.masters;

import com.google.gson.GsonBuilder;
import org.apache.commons.io.IOUtils;
import org.egov.commons.CFinancialYear;
import org.egov.commons.service.FinancialYearService;
import org.egov.infra.admin.master.entity.AppConfigValues;
import org.egov.infra.config.core.GlobalSettings;
import org.egov.infra.utils.DateUtils;
import org.egov.stms.masters.entity.SewerageRatesMaster;
import org.egov.stms.masters.entity.SewerageRatesMasterDetails;
import org.egov.stms.masters.entity.enums.PropertyType;
import org.egov.stms.masters.entity.enums.SewerageRateStatus;
import org.egov.stms.masters.pojo.SewerageRateComparatorOrderById;
import org.egov.stms.masters.pojo.SewerageRatesSearch;
import org.egov.stms.masters.service.SewerageRatesMasterService;
import org.egov.stms.utils.constants.SewerageTaxConstants;
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

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.math.BigDecimal;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.List;

import static org.springframework.http.MediaType.APPLICATION_JSON_VALUE;
import static org.springframework.web.bind.annotation.RequestMethod.GET;
import static org.springframework.web.bind.annotation.RequestMethod.POST;

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
    private SewerageMasterDataValidator sewerageMasterDataValidator;

    private Boolean getAppconfigValue() {
        final AppConfigValues appconfigvalue = sewerageRatesMasterService.getAppConfigValuesForSeweargeRate(
                SewerageTaxConstants.MODULE_NAME, SewerageTaxConstants.SEWERAGE_MONTHLY_RATES);
        if (appconfigvalue != null && appconfigvalue.getValue().equalsIgnoreCase("YES"))
            return false;
        else
            return true;

    }

    @RequestMapping(value = "/seweragerates", method = RequestMethod.GET)
    public String showForm(
            final Model model) {
        final CFinancialYear financialYear = financialYearService.getCurrentFinancialYear();
        if (financialYear != null)
            model.addAttribute("endDate", financialYear.getEndingDate());
        model.addAttribute("propertyTypes", PropertyType.values());
        model.addAttribute("sewerageRatesMaster", new SewerageRatesMaster());
        if (getAppconfigValue())
            return "sewerageRates-master";
        else
            return "seweragemonthlyRates-master";
    }

    @RequestMapping(value = "/seweragerates", method = RequestMethod.POST)
    public String create(
            @ModelAttribute final SewerageRatesMaster sewerageRatesMaster,
            final RedirectAttributes redirectAttrs, final Model model,
            final BindingResult resultBinder) {

        final List<SewerageRatesMasterDetails> sewerageRatesMasterDetailslist = new ArrayList<>();
        sewerageMasterDataValidator.validateMonthlyRate(sewerageRatesMaster, getAppconfigValue(), resultBinder);
        if (resultBinder.hasErrors()) {
            model.addAttribute("monthlyRate", sewerageRatesMaster.getMonthlyRate());
            model.addAttribute(SEWERAGE_RATES_MASTER, sewerageRatesMaster);
            if (getAppconfigValue())
                return "sewerageRates-master";
            else
                return "seweragemonthlyRates-master";
        }

        final List<SewerageRatesMaster> existingsewerageRatesMasterList = sewerageRatesMasterService
                .getLatestActiveRecord(sewerageRatesMaster.getPropertyType(), true);

        final SewerageRatesMaster sewerageRatesMasterExisting = sewerageRatesMasterService
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
            for (final SewerageRatesMasterDetails sewerageRatesMasterDetails : sewerageRatesMaster.getSewerageDetailmaster()) {
                sewerageRatesMasterDetails.setSewerageratemaster(sewerageRatesMaster);
                sewerageRatesMasterDetailslist.add(sewerageRatesMasterDetails);
            }
            sewerageRatesMaster.getSewerageDetailmaster().addAll(sewerageRatesMasterDetailslist);
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
            for (final SewerageRatesMasterDetails sewerageRatesMasterDetails : sewerageRatesMaster.getSewerageDetailmaster()) {
                sewerageRatesMasterDetails.setSewerageratemaster(sewerageRatesMaster);
                sewerageRatesMasterDetailslist.add(sewerageRatesMasterDetails);
            }
            sewerageRatesMaster.getSewerageDetailmaster().clear();
            sewerageRatesMaster.getSewerageDetailmaster().addAll(sewerageRatesMasterDetailslist);
            sewerageRatesMasterService.create(sewerageRatesMaster);
        }
        redirectAttrs.addFlashAttribute(MESSAGE, "msg.seweragemonthlyrate.creation.success");
        return SEWERAGE_RATES_SUCCESS_PAGE + sewerageRatesMaster.getId();

    }

    @RequestMapping(value = "/getseweragerates/{id}", method = RequestMethod.GET)
    public String getSeweragerates(@PathVariable("id") final Long id, final Model model) {
        final SewerageRatesMaster sewerageRatesMaster = sewerageRatesMasterService.findBy(id);
        if (getAppconfigValue() != null && !getAppconfigValue()) {
            for (final SewerageRatesMasterDetails swdm : sewerageRatesMaster.getSewerageDetailmaster())
                swdm.setAmount(BigDecimal.valueOf(swdm.getAmount()).setScale(2, BigDecimal.ROUND_HALF_EVEN).doubleValue());

            Collections.sort(sewerageRatesMaster.getSewerageDetailmaster(), new SewerageRateComparatorOrderById());
        }
        model.addAttribute(SEWERAGE_RATES_MASTER, sewerageRatesMaster);
        if (getAppconfigValue())

            return "sewerageRates-success";
        else
            return "seweragemonthlyRates-success";
    }

    @RequestMapping(value = "/ajaxexistingseweragevalidate", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
    @ResponseBody
    public double getSewerageRatesByAllCombinatons(@RequestParam("propertyType") final PropertyType propertyType,
                                                   @RequestParam("fromDate") final Date fromDate) {
        SewerageRatesMaster sewerageRatesMasterExist;
        sewerageRatesMasterExist = sewerageRatesMasterService
                .findByPropertyTypeAndFromDateAndActive(propertyType, fromDate, true);
        if (sewerageRatesMasterExist != null)
            return sewerageRatesMasterExist.getMonthlyRate() != null ? sewerageRatesMasterExist.getMonthlyRate() : 1;
        else
            return 0;
    }

    @RequestMapping(value = "/fromDateValidationWithLatestActiveRecord", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
    @ResponseBody
    public String getLatestActiveFromDate(@RequestParam("propertyType") final PropertyType propertyType,
                                          @RequestParam("fromDate") final Date fromDate) {
        final SimpleDateFormat newFormat = new SimpleDateFormat("dd-MM-yyyy");

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
        final SimpleDateFormat formatter = new SimpleDateFormat("dd/MM/yyyy");
        final SimpleDateFormat newFormat = new SimpleDateFormat("dd-MM-yyyy");
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
        final List<SewerageRatesSearch> seweragesearchlist = sewerageRatesMasterService.getSewerageMasters(type,
                effectivefromDate,
                sewerageRatesSearch.getStatus());
        final List<SewerageRatesSearch> displyalist = new ArrayList<>();
        for (final SewerageRatesSearch seRatesSearch : seweragesearchlist)
            if (getAppconfigValue() && seRatesSearch.getMonthlyRate() != null)
                displyalist.add(seRatesSearch);
            else if (!getAppconfigValue() && seRatesSearch.getMonthlyRate() == null)
                displyalist.add(seRatesSearch);
        IOUtils.write("{ \"data\":" + new GsonBuilder().setDateFormat(GlobalSettings.datePattern()).create()
                .toJson(displyalist)
                + "}", response.getWriter());
    }

    @RequestMapping(value = "update/{id}", method = GET)
    public String updateSewerageRates(@ModelAttribute final SewerageRatesMaster sewerageRatesMaster, @PathVariable final Long id,
                                      final Model model) {
        final SewerageRatesMaster existingratesMaster = sewerageRatesMasterService.findBy(id);
        model.addAttribute(SEWERAGE_RATES_MASTER, existingratesMaster);
        if (getAppconfigValue())
            return "sewerageRates-update";
        else {
            Collections.sort(existingratesMaster.getSewerageDetailmaster(), new SewerageRateComparatorOrderById());
            model.addAttribute(SEWERAGE_RATES_MASTER, existingratesMaster);
            model.addAttribute("sewerageDetailmaster", existingratesMaster.getSewerageDetailmaster());
            return "seweragemonthlyRates-update";
        }
    }

    @RequestMapping(value = "update/{id}", method = POST)
    public String update(@ModelAttribute final SewerageRatesMaster sewerageRatesMaster, @PathVariable final Long id,
                         final Model model, final RedirectAttributes redirectAttrs, final BindingResult errors) throws ParseException {
        final SimpleDateFormat newFormat = new SimpleDateFormat("dd-MM-yyyy");

        final SewerageRatesMaster ratesMaster = sewerageRatesMasterService.findBy(id);

        final String todaysdate = newFormat.format(new Date());
        final String effectiveFromDate = newFormat.format(ratesMaster.getFromDate());
        final Date currentDate = newFormat.parse(todaysdate);
        final Date effectiveDate = newFormat.parse(effectiveFromDate);
        if (getAppconfigValue())
            sewerageMasterDataValidator.validateSewerageMonthlyRateUpdate(sewerageRatesMaster, errors);
        if (errors.hasErrors()) {
            model.addAttribute("sewerageRatesMaster", sewerageRatesMaster);
            if (getAppconfigValue())
                return "seweragemonthlyRates-update";
            else
                return "sewerageRates-update";
        }

        if (effectiveDate.compareTo(currentDate) < 0) {
            model.addAttribute(MESSAGE, "msg.seweragerate.modification.rejected");
            return "sewerageRates-update";
        }
        if (getAppconfigValue()) {

            final List<SewerageRatesMasterDetails> existingSewerageDetailList = new ArrayList<>();
            if (sewerageRatesMaster != null && !sewerageRatesMaster.getSewerageDetailmaster().isEmpty())
                existingSewerageDetailList.addAll(ratesMaster.getSewerageDetailmaster());
            if (sewerageRatesMaster != null && sewerageRatesMaster.getSewerageDetailmaster() != null)
                sewerageRatesMasterService.updateSewerageRateMaster(sewerageRatesMaster, ratesMaster, existingSewerageDetailList);
        } else {
            ratesMaster.setMonthlyRate(sewerageRatesMaster.getMonthlyRate());
            sewerageRatesMasterService.update(ratesMaster);
        }
        redirectAttrs.addFlashAttribute(MESSAGE, "msg.seweragemonthlyrate.update.success");
        return SEWERAGE_RATES_SUCCESS_PAGE + id;
    }

    @RequestMapping(value = "/fromDateValues-by-propertyType", method = GET, produces = APPLICATION_JSON_VALUE)
    @ResponseBody
    public List<Date> effectiveFromDates(@RequestParam final PropertyType propertyType) {
        return sewerageRatesMasterService.findFromDateByPropertyType(propertyType);
    }

}