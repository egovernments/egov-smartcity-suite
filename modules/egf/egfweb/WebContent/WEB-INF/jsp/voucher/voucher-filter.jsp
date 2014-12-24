<%@ taglib prefix="s" uri="/WEB-INF/struts-tags.tld"%>
<%@ taglib prefix="egov" tagdir="/WEB-INF/tags"%>
<tr>
	<s:if test="%{shouldShowHeaderField('fund')}">
		<td class="bluebox"><s:text name="voucher.fund"/>
		<s:if test="%{isFieldMandatory('fund')}"><span class="mandatory">*</span></s:if></td>
		<td class="bluebox"><s:select name="fundId" id="fundId" list="dropdownData.fundList" listKey="id" listValue="name" headerKey="-1" headerValue="----Choose----" onChange="getSchemelist(this)"  value="%{fundId.id}"/></td>
	</s:if>
	<s:if test="%{shouldShowHeaderField('department')}">
		<td class="bluebox"><s:text name="voucher.department"/>
		<s:if test="%{isFieldMandatory('department')}"><span class="mandatory">*</span></s:if></td>
		<td class="bluebox"><s:select name="vouchermis.departmentid" id="vouchermis.departmentid" list="dropdownData.departmentList" listKey="id" listValue="deptName" headerKey="-1" headerValue="----Choose----"  value="%{vouchermis.departmentid.id}"/></td>
	</s:if>
</tr>
<tr>
	<s:if test="%{shouldShowHeaderField('scheme')}">
		<egov:ajaxdropdown id="schemeid" fields="['Text','Value']" dropdownId="schemeid" url="voucher/common!ajaxLoadSchemes.action"/>
		<td class="greybox"><s:text name="voucher.scheme"/>
		<s:if test="%{isFieldMandatory('scheme')}"><span class="mandatory">*</span></s:if></td>
		<td class="greybox"><s:select name="vouchermis.schemeid" id="schemeid" list="dropdownData.schemeList" listKey="id" listValue="name" headerKey="-1" headerValue="----Choose----" onChange="getSubSchemelist(this)"  value="%{vouchermis.schemeid.id}"/></td>
	</s:if>
	<s:if test="%{shouldShowHeaderField('subscheme')}">
		<egov:ajaxdropdown id="subschemeid" fields="['Text','Value']" dropdownId="subschemeid" url="voucher/common!ajaxLoadSubSchemes.action"/>
		<td class="greybox"><s:text name="voucher.subscheme"/>
		<s:if test="%{isFieldMandatory('subscheme')}"><span class="mandatory">*</span></s:if></td>
		<td class="greybox"><s:select name="vouchermis.subschemeid" id="subschemeid" list="dropdownData.subschemeList" listKey="id" listValue="name" headerKey="-1" headerValue="----Choose----"  value="%{vouchermis.subschemeid.id}"/></td>
	</s:if>
</tr>
<tr>
	<s:if test="%{shouldShowHeaderField('functionary')}">
		<td class="bluebox"><s:text name="voucher.functionary"/>
		<s:if test="%{isFieldMandatory('functionary')}"><span class="mandatory">*</span></s:if></td>
		<td class="bluebox"><s:select name="vouchermis.functionary" id="vouchermis.functionary" list="dropdownData.functionaryList" listKey="id" listValue="name" headerKey="-1" headerValue="----Choose----"  value="%{vouchermis.functionary.id}"/></td>
	</s:if>
	<s:if test="%{shouldShowHeaderField('fundsource')}">
		<td class="bluebox"><s:text name="voucher.fundsource"/>
		<s:if test="%{isFieldMandatory('fundsource')}"><span class="mandatory">*</span></s:if></td>
		<td class="bluebox"><s:select name="fundsourceId" id="fundsourceId" list="dropdownData.fundsourceList" listKey="id" listValue="name" headerKey="-1" headerValue="----Choose----"  value="%{vouchermis.fundsource.id}"/></td>
	</s:if>
</tr>
<tr>
	<s:if test="%{shouldShowHeaderField('field')}">
		<td class="greybox"><s:text name="voucher.field"/>
		<s:if test="%{isFieldMandatory('field')}"><span class="mandatory">*</span></s:if><br><br></td>
		<td class="greybox"><s:select name="vouchermis.divisionid" id="vouchermis.divisionid" list="dropdownData.fieldList" listKey="id" listValue="name" headerKey="-1" headerValue="----Choose----"  value="%{vouchermis.divisionid.id}"/></td>
	</s:if>
</tr>

<script>
	function getSchemelist(obj)
	{
		if(document.getElementById('schemeid'))
			populateschemeid({fundId:obj.value})
	}
	function getSubSchemelist(obj)
	{
		if(document.getElementById('subschemeid'))
			populatesubschemeid({schemeId:obj.value})
	}
</script>