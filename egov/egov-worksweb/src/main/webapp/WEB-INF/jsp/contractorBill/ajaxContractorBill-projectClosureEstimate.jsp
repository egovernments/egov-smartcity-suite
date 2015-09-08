<%@ page contentType="text/json" %>
<%@ taglib prefix="s" uri="/struts-tags" %>  
{
"ResultSet": {
    "Result":[
    {"estimateNumber":"<s:property value="%{estimateNumber}" />"}  
    ]
  }
}
