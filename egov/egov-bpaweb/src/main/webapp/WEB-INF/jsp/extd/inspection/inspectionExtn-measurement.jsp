<%@ taglib prefix="s" uri="/WEB-INF/struts-tags.tld"%>
<%@page pageEncoding="utf-8" contentType="text/html; charset=utf-8"%>
<s:set name="theme" value="'simple'" scope="page" />
 <SCRIPT>
var nodes = document.getElementById("measuredetails").getElementsByTagName('*');
for(var i = 0; i < nodes.length; i++)
{
     nodes[i].disabled = true;
}
</SCRIPT>
  
<div id="measuredetails" align="center"> 
<div class="formmainbox">
	<div align="center" >
		<!-- Service type  : 01, 03 and 06, show Land and Building Details -->
		<s:if test="%{ serviceTypeCode==@org.egov.bpa.constants.BpaConstants@NEWBUILDINGONVACANTPLOTCODE ||
			 serviceTypeCode==@org.egov.bpa.constants.BpaConstants@DEMOLITIONRECONSTRUCTIONCODE || 
			 serviceTypeCode==@org.egov.bpa.constants.BpaConstants@ADDITIONALCONSTRUCTIONCODE}">
				<h1 class="subhead" ><s:text name="inspectionExtn.landDetails.lbl"/></h1>
				<table  width="100%" border="0" cellspacing="0" cellpadding="0" class="tablebottom">
				<tr>
					<td class="bluebox" width="20%">&nbsp;</td>
					<td class="bluebox"><s:text name="inspectionlbl.landDetails.zoning"/><span class="mandatory">*</span></td> 
					<td class="bluebox"><s:radio name="inspection.landZoning" id="landZoning" list="%{landZoningMap}" /></td>
					<td class="bluebox"><s:text name="inspectionlbl.landDetails.landUsage"/><span class="mandatory">*</span></td> 
					<td class="bluebox" ><s:select id="landUsage" name="landUsage" list="dropdownData.landUsageList" listKey="id" listValue="name"  value="inspection.landUsage.id" headerKey="-1" headerValue="----Choose------"/></td>
				 </tr>
					 
				<tr> 
					<td class="bluebox" width="20%">&nbsp;</td>
					<td class="bluebox"><s:text name="inspectionlbl.landDetails.layoutType"/><span class="mandatory">*</span></td> 
					<td class="bluebox"><s:select id="layoutType" name="layoutType" list="dropdownData.layoutTypeList" listKey="id" listValue="name"  value="inspection.layoutType.id" headerKey="-1" headerValue="----Choose------" /></td>
					<td class="bluebox"><s:text name="inspectionlbl.landDetails.minPlotExtnt"/><span class="mandatory">*</span></td> 
					<td class="bluebox"><s:textfield id="lndMinPlotExtent" name="lndMinPlotExtent" value="%{inspection.lndMinPlotExtent}" /></td>   			
				</tr>
			    
			    <s:if test="%{inspection.lndOsrLandExtent!=0 && inspection.lndOsrLandExtent!=null}">    
			    <tr>
					<td class="bluebox" width="20%">&nbsp;</td>
					<td class="bluebox"><s:text name="inspectionlbl.landDetails.osr"/></td> 
					<td class="bluebox"><s:textfield id="lndOsrLandExtent" name="lndOsrLandExtent" value="%{inspection.lndOsrLandExtent}" /></td>
					<s:if test="%{inspection.lndGuideLineValue!=0 && inspection.lndGuideLineValue!=null}">       			
						<td class="bluebox" ><s:text name="inspectionlbl.landDetails.guideLine"/></td>
						<td class="bluebox"><s:textfield id="lndGuideLineValue" name="lndGuideLineValue" value="%{inspection.lndGuideLineValue}" /></td>
					</s:if>	
					<s:else>
						<td class="bluebox"></td> 
				  		<td class="bluebox"></td>
					</s:else>
				</tr>
				</s:if>
							 
				 <s:if test="%{isSurveyor==false}">
					<s:if test="%{inspection.lndPenaltyPeriod!=null || (inspection.lndRegularizationArea!=0 && inspection.lndRegularizationArea!=null)}"> 
					  	<tr id="showToOfficialRow">
							<td class="bluebox" width="20%">&nbsp;</td>
							<s:if test="%{inspection.lndPenaltyPeriod!=null && (inspection.lndRegularizationArea!=0 && inspection.lndRegularizationArea!=null)}">
								<td class="bluebox"><s:text name="inspectionlbl.landDetails.penaltyPeriod"/></td> 
								<td class="bluebox"><s:select id="lndPenaltyPeriod" name="lndPenaltyPeriod" list="dropdownData.penaltyPeriodList"  value="inspection.lndPenaltyPeriod" headerKey="" headerValue="----Choose------"/></td>   			
								
								<td class="bluebox" ><s:text name="inspectionlbl.landDetails.regularizationArea"/></td> 
								<td class="bluebox"><s:textfield id="lndRegularizationArea" name="lndRegularizationArea" value="%{inspection.lndRegularizationArea}" /></td>
							</s:if>
							<s:elseif test="%{inspection.lndPenaltyPeriod!=null && (inspection.lndRegularizationArea==0 || inspection.lndRegularizationArea==null)}">
								<td class="bluebox"><s:text name="inspectionlbl.landDetails.penaltyPeriod"/></td> 
								<td class="bluebox"><s:select id="lndPenaltyPeriod" name="lndPenaltyPeriod" list="dropdownData.penaltyPeriodList"  value="inspection.lndPenaltyPeriod" headerKey="" headerValue="----Choose------"/></td>   			
								
								<td class="bluebox"></td> 
			   					<td class="bluebox"></td>
							</s:elseif>
							<s:elseif test="%{inspection.lndPenaltyPeriod==null && (inspection.lndRegularizationArea!=0 && inspection.lndRegularizationArea!=null)}">
								<td class="bluebox" ><s:text name="inspectionlbl.landDetails.regularizationArea"/></td> 
								<td class="bluebox"><s:textfield id="lndRegularizationArea" name="lndRegularizationArea" value="%{inspection.lndRegularizationArea}" /></td>
								
								<td class="bluebox"></td> 
			   					<td class="bluebox"></td>
							</s:elseif>
						</tr>
					</s:if>
					
					<tr>
					 	<td class="bluebox">&nbsp;</td>
						<td class="bluebox"><s:text name="inspectionlbl.landDetails.regularisationCharges"/><span class="mandatory">*</span></td> 
						<td class="bluebox" ><s:select name="lndIsRegularisationCharges" id="lndIsRegularisationCharges" list="#{'true':'YES','false':'NO'}" value="%{inspection.lndIsRegularisationCharges}" /></td>
					
						<td class="bluebox"></td> 
			   			<td class="bluebox"></td>
					</tr>
				</s:if>
				</table>
 	
				<h1 class="subhead" ><s:text name="inspectionExtn.bldngDetails.lbl"/></h1>
				
				<table  width="100%" border="0" cellspacing="0" cellpadding="0" class="tablebottom">
				 <tr>
					<td class="bluebox" width="20%">&nbsp;</td>
					<td class="bluebox"><s:text name="inspectionlbl.bldngDetails.buildingType"/><span class="mandatory">*</span></td> 
					<td class="bluebox" ><s:select id="buildingType" name="buildingType" list="dropdownData.buildingTypeList" listKey="id" listValue="name"  value="inspection.buildingType.id" headerKey="-1" headerValue="----Choose------"/></td>
					<td class="bluebox"><s:text name="inspectionlbl.bldngDetails.roadWidth"/><span class="mandatory">*</span></td> 
					<td class="bluebox"><s:textfield id="bldngRoadWidth" name="bldngRoadWidth" value="%{inspection.bldngRoadWidth}" /></td>
				 </tr>
				 			 
			   	<tr> 
					<td class="bluebox">&nbsp;</td>
					<td class="bluebox"><s:text name="inspectionlbl.bldngDetails.totalBldpArea"/><span class="mandatory">*</span></td> 
					<td class="bluebox"><s:textfield id="bldngBuildUpArea" name="bldngBuildUpArea" value="%{inspection.bldngBuildUpArea}" /></td>   			
					<td class="bluebox"><s:text name="inspectionlbl.bldngDetails.prpsdPltFrntg"/><span class="mandatory">*</span></td>
					<td class="bluebox"><s:textfield id="bldngProposedPlotFrntage" name="bldngProposedPlotFrntage" value="%{inspection.bldngProposedPlotFrntage}" /></td>
			    </tr>
			    
			    <tr> 
					<td class="bluebox" width="20%">&nbsp;</td>
					<td class="bluebox"><s:text name="inspectionlbl.bldngDetails.cmpndWall"/><span class="mandatory">*</span></td>
					<td class="bluebox"><s:textfield id="bldngCompoundWall" name="bldngCompoundWall" value="%{inspection.bldngCompoundWall}" /></td>
					<td class="bluebox"><s:text name="inspectionlbl.bldngDetails.wellOht.sumpTankArea"/><span class="mandatory">*</span></td> 
			  		<td class="bluebox"><s:textfield id="bldngWellOht_SumpTankArea" name="bldngWellOht_SumpTankArea" value="%{inspection.bldngWellOht_SumpTankArea}"  /></td>   			
			    </tr>
			    
			     <s:if test="%{isSurveyor==false && inspection.bldngStormWaterDrain.id !=null && inspection.bldngStormWaterDrain.id != 0}">       
				     <tr>	
						<td class="bluebox" width="20%">&nbsp;</td>
						<td id="buildingstoreid" class="bluebox"><s:text name="inspectionlbl.bldngDetails.stormWaterDrain"/></td> 
						<td id="buildingstoreevalue" class="bluebox" id="numbers"><s:select id="bldngStormWaterDrain" name="bldngStormWaterDrain" list="dropdownData.stormWaterDrainList" listKey="id" listValue="dimension"  value="inspection.bldngStormWaterDrain.id" headerKey="-1" headerValue="----Choose------"/></td>
						
						<td class="bluebox" width="13%"></td> 
				   		<td class="bluebox" width="13%"></td> 
				     </tr>
			 	</s:if>    
			    
			  	<s:if test="%{inspection.dwellingUnit!=null && inspection.dwellingUnit!=0}">  
			          <tr>
			 			<td class="bluebox" width="20%">&nbsp;</td>
						<td class="bluebox" width="13%"><s:text name="inspectionlbl.bldngDetails.dwellingUnit"/><span class="mandatory">*</span></td> 
			   			<td class="bluebox" id="numbers"><s:textfield id="dwellingUnit" name="dwellingUnit" value="%{inspection.dwellingUnit}"  /></td>
			   			
			   			<td class="bluebox" width="13%"></td> 
			   			<td class="bluebox" width="13%"></td> 
			         </tr> 
			    </s:if>
			    
			    <s:if test="%{(inspection.bldngCommercial!=null && inspection.bldngCommercial!=0) 
			    				&& (inspection.bldngResidential!=null && inspection.bldngResidential!=0)}">  
				     <tr>
						<td class="bluebox" width="20%">&nbsp;</td>
						<td class="bluebox"><s:text name="inspectionlbl.bldngDetails.commercial"/></td> 
						<td class="bluebox"><s:textfield id="bldngCommercial" name="bldngCommercial" value="%{inspection.bldngCommercial}"  /></td>
						<td class="bluebox"><s:text name="inspectionlbl.bldngDetails.residential"/></td> 
						<td class="bluebox"><s:textfield id="bldngResidential" name="bldngResidential" value="%{inspection.bldngResidential}"  /></td>
					</tr> 
				</s:if>
			    
			    
			    <s:if test="%{serviceTypeCode==@org.egov.bpa.constants.BpaConstants@DEMOLITIONRECONSTRUCTIONCODE}">
				    <tr> 
						<td class="bluebox">&nbsp;</td>
						<td class="bluebox"><s:text name="inspectionlbl.bldngDetails.gflrTiledFlrArea"/></td>
						<td class="bluebox"><s:textfield id="bldngGFloor_TiledFloor" name="bldngGFloor_TiledFloor" value="%{inspection.bldngGFloor_TiledFloor}" /></td>
						<td class="bluebox"><s:text name="inspectionlbl.bldngDetails.gflrOtherTypes"/></td> 
						<td class="bluebox"><s:textfield id="bldngGFloor_OtherTypes" name="bldngGFloor_OtherTypes" value="%{inspection.bldngGFloor_OtherTypes}" /></td>   			
				    </tr>
				       
				    <tr>
				      	<td class="bluebox" width="20%">&nbsp;</td>
				      	<td class="bluebox"><s:text name="inspectionlbl.bldngDetails.frstFlrTotalArea"/></td> 
						<td class="bluebox"><s:textfield id="bldngFrstFloor_TotalArea" name="bldngFrstFloor_TotalArea" value="%{inspection.bldngFrstFloor_TotalArea}" /></td>
						<td class="bluebox"><s:text name="inspectionlbl.bldngDetails.prpsdBldngArea"/><span class="mandatory">*</span></td> 
						<td class="bluebox"><s:textfield id="bldngProposedBldngArea" name="bldngProposedBldngArea" value="%{inspection.bldngProposedBldngArea}" /></td>
					</tr>
				</s:if>
				
				<s:if test="%{isSurveyor==false}">
					<tr id="showToOfficialRow">
						<td class="bluebox" width="20%">&nbsp;</td>
						<td class="bluebox" width="13%"><s:text name="inspectionlbl.bldngDetails.improvementCharges"/></td> 
						<td class="bluebox"><s:select name="bldngIsImprovementCharges" id="bldngIsImprovementCharges" list="#{'true':'YES','false':'NO'}" value="%{inspection.bldngIsImprovementCharges}" /></td>
						
						<td class="bluebox" ><s:text name="inspectionlbl.bldngDetails.regularisationCharges"/></td> 
						<td class="bluebox"><s:select name="bldngIsRegularisationCharges" id="bldngIsRegularisationCharges" list="#{'true':'YES','false':'NO'}" value="%{inspection.bldngIsRegularisationCharges}" /></td>
					</tr>
				</s:if>	
				</table>
	</s:if> 
	
	<!-- Service type  : 04 and 05 show only Site Measurement Details -->
	<s:if test="%{serviceTypeCode==@org.egov.bpa.constants.BpaConstants@SUBDIVISIONOFLANDCODE ||
	 serviceTypeCode==@org.egov.bpa.constants.BpaConstants@LAYOUTAPPPROVALCODE}">
		<div align="center">
		<s:if test="%{plotDetails=='TRUE'}">
		  <div id="" class="formmainbox">
	      <h1 class="subhead" ><s:text name="inspectionlbl.sitemsrheader"/></h1>
		    <table width="100%" border="0" cellspacing="0" cellpadding="0" align="center">
			    <tr>
			     <td class="bluebox" width="20%">&nbsp;</td>	
				    <td class="bluebox" width="">&nbsp;</td>	
				    <td class="bluebox" width="">&nbsp;</td>	
				    <td class="bluebox" width="">&nbsp;</td>	
				    <td class="bluebox" width="">&nbsp;</td>	
			    <td class="bluebox" width="">&nbsp;</td>
			    </tr>
		   	 	<tr>
		 			<td class="bluebox" width="20%">&nbsp;</td>
					<td class="bluebox"><s:text name="inspectionlbl.msr.bldgextnt"/></td> 
		   			<td class="bluebox" ><s:textfield id="buildingextent" name="inspection.inspectionDetails.buildingExtent" value="%{inspection.inspectionDetails.buildingExtent}" /></td>
					<td class="bluebox" ><s:text name="inspectionlbl.msr.plots"/></td>
					<td class="bluebox" id="wholenumbers"><s:textfield id="numberofplots" name="inspection.inspectionDetails.numOfPlots" value="%{inspection.inspectionDetails.numOfPlots}" /></td>
					<td class="bluebox">&nbsp;</td>
		         </tr>         
		      </table>
		 </div>
		</s:if>	 
	    </div>
    </s:if>
    
    <!-- Service type  : 02,03,06 and 07 Show Building Measurement as per plan / site Details -->
    <s:if test="%{serviceTypeCode==@org.egov.bpa.constants.BpaConstants@APPLICATIONFORDEMOLITIONCODE || 
	   serviceTypeCode==@org.egov.bpa.constants.BpaConstants@DEMOLITIONRECONSTRUCTIONCODE || 
	   serviceTypeCode==@org.egov.bpa.constants.BpaConstants@ADDITIONALCONSTRUCTIONCODE ||
	   serviceTypeCode==@org.egov.bpa.constants.BpaConstants@CMDACODE}">
	  		<s:if test="%{inspectMeasurementDtls.size!=0}">
		 	 	<s:iterator value="inspectMeasurementDtls" status="row_status">
		 	 	<div align="center">
				<div  class="formmainbox">
		   		<table id="plan" width="100%" border="0" cellspacing="0" cellpadding="0" class="tablebottom">
				  <tr>
				    <td width="20%">&nbsp;</td>	
				   <td width="8%">&nbsp;</td>	
				   <td align="center">
				   	<h1 class="subhead" ><s:text name="inspectMeasurementDtls[%{#row_status.index}].header"/></h1>
				 	</td>
				 </tr>
		 
			    <tr>
				    <td class="bluebox" width="20%">&nbsp;</td>			
				   	<td class="bluebox" ><s:text name="inspectionlbl.msr.fsb"/><span class="mandatory">*</span></td>
					<td class="bluebox" ><s:textfield id="fsb" name="inspectMeasurementDtls[%{#row_status.index}].fsb"/></td>
					<td class="bluebox" ><s:text name="inspectionlbl.msr.rsb"/><span class="mandatory">*</span></td>
					<td class="bluebox" ><s:textfield id="rsb" name="inspectMeasurementDtls[%{#row_status.index}].rsb"/></td>
			    </tr>
			    <tr>
				    <td class="bluebox" width="20%">&nbsp;</td>
				   	<td class="bluebox" ><s:text name="inspectionlbl.msr.ssb1"/><span class="mandatory">*</span></td>
					<td class="bluebox" ><s:textfield id="fsb" name="inspectMeasurementDtls[%{#row_status.index}].ssb1"/></td>
					<td class="bluebox" ><s:text name="inspectionlbl.msr.ssb2"/><span class="mandatory">*</span></td>
					<td class="bluebox" ><s:textfield id="rsb" name="inspectMeasurementDtls[%{#row_status.index}].ssb2"/></td>
			    </tr>
		    </table>
		    </div>
			</div> 
		    </s:iterator>
		</s:if>	 
	</s:if>  

   <!-- Service type  : 08 -->
	<s:if test="%{serviceTypeCode==@org.egov.bpa.constants.BpaConstants@RECLASSIFICATIONCODE}">   
    <s:iterator value="constructionMesDtls" status="row_status">
		<div align="center">
			<div  class="formmainbox">
	      		 <h1 class="subhead" ><s:text name="constructionMesDtls[0].header"/></h1>
		    	 <table width="100%" border="0" cellspacing="0" cellpadding="0" align="center">
		     		<tr>
					    <td class="bluebox" width="20%">&nbsp;</td>
					   	<td class="bluebox" id="mandatoryfields"><s:text name="inspectionlbl.passwidth"/><span class="mandatory">*</span></td>
						<td class="bluebox" id="numbers"><s:textfield id="passagewidth" value="%{constructionMesDtls[0].passWidth}" name="constructionMesDtls[%{#row_status.index}].passWidth" maxlength="10"/></td>
						<td class="bluebox" width="">&nbsp;</td>	 
					    <td class="bluebox" width="">&nbsp;</td>	 
				    </tr>
		    
				    <tr>
					    <td class="greybox" width="20%">&nbsp;</td>			
					   	<td class="greybox" id="mandatoryfields"><s:text name="inspectionlbl.const.sbn"/><span class="mandatory">*</span></td>
						<td class="greybox" ><s:select id="surroundedByNorth" name="constructionMesDtls[%{#row_status.index}].surroundedByNorth.id" list="dropdownData.surroundedBuildingList"
							 listKey="id" listValue="name" value="%{constructionMesDtls[0].surroundedByNorth.id}" headerKey="-1" 
							 headerValue="----Choose------"/></td>
						<td class="greybox" id="mandatoryfields"><s:text name="inspectionlbl.const.sbs"/><span class="mandatory">*</span></td>
						<td class="greybox" id="numbers"><s:select id="surroundedBySouth" name="constructionMesDtls[%{#row_status.index}].surroundedBySouth.id" list="dropdownData.surroundedBuildingList"
							 listKey="id" listValue="name" value="%{constructionMesDtls[0].surroundedBySouth.id}" headerKey="-1" 
							 headerValue="----Choose------"/></td>
				    </tr>
		     
				    <tr>
					    <td class="bluebox" width="20%">&nbsp;</td>			
					   	<td class="bluebox"><s:text name="inspectionlbl.const.sbe"/></td>
					
						<td class="bluebox" ><s:select id="surroundedByEast" name="constructionMesDtls[%{#row_status.index}].surroundedByEast.id" list="dropdownData.surroundedBuildingList"
							 listKey="id" listValue="name" value="%{constructionMesDtls[0].surroundedByEast.id}" headerKey="-1" 
							 headerValue="----Choose------"/></td>
						<td class="bluebox"><s:text name="inspectionlbl.const.sbw"/></td>
						<td class="bluebox" id="numbers"><s:select id="surroundedByWest" name="constructionMesDtls[%{#row_status.index}].surroundedByWest.id" list="dropdownData.surroundedBuildingList"
							 listKey="id" listValue="name" value="%{constructionMesDtls[0].surroundedByWest.id}" headerKey="-1" 
							 headerValue="----Choose------"/></td>
				    </tr>
			  	</table>
			  </div>
		  </div>
	  </s:iterator>
	  </s:if>
	  
	  <!-- Service type  : 01,03, 06 and 07 show Construction Measurement as per plan/site Details based on construction stage -->
	   <s:if test="%{serviceTypeCode==@org.egov.bpa.constants.BpaConstants@NEWBUILDINGONVACANTPLOTCODE || 
	   serviceTypeCode==@org.egov.bpa.constants.BpaConstants@DEMOLITIONRECONSTRUCTIONCODE || 
	   serviceTypeCode==@org.egov.bpa.constants.BpaConstants@ADDITIONALCONSTRUCTIONCODE ||
	   serviceTypeCode==@org.egov.bpa.constants.BpaConstants@CMDACODE}">
			<s:if test="%{constructionMesDtls.size!=0}">
				<h1 class="subhead" ><s:text name="inspectionlbl.constmeasuredtls"/></h1>
		           <table width="100%" border="0" cellspacing="0" cellpadding="0" align="center">
		   	 	<tr>
		 			<td class="bluebox" width="20%">&nbsp;</td>
					<td class="bluebox" width="30%"><s:text name="inspectionlbl.conststage"/></td> 
					<td class="bluebox" ><s:select id="inspectionDetails.constStages.id" name="inspectionDetails.constStages.id" list="dropdownData.constructionStageList" listKey="id" listValue="description" value="%{inspection.inspectionDetails.constStages.id}" headerKey="-1" headerValue="----Choose------"/></td>   		
		       		 <td class="bluebox">&nbsp;</td>
		       		 <td class="bluebox" width="">&nbsp;</td>	
		         </tr>       
		          <tr>
				    <td class="bluebox" width="20%">&nbsp;</td>	
				    <td class="bluebox" width="">&nbsp;</td>	
				    <td class="bluebox" width="">&nbsp;</td>	
				    <td class="bluebox" width="">&nbsp;</td>	
				    <td class="bluebox" width="">&nbsp;</td>	
			    </tr>  
		      </table>
		   </s:if> 
	   
	   
		   <s:if test="%{inspection.inspectionDetails.constStages.constStage=='Started'}">
		    	<s:iterator value="constructionMesDtls" status="row_status">
					<div align="center">
					<div  class="formmainbox">
		      		<h1 class="subhead" ><s:text name="constructionMesDtls[0].header"/></h1>
			    	<table id="plan" width="100%" border="0" cellspacing="0" cellpadding="0" class="tablebottom">
						 <tr>
						    <td class="bluebox" width="20%">&nbsp;</td>
						   	<td class="bluebox" ><s:text name="inspectionlbl.passwidth"/></td>
							<td class="bluebox" ><s:textfield id="fsb" name="constructionMesDtls[%{#row_status.index}].passWidth"/></td>
							<td class="bluebox" ><s:text name="inspectionlbl.passlength"/></td>
							<td class="bluebox" ><s:textfield id="rsb" name="constructionMesDtls[%{#row_status.index}].passageLength"/></td>
					    </tr>
					    <tr>
						    <td class="bluebox" width="20%">&nbsp;</td>			
						   	<td class="bluebox" ><s:text name="inspectionlbl.const.fsb"/></td>
							<td class="bluebox" ><s:textfield id="fsb" name="constructionMesDtls[%{#row_status.index}].fsb"/></td>
							<td class="bluebox" ><s:text name="inspectionlbl.const.rsb"/></td>
							<td class="bluebox" ><s:textfield id="rsb" name="constructionMesDtls[%{#row_status.index}].rsb"/></td>
					    </tr>
					    <tr>
						    <td class="bluebox" width="20%">&nbsp;</td>
						   	<td class="bluebox" ><s:text name="inspectionlbl.const.ssb1"/></td>
							<td class="bluebox" ><s:textfield id="fsb" name="constructionMesDtls[%{#row_status.index}].ssb1"/></td>
							<td class="bluebox" ><s:text name="inspectionlbl.const.ssb2"/></td>
							<td class="bluebox" ><s:textfield id="rsb" name="constructionMesDtls[%{#row_status.index}].ssb2"/></td>
					    </tr>
					</table>
					</div>
					</div>
				</s:iterator>
			</s:if>
			<s:else>
			<div align="center">
					<div  class="formmainbox">
		      		<h1 class="subhead" ><s:text name="constructionMesDtls[0].header"/></h1>
			    	<table id="plan" width="100%" border="0" cellspacing="0" cellpadding="0" class="tablebottom">
						 <tr>
						    <td class="bluebox" width="20%">&nbsp;</td>
						   	<td class="bluebox" ><s:text name="inspectionlbl.passwidth"/></td>
							<td class="bluebox" ><s:textfield id="fsb" name="constructionMesDtls[0].passWidth"/></td>
							<td class="bluebox" ><s:text name="inspectionlbl.passlength"/></td>
							<td class="bluebox" ><s:textfield id="rsb" name="constructionMesDtls[0].passageLength"/></td>
					    </tr>
					    <tr>
						    <td class="bluebox" width="20%">&nbsp;</td>			
						   	<td class="bluebox" ><s:text name="inspectionlbl.const.fsb"/></td>
							<td class="bluebox" ><s:textfield id="fsb" name="constructionMesDtls[0].fsb"/></td>
							<td class="bluebox" ><s:text name="inspectionlbl.const.rsb"/></td>
							<td class="bluebox" ><s:textfield id="rsb" name="constructionMesDtls[0].rsb"/></td>
					    </tr>
					    <tr>
						    <td class="bluebox" width="20%">&nbsp;</td>
						   	<td class="bluebox" ><s:text name="inspectionlbl.const.ssb1"/></td>
							<td class="bluebox" ><s:textfield id="fsb" name="constructionMesDtls[0].ssb1"/></td>
							<td class="bluebox" ><s:text name="inspectionlbl.const.ssb2"/></td>
							<td class="bluebox" ><s:textfield id="rsb" name="constructionMesDtls[0].ssb2"/></td>
					    </tr>
					</table>
					</div>
					</div>
			</s:else>
	   </s:if>
    </div>
  </div>
</div>
   
   