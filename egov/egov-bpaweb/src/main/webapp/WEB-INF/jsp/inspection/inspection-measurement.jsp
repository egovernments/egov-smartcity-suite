<%@ taglib prefix="s" uri="/WEB-INF/struts-tags.tld"%>
<%@page pageEncoding="utf-8" contentType="text/html; charset=utf-8"%>
<s:set name="theme" value="'simple'" scope="page" />
 <SCRIPT>
  jQuery.noConflict();

jQuery(document).ready(function(){


jQuery('#measuredetails').children().each(function(){


jQuery(this).attr('disabled','true');

})
  });





</SCRIPT>
  
<div id="measuredetails" align="center"> 


 <div class="formmainbox">
  
	<div align="center" >
	<s:if test="%{inspectMeasurementDtls.size!=0}">
	<h1 class="subhead" ><s:text name="inspectionlbl.sitemsrheader"/></h1>
          
		  <s:iterator value="inspectMeasurementDtls" status="row_status">
		 
		   <table id="plan" width="100%" border="0" cellspacing="0" cellpadding="0" class="tablebottom">
		 
		  <tr>
		    <td class="bluebox" width="20%">&nbsp;</td>	
		    <td class="bluebox" width="">&nbsp;</td>	
		    <td class="bluebox" width="">&nbsp;</td>	
		    <td class="bluebox" width="">&nbsp;</td>	
		    <td class="bluebox" width="">&nbsp;</td>	
		 </tr>
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
			<s:hidden name="inspectMeasurementDtls[%{#row_status.index}].inspectionSource.id"/>
			
		    </tr>
		    
		    
		    <tr>
		    <td class="bluebox" width="20%">&nbsp;</td>	
		    <td class="bluebox" width="">&nbsp;</td>	
		    <td class="bluebox" width="">&nbsp;</td>	
		    <td class="bluebox" width="">&nbsp;</td>	
		    <td class="bluebox" width="">&nbsp;</td>	
		    <s:hidden name="inspectMeasurementDtls[%{#row_status.index}].id"/>	
		  
		
		    </tr>
		    </table>
		    </s:iterator>
	  
	</s:if>	 
    </div>
  
	</div> 

	
	
	<div align="center">
	<s:if test="%{constructionMesDtls.size!=0}">
	<h1 class="subhead" ><s:text name="inspectionlbl.constmeasuredtls"/></h1>
	  <s:if test="%{serviceTypeCode==null || serviceTypeCode==''}">
           <table width="100%" border="0" cellspacing="0" cellpadding="0" align="center">
	   	 	<tr>
	   	 	
	 			<td class="bluebox" width="20%">&nbsp;</td>
				<td class="bluebox" width="30%"><s:text name="inspectionlbl.conststage"/></td> 
				<td class="bluebox" ><s:select id="inspectionDetails.remarks" name="inspectionDetails.constStages.id" list="dropdownData.constructionStageList" listKey="id" listValue="description" value="%{inspection.inspectionDetails.constStages.id}" headerKey="-1" headerValue="----Choose------"/></td>   		
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
          
		  <s:iterator value="constructionMesDtls" status="row_status">
		 
		   <table id="plan" width="100%" border="0" cellspacing="0" cellpadding="0" class="tablebottom">
		 <tr>
		    <td width="20%">&nbsp;</td>	
		   <td width="11%">&nbsp;</td>	
		   <td align="center">
		   <h1 class="subhead" ><s:text name="constructionMesDtls[%{#row_status.index}].header"/></h1>
		 </td>
		  
		 </tr>
		   <s:if test="%{serviceTypeCode==null || serviceTypeCode==''}">
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
		    <s:hidden name="constructionMesDtls[%{#row_status.index}].inspectionSource.id"/>
		    </tr>
		    <tr>
		    <td class="bluebox" width="20%">&nbsp;</td>
		   	<td class="bluebox" ><s:text name="inspectionlbl.const.ssb1"/></td>
			<td class="bluebox" ><s:textfield id="fsb" name="constructionMesDtls[%{#row_status.index}].ssb1"/></td>
			<td class="bluebox" ><s:text name="inspectionlbl.const.ssb2"/></td>
			<td class="bluebox" ><s:textfield id="rsb" name="constructionMesDtls[%{#row_status.index}].ssb2"/></td>
		    </tr>
		    </s:if>		
		    
		    <s:elseif test="%{serviceTypeCode!=null && serviceTypeCode!=''}">
		    
		    <tr>
		    <td class="bluebox" width="20%">&nbsp;</td>
		   	<td class="bluebox" id="mandatoryfields"><s:text name="inspectionlbl.passwidth"/><span class="mandatory">*</span></td>
			<td class="bluebox" id="numbers"><s:textfield id="passagewidth" name="constructionMesDtls[%{#row_status.index}].passWidth" maxlength="10"/></td>
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
		    <s:hidden name="constructionMesDtls[%{#row_status.index}].inspectionSource.id"/> 
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
		    </s:elseif>	 	
		        
		    <tr>
		    <td class="bluebox" width="20%">&nbsp;</td>	
		    <td class="bluebox" width="">&nbsp;</td>	
		    <td class="bluebox" width="">&nbsp;</td>	
		    <td class="bluebox" width="">&nbsp;</td>	
		    <td class="bluebox" width="">&nbsp;</td>	
		   <s:hidden name="constructionMesDtls[%{#row_status.index}].id"/>
		  
		
		    </tr>
		    </table>
		    </s:iterator>
	  </s:if>
	  
	  	<div align="center" id="checklistdiv">
	<s:if test="%{chkListDet.size!=0}">
	<h1 class="subhead" ><s:text name="inspectionlbl.otherdtls"/></h1>
          
		  <s:iterator value="chkListDet" status="row_status">
		 
		   <table id="checklists" width="100%" border="0" cellspacing="0" cellpadding="0" class="tablebottom">
		 
		    <tr>
		     
		    <td class="bluebox" width="40%">&nbsp;</td>			
		   	<td class="bluebox" width="20%"><s:text name="chkListDet[%{#row_status.index}].checklistDetails.description"/></td>		   	
			<td class="bluebox" id="mandatorycheck"><s:hidden  name="chkListDet[%{#row_status.index}].checklistDetails.isMandatory"/>
			<s:checkbox  name="chkListDet[%{#row_status.index}].isChecked"/></td>		
			<s:hidden name="chkListDet[%{#row_status.index}].checklistDetails.id"/>
			<s:hidden name="chkListDet[%{#row_status.index}].checklistDetails.checkList.id"/>
			<s:hidden name="chkListDet[%{#row_status.index}].id"/>
			 <td class="bluebox" width="">&nbsp;</td>	
			 
		  </tr>   
		    
		    </table>
		    </s:iterator>
	  </s:if>
	 
		 
    </div>
	
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
		 
    </div>
    
 
	</div> 	
