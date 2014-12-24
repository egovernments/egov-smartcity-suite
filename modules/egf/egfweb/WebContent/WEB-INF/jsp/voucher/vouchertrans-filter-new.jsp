<%@ taglib prefix="s" uri="/WEB-INF/struts-tags.tld"%>
<%@ taglib prefix="egov" tagdir="/WEB-INF/tags"%>
<tr>
	<td class="greybox"></td>
	<s:if test="%{shouldShowHeaderField('fund')}">
	<td class="greybox"><s:text name="voucher.fund"/>
	<s:if test="%{isFieldMandatory('fund')}"><span class="bluebox"><span class="mandatory">*</span></span></s:if></td>
	<td class="greybox"><s:select name="fundId" id="fundId" list="dropdownData.fundList" listKey="id" listValue="name" headerKey="" headerValue="----Choose----" onChange="populateSchemes(this);loadBank(this);"  value="%{fundId.id}"/></td>
	</s:if>
	<s:if test="%{shouldShowHeaderField('scheme')}">
	<egov:ajaxdropdown id="scheme"fields="['Text','Value']" dropdownId="schemeid" url="voucher/common!ajaxLoadSchemes.action" />

	<td class="greybox"><s:text name="voucher.scheme"/>
	<s:if test="%{isFieldMandatory('scheme')}"><span class="mandatory">*</span></s:if></td>
	<td class="greybox"><s:select list="dropdownData.schemeList" name="vouchermis.schemeid" id="schemeid" listKey="id" listValue="name" headerKey="-1" headerValue="----Choose----" onChange= "populatesubSchemes(this)"  value="voucherHeader.vouchermis.schemeid.id"/></td>
	</s:if>
</tr>
<tr>
<td class="bluebox" ></td>

	<s:if test="%{shouldShowHeaderField('subscheme')}">
	<egov:ajaxdropdown id="subscheme"fields="['Text','Value']" dropdownId="subschemeid" url="voucher/common!ajaxLoadSubSchemes.action" />
	<td class="bluebox"><s:text name="voucher.subscheme"/>
	<s:if test="%{isFieldMandatory('subscheme')}"><span class="mandatory">*</span></s:if></td>
	<td class="bluebox"><s:select name="vouchermis.subschemeid" id="subschemeid" list="dropdownData.subschemeList" listKey="id" listValue="name" headerKey="-1" headerValue="----Choose----"  value="voucherHeader.vouchermis.subschemeid.id" onChange= "populateFundSource(this)"/></td>
	</s:if>
	
	<s:if test="%{shouldShowHeaderField('fundsource')}">
	<egov:ajaxdropdown id="fundsource"fields="['Text','Value']" dropdownId="fundsourceId" url="voucher/common!ajaxLoadFundSource.action" />
	<td class="bluebox"><s:text name="voucher.fundsource"/>
	<s:if test="%{isFieldMandatory('fundsource')}"><span class="bluebox"><span class="mandatory">*</span></span></s:if></td>
	<td class="bluebox"><s:select name="vouchermis.fundsource" id="fundsourceId" list="dropdownData.fundsourceList" listKey="id" listValue="name" headerKey="-1" headerValue="----Choose----"  value="voucherHeader.vouchermis.fundsource.id"/></td>
	</s:if>
</tr>
<tr>
<td class="greybox"></td>
<s:if test="%{shouldShowHeaderField('department')}">
	<td class="greybox"><s:text name="voucher.department"/>
	<s:if test="%{isFieldMandatory('department')}"><span class="bluebox"><span class="mandatory">*</span></span></s:if></td>
	<td class="greybox"><s:select name="vouchermis.departmentid" id="vouchermis.departmentid" list="dropdownData.departmentList" listKey="id" listValue="deptName" headerKey="" headerValue="----Choose----"  value="voucherHeader.vouchermis.departmentid.id" onChange="populateApproverDept(this);" /></td>
	</s:if>
<s:if test="%{shouldShowHeaderField('field')}">
	<td class="greybox"><s:text name="voucher.field"/>
	<s:if test="%{isFieldMandatory('field')}"><span class="mandatory">*</span></s:if></td>
	<td class="greybox"><s:select name="vouchermis.divisionid" id="vouchermis.divisionid" list="dropdownData.fieldList" listKey="id" listValue="name" headerKey="-1" headerValue="----Choose----"  value="voucherHeader.vouchermis.divisionid.id"/></td>
	</s:if>
</tr>
<tr>
<td class="bluebox"></td>
<s:if test="%{shouldShowHeaderField('functionary')}">
	<td class="bluebox"><s:text name="voucher.functionary"/>
	<s:if test="%{isFieldMandatory('functionary')}"><span class="bluebox"><span class="mandatory">*</span></span></s:if></td>
	<td class="bluebox"><s:select name="vouchermis.functionary" id="vouchermis.functionary" list="dropdownData.functionaryList" listKey="id" listValue="name" headerKey="-1" headerValue="----Choose----"  value="voucherHeader.vouchermis.functionary.id" style="width:180px"/></td>
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
			<s:if test="%{isFieldMandatory('vouchernumber')}"> 
				 if(null != document.getElementById('voucherNumber') && document.getElementById('voucherNumber').value.trim().length == 0 ){

					document.getElementById('lblError').innerHTML = "Please enter a voucher number";
					return false;
				 }
			 </s:if>
		 <s:if test="%{isFieldMandatory('voucherdate')}"> 
				 if(null != document.getElementById('voucherDate') && document.getElementById('voucherDate').value.trim().length == 0){

					document.getElementById('lblError').innerHTML = "Please enter a voucher date";
					return false;
				 }
			 </s:if>
		 <s:if test="%{isFieldMandatory('fund')}"> 
				 if(null != document.getElementById('fundId') && document.getElementById('fundId').value ==""){

					document.getElementById('lblError').innerHTML = "Please Select a fund";
					return false;
				 }
			 </s:if>
			<s:if test="%{isFieldMandatory('department')}"> 
				 if(null!= document.getElementById('vouchermis.departmentid') && document.getElementById('vouchermis.departmentid').value ==""){

					document.getElementById('lblError').innerHTML = "Please select a department";
					return false;
				 }
			</s:if>
			<s:if test="%{isFieldMandatory('scheme')}"> 
				 if(null!=document.getElementById('schemeid') &&  document.getElementById('schemeid').value ==""){

					document.getElementById('lblError').innerHTML = "Please select a scheme";
					return false;
				 }
			</s:if>
			<s:if test="%{isFieldMandatory('subscheme')}"> 
				 if(null!= document.getElementById('subschemeid') && document.getElementById('subschemeid').value ==""){

					document.getElementById('lblError').innerHTML = "Please select a subscheme";
					return false;
				 }
			</s:if>
			<s:if test="%{isFieldMandatory('functionary')}"> 
				 if(null!=document.getElementById('vouchermis.functionary') &&  document.getElementById('vouchermis.functionary').value ==""){

					document.getElementById('lblError').innerHTML = "Please select a functionary";
					return false;
				 }
			</s:if>
			<s:if test="%{isFieldMandatory('fundsource')}"> 
				 if(null !=document.getElementById('fundsourceId') &&  document.getElementById('fundsourceId').value ==""){

					document.getElementById('lblError').innerHTML = "Please select a fundsource";
					return false;
				}
			</s:if>
			<s:if test="%{isFieldMandatory('field')}"> 
				 if(null!= document.getElementById('vouchermis.divisionid') && document.getElementById('vouchermis.divisionid').value ==""){

					document.getElementById('lblError').innerHTML = "Please select a field";
					return false;
				 }
			</s:if>
			return  true;
}
function populateApproverDept(dept){
	
		if(null != document.getElementById('departmentid') &&  null != document.getElementById('approverUserId')){
			if(dept.options[dept.selectedIndex].value != ""){
				document.getElementById('departmentid').value = dept.options[dept.selectedIndex].value;
			}else{
				document.getElementById('departmentid').value = -1;
			}
		
			
		}
if(null != document.getElementById('departmentid')){
		<s:if test="%{isFieldMandatory('department')}"> 
				document.getElementById('departmentid').disabled="true";
				populateUser();
		</s:if>
		<s:else>
			populateDesg();
		</s:else>
	}
}
	</script>