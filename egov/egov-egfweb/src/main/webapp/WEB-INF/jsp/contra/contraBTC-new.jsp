<%--
  ~    eGov  SmartCity eGovernance suite aims to improve the internal efficiency,transparency,
  ~    accountability and the service delivery of the government  organizations.
  ~
  ~     Copyright (C) 2017  eGovernments Foundation
  ~
  ~     The updated version of eGov suite of products as by eGovernments Foundation
  ~     is available at http://www.egovernments.org
  ~
  ~     This program is free software: you can redistribute it and/or modify
  ~     it under the terms of the GNU General Public License as published by
  ~     the Free Software Foundation, either version 3 of the License, or
  ~     any later version.
  ~
  ~     This program is distributed in the hope that it will be useful,
  ~     but WITHOUT ANY WARRANTY; without even the implied warranty of
  ~     MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
  ~     GNU General Public License for more details.
  ~
  ~     You should have received a copy of the GNU General Public License
  ~     along with this program. If not, see http://www.gnu.org/licenses/ or
  ~     http://www.gnu.org/licenses/gpl.html .
  ~
  ~     In addition to the terms of the GPL license to be adhered to in using this
  ~     program, the following additional terms are to be complied with:
  ~
  ~         1) All versions of this program, verbatim or modified must carry this
  ~            Legal Notice.
  ~            Further, all user interfaces, including but not limited to citizen facing interfaces,
  ~            Urban Local Bodies interfaces, dashboards, mobile applications, of the program and any
  ~            derived works should carry eGovernments Foundation logo on the top right corner.
  ~
  ~            For the logo, please refer http://egovernments.org/html/logo/egov_logo.png.
  ~            For any further queries on attribution, including queries on brand guidelines,
  ~            please contact contact@egovernments.org
  ~
  ~         2) Any misrepresentation of the origin of the material is prohibited. It
  ~            is required that all modified versions of this material be marked in
  ~            reasonable ways as different from the original version.
  ~
  ~         3) This license does not grant any rights to any user of the program
  ~            with regards to rights under trademark law for use of the trade names
  ~            or trademarks of eGovernments Foundation.
  ~
  ~   In case of any queries, you can reach eGovernments Foundation at contact@egovernments.org.
  ~
  --%>


<%@ include file="/includes/taglibs.jsp"%>
<%@ page language="java"%>
<html>
<head>
<link rel="stylesheet" type="text/css"
	href="/EGF/resources/css/ccMenu.css?rnd=${app_release_no}" />
<script type="text/javascript"
	src="${pageContext.request.contextPath}/resources/javascript/voucherHelper.js?rnd=${app_release_no}"></script>
<script type="text/javascript"
	src="${pageContext.request.contextPath}/resources/javascript/contra.js?rnd=${app_release_no}"></script>
<script type="text/javascript"
	src="/EGF/resources/javascript/ajaxCommonFunctions.js?rnd=${app_release_no}"></script>
<meta http-equiv="Content-Type"
	content="text/html; charset=windows-1252">
<title>Cash Withdrawal</title>
</head>


<body onload="onloadtask();">
	<s:form action="contraBTC" theme="simple" name="cashWithDrawalForm">
		<jsp:include page="../budget/budgetHeader.jsp">
			<jsp:param name="heading" value="Cash Withdrawal" />
		</jsp:include>
		<div align="center">
			<font style='color: red;'>
				<p class="error-block" id="lblError"></p>
			</font> <span class="mandatory" id="errors"> <s:actionerror /> <s:fielderror />
				<s:actionmessage />
			</span>
		</div>

		<div class="formmainbox">
			<div class="formheading" />
			<div class="subheadnew">Cash Withdrawal</div>
		</div>
		<%@include file="contraBTC-form.jsp"%>
		<div class="buttonbottom" style="padding-bottom: 10px;">
			<s:submit type="submit" cssClass="buttonsubmit" value="Save & Close"
				id="save&close" method="create"
				onclick="return validateInput('saveAndClose')" />
			<s:submit type="submit" cssClass="buttonsubmit" value="Save & New"
				id="save&new" method="create"
				onclick="return validateInput('saveAndNew')" />
			<s:submit type="submit" cssClass="buttonsubmit" value="Save & VIew"
				id="save&View" method="create"
				onclick="return validateInput('saveAndView')" />
			<s:reset name="button" type="submit" cssClass="button" id="button"
				value="Cancel" />
			<s:submit value="Close" onclick="javascript: self.close()"
				cssClass="button" />
		</div>
		</div>
		<input type="hidden" id="voucherHeader.name" name="voucherHeader.name"
			value="BankToCash" />
		<input type="hidden" id="saveType" name="saveType" value="%{saveType}" />
		<input type="hidden" id="voucherHeader.type" name="voucherHeader.type"
			value="Contra" />
		<input type="hidden" id="voucherHeader.cgvn" name="voucherHeader.cgvn"
			value="BTC" />
		<s:hidden id="bankBalanceMandatory" name="bankBalanceMandatory"
			value="%{isBankBalanceMandatory()}" />
		<s:token />
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
	function validateInput(saveTypeName){
			document.getElementById('saveType').value = saveTypeName;
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
					bootbox.alert(insuffientAlert);
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
