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
<title><s:if test="showMode == 'new'">
		<s:text name="subScheme.add" />
	</s:if> <s:else>
		<s:text name="subscheme.modify.title" />
	</s:else></title>
<sx:head />
<SCRIPT type="text/javascript">
		function validate(){
			if (!validateForm_subSchemeForm()) {
	        	undoLoadingMask();
	    		return false;
	            }
			 if(!validateDate(document.getElementById('validfrom').value)){
					bootbox.alert("Invalid Date! valid from date is greater than current date");
					return false;
			}
			 else if (Date.parse(document.getElementById('validfrom').value) > Date.parse(document.getElementById('validtoId').value)) {
					bootbox.alert("Invalid Date Range! valid from date cannot be after valid to date!")
					return false;
					} 
			if(isNaN(document.getElementById('initialEstimateAmount').value)){
				bootbox.alert("Please enter valid Initial Eastimate Amount");
				return false;
			}
			var showMode = document.getElementById('showMode').value;
			if(showMode=='edit')
			var	url='${pageContext.request.contextPath}/masters/subScheme-edit.action';
			else
			var	url='${pageContext.request.contextPath}/masters/subScheme-create.action';
			document.subSchemeForm.action=url;
    		document.subSchemeForm.submit();
			return true;
		}
		function validateDate(date)
		{
			var todayDate = new Date();
			 var todayMonth = todayDate.getMonth() + 1;
			 var todayDay = todayDate.getDate();
			 var todayYear = todayDate.getFullYear();
			 var todayDateText = todayDay + "/" + todayMonth + "/" +  todayYear;
			if (Date.parse(date) > Date.parse(todayDateText)) {
				return false;
				}
			return true; 
		}
</script>

</head>
<body>
	<jsp:include page="../budget/budgetHeader.jsp" />
	<div class="formmainbox">
		<div class="subheadnew">
			<s:if test="showMode == 'new'">
				<s:text name="subScheme.add" />
			</s:if>
			<s:else>
				<s:text name="subscheme.modify.title" />
			</s:else>
			<div style="color: red">
				<s:actionerror />
				<s:fielderror />
			</div>
			<div style="color: green">
				<s:actionmessage />
			</div>
			<s:token />

			<s:form id="subSchemeForm" name="subSchemeForm" action="subScheme"
				theme="css_xhtml" validate="true">

				<s:hidden name="showMode" id="showMode" value="%{showMode}" />
				<s:hidden id="subSchemeId" name="subSchemeId"
					value="%{subScheme.id}" />
				<table width="100%" border="0" cellspacing="0" cellpadding="0">
					<tr>
						<td class="bluebox">&nbsp;</td>
						<td class="bluebox" width="20%"><strong>Scheme<span
								class="mandatory1"> *</span></strong></td>
						<td class="bluebox"><s:select name="scheme" id="scheme"
								list="dropdownData.schemeList" listKey="id" listValue="name"
								headerKey="" headerValue="--- Select ---"
								value="%{subScheme.scheme.id}" /></td>
						<td class="bluebox" width="20%"><strong>Name<span
								class="mandatory1"> *</span></strong></td>
						<td class="bluebox"><s:textfield id="name" name="name"
								value="%{subScheme.name}" cssStyle="width: 250px" /></td>
					</tr>
					<tr>
						<td class="greybox">&nbsp;</td>
						<td class="greybox"><strong>Code</strong><span
							class="mandatory1"> *</span></td>
						<td class="greybox"><s:textfield id="code" name="code"
								value="%{subScheme.code}" /></td>
						<td class="greybox"><strong>Valid From</strong><span
							class="mandatory1"> *</span></td>
						<td class="greybox"><s:date name="validfrom" id="validfrom"
								format="dd/MM/yyyy" /> <s:textfield name="validfrom"
								id="validfrom" value="%{subScheme.validfrom}" maxlength="10"
								onkeyup="DateFormat(this,this.value,event,false,'3')" /> <a
							href="javascript:show_calendar('subSchemeForm.validfrom',null,null,'DD/MM/YYYY');"
							style="text-decoration: none">&nbsp;<img
								src="/egi/resources/erp2/images/calendaricon.gif" border="0" /></a>(dd/mm/yyyy)</td>

						</td>
					</tr>
					<tr>
						<td class="bluebox">&nbsp;</td>
						<td class="bluebox"><strong>Valid To</strong><span
							class="mandatory1"> *</span></td>
						<td class="bluebox"><s:date name="validto" id="validtoId"
								format="dd/MM/yyyy" /> <s:textfield name="validto"
								id="validtoId" value="%{subScheme.validto}" maxlength="10"
								onkeyup="DateFormat(this,this.value,event,false,'3')" /> <a
							href="javascript:show_calendar('subSchemeForm.validto',null,null,'DD/MM/YYYY');"
							style="text-decoration: none">&nbsp;<img
								src="/egi/resources/erp2/images/calendaricon.gif" border="0" /></a>(dd/mm/yyyy)</td>
						</td>
						<td class="bluebox"><strong>Is Active</strong></td>
						<td class="bluebox"><s:checkbox name="isActive" value="%{isActive}" /></td>
					</tr>
					<tr>
						<td class="greybox">&nbsp;</td>
						<td class="greybox"><strong>Department</strong></td>
						<td class="greybox"><s:select
								list="dropdownData.departmentList" listKey="id" listValue="name"
								headerKey="0" headerValue="--- Select ---" name="department"
								id="department" value="%{subScheme.department.id}"></s:select></td>
						<td class="greybox"><strong>Initial Estimate Amount</strong></td>
						<td class="greybox"><s:textfield
								cssStyle="text-align: right;" id="initialEstimateAmount"
								name="initialEstimateAmount"
								value="%{subScheme.initialEstimateAmount}" /></td>
					</tr>
					<tr>
						<td class="bluebox">&nbsp;</td>
						<td class="bluebox"><strong>Council Loan Proposal
								Number</strong></td>
						<td class="bluebox"><s:textfield
								id="councilLoanProposalNumber" name="councilLoanProposalNumber"
								value="%{subScheme.councilLoanProposalNumber}" /></td>
						<td class="bluebox"><strong>Council Loan Proposal
								Date</strong></td>
						<td class="bluebox"><input type="text"
							id="councilLoanProposalDate" name="councilLoanProposalDate"
							style="width: 100px"
							value='<s:date name="councilLoanProposalDate" format="dd/MM/yyyy"/>'
							onkeyup="DateFormat(this,this.value,event,false,'3')" /> <a
							href="javascript:show_calendar('subSchemeForm.councilLoanProposalDate');"
							style="text-decoration: none">&nbsp;<img
								src="/egi/resources/erp2/images/calendaricon.gif" border="0" /></a>
						</td>
					</tr>
					<tr>
						<td class="greybox">&nbsp;</td>
						<td class="greybox"><strong>Council Admin Sanctioned
								Number</strong></td>
						<td class="greybox"><s:textfield
								id="councilAdminSanctionNumber"
								name="councilAdminSanctionNumber"
								value="%{subScheme.councilAdminSanctionNumber}" /></td>
						<td class="greybox"><strong>Council Admin Sanctioned
								Date</strong></td>
						<td class="greybox"><input type="text"
							id="councilAdminSanctionDate" name="councilAdminSanctionDate"
							style="width: 100px"
							value='<s:date name="councilAdminSanctionDate" format="dd/MM/yyyy"/>'
							onkeyup="DateFormat(this,this.value,event,false,'3')" /> <a
							href="javascript:show_calendar('subSchemeForm.councilAdminSanctionDate');"
							style="text-decoration: none">&nbsp;<img
								src="/egi/resources/erp2/images/calendaricon.gif" border="0" /></a>
						</td>
					</tr>
					<tr>
						<td class="bluebox">&nbsp;</td>
						<td class="bluebox"><strong>Government Loan Proposal
								Number</strong></td>
						<td class="bluebox"><s:textfield id="govtLoanProposalNumber"
								name="govtLoanProposalNumber"
								value="%{subScheme.govtLoanProposalNumber}" /></td>
						<td class="bluebox"><strong>Government Loan Proposal
								Date</strong></td>
						<td class="bluebox"><input type="text"
							id="govtLoanProposalDate" name="govtLoanProposalDate"
							style="width: 100px"
							value='<s:date name="govtLoanProposalDate" format="dd/MM/yyyy"/>'
							onkeyup="DateFormat(this,this.value,event,false,'3')" /> <a
							href="javascript:show_calendar('subSchemeForm.govtLoanProposalDate');"
							style="text-decoration: none">&nbsp;<img
								src="/egi/resources/erp2/images/calendaricon.gif" border="0" /></a>
						</td>
					</tr>
					<tr>
						<td class="greybox">&nbsp;</td>
						<td class="greybox"><strong>Government Admin
								Sanction Number</strong></td>
						<td class="greybox"><s:textfield id="govtAdminSanctionNumber"
								name="govtAdminSanctionNumber"
								value="%{subScheme.govtAdminSanctionNumber}" /></td>
						<td class="greybox"><strong>Government Admin
								Sanction Date</strong></td>
						<td class="greybox"><input type="text"
							id="govtAdminSanctionDate" name="govtAdminSanctionDate"
							style="width: 100px"
							value='<s:date name="govtAdminSanctionDate" format="dd/MM/yyyy"/>'
							onkeyup="DateFormat(this,this.value,event,false,'3')" /> <a
							href="javascript:show_calendar('subSchemeForm.govtAdminSanctionDate');"
							style="text-decoration: none">&nbsp;<img
								src="/egi/resources/erp2/images/calendaricon.gif" border="0" /></a>
						</td>
					</tr>
				</table>
				<br />
				<br />

				<s:if test="%{showMode=='new'}">
					<div align="center" class="buttonbottom"
						style="padding-bottom: 10px;">
						<input type="submit" class="buttonsubmit" value="Save"
							id="saveButton" name="button" onclick="return validate();" /> <input
							type="button" id="Close" value="Close"
							onclick="javascript:window.close()" class="button" />
					</div>
				</s:if>
				<s:elseif test="%{showMode=='edit'}">
					<div class="buttonbottom" style="padding-bottom: 10px;">
						<input type="submit" class="buttonsubmit" value="Save"
							id="saveButton" name="button" onclick="return validate();" /> <input
							type="button" id="Close" value="Close"
							onclick="javascript:window.close()" class="button" />
					</div>
				</s:elseif>
				<s:else>
					<div class="buttonbottom" style="padding-bottom: 10px;">
						<input type="button" id="Close" value="Close"
							onclick="javascript:window.close()" class="button" />
						</dev>
				</s:else>
			</s:form>
			<script type="text/javascript">
		<s:if test="%{clearValues == true}">
			document.getElementById('scheme').value = 0;
			document.getElementById('department').value = 0;
			document.getElementById('name').value = "";
			document.getElementById('code').value = "";
			document.getElementById('validfrom').value = "";
			document.getElementById('validto').value = "";
			document.getElementById('initialEstimateAmount').value = "";
			
			document.getElementById('councilLoanProposalNumber').value = "";
			document.getElementById('councilLoanProposalDate').value = "";
			document.getElementById('councilAdminSanctionNumber').value = "";
			document.getElementById('councilAdminSanctionDate').value = "";
			document.getElementById('govtLoanProposalNumber').value = "";
			document.getElementById('govtLoanProposalDate').value = "";
			document.getElementById('govtAdminSanctionNumber').value = "";
			document.getElementById('govtAdminSanctionDate').value = "";
		</s:if>
		</script>
</body>
</html>
