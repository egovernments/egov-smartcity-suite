package org.egov.pgrweb.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
public class ComplaintTypeController {

    @RequestMapping("/complaint-type")
    public String complaintType() {
        return "complaint-type";
    }
}
