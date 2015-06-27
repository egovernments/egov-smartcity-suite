<!--
	eGov suite of products aim to improve the internal efficiency,transparency, 
    accountability and the service delivery of the government  organizations.
 
    Copyright (C) <2015>  eGovernments Foundation
 
	The updated version of eGov suite of products as by eGovernments Foundation 
    is available at http://www.egovernments.org
 
    This program is free software: you can redistribute it and/or modify
    it under the terms of the GNU General Public License as published by
    the Free Software Foundation, either version 3 of the License, or
    any later version.
 
    This program is distributed in the hope that it will be useful,
    but WITHOUT ANY WARRANTY; without even the implied warranty of
    MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
    GNU General Public License for more details.
 
    You should have received a copy of the GNU General Public License
    along with this program. If not, see http://www.gnu.org/licenses/ or 
    http://www.gnu.org/licenses/gpl.html .
 
    In addition to the terms of the GPL license to be adhered to in using this
    program, the following additional terms are to be complied with:
 
 	1) All versions of this program, verbatim or modified must carry this 
 	   Legal Notice.
 
 	2) Any misrepresentation of the origin of the material is prohibited. It 
 	   is required that all modified versions of this material be marked in 
 	   reasonable ways as different from the original version.
 
 	3) This license does not grant any rights to any user of the program 
 	   with regards to rights under trademark law for use of the trade names 
 	   or trademarks of eGovernments Foundation.
 
   	In case of any queries, you can reach eGovernments Foundation at contact@egovernments.org.
-->
<%@ page language="java" pageEncoding="UTF-8"%>
<%@ include file="/includes/taglibs.jsp" %>
<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN">
<html>
  <head>
    <title><s:text name="unit.rate.master.title"/></title>
	<script src="<c:url value='/resources/global/js/bootstrap/bootstrap.js' context='/egi'/>"></script>
	<link href="<c:url value='/resources/global/css/bootstrap/bootstrap-datepicker.css' context='/egi'/>" rel="stylesheet" type="text/css" />
	<script src="<c:url value='/resources/global/js/bootstrap/bootstrap-datepicker.js' context='/egi'/>"></script>
	<script src="<c:url value='/resources/global/js/bootstrap/typeahead.bundle.js' context='/egi'/>"></script> 

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

function validateData(){
	var zoneId = document.getElementById("zoneId").value;
	var usageId = document.getElementById("usageId").value;
	var structureClassId = document.getElementById("structureClassId").value;
	var taxAmount = document.getElementById("categoryAmount").value;
	var fromDate = document.getElementById("fromDate").value;

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

	document.forms[0].action = 'unitRate-create.action';
	document.forms[0].submit;
	return true;
}
</script>
</head> 

<body>
  <div align="left">
  	<s:actionerror/>
  </div>
  <s:form name="unitRateForm" action="unitRate" theme="simple" >
  <s:push value="model">
  <s:token />
 	<div class="formmainbox">
	<div class="headingbg"><s:text name="unit.rate.master.title"/></div>
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
				<td class="greybox" width="30%"><s:text name="unit.rate.usage"/><span class="mandatory1">*</span> :</td>
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
					<s:textfield name="fromDate"  cssClass="form-control datepicker" id="fromDate" size="12" maxlength="12"></s:textfield>
				<td class="greybox" width="20%">&nbsp;</td>
			</tr>
			
		</table>
        	
        	<br />
        	<font size="2"><div align="right" class="mandatory1">&nbsp;&nbsp;<s:text name="mandtryFlds"/></div></font>
        
		    <div class="buttonbottom" align="center">	
		    	<s:submit value="Add" name="Add"
						id='Create:Save' cssClass="buttonsubmit" method="create" onclick="return validateData();" />  
				<input type="reset" value="Clear" class="btn btn-default" />
				<input type="reset" value="Close" class="btn btn-default" onclick="window.close();" />
			</div>
	</div>
	</s:push>
	</s:form>
</body>
</html>


