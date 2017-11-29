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


<%@ taglib prefix="s" uri="/WEB-INF/tags/struts-tags.tld"%>
<%@ taglib prefix="egov" tagdir="/WEB-INF/tags"%>
<div class="formmainbox">
	<div class="formheading"></div>
	<div class="subheadnew">Auto Remittance Payment Report</div>
	<script type="text/javascript"
		src="${pageContext.request.contextPath}/resources/javascript/contra.js?rnd=${app_release_no}"></script>
	<script type="text/javascript"
		src="/EGF/resources/javascript/ajaxCommonFunctions.js?rnd=${app_release_no}"></script>
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
	<script>  
function loadBank(fund){
	if(fund.value!=-1){
		populatebank({fundId:fund.options[fund.selectedIndex].value})   
	}else{       
		populatebank()       
	} 
}
function validateFund(){
	var fund = document.getElementById('fundId').value;
	var bank = document.getElementById('bank');
	if(fund == -1 && bank.options.length==1){
		bootbox.alert("Please select a Fund")
		return false;
	}
	return true;
}
function populateAccNumbers(bankBranch){
	var fund = document.getElementById('fundId');
	id = bankBranch.options[bankBranch.selectedIndex].value.split("-")[1]
	populateaccountNumber({branchId:id,fundId:fund.options[fund.selectedIndex].value})	
}
function populateDO(){
	var dept = document.getElementById('department');
	populatedrawingOfficerId({departmentId:dept.options[dept.selectedIndex].value})	
}
function validateBank(){
	var bank = document.getElementById('bank').value;
	if(bank == -1){
		bootbox.alert("Please select a Bank")
		return false;
	}
	return true;
}
	var path="../..";
	var oAutoCompEntityForJV;
	function autocompleteEntities12By20(obj,type)
	{
		//bootbox.alert("in side fun");
	  	   oACDS = new YAHOO.widget.DS_XHR(path+"/EGF/voucher/common!ajaxLoadEntitesBy20.action", [ "~^"]);
		   oACDS.responseType = YAHOO.widget.DS_XHR.TYPE_FLAT;
		   oACDS.scriptQueryParam = "startsWith";
		 //  bootbox.alert("in side fun2"); 
		   
		   oAutoCompEntityForJV = new YAHOO.widget.AutoComplete(obj.name,'codescontainer',oACDS);
		   oAutoCompEntityForJV.doBeforeSendQuery = function(sQuery){
			   loadWaitingImage(); 
			   return sQuery+"&accountDetailType="+type;
		   } 
		  // bootbox.alert("in side fun3"); 
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
	function splitEntitiesDetailCode(obj) 
	{	
		var entity=obj.value;
		if(entity.trim()!="")
		{
			var entity_array=entity.split("`~`");
			if(entity_array.length==2)
			{
				document.getElementById("supplierCode").value=entity_array[0].split("`-`")[0];
			}
		}

	}
	function validateData(){
		var level = document.getElementById("level").value;
		var department = document.getElementById("department").value;
		var remittancecoc = document.getElementById("recovery").value;
		if(level == -1){
			bootbox.alert("Please select a level")
			return false;
			}
		if(level=="atdepartment"){
			if(department == -1){
				bootbox.alert("Please select a Department")
				return false;
				}
			}
		if(level=="atcoc"){
			if(remittancecoc == -1){
				bootbox.alert("Please select a Remittance CoA")
				return false;
				}
			}
		var bankAccount = document.getElementById('accountNumber').value;
		var bank = document.getElementById('bank').value;
		if(bank != -1){
			if(bankAccount == -1){
				bootbox.alert("Please select a Bank Account")
				return false;
			}
		}
		if(isNaN(startDate)){
			bootbox.alert("Please enter a valid start date")
			return false;
		}
		var endDate =  Date.parse(document.getElementById('endDate').value);
		if(isNaN(endDate)){
			bootbox.alert("Please enter a valid end date")
			return false;
		}
		return true;
	}
	function autocompleteRTGSNumbers(obj)
	{
		//bootbox.alert(obj.name);
	  	   oACDS = new YAHOO.widget.DS_XHR(path+"/EGF/voucher/common!ajaxLoadRTGSNumberBy20.action", [ "~^"]);
		   oACDS.responseType = YAHOO.widget.DS_XHR.TYPE_FLAT;
		   oACDS.scriptQueryParam = "startsWith";
		 //bootbox.alert("in side fun2"); 
		   
		   oAutoCompEntityForJV = new YAHOO.widget.AutoComplete(obj.name,'codescontainer',oACDS);
		   oAutoCompEntityForJV.doBeforeSendQuery = function(sQuery){
			   loadWaitingImage(); 
			   return sQuery+"&bankaccountId="+document.getElementById('accountNumber').value+"&rtgsNumber="+document.getElementById('instrumentnumber').value;
		   } 
		 // bootbox.alert(document.getElementById('accountNumber').value); 
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
	function getData(){
		var bankAccount = document.getElementById('accountNumber').value;
		isValid = validateData();
		if(isValid == false)
			return false;
		//var url = '/EGF/report/bankBookReport!ajaxLoadBankBook.action?skipPrepare=true&bankAccount.id='+bankAccount+'&startDate='+startDate+'&endDate='+endDate+getMiscData();
	//	YAHOO.util.Connect.asyncRequest('POST', url, callback, null);
	}
	
</script>

	<s:form action="autoRemittanceReport" theme="simple"
		name="autoRemittanceReport">
		<table width="100%" cellpadding="0" cellspacing="0" border="0">
			<tr>
				<td class="bluebox">Level:<span class="mandatory">*</span></td>
				<td class="bluebox"><s:select name="level" id="level"
						list="#{'atdepartment':'At Department','atcoc':'At CoC'}"
						headerKey="-1" headerValue="----Choose----" /></td>
				<td class="bluebox"></td>
				<td class="bluebox"></td>
			</tr>
			<tr>
				<td class="greybox">Payment Voucher From date:</td>
				<td class="greybox"><s:textfield name="paymentVoucherFromDate"
						id="paymentVoucherFromDate" cssStyle="width:100px"
						value='%{getFormattedDate(paymentVoucherFromDate)}'
						onkeyup="DateFormat(this,this.value,event,false,'3')" /><a
					href="javascript:show_calendar('autoRemittanceReport.paymentVoucherFromDate');"
					style="text-decoration: none">&nbsp;<img
						src="/egi/resources/erp2/images/calendaricon.gif" border="0" /></a>(dd/mm/yyyy)<br />
				</td>
				<td class="greybox"></td>
				<td class="greybox">Payment Voucher To date:</td>
				<td class="greybox"><s:textfield name="paymentVoucherToDate"
						id="paymentVoucherToDate" cssStyle="width:100px"
						value='%{getFormattedDate(paymentVoucherToDate)}'
						onkeyup="DateFormat(this,this.value,event,false,'3')" /><a
					href="javascript:show_calendar('autoRemittanceReport.paymentVoucherToDate');"
					style="text-decoration: none">&nbsp;<img
						src="/egi/resources/erp2/images/calendaricon.gif" border="0" /></a>(dd/mm/yyyy)<br />
				</td>
			</tr>
			<tr>
				<td class="bluebox">Department:</td>
				<td class="bluebox"><s:select name="department" id="department"
						list="dropdownData.departmentList" listKey="id"
						listValue="name" headerKey="-1" headerValue="----Choose----"
						onChange="populateDO();" /><font color="red">This is
						mandate if Level is Department</font></td>
				<td class="bluebox">Remittance CoA:</td>
				<td class="bluebox"><s:select name="recovery" id="recovery"
						list="dropdownData.recoveryList" listKey="id" listValue="type"
						headerKey="-1" headerValue="----Choose----" /><font color="red">This
						is mandate if Level is CoC</font></td>

			</tr>
			<tr>
				<egov:ajaxdropdown id="drawingOfficerId" fields="['Text','Value']"
					dropdownId="drawingOfficerId"
					url="voucher/common!ajaxLoadDrawingOfficers.action" />
				<td class="greybox">Drawing officer:</td>
				<td class="greybox"><s:select name="drawingOfficer"
						id="drawingOfficerId" list="dropdownData.drawingList" listKey="id"
						listValue="name" headerKey="-1" headerValue="----Choose----" /></td>
				<td class="greybox">Contractor Code/Name:</td>
				<td class="greybox"><input type="text" name="contractorCode"
					id="contractorCode" autocomplete="off"
					onfocus='autocompleteEntities12By20(this,5);'
					onblur='splitEntitiesDetailCode(this);' /></td>
			</tr>
			<tr>
				<td class="bluebox">Supplier Code/Name:</td>
				<td class="bluebox"><input type="text" name="supplierCode"
					id="supplierCode" autocomplete="off"
					onfocus='autocompleteEntities12By20(this,2);'
					onblur='splitEntitiesDetailCode(this);' /></td>
				<td class="bluebox">Fund:</td>
				<td class="bluebox"><s:select name="fundId" id="fundId"
						list="dropdownData.fundList" listKey="id" listValue="name"
						headerKey="" headerValue="----Choose----"
						onChange="loadBank(this);" value="%{fundId.id}" /></td>
			</tr>
			<tr>
				<td class="greybox">RTGS Assigned From Date:</td>
				<td class="greybox"><s:textfield name="rtgsAssignedFromDate"
						id="rtgsAssignedFromDate" cssStyle="width:100px"
						value='%{getFormattedDate(rtgsAssignedFromDate)}'
						onkeyup="DateFormat(this,this.value,event,false,'3')" /><a
					href="javascript:show_calendar('pendingTDSReport.rtgsAssignedFromDate');"
					style="text-decoration: none">&nbsp;<img
						src="/egi/resources/erp2/images/calendaricon.gif" border="0" /></a>(dd/mm/yyyy)<br />
				</td>
				<td class="greybox">RTGS Assigned To Date:</td>
				<td class="greybox"><s:textfield name="rtgsAssignedToDate"
						id="rtgsAssignedToDate" cssStyle="width:100px"
						value='%{getFormattedDate(rtgsAssignedToDate)}'
						onkeyup="DateFormat(this,this.value,event,false,'3')" /><a
					href="javascript:show_calendar('pendingTDSReport.rtgsAssignedToDate');"
					style="text-decoration: none">&nbsp;<img
						src="/egi/resources/erp2/images/calendaricon.gif" border="0" /></a>(dd/mm/yyyy)<br />
				</td>
			</tr>
			<tr>
				<egov:ajaxdropdown id="bank" fields="['Text','Value']"
					dropdownId="bank" url="voucher/common!ajaxLoadAllBanks.action" />
				<td class="bluebox">Bank:</td>
				<td class="bluebox"><s:select name="bank" id="bank"
						list="dropdownData.bankList" listKey="bankBranchId"
						listValue="bankBranchName" headerKey="-1"
						headerValue="----Choose----" onclick="validateFund()"
						onChange="populateAccNumbers(this);" /></td>
				<egov:ajaxdropdown id="accountNumber" fields="['Text','Value']"
					dropdownId="accountNumber"
					url="voucher/common!ajaxLoadAccountNumbers.action" />
				<td class="bluebox">Account Number:</td>
				<td class="bluebox"><s:select name="bankAccount"
						id="accountNumber" list="dropdownData.accNumList" listKey="id"
						listValue="accountnumber" headerKey="-1"
						headerValue="----Choose----" onclick="validateBank()" /></td>
			</tr>
			<tr>
				<td class="greybox">RTGS Number:</td>
				<td class="greybox"><input type="text" name="instrumentnumber"
					id="instrumentnumber" autocomplete="off"
					onfocus='autocompleteRTGSNumbers(this);' /></td>
				<td class="greybox"></td>
				<td class="greybox"></td>
			</tr>
		</table>
		<br />
		<div class="buttonbottom">
			<input type="button" value="Search" class="buttonsubmit"
				onclick="return getData()" /> &nbsp;
			<s:reset name="button" type="submit" cssClass="button" id="button"
				value="Cancel" />
			<input type="button" value="Close" onclick="javascript: self.close()"
				Class="button" />
		</div>
		<s:hidden name="detailKey" id="detailKey"></s:hidden>
		<div id="codescontainer" />
	</s:form>
</div>

<div id="results"></div>
