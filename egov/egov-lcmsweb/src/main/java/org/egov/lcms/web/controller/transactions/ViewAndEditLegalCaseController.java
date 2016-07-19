package org.egov.lcms.web.controller.transactions;

import org.egov.lcms.transactions.entity.LegalCase;
import org.egov.lcms.transactions.service.LegalCaseService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

@Controller
@RequestMapping(value = "/application/")
public class ViewAndEditLegalCaseController extends GenericLegalCaseController {

    @Autowired
    private LegalCaseService legalCaseService;

    @ModelAttribute
    private LegalCase getLegalCase(@PathVariable final String lcNumber) {
        final LegalCase legalCase = legalCaseService.findByLcNumber(lcNumber);
        return legalCase;
    }

    @RequestMapping(value = "/view/{lcNumber}", method = RequestMethod.GET)
    public String view(@PathVariable("lcNumber") final String lcNumber, final Model model) {
        final LegalCase legalCase = legalCaseService.findByLcNumber(lcNumber);
        model.addAttribute("legalCase", legalCase);
        model.addAttribute("mode", "readOnly");
        return "legalcasedetails-view";
    }

}
