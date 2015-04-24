<%@ page language="java" pageEncoding="UTF-8"%>
<%@ include file="/includes/taglibs.jsp"%>
<%@ taglib prefix="s" uri="/WEB-INF/struts-tags.tld"%>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />

<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN">
<html>
	<head>
		<title>Acknowledgment of External Fees
		</title>

	</head>

	<body>
		<s:form name="citizenSearch" theme="simple">
			<s:if test="%{hasErrors()}">
				<div class="errorstyle">
					<s:actionerror />
					<s:fielderror />
				</div>
			</s:if>
			<s:if test="%{hasActionMessages()}">
				<div class="messagestyle">
					<s:actionmessage theme="simple" />
				</div>
			</s:if>
			<iframe src="/bpa/reportViewer?reportId=<s:property value='reportId'/>"
				width="98%" height="70%">
				<p>
					Your browser does not support iframes.
				</p>
			</iframe>
			<br />
			<div class="buttonbottom">
				<input name="buttonClose" type="button" class="button"
					id="buttonClose" value="Close" onclick="window.close()" />
				&nbsp;

			</div>
		</s:form>
	</body>
</html>