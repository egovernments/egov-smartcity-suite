<%@ page contentType="text/json" %>
<%@ taglib prefix="s" uri="/struts-tags" %>  
{
"ResultSet": {
    "Result":[
    <s:iterator var="s" value="projectCodeList" status="status">  
    {"value":"<s:property/>"
    }<s:if test="!#status.last">,</s:if>
    </s:iterator>       
    ]
  }
}
