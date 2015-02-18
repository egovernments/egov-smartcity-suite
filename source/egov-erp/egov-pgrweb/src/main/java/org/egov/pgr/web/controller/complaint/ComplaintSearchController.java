package org.egov.pgr.web.controller.complaint;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

@Controller
@RequestMapping(value = "/complaint/citizen/anonymous")
public class ComplaintSearchController {

    @RequestMapping(value = "/search", method = RequestMethod.GET)
    public String showSearchForm() {
        return "complaint-search";
    }
}
