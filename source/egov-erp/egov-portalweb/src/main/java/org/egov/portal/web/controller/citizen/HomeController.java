package org.egov.portal.web.controller.citizen;

import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.egov.infra.admin.master.entity.User;
import org.egov.infra.citizen.inbox.entity.CitizenInbox;
import org.egov.infra.citizen.inbox.service.CitizenInboxService;
import org.egov.infra.security.utils.SecurityUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

@Controller
@RequestMapping(value = "/home")
public class HomeController {

	@Autowired
	private CitizenInboxService citizenInboxService;

	@Autowired
	private SecurityUtils securityUtils;

	@RequestMapping(method = RequestMethod.GET)
	public String LoginForm(final HttpServletRequest request, final HttpSession session, final ModelMap modelData) {
		return setupHomePage(request, session, modelData);

	}
	
	@RequestMapping(value = "/refreshInbox", method = RequestMethod.GET)
    public @ResponseBody Integer refreshInbox(@RequestParam Long citizenInboxId) {
		CitizenInbox citizenInbox = citizenInboxService.getInboxMessageById(citizenInboxId);
		citizenInbox.setRead(true);
		return citizenInboxService.findUnreadMessagesCount(securityUtils.getCurrentUser());
    }

	private String setupHomePage(final HttpServletRequest request, final HttpSession session, final ModelMap modelData) {
		final User user = securityUtils.getCurrentUser();
		modelData.addAttribute("userName", user.getName());
		modelData.addAttribute("unreadMessageCount", getUnreadMessageCount());
		modelData.addAttribute("inboxMessages", getAllInboxMessages());
		modelData.addAttribute("myAccountMessages", getMyAccountMessages());
		modelData.addAttribute("cityLogo", session.getAttribute("citylogo"));
        modelData.addAttribute("cityName", session.getAttribute("cityname"));
        modelData.addAttribute("userName", user.getName() == null ? "Anonymous" : user.getName());
		return "citizen-home";
	}

	private List<CitizenInbox> getMyAccountMessages() {
		return citizenInboxService.findMyAccountMessages(securityUtils.getCurrentUser());
	}

	private List<CitizenInbox> getAllInboxMessages() {
		return citizenInboxService.findAllInboxMessage(securityUtils.getCurrentUser());
	}

	private Integer getUnreadMessageCount() {
		return citizenInboxService.findUnreadMessagesCount(securityUtils.getCurrentUser());
	}

}
