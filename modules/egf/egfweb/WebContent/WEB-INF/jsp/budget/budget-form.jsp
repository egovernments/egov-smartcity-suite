<%@ taglib prefix="s" uri="/WEB-INF/struts-tags.tld"%>

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
	<div class="formmainbox">
	<table width="100%" border="0" cellspacing="0" cellpadding="0">
		<s:push value="model">
			<tr>
				<td class="bluebox" width="10%">&nbsp;</td>
				<td class="bluebox"><s:text name="budget.budgetname"/> <span class="mandatory">*</span></td>
				<td class="bluebox"><s:textfield name="name" id="name" maxlength="50" size="60"/><span class="highlight2">Max. 50 characters</span></td>
			</tr>
			<tr>
				<td class="greybox">&nbsp;</td>
				<td class="greybox"><s:text name="budget.bere"/> <span class="mandatory">*</span></td>
				<td class="greybox"><s:radio name="isbere" list="isbereList" onchange="populateParent()"/></td>
			</tr>
			<tr>
				<td class="bluebox">&nbsp;</td>
				<td class="bluebox"><s:text name="budget.financialYear"/> <span class="mandatory">*</span></td>
				<td class="bluebox"><s:select name="financialYear" id="financialYear" list="dropdownData.financialYearList" listKey="id" listValue="finYearRange" headerKey="-1" headerValue="----Select----"  value="%{financialYear.id}" onchange="populateReferenceBudgets();populateParent()"/> </td>
			</tr>
			<tr>
				<td class="greybox">&nbsp;</td>
				<egov:ajaxdropdown id="parentId" fields="['Text','Value']" dropdownId="parentId" url="budget/budget!ajaxLoadParentBudgets.action"/>
				<td class="greybox"><s:text name="budget.parent"/></td>
				<td class="greybox"><s:select name="parentId" id="parentId" list="parMap"  headerKey="-1"  headerValue="----Select----"  value="%{parentId}"/></td>
			</tr>
			<tr>
				<td class="bluebox">&nbsp;</td>
				<td class="bluebox"><s:text name="budget.description"/></td>
				<td class="bluebox"><s:textarea name="description" id="description" cols="50" rows="5"/><span class="highlight2">Max. 250 characters</span></td>
			</tr>
			<tr>
				<td class="greybox">&nbsp;</td>
				<td class="greybox"><s:text name="budget.activebudget"/></td>
				<td class="greybox"><s:checkbox name="isActiveBudget" id="isActiveBudget" /></td>
			</tr>
			<tr>
				<td class="bluebox">&nbsp;</td>
				<td class="bluebox"><s:text name="budget.primarybudget"/></td>
				<td class="bluebox"><s:checkbox name="isPrimaryBudget" id="isPrimaryBudget"/></td>
			</tr>
			<tr>
				<td class="greybox">&nbsp;</td>
				<td class="greybox"><div id="refBudgetLabel"><s:text name="budget.referencebudget"/></div></td>
				<td class="greybox">
					<div id="result">
						<s:select name="referenceId" listKey="id" listValue="name" id="referenceId" list="referenceBudgetList"  headerKey="-1"  headerValue="----Select----"  value="%{referenceId}"/>
					</div>
				</td>
			</tr>
			</s:push>
		</table>
		</div>
