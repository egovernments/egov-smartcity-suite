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

</head>
<body onload="onloadTask();">
<s:form  action="contraBTC" theme="simple" name="cbtcform"  >
	<s:push value="model">
	<jsp:include page="../budget/budgetHeader.jsp">
		<jsp:param value="Bank to Cash Transfer" name="heading"/>
	</jsp:include>
	<div class="formmainbox">
		<div class="formheading"/>
		<div class="subheadnew">Reverse Cash Withdrawal</div>
		<div id="listid" style="display:block"><br/></div>
		<div align="center">
			<font  style='color: red ;'><p class="error-block" id="lblError" ></p></font>
			<span class="mandatory">
				<s:actionerror id="actionerror"/>  
				<s:fielderror id="fielderror"/>
				<s:actionmessage id="actionmessage"/>
			</span>
		</div>
		<%@include file="contraBTC-form.jsp"%>
		<tr>
			<s:if test="%{shouldShowHeaderField('vouchernumber')}">
				<td class="bluebox"><s:text name="reversalVoucherNumber"/><span class="mandatory">*</span></td>
				<td class="bluebox"><s:textfield name="reverseVoucherNumber" id="reversalVoucherNumber" /></td>
			</s:if>
			<td class="bluebox"><s:text name="reversalVoucherDate"/><span class="mandatory">*</span></td>
			<td class="bluebox"><s:textfield name="reverseVoucherDate"  id="reversalVoucherDate" onkeyup="DateFormat(this,this.value,event,false,'3')"/>
			<a href="javascript:show_calendar('cbtcform.reversalVoucherDate');" style="text-decoration:none">&nbsp;<img tabIndex="-1" src="${pageContext.request.contextPath}/image/calendaricon.gif" border="0"/></A>(dd/mm/yyyy)</td>
		</tr>	
		</s:push>
		<br/><br/>
		<input type="hidden" name="voucherHeader.id" value='<s:property value="voucherHeader.id"/>' id="voucherHeaderId"/>
		<div id="buttons">
			<s:submit type="submit" cssClass="buttonsubmit" value="Reverse and View" method="saveReverse" id="reverse" onClick="return validate();"/>
			<s:submit type="submit" cssClass="buttonsubmit" value="Reverse and Close" method="saveReverse" id="reverse" onClick="return validate();"/>
			<s:submit value="Close" onclick="javascript: self.close()" id="button2" cssClass="button"/>
		</div>
		<div id="resultGrid"></div>
	</div>
</s:form>
<SCRIPT type="text/javascript">

function onloadTask(){
	disable(0);
	document.getElementById('reversalVoucherDate').readOnly = false;
	document.getElementById('voucherHeaderId').readOnly = false;
	if(document.getElementById('reversalVoucherNumber') != undefined){
		document.getElementById('reversalVoucherNumber').readOnly = false;
	}
	document.getElementById('reverse').readOnly = false;
	var element = document.getElementById('accountNumber');
	if(element != undefined){
		populateAvailableBalance(element)
		if(element.value != -1){
			populateNarration(element)
		}
	}
}


function disable(frmIndex){
	for(var i=0;i<document.forms[frmIndex].length;i++){
		if(document.forms[frmIndex].elements[i].value != "Close" && document.forms[frmIndex].elements[i].value != "Print"){
			document.forms[frmIndex].elements[i].readOnly = true;
		}
	}
}


function validate(){
	document.getElementById('lblError').innerHTML = "";
		
	if(document.getElementById('reversalVoucherDate').value == ''){
		document.getElementById('lblError').innerHTML = "Please enter Reversal Voucher Date";
		return false;
	}
	if(document.getElementById('reversalVoucherNumber') != undefined && document.getElementById('reversalVoucherNumber').value == ''){
		document.getElementById('lblError').innerHTML = "Please enter Reversal Voucher Number";
		return false;
	}
}
</SCRIPT>
</body>
</html>