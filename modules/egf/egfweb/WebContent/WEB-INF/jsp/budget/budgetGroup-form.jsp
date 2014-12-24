<%@ taglib prefix="s" uri="/WEB-INF/struts-tags.tld"%>
	<table width="100%" border="0" cellspacing="0" cellpadding="0">
		<s:push value="model">
			<tr>
				<td class="bluebox"><s:text name="budgetgroup.groupname"/> <span class="mandatory">*</span></td>
				<td class="bluebox"><s:textfield name="name" id="name" maxlength="50" size="60"/><span class="highlight2">Max. 50 characters</span></td>
			</tr>
			<tr>
				<td class="greybox"><s:text name="budgetgroup.accounttype"/> <span class="mandatory">*</span></td>
				<td class="greybox"><s:select name="accountType" id="accountType" list="dropdownData.accountTypeList"  /> </td>
			</tr>
			<tr>
				<td class="bluebox"><s:text name="budgetgroup.budgetingtype"/> <span class="mandatory">*</span></td>
				<td class="bluebox"><s:select name="budgetingType" id="budgetingType" list="dropdownData.budgetingTypeList" /> </td>
			</tr>
			<tr>
				<td class="greybox"><s:text name="budgetgroup.majorcode"/></td>
				<td class="greybox"><s:select name="majorCode" id="majorCode" list="dropdownData.majorCodeList" listKey="id" listValue="%{glcode+'----'+name}" headerKey="-1"  headerValue="----Select----" value="%{majorCode.id}"/></td>
			</tr>
			<tr>
				<td class="bluebox" colspan="2" style="text-align:center">OR</td>
			</tr>
			<tr>
				<td class="greybox"><s:text name="budgetgroup.mincode"/></td>
				<td class="greybox"><s:select name="minCode" id="minCode" list="dropdownData.minCodeList" listKey="id" listValue="%{glcode+'----'+name}"  headerKey="-1"  headerValue="----Select----" value="%{minCode.id}"/></td>
			</tr>
			<tr>
				<td class="bluebox"><s:text name="budgetgroup.maxcode"/></td>
				<td class="bluebox"><s:select name="maxCode" id="maxCode" list="dropdownData.maxCodeList" listKey="id" listValue="%{glcode+'----'+name}"  headerKey="-1"  headerValue="----Select----" value="%{maxCode.id}"/></td>
			</tr>
			<tr>
				<td class="greybox"><s:text name="budgetgroup.description"/></td>
				<td class="greybox"><s:textarea name="description" id="description" cols="50" rows="5"/><span class="highlight2">Max. 250 characters</span></td>
			</tr>
			<tr>
				<td class="bluebox"><s:text name="budgetgroup.isactive"/></td>
				<td class="bluebox"><s:checkbox name="isActive" id="isActive" /></td>
			</tr>
		</s:push>
	</table>
