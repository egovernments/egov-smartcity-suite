package org.egov.council.web.controller;

import static org.egov.infra.utils.JsonUtils.toJSON;

import java.util.List;

import javax.validation.Valid;

import org.apache.commons.lang3.RandomStringUtils;
import org.egov.council.entity.CouncilDesignation;
import org.egov.council.service.CouncilDesignationService;
import org.egov.council.web.adaptor.CouncilDesignationJsonAdaptor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
@RequestMapping("/councildesignation")
public class CouncilDesignationController {
    private static final String COUNCIL_DESIGNATION = "councilDesignation";
    private static final String COUNCILDESIGNATION_NEW = "councildesignation-new";
    private static final String COUNCILDESIGNATION_RESULT = "councildesignation-result";
    private static final String COUNCILDESIGNATION_EDIT = "councildesignation-edit";
    private static final String COUNCILDESIGNATION_VIEW = "councildesignation-view";
    private static final String COUNCILDESIGNATION_SEARCH = "councildesignation-search";
    @Autowired
    private CouncilDesignationService councilDesignationService;
    @Autowired
    private MessageSource messageSource;

    @RequestMapping(value = "/new", method = RequestMethod.GET)
    public String newForm(final Model model) {
        CouncilDesignation councilDesignation = new CouncilDesignation();
        councilDesignation.setCode(RandomStringUtils.random(4, Boolean.TRUE, Boolean.TRUE).toUpperCase());

        model.addAttribute(COUNCIL_DESIGNATION, councilDesignation);

        return COUNCILDESIGNATION_NEW;
    }

    @RequestMapping(value = "/create", method = RequestMethod.POST)
    public String create(@Valid @ModelAttribute final CouncilDesignation councilDesignation,
            final BindingResult errors, final Model model, final RedirectAttributes redirectAttrs) {

        if (councilDesignation != null && councilDesignation.getCode() == null)
            councilDesignation.setCode(RandomStringUtils.random(4, Boolean.TRUE, Boolean.TRUE).toUpperCase());

        if (errors.hasErrors()) {
            return COUNCILDESIGNATION_NEW;
        }

        councilDesignationService.create(councilDesignation);
        redirectAttrs.addFlashAttribute("message",
                messageSource.getMessage("msg.councilDesignation.success", null, null));
        return "redirect:/councildesignation/result/" + councilDesignation.getId();
    }

    @RequestMapping(value = "/edit/{id}", method = RequestMethod.GET)
    public String edit(@PathVariable("id") final Long id, Model model) {
        CouncilDesignation councilDesignation = councilDesignationService.findOne(id);
        model.addAttribute(COUNCIL_DESIGNATION, councilDesignation);
        return COUNCILDESIGNATION_EDIT;
    }

    @RequestMapping(value = "/update", method = RequestMethod.POST)
    public String update(@Valid @ModelAttribute final CouncilDesignation councilDesignation,
            final BindingResult errors, final Model model, final RedirectAttributes redirectAttrs) {
        if (errors.hasErrors()) {
            return COUNCILDESIGNATION_EDIT;
        }
        councilDesignationService.update(councilDesignation);
        redirectAttrs.addFlashAttribute("message",
                messageSource.getMessage("msg.councilDesignation.success", null, null));
        return "redirect:/councildesignation/result/" + councilDesignation.getId();
    }

    @RequestMapping(value = "/view/{id}", method = RequestMethod.GET)
    public String view(@PathVariable("id") final Long id, Model model) {
        CouncilDesignation councilDesignation = councilDesignationService.findOne(id);
        model.addAttribute(COUNCIL_DESIGNATION, councilDesignation);
        return COUNCILDESIGNATION_VIEW;
    }

    @RequestMapping(value = "/result/{id}", method = RequestMethod.GET)
    public String result(@PathVariable("id") final Long id, Model model) {
        CouncilDesignation councilDesignation = councilDesignationService.findOne(id);
        model.addAttribute(COUNCIL_DESIGNATION, councilDesignation);
        return COUNCILDESIGNATION_RESULT;
    }

    @RequestMapping(value = "/search/{mode}", method = RequestMethod.GET)
    public String search(@PathVariable("mode") final String mode, Model model) {
        CouncilDesignation councilDesignation = new CouncilDesignation();
        model.addAttribute(COUNCIL_DESIGNATION, councilDesignation);
        return COUNCILDESIGNATION_SEARCH;

    }

    @RequestMapping(value = "/ajaxsearch/{mode}", method = RequestMethod.POST, produces = MediaType.TEXT_PLAIN_VALUE)
    @ResponseBody
    public String ajaxsearch(@PathVariable("mode") final String mode, Model model,
            @ModelAttribute final CouncilDesignation councilDesignation) {
        List<CouncilDesignation> searchResultList = councilDesignationService.search(councilDesignation);
        return new StringBuilder("{ \"data\":")
                .append(toJSON(searchResultList, CouncilDesignation.class, CouncilDesignationJsonAdaptor.class)).append("}")
                .toString();
    }

}