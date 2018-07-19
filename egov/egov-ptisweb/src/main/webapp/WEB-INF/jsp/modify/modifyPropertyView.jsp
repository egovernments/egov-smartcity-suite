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
<script src="https://maps.googleapis.com/maps/api/js?key=AIzaSyCgxY6DqJ4TxnRfKjlZR8SfLSQRtOSTxEU"></script>
<script src="<cdn:url value='/resources/js/app/property-map.js?rnd=${app_release_no}'/>"></script>
<table width="100%" border="0" cellspacing="0" cellpadding="0" class="table-fixed">
	<s:if test="modifyRsn=='AMALG'">
		<tr>
			<td class="bluebox2">
				&nbsp;
			</td>
			<td class="bluebox">
				<s:text name="AmalgProp" />
				:
			</td>
			<td class="bluebox">
				<table width="40%" border="0" cellspacing="0" cellpadding="0">					
					<s:iterator value="amalgPropIds" status="AmalgPropStat">
						<s:if test="%{amalgPropIds[#AmalgPropStat.index] != null && amalgPropIds[#AmalgPropStat.index]!=''}">
							<tr>
								<td class="bluebox" align="center">
									<span class="bold">
										<s:property value="%{amalgPropIds[#AmalgPropStat.index]}" /> 
									</span>
								</td>
								<td class="bluebox">
								</td>
							</tr>
						</s:if>
					</s:iterator>
				</table>
			</td>
			<td class="bluebox">
				&nbsp;
			</td>
			<td class="bluebox">
				&nbsp;
			</td>
		</tr>
	</s:if>
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
			&nbsp;
		</td>
		<td class="greybox">
			&nbsp;
		</td>
	</tr>
	<tr>
		<td class="greybox" width="5%">
			&nbsp;
		</td>
		<td class="greybox">
			<s:text name="MobileNumber" />:
		</td>
		<td class="greybox">
			<span class="bold"><s:property value="%{basicProp.mobileNumber}" default="N/A"/></span>
		</td>
		<td class="bluebox" width="20%">
			&nbsp;
		</td>
		<td class="bluebox" width="20%">
			&nbsp;
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
		<td class="greybox" width="25%"><s:text name="reg.docno"/> :</td>
		<td class="greybox" width="">
			<span class="bold"><s:property value="%{basicProp.regdDocNo}" default="N/A"/></span>
		</td>
		<td class="greybox" width="25%"><s:text name="reg.docdate"/> :</td>
		<td class="greybox">
		<s:date name="%{basicProperty.regdDocDate}" var="regdDocDate" format="dd/MM/yyyy" />
			<span class="bold"><s:property value="%{#regdDocDate}" default="N/A"/></span>
		</td>
	</tr>
	<tr class="extentSite">
		<td class="greybox" width="5%">&nbsp;</td>
		<td class="greybox" width="25%"><s:text name="extent.site"/> :</td>
		<td class="greybox" width="">
		   <span class="bold"><s:property value="%{propertyDetail.sitalArea.area}" default="N/A"/></span>
		</td>
		<td class="greybox" width="25%"></td>
		<td class="greybox">
		</td>
	</tr>
	<tr>
		<td class="greybox" width="5%">&nbsp;</td>
		<td class="greybox" width="25%"><s:text name="ownership.type"></s:text> :</td>
		<td class="greybox" width="">
			<span class="bold"><s:property value="%{propertyDetail.propertyTypeMaster.type}" default="N/A"/></span>
		</td>
		<td class="greybox" width="25%"><s:text name="property.type"></s:text></td>
		<td class="greybox">
			<span class="bold"><s:property value="%{@org.egov.ptis.constants.PropertyTaxConstants@PROPERTY_TYPE_CATEGORIES.get(propertyDetail.categoryType)}" default="N/A"/></span>
		</td>
	</tr>
	<tr id="apartmentRow">
		<td class="greybox">&nbsp;</td>
		<td class="greybox apartmentRow"><s:text name="apartcomplex.name"></s:text> :</td>
		<td class="greybox apartmentRow">
			<span class="bold"><s:property value="%{propertyDetail.apartment.name}" default="N/A"/></span>
		</td>
		<td class="greybox"><s:text name="certificationNumber"></s:text>:</td>
		<td class="greybox">
			<span class="bold">
				<s:if test='%{propertyDetail.occupancyCertificationNo == ""}'>N/A</s:if>
				<s:else><s:property value="%{propertyDetail.occupancyCertificationNo}" default="N/A"/></s:else>
			</span>
		</td>
	</tr>
	<tr>
		<td class="greybox">&nbsp;</td>
		<td class="greybox"><s:text name="Zone"></s:text>:</td>
		<td class="greybox">
			<span class="bold">
				<s:property value="%{basicProperty.propertyID.zone.name}" default="N/A"/>
			</span>
		</td>
		<td class="greybox" colspan=2>&nbsp;</td>
	</tr>
	<tr>
		<td class="greybox">&nbsp;</td>
	    <td class="greybox"><s:text name="longitude"/> : </td>
	    <td class="greybox"><span class="bold"><s:property default="N/A" value="%{basicProperty.longitude}" /> </span></td>
	    <td class="greybox"><s:text name="latitude"/> : </td>
	    <td class="greybox"><span class="bold"><s:property default="N/A" value="%{basicProperty.latitude}" /> </span></td>
	</tr>
	<s:if test="%{basicProperty.latitude != null && basicProperty.longitude != null}">
	<tr>
			<td class="greybox">&nbsp;</td>
			<td class="greybox">&nbsp;</td>
			<td class="greybox">&nbsp;</td>
			<td class="greybox">&nbsp;</td>
			<td><input type="button" name="showMap" id="show-map"
						value="View On Map" class="buttonsubmit" data-toggle="modal" data-target="#myModal"/></td>
	</tr>
	</s:if>
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
				<%@ include file="../common/amenitiesView.jsp"%>
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
				<%@ include file="../common/constructionView.jsp"%>
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
			<div class="overflow-x-scroll">
				<%@ include file="../common/FloorView.jsp"%>
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
				<%@ include file="../common/vacantLandView.jsp"%>
			</div>
		</td>
	</tr>
	<s:if test="%{!documentTypes.isEmpty()}">
		<tr>
			<td colspan="5">
				<%@ include file="../common/DocumentUploadView.jsp"%>
			</td>
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
	<s:if test="%{propertyDetail.propertyTypeMaster.code.equalsIgnoreCase(@org.egov.ptis.constants.PropertyTaxConstants@PROPTYPE_OPEN_PLOT) || ((propertyDetail.propertyTypeMaster.code.equalsIgnoreCase(@org.egov.ptis.constants.PropertyTaxConstants@PROPTYPE_STATE_GOVT) 
		|| propertyDetail.propertyTypeMaster.code.equalsIgnoreCase(@org.egov.ptis.constants.PropertyTaxConstants@PROPTYPE_CENTRAL_GOVT)) && propertyDetail.floorDetails.isEmpty())}">
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
<div id="myModal" class="modal fade" role="dialog">
  <div class="modal-dialog modal-lg">
    <div class="modal-content">
      <div class="modal-header">
        <button type="button" class="close" data-dismiss="modal">&times;</button>
        <h4 class="modal-title">Property On Google Map</h4>
      </div>
			<div id="map-canvas" style="height:500px;width:500pxpx;"></div>      
			<div class="modal-footer">
        <button type="button" class="btn btn-default" data-dismiss="modal">Close</button>
      </div>
    </div>

  </div>
</div>
<script type="text/javascript">
	jQuery('td.siteowner').hide();
	jQuery('tr.vacantlanddetaills').hide();
	var lat = parseFloat('<s:property value="%{basicProperty.latitude}"/>');
    var lng = parseFloat('<s:property value="%{basicProperty.longitude}"/>');
	jQuery('#show-map').on('click',initialize(lat, lng));
</script>