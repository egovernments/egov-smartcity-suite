<%@ taglib prefix="s" uri="/struts-tags"%>

<style type="text/css">
#yui-dt0-bodytable,#yui-dt1-bodytable,#yui-dt2-bodytable {
	Width: 100%;
}
</style>

<script>
function goBack()
{
	window.location="${pageContext.request.contextPath}/assetcategory/assetCategory!edit.action";	
}

function goNewForm()
{
	window.location="${pageContext.request.contextPath}/assetcategory/assetCategory!newform.action";	
}

function setupAjaxAssettype(elem){
    asset_type_id=elem.options[elem.selectedIndex].value;
    resetParentFields();
	populateparentcat({assetTypeId:asset_type_id});
	disableDepreciation(asset_type_id);
}
function disableDepreciation(assettype){
if(assettype==1){
document.getElementById('depmethord').disabled=true;
document.getElementById('accdepcode').disabled=true;
document.getElementById('depexpcode').disabled=true;
}else{
document.getElementById('depmethord').disabled=false;
document.getElementById('accdepcode').disabled=false;
document.getElementById('depexpcode').disabled=false;
}
}
function setupAjaxParentcat(elem){
	
    dom.get("category_error").style.display='none';
    parent_cat_id=elem.options[elem.selectedIndex].value;
    makeJSONCall(["xDepmethord","xAssetCode","xAccDepCode","xRevCode","xDepExpCode",
    	 "xUom","xCatAttrTemplate"],
    	'${pageContext.request.contextPath}/assetcategory/ajaxAssetCategory!populateParentDetails.action',
    	{parentCatId:parent_cat_id},mySuccessHandler,myFailureHandler) ;
}

function resetParentFields(){
	document.getElementById('depmethord').value=-1;
	document.getElementById('assetcode').value=-1;
	document.getElementById('accdepcode').value=-1;
	document.getElementById('revcode').value=-1;
	document.getElementById('depexpcode').value=-1;
	document.getElementById('uom').value=-1;
	document.getElementById('catTemVal').value='';
}

mySuccessHandler = function(req,res){
  results=res.results;
     asset_type_id=document.assetCategoryForm.assettype.value;
    if(asset_type_id!=1){
  if(results[0].xDepmethord!="")
	document.getElementById('depmethord').value=results[0].xDepmethord;
  else
	document.getElementById('depmethord').value=-1;
	}
  if(results[0].xAssetCode!="")
	document.getElementById('assetcode').value=results[0].xAssetCode;
  else
	document.getElementById('assetcode').value=-1;
	if(asset_type_id!=1){
  if(results[0].xAccDepCode!="")
	document.getElementById('accdepcode').value=results[0].xAccDepCode;
  else
	document.getElementById('accdepcode').value=-1;
	}
  if(results[0].xRevCode!="")
	document.getElementById('revcode').value=results[0].xRevCode;
  else
	document.getElementById('revcode').value=-1;
	if(asset_type_id!=1){
   if(results[0].xDepExpCode!="")
	document.getElementById('depexpcode').value=results[0].xDepExpCode;
  else
	document.getElementById('depexpcode').value=-1;	
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
  depmethod.Dropdown.selectedIndex=results.DepMethord;
  assetcode.Dropdown.selectedIndex=results.AssetCode;
  accdepcode.Dropdown.selectedIndex=results.AccDepCode;
  revcode.Dropdown.selectedIndex=results.RevCode;
  depexpcode.Dropdown.selectedIndex=results.DepExpCode;
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
							<img src="${pageContext.request.contextPath}/image/arrow.gif" />
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
							id="assettype" cssClass="selectwk"
							list="dropdownData.assetTypeList" listKey="id" listValue='name'
							value="%{assetType.id}" onChange="setupAjaxAssettype(this);"/>
						<egov:ajaxdropdown id="populateParentcat" fields="['Text','Value']" 
							dropdownId='parentcat' url='assetcategory/ajaxAssetCategory!populateParentCategories.action' 
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
						<s:select headerKey="-1" disabled="%{sDisabled}"
							headerValue="%{getText('list.default.select')}" name="depMethord"
							id="depmethord" cssClass="selectwk"
							list="dropdownData.depMethordList" listKey="id" listValue='name'
							value="%{depMethord.id}" />
					</td>
				</tr>
				<tr>
					<td width="11%" class="whiteboxwk">
						<span class="mandatory">*</span>
						<s:text	name="asset.acc.code" />
					</td>
					<td width="21%" class="whitebox2wk" colspan="3">
						<s:select headerKey="-1" disabled="%{fDisabled}"
							headerValue="%{getText('list.default.select')}" name="assetCode"
							id="assetcode" cssClass="selectwk"
							list="dropdownData.assetCodeList" listKey="id" 
							listValue='glcode  + " : " + name'	value="%{assetCode.id}" />
					</td>
				</tr>
				<tr>
					<td width="11%" class="greyboxwk">
						<s:text	name="asset.accumulated.dep.code" />
					</td>
					<td width="21%" class="greybox2wk" colspan="3">
						<s:select headerKey="-1" disabled="%{sDisabled}"
							headerValue="%{getText('list.default.select')}" name="accDepCode"
							id="accdepcode" cssClass="selectwk"
							list="dropdownData.accDepCodeList" listKey="id" 
							listValue='glcode  + " : " + name'	value="%{accDepCode.id}" />
					</td>
				</tr>
				<tr>
					<td width="11%" class="whiteboxwk">
						<span class="mandatory">*</span>
						<s:text	name="asset.reval.acc.code" />
					</td>
					<td width="21%" class="whitebox2wk" colspan="3">
						<s:select headerKey="-1" disabled="%{fDisabled}"
							headerValue="%{getText('list.default.select')}" name="revCode"
							id="revcode" cssClass="selectwk"
							list="dropdownData.revCodeList" listKey="id" 
							listValue='glcode  + " : " + name'	value="%{revCode.id}" />
					</td>
				</tr>
				
				<tr>
					<td width="11%" class="greyboxwk">
						<s:text	name="asset.dep.exp.acc" />
					</td>
					<td width="21%" class="greybox2wk" colspan="3">
						<s:select headerKey="-1" disabled="%{sDisabled}"
							headerValue="%{getText('list.default.select')}" name="depExpCode"
							id="depexpcode" cssClass="selectwk"
							list="dropdownData.depExpCodeList" listKey="id" 
							listValue='glcode  + " : " + name'	value="%{depExpCode.id}" />
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