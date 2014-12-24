<%@ page contentType="text/json" %>
<%@ taglib prefix="s" uri="/struts-tags" %>  
{
"ResultSet": {
    "Result":[
    <s:iterator var="s" value="mbMSheetDtls" status="status">
    {
    "Id":"<s:property value="%{woMeasurementSheet.id}" />",
    "estimateNo":"<s:property value="%{woMeasurementSheet.measurementSheet.activity.abstractEstimate.estimateNumber}" />",
     <s:if test="%{woMeasurementSheet.measurementSheet.activity.revisionType!=null}">
    	"revisionType":"<s:property value="%{woMeasurementSheet.measurementSheet.activity.revisionType}" />",  
    </s:if>
     <s:else>
   	 	"revisionType":"",
   	 </s:else> 
    <s:if test="%{woMeasurementSheet.measurementSheet.remarks!=null}">
   		 "description":"<s:property value="%{woMeasurementSheet.measurementSheet.remarks}" />",
   	 </s:if>
   	 <s:else>
   	 	"description":"",
   	 </s:else>
     <s:if test="%{woMeasurementSheet.no!=null}">
   		 "no":"<s:property value="%{woMeasurementSheet.no}" />",
   	 </s:if>
   	 <s:else>
   	 	"no":"",
   	 </s:else>
    <s:if test="%{woMeasurementSheet.length!=0.0}">
   		 "uomLength":"<s:property value="%{woMeasurementSheet.length}" />",
   	 </s:if>
   	 <s:else>
   	 	"uomLength":"",
   	 </s:else>
    <s:if test="%{woMeasurementSheet.width!=0.0}">
   		 "width":"<s:property value="%{woMeasurementSheet.width}" />",
   	 </s:if>
   	 <s:else>
   	 	"width":"",
   	 </s:else>
   	 <s:if test="%{woMeasurementSheet.width!=0.0}">
   		 "width":"<s:property value="%{woMeasurementSheet.width}" />",
   	 </s:if>
   	 <s:else>
   	 	"width":"",
   	 </s:else>
    <s:if test="%{woMeasurementSheet.depthOrHeight!=0.0}">
   		 "depthOrHeight":"<s:property value="%{woMeasurementSheet.depthOrHeight}" />",
   	 </s:if>
   	 <s:else>
   	 	"depthOrHeight":"",
   	 </s:else>
    <s:if test="%{woMeasurementSheet.measurementSheet.identifier!=0.0}">
   		 "identifier":"<s:property value="%{woMeasurementSheet.measurementSheet.identifier}" />",
   	 </s:if>
   	 <s:else>
   	 	"identifier":"",
   	 </s:else>
   	 "uom":"<s:property value="%{woMeasurementSheet.woActivity.activity.uom.uom}" />",
   	 "quantity":"<s:property value="%{woMeasurementSheet.quantity}" />",
 	 <s:if test="%{no!=null}">
   		 "curNo":"<s:property value="%{no}" />",
   	 </s:if>
   	 <s:else>
   	 	"curNo":"",
   	 </s:else>
   	<s:if test="%{uomLength!=0.0}">
   		 "curLength":"<s:property value="%{uomLength}" />",
   	 </s:if>
   	 <s:else>
   	 	"curLength":"",
   	 </s:else>
   	 <s:if test="%{width!=0.0}">
   		 "curWidth":"<s:property value="%{width}" />",
   	 </s:if>
   	 <s:else>
   	 	"curWidth":"",
   	 </s:else>
   	 <s:if test="%{depthOrHeight!=0.0}">
   		 "curDH":"<s:property value="%{depthOrHeight}" />",
   	 </s:if>
   	 <s:else>
   	 	"curDH":"",
   	 </s:else>
   	<s:if test="%{quantity!=0.0}">
   		 "curQuantity":"<s:property value="%{quantity}" />", 
   	 </s:if>
   	 <s:else>
   	 	"curQuantity":"",
   	 </s:else>
  
   	 <s:if test="%{cumulativeQuantity!=0.0}">
   		 "curCumQuantity":"<s:property value="%{cumulativeQuantity}" />"
   	 </s:if>
   	 <s:else>
   	 	"curCumQuantity":""
   	 </s:else>
   }<s:if test="!#status.last">,</s:if> 
   </s:iterator>        
   ]
  }
}
