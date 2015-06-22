<!-------------------------------------------------------------------------------
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
------------------------------------------------------------------------------->
<%@ page language="java" pageEncoding="UTF-8"%>
<%@ include file="/includes/taglibs.jsp"%>

<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN">
<html>
	<head>
		<title><s:text name='ModProp.title' />--</title>
		<sx:head />

       <link href="<c:url value='/resources/global/css/bootstrap/bootstrap-datepicker.css' context='/egi'/>" rel="stylesheet" type="text/css" />
       <script src="<c:url value='/resources/global/js/bootstrap/bootstrap-datepicker.js' context='/egi'/>"></script>
       
		<script type="text/javascript">
			jQuery.noConflict();
			jQuery("#loadingMask").remove();
			jQuery(function ($) {
				try { 
					jQuery(".datepicker").datepicker({
						format: "dd/mm/yyyy"
					}); 
					}catch(e){
					console.warn("No Date Picker "+ e);
				}

					jQuery('.datepicker').on('changeDate', function(ev){
						jQuery(this).datepicker('hide');
					});
			}); 
			
			function loadOnStartUp() {
				/* document.getElementById("rentBox").className = "hiddentext";
				document.getElementById("bldngCostId").className = "hiddentext";
				document.getElementById("opAlvId").className = "hiddentext";
				document.getElementById("occId").className = "hiddentext";
				document.getElementById("rentBox").readOnly = true;
				document.getElementById("bldngCostId").readOnly = true;
				document.getElementById("amenitiesId").disabled = true;
				document.getElementById("opAlvId").readOnly = true;
				document.getElementById("occId").readOnly = true;
				document.getElementById("dateOfCompletion").readOnly = true;
				document.getElementById("dateOfCompletion").className = "hiddentext";
				document.getElementById("floorDetailsConfirm").style.display = "none";
				document.getElementById("waterRate").style.display = "none";
				enableFieldsForPropType();
				enableRentBox();
				enableTaxExemptReason();
				var modifyRsn = '<s:property value="%{modifyRsn}" />';
				if (modifyRsn != 'DATA_UPDATE') {
					hideAddRmvBtnForResidFlats();
				}
				toggleFloorDetails();
		
				var complDateStr = document.getElementById("dateOfCompletion").value;
				if (complDateStr == "" || complDateStr == "DD/MM/YYYY"
						|| complDateStr == undefined) {
					waterMarkInitialize('dateOfCompletion', 'DD/MM/YYYY');
				}
				var tbl = document.getElementById('floorDetails');
				if (tbl != null) {
					resetDetailsForTenantOnload();
				}
				var ordDtStr = document.getElementById("orderDate").value;
				if (ordDtStr == "" || ordDtStr == "DD/MM/YYYY" || ordDtStr == undefined) {
					waterMarkInitialize('orderDate', 'DD/MM/YYYY');
				}
				enableCourtRulingDets();
				populateLocationFactors(); // pupulates Location Factors By Ward	
				populateFloorConstTypeDropDowns();
				toggleUnitTypeAndCategory();
				prepareUnitTypeCategories();
				prepareUsagesForUnitTypes();
		
				var intervalId = -1;
				var propTypeMstr = document.getElementById("propTypeMaster");
		
				if (propTypeMstr.options[propTypeMstr.selectedIndex].text == 'Mixed') {
					intervalId = setInterval(doOnValidationErrors, 1000);
				}
		
				if (areUnitTypeCatsAndUsagePopulated) {
					clearInterval(intervalId);
				}
				toggleForMixedPropertyOnUnitType(); */
				//enableSubmitButton();
			}
		
			/* var areUnitTypeCatsAndUsagePopulated = false;
			function doOnValidationErrors() {
		
				if (!areUnitTypeCatsAndUsagePopulated && isCategoriesPrepared
						&& isUsagesPrepared) {
					populateUnitTypeCatAndUsageOnValidationErrors();
				}
			}
		
			var modifyReason = "";
			function enableCourtRulingDets() {
				if (document.forms[0].reasonForModify != undefined) {
					modifyReason = document.forms[0].reasonForModify.options[document.forms[0].reasonForModify.selectedIndex].text;
				}
				if (modifyReason == "COURT RULING") {
					document.getElementById("courtOrdNo").readOnly = false;
					document.getElementById("courtOrdNo").className = "";
					document.getElementById("orderDate").readOnly = false;
					document.getElementById("orderDate").className = "";
					document.getElementById("judgeDet").readOnly = false;
					document.getElementById("judgeDet").className = "";
					document.getElementById("courtOrdNoRow").style.display = "";
					document.getElementById("JudgmtDetsRow").style.display = "";
				} else {
					document.getElementById("courtOrdNoRow").style.display = "none";
					document.getElementById("JudgmtDetsRow").style.display = "none";
				}
			}
		
			var targetitem = "";
			function getAmalgPropStatus(obj) {
				var index = getRow(obj).rowIndex;
				if (document.forms[0].amalgPropIds.length == undefined) {
					var billNo = document.forms[0].amalgPropIds.value;
					targetitem = document.forms[0].amalgPropIds;
				} else {
					var billNo = document.forms[0].amalgPropIds[index].value;
					targetitem = document.forms[0].amalgPropIds[index];
				}
				if (billNo == "") {
					alert("Please Enter a Property for Amalgamation");
					return false;
				}
		
				dataitem = window
						.open("../modify/modifyProperty!getStatus.action?oldpropId="
								+ billNo, 'dataitem',
								'resizable=yes,scrollbars=yes,height=700,width=800,status=yes');
				dataitem.targetitem = targetitem;
			}
		
			function resetFloorDetailsForResdAndNonResd(obj) {
				var propType = document.forms[0].propTypeMaster.options[document.forms[0].propTypeMaster.selectedIndex].text;
				var rowIndex = getRow(obj).rowIndex;
				var tbl = document.getElementById('floorDetails');
				if (tbl != null && propType == 'Residential & Non-Residential') {
					var rowo = tbl.rows;
					var RsdUsg = '<s:property value="@org.egov.ptis.constants.PropertyTaxConstants@USAGES_FOR_RESD"/>';
					var RsdUsgArray = RsdUsg.split(", ");
					var NonRsdUsg = '<s:property value="@org.egov.ptis.constants.PropertyTaxConstants@USAGES_FOR_NON_RESD"/>';
					var NonRsdUsgArray = NonRsdUsg.split(", ");
					var selectedUsage = obj.options[obj.selectedIndex].text;
					indexval = rowIndex - 1;
					var UsageTypeFlag;
		
					for (var i = 0; i < RsdUsgArray.length; i++) {
						if (selectedUsage == RsdUsgArray[i]) {
							UsageTypeFlag = 'Residential';
							break;
						}
					}
					if (UsageTypeFlag == null) {
						for (var j = 0; j < NonRsdUsgArray.length; j++) {
							if (selectedUsage == NonRsdUsgArray[j]) {
								UsageTypeFlag = 'Non-Residential';
								break;
							}
						}
					}
					if (UsageTypeFlag == 'Residential') {
						if (indexval == 0) {
							document.getElementById("width").value = "";
							document.getElementById("length").value = "";
							document.getElementById("interWallArea").value = "";
							document.getElementById("width").className = "hiddentext";
							document.getElementById("length").className = "hiddentext";
							document.getElementById("interWallArea").className = "hiddentext";
							document.getElementById("width").readOnly = true;
							document.getElementById("length").readOnly = true;
							document.getElementById("interWallArea").readOnly = true;
						} else {
							document.forms[0].width[indexval].setAttribute('name',
									'propertyDetail.floorDetailsProxy[' + indexval
											+ '].extraField4');
							document.forms[0].length[indexval].setAttribute('name',
									'propertyDetail.floorDetailsProxy[' + indexval
											+ '].extraField5');
							document.forms[0].interWallArea[indexval].setAttribute(
									'name', 'propertyDetail.floorDetailsProxy['
											+ indexval + '].extraField6');
							document.forms[0].width[indexval].value = "";
							document.forms[0].length[indexval].value = "";
							document.forms[0].interWallArea[indexval].value = "";
							document.forms[0].width[indexval].className = "hiddentext";
							document.forms[0].length[indexval].className = "hiddentext";
							document.forms[0].interWallArea[indexval].className = "hiddentext";
							document.forms[0].width[indexval].readOnly = true;
							document.forms[0].length[indexval].readOnly = true;
							document.forms[0].interWallArea[indexval].readOnly = true;
						}
					} else if (UsageTypeFlag == 'Non-Residential') {
						if (indexval == 0) {
							document.getElementById("width").className = "";
							document.getElementById("length").className = "";
							document.getElementById("interWallArea").className = "";
							document.getElementById("width").readOnly = false;
							document.getElementById("length").readOnly = false;
							document.getElementById("interWallArea").readOnly = false;
						} else {
							document.forms[0].width[indexval].setAttribute('name',
									'propertyDetail.floorDetailsProxy[' + indexval
											+ '].extraField4');
							document.forms[0].length[indexval].setAttribute('name',
									'propertyDetail.floorDetailsProxy[' + indexval
											+ '].extraField5');
							document.forms[0].interWallArea[indexval].setAttribute(
									'name', 'propertyDetail.floorDetailsProxy['
											+ indexval + '].extraField6');
							document.forms[0].width[indexval].className = "";
							document.forms[0].length[indexval].className = "";
							document.forms[0].interWallArea[indexval].className = "";
							document.forms[0].width[indexval].readOnly = false;
							document.forms[0].length[indexval].readOnly = false;
							document.forms[0].interWallArea[indexval].readOnly = false;
						}
					}
					if (i == 0) {
						document.getElementById("floorNo").disabled = false;
						document.getElementById("floorType").disabled = false;
						document.forms[0].floorUsage.options.length = 0;
						document.forms[0].floorUsage.options[0] = new Option("select",
								"-1");
						document.forms[0].floorUsage.value = "-1";
					} else if (i <= rowo.length - 2) {
						eval('document.forms[0].floorUsage' + (indexval - 1)
								+ '.options.length=0');
						eval('document.forms[0].floorUsage' + (indexval - 1)
								+ '.options[0] = new Option("select", "-1")');
						eval('document.forms[0].floorUsage' + (indexval - 1)
								+ '.value="-1"');
						eval('document.getElementById("floorConstType' + (indexval - 1)
								+ '").disabled=false');
						document.forms[0].constrYear[indexval].disabled = false;
					}
				}
			}
		
			function resetFloorDetailsForResdAndNonResdOnload() {
				var tbl = document.getElementById('floorDetails');
				if (tbl != null) {
					var rowo = tbl.rows;
					var RsdUsg = '<s:property value="@org.egov.ptis.constants.PropertyTaxConstants@USAGES_FOR_RESD"/>';
					var RsdUsgArray = RsdUsg.split(", ");
					var NonRsdUsg = '<s:property value="@org.egov.ptis.constants.PropertyTaxConstants@USAGES_FOR_NON_RESD"/>';
					var NonRsdUsgArray = NonRsdUsg.split(", ");
					for (var i = 0; i < rowo.length - 1; i++) {
						indexval = i;
						var UsageTypeFlag = null;
						if (i == 0) {
							var selectedUsage = document.forms[0].floorUsage.options[document.forms[0].floorUsage.selectedIndex].text;
						} else {
							var selectedUsage = eval('document.forms[0].floorUsage'
									+ eval(i - 1)
									+ '.options[document.forms[0].floorUsage'
									+ eval(i - 1) + '.selectedIndex].text');
						}
						if (selectedUsage != '--select--') {
							for (var j = 0; j < RsdUsgArray.length; j++) {
								if (selectedUsage == RsdUsgArray[j]) {
									UsageTypeFlag = 'Residential';
									break;
								}
							}
							if (UsageTypeFlag == null) {
								for (var k = 0; k < NonRsdUsgArray.length; k++) {
									if (selectedUsage == NonRsdUsgArray[k]) {
										UsageTypeFlag = 'Non-Residential';
										break;
									}
								}
							}
						}
						if (UsageTypeFlag == 'Residential'
								|| selectedUsage == '--select--') {
							if (i == 0) {
								document.getElementById("width").value = "";
								document.getElementById("length").value = "";
								document.getElementById("interWallArea").value = "";
								document.getElementById("width").className = "hiddentext";
								document.getElementById("length").className = "hiddentext";
								document.getElementById("interWallArea").className = "hiddentext";
								document.getElementById("width").readOnly = true;
								document.getElementById("length").readOnly = true;
								document.getElementById("interWallArea").readOnly = true;
							} else {
								document.forms[0].width[indexval].setAttribute('name',
										'propertyDetail.floorDetailsProxy[' + indexval
												+ '].extraField4');
								document.forms[0].length[indexval].setAttribute('name',
										'propertyDetail.floorDetailsProxy[' + indexval
												+ '].extraField5');
								document.forms[0].interWallArea[indexval].setAttribute(
										'name', 'propertyDetail.floorDetailsProxy['
												+ indexval + '].extraField6');
								document.forms[0].width[indexval].value = "";
								document.forms[0].length[indexval].value = "";
								document.forms[0].interWallArea[indexval].value = "";
								document.forms[0].width[indexval].className = "hiddentext";
								document.forms[0].length[indexval].className = "hiddentext";
								document.forms[0].interWallArea[indexval].className = "hiddentext";
								document.forms[0].width[indexval].readOnly = true;
								document.forms[0].length[indexval].readOnly = true;
								document.forms[0].interWallArea[indexval].readOnly = true;
							}
						} else if (UsageTypeFlag == 'Non-Residential') {
							if (i == 0) {
								document.getElementById("width").className = "";
								document.getElementById("length").className = "";
								document.getElementById("interWallArea").className = "";
								document.getElementById("width").readOnly = false;
								document.getElementById("length").readOnly = false;
								document.getElementById("interWallArea").readOnly = false;
							} else {
								document.forms[0].width[indexval].setAttribute('name',
										'propertyDetail.floorDetailsProxy[' + indexval
												+ '].extraField4');
								document.forms[0].length[indexval].setAttribute('name',
										'propertyDetail.floorDetailsProxy[' + indexval
												+ '].extraField5');
								document.forms[0].interWallArea[indexval].setAttribute(
										'name', 'propertyDetail.floorDetailsProxy['
												+ indexval + '].extraField6');
								document.forms[0].width[indexval].className = "";
								document.forms[0].length[indexval].className = "";
								document.forms[0].interWallArea[indexval].className = "";
								document.forms[0].width[indexval].readOnly = false;
								document.forms[0].length[indexval].readOnly = false;
								document.forms[0].interWallArea[indexval].readOnly = false;
							}
						}
					}
				}
			}
		
			function enableSubmitButton() {
				var modifyRsn = document.getElementById("modifyRsn").value;
				if (modifyRsn == "DATA_ENTRY" || modifyRsn == "MODIFY") {
					if (document.getElementById("allChngsCmpltd").checked == true) {
						document.getElementById("Modify:Save").disabled = true;
						document.getElementById("Modify:Forward").disabled = false;
					} else {
						document.getElementById("Modify:Save").disabled = false;
						document.getElementById("Modify:Forward").disabled = true;
					}
				}
			}
		
			function submitMsg() {
				if (document.getElementById("allChngsCmpltd").checked == true) {
					alert("Please uncheck the 'Have you completed making changes' checkbox to proceed with Submit");
					return false;
				}
		
				var modifyRsn = jQuery('#modifyRsn').val();
		
				if (modifyRsn != 'OBJ') {
					jQuery('#modifyRsn').val('DATA_ENTRY');
				}
			}
		
			function approveForwardMsg() {
				if (document.getElementById("allChngsCmpltd").checked == false) {
					alert("Please check the 'Have you completed making changes' checkbox to proceed with Approve/Forward");
					return false;
				}
			}
			function previewPrativrutta() {
				doLoadingMask();
				window
						.open(
								"../notice/propertyTaxNotice!generateNotice.action?basicPropId=<s:property value='%{basicProp.id}'/>&noticeType=Prativrutta&isPreviewPVR=true",
								"",
								"resizable=yes,scrollbars=yes,top=40, width=900, height=650");
				document.getElementById("GeneratePrativrutta").disabled = true;
				undoLoadingMask();
			}
		
			function setModificationType() {
				var dataEntryCode = '<s:property value="%{@org.egov.ptis.constants.PropertyTaxConstants@PROPERTY_MODIFY_REASON_DATA_ENTRY}" />';
				var modifyRsnCode = '<s:property value="%{@org.egov.ptis.constants.PropertyTaxConstants@PROPERTY_MODIFY_REASON_OBJ}" />';
				var modifyRsn = '<s:property value="%{modifyRsn}" />';
				if (modifyRsn == modifyRsnCode) {
					jQuery('#objModificationType').val(dataEntryCode);
				}
		
			} */
		</script>
</head>

<body onload="loadOnStartUp();">
	<div align="left">
		<s:actionerror />
	</div>
	<!-- Area for error display -->
	<div class="errorstyle" id="property_error_area" style="display: none;"></div>
	<s:form name="ModifyPropertyForm" action="modifyProperty"
		theme="simple" validate="true">
		<s:push value="model">
			<s:token />
			<div class="formmainbox">
				<div class="headingbg" id="modPropHdr">
					<s:text name="ModProp.title" />
				</div>
 					   <%@ include file="modifyPropertyForm.jsp"%>
					<!-- <div id="loadingMask" style="display: none">
						<p align="center">
							<img src="/egi/resources/erp2/images/bar_loader.gif"> <span
								id="message"><p style="color: red">Please wait....</p></span>
						</p>
					</div> -->
					<%-- <tr>
						<s:hidden name="modelId" id="modelId" value="%{modelId}" />
						<s:hidden id="indexNumber" name="indexNumber"
							value="%{indexNumber}" />
						<s:hidden id="modifyRsn" name="modifyRsn" value="%{modifyRsn}" />
						<s:hidden id="ownerName" name="ownerName" value="%{ownerName}" />
						<s:hidden id="propAddress" name="propAddress"
							value="%{propAddress}" />
						<s:hidden id="corrsAddress" name="corrsAddress"
							value="%{corrsAddress}" />
						<s:hidden id="fromDataEntry" name="fromDataEntry"
							value="%{fromDataEntry}" />--%>
					  
						<%--<td><s:submit value="Data Entry" name="Save"
								id="Modify:Save" method="save" cssClass="buttonsubmit"
								onclick="setModificationType();setWorkFlowInfo(this);return submitMsg();doLoadingMask();" /></td>
						<td><s:submit value="Forward" name="Forward"
								id="Modify:Forward" method="forwardModify"
								cssClass="buttonsubmit"
								onclick="setWorkFlowInfo(this);return approveForwardMsg();doLoadingMask();" /></td>
						<td><input type="button" name="button2" id="button2"
							value="Close" class="button" onclick="window.close();" /></td>
					</tr> --%>
					
				<%@ include file="../workflow/property-workflow.jsp"%>
				<div class="buttonbottom" align="center">		   
				 <s:submit value="Approve" name="Approve"
								id="Modify:Approve" method="save" cssClass="buttonsubmit"
								onclick="setWorkFlowInfo(this);return approveForwardMsg();doLoadingMask();" /></td>
				</div>
			</div>
			
		</s:push>
	</s:form>
</body>
</html>
