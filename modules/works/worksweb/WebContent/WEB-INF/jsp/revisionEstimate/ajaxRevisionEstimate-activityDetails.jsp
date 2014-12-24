<%@ page contentType="text/json" %>
<%@ taglib prefix="s" uri="/struts-tags" %>  
{
"ResultSet": {
    "Result":[
    	{
    		"xActId":"<s:property value="%{workOrderActivity.id}" />",
    		"xSORCode":"<s:property value="%{workOrderActivity.activity.schedule.code}" />",
    		"xSORDesc":"<s:property value="%{workOrderActivity.activity.schedule.description}" />",
    		"xSORSummary":"<s:property value="%{workOrderActivity.activity.schedule.summary}" />",
    		"xSORUOM":"<s:property value="%{workOrderActivity.activity.schedule.uom.uom}" />",
    		"xNonSORDesc":"<s:property value="%{workOrderActivity.activity.nonSor.description}" />",
    		"xNonSORSummary":"<s:property value="%{workOrderActivity.activity.nonSor.description}" />",
    		"xNonSORUOM":"<s:property value="%{workOrderActivity.activity.nonSor.uom.uom}" />",
    		"xApprdQunty":"<s:property value="%{totalEstQuantity}" />",    		
    		"xApprdRate":"<s:property value="%{workOrderActivity.approvedRate}" />",
    		"xApprdAmt":"<s:property value="%{workOrderActivity.approvedAmount}" />",
    		"xPrevCulmvEntry":"<s:property value="%{prevCulmEntry}" />",
    		"xWorkOrderNo":"<s:property value="%{workOrderActivity.workOrder.workOrderNumber}" />",
    		"xUomFactor":"<s:property value="%{workOrderActivity.activity.conversionFactor}" />",
    		"woMsheetSize":"<s:property value="%{workOrderActivity.woMeasurementSheetList.size}" />",
    		<s:if test='workOrderActivity.activity.revisionType!=null && workOrderActivity.activity.revisionType.toString().equals("EXTRA_ITEM")'>
    			"xIsExtraItem":"true"
    		</s:if>
    		<s:else>
    			"xIsExtraItem":"false"
    		</s:else>
    	}
    ]
  }
}
