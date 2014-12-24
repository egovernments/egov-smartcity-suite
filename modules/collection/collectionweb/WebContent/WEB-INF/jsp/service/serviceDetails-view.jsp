<%@ include file="/includes/taglibs.jsp"%>
<%@ taglib uri="/WEB-INF/struts-tags.tld" prefix="s"%>
<html>
<head>
 <meta http-equiv="Pragma" content="no-cache"/>
<script type="text/javascript" src="${pageContext.request.contextPath}/js/finAccountsTable.js"></script>
<script type="text/javascript">
function onBodyLoad(){
if(document.getElementById('voucherCreation').checked==false){
	document.getElementById("voucherApprovedDetails").style.display="none";
	}
else{
	document.getElementById("voucherApprovedDetails").style.display="";
}
}
</script>
<title> <s:text name="service.master.search.header"></s:text> </title>

</head>

<body onload="onBodyLoad();loadDropDownCodes();loadGridOnValidationFail();disableAll();">
<s:form theme="simple" name="serviceDetailsForm" action="serviceDetails" method="post">
<s:token />
<s:push value="model">
	
	<jsp:include page="serviceDetails-form.jsp"/>
	<div class="buttonbottom">
			
			
			<label>
				<input type="button" id="closeButton" value="Close" onclick="javascript:window.close()" class="button"/>
			</label>			
		</div>
</s:push>
</s:form>

<script>

function disableAll()  
{
	var frmIndex=0;
	for(var i=0;i<document.forms[frmIndex].length;i++)
	{
		for(var i=0;i<document.forms[0].length;i++)
		{
			if(document.forms[0].elements[i].value != 'Close'){
				document.forms[frmIndex].elements[i].disabled =true;   
			}						
		}	
	}
}
</script>
</body>
</html>