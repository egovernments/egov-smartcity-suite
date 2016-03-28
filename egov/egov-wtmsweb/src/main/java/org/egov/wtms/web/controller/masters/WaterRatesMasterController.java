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
package org.egov.wtms.web.controller.masters;

import static org.springframework.web.bind.annotation.RequestMethod.GET;

import java.text.SimpleDateFormat;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.validation.Valid;
import javax.validation.constraints.NotNull;

import org.egov.wtms.application.entity.WaterConnectionDetails;
import org.egov.wtms.masters.entity.DonationHeader;
import org.egov.wtms.masters.entity.UsageType;
import org.egov.wtms.masters.entity.WaterRatesDetails;
import org.egov.wtms.masters.entity.WaterRatesHeader;
import org.egov.wtms.masters.entity.WaterSource;
import org.egov.wtms.masters.entity.enums.ConnectionType;
import org.egov.wtms.masters.repository.WaterRatesHeaderRepository;
import org.egov.wtms.masters.service.PipeSizeService;
import org.egov.wtms.masters.service.UsageTypeService;
import org.egov.wtms.masters.service.WaterRatesDetailsService;
import org.egov.wtms.masters.service.WaterRatesHeaderService;
import org.egov.wtms.masters.service.WaterSourceService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
@RequestMapping(value = "/masters")
public class WaterRatesMasterController {

    @Autowired
    private WaterRatesDetailsService waterRatesDetailsService;

    @Autowired
    private WaterSourceService waterSourceService;

    @Autowired
    private WaterRatesHeaderService waterHeaderService;


    @Autowired
    private UsageTypeService usageTypeService;

    @Autowired
    private WaterRatesHeaderRepository waterRatesHeaderRepository;

    @Autowired
    private WaterRatesHeaderService waterRatesHeaderService;

    @Autowired
    private PipeSizeService pipeSizeService;


    @RequestMapping(value = "/waterRatesMaster", method = GET)
    public String viewForm(final Model model) {
        final WaterRatesHeader waterRatesHeader = new WaterRatesHeader();
        if (waterRatesHeader.getWaterRatesDetails().isEmpty())
            waterRatesHeader.addWaterRatesDetails(new WaterRatesDetails());
        waterRatesHeader.setConnectionType(ConnectionType.NON_METERED);
        model.addAttribute("waterRatesHeader", waterRatesHeader);
        model.addAttribute("waterRatesConnecionType", waterRatesHeader.getConnectionType());
        model.addAttribute("typeOfConnection", "WATERRATES");
        model.addAttribute("usageType", usageTypeService.getActiveUsageTypes());
        model.addAttribute("maxPipeSize", pipeSizeService.getAllActivePipeSize());
        model.addAttribute("waterSourceTypes", waterSourceService.getAllActiveWaterSourceTypes());
        model.addAttribute("reqAttr", "false");
        return "waterRates-master";
    }

    @RequestMapping(value = "/waterRatesMaster", method = RequestMethod.POST)
    public String addDonationMasterDetails(@Valid @ModelAttribute WaterRatesHeader waterRatesHeader,
            final RedirectAttributes redirectAttrs, final Model model, final BindingResult resultBinder) {
        if (resultBinder.hasErrors())
            return "waterRates-master";
        final WaterRatesHeader waterRatesHeaderTemp = waterRatesHeaderService
                .findByConnectionTypeAndUsageTypeAndWaterSourceAndPipeSize(waterRatesHeader.getConnectionType(),
                        waterRatesHeader.getUsageType(), waterRatesHeader.getWaterSource(),
                        waterRatesHeader.getPipeSize());
    
        if (waterRatesHeaderTemp != null) {
            waterRatesHeader = updateWateRatesetails(waterRatesHeaderTemp, waterRatesHeaderTemp.getWaterRatesDetails(),
                    waterRatesHeader.getWaterRatesDetails());
            redirectAttrs.addFlashAttribute("waterRatesHeader", waterRatesHeaderTemp);
            model.addAttribute("message", "Monthly Rent for Non-Meter Master Data already exists");
        } else {
            waterRatesHeader.setActive(true);
            waterRatesHeader = buildappConfigDetails(waterRatesHeader, waterRatesHeader.getWaterRatesDetails());
            waterRatesHeaderRepository.save(waterRatesHeader);
            redirectAttrs.addFlashAttribute("waterRatesHeader", waterRatesHeader);
            model.addAttribute("message", "Monthly Rent for Non-Meter Master Data Created Successfully");
        }

        final List<WaterRatesHeader> waterRatesHeaderList =  waterRatesHeaderService.findAllByConnectionType(ConnectionType.NON_METERED);
    	model.addAttribute("waterRatesHeaderList", waterRatesHeaderList);
    	 return "waterRates-master-list";
    }
    @RequestMapping(value = "/waterRatesMaster/list", method = GET)
    public String getWaterRatesMasterList(final Model model) { 
    	
    	final List<WaterRatesHeader> waterRatesHeaderList =  waterRatesHeaderService.findAllByConnectionType(ConnectionType.NON_METERED);
    	model.addAttribute("waterRatesHeaderList", waterRatesHeaderList);
    	 return "waterRates-master-list";
    	 
    }
    
    @RequestMapping(value = "/waterRatesMaster/{waterRatesHeaderid}", method = GET)
    public String getWaterRatesMasterData(final Model model,@PathVariable final String waterRatesHeaderid) { 
    	final WaterRatesHeader waterRatesHeader = waterRatesHeaderService.findBy(Long.parseLong(waterRatesHeaderid));  
    	waterRatesHeader.setConnectionType(ConnectionType.NON_METERED);
    	model.addAttribute("typeOfConnection", "WATERRATES");
    	model.addAttribute("waterRatesHeader", waterRatesHeader);
    	 model.addAttribute("waterRatesConnecionType", waterRatesHeader.getConnectionType());
    	model.addAttribute("usageType", usageTypeService.getActiveUsageTypes());
        model.addAttribute("maxPipeSize", pipeSizeService.getAllActivePipeSize());
        model.addAttribute("waterSourceTypes", waterSourceService.getAllActiveWaterSourceTypes());
        model.addAttribute("reqAttr", "true");
    	 return "waterRates-master";
    	 
    }
    
    @RequestMapping(value = "/waterRatesMaster/{waterRatesHeaderid}", method = RequestMethod.POST)
    public String addWaterRatesMasterData(@Valid @ModelAttribute WaterRatesHeader waterRatesHeader,@PathVariable final Long waterRatesHeaderid,
            final RedirectAttributes redirectAttrs, final Model model, final BindingResult resultBinder) { 
    	if (resultBinder.hasErrors())
            return "waterRates-master";
        final WaterRatesHeader waterRatesHeaderTemp = waterRatesHeaderService.findBy(waterRatesHeaderid);
                
         boolean status = waterRatesHeader.isActive();
         UsageType usageType = waterRatesHeader.getUsageType();
         WaterSource waterSource = waterRatesHeader.getWaterSource();
        if (waterRatesHeaderTemp != null) {
            waterRatesHeader = updateWateRatesetails(waterRatesHeaderTemp, waterRatesHeaderTemp.getWaterRatesDetails(),
                    waterRatesHeader.getWaterRatesDetails());
            waterRatesHeader.setActive(status);
            waterRatesHeader.setUsageType(usageType);
            waterRatesHeader.setWaterSource(waterSource);
            waterRatesHeaderRepository.save(waterRatesHeader);
        } else {
        	 waterRatesHeader.setActive(status);
             waterRatesHeader.setUsageType(usageType);
             waterRatesHeader.setWaterSource(waterSource);
            waterRatesHeader = buildappConfigDetails(waterRatesHeader, waterRatesHeader.getWaterRatesDetails());
            waterRatesHeaderRepository.save(waterRatesHeader);
        }
        final List<WaterRatesHeader> waterRatesHeaderList =  waterRatesHeaderService.findAllByConnectionType(ConnectionType.NON_METERED);
    	model.addAttribute("waterRatesHeaderList", waterRatesHeaderList);
    	 return "waterRates-master-list";
    }
    

    private WaterRatesHeader buildappConfigDetails(final WaterRatesHeader waterRatesHeader,
            final List<WaterRatesDetails> unitDetail) {
        final Set<WaterRatesDetails> unitSet = new HashSet<WaterRatesDetails>();

        for (final WaterRatesDetails unitdetail : unitDetail)
            if (unitdetail.getFromDate() != null && !"".equals(unitdetail.getMonthlyRate())) {
                unitdetail.setWaterRatesHeader(waterRatesHeader);

                unitSet.add(unitdetail);

                waterRatesHeader.getWaterRatesDetails().clear();

                waterRatesHeader.getWaterRatesDetails().addAll(unitSet);
            }
        return waterRatesHeader;
    }

    private WaterRatesHeader updateWateRatesetails(final WaterRatesHeader waterRatesHeader,
            final List<WaterRatesDetails> unitDetailOld, final List<WaterRatesDetails> unitDetail) {
    	String DATE_FORMAT = "MM/dd/yyyy";
        SimpleDateFormat sdf = new SimpleDateFormat(DATE_FORMAT);
        final Set<WaterRatesDetails> unitSet = new HashSet<WaterRatesDetails>();
        for (final WaterRatesDetails unitdetailOld : unitDetailOld)
            for (final WaterRatesDetails unitdetail : unitDetail)
                if (unitdetail.getFromDate() != null && !"".equals(unitdetail.getMonthlyRate())) {
                    unitdetailOld.setWaterRatesHeader(waterRatesHeader);
                    unitdetailOld.setMonthlyRate(unitdetail.getMonthlyRate());
                    unitdetailOld.setFromDate(unitdetail.getFromDate());
                    unitSet.add(unitdetailOld);

                    waterRatesHeader.getWaterRatesDetails().clear();

                    waterRatesHeader.getWaterRatesDetails().addAll(unitSet);
                }
        return waterRatesHeader;
    }

}