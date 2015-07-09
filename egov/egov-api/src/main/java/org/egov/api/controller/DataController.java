package org.egov.api.controller;

import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

/**
 * @author Nagesh.Chauhan
 *
 */
@org.springframework.web.bind.annotation.RestController
@RequestMapping("/v1.0/test")
public class DataController {

	@RequestMapping(value = "/citizen", method = RequestMethod.GET)
	public @ResponseBody String citizen() {
		return "citizen";
	}

	@PreAuthorize("#hasRole('ROLE_CLIENT')")
	@RequestMapping(value = "/complaint/1", method = RequestMethod.GET)
	public @ResponseBody String complaint1() {
		return "complaint-1";
	}

	@PreAuthorize("#hasRole('ROLE_ADMIN')")
	@RequestMapping(value = "/complaint/2", method = RequestMethod.GET)
	public @ResponseBody String complaint2() {
		return "complaint-2";
	}

	@PreAuthorize("#oauth2.clientHasRole('ROLE_CLIENT')")
	@RequestMapping(value = "/cc/3", method = RequestMethod.GET)
	public @ResponseBody String complaint3() {
		return "complaint-3";
	}

	@PreAuthorize("#oauth2.clientHasRole('ROLE_ADMIN')")
	@RequestMapping(value = "/cc/4", method = RequestMethod.GET)
	public @ResponseBody String complaint4() {
		return "complaint-4";
	}

	@RequestMapping(value = "/admin", method = RequestMethod.GET)
	public @ResponseBody String admin() {
		return "admin";
	}
	
	private void temp() {
		PageRequest request = new PageRequest(0, 20, Sort.Direction.DESC, "startTime");
	}

}
