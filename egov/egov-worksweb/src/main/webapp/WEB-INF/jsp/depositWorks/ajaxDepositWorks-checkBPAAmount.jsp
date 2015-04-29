<%@ page contentType="text/json" %>
<%@ taglib prefix="s" uri="/struts-tags" %>  
{
"ResultSet": {
    "Result":[
    {"Value":"<s:property value="%{isBPAAmountPaid}" />",
      "appDetailsId":"<s:property value="%{appDetailsId}" />",
      "bpaNumber":"<s:property value="%{bpaNumber}" />",
      "applicationNo":"<s:property value="%{appRequestNumber}" />"
      }
    ]
  }
}