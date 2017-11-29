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

<title><s:text name="fundingagency.funding.agency.modify" /></title>
<script type="text/javascript">
	function checkuniquenesscode(){
    	document.getElementById('codeuniquecode').style.display ='none';
		var code=document.getElementById('code').value;
		var id=document.getElementById('id').value;
		populatecodeuniquecode({code:code,id:id});
    }
	</script>

</head>

<body id="body">
	<div class="formmainbox">
		<div class="subheadnew">
			<s:text name="fundingagency.modify.funding.agency" />
		</div>
		<div style="color: red">
			<s:actionerror />
			<s:fielderror />
		</div>
		<div style="color: green">
			<s:actionmessage theme="simple" />
		</div>
		<br />
		<br />
		<div class="errorstyle" style="display: none" id="codeuniquecode">
			<s:text name="fundingagency.code.already.exists" />
		</div>
		<s:form name="fundingAgencyForm" action="fundingAgency" theme="simple">
			<s:token />
			<s:push value="model">
				<s:hidden id="id" name="id" />
				<table width="100%" border="0" cellspacing="0" cellpadding="0">
					<tr>
						<td class="greybox" width="10%">Code<span class="mandatory">*</span></td>
						<td class="greybox" width="30%" colspan=8><s:textfield
								id="code" name="code" onblur="checkuniquenesscode();" /></td>
						<egov:uniquecheck id="codeuniquecode" name="codeuniquecode"
							fieldtoreset="code" fields="['Value']"
							url='masters/fundingAgency!codeUniqueCheckCode.action' />
					</tr>
					<tr>
						<td class="bluebox" width="10%">Name<span class="mandatory">*</span></td>
						<td class="bluebox" width="46%"><s:textfield id="name"
								name="name" /></td>
						<td class="bluebox" width="2%">IsActive</td>
						<td class="bluebox"><s:checkbox id="isActive" name="isActive" />
						</td>
					</tr>

				</table>
				<table width="100%" border="0" cellspacing="0" cellpadding="0">
					<tr>
						<td class="greybox" width="10%">Remarks</td>
						<td class="greybox"><s:textarea id="remarks" name="remarks"
								style="width:470px" /></td>
					</tr>

				</table>
				<br />
				<br />
				<div class="buttonbottom" style="padding-bottom: 10px;">
					<s:submit name="Save" value="Save" method="edit"
						cssClass="buttonsubmit" />
					<input type="button" id="Close" value="Close"
						onclick="javascript:window.close()" class="button" />
				</div>
			</s:push>
		</s:form>
</body>
</html>
