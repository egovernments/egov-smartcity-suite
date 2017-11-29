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
<script type="text/javascript"
	src="${pageContext.request.contextPath}/resources/javascript/voucherHelper.js?rnd=${app_release_no}"></script>
<script type="text/javascript"
	src="/EGF/resources/javascript/ajaxCommonFunctions.js?rnd=${app_release_no}"></script>


<script type="text/javascript"
	src="/EGF/resources/javascript/calender.js?rnd=${app_release_no}"></script>
<script type="text/javascript"
	src="/EGF/resources/javascript/dateValidation.js?rnd=${app_release_no}"></script>
<meta http-equiv="Content-Type"
	content="text/html; charset=windows-1252">
<title>Contra - Cash Deposit</title>

</head>


<body onload="onloadtask();">
	<s:form action="contraCTB" theme="simple" name="cashDepositForm">
		<s:push value="model">
			<jsp:include page="../budget/budgetHeader.jsp">
				<jsp:param name="heading" value="Cash Deposit" />
			</jsp:include>

			<span class="mandatory"> <font
				style='color: red; font-weight: bold'> <s:actionerror /> <s:fielderror />
					<s:actionmessage /></font>
			</span>
			<div class="formmainbox">
				<div class="formheading" />
				<div class="subheadnew">Cash Deposit</div>
				<div id="listid" style="display: block">
					<div align="center">
						<font style='color: red; font-weight: bold'>
							<p class="error-block" id="lblError"></p>
						</font>
						<%@include file="contraCTB-form.jsp"%>
						<table border="0" width="100%">

							<tr>
								<td class="greybox" width="25%"><s:text
										name="contra.cashInHand" /></td>
								<td class="greybox" width="25%"><s:textfield
										name="contraBean.cashInHand" id="cashInHand" tabindex="-1"
										readonly="true" /></td>
								<td class="greybox" width="25%"></td>
								<td class="greybox" width="25%"></td>
							</tr>
						</table>
						<div class="subheadsmallnew" /></div>
						<div class="mandatory" align="left">* Mandatory Fields</div>
					</div>
					<div class="buttonbottom">
						<table border="0" width="80%" align="center">
							<tr></tr>
							<tr>
								<td><s:submit type="submit" cssClass="buttonsubmit"
										value="Save & Close" id="save&close" name="save&close"
										method="create" onclick="return validateInput('saveclose')" />
									<s:submit type="submit" cssClass="buttonsubmit"
										value="Save & New" id="save&new" name="save&new"
										method="create" onclick="return validateInput('savenew')" /> <s:submit
										type="submit" cssClass="buttonsubmit" value="Save & View"
										id="save&view" name="save&view" method="create"
										onclick="return validateInput('saveview')" /> <s:reset
										name="Cancel" type="submit" cssClass="buttonsubmit"
										id="Cancel" value="Cancel" /> <input type="submit"
									value="Close" onclick="javascript:window.close()"
									cssClass="buttonsubmit" class="buttonsubmit" /></td>
							</tr>
						</table>

					</div>
				</div>
			</div>
			</div>
			</div>
			<input type="hidden" id="voucherTypeBean.voucherName"
				name="voucherTypeBean.voucherName" value="CashToBank" />
			<input type="hidden" id="voucherTypeBean.voucherType"
				name="voucherTypeBean.voucherType" value="Contra" />
			<input type="hidden" id="voucherTypeBean.voucherNumType"
				name="voucherTypeBean.voucherNumType" value="Contra" />
			<input type="hidden" id="voucherTypeBean.cgnType"
				name="voucherTypeBean.cgnType" value="CTB" />
			<s:hidden name="contraBean.saveMode" id="saveMode" />
			<s:hidden name="contraBean.result" id="result" />
			<s:hidden name="contraBean.mode" id="mode" />
			<s:hidden id="cgn" name="cgn"></s:hidden>
			<s:hidden id="vouchermis.sourcePath" name="vouchermis.sourcePath"
				value="../contra/contraCTB!loadCTBVoucher.action?vhid="></s:hidden>
		</s:push>
	</s:form>
	<script>

function populateAccNum(branch){

	var bankbranchId = branch.options[branch.selectedIndex].value;
	var index=bankbranchId.indexOf("-");
	var bankId = bankbranchId.substring(0,index);
	var brId=bankbranchId.substring(index+1,bankbranchId.length);
	populateaccountNumber({bankId:bankId,branchId:brId})
}
function populateNarration(accnumObj){
	var accnum =  accnumObj.options[accnumObj.selectedIndex].value;
	var url = '../voucher/common!loadAccNumNarrationAndFund.action?accnum='+accnum;
	YAHOO.util.Connect.asyncRequest('POST', url, postType, null);

}
var postType = {
success: function(o) {
		var narrationfund= o.responseText;
		var index=narrationfund.indexOf("-");
		document.getElementById('accnumnar').value=narrationfund.substring(o,index);	
		var fundid = narrationfund.substring(index+1,narrationfund.length);	
		document.getElementById('fundId').value = fundid;
		document.getElementById('fundId').disabled =true;
		populateschemeid({fundId:fundid})
    },
    failure: function(o) {
    	bootbox.alert('failure');
    }
}

String.prototype.trim = function () {
    return this.replace(/^\s*/, "").replace(/\s*$/, "");
}
function onloadtask(){
	document.getElementById('fundId').disabled =true;
	var currentTime = new Date()
	var month = currentTime.getMonth() + 1
	var day = currentTime.getDate()
	var year = currentTime.getFullYear()
	
	for(var i=0;i<document.forms[0].length;i++)
		{
			if(document.getElementById('mode').value == "view")
			{
					document.forms[0].elements[i].disabled =true;
				}					
			}	
	if(document.getElementById('mode').value == "")
	{
		document.getElementById('voucherDate').value = day + "/" + month + "/" + year ;
		document.getElementById('voucherDate').focus();
	}
	
}
	function validateInput(saveMode)
	{
		document.getElementById('saveMode').value=saveMode;
		document.getElementById('fundId').enabled =true;
		document.getElementById('lblError').innerHTML = "";
		
		if(document.getElementById('voucherNumber') && document.getElementById('voucherNumber').value.trim().length == 0){
		document.getElementById('lblError').innerHTML = "Please enter voucher number";
		return false;
		}
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
		
		if(document.getElementById('amount').value.trim().length == 0 || document.getElementById('amount').value.trim() < 0){
		document.getElementById('lblError').innerHTML = "Please enter an amount greater than zero";
		return false;
		}
			if(document.getElementById('documentNum').value.trim().length == 0 || document.getElementById('documentNum').value.trim() ==""){
		document.getElementById('lblError').innerHTML = "Please enter an Reference Number";
		return false;
		}
		if(document.getElementById('documentDate').value.trim().length == 0 || document.getElementById('documentDate').value.trim() == 0){
		document.getElementById('lblError').innerHTML = "Please enter an Reference date";
		return false;
		}
		var validMis = validateMIS();
		if(validMis == true){
			document.getElementById('fundId').disabled =false;
			return true;
		}
		else{
			return false;
		}
		
}
function amountFormat(){

		var amount = document.getElementById('amount').value.trim();
		var amountlastchar = amount.substring(amount.length-1,amount.length);
		 var alphaCheck = "0123456789.";
		
	  if (alphaCheck.indexOf(amountlastchar) == -1 ) {
	   		document.getElementById('amount').value=amount.substring(0,amount.length-1);
	   		document.getElementById('amount').focus;
	   }
}
	if(dom.get('saveMode').value=='savenew' && dom.get('result').value=='sucess')
			{
				if(document.getElementById('fundId')){
					document.getElementById('fundId').value=-1;
				}
				if(document.getElementById('vouchermis.departmentid')){
					document.getElementById('vouchermis.departmentid').value=-1;
				}
				if(document.getElementById('schemeid')){
					document.getElementById('schemeid').value=-1;
				}
				if(document.getElementById('subschemeid')){
					document.getElementById('subschemeid').value=-1;
				}
				if(document.getElementById('vouchermis.functionary')){
					document.getElementById('vouchermis.functionary').value=-1;
				}
				if(document.getElementById('fundsourceId')){
					document.getElementById('fundsourceId').value=-1;
				}
				if(document.getElementById('vouchermis.divisionid')){
					document.getElementById('vouchermis.divisionid').value=-1;
				}
				if(document.getElementById('voucherNumber')){
					document.getElementById('voucherNumber').value='';
				}
				document.getElementById('bankId').value=-1;
				document.getElementById('accountNumber').value=-1;
				document.getElementById('accnumnar').value="";
				document.getElementById('amount').value=0;
			}	
			var voucherNumber = '<s:property value='%{voucherHeader.voucherNumber}'/>' ;
			var cgn = '<s:property value='%{cgn}'/>' ;
			
			if(dom.get('result').value=='sucess')
			{
				bootbox.alert("Transaction Succesfully Completed and VoucherNumber is :  "+voucherNumber);
				if(dom.get('saveMode').value=='saveclose'){
					window.close();
				}else if(dom.get('saveMode').value=='saveview'){
					var vhId='<s:property value='%{voucherHeader.id}'/>';
					document.forms[0].action = "${pageContext.request.contextPath}/voucher/preApprovedVoucher!loadvoucherview.action?vhid="+vhId;
					document.forms[0].submit();
					
					
				}
			}
</script>

</body>

</html>
