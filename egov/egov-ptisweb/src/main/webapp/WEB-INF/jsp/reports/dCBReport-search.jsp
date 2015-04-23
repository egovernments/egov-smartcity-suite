<%@ page language="java" pageEncoding="UTF-8"%>
<%@ include file="/includes/taglibs.jsp" %>

<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />

<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN">
<html>
  <head>  	
    <title><s:text name='dcbreport.search'/></title>

<script type="text/javascript">
 function generateDCBReport() 
 { 
	 var zoneid = document.getElementById("zoneId").value;
	 if(zoneid=="-1")
	  { 
		 alert("Please Select Zone");
		 return false;
	  } 
	 win1 =window.open('../reports/dCBReport!searchForm.action?zoneId='+zoneid,'window','scrollbars=yes,resizable=yes,height=700,width=800,status=yes');
 }
</script>
</head>
<body>
  
  <s:form name="dcbForm" action="dCBReport" theme="simple">
	  <div class="formmainbox">
	  <div class="formheading"></div>
			<div class="headingbg"><s:text name="dcbreport.search"/></div>
			<table width="100%" border="0" cellspacing="0" cellpadding="0">
				<tr>
					<td class="bluebox2" width="5%">&nbsp;</td>
					<td class="bluebox" width=""><s:text name="Zone" /> <span
						class="mandatory1">*</span> :</td>
					<td class="bluebox" width=""><s:select headerKey="-1"
							headerValue="%{getText('default.select')}" name="zoneId" id="zoneId"
							listKey="key" listValue="value" list="ZoneBndryMap"
							cssClass="selectnew" value="%{zoneId}" />
					</td>
					<td class="bluebox" colspan="2">&nbsp;</td>
				</tr>
			</table>
	  </div>
	  <div class="buttonbottom" align="center">		   
    	<input value="Search" name="Search" id='Search' type="button" class="buttonsubmit"
				onclick="generateDCBReport();" />			
		<input type="button" name="button2" id="button2"
				value="Close" class="button" onclick="window.close();">				
	 </div>
  </s:form>
</html>
