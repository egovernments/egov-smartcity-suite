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

package org.egov.adtax.web.controller.collection;

import org.egov.adtax.entity.Advertisement;
import org.egov.adtax.entity.AdvertisementPermitDetail;
import org.egov.adtax.entity.AgencyWiseCollection;
import org.egov.adtax.entity.AgencyWiseCollectionSearch;
import org.egov.adtax.service.AdvertisementDemandService;
import org.egov.adtax.service.AdvertisementPermitDetailService;
import org.egov.adtax.service.AgencyService;
import org.egov.adtax.service.AgencyWiseCollectionService;
import org.egov.adtax.service.collection.AdvertisementBillServiceImpl;
import org.egov.adtax.service.collection.AdvertisementBillable;
import org.egov.adtax.service.collection.AgencyWiseBillServiceImpl;
import org.egov.adtax.service.collection.AgencyWiseBillable;
import org.egov.adtax.utils.constants.AdvertisementTaxConstants;
import org.egov.infra.persistence.utils.DatabaseSequenceProvider;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.List;

import static org.springframework.web.bind.annotation.RequestMethod.GET;
import static org.springframework.web.bind.annotation.RequestMethod.POST;

@Controller
@RequestMapping(value = "/hoarding")
public class AdvertisementBillGeneratorController {

    private static final String ADVERTISEMENT_BILLNUMBER = "SEQ_advertisementbill_NUMBER";

    @Autowired
    private AdvertisementBillServiceImpl advertisementBillServiceImpl;

    @Autowired
    private AdvertisementBillable advertisementBillable;

    @Autowired
    private AgencyWiseBillable agencyWiseBillable;

    @Autowired
    private AgencyWiseBillServiceImpl agencyWiseBillServiceImpl;

    @Autowired
    private DatabaseSequenceProvider databaseSequenceProvider;

    @Autowired
    private AdvertisementDemandService advertisementDemandService;

    @Autowired
    private AgencyWiseCollectionService agencyWiseCollectionService;

    @Autowired
    private AgencyService agencyService;

    @Autowired
    private AdvertisementPermitDetailService advertisementPermitDetailService;


 /*
    @RequestMapping(value = "/collectTaxByAgency/{agencyName}/{hoardingIds}/{total}", method = GET)
    public String collectFeeByAgency(final Model model, @PathVariable final String hoardingIds,
            @PathVariable final String agencyName, @PathVariable final BigDecimal total) {

        if (hoardingIds != null && agencyName != null && total != null && total.compareTo(BigDecimal.ZERO) > 0) {

            final String hoardingList[] = hoardingIds.split("~");
            if (hoardingList != null && hoardingList.length > 0 && hoardingList[0] != null && hoardingList[0] != "") {

                // From List of Hoardings, Saved demand details in Agencywise
                // collection table and send billable object.
                // Iterate hordingids, get demand pending,penalty amount and
                // build agencywisecollection detail object.
                final Serializable referenceNumber = sequenceNumberGenerator.getNextSequence(ADVERTISEMENT_BILLNUMBER);

                final AgencyWiseCollection agencyWiseCollection = agencyWiseCollectionService
                        .buildAgencyWiseObjectByHoardings(hoardingList);

                agencyWiseCollection.setAgency(agencyService.findByName(agencyName));
                agencyWiseCollection.setBillNumber(AdvertisementTaxConstants.SERVICE_CODE.concat(String.format(
                        "%s%06d", "", referenceNumber)));
                agencyWiseCollectionService.createAgencyWiseCollection(agencyWiseCollection);

                agencyWiseBillable.setReferenceNumber(AdvertisementTaxConstants.SERVICE_CODE.concat(String.format(
                        "%s%06d", "", referenceNumber)));
                agencyWiseBillable.setAgencyWiseCollection(agencyWiseCollection);
                model.addAttribute("collectxml", agencyWiseBillServiceImpl.getBillXML(agencyWiseBillable));

                return "collectAdvtax-redirection";
            } else {

                model.addAttribute("message", "msg.collection.noPendingTax");
                return "collectAdvtax-error";
            }
        } else {
            model.addAttribute("message", "msg.collection.noPendingTax");
            return "collectAdvtax-error";
        }
        // return "collectAdvtax-error";
    }*/
    
    @RequestMapping(value = "/collectTaxByAgency", method = POST)
    public String collectFeeByAgencytest(final Model model,  final String hoardingIds,final String agencyName, final BigDecimal total) {

        if (hoardingIds != null && agencyName != null) {
            final String hoardingList[] = hoardingIds.split("~");
            if (hoardingList != null && hoardingList.length > 0 && hoardingList[0] != null && hoardingList[0] != "") {
         
              List<AgencyWiseCollectionSearch> agencyWiseCollectionList= agencyWiseCollectionService.buildAgencyWiseCollectionSearch(hoardingList);
             
                if (agencyWiseCollectionList != null && agencyWiseCollectionList.size() > 0) {

                    AgencyWiseCollectionSearch agencyWiseCollectionSearch = new AgencyWiseCollectionSearch();
                    agencyWiseCollectionSearch.setAgencyName(agencyName);
                    agencyWiseCollectionSearch.setAgencyWiseCollectionList(agencyWiseCollectionList);
                    model.addAttribute("agencyWiseCollectionSearch", agencyWiseCollectionSearch);
                    return "agencyWiseTaxCollect";
                }else
                {
                    model.addAttribute("message", "msg.collection.noPendingTax");
                    return "collectAdvtax-error";
                }
                
            } else {

                model.addAttribute("message", "msg.collection.noPendingTax");
                return "collectAdvtax-error";
            }
        } else {
            model.addAttribute("message", "msg.collection.noPendingTax");
            return "collectAdvtax-error";
        }
        // return "collectAdvtax-error";
    }


    @RequestMapping(value = "/collectTaxByAgency/{agencyName}", method = POST) 
    public String successView(@PathVariable("agencyName") final String agencyName,
            final Model model,  @ModelAttribute  AgencyWiseCollectionSearch agencyWiseCollectionSearch) {
        StringBuffer hoardings = new StringBuffer();
     
        if (agencyWiseCollectionSearch != null && agencyWiseCollectionSearch.getAgencyWiseCollectionList() != null
                && agencyWiseCollectionSearch.getAgencyWiseCollectionList().size() > 0) {
            for (AgencyWiseCollectionSearch searchResult : agencyWiseCollectionSearch.getAgencyWiseCollectionList()) {
                if(searchResult.isSelectedForCollection())
                hoardings.append(searchResult.getAdvertisementPermitId()).append("~");
            }
            final String hoardingList[] = hoardings.toString().split("~");
            if (hoardingList != null && hoardingList.length > 0 && hoardingList[0] != null && hoardingList[0] != "") {

                // From List of Hoardings, Saved demand details in Agencywise
                // collection table and send billable object.
                // Iterate hordingids, get demand pending,penalty amount and
                // build agencywisecollection detail object.
                final Serializable referenceNumber = databaseSequenceProvider.getNextSequence(ADVERTISEMENT_BILLNUMBER);

                final AgencyWiseCollection agencyWiseCollection = agencyWiseCollectionService
                        .buildAgencyWiseObjectByHoardings(hoardingList);

                agencyWiseCollection.setAgency(agencyService.findByName(agencyName));
                agencyWiseCollection.setBillNumber(AdvertisementTaxConstants.SERVICE_CODE.concat(String.format(
                        "%s%06d", "", referenceNumber)));
                agencyWiseCollectionService.createAgencyWiseCollection(agencyWiseCollection);

                agencyWiseBillable.setReferenceNumber(AdvertisementTaxConstants.SERVICE_CODE.concat(String.format(
                        "%s%06d", "", referenceNumber)));
                agencyWiseBillable.setAgencyWiseCollection(agencyWiseCollection);
                model.addAttribute("collectxml", agencyWiseBillServiceImpl.getBillXML(agencyWiseBillable));

                return "collectAdvtax-redirection";
            }
        } else {
            model.addAttribute("message", "msg.collection.noPendingTax");
            return "collectAdvtax-error";
        }
        return "collectAdvtax-redirection";
    }
    
    @RequestMapping(value = "/getPendingTaxByAgency/{agencyName}", method = GET)
    public String getAgencyWiseDetail(final Model model, 
            @PathVariable final String agencyName) {

        if (agencyName != null) {
            model.addAttribute("agency", agencyService.findByName(agencyName));
        }
        
        return null;
        
    }
    @RequestMapping(value = "/generatebill/{collectionType}/{id}", method = GET)
    public String showCollectFeeForm(final Model model, @PathVariable final String collectionType,
            @PathVariable final String id) {

        AdvertisementPermitDetail advertisementPermitDetail = advertisementPermitDetailService.findById(Long.valueOf(id));
        if (advertisementPermitDetail != null) {
            Advertisement advertisement = advertisementPermitDetail.getAdvertisement();

            // final Advertisement advertisement = advertisementService.findBy(Long.valueOf(hoardingCode));
            if (advertisement != null && advertisement.getDemandId() != null) {
                // CHECK ANY DEMAND PENDING OR NOT
                if (!advertisementDemandService.checkAnyTaxIsPendingToCollect(advertisement)) {
                    model.addAttribute("message", "msg.collection.noPendingTax");
                    return "collectAdvtax-error";
                }

                advertisementBillable.setCollectionType(AdvertisementTaxConstants.ADVERTISEMENT_COLLECTION_TYPE);

                if (collectionType != null && !"".equals(collectionType)) {
                    if (collectionType.equalsIgnoreCase("hoarding"))
                        advertisementBillable.setCollectionType(AdvertisementTaxConstants.ADVERTISEMENT_COLLECTION_TYPE);
                    else
                        advertisementBillable.setCollectionType(collectionType);

                }
                advertisementBillable.setAdvertisement(advertisement);

                advertisementBillable.setReferenceNumber(AdvertisementTaxConstants.SERVICE_CODE.concat(String.format(
                        "%s%06d", "", databaseSequenceProvider.getNextSequence(ADVERTISEMENT_BILLNUMBER))));
                model.addAttribute("collectxml", advertisementBillServiceImpl.getBillXML(advertisementBillable));
                return "collectAdvtax-redirection";
            } else {
                model.addAttribute("message", "msg.collection.noPendingTax");
                return "collectAdvtax-error";
            }
        }
        model.addAttribute("message", "msg.invalied.request");
         return "collectAdvtax-error";
    }

    @RequestMapping(value = "/generatebill/{id}", method = POST)
    public String payTax(@ModelAttribute Advertisement advertisement, @PathVariable final String collectionType,
            final RedirectAttributes redirectAttributes, @PathVariable final String id, final Model model) {

        AdvertisementPermitDetail advertisementPermitDetail = advertisementPermitDetailService.findById(Long.valueOf(id));
        if(advertisementPermitDetail!=null)
              advertisement = advertisementPermitDetail.getAdvertisement();
        //advertisement = advertisementService.findByAdvertisementNumber(hoardingCode);
        
        
        advertisementBillable.setCollectionType(AdvertisementTaxConstants.ADVERTISEMENT_COLLECTION_TYPE);
        if (advertisementBillable != null && collectionType!=null)
            advertisementBillable.setCollectionType(collectionType);
            
        advertisementBillable.setAdvertisement(advertisement);
        model.addAttribute("collectxml", advertisementBillServiceImpl.getBillXML(advertisementBillable));

        return "collecttax-redirection";
    }
}
