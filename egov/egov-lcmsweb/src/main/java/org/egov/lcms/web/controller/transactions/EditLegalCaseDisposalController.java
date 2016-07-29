package org.egov.lcms.web.controller.transactions;

import java.util.List;
import javax.validation.Valid;
import org.egov.lcms.transactions.entity.LegalCase;
import org.egov.lcms.transactions.entity.LegalCaseDisposal;
import org.egov.lcms.transactions.service.LegalCaseDisposalService;
import org.egov.lcms.transactions.service.LegalCaseService;
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
@RequestMapping(value = "/legalcasedisposal")
public class EditLegalCaseDisposalController {
    @Autowired
    private LegalCaseDisposalService legalCaseDisposalService;

    @Autowired
    private LegalCaseService legalCaseService;


    @ModelAttribute
    private LegalCase getLegalCase(@PathVariable final String lcNumber) {
        final LegalCase legalCase = legalCaseService.findByLcNumber(lcNumber);
        return legalCase;
    }

  

    @RequestMapping(value = "/edit/{lcNumber}", method = RequestMethod.GET)
    public String edit(@PathVariable final String lcNumber, final Model model) {
        final LegalCase legalCase = legalCaseService.findByLcNumber(lcNumber);
        final List<LegalCaseDisposal> legalCaseDisposalList = getLegalCase(lcNumber).getLegalcaseDisposal();
        final LegalCaseDisposal legalCaseDisposalObj = legalCaseDisposalList.get(0);
        
        model.addAttribute("legalCase", legalCase);
        model.addAttribute("legalCaseDisposal", legalCaseDisposalObj);
        model.addAttribute("mode", "edit");
        return "legalcaseDisposal-edit";
    }

    @RequestMapping(value = "/edit/{lcNumber}", method = RequestMethod.POST)
    public String update(@Valid @ModelAttribute final LegalCaseDisposal legalCaseDisposal, @PathVariable final String lcNumber,
            final BindingResult errors, final Model model, final RedirectAttributes redirectAttrs) {

        if (errors.hasErrors()) {
           
            return "legalcaseDisposal-edit";
        }
        legalCaseDisposalService.persist(legalCaseDisposal);
        redirectAttrs.addFlashAttribute("legalCaseDisposal", legalCaseDisposal);
        model.addAttribute("message", "Close Case updated successfully.");
        model.addAttribute("mode", "edit");
        return "legalcaseDisposal-success";
    }
}
