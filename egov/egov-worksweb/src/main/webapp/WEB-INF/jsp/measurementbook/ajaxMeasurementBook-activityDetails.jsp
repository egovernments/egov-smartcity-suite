<%@ page contentType="text/json" %>
<%@ taglib prefix="s" uri="/struts-tags" %>  
{
"ResultSet": {
    "Result":[
    	{
    		"xActId":"<s:property value="%{workOrderActivity.id}" />",
    		"xSORDesc":"<s:property value="%{workOrderActivity.activity.schedule.descriptionJS}"  escape="false"/>",
    		"xSORSummary":"<s:property value="%{workOrderActivity.activity.schedule.summaryJS}" escape="false"/>",
    		"xSORUOM":"<s:property value="%{workOrderActivity.activity.schedule.uom.uom}" />",
    		"xNonSORDesc":"<s:property value="%{workOrderActivity.activity.nonSor.descriptionJS}" escape="false" />",
    		"xNonSORSummary":"<s:property value="%{workOrderActivity.activity.nonSor.descriptionJS}" escape="false" />",
    		"xNonSORUOM":"<s:property value="%{workOrderActivity.activity.nonSor.uom.uom}" />",
    		"xApprdQunty":"<s:property value="%{totalEstQuantity}" />",
    		"xApprdRate":"<s:property value="%{workOrderActivity.approvedRate}" />",
    		"xPrevCulmvEntry":"<s:property value="%{prevCulmEntry}" />",
    		"xApprdAmt":"<s:property value="%{workOrderActivity.approvedAmount}" />",
    		"xWorkOrderNo":"<s:property value="%{workOrderActivity.workOrder.workOrderNumber}" />",
    		"xUomFactor":"<s:property value="%{workOrderActivity.conversionFactor}" />",
    		"activityRemarks":"<s:property value="%{activityRemarks}" />"
    	}
    ]
  }
}
