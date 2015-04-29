<%@ page contentType="text/json" %>
<%@ taglib prefix="s" uri="/struts-tags" %>  
{
"ResultSet": {
    "Result":[
    <s:iterator var="s" value="overheads" status="status">  
    {"Text":"<s:property value="%{name}" />",
    "Value":"<s:property value="%{id}" />",
    "Percentage":"<s:property value="%{getOverheadRateOn(estDate).percentage}" />",
    "Lumpsum":"<s:property value="%{getOverheadRateOn(estDate).lumpsumAmount.value}" />"
    }<s:if test="!#status.last">,</s:if>
    </s:iterator>       
    ]
  }
}
