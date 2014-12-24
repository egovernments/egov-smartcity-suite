<%@ page contentType="text/json" %>
<%@ taglib prefix="s" uri="/struts-tags" %>   
{
"ResultSet": {
    "Result":[
    <s:iterator var="s" value="sampleLetterList" status="status">  
    {"key":"<s:property value="%{id}" />",
     <s:if test="%{mode=='sampleLetter'}"> 
    "value":"<s:property value="%{sampleLetterNumber}" />"  
    </s:if>
     <s:if test="%{mode=='coveringLetter'}"> 
    "value":"<s:property value="%{coveringLetterNumber}" />"  
    </s:if>
    }<s:if test="!#status.last">,</s:if> 
    </s:iterator>       
    ]
  }
}