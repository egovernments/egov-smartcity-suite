<%@ include file="/includes/taglibs.jsp" %>
<head>
</head>
<body>

<%@ include file="challan-createReceipt.jsp" %>
<s:hidden id="challanNumber" name="challanNumber" value="%{model[0].receiptHeader.challan.challanNumber}"/>
</body>


