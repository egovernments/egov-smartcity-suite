/**
 * eGov suite of products aim to improve the internal efficiency,transparency,
   accountability and the service delivery of the government  organizations.

    Copyright (C) <2015>  eGovernments Foundation

    The updated version of eGov suite of products as by eGovernments Foundation
    is available at http://www.egovernments.org

    This program is free software: you can redistribute it and/or modify
    it under the terms of the GNU General Public License as published by
    the Free Software Foundation, either version 3 of the License, or
    any later version.

    This program is distributed in the hope that it will be useful,
    but WITHOUT ANY WARRANTY; without even the implied warranty of
    MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
    GNU General Public License for more details.

    You should have received a copy of the GNU General Public License
    along with this program. If not, see http://www.gnu.org/licenses/ or
    http://www.gnu.org/licenses/gpl.html .

    In addition to the terms of the GPL license to be adhered to in using this
    program, the following additional terms are to be complied with:

        1) All versions of this program, verbatim or modified must carry this
           Legal Notice.

        2) Any misrepresentation of the origin of the material is prohibited. It
           is required that all modified versions of this material be marked in
           reasonable ways as different from the original version.

        3) This license does not grant any rights to any user of the program
           with regards to rights under trademark law for use of the trade names
           or trademarks of eGovernments Foundation.

  In case of any queries, you can reach eGovernments Foundation at contact@egovernments.org.
 */
package org.egov.api.controller;

import java.text.SimpleDateFormat;
import java.util.Date;

import javax.servlet.http.HttpServletRequest;

import org.egov.api.adapter.UserAdapter;
import org.egov.api.controller.core.ApiController;
import org.egov.api.controller.core.ApiUrl;
import org.egov.api.controller.core.ApiResponse;
import org.egov.infra.admin.master.entity.User;
import org.egov.infra.admin.master.service.UserService;
import org.egov.infra.config.security.authentication.SecureUser;
import org.egov.infra.persistence.entity.enums.Gender;
import org.egov.portal.entity.Citizen;
import org.egov.portal.service.CitizenService;
import org.egov.search.service.SearchService;
import org.json.simple.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.oauth2.common.OAuth2AccessToken;
import org.springframework.security.oauth2.provider.OAuth2Authentication;
import org.springframework.security.oauth2.provider.token.TokenStore;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

@org.springframework.web.bind.annotation.RestController
@RequestMapping("/v1.0")
public class CitizenController extends ApiController {

	@Autowired
	private UserService userService;

	@Autowired
	private SearchService searchService;

	@Autowired
	private CitizenService citizenService;

	@Autowired
	private TokenStore tokenStore;

	// --------------------------------------------------------------------------------//

	/**
	 * 
	 * @param request
	 * @return User
	 * 
	 * @since version 1.0
	 * 
	 *        It will reaturn user information belongs to identity Identiy may
	 *        be the user email or mobile number
	 * 
	 */

	@RequestMapping(value = ApiUrl.CITIZEN_GET_PROFILE, method = RequestMethod.GET, produces = MediaType.TEXT_PLAIN_VALUE)
	public ResponseEntity<String> getProfle(HttpServletRequest request,
			OAuth2Authentication authentication) {
		ApiResponse res = ApiResponse.newInstance();

		SecureUser su = (SecureUser) authentication.getUserAuthentication()
				.getPrincipal();

		User user = userService.getUserByUsername(su.getUsername());
		if (user == null) {
			return res.error(getMessage("user.not.found"));
		}
		return res.setDataAdapter(new UserAdapter()).success(user);
	}

	// --------------------------------------------------------------------------------//
	/** 
	 * @param request
	 * @return 
	 */
	@RequestMapping(value = ApiUrl.CITIZEN_LOGOUT, method = RequestMethod.POST)
	public ResponseEntity<String> logout(HttpServletRequest request,
			OAuth2Authentication authentication) {

		OAuth2AccessToken token = tokenStore.getAccessToken(authentication);
		if (token == null) {
			return ApiResponse.newInstance().error(
					getMessage("msg.logout.unknown"));
		}

		tokenStore.removeAccessToken(token);
		return ApiResponse.newInstance().success(
				getMessage("msg.logout.success"));
	}

	// --------------------------------------------------------------------------------//
	/** 
	 * @param citizen
	 * @return 
	 */
	@RequestMapping(value = ApiUrl.CITIZEN_UPDATE_PROFILE, method = RequestMethod.PUT, consumes = { "application/json" })
	public ResponseEntity<String> updateProfile(@RequestBody JSONObject citizen) {

		ApiResponse res = ApiResponse.newInstance();
		String msg = "";
		try {
			Citizen citizenUpdate = citizenService.getCitizenByUserName(citizen
					.get("userName").toString());
			citizenUpdate.setName(citizen.get("name").toString());
			citizenUpdate.setGender(Gender.valueOf(citizen.get("gender")
					.toString()));
			citizenUpdate.setMobileNumber(citizen.get("mobileNumber")
					.toString());
			citizenUpdate.setEmailId(citizen.get("emailId").toString());
			citizenUpdate.setAltContactNumber(citizen.get("altContactNumber")
					.toString());

			SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
			Date dt = sdf.parse(citizen.get("dob").toString());
			citizenUpdate.setDob(dt);

			citizenUpdate.setPan(citizen.get("pan").toString());
			citizenUpdate.setAadhaarNumber(citizen.get("aadhaarNumber")
					.toString());
			citizenUpdate.setLocale(citizen.get("locale").toString());

			citizenService.update(citizenUpdate);
			citizenService.sendActivationMessage(citizenUpdate);
			return res.setDataAdapter(new UserAdapter()).success(citizen,
					this.getMessage("msg.citizen.update.success"));

		} catch (Exception e) {
			msg = e.getMessage();
		}
		return res.error(msg);
	}

}