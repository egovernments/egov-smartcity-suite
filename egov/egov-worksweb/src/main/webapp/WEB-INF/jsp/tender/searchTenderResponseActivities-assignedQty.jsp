<%@ page contentType="text/json" %>
<%@ taglib prefix="s" uri="/struts-tags" %>  
{
"ResultSet": {
    "Result":[
    {"assignedQty":"<s:property value="%{assignedQty}" />",
    "recordId":"<s:property value="%{recordId}" />"
	}
    ]
  }
}
