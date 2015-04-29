<%@page contentType="text/json" %>
<%@ taglib prefix="s" uri="/struts-tags" %>  
{
"ResultSet": {
    "Result":[
    {
    "Id":"<s:property value="%{sor.id}" />",
    "Description":"<s:property value="%{sor.summaryJS}" escape="false"/>",
    "Code":"<s:property value="%{sor.code}" />",
    "FullDescription":"<s:property value="%{sor.descriptionJS}" escape="false" />",
    "UOM":"<s:property value="%{sor.uom.uom}" />",
    "UnitRate":"<s:property value="%{currentRate.rate.value}" />"
    }
    ]
  }
}