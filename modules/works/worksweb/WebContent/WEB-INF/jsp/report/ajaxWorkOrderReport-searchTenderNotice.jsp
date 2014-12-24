<%@ page contentType="text/json" %> 
<%@ taglib prefix="s" uri="/struts-tags" %>  
{
"ResultSet": {
    "Result":[
    <s:iterator var="s" value="tenderNoticeList" status="status">  
    {"key":"<s:property value="%{id}" />",
    "value":"<s:property value="%{tenderNumber}" />" 
    }<s:if test="!#status.last">,</s:if> 
    </s:iterator>       
    ]
  }
}