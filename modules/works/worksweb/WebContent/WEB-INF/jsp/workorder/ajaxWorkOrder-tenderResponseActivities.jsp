<%@ page contentType="text/json" %>
<%@ taglib prefix="s" uri="/WEB-INF/struts-tags.tld" %> 
{
"ResultSet": {
    "Result":[
    <s:iterator var="s" value="tenderResponseActivitylist" status="status">
    {"tenderresponseactivityid":"<s:property value="%{tenderResponseLine.id}" />",
     "activityId":"<s:property value="%{activity.id}" />",
     "estimateno":"<s:property value="%{activity.abstractEstimate.estimateNumber}" />",
     "estimateqty":"<s:property value="%{activity.quantity}" />",
   		<s:if test="%{activity.schedule!=null}">
   	 		"schno":"<s:property value="activity.schedule.code"/>",
      		"description":"<s:property value="activity.schedule.summary"/>",
     		 "uom":"<s:property value="activity.schedule.uom.uom"/>",
      	</s:if>
   		<s:else>
   			"schno":"",
    		"description":"<s:property value="activity.nonSor.description"/>",
      		"uom":"<s:property value="activity.nonSor.uom.uom"/>",
    	</s:else> 
    "negotiatedRate":"<s:property value="%{negotiatedRate}" />",
    "negotiatedQty":"<s:property value="%{negotiatedQuantity}" />",
    "uomfactor":"<s:property value="activity.conversionFactor"/>",
    "estimateMSheetLength":"<s:property value="activity.measurementSheetList.size"/>"
   	}<s:if test="!#status.last">,</s:if>
    </s:iterator>       
    ]
  }
}
