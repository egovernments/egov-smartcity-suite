package org.egov.council.web.controller;

import java.io.IOException;
import java.util.List;

import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;

import org.apache.log4j.Logger;
import org.egov.council.entity.CouncilMeeting;
import org.egov.council.service.CommitteeTypeService;
import org.egov.council.service.CouncilMeetingService;
import org.egov.council.utils.constants.CouncilConstants;
import org.egov.council.web.adaptor.CouncilMeetingJsonAdaptor;
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
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

@Controller
@RequestMapping("/councilMeeting")
public class CouncilMeetingController {
    private final static String COUNCILMEETING_NEW = "councilMeeting-new";
    private final static String COUNCILMEETING_RESULT = "councilMeeting-result";
    private final static String COUNCILMEETING_EDIT = "councilMeeting-edit";
    private final static String COUNCILMEETING_VIEW = "councilMeeting-view";
    private final static String COUNCILMEETING_SEARCH = "councilMeeting-search";
    private final static String MODULE_NAME = "COUNCIL";
  
    @Autowired
    private CouncilMeetingService councilMeetingService;
    
    @Autowired
    private MessageSource messageSource;
    @Autowired
    private CommitteeTypeService committeeTypeService;
  
    @Qualifier("fileStoreService")
    protected @Autowired FileStoreService fileStoreService;
    protected @Autowired FileStoreUtils fileStoreUtils;
    private static final Logger LOGGER = Logger.getLogger(CouncilMeetingController.class);
    private void prepareNewForm(final Model model) {
        model.addAttribute("commiteeTypes", committeeTypeService.getActiveCommiteeType());
    }

    @RequestMapping(value = "/new", method = RequestMethod.GET)
    public String newForm(final Model model) {
        prepareNewForm(model);
        model.addAttribute("meetingTimingMap", CouncilConstants.MEETING_TIMINGS);
        model.addAttribute("councilMeeting", new CouncilMeeting());
        return COUNCILMEETING_NEW;
    }

    @RequestMapping(value = "/create", method = RequestMethod.POST)
    public String create(@Valid @ModelAttribute final CouncilMeeting councilMeeting,final BindingResult errors,
          final Model model,
            final RedirectAttributes redirectAttrs){
    	if (errors.hasErrors()) {
            prepareNewForm(model);
            return COUNCILMEETING_NEW;
        }
       /* if (councilMeeting != null && councilMeeting.getStatus() == null)
            councilMeeting.setStatus(CouncilMeetingStatus.ACTIVE);
*/

        councilMeetingService.create(councilMeeting);
        redirectAttrs.addFlashAttribute("message", messageSource.getMessage("msg.councilMeeting.success", null, null));
        return "redirect:/councilmember/result/" + councilMeeting.getId();
    }
   @RequestMapping(value = "/edit/{id}", method = RequestMethod.GET)
    public String edit(@PathVariable("id") final Long id, final Model model, final HttpServletResponse response)
            throws IOException {
        CouncilMeeting councilMeeting = councilMeetingService.findOne(id);
        prepareNewForm(model);
        model.addAttribute("councilMeeting", councilMeeting);

        return COUNCILMEETING_EDIT;
    }

    @RequestMapping(value = "/update", method = RequestMethod.POST)
    public String update(@Valid @ModelAttribute final CouncilMeeting councilMeeting, final BindingResult errors,
            final Model model, final RedirectAttributes redirectAttrs) {
        if (errors.hasErrors()) {
            prepareNewForm(model);
            return COUNCILMEETING_EDIT;
        }
 
        councilMeetingService.update(councilMeeting);
        redirectAttrs.addFlashAttribute("message", messageSource.getMessage("msg.councilMeeting.success", null, null));
        return "redirect:/councilmember/result/" + councilMeeting.getId();
    }

    @RequestMapping(value = "/view/{id}", method = RequestMethod.GET)
    public String view(@PathVariable("id") final Long id, Model model) {
        CouncilMeeting councilMeeting = councilMeetingService.findOne(id);
        prepareNewForm(model);
        model.addAttribute("councilMeeting", councilMeeting);

        return COUNCILMEETING_VIEW;
    }

    @RequestMapping(value = "/result/{id}", method = RequestMethod.GET)
    public String result(@PathVariable("id") final Long id, Model model) {
        CouncilMeeting councilMeeting = councilMeetingService.findOne(id);
        model.addAttribute("councilMeeting", councilMeeting);
        return COUNCILMEETING_RESULT;
    }

    @RequestMapping(value = "/search/{mode}", method = RequestMethod.GET)
    public String search(@PathVariable("mode") final String mode, Model model) {
        CouncilMeeting councilMeeting = new CouncilMeeting();
        prepareNewForm(model);
        model.addAttribute("councilMeeting", councilMeeting);
        return COUNCILMEETING_SEARCH;

    }

    @RequestMapping(value = "/ajaxsearch/{mode}", method = RequestMethod.POST, produces = MediaType.TEXT_PLAIN_VALUE)
    public @ResponseBody String ajaxsearch(@PathVariable("mode") final String mode, Model model,
            @ModelAttribute final CouncilMeeting councilMeeting) {
        List<CouncilMeeting> searchResultList = councilMeetingService.search(councilMeeting);
        String result = new StringBuilder("{ \"data\":").append(toSearchResultJson(searchResultList)).append("}")
                .toString();
        return result;
    }

 
    public Object toSearchResultJson(final Object object) {
        final GsonBuilder gsonBuilder = new GsonBuilder();
        final Gson gson = gsonBuilder.registerTypeAdapter(CouncilMeeting.class, new CouncilMeetingJsonAdaptor()).create();
        final String json = gson.toJson(object);
        return json;
    }

}