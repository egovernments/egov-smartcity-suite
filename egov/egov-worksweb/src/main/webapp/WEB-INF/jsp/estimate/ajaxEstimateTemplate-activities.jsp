<%@ page contentType="text/json" %>
<%@ taglib prefix="s" uri="/struts-tags" %>  
{
"ResultSet": {
    "Result":[
    <s:iterator var="s" value="estimateTemplate.activities" status="status">
    {<s:if test="%{schedule!=null}">
    "sor":"yes",
    "Id":"<s:property value="%{schedule.id}" />",
    "Description":"<s:property value="%{schedule.summary}" />",
    "Code":"<s:property value="%{schedule.code}" />",
    "FullDescription":"<s:property value="%{schedule.description}" />",
    "UOM":"<s:property value="%{schedule.uom.uom}" />",
    "UnitRate":"<s:if test="%{schedule.getRateOn(estimateDate)!=null}"><s:property value="%{schedule.getRateOn(estimateDate).rate.value}" /></s:if><s:else>0.0</s:else>"
    </s:if>
   	<s:else>
   	"sor":"no",
   	"Id":"<s:property value="nonSor.id"/>",
   	"Description":"<s:property value="nonSor.descriptionJS" escape="false"/>",
   	"UOM":"<s:property value="nonSor.uom.id"/>",
   	"UnitRate":"<s:property value="rate"/>",
   	"Code":"",
   	"FullDescription":""
    </s:else> 
   } <s:if test="!#status.last">,</s:if>
    </s:iterator>       
    ]
  }
}
