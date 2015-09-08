<%@ page contentType="text/json" %>
<%@ taglib prefix="s" uri="/struts-tags" %>  
{
"ResultSet": {
    "Result":[
    {"MarketRate":"<s:property value="%{marketRateValues}" />",
	"ScheduleId":"<s:property value="%{scheduleIds}" />"
    }
    ]
  }

} 
