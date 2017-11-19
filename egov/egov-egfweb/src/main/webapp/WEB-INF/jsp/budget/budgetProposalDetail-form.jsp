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
<script>


	function populateSubSchemes(scheme){
		populatebudgetDetail_subScheme({schemeId:scheme.options[scheme.selectedIndex].value});
	}
	
	function onHeaderSubSchemePopulation(req,res){
		if(budgetDetailsTable != null){
			headerSubScheme=dom.get('budgetDetail_subScheme');
			pattern = 'budgetDetailList[{index}].subScheme.id';
			processGrid(budgetDetailsTable,function(element,grid){
				if(element) copyOptions(headerSubScheme,element)
			},pattern)
		}
		if(typeof preselectSubScheme=='function') preselectSubScheme();
    }
	var callback = {
		     success: function(o) {
				document.getElementById('savedDataGrid').innerHTML = o.responseText;
				updateReference();
				if(budgetDetailsTable != null){
		        	updateHiddenFields('budget.id',document.getElementById('budgetDetail_budget').value);
				}
				updateYearHeader();
				if(document.getElementById('budgetDocNumber') && document.getElementById('newBudgetDocNum')){
					document.getElementById('budgetDocNumber').value=document.getElementById('newBudgetDocNum').value;
				}
		        },
		     failure: function(o) {
		     }
	} 

	function updateYearHeader(){
		var newPrevious = '';
		var newCurrent = '';
		var newLastButOne = '';
		var newNext = ''
		if(document.getElementById('newPreviousYearValue'))
			newPrevious = document.getElementById('newPreviousYearValue').value;
		if(document.getElementById('newCurrentYearValue'))
			newCurrent = document.getElementById('newCurrentYearValue').value;
		if(document.getElementById('newLastButOneYearValue'))
			newLastButOne = document.getElementById('newLastButOneYearValue').value;
		if(document.getElementById('newNextYearValue'))
			newNext = document.getElementById('newNextYearValue').value;
		document.getElementById('yui-dt0-th-old_actuals-liner').innerHTML = '<span class="yui-dt-label">Actuals<br/>'+newLastButOne+'(Rs)</span>';
		document.getElementById('yui-dt0-th-actual_previous_year-liner').innerHTML = '<span class="yui-dt-label">Actuals<br/>'+newPrevious+'(Rs)</span>';
		document.getElementById('yui-dt0-th-actual_current_year-liner').innerHTML = '<span class="yui-dt-label">BE Actuals<br/>'+newCurrent+'(Rs)</span>';
		document.getElementById('yui-dt0-th-approved_current_year-liner').innerHTML = '<span class="yui-dt-label">BE <br/>'+newCurrent+'(Rs)</span>';
		if(document.getElementById('yui-dt0-th-re_amount-liner')){
			document.getElementById('yui-dt0-th-re_amount-liner').innerHTML = '<span class="yui-dt-label">RE Amount(Rs)<br/>'+newCurrent+'<span class="mandatory1">*</span></span>';
		}
		if(document.getElementById('yui-dt0-th-amount-liner')){
			document.getElementById('yui-dt0-th-amount-liner').innerHTML = '<span class="yui-dt-label">BE Amount(Rs)<br/>'+newNext+'<span class="mandatory1">*</span></span>';
		}
		if(document.getElementById('yui-dt0-th-total-liner')){
			document.getElementById('yui-dt0-th-total-liner').innerHTML = '<span class="yui-dt-label">Total<br/>'+newCurrent+'(Rs)</span>';
		}
	}
	
	function updateReference(){
		if(document.getElementById('referenceBudgetName'))
			document.getElementById('referenceBudget').innerHTML = document.getElementById('referenceBudgetName').value;
	}
	
		
	var budgetsCallback = {
		     success: function(o) {
		     		//document.getElementById('budgets').innerHTML = o.responseText;
		     		document.getElementById('budgetDetail_budget').innerHTML = o.responseText;
		        },
		     failure: function(o) {
		     }
	} 
	 function fetchBudgets(){
		var financialYear = document.getElementById('financialYear');
		id = financialYear.value;
		beRe = document.getElementById('bere').value;
		if(document.getElementById("addNewDetails"))
			{
			var addNew=document.getElementById("addNewDetails").value;
			var transaction = YAHOO.util.Connect.asyncRequest('GET', 'budgetProposalDetail-ajaxLoadBudgets.action?financialYear.id='+id+'&bere='+beRe+'&addNewDetails='+addNew, budgetsCallback, null);
			}
		else
		var transaction = YAHOO.util.Connect.asyncRequest('GET', 'budgetProposalDetail-ajaxLoadBudgets.action?financialYear.id='+id+'&bere='+beRe, budgetsCallback, null);
	} 
	
	<s:if test="%{shouldShowHeaderField('scheme') and shouldShowHeaderField('subScheme')}">
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
		updateGrid('subScheme.id',document.getElementById('budgetDetail_subScheme').selectedIndex);
	}
	</s:if>
</script>

<script type="text/javascript"
	src="${pageContext.request.contextPath}/resources/javascript/calenderNew.js"></script>
		
<table width="100%" border="0" cellspacing="0" cellpadding="0">
	<tr>
		<td class="bluebox" width="15%">&nbsp;</td>
		<td class="bluebox" width="15%"><s:text
				name="budgetdetail.financialYear" /><span class="mandatory1">*</span>
		</td>
		<td class="bluebox"><s:select
				list="dropdownData.financialYearList" listKey="id"
				listValue="finYearRange" name="budget.financialYear.id"
				headerKey="0" headerValue="--- Select ---" value="financialYear.id"
				id="financialYear" onchange="fetchBudgets()"></s:select></td>
		<td class="bluebox" width="15%"><s:text
				name="budgetdetail.budget" /><span class="mandatory1">*</span>
		<td class="bluebox">
			<div id="budgets"> 
				<s:select list="dropdownData.budgetList" listKey="id"
					listValue="name" name="budget.id"  headerKey="0"
					headerValue="--- Select ---" value="budget.id" id="budgetDetail_budget" 
					></s:select>
			 </div> 
		</td>



	</tr>
	<tr>
		<td width="10%" class="bluebox">&nbsp;</td>
		<s:if test="%{shouldShowHeaderField('executingDepartment')}">
			<td class="bluebox" width="15%"><s:text
					name="budgetdetail.executingDepartment" /> <s:if
					test="%{isFieldMandatory('executingDepartment')}">
					<span class="mandatory1">*</span>
				</s:if></td>
			<td width="15%" class="bluebox"><s:select
					list="dropdownData.executingDepartmentList" listKey="id"
					listValue="name" name="executingDepartment.id" headerKey="0"
					headerValue="--- Select ---"
					onchange="getSavedData();updateGrid('executingDepartment.id',document.getElementById('budgetDetail_executingDepartment').selectedIndex);updateApproverDepartment(this)"
					value="executingDepartment.id"
					id="budgetDetail_executingDepartment"></s:select></td>
		</s:if>
		<s:if test="%{showRe}">
			<!-- <td class="bluebox" width="15%">Reference Budget</td>
			<td class="bluebox">
				<div id="referenceBudget"></div>
			</td> -->
		</s:if>
	</tr>
	<tr>
		<s:if test="%{shouldShowHeaderField('fund')}">
			<td class="bluebox">&nbsp;</td>
			<td class="bluebox"><s:text name="fund" /> <s:if
					test="%{isFieldMandatory('fund')}">
					<span class="mandatory1">*</span>
				</s:if></td>
			<td class="bluebox"><s:select list="dropdownData.fundList"
					listKey="id" listValue="name" name="fund.id" headerKey="0"
					headerValue="--- Select ---"
					onchange="updateGrid('fund.id',document.getElementById('budgetDetail_fund').selectedIndex)"
					value="fund.id" id="budgetDetail_fund"></s:select></td>
		</s:if>
		<s:if test="%{shouldShowHeaderField('function')}">
			<td class="bluebox">&nbsp;</td>
			<s:hidden name="function.id" id="hidden_function"/>
			<td class="bluebox"><s:text name="function" /> <s:if
					test="%{isFieldMandatory('function')}">
					<span class="mandatory1">*</span>
				</s:if></td>
			<td class="bluebox"><s:select list="dropdownData.functionList"
					listKey="id" listValue="name" name="function.id" headerKey="0"
					headerValue="--- Select ---"
					onchange="updateGrid('function.id',document.getElementById('budgetDetail_function').selectedIndex)"
					value="function.id" id="budgetDetail_function"></s:select></td>
		</s:if>
	</tr>
	<tr>
		<s:if test="%{shouldShowHeaderField('scheme')}">
			<td width="10%" class="bluebox">&nbsp;</td>
			<td class="bluebox"><s:text name="scheme" /> <s:if
					test="%{isFieldMandatory('scheme')}">
					<span class="mandatory1">*</span>
				</s:if></td>
			<td class="bluebox"><s:select list="dropdownData.schemeList"
					listKey="id" listValue="name" headerKey="0"
					headerValue="--- Select ---" name="scheme"
					onchange="updateGrid('scheme.id',document.getElementById('budgetDetail_scheme').selectedIndex);populateSubSchemes(this);"
					value="scheme.id" id="budgetDetail_scheme"></s:select></td>
		</s:if>
		<s:if test="%{shouldShowHeaderField('subScheme')}">
			<egov:ajaxdropdown id="subScheme" fields="['Text','Value']"
				dropdownId="budgetDetail_subScheme"
				url="budget/budgetProposalDetail-ajaxLoadSubSchemes.action"
				afterSuccess="onHeaderSubSchemePopulation" />
			<td class="bluebox"><s:text name="subScheme" /> <s:if
					test="%{isFieldMandatory('subScheme')}">
					<span class="mandatory1">*</span>
				</s:if></td>
			<td class="bluebox"><s:select list="dropdownData.subSchemeList"
					listKey="id" listValue="name" headerKey="0"
					headerValue="--- Select ---" name="subScheme"
					onchange="updateGrid('subScheme.id',document.getElementById('budgetDetail_subScheme').selectedIndex)"
					value="subScheme.id" id="budgetDetail_subScheme"></s:select></td>
		</s:if>
	</tr>
	<tr>
		<s:if test="%{shouldShowHeaderField('functionary')}">
			<td class="bluebox"><s:text name="functionary" /> <s:if
					test="%{isFieldMandatory('functionary')}">
					<span class="mandatory1">*</span>
				</s:if></td>
			<td class="bluebox"><s:select
					list="dropdownData.functionaryList" listKey="id" listValue="name"
					headerKey="0" headerValue="--- Select ---" name="functionary"
					onchange="updateGrid('functionary.id',document.getElementById('budgetDetail_functionary').selectedIndex)"
					value="functionary.id" id="budgetDetail_functionary"></s:select></td>
		</s:if>
		<s:if test="%{shouldShowHeaderField('boundary')}">
			<td class="bluebox"><s:text name="field" /> <s:if
					test="%{isFieldMandatory('boundary')}">
					<span class="mandatory1">*</span>
				</s:if></td>
			<td class="bluebox"><s:select list="dropdownData.boundaryList"
					listKey="id" listValue="name" headerKey="0"
					headerValue="--- Select ---" name="boundary"
					onchange="updateGrid('boundary.id',document.getElementById('budgetDetail_boundary').selectedIndex)"
					value="boundary.id" id="budgetDetail_boundary"></s:select></td>
		</s:if>
	</tr>
</table>
</div>

