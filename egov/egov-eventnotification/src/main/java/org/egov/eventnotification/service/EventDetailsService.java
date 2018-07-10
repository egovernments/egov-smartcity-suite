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
import static org.egov.eventnotification.utils.Constants.MODULE_NAME;
import static org.egov.eventnotification.utils.Constants.ZERO;
import static org.egov.infra.utils.DateUtils.getDate;
import static org.egov.infra.utils.DateUtils.getDefaultFormattedDate;

import java.io.IOException;

import org.egov.eventnotification.entity.Event;
import org.egov.eventnotification.entity.contracts.EventDetails;
import org.egov.infra.exception.ApplicationRuntimeException;
import org.egov.infra.filestore.service.FileStoreService;
import org.joda.time.DateTime;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

@Service
@Transactional(readOnly = true)
public class EventDetailsService {

    private static final Logger LOGGER = LoggerFactory.getLogger(EventDetailsService.class);
    @Autowired
    private FileStoreService fileStoreService;

    protected void populateEventDetails(Event event) {
        EventDetails details = new EventDetails();
        DateTime sd = new DateTime(event.getStartDate());
        details.setStartDt(getDate(getDefaultFormattedDate(event.getStartDate()), DDMMYYYY));
        if (sd.getHourOfDay() < MAX_TEN)
            details.setStartHH(ZERO + String.valueOf(sd.getHourOfDay()));
        else
            details.setStartHH(String.valueOf(sd.getHourOfDay()));
        if (sd.getMinuteOfHour() < MAX_TEN)
            details.setStartMM(ZERO + String.valueOf(sd.getMinuteOfHour()));
        else
            details.setStartMM(String.valueOf(sd.getMinuteOfHour()));

        DateTime ed = new DateTime(event.getEndDate());
        details.setEndDt(getDate(getDefaultFormattedDate(event.getEndDate()), DDMMYYYY));
        if (ed.getHourOfDay() < MAX_TEN)
            details.setEndHH(ZERO + String.valueOf(ed.getHourOfDay()));
        else
            details.setEndHH(String.valueOf(ed.getHourOfDay()));
        if (ed.getMinuteOfHour() < MAX_TEN)
            details.setEndMM(ZERO + String.valueOf(ed.getMinuteOfHour()));
        else
            details.setEndMM(String.valueOf(ed.getMinuteOfHour()));

        if (event.isPaid())
            details.setPaid("Yes");
        else
            details.setPaid("No");
        event.setDetails(details);
    }

    public void eventUploadWallpaper(Event event) {
        try {
            for (MultipartFile multipartFile : event.getDetails().getFile())
                if (!multipartFile.isEmpty())
                    event.setFilestore(
                            fileStoreService.store(multipartFile.getInputStream(), multipartFile.getOriginalFilename(),
                                    multipartFile.getContentType(), MODULE_NAME));
        } catch (final IOException e) {
            LOGGER.error("Error : Encountered an exception while upload an event image", e);
            throw new ApplicationRuntimeException(e.getMessage(), e);
        }
    }

    public void eventUploadWallpaper(Event existingEvent, Event event) {
        try {
            for (MultipartFile multipartFile : event.getDetails().getFile())
                if (!multipartFile.isEmpty())
                    existingEvent.setFilestore(
                            fileStoreService.store(multipartFile.getInputStream(), multipartFile.getOriginalFilename(),
                                    multipartFile.getContentType(), MODULE_NAME));
        } catch (final IOException e) {
            LOGGER.error("Error : Encountered an exception while upload an event image", e);
            throw new ApplicationRuntimeException(e.getMessage(), e);
        }

    }
}
