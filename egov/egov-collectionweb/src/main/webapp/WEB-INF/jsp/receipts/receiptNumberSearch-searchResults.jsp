<%@ page contentType="text/json" %>
<%@ taglib prefix="s" uri="/struts-tags" %>  
{
"ResultSet": {
    "Result":[
    <s:iterator var="s" value="receiptNumberList" status="status">  
    {"key":"<s:property value="%{receiptnumber}" />",
    "value":"<s:property value="%{receiptnumber}" />"
    }<s:if test="!#status.last">,</s:if>
    </s:iterator>       
    ]
  }
}