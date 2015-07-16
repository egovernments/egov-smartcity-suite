<!--
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
-->

<%@ page language="java" pageEncoding="UTF-8"%>
<%@ include file="/includes/taglibs.jsp"%>
<table width="100%" border="0" cellspacing="0" cellpadding="0">

    <tr>
		<td class="greybox2">&nbsp;</td>
		<s:if test="%{basicProp.upicNo!=null}">
			<td class="greybox"><s:text name="prop.Id" /> :</td>
			<td class="greybox"><span class="bold"><s:property
						default="N/A" value="%{basicProp.upicNo}" /> </span></td>
		</s:if>
	</tr>
	<tr>
		<td class="greybox" width="5%">&nbsp;</td>
		<td class="greybox" width="25%"><s:text name="ownership.type"></s:text>
			:</td>
		<td class="greybox" width=""><span class="bold"><s:property
					value="%{propertyDetail.propertyTypeMaster.type}" default="N/A" /></span>
		<td class="greybox" width="25%"><s:text name="property.type"></s:text>
			:</td>
		<td class="greybox" width=""><span class="bold"><s:property
					value="%{propertyDetail.categoryType}" default="N/A" /></span>
		</td>
	</tr>

	<tr>
		<td class="greybox2">&nbsp;</td>
		<td class="greybox"><s:text name="siteowner" /> :</td>
		<td class="greybox"><span class="bold"><s:property
					default="N/A" value="%{propertyDetail.siteOwner}" /> </span></td>
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
    <tr>
		<td colspan="5">
			<div class="headingsmallbg">
				<span class="bold"> Heading Not decided</span>
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
		<td class="greybox" width="25%"><s:text name="extent.site"/> :</td>
		<td class="greybox" width="">
		   <span class="bold"><s:property value="%{propertyDetail.extentSite}" default="N/A"/></span>
		</td>
		<td class="greybox" width="25%"><s:text name="vacantland.assmtno"/> :</td>
		<td class="greybox">
			<span class="bold"><s:property value="%{basicProperty.vacantLandAssmtNo}" default="N/A"/></span>
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
			<span class="bold"><s:property value="%{basicProperty.regdDocNo}" default="N/A"/></span>
		</td>
		<td class="greybox" width="25%"><s:text name="reg.docdate"/> :</td>
		<td class="greybox">
		<s:date name="%{basicProperty.regdDocDate}" var="regdDocDate" format="dd/MM/yyyy" />
			<span class="bold"><s:property value="%{#regdDocDate}" default="N/A"/></span>
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
		    <s:date name="%{buildingPermissionDate}" var="buildingPermNo" format="dd/MM/yyyy" />
			<span class="bold"><s:property value="%{#buildingPermNo}" default="N/A"/></span>
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
	
		<tr>
			<td colspan="5">
				<div align="center">
					<%@ include file="../common/FloorView.jsp"%>
				</div>
			</td>
		</tr>
	<%-- <tr>
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
	</tr> --%>

</table>
