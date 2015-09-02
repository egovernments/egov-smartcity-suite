<%@ include file="/includes/taglibs.jsp"%>
<%@ page language="java" pageEncoding="UTF-8"%>
<html>
	<head>
		<script type="text/javascript">
		function doSubmit() {
			document.forms[0].submit();
		}		
		</script>
	</head>
	<body onload="doSubmit()">
		<center style="color: #444; font: bold 13px tohoma, arial, helvetica; position: relative; top: 250px">
			Please wait...
		</center>
		<form action="/collection/receipts/receipt!newform.action" method="POST">
			<input type="hidden" id="collectXML" name="collectXML" value="${collectXML}" />
		</form>
	</body>
</html>
