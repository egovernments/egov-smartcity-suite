<%@ taglib prefix="s" uri="/WEB-INF/struts-tags.tld"%>
<%@ taglib prefix="egov" tagdir="/WEB-INF/tags"%>
<tr>
	<s:if test="%{shouldShowHeaderField('fund')}">
		<td class="bluebox"><s:text name="voucher.fund"/>
		<s:if test="%{isFieldMandatory('fund')}"><span class="mandatory">*</span></s:if></td>
		<td class="bluebox"><s:select name="fund" id="fund" list="dropdownData.fundList" listKey="id" listValue="name" headerKey="-1" headerValue="----Choose----" onChange="getSchemelist(this)"  value="%{fund.id}"/></td>
	</s:if>
	<s:if test="%{shouldShowHeaderField('department')}">
		<td class="bluebox"><s:text name="voucher.department"/>
		<s:if test="%{isFieldMandatory('department')}"><span class="mandatory">*</span></s:if></td>
		<td class="bluebox"><s:select name="department" id="department" list="dropdownData.departmentList" listKey="id" listValue="deptName" headerKey="-1" headerValue="----Choose----"  value="%{department.id}"/></td>
	</s:if>
</tr>
<tr>
	<s:if test="%{shouldShowHeaderField('scheme')}">
		<egov:ajaxdropdown id="schemeid" fields="['Text','Value']" dropdownId="schemeid" url="report/report!ajaxLoadSchemes.action"/>
		<td class="greybox"><s:text name="voucher.scheme"/>
		<s:if test="%{isFieldMandatory('scheme')}"><span class="mandatory">*</span></s:if></td>
		<td class="greybox"><s:select name="scheme" id="scheme" list="dropdownData.schemeList" listKey="id" listValue="name" headerKey="-1" headerValue="----Choose----" onChange="getSubSchemelist(this)"  value="%{scheme.id}"/></td>
	</s:if>
	<s:if test="%{shouldShowHeaderField('subscheme')}">
		<egov:ajaxdropdown id="subscheme" fields="['Text','Value']" dropdownId="subscheme" url="report/report!ajaxLoadSubSchemes.action"/>
		<td class="greybox"><s:text name="voucher.subscheme"/>
		<s:if test="%{isFieldMandatory('subscheme')}"><span class="mandatory">*</span></s:if></td>
		<td class="greybox"><s:select name="subscheme" id="subscheme" list="dropdownData.subschemeList" listKey="id" listValue="name" headerKey="-1" headerValue="----Choose----"  value="%{subscheme.id}"/></td>
	</s:if>
</tr>
<tr>
	<s:if test="%{shouldShowHeaderField('functionary')}">
		<td class="bluebox"><s:text name="voucher.functionary"/>
		<s:if test="%{isFieldMandatory('functionary')}"><span class="mandatory">*</span></s:if></td>
		<td class="bluebox"><s:select name="functionary" id="functionary" list="dropdownData.functionaryList" listKey="id" listValue="name" headerKey="-1" headerValue="----Choose----"  value="%{functionary.id}"/></td>
	</s:if>
	<s:if test="%{shouldShowHeaderField('fundsource')}">
		<td class="bluebox"><s:text name="voucher.fundsource"/>
		<s:if test="%{isFieldMandatory('fundsource')}"><span class="mandatory">*</span></s:if></td>
		<td class="bluebox"><s:select name="fundsource" id="fundsource" list="dropdownData.fundsourceList" listKey="id" listValue="name" headerKey="-1" headerValue="----Choose----"  value="%{fundsource.id}"/></td>
	</s:if>
</tr>
<tr>
	<s:if test="%{shouldShowHeaderField('function')}">
		<td class="greybox"><s:text name="voucher.function"/>
		<s:if test="%{isFieldMandatory('function')}"><span class="mandatory">*</span></s:if></td>
		<td class="greybox"><s:select name="function" id="function" list="dropdownData.functionList" listKey="id" listValue="name" headerKey="-1" headerValue="----Choose----"  value="%{function.id}"/></td>
	</s:if>
	<s:if test="%{shouldShowHeaderField('field')}">
		<td class="greybox">
		<s:if test="%{isFieldMandatory('field')}"><span class="mandatory">*</span></s:if><br><br></td>
		<td class="greybox">&gt;<s:select name="field" id="field" list="dropdownData.fieldList" listKey="id" listValue="name" headerKey="-1" headerValue="----Choose----"  value="%{field.id}"/></td>
	</s:if>
</tr>

<script>
	function getSchemelist(obj)
	{
		if(document.getElementById('scheme'))
			populatescheme({fund:obj.value})
	}
	function getSubSchemelist(obj)
	{
		if(document.getElementById('subscheme'))
			populatesubscheme({scheme:obj.value})
	}
</script>