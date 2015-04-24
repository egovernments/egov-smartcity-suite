<%@ page contentType="text/json" %>
<%@ taglib prefix="s" uri="/struts-tags" %>  
{
"ResultSet": {
    "Result":[
    <s:iterator var="s" value="manualReceiptNumberList" status="status">  
    {"key":"<s:property value="%{manualreceiptnumber}" />",
    "value":"<s:property value="%{manualreceiptnumber}" />"
    }<s:if test="!#status.last">,</s:if>
    </s:iterator>       
    ]
  }
}