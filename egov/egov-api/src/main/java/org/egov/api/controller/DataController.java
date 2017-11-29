/*
 *    eGov  SmartCity eGovernance suite aims to improve the internal efficiency,transparency,
 *    accountability and the service delivery of the government  organizations.
 *
 *     Copyright (C) 2017  eGovernments Foundation
 *
 *     The updated version of eGov suite of products as by eGovernments Foundation
 *     is available at http://www.egovernments.org
 *
 *     This program is free software: you can redistribute it and/or modify
 *     it under the terms of the GNU General Public License as published by
 *     the Free Software Foundation, either version 3 of the License, or
 *     any later version.
 *
 *     This program is distributed in the hope that it will be useful,
 *     but WITHOUT ANY WARRANTY; without even the implied warranty of
 *     MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 *     GNU General Public License for more details.
 *
 *     You should have received a copy of the GNU General Public License
 *     along with this program. If not, see http://www.gnu.org/licenses/ or
 *     http://www.gnu.org/licenses/gpl.html .
 *
 *     In addition to the terms of the GPL license to be adhered to in using this
 *     program, the following additional terms are to be complied with:
 *
 *         1) All versions of this program, verbatim or modified must carry this
 *            Legal Notice.
 *            Further, all user interfaces, including but not limited to citizen facing interfaces,
 *            Urban Local Bodies interfaces, dashboards, mobile applications, of the program and any
 *            derived works should carry eGovernments Foundation logo on the top right corner.
 *
 *            For the logo, please refer http://egovernments.org/html/logo/egov_logo.png.
 *            For any further queries on attribution, including queries on brand guidelines,
 *            please contact contact@egovernments.org
 *
 *         2) Any misrepresentation of the origin of the material is prohibited. It
 *            is required that all modified versions of this material be marked in
 *            reasonable ways as different from the original version.
 *
 *         3) This license does not grant any rights to any user of the program
 *            with regards to rights under trademark law for use of the trade names
 *            or trademarks of eGovernments Foundation.
 *
 *   In case of any queries, you can reach eGovernments Foundation at contact@egovernments.org.
 *
 */

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
