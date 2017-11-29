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


<script>
	function populateSubSchemes(scheme){
		populatebudgetDetail_subScheme({schemeId:scheme.options[scheme.selectedIndex].value})
	}
	
	function populateBudgets(financialYearRange){
		populatebudgetDetail_budget({financialYear:financialYearRange.options[financialYearRange.selectedIndex].value,skipPrepare:true})
	}
	function onHeaderSubSchemePopulation(req,res){
		if(budgetDetailsTable != null){
			headerSubScheme=dom.get('budgetDetail_subScheme');
			pattern = 'budgetDetailList[{index}].subScheme.id'
			processGrid(budgetDetailsTable,function(element,grid){
				if(element) copyOptions(headerSubScheme,element)
			},pattern)
		}
		if(typeof preselectSubScheme=='function') preselectSubScheme()
    }
</script>

<script type="text/javascript"
	src="${pageContext.request.contextPath}/resources/javascript/calenderNew.js?rnd=${app_release_no}"></script>

	<div class="formheading"></div>
	<table width="100%" border="0" cellspacing="0" cellpadding="0">
		<tr>
			<td width="10%" class="bluebox">&nbsp;</td>
			<td class="bluebox"><s:text name="budget.financialYear" /><span
				class="mandatory1">*</span>
			<td width="22%" class="bluebox"><s:select
					list="dropdownData.financialYearList" listKey="id"
					listValue="finYearRange" name="budget.financialYear.id"
					id="financialYearRange" onchange="populateBudgets(this);"></s:select></td>
		</tr>
		<tr>
			<td width="10%" class="greybox">&nbsp;</td>
			<egov:ajaxdropdown id="budget" fields="['Text','Value']"
				dropdownId="budgetDetail_budget"
				url="budget/budgetSearch-ajaxLoadBudget.action" />
			<td class="greybox"><s:text name="budgetdetail.budget" />
			<td width="22%" class="greybox"><s:select
					list="dropdownData.budgetList" listKey="id" listValue="name"
					name="budget.id" value="model.budget.id" id="budgetDetail_budget"
					disabled="%{headerDisabled}" headerKey="0"
					headerValue="--- Select ---"></s:select></td>
			<s:if
				test="%{shouldShowHeaderField('executingDepartment') || shouldShowGridField('executingDepartment')}">
				<td class="greybox"><s:text
						name="budgetdetail.executingDepartment" /></td>
				<td width="22%" class="greybox"><s:select
						list="dropdownData.executingDepartmentList" listKey="id"
						listValue="name" name="executingDepartment.id" headerKey="0"
						headerValue="--- Select ---"
						onchange="updateGrid('executingDepartment.id',document.getElementById('budgetDetail_executingDepartment').selectedIndex)"
						value="executingDepartment.id"
						id="budgetDetail_executingDepartment"></s:select></td>
			</s:if>
		</tr>
		<tr>
			<s:if
				test="%{shouldShowHeaderField('fund') || shouldShowGridField('fund')}">
				<td class="bluebox">&nbsp;</td>
				<td class="bluebox"><s:text name="budgetdetail.fund" /></td>
				<td class="bluebox"><s:select list="dropdownData.fundList"
						listKey="id" listValue="name" name="fund.id" headerKey="0"
						headerValue="--- Select ---"
						onchange="updateGrid('fund.id',document.getElementById('budgetDetail_fund').selectedIndex)"
						value="fund.id" id="budgetDetail_fund"></s:select></td>
			</s:if>
			<s:if
				test="%{shouldShowHeaderField('function') || shouldShowGridField('function')}">
				<td class="bluebox"><s:text name="budgetdetail.function" /></td>
				<td class="bluebox"><s:select list="dropdownData.functionList"
						listKey="id" listValue="name" name="function.id" headerKey="0"
						headerValue="--- Select ---"
						onchange="updateGrid('function.id',document.getElementById('budgetDetail_function').selectedIndex)"
						value="function.id" id="budgetDetail_function"></s:select></td>
			</s:if>
		</tr>
		<tr>
			<s:if
				test="%{shouldShowHeaderField('scheme') || shouldShowGridField('scheme')}">
				<td width="10%" class="bluebox">&nbsp;</td>
				<td class="greybox"><s:text name="budgetdetail.scheme" /></td>
				<td class="greybox"><s:select list="dropdownData.schemeList"
						listKey="id" listValue="name" headerKey="0"
						headerValue="--- Select ---" name="scheme"
						onchange="updateGrid('scheme.id',document.getElementById('budgetDetail_scheme').selectedIndex);populateSubSchemes(this);"
						value="scheme.id" id="budgetDetail_scheme"></s:select></td>
			</s:if>
			<s:if
				test="%{shouldShowHeaderField('subScheme') || shouldShowGridField('subScheme')}">
				<egov:ajaxdropdown id="subScheme" fields="['Text','Value']"
					dropdownId="budgetDetail_subScheme"
					url="budget/budgetDetail-ajaxLoadSubSchemes.action"
					afterSuccess="onHeaderSubSchemePopulation" />
				<td class="greybox"><s:text name="budgetdetail.subScheme" /></td>
				<td class="greybox"><s:select list="dropdownData.subSchemeList"
						listKey="id" listValue="name" headerKey="0"
						headerValue="--- Select ---" name="subScheme"
						onchange="updateGrid('subScheme.id',document.getElementById('budgetDetail_subScheme').selectedIndex)"
						value="subScheme.id" id="budgetDetail_subScheme"></s:select></td>
			</s:if>
		</tr>
		<tr>
			<s:if
				test="%{shouldShowHeaderField('functionary') || shouldShowGridField('functionary')}">
				<td class="bluebox"><s:text name="budgetdetail.functionary" /></td>
				<td class="bluebox"><s:select
						list="dropdownData.functionaryList" listKey="id" listValue="name"
						headerKey="0" headerValue="--- Select ---" name="functionary"
						onchange="updateGrid('functionary.id',document.getElementById('budgetDetail_functionary').selectedIndex)"
						value="functionary.id" id="budgetDetail_functionary"></s:select></td>
			</s:if>
			<s:if
				test="%{shouldShowHeaderField('boundary') || shouldShowGridField('boundary')}">
				<td class="bluebox"><s:text name="budgetdetail.field" /></td>
				<td class="bluebox"><s:select list="dropdownData.boundaryList"
						listKey="id" listValue="name" headerKey="0"
						headerValue="--- Select ---" name="boundary"
						onchange="updateGrid('boundary.id',document.getElementById('budgetDetail_boundary').selectedIndex)"
						value="boundary.id" id="budgetDetail_boundary"></s:select></td>
			</s:if>
			<s:else>
				<td class="bluebox">&nbsp;</td>
				<td class="bluebox">&nbsp;</td>
			</s:else>
		</tr>
	</table>

<script>
<s:if test="%{(shouldShowHeaderField('scheme') and shouldShowHeaderField('subScheme')) or (shouldShowGridField('scheme') and shouldShowGridField('subScheme'))}">
populateSubSchemes(document.getElementById('budgetDetail_scheme'))
function preselectSubScheme(){
	subSchemes =  document.getElementById('budgetDetail_subScheme');
	selectedValue="<s:property value='subScheme.id'/>"
	for(i=0;i<subSchemes.options.length;i++){
	  if(subSchemes.options[i].value==selectedValue){
		subSchemes.selectedIndex=i;
		break;
	  }
	}
}
</s:if>

</script>
