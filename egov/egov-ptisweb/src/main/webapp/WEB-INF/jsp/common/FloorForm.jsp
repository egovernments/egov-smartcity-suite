
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
<%@ include file="/includes/taglibs.jsp"%>
<table width="100%" border="0" cellpadding="0" cellspacing="0"
	class="tablebottom" id="floorDetails">
	<tr>
		<th class="bluebgheadtd"><s:text name="FloorNo" /><span	class="mandatory1">*</span></th>
		<th class="bluebgheadtd"><s:text name="ConstructionType" /><span class="mandatory1" id="constTypeMdtry">*</span><a
			onclick="openWindow('ConstType.jsp');"> <img src="../resources/image/help.gif" style="border: none" /></a></th>
		<th class="bluebgheadtd"><s:text name="Usage" /><span class="mandatory1" id="usageMdtry">*</span><a
			onclick="openWindow('UsageMaster.jsp');"> <img src="../resources/image/help.gif" style="border: none" /></a></th>
		<th class="bluebgheadtd"><s:text name="Occupancy" /><span class="mandatory1" id="occMdtry">*</span></th>
		<th class="bluebgheadtd"><s:text name="Bldgage" /><span	class="mandatory1">*</span></th>
		<th class="bluebgheadtd"><s:text name="constrdate" /><span	class="mandatory1">*</span></th>
		<th class="bluebgheadtd"><s:text name="Width" /></th>
		<th class="bluebgheadtd"><s:text name="Length" /></th>
		<th class="bluebgheadtd"><s:text name="PlinthArea" /><span class="mandatory1">*</span></th>
		<th class="bluebgheadtd"><s:text name="capitalvalue"></s:text></th>
		<th class="bluebgheadtd"><s:text name="planappr" /></th>
		<s:if test="modifyRsn != 'DATA_UPDATE'">
			<th class="bluebgheadtd"><s:text name="Add/Delete" /></th>
		</s:if>
	</tr>
	<s:if test="propertyDetail.floorDetailsProxy.size()==0">
		<tr id="Floorinfo">
			<td class="blueborderfortd" style="padding: 2px 2px">
				<div align="center">
					<s:select headerKey="-10"
						headerValue="%{getText('default.select')}"
						name="propertyDetail.floorDetailsProxy[0].floorNo" listKey="key"
						id="floorNo"
						value="%{propertyDetail.floorDetailsProxy[0].floorNo}"
						listValue="value" list="floorNoMap" cssClass="selectnew"
						cssStyle="width:100%" />
				</div>
			</td>
			<td class="blueborderfortd" style="padding: 2px 2px">
				<div align="center">
					<s:select headerKey="-1" headerValue="%{getText('default.select')}"
						name="propertyDetail.floorDetailsProxy[0].structureClassification.id"
						listKey="id" id="floorConstType"
						value="%{propertyDetail.floorDetailsProxy[0].structureClassification.id}"
						listValue="typeName" list="dropdownData.StructureList"
						cssClass="selectnew" cssStyle="width:100%" />
				</div>
			</td>
			<td class="blueborderfortd" style="padding: 2px 2px">
				<div align="center">
					<%-- 	<egov:ajaxdropdown id="floorUsage" fields="['Text','Value']"
						dropdownId="floorUsage"
						url="common/ajaxCommon!usageByPropType.action"
						afterSuccess="copyDropdown" /> --%>
					<s:select headerKey="-1" headerValue="%{getText('default.select')}"
						name="propertyDetail.floorDetailsProxy[0].propertyUsage.id"
						listKey="id" id="floorUsage"
						value="%{propertyDetail.floorDetailsProxy[0].propertyUsage.id}"
						listValue="usageName" list="dropdownData.UsageList"
						cssClass="selectnew"
						onchange="resetFloorDetailsForResdAndNonResd(this); "
						cssStyle="width:100%" />
				</div>
			</td>
			<td class="blueborderfortd" style="padding: 2px 2px">
				<div align="center">
					<s:select headerKey="-1" headerValue="%{getText('default.select')}"
						name="propertyDetail.floorDetailsProxy[0].propertyOccupation.id"
						listKey="id" id="propOccId"
						value="%{propertyDetail.floorDetailsProxy[0].propertyOccupation.id}"
						listValue="occupation" list="dropdownData.OccupancyList"
						cssClass="selectnew"
						onchange="resetDetailsForTenant(this); toggleDisplayOfRentAgreementFields(this);"
						cssStyle="width:100%" />
				</div>
			</td>

			<td class="blueborderfortd" style="padding: 2px 2px">
				<div align="center">
					<s:select headerKey="-1" headerValue="%{getText('default.select')}"
						name="propertyDetail.floorDetailsProxy[0].depreciationMaster.id"
						id="constrYear" listKey="id" listValue="depreciationName"
						list="dropdownData.AgeFactorList" cssClass="selectnew"
						value="%{propertyDetail.floorDetailsProxy[0].depreciationMaster.id}"
						cssStyle="width:100%" />
				</div>
			</td>
			<td class="blueborderfortd" style="padding: 2px 2px">
				<div align="center">
					<s:textfield
						name="%{propertyDetail.floorDetailsProxy[0].createdDate}"
						id="%{propertyDetail.floorDetailsProxy[0].createdDate}" size="10"
						maxlength="10" cssStyle="width:100%" cssClass="datepicker"></s:textfield>
				</div>
			</td>
			<td class="blueborderfortd" style="padding: 2px 2px">
				<div align="center">
					<s:textfield name="propertyDetail.floorDetailsProxy[0].extraField5"
						id="length" size="5" maxlength="7"
						onblur="trim(this,this.value);checkForTwoDecimals(this,'Length');checkZero(this,'Length');"
						value="%{propertyDetail.floorDetailsProxy[0].extraField5}"
						cssStyle="width:100%" />
				</div>
			</td>

			<td class="blueborderfortd" style="padding: 2px 2px">
				<div align="center">
					<s:textfield name="propertyDetail.floorDetailsProxy[0].extraField4"
						id="width" size="5" maxlength="7"
						onblur="trim(this,this.value);checkForTwoDecimals(this,'Width');checkZero(this,'Width');"
						value="%{propertyDetail.floorDetailsProxy[0].extraField4}"
						cssStyle="width:100%" />
				</div>
			</td>


			<td class="blueborderfortd" style="padding: 2px 2px">
				<div align="center">
					<s:textfield
						name="propertyDetail.floorDetailsProxy[0].builtUpArea.area"
						maxlength="15" size="10" id="assessableArea"
						value="%{propertyDetail.floorDetailsProxy[0].builtUpArea.area}"
						onblur="trim(this,this.value);checkForTwoDecimals(this,'Assessable Area');checkZero(this,'Assessable Area');"
						cssStyle="width:100%" />
				</div>
			</td>


			<td class="blueborderfortd" style="padding: 2px 2px">
				<div align="center">
						<s:textfield
							name="propertyDetail.floorDetailsProxy[0].capitalValue"
							maxlength="100" size="10" id="propertyDetail.floorDetailsProxy[0].capitalValue" value="%{propertyDetail.floorDetailsProxy[0].captialValue}"
							cssStyle="width:100%" />
				</div>
			</td>

			<td class="blueborderfortd" style="padding: 2px 2px">
				<div align="center">
					<s:select name="propertyDetail.floorDetailsProxy[0].planApproved" id="propertyDetail.floorDetailsProxy[0].planApproved" headerValue="Choose"
							headerKey="0" list="#{'true':'Yes','false':'No' }" value="%{propertyDetail.floorDetailsProxy[0].planApproved}"
							cssClass="selectwk">
						</s:select>
				</div>
			</td>


			<s:if test="modifyRsn != 'DATA_UPDATE'">
				<td class="blueborderfortd" id="AddRemoveFloor"><img id="addF"
					name="addF"
					src="${pageContext.request.contextPath}/resources/image/addrow.gif"
					alt="Add" onclick="javascript:addFloor(); return false;" width="18"
					height="18" border="0" /><img id="dDelF" name="dDelF"
					src="${pageContext.request.contextPath}/resources/image/removerow.gif"
					alt="Remove" onclick="javascript:delFloor(this);return false;"
					width="18" height="18" border="0" /></td>

			</s:if>
			<td id="tdAgreementPeriod"><s:hidden id="agreementPeriod"
					name="propertyDetail.floorDetailsProxy[0].rentAgreementDetail.agreementPeriod"
					value="%{propertyDetail.floorDetailsProxy[0].rentAgreementDetail.agreementPeriod}" />
			</td>
			<td id="tdAgreementDate"><s:hidden id="agreementDate"
					name="propertyDetail.floorDetailsProxy[0].rentAgreementDetail.agreementDate"
					value="%{propertyDetail.floorDetailsProxy[0].rentAgreementDetail.agreementDate}" />
			</td>
			<td id="tdIncrementInRent"><s:hidden id="incrementInRent"
					name="propertyDetail.floorDetailsProxy[0].rentAgreementDetail.incrementInRent"
					value="%{propertyDetail.floorDetailsProxy[0].rentAgreementDetail.incrementInRent}" />
			</td>
		</tr>
	</s:if>
	<s:else>
		<s:iterator value="(propertyDetail.floorDetailsProxy.size).{#this}"
			status="floorsstatus">
			<tr id="Floorinfo">

				<td class="blueborderfortd" style="padding: 2px 2px"><s:select
						headerKey="-10" headerValue="%{getText('default.select')}"
						name="propertyDetail.floorDetailsProxy[%{#floorsstatus.index}].floorNo"
						listKey="key" id="floorNo" listValue="value" list="floorNoMap"
						cssClass="selectnew"
						value="%{propertyDetail.floorDetailsProxy[#floorsstatus.index].floorNo}"
						cssStyle="width:100%" /></td>

				<td class="blueborderfortd" style="padding: 2px 2px">
					<div align="center">
						<s:if test="%{#floorsstatus.index == 0}">
							<s:select headerKey="-1"
								headerValue="%{getText('default.select')}"
								name="propertyDetail.floorDetailsProxy[%{#floorsstatus.index}].structureClassification.id"
								listKey="id" id="floorConstType"
								value="%{propertyDetail.floorDetailsProxy[#floorsstatus.index].structureClassification.id}"
								listValue="typeName" list="dropdownData.StructureList"
								cssClass="selectnew" cssStyle="width:100%" />
						</s:if>
						<s:else>
							<s:select headerKey="-1"
								headerValue="%{getText('default.select')}"
								name="propertyDetail.floorDetailsProxy[%{#floorsstatus.index}].structureClassification.id"
								listKey="id" id="floorConstType%{#floorsstatus.index-1}"
								value="%{propertyDetail.floorDetailsProxy[#floorsstatus.index].structureClassification.id}"
								listValue="typeName" list="dropdownData.StructureList"
								cssClass="selectnew" cssStyle="width:100%" />
						</s:else>
					</div>
				</td>

				<td class="blueborderfortd" style="padding: 2px 2px">
					<div align="center">
						<s:if test="%{#floorsstatus.index==0}">
							<%-- 	<egov:ajaxdropdown id="floorUsage" fields="['Text','Value']"
								dropdownId="floorUsage"
								url="common/ajaxCommon!usageByPropType.action"
								afterSuccess="copyDropdown" /> --%>
							<s:select headerKey="-1"
								headerValue="%{getText('default.select')}"
								name="propertyDetail.floorDetailsProxy[%{#floorsstatus.index}].propertyUsage.id"
								listKey="id" id="floorUsage" listValue="usageName"
								list="dropdownData.UsageList" cssClass="selectnew"
								value="%{propertyDetail.floorDetailsProxy[#floorsstatus.index].propertyUsage.id}"
								onchange="resetFloorDetailsForResdAndNonResd(this);"
								cssStyle="width:100%" />
						</s:if>
						<s:else>
							<s:select headerKey="-1"
								headerValue="%{getText('default.select')}"
								name="propertyDetail.floorDetailsProxy[%{#floorsstatus.index}].propertyUsage.id"
								listKey="id" id="floorUsage%{#floorsstatus.index-1}"
								listValue="usageName" list="dropdownData.UsageList"
								cssClass="selectnew"
								value="%{propertyDetail.floorDetailsProxy[#floorsstatus.index].propertyUsage.id}"
								onchange="resetFloorDetailsForResdAndNonResd(this);"
								cssStyle="width:100%" />
						</s:else>
					</div>
				</td>

				<td class="blueborderfortd" style="padding: 2px 2px">
					<div align="center">
						<s:select headerKey="-1"
							headerValue="%{getText('default.select')}"
							name="propertyDetail.floorDetailsProxy[%{#floorsstatus.index}].propertyOccupation.id"
							listKey="id" id="floorOccupation" listValue="occupation"
							list="dropdownData.OccupancyList" cssClass="selectnew"
							value="%{propertyDetail.floorDetailsProxy[#floorsstatus.index].propertyOccupation.id}"
							onchange="resetDetailsForTenant(this); toggleDisplayOfRentAgreementFields(this);"
							cssStyle="width:100%" />
					</div>
				</td>
				<td class="blueborderfortd" style="padding: 2px 2px">
					<div align="center">
						<s:select headerKey="-1"
							headerValue="%{getText('default.select')}"
							name="propertyDetail.floorDetailsProxy[0].depreciationMaster.id"
							id="constrYear" listKey="id" listValue="depreciationName"
							list="dropdownData.AgeFactorList" cssClass="selectnew"
							value="%{propertyDetail.floorDetailsProxy[0].depreciationMaster.id}"
							cssStyle="width:100%" />
					</div>
				</td>
				<td class="blueborderfortd" style="padding: 2px 2px">
					<div align="center">
						<s:textfield
							name="%{propertyDetail.floorDetailsProxy[0].createdDate}"
							id="%{propertyDetail.floorDetailsProxy[0].createdDate}" size="10"
							maxlength="10" cssStyle="width:100%" cssClass="datepicker"></s:textfield>
					</div>
				</td>

				<td class="blueborderfortd" style="padding: 2px 2px">
					<div align="center">
						<s:textfield
							name="propertyDetail.floorDetailsProxy[0].extraField5"
							id="length" size="5" maxlength="7"
							onblur="trim(this,this.value);checkForTwoDecimals(this,'Length');checkZero(this,'Length');"
							value="%{propertyDetail.floorDetailsProxy[0].extraField5}"
							cssStyle="width:100%" />
					</div>
				</td>

				<td class="blueborderfortd" style="padding: 2px 2px">
					<div align="center">
						<s:textfield
							name="propertyDetail.floorDetailsProxy[0].extraField4" id="width"
							size="5" maxlength="7"
							onblur="trim(this,this.value);checkForTwoDecimals(this,'Width');checkZero(this,'Width');"
							value="%{propertyDetail.floorDetailsProxy[0].extraField4}"
							cssStyle="width:100%" />
					</div>
				</td>
				<td class="blueborderfortd" style="padding: 2px 2px">
					<div align="center">
						<s:textfield
							name="propertyDetail.floorDetailsProxy[%{#floorsstatus.index}].builtUpArea.area"
							maxlength="15" size="10" id="assessableArea"
							value="%{propertyDetail.floorDetailsProxy[#floorsstatus.index].builtUpArea.area}"
							onblur="trim(this,this.value);checkForTwoDecimals(this,'Assessable Area');checkZero(this,'Assessable Area');"
							cssStyle="width:100%" />
					</div>
				</td>

				<td class="blueborderfortd" style="padding: 2px 2px">
					<div align="center">
						<s:textfield
							name="propertyDetail.floorDetailsProxy[0].capitalValue"
							maxlength="100" size="10" id="propertyDetail.floorDetailsProxy[0].capitalValue" value="%{propertyDetail.floorDetailsProxy[0].captialValue}"
							cssStyle="width:100%" />
					</div>
				</td>

				<td class="blueborderfortd" style="padding: 2px 2px">
					<div align="center">
						<s:select name="propertyDetail.floorDetailsProxy[0].planApproved" id="propertyDetail.floorDetailsProxy[0].planApproved" headerValue="Choose"
							headerKey="0" list="#{'true':'Yes','false':'No' }" value="%{propertyDetail.floorDetailsProxy[0].planApproved}"
							cssClass="selectwk">
						</s:select>
					</div>
				</td>

				<s:if test="modifyRsn != 'DATA_UPDATE'">
					<td class="blueborderfortd" id="AddRemoveFloor"><img id="addF"
						name="addF"
						src="${pageContext.request.contextPath}/resources/image/addrow.gif"
						alt="Add" onclick="javascript:addFloor(); return false;"
						width="18" height="18" border="0" /><img id="dDelF" name="dDelF"
						src="${pageContext.request.contextPath}/resources/image/removerow.gif"
						alt="Remove" onclick="javascript:delFloor(this);return false;"
						width="18" height="18" border="0" /></td>

					<td><s:hidden id="agreementPeriod"
							name="propertyDetail.floorDetailsProxy[%{#floorsstatus.index}].rentAgreementDetail.agreementPeriod"
							value="%{propertyDetail.floorDetailsProxy[#floorsstatus.index].rentAgreementDetail.agreementPeriod}" />
					</td>
					<td><s:hidden id="agreementDate"
							name="propertyDetail.floorDetailsProxy[%{#floorsstatus.index}].rentAgreementDetail.agreementDate"
							value="%{propertyDetail.floorDetailsProxy[#floorsstatus.index].rentAgreementDetail.agreementDate}" />
					</td>
					<td><s:hidden id="incrementInRent"
							name="propertyDetail.floorDetailsProxy[%{#floorsstatus.index}].rentAgreementDetail.incrementInRent"
							value="%{propertyDetail.floorDetailsProxy[#floorsstatus.index].rentAgreementDetail.incrementInRent}" />
					</td>
				</s:if>
			</tr>
		</s:iterator>
	</s:else>
</table>
<script>

	function checkForPropType(){
		var propType = document.forms[0].propTypeMaster.options[document.forms[0].propTypeMaster.selectedIndex].text;	
		if(propType == "--select--") {
			alert('Please select Property Type before proceeding..!');
			return false;
		}
		return true;
	}
	
	function populateFloorUsages(){
		populatefloorUsage({
			propTypeId: document.getElementById("propTypeMaster").value
		});			
	}

	function populateFloorConstTypesOnValidationErrors() {

		<s:iterator value="(propertyDetail.floorDetailsProxy.size).{#this}" status="floorsstatus">
			constTypeValues[<s:property value="%{#floorsstatus.index}" />] = '<s:property value="%{propertyDetail.floorDetailsProxy[#floorsstatus.index].structureClassification.id}" />';		
		</s:iterator>
		
		var noOfFloors = document.getElementById("floorDetails").rows.length - 1;
		var untyp = "select";
		propTypeDropDown = document.getElementById("propTypeMaster");
		propType = propTypeDropDown.options[propTypeDropDown.selectedIndex].text;
		for (var k = 0; k < noOfFloors; k++) {			
			
			dropdown = null;
			if (noOfFloors == 1) {
				oDate = document.forms[0].occupancyDate;
				dropdown = document.getElementById('floorConstType');
				var utObject = document.forms[0].unitType;
				if (utObject != null) {
					untyp = utObject.options[utObject.selectedIndex].text;	
				}
				 					
			} else {
				oDate = document.forms[0].occupancyDate[k];
				
				dropdown = (k == 0) ? document.getElementById('floorConstType') : document.getElementById('floorConstType'+(k-1));
				var utObject = document.forms[0].unitType[k];
				if (utObject != null) {
					untyp = utObject.options[utObject.selectedIndex].text;
				}
			}								
			if ((propType == 'Mixed' && untyp != 'select' && untyp != 'Open Plot') || propType != 'Mixed') {
				(constTypeValues.length == 0) ? populateDropDown(null) : populateDropDown(constTypeValues[k]); 
			}
		}	
	}
	
	var unitTypeCats = new Array();
	var unitTypeUsages = new Array();
	
	function populateUnitTypeCatAndUsageOnValidationErrors() {
		<s:iterator value="(propertyDetail.floorDetailsProxy.size).{#this}" status="floorsstatus">
			unitTypeCats[<s:property value="%{#floorsstatus.index}" />] = '<s:property value="%{propertyDetail.floorDetailsProxy[#floorsstatus.index].unitTypeCategory}" />';
			unitTypeUsages[<s:property value="%{#floorsstatus.index}" />] = '<s:property value="%{propertyDetail.floorDetailsProxy[#floorsstatus.index].propertyUsage.id}" />';
		</s:iterator>
		
		var floorDetails = document.getElementById("floorDetails");
		var currUnitType = null;
		
		for (var k = 1; k <= floorDetails.rows.length-1; k++) {			
			unitTypeCatDropDown = null;
			usageDropDown = null;
			setUnitTypeCatAndUsageDropDwn(floorDetails.rows[k], true);			
			
			if (navigator.appName == 'Microsoft Internet Explorer') {	
				currUnitType = floorDetails.rows[k].cells[1].childNodes[0].childNodes[0];
			} else {
				currUnitType = floorDetails.rows[k].cells[1].childNodes[1].childNodes[1];
			}
			
			if (currUnitType.options[currUnitType.selectedIndex].text != 'select') {
				populateUnitTypeCatDropDown(currUnitType, unitTypeCats[k-1]);
				populateUnitTypeUsageDropDown(currUnitType, unitTypeUsages[k-1]);
				toggleFields(floorDetails.rows[k], null);	
			}			
		}
		
		areUnitTypeCatsAndUsagePopulated = true;
	}

	var rentAgreementIcon;
	var rentalUnits = 0;
		
	function toggleDisplayOfRentAgreementFields(occupationDropDown) {
		var tenantOccupation = '<s:property value="%{@org.egov.ptis.constants.PropertyTaxConstants@TENANT_OCC}" />';
		var row = occupationDropDown.parentNode.parentNode.parentNode;
		var floorTable = document.getElementById('floorDetails');
		var noOfFloors = floorTable.rows.length - 1;
		var tableCell = row.cells[row.cells.length - 4];

		if (occupationDropDown.options[occupationDropDown.selectedIndex].text == tenantOccupation) {
			jQuery(floorTable.rows[0].lastElementChild).show();
			jQuery(tableCell).show();			
			rentalUnits += 1;
		} 
	}

	/* jQuery(function () {
		var mode = '<s:property value="%{mode}" />';
		var isTenantFloorPresent = '<s:property value="%{isTenantFloorPresent}" />';
		var noOfFloors = document.getElementById('floorDetails').rows.length - 1;
		if ( (mode != "view" && isTenantFloorPresent == "false") || (mode == "" && isTenantFloorPresent == "") ) {
			jQuery(rentAgrmntHeader).hide();
			if (noOfFloors == 1) {
				jQuery(rentAgrmntIconCell).hide();
			}
		}		
	}) */
	
	function handleRentIconClick(obj) {
		if (setFloorEffectiveDate(obj) == false) {
			return false;
		} 
		openRentAgreementWindow(obj, 'form');
		return true;
	}
	
</script>
