package org.egov.council.web.controller;

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

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

@Controller
@RequestMapping("/councildesignation")
public class CouncilDesignationController {
    private final static String COUNCILDESIGNATION_NEW = "councildesignation-new";
    private final static String COUNCILDESIGNATION_RESULT = "councildesignation-result";
    private final static String COUNCILDESIGNATION_EDIT = "councildesignation-edit";
    private final static String COUNCILDESIGNATION_VIEW = "councildesignation-view";
    private final static String COUNCILDESIGNATION_SEARCH = "councildesignation-search";
    @Autowired
    private CouncilDesignationService councilDesignationService;
    @Autowired
    private MessageSource messageSource;

    private void prepareNewForm(Model model) {
    }

    @RequestMapping(value = "/new", method = RequestMethod.GET)
    public String newForm(final Model model) {
        prepareNewForm(model);
        CouncilDesignation councilDesignation = new CouncilDesignation();
        if (councilDesignation != null && councilDesignation.getCode() == null)
            councilDesignation.setCode(RandomStringUtils.random(4, Boolean.TRUE, Boolean.TRUE).toUpperCase());

        model.addAttribute("councilDesignation", councilDesignation);

        return COUNCILDESIGNATION_NEW;
    }

    @RequestMapping(value = "/create", method = RequestMethod.POST)
    public String create(@Valid @ModelAttribute final CouncilDesignation councilDesignation,
            final BindingResult errors, final Model model, final RedirectAttributes redirectAttrs) {
       
        if(councilDesignation!=null && councilDesignation.getCode()==null)
            councilDesignation.setCode(RandomStringUtils.random(4, Boolean.TRUE, Boolean.TRUE).toUpperCase());
      
        if (errors.hasErrors()) {
            prepareNewForm(model);
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
        prepareNewForm(model);
        model.addAttribute("councilDesignation", councilDesignation);
        return COUNCILDESIGNATION_EDIT;
    }

    @RequestMapping(value = "/update", method = RequestMethod.POST)
    public String update(@Valid @ModelAttribute final CouncilDesignation councilDesignation,
            final BindingResult errors, final Model model, final RedirectAttributes redirectAttrs) {
        if (errors.hasErrors()) {
            prepareNewForm(model);
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
        prepareNewForm(model);
        model.addAttribute("councilDesignation", councilDesignation);
        return COUNCILDESIGNATION_VIEW;
    }

    @RequestMapping(value = "/result/{id}", method = RequestMethod.GET)
    public String result(@PathVariable("id") final Long id, Model model) {
        CouncilDesignation councilDesignation = councilDesignationService.findOne(id);
        model.addAttribute("councilDesignation", councilDesignation);
        return COUNCILDESIGNATION_RESULT;
    }

    @RequestMapping(value = "/search/{mode}", method = RequestMethod.GET)
    public String search(@PathVariable("mode") final String mode, Model model) {
        CouncilDesignation councilDesignation = new CouncilDesignation();
        prepareNewForm(model);
        model.addAttribute("councilDesignation", councilDesignation);
        return COUNCILDESIGNATION_SEARCH;

    }

    @RequestMapping(value = "/ajaxsearch/{mode}", method = RequestMethod.POST, produces = MediaType.TEXT_PLAIN_VALUE)
    public @ResponseBody String ajaxsearch(@PathVariable("mode") final String mode, Model model,
            @ModelAttribute final CouncilDesignation councilDesignation) {
        List<CouncilDesignation> searchResultList = councilDesignationService.search(councilDesignation);
        String result = new StringBuilder("{ \"data\":").append(toSearchResultJson(searchResultList)).append("}")
                .toString();
        return result;
    }

    public Object toSearchResultJson(final Object object) {
        final GsonBuilder gsonBuilder = new GsonBuilder();
        final Gson gson = gsonBuilder
                .registerTypeAdapter(CouncilDesignation.class, new CouncilDesignationJsonAdaptor()).create();
        final String json = gson.toJson(object);
        return json;
    }
}