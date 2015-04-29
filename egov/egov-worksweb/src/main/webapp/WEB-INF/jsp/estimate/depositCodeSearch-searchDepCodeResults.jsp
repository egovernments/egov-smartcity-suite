<%@ page contentType="text/json" %>
<%@ taglib prefix="s" uri="/struts-tags" %>  
{
"ResultSet": {
    "Result":[
    <s:iterator var="s" value="estimateDepositCodeList" status="status">  
    {"key":"<s:property value="%{id}" />",
    "value":"<s:property value="%{code}" /> - <s:property value="%{codeName}" />"
    }<s:if test="!#status.last">,</s:if>
    </s:iterator>       
    ]
  }
}