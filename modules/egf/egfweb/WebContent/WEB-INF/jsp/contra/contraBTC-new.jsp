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
	<link rel="stylesheet" type="text/css" href="/EGF/cssnew/ccMenu.css"/>
	<script type="text/javascript" src="${pageContext.request.contextPath}/javascript/voucherHelper.js"></script>
	<script type="text/javascript" src="${pageContext.request.contextPath}/javascript/contra.js"></script>
	<script type="text/javascript" src="/EGF/commonjs/ajaxCommonFunctions.js"></script>
	<meta http-equiv="Content-Type" content="text/html; charset=windows-1252">
	<title>Cash Withdrawal</title>
</head>

	
<body onload="onloadtask();">
<s:form action="contraBTC" theme="simple" name="cashWithDrawalForm" >
	<jsp:include page="../budget/budgetHeader.jsp">
      	<jsp:param name="heading" value="Cash Withdrawal" />
	</jsp:include>
	
	<div align="center">
		<font  style='color: red ;'> 
			<p class="error-block" id="lblError" ></p>
		</font>
		<span class="mandatory" id="errors">
			<s:actionerror/>  
			<s:fielderror />
			<s:actionmessage />
		</span>
	</div>
			
	<div class="formmainbox">
		<div class="formheading"/>
			<div class="subheadnew">Cash Withdrawal</div>
		</div>
		<%@include file="contraBTC-form.jsp"%>
		<div class="buttonbottom" style="padding-bottom:10px;"> 
			<s:submit type="submit" cssClass="buttonsubmit" value="Save & Close" id="save&close" method="saveAndClose" onclick="return validateInput()"/>
			<s:submit type="submit" cssClass="buttonsubmit" value="Save & New" id="save&new" method="saveAndNew" onclick="return validateInput()"/>
			<s:submit type="submit" cssClass="buttonsubmit" value="Save & VIew" id="save&View" method="saveAndView" onclick="return validateInput()"/>
			<s:reset name="button" type="submit" cssClass="button" id="button" value="Cancel"/>
			<s:submit value="Close" onclick="javascript: self.close()" cssClass="button"/>
		</div>
	</div>
	<input type="hidden" id="voucherHeader.name" name="voucherHeader.name" value="BankToCash"/>
	<input type="hidden" id="voucherHeader.type" name="voucherHeader.type" value="Contra"/>
	<input type="hidden" id="voucherHeader.cgvn" name="voucherHeader.cgvn" value="BTC"/>
	<s:hidden id="bankBalanceMandatory" name="bankBalanceMandatory" value="%{isBankBalanceMandatory()}"/>
	
</s:form>
<div id="resultGrid"></div>

<script>
	String.prototype.trim = function () {
	    return this.replace(/^\s*/, "").replace(/\s*$/, "");
	}
	function onloadtask(){
		document.getElementById('fundId').disabled =true;
		var currentTime = new Date()
		var month = currentTime.getMonth() + 1
		var day = currentTime.getDate()
		var year = currentTime.getFullYear()
		if(document.getElementById('voucherDate').value == ''){
			document.getElementById('voucherDate').value = day + "/" + month + "/" + year ;
		}
		if(document.getElementById('chequeDate').value == ''){
			document.getElementById('chequeDate').value = day + "/" + month + "/" + year ;
		}
		var accnum =  document.getElementById('accountNumber');
		if(accnum.value != -1){
			populateAvailableBalance(accnum)
		}
		document.getElementById('voucherDate').focus();
	}
	function validateInput(){
			document.getElementById('fundId').enabled =true;
			document.getElementById('lblError').innerHTML = "";
			document.getElementById('errors').innerHTML = "";
			
			if(document.getElementById('bankId').value == -1){
			document.getElementById('lblError').innerHTML = "Please select a bank ";
			return false;
			}
			
			if(document.getElementById('voucherDate').value.trim().length == 0){
			document.getElementById('lblError').innerHTML = "Please enter  voucher date ";
			return false;
			}
			
			if(document.getElementById('accountNumber').value == -1 ){
			document.getElementById('lblError').innerHTML = "Please select an account number ";
			return false;
			}
			
			if(document.getElementById('amount').value.trim().length == 0 || document.getElementById('amount').value.trim() == 0){
			document.getElementById('lblError').innerHTML = "Please enter an amount greater than zero";
			return false;
			}
	
			if(document.getElementById('chequeNumber') != undefined && document.getElementById('chequeNumber').value == ''){
				document.getElementById('lblError').innerHTML = "Please enter cheque number";
				return false;
			}
	
			if(document.getElementById('chequeDate').value == ''){
			document.getElementById('lblError').innerHTML = "Please enter cheque date";
			return false;
			}
	
			var validMis = validateMIS();
			if(validMis == true){
				document.getElementById('fundId').disabled =false;
				//return true;
			}
			else{
				return false;
			}
			var 	insuffientAlert='There is no sufficient bank balance. ';
			var 	continueAlert='Do you want to continue ? ';
			var 	fundFlowNotGeneratedAlert='';				
				if(parseFloat(document.getElementById('availableBalance').value)==-1)
				{
					fundFlowNotGeneratedAlert="FundFlowReport is not generated for the for the day. ";
				}
			
			if(parseFloat(document.getElementById('amount').value)>parseFloat(document.getElementById('availableBalance').value))
			{
				if(document.getElementById('bankBalanceMandatory').value=='true')
				{
					alert(insuffientAlert);
					return false;
				}
				else
				{
					if(confirm(fundFlowNotGeneratedAlert+insuffientAlert+continueAlert))
					{
						return true;
					}
					else
					{
						return false;
					}
				}
			}
			return true;
	}
	function validateAmountFormat(){
		var amount = document.getElementById('amount').value.trim();
	}
	
	if('<s:text name="%{isBankBalanceMandatory()}"/>'=='')
		document.getElementById('lblError').innerHTML = "bank_balance_mandatory parameter is not defined";
</script>
</body>
</html>