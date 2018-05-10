package org.egov.eventnotification.web.controller.notificationdraft;

import java.io.IOException;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Locale;

import javax.servlet.http.HttpServletRequest;

import org.apache.log4j.Logger;
import org.egov.eventnotification.constants.EventnotificationConstant;
import org.egov.eventnotification.entity.CategoryParameters;
import org.egov.eventnotification.entity.Event;
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
        model.addAttribute(EventnotificationConstant.EVENT, event);
        model.addAttribute(EventnotificationConstant.HOUR_LIST, eventnotificationUtil.getAllHour());
        model.addAttribute(EventnotificationConstant.MINUTE_LIST, eventnotificationUtil.getAllMinute());
        List eventList = new ArrayList<>(Arrays.asList(EventnotificationConstant.EVENT_TYPE.values()));
        model.addAttribute(EventnotificationConstant.EVENT_LIST, eventList);
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

}
