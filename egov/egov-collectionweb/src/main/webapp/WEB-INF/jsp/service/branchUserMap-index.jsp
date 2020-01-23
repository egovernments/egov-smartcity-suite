<%--
  ~    eGov  SmartCity eGovernance suite aims to improve the internal efficiency,transparency,
  ~    accountability and the service delivery of the government  organizations.
  ~
  ~     Copyright (C) 2018  eGovernments Foundation
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
	populatebranchId({
		bankId : bankId,
	});
}
	function validate(obj){
		dom.get('error_area').innerHTML = '';
		dom.get("error_area").style.display="none";
		if(dom.get('branchUserMapId').value == "" || dom.get('branchUserMapId').value == null ) {
			dom.get("error_area").innerHTML = '<s:text name="branchuser.master.modify.validate.selectrecord" />';
			dom.get("error_area").style.display="block";
			return false;
		}
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

	<s:if test="%{hasErrors()}">
		<div align="center">
			<div id="actionErrorMessages" class="alert alert-danger">
				<s:actionerror />
				<s:fielderror />
			</div>
		</div>
	</s:if>
	<s:form name="branchUserMapForm" method="post" theme="simple">
		<s:push value="model">
			<div class="errorstyle" id="error_area" style="display: none;"></div>
			<div class="formmainbox">
				<div class="subheadnew">
					<s:if test="mode=='view'">
						<s:text name="branchuser.master.view.title" />
					</s:if>
					<s:elseif test="mode=='modify'">
						<s:text name="branchuser.master.modify.title" />
					</s:elseif>
				</div>

				<table width="100%" border="0" cellspacing="0" cellpadding="0"
					style="max-width: 960px; margin: 0 auto;">
					<tr>
						<td class="greybox"><s:text name="branchuser.master.bank" /></td>
						<td class="greybox"><s:select headerKey="-1"
								headerValue="----Choose----" name="bankId" id="bankId"
								cssClass="selectwk" list="dropdownData.bankNameList"
								listKey="id" listValue="name" value="%{bankId}"
								onchange="onChangeBankBranch(this.value)" /> <egov:ajaxdropdown
								id="branchIdDropdown" fields="['Text','Value']"
								dropdownId='branchId'
								url='service/branchUserMap-bankBranchsByBankForReceiptPayments.action' /></td>
						<td class="greybox"><s:text
								name="branchuser.master.bankbranch" /></td>
						<td class="greybox"><s:select headerKey="-1"
								headerValue="----Choose----" name="branchId" id="branchId"
								cssClass="selectwk" list="dropdownData.bankBranchList"
								listKey="id" listValue="branchname" value="%{branchId}" />
					</tr>
					<tr>
						<td class="bluebox"><s:text name="branchuser.master.bankuser" /></td>
						<td class="bluebox"><s:select headerKey="-1"
								headerValue="----Choose----" name="userId" id="userId"
								cssClass="selectwk"
								list="dropdownData.mappedBankCollectionOperatorUserList"
								listKey="id" listValue="username" value="%{userId}" /></td>
						<td class="bluebox">&nbsp;</td>
						<td class="bluebox">&nbsp;</td>
					</tr>
					<input type="hidden" name="mode" id="mode" value="${mode}" />
					<input type="hidden" name="branchUserMapId" id="branchUserMapId"
						value="${branchUserMapId}" />
				</table>
			</div>
			<div class="buttonbottom">
				<s:if test="mode=='view'">
					<s:submit name="sumbit" cssClass="buttonsubmit" id="button32"
						onclick="document.branchUserMapForm.action='branchUserMap-search.action';"
						value="View" />
				</s:if>
				<s:elseif test="mode=='modify'">
					<s:submit name="sumbit" cssClass="buttonsubmit" id="button32"
						onclick="document.branchUserMapForm.action='branchUserMap-search.action';"
						value="Search" />
				</s:elseif>
				<input type="button" class="button" value="Reset" id="resetbutton"
					name="clear" onclick="resetValues();"> <input name="close"
					type="button" class="button" id="button" onclick="window.close()"
					value="Close" />
			</div>
			<%-- <s:hidden id="serviceAccountId" name="serviceAccountId" />
			<s:hidden id="sourcePage" name="sourcePage" value="modify" /> --%>
			<div>
				<s:if test="%{null != branchUserList && branchUserList.size() >0}">
					<div align="center">
						<table width="100%" border="1">
							<tr>
								<s:if test="mode=='modify'">
									<th class="bluebgheadtd"><s:text
											name="service.select.table.header" /></th>
								</s:if>
								<s:elseif test="mode=='view'">
									<th class="bluebgheadtd"><s:text
											name="branchuser.search.lbl.sno" /></th>
								</s:elseif>
								<th class="bluebgheadtd" style="text-align: left;"><s:text
										name="branchuser.search.header.bank" /></th>
								<th class="bluebgheadtd" style="text-align: left;"><s:text
										name="branchuser.search.header.bankbranch" /></th>
								<th class="bluebgheadtd" style="text-align: left;"><s:text
										name="branchuser.search.header.bankuser" /></th>
								<th class="bluebgheadtd" style="text-align: left;"><s:text
										name="branchuser.search.header.IsEnable/Disable" /></th>
							</tr>
							<s:iterator var="p" value="%{branchUserList}" status="s">
								<tr>
									<s:if test="mode=='modify'">
										<td width="5%" class="bluebox"><input type="radio"
											onclick='dom.get("branchUserMapId").value = <s:property value="id"/>'
											name="radioButton1" /></td>
									</s:if>
									<s:elseif test="mode=='view'">
										<td width="5%" class="bluebox">
											<div align="center">
												<s:property value="#s.index+1" />
											</div>
										</td>
									</s:elseif>
									<td class="bluebox"><div align="left">
											<s:property value="bankbranch.bank.name" />
										</div></td>
									<td class="bluebox"><div align="left">
											<s:property value="bankbranch.branchname" />
										</div></td>
									<td class="bluebox"><div align="left">
											<s:property value="bankuser.username" />
										</div></td>
									<td class="bluebox">
										<div align="left">
											<s:if test="isActive">
												<s:text name="text.yes"></s:text>
											</s:if>
											<s:else>
												<s:text name="text.no"></s:text>
											</s:else>
										</div>
									</td>
								</tr>
							</s:iterator>
						</table>
						<s:if test="mode=='modify'">
							<s:submit type="submit" cssClass="buttonsubmit" id="button32"
								value="Modify"
								onclick="return validate('branchUserMap-beforeModify.action');" />
						</s:if>
						<input type="button" id="Close" value="Close"
							onclick="javascript:window.close()" class="buttonsubmit" />
					</div>
				</s:if>
				<s:else>
					<s:if test='%{branchUserList.isEmpty()}'>
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
