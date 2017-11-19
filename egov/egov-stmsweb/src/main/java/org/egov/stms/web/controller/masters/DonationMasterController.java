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

import com.google.gson.GsonBuilder;
import org.apache.commons.io.IOUtils;
import org.egov.commons.CFinancialYear;
import org.egov.commons.service.FinancialYearService;
import org.egov.infra.config.core.LocalizationSettings;
import org.egov.infra.utils.DateUtils;
import org.egov.stms.masters.entity.DonationDetailMaster;
import org.egov.stms.masters.entity.DonationMaster;
import org.egov.stms.masters.entity.enums.PropertyType;
import org.egov.stms.masters.entity.enums.SewerageRateStatus;
import org.egov.stms.masters.pojo.DonationMasterSearch;
import org.egov.stms.masters.pojo.DonationRateComparatorOrderById;
import org.egov.stms.masters.service.DonationMasterService;
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
public class DonationMasterController {

    private static final Logger LOG = LoggerFactory.getLogger(DonationMasterController.class);
    private static final String DONATIONMASTER = "donationMaster";
    private static final String REDIRECT_TO_SUCCESS_PAGE = "redirect:/masters/success/";
    private static final String MESSAGE = "message";
    private static final String DONATION_MASTER_UPDATE = "donation-master-update";

    @Autowired
    private DonationMasterService donationMasterService;

    @Autowired
    private FinancialYearService financialYearService;

    @Autowired
    private SewerageMasterDataValidator sewerageMasterDataValidator;

    @RequestMapping(value = "/donationmaster", method = RequestMethod.GET)
    public String showForm(@ModelAttribute final DonationMaster donationMaster, final Model model) {
        final DonationMaster donationMasterObj = new DonationMaster();
        final CFinancialYear financialYear = financialYearService.getCurrentFinancialYear();
        if (financialYear != null)
            model.addAttribute("endDate", financialYear.getEndingDate());
        model.addAttribute(DONATIONMASTER, donationMasterObj);
        model.addAttribute("propertyTypes", PropertyType.values());

        return "donation-master";
    }

    @RequestMapping(value = "/donationmaster", method = RequestMethod.POST, produces = MediaType.APPLICATION_JSON_VALUE)
    public String createDonationMaster(@ModelAttribute final DonationMaster donationMaster,
                                       final BindingResult errors, final RedirectAttributes redirectAttrs, final Model model) {
        final List<DonationDetailMaster> donationMasterDetailList = new ArrayList<>();
        DonationMaster donationMasterObj;
        sewerageMasterDataValidator.validateDonationMaster(errors, donationMaster);
        if (errors.hasErrors()) {
            model.addAttribute(DONATIONMASTER, donationMaster);
            return "donation-master";
        }
        final List<DonationMaster> existingdonationMaster = donationMasterService
                .getLatestActiveRecordByPropertyTypeAndActive(donationMaster.getPropertyType(), true);

        final DonationMaster donationMasterExist = donationMasterService.findByPropertyTypeAndFromDateAndActive(
                donationMaster.getPropertyType(),
                donationMaster.getFromDate(), true);
        // overwrite existing combination with propertyType, fromDate, isActive = true
        if (donationMasterExist != null) {
            final DateTime dateTime = DateUtils.endOfGivenDate(new DateTime(donationMaster.getFromDate()));
            final Date dateformat = dateTime.toDate();
            donationMasterExist.setActive(false);
            donationMasterExist.setToDate(dateformat);
            donationMaster.setActive(true);
            for (final DonationDetailMaster donationDetailMaster : donationMaster.getDonationDetail()) {
                donationDetailMaster.setDonation(donationMaster);
                donationMasterDetailList.add(donationDetailMaster);
            }

            donationMaster.getDonationDetail().addAll(donationMasterDetailList);
            donationMasterObj = donationMasterService.createDonationRate(donationMaster);

        } else {
            // set todate for the record with same propertyType and isActive = true and create new record
            DonationMaster donationMasterOld = null;
            if (!existingdonationMaster.isEmpty())
                donationMasterOld = existingdonationMaster.get(0);
            if (donationMasterOld != null) {
                if (donationMaster.getFromDate().compareTo(new Date()) < 0)
                    donationMasterOld.setActive(false);
                // sets the endofGiven date as 23:59:59
                final DateTime dateTime = DateUtils.endOfGivenDate(new DateTime(donationMaster.getFromDate()).minusDays(1));
                final Date dateformat = dateTime.toDate();
                donationMasterOld.setToDate(dateformat);
                donationMasterService.update(donationMasterOld);
            }
            donationMaster.setActive(true);
            for (final DonationDetailMaster donationDetailMaster : donationMaster.getDonationDetail()) {
                donationDetailMaster.setDonation(donationMaster);
                donationMasterDetailList.add(donationDetailMaster);
            }
            donationMaster.getDonationDetail().clear();
            donationMaster.getDonationDetail().addAll(donationMasterDetailList);
            donationMasterObj = donationMasterService.createDonationRate(donationMaster);
        }
        redirectAttrs.addFlashAttribute(MESSAGE, "msg.donationrate.creation.success");
        return REDIRECT_TO_SUCCESS_PAGE + donationMasterObj.getId();
    }

    @RequestMapping(value = "/success/{id}", method = RequestMethod.GET)
    public String getSeweragerates(@ModelAttribute final DonationMaster donationMaster, @PathVariable("id") final Long id,
                                   final Model model) {
        final DonationMaster donationMaster1 = donationMasterService.findById(id);
        for (final DonationDetailMaster ddm : donationMaster1.getDonationDetail())
            ddm.setAmount(BigDecimal.valueOf(ddm.getAmount()).setScale(2, BigDecimal.ROUND_HALF_EVEN).doubleValue());
        Collections.sort(donationMaster1.getDonationDetail(), new DonationRateComparatorOrderById());
        model.addAttribute(DONATIONMASTER, donationMaster1);
        return "donation-master-success";
    }

    @RequestMapping(value = "/fromDateValidationWithActiveRecord", method = RequestMethod.GET)
    @ResponseBody
    public String validateFromDateWithActiveDate(@RequestParam("propertyType") final PropertyType propertyType,
                                                 @RequestParam("fromDate") final Date date) {
        final SimpleDateFormat formatter = new SimpleDateFormat("dd-MM-yyyy");
        if (date != null) {
            final List<DonationMaster> donationList = donationMasterService.getLatestActiveRecordByPropertyTypeAndActive(
                    propertyType,
                    true);
            if (!donationList.isEmpty()) {
                final DonationMaster existingActiveDonationObject = donationList.get(0);
                if (existingActiveDonationObject.getFromDate().compareTo(new Date()) >= 0
                        && date.compareTo(existingActiveDonationObject.getFromDate()) < 0)
                    return formatter.format(existingActiveDonationObject.getFromDate()).toString();
            }
        }
        return "true";
    }

    @RequestMapping(value = "/ajaxexistingdonationvalidate", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
    @ResponseBody
    public double geWaterRatesByAllCombinatons(@RequestParam("propertyType") final PropertyType propertyType,
                                               @RequestParam("fromDate") final Date fromDate) {
        DonationMaster donationMasterMaster;
        donationMasterMaster = donationMasterService
                .findByPropertyTypeAndFromDateAndActive(propertyType, fromDate, true);
        if (donationMasterMaster != null)
            return 1;
        else
            return 0;
    }

    @RequestMapping(value = "/view", method = RequestMethod.GET)
    public String viewDonationMaster(final Model model, @ModelAttribute final DonationMasterSearch donationMasterSearch) {
        model.addAttribute("propertyType", PropertyType.values());
        model.addAttribute("statusValues", SewerageRateStatus.values());
        return "donationMaster-view";
    }

    @RequestMapping(value = "/search-donation-master", method = GET, produces = APPLICATION_JSON_VALUE)
    @ResponseBody
    public void searchDonationMaster(@ModelAttribute final DonationMasterSearch donationMasterSearch,
                                     final HttpServletResponse response) throws IOException {
        PropertyType type = null;
        String effectivefromDate = null;
        if (donationMasterSearch.getPropertyType() != null)
            type = PropertyType.valueOf(donationMasterSearch.getPropertyType());
        final SimpleDateFormat formatter = new SimpleDateFormat("dd/MM/yyyy");
        final SimpleDateFormat myFormat = new SimpleDateFormat("dd-MM-yyyy");
        if (donationMasterSearch.getFromDate() != null)
            try {
                effectivefromDate = myFormat.format(formatter.parse(donationMasterSearch.getFromDate()));

            } catch (final ParseException e) {
                LOG.error("Parse Exception" + e);
            }
        IOUtils.write("{ \"data\":" + new GsonBuilder().setDateFormat(LocalizationSettings.datePattern()).create()
                .toJson(donationMasterService.getDonationMasters(type, effectivefromDate,
                        donationMasterSearch.getStatus()))
                + "}", response.getWriter());

    }

    @RequestMapping(value = "/fromDate-by-propertyType", method = GET, produces = APPLICATION_JSON_VALUE)
    @ResponseBody
    public List<Date> effectiveFromDates(@RequestParam final PropertyType propertyType) {
        return donationMasterService.findFromDateByPropertyType(propertyType);
    }

    @RequestMapping(value = "/donationView/{id}", method = GET)
    public String viewDonation(@PathVariable final Long id) {
        return REDIRECT_TO_SUCCESS_PAGE + id;
    }

    @RequestMapping(value = "/donationUpdate/{id}", method = GET)
    public String updateDonation(@PathVariable final Long id, final Model model) {
        final DonationMaster dm = donationMasterService.findById(id);
        Collections.sort(dm.getDonationDetail(), new DonationRateComparatorOrderById());
        model.addAttribute("donationMaster", dm);
        model.addAttribute("donationDetail", dm.getDonationDetail());
        return DONATION_MASTER_UPDATE;
    }

    @RequestMapping(value = "/donationUpdate/{id}", method = POST)
    public String updateDonationValues(@ModelAttribute final DonationMaster donationMaster, @PathVariable final Long id,
                                       final Model model, final BindingResult errors,
                                       final RedirectAttributes redirectAttrs) throws ParseException {

        sewerageMasterDataValidator.validateDonationMasterUpdate(errors, donationMaster);
        if (errors.hasErrors()) {
            model.addAttribute(DONATIONMASTER, donationMaster);
            return DONATION_MASTER_UPDATE;
        }
        final DonationMaster donationMstr = donationMasterService.findById(id);
        if (donationMstr != null) {
            final SimpleDateFormat myFormat = new SimpleDateFormat("dd-MM-yyyy");
            final String todaysdate = myFormat.format(new Date());
            final String effectiveFromDate = myFormat.format(donationMstr.getFromDate());

            final Date effectiveDate = myFormat.parse(effectiveFromDate);
            final Date currentDate = myFormat.parse(todaysdate);

            if (effectiveDate.compareTo(currentDate) < 0) {
                model.addAttribute(MESSAGE, "msg.donationrate.modification.rejected");
                return DONATION_MASTER_UPDATE;
            }
            donationMstr.setLastModifiedDate(new Date());
            final List<DonationDetailMaster> existingdonationDetailList = new ArrayList<>();
            if (donationMaster != null && !donationMaster.getDonationDetail().isEmpty())
                existingdonationDetailList.addAll(donationMstr.getDonationDetail());
            if (donationMaster != null && donationMaster.getDonationDetail() != null)
                updateDonationMaster(donationMaster, donationMstr, existingdonationDetailList);
        } else {
            model.addAttribute(MESSAGE, "msg.donationrate.notfound");
            return DONATION_MASTER_UPDATE;
        }
        redirectAttrs.addFlashAttribute(MESSAGE, "msg.donationrate.update.success");
        return REDIRECT_TO_SUCCESS_PAGE + id;
    }

    private void updateDonationMaster(final DonationMaster donationMaster, final DonationMaster donationMstr,
                                      final List<DonationDetailMaster> existingdonationDetailList) {
        if (!existingdonationDetailList.isEmpty()) {

            for (final DonationDetailMaster dtlObject : existingdonationDetailList)
                if (!donationMaster.getDonationDetail().contains(dtlObject))
                    donationMstr.deleteDonationDetail(dtlObject);
            for (final DonationDetailMaster dtlMaster : donationMaster.getDonationDetail())
                if (dtlMaster.getId() == null) {
                    final DonationDetailMaster donationDetailObject = new DonationDetailMaster();
                    donationDetailObject.setNoOfClosets(dtlMaster.getNoOfClosets());
                    donationDetailObject.setAmount(dtlMaster.getAmount());
                    donationDetailObject.setDonation(donationMstr);
                    donationMstr.addDonationDetail(donationDetailObject);
                } else if (dtlMaster.getId() != null && existingdonationDetailList.contains(dtlMaster))
                    updateDonationDetail(donationMstr, dtlMaster);
        }
        donationMasterService.update(donationMstr);
    }

    private void updateDonationDetail(final DonationMaster donationMstr, final DonationDetailMaster dtlMaster) {
        for (final DonationDetailMaster dtlObject : donationMstr.getDonationDetail())
            if (dtlObject.getId().equals(dtlMaster.getId())) {
                dtlObject.setAmount(dtlMaster.getAmount());
                dtlObject.setNoOfClosets(dtlMaster.getNoOfClosets());
            }
    }

}