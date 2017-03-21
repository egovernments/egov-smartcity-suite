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
<link rel="stylesheet" href="<cdn:url value='/resources/global/css/font-icons/font-awesome/css/font-awesome.min.css' context='/egi'/>">
<table width="100%" border="0" cellspacing="0" cellpadding="0">
	<tr>
		<td class="greybox" width="5%">
			&nbsp;
		</td>
		<td class="bluebox" width="20%">
			<s:text name="prop.Id" />
			:
		</td>
		<td class="bluebox" width="15%">
			<span class="bold"><s:property default="N/A"
					value="%{basicProp.upicNo}" /> </span>						
		</td>
		<td class="bluebox" width="20%">
			&nbsp;
		</td>
		<td class="bluebox" width="20%">
			&nbsp;
		</td>
	</tr>
	<tr>
		<td class="greybox">
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
			<s:text name="MobileNumber" />:
		</td>
		<td class="greybox">
			<span class="bold"><s:property value="%{basicProp.mobileNumber}" default="N/A"/></span>
		</td>
	</tr>

	<tr>
		<td class="greybox">
			&nbsp;
		</td>
		<td class="bluebox" width="8%">
			<s:text name="PropertyAddress" />
			:
		</td>
		<td class="bluebox">
			<span class="bold"><s:property default="N/A"
					value="%{propAddress}" /> </span>
		</td>
		<td class="bluebox">
			&nbsp;
		</td>
		<td class="bluebox">
			&nbsp;
		</td>
	</tr>
	<tr>
		<td colspan="5">
			<div class="headingsmallbg">
				<span class="bold"><s:text name="assessmentDetails.title"/></span>
			</div>
		</td>
	</tr>
	<tr>
		<td class="greybox" width="5%">&nbsp;</td>
		<td class="bluebox" width="25%"><s:text name="reg.docno"/> :</td>
		<td class="bluebox" width="">
			<span class="bold"><s:property value="%{basicProp.regdDocNo}" default="N/A"/></span>
		</td>
		<td class="bluebox" width="25%"><s:text name="reg.docdate"/> :</td>
		<td class="bluebox">
		<s:date name="basicProp.regdDocDate" var="regDate" format="dd/MM/yyyy" /> 
			<span class="bold"><s:property value="%{#regDate}" default="N/A"/></span>
		</td>
	</tr>
	<tr class="extentSite">
		<td class="greybox" width="5%">&nbsp;</td>
		<td class="greybox" width="25%"><s:text name="extent.site"/> :</td>
		<td class="greybox" width="">
		   <s:textfield name="areaOfPlot" id="areaOfPlot" size="12"	maxlength="15" value="%{areaOfPlot}"
		   onblur="trim(this,this.value);checkForTwoDecimals(this,'extent of site');checkZero(this,'extent of site');"></s:textfield>
		</td>
		<td class="greybox" width="25%"></td>
		<td class="greybox">
		</td>
	</tr>
	<%-- <tr class="superStructureRow">
		<td class="greybox">&nbsp;</td>
		<td class="bluebox"><s:text name="superstructure"></s:text> :</td>
		<td class="bluebox">
			 <s:checkbox name="propertyDetail.structure" id="propertyDetail.structure"
				value="%{propertyDetail.structure}" onclick="enableOrDisableSiteOwnerDetails(this);" />
		</td>
		<td class="greybox siteowner"><s:text name="siteowner"></s:text><span class="mandatory1"> *</span> :</td>
		<td class="greybox siteowner"><s:textfield maxlength="64" value="%{propertyDetail.siteOwner}"
				name="propertyDetail.siteOwner" id="propertyDetail.siteOwner"></s:textfield></td>
	</tr> --%>
	<tr>
		<td class="greybox" width="5%">&nbsp;</td>
		<td class="greybox" width="25%"><s:text name="ownership.type"></s:text>
			<span class="mandatory1">*</span> :</td>
		<td class="greybox" width=""><s:select headerKey="-1" headerValue="%{getText('default.select')}" name="propTypeId"
				id="propTypeId" listKey="id" listValue="type" list="dropdownData.PropTypeMaster" value="%{propTypeId}"
				cssClass="selectnew" onchange="populatePropTypeCategory();toggleFloorDetails();enableFieldsForPropType();" /></td>
				
		<td class="greybox" width="25%"><s:text name="property.type"></s:text>
			<span class="mandatory1" id="prntMandatory">*</span> :</td>
		<egov:ajaxdropdown id="propTypeCategoryId" fields="['Text','Value']" dropdownId="propTypeCategoryId"
			url="/common/ajaxCommon-propTypeCategoryByPropType.action" />
		<td class="greybox">
		   <s:select headerKey="-1"	headerValue="%{getText('default.select')}" name="propertyDetail.categoryType"
				id="propTypeCategoryId" listKey="key" listValue="value" list="propTypeCategoryMap" value="%{propertyDetail.categoryType}"
				cssClass="selectnew"/>
		</td>
	</tr>
	
	<tr id="apartmentRow">
		<td class="greybox">&nbsp;</td>
		<td class="greybox"><s:text name="apartcomplex.name"></s:text> :</td>
		<td class="greybox"><s:select headerKey=""
				headerValue="%{getText('default.select')}" 	name="propertyDetail.apartment" id="propertyDetail.apartment.id"
				listKey="id" listValue="name" value="%{propertyDetail.apartment.id}"
				list="dropdownData.apartments" cssClass="selectnew" /></td>
		<td class="greybox" colspan="2">
			&nbsp;			
		</td>
	</tr>
	<tr class="appurtenant">
		<td class="bluebox">&nbsp;</td>
		<td class="bluebox"><s:text name="certificationNumber"></s:text>:</td>
		<td class="bluebox"><s:textfield maxlength="64" name="propertyDetail.occupancyCertificationNo" id="certificationNumber" value="%{propertyDetail.occupancyCertificationNo}"/></td>
		<td class="bluebox" colspan="2">&nbsp;</td>
	</tr>
	<tr class="bpddetailsheader">
		<td class="greybox">&nbsp;</td>
		<td class="bluebox">&nbsp;</td>
		<td class="bluebox"><s:text name="builidingdetails"></s:text> :</td>
		<td class="bluebox">
		 <s:checkbox name="propertyDetail.buildingPlanDetailsChecked" id="buildingPlanDetailsChecked"
			value="%{propertyDetail.buildingPlanDetailsChecked}" onclick="enableOrDisableBPADetails(this);" />
		</td>
		<td class="bluebox" colspan="2">
			&nbsp;
		</td>
	</tr>
	
	<tr class="bpddetails">
		<td class="greybox">&nbsp;</td>
		<td class="greybox"><s:text name="building.permNo"></s:text><span class="mandatory1"> *</span> :</td>
		<td class="greybox"><s:textfield name="propertyDetail.buildingPermissionNo" id="propertyDetail.buildingPermissionNo" size="12" maxlength="12"
				onchange="trim(this,this.value);" onblur="checkZero(this);" value="%{propertyDetail.buildingPermissionNo}"></s:textfield>
		</td>
		<td class="greybox"><s:text name="buildingpermdate"></s:text><span class="mandatory1"> *</span> :</td>
		<td class="greybox"><s:date name="propertyDetail.buildingPermissionDate" var="buildingPermDate" format="dd/MM/yyyy" /> 
		<s:textfield name="propertyDetail.buildingPermissionDate" cssClass="datepicker" value="%{#buildingPermDate}" autocomplete="off"
				id="propertyDetail.buildingPermissionDate" size="12" maxlength="12"></s:textfield>
		</td>

	</tr>

    <tr class="bpddetails">
		<td class="greybox">&nbsp;</td>
		<td class="bluebox"><s:text name="deviationper"></s:text><span class="mandatory1">*</span> :</td>
		<td class="bluebox"><s:select headerKey="-1" headerValue="%{getText('default.select')}" name="propertyDetail.deviationPercentage"
				id="propertyDetail.deviationPercentage" listKey="key" listValue="value" list="deviationPercentageMap" value="%{propertyDetail.deviationPercentage}"
				cssClass="selectnew"/>
		</td>
		<td class="bluebox" colspan="2">
			&nbsp;
		</td>
   </tr>
	
	<!-- Amenities section -->
	
	<tr id="amenitiesHeaderRow" class="amenities">
		<td colspan="5">
			<div class="headingsmallbg">
				<span class="bold"> <s:text name="amenities"></s:text>
				</span>
			</div>
		</td>
	</tr>

	<tr class="amenities">
		<td colspan="5">
			<div id="AmenitiesDiv">
				<%@ include file="../common/amenitiesForm.jsp"%>
			</div>
		</td>
	</tr>
	 
	
	<!-- Floor type details -->
	
	<tr id="constructionHeaderRow" class="construction">
		<td colspan="5">
			<div class="headingsmallbg">
				<span class="bold"><s:text name="title.constructiontypes" /></span>
			</div>
		</td>
	</tr>
	
	<tr class="construction">
		<td colspan="5">
			<div id="AmenitiesDiv">
				<%@ include file="../common/constructionForm.jsp"%>
			</div>
		</td>
	</tr>
	
	<tr id="floorHeaderRow" class="floordetails">
		<td colspan="5">
			<div class="headingsmallbg">
				<span class="bold"><s:text name="FloorDetailsHeader" /> </span>
			</div>
		</td>
	</tr>

	<!-- Floor Details Section -->

	<tr class="floordetails">
		<td colspan="5">
			<div align="center">
				<%@ include file="../common/FloorForm.jsp"%>
			</div>
		</td>
	</tr>

	<tr id="vacantLandRow" class="vacantlanddetaills">
		<td colspan="5">
			<div class="headingsmallbg">
				<span class="bold"><s:text name="VacantLandDetailsHeader" /> </span>
			</div>
		</td>
	</tr>

	<tr class="vacantlanddetaills">
		<td colspan="5">
			<div align="center">
				<%@ include file="../common/vacantLandForm.jsp"%>
			</div>
		</td>
	</tr>
	<s:if test="%{!documentTypes.isEmpty()}">
		<tr>
			<td colspan="5">
				<%@ include file="../common/DocumentUploadForm.jsp"%>
			</td>
		</tr>
	</s:if>
</table>
<script type="text/javascript">
	function populatePropTypeCategory() {
		populatepropTypeCategoryId({
			propTypeId : document.getElementById("propTypeId").value
		});
	}
	//hide rows and columns of fields
	jQuery('td.siteowner').hide();
	jQuery('tr.bpddetails').hide();
	jQuery('tr.vacantlanddetaills').hide();
    
</script>