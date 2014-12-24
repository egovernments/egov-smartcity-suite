<%@ page contentType="text/json" %>
<%@ taglib prefix="s" uri="/struts-tags" %>  
{
"ResultSet": {
    "Result":[
    {"totalPendingBalance":"<s:property value="%{totalPendingBalance}" />"
    }
    ]
  }
} 