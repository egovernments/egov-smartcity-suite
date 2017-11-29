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

package org.egov.stms.web.controller.utils;

import org.egov.stms.masters.entity.DonationMaster;
import org.egov.stms.masters.entity.SewerageRatesMaster;
import org.egov.stms.masters.service.DonationMasterService;
import org.egov.stms.masters.service.SewerageRatesMasterService;
import org.jfree.util.Log;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.validation.Errors;
import org.springframework.validation.ValidationUtils;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Component
public class SewerageMasterDataValidator extends SewerageApplicationCommonValidator {

    private static final String NOTEMPTY_SEWERAGE_PROPERTYTYPE = "err.propertytype.required.validate";
    private static final String NOTEMPTY_SEWERAGE_FROMDATE = "notempty.sewerage.fromdate";
    private static final String NOTEMPTY_SEWERAGE_NOOFCLOSETS = "notempty.sewerage.noofclosets";
    private static final String NOTEMPTY_SEWERAGE_DONATIONAMT = "notempty.sewerage.donationamount";
    private static final Logger LOG = LoggerFactory.getLogger(SewerageMasterDataValidator.class);
    private static final String SEWERAGE_FROMDATE_LESSTHAN_CURRENTDATE = "err.validate.effective.date";
    private static final String SEWERAGE_FROMDATE_LESSTHAN_ACTIVEDATE = "err.fromdate.lessthan.activedate";
    private static final String SEWERAGE_NOOFCLOSETS_NONZERO = "err.numberofclosets.reject.0";
    private static final String SEWERAGE_NOOFCLOSETS_NONDUPLICATE = "err.validate.duplicatenoofclosets";
    private static final String FROMDATE = "fromDate";
    private static final String NOTEMPTY_SEWERAGE_MONTHLY_RATE = "notempty.sewerage.monthlyrate";

    SimpleDateFormat formatter = new SimpleDateFormat("dd/MM/yyyy");
    SimpleDateFormat newFormat = new SimpleDateFormat("dd-MM-yyyy");

    @Autowired
    private DonationMasterService donationMasterService;

    @Autowired
    private SewerageRatesMasterService sewerageRatesMasterService;

    // validate sewerage monthly rate screen inputs
    public void validateMonthlyRate(final SewerageRatesMaster sewerageRatesMaster, final Boolean swgRateappconfig, final Errors errors) {

        ValidationUtils.rejectIfEmptyOrWhitespace(errors, "propertyType", NOTEMPTY_SEWERAGE_PROPERTYTYPE);
        ValidationUtils.rejectIfEmptyOrWhitespace(errors, "fromDate", NOTEMPTY_SEWERAGE_FROMDATE);
        if (swgRateappconfig)
        ValidationUtils.rejectIfEmptyOrWhitespace(errors, "monthlyRate", NOTEMPTY_SEWERAGE_MONTHLY_RATE);

        try {
            if (sewerageRatesMaster.getFromDate() != null) {
                final Date currentDate = newFormat.parse(newFormat.format(new Date()));
                final List<SewerageRatesMaster> sewerageRateList = sewerageRatesMasterService.getLatestActiveRecord(
                        sewerageRatesMaster.getPropertyType(), true);
                if (!sewerageRateList.isEmpty()) {
                    final SewerageRatesMaster activeRecord = sewerageRateList.get(0);
                    validateSewerageRateFromDate(sewerageRatesMaster, errors, currentDate, activeRecord);
                }
            }
        } catch (final ParseException e) {
            Log.error("Exception in parsing date " + e);
        }

    }

    // validate sewerage effective from rate
    private void validateSewerageRateFromDate(final SewerageRatesMaster sewerageRatesMaster, final Errors errors,
            final Date currentDate, final SewerageRatesMaster activeRecord) {
        if (activeRecord.getFromDate().compareTo(currentDate) <= 0
                && sewerageRatesMaster.getFromDate().compareTo(currentDate) < 0)
            errors.rejectValue(FROMDATE, SEWERAGE_FROMDATE_LESSTHAN_CURRENTDATE);
        else if (activeRecord.getFromDate().compareTo(new Date()) >= 0
                && sewerageRatesMaster.getFromDate().compareTo(activeRecord.getFromDate()) < 0)
            errors.rejectValue(FROMDATE, SEWERAGE_FROMDATE_LESSTHAN_ACTIVEDATE,
                    new String[] { formatter.format(activeRecord.getFromDate()) },
                    SEWERAGE_FROMDATE_LESSTHAN_ACTIVEDATE);
    }

    // validate sewerage monthly rate update screen inputs
    public void validateSewerageMonthlyRateUpdate(final SewerageRatesMaster sewerageRatesMaster, final Errors errors) {
        ValidationUtils.rejectIfEmptyOrWhitespace(errors, "monthlyRate", "notempty.sewerage.monthlyrate");
    }

    // validate sewerage donation master screen input
    public void validateDonationMaster(final Errors errors, final DonationMaster donationMaster) {
        final List<Integer> noOfClosetArray = new ArrayList<>();
        ValidationUtils.rejectIfEmptyOrWhitespace(errors, "propertyType", NOTEMPTY_SEWERAGE_PROPERTYTYPE);
        ValidationUtils.rejectIfEmptyOrWhitespace(errors, FROMDATE, NOTEMPTY_SEWERAGE_FROMDATE);

        for (int i = 0; i < donationMaster.getDonationDetail().size(); i++) {
            ValidationUtils.rejectIfEmptyOrWhitespace(errors, "donationDetail[" + i + "].amount", NOTEMPTY_SEWERAGE_DONATIONAMT);
            ValidationUtils.rejectIfEmptyOrWhitespace(errors, "donationDetail[" + i + "].noOfClosets",
                    NOTEMPTY_SEWERAGE_NOOFCLOSETS);

        }

        if (donationMaster.getFromDate() != null) {
            Date currentDate;
            try {
                currentDate = newFormat.parse(newFormat.format(new Date()));
                final List<DonationMaster> donationList = donationMasterService.getLatestActiveRecordByPropertyTypeAndActive(
                        donationMaster.getPropertyType(), true);
                if (!donationList.isEmpty())
                    validateDonationMasterFromDate(errors, donationMaster, newFormat, currentDate, donationList);

                for (int i = 0; i < donationMaster.getDonationDetail().size(); i++)
                    validateNumberOfClosets(errors, donationMaster, noOfClosetArray, i);
            } catch (final ParseException e) {
                LOG.error("Parse Exception" + e);
            }

        }
    }

    // validate donation master update screen inputs
    public void validateDonationMasterUpdate(final Errors errors, final DonationMaster donationMaster) {
        final List<Integer> noOfClosetArray = new ArrayList<>();
        for (int i = 0; i < donationMaster.getDonationDetail().size(); i++) {
            if (donationMaster.getDonationDetail().get(i).getNoOfClosets() == null)
                errors.rejectValue("donationDetail[" + i + "].noOfClosets", NOTEMPTY_SEWERAGE_NOOFCLOSETS);
            if (donationMaster.getDonationDetail().get(i).getAmount() == null)
                errors.rejectValue("donationDetail[" + i + "].amount", NOTEMPTY_SEWERAGE_DONATIONAMT);
            validateNumberOfClosets(errors, donationMaster, noOfClosetArray, i);
        }
    }

    // validate donation master number of closets
    private void validateNumberOfClosets(final Errors errors, final DonationMaster donationMaster,
            final List<Integer> noOfClosetArray,
            final int i) {
        if (donationMaster.getDonationDetail().get(i).getNoOfClosets() != null)
            if (donationMaster.getDonationDetail().get(i).getNoOfClosets() == 0)
                errors.rejectValue("donationDetail[" + i + "].noOfClosets", SEWERAGE_NOOFCLOSETS_NONZERO);
            else if (noOfClosetArray.contains(donationMaster.getDonationDetail().get(i).getNoOfClosets()))
                errors.rejectValue("donationDetail[" + i + "].noOfClosets", SEWERAGE_NOOFCLOSETS_NONDUPLICATE,
                        new String[] { donationMaster.getDonationDetail().get(i).getNoOfClosets().toString() },
                        null);
            else
                noOfClosetArray.add(donationMaster.getDonationDetail().get(i).getNoOfClosets());
    }

    // validate donation master screen effective from date
    private void validateDonationMasterFromDate(final Errors errors, final DonationMaster donationMaster,
            final SimpleDateFormat formatter,
            final Date currentDate, final List<DonationMaster> donationList) {
        final DonationMaster existingActiveRecord = donationList.get(0);
        if (existingActiveRecord.getFromDate().compareTo(currentDate) <= 0
                && donationMaster.getFromDate().compareTo(currentDate) < 0)
            errors.rejectValue(FROMDATE, SEWERAGE_FROMDATE_LESSTHAN_CURRENTDATE);

        else if (existingActiveRecord.getFromDate().compareTo(new Date()) >= 0
                && donationMaster.getFromDate().compareTo(existingActiveRecord.getFromDate()) < 0)
            errors.rejectValue(FROMDATE, SEWERAGE_FROMDATE_LESSTHAN_ACTIVEDATE,
                    new String[] { formatter.format(existingActiveRecord.getFromDate()) }, SEWERAGE_FROMDATE_LESSTHAN_ACTIVEDATE);
    }

}
