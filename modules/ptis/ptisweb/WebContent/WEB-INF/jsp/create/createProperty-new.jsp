<%@ page language="java" pageEncoding="UTF-8"%>
<%@ include file="/includes/taglibs.jsp" %>

<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />

<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN">
<html>
  <head>
    <title><s:text name='NewProp.title'/></title>
<sx:head/>
<script type="text/javascript">
function loadOnStartUp() {
	document.getElementById("plotArea").style.display = "";
	document.getElementById("undivArea").style.display = "none";		
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
	document.getElementById("waterRate").style.display = "none";
	
	enableFieldsForPropType();
	hideAddRmvBtnForResidFlats();
	enableCorresAddr();
	enableTaxExemptReason();
	makeMandatory();
	enableRentBox();
			
	var complDateStr = document.getElementById("dateOfCompletion").value;
	if(complDateStr == "" || complDateStr == "DD/MM/YYYY" || complDateStr == undefined)
	{		
		waterMarkInitialize('dateOfCompletion','DD/MM/YYYY');
	}
	var tbl = document.getElementById('floorDetails');	
	if(tbl!=null) {
		resetDetailsForTenantOnload();
	}
	
	populateLocationFactors();	
	populateFloorConstTypeDropDowns();
	toggleForResNonRes();	
	toggleFloorDetails();
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

	//enableSubmitButton();
}

var areUnitTypeCatsAndUsagePopulated = false;
function doOnValidationErrors() {	

	if (!areUnitTypeCatsAndUsagePopulated && isCategoriesPrepared && isUsagesPrepared) {
		populateUnitTypeCatAndUsageOnValidationErrors();	
	} 
}

function resetFloorDetailsForResdAndNonResd(obj) {
	var propType = document.forms[0].propTypeMaster.options[document.forms[0].propTypeMaster.selectedIndex].text;
	var rowIndex = getRow(obj).rowIndex;
	var tbl = document.getElementById('floorDetails');
	if (tbl != null && propType == 'Residential & Non-Residential') {
		var rowo = tbl.rows;
		var RsdUsg = '<s:property value="@org.egov.ptis.nmc.constants.NMCPTISConstants@USAGES_FOR_RESD"/>';
		var RsdUsgArray = RsdUsg.split(", "); 
		var NonRsdUsg = '<s:property value="@org.egov.ptis.nmc.constants.NMCPTISConstants@USAGES_FOR_NON_RESD"/>';
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
		var RsdUsg = '<s:property value="@org.egov.ptis.nmc.constants.NMCPTISConstants@USAGES_FOR_RESD"/>';
		var RsdUsgArray = RsdUsg.split(", "); 
		var NonRsdUsg = '<s:property value="@org.egov.ptis.nmc.constants.NMCPTISConstants@USAGES_FOR_NON_RESD"/>';
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

function submitMsg() {
	if(document.getElementById("allChngsCmpltd").checked == true) {
		alert("Please uncheck the Do you finish all changes checkbox to proceed with Submit");
		return false;
	}
}

function forwardMsg() {
	if(document.getElementById("allChngsCmpltd").checked == false) {
		alert("Please check the Do you finish all changes checkbox to proceed with Forward");
		return false;
	}
}
</script>
</head>
  
  <body onload="loadOnStartUp();">
  
  <div align="left">
  	<s:actionerror/>
  </div>
<div class="errorstyle" id="property_error_area" style="display:none;"></div>

  <s:form name="CreatePropertyForm" action="createProperty" theme="simple" validate="true">
  <s:push value="model">
  <s:token />
  <div class="formmainbox">
  <div class="formheading"></div>
		<div class="headingbg"><s:text name="CreatePropertyHeader"/></div>
		<table width="100%" border="0" cellspacing="0" cellpadding="0">
		<tr>
        	<%@ include file="createPropertyForm.jsp"%>  
        </tr>
        <tr>
        	<%@ include file="../workflow/property-workflow.jsp" %>
        </tr>
        <s:hidden name="modelId" id="modelId" value="%{modelId}" />
        <tr>
        	<font size="2"><div align="left" class="mandatory"><s:text name="mandtryFlds"/></div></font>
        </tr>
		<tr>
		    <div class="buttonbottom" align="center">
		    <s:if test="%{userRole==@org.egov.ptis.nmc.constants.NMCPTISConstants@PTCREATOR_ROLE}">
		    	<td><s:submit value="Forward" name="Forward" id="Create:Forward"  method="forward" cssClass="buttonsubmit" onclick="setWorkFlowInfo(this);return forwardMsg();"/></td>
		    	<td><s:submit value="Submit" name="Save" id="Create:Save"  method="create" cssClass="buttonsubmit" onclick="setWorkFlowInfo(this);return submitMsg();"/></td>
		    </s:if>
		    	<td><input type="button" name="button2" id="button2" value="Close" class="button" onclick="return confirmClose();"></td>		    	
		    </div>
		</tr>                  
		</table>
	</div>
  </s:push>
  </s:form>
  </body>
</html>