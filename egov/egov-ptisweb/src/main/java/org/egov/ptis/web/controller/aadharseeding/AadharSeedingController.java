/*
 *    eGov  SmartCity eGovernance suite aims to improve the internal efficiency,transparency,
 *    accountability and the service delivery of the government  organizations.
 *
 *     Copyright (C) 2018  eGovernments Foundation
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
package org.egov.ptis.web.controller.aadharseeding;

import java.util.List;

import javax.servlet.http.HttpServletRequest;

import org.egov.ptis.bean.aadharseeding.AadharSearchResult;
import org.egov.ptis.bean.aadharseeding.AadharSeedingRequest;
import org.egov.ptis.service.aadharseeding.AadharSeedingService;
import org.egov.ptis.service.aadharseeding.AadharService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import org.egov.ptis.bean.aadharseeding.AadhaarDetails;
import com.google.gson.GsonBuilder;

@Controller
@RequestMapping(value = { "/aadharseeding" })
public class AadharSeedingController {

    @Autowired
    private AadharSeedingService aadharSeedingService;
    
    @Autowired
    private ApplicationContext beanProvider;
    
    @GetMapping(value = { "/search" })
    public String getAadhaarSeedingSearchForm(final Model model) {
        aadharSeedingService.addModelAttributes(model);
        return "aadharseedingsearch-form";
    }
    
    @GetMapping(value = "/result", produces = MediaType.TEXT_PLAIN_VALUE)
    @ResponseBody
    public String searchProperties(AadharSeedingRequest aadharSeedingRequest) {
        List<AadharSearchResult> aadharSeeding = aadharSeedingService.prepareOutput(aadharSeedingRequest);
        return "{ \"data\":"+new GsonBuilder().create().toJson(aadharSeeding)+"}";
    }

    @GetMapping(value = "/peoplehubdata/{aadharNumber}", produces = MediaType.APPLICATION_JSON_VALUE)
    @ResponseBody
    public AadhaarDetails geDetails(@PathVariable final String aadharNumber) {
        AadharService aadharService = (AadharService) beanProvider
                .getBean("aadharService");
        return aadharService.getAadharDetails(aadharNumber);
    }

    @GetMapping(value = { "/aadhardataupdateform/{assessmentNo}/{status}" })
    public String getAadharSeedingUpdateForm(final Model model, @PathVariable final String assessmentNo, @PathVariable final String status, final HttpServletRequest request) {
        aadharSeedingService.addPropertyDetailstoModel(model , assessmentNo, status);
        return "aadharseedingupdate-form";
    }
    
    @PostMapping(value = "/aadhardataupdateform/{assessmentNo}/{status}")
    public String saveAadharSeeding(@ModelAttribute final AadharSeedingRequest aadharSeedingRequest){
        aadharSeedingService.saveSeedingDetails(aadharSeedingRequest);
        final String successMsg = "Aadhar Details Updated and sent to Commissioner for Approval";
        aadharSeedingRequest.setSuccessMessage(successMsg);
        return "aadharseeding-success";
    }
    
    @GetMapping(value = "/aadhardataapprovalform")
    public String getPropertiesForApproval(@ModelAttribute final AadharSearchResult aadharSearchResult, final Model model){
        model.addAttribute("serachResult",aadharSeedingService.getProperties());
        return "aadharseeding-approvalform";
    }
    
    @PostMapping(value = "/aadhardataapprovalform", consumes = MediaType.APPLICATION_JSON_VALUE)
    @ResponseBody
    public String updateDetails(@RequestBody String  assessmentList){
        aadharSeedingService.approveAadharSeeding(assessmentList);
        return "SUCCESS";
    }
}