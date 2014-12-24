<%@page contentType="text/json" %>
<%@ taglib prefix="s" uri="/struts-tags" %>  
{
"ResultSet": {
    "Result":[
    {
    "Id":"<s:property value="%{rcDetail.indentDetail.nonSor.id}" />",
    "Description":"<s:property value="rcDetail.indentDetail.nonSor.descriptionJS" escape="false"/>",
    "UOM":"<s:property value="%{rcDetail.indentDetail.nonSor.uom.id}" />",
    "UnitRate":"<s:property value="%{rcDetail.rcRate.value}" />",
    "ContractorCode":"<s:property value="%{rcDetail.rateContract.contractor.code}" />", 
    "RcNumber":"<s:property value="%{rcDetail.rateContract.rcNumber}" />",
    "RcId":"<s:property value="%{rcDetail.rateContract.id}" />"
    }
    ]
  }
}