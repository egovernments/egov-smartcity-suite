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

import java.util.ArrayList;
import java.util.List;

import org.apache.commons.lang3.StringUtils;
import org.apache.log4j.Logger;
import org.egov.api.adapter.UserAdapter;
import org.egov.api.controller.core.ApiController;
import org.egov.api.controller.core.ApiResponse;
import org.egov.api.controller.core.ApiUrl;
import org.egov.pushbox.application.entity.MessageContent;
import org.egov.pushbox.application.entity.UserDevice;
import org.egov.pushbox.application.service.PushNotificationService;
import org.json.JSONArray;
import org.json.simple.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

/**
 *
 * @author Darshan Nagesh
 *
 */
@org.springframework.web.bind.annotation.RestController
public class RestPushBoxController extends ApiController {

    public static final String USER_ID = "userId";
    public static final String USER_TOKEN_ID = "userToken";
    public static final String SEND_ALL = "sendAll";
    public static final String USER_DEVICE_ID = "deviceId";

    private static final Logger LOGGER = Logger.getLogger(RestPushBoxController.class);

    @Autowired
    private PushNotificationService notificationService;

    @RequestMapping(value = ApiUrl.UPDATE_USER_TOKEN, method = RequestMethod.POST, consumes = { "application/json" })
    public @ResponseBody ResponseEntity<String> updateToken(@RequestBody JSONObject tokenUpdate) {
        LOGGER.info("##PushBoxFox### : Update Token Method Request Received");
        ApiResponse res = ApiResponse.newInstance();
        try {
            UserDevice userDevice = new UserDevice();
            userDevice.setUserDeviceToken(tokenUpdate.get(USER_TOKEN_ID).toString());
            userDevice.setUserId(Long.valueOf(tokenUpdate.get(USER_ID).toString()));
            userDevice.setDeviceId(tokenUpdate.get(USER_DEVICE_ID).toString());

            if (StringUtils.isBlank(userDevice.getUserDeviceToken()))
                return res.error(getMessage("userdevice.device.tokenunavailable"));

            if (null == userDevice.getUserId())
                return res.error(getMessage("userdevice.user.useridunavailable"));

            if (StringUtils.isBlank(userDevice.getDeviceId()))
                return res.error(getMessage("userdevice.user.deviceidunavailable"));
            LOGGER.info("##PushBoxFox### : Update Token Method passed the Request Received : " + userDevice);
            UserDevice responseObject = notificationService.persist(userDevice);
            return res.setDataAdapter(new UserAdapter()).success(responseObject, getMessage("msg.userdevice.update.success"));
        } catch (Exception e) {
            LOGGER.error(EGOV_API_ERROR, e);
            return res.error(getMessage(SERVER_ERROR_KEY));
        }
    }

    @RequestMapping(value = ApiUrl.SEND_NOTIFICATIONS, method = RequestMethod.POST, consumes = { "application/json" })
    public @ResponseBody ResponseEntity<String> sendNotification(@RequestBody JSONObject content) {
        LOGGER.info("##PushBoxFox## : Received the Message Content at Pushbox");
        Boolean sendAll = (Boolean) content.get(SEND_ALL);
        ApiResponse res = ApiResponse.newInstance();
        try {
            MessageContent message = createMessageContentFromRequest(content);
            if (!sendAll) {
                JSONArray jsonArray = new JSONArray(content.get("userIdList"));
                List<Long> userIdList = new ArrayList<>();
                for (int i = 0; i < jsonArray.length(); i++)
                    userIdList.add(Long.valueOf(StringUtils.isNotBlank(jsonArray.getString(i)) ? jsonArray.getString(i) : "0"));
                message.setUserIdList(userIdList);
            } else
                message.setSendAll(Boolean.TRUE);
            LOGGER.info("##PushBoxFox## : Sending the Message to Send Notifications Method : " + message);
            notificationService.sendNotifications(message);

            UserDevice responseObject = new UserDevice();
            return res.setDataAdapter(new UserAdapter()).success(responseObject, getMessage("msg.userdevice.update.success"));
        } catch (Exception e) {
            LOGGER.error(EGOV_API_ERROR, e);
            return res.error(getMessage(SERVER_ERROR_KEY));
        }
    }

    private MessageContent createMessageContentFromRequest(JSONObject content) {
        MessageContent message = new MessageContent();
        message.setMessageId(Long.valueOf(content.get("messageId").toString()));
        message.setCreatedDateTime(Long.valueOf(content.get("createdDate").toString()));
        message.setEventAddress(content.get("eventAddress").toString());
        message.setEventDateTime(Long.valueOf(content.get("eventDateTime").toString()));
        message.setEventLocation(content.get("eventLocation").toString());
        message.setExpiryDate(Long.valueOf(content.get("expiryDate").toString()));
        message.setImageUrl(content.get("imageUrl").toString());
        message.setMessageBody(content.get("messageBody").toString());
        message.setModuleName(content.get("moduleName").toString());
        message.setNotificationDateTime(Long.valueOf(content.get("notificationDateTime").toString()));
        message.setNotificationType(content.get("notificationType").toString());
        message.setSenderId(Long.valueOf(content.get("senderId").toString()));
        message.setSenderName(content.get("senderName").toString());
        return message;
    }

}