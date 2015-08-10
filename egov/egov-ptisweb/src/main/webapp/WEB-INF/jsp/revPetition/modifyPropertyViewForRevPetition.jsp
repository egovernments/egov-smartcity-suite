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
		<td class="bluebox2">
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
		<td class="greybox" width="25%"><s:text name="extent.site"/> :</td>
		<td class="greybox" width="">
		   <span class="bold">
		   <s:hidden id="property.propertyDetail.id" name="property.propertyDetail.id" value="%{property.propertyDetail.id}" />
			 <s:hidden id="property.id" name="property.id" value="%{property.id}" />
							
		   <s:property value="%{property.propertyDetail.extentSite}" default="N/A"/></span>
		</td>
		<td class="greybox" width="25%"></td>
		<td class="greybox">
		</td>
	</tr>

	<tr>
		<td class="bluebox" width="5%">&nbsp;</td>
		<td class="bluebox" width="25%"><s:text name="reg.docno"/> :</td>
		<td class="bluebox" width="">
			<span class="bold"><s:property value="%{basicProp.regdDocNo}" default="N/A"/></span>
		</td>
		<td class="bluebox" width="25%"><s:text name="reg.docdate"/> :</td>
		<td class="bluebox">
			<span class="bold"><s:property value="%{basicProp.regdDocDate}" default="N/A"/></span>
		</td>
	</tr>
	
	<tr>
		<td class="greybox">&nbsp;</td>
		<td class="greybox"><s:text name="superstructure"></s:text> :</td>
		<td class="greybox">
			<span class="bold"><s:checkbox name="property.propertyDetail.structure" disabled="true"/></span>
		</td>
		<td class="greybox siteowner"><s:text name="siteowner"></s:text>:</td>
		<td class="greybox siteowner">
			<span class="bold"><s:property value="%{property.propertyDetail.siteOwner}" default="N/A"/></span>
		</td>
	</tr>
	
	<tr>
		<td class="bluebox">&nbsp;</td>
		<td class="bluebox" width="25%"><s:text name="ModifyReason"></s:text> :</td>
		<td class="bluebox" width="">
			<span class="bold"><s:property value="%{property.propertyDetail.propertyMutationMaster.mutationName}" default="N/A"/></span>
		</td>
		<td class="bluebox" colspan="2">
			&nbsp;
		</td>
	</tr>
	
	<tr>
		<td class="greybox" width="5%">&nbsp;</td>
		<td class="greybox" width="25%"><s:text name="ownership.type"></s:text> :</td>
		<td class="greybox" width="">
			<span class="bold"><s:property value="%{property.propertyDetail.propertyTypeMaster.type}" default="N/A"/></span>
		</td>
		<td class="greybox" width="25%"><s:text name="property.type"></s:text></td>
		<td class="greybox">
			<span class="bold"><s:property value="%{property.propertyDetail.categoryType}" default="N/A"/></span>
		</td>
	</tr>
	<tr id="apartmentRow">
		<td class="greybox">&nbsp;</td>
		<td class="greybox"><s:text name="apartcomplex.name"></s:text> :</td>
		<td class="greybox">
			<span class="bold"><s:property value="%{property.propertyDetail.apartment.name}" default="N/A"/></span>
		</td>
		<td class="greybox" colspan="2">
			&nbsp;			
		</td>
	</tr>
	
	<tr class="appurtenant">
		<td class="greybox">&nbsp;</td>
		<td class="bluebox"><s:text name="extent.appurtntland" /> : </td>
		<td class="bluebox"><s:checkbox name="property.propertyDetail.appurtenantLandChecked" disabled="true" id="appurtenantLandChecked"/>
		</td>
		<td class="greybox"><s:text name="certificationNumber"></s:text>:</td>
		<td class="greybox">
			<span class="bold"><s:property value="%{property.propertyDetail.occupancyCertificationNo}"/></span>
		</td>
	</tr>
	<tr id="appurtenantRow">
		<td class="greybox">&nbsp;</td>
		<td class="greybox"><s:text name="extent.appurtntland"></s:text> :</td>
		<td class="greybox">
			<span class="bold"><s:property value="%{property.propertyDetail.extentAppartenauntLand}" default="N/A"/></span>
		</td>
		<td class="bluebox" colspan="2">
			&nbsp;
		</td>
	</tr>
	<tr>
		<td class="bluebox">&nbsp;</td>
		<td class="bluebox"><s:text name="builidingdetails"></s:text> :</td>
		<td class="bluebox">
		 	<span class="bold"><s:checkbox name="property.propertyDetail.buildingPlanDetailsChecked" disabled="true" id="buildingPlanDetailsChecked"/></span>
		</td>
		<td class="bluebox" colspan="2">
			&nbsp;
		</td>
	</tr>
	<tr class="bpddetails">
		<td class="greybox" width="5%">&nbsp;</td>
		<td class="greybox" width="25%"><s:text name="building.permNo"></s:text> :</td>
		<td class="greybox" width="">
			<span class="bold"><s:property value="%{property.propertyDetail.buildingPermissionNo}" default="N/A"/></span>
		</td>
		<td class="greybox" width="25%"><s:text name="buildingpermdate"></s:text> :</td>
		<td class="greybox">
			<span class="bold"><s:property value="%{property.propertyDetail.buildingPermissionDate}" default="N/A"/></span>
		</td>
	</tr>
	<tr class="bpddetails">
		<td class="bluebox">&nbsp;</td>
		<td class="bluebox"><s:text name="deviationper"></s:text> :</td>
		<td class="bluebox">
			<span class="bold"><s:property value="%{property.propertyDetail.deviationPercentage}" default="N/A"/></span>
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
				<%@ include file="../common/amenitiesViewForRevPetition.jsp"%>
			</div>
		</td>
	</tr>
			<!-- Floor type details -->
	
	<tr id="constructionHeaderRow" class="construction">
		<td colspan="5">
			<div class="headingsmallbg">
				<span class="bold"><s:text name="title.constructiontypes"/></span>
			</div>
		</td> 
	</tr>
	
	<tr class="construction">
		<td colspan="5">
			<div id="AmenitiesDiv">
				<%@include file="../common/constructionViewForRevPetition.jsp"%>
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

	<tr class="floordetails">
		<td colspan="5">
			<div align="center">
				<%@ include file="../common/FloorViewForRevPetition.jsp"%>
				<br/>
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
				<%@ include file="../common/vacantLandViewForRevPetition.jsp"%>
			</div>
		</td>
	</tr>
	<tr>
		<td colspan="5">
			<%@ include file="../common/DocumentUploadView.jsp"%>
		</td>
	</tr>
	
	<s:if test="%{property.propertyDetail.propertyTypeMaster.code.equalsIgnoreCase(@org.egov.ptis.constants.PropertyTaxConstants@PROPTYPE_OPEN_PLOT) || ((property.propertyDetail.propertyTypeMaster.code.equalsIgnoreCase(@org.egov.ptis.constants.PropertyTaxConstants@PROPTYPE_STATE_GOVT) 
		|| property.propertyDetail.propertyTypeMaster.code.equalsIgnoreCase(@org.egov.ptis.constants.PropertyTaxConstants@PROPTYPE_CENTRAL_GOVT)) && property.propertyDetail.floorDetails.isEmpty())}">
		<tr>
			<td class="greybox" width="5%">&nbsp;</td>
			<td class="greybox" width="25%"><s:text name="constCompl.date"></s:text> :</td>
			<td class="greybox">
				<s:date name="basicProp.propCreateDate"
					var="docFormat" format="dd/MM/yyyy" /> <span class="bold"><s:property
					default="N/A" value="%{docFormat}" /> </span>
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
	function loadOnStartUp() {
		enableFieldsForPropTypeView();
		enableAppartnaumtLandDetailsView();
		enableOrDisableSiteOwnerDetails(jQuery('input[name="property.propertyDetail.structure"]'));
		enableOrDisableBPADetails(jQuery('input[name="property.propertyDetail.buildingPlanDetailsChecked"]'));
		toggleFloorDetailsView();
	}

	function enableAppartnaumtLandDetailsView() {
		if (document.forms[0].appurtenantLandChecked.checked == true) {
			jQuery('tr.vacantlanddetaills').show();
			jQuery('#appurtenantRow').show();
			jQuery('tr.floordetails').show();
			jQuery('tr.extentSite').hide();
		} else {
			enableFieldsForPropTypeView();
		}
	}

	function enableFieldsForPropTypeView() {
		var propType = '<s:property value="%{property.propertyDetail.propertyTypeMaster.type}"/>';
			if (propType != "select") {
			//onChangeOfPropertyTypeFromMixedToOthers(propType);
			if (propType == "Vacant Land") {
				jQuery('tr.floordetails').hide();
				jQuery('tr.vacantlanddetaills').show();
				jQuery('tr.construction').hide();
				jQuery('tr.amenities').hide();
				jQuery('#appurtenantRow').hide();
				jQuery('tr.extentSite').hide();
				jQuery('tr.appurtenant').hide();
			} else {
				jQuery('tr.floordetails').show();
				jQuery('tr.vacantlanddetaills').hide();
				jQuery('tr.construction').show();
				jQuery('tr.amenities').show();
				jQuery('#appurtenantRow').hide();
				jQuery('tr.extentSite').show();
				jQuery('tr.appurtenant').show();
			}
		}
	}

	function toggleFloorDetailsView() {
		var propType = '<s:property value="%{property.propertyDetail.propertyTypeMaster.type}"/>';
		if (propType == "Vacant Land") {
			jQuery('tr.floordetails').hide();
		} else {
			jQuery('tr.floordetails').show();
		}
		if (propType == "Apartments") {
			alert("Please select Apartment/Complex Name");
		}
	}

	//hide rows and columns of fields
	jQuery('td.siteowner').hide();
	jQuery('tr.bpddetails').hide();
	jQuery('tr.vacantlanddetaills').hide();
    
</script>