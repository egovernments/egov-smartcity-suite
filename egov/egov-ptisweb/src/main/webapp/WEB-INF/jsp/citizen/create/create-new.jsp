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

<%@ page language="java" pageEncoding="UTF-8"%>
<%@ include file="/includes/taglibs.jsp"%>
<%@ taglib uri="/WEB-INF/taglib/cdn.tld" prefix="cdn"%>
<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN">
<html>
<head>
<title>
	<s:text name='NewProp.title' />
</title>
<link rel="stylesheet" href="<cdn:url value='/resources/global/css/font-icons/font-awesome/css/font-awesome.min.css' context='/egi'/>">
<script
	src="<cdn:url value='/resources/global/js/bootstrap/bootstrap.js' context='/egi'/>"
	type="text/javascript"></script>
<link
	href="<cdn:url value='/resources/global/css/bootstrap/bootstrap-datepicker.css' context='/egi'/>"
	rel="stylesheet" type="text/css" />
<script
	src="<cdn:url value='/resources/global/js/bootstrap/bootstrap-datepicker.js' context='/egi'/>"
	type="text/javascript"></script>
<script
	src="<cdn:url value='/resources/global/js/bootstrap/typeahead.bundle.js' context='/egi'/>"
	type="text/javascript"></script>
<script src="<cdn:url value='/resources/javascript/helper.js' context='/ptis'/>"></script>
</head>

<body onload="loadOnStartUp();">
	<s:if test="%{hasErrors()}">
		<div class="errorstyle" id="property_error_area">
			<div class="errortext">
				<s:actionerror />
			</div>
		</div>
	</s:if>
	<s:form name="CitizenCreateForm" action="create-create"
		theme="simple" enctype="multipart/form-data">
		<s:push value="model">
			<s:token />
			<s:hidden name="applicationSource" id="applicationSource" value="%{applicationSource}" />
			<s:hidden name="modelId" id="modelId" value="%{modelId}" />
			<div class="formmainbox">
				<div class="headingbg">
					<s:text name="CreatePropertyHeader" /> 
				</div>
				<table width="100%" border="0" cellspacing="0" cellpadding="0">
					<tr>
						<%@ include file="../../create/createPropertyForm.jsp"%>
					</tr>
					<s:if test="%{!documentTypes.isEmpty()}">
						<tr>
							<%@ include file="../../common/DocumentUploadForm.jsp"%>
						</tr>
					</s:if>
					<tr>
						<%@ include file="../../workflow/commonWorkflowMatrix-button.jsp"%>
					</tr>
				</table>
			</div>
		</s:push>
	</s:form>
	<script type="text/javascript">
		jQuery.noConflict();
		jQuery("#loadingMask").remove();
		jQuery(function($) {
			try {
				$(".datepicker").datepicker({
					format : "dd/mm/yyyy",
					autoclose:true
				});
			} catch (e) {
				console.warn("No Date Picker " + e);
			}
		});

		function loadOnStartUp() {
			document.getElementById('assessmentRow').style.display = "none";
			enableCorresAddr();
			enableFieldsForPropType();
			makeMandatory();
			enableOrDisableSiteOwnerDetails(jQuery('input[name="propertyDetail.structure"]'));
			enableOrDisableBPADetails(jQuery('input[name="propertyDetail.buildingPlanDetailsChecked"]'));
			var category = '<s:property value="%{propertyDetail.categoryType}"/>';
			document.forms[0].propTypeCategoryId.options[document.forms[0].propTypeCategoryId.selectedIndex].value = category;
			toggleFloorDetails();
			
			var aadhartextboxes = jQuery('.txtaadhar');
			aadhartextboxes.each(function() {
				if (jQuery(this).val()) {
					getAadharDetails(this);
				}
			});
			populateBoundaries();
			showHideFirmName();
			if (jQuery('#floorDetailsEntered').is(":checked")) {
				jQuery('#areaOfPlot').attr('readonly', true);
				disableBuiltUpAreaDetails();
			} else {
				showHideLengthBreadth();
			}
			<s:if test = '%{propertyDetail.appurtenantLandChecked}'>
				jQuery('#vacantLandArea').attr('readOnly', true);
			</s:if>
		}

		function onSubmit() {
			jQuery('#gender, #guardianRelation').removeAttr('disabled');
			<s:if test="%{state == null}">
				var propertyType = jQuery("#propTypeId option:selected").text();
				if (propertyType != "Vacant Land" && !jQuery('#floorDetailsEntered').prop("checked")) {
					bootbox.alert('Please check floor details entered checkbox');
					return false;
				}
				jQuery("#floorDetails tr").find('select').each(function() {
					if(jQuery(this).attr('id') == 'unstructuredLand') {
						jQuery(this).removeAttr('disabled');
					}
				});
			</s:if>
			document.forms[0].action = 'create-create.action';
			document.forms[0].submit;
			return true;
		}

		function enableDisableFirmName(obj){ 
			var selIndex = obj.selectedIndex;
			if(selIndex != undefined){
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

		function disableBuiltUpAreaDetails() {
			jQuery("#floorDetails tr").find('input, select').each(function() {
				if (jQuery(this).attr('id') == 'builtUpArealength' || jQuery(this).attr('id') == 'builtUpAreabreadth' 
						|| jQuery(this).attr('id') == 'builtUpArea') {
					jQuery(this).attr('readonly', true);
				}
				if (jQuery(this).attr('id') == 'unstructuredLand') {
					jQuery(this).attr('disabled', true);
				}
			});
		}
				
	</script>
</body>
</html>