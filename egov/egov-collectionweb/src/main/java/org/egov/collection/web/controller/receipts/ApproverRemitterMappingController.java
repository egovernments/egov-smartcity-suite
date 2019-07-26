package org.egov.collection.web.controller.receipts;

import org.apache.log4j.Logger;
import org.egov.collection.constants.CollectionConstants;
import org.egov.collection.entity.ApproverRemitterMapping;
import org.egov.collection.service.ApproverRemitterMappingService;
import org.egov.collection.utils.CollectionsUtil;
import org.egov.infra.admin.master.entity.User;
import org.egov.infra.admin.master.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.view.RedirectView;

import javax.validation.Valid;
import java.util.Collections;
import java.util.Set;

@RestController
@RequestMapping(path = "/receipts/approverRemitterMapping")
public class ApproverRemitterMappingController {

    public static final String VIEW_APPROVER_REMITTER_MAPPING_ERROR = "approverRemitterMapping-error";
    public static final String ATTR_SUCCESS_MSG = "successMsg";
    public static final String VIEW_APPROVER_REMITTER_MAPPING_UPDATE = "approverRemitterMapping-update";
    private static final String ATTR_APPROVER_REMITTER_MAPPING_SPEC = "approverRemitterMappingSpec";
    private static final String ATTR_APPROVER_REMITTER_MAPPING_LIST = "approverRemitterMappingList";
    private static final String VIEW_INDEX = "approverRemitterMapping-view";
    private static final Logger LOGGER = Logger.getLogger(ApproverRemitterMappingController.class);
    @Autowired
    UserService userService;

    @Autowired
    ApproverRemitterMappingService mappingService;

    @Autowired
    CollectionsUtil collectionsUtil;

    private void prepareSearchModel(final Model model) {
        prepareSearchModel(model, new ApproverRemitterMappingService.ApproverRemitterMappingSpec());
    }

    private void prepareSearchModel(final Model model, ApproverRemitterMappingService.ApproverRemitterMappingSpec givenSpec) {
        String remitterRole = collectionsUtil.getAppConfigValue(
                CollectionConstants.MODULE_NAME_COLLECTIONS_CONFIG,
                CollectionConstants.APPCONFIG_VALUE_COLLECTION_REMITTER_ROLE,
                CollectionConstants.ROLE_COLLECTION_REMITTER);

        Set<User> approverSet = userService.getUsersByRoleName(CollectionConstants.ROLE_COLLECTION_APPROVER);
        Set<User> activelyMappedApprvSet = mappingService.getActivelyMappedApprover();

        model.addAttribute(ATTR_APPROVER_REMITTER_MAPPING_SPEC, givenSpec);
        approverSet.removeAll(activelyMappedApprvSet);
        model.addAttribute("freeApproverList", approverSet);
        model.addAttribute("activeMappedApproverList", activelyMappedApprvSet);

        // model.addAttribute("approverList", approverSet);
        model.addAttribute("remitterList", userService.getUsersByRoleName(remitterRole));
        model.addAttribute(ATTR_APPROVER_REMITTER_MAPPING_LIST, Collections.emptyList());
    }

    private void prepareModelAndViewForEdit(Long mappingId, Model model) {
        ApproverRemitterMapping mapping = mappingService.findById(mappingId);
        if (mapping != null) {
            ApproverRemitterMappingService.ApproverRemitterMappingSpec remitterMappingSpec = ApproverRemitterMappingService.ApproverRemitterMappingSpec
                    .of(mapping);
            prepareSearchModel(model, remitterMappingSpec);
            ((Set<User>) model.asMap().get("activeMappedApproverList")).remove(mapping.getApprover());
        } else {
            model.addAttribute("errors", Collections.singletonList("Mapping Not Found for id " + mappingId));
        }
        model.addAttribute("mode", "MODIFY");
    }

    @GetMapping("/view")
    public ModelAndView getViewMapping(final Model model) {
        prepareSearchModel(model);
        return new ModelAndView(VIEW_INDEX, model.asMap());
    }

    @PostMapping("/view")
    public ModelAndView postViewMapping(
            @ModelAttribute(ATTR_APPROVER_REMITTER_MAPPING_SPEC) ApproverRemitterMappingService.ApproverRemitterMappingSpec searchSpec,
            BindingResult bindingResult, Model model) {
        prepareSearchModel(model, searchSpec);

        if (!bindingResult.hasErrors()) {
            model.addAttribute(ATTR_APPROVER_REMITTER_MAPPING_LIST, mappingService.searchMappingBySpec(searchSpec));
        }

        // return "approverRemitterMapping-view";
        return new ModelAndView(VIEW_INDEX, model.asMap());
    }

    @GetMapping("/modify")
    public ModelAndView getModifyMapping(Model model) {
        prepareSearchModel(model);
        model.addAttribute("mode", "MODIFY");
        model.addAttribute("modifyRequest", new ModifyRequestSpec());

        return new ModelAndView(VIEW_INDEX, model.asMap());
    }

    @PostMapping("/modify")
    public ModelAndView postModifyMapping(
            @ModelAttribute(ATTR_APPROVER_REMITTER_MAPPING_SPEC) ApproverRemitterMappingService.ApproverRemitterMappingSpec searchSpec,
            BindingResult bindingResult, Model model) {
        this.postViewMapping(searchSpec, bindingResult, model);
        model.addAttribute("mode", "MODIFY");
        model.addAttribute("modifyRequest", new ModifyRequestSpec());
        return new ModelAndView(VIEW_INDEX, model.asMap());
    }

    @GetMapping("/edit")
    public ModelAndView getEditMapping() {
        return new ModelAndView(new RedirectView("/collection/receipts/approverRemitterMapping/view"));
    }

    @PostMapping("/edit")
    public ModelAndView postEditMappingIndex(@ModelAttribute("modifyRequest") ModifyRequestSpec modifyRequest,
            BindingResult bindingResult) {
        if (modifyRequest.selectedId != null || modifyRequest.selectedId > -1) {
            return new ModelAndView(
                    new RedirectView("/collection/receipts/approverRemitterMapping/edit/" + modifyRequest.selectedId));
        }
        return new ModelAndView(new RedirectView("/collection/receipts/approverRemitterMapping/view"));
    }

    @GetMapping("/edit/{id}")
    public ModelAndView getEditMapping(@PathVariable("id") Long mappingId, Model model) {
        prepareModelAndViewForEdit(mappingId, model);
        ModelAndView mav = new ModelAndView(VIEW_APPROVER_REMITTER_MAPPING_UPDATE, model.asMap());
        return mav;
    }

    @PostMapping("/edit/{id}")
    public ModelAndView postEditMapping(
            @PathVariable("id") Long mappingId,
            @Valid @ModelAttribute(ATTR_APPROVER_REMITTER_MAPPING_SPEC) ApproverRemitterMappingService.ApproverRemitterMappingSpec searchSpec,
            BindingResult bindingResult,
            Model model) {
        if (!bindingResult.hasErrors()) {
            // For some reason spring is not setting id to ModelAttribute even as id is getting passed in POST request.
            if (searchSpec.getId() == null) {
                searchSpec.setId(mappingId);
            }
            if (mappingService.validateAndUpdateMapping(searchSpec, bindingResult)) {
                model.addAttribute(ATTR_SUCCESS_MSG, "Successfully updated mapping.");
            }
        }
        prepareModelAndViewForEdit(mappingId, model);
        ModelAndView mav = new ModelAndView(VIEW_APPROVER_REMITTER_MAPPING_UPDATE);
        if (bindingResult.hasErrors()) {
            mav.addAllObjects(bindingResult.getModel());
        } else {
            mav.setViewName(VIEW_APPROVER_REMITTER_MAPPING_ERROR);
        }
        mav.addAllObjects(model.asMap());
        return mav;

    }

    @GetMapping("/create")
    public ModelAndView getCreateMapping(
            Model model) {
        model.addAttribute("mode", "CREATE");
        prepareSearchModel(model);
        ModelAndView mav = new ModelAndView("approverRemitterMapping-update", model.asMap());
        return mav;
    }

    @PostMapping("/create")
    public ModelAndView postCreateMapping(
            @Valid @ModelAttribute(ATTR_APPROVER_REMITTER_MAPPING_SPEC) ApproverRemitterMappingService.ApproverRemitterMappingSpec searchSpec,
            BindingResult bindingResult,
            Model model) {
        model.addAttribute("mode", "CREATE");
        prepareSearchModel(model, searchSpec);

        ModelAndView mav = new ModelAndView("approverRemitterMapping-update", model.asMap());
        if (!bindingResult.hasErrors()) {

            if (mappingService.validateAndCreateMapping(searchSpec, bindingResult)) {
                mav.setViewName(VIEW_APPROVER_REMITTER_MAPPING_ERROR);
                mav.addObject(ATTR_SUCCESS_MSG, "Successfully created mapping.");
            }
            mav.addAllObjects(bindingResult.getModel());
        }
        if (bindingResult.hasErrors()) {
            mav.addAllObjects(bindingResult.getModel());
        }
        return mav;
    }

    /**
     * Lightweight POJO to pass the data between UI and backend
     */
    public static class ModifyRequestSpec {
        Long selectedRow;
        Long selectedId;

        public Long getSelectedRow() {
            return selectedRow;
        }

        public void setSelectedRow(Long selectedRow) {
            this.selectedRow = selectedRow;
        }

        public Long getSelectedId() {
            return selectedId;
        }

        public void setSelectedId(Long selectedId) {
            this.selectedId = selectedId;
        }

        @Override
        public String toString() {
            return "ModifyRequestSpec{" +
                    "selectedRow=" + selectedRow +
                    ", selectedId=" + selectedId +
                    '}';
        }
    }

}
