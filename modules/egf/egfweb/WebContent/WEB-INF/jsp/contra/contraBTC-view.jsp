<html>
<head>
<%@ taglib prefix="s" uri="/WEB-INF/struts-tags.tld"%>
<%@ taglib prefix="EGF" tagdir="/WEB-INF/tags"%>
<%@ page language="java"%>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean"%>
<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html"%>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic"%>
<%@ taglib uri="/WEB-INF/struts-nested.tld" prefix="nested"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib prefix="egov" tagdir="/WEB-INF/tags"%>
<script type="text/javascript" src="${pageContext.request.contextPath}/javascript/voucherHelper.js"></script>
<script type="text/javascript" src="${pageContext.request.contextPath}/javascript/contra.js"></script>
<script type="text/javascript" src="/EGF/commonjs/ajaxCommonFunctions.js"></script>
<style type="text/css">
@media print {
input#button1 {
display: none;
}
}

@media print {
input#button2 {
display: none;
}
}
</style>

</head>
<body onload="onloadTask();">
	<s:form  action="contraBTC" theme="simple" name="cbtcform"  >
	<s:push value="model">
		<jsp:include page="../budget/budgetHeader.jsp">
			<jsp:param value="Cash Withdrawal" name="heading"/>
		</jsp:include>
	<div class="formmainbox">
		<div class="subheadnew">View Cash Withdrawal</div>
		<div id="listid" style="display:block"><br/></div>
		<div align="center">
			<font  style='color: red ;'><p class="error-block" id="lblError" ></p></font>
			<s:if test="%{not close}">
				<span class="mandatory">
					<s:actionerror/>  
					<s:fielderror />
					<s:actionmessage />
				</span>
			</s:if>
		</div>
		<%@include file="contraBTC-form.jsp"%>	
		</s:push>
		<br/>
		<div id="buttons">
			<input name="button" type="button" class="buttonsubmit" id="button1" value="Print" onclick="window.print()"/>&nbsp;
			<s:submit value="Close" onclick="javascript: self.close()" id="button2" cssClass="button"/>
		</div>
		<div id="resultGrid"></div>
	</div>
</s:form>
<SCRIPT type="text/javascript">
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

</SCRIPT>
</body>
</html>