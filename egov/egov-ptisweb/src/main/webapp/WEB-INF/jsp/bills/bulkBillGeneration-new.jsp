<%@ page language="java" pageEncoding="UTF-8"%>
<%@ include file="/includes/taglibs.jsp" %>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />

<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN">
<html>
  <head>
    <title><s:text name='BulkBillNew'/></title>
  </head>
  
  <body>
  <s:form name="BulkBillGenerationForm" theme="simple">
  <s:token />
  <div class="formmainbox">
  <div class="formheading"></div>
		<div class="headingbg"><s:text name="BulkBillNew"/></div>
		<table width="100%" border="0" cellspacing="0" cellpadding="0">
		<tr>
			<td>&nbsp;</td>
			<td>&nbsp;</td>
			<td>&nbsp;</td>
			<td>&nbsp;</td>
			<td>&nbsp;</td>
		</tr>
		<tr>
			<td>&nbsp;</td>
			<td><s:text name="Ward" /> <span
				class="mandatory1">*</span> :</td>
			<td>
					<s:select headerKey="-1" headerValue="%{getText('default.select')}" name="wardId"
					id="wardNum" listKey="id" listValue="name" list="wardList" 
					value="%{wardNumber}" cssClass="selectnew" onchange="return populatePartNumbers(this.id);" />
			</td>
			<td>&nbsp;</td>
			<td>&nbsp;</td>
		</tr>
		<tr>
			<td></td>
			<%@ include file="../common/partnumber.jsp" %>
			<td></td>
			<td></td>			
		</tr>
		<tr>
			<td colspan="5">&nbsp;</td>
		</tr>
		</table>
	</div>
	<div class="buttonbottom">
		<s:submit value="Generate Bills" name="generateBills" id='generateBills' cssClass="buttonsubmit" method="generateBills"/>
		<input name="buttonClose" type="button" class="button"	id="buttonClose" value="Close" onclick="window.close();" />&nbsp;
	</div>
  </s:form>
  <script type="text/javascript">
  	paintAlternateColorForRows();
  </script>
  </body>
</html>