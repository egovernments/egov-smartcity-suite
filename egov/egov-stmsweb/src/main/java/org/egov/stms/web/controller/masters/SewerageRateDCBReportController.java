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

import static java.util.Arrays.asList;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.io.IOUtils;
import org.egov.config.search.Index;
import org.egov.config.search.IndexType;
import org.egov.infra.admin.master.entity.Boundary;
import org.egov.infra.admin.master.entity.City;
import org.egov.infra.admin.master.service.BoundaryService;
import org.egov.infra.admin.master.service.CityService;
import org.egov.infra.config.core.ApplicationThreadLocals;
import org.egov.infra.config.properties.ApplicationProperties;
import org.egov.ptis.domain.model.AssessmentDetails;
import org.egov.search.domain.Document;
import org.egov.search.domain.Page;
import org.egov.search.domain.SearchResult;
import org.egov.search.domain.Sort;
import org.egov.search.service.SearchService;
import org.egov.stms.masters.entity.enums.PropertyType;
import org.egov.stms.masters.pojo.DCBReportWardwiseResult;
import org.egov.stms.transactions.entity.SewerageApplicationDetails;
import org.egov.stms.transactions.service.SewerageApplicationDetailsService;
import org.egov.stms.transactions.service.SewerageDCBReporService;
import org.egov.stms.transactions.service.SewerageThirdPartyServices;
import org.elasticsearch.search.sort.SortOrder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;
import org.egov.infra.admin.master.entity.BoundaryType;
import org.egov.infra.admin.master.service.BoundaryTypeService;
import org.egov.stms.utils.constants.SewerageTaxConstants;

import com.google.gson.GsonBuilder;

@Controller
@RequestMapping(value = "/reports")
public class SewerageRateDCBReportController {

    @Autowired
    private SewerageApplicationDetailsService sewerageApplicationDetailsService;

    @Autowired
    private SewerageDCBReporService sewerageDCBReporService;
    
    @Autowired
    private BoundaryService boundaryService;

    @Autowired
    private SewerageThirdPartyServices sewerageThirdPartyServices;
    
    @PersistenceContext
    EntityManager entityManager;
    
    @Autowired
    private SearchService searchService;
    
    @Autowired
    private CityService cityService;
    
    @Autowired 
    private ApplicationProperties applicationProperties;
    
    @Autowired
    private BoundaryTypeService boundaryTypeService;

    @ModelAttribute 
    public DCBReportWardwiseResult dCBReportWardWise(){
        return new DCBReportWardwiseResult();
    }
    
    @RequestMapping(value = "/sewerageRateReportView/{consumernumber}/{assessmentnumber}", method = RequestMethod.GET)
    public ModelAndView getSewerageRateReport(@ModelAttribute SewerageApplicationDetails sewerageApplicationDetails,
            @PathVariable final String consumernumber, @PathVariable final String assessmentnumber, final Model model,
            final ModelMap modelMap, final HttpServletRequest request) {
        SimpleDateFormat formatter = new SimpleDateFormat("dd-MM-yyyy");
        String currentDate = formatter.format(new Date());
        if (consumernumber != null) {
            sewerageApplicationDetails = sewerageApplicationDetailsService.findByApplicationNumber(consumernumber);
            if (sewerageApplicationDetails != null) {
                final AssessmentDetails propertyOwnerDetails = sewerageThirdPartyServices.getPropertyDetails(
                        assessmentnumber, request);
                model.addAttribute("applicationNumber", sewerageApplicationDetails.getApplicationNumber());
                model.addAttribute("propertyOwnerDetails", propertyOwnerDetails);
                model.addAttribute("assessmentnumber", assessmentnumber);
                model.addAttribute("currentDate", currentDate);
                model.addAttribute("dcbResultList",
                        sewerageDCBReporService.getSewerageRateDCBReport(sewerageApplicationDetails));
            }
        }
        return new ModelAndView("report-sewerageRate-view", "sewerageApplicationDetails", sewerageApplicationDetails);
    }
    
    @RequestMapping(value="/dcb-report-wardwise", method=RequestMethod.GET)
    public String WardwiseSearch(@ModelAttribute DCBReportWardwiseResult dcbReportWardwiseResult, final Model model ){
       // List<Boundary> wardsList=boundaryService.getActiveBoundariesByBndryTypeNameAndHierarchyTypeName("Ward", "REVENUE");
        model.addAttribute("dcbReportWardwiseResult",dcbReportWardwiseResult);
        List<String> propertytype= new ArrayList<String>();
        for(PropertyType s : PropertyType.values()){
        propertytype.add(s.toString());
        }
        List<String> boundaryList = new ArrayList<String>();
        List<Boundary> bList = new ArrayList<Boundary>();
        boundaryList.add("ALL");
        bList = boundaryService.getActiveBoundariesByBndryTypeNameAndHierarchyTypeName("Ward", "REVENUE");
        for(Boundary b : bList){
            boundaryList.add(b.toString());
        }
        model.addAttribute("propertyType", propertytype);
        model.addAttribute("wards",boundaryService.getActiveBoundariesByBndryTypeNameAndHierarchyTypeName("Ward", "REVENUE"));
        return "sewerage-dcbwardwise";
    }
    
    
    @RequestMapping(value="/dcbReportWardwiseList", method = RequestMethod.GET)
    public @ResponseBody void searchApplication(@ModelAttribute final DCBReportWardwiseResult searchRequest, final Model model,
            final HttpServletResponse response) throws IOException {
        List<Boundary> wardList = new ArrayList<Boundary>();
        List<Boundary> wards = new ArrayList<Boundary>();
        String wardValue = null;
        String[] wardIds;
        Map<String, List<SewerageApplicationDetails>> dcbMap = new HashMap<String, List<SewerageApplicationDetails>>();
        if(searchRequest.getMode()!=null){
            wardIds = searchRequest.getMode().split("~");
            for(String idValue : wardIds){
                Boundary ward = boundaryService.getBoundaryById(Long.parseLong(idValue));
                if(ward!=null)
                    wardList.add(ward);
            }
        }
        if(wardList!=null && !wardList.isEmpty()){
            searchRequest.setWards(wardList);
        }
        final City cityWebsite = cityService.getCityByURL(ApplicationThreadLocals.getDomainName());
        searchRequest.setUlbName(cityWebsite.getName());
        final Sort sort = Sort.by().field("clauses.applicationdate", SortOrder.DESC);
        if(null!=searchRequest.getWards() && !searchRequest.getWards().isEmpty() && searchRequest.getWards().get(0)!=null){
            wards.addAll(searchRequest.getWards());
        }
        else{
            wards.addAll(boundaryService.getBoundariesByBndryTypeNameAndHierarchyTypeName(SewerageTaxConstants.BOUNDARYTYPE_WARD, SewerageTaxConstants.HIERARCHYTYPE_REVENUE));
        }
        
        for(Boundary boundary : wards){
           if(boundary!=null){
            searchRequest.setRevenueWard(boundary.getLocalName());
            model.addAttribute("wardId", boundary.getId());
           }
        final SearchResult searchResult = searchService.search(asList(Index.SEWARAGE.toString()),
                asList(IndexType.SEWARAGESEARCH.toString()), searchRequest.searchQuery(),
                searchRequest.searchFilters(), sort, Page.NULL);
        
            for (Document document : searchResult.getDocuments()) {
                Map<String, String> searchableObjects = (Map<String, String>) document.getResource().get("searchable");
                Map<String, String> clausesObject = (Map<String, String>) document.getResource().get("clauses");
                List<SewerageApplicationDetails> appList = null;
                wardValue = clausesObject.get("revwardname");
                SewerageApplicationDetails sewerageAppDtl = sewerageApplicationDetailsService
                        .findByApplicationNumber(searchableObjects.get("consumernumber"));
                if (sewerageAppDtl != null) {
                    if (null != dcbMap.get(wardValue)) {
                        dcbMap.get(wardValue).add(sewerageAppDtl);
                    } else {
                        appList = new ArrayList<SewerageApplicationDetails>();
                        appList.add(sewerageAppDtl);
                        dcbMap.put(wardValue, appList);
                    }

                }
            }
        }

        IOUtils.write("{ \"data\":" + new GsonBuilder().setDateFormat(applicationProperties.defaultDatePattern()).create()
                .toJson(sewerageDCBReporService.getSewerageRateDCBWardwiseReport(dcbMap, searchRequest.getPropertyType()))
                + "}", response.getWriter());
    }
    
    @RequestMapping(value="/dcbViewWardConnections/{id}", method=RequestMethod.GET)
    public String getConnectionDCBReport(@ModelAttribute final DCBReportWardwiseResult searchRequest, final Model model, @PathVariable final String id, final HttpServletRequest request){
        String propType = null;
        Long wardId = null;
        String revenueWard = null;
        String[] wardDtl = id.split("~");
        List<String> wardList = new ArrayList<String>();
        List<SewerageApplicationDetails> applicationDetailList = new ArrayList<SewerageApplicationDetails>();
        Map<String, List<SewerageApplicationDetails>> wardConnectionMap = new HashMap<String, List<SewerageApplicationDetails>>();
        for(String value : wardDtl){
            wardList.add(value);
        }
        wardId = Long.parseLong(wardList.get(0));
        if(wardId!=null){
        Boundary boundary = boundaryService.getBoundaryById(wardId);
        revenueWard = boundary.getLocalName();
        }
        if(wardList.size()==2){
            if(null!=wardList.get(1) || wardList.get(1)!=""){
            propType = wardList.get(1);
            }
        }
        
        final City cityWebsite = cityService.getCityByURL(ApplicationThreadLocals.getDomainName());
        searchRequest.setUlbName(cityWebsite.getName());
        final Sort sort = Sort.by().field("clauses.applicationdate", SortOrder.DESC);
        searchRequest.setPropertyType(propType);
        searchRequest.setRevenueWard(revenueWard);
        final SearchResult searchResult = searchService.search(asList(Index.SEWARAGE.toString()),
                asList(IndexType.SEWARAGESEARCH.toString()), searchRequest.searchQuery(),
                searchRequest.searchFilters(), sort, Page.NULL);
        
        for(Document document : searchResult.getDocuments()){
            Map<String, String> searchableObjects = (Map<String,String>)document.getResource().get("searchable");
            List<SewerageApplicationDetails> appList = new ArrayList<SewerageApplicationDetails>();
            SewerageApplicationDetails sewerageAppDtl=sewerageApplicationDetailsService.findByApplicationNumber(searchableObjects.get("consumernumber"));
            sewerageAppDtl.setOwnerName(searchableObjects.get("consumername"));
            if(applicationDetailList.isEmpty())
              if(sewerageAppDtl!=null){
                    applicationDetailList.add(sewerageAppDtl);
              }
            if(null!=sewerageAppDtl){
                if(wardConnectionMap.isEmpty()){
                    wardConnectionMap.put(sewerageAppDtl.getApplicationNumber(), applicationDetailList);
                }
                else if(wardConnectionMap.get(sewerageAppDtl.getApplicationNumber())!=null){
                    wardConnectionMap.get(sewerageAppDtl.getApplicationNumber()).add(sewerageAppDtl);
                }
                else{
                    appList.add(sewerageAppDtl);
                    wardConnectionMap.put(sewerageAppDtl.getApplicationNumber(), appList);
                }
            }
        }
        model.addAttribute("revenueWard", revenueWard);
        model.addAttribute("dcbResultList",sewerageDCBReporService.getSewerageDCBWardConnections(wardConnectionMap, propType, request));
        return "sewerage-dcbWardConnections";
    }
}