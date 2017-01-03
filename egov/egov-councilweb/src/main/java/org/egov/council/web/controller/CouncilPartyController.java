package org.egov.council.web.controller;

import static org.egov.infra.utils.JsonUtils.toJSON;

import java.util.List;

import javax.validation.Valid;

import org.apache.commons.lang3.RandomStringUtils;
import org.egov.council.entity.CouncilParty;
import org.egov.council.service.CouncilPartyService;
import org.egov.council.web.adaptor.CouncilPartyJsonAdaptor;
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
@RequestMapping("/councilparty")
public class CouncilPartyController {
    private static final String COUNCIL_PARTY = "councilParty";
    private static final String COUNCILPARTY_NEW = "councilparty-new";
    private static final String COUNCILPARTY_RESULT = "councilparty-result";
    private static final String COUNCILPARTY_EDIT = "councilparty-edit";
    private static final String COUNCILPARTY_VIEW = "councilparty-view";
    private static final String COUNCILPARTY_SEARCH = "councilparty-search";
    @Autowired
    private CouncilPartyService councilPartyService;
    @Autowired
    private MessageSource messageSource;

    @RequestMapping(value = "/new", method = RequestMethod.GET)
    public String newForm(final Model model) {
        CouncilParty councilParty = new CouncilParty();
        councilParty.setCode(RandomStringUtils.random(4, Boolean.TRUE, Boolean.TRUE).toUpperCase());
        model.addAttribute(COUNCIL_PARTY, councilParty);

        return COUNCILPARTY_NEW;
    }

    @RequestMapping(value = "/create", method = RequestMethod.POST)
    public String create(@Valid @ModelAttribute final CouncilParty councilParty, final BindingResult errors,
            final Model model, final RedirectAttributes redirectAttrs) {

        if (councilParty != null && councilParty.getCode() == null)
            councilParty.setCode(RandomStringUtils.random(4, Boolean.TRUE, Boolean.TRUE).toUpperCase());

        if (errors.hasErrors()) {
            return COUNCILPARTY_NEW;
        }
        councilPartyService.create(councilParty);
        redirectAttrs.addFlashAttribute("message", messageSource.getMessage("msg.councilParty.success", null, null));
        return "redirect:/councilparty/result/" + councilParty.getId();
    }

    @RequestMapping(value = "/edit/{id}", method = RequestMethod.GET)
    public String edit(@PathVariable("id") final Long id, Model model) {
        CouncilParty councilParty = councilPartyService.findOne(id);
        model.addAttribute(COUNCIL_PARTY, councilParty);
        return COUNCILPARTY_EDIT;
    }

    @RequestMapping(value = "/update", method = RequestMethod.POST)
    public String update(@Valid @ModelAttribute final CouncilParty councilParty, final BindingResult errors,
            final Model model, final RedirectAttributes redirectAttrs) {
        if (errors.hasErrors()) {
            return COUNCILPARTY_EDIT;
        }
        councilPartyService.update(councilParty);
        redirectAttrs.addFlashAttribute("message", messageSource.getMessage("msg.councilParty.success", null, null));
        return "redirect:/councilparty/result/" + councilParty.getId();
    }

    @RequestMapping(value = "/view/{id}", method = RequestMethod.GET)
    public String view(@PathVariable("id") final Long id, Model model) {
        CouncilParty councilParty = councilPartyService.findOne(id);
        model.addAttribute(COUNCIL_PARTY, councilParty);
        return COUNCILPARTY_VIEW;
    }

    @RequestMapping(value = "/result/{id}", method = RequestMethod.GET)
    public String result(@PathVariable("id") final Long id, Model model) {
        CouncilParty councilParty = councilPartyService.findOne(id);
        model.addAttribute(COUNCIL_PARTY, councilParty);
        return COUNCILPARTY_RESULT;
    }

    @RequestMapping(value = "/search/{mode}", method = RequestMethod.GET)
    public String search(@PathVariable("mode") final String mode, Model model) {
        CouncilParty councilParty = new CouncilParty();
        model.addAttribute(COUNCIL_PARTY, councilParty);
        return COUNCILPARTY_SEARCH;

    }

    @RequestMapping(value = "/ajaxsearch/{mode}", method = RequestMethod.POST, produces = MediaType.TEXT_PLAIN_VALUE)
    @ResponseBody
    public String ajaxsearch(@PathVariable("mode") final String mode, Model model,
            @ModelAttribute final CouncilParty councilParty) {
        List<CouncilParty> searchResultList = councilPartyService.search(councilParty);
        return new StringBuilder("{ \"data\":")
                .append(toJSON(searchResultList, CouncilParty.class, CouncilPartyJsonAdaptor.class)).append("}")
                .toString();
    }
}