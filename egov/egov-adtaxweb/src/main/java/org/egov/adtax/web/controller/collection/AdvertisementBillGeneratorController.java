package org.egov.adtax.web.controller.collection;

import static org.springframework.web.bind.annotation.RequestMethod.GET;
import static org.springframework.web.bind.annotation.RequestMethod.POST;

import java.io.Serializable;

import org.egov.adtax.entity.Hoarding;
import org.egov.adtax.service.AdvertisementDemandService;
import org.egov.adtax.service.HoardingService;
import org.egov.adtax.service.collection.AdvertisementBillServiceImpl;
import org.egov.adtax.service.collection.AdvertisementBillable;
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
 
    private final AdvertisementBillServiceImpl  advertisementBillServiceImpl;
    private final HoardingService hoardingService;
    private final AdvertisementBillable  advertisementBillable;
    
    @Autowired
    private SequenceNumberGenerator sequenceNumberGenerator;
    
    @Autowired
    private  AdvertisementDemandService advertisementDemandService;
    
    
    String ADVERTISEMENT_BILLNUMBER = "SEQ_advertisementbill_NUMBER";
    @Autowired
    public AdvertisementBillGeneratorController(final AdvertisementBillable  advertisementBillable,final AdvertisementBillServiceImpl  advertisementBillServiceImpl, final HoardingService hoardingService) {
        this.hoardingService = hoardingService;
        this.advertisementBillServiceImpl = advertisementBillServiceImpl;
        this.advertisementBillable=advertisementBillable;
   }
    
    @RequestMapping(value = "/generatebill/{collectionType}/{hoardingCode}", method = GET) 
    public String showCollectFeeForm(final Model model,   @PathVariable final String collectionType,@PathVariable final String hoardingCode) {
      
      
        Hoarding  hoarding = hoardingService.findByHoardingNumber(hoardingCode);
        if(hoarding!=null && hoarding.getDemandId()!=null)
        {
            // CHECK ANY DEMAND PENDING OR NOT
           if(!advertisementDemandService.checkAnyTaxIsPendingToCollect(hoarding))
               {
               model.addAttribute("message", "msg.collection.noPendingTax");  
               return "collectAdvtax-error";
               } 
           
            if (collectionType != null && !"".equals(collectionType)) {
                advertisementBillable.setCollectionType(collectionType);
            } else {
                advertisementBillable.setCollectionType("Hoarding");
            }
            advertisementBillable.setHoarding(hoarding);
       
            Serializable referenceNumber=sequenceNumberGenerator.getNextSequence(ADVERTISEMENT_BILLNUMBER); 
            advertisementBillable.setReferenceNumber(AdvertisementTaxConstants.SERVICE_CODE.concat(String.format("%s%06d", "", referenceNumber)));
            model.addAttribute("collectxml", advertisementBillServiceImpl.getBillXML(advertisementBillable));
            return "collectAdvtax-redirection";
        }else
        {
            model.addAttribute("message", "msg.collection.noPendingTax");  
            return "collectAdvtax-error";
        }
      //  return "collectAdvtax-error";
       
    }

    @RequestMapping(value = "/generatebill/{hoardingCode}", method = POST)
    public String payTax(@ModelAttribute Hoarding hoarding,  @PathVariable final String collectionType,
            final RedirectAttributes redirectAttributes, @PathVariable final String hoardingCode, final Model model) {
       
        hoarding = hoardingService.findByHoardingNumber(hoardingCode);
        if (advertisementBillable != null) {
            advertisementBillable.setCollectionType(collectionType);
        } else {
            advertisementBillable.setCollectionType("Hoarding");
        }
        advertisementBillable.setHoarding(hoarding);
        model.addAttribute("collectxml", advertisementBillServiceImpl.getBillXML(advertisementBillable));

        return "collecttax-redirection";
    }
}
