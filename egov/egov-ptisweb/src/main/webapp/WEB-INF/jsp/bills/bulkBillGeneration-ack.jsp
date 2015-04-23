<%@ page language="java" pageEncoding="UTF-8"%>
<%@ include file="/includes/taglibs.jsp" %>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />

<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN">
<html>
  <head>
    <title><s:text name='BulkBillAck'/></title>
  </head>
  
  <body>
  <s:form name="BillGenerationForm" theme="simple">
  <s:token />
  <div class="formmainbox">
  <div class="formheading"></div>
		<div class="headingbg"><s:text name="BulkBillAck"/></div>
		<table width="100%" border="0" cellspacing="0" cellpadding="0">
		<tr>
	       	<td colspan="5" style="color: #0000A0;font-size: 15px;" align="center">
	        	<s:property value="%{ackMessage}"/><a href="billGeneration!billsGenStatus.action">Generated Bills Report</a>
	        </td>
		</tr>
		</table>
	</div>
	<div class="buttonbottom">
		<s:submit value="Go Back" name="newform" id='newform' cssClass="buttonsubmit" method="newForm"/>
		<input name="buttonClose" type="button" class="button"	id="buttonClose" value="Close" onclick="window.close();" />&nbsp;
	</div>
  </s:form>
  </body>
</html>