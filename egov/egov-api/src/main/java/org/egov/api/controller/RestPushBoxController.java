/*
 *    eGov  SmartCity eGovernance suite aims to improve the internal efficiency,transparency,
 *    accountability and the service delivery of the government  organizations.
 *
 *     Copyright (C) 2018  eGovernments Foundation
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

import static org.apache.commons.lang.StringUtils.EMPTY;
import static org.egov.api.controller.core.ApiUrl.SEND_NOTIFICATIONS;
import static org.egov.api.controller.core.ApiUrl.UPDATE_USER_TOKEN;
import static org.springframework.http.MediaType.APPLICATION_JSON_VALUE;

import javax.validation.Valid;

import org.egov.api.adapter.UserAdapter;
import org.egov.api.adapter.UserDeviceAdapter;
import org.egov.api.controller.core.ApiController;
import org.egov.api.controller.core.ApiResponse;
import org.egov.pushbox.entity.UserFcmDevice;
import org.egov.pushbox.entity.contracts.SendNotificationRequest;
import org.egov.pushbox.entity.contracts.UserTokenRequest;
import org.egov.pushbox.service.PushNotificationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

/**
 *
 * @author Darshan Nagesh
 *
 */
@RestController
public class RestPushBoxController extends ApiController {

    @Autowired
    private PushNotificationService notificationService;

    @PostMapping(path = UPDATE_USER_TOKEN, consumes = APPLICATION_JSON_VALUE)
    public @ResponseBody ResponseEntity<String> updateToken(@Valid @RequestBody UserTokenRequest userTokenRequest,
            BindingResult errors) {
        ApiResponse res = ApiResponse.newInstance();

        if (errors.hasErrors()) {
            String errorMessage = EMPTY;
            for (FieldError fieldError : errors.getFieldErrors())
                errorMessage = errorMessage
                        .concat(fieldError.getField().concat(" ").concat(fieldError.getDefaultMessage()).concat(" <br>"));
            return res.error(errorMessage);
        }

        UserFcmDevice responseObject = notificationService.saveUserDevice(userTokenRequest);
        return res.setDataAdapter(new UserDeviceAdapter()).success(responseObject,
                getMessage("msg.userdevice.update.success"));
    }

    @PostMapping(path = SEND_NOTIFICATIONS, consumes = APPLICATION_JSON_VALUE)
    public @ResponseBody ResponseEntity<String> sendNotification(@RequestBody SendNotificationRequest notificationRequest) {
        ApiResponse res = ApiResponse.newInstance();
        notificationService.buildAndSendNotification(notificationRequest);

        UserFcmDevice responseObject = new UserFcmDevice();
        return res.setDataAdapter(new UserAdapter()).success(responseObject, getMessage("msg.userdevice.update.success"));
    }
}