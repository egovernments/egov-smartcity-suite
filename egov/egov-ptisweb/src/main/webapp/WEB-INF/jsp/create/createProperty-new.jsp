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
<title><s:if test="mode=='create' || mode=='edit'">
		<s:text name='NewProp.title' />
	</s:if></title>
<link
	href="<cdn:url value='/resources/global/css/bootstrap/bootstrap-datepicker.css' context='/egi'/>"
	rel="stylesheet" type="text/css" />
<script
	src="<cdn:url value='/resources/global/js/bootstrap/bootstrap-datepicker.js' context='/egi'/>"
	type="text/javascript"></script>
<script
	src="<cdn:url value='/resources/global/js/bootstrap/typeahead.bundle.js' context='/egi'/>"
	type="text/javascript"></script>
</head>

<body onload="loadOnStartUp();">
	<s:if test="%{hasErrors()}">
		<div class="errorstyle" id="property_error_area">
			<div class="errortext">
				<s:actionerror />
			</div>
		</div>
	</s:if>
	<s:form name="CreatePropertyForm" action="createProperty-create"
		theme="simple" enctype="multipart/form-data">
		<s:push value="model">
			<s:token />
			<s:hidden name="mode" id="mode" value="%{mode}" />
			<s:hidden name="meesevaApplicationNumber" id="meesevaApplicationNumber" value="%{meesevaApplicationNumber}" />
			<s:hidden name="meesevaServiceCode" id="meesevaServiceCode" value="%{meesevaServiceCode}" /> 
			<s:hidden name="modelId" id="modelId" value="%{modelId}" />
			<div class="formmainbox">
				<div class="headingbg">
					<s:text name="CreatePropertyHeader" />
				</div>
				<table width="100%" border="0" cellspacing="0" cellpadding="0">
					<tr>
						<%@  include file="createPropertyForm.jsp"%>
					</tr>
					<s:if test="%{!assessmentDocumentTypes.isEmpty()}">
						<tr>
							<%@ include file="document-typedetails-form.jsp"%>
						</tr>
					</s:if>
					<s:if test="%{!documentTypes.isEmpty()}">
						<tr>
							<%@ include file="../common/DocumentUploadForm.jsp"%>
						</tr>
					</s:if>
					<s:if test="%{propertyTaxDetailsMap.size != 0 && isExemptedFromTax == false}">
						<tr class="taxDetails">
							<td colspan="5">
								<div class="headingsmallbg">
									<span class="bold"><s:text name="taxdetailsheader" /> </span>
								</div>
							</td>
						</tr>
						<tr>
							<td colspan="5">
								<div align="center">
									<%@ include file="../common/propertyTaxDetailsForm.jsp"%>
								</div>
							</td>
						</tr>
					</s:if>
					<s:if test="%{state != null}">
						<tr>
							<%@ include file="../common/workflowHistoryView.jsp"%>
						<tr>
					</s:if>
					<s:if test="%{eligibleInitiator == true}">
					<s:if test="%{propertyByEmployee == true}">
						<tr>
							<%@ include file="../workflow/commonWorkflowMatrix.jsp"%>
						</tr>
						<tr>
							<font size="2"><div align="left" class="mandatory1">
									&nbsp;&nbsp;
									<s:text name="mandtryFlds" />
								</div></font>
						</tr>
						<tr>
							<%@ include file="../workflow/commonWorkflowMatrix-button.jsp"%>
						</tr>
					</s:if>
					<s:else>
						<tr>
							<%@ include file="../workflow/commonWorkflowMatrix-button.jsp"%>
						</tr>
					</s:else>
					</s:if>
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
			}
		});

		jQuery('#calculateTax').click( function () {
			jQuery.ajax({url:"/ptis/create/createProperty-calculateTax.action",
				data : jQuery("form").serialize(),
    			cache:false,
    			beforeSend: function(){
    				jQuery("#fpoptbl").hide();
    				jQuery('#loading').show();
    				jQuery('#popup').show();
    				jQuery('.loader-class').modal('show', {backdrop: 'static'});
    			}
    		}).success(function (response) {
    			jQuery('.loader-class').modal('hide');
    			jQuery('.popup').addClass('popup-show');
    			jQuery('#fpoptbl tbody').html("");
    			if(response.startsWith("Please")) {
    				jQuery('#fpoptbl tbody').append(response);
    				jQuery("#fpoptbl").show();
					jQuery('#loading').hide(); 
        		} else {
    				var splitByTelde = response.split('~');
    				for(i=0; i<splitByTelde.length;i++) {
    					var rowarry=splitByTelde[i].split('=');
    					var tablerow="<tr><td>"+ rowarry[0] +"</td><td>"+"Rs "+ rowarry[1] +"</td><tr>";
    					jQuery('#fpoptbl tbody').append(tablerow);
    					jQuery("#fpoptbl").show();
    					jQuery('#loading').hide(); 
    				}
        		}
    		});
		});

		jQuery(document).on('click', '.popup', function (event) {
			if(jQuery(event.target).hasClass('close-pop'))
			{
				jQuery(this).removeClass('popup-show');
			} 
		}); 

		function loadOnStartUp() {
			document.getElementById('assessmentRow').style.display = "none";
			enableCorresAddr();
			enableAppartnaumtLandDetails();
			makeMandatory();
			document.getElementById("appurtenantRow").style.display = "none";
			enableOrDisableSiteOwnerDetails(jQuery('input[name="propertyDetail.structure"]'));
			enableOrDisableBPADetails(jQuery('input[name="propertyDetail.buildingPlanDetailsChecked"]'));
			var appartunentLand = jQuery('input[name="propertyDetail.appurtenantLandChecked"]');
			if (jQuery(appartunentLand).is(":checked")) {
				enableAppartnaumtLandDetails();
			}
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
			loadDesignationFromMatrix();
			showHideFirmName();
			showHideLengthBreadth();
		}

		function onSubmit() {
			jQuery('#gender, #guardianRelation').removeAttr('disabled');
			document.forms[0].action = 'createProperty-create.action';
			<s:if test="mode=='edit'">
			document.forms[0].action = 'createProperty-forward.action';
			</s:if>
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
				
	</script>
	<script
            src="<cdn:url value='/resources/global/js/egov/inbox.js?rnd=${app_release_no}' context='/egi'/>"></script>
		<script src="<cdn:url value='/resources/javascript/helper.js?rnd=${app_release_no}' context='/ptis'/>"></script>
	<%@ include file="../workflow/commontaxcalc-details.jsp"%>
</body>
</html>