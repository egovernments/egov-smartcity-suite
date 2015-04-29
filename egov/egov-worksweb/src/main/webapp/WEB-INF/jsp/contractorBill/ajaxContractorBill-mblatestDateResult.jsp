<%@ page contentType="text/json" %>
<%@ taglib prefix="s" uri="/struts-tags" %>  
{
"ResultSet": {
    "Result":[
    {"mbDate":"<s:date name="latestMBDate" format="dd/MM/yyyy"/>",
    "refno":"<s:property value="%{refNo}" />"
    }
    ]
  }
}