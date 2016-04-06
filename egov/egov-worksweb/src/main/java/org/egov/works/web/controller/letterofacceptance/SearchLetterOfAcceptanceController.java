package org.egov.works.web.controller.letterofacceptance;

import org.egov.infra.admin.master.service.DepartmentService;
import org.egov.infra.exception.ApplicationException;
import org.egov.works.letterofacceptance.entity.SearchRequestLetterOfAcceptance;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

@Controller
@RequestMapping(value = "/search-milestone")
public class SearchLetterOfAcceptanceController {

    @Autowired
    private DepartmentService departmentService;

    @RequestMapping(value = "/searchloa-milestone", method = RequestMethod.GET)
    public String searchMilestone(
            @ModelAttribute final SearchRequestLetterOfAcceptance searchRequestLetterOfAcceptance,
            final Model model) throws ApplicationException {
        setDropDownValues(model);
        model.addAttribute("searchRequestLetterOfAcceptance", searchRequestLetterOfAcceptance);
        return "search-searchmilestone";
    }

    private void setDropDownValues(final Model model) {
        model.addAttribute("department", departmentService.getAllDepartments());
    }

    public Object toSearchResultJson(final Object object)
    {
        final GsonBuilder gsonBuilder = new GsonBuilder();
        final Gson gson = gsonBuilder.registerTypeAdapter(SearchRequestLetterOfAcceptance.class, new SearchLetterOfAcceptanceJsonAdaptor()).create();
        final String json = gson.toJson(object);
        return json;
    }

}
