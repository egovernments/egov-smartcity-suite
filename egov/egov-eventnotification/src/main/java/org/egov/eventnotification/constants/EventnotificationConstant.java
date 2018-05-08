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
package org.egov.eventnotification.constants;

public class EventnotificationConstant {
    public static final String MODULE_NAME = "Eventnotification";

    public static final String EVENT_ID = "id";
    public static final String EVENT_NAME = "name";
    public static final String EVENT_DESC = "description";
    public static final String EVENT_STARTDATE = "startDate";
    public static final String EVENT_STARTTIME = "startTime";
    public static final String EVENT_ENDDATE = "endDate";
    public static final String EVENT_ENDTIME = "endTime";
    public static final String EVENT_HOST = "eventhost";
    public static final String EVENT_LOCATION = "eventlocation";
    public static final String EVENT_ADDRESS = "address";
    public static final String EVENT_ISPAID = "ispaid";
    public static final String EVENT_EVENTTYPE = "eventType";
    public static final String EVENT_FILESTOREID = "fileStoreId";
    public static final String EVENT_FILENAME = "fileName";
    public static final String EVENT_COST = "cost";

    public static final String DDMMYYYY = "dd/MM/yyyy";

    public static final String EVENT_EVENTNAME = "eventName";
    public static final String EVENT_EVENTHOST = "eventHost";

    public static final String EVENT_LIST = "eventList";
    public static final String EVENT_TYPE_LIST = "eventTypeList";
    public static final String MODE = "mode";

    public static final String MODE_VIEW = "view";
    public static final String MODE_UPDATE = "update";
    public static final String MODE_CREATE = "create";

    public static final String EVENT = "event";
    public static final String FILE = "file";

    public static final String HOUR_LIST = "hourList";
    public static final String MINUTE_LIST = "minuteList";

    public static final String MESSAGE = "message";

    public static final String MSG_EVENT_CREATE_SUCCESS = "msg.event.create.success";
    public static final String MSG_EVENT_UPDATE_SUCCESS = "msg.event.update.success";

    public static final String API_UPDATE_ID = "update/{id}";
    public static final String API_CREATE = "create/";
    public static final String API_VIEW = "view/";
    public static final String API_VIEW_ID = "view/{id}";
    public static final String API_EVENT = "/event/";

    public static final String VIEW_EVENTVIEW = "event-view";
    public static final String VIEW_EVENTVIEWRESULT = "event-view-result";
    public static final String VIEW_EVENTCREATE = "event-create";
    public static final String VIEW_EVENTSUCCESS = "event-success";
    public static final String VIEW_EVENTUPDATE = "event-update";
    public static final String VIEW_EVENTUPDATESUCCESS = "event-update-success";

    public static final String PAGE = "page";
    public static final String SIZE = "size";
    public static final String TOTAL_PAGES = "totalPages";

    public static final String ACTIVE = "Active";
    public static final String INACTIVE = "Inactive";
    public static final String EVENT_STATUS_LIST = "eventStatusList";
    public static final String STATUS = "Status";
    public static final String USERID = "userid";
    public static final String EVENTID = "eventid";
    public static final String SUCCESS = "success";
    public static final String FAIL = "fail";
    public static final String STATUS_COLUMN = "status";
    public static final String INTERESTED_COUNT = "interestedCount";
    public static final String URL = "url";
    public static final String EVENT_DATE_TYPE = "eventDateType";
    public static final String UPCOMING = "upcoming";
    public static final String ONGOING = "ongoing";
    public static final String NOTIFICATION_TYPE_EVENT = "Event";
    public static final String USER_INTERESTED = "userInterested";
    
    public static final String SCHEDULE_LIST = "scheduleList";
    public static final String DRAFT_LIST = "draftList";
    public static final String SCHEDULE_VIEW = "schedule-view";
    public static final String SCHEDULED_STATUS = "scheduled";
    public static final String SCHEDULE_MONTH = "month";
    public static final String SCHEDULE_DAY = "day";
    public static final String SCHEDULE_YEAR= "year";
    public static final String NOTIFICATION_JOB = "notificationJob";
    public static final String TO_BE_SCHEDULED = "To be Scheduled";
    public static final String SCHEDULE = "Schedule";
    public static final String NOTIFICATION_SCHEDULE = "notificationSchedule";
    public static final String SCHEDULE_CREATE_VIEW = "schedule-create-view";
    public static final String SCHEDULER_REPEAT_LIST = "repeatList";
    public static final String SCHEDULE_CREATE_SUCCESS = "schedule-create-success";
    public static final String USER = "user";
    public static final String SCHEDULEID = "scheduleId";
    public static final String NO = "No";
    public static final String YES = "Yes";
    
    public static final String MSG_NOTIFICATION_SCHEDULED_SUCCESS = "msg.notification.schedule.success";

    public static enum EVENT_TYPE {
        Business, Exhibition, Cultural, Drama
    };

    public static enum EVENT_STATUS {
        Active, Inactive
    };
    
    public static enum SCHEDULER_REPEAT {
        Month, Day, Year
    };
}
