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
import java.math.BigDecimal;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.List;

import javax.servlet.http.HttpServletResponse;

import org.apache.commons.io.IOUtils;
import org.egov.commons.CFinancialYear;
import org.egov.commons.service.FinancialYearService;
import org.egov.infra.config.properties.ApplicationProperties;
import org.egov.infra.utils.DateUtils;
import org.egov.stms.masters.entity.DonationDetailMaster;
import org.egov.stms.masters.entity.DonationMaster;
import org.egov.stms.masters.entity.enums.PropertyType;
import org.egov.stms.masters.entity.enums.SewerageRateStatus;
import org.egov.stms.masters.pojo.DonationMasterSearch;
import org.egov.stms.masters.pojo.DonationRateComparatorOrderById;
import org.egov.stms.masters.service.DonationMasterService;
import org.joda.time.DateTime;
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
public class DonationMasterController {
    
    @Autowired
    private DonationMasterService donationMasterService;
    
    @Autowired
    private FinancialYearService financialYearService;
    
    @Autowired
    private ApplicationProperties applicationProperties;
    
    @RequestMapping(value = "/donationmaster", method = RequestMethod.GET)
    public String showForm(@ModelAttribute DonationMaster donationMaster, final Model model) {
        donationMaster = new DonationMaster();
        CFinancialYear financialYear = financialYearService.getCurrentFinancialYear();
        if(financialYear!=null)
        model.addAttribute("endDate", financialYear.getEndingDate());
        model.addAttribute("donationmaster", donationMaster);
        model.addAttribute("propertyTypes", PropertyType.values());
        return "donation-master";
    }

    @RequestMapping(value = "/donationmaster", method = RequestMethod.POST, produces = MediaType.APPLICATION_JSON_VALUE)
    public String createDonationMaster(@ModelAttribute DonationMaster donationMaster,
            final BindingResult errors, final RedirectAttributes redirectAttrs, final Model model) {
        List<DonationDetailMaster> donationMasterDetailList = new ArrayList<DonationDetailMaster>();
     
        List<DonationMaster> existingdonationMaster = donationMasterService
                .getLatestActiveRecordByPropertyTypeAndActive(donationMaster.getPropertyType(), true);
     
        DonationMaster donationMasterExist = donationMasterService.findByPropertyTypeAndFromDateAndActive(donationMaster.getPropertyType(),
                donationMaster.getFromDate(), true);
      //overwrite existing combination with propertyType, fromDate, isActive = true 
        if (donationMasterExist != null) {
            DateTime dateTime = DateUtils.endOfGivenDate(new DateTime(donationMaster.getFromDate()));
            Date dateformat = dateTime.toDate();
            donationMasterExist.setActive(false);
            donationMasterExist.setToDate(dateformat);
            donationMaster.setActive(true);
            for (DonationDetailMaster donationDetailMaster : donationMaster.getDonationDetail()) {
                donationDetailMaster.setDonation(donationMaster);
                donationMasterDetailList.add(donationDetailMaster);
            }
            
            donationMaster.getDonationDetail().addAll(donationMasterDetailList);
            donationMaster = donationMasterService.createDonationRate(donationMaster);

        } else {
            // set todate for the record with same propertyType and isActive = true and create new record
            DonationMaster donationMasterOld = null;
            if (!existingdonationMaster.isEmpty()) {
                donationMasterOld = existingdonationMaster.get(0);
            }
            if (donationMasterOld != null) {
                if (donationMaster.getFromDate().compareTo(new Date()) < 0) {
                    donationMasterOld.setActive(false);
                }
                //sets the endofGiven date as 23:59:59
                DateTime dateTime = DateUtils.endOfGivenDate(new DateTime(donationMaster.getFromDate()).minusDays(1));
                Date  dateformat = dateTime.toDate();
                donationMasterOld.setToDate(dateformat);
                donationMasterService.update(donationMasterOld);
            }
            donationMaster.setActive(true);
            for (DonationDetailMaster donationDetailMaster : donationMaster.getDonationDetail()) {
                donationDetailMaster.setDonation(donationMaster);
                donationMasterDetailList.add(donationDetailMaster);
            }
            donationMaster.getDonationDetail().clear();
            donationMaster.getDonationDetail().addAll(donationMasterDetailList);
            donationMaster = donationMasterService.createDonationRate(donationMaster);
        }
        redirectAttrs.addFlashAttribute("message", "msg.donationrate.creation.success");
        return "redirect:/masters/donationmastersuccess/" +donationMaster.getId();
    }

    @RequestMapping(value = "/donationmastersuccess/{id}", method = RequestMethod.GET)
    public String getSeweragerates(@ModelAttribute DonationMaster donationMaster, @PathVariable("id") Long id,
            final RedirectAttributes redirectAttrs, Model model) {
        DonationMaster donationMaster1 = donationMasterService.findById(id);
        for (DonationDetailMaster ddm : donationMaster1.getDonationDetail()) {
            ddm.setAmount(BigDecimal.valueOf(ddm.getAmount()).setScale(2, BigDecimal.ROUND_HALF_EVEN).doubleValue());
        }
        Collections.sort(donationMaster1.getDonationDetail(), new DonationRateComparatorOrderById());
        model.addAttribute("donationMaster", donationMaster1);
        return "donation-master-success";
    }

    @RequestMapping(value="/fromDateValidationWithActiveRecord", method=RequestMethod.GET)
    public @ResponseBody String validateFromDateWithActiveDate(@RequestParam("propertyType") PropertyType propertyType ,@RequestParam("fromDate") Date date, final Model model){
        List<DonationMaster> donationList = donationMasterService.getLatestActiveRecordByPropertyTypeAndActive(propertyType, true);
        if(!donationList.isEmpty()){
        DonationMaster existingActiveDonationObject = donationList.get(0);
            if(date.compareTo(existingActiveDonationObject.getFromDate())<0){
           SimpleDateFormat formatter = new SimpleDateFormat("dd-MM-yyyy");
           String activeDate=formatter.format(existingActiveDonationObject.getFromDate());
           return activeDate;
            }
        }
        return "true";
    }
    
    @RequestMapping(value = "/ajaxexistingdonationvalidate", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
    public @ResponseBody double geWaterRatesByAllCombinatons(@RequestParam("propertyType") final PropertyType propertyType,
            @RequestParam("fromDate") Date fromDate) {
        DonationMaster donationMasterMaster = null;
        donationMasterMaster = donationMasterService
                .findByPropertyTypeAndFromDateAndActive(propertyType, fromDate, true);
        if (donationMasterMaster != null) {
            return 1;
        } else
            return 0;
    }
    
    
    @RequestMapping(value="/view" , method = RequestMethod.GET)
    public String viewDonationMaster(final Model model, @ModelAttribute final DonationMasterSearch donationMasterSearch){
        model.addAttribute("propertyType", PropertyType.values());
        model.addAttribute("statusValues",SewerageRateStatus.values());
        return "donationMaster-view";
    }
    
    @RequestMapping(value="/search-donation-master",method = GET, produces = APPLICATION_JSON_VALUE)
    public @ResponseBody void searchDonationMaster(@ModelAttribute final DonationMasterSearch donationMasterSearch, final HttpServletResponse response) throws IOException, ParseException{
        PropertyType type =null;
        String effectivefromDate=null;
        if(donationMasterSearch.getPropertyType()!=null){
        type = PropertyType.valueOf(donationMasterSearch.getPropertyType());
       }
        SimpleDateFormat formatter = new SimpleDateFormat("dd/MM/yyyy");
        SimpleDateFormat myFormat = new SimpleDateFormat("dd-MM-yyyy");
        if(donationMasterSearch.getFromDate()!=null){
        effectivefromDate=myFormat.format(formatter.parse(donationMasterSearch.getFromDate()));
        }
        IOUtils.write("{ \"data\":" + new GsonBuilder().setDateFormat(applicationProperties.defaultDatePattern()).create()
                .toJson(donationMasterService.getDonationMasters(type,effectivefromDate, donationMasterSearch.getStatus()))
                + "}", response.getWriter());
    }
    
    @RequestMapping(value="/fromDate-by-propertyType", method=GET, produces=APPLICATION_JSON_VALUE)
    public @ResponseBody List<Date> effectiveFromDates(@RequestParam final PropertyType propertyType){
        return donationMasterService.findFromDateByPropertyType(propertyType);
    }
    
    @RequestMapping(value="/viewDonation/{id}", method=GET)
    public String ViewDonation( @PathVariable final Long id, final Model model, final RedirectAttributes redirectAttrs){
       return "redirect:/masters/donationmastersuccess/"+id;
    }
    
    @RequestMapping(value="/updateDonation/{id}", method=GET)
    public String UpdateDonation(@PathVariable final Long id, final Model model){
        DonationMaster dm = donationMasterService.findById(id);
        Collections.sort(dm.getDonationDetail(), new DonationRateComparatorOrderById());
        model.addAttribute("donationMaster",dm);
        model.addAttribute("donationDetail", dm.getDonationDetail());
        return "donation-master-update";
    }
    
    @RequestMapping(value="/updateDonation/{id}", method=POST)
    public String updateDonationValues(@ModelAttribute DonationMaster donationMaster, @PathVariable final Long id, final Model model,
            final RedirectAttributes redirectAttrs) throws ParseException{
      
        DonationMaster donationMstr = donationMasterService.findById(id);
      if(donationMstr!=null) { 
        SimpleDateFormat myFormat = new SimpleDateFormat("dd-MM-yyyy");
        String todaysdate=myFormat.format(new Date());
        String effectiveFromDate=myFormat.format(donationMstr.getFromDate());
        
        Date effectiveDate= myFormat.parse(effectiveFromDate);
        Date currentDate=myFormat.parse(todaysdate);
  
        if(effectiveDate.compareTo(currentDate)<0){
            model.addAttribute("message","msg.donationrate.modification.rejected");
            return "donation-master-update";
          }
        donationMstr.setLastModifiedDate(new Date());
         List<DonationDetailMaster> existingdonationDetailList=new ArrayList<DonationDetailMaster>();
        if(!donationMaster.getDonationDetail().isEmpty()){
        existingdonationDetailList.addAll(donationMstr.getDonationDetail());
        }
        if(donationMaster!=null && donationMaster.getDonationDetail()!=null){
            if(!existingdonationDetailList.isEmpty()){
                
                for(DonationDetailMaster dtlObject : existingdonationDetailList){
                    if(!donationMaster.getDonationDetail().contains(dtlObject)){
                        donationMstr.deleteDonationDetail(dtlObject);
                    }
                 }
                for(DonationDetailMaster dtlMaster : donationMaster.getDonationDetail()){
                    if(dtlMaster.getId()==null){
                    DonationDetailMaster donationDetailObject = new DonationDetailMaster();
                    donationDetailObject.setNoOfClosets(dtlMaster.getNoOfClosets());
                    donationDetailObject.setAmount(dtlMaster.getAmount());
                    donationDetailObject.setDonation(donationMstr);
                    donationMstr.addDonationDetail(donationDetailObject);
                    }else if(dtlMaster.getId()!=null && existingdonationDetailList.contains(dtlMaster))
                    {
                        for(DonationDetailMaster dtlObject : donationMstr.getDonationDetail()){
                        
                             if(dtlObject.getId().equals(dtlMaster.getId()))
                             {
                                 dtlObject.setAmount(dtlMaster.getAmount());
                                 dtlObject.setNoOfClosets(dtlMaster.getNoOfClosets());
                             }
                        }
                    }
                }
            }
            donationMasterService.update(donationMstr);  
            } 
        }
      else{
          model.addAttribute("message","msg.donationrate.notfound");
          return "donation-master-update";
      }
        redirectAttrs.addFlashAttribute("message", "msg.donationrate.update.success");
        return "redirect:/masters/donationmastersuccess/" +id;
    }
}
