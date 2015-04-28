#-------------------------------------------------------------------------------
# eGov suite of products aim to improve the internal efficiency,transparency, 
#    accountability and the service delivery of the government  organizations.
# 
#     Copyright (C) <2015>  eGovernments Foundation
# 
#     The updated version of eGov suite of products as by eGovernments Foundation 
#     is available at http://www.egovernments.org
# 
#     This program is free software: you can redistribute it and/or modify
#     it under the terms of the GNU General Public License as published by
#     the Free Software Foundation, either version 3 of the License, or
#     any later version.
# 
#     This program is distributed in the hope that it will be useful,
#     but WITHOUT ANY WARRANTY; without even the implied warranty of
#     MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
#     GNU General Public License for more details.
# 
#     You should have received a copy of the GNU General Public License
#     along with this program. If not, see http://www.gnu.org/licenses/ or 
#     http://www.gnu.org/licenses/gpl.html .
# 
#     In addition to the terms of the GPL license to be adhered to in using this
#     program, the following additional terms are to be complied with:
# 
# 	1) All versions of this program, verbatim or modified must carry this 
# 	   Legal Notice.
# 
# 	2) Any misrepresentation of the origin of the material is prohibited. It 
# 	   is required that all modified versions of this material be marked in 
# 	   reasonable ways as different from the original version.
# 
# 	3) This license does not grant any rights to any user of the program 
# 	   with regards to rights under trademark law for use of the trade names 
# 	   or trademarks of eGovernments Foundation.
# 
#   In case of any queries, you can reach eGovernments Foundation at contact@egovernments.org.
#-------------------------------------------------------------------------------
<%@ page language="java" pageEncoding="UTF-8"%>
<%@ include file="/includes/taglibs.jsp"%>
<table width="100%" border="0" cellspacing="0" cellpadding="0">
	<s:if test="%{basicProp.upicNo!=null}">
		<tr>
			<td class="greybox2">
				&nbsp;
			</td>
			<td class="greybox">
				<s:text name="prop.Id" />
				:
			</td>
			<td class="greybox">
				<span class="bold"><s:property default="N/A"
						value="%{basicProp.upicNo}" /> </span>
			</td>
			<td class="greybox" colspan="2">
				&nbsp;
			</td>
		</tr>
	</s:if>
	<tr>
		<td class="bluebox2" width="5%">
			&nbsp;
		</td>
		<td class="bluebox" width="">
			<s:text name="rsnForCreatin" />
			:
		</td>
		<td class="bluebox" width="">
			<span class="bold"><s:property default="N/A"
					value="%{basicProp.propertyMutationMaster.mutationName}" /> </span>
		</td>
		<td class="bluebox" width="">
			<s:text name="Zone" />
			:
		</td>
		<td class="bluebox" width="">
			<span class="bold"><s:property
					value="%{basicProp.boundary.parent.boundaryNum}" />-<s:property
					default="N/A" value="%{basicProp.boundary.parent.name}" /> </span>
		</td>
	</tr>
	<tr>
		<td class="greybox2" width="5%">
			&nbsp;&nbsp;&nbsp;
		</td>
		<td class="greybox" width="">
			<s:text name="prntPropIndexNum" />
			:
		</td>
		<td class="greybox">
			<b><s:property value="%{parentIndex}" default="N/A" />
			</b>
		</td>
		<td class="greybox" width="">
			<s:text name="Ward" />
			:
		</td>
		<td class="greybox" width="">
			<span class="bold"><s:property
					value="%{basicProp.boundary.boundaryNum}" />-<s:property
					default="N/A" value="%{basicProp.boundary.name}" /> </span>
		</td>
	</tr>
	<tr>
		<td class="bluebox2" width="5%">&nbsp;&nbsp;&nbsp;</td>
		<td class="bluebox" width=""><s:text name="partNo" /> :</td>
		<td class="bluebox" width="">
			<span class="bold"><s:property value="%{basicProp.partNo}" default="N/A"/></span>
		</td>
		<td class="bluebox" colspan="2">&nbsp;</td>
	</tr>
	<tr>
		<td colspan="5">
			<div id="OwnerNameDiv">
				<%@ include file="../common/OwnerNameView.jsp"%>
			</div>
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
				<span class="bold">+91 <s:property default="N/A"
						value="%{basicProp.address.mobileNo}" /> </span>
			</div>
		</td>
		<td class="bluebox">
			<s:text name="EmailAddress" />
			:
		</td>
		<td class="bluebox">
			<span class="bold"><s:property default="N/A"
					value="%{basicProp.address.emailAddress}" /> </span>
		</td>
	</tr>
	<tr>
		<td>
			<div id="PropAddrDiv">
				<%@ include file="../common/PropAddressView.jsp"%>
			</div>
		</td>
	</tr>
	<tr>
		<td class="bluebox2">
			&nbsp;
		</td>
		<td class="bluebox">
			<s:text name="ParcelID" />
			:
		</td>
		<td class="bluebox">
			<span class="bold"><s:property default="N/A"
					value="%{basicProp.gisReferenceNo}" /> </span>
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
			<s:text name="ExemptedFromTax" />
			:
		</td>
		<td class="greybox">
			<span class="bold"><s:property value="%{isExemptedFromTax}"
					default="N/A" />
			</span>
			<!--  how to retrieve this value -->
		</td>
		<td class="greybox">
			<s:text name="TaxExmRsn" />
			:
		</td>
		<td class="greybox">
			<span class="bold"> <s:property value="%{taxExemptReason}"
					default="N/A" /> </span>
			<!--  where is this value persisted -->
		</td>
	</tr>
	<tr>
		<td>
			<div id="CorrAddrDiv">
				<%@ include file="../common/CorrAddressView.jsp"%>
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
		<td class="greybox2">
			&nbsp;
		</td>
		<td class="greybox">
			<s:text name="NorthWard" />
			:
		</td>
		<td class="greybox">
			<span class="bold"><s:property default="N/A"
					value="%{basicProp.propertyID.northBoundary}" /> </span>
		</td>
		<td class="greybox">
			<s:text name="SouthWard" />
			:
		</td>
		<td class="greybox">
			<span class="bold"><s:property default="N/A"
					value="%{basicProp.propertyID.southBoundary}" /> </span>
		</td>
	</tr>
	<tr>
		<td class="bluebox2">
			&nbsp;
		</td>
		<td class="bluebox">
			<s:text name="EastWard" />
			:
		</td>
		<td class="bluebox">
			<span class="bold"><s:property default="N/A"
					value="%{basicProp.propertyID.eastBoundary}" /> </span>
		</td>
		<td class="bluebox">
			<s:text name="WestWard" />
			:
		</td>
		<td class="bluebox">
			<span class="bold"><s:property default="N/A"
					value="%{basicProp.propertyID.westBoundary}" /> </span>
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
			:
		</td>
		<td class="greybox">
			<span class="bold"><s:property default="N/A"
					value="%{propertyDetail.propertyTypeMaster.type}" /> </span>
		</td>
		<td class="greybox">
			&nbsp;
		</td>
		<td class="greybox">
			&nbsp;
		</td>
	</tr>

	<s:if
		test="%{propertyDetail.propertyTypeMaster.code.equalsIgnoreCase(@org.egov.ptis.constants.PropertyTaxConstants@PROPTYPE_RESD) ||
					propertyDetail.propertyTypeMaster.code.equalsIgnoreCase(@org.egov.ptis.constants.PropertyTaxConstants@PROPTYPE_NON_RESD) 
					 || propertyDetail.propertyTypeMaster.code.equalsIgnoreCase(@org.egov.ptis.constants.PropertyTaxConstants@PROPTYPE_OPEN_PLOT)}">
		<tr>
			<td class="greybox2">
				&nbsp;
			</td>
			<td class="greybox">
				<s:text name="PropertyTypeCategory" />
				:
			</td>
			<td class="greybox">
				<span class="bold"> <s:if
						test="%{propertyDetail.propertyTypeMaster.code.equalsIgnoreCase(@org.egov.ptis.constants.PropertyTaxConstants@PROPTYPE_RESD)}">
						<s:property default="N/A"
							value="%{@org.egov.ptis.constants.PropertyTaxConstants@RESIDENTIAL_PROPERTY_TYPE_CATEGORY[propertyDetail.extra_field5]}" />
					</s:if>
					<s:elseif test="%{propertyDetail.propertyTypeMaster.code.equalsIgnoreCase(@org.egov.ptis.constants.PropertyTaxConstants@PROPTYPE_OPEN_PLOT)}">
						<s:property default="N/A" value="%{@org.egov.ptis.constants.PropertyTaxConstants@OPEN_PLOT_PROPERTY_TYPE_CATEGORY[propertyDetail.extra_field5]}" />
					</s:elseif>
					 <s:else>
						<s:property default="N/A"
							value="%{@org.egov.ptis.constants.PropertyTaxConstants@NON_RESIDENTIAL_PROPERTY_TYPE_CATEGORY[propertyDetail.extra_field5]}" />
					</s:else> </span>
			</td>
			<td class="greybox">
				&nbsp;
			</td>
			<td class="greybox">
				&nbsp;
			</td>
		</tr>
	</s:if>
	<tr>
		<td class="bluebox2">
			&nbsp;
		</td>
		<s:if test="%{propertyDetail.extra_field5 == 'RESIDENTIAL_FLATS'}">
			<td class="bluebox">
				<s:text name="undivArea" />
				:
			</td>
		</s:if>
		<s:else>
			<td class="bluebox">
				<s:text name="PlotArea" />
				:
			</td>
		</s:else>
		<td class="bluebox">
			<span class="bold"><s:property default="N/A"
					value="%{propertyDetail.sitalArea.area}" /> </span>
		</td>

		<td class="bluebox">
			&nbsp;
		</td>
		<td class="bluebox">
			&nbsp;
		</td>
	</tr>
	<s:if test="%{propertyDetail.extra_field5 == @org.egov.ptis.constants.PropertyTaxConstants@PROPTYPE_CAT_RESD_CUM_NON_RESD}">
		<tr>
			<td class="bluebox2">
				&nbsp;
			</td>					
			<td class="bluebox">
				<s:text name="nonResPlotArea" />
				:
			</td>
			<td class="bluebox">
				<span class="bold"><s:property default="N/A"
						value="%{propertyDetail.nonResPlotArea.area}" /> </span>
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
		<td class="greybox2">
			&nbsp;
		</td>
		<td class="greybox">
			<s:text name="locationFactor" />
			:
		</td>
		<td class="greybox">
			<span class="bold">
				<s:property default="N/A" value="%{propertyCategory.categoryName}" /> 
			</span>
			<s:hidden name="propertyDetail.extra_field6" value="%{propertyDetail.extra_field6}" />
		</td>
		<td class="greybox">
			&nbsp;
		</td>
		<td class="greybox">
			&nbsp;
		</td>
	</tr>
	<s:if
		test="%{propertyDetail.propertyTypeMaster.code.equalsIgnoreCase(@org.egov.ptis.constants.PropertyTaxConstants@PROPTYPE_OPEN_PLOT)
		|| propertyDetail.floorDetails.size()==0}">

		<tr>
			<td class="bluebox2">
				&nbsp;
			</td>
			<td class="bluebox">
				<s:text name="OccupationDate" />
				:
			</td>
			<td class="bluebox">
				<span class="bold"><s:date
						name="%{basicProperty.propCreateDate}" format="dd/MM/yyyy" />
				</span>
			</td>
			<td class="bluebox">
				&nbsp;
			</td>
			<td class="bluebox">
				&nbsp;
			</td>
		</tr>				
	</s:if>
	<s:if
		test="%{propertyDetail.propertyTypeMaster.code.equalsIgnoreCase(@org.egov.ptis.constants.PropertyTaxConstants@PROPTYPE_OPEN_PLOT)
		|| propertyDetail.propertyTypeMaster.code.equalsIgnoreCase(@org.egov.ptis.constants.PropertyTaxConstants@PROPTYPE_STATE_GOVT) 
		|| propertyDetail.propertyTypeMaster.code.equalsIgnoreCase(@org.egov.ptis.constants.PropertyTaxConstants@PROPTYPE_CENTRAL_GOVT)}">
		<tr>
			<td class="greybox2">&nbsp;</td>
			<td class="greybox"><s:text name="GenWaterRate" /> :</td>
			<td class="greybox"><span class="bold"><s:property
						value="%{genWaterRate}" default="N/A" /></span></td>
			<td class="greybox">&nbsp;</td>
			<td class="greybox">&nbsp;</td>
		</tr>
	</s:if>
	<s:if
		test="%{propertyDetail.propertyTypeMaster.code.equalsIgnoreCase(@org.egov.ptis.constants.PropertyTaxConstants@PROPTYPE_OPEN_PLOT)}">
		<tr>
			<td class="bluebox2">
				&nbsp;
			</td>
			<td class="bluebox">
				<s:text name="Usage" />
				<a onclick=openWindow('UsageMaster.jsp');> 
					<img src="../image/help.gif" style="border: none" /> 
				</a> :
			</td>
			<td class="bluebox">
				<span class="bold"><s:property default="N/A"
						value="%{propertyDetail.propertyUsage.usageName}" /> </span>
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
				<s:text name="Occupancy" />
				:
			</td>
			<td class="greybox">
				<span class="bold"><s:property default="N/A"
						value="%{propertyDetail.propertyOccupation.occupation}" /> </span>
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
				<s:text name="rent" />
				:
			</td>
			<td class="bluebox">
				<span class="bold"><s:property default="N/A"
						value="%{propertyDetail.extra_field2}" /> </span>
			</td>
			<td class="bluebox">
				&nbsp;
			</td>
			<td class="bluebox">
				&nbsp;
			</td>
		</tr>
		<tr>
			<td class="greybox2">&nbsp;</td>
			<td class="greybox"><s:text name="openPLotManualAlv" />	: </td>
			<td class="greybox"><span class="bold"><s:property default="N/A"
						value="%{propertyDetail.manualAlv}" /> </span>
			</td>
			<td class="greybox">&nbsp;</td>
			<td class="greybox">&nbsp;</td>
		</tr>
				<tr>
			<td class="bluebox2">&nbsp;</td>
			<td class="bluebox"><s:text name="OccupierName" />	: </td>
			<td class="bluebox"><span class="bold"><s:property default="N/A"
						value="%{propertyDetail.occupierName}" /> </span>
			</td>
			<td class="bluebox">&nbsp;</td>
			<td class="bluebox">&nbsp;</td>
		</tr>
	</s:if>
	<s:if
		test="%{propertyDetail.propertyTypeMaster.code.equalsIgnoreCase(@org.egov.ptis.constants.PropertyTaxConstants@PROPTYPE_STATE_GOVT) 
					|| propertyDetail.propertyTypeMaster.code.equalsIgnoreCase(@org.egov.ptis.constants.PropertyTaxConstants@PROPTYPE_CENTRAL_GOVT)}">
		<tr>
			<td class="greybox2" width="5%">
				&nbsp;
			</td>
			<td class="greybox">
				<s:text name="bldngCost" />
				:
			</td>
			<td class="greybox">
				<span class="bold"><s:property default="N/A"
						value="%{propertyDetail.extra_field3}" /> </span>
			</td>
			<td class="greybox">
				&nbsp;
			</td>
			<td class="greybox">
				&nbsp;
			</td>
		</tr>
	</s:if>
	<s:if
		test="%{propertyDetail.propertyTypeMaster.code.equalsIgnoreCase(@org.egov.ptis.constants.PropertyTaxConstants@PROPTYPE_CENTRAL_GOVT)}">
		<tr>
			<td class="bluebox2" width="5%">
				&nbsp;
			</td>
			<td class="bluebox">
				<s:text name="amenities" />
				:
			</td>
			<td class="bluebox">
				<span class="bold"><s:property default="N/A"
						value="%{amenities}" /> </span>
				<s:hidden name="amenities" value="%{amenities}"></s:hidden>
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
		<td class="greybox2">
			&nbsp;
		</td>
		<td class="greybox">
			<s:text name="AuthProp" />
			:
		</td>
		<td class="greybox">
			<span class="bold"><s:property default="N/A"
					value="%{basicProp.extraField1}" /> </span>
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
			:
		</td>
		<td class="bluebox">
			<span class="bold"><s:property default="N/A"
					value="%{extra_field2}" /> </span>
		</td>
		<td class="bluebox">
			&nbsp;
		</td>
		<td class="bluebox">
			&nbsp;
		</td>
	</tr>
	<c:if test="${docNumber!=null && docNumber!='' }">
		<tr>
		<td class="greybox"></td><td class="greybox"></td>	
	       <td class="greybox">
	       <span class="bold">
				<a
					href='#'
					target="_parent" 
					onclick="window.open('/egi/docmgmt/basicDocumentManager!viewDocument.action?moduleName=ptis&docNumber=${docNumber}'
					,'ViewDocuments','resizable=yes,scrollbars=yes,height=650,width=700,status=yes');">View Document</a>
	        </span>
	        </td>
	        <td class="greybox">&nbsp;</td><td class="greybox">&nbsp;</td>       
		</tr>
	</c:if>
	<s:if test="%{propertyDetail.propertyTypeMaster.code != 'OPEN_PLOT'  && propertyDetail.floorDetails.size()!=0}">
		<tr>
			<td colspan="5">
				<div class="headingsmallbg">
					<table width="35%" border="0" align="center" cellpadding="0"
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
					<%@ include file="../common/FloorView.jsp"%>
				</div>
			</td>
		</tr>
	</s:if>
	<tr>
		<td class="greybox">
			&nbsp;
		</td>
		<td class="greybox">
			<s:text name="allChangesDone" />
			:
		</td>
		<td class="greybox">
			<span class="bold">
				<s:if test="basicProp.allChangesCompleted != null && basicProp.allChangesCompleted == true">
					Yes
				</s:if>
				<s:else>
					<s:property default="N/A" value="%{basicProp.allChangesCompleted}" />
				</s:else> 
			</span>
		</td>
		<td class="greybox">
			&nbsp;
		</td>
		<td class="greybox">
			&nbsp;
		</td>
	</tr>

</table>
