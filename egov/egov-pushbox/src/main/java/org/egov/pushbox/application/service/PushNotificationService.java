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

package org.egov.pushbox.application.service;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.apache.log4j.Logger;
import org.egov.infra.filestore.service.FileStoreService;
import org.egov.pushbox.application.entity.MessageContent;
import org.egov.pushbox.application.entity.ScheduleLog;
import org.egov.pushbox.application.entity.UserDevice;
import org.egov.pushbox.application.repository.PushNotificationRepository;
import org.egov.pushbox.application.repository.ScheduleLogRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ClassPathResource;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.google.auth.oauth2.GoogleCredentials;
import com.google.firebase.FirebaseApp;
import com.google.firebase.FirebaseOptions;
import com.google.firebase.messaging.FirebaseMessaging;
import com.google.firebase.messaging.Message;
import com.google.gson.Gson;

/**
 *
 * @author Darshan Nagesh
 *
 */
@Service
@Transactional(readOnly = true)
public class PushNotificationService {
    private static final Logger LOGGER = Logger.getLogger(PushNotificationService.class);
    private static final String FCM_APP_BDURL = "https://ap-megov.firebaseio.com";
    private final PushNotificationRepository pushNotificationRepo;
    private static final String MODULE_NAME = "Eventnotification";

    @Autowired
    private FileStoreService fileStoreService;

    @Autowired
    private ScheduleLogRepository scheduleLogRepository;

    @Autowired
    public PushNotificationService(final PushNotificationRepository pushNotificationRepo) {
        this.pushNotificationRepo = pushNotificationRepo;
    }

    @Transactional
    public UserDevice persist(final UserDevice userDevice) {
        LOGGER.info("Persisting the User Device Details : " + userDevice);
        UserDevice existingRecord = pushNotificationRepo.findByUserIdAndDeviceId(userDevice.getUserId(),
                userDevice.getDeviceId());
        if (null != existingRecord) {
            existingRecord.setUserId(userDevice.getUserId());
            existingRecord.setUserDeviceToken(userDevice.getUserDeviceToken());
            existingRecord.setDeviceId(userDevice.getDeviceId());
            return existingRecord;
        }
        return pushNotificationRepo.save(userDevice);
    }

    public List<UserDevice> findAll() {
        return pushNotificationRepo.findAll();
    }

    public UserDevice findOne(final Long id) {
        return pushNotificationRepo.findOne(id);
    }

    public UserDevice findUserDeviceByUserId(final Long userId) {
        return pushNotificationRepo.findDeviceTokenByUserId(userId);
    }

    public void sendNotifications(MessageContent messageContent) {
        LOGGER.info("##PushBoxFox## : Received the Message Content at SendNotifications Method");
        List<UserDevice> userDeviceList = new ArrayList<>();
        if (messageContent.getSendAll())
            userDeviceList = getAllUserDeviceList();
        else
            userDeviceList = getUserDeviceList(messageContent);
        LOGGER.info("##PushBoxFox## : List of Devices Obtained : Size is : " + userDeviceList.size());
        setUpFirebaseApp();
        LOGGER.info("##PushBoxFox## : Sending Messages to the Devices ");
        sendMessagesToDevices(userDeviceList, messageContent);
        /*
         * LOGGER.info("##PushBoxFox## : Writing Message Log "); logMessages(userDeviceList, messageContent);
         */
    }

    private List<UserDevice> getUserDeviceList(MessageContent messageContent) {
        List<UserDevice> userDeviceList = new ArrayList<>();
        for (Long userId : messageContent.getUserIdList()) {
            LOGGER.info("#PushBoxFox## : Getting the Device Token for the User ID : " + userId);
            UserDevice device = findUserDeviceByUserId(userId);
            if (null != device)
                userDeviceList.add(device);
        }
        return userDeviceList;
    }

    private List<UserDevice> getAllUserDeviceList() {
        return pushNotificationRepo.findAll();
    }

    private void setUpFirebaseApp() {
        if (FirebaseApp.getApps().size() == 0) {
            FileInputStream serviceAccount;
            try {
                // Fetch the service account key JSON file contents
                File file = new ClassPathResource("private.json").getFile();
                serviceAccount = new FileInputStream(file);

                FirebaseOptions options = new FirebaseOptions.Builder()
                        .setCredentials(GoogleCredentials.fromStream(serviceAccount))
                        .setDatabaseUrl(FCM_APP_BDURL).build();

                FirebaseApp.initializeApp(options);
                LOGGER.info("##PushBoxFox## : Firebase App Initialized");
            } catch (FileNotFoundException e) {
                System.out.println(e.getMessage());
            } catch (IOException e) {
                System.out.println(e.getMessage());
            }
        }
    }

    private void sendMessagesToDevices(List<UserDevice> userDeviceList, MessageContent messageContent) {
        for (UserDevice userDevice : userDeviceList)
            try {
                Message message = Message.builder()
                        .putData("content", new Gson().toJson(messageContent))
                        .setToken(userDevice.getUserDeviceToken()).build();
                String response = FirebaseMessaging.getInstance().sendAsync(message).get();
                LOGGER.info("##PushBoxFox## : Message Send Status : " + response);
            } catch (Exception ex) {
                LOGGER.error("##PushBoxFox## : Error : Encountered an exception while sending the message : " + ex.getMessage());
            }
    }

    @Transactional
    public ScheduleLog insertScheduleLog(File file) {
        ScheduleLog scheduleLog = new ScheduleLog();
        uploadScheduleLogFile(scheduleLog, file);
        scheduleLog = scheduleLogRepository.save(scheduleLog);
        scheduleLogRepository.flush();
        return scheduleLog;
    }

    /**
     * This method is used to upload the file into filestore
     * @param event
     * @throws IOException
     */
    private void uploadScheduleLogFile(ScheduleLog scheduleLog, File file) {
        try {
            scheduleLog.setFilestore(
                    fileStoreService.store(new FileInputStream(file), file.getName(), "text/rtf", MODULE_NAME));
        } catch (FileNotFoundException e) {
            LOGGER.error("##PushBoxFox## : Error in upload schedule log file", e);
        }

    }

}
