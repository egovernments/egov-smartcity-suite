<%@ include file="/includes/taglibs.jsp"%>

<html xmlns="http://www.w3.org/1999/xhtml">
<head>
<title><s:text name="remittanceVoucherReport.title" /></title>
</head>
<body>
<s:form theme="simple" name="displayReportForm">
	<s:if test="%{hasErrors()}">
		<div class="errorstyle"><s:actionerror /> <s:fielderror /></div>
	</s:if>
	<s:if test="%{hasActionMessages()}">
		<div class="messagestyle"><s:actionmessage theme="simple" /></div>
	</s:if>
	<iframe src="../reportViewer?reportId=<s:property value='reportId'/>" width="98%"
		height="70%">
	<p>Your browser does not support iframes.</p>
	</iframe>
	<br />
	<div class="buttonbottom">
	<input name="collectionReport.close" type="button" class="button"
		id="buttonClose" value="Close" onclick="window.close()" /> &nbsp;
	<input name="collectionReport.back" type="button" class="button"
		id="buttonBack" value="Back" onclick="window.location='${pageContext.request.contextPath}/reports/remittanceVoucherReport-criteria.action';" /> &nbsp;
	</div>
</s:form>
</body>
</html>
