<%@ page language="java" pageEncoding="UTF-8"%>
<%@ include file="/includes/taglibs.jsp"%>
<table width="100%" border="0" cellspacing="0" cellpadding="0">
	<tr>
		<td class="greybox" width="5%">&nbsp;</td>
		<td class="greybox" width=""><s:text name="rsnForCreatin" /> <span
			class="mandatory1">*</span> :</td>
		<td class="greybox" width=""><s:select headerKey="-1"
				headerValue="%{getText('default.select')}" name="mutationId"
				id="mutationId" listKey="idMutation" listValue="mutationName"
				list="dropdownData.MutationList" value="%{mutationId}"
				cssClass="selectnew" onchange="makeMandatory();" />
		</td>
		<td class="greybox" width=""><s:text name="prntPropIndexNum" />
			<span class="mandatory1" id="prntMandatory">*</span> :</td>
		<td class="greybox"><s:textfield name="parentIndex"
				id="parentIndex" size="12" maxlength="12"></s:textfield>
		</td>

	</tr>
	<tr>
		<td class="bluebox2" width="5%">&nbsp;</td>
		<td class="bluebox" width=""><s:text name="Zone" /> <span
			class="mandatory1">*</span> :</td>
		<td class="bluebox" width=""><s:select headerKey="-1"
				headerValue="%{getText('default.select')}" name="zoneId" id="zoneId"
				listKey="key" listValue="value" list="ZoneBndryMap"
				cssClass="selectnew" onchange="return populateWard();"
				value="%{zoneId}" />
		</td>
		<td class="bluebox" colspan="2">&nbsp;</td>
	</tr>
	<tr>
		<td class="greybox" width="5%">&nbsp;&nbsp;&nbsp;</td>

		<egov:ajaxdropdown id="wardId" fields="['Text','Value']"
			dropdownId="wardId" url="common/ajaxCommon!wardByZone.action" />
		<td class="greybox" width=""><s:text name="Ward" /> <span
			class="mandatory1">*</span> :</td>
		<td class="greybox" width=""><s:select name="wardId" id="wardId"
				list="dropdownData.wardList" listKey="id" listValue="name"
				headerKey="-1" headerValue="%{getText('default.select')}"
				onchange="populateArea(); populateLocationFactors();" value="%{wardId}" />
		</td>
		<td class="greybox" colspan="2">&nbsp;</td>
	</tr>
	<tr>
		<td class="bluebox2" width="5%">&nbsp;&nbsp;&nbsp;</td>
		<td class="bluebox" width=""><s:text name="partNo" /> <span
			class="mandatory1">*</span> :</td>
		<td class="bluebox" width="">
			<s:textfield id="partNo" name="partNo" value="%{partNo}" maxlength="12"/>
		</td>
		<td class="bluebox" colspan="2">&nbsp;</td>
	</tr>
	<tr>
		<td colspan="5">
			<div id="OwnerNameDiv">
				<%@ include file="../common/OwnerNameForm.jsp"%>
			</div>
		</td>
	</tr>
	<tr>
		<td class="bluebox2">&nbsp;</td>
		<td class="bluebox"><s:text name="MobileNumber" /> :</td>
		<td class="bluebox">
			<div>
				+91
				<s:textfield name="mobileNo" maxlength="10"
					onblur="validNumber(this);checkZero(this,'Mobile Number');" />
			</div>
		</td>
		<td class="bluebox"><s:text name="EmailAddress" /> :</td>
		<td class="bluebox"><s:textfield name="email" maxlength="64"
				onblur="trim(this,this.value);validateEmail(this);" />
		</td>
	</tr>
	<tr>
		<td>
			<div id="PropAddrDiv">
				<%@ include file="../common/PropAddressForm.jsp"%>
			</div>
		</td>
	</tr>
	<tr>
		<td class="bluebox2">&nbsp;</td>
		<td class="bluebox"><s:text name="ParcelID" /> <span
			class="mandatory1">*</span> :</td>
		<td class="bluebox"><s:textfield name="parcelID" maxlength="50"/>
		</td>
		<td class="bluebox">&nbsp;</td>
		<td class="bluebox">&nbsp;</td>
	</tr>
	<tr>
		<td>
			<div id="CorrAddrDiv">
				<%@ include file="../common/CorrAddressForm.jsp"%>
			</div>
		</td>
	</tr>
	<tr>
		<td colspan="5">
			<div class="headingsmallbg">
				<span class="bold"><s:text name="PropBoundedBy" /> </span>
			</div>
		</td>
	</tr>
	<tr>
		<td class="greybox2">&nbsp;</td>
		<td class="greybox"><s:text name="NorthWard" /> :</td>
		<td class="greybox"><s:textfield name="northBound"
				value="%{northBound}" maxlength="126"></s:textfield>
		</td>
		<td class="greybox"><s:text name="SouthWard" /> :</td>
		<td class="greybox"><s:textfield name="southBound"
				value="%{southBound}" maxlength="126"></s:textfield>
		</td>
	</tr>
	<tr>
		<td class="bluebox2">&nbsp;</td>
		<td class="bluebox"><s:text name="EastWard" /> :</td>
		<td class="bluebox"><s:textfield name="eastBound"
				value="%{eastBound}" maxlength="126"></s:textfield>
		</td>
		<td class="bluebox"><s:text name="WestWard" /> :</td>
		<td class="bluebox"><s:textfield name="westBound"
				value="%{westBound}" maxlength="126"></s:textfield>
		</td>
	</tr>
	<tr>
		<td colspan="5">
			<div class="headingsmallbg">
				<span class="bold"><s:text name="PropertyType" /> </span>
			</div>
		</td>
	</tr>
	<tr>
		<td class="bluebox2">&nbsp;</td>
		<td class="bluebox"><s:text name="PropertyType" /> <span
			class="mandatory1">*</span> :</td>
		<td class="bluebox"><s:select name="propTypeId"
				id="propTypeMaster" list="dropdownData.PropTypeMaster" listKey="id"
				listValue="type" headerKey="-1"
				headerValue="%{getText('default.select')}" value="%{propTypeId}"
				onchange="enableFieldsForPropType(); populateUsg(); populatePropTypeCategory(); toggleForResNonRes();toggleFloorDetails(); " />
		</td>
		<td class="bluebox">&nbsp;</td>
		<td class="bluebox">&nbsp;</td>
	</tr>
	<tr id="propTypeCategoryRow">
		<td class="greybox2">&nbsp;</td>
		<td class="greybox"><s:text name="PropertyTypeCategory" /> <span
			class="mandatory1">*</span> :</td>
		<egov:ajaxdropdown id="propTypeCategoryId" fields="['Text','Value']"
			dropdownId="propTypeCategoryId"
			url="common/ajaxCommon!propTypeCategoryByPropType.action" />
		<td class="greybox"><s:select name="propertyDetail.extra_field5"
				id="propTypeCategoryId" list="propTypeCategoryMap" listKey="key"
				listValue="value" headerKey="-1"
				headerValue="%{getText('default.select')}"
				value="%{propertyDetail.extra_field5}"
				onchange="hideAddRmvBtnForResidFlats(); toggleForResNonRes();" />
		</td>
		<td class="greybox" colspan="2">&nbsp;</td>
	</tr>
	<tr id="floorDetailsConfirm">
		<td class="bluebox2" width="8%">&nbsp;</td>
      	<td class="bluebox" colspan="3"><span class="bold"><s:text name="floorDetailsConfirm"/></span>
			<s:checkbox name="isfloorDetailsRequired" id="isfloorDetailsRequired" onclick="toggleFloorDetails();resetGovtFloorDtls();"/>
		</td>
      	<td class="bluebox" width="20%">&nbsp;</td>	
	</tr>
	<tr id="docRow">
		<td class="bluebox2">&nbsp;</td>
		<td class="bluebox"><s:text name="OccupationDate" /> <span
			class="mandatory1">*</span> :</td>
		<td class="bluebox"><s:date name="dateOfCompletion"
				var="docFormat" format="dd/MM/yyyy" /> <s:textfield
				name="dateOfCompletion" id="dateOfCompletion" maxlength="10"
				value="%{dateOfCompletion}"
				onkeyup="DateFormat(this,this.value,event,false,'3')"
				onfocus="waterMarkTextIn('dateOfCompletion','DD/MM/YYYY');"
				onblur="validateDateFormat(this);waterMarkTextOut('dateOfCompletion','DD/MM/YYYY');" />
		</td>
		<td class="bluebox" colspan="2">&nbsp;</td>
	</tr>
	<tr>
		<td class="bluebox2">&nbsp;</td>
		<td class="bluebox">
			<div id="plotArea">
				<s:text name="PlotArea"/>
				<span class="mandatory1">*</span> :
			</div>
			<div id="undivArea">
				<s:text name="undivArea"/>
				<span class="mandatory1">*</span> :
			</div>
		</td>
		<td class="bluebox" colspan="2"><s:textfield name="areaOfPlot"
				maxlength="15"
				onblur="trim(this,this.value);checkForTwoDecimals(this,'Area Of Plot');checkZero(this,'Area Of Plot');" />
			<span class="highlight2"><s:text
					name="msgForCompulsionOfOpenPlot" /> </span>
		</td>
		<td class="bluebox">&nbsp;</td>
	</tr>
	<tr id="nonResPlotAreaRow">
		<td class="greybox2">&nbsp;</td>
		<td class="greybox">
				<s:text name="nonResPlotArea"/>
				<span class="mandatory1">*</span> :
		</td>
		<td class="greybox" colspan="2"><s:textfield name="nonResPlotArea"
				id="nonResPlotArea" maxlength="15"
				onblur="trim(this,this.value);checkForTwoDecimals(this,'Commercial Area Of Plot');checkZero(this,'Commercial Area Of Plot');" />
		</td>
		<td class="greybox">&nbsp;</td>
	</tr>
	<tr id="taxExemptRow">
		<td class="greybox2">&nbsp;</td>		
		<td class="greybox"><s:text name="ExemptedFromTax" /> :</td>
		<td class="greybox">
			<s:checkbox name="isExemptedFromTax"
				id="chkIsTaxExempted" onclick="enableTaxExemptReason()"></s:checkbox>
		</td>
		<td class="greybox"><s:text name="TaxExmRsn" /> :</td>
		<td class="greybox">
			<s:select
				headerValue="%{getText('default.select')}" headerKey="-1"
				name="taxExemptReason" id="taxExemptReason"
				list="dropdownData.taxExemptedList" cssClass="selectnew" />
		</td>
	</tr>
	<tr>
		<td class="greybox2">&nbsp;</td>
		<td class="greybox">
			<s:text name="locationFactor" /> 
			<span class="mandatory1">*</span> :
		</td>
		<egov:ajaxdropdown fields="['Text', 'Value']" url="common/ajaxCommon!locationFactorsByWard.action" 
			dropdownId="locationFactor" id="locationFactor" afterSuccess="setLocationFactor"/>
		<td class="greybox">
			<s:select name="propertyDetail.extra_field6"
				id="locationFactor" list="dropdownData.LocationFactorList" listKey="key"
				listValue="value" headerKey="-1"
				headerValue="%{getText('default.select')}"
				value="%{propertyDetail.extra_field6}" />
		</td>
		<td class="greybox">&nbsp;</td>
		<td class="greybox">&nbsp;</td>
	</tr>	
	<tr id="waterRate">
		<td class="bluebox2">&nbsp;</td>
		<td class="bluebox"><s:text name="GenWaterRate" /> <span
			class="mandatory1">*</span> :</td>
		<td class="bluebox" width="3%">
			<s:select name="propertyDetail.extra_field1"
				id="genWaterRate" list="waterMeterMap" listKey="key"
				listValue="value" headerKey="-1"
				headerValue="%{getText('default.select')}"
				value="%{propertyDetail.extra_field1}" 
				cssStyle="width:120px" />
		</td>
		<td class="bluebox" colspan="2">&nbsp;</td>
	</tr>
	<tr id="usageRow">
		<td class="greybox2">&nbsp;</td>
		<td class="greybox"><s:text name="Usage" /> <span
			class="mandatory1">*</span><a
			onclick="openWindow('UsageMaster.jsp');"> <img
				src="../image/help.gif" style="border: none" /> </a> :</td>
		<egov:ajaxdropdown id="usage" fields="['Text','Value']"
			dropdownId="usage" url="common/ajaxCommon!usageByPropType.action" />
		<td class="greybox"><s:select headerKey="-1"
				headerValue="%{getText('default.select')}" name="propUsageId"
				listKey="id" id="usage" listValue="usageName"
				list="dropdownData.UsageList" cssClass="selectnew"
				value="%{propUsageId}" />
		</td>
		<td class="greybox">&nbsp;</td>
		<td class="greybox">&nbsp;</td>
	</tr>
	<tr id="occupancyRow">
		<td class="bluebox2">&nbsp;</td>
		<td class="bluebox"><s:text name="Occupancy" /> <span
			class="mandatory1">*</span> :</td>
		<td class="bluebox"><s:select headerKey="-1"
				headerValue="%{getText('default.select')}" name="propOccId"
				listKey="id" id="occupation" onchange="enableRentBox();"
				listValue="occupation" list="dropdownData.OccupancyList"
				cssClass="selectnew" value="%{propOccId}" />
		</td>
		<td class="bluebox">&nbsp;</td>
		<td class="bluebox">&nbsp;</td>
	</tr>
	<tr id="rentRow">
		<td class="greybox2">&nbsp;</td>
		<td class="greybox"><s:text name="rent" /> <span
			class="mandatory1" id="rentBoxMandatory">*</span>:</td>
		<td class="greybox"><s:textfield id="rentBox"
				name="propertyDetail.extra_field2" size="10"
				value="%{propertyDetail.extra_field2}">
			</s:textfield>
		</td>
		<td class="greybox">&nbsp;</td>
		<td class="greybox">&nbsp;</td>
	</tr>

	<tr id="buildingcostRow">
		<td class="greybox2" width="5%">&nbsp;</td>
		<td class="greybox"><s:text name="bldngCost" /> <span
			class="mandatory1" id="bldngCostMandatory">*</span> :</td>
		<td class="greybox"><s:textfield id="bldngCostId"
				name="propertyDetail.extra_field3"
				value="%{propertyDetail.extra_field3}" size="10" maxlength="10"
				onblur="validateInteger(this);">
			</s:textfield>
		</td>
		<td class="greybox">&nbsp;</td>
		<td class="greybox">&nbsp;</td>

	</tr>
	<tr id="amenitiesRow">
		<td class="bluebox2" width="5%">&nbsp;</td>
		<td class="bluebox"><s:text name="amenities" /> :</td>
		<td class="bluebox"><s:select headerKey="-1"
				headerValue="%{getText('default.select')}" id="amenitiesId"
				name="propertyDetail.extra_field4" listKey="key" listValue="value"
				list="amenitiesMap" cssClass="selectnew"
				value="%{propertyDetail.extra_field4}" />
		</td>
		<td class="bluebox">&nbsp;</td>
		<td class="bluebox">&nbsp;</td>

	</tr>
	<tr id="openPlotALV">
		<td class="bluebox2" width="5%">&nbsp;</td>
		<td class="bluebox"><s:text name="openPLotManualAlv" />  :</td>
		<td class="bluebox"><s:textfield id="opAlvId"
				name="propertyDetail.manualAlv"
				value="%{propertyDetail.manualAlv}" size="10" maxlength="10"
				onblur="validateInteger(this);">
			</s:textfield>
		</td>
		<td class="bluebox">&nbsp;</td>
		<td class="bluebox">&nbsp;</td>

	</tr>
	<tr id="nameOfOccupier">
		<td class="greybox2" width="5%">&nbsp;</td>
		<td class="greybox"><s:text name="OccupierName" />  :</td>
		<td class="greybox"><s:textfield id="occId"
				name="propertyDetail.occupierName"
				value="%{propertyDetail.occupierName}" size="15" maxlength="32">
			</s:textfield>
		</td>
		<td class="greybox">&nbsp;</td>
		<td class="greybox">&nbsp;</td>

	</tr>
	<tr>
		<td class="bluebox2">&nbsp;</td>
		<td class="bluebox"><s:text name="AuthProp" /> <span
			class="mandatory1">*</span> :</td>
		<td class="bluebox"><s:radio name="isAuthProp"
				list="dropdownData.AuthPropList" />
		</td>
		<td class="bluebox">&nbsp;</td>
		<td class="bluebox">&nbsp;</td>
	</tr>
	<tr>
		<td class="greybox2">&nbsp;</td>
		<td class="greybox"><s:text name="NoticeGenConfirm" /> <span
			class="mandatory1">*</span> :</td>
		<td class="greybox"><s:radio name="extra_field2"
				list="dropdownData.NoticeTypeList" />
		</td>
		<td class="greybox">&nbsp;</td>
		<td class="greybox">&nbsp;</td>
	</tr>
	<tr>
		<td class="bluebox2">&nbsp;</td>
		<td class="bluebox">Upload Document</td>
		<td class="bluebox"><input type="button" class="button"
			value="Upload Document" id="docUploadButton"
			onclick="showDocumentManager();" /> <s:hidden name="docNumber"
				id="docNumber" /></td>
		<td class="bluebox" colspan="2">&nbsp;</td>
	</tr>
	<tr id="floorHeaderRow">
		<td colspan="5">
			<div class="headingsmallbg">
				<table width="50%" border="0" align="center" cellpadding="0"
					cellspacing="0">
					<tr id="floorHeader">
						<td colspan="5" width="40%">
							<div class="headingsmallbg">
								<span class="bold"><s:text name="FloorDetailsHeader" />
								</span>
							</div>
						</td>
					</tr>
				</table>
			</div>
		</td>
	</tr>	
	<tr>
		<td colspan="5">
			<div align="center">
				<%@ include file="../common/FloorForm.jsp"%>
			</div>
		</td>
	</tr>
	<tr>
		<td class="greybox">&nbsp;</td>
		<td class="greybox" colspan="2">
			<span class="bold"><s:text name="allChangesDone"/></span>
			<s:checkbox id="allChngsCmpltd" name="allChangesCompleted"/>
		</td>
		<td class="greybox" colspan="2">&nbsp;</td>
	</tr>

</table>
<script type="text/javascript">
	function showDocumentManager() {
			var docNum = document.getElementById("docNumber").value;
			var url;
			if (docNum == null || docNum == '' || docNum == 'To be assigned') {
				url = "/egi/docmgmt/basicDocumentManager.action?moduleName=ptis";
			} else {
				url = "/egi/docmgmt/basicDocumentManager!editDocument.action?docNumber="
						+ docNum + "&moduleName=ptis";
			}
			window.open(url, 'docupload', 'width=1000,height=400');
		}
		
	function populateWard() {
		populatewardId({
			zoneId : document.getElementById("zoneId").value
		});
		document.getElementById("areaId").options.length = 0;
		document.getElementById("areaId").value = "-1";
	}
	function populateArea() {
		populateareaId({
			wardId : document.getElementById("wardId").value
		});
	}
	function populatePropTypeCategory() {
		populatepropTypeCategoryId({
			propTypeId : document.getElementById("propTypeMaster").value
		});
	}
	function populateLocationFactors() {
		populatelocationFactor({
			wardId : document.getElementById("wardId").value
		});
	}
	function setLocationFactor() {
		<s:if test="%{propertyDetail.extra_field6 != null}">
			document.getElementById('locationFactor').value = <s:property value="%{propertyDetail.extra_field6}"/>;			
		</s:if>
	}
</script>
