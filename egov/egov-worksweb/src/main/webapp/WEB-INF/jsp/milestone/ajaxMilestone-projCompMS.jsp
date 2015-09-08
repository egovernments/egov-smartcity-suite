<%@ page contentType="text/json" %>
<%@ taglib prefix="s" uri="/struts-tags" %>
{
"ResultSet": {
    "Result":[
    <s:iterator var="s" value="workOrdEstList" status="status">
    {"msRefNo":"<s:property value="%{msRefNo}" />"
    }<s:if test="!#status.last">,</s:if>
    </s:iterator>       
    ]
  }
}
