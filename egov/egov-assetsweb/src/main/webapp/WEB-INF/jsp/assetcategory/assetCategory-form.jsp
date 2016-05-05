<%--
  ~ eGov suite of products aim to improve the internal efficiency,transparency,
  ~    accountability and the service delivery of the government  organizations.
  ~
  ~     Copyright (C) <2015>  eGovernments Foundation
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
  --%>

<style type="text/css">
#yui-dt0-bodytable,#yui-dt1-bodytable,#yui-dt2-bodytable {
	Width: 100%;
}
</style>

<script>
function goBack()
{
	window.location="${pageContext.request.contextPath}/assetcategory/assetCategory-edit.action";	
}

function goNewForm()
{
	window.location="${pageContext.request.contextPath}/assetcategory/assetCategory-newform.action";	
}

function setupAjaxAssettype(elem){
    resetParentFields();
    //TODO : Phoenix migration - commented out since ajax call giving an exception
	populateparentcat({assetType:elem.value});
	disableDepreciation(elem.value);
}
function disableDepreciation(assettype){
	if(assettype == '<s:property value="@org.egov.asset.model.AssetType@LAND" />'){
		document.getElementById('depreciationMethod').disabled=true;
		document.getElementById('accDepAccountCode').disabled=true;
		document.getElementById('depExpAccountCode').disabled=true;
	}else{
		document.getElementById('depreciationMethod').disabled=false;
		document.getElementById('accDepAccountCode').disabled=false;
		document.getElementById('depExpAccountCode').disabled=false;
	}
}
function setupAjaxParentcat(elem){
	
    dom.get("category_error").style.display='none';
    parent_cat_id=elem.options[elem.selectedIndex].value;
    makeJSONCall(["xDepreciationMethod","xAssetAccountCode","xAccDepAccountCode","xRevAccountCode","xDepExpAccountCode",
    	 "xUom","xCatAttrTemplate"],
    	'${pageContext.request.contextPath}/assetcategory/ajaxAssetCategory-populateParentDetails.action',
    	{parentCatId:parent_cat_id},mySuccessHandler,myFailureHandler) ;
}

function resetParentFields(){
	document.getElementById('depreciationMethod').value='';
	document.getElementById('assetAccountCode').value=-1;
	document.getElementById('accDepAccountCode').value=-1;
	document.getElementById('revAccountCode').value=-1;
	document.getElementById('depExpAccountCode').value=-1;
	document.getElementById('uom').value=-1;
	document.getElementById('catTemVal').value='';
}

mySuccessHandler = function(req,res){
  results=res.results;
     asset_type_id=document.assetCategoryForm.assetType.value;
    if(asset_type_id!=1){
  if(results[0].xDepreciationMethod!="")
	document.getElementById('depreciationMethod').value=results[0].xDepreciationMethod;
  else
	document.getElementById('depreciationMethod').value='';
	}
  if(results[0].xAssetAccountCode!="")
	document.getElementById('assetAccountCode').value=results[0].xAssetAccountCode;
  else
	document.getElementById('assetAccountCode').value=-1;
	if(asset_type_id!=1){
  if(results[0].xAccDepAccountCode!="")
	document.getElementById('accDepAccountCode').value=results[0].xAccDepAccountCode;
  else
	document.getElementById('accDepAccountCode').value=-1;
	}
  if(results[0].xRevAccountCode!="")
	document.getElementById('revAccountCode').value=results[0].xRevAccountCode;
  else
	document.getElementById('revAccountCode').value=-1;
	if(asset_type_id!=1){
   if(results[0].xDepExpAccountCode!="")
	document.getElementById('depExpAccountCode').value=results[0].xDepExpAccountCode;
  else
	document.getElementById('depExpAccountCode').value=-1;	
	}
  if(results[0].xUom!="")
	document.getElementById('uom').value=results[0].xUom;
  else
	document.getElementById('uom').value=-1;
  
  if(!results[0].xCatAttrTemplate.blank()) 
  	$('catTemVal').value = results[0].xCatAttrTemplate.gsub('&quot;','')
  else
  	$('catTemVal').value = '';
}

myFailureHandler= function(){
    dom.get("category_error").style.display='block';
	document.getElementById("category_error").innerHTML='Unable to load Parent Category Details';
}

parentCatDetailsSuccessHandler = function(req,res){
  results=res.results;
  depreciationMethod.Dropdown.selectedIndex=results.DepreciationMethod;
  assetAccountCode.Dropdown.selectedIndex=results.AssetAccountCode;
  accDepAccountCode.Dropdown.selectedIndex=results.AccDepAccountCode;
  revAccountCode.Dropdown.selectedIndex=results.RevAccountCode;
  depExpAccountCode.Dropdown.selectedIndex=results.DepExpAccountCode;
}

</script>

<table id="formTable" width="100%" border="0" cellspacing="0" cellpadding="0">
	<tr>
		<td>
			&nbsp;
		</td>
	</tr>
	<tr>
		<td>
			<table id="catDetailsTable" width="100%" border="0" cellspacing="0"
				cellpadding="0">
				<tr>
					<td colspan="4" class="headingwk">
						<div class="arrowiconwk">
							<img src="/egassets/resources/erp2/images/arrow.gif" />
						</div>
						<div class="headplacer">
							<s:text name='title.asset.cat.details' />
						</div>
					</td>
				</tr>
				<tr>
					<td width="11%" class="whiteboxwk">
						<span class="mandatory">*</span>
						<s:text name="asset.cat.code" />:
					</td>
					<td width="21%" class="whitebox2wk" colspan="3">
						<div id="codeLabel" class="estimateno" style="display:none;"><s:if test="%{not code}">&lt; Not Assigned &gt;</s:if><s:property value="code" /></div>
						<s:textfield label="code" name="code" value="%{code}" id="code" 
						disabled="%{fDisabled}"	cssClass="selectwk" maxlength="30" />
					</td>
				</tr>
				<tr>
					<td width="15%" class="whiteboxwk">
						<span class="mandatory">*</span>
						<s:text name="asset.cat.name" />
					</td>
					<td width="53%" class="whitebox2wk" colspan="3">
						<s:textfield label="name" name="name" value="%{name}" id="name" 
						disabled="%{fDisabled}"	cssClass="selectwk" maxlength="255" />
					</td>
				</tr>
				<tr>
					<td width="11%" class="greyboxwk">
						<span class="mandatory">*</span> 
						<s:text name="asset.cat.type" />
					</td>
					<td width="21%" class="greybox2wk" colspan="3">
						<s:select headerKey="-1" disabled="%{sDisabled}"
							headerValue="%{getText('list.default.select')}" name="assetType"
							id="assetType" cssClass="selectwk"
							list="dropdownData.assetTypeList" 
							onChange="setupAjaxAssettype(this);"/>
						<egov:ajaxdropdown id="parentcat" fields="['Text','Value']" 
							dropdownId="parentcat" url="assetcategory/ajaxAssetCategory-populateParentCategories.action" 
							selectedValue="%{parent.id}"/>
					</td>
				</tr>
				<tr>
					<td width="11%" class="whiteboxwk">
						<s:text name="asset.cat.parent" />
					</td>
					<td width="21%" class="whitebox2wk" colspan="3">
						<s:select headerKey="-1" disabled="%{sDisabled}"
							headerValue="%{getText('list.default.select')}" name="parentId"
							id="parentcat" cssClass="selectwk"
							list="parentMap" value="%{parent.id}" onChange="setupAjaxParentcat(this);"/>
						
					</td>
				</tr>
				<tr>
					<td width="11%" class="greyboxwk">
						<s:text	name="asset.dep.method" />
					</td>
					<td width="21%" class="greybox2wk" colspan="3">
						<s:select headerKey="" disabled="%{sDisabled}"
							headerValue="%{getText('list.default.select')}" name="depreciationMethod"
							id="depreciationMethod" cssClass="selectwk"
							list="dropdownData.depreciationMethodList"/>
					</td>
				</tr>
				<tr>
					<td width="11%" class="whiteboxwk">
						<span class="mandatory">*</span>
						<s:text	name="asset.acc.code" />
					</td>
					<td width="21%" class="whitebox2wk" colspan="3">
						<s:select headerKey="-1" disabled="%{fDisabled}"
							headerValue="%{getText('list.default.select')}" name="assetAccountCode"
							id="assetAccountCode" cssClass="selectwk"
							list="dropdownData.assetAccountCodeList" listKey="id" 
							listValue='glcode  + " : " + name'	value="%{assetAccountCode.id}" />
					</td>
				</tr>
				<tr>
					<td width="11%" class="greyboxwk">
						<s:text	name="asset.accumulated.dep.code" />
					</td>
					<td width="21%" class="greybox2wk" colspan="3">
						<s:select headerKey="-1" disabled="%{sDisabled}"
							headerValue="%{getText('list.default.select')}" name="accDepAccountCode"
							id="accDepAccountCode" cssClass="selectwk"
							list="dropdownData.accDepAccountCodeList" listKey="id" 
							listValue='glcode  + " : " + name'	value="%{accDepAccountCode.id}" />
					</td>
				</tr>
				<tr>
					<td width="11%" class="whiteboxwk">
						<span class="mandatory">*</span>
						<s:text	name="asset.reval.acc.code" />
					</td>
					<td width="21%" class="whitebox2wk" colspan="3">
						<s:select headerKey="-1" disabled="%{fDisabled}"
							headerValue="%{getText('list.default.select')}" name="revAccountCode"
							id="revAccountCode" cssClass="selectwk"
							list="dropdownData.revAccountCodeList" listKey="id" 
							listValue='glcode  + " : " + name'	value="%{revAccountCode.id}" />
					</td>
				</tr>
				
				<tr>
					<td width="11%" class="greyboxwk">
						<s:text	name="asset.dep.exp.acc" />
					</td>
					<td width="21%" class="greybox2wk" colspan="3">
						<s:select headerKey="-1" disabled="%{sDisabled}"
							headerValue="%{getText('list.default.select')}" name="depExpAccountCode"
							id="depExpAccountCode" cssClass="selectwk"
							list="dropdownData.depExpAccountCodeList" listKey="id" 
							listValue='glcode  + " : " + name'	value="%{depExpAccountCode.id}" />
					</td>
				</tr>
				<tr>
					<td width="15%" class="whiteboxwk">
						<span class="mandatory">*</span>
						<s:text	name="asset.uom" />
					</td>
					<td width="53%" class="whitebox2wk" colspan="3">
						<s:select headerKey="-1" disabled="%{fDisabled}"
							headerValue="%{getText('list.default.select')}" name="uom"
							id="uom" cssClass="selectwk"
							list="dropdownData.uomList" listKey="id" listValue='uom'
							value="%{uom.id}" />
					</td>
				</tr>
				<tr>
					<td width="11%" class="greyboxwk">
						<s:text	name="asset.cat.attr.template" />
					</td>
					<td width="21%" class="greybox2wk" colspan="3">
						<s:hidden name="catAttrTemplate" id="catAttrTemplate"/>
						<s:textarea name="catTemVal" value="%{catAttrTemplate}" rows="5" cols="40"
							id="catTemVal" disabled="%{fDisabled}" cssClass="selectwk"/>
					</td>
				</tr>

			</table>
		</td>
	</tr>
 	<tr>
 		<td>&nbsp;</td>
 	</tr>
</table>

<script>
	if(!$F('catAttrTemplate').blank())
		$('catTemVal').value = $F('catAttrTemplate').evalJSON();
	
	<s:if test="%{isAutoGeneratedCode=='yes' || isAutoGeneratedCode==null}">
	   document.getElementById('code').readonly=true;	
	   document.getElementById('code').disabled=true;
	   dom.get("codeLabel").style.display='';
	   $('code').hide();
	</s:if>
</script>
