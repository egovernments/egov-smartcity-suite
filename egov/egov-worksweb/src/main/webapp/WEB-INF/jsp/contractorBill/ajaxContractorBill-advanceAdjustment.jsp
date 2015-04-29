<%@ page contentType="text/json" %>
<%@ taglib prefix="s" uri="/struts-tags" %>  
{
"ResultSet": {
    "Result":[
    {"totalAdvancePaid":"<s:property value="%{totalAdvancePaid}" />",
    "totalPendingBalance":"<s:property value="%{totalPendingBalance}" />"
    }
    ]
  }
} 