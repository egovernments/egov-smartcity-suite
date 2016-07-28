package org.egov.council.web.controller;

import java.util.List;

import javax.validation.Valid;

import org.apache.commons.lang.RandomStringUtils;
import org.egov.council.entity.CommitteeType;
import org.egov.council.service.CommitteeTypeService;
import org.egov.council.web.adaptor.CouncilCommitteeTypeJsonAdaptor;
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

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

@Controller
@RequestMapping("/committeetype")
public class CouncilCommitteeTypeController {

    private final static String COUNCILCOMMITTEETYPE_NEW = "councilcommittetype-new";
    private final static String COUNCILCOMMITTEETYPE_RESULT = "councilcommitteetype-result";
    private final static String COUNCILCOMMITTEETYPE_EDIT = "councilcommitteetype-edit";
    private final static String COUNCILCOMMITTEETYPE_VIEW = "councilcommitteetype-view";
    private final static String COUNCILCOMMITTEETYPE_SEARCH = "councilcommitteetype-search";

    @Autowired
    private CommitteeTypeService committeeTypeService;

    @Autowired
    private MessageSource messageSource;

    private void prepareNewForm(Model model) {
    }

    @RequestMapping(value = "/new", method = RequestMethod.GET)
    public String newForm(final Model model) {
        prepareNewForm(model);

        CommitteeType committeeType = new CommitteeType();

        if (committeeType != null && committeeType.getCode() == null)
            committeeType.setCode(RandomStringUtils.random(4, Boolean.TRUE, Boolean.TRUE).toUpperCase());

        model.addAttribute("committeeType", committeeType);
        return COUNCILCOMMITTEETYPE_NEW;
    }

    @RequestMapping(value = "/create", method = RequestMethod.POST)
    public String create(@Valid @ModelAttribute final CommitteeType committeeType, final BindingResult errors,
            final Model model, final RedirectAttributes redirectAttrs) {
        if (errors.hasErrors()) {
            prepareNewForm(model);
            return COUNCILCOMMITTEETYPE_NEW;
        }
        committeeTypeService.create(committeeType);
        redirectAttrs.addFlashAttribute("message",
                messageSource.getMessage("msg.councilCommitteeType.success", null, null));
        return "redirect:/committeetype/result/" + committeeType.getId();
    }

    @RequestMapping(value = "/view/{id}", method = RequestMethod.GET)
    public String view(@PathVariable("id") final Long id, Model model) {
        CommitteeType committeeType = committeeTypeService.findOne(id);
        prepareNewForm(model);
        model.addAttribute("committeeType", committeeType);
        return COUNCILCOMMITTEETYPE_VIEW;
    }

    @RequestMapping(value = "/edit/{id}", method = RequestMethod.GET)
    public String edit(@PathVariable("id") final Long id, Model model) {
        CommitteeType committeeType = committeeTypeService.findOne(id);
        prepareNewForm(model);
        model.addAttribute("committeeType", committeeType);
        return COUNCILCOMMITTEETYPE_EDIT;
    }

    @RequestMapping(value = "/update", method = RequestMethod.POST)
    public String update(@Valid @ModelAttribute final CommitteeType committeeType, final BindingResult errors,
            final Model model, final RedirectAttributes redirectAttrs) {

        if (errors.hasErrors()) {
            prepareNewForm(model);
            return COUNCILCOMMITTEETYPE_EDIT;
        }
        committeeTypeService.update(committeeType);
        redirectAttrs.addFlashAttribute("message",
                messageSource.getMessage("msg.councilCommitteeType.success", null, null));
        return "redirect:/committeetype/result/" + committeeType.getId();
    }

    @RequestMapping(value = "/result/{id}", method = RequestMethod.GET)
    public String result(@PathVariable("id") final Long id, Model model) {
        CommitteeType committeeType = committeeTypeService.findOne(id);
        model.addAttribute("committeeType", committeeType);
        return COUNCILCOMMITTEETYPE_RESULT;
    }

    @RequestMapping(value = "/search/{mode}", method = RequestMethod.GET)
    public String search(@PathVariable("mode") final String mode, Model model) {
        CommitteeType committeeType = new CommitteeType();
        prepareNewForm(model);
        model.addAttribute("committeeType", committeeType);
        return COUNCILCOMMITTEETYPE_SEARCH;

    }

    @RequestMapping(value = "/ajaxsearch/{mode}", method = RequestMethod.POST, produces = MediaType.TEXT_PLAIN_VALUE)
    public @ResponseBody String ajaxsearch(@PathVariable("mode") final String mode, Model model,
            @ModelAttribute final CommitteeType committeeType) {
        List<CommitteeType> searchResultList = committeeTypeService.search(committeeType);
        String result = new StringBuilder("{ \"data\":").append(toSearchResultJson(searchResultList)).append("}")
                .toString();
        return result;
    }

    public Object toSearchResultJson(final Object object) {
        final GsonBuilder gsonBuilder = new GsonBuilder();
        final Gson gson = gsonBuilder.registerTypeAdapter(CommitteeType.class, new CouncilCommitteeTypeJsonAdaptor())
                .create();
        final String json = gson.toJson(object);
        return json;
    }
}
