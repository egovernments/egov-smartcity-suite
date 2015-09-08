<%@ page contentType="text/json" %>
<%@ taglib prefix="s" uri="/struts-tags" %>  
{
"ResultSet": {
    "Result":[
    {
     "cumulativeBillValue":"<s:property value="%{cumulativeBillValue}" />"
    }
    ]
  }
} 
