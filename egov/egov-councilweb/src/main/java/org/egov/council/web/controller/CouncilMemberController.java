package org.egov.council.web.controller;

import java.io.IOException;
import java.util.List;

import javax.validation.Valid;

import org.apache.log4j.Logger;
import org.egov.council.entity.CouncilMember;
import org.egov.council.entity.CouncilMemberStatus;
import org.egov.council.service.CouncilCasteService;
import org.egov.council.service.CouncilDesignationService;
import org.egov.council.service.CouncilMemberService;
import org.egov.council.service.CouncilPartyService;
import org.egov.council.service.CouncilQualificationService;
import org.egov.council.web.adaptor.CouncilMemberJsonAdaptor;
import org.egov.infra.admin.master.service.BoundaryService;
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
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

@Controller
@RequestMapping("/councilmember")
public class CouncilMemberController {
    private final static String COUNCILMEMBER_NEW = "councilmember-new";
    private final static String COUNCILMEMBER_RESULT = "councilmember-result";
    private final static String COUNCILMEMBER_EDIT = "councilmember-edit";
    private final static String COUNCILMEMBER_VIEW = "councilmember-view";
    private final static String COUNCILMEMBER_SEARCH = "councilmember-search";
    @Autowired
    private CouncilMemberService councilMemberService;
    @Autowired
    private MessageSource messageSource;
    @Autowired
    private BoundaryService boundaryService;
    @Autowired
    private CouncilDesignationService councilDesignationService;
    @Autowired
    private CouncilQualificationService councilQualificationService;
    @Autowired
    private CouncilCasteService councilCasteService;
    @Autowired
    private CouncilPartyService councilPartyService;
    private static final Logger LOGGER = Logger.getLogger(CouncilMemberController.class);


    private void prepareNewForm(Model model) {
        model.addAttribute("boundarys",
                boundaryService.getActiveBoundariesByBndryTypeNameAndHierarchyTypeName(
                        "Ward","ADMINISTRATION"));//GET ELECTION WARD.
        model.addAttribute("councilDesignations", councilDesignationService.findAll());
        model.addAttribute("councilQualifications", councilQualificationService.findAll());
        model.addAttribute("councilCastes", councilCasteService.findAll());
        model.addAttribute("councilPartys", councilPartyService.findAll());
       // model.addAttribute("genders", genderService.findAll());
       // model.addAttribute("addresss", addressService.findAll());
    }

    @RequestMapping(value = "/new", method = RequestMethod.GET)
    public String newForm(final Model model) {
        prepareNewForm(model);
        model.addAttribute("councilMember", new CouncilMember());
        return COUNCILMEMBER_NEW;
    }

    @RequestMapping(value = "/create", method = RequestMethod.POST)
    public String create(@Valid @ModelAttribute final CouncilMember councilMember, @RequestParam final MultipartFile attachments, final BindingResult errors,
            final Model model, final RedirectAttributes redirectAttrs)  {
        if (errors.hasErrors()) {
            prepareNewForm(model);
            return COUNCILMEMBER_NEW;
        }
        if(councilMember!=null && councilMember.getStatus()==null)
        councilMember.setStatus(CouncilMemberStatus.ACTIVE);
        
        if(attachments!=null){
            try {
                councilMember.setPhoto(attachments.getBytes());
            } catch (IOException e) {
                LOGGER.error("Error in loading Employee photo" + e.getMessage(), e);
            }
        }
        councilMemberService.create(councilMember);
        redirectAttrs.addFlashAttribute("message", messageSource.getMessage("msg.councilMember.success", null, null));
        return "redirect:/councilmember/result/" + councilMember.getId();
    }

    @RequestMapping(value = "/edit/{id}", method = RequestMethod.GET)
    public String edit(@PathVariable("id") final Long id, Model model) {
        CouncilMember councilMember = councilMemberService.findOne(id);
        prepareNewForm(model);
        model.addAttribute("councilMember", councilMember);
        return COUNCILMEMBER_EDIT;
    }

    @RequestMapping(value = "/update", method = RequestMethod.POST)
    public String update(@Valid @ModelAttribute final CouncilMember councilMember, final BindingResult errors,
            final Model model, final RedirectAttributes redirectAttrs) {
        if (errors.hasErrors()) {
            prepareNewForm(model);
            return COUNCILMEMBER_EDIT;
        }
        councilMemberService.update(councilMember);
        redirectAttrs.addFlashAttribute("message", messageSource.getMessage("msg.councilMember.success", null, null));
        return "redirect:/councilmember/result/" + councilMember.getId();
    }

    @RequestMapping(value = "/view/{id}", method = RequestMethod.GET)
    public String view(@PathVariable("id") final Long id, Model model) {
        CouncilMember councilMember = councilMemberService.findOne(id);
        prepareNewForm(model);
        model.addAttribute("councilMember", councilMember);
        return COUNCILMEMBER_VIEW;
    }

    @RequestMapping(value = "/result/{id}", method = RequestMethod.GET)
    public String result(@PathVariable("id") final Long id, Model model) {
        CouncilMember councilMember = councilMemberService.findOne(id);
        model.addAttribute("councilMember", councilMember);
        return COUNCILMEMBER_RESULT;
    }

    @RequestMapping(value = "/search/{mode}", method = RequestMethod.GET)
    public String search(@PathVariable("mode") final String mode, Model model) {
        CouncilMember councilMember = new CouncilMember();
        prepareNewForm(model);
        model.addAttribute("councilMember", councilMember);
        return COUNCILMEMBER_SEARCH;

    }

    @RequestMapping(value = "/ajaxsearch/{mode}", method = RequestMethod.POST, produces = MediaType.TEXT_PLAIN_VALUE)
    public @ResponseBody String ajaxsearch(@PathVariable("mode") final String mode, Model model,
            @ModelAttribute final CouncilMember councilMember) {
        List<CouncilMember> searchResultList = councilMemberService.search(councilMember);
        String result = new StringBuilder("{ \"data\":").append(toSearchResultJson(searchResultList)).append("}")
                .toString();
        return result;
    }

    public Object toSearchResultJson(final Object object) {
        final GsonBuilder gsonBuilder = new GsonBuilder();
        final Gson gson = gsonBuilder.registerTypeAdapter(CouncilMember.class, new CouncilMemberJsonAdaptor()).create();
        final String json = gson.toJson(object);
        return json;
    }
}