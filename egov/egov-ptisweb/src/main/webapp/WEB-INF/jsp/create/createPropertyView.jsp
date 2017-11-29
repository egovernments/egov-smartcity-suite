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
<table border="0" cellspacing="0" cellpadding="0" class="table-fixed">

    <tr>
		<td class="greybox" width="5%">&nbsp;</td>
		<s:if test="%{basicProp.upicNo!=null}">
			<td class="greybox"><s:text name="prop.Id" /> :</td>
			<td class="greybox"><span class="bold"><s:property default="N/A" value="%{basicProp.upicNo}" /> </span></td>
		</s:if>
	</tr>
	<tr>
		<td class="greybox" width="5%">&nbsp;</td>
		<td class="greybox" width="20%"><s:text name="ownership.type"></s:text>:</td>
		<td class="greybox" width="20%"><span class="bold">
		     <s:property value="%{propertyDetail.propertyTypeMaster.type}" default="N/A" /></span>
		<td class="greybox" width="20%"><s:text name="property.type"></s:text>:</td>
		<td class="greybox" width="20%"><span class="bold">
		    <s:property value="%{@org.egov.ptis.constants.PropertyTaxConstants@PROPERTY_TYPE_CATEGORIES.get(propertyDetail.categoryType)}" default="N/A" /></span>
		</td>
	</tr>
	<tr>
		<td class="greybox" width="5%">&nbsp;</td>
		<td class="greybox apartmentRow" width="20%"><s:text name="apartcomplex.name"></s:text> :</td>
		<td class="greybox apartmentRow" width="20%"><span class="bold"><s:property
					value="%{propertyDetail.apartment.name}" default="N/A" /></span>
		</td>
	</tr>
	
	<tr>
		<td class="greybox" width="5%">&nbsp;</td>
		<td class="greybox" width="20%"><s:text name="label.property.department"></s:text>:</td>
		<td class="greybox" width="20%"><span class="bold">
		    <s:property value="%{propertyDetail.propertyDepartment.name}" default="N/A" /></span>
		</td>
	</tr>

	<tr>
		<td class="greybox2">&nbsp;</td>
		
	</tr>

	<!-- Owner details section -->
	<tr>
		<td colspan="5">
			<div class="headingsmallbg">
				<span class="bold"><s:text name="ownerdetails.title"></s:text></span>
			</div>
		</td>
	</tr>
	<tr>
		<td colspan="5">
			<div id="OwnerNameDiv">
				<%@ include file="../common/OwnerNameView.jsp"%>
			</div>
		</td>
	</tr>
	
	
	<!-- property address section -->
	
	<tr>
		<td>
			<div id="PropAddrDiv">
				<%@ include file="../common/PropAddressView.jsp"%>
			</div>
		</td>
	</tr>
	<tr>
		<td colspan="5">
			<div id="CorrAddrDiv">
				<%@ include file="../common/CorrAddressView.jsp"%>
			</div>
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
		<td class="greybox">&nbsp;</td>
		<td class="greybox" width="25%"><s:text name="ModifyReason"></s:text> :</td>
		<td class="greybox" width="">
			<span class="bold"><s:property value="%{propertyDetail.propertyMutationMaster.mutationName}" default="N/A"/></span>
		</td>
		<td class="greybox parentIndexText"><s:text name="prntPropAssessmentNum" />:</td>
		<td class="greybox parentIndexText"><span class="bold"><s:property value="%{parentIndex}" default="N/A"/></span></td>
	</tr>
	<tr class="occupancydetails">
		<td class="greybox" width="5%">&nbsp;</td>
		<td class="greybox" width="20%"><s:text
				name="certificationNumber" />:</td>
		<td class="greybox" width="20%"><span class="bold"> <s:if
					test='%{propertyDetail.occupancyCertificationNo == ""}'>N/A</s:if>
				<s:else>
					<s:property value="%{propertyDetail.occupancyCertificationNo}"
						default="N/A" />
				</s:else>
		</span></td>
		<td class="greybox" width="25%"><s:text name="certificationDate" />
			:</td>
		<td class="greybox"><s:date
				name="%{propertyDetail.occupancyCertificationDate}"
				var="occupancyCertificationDate" format="dd/MM/yyyy" /> <span
			class="bold"><s:property
					value="%{#occupancyCertificationDate}" default="N/A" /></span></td>
	</tr>
	<tr class="extentSite">
		<td class="greybox" width="5%">&nbsp;</td>
		<td class="greybox" width="20%"><s:text name="extent.site"/> :</td>
		<td class="greybox" width="20%">
		   <span class="bold"><s:property value="%{propertyDetail.sitalArea.area}" default="N/A"/></span>
		</td>
	</tr>
	<%-- <tr class="superStructureRow">
	<td class="greybox" width="5%">&nbsp;</td>
	<td class="greybox" width="20%"><s:text name="superstructure"/>:</td>
		<td class="greybox" width="20%">
		<s:if test="propertyDetail.structure == true">
				<span class="bold">Yes</span>
			</s:if> <s:else>
				<span class="bold">No</span>
			</s:else>
		</td>
	<td class="greybox siteowner"><s:text name="siteowner"/> :</td>
		<td class="greybox siteowner"><span class="bold"><s:property default="N/A" value="%{propertyDetail.siteOwner}" /> </span></td>
	</tr> --%>
	
	<s:if test="%{!basicProperty.regdDocNo.isEmpty()}">
		<tr>
			<td class="greybox" width="5%">&nbsp;</td>
			<td class="greybox" width="25%"><s:text name="reg.docno"/> :</td>
			<td class="greybox" width="">
				<span class="bold"><s:property value="%{basicProperty.regdDocNo}" default="N/A"/></span>
			</td>
			<td class="greybox" width="25%"><s:text name="reg.docdate"/> :</td>
			<td class="greybox">
			<s:date name="%{basicProperty.regdDocDate}" var="regdDocDate" format="dd/MM/yyyy" />
				<span class="bold"><s:property value="%{#regdDocDate}" default="N/A"/></span>
			</td>
		</tr>
	</s:if>
	
	
	<tr class="amenities">
		<td colspan="5">
			<div class="headingsmallbg">
				<span class="bold"> <s:text name="amenities"></s:text> </span>
			</div>
		</td>
	</tr>
	<tr class="amenities">
		<td colspan="5">
		 <%@ include file="../common/amenitiesViewForm.jsp"%>
		</td>
	</tr>
	<!-- Floor type details -->
	
	<tr class="construction">
		<td colspan="5">
			<div class="headingsmallbg">
				<span class="bold"><s:text name="title.constructiontypes"/></span>
			</div>
		</td>
	</tr>
	
	<tr class="construction">
		<td class="greybox" width="5%">&nbsp;</td>
		<td class="greybox" width="25%"><s:text name="floortype"></s:text> :</td>
		<td class="greybox" width="">
			<span class="bold"><s:property value="%{propertyDetail.floorType.name}" default="N/A"/></span>
		</td>
		<td class="greybox" width="25%"><s:text name="rooftype"></s:text> :</td>
		<td class="greybox" width="">
			<span class="bold"><s:property value="%{propertyDetail.roofType.name}" default="N/A"/></span>
		</td>
	</tr>
	
	<tr class="construction">
		<td class="greybox" width="5%">&nbsp;</td>
		<td class="greybox" width="25%"><s:text name="walltype"></s:text> :</td>
		<td class="greybox" width="">
			<span class="bold"><s:property value="%{propertyDetail.wallType.name}" default="N/A"/></span>
		</td>
		<td class="greybox" width="25%"><s:text name="woodtype"></s:text> :</td>
		<td class="greybox" width="">
			<span class="bold"><s:property value="%{propertyDetail.woodType.name}" default="N/A"/></span>
		</td>
	</tr>
	
	<tr class="floordetails">
			<td colspan="5" width="5%">
				<div class="headingsmallbg">
					<span class="bold"><s:text name="FloorDetailsHeader" />
					</span>
				</div>
			</td>
	 </tr>

	<tr class="floordetails">
		<td colspan="5">
			<div class="overflow-x-scroll">
				<%@ include file="../common/FloorView.jsp"%>
			</div>
		</td>
	</tr>
	<tr class="vacantlanddetaills">
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
				<%@ include file="../common/vacantLandView.jsp"%>
			</div>
		</td>
	</tr>

</table>