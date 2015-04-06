package org.egov.infra.web.controller;

import org.egov.infra.admin.master.entity.User;
import org.egov.infra.admin.master.entity.enums.UserType;
import org.egov.infra.security.utils.SecurityUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

@Controller
@RequestMapping(value = "/home")
public class HomeController {

    @Autowired
    private SecurityUtils securityUtils;

    @RequestMapping(method = RequestMethod.GET)
    public String LoginForm() {
       
        final User user = securityUtils.getCurrentUser();

        if (user.getType().equals(UserType.EMPLOYEE)){
            return "redirect:/common/homepage.action";
        } else{
            return "redirect:/../portal/home";
        }
    }

}
