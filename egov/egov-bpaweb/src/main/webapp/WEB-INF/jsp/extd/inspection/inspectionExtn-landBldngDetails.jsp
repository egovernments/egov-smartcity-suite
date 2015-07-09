#-------------------------------------------------------------------------------
# <!-- #-------------------------------------------------------------------------------
# # eGov suite of products aim to improve the internal efficiency,transparency, 
# #    accountability and the service delivery of the government  organizations.
# # 
# #     Copyright (C) <2015>  eGovernments Foundation
# # 
# #     The updated version of eGov suite of products as by eGovernments Foundation 
# #     is available at http://www.egovernments.org
# # 
# #     This program is free software: you can redistribute it and/or modify
# #     it under the terms of the GNU General Public License as published by
# #     the Free Software Foundation, either version 3 of the License, or
# #     any later version.
# # 
# #     This program is distributed in the hope that it will be useful,
# #     but WITHOUT ANY WARRANTY; without even the implied warranty of
# #     MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
# #     GNU General Public License for more details.
# # 
# #     You should have received a copy of the GNU General Public License
# #     along with this program. If not, see http://www.gnu.org/licenses/ or 
# #     http://www.gnu.org/licenses/gpl.html .
# # 
# #     In addition to the terms of the GPL license to be adhered to in using this
# #     program, the following additional terms are to be complied with:
# # 
# # 	1) All versions of this program, verbatim or modified must carry this 
# # 	   Legal Notice.
# # 
# # 	2) Any misrepresentation of the origin of the material is prohibited. It 
# # 	   is required that all modified versions of this material be marked in 
# # 	   reasonable ways as different from the original version.
# # 
# # 	3) This license does not grant any rights to any user of the program 
# # 	   with regards to rights under trademark law for use of the trade names 
# # 	   or trademarks of eGovernments Foundation.
# # 
# #   In case of any queries, you can reach eGovernments Foundation at contact@egovernments.org.
# #------------------------------------------------------------------------------- -->
#-------------------------------------------------------------------------------
<%@ taglib prefix="s" uri="/WEB-INF/taglibs/struts-tags.tld"%>
<%@page pageEncoding="utf-8" contentType="text/html; charset=utf-8"%>

<div id="inspLandDetails" class="formmainbox"> 
	<div align="center">
	 	<div id="lnd_DetailsDiv">
		<table  width="100%" border="0" cellspacing="0" cellpadding="0" class="tablebottom" id="land_DetailsTbl">
			  <tr>
			    <td width="20%">&nbsp;</td>	
			   <td width="8%">&nbsp;</td>	
			   <td align="center"><h1 class="subhead" ><s:text name="inspectionExtn.landDetails.lbl"/></h1></td>
			 </tr>
		 
			 <tr>
	 			<td class="bluebox" width="20%">&nbsp;</td>
				<td class="bluebox" width="13%" id="mandatoryfields"><s:text name="inspectionlbl.landDetails.zoning"/><span class="mandatory">*</span></td> 
	   			<td class="bluebox"><s:radio name="landZoning" id="landZoning" list="%{landZoningMap}" /></td>

				<td class="bluebox"><s:text name="inspectionlbl.landDetails.landUsage"/><span class="mandatory">*</span></td> 
				<td class="bluebox" ><s:select id="landUsage" name="landUsage" list="dropdownData.landUsageList" listKey="id" listValue="name"  value="landUsage.id" headerKey="-1" headerValue="----Choose------"/></td>
			
			 </tr>
		 
			 <tr> 
			 	<td class="greybox" width="20%">&nbsp;</td>
				<td class="greybox"><s:text name="inspectionlbl.landDetails.layoutType"/><span class="mandatory">*</span></td> 
				<td class="greybox"><s:select id="layoutType" name="layoutType" list="dropdownData.layoutTypeList" listKey="id" listValue="name"  value="layoutType.id" headerKey="-1" headerValue="----Choose------" onchange="osrLandMandatory();" /></td>
			 
				<td class="greybox" width="13%" id="mandatoryfields"><s:text name="inspectionlbl.landDetails.minPlotExtnt"/><span class="mandatory">*</span></td> 
				<td class="greybox" id="numbers"><s:textfield id="lndMinPlotExtent" name="lndMinPlotExtent" value="%{lndMinPlotExtent}" /></td>   			
				<!-- 
					<s:if test="%{!isUserMappedToSurveyorRole()}">
						<td class="greybox" id="mandatoryfields"><s:text name="inspectionlbl.landDetails.prpsdPltExtnt"/><span class="mandatory">*</span></td>
						<td class="greybox" id="numbers"><s:textfield id="lndProposedPlotExtent" name="lndProposedPlotExtent" value="%{lndProposedPlotExtent}" /></td>
					</s:if>
					<s:else>
						<td class="greybox" width="20%">&nbsp;</td>
						<td class="greybox" >&nbsp;</td>
					</s:else>
				 -->
			</tr>
	         
	       <tr id="osrLandRow" style="display: none;">
				<td class="bluebox" width="20%">&nbsp;</td>
				<td class="bluebox" width="13%" id="osrLandLbl" style="display: none;"><s:text name="inspectionlbl.landDetails.osr"/><span class="mandatory">*</span></td> 
				<td class="bluebox" id="numbers"><s:textfield id="lndOsrLandExtent" name="lndOsrLandExtent" style="display: none;" value="%{lndOsrLandExtent}" onblur="guideLneMandatory();" /></td>   			
				
				<td class="bluebox" id="lndGuideLineLbl" style="display: none;"><s:text name="inspectionlbl.landDetails.guideLine"/><span class="mandatory">*</span></td>
				<td class="bluebox" id="numbers"><s:textfield id="lndGuideLineValue" name="lndGuideLineValue" style="display: none;" value="%{lndGuideLineValue}" /><span class="mandatory" id="guideLnMsg" style="display:none">10 % of OSR Charges have to be paid</span></td>
				<td class="bluebox" >&nbsp;</td>
			</tr>
			
			 <s:if test="%{isSurveyor==false}">
			  	<tr id="showToOfficialRow" style="display: none;">
					<td class="greybox" width="20%">&nbsp;</td>
					<td class="greybox" width="13%"><s:text name="inspectionlbl.landDetails.penaltyPeriod"/></td> 
					<td class="greybox"><s:select id="lndPenaltyPeriod" name="lndPenaltyPeriod" list="dropdownData.penaltyPeriodList"  value="lndPenaltyPeriod" headerKey="" headerValue="----Choose------"/></td>   			
					
					<td class="greybox" ><s:text name="inspectionlbl.landDetails.regularizationArea"/></td> 
					<td class="greybox" id="numbers"><s:textfield id="lndRegularizationArea" name="lndRegularizationArea" value="%{lndRegularizationArea}" /></td>
				</tr>
				
				<tr>
				 	<td class="bluebox">&nbsp;</td>
					<td class="bluebox"><s:text name="inspectionlbl.landDetails.regularisationCharges"/><span class="mandatory">*</span></td> 
					<td class="bluebox" ><s:select name="lndIsRegularisationCharges" id="lndIsRegularisationCharges" headerKey="-1" headerValue="--Choose--" list="#{'true':'YES','false':'NO'}" value="%{lndIsRegularisationCharges}" /></td>
				
					<td class="bluebox"></td> 
		   			<td class="bluebox"></td>
			 	</tr>
			</s:if>
			
		 </table>
		 </div>
		 
	 	 <div id="bldng_DetailsDiv">
	 	 <table  width="100%" border="0" cellspacing="0" cellpadding="0" class="tablebottom" id="bldng_DetailsTbl">
			  <tr>
			    <td width="20%">&nbsp;</td>	
			   <td width="8%">&nbsp;</td>	
			   <td align="center"><h1 class="subhead" ><s:text name="inspectionExtn.bldngDetails.lbl"/></h1></td> 
			 </tr>
		
			 <tr>
	 			<!-- 
	 			 <s:if test="%{!isUserMappedToSurveyorRole()}">
	 			<td class="bluebox" width="13%" id="mandatoryfields"><s:text name="inspectionlbl.bldngDetails.zoning"/><span class="mandatory">*</span></td> 
	   			<td class="bluebox"><s:radio name="buildingZoning" id="buildingZoning" list="%{bldngZoningMap}" /></td> 
			  </s:if>
			   -->
			    <td class="greybox" width="20%">&nbsp;</td>	
			    <td class="greybox"><s:text name="inspectionlbl.bldngDetails.buildingType"/><span class="mandatory">*</span></td> 
				<td class="greybox" ><s:select id="buildingType" name="buildingType" list="dropdownData.buildingTypeList" listKey="id" listValue="name"  value="buildingType.id" headerKey="-1" headerValue="----Choose------" onchange="makeDwelUnit_CommerResdntlMandatory()"/></td>
			   
			   	<td class="greybox" width="13%" id="mandatoryfields"><s:text name="inspectionlbl.bldngDetails.roadWidth"/><span class="mandatory">*</span></td> 
	   			<td class="greybox" id="numbers"><s:textfield id="bldngRoadWidth" name="bldngRoadWidth" value="%{bldngRoadWidth}" onblur="strmWaterMandatory();" /></td>
			
			 </tr>
		 
		   	<tr> 
	 			<td class="bluebox" width="20%">&nbsp;</td>
				<td class="bluebox" width="13%" id="mandatoryfields"><s:text name="inspectionlbl.bldngDetails.totalBldpArea"/><span class="mandatory">*</span></td> 
	   			<td class="bluebox" id="numbers"><s:textfield id="bldngBuildUpArea" name="bldngBuildUpArea" value="%{bldngBuildUpArea}" /></td>   			
				<td class="bluebox"  id="mandatoryfields"><s:text name="inspectionlbl.bldngDetails.prpsdPltFrntg"/><span class="mandatory">*</span></td>
				<td class="bluebox" id="numbers"><s:textfield id="bldngProposedPlotFrntage" name="bldngProposedPlotFrntage" value="%{bldngProposedPlotFrntage}" /></td>
	         </tr>
	         
	         <tr> 
	 			<td class="greybox" width="20%">&nbsp;</td>
				<td class="greybox" id="mandatoryfields"><s:text name="inspectionlbl.bldngDetails.cmpndWall"/><span class="mandatory">*</span></td>
				<td class="greybox" id="numbers"><s:textfield id="bldngCompoundWall" name="bldngCompoundWall" value="%{bldngCompoundWall}" /></td>
				
				<td class="greybox" width="13%" id="mandatoryfields"><s:text name="inspectionlbl.bldngDetails.wellOht.sumpTankArea"/><span class="mandatory">*</span></td> 
	   			<td class="greybox" id="numbers"><s:textfield id="bldngWellOht_SumpTankArea" name="bldngWellOht_SumpTankArea" value="%{bldngWellOht_SumpTankArea}"  /></td>   			
	         </tr>
	         
	         <s:if test="%{!isUserMappedToSurveyorRole()}">
	         	<tr id="strmWaterRow" style="display: none;">
		 			<td class="bluebox" width="20%">&nbsp;</td>
		 			<td class="bluebox" width="13%" id="stormWaterLbl"><s:text name="inspectionlbl.bldngDetails.stormWaterDrain"/><span class="mandatory">*</span></td> 
		   			<td class="bluebox" id="numbers"><s:select id="bldngStormWaterDrain" name="bldngStormWaterDrain" list="dropdownData.stormWaterDrainList" listKey="id" listValue="dimension"  value="bldngStormWaterDrain.id" headerKey="-1" headerValue="----Choose------"/></td>
		   			
		   			<td class="bluebox" width="20%">&nbsp;</td>
		 			<td class="bluebox" width="20%">&nbsp;</td>
	   			</tr>
	   		</s:if>	
	        <tr id="dwlUnitRow" style="display: none;">
						<td class="greybox" width="20%">&nbsp;</td>
						<td class="greybox" width="13%" id="mandatoryfields" name="dwlUnitLbl"><s:text name="inspectionlbl.bldngDetails.dwellingUnit"/><span class="mandatory">*</span></td> 
			   			<td class="greybox" id="numbers"><s:textfield id="dwellingUnit" name="dwellingUnit" value="%{dwellingUnit}"  /></td>
			        	<td class="greybox" width="20%">&nbsp;</td>
			 			<td class="greybox" width="20%">&nbsp;</td>
	         </tr>
         	
	          <tr id="resdntlCmrclRow" style="display : none;">
	 			<td class="bluebox" width="20%">&nbsp;</td>
	 			<td class="bluebox" width="13%"><s:text name="inspectionlbl.bldngDetails.residential"/><span class="mandatory">*</span></td> 
	   			<td class="bluebox" id="numbers"><s:textfield id="bldngResidential" name="bldngResidential" value="%{bldngResidential}"  /></td>
	 			
				<td class="bluebox" width="13%"><s:text name="inspectionlbl.bldngDetails.commercial"/><span class="mandatory">*</span></td> 
	   			<td class="bluebox" id="numbers"><s:textfield id="bldngCommercial" name="bldngCommercial" value="%{bldngCommercial}"  /></td>
	         </tr> 
	         
	          <s:if test="%{serviceTypeCode==@org.egov.bpa.constants.BpaConstants@DEMOLITIONRECONSTRUCTIONCODE}">
	         <tr> 
	 			<td class="greybox" width="20%">&nbsp;</td>
				<td class="greybox"><s:text name="inspectionlbl.bldngDetails.gflrTiledFlrArea"/></td>
				<td class="greybox" id="numbers"><s:textfield id="bldngGFloor_TiledFloor" name="bldngGFloor_TiledFloor" value="%{bldngGFloor_TiledFloor}" /></td>
				
				<td class="greybox" width="13%" ><s:text name="inspectionlbl.bldngDetails.gflrOtherTypes"/></td> 
	   			<td class="greybox" id="numbers"><s:textfield id="bldngGFloor_OtherTypes" name="bldngGFloor_OtherTypes" value="%{bldngGFloor_OtherTypes}" /></td>   			
	         </tr>
         
	         <tr>
	         	<td class="bluebox" width="20%">&nbsp;</td>
	         	<td class="bluebox" width="13%"><s:text name="inspectionlbl.bldngDetails.frstFlrTotalArea"/></td> 
	   			<td class="bluebox" id="numbers"><s:textfield id="bldngFrstFloor_TotalArea" name="bldngFrstFloor_TotalArea" value="%{bldngFrstFloor_TotalArea}" /></td>
	 			
	 			<td class="bluebox" width="13%" id="mandatoryfields"><s:text name="inspectionlbl.bldngDetails.prpsdBldngArea"/><span class="mandatory">*</span></td> 
	   			<td class="bluebox" id="numbers"><s:textfield id="bldngProposedBldngArea" name="bldngProposedBldngArea" value="%{bldngProposedBldngArea}" /></td>
	 		</tr>
	 		</s:if>	
	         
	          <s:if test="%{isSurveyor==false}">
			  	<tr id="showToOfficialRow">
					<td class="greybox" width="20%">&nbsp;</td>
					<td class="greybox" width="13%"><s:text name="inspectionlbl.bldngDetails.improvementCharges"/><span class="mandatory">*</span></td> 
					<td class="greybox"><s:select name="bldngIsImprovementCharges" id="bldngIsImprovementCharges" headerKey="-1" headerValue="--Choose--" list="#{'true':'YES','false':'NO'}" value="%{bldngIsImprovementCharges}" /></td>
					
					<td class="greybox" ><s:text name="inspectionlbl.bldngDetails.regularisationCharges"/><span class="mandatory">*</span></td> 
					<td class="greybox"><s:select name="bldngIsRegularisationCharges" id="bldngIsRegularisationCharges" headerKey="-1" headerValue="--Choose--" list="#{'true':'YES','false':'NO'}" value="%{bldngIsRegularisationCharges}" /></td>
				</tr>
			</s:if> 
			
			
			
		</table>
		 </div>
    </div>
</div>
 
