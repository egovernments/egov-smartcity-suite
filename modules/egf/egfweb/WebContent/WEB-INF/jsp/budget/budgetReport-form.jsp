<script>
	function validateFinYear()
	{
		if(document.getElementById('financialYear').value==0)
		{
			alert('Please select a financial year');
			return false;
		}
		else
			return true;
	}
</script>

<table width="100%" border="0" cellspacing="0" cellpadding="0">
	<tr>
		<td class="greybox" width="5%"/>
		<td class="greybox"><s:text name="report.financialYear"/> <span class="mandatory">*</span></td>
		<td class="greybox"><s:select name="financialYear" id="financialYear" list="dropdownData.financialYearList" listKey="id" listValue="finYearRange" headerKey="0" headerValue="----Select----"  value="%{model.financialYear.id}" /> </td>
		<td class="greybox"><s:text name="report.department"/></td>
		<td class="greybox"><s:select name="department" id="department" list="dropdownData.departmentList" listKey="id" listValue="deptName" headerKey="0" headerValue="----Select----"  value="%{model.department.id}" /> </td>
	</tr>
	<tr>
		<td class="bluebox" width="5%"/>
		<td class="bluebox"><s:text name="report.type"/></td>
		<td class="bluebox"><s:select name="type" id="type" list="#{'All':'---Select---','I':'Revenue','E':'Expense','L':'Liability','A':'Asset','IE':'Revenue & Expense'}" /> </td>
		<td class="bluebox" id="function_label" style="visibility:visible"><s:text name="report.function"/></td>
		<td class="bluebox"><s:select name="function" id="function" list="dropdownData.functionList" listKey="id" listValue="name" headerKey="-1" headerValue="----Select----"  value="%{function.id}" /> </td>
	</tr>
</table>
<td><div align="left" class="mandatory">* Mandatory Fields</div></td>
