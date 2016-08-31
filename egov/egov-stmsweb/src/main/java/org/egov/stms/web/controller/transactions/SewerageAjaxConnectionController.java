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

package org.egov.stms.web.controller.transactions;

import java.math.BigDecimal;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashSet;
import java.util.LinkedList;
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
    public @ResponseBody String isConnectionPresentForProperty(@RequestParam final String propertyID) {
        return sewerageApplicationDetailsService.checkConnectionPresentForProperty(propertyID);
    }

    @RequestMapping(value = "/ajaxconnection/check-watertax-due", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
    public @ResponseBody HashMap<String,Object> getWaterTaxDueAndCurrentTax(@RequestParam("assessmentNo") final String assessmentNo,
            final HttpServletRequest request) {
        return sewerageThirdPartyServices.getWaterTaxDueAndCurrentTax(assessmentNo, request);
    }
   
    
    @RequestMapping(value = "/ajaxconnection/check-closets-exists", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
    public @ResponseBody String isClosetsPresent(@RequestParam final PropertyType propertyType,
            @RequestParam final Integer noOfClosets, @RequestParam final Boolean flag) {
        if (flag)  
            return donationMasterService.checkClosetsPresentForGivenCombination(PropertyType.RESIDENTIAL, noOfClosets);
        else
            return donationMasterService.checkClosetsPresentForGivenCombination(PropertyType.NON_RESIDENTIAL,
                    noOfClosets);

    }
    
    @RequestMapping(value = "/ajaxconnection/check-application-inworkflow/{shscNumber}", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
    public @ResponseBody String isModifyClosetInProgress(@PathVariable final String shscNumber) {
        String validationMessage="";
        SewerageApplicationDetails sewerageAppDtl = new SewerageApplicationDetails();
        sewerageAppDtl = sewerageApplicationDetailsService.checkModifyClosetInProgress(shscNumber);
        if(sewerageAppDtl!=null){
            validationMessage = messageSource.getMessage("err.validate.changenoofclosets.application.inprogress", new String[] {sewerageAppDtl.getConnectionDetail().getPropertyIdentifier(), sewerageAppDtl.getApplicationNumber()},null);
            return validationMessage;
        }
        else
            return validationMessage; 
    }
    

    @RequestMapping(value = "/ajaxconnection/check-shscnumber-exists", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
    public @ResponseBody String isSHSCNumberUnique(@RequestParam final String shscNumber) { 
        if(sewerageApplicationDetailsService.checkSHSCNumberExists(shscNumber)!=null)
            return messageSource.getMessage("err.validate.shscnumber.exists", new String[] {shscNumber},null);
        else 
            return "";
    }
    
    @RequestMapping(value = "/ajaxconnection/getlegacy-demand-details", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
    public @ResponseBody List<SewerageDemandDetail> getLegacyDemandDetails(@RequestParam("executionDate") final String executionDate,
            final HttpServletRequest request) {
        List<SewerageDemandDetail> sdd = new LinkedList<SewerageDemandDetail>(); 
        
        final List<SewerageDemandDetail> demandDetailBeanList = new ArrayList<SewerageDemandDetail>();
        final Set<SewerageDemandDetail> tempDemandDetail = new LinkedHashSet<SewerageDemandDetail>();
        List<Installment> allInstallments = new ArrayList<Installment>();
        SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
        try {
            allInstallments = sewerageTaxUtils.getInstallmentsForCurrYear(
                    sdf.parse(executionDate));
        } catch (final ParseException e) {
            throw new ApplicationRuntimeException("Error while getting all installments from start date", e);
        }
        SewerageDemandDetail dmdDtl = null;
            for (final Installment installObj : allInstallments) {
                final EgDemandReason demandReasonObj = sewerageDemandService
                        .getDemandReasonByCodeAndInstallment(SewerageTaxConstants.FEES_SEWERAGETAX_CODE, installObj.getId());
                if (demandReasonObj != null) {
                        dmdDtl = createDemandDetailBean(installObj, demandReasonObj);
                }
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

}
