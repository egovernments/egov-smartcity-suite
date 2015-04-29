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
    		"xWorkOrderNo":"<s:property value="%{workOrderActivity.workOrderEstimate.workOrder.workOrderNumber}" />",
    		<s:if test="%{workOrderActivity.activity.revisionType.toString().equals('NON_TENDERED_ITEM')}" >
    			"xUomFactor":"<s:property value="%{workOrderActivity.activity.getConversionFactorForRE(workOrderActivity.workOrderEstimate.workOrder.parent.workOrderDate)}" />"
    		</s:if>
    		<s:else>
    			"xUomFactor":"<s:property value="%{workOrderActivity.activity.conversionFactor}" />"
    		</s:else>
    	}
    ]
  }
}
