<%@ taglib prefix="s" uri="/WEB-INF/struts-tags.tld"%>
<%@ taglib prefix="egov" tagdir="/WEB-INF/tags"%>
<tr>
	    <td width="10%" class="bluebox">&nbsp;</td>
		<td class="bluebox"><s:text name="budget.financialYear"/><span class="mandatory">*</span></td>
	    <td class="bluebox"><s:select list="dropdownData.finYearList"  listKey="id" listValue="finYearRange" name="financialYear.id" value="financialYear.id" id="financialYear" headerKey="0" headerValue="--- Select ---" ></s:select></td>
		<td class="bluebox"><s:text name="budget.bere"/></td>
	    <td class="bluebox"><s:select name="isBeRe" id="isBeRe" list="#{'BE':'BE','RE':'RE'}" name="budgetDetail.budget.isbere" value="budgetDetail.budget.isbere"/></td>
	</tr>
	<tr>
		<s:if test="%{shouldShowHeaderField('fund') || shouldShowGridField('fund')}">
			<td class="greybox">&nbsp;</td>
			<td  class="greybox">
				<s:text name="fund"/>
			</td>
		    <td  class="greybox"><s:select list="dropdownData.fundList"  listKey="id" listValue="name" name="budgetDetail.fund.id" headerKey="0" headerValue="--- Select ---"  value="fund.id" id="budgetReAppropriation_fund"></s:select></td>
		</s:if>
		<s:if test="%{shouldShowHeaderField('executingDepartment') || shouldShowGridField('executingDepartment')}">
			<td  class="greybox">
				<s:text name="budgetdetail.executingDepartment"/>
			</td>
		    <td width="22%" class="greybox"><s:select list="dropdownData.executingDepartmentList"  listKey="id" listValue="deptName" name="budgetDetail.executingDepartment.id" headerKey="0" headerValue="--- Select ---"  value="budgetDetail.executingDepartment.id" id="budgetReAppropriation_executingDepartment"></s:select></td>
		</s:if>
	</tr>
	<tr>
		<s:if test="%{shouldShowField('function') || shouldShowGridField('function')}">
		    <td class="bluebox">&nbsp;</td>
			<td  class="bluebox">
				<s:text name="function"/>
			</td>
		    <td  class="bluebox"><s:select list="dropdownData.functionList"  listKey="id" listValue="name" name="budgetDetail.function.id" headerKey="0" headerValue="--- Select ---"  value="function.id" id="budgetReAppropriation_function"></s:select></td>
		</s:if>
		<s:if test="%{shouldShowHeaderField('functionary') || shouldShowGridField('functionary')}">
			<td class="bluebox">
				<s:text name="functionary"/>
			</td>
		    <td class="bluebox"><s:select list="dropdownData.functionaryList"  listKey="id" listValue="name" headerKey="0" headerValue="--- Select ---" name="budgetDetail.functionary.id"  value="functionary.id" id="budgetReAppropriation_functionary"></s:select></td>
		</s:if>
	</tr>
	<tr>
		<s:if test="%{shouldShowHeaderField('scheme') || shouldShowGridField('scheme')}">
				<td width="10%" class="bluebox">&nbsp;</td>
				<td class="greybox">
					<s:text name="scheme"/>
				</td>
			    <td class="greybox"><s:select list="dropdownData.schemeList"  listKey="id" listValue="name" headerKey="0" headerValue="--- Select ---" name="budgetDetail.scheme.id" onchange="populateSubSchemes(this);" value="scheme.id" id="budgetReAppropriation_scheme"></s:select></td>
		</s:if>
		<s:if test="%{shouldShowHeaderField('subScheme') || shouldShowGridField('subScheme')}">
				<egov:ajaxdropdown id="subScheme" fields="['Text','Value']" dropdownId="budgetReAppropriation_subScheme" url="budget/budgetDetail!ajaxLoadSubSchemes.action" afterSuccess="onHeaderSubSchemePopulation"/>
				<td class="greybox">
					<s:text name="subscheme"/>
				</td>
			    <td class="greybox"><s:select list="dropdownData.subSchemeList"  listKey="id" listValue="name" headerKey="0" headerValue="--- Select ---" name="budgetDetail.subScheme"  value="subScheme.id" id="budgetReAppropriation_subScheme"></s:select></td>
		</s:if>
		
	</tr>
	<tr>
		<s:if test="%{shouldShowHeaderField('boundary') || shouldShowGridField('boundary')}">
				<td class="bluebox">
					<s:text name="field"/>
				</td>
			    <td class="bluebox"><s:select list="dropdownData.boundaryList"  listKey="id" listValue="name" headerKey="0" headerValue="--- Select ---" name="budgetDetail.boundary.id"  value="boundary.id" id="budgetReAppropriation_boundary"></s:select></td>
		</s:if>
	</tr>