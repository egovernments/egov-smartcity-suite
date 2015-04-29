<%@ page contentType="text/json" %>
<%@ taglib prefix="s" uri="/struts-tags" %>  
{
"ResultSet": {
    "Result":[
    <s:iterator var="s" value="rateContractNoList" status="status">  
    {"value":"<s:property value="%{rcNumber}"/>"
    }<s:if test="!#status.last">,</s:if>
    </s:iterator>       
    ]
  }
}