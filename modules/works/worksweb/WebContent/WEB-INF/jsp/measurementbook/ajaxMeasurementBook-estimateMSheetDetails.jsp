<%@ page contentType="text/json" %>
<%@ taglib prefix="s" uri="/struts-tags" %>  
{
"ResultSet": {
    "Result":[
    <s:iterator var="s" value="estimateMSheetDtls" status="status">
    {
    "no":"<s:property value="%{no}" />",
    "Id":"<s:property value="%{id}" />",
    "uomLength":"<s:property value="%{uomLength}" />",
    "width":"<s:property value="%{width}" />",
    "depthOrHeight":"<s:property value="%{depthOrHeight}" />",
   	"identifier":"<s:property value="%{identifier}" />",
   	"quantity":"<s:property value="%{totalQuantity}" />"
   }<s:if test="!#status.last">,</s:if> 
   </s:iterator>        
   ]
  }
}
