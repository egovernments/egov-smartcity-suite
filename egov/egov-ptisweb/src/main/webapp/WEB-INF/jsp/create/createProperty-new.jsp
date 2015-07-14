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
    <title>
    <s:if test="mode=='create' || mode=='edit'">
    	<s:text name='NewProp.title'/>
    	</s:if>
    	<s:if test="mode=='view'">
    	View Property Form
    	</s:if>
    </title>
	<sx:head/>	
	<!-- <script type="text/javascript" src="/ptis/resources/javascript/unitRentAgreement.js"></script> -->
<script src="<c:url value='/resources/global/js/bootstrap/bootstrap.js' context='/egi'/>"></script>
<link href="<c:url value='/resources/global/css/bootstrap/bootstrap-datepicker.css' context='/egi'/>" rel="stylesheet" type="text/css" />
<script src="<c:url value='/resources/global/js/bootstrap/bootstrap-datepicker.js' context='/egi'/>"></script>
<script src="<c:url value='/resources/global/js/bootstrap/typeahead.bundle.js' context='/egi'/>"></script>
<script type="text/javascript">

jQuery.noConflict();
jQuery("#loadingMask").remove();
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
function loadOnStartUp() {
	enableCorresAddr();
	/* document.getElementById("plotArea").style.display = ""; */
	document.getElementById("apartmentRow").style.display = "none";
	/* document.getElementById("ownerShipRow").style.display = "none";
	document.getElementById("vacantAreaRow").style.display = "none"; */
	document.getElementById("appartenantRow").style.display = "none";
	document.getElementById("drainageseatsrow").style.display = "none";
	/* document.getElementById("undivArea").style.display = "none";		
	document.getElementById("rentBox").className="hiddentext";
	document.getElementById("bldngCostId").className="hiddentext";
	document.getElementById("parentIndex").className="hiddentext";
	document.getElementById("opAlvId").className="hiddentext";
	document.getElementById("occId").className="hiddentext";
	document.getElementById("rentBox").readOnly=true;
	document.getElementById("bldngCostId").readOnly=true;
	document.getElementById("amenitiesId").disabled=true;
	document.getElementById("opAlvId").readOnly=true;
	document.getElementById("occId").readOnly=true;
	document.getElementById("parentIndex").readOnly=true;
	document.getElementById("dateOfCompletion").readOnly=true;
	document.getElementById("dateOfCompletion").className="hiddentext";
	document.getElementById("floorDetailsConfirm").style.display = "none";
	document.getElementById("waterRate").style.display = "none"; */
	
	enableFieldsForPropType();
	//hideAddRmvBtnForResidFlats();
	//enableCorresAddr();
	//enableTaxExemptReason();
	//makeMandatory();
	//enableRentBox();
			
	/* var complDateStr = document.getElementById("dateOfCompletion").value;
	if(complDateStr == "" || complDateStr == "DD/MM/YYYY" || complDateStr == undefined)
	{		
		waterMarkInitialize('dateOfCompletion','DD/MM/YYYY');
	}
	var tbl = document.getElementById('floorDetails');	
	if(tbl!=null) {
		resetDetailsForTenantOnload();
	} */
	
	//populateLocationFactors();	
	//populateFloorConstTypeDropDowns();
	//toggleForResNonRes();	
	 toggleFloorDetails();
	//toggleUnitTypeAndCategory();
	//prepareUnitTypeCategories();
	//prepareUsagesForUnitTypes();
	
	/* var intervalId = -1;
	var propTypeMstr = document.getElementById("propTypeMaster");
	
	if (propTypeMstr.options[propTypeMstr.selectedIndex].text == 'Mixed') {
		intervalId = setInterval(doOnValidationErrors, 1000);
	} 
	 
	if (areUnitTypeCatsAndUsagePopulated) {
		clearInterval(intervalId);
	} 	
	
	document.getElementById("taxExemptRow").style.display = "none";		 */
	//enableSubmitButton();  
}
function onSubmit(action,obj) { 
	//alert("On Submit called");
	document.getElementById('workflowBean.actionName').value = obj.id;
	document.forms[0].action = 'createProperty-create.action';
	//alert("On Submit called with  "+document.forms[0].action );
	document.forms[0].submit;
   return true;      
}

function onSubmit() { 
	//alert("On Submit called");
	document.forms[0].action = 'createProperty-create.action';
	<s:if test="mode=='edit'">
	document.forms[0].action = 'createProperty-forward.action';
	</s:if>
//	alert("On Submit called with  "+document.forms[0].action );
	document.forms[0].submit;
   return true;
}
/* var areUnitTypeCatsAndUsagePopulated = false;
function doOnValidationErrors() {	

	/* if (!areUnitTypeCatsAndUsagePopulated && isCategoriesPrepared && isUsagesPrepared) {
		populateUnitTypeCatAndUsageOnValidationErrors();	
	}  
}*/

/* function resetFloorDetailsForResdAndNonResd(obj) {
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
		indexval = rowIndex-1;
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
				document.forms[0].width[indexval]
					.setAttribute(
						'name',
						'propertyDetail.floorDetailsProxy[' + indexval + '].extraField4');
				document.forms[0].length[indexval]
					.setAttribute(
						'name',
						'propertyDetail.floorDetailsProxy[' + indexval + '].extraField5');
				document.forms[0].interWallArea[indexval]
					.setAttribute(
						'name',
						'propertyDetail.floorDetailsProxy[' + indexval + '].extraField6');
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
				document.forms[0].width[indexval]
					.setAttribute(
						'name',
						'propertyDetail.floorDetailsProxy[' + indexval + '].extraField4');
				document.forms[0].length[indexval]
					.setAttribute(
						'name',
						'propertyDetail.floorDetailsProxy[' + indexval + '].extraField5');
				document.forms[0].interWallArea[indexval]
					.setAttribute(
						'name',
						'propertyDetail.floorDetailsProxy[' + indexval + '].extraField6');
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

function resetFloorDetailsForResdAndNonResdOnload() {
	var tbl = document.getElementById('floorDetails');
	if (tbl != null) {
		var rowo = tbl.rows;
		var RsdUsg = '<s:property value="@org.egov.ptis.constants.PropertyTaxConstants@USAGES_FOR_RESD"/>';
		var RsdUsgArray = RsdUsg.split(", "); 
		var NonRsdUsg = '<s:property value="@org.egov.ptis.constants.PropertyTaxConstants@USAGES_FOR_NON_RESD"/>';
		var NonRsdUsgArray = NonRsdUsg.split(", ");
		for ( var i = 0; i < rowo.length - 1; i++) {
			indexval = i;
			var UsageTypeFlag = null;
			if (i == 0) {
				var selectedUsage = document.forms[0].floorUsage.options[document.forms[0].floorUsage.selectedIndex].text;
			} else {
				var selectedUsage = eval('document.forms[0].floorUsage' + eval(i-1) + '.options[document.forms[0].floorUsage' + eval(i-1) + '.selectedIndex].text');
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
			if (UsageTypeFlag == 'Residential' || selectedUsage == '--select--') {
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
					document.forms[0].width[indexval]
						.setAttribute(
							'name',
							'propertyDetail.floorDetailsProxy[' + indexval + '].extraField4');
					document.forms[0].length[indexval]
						.setAttribute(
							'name',
							'propertyDetail.floorDetailsProxy[' + indexval + '].extraField5');
					document.forms[0].interWallArea[indexval]
						.setAttribute(
							'name',
							'propertyDetail.floorDetailsProxy[' + indexval + '].extraField6');
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
					document.forms[0].width[indexval]
						.setAttribute(
							'name',
							'propertyDetail.floorDetailsProxy[' + indexval + '].extraField4');
					document.forms[0].length[indexval]
						.setAttribute(
							'name',
							'propertyDetail.floorDetailsProxy[' + indexval + '].extraField5');
					document.forms[0].interWallArea[indexval]
						.setAttribute(
							'name',
							'propertyDetail.floorDetailsProxy[' + indexval + '].extraField6');
					document.forms[0].width[indexval].className = "";
					document.forms[0].length[indexval].className = "";
					document.forms[0].interWallArea[indexval].className = "";
					document.forms[0].width[indexval].readOnly = false;
					document.forms[0].length[indexval].readOnly = false;
					document.forms[0].interWallArea[indexval].readOnly = false;
				}
			}
			if (i==0) {
				document.getElementById("floorNo").disabled = false;
				document.getElementById("floorType").disabled = false;	
				document.forms[0].floorUsage.options.length=0;
				document.forms[0].floorUsage.options[0] = new Option("select", "-1");
				document.forms[0].floorUsage.value="-1";
			} else if (i <= rowo.length - 2){
				eval('document.forms[0].floorUsage'+(indexval-1)+'.options.length=0');
				eval('document.forms[0].floorUsage'+(indexval-1)+'.options[0] = new Option("select", "-1")');
				eval('document.forms[0].floorUsage'+(indexval-1)+'.value="-1"');
				eval('document.getElementById("floorConstType'+(indexval-1)+'").disabled=false');
				document.forms[0].constrYear[indexval].disabled = false;
			}			
		}
	}
}

function enableSubmitButton(){
	if(document.getElementById("allChngsCmpltd").checked == true) {
			document.getElementById("Create:Save").disabled = true;
			document.getElementById("Create:Forward").disabled = false;
	} else {
			document.getElementById("Create:Save").disabled = false;
			document.getElementById("Create:Forward").disabled = true;
	}
}
var allChngsCmpltdLabel = '<s:property value="%{getText(\'allChangesDone\')}"/>';
function submitMsg(button) {
	if(document.getElementById("allChngsCmpltd").checked == true) {
		var action = button.value;
		alert("Please uncheck the '" + allChngsCmpltdLabel.slice(0, allChngsCmpltdLabel.length-1) + "' to proceed with " + action);
		return false;
	}
}

function finishAllChangesMsg(button) {
	if(document.getElementById("allChngsCmpltd").checked == false) {
		var action = button.value;
		alert("Please check the '" + allChngsCmpltdLabel.slice(0, allChngsCmpltdLabel.length-1) + "' to proceed with " + action);
		return false;
	}
}  */


function loadDesignationFromMatrix() {
	var e = dom.get('approverDepartment');
	var dept = e.options[e.selectedIndex].text;
		var currentState = dom.get('currentState').value;
		var amountRule="";
	var pendingAction=document.getElementById('pendingActions').value;
	loadDesignationByDeptAndType('PropertyImpl',dept,currentState,amountRule,"",pendingAction); 
}
function populateApprover() {
	getUsersByDesignationAndDept();
}
</script>
</head>
 
  <body onload="loadOnStartUp();">
  
  <div align="left">
  	<s:actionerror/>
  </div>
    <div class="errorcss" id="jsValidationErrors" style="display:none;"></div>

  <s:form name="CreatePropertyForm" action="createProperty-create" theme="simple" validate="true">
  
  <s:push value="model">
  <s:token />
  
  <!-- The mode value is used in floorform.jsp file to stop from remmoving the rent agreement header icon -->
  <s:hidden name="mode" value="form" />
  <div class="formmainbox">
		<div class="headingbg"><s:text name="CreatePropertyHeader"/></div>
		<table width="100%" border="0" cellspacing="0" cellpadding="0">
		<tr>
        	<%@  include file="createPropertyForm.jsp"%>  
        </tr>
        	<% System.out.println("hellllo.111................"); %>
         <tr>
        	<%@ include file="../workflow/commonWorkflowMatrix.jsp"    %>    
        </tr>
        <s:hidden name="modelId" id="modelId" value="%{modelId}" />
      <tr>
        	<!-- <div id="loadingMask" style="display:none" align="center">
        	<p align="center"><img src="/egi/resources/erp2/images/bar_loader.gif"> 
        		<span id="message">
        			<p style="color: red">Please wait....</p>
        		</span>
        	</p>
        	</div> -->
        	<font size="2"><div align="left" class="mandatory1">&nbsp;&nbsp;<s:text name="mandtryFlds"/></div></font>
        </tr>
		<tr>
		<%@ include file="../workflow/commonWorkflowMatrix-button.jsp" %>
		</tr>
		<!--  
		<tr>
		
		
		    <td>
			    <div class="buttonbottom" align="center">		   
					<s:if test="mode=='create'">
					<s:submit value="Forward" name="Forward" id='Create:Forward' cssClass="buttonsubmit" onclick="return onSubmit('createProperty-create.action',this);"/>&nbsp;			
					</s:if>
					<s:if test="mode=='edit'">
					<s:submit value="Forward" name="Forward" id='Create:Forward' cssClass="buttonsubmit" onclick="return onSubmit('createProperty-forward.action',this);"/>&nbsp;
					<s:if test="%{userRole==@org.egov.ptis.constants.PropertyTaxConstants@PTVERIFIER_ROLE}">
								<s:submit value="Approve" name="Approve" id='Create:Approve' cssClass="buttonsubmit" onclick="return onSubmit('createProperty-approve.action',this);"/>&nbsp;	
					</s:if>	
					<s:submit value="Reject" name="Reject" id='Create:Reject' cssClass="buttonsubmit" onclick="return onSubmit('createProperty-reject.action',this);"/>&nbsp;		
					<input type="button" class="button" onclick="window.close();" value="Close">
					</s:if>
				</div>
			</td>
		</tr> -->
		</table>
	</div>
  </s:push>
  </s:form>
  </body>
</html>
