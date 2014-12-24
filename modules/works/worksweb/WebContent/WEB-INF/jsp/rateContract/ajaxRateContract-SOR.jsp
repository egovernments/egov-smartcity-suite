<%@page contentType="text/json" %>
<%@ taglib prefix="s" uri="/struts-tags" %>  
{
"ResultSet": {
    "Result":[
    {
    "Id":"<s:property value="%{rcDetail.indentDetail.scheduleOfRate.id}" />",
    "Description":"<s:property value="%{rcDetail.indentDetail.scheduleOfRate.summary}" />",
    "Code":"<s:property value="%{rcDetail.indentDetail.scheduleOfRate.code}" />",
    "FullDescription":"<s:property value="%{rcDetail.indentDetail.scheduleOfRate.description}" />",
    "UOM":"<s:property value="%{rcDetail.indentDetail.scheduleOfRate.uom.uom}" />",
    "UnitRate":"<s:property value="%{rcDetail.rcRate.value}" />",
    "ContractorCode":"<s:property value="%{rcDetail.rateContract.contractor.code}" />",
    "RcNumber":"<s:property value="%{rcDetail.rateContract.rcNumber}" />",
    "RcId":"<s:property value="%{rcDetail.rateContract.id}" />" 
    }
    ]
  }
}