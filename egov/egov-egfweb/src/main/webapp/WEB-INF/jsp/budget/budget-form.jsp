<!--
  ~ eGov suite of products aim to improve the internal efficiency,transparency,
  ~    accountability and the service delivery of the government  organizations.
  ~
  ~     Copyright (C) <2015>  eGovernments Foundation
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
  -->
<%@ include file="/includes/taglibs.jsp"%>
<%@ page language="java"%>

<script>
		function populateParent(){
			var finYear = document.getElementById('financialYear');
			if(finYear.value == -1)
				return;
			var bereValue;
			if(document.getElementById('budget_isbereBE').checked == true)
				bereValue = document.getElementById('budget_isbereBE').value;
			else
				bereValue = document.getElementById('budget_isbereRE').value;
			if(document.getElementById('tempId'))
				populateparentId({bere:bereValue+'&tempId='+document.getElementById('tempId').value+'&date='+new Date(),financialYearId:finYear.value})
			else
				populateparentId({bere:bereValue+'&date='+new Date(),financialYearId:finYear.value})
		}

	var callback = {
		success: function(o){
			document.getElementById('result').innerHTML=o.responseText;
			},
			failure: function(o) {
		    }
		}
		
		function populateReferenceBudgets(){
			var be = document.getElementById('budget_isbereBE');
			if(be.checked == true){
				document.getElementById('referenceId').disabled = false;
			}
			var re = document.getElementById('budget_isbereRE');
			if(re.checked == true){
				document.getElementById('referenceId').disabled = true;
				return;
			}
			var finYear = document.getElementById('financialYear');
			if(finYear.value == -1){
				return;
			}
			var url = '/EGF/budget/budget!ajaxLoadReferenceBudgets.action?financialYear.id='+finYear.value;
			YAHOO.util.Connect.asyncRequest('POST', url, callback, null);
		}
		
	</script>
<table width="100%" border="0" cellspacing="0" cellpadding="0">
	<s:push value="model">
		<tr>
			<td class="bluebox" width="10%">&nbsp;</td>
			<td class="bluebox"><s:text name="budget.budgetname" /> <span
				class="mandatory"></span></td>
			<td class="bluebox"><s:textfield name="name" id="name"
					maxlength="50" size="60" /><span class="highlight2">Max. 50
					characters</span></td>
		</tr>
		<tr>
			<td class="greybox">&nbsp;</td>
			<td class="greybox"><s:text name="budget.bere" /> <span
				class="mandatory"></span></td>
			<td class="greybox"><s:radio name="isbere" list="isbereList"
					onchange="populateParent()" /></td>
		</tr>
		<tr>
			<td class="bluebox">&nbsp;</td>
			<td class="bluebox"><s:text name="budget.financialYear" /> <span
				class="mandatory"></span></td>
			<td class="bluebox"><s:select name="financialYear"
					id="financialYear" list="dropdownData.financialYearList"
					listKey="id" listValue="finYearRange" headerKey="-1"
					headerValue="----Select----" value="%{financialYear.id}"
					onchange="populateReferenceBudgets();populateParent()" /></td>
		</tr>
		<tr>
			<td class="greybox">&nbsp;</td>
			<egov:ajaxdropdown id="parentId" fields="['Text','Value']"
				dropdownId="parentId"
				url="budget/budget!ajaxLoadParentBudgets.action" />
			<td class="greybox"><s:text name="budget.parent" /></td>
			<td class="greybox"><s:select name="parentId" id="parentId"
					list="parMap" headerKey="-1" headerValue="----Select----"
					value="%{parentId}" /></td>
		</tr>
		<tr>
			<td class="bluebox">&nbsp;</td>
			<td class="bluebox"><s:text name="budget.description" /></td>
			<td class="bluebox"><s:textarea name="description"
					id="description" cols="50" rows="5" /><span class="highlight2">Max.
					250 characters</span></td>
		</tr>
		<tr>
			<td class="greybox">&nbsp;</td>
			<td class="greybox"><s:text name="budget.activebudget" /></td>
			<td class="greybox"><s:checkbox name="isActiveBudget"
					id="isActiveBudget" /></td>
		</tr>
		<tr>
			<td class="bluebox">&nbsp;</td>
			<td class="bluebox"><s:text name="budget.primarybudget" /></td>
			<td class="bluebox"><s:checkbox name="isPrimaryBudget"
					id="isPrimaryBudget" /></td>
		</tr>
		<tr>
			<td class="greybox">&nbsp;</td>
			<td class="greybox"><div id="refBudgetLabel">
					<s:text name="budget.referencebudget" />
				</div></td>
			<td class="greybox">
				<div id="result">
					<s:select name="referenceId" listKey="id" listValue="name"
						id="referenceId" list="referenceBudgetList" headerKey="-1"
						headerValue="----Select----" value="%{referenceId}" />
				</div>
			</td>
		</tr>
	</s:push>
</table>
