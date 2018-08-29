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

package org.egov.pushbox.service;

import com.google.api.client.googleapis.util.Utils;
import com.google.api.client.json.JsonFactory;
import com.google.auth.oauth2.GoogleCredentials;
import com.google.firebase.FirebaseApp;
import com.google.firebase.FirebaseOptions;
import com.google.firebase.messaging.FirebaseMessaging;
import com.google.firebase.messaging.Message;
import com.google.gson.Gson;
import org.egov.infra.admin.master.entity.User;
import org.egov.infra.admin.master.service.UserService;
import org.egov.infra.exception.ApplicationRuntimeException;
import org.egov.pushbox.entity.UserFcmDevice;
import org.egov.pushbox.entity.contracts.MessageContent;
import org.egov.pushbox.entity.contracts.MessageContentDetails;
import org.egov.pushbox.entity.contracts.SendNotificationRequest;
import org.egov.pushbox.entity.contracts.UserTokenRequest;
import org.egov.pushbox.repository.UserFcmDeviceRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.PostConstruct;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * @author Darshan Nagesh
 */
@Service
@Transactional(readOnly = true)
public class PushNotificationService {
    private static final Logger LOGGER = LoggerFactory.getLogger(PushNotificationService.class);

    @Autowired
    private UserFcmDeviceRepository pushNotificationRepo;

    @Autowired
    private UserService userService;

    @Autowired
    private Environment env;

    @Value("${firebase.enabled}")
    private boolean firebaseEnabled;

    @Transactional
    public UserFcmDevice saveUserDevice(final UserTokenRequest userTokenRequest) {
        UserFcmDevice existingRecord = pushNotificationRepo.findByUserIdAndDeviceId(userTokenRequest.getUserId(),
                userTokenRequest.getDeviceId());
        if (existingRecord == null) {
            UserFcmDevice userDevice = new UserFcmDevice();
            userDevice.setDeviceToken(userTokenRequest.getUserToken());
            User user = userService.getUserById(userTokenRequest.getUserId());
            userDevice.setUser(user);
            userDevice.setDeviceId(userTokenRequest.getDeviceId());
            return pushNotificationRepo.save(userDevice);
        } else {
            existingRecord.setDeviceToken(userTokenRequest.getUserToken());
            return pushNotificationRepo.save(existingRecord);
        }
    }

    public UserFcmDevice getUserFcmDevice(final Long id) {
        return pushNotificationRepo.findOne(id);
    }

    public UserFcmDevice getUserDeviceByUser(final Long userId) {
        return pushNotificationRepo.findByUserId(userId);
    }

    public List<UserFcmDevice> getAllUserDeviceByUser(List<Long> ids) {
        return pushNotificationRepo.findByUserIdIn(ids);
    }

    /**
     * This method fetch the userFcmDevice based on the user id and call the sendMessagesToDevices() method to send the push
     * messages
     *
     * @param messageContent
     */
    public void sendNotifications(MessageContent messageContent) {
        List<UserFcmDevice> userDeviceList = null;
        if (messageContent.getDetails().isSendAll())
            userDeviceList = getAllUserDeviceList();
        else
            userDeviceList = getUserDeviceList(messageContent);
        sendMessagesToDevices(userDeviceList, messageContent);
    }

    private List<UserFcmDevice> getUserDeviceList(MessageContent messageContent) {
        return getAllUserDeviceByUser(messageContent.getDetails().getUserIdList());
    }

    private List<UserFcmDevice> getAllUserDeviceList() {
        return pushNotificationRepo.findAll();
    }

    /**
     * This method build the message and send it to firebase for push notification. It uses messageContent and deviceToken to
     * build a message.
     *
     * @param userDeviceList
     * @param messageContent
     */
    private void sendMessagesToDevices(List<UserFcmDevice> userDeviceList, MessageContent messageContent) {
        for (UserFcmDevice userDevice : userDeviceList)
            try {
                Message message = Message.builder()
                        .putData("content", new Gson().toJson(messageContent))
                        .setToken(userDevice.getDeviceToken()).build();
                String response = FirebaseMessaging.getInstance().sendAsync(message).get();
                if (LOGGER.isInfoEnabled())
                    LOGGER.info("##PushBoxFox## : Message Send Status : {}", response);
            } catch (Exception ex) {
                LOGGER.error("##PushBoxFox## : Error : Encountered an exception while sending the message.", ex);
            }
    }

    public void buildAndSendNotification(SendNotificationRequest notificationRequest) {
        MessageContent message = createMessageContentFromRequest(notificationRequest);
        if (notificationRequest.isSendAll())
            message.getDetails().setSendAll(true);
        else
            message.getDetails().setUserIdList(notificationRequest.getUserIdList());
        message.getDetails().setSendAll(true);
        sendNotifications(message);
    }

    private MessageContent createMessageContentFromRequest(SendNotificationRequest notificationRequest) {
        MessageContent message = new MessageContent();
        MessageContentDetails messageDetails = new MessageContentDetails();
        message.setMessageId(Long.parseLong(notificationRequest.getMessageId()));
        message.setCreatedDateTime(Long.parseLong(notificationRequest.getCreatedDate()));
        messageDetails.setEventAddress(notificationRequest.getEventAddress());
        messageDetails.setEventDateTime(Long.parseLong(notificationRequest.getEventDateTime()));
        messageDetails.setEventLocation(notificationRequest.getEventLocation());
        message.setExpiryDate(Long.parseLong(notificationRequest.getExpiryDate()));
        message.setImageUrl(notificationRequest.getImageUrl());
        message.setMessageBody(notificationRequest.getMessageBody());
        message.setModuleName(notificationRequest.getCreatedDate());
        message.setNotificationDateTime(Long.parseLong(notificationRequest.getNotificationDateTime()));
        message.setNotificationType(notificationRequest.getNotificationType());
        message.setSenderId(Long.parseLong(notificationRequest.getSenderId()));
        message.setSenderName(notificationRequest.getSenderName());
        message.setDetails(messageDetails);
        return message;
    }

    /**
     * This method take the required parameters from the properties file and initialize the firebase. It will initialize only
     * once.
     */
    @PostConstruct
    public void setUpFirebaseApp() {
        if (firebaseEnabled && FirebaseApp.getApps().isEmpty()) {
            JsonFactory jsonFactory = Utils.getDefaultJsonFactory();
            Map<String, Object> secretJson = new ConcurrentHashMap<>();
            secretJson.put("type", env.getProperty("type"));
            secretJson.put("project_id", env.getProperty("project_id"));
            secretJson.put("private_key_id", env.getProperty("private_key_id"));
            secretJson.put("private_key", env.getProperty("private_key"));
            secretJson.put("client_email", env.getProperty("client_email"));
            secretJson.put("client_id", env.getProperty("client_id"));
            secretJson.put("auth_uri", env.getProperty("auth_uri"));
            secretJson.put("token_uri", env.getProperty("token_uri"));
            secretJson.put("auth_provider_x509_cert_url", env.getProperty("auth_provider_x509_cert_url"));
            secretJson.put("client_x509_cert_url", env.getProperty("client_x509_cert_url"));

            try (InputStream refreshTokenStream = new ByteArrayInputStream(jsonFactory.toByteArray(secretJson))) {

                FirebaseOptions options = new FirebaseOptions.Builder()
                        .setCredentials(GoogleCredentials.fromStream(refreshTokenStream))
                        .setDatabaseUrl(env.getProperty("bdurl")).build();

                FirebaseApp.initializeApp(options);
                if (LOGGER.isInfoEnabled())
                    LOGGER.info("##PushBoxFox## : Firebase App Initialized");
            } catch (IOException e) {
                LOGGER.error("##PushBoxFox## : Error in setup firebase app", e);
                throw new ApplicationRuntimeException("Error occurred while setup firebase app", e);
            }
        }
    }
}
