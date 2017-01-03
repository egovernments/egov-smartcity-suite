/* eGov suite of products aim to improve the internal efficiency,transparency,
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

package org.egov.mrs.web.controller.application.registration;

import static org.egov.infra.utils.JsonUtils.toJSON;

import java.io.IOException;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

import javax.servlet.http.HttpServletResponse;

import org.egov.infra.admin.master.entity.Role;
import org.egov.infra.security.utils.SecurityUtils;
import org.egov.infra.utils.FileStoreUtils;
import org.egov.mrs.application.MarriageConstants;
import org.egov.mrs.application.service.MarriageCertificateService;
import org.egov.mrs.domain.entity.MarriageCertificate;
import org.egov.mrs.domain.entity.MarriageRegistration;
import org.egov.mrs.domain.entity.MarriageRegistrationSearchFilter;
import org.egov.mrs.domain.entity.ReIssue;
import org.egov.mrs.domain.enums.MarriageCertificateType;
import org.egov.mrs.domain.service.MarriageRegistrationService;
import org.egov.mrs.masters.service.MarriageRegistrationUnitService;
import org.egov.mrs.web.adaptor.MarriageCerftificateJsonAdaptor;
import org.egov.mrs.web.adaptor.MarriageReIssueJsonAdaptor;
import org.egov.mrs.web.adaptor.MarriageRegistrationJsonAdaptor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

/**
 * Handles the Registration search
 *
 * @author nayeem
 *
 */
@Controller
@RequestMapping(value = "/registration")
public class SearchRegistrationController {

    private final MarriageRegistrationService marriageRegistrationService;
    private final SecurityUtils securityUtils;
    @Autowired
    private MarriageCertificateService marriageCertificateService;
    @Autowired
    private FileStoreUtils fileStoreUtils;
    @Autowired
    protected MarriageRegistrationUnitService marriageRegistrationUnitService;

    @Autowired
    public SearchRegistrationController(final MarriageRegistrationService marriageRegistrationService, final SecurityUtils securityUtils) {
        this.marriageRegistrationService = marriageRegistrationService;
        this.securityUtils = securityUtils;
    }

    public void prepareSearchForm(final Model model){
        model.addAttribute("marriageRegistrationUnit", marriageRegistrationUnitService.getActiveRegistrationunit());

    }
    @RequestMapping(value = "/search", method = RequestMethod.GET)
    public String showSearch( final Model model) {
        
        final List<Role> operatorRoles = securityUtils.getCurrentUser().getRoles().stream()
                .filter(role -> role.getName().equalsIgnoreCase("Collection Operator")).collect(Collectors.toList());
        model.addAttribute("registration", new MarriageRegistration());
        final boolean isCollectionOperator = operatorRoles == null || operatorRoles.isEmpty() ? false : true;
        model.addAttribute("isCollectionOperator", isCollectionOperator);
        prepareSearchForm(model);
        return "registration-search";
    }
    
    @RequestMapping(value = "/searchApproved", method = RequestMethod.GET)
    public String showSearchApproved( final Model model) {
        
        final List<Role> operatorRoles = securityUtils.getCurrentUser().getRoles().stream()
                .filter(role -> role.getName().equalsIgnoreCase("Collection Operator")).collect(Collectors.toList());
        model.addAttribute("registration", new MarriageRegistration());
        final boolean isCollectionOperator = operatorRoles == null || operatorRoles.isEmpty() ? false : true;
        model.addAttribute("isCollectionOperator", isCollectionOperator);
        prepareSearchForm(model);
        return "registration-search-approved";
    }
    
    public @ModelAttribute("marriageRegistrationTypes") List<MarriageCertificateType> mrTypeList() {
        List<MarriageCertificateType> enumValues = new ArrayList<MarriageCertificateType>();
        for(MarriageCertificateType mct: Arrays.asList(MarriageCertificateType.values())){
            if(!mct.name().equalsIgnoreCase("REJECTION")){
                enumValues.add(mct);
            }
        }
        return enumValues;   
    } 
        
    @RequestMapping(value = "/collectmrfee", method = RequestMethod.GET)
    public String showSearchApprovedforFee( final Model model) {
         model.addAttribute("registration", new MarriageRegistration());
         model.addAttribute("mode", "collectmrfee");
         prepareSearchForm(model);
         return "registration-search-forfee";
    }
    
    @RequestMapping(value = "/search", method = RequestMethod.POST, produces = MediaType.TEXT_PLAIN_VALUE)
    public @ResponseBody String search(Model model,@ModelAttribute final MarriageRegistration registration) throws ParseException {
        List<MarriageRegistration> searchResultList = marriageRegistrationService.searchMarriageRegistrations(registration);
         String result = new StringBuilder("{ \"data\":").append(toJSON(searchResultList,MarriageRegistration.class,  MarriageRegistrationJsonAdaptor.class)).append("}")
                   .toString();
          return result;
    }
    
    @RequestMapping(value = "/searchApproved", method = RequestMethod.POST, produces = MediaType.TEXT_PLAIN_VALUE)
    public @ResponseBody String searchRegisterStatusMarriageRecords(Model model,@ModelAttribute final MarriageRegistration registration) throws ParseException {
        List<MarriageRegistration> searchResultList = marriageRegistrationService.searchRegistrationByStatus(registration,MarriageRegistration.RegistrationStatus.REGISTERED.toString());
         String result = new StringBuilder("{ \"data\":").append(toJSON(searchResultList,MarriageRegistration.class,  MarriageRegistrationJsonAdaptor.class)).append("}")
                   .toString();
          return result;
    }
    
    @RequestMapping(value = "/collectmrfeeajaxsearch", method = RequestMethod.POST, produces = MediaType.TEXT_PLAIN_VALUE)
    public @ResponseBody String searchMarriageRegistrationsForFeeCollection(Model model,@ModelAttribute final MarriageRegistration registration) throws ParseException {
        List<MarriageRegistration> searchResultList = marriageRegistrationService.searchMarriageRegistrationsForFeeCollection(registration);
        List<MarriageRegistration> newSearchResultList = new ArrayList<MarriageRegistration>();
        for(MarriageRegistration mr : searchResultList){
            if(mr!=null && !mr.isFeeCollected())
                newSearchResultList.add(mr);
        }
         String result = new StringBuilder("{ \"data\":").append(toJSON(newSearchResultList,MarriageRegistration.class,  MarriageRegistrationJsonAdaptor.class)).append("}")
                   .toString();
          return result;
    }
    
    @RequestMapping(value = "/collectmrreissuefeeajaxsearch", method = RequestMethod.POST, produces = MediaType.TEXT_PLAIN_VALUE)
    public @ResponseBody String searchApprovedMarriageReIssueRecordsForFee(Model model,
            @ModelAttribute final MarriageRegistrationSearchFilter mrSearchFilter) throws ParseException {
        List<ReIssue> searchResultList = marriageRegistrationService.searchApprovedReIssueRecordsForFeeCollection(mrSearchFilter);
        List<ReIssue> newSearchResultList = new ArrayList<ReIssue>();
        for(ReIssue mrr : searchResultList){
            if(mrr !=null && !mrr.isFeeCollected())
                newSearchResultList.add(mrr);
        }
         String result = new StringBuilder("{ \"data\":").append(toJSON(newSearchResultList,ReIssue.class,  MarriageReIssueJsonAdaptor.class)).append("}")
                   .toString();
          return result;
    }
    
    @RequestMapping(value = "/searchregisteredrecord", method = RequestMethod.POST, produces = MediaType.TEXT_PLAIN_VALUE)
    public @ResponseBody String searchRegisteredStatusMarriageRecords(Model model,@ModelAttribute final MarriageRegistration registration) throws ParseException {
        List<MarriageRegistration> searchResultList = marriageRegistrationService.searchRegistrationByStatus(registration,MarriageRegistration.RegistrationStatus.REGISTERED.toString());
         String result = new StringBuilder("{ \"data\":").append(toJSON(searchResultList,MarriageRegistration.class,  MarriageRegistrationJsonAdaptor.class)).append("}")
                   .toString();
          return result;
    }
    @RequestMapping(value = "/reissuecertificate", method = RequestMethod.GET)
    public String reissueCertificateSearch( final Model model) {
        model.addAttribute("registration", new MarriageRegistration());
          prepareSearchForm(model);
          return "registration-search-certificateissue";
    }
    
    @RequestMapping(value = "/searchcertificates", method = RequestMethod.GET)
    public String showReportForm(final Model model) {
        model.addAttribute("certificate", new MarriageCertificate());
        return "registration-search-certificate";
    }

    @RequestMapping(value = "/searchcertificates", method = RequestMethod.POST, produces = MediaType.TEXT_PLAIN_VALUE)
    public @ResponseBody String searchApprovedMarriageRecords(Model model,@ModelAttribute final MarriageCertificate certificate) {
        List<MarriageCertificate> searchResultList = marriageCertificateService.searchMarriageCertificates(certificate);
         String result = new StringBuilder("{ \"data\":").append(toJSON(searchResultList,MarriageCertificate.class,  MarriageCerftificateJsonAdaptor.class)).append("}")
                   .toString();
          return result;
    } 
    
    @RequestMapping(value = "/printcertficate/{id}")
    public void download(@PathVariable final long id, final HttpServletResponse response) throws IOException {
        MarriageCertificate certificate = marriageCertificateService.findById(id);
        fileStoreUtils.fetchFileAndWriteToStream(certificate.getFileStore().getFileStoreId(), MarriageConstants.FILESTORE_MODULECODE, false, response);
    }
}