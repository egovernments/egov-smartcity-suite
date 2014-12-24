<%@ taglib prefix="s" uri="/WEB-INF/struts-tags.tld"%>
<%@ taglib prefix="egov" tagdir="/WEB-INF/tags"%>
<tr>
	<s:if test="%{shouldShowHeaderField('fund')}">
		<td class="bluebox"><s:text name="bill.search.fund"/>
		<s:if test="%{isFieldMandatory('fund')}"><span class="mandatory">*</span></s:if></td>
		<td class="bluebox"><s:select name="egBillregistermis.fund" id="fundId" list="dropdownData.fundList" listKey="id" listValue="name" headerKey="-1" headerValue="----Choose----"   value="%{egBillregistermis.fund.id}" onChange="populateSchemes(this);"/></td>
	</s:if>
	<s:if test="%{shouldShowHeaderField('department')}">
		<td class="bluebox"><s:text name="bill.search.dept"/>
		<s:if test="%{isFieldMandatory('department')}"><span class="mandatory">*</span></s:if></td>
		<td class="bluebox"><s:select name="egBillregistermis.egDepartment" id="departmentid" list="dropdownData.departmentList" listKey="id" listValue="deptName" headerKey="-1" headerValue="----Choose----"  value="%{egBillregistermis.egDepartment.id}"/></td>
	</s:if>
</tr>
<tr>
	<s:if test="%{shouldShowHeaderField('scheme')}">
		<egov:ajaxdropdown id="schemeid" fields="['Text','Value']" dropdownId="schemeid" url="voucher/common!ajaxLoadSchemes.action"/>
		<td class="greybox"><s:text name="bill.search..scheme"/>
		<s:if test="%{isFieldMandatory('scheme')}"><span class="mandatory">*</span></s:if></td>
		<td class="greybox"><s:select name="egBillregistermis.scheme" id="schemeid" list="dropdownData.schemeList" listKey="id" listValue="name" headerKey="-1" headerValue="----Choose----"  value="%{egBillregistermis.scheme.id}" onChange= "populatesubSchemes(this)"/></td>
	</s:if>
	<s:if test="%{shouldShowHeaderField('subscheme')}">
		<egov:ajaxdropdown id="subschemeid" fields="['Text','Value']" dropdownId="subschemeid" url="voucher/common!ajaxLoadSubSchemes.action"/>
		<td class="greybox"><s:text name="bill.search..subscheme"/>
		<s:if test="%{isFieldMandatory('subscheme')}"><span class="mandatory">*</span></s:if></td>
		<td class="greybox"><s:select name="egBillregistermis.subScheme" id="subschemeid" list="dropdownData.subschemeList" listKey="id" listValue="name" headerKey="-1" headerValue="----Choose----"  value="%{egBillregistermis.subScheme.id}"/></td>
	</s:if>
</tr>
<tr>
	<s:if test="%{shouldShowHeaderField('functionary')}">
		<td class="bluebox"><s:text name="bill.search..functionary"/>
		<s:if test="%{isFieldMandatory('functionary')}"><span class="mandatory">*</span></s:if></td>
		<td class="bluebox"><s:select name="egBillregistermis.functionaryid" id="functionaryId" list="dropdownData.functionaryList" listKey="id" listValue="name" headerKey="-1" headerValue="----Choose----"  value="%{egBillregistermis.subScheme.id}"/></td>
	</s:if>
	<s:if test="%{shouldShowHeaderField('fundsource')}">
		<td class="bluebox"><s:text name="bill.search..fundsource"/>
		<s:if test="%{isFieldMandatory('fundsource')}"><span class="mandatory">*</span></s:if></td>
		<td class="bluebox"><s:select name="egBillregistermis.fundsource" id="fundsourceId" list="dropdownData.fundsourceList" listKey="id" listValue="name" headerKey="-1" headerValue="----Choose----"  value="%{egBillregistermis.fundsource.id}"/></td>
	</s:if>
</tr>
<tr>
	<s:if test="%{shouldShowHeaderField('field')}">
		<td class="greybox"><s:text name="bill.search..field"/>
		<s:if test="%{isFieldMandatory('field')}"><span class="mandatory">*</span></s:if><br><br></td>
		<td class="greybox"><s:select name="egBillregistermis.fieldid" id="divisionid" list="dropdownData.fieldList" listKey="id" listValue="name" headerKey="-1" headerValue="----Choose----"  value="%{egBillregistermis.fieldid.id}"/></td>
	</s:if>
</tr>
<script>
function populateSchemes(fund){
	if(null != document.getElementById("schemeid")){
		populateschemeid({fundId:fund.options[fund.selectedIndex].value})
	}
		
	}
function populatesubSchemes(scheme){
	
	populatesubschemeid({schemeId:scheme.options[scheme.selectedIndex].value})	
	}
</script>