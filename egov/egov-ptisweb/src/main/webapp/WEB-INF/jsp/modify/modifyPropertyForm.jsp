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
<link rel="stylesheet"
	href="<cdn:url value='/resources/global/css/font-icons/font-awesome/css/font-awesome.min.css' context='/egi'/>">
<table width="100%" border="0" cellspacing="0" cellpadding="0"
	class="table-fixed">
	<tr>
		<td class="greybox" width="5%">&nbsp;</td>
		<td class="bluebox" width="20%"><s:text name="prop.Id" /> :</td>
		<td class="bluebox" width="15%"><span class="bold"><s:property
					default="N/A" value="%{basicProp.upicNo}" /> </span></td>
		<td class="bluebox" width="20%">&nbsp;</td>
		<td class="bluebox" width="20%">&nbsp;</td>
	</tr>
	<tr>
		<td class="greybox">&nbsp;</td>
		<td class="greybox"><s:text name="OwnerName" /> :</td>
		<td class="greybox"><span class="bold"><s:property
					default="N/A" value="%{ownerName}" /> </span></td>
		<td class="greybox"><s:text name="MobileNumber" />:</td>
		<td class="greybox"><span class="bold"><s:property
					value="%{basicProp.mobileNumber}" default="N/A" /></span></td>
	</tr>

	<tr>
		<td class="greybox">&nbsp;</td>
		<td class="bluebox" width="8%"><s:text name="PropertyAddress" />
			:</td>
		<td class="bluebox"><span class="bold"><s:property
					default="N/A" value="%{propAddress}" /> </span></td>
		<td class="bluebox">&nbsp;</td>
		<td class="bluebox">&nbsp;</td>
	</tr>
	<tr>
		<td colspan="5">
			<div class="headingsmallbg">
				<span class="bold"><s:text name="assessmentDetails.title" /></span>
			</div>
		</td>
	</tr>
	<tr>
		<td class="greybox" width="5%">&nbsp;</td>
		<td class="bluebox" width="25%"><s:text name="reg.docno" /> :</td>
		<td class="bluebox" width=""><span class="bold"><s:property
					value="%{basicProp.regdDocNo}" default="N/A" /></span></td>
		<td class="bluebox" width="25%"><s:text name="reg.docdate" /> :</td>
		<td class="bluebox"><s:date name="basicProp.regdDocDate"
				var="regDate" format="dd/MM/yyyy" /> <span class="bold"><s:property
					value="%{#regDate}" default="N/A" /></span></td>
	</tr>
	<tr class="extentSite">
		<td class="greybox" width="5%">&nbsp;</td>
		<td class="greybox" width="25%"><s:text name="extent.site" /><span
			class="mandatory1"> *</span> :</td>
		<td class="greybox" width=""><s:textfield name="areaOfPlot"
				id="areaOfPlot" size="12" maxlength="15" value="%{areaOfPlot}"
				onblur="trim(this,this.value);checkForTwoDecimals(this,'extent of site');checkZero(this,'extent of site');"></s:textfield>
		</td>
		<td class="greybox" width="25%"></td>
		<td class="greybox"></td>
	</tr>
	<%-- <tr class="superStructureRow">
		<td class="greybox">&nbsp;</td>
		<td class="bluebox"><s:text name="superstructure"></s:text> :</td>
		<td class="bluebox"><s:checkbox name="propertyDetail.structure"
				id="propertyDetail.structure" value="%{propertyDetail.structure}"
				onclick="enableOrDisableSiteOwnerDetails(this);" /></td>
		<td class="greybox siteowner"><s:text name="siteowner"></s:text><span
			class="mandatory1"> *</span> :</td>
		<td class="greybox siteowner"><s:textfield maxlength="64"
				value="%{propertyDetail.siteOwner}" name="propertyDetail.siteOwner"
				id="siteOwner"></s:textfield></td>
	</tr> --%>
	<tr>
		<td class="greybox" width="5%">&nbsp;</td>
		<td class="greybox" width="25%"><s:text name="ownership.type"></s:text>
			<span class="mandatory1">*</span> :</td>
		<td class="greybox" width=""><s:select headerKey="-1"
				headerValue="%{getText('default.select')}" name="propTypeId"
				id="propTypeId" listKey="id" listValue="type"
				list="dropdownData.PropTypeMaster" value="%{propTypeId}"
				cssClass="selectnew"
				onchange="populatePropTypeCategory();toggleFloorDetails();enableFieldsForPropType();" /></td>

		<td class="greybox" width="25%"><s:text name="property.type"></s:text>
			<span class="mandatory1" id="prntMandatory">*</span> :</td>
		<egov:ajaxdropdown id="propTypeCategoryId" fields="['Text','Value']"
			dropdownId="propTypeCategoryId"
			url="/common/ajaxCommon-propTypeCategoryByPropType.action" />
		<td class="greybox"><s:select headerKey="-1"
				headerValue="%{getText('default.select')}"
				name="propertyDetail.categoryType" id="propTypeCategoryId"
				listKey="key" listValue="value" list="propTypeCategoryMap"
				value="%{propertyDetail.categoryType}" cssClass="selectnew"
				onchange="populateUsages();" /> <s:hidden name="propertyCategory"
				id="propertyCategory" /></td>
	</tr>

	<tr id="apartmentRow">
		<td class="greybox">&nbsp;</td>
		<td class="greybox apartmentRow"><s:text name="apartcomplex.name"></s:text>
			:</td>
		<td class="greybox apartmentRow"><s:select headerKey=""
				headerValue="%{getText('default.select')}"
				name="propertyDetail.apartment" id="apartment" listKey="id"
				listValue="name" value="%{propertyDetail.apartment.id}"
				list="dropdownData.apartments" cssClass="selectnew" /></td>
		<td class="bluebox"><s:text name="certificationNumber"></s:text>:</td>
		<td class="bluebox"><s:textfield maxlength="64"
				name="propertyDetail.occupancyCertificationNo"
				id="certificationNumber"
				value="%{propertyDetail.occupancyCertificationNo}" /></td>
	</tr>
	<tr>
		<td class="greybox">&nbsp;</td>
		<td class="greybox"><s:text name="zone"></s:text> <span
			class="mandatory1">*</span> :</td>
		<s:if test="%{basicProperty.propertyID.zone !=null && isZoneActive()}">
			<td class="greybox"><s:textfield name="zone" id="zone"
					value="%{basicProperty.propertyID.zone.name}" /></td>
		</s:if>
		<s:else>
			<td class="bluebox"><s:select list="dropdownData.zones"
					name="zoneId" value="%{zoneId}" headerKey="-1" id="zoneId"
					headerValue="%{getText('default.select')}" listKey="id"
					listValue="name" /></td>
		</s:else>
		<td class="greybox"><s:text name="locality"></s:text> <span
			class="mandatory1">*</span> :</td>
		<s:set var="isLocalityActive" value="%{isLocalityActive()}"></s:set>
		<s:if
			test="%{basicProperty.propertyID.locality !=null && isLocalityActive()}">
			<td class="greybox"><s:textfield name="locality" id="locality"
					value="%{basicProperty.propertyID.locality.name}" /></td>
		</s:if>
		<s:else>
			<td class="bluebox"><s:select list="dropdownData.localities"
					name="localityId" value="%{localityId}" headerKey="-1"
					id="localityId" headerValue="%{getText('default.select')}"
					listKey="id" listValue="name" /></td>
		</s:else>
	</tr>
	<tr>
		<td class="greybox">&nbsp;</td>
		<td class="greybox"><s:text name="blockno"></s:text> <span
			class="mandatory1">*</span> :</td>
		<s:if
			test="%{basicProperty.propertyID.area !=null && isBlockActive() && isLocalityActive()}">
			<td class="greybox"><s:textfield name="block" id="block"
					value="%{basicProperty.propertyID.area.name}" /></td>
		</s:if>
		<s:else>
			<td class="bluebox"><s:select list="dropdownData.blocks"
					name="blockId" value="%{blockId}" headerKey="-1" id="blockId"
					headerValue="%{getText('default.select')}" listKey="id"
					listValue="name" /></td>
		</s:else>
		<td class="greybox"><s:text name="revwardno"></s:text> <span
			class="mandatory1">*</span> :</td>
		<s:if
			test="%{basicProperty.propertyID.ward !=null && isWardActive() && isLocalityActive()}">
			<td class="greybox"><s:textfield name="ward" id="ward"
					value="%{basicProperty.propertyID.ward.name}" /></td>
		</s:if>
		<s:else>
			<td class="bluebox"><s:select list="dropdownData.wards"
					name="wardId" value="%{wardId}" headerKey="-1" id="wardId"
					headerValue="%{getText('default.select')}" listKey="id"
					listValue="name" onchange="populateBlock();" /></td>
		</s:else>
	</tr>
	<tr>
		<td class="greybox">&nbsp;</td>
		<td class="greybox"><s:text name="elec.wardno"></s:text> <span
			class="mandatory1">*</span> :</td>
		<s:if
			test="%{basicProperty.propertyID.electionBoundary !=null && isElectionWardActive()}">
			<td class="greybox"><s:textfield name="electionWard"
					id="electionWard"
					value="%{basicProperty.propertyID.electionBoundary.name}" /></td>
		</s:if>
		<s:else>
			<td class="bluebox"><s:select list="dropdownData.electionWards"
					name="electionWardId" value="%{electionWardId}" headerKey="-1"
					id="electionWardId" headerValue="%{getText('default.select')}"
					listKey="id" listValue="name" /></td>
		</s:else>
	</tr>
	<tr>
		<td class="greybox">&nbsp;</td>
		<s:if
			test="%{oldPropertyTypeCode==@org.egov.ptis.constants.PropertyTaxConstants@OWNERSHIP_TYPE_VAC_LAND && modifyRsn==@org.egov.ptis.constants.PropertyTaxConstants@PROPERTY_MODIFY_REASON_ADD_OR_ALTER}">
			<td class="greybox"><s:text name="Door No"></s:text> :</td>
			<s:if test="%{houseNo!=null && !houseNo.isEmpty()}">
				<td class="greybox"><s:textfield name="houseNo" id="houseNo"
						value="%{houseNo}" /></td>
			</s:if>
			<s:else>
				<td class="greybox"><s:textfield name="houseNo" id="houseNo"
						size="12" maxlength="15" value="%{houseNo}" /></td>
			</s:else>
		</s:if>
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
			<div align="center"
				class="overflow-x-scroll floors-tbl-freeze-column-div">
				<%@ include file="../common/FloorForm.jsp"%>
			</div>
		</td>
	</tr>

	<tr id="vacantLandRow" class="vacantlanddetaills">
		<td colspan="5">
			<div class="headingsmallbg">
				<span class="bold"><s:text name="VacantLandDetailsHeader" />
				</span>
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
			<td colspan="5"><%@ include
					file="../common/DocumentUploadForm.jsp"%></td>
		</tr>
	</s:if>
	<s:if
		test="%{propertyTaxDetailsMap.size != 0 && isExemptedFromTax == false}">
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
	jQuery(document).ready(function() {
		if(!'<s:property value="%{isLocalityActive()}"/>'){
			populateBoundaries();
		}
		var propType = jQuery('#propTypeId :selected').text();
		var doorno = jQuery("#houseNo").val() != '';
		if(doorno && propType=='<s:property value="%{@org.egov.ptis.constants.PropertyTaxConstants@OWNERSHIP_TYPE_VAC_LAND_STR}"/>'){
			jQuery("#houseNo").prop("readonly", true);
		}
		else{
			jQuery("#houseNo").prop("readonly", false);
		}
		if('<s:property value="%{isZoneActive()}"/>'){
			jQuery("#zone").prop("readonly", true);
		}
		if('<s:property value="%{isElectionWardActive()}"/>'){
			jQuery("#electionWard").prop("readonly", true);
		}
		if('<s:property value="%{isLocalityActive()}"/>'){
			jQuery("#locality").prop("readonly", true);
			if('<s:property value="%{isBlockActive()}"/>'){
				jQuery("#block").prop("readonly", true);
			}
			if('<s:property value="%{isWardActive()}"/>'){
				jQuery("#ward").prop("readonly", true);
			}
		}
		jQuery('#localityId').change(function() {
			 populateBoundaries();
		 });
		var blocks = jQuery('#blockId').children('option').length;
		var wards = jQuery('#wardId').children('option').length;
		if(blocks < 2 && !'<s:property value="%{isBlockActive()}"/>'){
			bootbox.alert("No block details mapped for ward.");
			return false;
		}
		if(wards < 2 && !'<s:property value="%{isWardActive()}"/>'){
			bootbox.alert("No ward details mapped for locality.");
			return false;
		}
	});
	jQuery(function() {
		jQuery("#propTypeId").change(function(){
			var propType = jQuery('#propTypeId :selected').text();
			var doorno = jQuery("#houseNo").val() != '';
			if(doorno && propType=='<s:property value="%{@org.egov.ptis.constants.PropertyTaxConstants@OWNERSHIP_TYPE_VAC_LAND_STR}"/>'){
				jQuery("#houseNo").prop("readonly", true);
			}
			else{
				jQuery("#houseNo").prop("readonly", false);
			}
        });
    });
	
	function populateBoundaries() {
		jQuery.ajax({
			url: "/egi/public/boundary/ajaxBoundary-blockByLocality",
			type: "GET",
			data: {
				locality : jQuery('#localityId').val()
			},
			cache: false,
			dataType: "json",
			success: function (response) {
				jQuery('#wardId').html("");
				jQuery('#blockId').html("");
				jQuery('#streetId').html("");
				jQuery.each(response.results.boundaries, function (j, boundary) {
					if (boundary.wardId) {
						jQuery('#wardId').append("<option value='"+boundary.wardId+"'>"+boundary.wardName+"</option>");
					}
					jQuery('#blockId').append("<option value='"+boundary.blockId+"'>"+boundary.blockName+"</option>");
				});
				jQuery.each(response.results.streets, function (j, street) {
					jQuery('#streetId').append("<option value='"+street.streetId+"'>"+street.streetName+"</option>");
				});
				<s:if test="%{wardId != null}">
					jQuery('#wardId').val('<s:property value="%{wardId}"/>');
				</s:if>
				<s:if test="%{blockId != null}">
					jQuery('#blockId').val('<s:property value="%{blockId}"/>');
				</s:if>
				<s:if test="%{streetId != null}">
					jQuery('#streetId').val('<s:property value="%{streetId}"/>');
				</s:if>
			}, 
			error: function (response) {
				jQuery('#wardId').html("");
				jQuery('#blockId').html("");
				jQuery('#streetId').html("");
				bootbox.alert("No boundary details mapped for locality")
			}
		});

	}

	function populateBlock() {
		jQuery.ajax({
			url: "/egi/boundary/ajaxBoundary-blockByWard.action",
			type: "GET",
			data: {
				wardId : jQuery('#wardId').val()
			},
			cache: false,
			dataType: "json",
			success: function (response) {
				jQuery('#blockId').html("");
				jQuery.each(response, function (j, block) {
					jQuery('#blockId').append("<option value='"+block.blockId+"'>"+block.blockName+"</option>");
				});
				<s:if test="%{blockId != null}">
					jQuery('#blockId').val('<s:property value="%{blockId}"/>');
				</s:if>
			}, 
			error: function (response) {
				jQuery('#blockId').html("");
				bootbox.alert("No block details mapped for ward")
			}
		});
	}
</script>
