package org.egov.eventnotification.web.controller.notificationdraft;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Locale;

import org.apache.log4j.Logger;
import org.egov.eventnotification.constants.Constants;
import org.egov.eventnotification.entity.DraftType;
import org.egov.eventnotification.entity.Event;
import org.egov.eventnotification.entity.NotificationDrafts;
import org.egov.eventnotification.service.DraftService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
@RequestMapping(value = Constants.NOTIFICATION_DRAFTS_VIEW)
public class NotificationDraftController {

    private static final Logger LOGGER = Logger.getLogger(NotificationDraftController.class);

    @Autowired
    private DraftService draftService;

    @Autowired
    private MessageSource messageSource;

    @GetMapping(Constants.API_VIEW)
    public String view(final Model model) {
        LOGGER.info("Request for View Drafts Page");
        model.addAttribute(Constants.NOTIFICATION_DRAFT_LIST, draftService.getAllNotificationDrafts());
        model.addAttribute(Constants.DRAFT_LIST, new ArrayList<>(Arrays.asList(DraftType.values())));
        return Constants.VIEW_DRAFTS_VIEW;
    }

    @GetMapping(Constants.API_VIEW_ID)
    public String viewByDraft(@PathVariable(Constants.DRAFT_ID) Long id, final Model model) {
        model.addAttribute(Constants.NOTIFICATION_DRAFT, draftService.findDraftById(id));
        model.addAttribute(Constants.MODE, Constants.MODE_VIEW);

        return Constants.VIEW_DRAFTVIEWRESULT;
    }

    @GetMapping(Constants.CATEGORY_FOR_MODULE)
    public String getCategoriesForModule(@ModelAttribute Long moduleId, final Model model) {
        model.addAttribute(Constants.MODULE_CATEGORY, draftService.getCategoriesForModule(moduleId));
        return model.toString();
    }

    @GetMapping(Constants.API_CREATE)
    public String create(@ModelAttribute NotificationDrafts draft, @ModelAttribute Event event, Model model) {
        model.addAttribute(Constants.DRAFT_LIST, new ArrayList<>(Arrays.asList(DraftType.values())));
        model.addAttribute(Constants.NOTIFICATION_DRAFT, draft);
        model.addAttribute(Constants.TEMPLATE_MODULE, draftService.getAllModules());
        model.addAttribute(Constants.MODULE_CATEGORY, draftService.getAllCategories());
        model.addAttribute(Constants.CATEGORY_PARAMETERS, draftService.getAllCategories());
        model.addAttribute(Constants.MODE, Constants.MODE_CREATE);
        return Constants.VIEW_DRAFTS_CREATE;
    }

    @PostMapping(Constants.API_CREATE)
    public String create(@ModelAttribute(Constants.DRAFT) NotificationDrafts notificationDraft,
            Model model,
            RedirectAttributes redirectAttrs, BindingResult errors) {
        if (errors.hasErrors()) {
            model.addAttribute(Constants.MESSAGE,
                    messageSource.getMessage(Constants.MSG_DRAFT_CREATE_ERROR, null, Locale.ENGLISH));
            model.addAttribute(Constants.MODE, Constants.MODE_CREATE);
            return Constants.VIEW_EVENTCREATE;
        }
        draftService.save(notificationDraft);
        redirectAttrs.addFlashAttribute(Constants.NOTIFICATION_DRAFT, notificationDraft);
        model.addAttribute(Constants.MESSAGE,
                messageSource.getMessage(Constants.MSG_DRAFT_CREATE_SUCCESS, null, Locale.ENGLISH));
        model.addAttribute(Constants.MODE, Constants.MODE_VIEW);
        return Constants.VIEW_DRAFTS_CREATE_SUCCESS;
    }
}
