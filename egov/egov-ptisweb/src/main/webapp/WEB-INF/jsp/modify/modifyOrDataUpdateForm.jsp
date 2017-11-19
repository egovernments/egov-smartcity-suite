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
<%@ include file="/includes/taglibs.jsp"%>
<%@ taglib uri="/WEB-INF/taglib/cdn.tld" prefix="cdn"%>
<link rel="stylesheet" href="<cdn:url value='/resources/global/css/font-icons/font-awesome/css/font-awesome.min.css' context='/egi'/>">
<table width="100%" border="0" cellspacing="0" cellpadding="0">
	<tr>
		<td class="bluebox2" width="5%">
			&nbsp;
		</td>
		<td class="bluebox" width="20%">
			<s:text name="prop.Id" />
			:
		</td>
		<td class="bluebox" width="15%">
			<span class="bold">
				<s:property default="N/A" value="%{basicProp.upicNo}" />				
			</span>									
		</td>
		<td class="bluebox" width="20%">
			&nbsp;
		</td>
		<td class="bluebox" width="20%">
			&nbsp;
		</td>
	</tr>
	<tr>
		<td class="greybox2" width="5%">
			&nbsp;
		</td>
		<td class="greybox" width="20%">
			<s:text name="Zone" />
			:
		</td>
		<td class="greybox" width="15%">
			<span class="bold"><s:property value="%{basicProp.boundary.parent.boundaryNum}"/>-<s:property default="N/A"
					value="%{basicProp.boundary.parent.name}" /> </span>
		</td>
		<td class="greybox" colspan="2">&nbsp;</td>
		
	</tr>
	<tr>
		<td class="bluebox2">&nbsp;</td>
		<td class="bluebox" width="20%">
			<s:text name="Ward" />
			:
		</td>
		<td class="bluebox" width="20%">
			<span class="bold"><s:property value="%{basicProp.boundary.boundaryNum}"/>-<s:property default="N/A"
					value="%{basicProp.boundary.name}" /> </span>
		</td>
		<td class="bluebox" colspan="2">&nbsp;</td>
	</tr>
	<tr>
		<td class="greybox2">
			&nbsp;
		</td>
		<td class="greybox">
			<s:text name="OwnerName" />
			:
		</td>
		<td class="greybox">
			<span class="bold"><s:property default="N/A"
					value="%{ownerName}" /> </span>
		</td>
		<td class="greybox">
			&nbsp;
		</td>
		<td class="greybox">
			&nbsp;
		</td>
	</tr>
	<tr>
		<td class="bluebox2">
			&nbsp;
		</td>
		<td class="bluebox">
			<s:text name="MobileNumber" />
			:
		</td>
		<td class="bluebox">
			<div>
				+91
				<s:textfield name="propertyAddr.mobileNo" maxlength="10"
					onblur="validNumber(this);checkZero(this,'Mobile Number');"
					value="%{propertyAddr.mobileNo}" />
			</div>
		</td>
		<td class="bluebox">
			<s:text name="EmailAddress" />
			:
		</td>
		<td class="bluebox">
			<s:textfield name="propertyAddr.emailAddress" maxlength="64"
				onblur="trim(this,this.value);validateEmail(this);"
				value="%{propertyAddr.emailAddress}" /> 
		</td>
	</tr>
	<tr>
		<td class="greybox2">
			&nbsp;
		</td>
		<td class="greybox">
			<s:text name="Area" />
			:
		</td>
		<td class="greybox">
			<s:select name="areaId" id="areaId" list="dropdownData.areaList"
				listKey="id" listValue="name" headerKey="-1"
				headerValue="%{getText('default.select')}" value="%{areaId}" />
		</td>
		<td class="greybox" colspan="2">
			&nbsp;
		</td>
	</tr>

	<!-- 
	   New fields added as part of assessment data update
	   
	   New fields start here
	
	 -->	 	 
	 <tr>
		<td colspan="5"><div class="headingsmallbg"><span class="bold"><s:text name="PropertyAddress"/></span></div></td>
	 </tr>
	 <tr>
		<td class="bluebox2" width="8%">&nbsp;</td>
	    <td class="bluebox" width="8%">
	    	<s:text name="HouseNo"/><span class="mandatory1">*</span> : 
	    </td>
	    <td class="bluebox">
	    	<s:textfield name="propertyAddr.houseNo" value="%{propertyAddr.houseNo}"
				maxlength="50" onblur="checkHouseNoStartsWithNo(this); validatePlotNo(this,'Plot No/House No');" />
		</td>
	    <td class="bluebox" width="10%">
	    	<s:text name="OldNo"/> : </td>
	    <td class="bluebox">
	    	<s:textfield name="propertyAddr.doorNumOld" value="%{propertyAddr.doorNumOld}"
				maxlength="50" onblur="validatePlotNo(this,'Old No');" />
		</td>
	</tr>
	<tr>
		<td class="greybox2" width="8%">&nbsp;</td>
	    <td class="greybox" width="8%">
	    	<s:text name="Address"/> : 
	    </td>
	    <td class="greybox">
	    	<s:textfield name="propertyAddr.streetAddress1"
				value="%{propertyAddr.streetAddress1}" maxlength="512"
				onblur="validateAddress(this);" />
		</td>
	    <td class="greybox" width="10%">
	    	<s:text name="PinCode"/> : 
	    </td>
	    <td class="greybox">
	    	<s:textfield name="propertyAddr.pinCode" value="%{propertyAddr.pinCode}"
				onchange="trim(this,this.value);" maxlength="6"
				onblur="validNumber(this);checkZero(this);" />
		</td>
	</tr>
	
	<tr>
		<td class="bluebox2" width="8%">&nbsp;</td>
	    <td class="bluebox" width="8%">
	    	<s:text name="address.khasraNumber"/> : 
	    </td>
	    <td class="bluebox">
	    	<s:textfield id="khasraNumber" name="propertyAddr.extraField1"
				value="%{propertyAddr.extraField1}" maxlength="128" />
		</td>
	    <td class="bluebox" width="10%">
	    	<s:text name="address.Mauza"/> : 
	    </td>
		<td class="bluebox"><s:textfield id="mauza" name="propertyAddr.extraField2"
				value="%{propertyAddr.extraField2}" maxlength="128" /></td>
	</tr>
	
	<tr>
		<td class="greybox2" width="8%">&nbsp;</td>
	    <td class="greybox" width="8%">
	    	<s:text name="address.citySurveyNumber"/> : 
	    </td>
	    <td class="greybox">
	    	<s:textfield id="citySurveyNumber"
				name="propertyAddr.extraField3"
				value="%{propertyAddr.extraField3}" maxlength="128" />
		</td>
	    <td class="greybox" width="10%">
	    	<s:text name="address.sheetNumber"/> : 
	    </td>
	    <td class="greybox">
	    	<s:textfield id="sheetNumber" name="propertyAddr.extraField4"
				value="%{propertyAddr.extraField4}" maxlength="128" />
		</td>
	</tr>
	 
	 <!-- New fields end here -->
	 
	<tr>
		<td class="bluebox2">
			&nbsp;
		</td>
		<td class="bluebox">
			<s:text name="ParcelID" /><span class="mandatory1">*</span> :
		</td>
		<td class="bluebox">
			<s:textfield name="parcelId" value="%{parcelId}" maxlength="50"/>
		</td>
		<td class="bluebox">
			&nbsp;
		</td>
		<td class="bluebox">
			&nbsp;
		</td>
	</tr>		
	<tr>
		<td class="bluebox2" width="5%">
			&nbsp;
		</td>
		<td class="bluebox" width="10%">
			<s:text name="CorrAddr" />
			:
		</td>
		<td class="bluebox" width="15%">
			<span class="bold"><s:property default="N/A"
					value="%{corrsAddress}" /> </span>
		</td>
		<td class="bluebox" colspan="2" width="20%">
			&nbsp;
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
		<td class="greybox2">
			&nbsp;
		</td>
		<td class="greybox">
			<s:text name="PropertyType" />
			<span class="mandatory1">*</span> :
		</td>
		<td class="greybox">
			<s:select name="propTypeId" id="propTypeMaster"
				list="dropdownData.PropTypeMaster" listKey="id" listValue="type"
				headerKey="-1" headerValue="%{getText('default.select')}"
				value="%{propTypeId}"
				onchange="enableFieldsForPropType(); populateUsg(); populatePropTypeCategory();toggleFloorDetails(); " />
		</td>
		<td class="greybox">
			&nbsp;
		</td>
		<td class="greybox">
			&nbsp;
		</td>
	</tr>
	<tr id="floorDetailsConfirm">
		<td class="bluebox2" width="8%">&nbsp;</td>
      	<td class="bluebox" colspan="3"><span class="bold"><s:text name="floorDetailsConfirm"/></span>
			<s:checkbox name="isfloorDetailsRequired" id="isfloorDetailsRequired" onclick="toggleFloorDetails();resetGovtFloorDtls();"/>
		</td>
      	<td class="bluebox" width="20%">&nbsp;</td>	
	</tr>
	<tr id="docRow">
		<td class="bluebox2">
			&nbsp;
		</td>
		<td class="bluebox">
			<s:text name="OccupationDate" />
			<span class="mandatory1">*</span> :
		</td>
		<td class="bluebox">
			<s:textfield name="dateOfCompletion" id="dateOfCompletion"
				maxlength="10" value="%{dateOfCompletion}"
				onkeyup="DateFormat(this,this.value,event,false,'3')"
				onfocus="waterMarkTextIn('dateOfCompletion','DD/MM/YYYY');"
				onblur="validateDateFormat(this);waterMarkTextOut('dateOfCompletion','DD/MM/YYYY');" />
		</td>
		<td class="bluebox">
			&nbsp;
		</td>
		<td class="bluebox">
			&nbsp;
		</td>
	</tr>
	<tr id="propTypeCategoryRow">
		<td class="bluebox2">
			&nbsp;
		</td>
		<td class="bluebox">
			<s:text name="PropertyTypeCategory" />
			<span class="mandatory1">*</span> :
		</td>
		<egov:ajaxdropdown id="propTypeCategoryId" fields="['Text','Value']"
			dropdownId="propTypeCategoryId"
			url="common/ajaxCommon!propTypeCategoryByPropType.action" />
		<td class="bluebox">
			<s:select name="propertyDetail.extra_field5" id="propTypeCategoryId"
				list="propTypeCategoryMap" listKey="key" listValue="value"
				headerKey="-1" headerValue="%{getText('default.select')}"
				value="%{propertyDetail.extra_field5}"
				onchange="hideAddRmvBtnForResidFlats();" />
		</td>
		<td class="bluebox" colspan="2">
			&nbsp;
		</td>
	</tr>
	<tr>
		<td class="greybox2">
			&nbsp;
		</td>
		<td class="greybox">
			<div id="plotArea">
				<s:text name="PlotArea" />
				<span class="mandatory1">*</span> :
			</div>
			<div id="undivArea">
				<s:text name="undivArea"/>
				<span class="mandatory1">*</span> :
			</div>
		</td>
		<td class="greybox">
			<s:textfield name="areaOfPlot" maxlength="15"
				onblur="trim(this,this.value);checkForTwoDecimals(this,'Area Of Plot');
        		checkZero(this,'Area Of Plot');" />
			<span class="highlight2"><s:text
					name="msgForCompulsionOfOpenPlot" /> </span>
		</td>
		<td class="greybox">
			&nbsp;
		</td>
		<td class="greybox">
			&nbsp;
		</td>
	</tr>
	<tr id="taxExemptRow">
		<td class="bluebox2">
			&nbsp;
		</td>
		<td class="bluebox">
			<s:text name="ExemptedFromTax" />
			:
		</td>
		<td class="bluebox">
			<s:checkbox name="isExemptedFromTax" id="chkIsTaxExempted" onclick="enableTaxExemptReason()"></s:checkbox> 
		</td>
		<td class="bluebox">
			<s:text name="TaxExmRsn" />
			:
		</td>
		<td class="bluebox">
			<s:select  headerValue="%{getText('default.select')}" headerKey="-1"
				name="taxExemptReason" id="taxExemptReason" 
				list="dropdownData.taxExemptedList" cssClass="selectnew" value="%{taxExemptReason}"	 />
		</td>
	</tr>
	<tr>
		<td class="greybox2">
			&nbsp;
		</td>
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
		<td class="greybox">
			&nbsp;
		</td>
		<td class="greybox">
			&nbsp;
		</td>
	</tr>
	<tr id="waterRate">
		<td class="bluebox2">
			&nbsp;
		</td>
		<td class="bluebox">
			<s:text name="GenWaterRate" />
			<span class="mandatory1">*</span> :
		</td>
		<td class="bluebox">
			<s:select name="propertyDetail.extra_field1" id="genWaterRate"
				list="waterMeterMap" listKey="key" listValue="value" headerKey="-1"
				headerValue="%{getText('default.select')}"
				value="%{propertyDetail.extra_field1}" />
		</td>
		<td class="bluebox">
			&nbsp;
		</td>
		<td class="bluebox">
			&nbsp;
		</td>
	</tr>
	<tr id="usageRow">
		<td class="greybox2">
			&nbsp;
		</td>
		<td class="greybox">
			<s:text name="Usage" />
			<span class="mandatory1">*</span>
			<a onclick="openWindow('UsageMaster.jsp');"> <img
					src="../image/help.gif" style="border: none" /> </a> :
		</td>
		<egov:ajaxdropdown id="usage" fields="['Text','Value']"
			dropdownId="usage" url="common/ajaxCommon!usageByPropType.action" />
		<td class="greybox">
			<s:select headerKey="-1" headerValue="%{getText('default.select')}"
				name="propUsageId" listKey="id" id="usage" listValue="usageName"
				list="dropdownData.UsageList" cssClass="selectnew"
				value="%{propUsageId}" />
		</td>
		<td class="greybox">
			&nbsp;
		</td>
		<td class="greybox">
			&nbsp;
		</td>
	</tr>
	<tr id="occupancyRow">
		<td class="bluebox2">
			&nbsp;
		</td>
		<td class="bluebox">
			<s:text name="Occupancy" />
			<span class="mandatory1">*</span> :
		</td>
		<td class="bluebox">
			<s:select headerKey="-1" headerValue="%{getText('default.select')}"
				name="propOccId" listKey="id" onchange="enableRentBox();"
				id="occupation" listValue="occupation"
				list="dropdownData.OccupancyList" cssClass="selectnew"
				value="%{propOccId}" />
		</td>
		<td class="bluebox">
			&nbsp;
		</td>
		<td class="bluebox">
			&nbsp;
		</td>
	</tr>
	<tr id="rentRow">
		<td class="bluebox2">
			&nbsp;
		</td>
		<td class="bluebox">
			<s:text name="rent" />
			<span class="mandatory1" id="rentBoxMandatory">*</span>:
		</td>
		<td class="bluebox">
			<s:textfield id="rentBox" name="propertyDetail.extra_field2"
				maxlength="10" value="%{propertyDetail.extra_field2}">
			</s:textfield>
		</td>
		<td class="bluebox">
			&nbsp;
		</td>
		<td class="bluebox">
			&nbsp;
		</td>
	</tr>

	<tr id="buildingcostRow">
		<td class="greybox2" width="5%">
			&nbsp;
		</td>
		<td class="greybox">
			<s:text name="bldngCost" />
			<span class="mandatory1" id="bldngCostMandatory">*</span> :
		</td>
		<td class="greybox">
			<s:textfield id="bldngCostId" name="propertyDetail.extra_field3"
				value="%{propertyDetail.extra_field3}" maxlength="10">
			</s:textfield>
		</td>
		<td class="greybox">
			&nbsp;
		</td>
		<td class="greybox">
			&nbsp;
		</td>
	</tr>
	<tr id="amenitiesRow">
		<td class="bluebox2" width="5%">
			&nbsp;
		</td>
		<td class="bluebox">
			<s:text name="amenities" />
			:
		</td>
		<td class="bluebox">
			<s:select headerKey="-1" headerValue="%{getText('default.select')}"
				id="amenitiesId" name="propertyDetail.extra_field4" listKey="key"
				listValue="value" list="amenitiesMap" cssClass="selectnew"
				value="%{propertyDetail.extra_field4}" />
		</td>
		<td class="bluebox">
			&nbsp;
		</td>
		<td class="bluebox">
			&nbsp;
		</td>
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
		<td class="greybox2">
			&nbsp;
		</td>
		<td class="greybox">
			<s:text name="AuthProp" />
			<span class="mandatory1">*</span> :
		</td>
		<td class="greybox">
			<s:radio name="isAuthProp" list="dropdownData.AuthPropList" />
		</td>
		<td class="greybox">
			&nbsp;
		</td>
		<td class="greybox">
			&nbsp;
		</td>
	</tr>
	<tr>
		<td class="bluebox2">
			&nbsp;
		</td>
		<td class="bluebox">
			<s:text name="NoticeGenConfirm" />
			<span class="mandatory1">*</span> :
		</td>
		<td class="bluebox">
			<s:radio name="extra_field2" list="dropdownData.NoticeTypeList" />
		</td>
		<td class="bluebox">
			&nbsp;
		</td>
		<td class="bluebox">
			&nbsp;
		</td>
	</tr>
	<tr>
		<td class="greybox2">
			&nbsp;
		</td>
		<td class="greybox">
			<s:text name="ModifyReason" />
		</td>
		<td class="greybox">			
			<s:if test="modifyRsn=='DATA_UPDATE'">
				<span class="bold"><div id="rsnForModification">DATA_UPDATE</div></span>
			</s:if>
			<s:if test="modifyRsn=='DATA_ENTRY'">
				<span class="bold">DATA_ENTRY</span>
			</s:if>
			<s:elseif test="modifyRsn=='MODIFY'">
				<s:select headerKey="-1" headerValue="%{getText('default.select')}"
					name="reasonForModify" listKey="code" listValue="mutationName"
					list="dropdownData.MutationList" cssClass="selectnew"
					onchange="return enableCourtRulingDets();" />
			</s:elseif>
		</td>
		<td class="greybox">
			&nbsp;
		</td>
		<td class="greybox">
			&nbsp;
		</td>
	</tr>
	<s:if test="modifyRsn=='OBJ' || objNum != null">
		<tr>
			<td class="bluebox2">
				&nbsp;
			</td>
			<td class="bluebox">
				<s:text name="objNumber" />
				:
			</td>
			<td class="bluebox">
				<span class="bold"><s:property default="N/A"
						value="%{objNum}" /> </span>
			</td>
			<td class="bluebox">
				<s:text name="objDate" />
				:
			</td>
			<td class="bluebox">
				<s:date name="objDate" var="objFormat" format="dd/MM/yyyy" />
				<span class="bold"><s:property default="N/A"
						value="%{objDate}" /> </span>
			</td>
		</tr>
	</s:if>
		<tr id="courtOrdNoRow">
			<td class="bluebox2">
				&nbsp;
			</td>
			<td class="bluebox">
				<s:text name="courtOrdNumber" />
				:
			</td>
			<td class="bluebox">
				<s:textfield name="courtOrdNum" id="courtOrdNo" maxlength="40"
					onblur="trim(this,this.value);checkZero(this,'Court Order Number');" />
			</td>
			<td class="bluebox">
				<s:text name="dtOfOrder" />
				:
			</td>
			<td class="bluebox">
				<s:date name="orderDate" var="ordFormat" format="dd/MM/yyyy" />
				<s:textfield name="orderDate" id="orderDate" maxlength="10"
					value="%{orderDate}"
					onkeyup="DateFormat(this,this.value,event,false,'3')"
					onfocus="waterMarkTextIn('orderDate','DD/MM/YYYY');"
					onblur="validateDateFormat(this);waterMarkTextOut('orderDate','DD/MM/YYYY');" />
			</td>
		</tr>
		<tr id="JudgmtDetsRow">
			<td class="bluebox2">
				&nbsp;
			</td>
			<td class="bluebox">
				<s:text name="JudgmtDets" />
				:
			</td>
			<td class="bluebox">
				<s:textarea cols="80" rows="2" name="judgmtDetails" id="judgeDet"
					onblur="trim(this,this.value);" />
			</td>
			<td class="bluebox" colspan="2">
				&nbsp;
			</td>
		</tr>
	
	<tr>
		<td class="greybox2">&nbsp;</td>
		<td class="greybox">Upload Document</td>
		<td class="greybox"><input type="button" class="button"
			value="Upload Document" id="docUploadButton"
			onclick="showDocumentManager();" /> <s:hidden name="docNumber"
				id="docNumber" /></td>
		<td class="greybox" colspan="2">&nbsp;</td>
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
	<s:if test="modifyRsn=='DATA_ENTRY' || modifyRsn=='MODIFY' || modifyRsn=='OBJ'">
		<tr id="allChangeDoneRow">
			<td class="greybox">&nbsp;</td>
			<td class="greybox">
				<span class="bold"><s:text name="allChangesDone"/></span>
			</td>
			<td class="greybox"><s:checkbox id="allChngsCmpltd" name="allChangesCompleted" value="%{allChangesCompleted}"/></td>
			<td class="greybox" colspan="2">&nbsp;</td>
		</tr>
	</s:if>
	<s:if test="%{basicProp.isMigrated != null && basicProp.isMigrated == 'Y' && modifyRsn=='DATA_UPDATE'}">
			<s:hidden id="updateData" name="updateData" value="true" /> 		
	</s:if>
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
		
	function populatePropTypeCategory() {
		populatepropTypeCategoryId( {
			propTypeId : document.getElementById("propTypeMaster").value
		});
	}
	
	function populateLocationFactors() {
		populatelocationFactor({
			wardId : <s:property value="%{basicProp.boundary.id}" />
		});
	}
	
	function setLocationFactor() {
		<s:if test="%{propertyDetail.extra_field6 != null}">
			document.getElementById('locationFactor').value = <s:property value="%{propertyDetail.extra_field6}"/>;			
		</s:if>
	}
</script>
