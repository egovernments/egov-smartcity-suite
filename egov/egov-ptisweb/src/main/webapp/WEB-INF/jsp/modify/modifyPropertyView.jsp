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
				<span class="bold"> Heading Not decided</span>
			</div>
		</td>
	</tr>
	<tr>
		<td class="greybox" width="5%">&nbsp;</td>
		<td class="greybox" width="25%"><s:text name="extent.site"/> :</td>
		<td class="greybox" width="">
		   <span class="bold"><s:property value="%{propertyDetail.extentSite}" default="N/A"/></span>
		</td>
		<td class="greybox" width="25%"><s:text name="vacantland.assmtno"/> :</td>
		<td class="greybox">
			<span class="bold"><s:property value="%{basicProp.vacantLandAssmtNo}" default="N/A"/></span>
		</td>
	</tr>
	
	<tr>
		<td class="greybox" width="5%">&nbsp;</td>
		<td class="greybox" width="25%"><s:text name="extent.appurtntland"/> :</td>
		<td class="greybox" width="">
			<span class="bold"><s:property value="%{propertyDetail.extentAppartenauntLand}" default="N/A"/></span>
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
			<span class="bold"><s:property value="%{basicProp.regdDocDate}" default="N/A"/></span>
		</td>
	</tr>
	
	<tr>
		<td class="greybox" width="5%">&nbsp;</td>
		<td class="greybox" width="25%"><s:text name="building.permNo"></s:text> :</td>
		<td class="greybox" width="">
			<span class="bold"><s:property value="%{buildingPermissionNo}" default="N/A"/></span>
		</td>
		<td class="greybox" width="25%"><s:text name="buildingpermdate"></s:text> :</td>
		<td class="greybox">
			<span class="bold"><s:property value="%{buildingPermissionDate}" default="N/A"/></span>
		</td>

	</tr>
	<!-- Amenities section -->
	
	<tr>
		<td colspan="5">
			<div class="headingsmallbg">
				<span class="bold"> <s:text name="amenities"></s:text> </span>
			</div>
		</td>
	</tr>
	<tr>
		<td colspan="5">
		  <table width="100%" class="checkbox-section">
	        <tr>
	            <td width="10%"></td>
				<td width="20%" align="right">
				  <label> Lift <s:checkbox name="propertyDetail.lift" id="propertyDetail.lift" disabled="true"/></label>
				  <br/> <label> Drainage <s:checkbox name="propertyDetail.drainage" id="propertyDetail.drainage" disabled="true"/></label>
				  <br/> <label>Cable Connection <s:checkbox name="propertyDetail.cable" id="propertyDetail.cable" disabled="true"/></label> 
				</td>
				<td width="20%" align="right">
				  <label>Toilets <s:checkbox name="propertyDetail.toilets" id="propertyDetail.toilets" disabled="true"/> </label>
				  <br/> <label>Electricity <s:checkbox name="propertyDetail.electricity" id="propertyDetail.electricity" disabled="true"/> </label>
				</td>
				<td width="20%" align="right">
				  <label>Water Tap <s:checkbox name="propertyDetail.waterTap" id="propertyDetail.waterTap" disabled="true"/></label> 
				   <br/> <label>Attached Bathroom <s:checkbox name="propertyDetail.attachedBathRoom" id="propertyDetail.attachedBathRoom" disabled="true"/> </label>
			    </td>
			    <td width="20%" align="right">
				  <label>Super Structure <s:checkbox name="propertyDetail.structure" id="propertyDetail.structure" disabled="true"/></label>
				  <br/>  <label>Water Harvesting <s:checkbox name="propertyDetail.waterHarvesting" id="propertyDetail.waterHarvesting" disabled="true"/></label>
			    </td>
			    <td width="10%"></td>
			</tr>
			
		   </table>
		</td>
	</tr>
	<!-- Floor type details -->
	
	<tr>
		<td colspan="5">
			<div class="headingsmallbg">
				<span class="bold"><s:text name="title.constructiontypes"/></span>
			</div>
		</td>
	</tr>
	
	<tr>
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
	
	<tr>
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
	
	<!-- Ownership section -->
	
	<tr>
		<td colspan="5">
			<div class="headingsmallbg">
				<span class="bold"><s:text name="title.ownership"/></span>
			</div>
		</td>
	</tr>
	
	<tr>
		<td class="bluebox">&nbsp;</td>
		<td class="bluebox" width="25%"><s:text name="ModifyReason"></s:text> :</td>
		<td class="greybox" width="">
			<span class="bold"><s:property value="%{propertyDetail.propertyMutationMaster.mutationName}" default="N/A"/></span>
		</td>
	</tr>
	
	<tr>
		<td class="greybox" width="5%">&nbsp;</td>
		<td class="greybox" width="25%"><s:text name="ownership.type"></s:text> :</td>
		<td class="greybox" width="">
			<span class="bold"><s:property value="%{propertyDetail.propertyTypeMaster.type}" default="N/A"/></span>
		</td>
	</tr>
	
	<tr>
		<td class="greybox" width="5%">&nbsp;</td>
		<td class="bluebox">
			<div id="plotArea">
				<s:text name="PlotArea"/> :
			</div>
			<div id="undivArea">
				<s:text name="undivArea"/> :
			</div>
		</td>
		<td class="bluebox" colspan="2">
			<span class="bold"><s:property value="%{propertyDetail.sitalArea.area}" default="N/A"/></span>
		</td>
		<td class="bluebox">&nbsp;</td>
	</tr>
	
	<tr>
		<td class="greybox">&nbsp;</td>
		<td class="greybox" width="25%"><s:text name="apartcomplex.name"></s:text> :</td>
		<td class="greybox" width="">
			
		</td>
	</tr>
	<tr id="floorHeaderRow">
			<td colspan="5" width="5%">
				<div class="headingsmallbg">
					<span class="bold"><s:text name="FloorDetailsHeader" />
					</span>
				</div>
			</td>
	 </tr>
	
	<!-- Floor Details Section -->
		
	<tr>
		<td colspan="5">
			<div align="center">
				<%@ include file="../common/FloorView.jsp"%>
				<br/>
			</div>
		</td>
	</tr>
	<tr>
		<td colspan="5">
			<div class="headingsmallbg">
				<s:text name="docsectiontitle" /> 
			</div>
		</td>
	</tr>
	<tr>
		<td colspan="5">
			<table class="tablebottom" id="nameTable" width="100%" border="0" cellpadding="0" cellspacing="0">
				<tbody>
					<tr>
						<th class="bluebgheadtd"><s:text name="doctable.docenclosed" /></th>
						<th class="bluebgheadtd"><s:text name="doctable.doctype" /></th>
						<th class="bluebgheadtd"><s:text name="doctable.docdate" /></th>
						<th class="bluebgheadtd"><s:text name="doctable.docdetails" /></th>
						<th class="bluebgheadtd">Upload File</th>
					</tr>
					<s:iterator value="documentTypes" status="status" var="documentType">
						<tr>
							<td class="blueborderfortd" align="center">
							  <s:checkbox name="documents[%{#status.index}].enclosed" disabled="true"/>
							</td>
							<td class="blueborderfortd" style="text-align:left">
							  <s:property value="%{name}"/>
							</td>
							<td class="blueborderfortd" align="center">
								<s:date name="%{documents[#status.index].docDate}" format="dd/MM/yyyy" var="documentDate"/>
								<s:property value="%{#documentDate}"/>
							</td>
							<td class="blueborderfortd" align="center">
								<s:property value="%{documents[#status.index].description}"/>
							</td>
							<td class="blueborderfortd" align="center">
								<s:iterator value="%{documents[#status.index].files}">
									<s:property value="%{fileName}"/>
								</s:iterator>
							</td>
						</tr>
					</s:iterator>
				</tbody>
			</table>
		</td>
	</tr>
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
