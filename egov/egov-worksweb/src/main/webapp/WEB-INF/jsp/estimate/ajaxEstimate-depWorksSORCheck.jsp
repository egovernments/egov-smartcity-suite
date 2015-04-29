<%@ page contentType="text/json" %>
<%@ taglib prefix="s" uri="/struts-tags" %>  
{
"ResultSet": {
    "Result":[
    {"Value":"<s:property value="%{dwSORCheck}" />",
     "sorCodes":"<s:property value="%{sorCodes}" />"}
    ]
  }
}