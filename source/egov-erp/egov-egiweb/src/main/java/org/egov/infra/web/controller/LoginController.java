package org.egov.infra.web.controller;

import javax.servlet.http.HttpServletRequest;

import org.egov.infra.admin.master.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

@Controller
@RequestMapping(value = "/login")
public class LoginController {

    @Autowired
    private UserService userService;

    @RequestMapping(value = "/send-pwd", method = RequestMethod.POST)
    public String handlePasswordRecovery(HttpServletRequest request) {
        String SUCCESS = "redirect:/login/securityLogin.jsp";
        if (request.getParameter("emailOrMobileNum") != null) {
            if (userService.sentPasswordRecovery(request.getParameter("emailOrMobileNum"))) {
                SUCCESS = SUCCESS + "?passwordSendingSuccess=true";
            } else {
                SUCCESS = SUCCESS + "?passwordSendingFailed=true";
            }

        }
        return SUCCESS;
    }

}
