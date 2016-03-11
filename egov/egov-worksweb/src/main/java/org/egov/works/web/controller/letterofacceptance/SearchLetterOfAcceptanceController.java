package org.egov.works.web.controller.letterofacceptance;

import java.util.List;

import org.egov.infra.admin.master.service.DepartmentService;
import org.egov.infra.exception.ApplicationException;
import org.egov.works.entity.letterofacceptance.SearchLetterOfAcceptanceRequest;
import org.egov.works.models.milestone.Milestone;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

@Controller
/*@RequestMapping(value = "/search")*/
public class SearchLetterOfAcceptanceController {

    @Autowired
    private DepartmentService departmentService;


    @ModelAttribute
    public void getReportModel(final Model model) {
        final SearchLetterOfAcceptanceRequest milestoneResult = new SearchLetterOfAcceptanceRequest();
        model.addAttribute("milestoneResult", milestoneResult);
    }

    @RequestMapping(value = "/searchloa-milestone", method = RequestMethod.GET)
    public String searchMilestone(final Model model) {
        setDropDownValues(model);
        return "search-searchloa";
    }

    private void setDropDownValues(final Model model) {

        model.addAttribute("executingDepartments", departmentService.getAllDepartments());
    }

    public DepartmentService getDepartmentService() {
        return departmentService;
    }

    public void setDepartmentService(final DepartmentService departmentService) {
        this.departmentService = departmentService;
    }

    @RequestMapping(value = "/searchloa-milestone", method = RequestMethod.POST, produces = MediaType.APPLICATION_JSON_VALUE)
    public @ResponseBody String searchMilestone(@ModelAttribute final Milestone milestone,
            final Model model) throws ApplicationException {
        setDropDownValues(model);
        model.addAttribute("milestone", milestone);
        return "Json String";
    }

    // TODO: Get result and store in list and display, write a query
    @RequestMapping(value = "/ajax-searchresultloa", method = RequestMethod.POST, produces = MediaType.TEXT_PLAIN_VALUE)
    public @ResponseBody String ajaxsearch(final Model model, @ModelAttribute final SearchLetterOfAcceptanceRequest milestoneSearchRequest)
    {
        final List<Milestone> searchResultList = search(milestoneSearchRequest);
        final String result = new StringBuilder("{ \"data\":").append(toSearchResultJson(searchResultList)).append("}")
                .toString();
        return result;
    }

    public Object toSearchResultJson(final Object object)
    {
        final GsonBuilder gsonBuilder = new GsonBuilder();
        final Gson gson = gsonBuilder.registerTypeAdapter(SearchLetterOfAcceptanceRequest.class, new SearchLetterOfAcceptanceJsonAdaptor())
                .create();
        final String json = gson.toJson(object);
        return json;
    }
    
    public List<Milestone> search(final SearchLetterOfAcceptanceRequest searchLetterOfAcceptanceRequest) {
        return list();
    }

    private List<Milestone> list() {
        // TODO need to work on this its just dummy not correct
        return null;
    }

}
