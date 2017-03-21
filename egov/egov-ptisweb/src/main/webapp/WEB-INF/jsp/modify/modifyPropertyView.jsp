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
	<%-- <tr class="superStructureRow">
		<td class="greybox">&nbsp;</td>
		<td class="greybox"><s:text name="superstructure"></s:text> :</td>
		<td class="greybox">
			<span class="bold"><s:checkbox name="propertyDetail.structure" disabled="true"/></span>
		</td>
		<td class="greybox siteowner"><s:text name="siteowner"></s:text>:</td>
		<td class="greybox siteowner">
			<span class="bold"><s:property value="%{propertyDetail.siteOwner}" default="N/A"/></span>
		</td>
	</tr> --%>
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
<script type="text/javascript">
	jQuery('td.siteowner').hide();
	jQuery('tr.vacantlanddetaills').hide();
</script>