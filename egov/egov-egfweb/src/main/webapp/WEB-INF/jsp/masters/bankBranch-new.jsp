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
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ taglib prefix="egov" tagdir="/WEB-INF/tags"%>

<%@ page language="java"%>
<html>
<head>

<title><s:text name="bank.branch.create.new" /></title>
<SCRIPT type="text/javascript">
    function checkuniquenessmicr(){
    	document.getElementById('micruniquemicr').style.display ='none';
		var micr=document.getElementById('branchMICR').value;
		populatemicruniquemicr({branchMICR:micr});
    }
    
    </SCRIPT>
</head>
<body>
	<div class="formmainbox">
		<div class="subheadnew">
			<s:text name="bank.branch.create" />
		</div>
		<div style="color: red">
			<s:actionerror />
			<s:fielderror />
		</div>
		<div style="color: green">
			<s:actionmessage theme="simple" />
		</div>
		<div class="errorstyle" style="display: none" id="micruniquemicr">
			<s:text name="micr.code.already.exists" />
		</div>

		<s:form name="bankBranchForm" action="bankBranch" theme="simple">
			<s:token />
			<s:push value="model">
				<s:hidden id="id" name="id" />
				<table width="100%" border="0" cellspacing="0" cellpadding="0">
					<tr>
						<td class="bluebox" width="10%"></td>
						<td class="bluebox" width="10%"><s:text
								name="bank.name.select" /><span class="mandatory">*</span></td>
						<td class="bluebox"><s:select list="dropdownData.bankList"
								listKey="id" listValue="name" id="bank" name="bank"
								headerKey="0" headerValue="--- Select ---" value="%{bank.id}"></s:select>
						</td>
					</tr>
					<tr>
						<td class="greybox" width="10%"></td>
						<td class="greybox" width="10%"><s:text
								name="bank.branch.code" /></td>
						<td class="greybox" width="46%"><s:textfield
								id="bankBranch.branchcode" name="bankBranch.branchcode" /></td>
						<td class="greybox" width="2%"><s:text
								name="bank.create.isactive" /></td>
						<td class="greybox"><s:checkbox id="isActive" name="isActive"
								value="%{isActive}" /></td>
					</tr>
					<tr>
						<td class="bluebox" width="10%"></td>
						<td class="bluebox" width="10%"><s:text
								name="bank.branch.name" /></td>
						<td class="bluebox" width="46%"><s:textfield
								id="bankBranch.branchname" name="bankBranch.branchname" /></td>
						<td class="bluebox" width="10%"><s:text
								name="bank.branch.micr" /></td>
						<td class="bluebox" width="46%"><s:textfield id="branchMICR"
								name="branchMICR" onblur="checkuniquenessmicr();" /></td>
						<egov:uniquecheck id="micruniquemicr" name="micruniquemicr"
							fieldtoreset="branchMICR" fields="['Value']"
							url='masters/bankBranch!micrUniqueCheckMicr.action' />
					</tr>
					<tr>
						<td class="greybox" width="10%"></td>
						<td class="greybox" width="10%"><s:text
								name="bank.branch.addres.one" /></td>
						<td class="greybox" width="46%"><s:textfield
								id="bankBranch.branchaddress1" name="bankBranch.branchaddress1" /></td>
						<td class="greybox" width="10%"><s:text
								name="bank.branch.pin" /></td>
						<td class="greybox" width="46%"><s:textfield
								id="bankBranch.branchpin" name="bankBranch.branchpin" /></td>
					</tr>
					<tr>
						<td class="bluebox" width="10%"></td>
						<td class="bluebox" width="10%"><s:text
								name="bank.branch.addres.two" /></td>
						<td class="bluebox" width="46%"><s:textfield
								id="bankBranch.branchaddress2" name="bankBranch.branchaddress2" /></td>
					</tr>
					<tr>
						<td class="greybox" width="10%"></td>
						<td class="greybox" width="10%"><s:text
								name="bank.branch.city" /></td>
						<td class="greybox" width="46%"><s:textfield
								id="bankBranch.branchcity" name="bankBranch.branchcity" /></td>
						<td class="greybox" width="10%"><s:text
								name="bank.branch.fax" /></td>
						<td class="greybox" width="46%"><s:textfield
								id="bankBranch.branchfax" name="bankBranch.branchfax" /></td>
					</tr>
					<tr>
						<td class="bluebox" width="10%"></td>
						<td class="bluebox" width="10%"><s:text
								name="bank.branch.phone" /></td>
						<td class="bluebox" width="46%"><s:textfield
								id="bankBranch.branchphone" name="bankBranch.branchphone" /></td>
						<td class="bluebox" width="10%"><s:text
								name="bank.branch.contact.person" /></td>
						<td class="bluebox" width="46%"><s:textfield
								id="bankBranch.contactperson" name="bankBranch.contactperson" /></td>
					</tr>

				</table>
				<table width="100%" border="0" cellspacing="0" cellpadding="0">
					<tr>
						<td class="greybox" width="10%"></td>
						<td class="greybox" width="10%"><s:text
								name="bank.branch.narration" /></td>
						<td class="greybox"><s:textarea id="bankBranch.narration"
								name="bankBranch.narration" style="width:470px" /></td>
					</tr>

				</table>
				<br />
				<br />
				<div class="buttonbottom" style="padding-bottom: 10px;">
					<s:submit name="Save" value="Save" method="create"
						cssClass="buttonsubmit" />
					<input type="button" id="Close" value="Close"
						onclick="javascript:window.close()" class="button" />
				</div>
			</s:push>
		</s:form>
</body>
</html>