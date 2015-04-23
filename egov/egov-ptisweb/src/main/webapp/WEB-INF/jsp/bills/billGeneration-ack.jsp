<%@ page language="java" pageEncoding="UTF-8"%>
<%@ include file="/includes/taglibs.jsp" %>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />

<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN">
<html>
  <head>
    <title><s:text name='BillAck'/></title>
  </head>
  
  <body>
  <s:form name="BillGenerationForm" theme="simple">
  <s:token />
  <div class="formmainbox">
  <div class="formheading"></div>
		<div class="headingbg"><s:text name="BillAck"/></div>
		<table width="100%" border="0" cellspacing="0" cellpadding="0">
		<tr>
	       	<td colspan="5" style="background-color: #FDF7F0;font-size: 15px;" align="center">
	        	<span class="bold"><s:property value="%{ackMessage}"/></span>
	        </td>
		</tr>
		</table>
	</div>
	<div class="buttonbottom">
		<input name="buttonClose" type="button" class="button"	id="buttonClose" value="Close" onclick="window.close();" />&nbsp;
	</div>
  </s:form>
  </body>
</html>
