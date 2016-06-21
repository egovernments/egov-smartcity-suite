package org.egov.council.web.controller;

import java.util.List;

import javax.validation.Valid;

import org.apache.commons.lang3.RandomStringUtils;
import org.egov.council.entity.CouncilCaste;
import org.egov.council.service.CouncilCasteService;
import org.egov.council.web.adaptor.CouncilCasteJsonAdaptor;
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
@RequestMapping("/councilcaste")
public class CouncilCasteController {
    private final static String COUNCILCASTE_NEW = "councilcaste-new";
    private final static String COUNCILCASTE_RESULT = "councilcaste-result";
    private final static String COUNCILCASTE_EDIT = "councilcaste-edit";
    private final static String COUNCILCASTE_VIEW = "councilcaste-view";
    private final static String COUNCILCASTE_SEARCH = "councilcaste-search";
    @Autowired
    private CouncilCasteService councilCasteService;
    @Autowired
    private MessageSource messageSource;

    private void prepareNewForm(Model model) {
    }

    @RequestMapping(value = "/new", method = RequestMethod.GET)
    public String newForm(final Model model) {
        prepareNewForm(model);

        CouncilCaste councilCaste = new CouncilCaste();
        if (councilCaste != null && councilCaste.getCode() == null)
            councilCaste.setCode(RandomStringUtils.random(4, Boolean.TRUE, Boolean.TRUE).toUpperCase());

        model.addAttribute("councilCaste", councilCaste);
        return COUNCILCASTE_NEW;
    }

    @RequestMapping(value = "/create", method = RequestMethod.POST)
    public String create(@Valid @ModelAttribute final CouncilCaste councilCaste, final BindingResult errors,
            final Model model, final RedirectAttributes redirectAttrs) {
        if (errors.hasErrors()) {
            prepareNewForm(model);
            return COUNCILCASTE_NEW;
        }
        councilCasteService.create(councilCaste);
        redirectAttrs.addFlashAttribute("message", messageSource.getMessage("msg.councilCaste.success", null, null));
        return "redirect:/councilcaste/result/" + councilCaste.getId();
    }

    @RequestMapping(value = "/edit/{id}", method = RequestMethod.GET)
    public String edit(@PathVariable("id") final Long id, Model model) {
        CouncilCaste councilCaste = councilCasteService.findOne(id);
        prepareNewForm(model);
        model.addAttribute("councilCaste", councilCaste);
        return COUNCILCASTE_EDIT;
    }

    @RequestMapping(value = "/update", method = RequestMethod.POST)
    public String update(@Valid @ModelAttribute final CouncilCaste councilCaste, final BindingResult errors,
            final Model model, final RedirectAttributes redirectAttrs) {
      
        if (errors.hasErrors()) {
            prepareNewForm(model);
            return COUNCILCASTE_EDIT;
        }
          councilCasteService.update(councilCaste);
        redirectAttrs.addFlashAttribute("message", messageSource.getMessage("msg.councilCaste.success", null, null));
        return "redirect:/councilcaste/result/" + councilCaste.getId();
    }

    @RequestMapping(value = "/view/{id}", method = RequestMethod.GET)
    public String view(@PathVariable("id") final Long id, Model model) {
        CouncilCaste councilCaste = councilCasteService.findOne(id);
        prepareNewForm(model);
        model.addAttribute("councilCaste", councilCaste);
        return COUNCILCASTE_VIEW;
    }

    @RequestMapping(value = "/result/{id}", method = RequestMethod.GET)
    public String result(@PathVariable("id") final Long id, Model model) {
        CouncilCaste councilCaste = councilCasteService.findOne(id);
        model.addAttribute("councilCaste", councilCaste);
        return COUNCILCASTE_RESULT;
    }

    @RequestMapping(value = "/search/{mode}", method = RequestMethod.GET)
    public String search(@PathVariable("mode") final String mode, Model model) {
        CouncilCaste councilCaste = new CouncilCaste();
        prepareNewForm(model);
        model.addAttribute("councilCaste", councilCaste);
        return COUNCILCASTE_SEARCH;

    }

    @RequestMapping(value = "/ajaxsearch/{mode}", method = RequestMethod.POST, produces = MediaType.TEXT_PLAIN_VALUE)
    public @ResponseBody String ajaxsearch(@PathVariable("mode") final String mode, Model model,
            @ModelAttribute final CouncilCaste councilCaste) {
        List<CouncilCaste> searchResultList = councilCasteService.search(councilCaste);
        String result = new StringBuilder("{ \"data\":").append(toSearchResultJson(searchResultList)).append("}")
                .toString();
        return result;
    }

    public Object toSearchResultJson(final Object object) {
        final GsonBuilder gsonBuilder = new GsonBuilder();
        final Gson gson = gsonBuilder.registerTypeAdapter(CouncilCaste.class, new CouncilCasteJsonAdaptor()).create();
        final String json = gson.toJson(object);
        return json;
    }
}