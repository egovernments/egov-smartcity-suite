<!-- #-------------------------------------------------------------------------------
# eGov suite of products aim to improve the internal efficiency,transparency,
#    accountability and the service delivery of the government  organizations.
# 
#     Copyright (C) <2015>  eGovernments Foundation
# 
#     The updated version of eGov suite of products as by eGovernments Foundation
#     is available at http://www.egovernments.org
# 
#     This program is free software: you can redistribute it and/or modify
#     it under the terms of the GNU General Public License as published by
#     the Free Software Foundation, either version 3 of the License, or
#     any later version.
# 
#     This program is distributed in the hope that it will be useful,
#     but WITHOUT ANY WARRANTY; without even the implied warranty of
#     MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
#     GNU General Public License for more details.
# 
#     You should have received a copy of the GNU General Public License
#     along with this program. If not, see http://www.gnu.org/licenses/ or
#     http://www.gnu.org/licenses/gpl.html .
# 
#     In addition to the terms of the GPL license to be adhered to in using this
#     program, the following additional terms are to be complied with:
# 
# 	1) All versions of this program, verbatim or modified must carry this
# 	   Legal Notice.
# 
# 	2) Any misrepresentation of the origin of the material is prohibited. It
# 	   is required that all modified versions of this material be marked in
# 	   reasonable ways as different from the original version.
# 
# 	3) This license does not grant any rights to any user of the program
# 	   with regards to rights under trademark law for use of the trade names
# 	   or trademarks of eGovernments Foundation.
# 
#   In case of any queries, you can reach eGovernments Foundation at contact@egovernments.org.
#-------------------------------------------------------------------------------  -->


<style type="text/css">
#yui-dt0-bodytable, #yui-dt1-bodytable, #yui-dt2-bodytable {
	Width: 100%;
}
</style>
<script>
function setupAjaxWards(elem){
    zone_id=elem.options[elem.selectedIndex].value;
    populateassetward({zoneId:zone_id});
}
function setupAjaxStreet(elem){
    var ward_id=elem.options[elem.selectedIndex].value;
    populatestreet({wardId:ward_id});
}
function goBack()
{
	window.location="${pageContext.request.contextPath}/assetmaster/asset-edit.action";	
}
function goNewForm()
{
	window.location="${pageContext.request.contextPath}/assetmaster/asset-newform.action";	
}

function trim(str) {
        return str.replace(/^\s+|\s+$/g,"");
}

function validateFormAndSubmit(){
	var code= dom.get("code").value;
	var name= dom.get("name").value;
	var pattern=/[^0-9a-zA-Z-&:/ ]/;
	var namepattern=/[^0-9a-zA-Z-&:/ ]/;
	var lenVal = document.getElementById("length").value;
	var widthVal = document.getElementById("width").value;
	var totalAreaVal = document.getElementById("totalArea").value;
	if(code.match(pattern)){
	   dom.get("asset_error").style.display='';
	   document.getElementById("asset_error").innerHTML='<s:text name='asset.code.alphaNumericwithspecialchar' />'
	    	return false;
	}else if(name.match(namepattern) ){
	   dom.get("asset_error").style.display='';
	   document.getElementById("asset_error").innerHTML='<s:text name='asset.name.alphaNumericwithspecialchar' />'
	    	return false;
	}
	
	if(document.getElementById("status").value!=-1){
	       var status=document.getElementById('status').options[document.getElementById('status').selectedIndex].text;
		   if(status=='Cancelled') {
			var remarks=document.getElementById("remarks").value;
			if(trim(remarks)==""){
				dom.get("asset_error").style.display='';
	    		document.getElementById("asset_error").innerHTML='<s:text name="asset.remarks.null" />';
	    		return false;
			}
		}
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
	 
	if(document.getElementById("catTypeIdDummy").value=='-1'){
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
	if(document.getElementById("modeofacqui").value==''){
	  dom.get("asset_error").style.display='';
	  document.getElementById("asset_error").innerHTML='<s:text name="asset.modeofacqui.null"/>';
	  document.getElementById("modeofacqui").focus();
	  return false;
	}
	if(lenVal!='' && (isNaN(lenVal) || getNumber(lenVal)<0) )
	{
		dom.get("asset_error").style.display='';
		document.getElementById("asset_error").innerHTML='<s:text name="asset.valid.number" /> length' ;
		return false;
	}	
	if(widthVal!='' && (isNaN(widthVal) || getNumber(widthVal)<0) )
	{
		dom.get("asset_error").style.display='';
		document.getElementById("asset_error").innerHTML='<s:text name="asset.valid.number" /> width' ;
		return false;
	}
	if(totalAreaVal!='' && (isNaN(totalAreaVal) || getNumber(totalAreaVal)<0) )
	{
		dom.get("asset_error").style.display='';
		document.getElementById("asset_error").innerHTML='<s:text name="asset.valid.number" /> area' ;
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
	    	document.assetForm.action='${pageContext.request.contextPath}/assetmaster/asset-save.action';
	    	document.assetForm.submit();
	    }
	    
	}
}

function validateLocationDetails(){
    if($('catTypeIdDummy').options[$('catTypeIdDummy').selectedIndex].text == '<s:property value="@org.egov.asset.model.AssetType@IMMOVABLEASSET" />' || 
    		$('catTypeIdDummy').options[$('catTypeIdDummy').selectedIndex].text == '<s:property value="@org.egov.asset.model.AssetType@LAND" />'){
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
		  return false;
		}
		if(document.getElementById("assetarea").value=='-1'){
		  dom.get("asset_error").style.display='';
		  document.getElementById("asset_error").innerHTML='Please select Area'
		  document.getElementById("assetarea").focus();
		  return false;
		}
		if(document.getElementById("location").value=='-1'){
		  dom.get("asset_error").style.display='';
		  document.getElementById("asset_error").innerHTML='Please select Location'
		  document.getElementById("location").focus();
		  return false;
		}		
	}
	return true;
}

function setupAjaxArea(elem){
   var area_id=elem.options[elem.selectedIndex].value;
    populateassetarea({wardId:area_id});
}
function resetDropDowns() {
dom.get("assetarea").value=-1;
dom.get("location").value=-1;
	
}
function setupAjaxLocation(elem){
    var street_id=elem.options[elem.selectedIndex].value;
    populatelocation({areaId:street_id});
}

function showValueSummary()
{	
	if($('status').options[$('status').selectedIndex].text == 'Capitalized'){
		$('grossvalue').value='0';
		//$('accdepreciation').value='0';
		//$('writtendownvalue').value='0';
		$('grossval').show();
		//$('accdep').show();
		//$('wdv').hide();
	}else{
		$('grossvalue').value='0';
		//$('accdepreciation').value='0';
		//$('writtendownvalue').value='0';
		$('grossval').hide();
		//$('accdep').hide();
		//$('wdv').hide();
	}
}

function setupAjaxAssetcat(elem){
    category_id=elem.options[elem.selectedIndex].value;
    makeJSONCall(["xCatAttrTemplate"],
    	'${pageContext.request.contextPath}/assetmaster/ajaxAsset-populateCategoryDetails.action',
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
    populateassetcat({assetType:elem.value});
}
function validateNumbers(elem){
    if(elem.value!='' && (isNaN(elem.value) || getNumber(elem.value)<0)){
    	dom.get("asset_error").style.display='';
    	document.getElementById("asset_error").innerHTML='<s:text name="asset.valid.number" /> '+elem.name ;
    	return false;
    }
    return true;
}
function calculateArea()
{
	var lenVal = document.getElementById("length").value;
	var widthVal = document.getElementById("width").value;
	if((lenVal!='' && !(isNaN(lenVal) || getNumber(lenVal)<0))
		&& (widthVal!='' && !(isNaN(widthVal) || getNumber(widthVal)<0)))	
	{
		document.getElementById("totalArea").value=	getNumber(lenVal)*getNumber(widthVal);
	}	
}

function openVehicleMaster() {
	var url = '<s:property value="%{sourcePath}" />' + '&mode=view' ;
	window.open(url,'','resizable=yes,scrollbars=yes,left=300,top=40, width=900, height=700');
	
}

</script>
<table id="formTable" width="100%" border="0" cellspacing="0"
	cellpadding="0">
	<tr>
		<td>&nbsp;</td>
	</tr>
	<tr>
		<td>
			<table id="asDetailsTable" width="100%" border="0" cellspacing="0"
				cellpadding="0">
				<tr>
					<td colspan="4" class="headingwk">
						<div class="arrowiconwk">
							<img
								src="/egassets/resources/erp2/images/arrow.gif" />
						</div>
						<div class="headplacer">
							<s:text name='title.asset.details' />
						</div>
					</td>
				</tr>
				<tr>
					<td width="11%" class="whiteboxwk"><span class="mandatory">*</span>
						<s:text name="asset.code" />:</td>
					<td width="21%" class="whitebox2wk" colspan="3">
						<div id="codeLabel" class="estimateno" style="display: none;">
							<s:if test="%{not code}">&lt; Not Assigned &gt;</s:if>
							<s:property value="code" />
						</div> <s:textfield name="code" value="%{code}" id="code"
							disabled="%{fDisabled}" cssClass="selectwk" maxlength="30" />
					</td>
				</tr>
				<tr>
					<td width="11%" class="greyboxwk"><span class="mandatory">*</span>
						<s:text name="asset.name" /></td>
					<td width="21%" class="greybox2wk" colspan="3"><s:textfield
							name="name" value="%{name}" id="name" disabled="%{fDisabled}"
							cssClass="selectwk" maxlength="255" /></td>
				</tr>
				<tr>
					<td width="11%" class="whiteboxwk"><span class="mandatory">*</span>
						<s:text name="asset.dept" /></td>
					<td width="21%" class="whitebox2wk" colspan="3"><s:select
							headerKey="-1" disabled="%{sDisabled}"
							headerValue="%{getText('list.default.select')}" name="department"
							id="department" cssClass="selectwk"
							list="dropdownData.departmentList" listKey="id" listValue='name'
							value="%{department.id}" /></td>
				</tr>
				<tr>
					<td width="11%" class="greyboxwk"><span class="mandatory">*</span>
						<s:text name="asset.cat.type" />:</td>
					<td width="21%" class="greybox2wk" colspan="3"><s:select
							headerKey="-1" disabled="%{sDisabled}"
							headerValue="%{getText('list.default.select')}"
							id="catTypeIdDummy" name="assetType" cssClass="selectwk"
							list="dropdownData.assetTypeList"
							value="%{assetCategory.assetType}"
							onChange="setupAjaxAssettype(this);" /> <egov:ajaxdropdown
							id="populateAssetcat" fields="['Text','Value']"
							dropdownId='assetcat'
							url='assetmaster/ajaxAsset-populateParentCategories.action'
							selectedValue="%{id}" /></td>
				</tr>
				<tr>
					<td width="11%" class="whiteboxwk"><span class="mandatory">*</span>
						<s:text name="asset.category" /></td>
					<td width="21%" class="whitebox2wk" colspan="3"><s:select
							headerKey="-1" disabled="%{sDisabled}"
							headerValue="%{getText('list.default.select')}"
							name="assetCategory" id="assetcat" cssClass="selectwk"
							list="dropdownData.assetCategoryList" listKey="id"
							listValue='name' value="%{assetCategory.id}"
							onChange="setupAjaxAssetcat(this);" /></td>
				</tr>
				<tr>
					<td width="11%" class="greyboxwk"><s:text name="asset.desc" />
					</td>
					<td width="21%" class="greybox2wk" colspan="3"><s:textarea
							name="description" value="%{description}" rows="3" cols="20"
							id="desc" disabled="%{fDisabled}" cssClass="selectwk"
							maxlength="200" /></td>
				</tr>
				<tr>
					<td colspan="4" class="headingwk">
						<div class="arrowiconwk">
							<img
								src="/egassets/resources/erp2/images/arrow.gif" />
						</div>
						<div class="headplacer">
							<s:text name='title.location.details' />
						</div>
					</td>
				</tr>
				<tr>
					<td width="11%" class="whiteboxwk"><s:text
							name="asset.location.zone" />:</td>
					<td width="21%" class="whitebox2wk"><s:select id="zoneId"
							name="zoneId" cssClass="selectwk" disabled="%{fDisabled}"
							list="dropdownData.zoneList" listKey="id" listValue="name"
							headerKey="-1" headerValue="%{getText('list.default.select')}"
							value="%{zoneId}" onChange="setupAjaxWards(this);" /> <egov:ajaxdropdown
							id="populateWard" fields="['Text','Value']"
							dropdownId='assetward'
							url='assetmaster/ajaxAsset-populateWard.action' /></td>
				</tr>
				<tr>
					<td width="11%" class="greyboxwk"><s:text
							name="asset.location.ward" /></td>
					<td width="21%" class="greybox2wk"><s:select id="assetward"
							name="ward" cssClass="selectwk" disabled="%{fDisabled}"
							list="dropdownData.wardList" listKey="id" listValue="name"
							headerKey="-1" headerValue="%{getText('list.default.select')}"
							onChange="setupAjaxStreet(this);" value="%{ward.id}" /> <egov:ajaxdropdown
							id="populateStreet" fields="['Text','Value']" dropdownId='street'
							url='assetmaster/ajaxAsset-populateStreets.action'
							selectedValue="%{ward.id}" /></td>
				</tr>
				<tr>
					<td width="11%" class="whiteboxwk"><s:text
							name="asset.location.street" /></td>
					<td width="21%" class="whitebox2wk" colspan="3"><s:select
							headerKey="-1" disabled="%{fDisabled}"
							headerValue="%{getText('list.default.select')}" name="street"
							id="street" cssClass="selectwk" list="dropdownData.streetList"
							listKey="id" listValue='name'
							onChange="setupAjaxLocation(this);resetDropDowns();"
							value="%{street.id}" /> <egov:ajaxdropdown id="populateLocation"
							fields="['Text','Value']" dropdownId='location'
							url='assetmaster/ajaxAsset-populateLocations.action'
							selectedValue="%{street.id}" /></td>
				</tr>
				<tr>
					<td width="11%" class="greyboxwk"><s:text
							name="asset.location" /></td>
					<td width="21%" class="greybox2wk" colspan="3"><s:select
							headerKey="-1" disabled="%{fDisabled}"
							headerValue="%{getText('list.default.select')}" name="location"
							id="location" cssClass="selectwk"
							list="dropdownData.locationList" listKey="id" listValue='name'
							onChange="setupAjaxArea(this);" value="%{location.id}" /> <egov:ajaxdropdown
							id="populateArea" fields="['Text','Value']"
							dropdownId='assetarea'
							url='assetmaster/ajaxAsset-populateArea.action'
							selectedValue="%{area.id}" /></td>
				</tr>
				<tr>
					<td width="11%" class="whiteboxwk"><s:text
							name="asset.location.area" /></td>
					<td width="21%" class="whitebox2wk" colspan="3"><s:select
							headerKey="-1" disabled="%{fDisabled}"
							headerValue="%{getText('list.default.select')}" name="area"
							id="assetarea" cssClass="selectwk" list="dropdownData.areaList"
							listKey="id" listValue='name' value="%{area.id}" /></td>
				</tr>
				<tr>
					<td colspan="4" class="headingwk">
						<div class="arrowiconwk">
							<img
								src="/egassets/resources/erp2/images/arrow.gif" />
						</div>
						<div class="headplacer">
							<s:text name='title.asset.details' />
						</div>
					</td>
				</tr>
				<tr>
					<td width="11%" class="greyboxwk"><s:text name="asset.details" />
					</td>
					<td width="21%" class="greybox2wk" colspan="3"><s:textarea
							name="assetDetails" value="%{assetDetails}" rows="5" cols="40"
							id="assetdetails" disabled="%{fDisabled}" cssClass="selectwk" />
					</td>
				</tr>
				<tr>
					<td width="11%" class="whiteboxwk"><span class="mandatory">*</span> 
					<s:text	name="asset.acquisition.mode" /></td>
					<td width="21%" class="whitebox2wk" colspan="3"><s:select
							headerKey="" disabled="%{fDisabled}"
							headerValue="%{getText('list.default.select')}"
							name="modeOfAcquisition" id="modeofacqui" cssClass="selectwk"
							list="dropdownData.acquisitionModeList" value="%{modeOfAcquisition}" /></td>
				</tr>
				<tr>
					<td width="11%" class="whiteboxwk"><span class="mandatory">*</span>
						<s:text name="asset.date.of.creation" /></td>
					<td width="21%" class="whitebox2wk" colspan="3"><s:date
							name="dateOfCreation" id="dateOfCreationId" format="dd/MM/yyyy" />
						<s:textfield name="dateOfCreation" value="%{dateOfCreationId}"
							id="dateOfCreation" cssClass="selectboldwk"
							onfocus="javascript:vDateType='3';" maxlength="10"
							disabled="%{fDisabled}"
							onkeyup="DateFormat(this,this.value,event,false,'3')" /> <a
						href="javascript:show_calendar('forms[0].dateOfCreation',null,null,'DD/MM/YYYY');"
						onmouseover="window.status='Date Picker';return true;"
						onmouseout="window.status='';return true;"> <img
							src="/egassets/resources/erp2/images/calendar.png"
							alt="Calendar" width="16" height="16" border="0"
							align="absmiddle" />
					</a></td>
				</tr>
				<tr>
					<td width="11%" class="whiteboxwk"><s:text name="asset.length" />
					</td>
					<td width="21%" class="whitebox2wk" colspan="3"><s:textfield
							name="lengthValue" value="%{length}" id="length"
							onblur="validateNumbers(this);calculateArea();"
							cssClass="selectwk" /> <span style="color: red">&nbsp;In
							Meters</span></td>
				</tr>
				<tr>
					<td width="11%" class="whiteboxwk"><s:text name="asset.width" />
					</td>
					<td width="21%" class="whitebox2wk" colspan="3"><s:textfield
							name="widthValue" value="%{width}" id="width"
							onblur="validateNumbers(this);calculateArea();"
							cssClass="selectwk" /> <span style="color: red">&nbsp;In
							Meters</span></td>
				</tr>
				<tr>
					<td width="11%" class="whiteboxwk"><s:text name="asset.area" />
					</td>
					<td width="21%" class="whitebox2wk" colspan="3"><s:textfield
							name="areaValue" value="%{totalArea}" id="totalArea"
							onblur="validateNumbers(this)" cssClass="selectwk" /> <span
						style="color: red">&nbsp;In Square Meters</span> <s:if
							test="(sourcePath!=null && (userMode=='view' || userMode=='edit'))">
							<input class="buttonfinal" type="button" id="sourcePath"
								value='<s:text name="asset.vehicle.master.view"/>'
								onclick="return openVehicleMaster();" />
						</s:if></td>

				</tr>
				<tr>
					<td colspan="4" class="headingwk">
						<div class="arrowiconwk">
							<img
								src="/egassets/resources/erp2/images/arrow.gif" />
						</div>
						<div class="headplacer">
							<s:text name='title.value.summary' />
						</div>
					</td>
				</tr>
				<tr>
					<td width="11%" class="whiteboxwk"><span class="mandatory">*</span>
						<s:text name="asset.status" /></td>
					<td width="21%" class="whitebox2wk" colspan="3" id="tdstatmain">
						<s:select headerKey="-1" disabled="%{fDisabled}"
							headerValue="%{getText('list.default.select')}" name="status"
							id="status" cssClass="selectwk" list="dropdownData.statusList"
							listKey="id" listValue='description' value="%{status.id}"
							onchange="showValueSummary();" />
					</td>
					<td width="21%" class="whitebox2wk" colspan="3" id="tdstatalt"
						style="display: none;"><s:text name="%{status.description}" />
					</td>
				</tr>
				<tr id="grossval">
					<td width="11%" class="greyboxwk"><s:text
							name="asset.gross.value" /></td>
					<td width="21%" class="greybox2wk" colspan="3"><s:textfield
							name="grossValue" value="%{grossValue}" id="grossvalue"
							disabled="%{fDisabled}" cssClass="selectamountwk" maxlength="30" />
					</td>
				</tr>
				<!-- <tr id="accdep">
					<td width="11%" class="whiteboxwk">
						<s:text	name="asset.accumulative.dep" />
					</td>
					<td width="21%" class="whitebox2wk" colspan="3">
						<s:textfield name="accDepreciation" value="%{accDepreciation}" id="accdepreciation" 
						disabled="%{fDisabled}"	cssClass="selectamountwk" maxlength="30" />
					</td>
				</tr> -->
				<tr>
					<td width="11%" class="whiteboxwk"><s:text
							name="asset.remarks" /></td>
					<td width="21%" class="whitebox2wk" colspan="3"><s:textarea
							name="remarks" value="%{remarks}" rows="5" cols="40" id="remarks"
							disabled="%{fDisabled}" cssClass="selectwk" /></td>
				</tr>
				<tr>
					<td colspan="4" class="shadowwk"></td>
				</tr>
				<s:if test="%{userMode=='edit' && sourcePath!=null}">
					<tr>
						<td colspan="4"><div align="right" class="mandatory"
								style="font-size: 11px; padding-right: 20px;">
								*
								<s:text name="asset.vehicle.master.footnote" />
							</div></td>
					</tr>
				</s:if>
				<tr>
					<td colspan="4"><div align="right" class="mandatory"
							style="font-size: 11px; padding-right: 20px;">
							*
							<s:text name="default.message.mandatory" />
						</div></td>
				</tr>
			</table>
		</td>
	</tr>
	<tr>
		<td>&nbsp;</td>
	</tr>
</table>
<script>
	if($('status').options[$('status').selectedIndex].text != 'Capitalized'){
		$('grossval').hide();
		//$('accdep').hide();
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
