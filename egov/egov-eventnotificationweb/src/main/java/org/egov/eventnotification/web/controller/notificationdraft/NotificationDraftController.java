package org.egov.eventnotification.web.controller.notificationdraft;

import static org.egov.eventnotification.constants.Constants.API_CREATE;
import static org.egov.eventnotification.constants.Constants.API_VIEW;
import static org.egov.eventnotification.constants.Constants.API_VIEW_ID;
import static org.egov.eventnotification.constants.Constants.CATEGORY_FOR_MODULE;
import static org.egov.eventnotification.constants.Constants.CATEGORY_PARAMETERS;
import static org.egov.eventnotification.constants.Constants.DRAFT_LIST;
import static org.egov.eventnotification.constants.Constants.MESSAGE;
import static org.egov.eventnotification.constants.Constants.MODE;
import static org.egov.eventnotification.constants.Constants.MODE_CREATE;
import static org.egov.eventnotification.constants.Constants.MODE_VIEW;
import static org.egov.eventnotification.constants.Constants.MODULE_CATEGORY;
import static org.egov.eventnotification.constants.Constants.NOTIFICATION_DRAFT;
import static org.egov.eventnotification.constants.Constants.NOTIFICATION_DRAFTS_VIEW;
import static org.egov.eventnotification.constants.Constants.NOTIFICATION_DRAFT_LIST;
import static org.egov.eventnotification.constants.Constants.TEMPLATE_MODULE;
import static org.egov.eventnotification.constants.Constants.VIEW_DRAFTS_CREATE;
import static org.egov.eventnotification.constants.Constants.VIEW_DRAFTS_CREATE_SUCCESS;
import static org.egov.eventnotification.constants.Constants.VIEW_DRAFTS_VIEW;
import static org.egov.eventnotification.constants.Constants.VIEW_DRAFTVIEWRESULT;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Locale;

import javax.validation.Valid;

import org.egov.eventnotification.constants.Constants;
import org.egov.eventnotification.entity.DraftType;
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

@Controller
@RequestMapping(value = NOTIFICATION_DRAFTS_VIEW)
public class NotificationDraftController {

    @Autowired
    private DraftService draftService;

    @Autowired
    private MessageSource messageSource;

    @GetMapping(API_VIEW)
    public String view(final Model model) {
        model.addAttribute(NOTIFICATION_DRAFT_LIST, draftService.getAllNotificationDrafts());
        model.addAttribute(DRAFT_LIST, new ArrayList<>(Arrays.asList(DraftType.values())));
        return VIEW_DRAFTS_VIEW;
    }

    @GetMapping(API_VIEW_ID)
    public String viewByDraft(@PathVariable Long id, final Model model) {
        model.addAttribute(NOTIFICATION_DRAFT, draftService.findDraftById(id));
        model.addAttribute(MODE, MODE_VIEW);

        return VIEW_DRAFTVIEWRESULT;
    }

    @GetMapping(CATEGORY_FOR_MODULE)
    public String getCategoriesForModule(@ModelAttribute Long moduleId, final Model model) {
        model.addAttribute(MODULE_CATEGORY, draftService.getCategoriesForModule(moduleId));
        return model.toString();
    }

    @GetMapping(API_CREATE)
    public String create(@ModelAttribute NotificationDrafts notificationDraft, Model model) {
        model.addAttribute(DRAFT_LIST, new ArrayList<>(Arrays.asList(DraftType.values())));
        model.addAttribute(NOTIFICATION_DRAFT, notificationDraft);
        model.addAttribute(TEMPLATE_MODULE, draftService.getAllModules());
        model.addAttribute(MODULE_CATEGORY, draftService.getAllCategories());
        model.addAttribute(CATEGORY_PARAMETERS, draftService.getAllCategories());
        model.addAttribute(MODE, MODE_CREATE);
        return VIEW_DRAFTS_CREATE;
    }

    @PostMapping(API_CREATE)
    public String create(@Valid @ModelAttribute NotificationDrafts notificationDraft,
            BindingResult errors, Model model) {
        if (errors.hasErrors()) {
            model.addAttribute(MESSAGE,
                    messageSource.getMessage("msg.draft.create.error", null, Locale.ENGLISH));
            model.addAttribute(MODE, MODE_CREATE);
            return Constants.VIEW_EVENTCREATE;
        }
        draftService.saveDraft(notificationDraft);
        model.addAttribute(NOTIFICATION_DRAFT, notificationDraft);
        model.addAttribute(MESSAGE,
                messageSource.getMessage("msg.draft.create.success", null, Locale.ENGLISH));
        model.addAttribute(MODE, MODE_VIEW);
        return VIEW_DRAFTS_CREATE_SUCCESS;
    }
}
