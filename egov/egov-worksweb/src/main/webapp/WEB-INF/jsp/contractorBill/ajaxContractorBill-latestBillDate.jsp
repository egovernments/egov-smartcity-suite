<%@ page contentType="text/json" %>
<%@ taglib prefix="s" uri="/struts-tags" %>  
{
"ResultSet": {
    "Result":[
    {"latestBillDate":"<s:property value="%{latestBillDateStr}" />"
    }    
    ]
  }
} 
