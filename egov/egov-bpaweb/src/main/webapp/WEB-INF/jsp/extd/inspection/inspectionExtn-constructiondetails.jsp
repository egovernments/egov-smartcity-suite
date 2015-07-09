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
<s:set name="theme" value="'simple'" scope="page" />

<script>

if(jQuery('#constructionstage').val()=="-1" || jQuery('#constructionstage option:selected').html()=="NotStarted"){
	jQuery('#constructiontablesite').hide();	
	jQuery('#constructiontablesite').find("td").each(function(){
		if(jQuery(this).attr('id')=='mandatoryfields'){
			jQuery(this).find('span').remove();
			jQuery(this).attr('id', 'changedtononmandatory');
		}
	});
}
 
jQuery('#constructionstage').change(function(){
	if(jQuery('#constructionstage option:selected').html()=="NotStarted" ||  jQuery(this).val()=="-1"){
		jQuery('#constructiontablesite').hide();
		jQuery('#constructiontablesite').find("td").each(function(){
			if(jQuery(this).attr('id')=='mandatoryfields'){ 
				jQuery(this).find('span').remove();
				jQuery(this).attr('id', 'changedtononmandatory');
			}
		});
		jQuery('#constructiontablesite tr').each(function() { 
            jQuery(this).find('td').each (function() {   
               data=jQuery(this).find('input').val();
               if(data!='undefined') 
            	   jQuery(this).find('input').val(""); 
            });
        }); 
	}else if(jQuery('#constructionstage option:selected').html()=="Started"){	
		 jQuery('#constructiontablesite').show();
		 jQuery('#constructiontablesite').find("td").each(function(){ 
			 if(jQuery(this).attr('id')=='changedtononmandatory'){
			 	jQuery(this).attr('id', 'mandatoryfields');
			 	if(jQuery(this).attr('id')=='mandatoryfields'){
			 		jQuery(this).append('<span class="mandatory">*</span>');
			 	}
			 }
		}); 
	}
});


</script>
<div align="center"> 
 <div id="dplandetails" class="formmainbox">
  
	<div align="center">
	<s:if test="%{constructionMesDtls.size!=0}">

	<h1 class="subhead" ><s:text name="inspectionlbl.constmeasuredtls"/></h1>
	 <s:if test="%{serviceTypeCode==null || serviceTypeCode==''}">
           <table width="100%" border="0" cellspacing="0" cellpadding="0" align="center">
	   	 	<tr>
	   	 	
	 			<td class="bluebox" width="20%">&nbsp;</td>
				<td class="bluebox" width="30%"><s:text name="inspectionlbl.conststage"/><span class="mandatory">*</span></td> 
				<td class="bluebox" ><s:select id="constructionstage" name="constructionstage" list="dropdownData.constructionStageList" listKey="id" listValue="description" value="%{inspection.inspectionDetails.constStages.id}" headerKey="-1" headerValue="----Choose------"/></td>   		
	       		 <td class="bluebox">&nbsp;</td>
	       		 <td class="bluebox" width="">&nbsp;</td>	
	         </tr>       
	          <tr>
		    <td class="greybox" width="20%">&nbsp;</td>	
		    <td class="greybox" width="">&nbsp;</td>	
		    <td class="greybox" width="">&nbsp;</td>	
		    <td class="greybox" width="">&nbsp;</td>	
		    <td class="greybox" width="">&nbsp;</td>	
		    </tr>  
	      </table>
      </s:if>  
          
		  <s:iterator value="constructionMesDtls" status="row_status">
		 <s:if test="#row_status.odd == true">
		   <table id="constructiontablebuilding" width="100%" border="0" cellspacing="0" cellpadding="0" class="tablebottom">
		   </s:if>
		   <s:else>
		    <table id="constructiontablesite" width="100%" border="0" cellspacing="0" cellpadding="0" class="tablebottom">
		   </s:else>
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
		   	<td class="bluebox" id="mandatoryfields"><s:text name="inspectionlbl.passwidth"/><span class="mandatory">*</span></td>
			<td class="bluebox" id="numbers"><s:textfield id="passagewidth" name="constructionMesDtls[%{#row_status.index}].passWidth" maxlength="10"/></td>
			<td class="bluebox" id="mandatoryfields"><s:text name="inspectionlbl.passlength"/><span class="mandatory">*</span></td>
			<td class="bluebox" id="numbers"><s:textfield id="passagelength" name="constructionMesDtls[%{#row_status.index}].passageLength" maxlength="10"/></td>
		    </tr>
		 
		    <tr>
		    <td class="greybox" width="20%">&nbsp;</td>			
		   	<td class="greybox" id="mandatoryfields"><s:text name="inspectionlbl.const.fsb"/><span class="mandatory">*</span></td>
			<td class="greybox" id="numbers"><s:textfield id="fsb" name="constructionMesDtls[%{#row_status.index}].fsb" maxlength="10"/></td>
			<td class="greybox" id="mandatoryfields"><s:text name="inspectionlbl.const.rsb"/><span class="mandatory">*</span></td>
			<td class="greybox" id="numbers"><s:textfield id="rsb" name="constructionMesDtls[%{#row_status.index}].rsb" maxlength="10"/></td>
		    <s:hidden name="constructionMesDtls[%{#row_status.index}].inspectionSource.id"/>
		    </tr>
		    <tr>
		    <td class="bluebox" width="20%">&nbsp;</td>
		   	<td class="bluebox" id="mandatoryfields"><s:text name="inspectionlbl.const.ssb1"/><span class="mandatory">*</span></td>
			<td class="bluebox"  id="numbers"><s:textfield id="ssb1" name="constructionMesDtls[%{#row_status.index}].ssb1" maxlength="10"/></td>
			<td class="bluebox" id="mandatoryfields"><s:text name="inspectionlbl.const.ssb2"/><span class="mandatory">*</span></td>
			<td class="bluebox" id="numbers"><s:textfield id="ssb2" name="constructionMesDtls[%{#row_status.index}].ssb2" maxlength="10"/></td>
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
		    <td class="greybox" width="20%">&nbsp;</td>	
		    <td class="greybox" width="">&nbsp;</td>	
		    <td class="greybox" width="">&nbsp;</td>	
		    <td class="greybox" width="">&nbsp;</td>	
		    <td class="greybox" width="">&nbsp;</td>	
		   <s:hidden name="constructionMesDtls[%{#row_status.index}].id"/>
		  
		
		    </tr>
		    </table>
		    </s:iterator>
	  </s:if>
	  
	
		 
    </div>
  
	</div> 	
</div>
