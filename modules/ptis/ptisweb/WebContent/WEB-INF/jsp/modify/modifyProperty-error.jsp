<%@ page language="java" pageEncoding="UTF-8"%>
<%@ include file="/includes/taglibs.jsp"%>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />

<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN">
<html>
<head>
<title><s:text name="assessmentDataUpdate"></s:text></title>
</head>
<body>
	<div class="formmainbox">
		<div class="formheading"></div>
		<div class="headingbg">
			<s:text name="assessmentDataUpdate" />
		</div>
		<table width="100%" border="0" cellspacing="0" cellpadding="0">
			<tr>
				<td class="bluebox">
				 &nbsp; &nbsp;
				</td>
			</tr>
			<tr>
				<td colspan="5" style="background-color: #FDF7F0; font-size: 15px;" align="center">
					<s:property value="%{errorMessage}" />
				</td>
			</tr>
			<tr>
				<td class="bluebox">
				 &nbsp; &nbsp;
				</td>
			</tr>
		</table>
	</div>
	<div class="buttonbottom" align="center">
		<td><input type="button" class="button" name="SearchProperty"
			id="SearchProperty" value="Search Property"
			onclick="window.location='../search/searchProperty!searchForm.action';" />
		</td>
		<td><input type="button" name="button2" id="button2"
			value="Close" class="button" onclick="window.close();" /></td>
	</div>
</body>
</html>
