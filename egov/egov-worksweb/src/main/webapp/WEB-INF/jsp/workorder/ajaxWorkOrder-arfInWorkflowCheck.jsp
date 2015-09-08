<%@ page contentType="text/json" %>
<%@ taglib prefix="s" uri="/struts-tags" %>  
{
"ResultSet": {
    "Result":[
    {"Value":"<s:property value="%{arfInWorkFlowCheck}" />",
    "WOId":"<s:property value="%{workOrderId}" />",
    "EstimateNo":"<s:property value="%{estimateNo}" />",
    "ARFNo":"<s:property value="%{advanceRequisitionNo}" />",
    "Owner":"<s:property value="%{owner}" />"}
    ]
  }
}
