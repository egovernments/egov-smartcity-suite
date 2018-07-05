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
package org.egov.api.adapter;

import static org.egov.eventnotification.utils.Constants.ADDRESS;
import static org.egov.eventnotification.utils.Constants.CONTACT_NO;
import static org.egov.eventnotification.utils.Constants.DESCRIPTION;
import static org.egov.eventnotification.utils.Constants.DOUBLE_DEFAULT;
import static org.egov.eventnotification.utils.Constants.EVENTTYPE;
import static org.egov.eventnotification.utils.Constants.EVENT_COST;
import static org.egov.eventnotification.utils.Constants.EVENT_HOST;
import static org.egov.eventnotification.utils.Constants.EVENT_ID;
import static org.egov.eventnotification.utils.Constants.EVENT_LOC;
import static org.egov.eventnotification.utils.Constants.ISPAID;
import static org.egov.eventnotification.utils.Constants.NAME;
import static org.egov.eventnotification.utils.Constants.NO;
import static org.egov.eventnotification.utils.Constants.USER_INTERESTED;

import org.egov.eventnotification.entity.Event;

import com.google.gson.JsonObject;

public class EventDetailsAdapter {

    public void populateData(JsonObject jsonObjectEvent,Event event) {
        jsonObjectEvent.addProperty(EVENT_ID, event.getId());
        jsonObjectEvent.addProperty(NAME, event.getName());
        jsonObjectEvent.addProperty(DESCRIPTION, event.getDescription());
        
        jsonObjectEvent.addProperty(EVENT_HOST, event.getEventAddress().getEventHost());
        jsonObjectEvent.addProperty(EVENT_LOC, event.getEventAddress().getEventLocation());
        jsonObjectEvent.addProperty(ADDRESS, event.getEventAddress().getAddress());
        jsonObjectEvent.addProperty(CONTACT_NO, event.getEventAddress().getContactNumber());
        jsonObjectEvent.addProperty(ISPAID, event.isPaid());
        jsonObjectEvent.addProperty(EVENTTYPE, event.getEventType().getName());
        jsonObjectEvent.addProperty(USER_INTERESTED, NO);
        
        if (event.getCost() == null)
            jsonObjectEvent.addProperty(EVENT_COST, DOUBLE_DEFAULT);
        else
            jsonObjectEvent.addProperty(EVENT_COST, event.getCost());
    }
}
