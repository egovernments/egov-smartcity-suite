<%@ page contentType="text/json" %>
<%@ taglib prefix="s" uri="/WEB-INF/struts-tags.tld" %> 
{
"ResultSet": {
    "Result":[
    <s:iterator var="s" value="approvedBidResponseList" status="status">
    {"bidNo":"<s:property value="%{number}" />"
    }<s:if test="!#status.last">,</s:if>
    </s:iterator>       
    ]
  }
}