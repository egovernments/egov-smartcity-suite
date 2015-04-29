<%@ page contentType="text/json" %>
<%@ taglib prefix="s" uri="/struts-tags" %>  
{
"ResultSet": {
    "Result":[
    {"wpNumber":"<s:property value="%{wpNumber}" />",
    "isVoucherExists":"<s:property value="%{isVoucherExists}" />",
    "yearEndApprOwner":"<s:property value="%{yearEndApprOwner}" />",
    "woNumber":"<s:property value="%{woNumber}" />"} 
    ]
  }
}