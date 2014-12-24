<%@ page contentType="text/json" %>
<%@ taglib prefix="s" uri="/struts-tags" %>  
{
"ResultSet": {
    "Result":[
    {"noticeNumber":"<s:property value="%{tenderNoticeNumber}" />"}    
    ]
  }
}