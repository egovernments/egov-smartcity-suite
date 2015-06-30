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
					value="%{basicProperty.upicNo}" /> </span>						
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
					value="%{propertyAddress}" /> </span>
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
		   <span class="bold"><s:property value="%{referenceProperty.propertyDetail.extentSite}" default="N/A"/></span>
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
			<span class="bold"><s:property value="%{referenceProperty.propertyDetail.extentAppartenauntLand}" default="N/A"/></span>
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
			<span class="bold"><s:property value="%{basicProperty.regdDocDate}" default="N/A"/></span>
		</td>
	</tr>
	<tr>
		<td class="greybox" width="5%">&nbsp;</td>
		<td class="greybox" width="25%"><s:text name="building.permNo"></s:text> :</td>
		<td class="greybox" width="">
		   <s:textfield name="propStatVal.buildingPermissionNo" id="buildingPermissionNo" size="12" maxlength="12" onchange="trim(this,this.value);" onblur = "validNumber(this);checkZero(this);"></s:textfield>
		</td>
		<td class="greybox" width="25%"><s:text name="buildingpermdate"></s:text> :</td>
		<td class="greybox">
		  <s:textfield name="propStatVal.buildingPermissionDate"  cssClass="datepicker" id="buildingPermissionDate" size="12" maxlength="12"></s:textfield>
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
				  <label> Lift <s:checkbox name="referenceProperty.propertyDetail.lift" id="referenceProperty.propertyDetail.lift"/></label>
				  <br/> <label> Drainage <s:checkbox name="referenceProperty.propertyDetail.drainage" id="referenceProperty.propertyDetail.drainage"/></label>
				  <br/> <label>Cable Connection <s:checkbox name="referenceProperty.propertyDetail.cable" id="referenceProperty.propertyDetail.cable"/></label> 
				</td>
				<td width="20%" align="right">
				  <label>Toilets <s:checkbox name="referenceProperty.propertyDetail.toilets" id="referenceProperty.propertyDetail.toilets"/> </label>
				  <br/> <label>Electricity <s:checkbox name="referenceProperty.propertyDetail.electricity" id="referenceProperty.propertyDetail.electricity"/> </label>
				</td>
				<td width="20%" align="right">
				  <label>Water Tap <s:checkbox name="referenceProperty.propertyDetail.waterTap" id="referenceProperty.propertyDetail.waterTap"/></label> 
				   <br/> <label>Attached Bathroom <s:checkbox name="referenceProperty.propertyDetail.attachedBathRoom" id="referenceProperty.propertyDetail.attachedBathRoom"/> </label>
			    </td>
			    <td width="20%" align="right">
				  <label>Super Structure <s:checkbox name="referenceProperty.propertyDetail.structure" id="referenceProperty.propertyDetail.structure"/></label>
				  <br/>  <label>Water Harvesting <s:checkbox name="referenceProperty.propertyDetail.waterHarvesting" id="referenceProperty.propertyDetail.waterHarvesting"/></label>
			    </td>
			    <td width="10%"></td>
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
		<td class="greybox" width="25%"><s:text name="floortype"></s:text> <span
			class="mandatory1">*</span> :</td>
		<td class="greybox" width=""><s:select headerKey="-1"
				headerValue="%{getText('default.select')}" name="referenceProperty.propertyDetail.floorType.id"
				id="referenceProperty.propertyDetail.floorType.id" listKey="id" listValue="name"
				list="dropdownData.floorType" value="%{referenceProperty.propertyDetail.floorType.id}"
				cssClass="selectnew" onchange="makeMandatory();" />
		</td>
		<td class="greybox" width="25%"><s:text name="rooftype"></s:text> <span
			class="mandatory1">*</span> :</td>
		<td class="greybox" width=""><s:select headerKey="-1"
				headerValue="%{getText('default.select')}" name="referenceProperty.propertyDetail.roofType.id"
				id="referenceProperty.propertyDetail.roofType.id" listKey="id" listValue="name"
				list="dropdownData.roofType" value="%{referenceProperty.propertyDetail.roofType.id}"
				cssClass="selectnew" onchange="makeMandatory();" />
		</td>
	</tr>
	
	<tr>
		<td class="greybox" width="5%">&nbsp;</td>
		<td class="greybox" width="25%"><s:text name="walltype"></s:text> <span
			class="mandatory1">*</span> :</td>
		<td class="greybox" width=""><s:select headerKey="-1"
				headerValue="%{getText('default.select')}" name="referenceProperty.propertyDetail.wallType.id"
				id="referenceProperty.propertyDetail.wallType.id" listKey="id" listValue="name"
				list="dropdownData.wallType" value="%{referenceProperty.propertyDetail.wallType.id}"
				cssClass="selectnew" onchange="makeMandatory();" />
		</td>
		<td class="greybox" width="25%"><s:text name="woodtype"></s:text> <span
			class="mandatory1">*</span> :</td>
		<td class="greybox" width=""><s:select headerKey="-1"
				headerValue="%{getText('default.select')}" name="referenceProperty.propertyDetail.woodType.id"
				id="referenceProperty.propertyDetail.woodType.id" listKey="id" listValue="name"
				list="dropdownData.woodType" value="%{referenceProperty.propertyDetail.woodType.id}"
				cssClass="selectnew" onchange="makeMandatory();" />
		</td>
	</tr>
	
	
	
		   </table>
		</td>
	</tr>
	
	
</table>
