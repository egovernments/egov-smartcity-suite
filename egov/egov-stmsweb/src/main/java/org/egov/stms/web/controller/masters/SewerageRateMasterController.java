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
import javax.validation.Valid;

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
public class SewerageRateMasterController {
    SimpleDateFormat formatter=new SimpleDateFormat("dd/MM/yyyy");
    SimpleDateFormat myFormat = new SimpleDateFormat("dd-MM-yyyy");

    @Autowired
    private SewerageRatesMasterService sewerageRatesMasterService;
    
    @Autowired
    private FinancialYearService financialYearService;
    
    @Autowired 
    private ApplicationProperties applicationProperties;

    @RequestMapping(value = "/seweragerates", method = RequestMethod.GET)
    public String showForm(
            @ModelAttribute SewerageRatesMaster sewerageRatesMaster,
            final Model model) {
        sewerageRatesMaster = new SewerageRatesMaster();
        
        //TODO : check for null condition
        CFinancialYear financialYear = financialYearService.getCurrentFinancialYear();
       if(financialYear!=null)
        model.addAttribute("endDate",financialYear.getEndingDate());
        model.addAttribute("propertyTypes", PropertyType.values());

        return "sewerageRates-master";
    }

    @RequestMapping(value = "/seweragerates", method = RequestMethod.POST)
    public String create(
            @Valid @ModelAttribute final SewerageRatesMaster sewerageRatesMaster,
            final RedirectAttributes redirectAttrs, final Model model,
            final BindingResult resultBinder) {

        List<SewerageRatesMaster> existingsewerageRatesMasterList = sewerageRatesMasterService.getLatestActiveRecord(sewerageRatesMaster.getPropertyType(),true);
        
        SewerageRatesMaster sewerageRatesMasterExisting = new SewerageRatesMaster();
        sewerageRatesMasterExisting = sewerageRatesMasterService
                .findByPropertyTypeAndFromDateAndActive(
                        sewerageRatesMaster.getPropertyType(),
                        sewerageRatesMaster.getFromDate(), true);
        if (sewerageRatesMasterExisting != null) {
            model.addAttribute("existingMonthlyRate",sewerageRatesMasterExisting.getMonthlyRate());
            sewerageRatesMasterExisting.setActive(false);
            /* append the end time of given date as 23:59:59  */
            DateTime dateValue = DateUtils.endOfGivenDate(new DateTime(sewerageRatesMaster.getFromDate()));
            Date formattedToDate=dateValue.toDate();
            sewerageRatesMasterExisting.setToDate(formattedToDate);
            sewerageRatesMasterService.update(sewerageRatesMasterExisting);
            
            sewerageRatesMaster.setActive(true);
            sewerageRatesMasterService.create(sewerageRatesMaster);
        } else {
            SewerageRatesMaster sewerageRatesMasterOld = null;
            if(!existingsewerageRatesMasterList.isEmpty()){
            sewerageRatesMasterOld=existingsewerageRatesMasterList.get(0);
            }
            if (sewerageRatesMasterOld != null) {
                if(sewerageRatesMaster.getFromDate().compareTo(new Date())<0){
                sewerageRatesMasterOld.setActive(false);
                }
                
                DateTime oldDate=DateUtils.endOfGivenDate(new DateTime(sewerageRatesMaster.getFromDate()).minusDays(1));
                Date formattedToDate=oldDate.toDate();
                sewerageRatesMasterOld.setToDate(formattedToDate);
                sewerageRatesMasterService.update(sewerageRatesMasterOld);
            }
            sewerageRatesMaster.setActive(true);
            sewerageRatesMasterService.create(sewerageRatesMaster);
        }
        redirectAttrs.addFlashAttribute("message", "msg.seweragemonthlyrate.creation.success");
        return "redirect:/masters/getseweragerates/" + sewerageRatesMaster.getId();

    }
    
    
    @RequestMapping(value = "/getseweragerates/{id}", method = RequestMethod.GET)
    public String getSeweragerates(@PathVariable("id") Long id, Model model) {
        SewerageRatesMaster sewerageRatesMaster = sewerageRatesMasterService.findBy(id);
        model.addAttribute("sewerageRatesMaster",sewerageRatesMaster);
        return "sewerageRates-success";
    }
    
    
    @RequestMapping(value = "/ajaxexistingseweragevalidate", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
    public @ResponseBody double getSewerageRatesByAllCombinatons(@RequestParam("propertyType") final PropertyType propertyType,
            @RequestParam("fromDate") Date fromDate) {
        SewerageRatesMaster sewerageRatesMasterExist = null;
        sewerageRatesMasterExist = sewerageRatesMasterService
                .findByPropertyTypeAndFromDateAndActive(propertyType, fromDate, true);
        if (sewerageRatesMasterExist != null)
            return sewerageRatesMasterExist.getMonthlyRate();
        else
            return 0;
    }
    
    @RequestMapping(value = "/fromDateValidationWithLatestActiveRecord", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
    public @ResponseBody String getLatestActiveFromDate(@RequestParam("propertyType") final PropertyType propertyType,
            @RequestParam("fromDate") Date fromDate) {
        List<SewerageRatesMaster> existingsewerageRatesMasterList = sewerageRatesMasterService.getLatestActiveRecord(propertyType,true);
       if(!existingsewerageRatesMasterList.isEmpty()){
            SewerageRatesMaster existingActiveSewerageRatesObject = existingsewerageRatesMasterList.get(0);
            
            //TODO : move formatter code at declarative section
            if(fromDate.compareTo(existingActiveSewerageRatesObject.getFromDate())<0){
                SimpleDateFormat formatter = new SimpleDateFormat("dd-MM-yyyy");
                return formatter.format(existingActiveSewerageRatesObject.getFromDate());
            }
        }
       return "true";
       
    }
    
   
    @RequestMapping(value = "/viewSewerageRate", method = RequestMethod.GET)
    public String view(@ModelAttribute SewerageRatesSearch sewerageRatesSearch, final Model model) {
        model.addAttribute("propertyTypes",PropertyType.values());
        model.addAttribute("statusValues", SewerageRateStatus.values());
        return "sewerageRates-view";
    }
    
    @RequestMapping(value = "/rateView/{id}", method = RequestMethod.GET)
    public String view( @ModelAttribute SewerageRatesSearch sewerageRatesSearch, final Model model, @PathVariable("id") final Long id, final RedirectAttributes redirectAttrs) {
        SewerageRatesMaster sewerageRatesMaster = sewerageRatesMasterService.findBy(id);
        model.addAttribute("sewerageRatesSearch",sewerageRatesMaster);
        return "redirect:/masters/getseweragerates/"+id;
    }
    
    @RequestMapping(value = "/search-sewerage-rates", method = GET)
    public @ResponseBody void sewerageRatesSearch(final Model model, @ModelAttribute SewerageRatesSearch sewerageRatesSearch, final HttpServletResponse response)throws IOException,ParseException {
        PropertyType type=null;
        String effectivefromDate=null;
        if(sewerageRatesSearch.getPropertyType()!=null){
            type=PropertyType.valueOf(sewerageRatesSearch.getPropertyType());
        }
       
        if(sewerageRatesSearch.getFromDate()!=null){
            effectivefromDate=myFormat.format(formatter.parse(sewerageRatesSearch.getFromDate()));
        }
        
        IOUtils.write("{ \"data\":" + new GsonBuilder().setDateFormat(applicationProperties.defaultDatePattern()).create()
                .toJson(sewerageRatesMasterService.getSewerageMasters(type, effectivefromDate, sewerageRatesSearch.getStatus()))
                + "}", response.getWriter());
    }
    
    @RequestMapping(value="update/{id}", method=GET)
    public String updateSewerageRates(@ModelAttribute SewerageRatesMaster sewerageRatesMaster, @PathVariable final Long id, final Model model){
        SewerageRatesMaster existingratesMaster = sewerageRatesMasterService.findBy(id);
        model.addAttribute("sewerageRatesMaster",existingratesMaster);
        return "sewerageRates-update";
    }
    
    @RequestMapping(value="update/{id}", method=POST)
    public String update(@ModelAttribute SewerageRatesMaster sewerageRatesMaster, @PathVariable final Long id, final Model model, final RedirectAttributes redirectAttrs) throws ParseException{
        SewerageRatesMaster ratesMaster = sewerageRatesMasterService.findBy(id);
       
        String todaysdate=myFormat.format(new Date());
        String effectiveFromDate=myFormat.format(ratesMaster.getFromDate());
        Date currentDate=myFormat.parse(todaysdate);
        Date effectiveDate=myFormat.parse(effectiveFromDate);
        
        if(effectiveDate.compareTo(currentDate)<0){
            model.addAttribute("message","msg.seweragerate.modification.rejected");
            return "sewerageRates-update";
        }
        ratesMaster.setMonthlyRate(sewerageRatesMaster.getMonthlyRate());
        sewerageRatesMasterService.update(ratesMaster);
        redirectAttrs.addFlashAttribute("message","msg.seweragemonthlyrate.update.success");
        return "redirect:/masters/getseweragerates/"+id;
    }
    
    @RequestMapping(value="/fromDateValues-by-propertyType", method=GET, produces=APPLICATION_JSON_VALUE)
    public @ResponseBody List<Date> effectiveFromDates(@RequestParam final PropertyType propertyType){
        return sewerageRatesMasterService.findFromDateByPropertyType(propertyType);
    }
}
