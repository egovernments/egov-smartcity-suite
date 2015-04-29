<%@ page contentType="text/json" %>
<%@ taglib prefix="s" uri="/struts-tags" %>  
{
"ResultSet": {
    "Result":[
    {"receiptAmount":"<s:property value="%{receiptAmount}" />",
    "damageFeeEstAmount":"<s:property value="%{damageFeeEstAmount}" />",
    "appDetailsId":"<s:property value="%{appDetailsId}" />"
    }       
    ]
  }
}