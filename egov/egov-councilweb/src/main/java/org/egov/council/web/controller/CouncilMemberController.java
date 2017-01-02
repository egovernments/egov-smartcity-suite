package org.egov.council.web.controller;

import static org.egov.infra.utils.JsonUtils.toJSON;

import java.io.IOException;
import java.util.List;

import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;

import org.apache.log4j.Logger;
import org.egov.council.entity.CouncilMember;
import org.egov.council.entity.CouncilMemberStatus;
import org.egov.council.service.CouncilCasteService;
import org.egov.council.service.CouncilDesignationService;
import org.egov.council.service.CouncilMemberService;
import org.egov.council.service.CouncilPartyService;
import org.egov.council.service.CouncilQualificationService;
import org.egov.council.service.es.CouncilMemberIndexService;
import org.egov.council.utils.constants.CouncilConstants;
import org.egov.council.web.adaptor.CouncilMemberJsonAdaptor;
import org.egov.infra.admin.master.service.BoundaryService;
import org.egov.infra.filestore.service.FileStoreService;
import org.egov.infra.utils.FileStoreUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
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

@Controller
@RequestMapping("/councilmember")
public class CouncilMemberController {
    private static final String COUNCIL_MEMBER = "councilMember";
    private static final String COUNCILMEMBER_NEW = "councilmember-new";
    private static final String COUNCILMEMBER_RESULT = "councilmember-result";
    private static final String COUNCILMEMBER_EDIT = "councilmember-edit";
    private static final String COUNCILMEMBER_VIEW = "councilmember-view";
    private static final String COUNCILMEMBER_SEARCH = "councilmember-search";
    private static final String MODULE_NAME = "COUNCIL";
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
    @Qualifier("fileStoreService")
    @Autowired 
    protected FileStoreService fileStoreService;
    @Autowired 
    protected FileStoreUtils fileStoreUtils;
    @Autowired private CouncilMemberIndexService councilMemberIndexService;
    private static final Logger LOGGER = Logger.getLogger(CouncilMemberController.class);

    private void prepareNewForm(final Model model) {
        model.addAttribute("boundarys",
                boundaryService.getActiveBoundariesByBndryTypeNameAndHierarchyTypeName("Ward", "ADMINISTRATION"));// GET
                                                                                                                  // ELECTION
        model.addAttribute("councilDesignations",councilDesignationService.getActiveDesignations());                                                                                                      // WARD.
        model.addAttribute("councilQualifications", councilQualificationService.getActiveQualifications());
        model.addAttribute("councilCastes", councilCasteService.getActiveCastes());
        model.addAttribute("councilPartys", councilPartyService.getActiveParties());
        model.addAttribute("category", CouncilConstants.CATEGORY);
    }

    @RequestMapping(value = "/new", method = RequestMethod.GET)
    public String newForm(final Model model) {
        prepareNewForm(model);
        model.addAttribute("state", "NEW");
        model.addAttribute(COUNCIL_MEMBER, new CouncilMember());
        return COUNCILMEMBER_NEW;
    }

    @RequestMapping(value = "/create", method = RequestMethod.POST)
    public String create(@Valid @ModelAttribute final CouncilMember councilMember,final BindingResult errors,
            @RequestParam final MultipartFile attachments,final Model model,
            final RedirectAttributes redirectAttrs){
    	if (errors.hasErrors()) {
            prepareNewForm(model);
            return COUNCILMEMBER_NEW;
        }
        if (councilMember != null && councilMember.getStatus() == null)
            councilMember.setStatus(CouncilMemberStatus.ACTIVE);

        if (attachments.getSize() > 0) {
            try {
                councilMember.setPhoto(fileStoreService.store(attachments.getInputStream(),
                        attachments.getOriginalFilename(), attachments.getContentType(), MODULE_NAME));
            } catch (IOException e) {
                LOGGER.error("Error in loading Employee photo" + e.getMessage(), e);
            }
        }
        councilMemberService.create(councilMember);
        councilMemberIndexService.createCouncilMemberIndex(councilMember);
        redirectAttrs.addFlashAttribute("message", messageSource.getMessage("msg.councilMember.success", null, null));
        return "redirect:/councilmember/result/" + councilMember.getId();
    }
   @RequestMapping(value = "/edit/{id}", method = RequestMethod.GET)
    public String edit(@PathVariable("id") final Long id, final Model model, final HttpServletResponse response)
            throws IOException {
        CouncilMember councilMember = councilMemberService.findOne(id);
        prepareNewForm(model);
        model.addAttribute(COUNCIL_MEMBER, councilMember);

        return COUNCILMEMBER_EDIT;
    }

    @RequestMapping(value = "/update", method = RequestMethod.POST)
    public String update(@Valid @ModelAttribute final CouncilMember councilMember,@RequestParam final MultipartFile attachments, final BindingResult errors,
            final Model model, final RedirectAttributes redirectAttrs) {
        if (errors.hasErrors()) {
            prepareNewForm(model);
            return COUNCILMEMBER_EDIT;
        }
        if (attachments.getSize() > 0) {
            try {
                councilMember.setPhoto(fileStoreService.store(attachments.getInputStream(),
                        attachments.getOriginalFilename(), attachments.getContentType(), MODULE_NAME));
            } catch (IOException e) {
                LOGGER.error("Error in loading Employee photo" + e.getMessage(), e);
            }
        }
        councilMemberService.update(councilMember);
        councilMemberIndexService.createCouncilMemberIndex(councilMember);
        redirectAttrs.addFlashAttribute("message", messageSource.getMessage("msg.councilMember.success", null, null));
        return "redirect:/councilmember/result/" + councilMember.getId();
    }

    @RequestMapping(value = "/view/{id}", method = RequestMethod.GET)
    public String view(@PathVariable("id") final Long id, Model model) {
        CouncilMember councilMember = councilMemberService.findOne(id);
        prepareNewForm(model);
        model.addAttribute(COUNCIL_MEMBER, councilMember);

        return COUNCILMEMBER_VIEW;
    }

    @RequestMapping(value = "/result/{id}", method = RequestMethod.GET)
    public String result(@PathVariable("id") final Long id, Model model) {
        CouncilMember councilMember = councilMemberService.findOne(id);
        model.addAttribute(COUNCIL_MEMBER, councilMember);
        return COUNCILMEMBER_RESULT;
    }

    @RequestMapping(value = "/search/{mode}", method = RequestMethod.GET)
    public String search(@PathVariable("mode") final String mode, Model model) {
        CouncilMember councilMember = new CouncilMember();
        prepareNewForm(model);
        model.addAttribute(COUNCIL_MEMBER, councilMember);
        return COUNCILMEMBER_SEARCH;

    }

    @RequestMapping(value = "/ajaxsearch/{mode}", method = RequestMethod.POST, produces = MediaType.TEXT_PLAIN_VALUE)
    @ResponseBody public String ajaxsearch(@PathVariable("mode") final String mode, Model model,
            @ModelAttribute final CouncilMember councilMember) {
        List<CouncilMember> searchResultList = councilMemberService.search(councilMember);
        return new StringBuilder("{ \"data\":").append(toJSON(searchResultList,CouncilMember.class,  CouncilMemberJsonAdaptor.class)).append("}")
                .toString();
    }

    @RequestMapping(value = "/downloadfile/{fileStoreId}")
    public void download(@PathVariable final String fileStoreId, final HttpServletResponse response) throws IOException {
        fileStoreUtils.fetchFileAndWriteToStream(fileStoreId, MODULE_NAME, false, response);
    }

}