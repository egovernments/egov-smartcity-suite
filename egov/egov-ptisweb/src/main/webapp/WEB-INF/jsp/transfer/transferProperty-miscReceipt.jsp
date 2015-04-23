<%@ include file="/includes/taglibs.jsp"%>
<html>
<head>
<script type="text/javascript">
	function loadMiscReceipt() {
		var consumerCode = '<s:property value="%{basicProperty.upicNo}"/>';
		consumerCode = consumerCode
				+ "(Zone:"
				+ '<s:property value="%{basicProperty.propertyID.zone.boundaryNum}"/>'
				+ " Ward:"
				+ '<s:property value="%{basicProperty.propertyID.ward.boundaryNum}"/>'
				+ ")";
		window.location = "/../collection/citizen/onlineReceipt!viewReceipt.action?receiptNumber=<s:property value='%{receiptNum}'/>&consumerCode="
				+ consumerCode + "&serviceCode=PT";
	}
</script>
</head>
<body onload="refreshParentInbox();loadMiscReceipt();">
</body>
</html>