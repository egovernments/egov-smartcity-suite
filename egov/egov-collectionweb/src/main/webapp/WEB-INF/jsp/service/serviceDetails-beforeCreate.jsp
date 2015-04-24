<%@ include file="/includes/taglibs.jsp"%>
<html>
<head>
<script type="text/javascript" src="${pageContext.request.contextPath}/js/finAccountsTable.js"></script>
<script type="text/javascript">
function onBodyLoad()
{
	document.getElementById("voucherApprovedDetails").style.display="none";
}

function EnableVoucherDetails(obj){
	if(obj.checked){
		document.getElementById("voucherApprovedDetails").style.display="";
		}
		else{
		document.getElementById("voucherApprovedDetails").style.display="none";
		}
}
</script>
<title> <s:text name="service.master.search.header"></s:text> </title>

</head>

<body onLoad="onBodyLoad();loadDropDownCodes();loadDropDownCodesFunction();loadGridOnValidationFail();">
<s:form theme="simple" name="serviceDetailsForm" action="serviceDetails" method="post">
<s:token />
<s:push value="model">
	
	<jsp:include page="serviceDetails-form.jsp"/>
	<div class="buttonbottom">
			<label>
				<s:submit type="submit" cssClass="buttonsubmit" id="button"
					value="Save" method="create" onClick = "return validate();"/>
			</label>&nbsp;
			
			<label>
				<input type="button" id="Close" value="Close" onclick="javascript:window.close()" class="buttonsubmit"/>
			</label>			
		</div>
</s:push>
</s:form>

</body>
</html>