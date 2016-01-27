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
<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<script>
jQuery(document).ready(function(){
	 jQuery('#subCategory').change(function() {
		console.log("came jursidiction"+jQuery('#subCategory').val());
		getUom();
	});
	 <s:if test="%{hasErrors() || mode=='view' || mode=='edit'}">
	 if(jQuery('#subCategory').val()!='-1'){
		 getUom();
	 }
	</s:if>  
});

function getUom(){
	jQuery.ajax({
		url: "../domain/commonTradeLicenseAjax-ajaxLoadUomName.action", 
		type: "GET",
		data: {
			subCategoryId : jQuery('#subCategory').val(),
			feeTypeId :  jQuery('#feeTypeId').val()
		},
		cache: false,
		dataType: "json",
		success: function (response) {
			jQuery('#uom').val(response.uom);
		}, 
		error: function (response) {
			console.log("failed");
			jQuery('#uom').val('');
			bootbox.alert("No UOM mapped for SubCategory")
		}
	});
}
</script>
<div class="panel-heading custom_form_panel_heading">
    <div class="panel-title"><s:text name='license.details.lbl' /></div>
</div>
<div class="form-group">
    <label class="col-sm-3 control-label text-right"><s:text name='license.nameOfEst.lbl' /></label>
    <div class="col-sm-3 add-margin">
         	<s:textfield name="nameOfEstablishment" cssClass="form-control patternvalidation"  data-pattern="alphabetwithspace" id="nameOfEstablishment" value="%{nameOfEstablishment}" maxlength="32" onBlur="checkLength(this,32)"/>
    </div>
  
    <label class="col-sm-2 control-label text-right"><s:text name='license.tradeType.lbl' /><span class="mandatory"></span></label>
    <div class="col-sm-3 add-margin">
        <s:select name="buildingType" id="buildingType" list="dropdownData.tradeTypeList"
	listKey="id" listValue="name" headerKey="-1" headerValue="%{getText('default.select')}" value="%{buildingType.id}" class="form-control"   />
    </div>
</div>
<div class="form-group">
    <label class="col-sm-3 control-label text-right"><s:text name='license.category.lbl' /><span class="mandatory"></span></label>
    <div class="col-sm-3 add-margin">
        <s:select name="category" id="category" list="dropdownData.categoryList"
	listKey="id" listValue="name" headerKey="-1" headerValue="%{getText('default.select')}" value="%{category.id}" class="form-control" onChange="setupAjaxSubCategory(this);" />
	<egov:ajaxdropdown id="populateSubCategory" fields="['Text','Value']" dropdownId='subCategory' url='domain/commonTradeLicenseAjax-populateSubCategory.action' />
    </div>
    
    <label class="col-sm-2 control-label text-right"><s:text name='license.subCategory.lbl' /><span class="mandatory"></span></label>
    <div class="col-sm-3 add-margin">
        <s:select name="tradeName" id="subCategory" list="dropdownData.subCategoryList"
	listKey="id" listValue="name" headerKey="-1" headerValue="%{getText('default.select')}" value="%{tradeName.id}" class="form-control"/>
    </div> 
</div>

<div class="form-group">
    <label class="col-sm-3 control-label text-right"><s:text name='license.uom.lbl' /><span class="mandatory"></span></label>
     <div class="col-sm-3 add-margin">
        <s:textfield name="uom" maxlength="8" id="uom" value="%{uom}"  readOnly="true" class="form-control"  />
    </div>
    <label class="col-sm-2 control-label text-right"><s:text name='license.premises.lbl' /><span class="mandatory"></span></label>
    <div class="col-sm-3 add-margin">
        <s:textfield name="tradeArea_weight" maxlength="8" id="tradeArea_weight" value="%{tradeArea_weight}" cssClass="form-control patternvalidation"  data-pattern="number"  />
    </div>
</div>

<div class="form-group">
    <label class="col-sm-3 control-label text-right"><s:text name='license.remarks.lbl' /></label>
    <div class="col-sm-3 add-margin">
         <s:textarea name="remarks" id="remarks" value="%{remarks}" maxlength="500" class="form-control"/>
    </div>
    
	<label class="col-sm-2 control-label text-right"><s:text name='license.startdate' /></label>
	<div class="col-sm-3 add-margin">
	<s:date name="startDate" id="formattedStartDate" format="dd/MM/yyyy" />
	<s:textfield name="startDate" cssClass="form-control datepicker" data-date-end-date="0d" id="startDate"  maxlength="10" value="%{formattedStartDate}"/>
	    
		<%--<s:date name="startDate" id="startdate" format="dd/MM/yyyy" />
		 <s:textfield name="startDate" id="startDate" onfocus="waterMarkTextIn('startDate','dd/mm/yyyy');" onblur="validateDateFormat(this);waterMarkTextOut('startDate','dd/mm/yyyy'); lessThanOrEqualToCurrentDate(this);" maxlength="10" size="10" value="%{startdate}" tabindex="4" onkeyup="DateFormat(this,this.value,event,false,'3')" />
		<a href="javascript:show_calendar('forms[0].startDate',null,null,'DD/MM/YYYY');" onmouseover="window.status='Date Picker';return true;" onmouseout="window.status='';return true;"> <img  alt="Date" width="18" height="18" border="0" align="absmiddle" id="calenderImgId" src="${pageContext.request.contextPath}/resources/image/calendaricon.gif" /> </a>
		 --%>
    </div>
</div>