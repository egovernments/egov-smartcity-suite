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
package org.egov.stms.web.controller.masters;


import org.egov.commons.CFinancialYear;
import org.egov.commons.service.FinancialYearService;
import org.egov.infra.utils.DateUtils;
import org.egov.stms.masters.entity.SewerageRatesMaster;
import org.egov.stms.masters.entity.SewerageRatesMasterDetails;
import org.egov.stms.masters.entity.enums.PropertyType;
import org.egov.stms.masters.pojo.SewerageRateComparatorOrderById;
import org.egov.stms.masters.service.SewerageRatesMasterService;
import org.egov.stms.web.controller.utils.SewerageMasterDataValidator;
import org.joda.time.DateTime;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.math.BigDecimal;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.List;

import static org.egov.stms.utils.constants.SewerageTaxConstants.DATEFORMATHYPEN;
import static org.egov.stms.utils.constants.SewerageTaxConstants.MESSAGE;
import static org.egov.stms.utils.constants.SewerageTaxConstants.SEWERAGE_RATES_SUCCESS_PAGE;
import static org.springframework.http.MediaType.APPLICATION_JSON_VALUE;

@Controller
@RequestMapping(value = "/masters")
public class CreateSewerageRateMasterController {
    private static final String SEWERAGE_RATES_MASTER = "sewerageRatesMaster";
    @Autowired
    private SewerageRatesMasterService sewerageRatesMasterService;

    @Autowired
    private FinancialYearService financialYearService;

    @Autowired
    private SewerageMasterDataValidator sewerageMasterDataValidator;

    @GetMapping("/seweragerates")
    public String showForm(
            final Model model) {
        final CFinancialYear financialYear = financialYearService.getCurrentFinancialYear();
        if (financialYear != null)
            model.addAttribute("endDate", financialYear.getEndingDate());
        model.addAttribute("propertyTypes", PropertyType.values());
        model.addAttribute(SEWERAGE_RATES_MASTER, new SewerageRatesMaster());
        if (sewerageRatesMasterService.getMultipleClosetAppconfigValue())
            return "seweragemonthlyRates-master";
        else
            return "sewerageRates-master";
    }

    @PostMapping("/seweragerates")
    public String create(
            @ModelAttribute final SewerageRatesMaster sewerageRatesMaster,
            final RedirectAttributes redirectAttrs, final Model model,
            final BindingResult resultBinder) {

        Boolean isMultipleRatesAllowed = sewerageRatesMasterService.getMultipleClosetAppconfigValue();
        sewerageMasterDataValidator.validateMonthlyRate(sewerageRatesMaster, isMultipleRatesAllowed, resultBinder);
        if (resultBinder.hasErrors()) {
            model.addAttribute("monthlyRate", sewerageRatesMaster.getMonthlyRate());
            model.addAttribute(SEWERAGE_RATES_MASTER, sewerageRatesMaster);
            return isMultipleRatesAllowed ? "seweragemonthlyRates-master" : "sewerageRates-master";
        }
        final SewerageRatesMaster sewerageRatesMasterExisting = sewerageRatesMasterService
                .findByPropertyTypeAndFromDateAndActive(
                        sewerageRatesMaster.getPropertyType(),
                        sewerageRatesMaster.getFromDate(), true);
        if (sewerageRatesMasterExisting == null) {
            final List<SewerageRatesMaster> existingSewerageRatesMasterList = sewerageRatesMasterService
                    .getLatestActiveRecord(sewerageRatesMaster.getPropertyType());
            if (!existingSewerageRatesMasterList.isEmpty()) {
                SewerageRatesMaster sewerageRatesMasterOld = existingSewerageRatesMasterList.get(0);
                if (sewerageRatesMasterOld != null) {
                    if (sewerageRatesMaster.getFromDate().compareTo(new Date()) < 0)
                        sewerageRatesMasterOld.setActive(false);
                    final DateTime oldDate = DateUtils.endOfGivenDate(new DateTime(sewerageRatesMaster.getFromDate()).minusDays(1));
                    final Date formattedToDate = oldDate.toDate();
                    sewerageRatesMasterOld.setToDate(formattedToDate);
                    sewerageRatesMasterService.update(sewerageRatesMasterOld);
                }
            }
            saveSewerageMaster(sewerageRatesMaster);
        } else {
            model.addAttribute("existingMonthlyRate", sewerageRatesMasterExisting.getMonthlyRate());
            sewerageRatesMasterExisting.setActive(false);
            /* append the end time of given date as 23:59:59 */
            final DateTime dateValue = DateUtils.endOfGivenDate(new DateTime(sewerageRatesMaster.getFromDate()));
            final Date formattedToDate = dateValue.toDate();
            sewerageRatesMasterExisting.setToDate(formattedToDate);
            sewerageRatesMasterService.update(sewerageRatesMasterExisting);
            saveSewerageMaster(sewerageRatesMaster);
        }
        redirectAttrs.addFlashAttribute(MESSAGE, "msg.seweragemonthlyrate.creation.success");
        return SEWERAGE_RATES_SUCCESS_PAGE + sewerageRatesMaster.getId();

    }

    private void saveSewerageMaster(@ModelAttribute SewerageRatesMaster sewerageRatesMaster) {
        List<SewerageRatesMasterDetails> sewerageRatesMasterDetailsList = new ArrayList<>();
        sewerageRatesMaster.setActive(true);
        sewerageRatesMaster.getSewerageDetailMaster().removeIf(SewerageRatesMasterDetails::isMarkedForRemoval);
        sewerageRatesMaster.getSewerageDetailMaster().stream().forEach(sewerageRatesMasterDetails -> {
            sewerageRatesMasterDetails.setSewerageRateMaster(sewerageRatesMaster);
            sewerageRatesMasterDetailsList.add(sewerageRatesMasterDetails);
        });
        sewerageRatesMaster.getSewerageDetailMaster().clear();
        sewerageRatesMaster.getSewerageDetailMaster().addAll(sewerageRatesMasterDetailsList);
        sewerageRatesMasterService.create(sewerageRatesMaster);
    }

    @GetMapping("/getseweragerates/{id}")
    public String getSewerageRates(@PathVariable("id") final Long id, final Model model) {
        final SewerageRatesMaster sewerageRatesMaster = sewerageRatesMasterService.findBy(id);
        Boolean isMultipleClosetRatesAllowed = sewerageRatesMasterService.getMultipleClosetAppconfigValue();
        if (isMultipleClosetRatesAllowed != null && isMultipleClosetRatesAllowed) {
            for (final SewerageRatesMasterDetails swdm : sewerageRatesMaster.getSewerageDetailMaster())
                swdm.setAmount(BigDecimal.valueOf(swdm.getAmount()).setScale(2, BigDecimal.ROUND_HALF_EVEN).doubleValue());

            Collections.sort(sewerageRatesMaster.getSewerageDetailMaster(), new SewerageRateComparatorOrderById());
        }
        model.addAttribute(SEWERAGE_RATES_MASTER, sewerageRatesMaster);
        return isMultipleClosetRatesAllowed ? "seweragemonthlyRates-success" : "sewerageRates-success";
    }

    @GetMapping(value = "/ajaxexistingseweragevalidate", produces = APPLICATION_JSON_VALUE)
    @ResponseBody
    public double getSewerageRatesByAllCombinations(@RequestParam("propertyType") final PropertyType propertyType,
                                                    @RequestParam("fromDate") final Date fromDate) {
        SewerageRatesMaster sewerageRatesMasterExist;
        sewerageRatesMasterExist = sewerageRatesMasterService
                .findByPropertyTypeAndFromDateAndActive(propertyType, fromDate, true);
        if (sewerageRatesMasterExist == null)
            return 0;
        else
            return sewerageRatesMasterExist.getMonthlyRate() == null ? 1 : sewerageRatesMasterExist.getMonthlyRate();
    }

    @GetMapping(value = "/fromDateValidationWithLatestActiveRecord", produces = APPLICATION_JSON_VALUE)
    @ResponseBody
    public String getLatestActiveFromDate(@RequestParam("propertyType") final PropertyType propertyType,
                                          @RequestParam("fromDate") final Date fromDate) {
        final SimpleDateFormat newFormat = new SimpleDateFormat(DATEFORMATHYPEN);

        final List<SewerageRatesMaster> existingsewerageRatesMasterList = sewerageRatesMasterService
                .getLatestActiveRecord(propertyType);
        if (!existingsewerageRatesMasterList.isEmpty() && fromDate != null) {
            final SewerageRatesMaster existingActiveSewerageRatesObject = existingsewerageRatesMasterList.get(0);

            if (fromDate.compareTo(existingActiveSewerageRatesObject.getFromDate()) < 0)
                return newFormat.format(existingActiveSewerageRatesObject.getFromDate());
        }
        return "true";

    }

    @GetMapping(value = "/fromDateValues-by-propertyType", produces = APPLICATION_JSON_VALUE)
    @ResponseBody
    public List<Date> effectiveFromDates(@RequestParam final PropertyType propertyType) {
        return sewerageRatesMasterService.findFromDateByPropertyType(propertyType);
    }
}