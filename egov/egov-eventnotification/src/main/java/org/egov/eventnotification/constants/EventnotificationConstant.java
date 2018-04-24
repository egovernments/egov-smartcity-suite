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

    public static enum EVENT_TYPE {
        Business, Political, Exhibition, Cultural, Drama
    };
}
