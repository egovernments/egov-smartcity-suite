package org.egov.api.controller;

import java.io.File;
import java.io.IOException;
import java.io.OutputStream;
import java.nio.file.Files;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.io.FileUtils;
import org.apache.commons.io.IOUtils;
import org.egov.api.adapter.UserAdapter;
import org.egov.api.controller.core.ApiController;
import org.egov.api.controller.core.ApiResponse;
import org.egov.api.controller.core.ApiUrl;
import org.egov.exceptions.DuplicateElementException;
import org.egov.exceptions.EGOVRuntimeException;
import org.egov.infra.admin.common.service.IdentityRecoveryService;
import org.egov.portal.entity.Citizen;
import org.egov.portal.service.CitizenService;
import org.json.simple.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

/**
 * @author Sheik
 *
 */
@org.springframework.web.bind.annotation.RestController
@RequestMapping("/v1.0")
public class CommonController extends ApiController {

	@Autowired
	private CitizenService citizenService;

	@Autowired
	private IdentityRecoveryService identityRecoveryService;

	@RequestMapping(value = { "/errorHandler" }, method = { RequestMethod.GET,
			RequestMethod.POST, RequestMethod.PUT, RequestMethod.DELETE })
	@ResponseBody
	public ResponseEntity<String> handle404() {
		return new ResponseEntity<String>("404 error============= ",
				HttpStatus.NOT_FOUND);
	}

	// -----------------------------------------------------------------
	/**
	 * 
	 * @param citizen
	 * @return
	 */
	@RequestMapping(value = ApiUrl.CITIZEN_REGISTER, method = RequestMethod.POST, consumes = { "application/json" })
	public @ResponseBody ResponseEntity<String> register(
			@RequestBody JSONObject citizen) {
		ApiResponse res = ApiResponse.newInstance();
		String msg = "";

		try {
			Citizen citizenCreate = new Citizen();
			citizenCreate.setMobileNumber(citizen.get("mobileNumber")
					.toString());
			citizenCreate.setName(citizen.get("name").toString());
			citizenCreate.setEmailId(citizen.get("emailId").toString());
			citizenCreate.setPassword(citizen.get("password").toString());
			citizenService.create(citizenCreate);
			citizenService.sendActivationMessage(citizenCreate);
			return res.setDataAdapter(new UserAdapter()).success(citizenCreate,
					this.getMessage("msg.citizen.reg.success"));

		} catch (DuplicateElementException e) {
			msg = e.getMessage();
		} catch (EGOVRuntimeException e) {
			msg = e.getMessage();
		}
		return res.error(msg);
	}

	// --------------------------------------------------------------------------------//
	/**
	 * 
	 * @param String
	 *            userName
	 * @param String
	 *            activationCode
	 * @return
	 */
	@RequestMapping(value = ApiUrl.CITIZEN_ACTIVATE, method = RequestMethod.POST)
	public ResponseEntity<String> activate(
			@RequestParam("userName") String userName,
			@RequestParam("activationCode") String activationCode) {
		Citizen citizen = citizenService.getCitizenByUserName(userName);
		ApiResponse res = ApiResponse.newInstance();

		if (citizen == null) {
			return res.error(getMessage("citizen.not.found"));
		} else if (activationCode == null) {
			return res.error(getMessage("citizen.valid.activationCode"));
		} else if (citizen.isActive()) {
			return res.success("", getMessage("citizen.activated"));
		} else if (citizen.getActivationCode().equals(activationCode)) {
			citizen.setActive(true);
			citizenService.update(citizen);
			return res.success("", getMessage("citizen.success.activated"));
		} else {
			return res.error(getMessage("citizen.valid.activationCode"));
		}

	}

	// --------------------------------------------------------------------------------//
	/**
	 * 
	 * @param request
	 * @return
	 */
	@RequestMapping(value = ApiUrl.CITIZEN_PASSWORD_RECOVER, method = RequestMethod.POST)
	public ResponseEntity<String> passwordRecover(HttpServletRequest request) {
		ApiResponse res = ApiResponse.newInstance();
		String identity = request.getParameter("identity");

		if (identity != null) {
			if (identityRecoveryService.generateAndSendUserPasswordRecovery(
					identity, request.getRequestURL()
							+ "/egi/login/password/reset?token=")) {
				String msg = "";
				if (identity.indexOf('@') != -1) {
					msg = "email";
				} else {
					msg = "mobile";
				}
				msg = "Password has been sent to " + msg;
				return res.success("", msg);
			} else {
				return res.error("Password send failed");
			}
		} else {
			return res.error("Invalid email or mobile number");
		}

	}

	// -------------------------------------------------------------------------
	/**
	 * 
	 * @param id
	 * @return
	 */
	@RequestMapping(value = ApiUrl.COMPLAINT_DOWNLOAD_COMPLAINT_TYPE_DOCUMENT, method = RequestMethod.GET)
	public void getComplaintTypeDoc(@RequestParam("id") Long id,
			HttpServletResponse response) {
		try {
			File downloadFile = new File("/root/egovfilestore/complaintType/"
					+ id);
			response.setHeader("Content-Length",
					String.valueOf(downloadFile.length()));
			response.setHeader("Content-Disposition", "attachment;filename="
					+ id);
			response.setContentType(Files.probeContentType(downloadFile
					.toPath()));
			OutputStream out = response.getOutputStream();
			IOUtils.write(FileUtils.readFileToByteArray(downloadFile), out);
			IOUtils.closeQuietly(out);

		} catch (IOException e) {
			e.printStackTrace();
		}

	}

}
