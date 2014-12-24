<%@ page contentType="text/json" %>
<%@ taglib prefix="s" uri="/struts-tags" %>  
{
"ResultSet": {
    "Result":[
    {
     "totalWithHoldAmount":"<s:property value="%{totalWithHoldAmount}" />",
     "recordId":"<s:property value="%{recordId}" />"
    }
   ]
  }
} 