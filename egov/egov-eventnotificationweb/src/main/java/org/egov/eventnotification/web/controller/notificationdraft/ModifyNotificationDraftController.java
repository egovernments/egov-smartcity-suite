package org.egov.eventnotification.web.controller.notificationdraft;

import static org.egov.eventnotification.constants.Constants.CATEGORY_PARAMETERS;
import static org.egov.eventnotification.constants.Constants.DRAFT_LIST;
import static org.egov.eventnotification.constants.Constants.MESSAGE;
import static org.egov.eventnotification.constants.Constants.MODE;
import static org.egov.eventnotification.constants.Constants.MODE_CREATE;
import static org.egov.eventnotification.constants.Constants.MODE_VIEW;
import static org.egov.eventnotification.constants.Constants.MODULE_CATEGORY;
import static org.egov.eventnotification.constants.Constants.MSG_DRAFT_UPDATE_ERROR;
import static org.egov.eventnotification.constants.Constants.MSG_DRAFT_UPDATE_SUCCESS;
import static org.egov.eventnotification.constants.Constants.NOTIFICATION_DRAFT;
import static org.egov.eventnotification.constants.Constants.TEMPLATE_MODULE;
import static org.egov.eventnotification.constants.Constants.VIEW_DRAFTUPDATE;
import static org.egov.eventnotification.constants.Constants.VIEW_DRAFTVIEWRESULT;
import static org.egov.eventnotification.constants.Constants.VIEW_EVENTCREATE;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Locale;

import org.egov.eventnotification.entity.CategoryParameters;
import org.egov.eventnotification.entity.DraftType;
import org.egov.eventnotification.entity.ModuleCategory;
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
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
public class ModifyNotificationDraftController {
    @Autowired
    private DraftService draftService;

    @Autowired
    private MessageSource messageSource;

    @ModelAttribute("notificationDrafts")
    public NotificationDrafts getNotificationDrafts(@PathVariable("id") Long id) {
        return draftService.findDraftById(id);
    }

    @GetMapping("/drafts/update/{id}")
    public String update(@ModelAttribute NotificationDrafts notificationDrafts, Model model) {
        List<ModuleCategory> moduleCategoryList = new ArrayList<>();
        List<CategoryParameters> categoryParametersList = new ArrayList<>();
        if (null != notificationDrafts.getModule())
            moduleCategoryList = draftService.getCategoriesForModule(notificationDrafts.getModule().getId());
        if (null != notificationDrafts.getCategory())
            categoryParametersList = draftService.getParametersForCategory(notificationDrafts.getCategory().getId());
        model.addAttribute(DRAFT_LIST, new ArrayList<>(Arrays.asList(DraftType.values())));
        model.addAttribute(TEMPLATE_MODULE, draftService.getAllModules());
        model.addAttribute(MODULE_CATEGORY, moduleCategoryList);
        model.addAttribute(CATEGORY_PARAMETERS, categoryParametersList);

        model.addAttribute(MODE, MODE_VIEW);

        return VIEW_DRAFTUPDATE;
    }

    @PostMapping("/drafts/update/{id}")
    public String update(@PathVariable("id") Long id, @ModelAttribute NotificationDrafts notificationDrafts,
            RedirectAttributes redirectAttrs, BindingResult errors, Model model) {
        if (errors.hasErrors()) {
            model.addAttribute(MESSAGE,
                    messageSource.getMessage(MSG_DRAFT_UPDATE_ERROR, null, Locale.ENGLISH));
            model.addAttribute(MODE, MODE_CREATE);
            return VIEW_EVENTCREATE;
        }
        notificationDrafts.setId(id);
        draftService.updateDraft(notificationDrafts);
        redirectAttrs.addFlashAttribute(NOTIFICATION_DRAFT, notificationDrafts);
        model.addAttribute(MESSAGE,
                messageSource.getMessage(MSG_DRAFT_UPDATE_SUCCESS, null, Locale.ENGLISH));
        model.addAttribute(MODE, MODE_VIEW);
        return VIEW_DRAFTVIEWRESULT;
    }
}
