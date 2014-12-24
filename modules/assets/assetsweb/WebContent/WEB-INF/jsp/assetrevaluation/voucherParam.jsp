<%@ taglib prefix="s" uri="/WEB-INF/struts-tags.tld"%>
<%@ taglib prefix="egov" tagdir="/WEB-INF/tags"%>
<tr>
	<s:if test="%{shouldShowHeaderField('fund')}">
	<td  width="25%" class="greyboxwk"><s:text name="voucher.fund"/>
	<s:if test="%{isFieldMandatory('fund')}"><span class="bluebox"><span class="mandatory">*</span></span></s:if></td>
	<td width="25%" class="greybox2wk"><s:select name="fund" id="fundId" list="dropdownData.fundList" listKey="id" listValue="name" headerKey="-1" headerValue="%{getText('list.default.select')}" onChange="populateSchemes(this);"   value="%{fund.id}"/></td>
	</s:if>
	<s:if test="%{shouldShowHeaderField('scheme')}">
	<egov:ajaxdropdown id="scheme"fields="['Text','Value']" dropdownId="schemeid" url="/assetmaster/ajaxAsset!ajaxLoadSchemes.action" />

	<td width="25%" class="greyboxwk"><s:text name="voucher.scheme"/>
	<s:if test="%{isFieldMandatory('scheme')}"><span class="mandatory">*</span></s:if></td>
	<td width="25%" class="greybox2wk"><s:select list="dropdownData.schemeList" name="scheme" id="schemeid" listKey="id" listValue="name" headerKey="-1" headerValue="%{getText('list.default.select')}" onChange= "populatesubSchemes(this)"  value="%{scheme.id}"/></td>
	</s:if>
	
</tr>
<tr>
	<s:if test="%{shouldShowHeaderField('subscheme')}">
	<egov:ajaxdropdown id="subscheme"fields="['Text','Value']" dropdownId="subschemeid" url="/assetmaster/ajaxAsset!ajaxLoadSubSchemes.action" />
	<td width="25%" class="whiteboxwk"><s:text name="voucher.subscheme"/>
	<s:if test="%{isFieldMandatory('subscheme')}"><span class="mandatory">*</span></s:if></td>
	<td width="25%" class="whitebox2wk"><s:select name="subscheme" id="subschemeid" list="dropdownData.subschemeList" listKey="id" listValue="name" headerKey="-1" headerValue="%{getText('list.default.select')}"  value="%{subscheme.id}" onChange= "populateFundSource(this)"/></td>
	</s:if>
	<s:if test="%{shouldShowHeaderField('fundsource')}">
	<egov:ajaxdropdown id="fundsource"fields="['Text','Value']" dropdownId="fundsourceId" url="/assetmaster/ajaxAsset!ajaxLoadFundSource.action" />
	<td width="25%" class="whiteboxwk"><s:text name="voucher.fundsource"/>
	<s:if test="%{isFieldMandatory('fundsource')}"><span class="bluebox"><span class="mandatory">*</span></span></s:if></td>
	<td width="25%" class="whitebox2wk"><s:select name="fundsource" id="fundsourceId" list="dropdownData.fundsourceList" listKey="id" listValue="name" headerKey="-1" headerValue="%{getText('list.default.select')}"  value="%{fundsource.id}"/></td>
	</s:if>
</tr>
<tr>
<s:if test="%{shouldShowHeaderField('department')}">
	<td width="25%" class="greyboxwk" id="deptLabel"><s:text name="voucher.department"/>
	<s:if test="%{isFieldMandatory('department')}"><span class="bluebox"><span class="mandatory">*</span></span></s:if></td>
	<td width="25%" class="greybox2wk"><s:select name="department" id="departmentid" list="dropdownData.departmentList" listKey="id" listValue="deptName" headerKey="-1" headerValue="%{getText('list.default.select')}" value="%{department.id}"/></td>
	</s:if>

	<s:if test="%{shouldShowHeaderField('field')}">
	<td width="25%"  class="greyboxwk"><s:text name="voucher.field"/>
	<s:if test="%{isFieldMandatory('field')}"><span class="mandatory">*</span></s:if></td>
	<td width="25%" class="greybox2wk"><s:select name="field" id="fieldid" list="dropdownData.fieldList" listKey="id" listValue="name" headerKey="-1" headerValue="%{getText('list.default.select')}"  value="%{field.id}"/></td>
	</s:if>
</tr>
<tr>
<s:if test="%{shouldShowHeaderField('functionary')}">
	<td width="25%" class="whiteboxwk"><s:text name="voucher.functionary"/>
	<s:if test="%{isFieldMandatory('functionary')}"><span class="bluebox"><span class="mandatory">*</span></span></s:if></td>
	<td width="25%" class="whitebox2wk"><s:select name="functionary" id="functionaryId" list="dropdownData.functionaryList" listKey="id" listValue="name" headerKey="-1" headerValue="%{getText('list.default.select')}" value="%{functionary.id}"/></td>
	</s:if>

</tr>
<script>
function populateSchemes(fund){
	if(null != document.getElementById("schemeid")){
		populateschemeid({fundId:fund.options[fund.selectedIndex].value});
		populatesubschemeid({schemeId:-1});
		populatefundsourceId({subSchemeId:-1});	
	}
		
}
function populatesubSchemes(scheme){
	
	populatesubschemeid({schemeId:scheme.options[scheme.selectedIndex].value});	
	populatefundsourceId({subSchemeId:-1});
}
function populateFundSource(subSchemeId){
	
	populatefundsourceId({subSchemeId:subSchemeId.options[subSchemeId.selectedIndex].value});	
}
function validateMIS(){
	// Javascript validation of the MIS Manadate attributes.

		 <s:if test="%{isFieldMandatory('fund')}"> 
				 if(null != document.getElementById('fundId') && document.getElementById('fundId').value == -1){

					return "Please Select a fund";
				 }
			 </s:if>
			<s:if test="%{isFieldMandatory('department')}"> 
				 if(null != document.getElementById('departmentid') && document.getElementById('departmentid').value == -1){
					return "Please select a department";
				 }
			</s:if>
			<s:if test="%{isFieldMandatory('scheme')}"> 
				 if(null !=document.getElementById('schemeid') &&  document.getElementById('schemeid').value == -1){

					return "Please select a scheme";
				 }
			</s:if>
			<s:if test="%{isFieldMandatory('subscheme')}"> 
				 if(null != document.getElementById('subschemeid') && document.getElementById('subschemeid').value == -1){

					return "Please select a subscheme";
				 }
			</s:if>
			<s:if test="%{isFieldMandatory('functionary')}"> 
				 if(null !=document.getElementById('functionaryId') &&  document.getElementById('functionaryId').value == -1){

					return "Please select a functionary";
				 }
			</s:if>
			<s:if test="%{isFieldMandatory('fundsource')}"> 
				 if(null !=document.getElementById('fundsourceId') &&  document.getElementById('fundsourceId').value == -1){

					return "Please select a fundsource";
				}
			</s:if>
			<s:if test="%{isFieldMandatory('field')}"> 
				 if(null!= document.getElementById('fieldid') && document.getElementById('fieldid').value == -1){

					return "Please select a field";
				 }
			</s:if>
			return  "";
}
</script>