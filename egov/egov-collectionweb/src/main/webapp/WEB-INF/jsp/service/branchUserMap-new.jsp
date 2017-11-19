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
<html>
<head>
<title>  <s:text name="branchuser.master.title"></s:text> </title>

<script>
function onChangeBankBranch(bankId) {
	populatebranchId({
		bankId : bankId,
	});
}
function onSubmit(obj){
	if(validate())
	{
	  document.forms[0].action=obj;
	   document.forms[0].submit;
	   return true;
	}
	else
		return false 
}
function validate() {
	var valid = true;
	document.getElementById('error_area').innerHTML = '';
	document.getElementById("error_area").style.display = "none";
	if (document.getElementById('bankId').value == "-1") {
		document.getElementById("error_area").innerHTML = '<s:text name="branchuser.master.bank.error" />';
		valid = false;
	}
	else if (document.getElementById('branchId').value == "-1") {
		document.getElementById("error_area").innerHTML = '<s:text name="branchuser.master.branch.error" />';
		valid = false;
	}
	else if (document.getElementById('userId').value == "-1") {
		document.getElementById("error_area").innerHTML = '<s:text name="branchuser.master.user.error" />';
		valid = false;
	}
	if (valid == false) {
		document.getElementById("error_area").style.display = "block";
		window.scroll(0, 0);
	}
	return valid;
}

</script>
</head>

<body>
<div class="errorstyle" id="error_area" style="display: none;"></div>

	<span id="remerror"> <font style='color: red; font-weight: bold'
		size="2"> <s:actionerror /> <s:fielderror />
	</font>
	</span>

<s:form action="branchUserMap.action" theme="simple">
   <div class="errorstyle" id="error_area" style="display: none;"></div>
   <s:token />
    <div class="formmainbox">
		<div class="subheadnew">
			 <s:text name="branchuser.master.create.title" />
		</div>
	<table width="100%" border="0" cellspacing="0" cellpadding="0" style="max-width:960px;margin:0 auto;"  theme="simple">
	   	
		<tr>
			<td class="greybox"><s:text name="branchuser.master.bank"></s:text> <span class="mandatory"></span></td>
			<td class="greybox"><s:select headerKey="-1"
								headerValue="----Choose----" name="bankId" id="bankId"
								cssClass="selectwk" list="dropdownData.bankNameList"
								listKey="id" listValue="name" value="%{bank.id}"
								onchange="onChangeBankBranch(this.value)" /> <egov:ajaxdropdown
								id="branchIdDropdown" fields="['Text','Value']"
								dropdownId='branchId'
								url='service/branchUserMap-bankBranchsByBankForReceiptPayments.action' /></td>
			<td class="greybox"><s:text name="branchuser.master.bankbranch" />
							<span class="mandatory" /></td>
			<td class="greybox"><s:select headerKey="-1"
								headerValue="----Choose----" name="branchId" id="branchId"
								cssClass="selectwk" list="dropdownData.bankBranchList"
								listKey="id" listValue="branchname"
								value="%{bankbranch.id}" />
		</tr>
		<tr>
			<td class="bluebox"><s:text name="branchuser.master.bankuser"></s:text> <span class="mandatory"></span></td>
			<td class="bluebox"><s:select headerKey="-1"
								headerValue="----Choose----" name="userId" id="userId"
								cssClass="selectwk" list="dropdownData.bankCollectionOperatorUserList"
								listKey="id" listValue="username" value="%{bankuser.id}"/></td>
			<td class="bluebox">&nbsp;</td>
			<td class="bluebox">&nbsp;</td>
		</tr>
		<tr>
			<td class="greybox"><label for="isActive"><s:text
						name="branchuser.master.IsEnable/Disable" /> </label></td>
			<td class="greybox"><s:checkbox name="isActive" id="isActive" /></td>
		</tr>
	</table>
	<div align="left" class="mandatorycoll">
					&nbsp;&nbsp;&nbsp;
		<s:text name="common.mandatoryfields" />
    </div>
	<br />
</div>
    <div class="buttonbottom">
				<s:submit name="sumbit" cssClass="buttonsubmit" id="button32" 
					onclick="return onSubmit('branchUserMap-create.action'); "
					value="Create Mapping" />
				<s:reset name="reset" cssClass="button" id="button" value="Reset" />
				<input name="close" type="button" class="button" id="button"
					onclick="window.close()" value="Close" />
	</div>
</s:form>
</body>
</html>
