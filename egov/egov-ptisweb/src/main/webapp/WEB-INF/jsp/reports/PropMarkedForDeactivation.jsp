<%@ page contentType="text/html; charset=UTF-8" %>
<%@ include file="../includes/taglibs.jsp" %>
<%@ page import= "java.util.*,org.egov.ptis.property.client.reports.coc.*"%>
					
<html>
  <head>
    <meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
    <title>Report - List Of Properties Marked For Deactivation</title>
 	<link href="<c:url value='/css/propertytax.css'/>" rel="stylesheet" type="text/css" />
	<link href="<c:url value='/css/commonegov.css'/>" rel="stylesheet" type="text/css" />
	<script type="text/javascript" src="../commonjs/ajaxCommonFunctions.js"></script>
 

 <script type="text/javascript">
function checkBeforeSubmit()
 {
 	var zoneNo = document.propMarkedDeactiveForm.zoneNumber.value;
 	var WardNo = document.propMarkedDeactiveForm.divNumber.value;
 	if(zoneNo==null || zoneNo=="" || zoneNo=="0")
   		{
   		alert("Please Select a Zone Number");
   		return false;
   		}
   	if(WardNo==null || WardNo=="" || WardNo=="0")
   		{
   		alert("Please Select a Ward Number");
   		return false;
   		}
 }
 </script>
  </head>
  
<html:form action="/reports/BeforeListOfPropMarkedForDeactivationAction.do">
  <body>
<div class="formmainbox"><div class="formheading"></div>
  <div class="headingbg"><bean:message key="listOfPropMarkDeactive" ></bean:message></div>
<table width="100%" border="0" cellspacing="0" cellpadding="0">

  <tr>
    <td class="greybox" width="10%">&nbsp;</td>
    <td class="greybox" width="8%"><bean:message key="zonenum"/>:<span class="mandatory">*</span></td>
    <td class="greybox">
    <html:select property="zoneNumber" styleId = "zoneNumber" onchange = "loadSelectData('../commonyui/egov/loadComboAjax.jsp', 'eg_boundary', 'ID_BNDRY', 'name', 'parent=#1 order by name', 'zoneNumber', 'divNumber');" styleClass="selectnew">
    	<html:option value="0">--Choose--</html:option>
    	<html:options collection="ZoneBoundary" property="id" labelProperty="name" />	          
	</html:select>
    </td>
    <td class="greybox" width="8%"><bean:message key="divNum"/>:<span class="mandatory">*</span></td>
    <td class="greybox"><label>
      <select class="selectnew" name="divNumber" id="divNumber">
		<option  value = "0"><bean:message key="choose" /></option>  	          
      </select>
      </label></td>
  </tr>

</table>
</div>

<font size="1"><div align="left" class="mandatory"><bean:message key="mandtryFlds"/></div></font>
 
<div class="buttonbottom" align="center">
<input type="button" name="button2" id="button2" value="Close" class="button" onclick="window.close();"/>
 <input type="submit" name="button3" id="button3" value="List" class="buttonsubmit" onclick = "return checkBeforeSubmit();"/>
 </div>
 
 
</body>
</html:form>
</html>
