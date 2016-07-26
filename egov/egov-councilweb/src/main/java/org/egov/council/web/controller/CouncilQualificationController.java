package org.egov.council.web.controller;

import java.util.List;

import javax.validation.Valid;

import org.apache.commons.lang3.RandomStringUtils;
import org.egov.council.entity.CouncilQualification;
import org.egov.council.service.CouncilQualificationService;
import org.egov.council.web.adaptor.CouncilQualificationJsonAdaptor;
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
@RequestMapping("/councilqualification")
public class CouncilQualificationController {
    private final static String COUNCILQUALIFICATION_NEW = "councilqualification-new";
    private final static String COUNCILQUALIFICATION_RESULT = "councilqualification-result";
    private final static String COUNCILQUALIFICATION_EDIT = "councilqualification-edit";
    private final static String COUNCILQUALIFICATION_VIEW = "councilqualification-view";
    private final static String COUNCILQUALIFICATION_SEARCH = "councilqualification-search";
    @Autowired
    private CouncilQualificationService councilQualificationService;
    @Autowired
    private MessageSource messageSource;

    private void prepareNewForm(Model model) {
    }

    @RequestMapping(value = "/new", method = RequestMethod.GET)
    public String newForm(final Model model) {
        prepareNewForm(model);

        CouncilQualification councilQualification = new CouncilQualification();
        if (councilQualification != null && councilQualification.getCode() == null)
            councilQualification.setCode(RandomStringUtils.random(4, Boolean.TRUE, Boolean.TRUE).toUpperCase());
        model.addAttribute("councilQualification", councilQualification);
        return COUNCILQUALIFICATION_NEW;
    }

    @RequestMapping(value = "/create", method = RequestMethod.POST)
    public String create(@Valid @ModelAttribute final CouncilQualification councilQualification,
            final BindingResult errors, final Model model, final RedirectAttributes redirectAttrs) {
        if(councilQualification!=null && councilQualification.getCode()==null)
            councilQualification.setCode(RandomStringUtils.random(4, Boolean.TRUE, Boolean.TRUE).toUpperCase());
      
     
        if (errors.hasErrors()) {
            prepareNewForm(model);
            return COUNCILQUALIFICATION_NEW;
        }
          councilQualificationService.create(councilQualification);
        redirectAttrs.addFlashAttribute("message",
                messageSource.getMessage("msg.councilQualification.success", null, null));
        return "redirect:/councilqualification/result/" + councilQualification.getId();
    }

    @RequestMapping(value = "/edit/{id}", method = RequestMethod.GET)
    public String edit(@PathVariable("id") final Long id, Model model) {
        CouncilQualification councilQualification = councilQualificationService.findOne(id);
        prepareNewForm(model);
        model.addAttribute("councilQualification", councilQualification);
        return COUNCILQUALIFICATION_EDIT;
    }

    @RequestMapping(value = "/update", method = RequestMethod.POST)
    public String update(@Valid @ModelAttribute final CouncilQualification councilQualification,
            final BindingResult errors, final Model model, final RedirectAttributes redirectAttrs) {
        if (errors.hasErrors()) {
            prepareNewForm(model);
            return COUNCILQUALIFICATION_EDIT;
        }
        councilQualificationService.update(councilQualification);
        redirectAttrs.addFlashAttribute("message",
                messageSource.getMessage("msg.councilQualification.success", null, null));
        return "redirect:/councilqualification/result/" + councilQualification.getId();
    }

    @RequestMapping(value = "/view/{id}", method = RequestMethod.GET)
    public String view(@PathVariable("id") final Long id, Model model) {
        CouncilQualification councilQualification = councilQualificationService.findOne(id);
        prepareNewForm(model);
        model.addAttribute("councilQualification", councilQualification);
        return COUNCILQUALIFICATION_VIEW;
    }

    @RequestMapping(value = "/result/{id}", method = RequestMethod.GET)
    public String result(@PathVariable("id") final Long id, Model model) {
        CouncilQualification councilQualification = councilQualificationService.findOne(id);
        model.addAttribute("councilQualification", councilQualification);
        return COUNCILQUALIFICATION_RESULT;
    }

    @RequestMapping(value = "/search/{mode}", method = RequestMethod.GET)
    public String search(@PathVariable("mode") final String mode, Model model) {
        CouncilQualification councilQualification = new CouncilQualification();
        prepareNewForm(model);
        model.addAttribute("councilQualification", councilQualification);
        return COUNCILQUALIFICATION_SEARCH;

    }

    @RequestMapping(value = "/ajaxsearch/{mode}", method = RequestMethod.POST, produces = MediaType.TEXT_PLAIN_VALUE)
    public @ResponseBody String ajaxsearch(@PathVariable("mode") final String mode, Model model,
            @ModelAttribute final CouncilQualification councilQualification) {
        List<CouncilQualification> searchResultList = councilQualificationService.search(councilQualification);
        String result = new StringBuilder("{ \"data\":").append(toSearchResultJson(searchResultList)).append("}")
                .toString();
        return result;
    }

    public Object toSearchResultJson(final Object object) {
        final GsonBuilder gsonBuilder = new GsonBuilder();
        final Gson gson = gsonBuilder.registerTypeAdapter(CouncilQualification.class,
                new CouncilQualificationJsonAdaptor()).create();
        final String json = gson.toJson(object);
        return json;
    }
}