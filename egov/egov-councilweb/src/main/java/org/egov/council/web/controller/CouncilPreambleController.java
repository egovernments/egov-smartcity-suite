package org.egov.council.web.controller;

import java.io.IOException;
import java.util.List;

import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;

import org.apache.log4j.Logger;
import org.egov.commons.dao.EgwStatusHibernateDAO;
import org.egov.council.autonumber.PreambleNumberGenerator;
import org.egov.council.entity.CouncilPreamble;
import org.egov.council.service.CouncilPreambleService;
import org.egov.council.utils.constants.CouncilConstants;
import org.egov.council.web.adaptor.CouncilPreambleJsonAdaptor;
import org.egov.infra.admin.master.service.BoundaryService;
import org.egov.infra.admin.master.service.DepartmentService;
import org.egov.infra.filestore.service.FileStoreService;
import org.egov.infra.utils.FileStoreUtils;
import org.egov.infra.utils.autonumber.AutonumberServiceBeanResolver;
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

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

@Controller
@RequestMapping("/councilpreamble")
public class CouncilPreambleController {
    private final static String COUNCILPREAMBLE_NEW = "councilpreamble-new";
    private final static String COUNCILPREAMBLE_RESULT = "councilpreamble-result";
    private final static String COUNCILPREAMBLE_EDIT = "councilpreamble-edit";
    private final static String COUNCILPREAMBLE_VIEW = "councilpreamble-view";
    private final static String COUNCILPREAMBLE_SEARCH = "councilpreamble-search";
    public static final String WARDWISE = "ward";

    @Autowired
    private CouncilPreambleService councilPreambleService;
    @Autowired
    private MessageSource messageSource;

    @Autowired
    private DepartmentService departmentService;

    @Autowired
    private EgwStatusHibernateDAO egwStatusHibernateDAO;

    @Autowired
    private AutonumberServiceBeanResolver autonumberServiceBeanResolver;

    @Qualifier("fileStoreService")
    protected @Autowired FileStoreService fileStoreService;

    protected @Autowired FileStoreUtils fileStoreUtils;
    
    @Autowired
    private BoundaryService boundaryService;

    private static final Logger LOGGER = Logger.getLogger(CouncilPreambleController.class);

    private void prepareNewForm(final Model model) {
        model.addAttribute("departments", departmentService.getAllDepartments()); // WARD.
        model.addAttribute("wards", boundaryService.getActiveBoundariesByBndryTypeNameAndHierarchyTypeName(CouncilConstants.WARD,
                CouncilConstants.REVENUE_HIERARCHY_TYPE)); 
    }

    @RequestMapping(value = "/new", method = RequestMethod.GET)
    public String newForm(final Model model) {
        prepareNewForm(model);
        model.addAttribute("councilPreamble", new CouncilPreamble());
        
        return COUNCILPREAMBLE_NEW;
    }

    @RequestMapping(value = "/create", method = RequestMethod.POST)
    public String create(@Valid @ModelAttribute final CouncilPreamble councilPreamble, final BindingResult errors,
            @RequestParam final MultipartFile attachments, final Model model, final RedirectAttributes redirectAttrs) {
        if (errors.hasErrors()) {
            prepareNewForm(model);
            return COUNCILPREAMBLE_NEW;
        }

        if (attachments.getSize() > 0) {
            try {
                councilPreamble.setFilestoreid(fileStoreService.store(attachments.getInputStream(),
                        attachments.getOriginalFilename(), attachments.getContentType(), CouncilConstants.MODULE_NAME));
            } catch (IOException e) {
                LOGGER.error("Error in loading documents" + e.getMessage(), e);
            }
        }
        /*List<PreambleWards> preambleWardsList = new ArrayList<PreambleWards>();
        
        String [] wards = councilPreamble.getWards().split(",");
        for (String ward : wards) {
            PreambleWards preamWards = new PreambleWards(); 
            preamWards.setWard(Long.valueOf(ward));
            preambleWardsList.add(preamWards);
        }
        councilPreamble.setPreambleWards(preambleWardsList);*/
        PreambleNumberGenerator preamblenumbergenerator = autonumberServiceBeanResolver
                .getAutoNumberServiceFor(PreambleNumberGenerator.class);
        councilPreamble.setPreambleNumber(preamblenumbergenerator.getNextNumber(councilPreamble));
        councilPreamble.setStatus(egwStatusHibernateDAO.getStatusByModuleAndCode(CouncilConstants.PREAMBLE_MODULENAME,
                CouncilConstants.PREAMBLE_STATUS_CREATED));
        councilPreambleService.create(councilPreamble);
        redirectAttrs.addFlashAttribute("message", messageSource.getMessage("msg.councilPreamble.success", null, null));
        return "redirect:/councilpreamble/result/" + councilPreamble.getId();
    }

    @RequestMapping(value = "/downloadfile/{fileStoreId}")
    public void download(@PathVariable final String fileStoreId, final HttpServletResponse response) throws IOException {
        fileStoreUtils.fetchFileAndWriteToStream(fileStoreId, CouncilConstants.MODULE_NAME, false, response);
    }

    @RequestMapping(value = "/result/{id}", method = RequestMethod.GET)
    public String result(@PathVariable("id") final Long id, Model model) {
        CouncilPreamble councilPreamble = councilPreambleService.findOne(id);
        model.addAttribute("councilPreamble", councilPreamble);
        return COUNCILPREAMBLE_RESULT;
    }

    @RequestMapping(value = "/update", method = RequestMethod.POST)
    public String update(@Valid @ModelAttribute final CouncilPreamble councilPreamble, final Model model,
            @RequestParam final MultipartFile attachments, final BindingResult errors,
            final RedirectAttributes redirectAttrs) {
        if (errors.hasErrors()) {
            prepareNewForm(model);
            return COUNCILPREAMBLE_EDIT;
        }
        if (attachments.getSize() > 0) {
            try {
                councilPreamble.setFilestoreid(fileStoreService.store(attachments.getInputStream(),
                        attachments.getOriginalFilename(), attachments.getContentType(), CouncilConstants.MODULE_NAME));
            } catch (IOException e) {
                LOGGER.error("Error in loading Employee photo" + e.getMessage(), e);
            }
        }
        councilPreambleService.update(councilPreamble);
        redirectAttrs.addFlashAttribute("message", messageSource.getMessage("msg.councilPreamble.success", null, null));
        return "redirect:/councilpreamble/result/" + councilPreamble.getId();
    }

    @RequestMapping(value = "/edit/{id}", method = RequestMethod.GET)
    public String edit(@PathVariable("id") final Long id, final Model model, final HttpServletResponse response)
            throws IOException {
        CouncilPreamble councilPreamble = councilPreambleService.findOne(id);
        prepareNewForm(model);
        model.addAttribute("councilPreamble", councilPreamble);
        if(councilPreamble.getStatus().getCode().equals("PREAMBLEAPPROVEDFORMOM")){
            return COUNCILPREAMBLE_VIEW;
        } else{
            return COUNCILPREAMBLE_EDIT;
        }
    }

    @RequestMapping(value = "/view/{id}", method = RequestMethod.GET)
    public String view(@PathVariable("id") final Long id, Model model) {
        CouncilPreamble councilPreamble = councilPreambleService.findOne(id);
        prepareNewForm(model);
        model.addAttribute("councilPreamble", councilPreamble);

        return COUNCILPREAMBLE_VIEW;
    }

    @RequestMapping(value = "/search/{mode}", method = RequestMethod.GET)
    public String search(@PathVariable("mode") final String mode, Model model) {
        prepareNewForm(model);
        model.addAttribute("councilPreamble", new CouncilPreamble());
        return COUNCILPREAMBLE_SEARCH;

    }

    @RequestMapping(value = "/ajaxsearch/{mode}", method = RequestMethod.POST, produces = MediaType.TEXT_PLAIN_VALUE)
    public @ResponseBody String ajaxsearch(@PathVariable("mode") final String mode, Model model,
            @ModelAttribute final CouncilPreamble councilPreamble) {
        List<CouncilPreamble> searchResultList = councilPreambleService.search(councilPreamble);
        String result = new StringBuilder("{ \"data\":").append(toSearchResultJson(searchResultList)).append("}")
                .toString();
        return result;
    }

    public Object toSearchResultJson(final Object object) {
        final GsonBuilder gsonBuilder = new GsonBuilder();
        final Gson gson = gsonBuilder.registerTypeAdapter(CouncilPreamble.class, new CouncilPreambleJsonAdaptor())
                .create();
        final String json = gson.toJson(object);
        return json;
    }
}
