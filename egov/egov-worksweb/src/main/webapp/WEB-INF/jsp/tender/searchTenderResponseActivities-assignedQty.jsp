<%@ page contentType="text/json" %>
<%@ taglib prefix="s" uri="/WEB-INF/struts-tags.tld" %> 
{
"ResultSet": {
    "Result":[
    {"assignedQty":"<s:property value="%{assignedQty}" />",
    "recordId":"<s:property value="%{recordId}" />"
	}
    ]
  }
}
