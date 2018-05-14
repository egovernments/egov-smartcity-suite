package org.egov.eventnotification.web.controller.notificationdraft;

import java.io.IOException;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;
import java.util.Locale;

import javax.servlet.http.HttpServletRequest;

import org.apache.log4j.Logger;
import org.egov.eventnotification.constants.EventnotificationConstant;
import org.egov.eventnotification.entity.CategoryParameters;
import org.egov.eventnotification.entity.Event;
import org.egov.eventnotification.entity.EventDetails;
import org.egov.eventnotification.entity.ModuleCategory;
import org.egov.eventnotification.entity.NotificationDrafts;
import org.egov.eventnotification.entity.TemplateModule;
import org.egov.eventnotification.service.DraftService;
import org.egov.eventnotification.utils.EventnotificationUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
@RequestMapping(value = EventnotificationConstant.NOTIFICATION_DRAFTS_VIEW)
public class NotificationDraftController {
	
	private static final Logger LOGGER = Logger.getLogger(NotificationDraftController.class);
    
    @Autowired
    private DraftService draftService;
    
    @Autowired
    private EventnotificationUtil eventnotificationUtil; 

    @Autowired
    private MessageSource messageSource;

    
    @RequestMapping(value = { EventnotificationConstant.API_VIEW }, method = RequestMethod.GET)
    public String view(final Model model) {
    	LOGGER.info("Request for View Drafts Page");
        List<NotificationDrafts> draftsList = draftService.getAllNotificationDrafts();
        model.addAttribute(EventnotificationConstant.NOTIFICATION_DRAFT_LIST, draftsList);
        List draftList = new ArrayList<>(Arrays.asList(EventnotificationConstant.DRAFT_TYPE.values()));
        model.addAttribute(EventnotificationConstant.DRAFT_LIST, draftList);
        return EventnotificationConstant.VIEW_DRAFTS_VIEW;
    }
    
    @RequestMapping(value = { EventnotificationConstant.API_VIEW_ID }, method = RequestMethod.GET)
    public String viewById(final Model model, @PathVariable(EventnotificationConstant.DRAFT_ID) Long id) {
        NotificationDrafts draft = draftService.findDraftById(id);

        model.addAttribute(EventnotificationConstant.NOTIFICATION_DRAFT, draft);
        model.addAttribute(EventnotificationConstant.MODE, EventnotificationConstant.MODE_VIEW);
    	
        return EventnotificationConstant.VIEW_DRAFTVIEWRESULT;
    }
    
    @RequestMapping(value = { EventnotificationConstant.CATEGORY_FOR_MODULE }, method = RequestMethod.GET)
    public String getCategoriesForModule(final Model model, @ModelAttribute Long moduleId) {
        List<ModuleCategory> categoryList = draftService.getCategoriesForModule(moduleId);
        model.addAttribute(EventnotificationConstant.MODULE_CATEGORY, categoryList);
        return model.toString();
    }
    
    @RequestMapping(value = EventnotificationConstant.API_CREATE, method = RequestMethod.GET)
    public String newDraft(@ModelAttribute NotificationDrafts draft, @ModelAttribute Event event, Model model) {
    	LOGGER.info("Request for Create Drafts Page");
    	List<TemplateModule> templateModuleList = draftService.getAllModules(); 
        List<ModuleCategory> moduleCategoryList = draftService.getAllCategories(); 
        List<CategoryParameters> categoryParametersList = draftService.getAllCategoryParameters();
        List draftList = new ArrayList<>(Arrays.asList(EventnotificationConstant.DRAFT_TYPE.values()));
        LOGGER.info("Obtained Module List, Category List and Parameter List with Draft Types");
        model.addAttribute(EventnotificationConstant.DRAFT_LIST, draftList);
        model.addAttribute(EventnotificationConstant.NOTIFICATION_DRAFT, draft); 
        model.addAttribute(EventnotificationConstant.TEMPLATE_MODULE, templateModuleList);
        model.addAttribute(EventnotificationConstant.MODULE_CATEGORY, moduleCategoryList);
        model.addAttribute(EventnotificationConstant.CATEGORY_PARAMETERS, categoryParametersList);
        model.addAttribute(EventnotificationConstant.MODE, EventnotificationConstant.MODE_CREATE);
        return EventnotificationConstant.VIEW_DRAFTS_CREATE;
    }
    
    @RequestMapping(value = EventnotificationConstant.API_CREATE, method = RequestMethod.POST)
    public String create(@ModelAttribute(EventnotificationConstant.DRAFT) NotificationDrafts notificationDraft,
            Model model,
            RedirectAttributes redirectAttrs, HttpServletRequest request, BindingResult errors)
            throws IOException, ParseException {
    	LOGGER.info("Persist Action has been initiated");
        if (errors.hasErrors()) {
            model.addAttribute(EventnotificationConstant.MODE, EventnotificationConstant.MODE_CREATE);
            return EventnotificationConstant.VIEW_EVENTCREATE;
        }
        draftService.persist(notificationDraft);
        LOGGER.info("Draft has been persisted successfully");
        redirectAttrs.addFlashAttribute(EventnotificationConstant.NOTIFICATION_DRAFT, notificationDraft);
        model.addAttribute(EventnotificationConstant.MESSAGE,
                messageSource.getMessage(EventnotificationConstant.MSG_DRAFT_CREATE_SUCCESS, null, Locale.ENGLISH));
        model.addAttribute(EventnotificationConstant.MODE, EventnotificationConstant.MODE_VIEW);
        return EventnotificationConstant.VIEW_DRAFTS_CREATE_SUCCESS;
    }
    
    @RequestMapping(value = EventnotificationConstant.API_UPDATE_ID, method = RequestMethod.GET)
    public String viewUpdate(@ModelAttribute NotificationDrafts updateDraft, Model model,
            @PathVariable(EventnotificationConstant.DRAFT_ID) Long id) {
    	updateDraft = draftService.findDraftById(id);
    	model.addAttribute(EventnotificationConstant.NOTIFICATION_DRAFT, updateDraft);
    	List<TemplateModule> templateModuleList = draftService.getAllModules();
    	List<ModuleCategory> moduleCategoryList = new ArrayList<>();
    	List<CategoryParameters> categoryParametersList = new ArrayList<>(); 
    	if(null != updateDraft.getModule()) { 
    		moduleCategoryList = draftService.getCategoriesForModule(updateDraft.getModule().getId());
    	}
    	if(null != updateDraft.getCategory()) { 
    		categoryParametersList = draftService.getParametersForCategory(updateDraft.getCategory().getId());
    	}
    	List draftList = new ArrayList<>(Arrays.asList(EventnotificationConstant.DRAFT_TYPE.values()));
        LOGGER.info("Obtained Module List, Category List and Parameter List with Draft Types");
        model.addAttribute(EventnotificationConstant.DRAFT_LIST, draftList);
        model.addAttribute(EventnotificationConstant.TEMPLATE_MODULE, templateModuleList);
        model.addAttribute(EventnotificationConstant.MODULE_CATEGORY, moduleCategoryList);
        model.addAttribute(EventnotificationConstant.CATEGORY_PARAMETERS, categoryParametersList);
        
        model.addAttribute(EventnotificationConstant.MODE, EventnotificationConstant.MODE_VIEW);
    	
        return EventnotificationConstant.VIEW_DRAFTUPDATE;
    }
    
    @RequestMapping(value = EventnotificationConstant.API_UPDATE_ID, method = RequestMethod.POST)
    public String update(@ModelAttribute(EventnotificationConstant.NOTIFICATION_DRAFT) NotificationDrafts draft,
            Model model,
            RedirectAttributes redirectAttrs, HttpServletRequest request, BindingResult errors,
            @PathVariable(EventnotificationConstant.DRAFT_ID) Long id)
            throws IOException, ParseException {
    	
    	LOGGER.info("Update Method initiated" );
    	draft.setId(id);
    	draftService.updateDraftById(id, draft);
    	LOGGER.info("Draft has been updated");
        redirectAttrs.addFlashAttribute(EventnotificationConstant.NOTIFICATION_DRAFT, draft);
        model.addAttribute(EventnotificationConstant.MESSAGE,
                messageSource.getMessage(EventnotificationConstant.MSG_DRAFT_UPDATE_SUCCESS, null, Locale.ENGLISH));
        model.addAttribute(EventnotificationConstant.MODE, EventnotificationConstant.MODE_VIEW);
        return EventnotificationConstant.VIEW_DRAFTVIEWRESULT;
    }

}
