package org.egov.lcms.web.controller.transactions;

import java.util.ArrayList;
import java.util.List;

import javax.validation.Valid;

import org.egov.lcms.masters.service.CaseTypeMasterService;
import org.egov.lcms.masters.service.CourtMasterService;
import org.egov.lcms.masters.service.CourtTypeMasterService;
import org.egov.lcms.masters.service.PetitionTypeMasterService;
import org.egov.lcms.transactions.entity.BipartisanDetails;
import org.egov.lcms.transactions.entity.LegalCase;
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
@RequestMapping(value = "/application/")
public class ViewAndEditLegalCaseController {

    @Autowired
    private LegalCaseService legalCaseService;

    @Autowired
    private CourtTypeMasterService courtTypeMasterService;

    @Autowired
    private CaseTypeMasterService caseTypeMasterService;

    @Autowired
    private PetitionTypeMasterService petitiontypeMasterService;

    @Autowired
    private CourtMasterService courtMasterService;

    @ModelAttribute
    private LegalCase getLegalCase(@PathVariable final String lcNumber) {
        final LegalCase legalCase = legalCaseService.findByLcNumber(lcNumber);
        return legalCase;
    }

    @RequestMapping(value = "/view/{lcNumber}", method = RequestMethod.GET)
    public String view(@PathVariable("lcNumber") final String lcNumber, final Model model) {
        final LegalCase legalCase = legalCaseService.findByLcNumber(lcNumber);
        model.addAttribute("legalCase", legalCase);
        final List<BipartisanDetails> pettempList = new ArrayList<BipartisanDetails>();
        final List<BipartisanDetails> respoTempList = new ArrayList<BipartisanDetails>();
        for (final BipartisanDetails dd : legalCase.getBipartisanDetails())
            if (dd.getIsRepondent())
                pettempList.add(dd);
            else
                respoTempList.add(dd);
        model.addAttribute("mode", "view");
        model.addAttribute("pettempList", pettempList);
        model.addAttribute("respoTempList", respoTempList);
        return "legalcasedetails-view";
    }

    @RequestMapping(value = "/edit/{lcNumber}", method = RequestMethod.GET)
    public String edit(@PathVariable final String lcNumber, final Model model) {
        final LegalCase legalCase = legalCaseService.findByLcNumber(lcNumber);
        model.addAttribute("legalCase", legalCase);
        setDropDownValues(model);
        final List<BipartisanDetails> pettempList = new ArrayList<BipartisanDetails>();
        final List<BipartisanDetails> respoTempList = new ArrayList<BipartisanDetails>();
        for (final BipartisanDetails dd : legalCase.getBipartisanDetails())
            if (dd.getIsRepondent())
                pettempList.add(dd);
            else
                respoTempList.add(dd);
        model.addAttribute("pettempList", pettempList);
        model.addAttribute("respoTempList", respoTempList);
        model.addAttribute("mode", "edit");
        return "legalcase-edit";
    }

    @RequestMapping(value = "/edit/{lcNumber}", method = RequestMethod.POST)
    public String update(@Valid @ModelAttribute final LegalCase legalCase, @PathVariable final String lcNumber,
            final BindingResult errors, final Model model, final RedirectAttributes redirectAttrs) {
        if (errors.hasErrors())
            return "legalcase-edit";
        legalCaseService.persist(legalCase);
        setDropDownValues(model);
        redirectAttrs.addFlashAttribute("legalCase", legalCase);
        model.addAttribute("mode", "edit");
        model.addAttribute("message", "LegalCase updated successfully.");
        return "legalcase-success";
    }

    private void setDropDownValues(final Model model) {
        model.addAttribute("courtTypeList", courtTypeMasterService.getCourtTypeList());
        model.addAttribute("courtsList", courtMasterService.findAll());
        model.addAttribute("caseTypeList", caseTypeMasterService.getCaseTypeList());
        model.addAttribute("petitiontypeList", petitiontypeMasterService.getPetitiontypeList());
    }

}
