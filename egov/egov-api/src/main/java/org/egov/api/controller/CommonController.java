package org.egov.api.controller;

import javax.servlet.http.HttpServletRequest;

import org.egov.api.adapter.UserAdapter;
import org.egov.api.controller.core.ApiController;
import org.egov.api.controller.core.ApiResponse;
import org.egov.api.controller.core.ApiUrl;
import org.egov.exceptions.DuplicateElementException;
import org.egov.exceptions.EGOVRuntimeException;
import org.egov.infra.admin.common.service.IdentityRecoveryService;
import org.egov.infra.admin.master.entity.Device;
import org.egov.infra.admin.master.repository.DeviceRepository;
import org.egov.portal.entity.Citizen;
import org.egov.portal.service.CitizenService;
import org.json.simple.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
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
    private DeviceRepository deviceRepository;

    @Autowired
    private IdentityRecoveryService identityRecoveryService;

    // -----------------------------------------------------------------
    /**
     * 
     * @param citizen
     * @return
     */
    @RequestMapping(value = ApiUrl.CITIZEN_REGISTER, method = RequestMethod.POST, consumes = { "application/json" })
    public @ResponseBody ResponseEntity<String> register(@RequestBody JSONObject citizen) {
        ApiResponse res = ApiResponse.newInstance();
        String msg = "";

        try {
            Citizen citizenCreate = new Citizen();
            citizenCreate.setMobileNumber(citizen.get("mobileNumber").toString());
            citizenCreate.setName(citizen.get("name").toString());
            citizenCreate.setEmailId(citizen.get("emailId").toString());
            citizenCreate.setPassword(citizen.get("password").toString());
            Device device = deviceRepository.findByDeviceUId(citizen.get("deviceId").toString());
            if (device == null) {
                device = new Device();
                device.setDeviceId(citizen.get("deviceId").toString());
                device.setType(citizen.get("deviceType").toString());
                device.setOSVersion(citizen.get("OSVersion").toString());
            }
            citizenCreate.getDevices().add(device);
            citizenService.create(citizenCreate);
            citizenService.sendActivationMessage(citizenCreate);
            return res.setDataAdapter(new UserAdapter()).success(citizenCreate, this.getMessage("msg.citizen.reg.success"));
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
     * @param String userName
     * @param String activationCode
     * @return
     */
    @RequestMapping(value = ApiUrl.CITIZEN_ACTIVATE, method = RequestMethod.POST)
    public ResponseEntity<String> activate(@RequestParam("userName") String userName,
            @RequestParam("activationCode") String activationCode) {
        Citizen citizen = citizenService.getCitizenByUserName(userName);
        if (citizen == null) {
            citizen = citizenService.getCitizenByEmailId(userName);
        }
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
                return res.success("", "Password has been sent to mail");
            } else {
                return res.error("Password send failed");
            }
        } else {
            return res.error("Invalid mobile number");
        }

    }

}
