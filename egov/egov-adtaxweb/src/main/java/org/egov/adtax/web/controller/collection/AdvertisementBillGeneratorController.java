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

package org.egov.adtax.web.controller.collection;

import static org.springframework.web.bind.annotation.RequestMethod.GET;
import static org.springframework.web.bind.annotation.RequestMethod.POST;

import java.io.Serializable;
import java.math.BigDecimal;

import org.egov.adtax.entity.Advertisement;
import org.egov.adtax.entity.AgencyWiseCollection;
import org.egov.adtax.service.AdvertisementDemandService;
import org.egov.adtax.service.AdvertisementService;
import org.egov.adtax.service.AgencyService;
import org.egov.adtax.service.AgencyWiseCollectionService;
import org.egov.adtax.service.collection.AdvertisementBillServiceImpl;
import org.egov.adtax.service.collection.AdvertisementBillable;
import org.egov.adtax.service.collection.AgencyWiseBillServiceImpl;
import org.egov.adtax.service.collection.AgencyWiseBillable;
import org.egov.adtax.utils.constants.AdvertisementTaxConstants;
import org.egov.infra.persistence.utils.SequenceNumberGenerator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
@RequestMapping(value = "/hoarding")
public class AdvertisementBillGeneratorController {

    private @Autowired AdvertisementBillServiceImpl advertisementBillServiceImpl;
    private @Autowired AdvertisementService advertisementService;
    private @Autowired AdvertisementBillable advertisementBillable;
    private @Autowired AgencyWiseBillable agencyWiseBillable;

    private @Autowired AgencyWiseBillServiceImpl agencyWiseBillServiceImpl;
    private @Autowired SequenceNumberGenerator sequenceNumberGenerator;
    private @Autowired AdvertisementDemandService advertisementDemandService;
    private @Autowired AgencyWiseCollectionService agencyWiseCollectionService;
    private @Autowired AgencyService agencyService;
    private String ADVERTISEMENT_BILLNUMBER = "SEQ_advertisementbill_NUMBER";

    
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
    }

    @RequestMapping(value = "/generatebill/{collectionType}/{hoardingCode}", method = GET)
    public String showCollectFeeForm(final Model model, @PathVariable final String collectionType,
            @PathVariable final String hoardingCode) {

        final Advertisement advertisement = advertisementService.findByAdvertisementNumber(hoardingCode);
        if (advertisement != null && advertisement.getDemandId() != null) {
            // CHECK ANY DEMAND PENDING OR NOT
            if (!advertisementDemandService.checkAnyTaxIsPendingToCollect(advertisement)) {
                model.addAttribute("message", "msg.collection.noPendingTax");
                return "collectAdvtax-error";
            }
            
             advertisementBillable.setCollectionType(AdvertisementTaxConstants.ADVERTISEMENT_COLLECTION_TYPE);
            
             if (collectionType != null && !"".equals(collectionType)){
                 if(collectionType.equalsIgnoreCase("hoarding"))
                     advertisementBillable.setCollectionType(AdvertisementTaxConstants.ADVERTISEMENT_COLLECTION_TYPE);  
                 else
                     advertisementBillable.setCollectionType(collectionType);
                           
             }
            advertisementBillable.setAdvertisement(advertisement);

            final Serializable referenceNumber = sequenceNumberGenerator.getNextSequence(ADVERTISEMENT_BILLNUMBER);
            advertisementBillable.setReferenceNumber(AdvertisementTaxConstants.SERVICE_CODE.concat(String.format(
                    "%s%06d", "", referenceNumber)));
            model.addAttribute("collectxml", advertisementBillServiceImpl.getBillXML(advertisementBillable));
            return "collectAdvtax-redirection";
        } else {
            model.addAttribute("message", "msg.collection.noPendingTax");
            return "collectAdvtax-error";
        }
        // return "collectAdvtax-error";

    }

    @RequestMapping(value = "/generatebill/{hoardingCode}", method = POST)
    public String payTax(@ModelAttribute Advertisement advertisement, @PathVariable final String collectionType,
            final RedirectAttributes redirectAttributes, @PathVariable final String hoardingCode, final Model model) {

        advertisement = advertisementService.findByAdvertisementNumber(hoardingCode);
        
        advertisementBillable.setCollectionType(AdvertisementTaxConstants.ADVERTISEMENT_COLLECTION_TYPE);
        if (advertisementBillable != null && collectionType!=null)
            advertisementBillable.setCollectionType(collectionType);
            
        advertisementBillable.setAdvertisement(advertisement);
        model.addAttribute("collectxml", advertisementBillServiceImpl.getBillXML(advertisementBillable));

        return "collecttax-redirection";
    }
}
