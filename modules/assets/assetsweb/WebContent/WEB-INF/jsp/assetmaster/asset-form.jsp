<%@ taglib prefix="s" uri="/struts-tags"%>

<style type="text/css">
#yui-dt0-bodytable,#yui-dt1-bodytable,#yui-dt2-bodytable {
	Width: 100%;
}
</style>

<script>
function setupAjaxWards(elem){
    zone_id=elem.options[elem.selectedIndex].value;
    populateassetward({zoneId:zone_id});
}
function setupAjaxAreas(elem){
    ward_id=elem.options[elem.selectedIndex].value;
    populateassetarea({wardId:ward_id});
    populatestreet({wardId:ward_id});
}
function goBack()
{
	window.location="${pageContext.request.contextPath}/assetmaster/asset!edit.action";	
}
function goNewForm()
{
	window.location="${pageContext.request.contextPath}/assetmaster/asset!newform.action";	
}

function validateFormAndSubmit(){
var code= dom.get("code").value;
var name= dom.get("name").value;
var pattern=/[^0-9a-zA-Z-&:/ ]/;
var namepattern=/[^0-9a-zA-Z-&:/ ]/;
if(code.match(pattern)){
   dom.get("asset_error").style.display='';
   document.getElementById("asset_error").innerHTML='<s:text name='asset.code.alphaNumericwithspecialchar' />'
    	return false;
}else if(name.match(namepattern) ){
   dom.get("asset_error").style.display='';
   document.getElementById("asset_error").innerHTML='<s:text name='asset.name.alphaNumericwithspecialchar' />'
    	return false;
}
if(document.getElementById("name").value==''){
  dom.get("asset_error").style.display='';
  document.getElementById("asset_error").innerHTML='Please enter asset name'
  document.getElementById("name").focus();
  return false;
}
if(document.getElementById("department").value=='-1'){
  dom.get("asset_error").style.display='';
  document.getElementById("asset_error").innerHTML='Please select department'
  document.getElementById("department").focus();
  return false;
}
 
if(document.getElementById("catTypeIdDummy").value==''){
  dom.get("asset_error").style.display='';
  document.getElementById("asset_error").innerHTML='Please select asset category type'
  document.getElementById("catTypeIdDummy").focus();
  return false;
}
if(document.getElementById("assetcat").value=='-1'){
  dom.get("asset_error").style.display='';
  document.getElementById("asset_error").innerHTML='Please select asset category'
  document.getElementById("assetcat").focus();
  return false;
}
if(document.getElementById("dateOfCreation").value==''){
  dom.get("asset_error").style.display='';
  document.getElementById("asset_error").innerHTML='Please select asset date of creation'
  document.getElementById("dateOfCreation").focus();
  return false;
}

if(!validateLocationDetails()){
	return false;
}

if(document.getElementById("status").value=='-1'){
  dom.get("asset_error").style.display='';
  document.getElementById("asset_error").innerHTML='Please select asset status'
  document.getElementById("status").focus();
  return false;
}
else{
    clearMessage('asset_error')
	links=document.assetForm.getElementsByTagName("span");
	errors=false;
	for(i=0;i<links.length;i++)
    {
        if(links[i].innerHTML=='&nbsp;x' && links[i].style.display!='none'){
            errors=true;
            break;
        }
    }
    if(errors){
        dom.get("asset_error").style.display='';
    	document.getElementById("asset_error").innerHTML='Please enter valid values where indicated';
    	return false;
    }else{
    	enableFields();
    	return true;
    }
    
}
}

function validateLocationDetails(){
    /*if($('catTypeIdDummy').options[$('catTypeIdDummy').selectedIndex].text == 'ImmovableAsset' || 
    		$('catTypeIdDummy').options[$('catTypeIdDummy').selectedIndex].text == 'Land'){
		if(document.getElementById("zoneId").value=='-1'){
		  dom.get("asset_error").style.display='';
		  document.getElementById("asset_error").innerHTML='Please select Zone'
		  document.getElementById("zoneId").focus();
		  return false;
		}
		if(document.getElementById("assetward").value=='-1'){
		  dom.get("asset_error").style.display='';
		  document.getElementById("asset_error").innerHTML='Please select Ward'
		  document.getElementById("assetward").focus();
		  return false;
		}
		if(document.getElementById("street").value=='-1'){
		  dom.get("asset_error").style.display='';
		  document.getElementById("asset_error").innerHTML='Please select Street'
		  document.getElementById("street").focus();
		  //return false;
		} 
		if(document.getElementById("assetarea").value=='-1'){
		  dom.get("asset_error").style.display='';
		  document.getElementById("asset_error").innerHTML='Please select Area'
		  document.getElementById("assetarea").focus();
		  //return false;
		}
		if(document.getElementById("location").value=='-1'){
		  dom.get("asset_error").style.display='';
		  document.getElementById("asset_error").innerHTML='Please select Location'
		  document.getElementById("location").focus();
		  //return false;
		}		
	} */
	return true;
}

function setupAjaxArea(elem){
    area_id=elem.options[elem.selectedIndex].value;
    populatelocation({areaId:area_id});
}

function setupAjaxLocation(elem){
    location_id=elem.options[elem.selectedIndex].value;
    populatestreet({locationId:location_id});
}

function showValueSummary()
{	
	if($('status').options[$('status').selectedIndex].text == 'Capitalized'){
		$('grossvalue').value='0';
		$('accdepreciation').value='0';
		//$('writtendownvalue').value='0';
		$('grossval').show();
		$('accdep').show();
		//$('wdv').hide();
	}else{
		$('grossvalue').value='0';
		$('accdepreciation').value='0';
		//$('writtendownvalue').value='0';
		$('grossval').hide();
		$('accdep').hide();
		//$('wdv').hide();
	}
}

function setupAjaxAssetcat(elem){
    category_id=elem.options[elem.selectedIndex].value;
    makeJSONCall(["xCatAttrTemplate"],
    	'${pageContext.request.contextPath}/assetmaster/ajaxAsset!populateCategoryDetails.action',
    	{categoryId:category_id},mySuccessHandler,myFailureHandler) ;
}

mySuccessHandler = function(req,res){
  results=res.results;
  //document.getElementById('assetdetails').value=results[0].xCatAttrTemplate;
  $('assetdetails').value = results[0].xCatAttrTemplate.gsub('&quot;','')
}

myFailureHandler= function(){
    dom.get("asset_error").style.display='';
	document.getElementById("asset_error").innerHTML='Unable to load Category Details';
}

function setupAjaxAssettype(elem){
    $('assetdetails').value='';
    asset_type_id=elem.options[elem.selectedIndex].value;
    populateassetcat({assetTypeId:asset_type_id});
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
			<table id="asDetailsTable" width="100%" border="0" cellspacing="0"
				cellpadding="0">
				<tr>
					<td colspan="6" class="headingwk">
						<div class="arrowiconwk">
							<img src="${pageContext.request.contextPath}/image/arrow.gif" />
						</div>
						<div class="headplacer">
							<s:text name='title.asset.details' />
						</div>
					</td>
				</tr>
				<tr>
					<td width="11%" class="whiteboxwk">
						<span class="mandatory">*</span> 
						<s:text	name="asset.code" />:
					</td>
					<td width="21%" class="whitebox2wk" colspan="3">
						<div id="codeLabel" class="estimateno" style="display:none;"><s:if test="%{not code}">&lt; Not Assigned &gt;</s:if><s:property value="code" /></div>
						<s:textfield name="code" value="%{code}" id="code" 
						disabled="%{fDisabled}"	cssClass="selectwk" maxlength="30" />
					</td>
				</tr>
				<tr>
					<td width="11%" class="greyboxwk">
						<span class="mandatory">*</span> 
						<s:text	name="asset.name" />
					</td>
					<td width="21%" class="greybox2wk" colspan="5">
						<s:textfield name="name" value="%{name}" id="name" 
						disabled="%{fDisabled}"	cssClass="selectwk" maxlength="255" />
					</td>
				</tr>
				<tr>
					<td width="11%" class="whiteboxwk">
						<span class="mandatory">*</span> 
						<s:text	name="asset.dept" />
					</td>
					<td width="21%" class="whitebox2wk" colspan="3">
						<s:select headerKey="-1" disabled="%{sDisabled}"
							headerValue="%{getText('list.default.select')}" name="department"
							id="department" cssClass="selectwk"
							list="dropdownData.departmentList" listKey="id" listValue='deptName'
							value="%{department.id}" />
					</td>
				</tr>
				<tr>
					<td width="11%" class="greyboxwk">
						<span class="mandatory">*</span> 
						<s:text name="asset.cat.type" />:
					</td>
					<td width="21%" class="greybox2wk" colspan="5">
						<s:select headerKey="" disabled="%{sDisabled}"
							headerValue="%{getText('list.default.select')}"
							id="catTypeIdDummy"
							cssClass="selectwk" list="dropdownData.assetTypeList"
							listKey="id" listValue='name'
							value="%{assetCategory.assetType.id}"
							onChange="setupAjaxAssettype(this);" />
						<egov:ajaxdropdown id="populateAssetcat"
							fields="['Text','Value']" dropdownId='assetcat'
							url='assetmaster/ajaxAsset!populateParentCategories.action'
							selectedValue="%{id}" />
					</td>
				</tr>
				<tr>
					<td width="11%" class="whiteboxwk">
						<span class="mandatory">*</span> 
						<s:text	name="asset.category" />
					</td>
					<td width="21%" class="whitebox2wk" colspan="3">
						<s:select headerKey="-1" disabled="%{sDisabled}"
							headerValue="%{getText('list.default.select')}" name="assetCategory"
							id="assetcat" cssClass="selectwk"
							list="dropdownData.assetCategoryList" listKey="id" listValue='name'
							value="%{assetCategory.id}" onChange="setupAjaxAssetcat(this);"/>
					</td>
				</tr>
				<tr>
					<td width="11%" class="greyboxwk">
						<s:text	name="asset.desc" />
					</td>
					<td width="21%" class="greybox2wk" colspan="5">
						<s:textarea  name="description" value="%{description}" rows="3" cols="20"
							id="desc" disabled="%{fDisabled}" cssClass="selectwk" maxlength="200" />
					</td>
				</tr>
				<tr>
					<td colspan="6" class="headingwk">
						<div class="arrowiconwk">
							<img src="${pageContext.request.contextPath}/image/arrow.gif" />
						</div>
						<div class="headplacer">
							<s:text name='title.location.details' />
						</div>
					</td>
				</tr>
				<tr>
					<td width="11%" class="whiteboxwk">
					        
							<s:text	name="asset.location.zone" />:
								</td>
								<td width="21%" class="whitebox2wk">
									<s:select id="zoneId" name="zoneId" cssClass="selectwk" disabled="%{fDisabled}"
										list="dropdownData.zoneList" listKey="id" listValue="name" 
										headerKey="-1" headerValue="%{getText('list.default.select')}"
										value="%{zoneId}" onChange="setupAjaxWards(this);" />	
										<egov:ajaxdropdown id="populateWard"
							fields="['Text','Value']" dropdownId='assetward'
							url='assetmaster/ajaxAsset!populateWard.action'
							 />
				           </td>
				</tr>
				<tr>
					<td width="11%" class="greyboxwk" >
					    
						<s:text	name="asset.location.ward" />
					</td>
					<td width="21%" class="greybox2wk" colspan="5" >
									<s:select id="assetward" name="ward" cssClass="selectwk"  disabled="%{fDisabled}"
										list="dropdownData.wardList" listKey="id" listValue="name" 
										headerKey="-1" headerValue="%{getText('list.default.select')}" onChange="setupAjaxAreas(this);" value="%{ward.id}"/>
						<egov:ajaxdropdown id="populateArea"
							fields="['Text','Value']" dropdownId='assetarea'
							url='assetmaster/ajaxAsset!populateArea.action'
							 />	
							 <egov:ajaxdropdown id="populateStreet" 
							 fields="['Text','Value']" dropdownId='street'
							  url='assetmaster/ajaxAsset!populateStreets.action' />	
					</td>
				</td>
				</tr>
				<tr>
					<td width="11%" class="whiteboxwk">
					    
						<s:text	name="asset.location.street" />
					</td>
					<td width="21%" class="whitebox2wk" colspan="3">
						<s:select headerKey="-1" disabled="%{fDisabled}"
							headerValue="%{getText('list.default.select')}" name="street"
							id="street" cssClass="selectwk"
							list="dropdownData.streetList" listKey="id" listValue='name'
							value="%{street.id}" />
					</td>
				</tr>
				<tr>
					<td width="11%" class="greyboxwk">
					    
						<s:text	name="asset.location.area" />
					</td>
					<td width="21%" class="greybox2wk" colspan="5">
						<s:select headerKey="-1" disabled="%{fDisabled}"
							headerValue="%{getText('list.default.select')}" name="area"
							id="assetarea" cssClass="selectwk"
							list="dropdownData.areaList" listKey="id" listValue='name'
							onChange="setupAjaxArea(this);"  value="%{area.id}"/>
						<egov:ajaxdropdown id="populateLocation"
							fields="['Text','Value']" dropdownId='location'
							url='assetmaster/ajaxAsset!populateLocations.action'
							selectedValue="%{area.id}" />
					</td>
				</tr>
				<tr>
					<td width="11%" class="whiteboxwk">
					    
						<s:text	name="asset.location" />
					</td>
					<td width="21%" class="whitebox2wk" colspan="3">
						<s:select headerKey="-1" disabled="%{fDisabled}"
							headerValue="%{getText('list.default.select')}" name="location"
							id="location" cssClass="selectwk"
							list="dropdownData.locationList" listKey="id" listValue='name'
							value="%{location.id}"/>
						
					</td>
				</tr>
								
				<tr>
					<td colspan="6" class="headingwk">
						<div class="arrowiconwk">
							<img src="${pageContext.request.contextPath}/image/arrow.gif" />
						</div>
						<div class="headplacer">
							<s:text name='title.asset.details' />
						</div>
					</td>
				</tr>
				<tr>
					<td class="greyboxwk" rowspan="2">
						<s:text	name="asset.details" />
					</td>
					<td class="greybox2wk" rowspan="2">
						<s:textarea  name="assetDetails" value="%{assetDetails}" rows="5" cols="40"
							id="assetdetails" disabled="%{fDisabled}" cssClass="selectwk" maxlength="1024"/>
					</td>
					
					
					
					<td class="greyboxwk" rowspan="2">
					<s:text name="asset.address" /></td>
                <td class="greybox2wk" rowspan="2">
                <s:textarea name="address" value="%{address}" rows="5" cols="40" id="address" 
                 disabled="%{fDisabled}" cssClass="selectwk" maxlength="200"/></td>
                
				</tr>

			<tr><td>&nbsp</td></tr>
				<tr>
					<td class="whiteboxwk">
                <s:text name="asset.landmark" /></td>
                <td class="whitebox2wk"><s:textarea name="landmark" value="%{landmark}" rows="2" cols="30" id="landmark" cssClass="selectwk" disabled="%{fDisabled}" maxlength="256"/>
                </td>
					<td width="11%" class="whiteboxwk">
						<s:text	name="asset.acquisition.mode" />
					</td>
					<td width="11%" class="whitebox2wk" colspan="2">
						<s:select headerKey="-1" disabled="%{fDisabled}"
							headerValue="%{getText('list.default.select')}" name="modeOfAcquisition"
							id="modeofacqui" cssClass="selectwk"
							list="dropdownData.acquisitionModeList" listKey="value" listValue='value'
							value="%{modeOfAcquisition}" />
					</td>
				</tr>
					
				<tr>	<td width="11%" class="greyboxwk">
						<span class="mandatory">*</span>
						<s:text	name="asset.date.of.creation" />
					</td>
					<td width="11%" class="greybox2wk" colspan="5">
						<s:date name="dateOfCreation" id="dateOfCreationId" format="dd/MM/yyyy"/>
						<s:textfield name="dateOfCreation" value="%{dateOfCreationId}" id="dateOfCreation" cssClass="selectboldwk" 
							onfocus="javascript:vDateType='3';" maxlength="10" disabled="%{fDisabled}"
							onkeyup="DateFormat(this,this.value,event,false,'3')"/>
                    	<a href="javascript:show_calendar('forms[0].dateOfCreation',null,null,'DD/MM/YYYY');" 
                    		onmouseover="window.status='Date Picker';return true;"  onmouseout="window.status='';return true;">
                    		<img src="${pageContext.request.contextPath}/image/calendar.png" alt="Calendar" width="16" 
                    		height="16" border="0" align="absmiddle" />
                    	</a>
					</td>
				</tr>
				<tr>
					<td colspan="6" class="headingwk">
						<div class="arrowiconwk">
							<img src="${pageContext.request.contextPath}/image/arrow.gif" />
						</div>
						<div class="headplacer">
							<s:text name='title.value.summary' />
						</div>
					</td>
				</tr>
				<tr>
					<td width="11%" class="whiteboxwk">
						<span class="mandatory">*</span> 
						<s:text	name="asset.status" />
					</td>
					<td width="21%" class="whitebox2wk" colspan="3" id="tdstatmain">
						<s:select headerKey="-1" disabled="%{fDisabled}"
							headerValue="%{getText('list.default.select')}" name="status"
							id="status" cssClass="selectwk"
							list="dropdownData.statusList" listKey="id" listValue='description'
							value="%{status.id}" onchange="showValueSummary();"/>
					</td>
					<td width="21%" class="whitebox2wk" colspan="3" id="tdstatalt" style="display:none;">
						<s:text name="%{status.description}"/>
					</td>
				</tr>
				<tr id="grossval"> 
					<td width="11%" class="greyboxwk">
						<s:text	name="asset.gross.value" />
					</td>
					<td width="21%" class="greybox2wk" colspan="5">
						<s:textfield name="grossValue" value="%{grossValue}" id="grossvalue" 
						disabled="%{fDisabled}"	cssClass="selectamountwk" maxlength="30" />
					</td>
				</tr>
				<tr id="accdep">
					<td width="11%" class="whiteboxwk">
						<s:text	name="asset.accumulative.dep" />
					</td>
					<td width="21%" class="whitebox2wk" colspan="3">
						<s:textfield name="accDepreciation" value="%{accDepreciation}" id="accdepreciation" 
						disabled="%{fDisabled}"	cssClass="selectamountwk" maxlength="30" />
					</td>
				</tr>
				<tr>
					<td colspan="6" class="shadowwk"></td>
				</tr>
				<tr>
            		<td colspan="5"><div align="right" class="mandatory" 
            			style="font-size:11px;padding-right:20px;">* <s:text name="default.message.mandatory" />
            		</div></td>
          		</tr>
			</table>
		</td>
	</tr>
	<tr>
		<td>
			&nbsp;
		</td>
	</tr>
</table>
<script>
	if($('status').options[$('status').selectedIndex].text != 'Capitalized'){
		$('grossval').hide();
		$('accdep').hide();
		//$('wdv').hide();
	}
	<s:if test="%{isAutoGeneratedCode=='yes' || isAutoGeneratedCode==null}">
	   document.getElementById('code').readonly=true;	
	   document.getElementById('code').disabled=true;
	   dom.get("codeLabel").style.display='';
	   $('code').hide();
	</s:if>
	
	<s:if test="%{id==null}">
		var cdate=document.getElementById('dateOfCreation').value;
		if(cdate=='') {
			document.getElementById('dateOfCreation').value='<%=new java.text.SimpleDateFormat("dd/MM/yyyy").format(new java.util.Date())%>';
		}else{	
			document.getElementById('dateOfCreation').value=cdate;
		}
	</s:if>
</script>

