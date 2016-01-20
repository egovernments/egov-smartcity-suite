<!--  #-------------------------------------------------------------------------------
# eGov suite of products aim to improve the internal efficiency,transparency, 
#      accountability and the service delivery of the government  organizations.
#   
#       Copyright (C) <2015>  eGovernments Foundation
#   
#       The updated version of eGov suite of products as by eGovernments Foundation 
#       is available at http://www.egovernments.org
#   
#       This program is free software: you can redistribute it and/or modify
#       it under the terms of the GNU General Public License as published by
#       the Free Software Foundation, either version 3 of the License, or
#       any later version.
#   
#       This program is distributed in the hope that it will be useful,
#       but WITHOUT ANY WARRANTY; without even the implied warranty of
#       MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
#       GNU General Public License for more details.
#   
#       You should have received a copy of the GNU General Public License
#       along with this program. If not, see http://www.gnu.org/licenses/ or 
#       http://www.gnu.org/licenses/gpl.html .
#   
#       In addition to the terms of the GPL license to be adhered to in using this
#       program, the following additional terms are to be complied with:
#   
#   	1) All versions of this program, verbatim or modified must carry this 
#   	   Legal Notice.
#   
#   	2) Any misrepresentation of the origin of the material is prohibited. It 
#   	   is required that all modified versions of this material be marked in 
#   	   reasonable ways as different from the original version.
#   
#   	3) This license does not grant any rights to any user of the program 
#   	   with regards to rights under trademark law for use of the trade names 
#   	   or trademarks of eGovernments Foundation.
#   
#     In case of any queries, you can reach eGovernments Foundation at contact@egovernments.org.
#-------------------------------------------------------------------------------  -->
<%@ include file="/includes/taglibs.jsp"%>
<%@ page language="java"%>


<html>
<head>
<meta http-equiv="Content-Type"
	content="text/html; charset=windows-1252">
<style type="text/css">
#codescontainer {
	position: absolute;
	left: 11em;
	width: 9%;
	text-align: left;
}

#codescontainer .yui-ac-content {
	position: absolute;
	width: 600px;
	border: 1px solid #404040;
	background: #fff;
	overflow: hidden;
	z-index: 9050;
}

#codescontainer .yui-ac-shadow {
	position: absolute;
	margin: .3em;
	width: 300px;
	background: #a0a0a0;
	z-index: 9049;
}

#codescontainer ul {
	padding: 5px 0;
	width: 100%;
}

#codescontainer li {
	padding: 0 5px;
	cursor: default;
	white-space: nowrap;
}

#codescontainer li.yui-ac-highlight {
	background: #ff0;
}

#codescontainer li.yui-ac-prehighlight {
	background: #FFFFCC;
}
</style>
<title><s:text name="rtgs.issueregister.report" /></title>

</head>
<script>
	function doAfterSubmit(){
		document.getElementById('loading').style.display ='block';
	}
	
	function populateBankBranch(bank){
		var bankId= bank.options[bank.selectedIndex].value;
		populatebankbranch({bankId:bankId})
	}
	function populateBankAccount(branch){
		var branchId= branch.options[branch.selectedIndex].value;
		populatebankaccount({branchId:branchId})
	}
	function populateInstrumentNumber(instrument){
		var bankaccountId= instrument.options[instrument.selectedIndex].value;
		populateinstrumentnumber({bankaccountId:bankaccountId})
	}
	var path="../..";
	var oAutoCompEntityForJV;
	function autocompleteRTGSNumbers(obj)
	{
	  	   oACDS = new YAHOO.widget.DS_XHR(path+"/EGF/voucher/common!ajaxLoadRTGSNumberByAccountId.action", [ "~^"]);
		   oACDS.responseType = YAHOO.widget.DS_XHR.TYPE_FLAT;
		   oACDS.scriptQueryParam = "startsWith";
		   
		   oAutoCompEntityForJV = new YAHOO.widget.AutoComplete(obj.name,'codescontainer',oACDS);
		   oAutoCompEntityForJV.doBeforeSendQuery = function(sQuery){
			   return sQuery+"&bankaccountId="+document.getElementById('bankaccount').value+"&rtgsNumber="+document.getElementById('instrumentnumber').value;
		   } 
		   oAutoCompEntityForJV.queryDelay = 0.5;
		   oAutoCompEntityForJV.minQueryLength = 3;
		   oAutoCompEntityForJV.prehighlightClassName = "yui-ac-prehighlight";
		   oAutoCompEntityForJV.useShadow = true;
		   oAutoCompEntityForJV.forceSelection = true;
		   oAutoCompEntityForJV.maxResultsDisplayed = 20;
		   oAutoCompEntityForJV.useIFrame = true;
		   oAutoCompEntityForJV.doBeforeExpandContainer = function(oTextbox, oContainer, sQDetauery, aResults) {
			   clearWaitingImage();
		           var pos = YAHOO.util.Dom.getXY(oTextbox);
		           pos[1] += YAHOO.util.Dom.get(oTextbox).offsetHeight + 6;
		           oContainer.style.width=300;
		           YAHOO.util.Dom.setXY(oContainer,pos);
		           return true;
		   }
	}
</script>
<body>
	<s:form action="rtgsIssueRegisterReport" name="rtgsIssueRegisterReport"
		theme="simple" method="post" onsubmit="javascript:doAfterSubmit()">
		<span class="mandatory"> <s:actionerror /> <s:fielderror /> <s:actionmessage />
		</span>
		<font style='color: red; font-weight: bold'>
			<p class="error-block" id="lblError"></p>
		</font>
		<div class="formmainbox">
			<div class="subheadnew">
				<s:text name="rtgs.issue.report" />
			</div>
		</div>
		<table align="center" width="100%" cellpadding="0" cellspacing="0">
			<tr>
				<td class="bluebox" width="10%"><s:text name="voucher.fund" /><span
					class="mandatory">*</span></td>
				<td class="bluebox"><s:select name="fundId" id="fundId"
						list="dropdownData.fundList" listKey="id" listValue="name"
						headerKey="-1" headerValue="----Choose----"
						onChange="loadBank(this);" value="%{fundId.id}" /></td>
				<td class="bluebox" width="10%"><s:text
						name="voucher.department" />
				<td class="bluebox"><s:select name="departmentid"
						id="departmentid" list="dropdownData.departmentList" listKey="id"
						listValue="name" headerKey="-1" headerValue="----Choose----"
						onChange="alertWhileSelectingDepartment(this);"
						value="%{departmentId.id}" /></td>
			</tr>
			<tr>
				<td class="greybox"><s:text name="report.rtgsassignedfromdate" />:</td>
				<td class="greybox"><s:textfield name="rtgsAssignedFromDate"
						id="rtgsAssignedFromDate" cssStyle="width:100px"
						value='%{getFormattedDate(fromDate)}'
						onkeyup="DateFormat(this,this.value,event,false,'3')" /><a
					href="javascript:show_calendar('rtgsIssueRegisterReport.rtgsAssignedFromDate');"
					style="text-decoration: none">&nbsp;<img
						src="/egi/resources/erp2/images/calendaricon.gif" border="0" /></a>(dd/mm/yyyy)<br />
				</td>
				<td class="greybox"><s:text name="report.rtgsassignedtodate" />:</td>
				<td class="greybox"><s:textfield name="rtgsAssignedToDate"
						id="rtgsAssignedToDate" cssStyle="width:100px"
						value='%{getFormattedDate(toDate)}'
						onkeyup="DateFormat(this,this.value,event,false,'3')" /><a
					href="javascript:show_calendar('rtgsIssueRegisterReport.rtgsAssignedToDate');"
					style="text-decoration: none">&nbsp;<img
						src="/egi/resources/erp2/images/calendaricon.gif" border="0" /></a>(dd/mm/yyyy)<br />
				</td>
			</tr>
			<tr>
				<egov:ajaxdropdown id="bank" fields="['Text','Value']"
					dropdownId="bank"
					url="voucher/common!ajaxLoadAllBanksByFund.action" />
				<td class="bluebox" width="10%"><s:text name="bank" />:</td>
				<td class="bluebox"><s:select name="bank" id="bank"
						list="dropdownData.bankList" listKey="id" listValue="name"
						headerKey="-1" headerValue="----Choose----"
						onclick="validateFund()" onChange="populateBankBranch(this);" /></td>
				<egov:ajaxdropdown id="bankbranch" fields="['Text','Value']"
					dropdownId="bankbranch"
					url="voucher/common!ajaxLoadBankBranchFromBank.action" />
				<td class="bluebox" width="10%"><s:text name="bankbranch" />:</td>
				<td class="bluebox"><s:select name="bankbranch.id"
						id="bankbranch" list="dropdownData.bankBranchList" listKey="id"
						listValue="branchname" headerKey="-1" headerValue="----Choose----"
						onChange="populateBankAccount(this);" /></td>
			</tr>
			<tr>
				<egov:ajaxdropdown id="bankaccount" fields="['Text','Value']"
					dropdownId="bankaccount"
					url="voucher/common!ajaxLoadBankAccFromBranch.action" />
				<td class="greybox" width="10%"><s:text name="bankaccount" />:</td>
				<td class="greybox"><s:select name="bankaccount.id"
						id="bankaccount" list="dropdownData.accNumList" listKey="id"
						listValue="accountnumber" headerKey="-1"
						headerValue="----Choose----" /></td>
				<td class="greybox"><s:text name="report.rtgsnumber" />:</td>
				<td class="greybox"><input type="text" name="instrumentnumber"
					id="instrumentnumber" autocomplete="off"
					onfocus='autocompleteRTGSNumbers(this);' /></td>

			</tr>
			<tr>

			</tr>
		</table>
		<div class="buttonbottom">
			<s:submit method="exportHtml" value="Search" cssClass="buttonsubmit"
				onclick="return validateFund()" />
			<s:submit method="exportPdf" value="Save As Pdf"
				cssClass="buttonsubmit" onclick="return validateFund()" />
			<s:submit method="exportXls" value="Save As Xls"
				cssClass="buttonsubmit" onclick="return validateFund()" />
			<input type="button" value="Close"
				onclick="javascript:window.close()" class="button" />

		</div>
		<div id="loading" class="loading"
			style="width: 700; height: 700; display: none" align="center">
			<blink style="color: red">Searching processing, Please
				wait...</blink>
		</div>
		<br>
		<s:if test="%{searchResult}">
			<logic:empty name="rtgsDisplayList">
				<blink>Nothing found to display.</blink>
			</logic:empty>
		</s:if>
		<div id="codescontainer" />
	</s:form>

	<script>


function validateFund(){
	var fund = document.getElementById('fundId').value;
	var bank = document.getElementById('bank');
	if(fund == -1){
		bootbox.alert("Please select a Fund");
		return false;
	}
	if(fund == -1 && bank.options.length==1){
		bootbox.alert("Please select a Fund");
		return false;
	}
	return true;
}
function alertWhileSelectingDepartment(){
	var department = document.getElementById('departmentid').value;
	if(department == -1){
		return true;
	}else{
		bootbox.alert("The payments that are not of the selected department in the respective RTGS transaction will be not be shown here. Please unselect department if you wish to see all the payments in the RTGS transactions");
		return true;
		}
	
}
	function loadBank(fund){
		if(fund.value!=-1){
			populatebank({fundId:fund.options[fund.selectedIndex].value})   
		}else{       
			populatebank()       
		} 
	}
	
 function exportPDF() {
 	 var bank=document.getElementById("bank.id").value;
 	 var bankbranch=document.getElementById("bankbranch").value;
 	 var bankaccount=document.getElementById("bankaccount").value;
 	 var instrumentnumber=document.getElementById("instrumentnumber").value;
	 var url="${pageContext.request.contextPath}/report/bankAdviceReport!exportPDF.action?bank.id="+bank+"&bankbranch.id="+bankbranch+"&bankaccount.id="+bankaccount+"&instrumentnumber.id="+instrumentnumber;
	 window.open(url,'','height=650,width=980,scrollbars=yes,left=0,top=0,status=yes');
	 }
 function exportExcel() {
 	 var bank=document.getElementById("bank.id").value;
 	 var bankbranch=document.getElementById("bankbranch").value;
 	 var bankaccount=document.getElementById("bankaccount").value;
 	 var instrumentnumber=document.getElementById("instrumentnumber").value;
	 var url="${pageContext.request.contextPath}/report/bankAdviceReport!exportExcel.action?bank.id="+bank+"&bankbranch.id="+bankbranch+"&bankaccount.id="+bankaccount+"&instrumentnumber.id="+instrumentnumber;
	 window.open(url,'','height=650,width=980,scrollbars=yes,left=0,top=0,status=yes');
}
 function exportHtml() {
 	 var bank=document.getElementById("bank.id").value;
 	 var bankbranch=document.getElementById("bankbranch").value;
 	 var bankaccount=document.getElementById("bankaccount").value;
 	 var instrumentnumber=document.getElementById("instrumentnumber").value;
	 var url="${pageContext.request.contextPath}/report/bankAdviceReport!exportHtml.action?bank.id="+bank+"&bankbranch.id="+bankbranch+"&bankaccount.id="+bankaccount+"&instrumentnumber.id="+instrumentnumber;
	 window.open(url,'','height=650,width=980,scrollbars=yes,left=0,top=0,status=yes');
}

</script>
</body>
</html>
