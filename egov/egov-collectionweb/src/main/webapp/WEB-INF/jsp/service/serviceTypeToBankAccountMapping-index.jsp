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
<title></title>
<script>
	function onChangeBankBranch(bankId) {
		dom.get("bankAccountId").value = "-1";
		populatebranchId({
			bankId : bankId,
		});
	}

	function onChangeBankAccount(branchId,serviceId) {
		populatebankAccountId({
			branchId : branchId,
			serviceId : serviceId,
		});
	}
	function populateService(serviceId) {
		document.getElementById('serviceDetailsId').value = "-1"
		populateserviceDetailsId({
			serviceCatId : serviceId,
		});
	}

	function validate(obj){
		dom.get('error_area').innerHTML = '';
		dom.get("error_area").style.display="none";
		<s:if test="%{null != bankAccountServices && bankAccountServices.size() >0}">
			if(dom.get('serviceAccountId').value == "") {
				dom.get("error_area").innerHTML = '<s:text name="service.error.select" />';
				dom.get("error_area").style.display="block";
				return false;
			}
		</s:if>
		document.forms[0].action=obj;
		document.forms[0].submit;
	}

	function resetValues()
	{
		jQuery("select").val(-1);
	}
</script>
</head>
<body>
	<s:form name="serviceBankMappingForm" method="post" theme="simple">
		<s:push value="model">
			<div class="errorstyle" id="error_area" style="display: none;"></div>

			<div class="formmainbox">
				<div class="subheadnew">
					<s:text name="service.master.bankmappping.view.header" />
				</div>

				<table width="100%" border="0" cellspacing="0" cellpadding="0"
					style="max-width: 960px; margin: 0 auto;">
					<tr>
						<td class="bluebox">&nbsp;</td>
						<td class="bluebox"><s:text name="service.master.bankname" />
							</td>
						<td class="bluebox"><s:select headerKey="-1"
								headerValue="----Choose----" name="bankId" id="bankId"
								cssClass="selectwk" list="dropdownData.bankNameList"
								listKey="id" listValue="name" value="%{bankId}"
								onchange="onChangeBankBranch(this.value)" /> <egov:ajaxdropdown
								id="bankIdDropdown" fields="['Text','Value']"
								dropdownId='branchId'
								url='receipts/ajaxBankRemittance-bankBranchsByBankForReceiptPayments.action' /></td>
						<td class="bluebox"><s:text name="service.master.branchName" />
							</td>
						<td class="bluebox"><s:select headerKey="-1"
								headerValue="----Choose----" name="branchId" id="branchId"
								cssClass="selectwk" list="dropdownData.bankBranchList"
								listKey="id" listValue="branchname" value="%{branchId}"
								onChange="onChangeBankAccount(this.value,document.getElementById('serviceDetailsId').value)" /> <egov:ajaxdropdown
								id="bankbranchIdDropDown" fields="['Text','Value']"
								dropdownId='bankAccountId'
								url='receipts/ajaxBankRemittance-bankAccountByBankBranch.action' /></td>
					</tr>
					<tr>
						<td class="bluebox">&nbsp;</td>
						<td class="bluebox"><s:text
								name="service.master.accountnumber" /> </td>
						<td class="bluebox"><s:select headerKey="-1"
								headerValue="----Choose----" name="bankAccountId"
								id="bankAccountId" cssClass="selectwk" 
								list="dropdownData.bankAccountIdList" listKey="id"
								listValue="accountnumber" value="%{bankAccountId.id}" /></td>
					</tr>
					<tr>
						<td class="bluebox">&nbsp;</td>
						<td class="bluebox"><s:text
								name="service.master.search.category" /></td>
						<td class="bluebox"><s:select headerKey="-1"
								headerValue="----Choose----" name="serviceCategory"
								id="serviceCategory" cssClass="selectwk"
								list="dropdownData.serviceCategoryList" listKey="id"
								listValue="name" value="%{serviceCategory}"
								onChange="populateService(this.value);" /> <egov:ajaxdropdown
								id="service" fields="['Text','Value']"
								dropdownId="serviceDetailsId"
								url="receipts/ajaxReceiptCreate-ajaxLoadServiceByCategory.action" /></td>
						<td class="bluebox"><s:text name="service.master.servicetype" />
							</td>
						<td class="bluebox"><s:select headerKey="-1"
								headerValue="----Choose----" name="serviceDetails"
								id="serviceDetailsId" cssClass="selectwk"
								list="dropdownData.serviceDetailsList" listKey="id"
								listValue="name" value="%{serviceDetails.id}" /></td>
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
					onclick="document.serviceBankMappingForm.action='serviceTypeToBankAccountMapping-search.action';"
					value="View" />
					<input type="button" class="button" value="Reset"
						id="resetbutton" name="clear" onclick="resetValues();">
				<input name="close" type="button" class="button" id="button"
					onclick="window.close()" value="Close" />
			</div>
			<s:hidden id="serviceAccountId" name="serviceAccountId" />
			<s:hidden id="sourcePage" name="sourcePage" value="modify" />
			<div>
				<s:if
					test="%{null != bankAccountServices && bankAccountServices.size() >0}">
					<div align="center">
						<table width="100%" border="1">
							<tr>
								<th class="bluebgheadtd"><s:text
										name="service.select.table.header" /></th>
								<th class="bluebgheadtd" style="text-align: left;"><s:text
										name="service.master.search.category" /></th>
								<th class="bluebgheadtd" style="text-align: left;"><s:text
										name="service.master.servicetype" /></th>
								<th class="bluebgheadtd" style="text-align: left;"><s:text
										name="service.master.bankname" /></th>
								<th class="bluebgheadtd" style="text-align: left;"><s:text
										name="service.master.branchName" /></th>
								<th class="bluebgheadtd" style="text-align: left;"><s:text
										name="billreceipt.accountdetails.description" /></th>
								<th class="bluebgheadtd" style="text-align: left;"><s:text
										name="service.master.accountnumber" /></th>
								<th class="bluebgheadtd" style="text-align: left;"><s:text
										name="service.master.bankmappping.glcode" /></th>
							</tr>
							<s:iterator var="p" value="%{bankAccountServices}" status="s">
								<tr>
									<td width="5%" class="bluebox"><input type="radio"
										onclick='dom.get("serviceAccountId").value = <s:property value="id"/>'
										name="radioButton1" /></td>
									<td class="bluebox"><div align="left">
											<s:property value="serviceDetails.serviceCategory.name" />
										</div></td>
									<td class="bluebox"><div align="left">
											<s:property value="serviceDetails.name" />
										</div></td>
									<td class="bluebox"><div align="left">
											<s:property value="bankAccountId.bankbranch.bank.name" />
										</div></td>
									<td class="bluebox"><div align="left">
											<s:property value="bankAccountId.bankbranch.branchname" />
										</div></td>
									<td class="bluebox"><div align="left">
											<s:property value="bankAccountId.narration" />
										</div></td>
									<td class="bluebox"><div align="left">
											<s:property value="bankAccountId.accountnumber" />
										</div></td>
									<td class="bluebox"><div align="left">
											<s:property value="bankAccountId.chartofaccounts.glcode" />
										</div></td>
								</tr>
							</s:iterator>
						</table>
						<s:submit type="submit" cssClass="buttonsubmit" id="button32"
							value="Modify"
							onclick="return validate('serviceTypeToBankAccountMapping-newform.action');" />
						<input type="button" id="Close" value="Close"
							onclick="javascript:window.close()" class="buttonsubmit" />
					</div>
				</s:if>
				<s:else>
					<s:if test="target=='searchresult'">
					<table width="90%" border="0" align="center" cellpadding="0"
						cellspacing="0" class="tablebottom">
						<tr>
							<div>&nbsp;</div>
							<div class="subheadnew">
								<s:text name="searchresult.norecord" />
							</div>
						</tr>
					</table>
					</s:if>
				</s:else>
			</div>
		</s:push>
	</s:form>
</body>
</html>
