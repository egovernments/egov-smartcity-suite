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
package org.egov.eventnotification.service;

import static org.egov.eventnotification.utils.Constants.DDMMYYYY;
import static org.egov.eventnotification.utils.Constants.MAX_TEN;
import static org.egov.eventnotification.utils.Constants.ZERO;
import static org.egov.infra.utils.DateUtils.getDate;
import static org.egov.infra.utils.DateUtils.getDefaultFormattedDate;
import static org.egov.infra.utils.DateUtils.startOfGivenDate;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.egov.eventnotification.entity.Drafts;
import org.egov.eventnotification.entity.Schedule;
import org.egov.eventnotification.entity.contracts.EventDetails;
import org.egov.eventnotification.entity.contracts.EventNotificationProperties;
import org.egov.eventnotification.entity.contracts.TaxDefaulterRequest;
import org.egov.eventnotification.entity.contracts.TaxDefaulterResponse;
import org.egov.eventnotification.entity.contracts.UserTaxInformation;
import org.egov.eventnotification.repository.DraftRepository;
import org.egov.eventnotification.repository.ScheduleRepository;
import org.joda.time.DateTime;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.client.RestTemplate;

@Service
@Transactional(readOnly = true)
public class ScheduleService {

    @Autowired
    private ScheduleRepository scheduleRepository;

    @Autowired
    private RestTemplate restTemplate;

    @Autowired
    private ScheduleDetailsService scheduleDetailsService;

    @Autowired
    private DraftRepository draftRepository;

    @Autowired
    private EventNotificationProperties appProperties;

    public List<Schedule> getAllSchedule() {
        List<Schedule> notificationScheduleList = null;
        notificationScheduleList = scheduleRepository.findByOrderByIdDesc();
        if (!notificationScheduleList.isEmpty())
            for (Schedule notificationSchedule : notificationScheduleList) {
                EventDetails details = new EventDetails();
                DateTime sd = new DateTime(notificationSchedule.getStartDate());
                details.setStartDt(
                        getDate(getDefaultFormattedDate(notificationSchedule.getStartDate()),
                                DDMMYYYY));
                if (sd.getHourOfDay() < MAX_TEN)
                    details.setStartHH(ZERO + String.valueOf(sd.getHourOfDay()));
                else
                    details.setStartHH(String.valueOf(sd.getHourOfDay()));
                if (sd.getMinuteOfHour() < MAX_TEN)
                    details.setStartMM(ZERO + String.valueOf(sd.getMinuteOfHour()));
                else
                    details.setStartMM(String.valueOf(sd.getMinuteOfHour()));
                notificationSchedule.setDetails(details);
            }
        return notificationScheduleList;
    }

    public Schedule getScheduleById(Long id) {
        Schedule schedule = scheduleRepository.findOne(id);
        EventDetails details = new EventDetails();
        DateTime sd = new DateTime(schedule.getStartDate());
        details
                .setStartDt(getDate(getDefaultFormattedDate(schedule.getStartDate()),
                        DDMMYYYY));
        if (sd.getHourOfDay() < MAX_TEN)
            details.setStartHH(ZERO + String.valueOf(sd.getHourOfDay()));
        else
            details.setStartHH(String.valueOf(sd.getHourOfDay()));
        if (sd.getMinuteOfHour() < MAX_TEN)
            details.setStartMM(ZERO + String.valueOf(sd.getMinuteOfHour()));
        else
            details.setStartMM(String.valueOf(sd.getMinuteOfHour()));
        schedule.setDetails(details);
        return schedule;
    }

    @Transactional
    public Schedule saveSchedule(Schedule notificationSchedule, String fullURL) {
        DateTime sd = startOfGivenDate(new DateTime(notificationSchedule.getDetails().getStartDt()))
                .withHourOfDay(Integer.parseInt(notificationSchedule.getDetails().getStartHH()))
                .withMinuteOfHour(Integer.parseInt(notificationSchedule.getDetails().getStartMM()));
        sd = sd.withSecondOfMinute(00);
        Drafts draft = draftRepository.findOne(notificationSchedule.getDetails().getDraftId());
        notificationSchedule.setStartDate(sd.toDate());
        notificationSchedule.setStatus("scheduled");
        notificationSchedule.setUrl(draft.getUrl());
        notificationSchedule.setMethod(draft.getMethod());
        scheduleRepository.save(notificationSchedule);

        scheduleDetailsService.executeScheduler(notificationSchedule, fullURL);

        return notificationSchedule;
    }

    @Transactional
    public Schedule updateSchedule(Schedule schedule) {
        DateTime sd = startOfGivenDate(new DateTime(schedule.getDetails().getStartDt()))
                .withHourOfDay(Integer.parseInt(schedule.getDetails().getStartHH()))
                .withMinuteOfHour(Integer.parseInt(schedule.getDetails().getStartMM()));
        schedule.setStartDate(sd.toDate());
        schedule.setStatus("scheduled");
        scheduleRepository.save(schedule);

        scheduleDetailsService.modifyScheduler(schedule);

        return schedule;
    }

    @Transactional
    public Schedule disableSchedule(Long id) {
        Schedule notificationSchedule = getScheduleById(id);
        notificationSchedule.setStatus("Disabled");

        scheduleRepository.save(notificationSchedule);
        scheduleDetailsService.removeScheduler(notificationSchedule);
        return notificationSchedule;
    }

    @Transactional
    public synchronized Schedule updateScheduleStatus(Schedule schedule) {
        return scheduleRepository.saveAndFlush(schedule);
    }

    /**
     * This method build the URL and call the rest API using the restTemplate and return an object.
     * @param contextURL
     * @param urlPath
     * @return
     */
    public List<UserTaxInformation> getDefaulterUserList(String url, String method, String ulbCode) {
        ResponseEntity<UserTaxInformation[]> results = null;
        HttpHeaders headers = new HttpHeaders();
        headers.set("Referer", appProperties.getRefererIp());
        HttpEntity<String> entity = new HttpEntity<>("parameters", headers);
        if (method.equals("GET")) {
            results = restTemplate.getForEntity(url, UserTaxInformation[].class, entity);
            return Arrays.asList(results.getBody());
        } else {
            ResponseEntity<TaxDefaulterResponse> response = null;
            int page = 1;
            List<UserTaxInformation> uerTaxInformationList = new ArrayList<>();
            do {
                TaxDefaulterRequest req = new TaxDefaulterRequest();
                req.setMobileOnly(true);
                req.setUlbCode(ulbCode);
                req.setPage(page);
                req.setPageSize(1000);
                HttpEntity<TaxDefaulterRequest> request = new HttpEntity<>(req, headers);
                response = restTemplate.postForEntity(url, request, TaxDefaulterResponse.class);
                uerTaxInformationList.addAll(response.getBody().getDefaultersResultHolderList());
                page++;
            } while (response.getBody().isHasNext());
            return uerTaxInformationList;

        }
    }
}
