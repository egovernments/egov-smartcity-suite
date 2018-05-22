package org.egov.eventnotification.web.controller.notificationdraft;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Locale;

import org.egov.eventnotification.constants.Constants;
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
        model.addAttribute(Constants.DRAFT_LIST, new ArrayList<>(Arrays.asList(DraftType.values())));
        model.addAttribute(Constants.TEMPLATE_MODULE, draftService.getAllModules());
        model.addAttribute(Constants.MODULE_CATEGORY, moduleCategoryList);
        model.addAttribute(Constants.CATEGORY_PARAMETERS, categoryParametersList);

        model.addAttribute(Constants.MODE, Constants.MODE_VIEW);

        return Constants.VIEW_DRAFTUPDATE;
    }

    @PostMapping("/drafts/update/{id}")
    public String update(@ModelAttribute NotificationDrafts notificationDrafts,
            @PathVariable("id") Long id,
            RedirectAttributes redirectAttrs, BindingResult errors, Model model) {
        if (errors.hasErrors()) {
            model.addAttribute(Constants.MESSAGE,
                    messageSource.getMessage(Constants.MSG_DRAFT_UPDATE_ERROR, null, Locale.ENGLISH));
            model.addAttribute(Constants.MODE, Constants.MODE_CREATE);
            return Constants.VIEW_EVENTCREATE;
        }
        notificationDrafts.setId(id);
        draftService.updateDraft(notificationDrafts);
        redirectAttrs.addFlashAttribute(Constants.NOTIFICATION_DRAFT, notificationDrafts);
        model.addAttribute(Constants.MESSAGE,
                messageSource.getMessage(Constants.MSG_DRAFT_UPDATE_SUCCESS, null, Locale.ENGLISH));
        model.addAttribute(Constants.MODE, Constants.MODE_VIEW);
        return Constants.VIEW_DRAFTVIEWRESULT;
    }
}
