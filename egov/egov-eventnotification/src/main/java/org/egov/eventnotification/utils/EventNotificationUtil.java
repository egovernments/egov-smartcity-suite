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
package org.egov.eventnotification.utils;

import java.util.ArrayList;
import java.util.List;

import org.egov.infra.exception.ApplicationRuntimeException;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.stereotype.Service;

@Service
public class EventNotificationUtil {

    private static final int HOURS_MAX_NUMBER_OF_REQUESTS = 24;
    private static final int MINUTES_MAX_NUMBER_OF_REQUESTS = 60;
    private static final int MAX_NUMBER_OF_REQUESTS = 10;

    @Autowired
    private ApplicationContext context;

    public List<String> getAllHour() {
        final List<String> hoursList = new ArrayList<>();
        for (int i = 0; i < HOURS_MAX_NUMBER_OF_REQUESTS; i++)
            if (i < MAX_NUMBER_OF_REQUESTS)
                hoursList.add("0" + i);
            else
                hoursList.add(String.valueOf(i));
        return hoursList;
    }

    public List<String> getAllMinute() {
        final List<String> minutesList = new ArrayList<>();
        for (int i = 0; i < MINUTES_MAX_NUMBER_OF_REQUESTS; i += 15)
            if (i < MAX_NUMBER_OF_REQUESTS)
                minutesList.add("0" + i);
            else
                minutesList.add(String.valueOf(i));
        return minutesList;
    }

    public Object getBean(final String beanName) {

        Object bean = null;
        try {
            bean = context.getBean(beanName);
        } catch (final BeansException e) {
            final String errorMsg = "Could not locate bean [" + beanName + "]";
            throw new ApplicationRuntimeException(errorMsg, e);
        }
        return bean;
    }

}