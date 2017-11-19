<%--
  ~    eGov  SmartCity eGovernance suite aims to improve the internal efficiency,transparency,
  ~    accountability and the service delivery of the government  organizations.
  ~
  ~     Copyright (C) 2017  eGovernments Foundation
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
  ~            Further, all user interfaces, including but not limited to citizen facing interfaces,
  ~            Urban Local Bodies interfaces, dashboards, mobile applications, of the program and any
  ~            derived works should carry eGovernments Foundation logo on the top right corner.
  ~
  ~            For the logo, please refer http://egovernments.org/html/logo/egov_logo.png.
  ~            For any further queries on attribution, including queries on brand guidelines,
  ~            please contact contact@egovernments.org
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
  ~
  --%>

<%@ page language="java" pageEncoding="UTF-8"%>
<%@ include file="/includes/taglibs.jsp" %>
<%@ taglib uri="/WEB-INF/taglib/cdn.tld" prefix="cdn"%>
<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN">
<html>
  <head>
    <title><s:text name="unit.rate.master.title"/></title>
	<script src="<cdn:url value='/resources/global/js/bootstrap/bootstrap.js' context='/egi'/>"></script>
	<link href="<cdn:url value='/resources/global/css/bootstrap/bootstrap-datepicker.css' context='/egi'/>" rel="stylesheet" type="text/css" />
	<script src="<cdn:url value='/resources/global/js/bootstrap/bootstrap-datepicker.js' context='/egi'/>"></script>
	<script src="<cdn:url value='/resources/global/js/bootstrap/typeahead.bundle.js' context='/egi'/>"></script>

<script type="text/javascript">

jQuery.noConflict();
jQuery(function ($) {
	try { 
		$(".datepicker").datepicker({
			format: "dd/mm/yyyy"
		}); 
		}catch(e){
		console.warn("No Date Picker "+ e);
	}

		$('.datepicker').on('changeDate', function(ev){
		    $(this).datepicker('hide');
		});
});

var validationMessage = '';
var showMessage = false;
function validateData(name){
	var zoneId = document.getElementById("zoneId").value;
	var usageId = document.getElementById("usageId").value;
	var structureClassId = document.getElementById("structureClassId").value;
	var taxAmount = document.getElementById("categoryAmount").value;
	var fromDate = document.getElementById("fromDate").value;

	if(name == 'add') {
		if(zoneId == -1){
			alert('Please select the Zone');
			return false;
		}
		if(usageId == -1){
			alert('Please select the Nature of Building Use');
			return false;
		} 
		if(structureClassId == -1){
			alert('Please select the Classification of Building');
			return false;
		}
		if(taxAmount =='' || eval(taxAmount)==0){
			alert('Please enter the Unit Rate');
			return false;
		}
		if(fromDate == ''){
			alert('Please enter the Effect From Date');
			return false;
		}

		if(validationMessage != ''){
		 	if(!confirmSubmit(validationMessage)) {
				return false;
			}
		}
 	document.forms[0].action = 'unitRate-create.action';
	} else if(name == 'update') {
		if(taxAmount =='' || eval(taxAmount)==0){
			alert('Please enter the Unit Rate');
			return false;
		}
		jQuery("#zoneId").removeAttr('disabled');
		jQuery("#usageId").removeAttr('disabled');
		jQuery("#structureClassId").removeAttr('disabled');
		jQuery("#fromDate").removeAttr('disabled');
		document.forms[0].action = 'unitRate-update.action';
	 }  else {
		 document.forms[0].action = 'unitRate-searchForm.action?mode='+name;
		 }
	document.forms[0].submit;
	return true;
}

function checkIfCategoryExists(){
	var zoneId = document.getElementById("zoneId").value;
	var usageId = document.getElementById("usageId").value;
	var structureClassId = document.getElementById("structureClassId").value;
	var taxAmount = document.getElementById("categoryAmount").value;
	var fromDate = document.getElementById("fromDate").value;

	makeJSONCall(["Value","validationMessage"],'/ptis/common/ajaxCommon-checkIfCategoryExists.action',{zoneId:zoneId,usageId:usageId,structureClassId:structureClassId,categoryFromDate:fromDate},
			categoryCheckSuccess,categoryCheckFailure) ;
}

categoryCheckSuccess = function(req,res){
results=res.results;
var checkResult='';

	if(results != '') {
		checkResult =   results[0].Value;
		validationMessage =   results[0].validationMessage;
	}
	if(checkResult != '' && checkResult=='yes'){
		showMessage = true;
	}	
}
categoryCheckFailure= function(){
	alert('Unable to check for existing category');
}

function makeReadyOnly() {
	var mode = '<s:property value="%{mode}"/>';
	if(mode == 'edit') {
		jQuery("#zoneId").attr('disabled', 'disabled');
		jQuery("#usageId").attr('disabled', 'disabled');
		jQuery("#structureClassId").attr('disabled', 'disabled');
		jQuery("#fromDate").attr('disabled', 'disabled');
	} 
	if(mode == 'view') {
		jQuery("#zoneId").attr('disabled', 'disabled');
		jQuery("#usageId").attr('disabled', 'disabled');
		jQuery("#structureClassId").attr('disabled', 'disabled');
		jQuery("#fromDate").attr('disabled', 'disabled');
		jQuery("#categoryAmount").attr('disabled', 'disabled');
		jQuery("")
	} 
}
</script>
</head> 

<body onload="makeReadyOnly();">
  <s:if test="%{hasErrors()}">
		<div class="errorstyle" id="unitrate_error_area">
			<div class="errortext">
				<s:actionerror />
			</div>
		</div>
	  </s:if>
  <s:form name="unitRateForm" action="unitRate" theme="simple" >
  <s:push value="model">
  <s:hidden name="id" value="%{id}"></s:hidden>
  <s:hidden name="categoryName" value="%{categoryName}"></s:hidden>
  <s:token />
 	<div class="formmainbox">
 	<s:if test="%{mode == 'new'}">
	<div class="headingbg"><s:text name="unit.rate.master.title"/></div>
	</s:if>
	<s:if test="%{mode == 'edit'}">
	<div class="headingbg"><s:text name="unit.rate.master.update.title"/></div>
	</s:if>
	<s:if test="%{mode == 'view'}">
	<div class="headingbg"><s:text name="unit.rate.master.view.title"/></div>
	</s:if>
		<table width="100%" border="0" cellspacing="0" cellpadding="0" >
			<tr>
				<td class="greybox" width="20%">&nbsp;</td>
				<td class="greybox" width="30%"><s:text name="unit.rate.zone"/><span class="mandatory1">*</span> :</td>
				<td class="greybox" width="30%">
			   	<s:select headerKey="-1"
					headerValue="%{getText('default.select')}" name="zoneId"
					id="zoneId" listKey="id" listValue="name"
					list="dropdownData.ZoneList" value="%{zoneId}"
					cssClass="selectnew"  />
				</td>
				<td class="greybox" width="20%">&nbsp;</td>
			</tr>
			<tr>
				<td class="greybox" width="20%">&nbsp;</td>
				<td class="greybox" width="30%"><s:text name="unit.rate.usage"/><span id="spanMandatory" class="mandatory1">*</span> :</td>
				<td class="greybox" width="30%">
			   	<s:select headerKey="-1"
					headerValue="%{getText('default.select')}" name="usageId"
					id="usageId" listKey="id" listValue="usageName"
					list="dropdownData.UsageList" value="%{usageId}"
					cssClass="selectnew"  />
				</td>
				<td class="greybox" width="20%">&nbsp;</td>
			</tr>
			<tr>
				<td class="greybox" width="20%">&nbsp;</td>
				<td class="greybox" width="30%"><s:text name="unit.rate.structure.classification"/> <span class="mandatory1">*</span> :</td>
				<td class="greybox" width="30%">
			   	<s:select headerKey="-1"
					headerValue="%{getText('default.select')}" name="structureClassId"
					id="structureClassId" listKey="id" listValue="typeName"
					list="dropdownData.StructureClassificationList" value="%{structureClassId}"
					cssClass="selectnew"  />
				</td>
				<td class="greybox" width="20%">&nbsp;</td>
			</tr>
			<tr>
				<td class="greybox" width="20%">&nbsp;</td>
				<td class="greybox" width="30%"><s:text name="unit.rate.amount"/> <span class="mandatory1">*</span> :</td>
				<td class="greybox" width="30%">
			   		<s:textfield name="categoryAmount" id="categoryAmount" size="12" maxlength="12" onblur = "trim(this,this.value);checkForTwoDecimals(this,'Unit Rate');checkZero(this,'Unit Rate');"></s:textfield>
				<td class="greybox" width="20%">&nbsp;</td>
			</tr>
			<tr>
				<td class="greybox" width="20%">&nbsp;</td>
				<td class="greybox" width="30%"><s:text name="unit.rate.fromDate"/> <span class="mandatory1">*</span> :</td>
				<td class="greybox" width="30%">
				    <s:date name="fromDate" var="efffromDate" format="dd/MM/yyyy" /> 
					<s:textfield name="fromDate"  cssClass="form-control datepicker" id="fromDate" size="12" autocomplete="off" maxlength="12" value="%{#efffromDate}"></s:textfield>
				<td class="greybox" width="20%">&nbsp;</td>
			</tr>
			
		</table>
        	
        	<br />
        	<font size="2"><div align="right" class="mandatory1">&nbsp;&nbsp;<s:text name="mandtryFlds"/></div></font>
		    <div class="buttonbottom" align="center">	
		    <s:if test="%{mode =='new'}">
		    	<s:submit value="Add" name="Add"
						id='Add' cssClass="buttonsubmit"  onclick="return validateData('add');" />  
			
			</s:if>
		    <s:elseif test="%{mode == 'edit'}">
						<s:submit value="Update" name="Update" id='Update' cssClass="buttonsubmit" onclick="return validateData('update');" /> 
		     </s:elseif> 
				<input type="button" name="button2" id="button2" value="Close"  class="button" onclick="window.close();" />
			</div>
	</div>
	</s:push>
	</s:form>
</body>
</html>


