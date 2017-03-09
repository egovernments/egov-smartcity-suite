<%--
  ~ eGov suite of products aim to improve the internal efficiency,transparency,
  ~      accountability and the service delivery of the government  organizations.
  ~
  ~       Copyright (C) 2016  eGovernments Foundation
  ~
  ~       The updated version of eGov suite of products as by eGovernments Foundation
  ~       is available at http://www.egovernments.org
  ~
  ~       This program is free software: you can redistribute it and/or modify
  ~       it under the terms of the GNU General Public License as published by
  ~       the Free Software Foundation, either version 3 of the License, or
  ~       any later version.
  ~
  ~       This program is distributed in the hope that it will be useful,
  ~       but WITHOUT ANY WARRANTY; without even the implied warranty of
  ~       MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
  ~       GNU General Public License for more details.
  ~
  ~       You should have received a copy of the GNU General Public License
  ~       along with this program. If not, see http://www.gnu.org/licenses/ or
  ~       http://www.gnu.org/licenses/gpl.html .
  ~
  ~       In addition to the terms of the GPL license to be adhered to in using this
  ~       program, the following additional terms are to be complied with:
  ~
  ~           1) All versions of this program, verbatim or modified must carry this
  ~              Legal Notice.
  ~
  ~           2) Any misrepresentation of the origin of the material is prohibited. It
  ~              is required that all modified versions of this material be marked in
  ~              reasonable ways as different from the original version.
  ~
  ~           3) This license does not grant any rights to any user of the program
  ~              with regards to rights under trademark law for use of the trade names
  ~              or trademarks of eGovernments Foundation.
  ~
  ~     In case of any queries, you can reach eGovernments Foundation at contact@egovernments.org.
  --%>

<%@ page language="java" pageEncoding="UTF-8"%>
<%@ include file="/includes/taglibs.jsp"%>
<%@ taglib uri="/WEB-INF/taglib/cdn.tld" prefix="cdn"%>

<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN">
<html>
<head>
<title>
	<s:if test="%{@org.egov.ptis.constants.PropertyTaxConstants@PROPERTY_MODIFY_REASON_ADD_OR_ALTER.equals(modifyRsn)}">
		<s:text name='ModProp.title' />
	</s:if>
	<s:elseif test="%{@org.egov.ptis.constants.PropertyTaxConstants@PROPERTY_MODIFY_REASON_BIFURCATE.equals(modifyRsn)}">
		<s:text name='BifurProp.title' />
	</s:elseif>
	<s:elseif test="%{@org.egov.ptis.constants.PropertyTaxConstants@PROPERTY_MODIFY_REASON_GENERAL_REVISION_PETITION.equals(modifyRsn)}">
		<s:text name='GenRevPetition.title' />
	</s:elseif>
</title>

<link href="<cdn:url value='/resources/global/css/bootstrap/bootstrap-datepicker.css' context='/egi'/>" rel="stylesheet" type="text/css" />
<script src="<cdn:url value='/resources/global/js/bootstrap/bootstrap-datepicker.js' context='/egi'/>"></script>
<script src="<cdn:url value='/resources/global/js/bootstrap/typeahead.bundle.js' context='/egi'/>"></script>

<script type="text/javascript">
	jQuery.noConflict();
	jQuery("#loadingMask").remove();
	jQuery(function($) {
		try {
			jQuery(".datepicker").datepicker({
				format : "dd/mm/yyyy",
				autoclose:true
			});
		} catch (e) {
			console.warn("No Date Picker " + e);
		}
	});

	function onSubmit() {
		var actionName = document.getElementById('workFlowAction').value;
		var action = null;
		if (actionName == 'Forward') {
			action = 'modifyProperty-forward.action';
		} else if (actionName == 'Approve') {
			action = 'modifyProperty-approve.action';
		} else if (actionName == 'Reject') {
			action = 'modifyProperty-reject.action';
		}
		document.forms[0].action = action;
		document.forms[0].submit;
		return true;
	}

	function loadOnStartUp() {
		enableFieldsForPropType();
		enableOrDisableSiteOwnerDetails(jQuery('input[name="propertyDetail.structure"]'));
		toggleFloorDetails();
		showHideFirmName();
		showHideLengthBreadth();
		<s:if test = '%{propertyByEmployee}'>
		loadDesignationFromMatrix();
		</s:if>
	}
	function submitDateEntry() { 
		document.forms[0].action = 'modifyProperty-saveDataEntry.action';
		document.forms[0].submit;
	   return true;
	}

	function enableDisableFirmName(obj){ 
		var selIndex = obj.selectedIndex;
		var selText = obj.options[selIndex].text; 
		var rIndex = getRow(obj).rowIndex;
		var tbl = document.getElementById('floorDetails');
		var firmval=getControlInBranch(tbl.rows[rIndex],'firmName'); 
		if(selText!=null && selText=='<s:property value="%{@org.egov.ptis.constants.PropertyTaxConstants@NATURE_OF_USAGE_RESIDENCE}"/>'){
			if(firmval.value!=null && firmval.value!="") 
				firmval.value="";
			firmval.readOnly = true;      
		} else{
			firmval.readOnly = false;
		}
	}  

	function showHideFirmName(){
		var tbl=document.getElementById("floorDetails");
        var tabLength = (tbl.rows.length)-1;
        for(var i=1;i<=tabLength;i++){
             enableDisableFirmName(getControlInBranch(tbl.rows[i],'floorUsage'));
        }
	}

	function showHideLengthBreadth(){
		var tbl=document.getElementById("floorDetails");
        var tabLength = (tbl.rows.length)-1;
        for(var i=1;i<=tabLength;i++){
             enableDisableLengthBreadth(getControlInBranch(tbl.rows[i],'unstructuredLand'));
        }
	}

	function calculatePlintArea(obj){ 
		var rIndex = getRow(obj).rowIndex;
		var tbl = document.getElementById('floorDetails');
		var builtUpArea=getControlInBranch(tbl.rows[rIndex],'builtUpArea');
		var unstructureLand = getControlInBranch(tbl.rows[rIndex],'unstructuredLand');
		if(unstructureLand.options[unstructureLand.selectedIndex].text=='No'){
			if(obj.value!=null && obj.value!=""){
				var buildLength=getControlInBranch(tbl.rows[rIndex],'builtUpArealength');
				var buildbreadth=getControlInBranch(tbl.rows[rIndex],'builtUpAreabreadth');
				  
				if(buildLength.value!=null && buildLength.value!="" && buildbreadth.value!=null && buildbreadth.value!=""){
					builtUpArea.value= roundoff(eval(buildLength.value * buildbreadth.value));
					trim(builtUpArea,builtUpArea.value);
					checkForTwoDecimals(builtUpArea,'Assessable Area');
					checkZero(builtUpArea,'Assessable Area');
				}
				else
					builtUpArea.value="";
			}else
				builtUpArea.value="";
		}
	}
	
	function enableDisableLengthBreadth(obj){ 
		var selIndex = obj.selectedIndex;
		if(obj.value=='true'){
				obj.value='true';
				obj.options[selIndex].selected = true;
		}
		else{
			obj.value='false';
			obj.options[selIndex].selected = true;
		}
		
		if(selIndex != undefined){
			var selText = obj.options[selIndex].text; 
			var rIndex = getRow(obj).rowIndex;
			var tbl = document.getElementById('floorDetails');
			var buildLength=getControlInBranch(tbl.rows[rIndex],'builtUpArealength');
			var buildbreadth=getControlInBranch(tbl.rows[rIndex],'builtUpAreabreadth');  
			var builtUpArea=getControlInBranch(tbl.rows[rIndex],'builtUpArea');
			if(selText!=null && selText=='No'){
				buildLength.readOnly = false;      
				buildbreadth.readOnly = false;
				builtUpArea.readOnly = true;
			} else{
				buildLength.value="";
				buildLength.readOnly = true;
				buildbreadth.value="";
				buildbreadth.readOnly = true;
				builtUpArea.readOnly = false;
			}
		}
	}
</script>
    <script src="<cdn:url value='/resources/global/js/egov/inbox.js?rnd=${app_release_no}' context='/egi'/>"></script>
<script src="<cdn:url value='/resources/javascript/helper.js' context='/ptis'/>"></script>
</head>

<body onload="loadOnStartUp();">
	<div align="left" class="errortext">
		<s:actionerror />
	</div>
	<!-- Area for error display -->
	<div class="errorcss" id="jsValidationErrors" style="display:none;"></div>
	<s:form name="ModifyPropertyForm" action="modifyProperty" enctype="multipart/form-data" method="post"
		theme="simple" validate="true">
		<s:push value="model">
			<s:token />
			<div class="formmainbox">
				<div class="headingbg" id="modPropHdr">
					<s:if test="%{@org.egov.ptis.constants.PropertyTaxConstants@PROPERTY_MODIFY_REASON_ADD_OR_ALTER.equals(modifyRsn)}">
						<s:text name='ModProp.title' />
					</s:if>
					<s:elseif test="%{@org.egov.ptis.constants.PropertyTaxConstants@PROPERTY_MODIFY_REASON_BIFURCATE.equals(modifyRsn)}">
						<s:text name='BifurProp.title' />
					</s:elseif>
					<s:elseif test="%{@org.egov.ptis.constants.PropertyTaxConstants@PROPERTY_MODIFY_REASON_EDIT_DATA_ENTRY.equals(modifyRsn)}">
						<s:text name='editdataentry.title' />
					</s:elseif>
					<s:elseif test="%{@org.egov.ptis.constants.PropertyTaxConstants@PROPERTY_MODIFY_REASON_GENERAL_REVISION_PETITION.equals(modifyRsn)}">
		                <s:text name='GenRevPetition.title' />
	                </s:elseif>
				</div>
				<%@ include file="modifyPropertyForm.jsp"%>
				<s:hidden name="modelId" id="modelId" value="%{modelId}" />
				<s:hidden id="indexNumber" name="indexNumber" value="%{indexNumber}" />
				<s:hidden id="modifyRsn" name="modifyRsn" value="%{modifyRsn}" />
				<s:hidden id="ownerName" name="ownerName" value="%{ownerName}" />
				<s:hidden id="propAddress" name="propAddress" value="%{propAddress}" />
			    <s:hidden name="meesevaApplicationNumber" id="meesevaApplicationNumber" value="%{meesevaApplicationNumber}" />
				<s:if test="%{@org.egov.ptis.constants.PropertyTaxConstants@PROPERTY_MODIFY_REASON_EDIT_DATA_ENTRY.equals(modifyRsn)}">
				<div class="buttonbottom" align="center">
					<input type="submit" id="Save" class="btn btn-primary" value="Save" onclick="submitDateEntry();"/> 
					<input type="button" name="button2" id="button2" value="Close" class="btn btn-primary" onclick="window.close();" /></td>
				</div>
				</s:if>
				<s:elseif test="%{propertyByEmployee == true}">
				<%@ include file="../workflow/commonWorkflowMatrix.jsp"%>
				<div class="buttonbottom" align="center">
					<%@ include file="../workflow/commonWorkflowMatrix-button.jsp" %>
				</div>
				</s:elseif>
				<s:else>
				<div class="buttonbottom" align="center">
					<%@ include file="../workflow/commonWorkflowMatrix-button.jsp" %>
				</div>
				</s:else>
			</div>
		</s:push>
	</s:form>
	<%@ include file="../workflow/commontaxcalc-details.jsp"%>
	<script type="text/javascript" src="<cdn:url value='/resources/javascript/tax-calculator.js?rnd=${app_release_no}'/>"></script>
</body>
</html>
