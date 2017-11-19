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

package org.egov.stms.web.controller.transactions;

import static org.egov.stms.utils.constants.SewerageTaxConstants.CHANGEINCLOSETS;
import static org.egov.stms.utils.constants.SewerageTaxConstants.CLOSESEWERAGECONNECTION;

import java.math.BigDecimal;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Set;

import javax.servlet.http.HttpServletRequest;

import org.egov.commons.Installment;
import org.egov.demand.model.EgDemandReason;
import org.egov.infra.exception.ApplicationRuntimeException;
import org.egov.stms.masters.entity.enums.PropertyType;
import org.egov.stms.masters.service.DonationMasterService;
import org.egov.stms.transactions.entity.SewerageApplicationDetails;
import org.egov.stms.transactions.entity.SewerageDemandDetail;
import org.egov.stms.transactions.service.SewerageApplicationDetailsService;
import org.egov.stms.transactions.service.SewerageDemandService;
import org.egov.stms.transactions.service.SewerageThirdPartyServices;
import org.egov.stms.utils.SewerageTaxUtils;
import org.egov.stms.utils.constants.SewerageTaxConstants;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

@Controller
public class SewerageAjaxConnectionController {

    @Autowired
    private SewerageApplicationDetailsService sewerageApplicationDetailsService;

    @Autowired
    private DonationMasterService donationMasterService;

    @Autowired
    private SewerageThirdPartyServices sewerageThirdPartyServices;

    @Autowired
    private MessageSource messageSource;

    @Autowired
    private SewerageTaxUtils sewerageTaxUtils;

    @Autowired
    private SewerageDemandService sewerageDemandService;

    @RequestMapping(value = "/ajaxconnection/check-connection-exists", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
    @ResponseBody
    public String isConnectionPresentForProperty(@RequestParam final String propertyID) {
        return sewerageApplicationDetailsService.isConnectionExistsForProperty(propertyID);
    }

    @RequestMapping(value = "/ajaxconnection/check-watertax-due", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
    @ResponseBody
    public HashMap<String, Object> getWaterTaxDueAndCurrentTax(
            @RequestParam("assessmentNo") final String assessmentNo,
            final HttpServletRequest request) {
        return sewerageThirdPartyServices.getWaterTaxDueAndCurrentTax(assessmentNo, request);
    }

    @RequestMapping(value = "/ajaxconnection/check-closets-exists", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
    @ResponseBody
    public String isClosetsPresent(@RequestParam final PropertyType propertyType,
            @RequestParam final Integer noOfClosets, @RequestParam final Boolean flag) {
        if (flag)
            return donationMasterService.checkClosetsPresentForGivenCombination(PropertyType.RESIDENTIAL, noOfClosets);
        else
            return donationMasterService.checkClosetsPresentForGivenCombination(PropertyType.NON_RESIDENTIAL,
                    noOfClosets);

    }

    @RequestMapping(value = "/ajaxconnection/check-application-inworkflow/{shscNumber}", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
    @ResponseBody
    public String checkApplicationInProgress(@PathVariable final String shscNumber) {
        String validationMessage = "";
        SewerageApplicationDetails sewerageAppDtl;
        sewerageAppDtl = sewerageApplicationDetailsService.isApplicationInProgress(shscNumber);
        if (sewerageAppDtl != null && sewerageAppDtl.getApplicationType().getCode().equalsIgnoreCase(CHANGEINCLOSETS)) {
            validationMessage = messageSource.getMessage(
                    "err.validate.changenoofclosets.application.inprogress", new String[] {
                            sewerageAppDtl.getConnectionDetail().getPropertyIdentifier(), sewerageAppDtl.getApplicationNumber() },
                    null);
            return validationMessage;
        } else if (sewerageAppDtl != null
                && sewerageAppDtl.getApplicationType().getCode().equalsIgnoreCase(CLOSESEWERAGECONNECTION)) {
            validationMessage = messageSource.getMessage(
                    "err.validate.closeconnection.application.inprogress", new String[] {
                            sewerageAppDtl.getConnectionDetail().getPropertyIdentifier(), sewerageAppDtl.getApplicationNumber() },
                    null);
            return validationMessage;
        } else
            return validationMessage;
    }

    @RequestMapping(value = "/ajaxconnection/check-shscnumber-exists", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
    @ResponseBody
    public String isSHSCNumberUnique(@RequestParam final String shscNumber) {
        List<SewerageApplicationDetails> appDetailList;
        appDetailList = sewerageApplicationDetailsService.findByConnectionShscNumber(shscNumber);
        if (!appDetailList.isEmpty() && appDetailList.get(0).getConnection().getShscNumber() != null)
            return messageSource.getMessage("err.validate.shscnumber.exists", new String[] { shscNumber }, null);
        else
            return "";
    }

    @RequestMapping(value = "/ajaxconnection/getlegacy-demand-details", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
    @ResponseBody
    public List<SewerageDemandDetail> getLegacyDemandDetails(
            @RequestParam("executionDate") final String executionDate,
            final HttpServletRequest request) {

        final List<SewerageDemandDetail> demandDetailBeanList = new ArrayList<>();
        final Set<SewerageDemandDetail> tempDemandDetail = new LinkedHashSet<>();
        List<Installment> allInstallments = new ArrayList<>();
        final SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
        try {
            allInstallments = sewerageTaxUtils.getInstallmentsForCurrYear(
                    sdf.parse(executionDate));

        } catch (final ParseException e) {
            throw new ApplicationRuntimeException("Error while getting all installments from start date", e);
        }
        SewerageDemandDetail dmdDtl = null;
        for (final Installment installObj : allInstallments) {
            // check whether the from date of installment is less than current date. To eliminate future installments
            final EgDemandReason demandReasonObj = sewerageDemandService
                    .getDemandReasonByCodeAndInstallment(SewerageTaxConstants.FEES_SEWERAGETAX_CODE, installObj.getId());
            if (demandReasonObj != null)
                dmdDtl = createDemandDetailBean(installObj, demandReasonObj);
            tempDemandDetail.add(dmdDtl);
        }
        for (final SewerageDemandDetail demandDetList : tempDemandDetail)
            if (demandDetList != null)
                demandDetailBeanList.add(demandDetList);
        return demandDetailBeanList;
    }

    private SewerageDemandDetail createDemandDetailBean(final Installment installment, final EgDemandReason demandReasonObj) {
        final SewerageDemandDetail demandDetail = new SewerageDemandDetail();
        demandDetail.setInstallment(installment.getDescription());
        demandDetail.setReasonMaster(demandReasonObj.getEgDemandReasonMaster().getCode());
        demandDetail.setInstallmentId(installment.getId());
        demandDetail.setDemandReasonId(demandReasonObj.getId());
        demandDetail.setActualAmount(BigDecimal.ZERO);
        demandDetail.setActualCollection(BigDecimal.ZERO);
        demandDetail.setReasonMasterDesc(demandReasonObj.getEgDemandReasonMaster().getReasonMaster());
        return demandDetail;
    }

    @RequestMapping(value = "/ajaxconnection/getlegacy-donation-amount", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
    @ResponseBody
    public String getLegacyDonationAmount(@RequestParam("propertytype") final PropertyType propertyType,
            @RequestParam("noofclosetsresidential") final Integer noofclosetsresidential,
            @RequestParam("noofclosetsnonresidential") final Integer noofclosetsnonresidential) {

        BigDecimal legacyDonationAmount = BigDecimal.ZERO;
        BigDecimal residentialAmount = BigDecimal.ZERO;
        BigDecimal nonResidentialAmount = BigDecimal.ZERO;
        if (noofclosetsresidential != null && noofclosetsresidential != 0)
            residentialAmount = donationMasterService.getDonationAmountByNoOfClosetsAndPropertytypeForCurrentDate(
                    noofclosetsresidential, PropertyType.RESIDENTIAL);
        if (noofclosetsnonresidential != null && noofclosetsnonresidential != 0)
            nonResidentialAmount = donationMasterService.getDonationAmountByNoOfClosetsAndPropertytypeForCurrentDate(
                    noofclosetsnonresidential, PropertyType.NON_RESIDENTIAL);
        if (propertyType != null && propertyType.equals(PropertyType.MIXED) && residentialAmount != null
                && nonResidentialAmount != null)
            legacyDonationAmount = residentialAmount.add(nonResidentialAmount);
        else if (propertyType != null && propertyType.equals(PropertyType.RESIDENTIAL) && residentialAmount != null)
            legacyDonationAmount = residentialAmount;
        else if (propertyType != null && propertyType.equals(PropertyType.NON_RESIDENTIAL) && nonResidentialAmount != null)
            legacyDonationAmount = nonResidentialAmount;
        else if (propertyType != null && residentialAmount == null && nonResidentialAmount == null)
            return messageSource.getMessage("msg.validate.donationamount.notexistForBothcombination",
                    new String[] { propertyType.toString() }, null);
        else if (residentialAmount == null && noofclosetsresidential != null)
            return messageSource.getMessage("msg.validate.donationamount.notexist",
                    new String[] { PropertyType.RESIDENTIAL.toString(), noofclosetsresidential.toString() }, null);
        else if (nonResidentialAmount == null && noofclosetsnonresidential != null)
            return messageSource.getMessage("msg.validate.donationamount.notexist",
                    new String[] { PropertyType.NON_RESIDENTIAL.toString(), noofclosetsnonresidential.toString() }, null);
        return legacyDonationAmount.toString();
    }

}