<%@ taglib prefix="s" uri="/WEB-INF/struts-tags.tld"%>
<%@ page language="java"%>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean"%>
<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html"%>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic"%>
<%@ taglib uri="/WEB-INF/struts-nested.tld" prefix="nested"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib prefix="egov" tagdir="/WEB-INF/tags"%>
<html>

<head>
<script type="text/javascript" src="${pageContext.request.contextPath}/javascript/voucherHelper.js"></script>
<script type="text/javascript" src="/EGF/commonjs/ajaxCommonFunctions.js"></script>
	
		
		<script type="text/javascript" src="/EGF/javascript/calender.js"></script>
		<script type="text/javascript" src="/EGF/script/calendar.js" ></script>
		<script type="text/javascript" src="/EGF/javascript/dateValidation.js"></script>
<meta http-equiv="Content-Type" content="text/html; charset=windows-1252">
<title>Contra - Cash Deposit</title>

</head>

	
<body onload="onloadTask();">
<s:form action="contraCTB" theme="simple" name="cashDepositForm" >
<s:push value="model">
			<jsp:include page="../budget/budgetHeader.jsp">
        		<jsp:param name="heading" value="Cash Deposit" />
			</jsp:include>
			
			<span class="mandatory">
			<font  style='color: red ; font-weight:bold '> 
				<s:actionerror/>  
				<s:fielderror />
				<s:actionmessage /></font>
			</span>
<div class="formmainbox">
<div class="formheading"/><div class="subheadnew">Cash Deposit</div>
<div id="listid" style="display:block"><br/>
<div align="center">
<font  style='color: red ; font-weight:bold '> 
<p class="error-block" id="lblError" ></p></font>
	 <table id="boundarytableGrid" border="0" cellSpacing=0 cellPadding=0 width="55%" align="center" >
		  <tr class="tableheader" >
				<td class="greybox"><s:text name="contra.cashInHand"/></td>
	          </tr>
			<td class="greybox"><s:textfield name="contraBean.cashInHand" id="cashInHand" readonly="true" /></td>
		</tr>
	</table>
	<%@include file="contraCTB-form.jsp"%>
</div>
</div>
</div>
</div>
</s:push>
</s:form>
<script>

function onloadTask(){
	disableControls(0,true);
	var message = '<s:property value="message"/>';
	if(message != null && message != '')
		showMessage(message);
	var element = document.getElementById('accountNumber');
	if(element != undefined){
		populateAvailableBalance(element)
		if(element.value != -1){
			populateNarration(element)
		}
	}
}

function showMessage(message){
	var close = <s:property value="close"/>;
	var voucherHeaderId = <s:property value="voucherHeader.id"/>;
	alert(message);
	if(close == true){
		self.close();
	}
	document.forms[0].action = "${pageContext.request.contextPath}/voucher/preApprovedVoucher!loadvoucherview.action?vhid="+voucherHeaderId;
	document.forms[0].submit();
}

</script>

</body>

</html>
