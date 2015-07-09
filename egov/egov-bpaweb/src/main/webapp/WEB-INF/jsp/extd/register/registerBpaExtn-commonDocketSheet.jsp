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
<%@ taglib prefix="s" uri="/WEB-INF/struts-tags.tld"%>
<%@page pageEncoding="utf-8" contentType="text/html; charset=utf-8"%>

<h1 class="subhead" ><s:text name="Inspection.docketHeaderLbl"/></h1>
	        
			  <table  width="100%" border="0" cellspacing="0" cellpadding="0" class="tablebottom" id="docketSheet_Tbl">
			    <tr>
	 			<td class="bluebox" width="20%">&nbsp;</td>
				<td class="bluebox" width="13%" id="mandatoryfields"><s:text name="inspectionlbl.docket.proposedActvtyPermissible"/><span class="mandatory">*</span></td> 
	   			<td class="bluebox"><s:radio name="docket.statusOfApplicant" id="statusOfApplicant" list="%{applicantStatus}"  value="%{docket.statusOfApplicant}"  /></td> 
				
				<td class="bluebox" width="13%" >&nbsp;</td> 
	   			<td class="bluebox" id="numbers">&nbsp;</td>
		     	 </tr>
			    </table>
	  
	 <h1 class="subhead" ><s:text name="inspectionlbl.docket.documentEnclosed"/></h1>
	   <s:if test="%{docketDocumentDtls.size!=0}">
		  <s:iterator value="docketDocumentDtls" status="row_status">
		   <table id="documentDtlchecklists" width="100%" border="0" cellspacing="0" cellpadding="0" class="tablebottom">
		    <tr>
			    <td class="greybox" width="20%">&nbsp;</td>			
			   	<td class="greybox" width="30%"><s:text name="docketDocumentDtls[%{#row_status.index}].checkListDetails.description"/></td>		   	
		     	 <td class="blueborderfortd">
			     <s:textfield id="docketDoc" name="docketDocumentDtls[%{#row_status.index}].value" maxlength="256"  readonly="true" /></td>
			 	<s:hidden name="docketDocumentDtls[%{#row_status.index}].checkListDetails.id"/>
				<s:hidden name="docketDocumentDtls[%{#row_status.index}].checkListDetails.checkList.id"/>
				<s:hidden name="docketDocumentDtls[%{#row_status.index}].checkListDetails.description" />
				<s:hidden name="docketDocumentDtls[%{#row_status.index}].checkListDetails.docket.id"/>
				<s:hidden name="docketDocumentDtls[%{#row_status.index}].id"/>
		    </tr>   
		   </table>
		  </s:iterator>
	  </s:if>
	  		    
	 <h1 class="subhead" ><s:text name="inspectionlbl.docket.proposalLocation"/></h1>
	 <table  width="100%" border="0" cellspacing="0" cellpadding="0" class="tablebottom" id="proposedLocation_Tbl">
	     <tr>
	 			<td class="greybox" width="20%">&nbsp;</td>
				<td class="greybox" width="25%" id="mandatoryfields"><s:text name="inspectionlbl.docket.proposedActvtyPermissible"/><span class="mandatory">*</span></td> 
	   			<td class="greybox"><s:select name="docket.proposedActivityIsPermissible" id="proposedActivityIsPermissible"  list="#{'NA':'NA','YES':'YES','NO':'NO'}"   value="%{docket.proposedActivityIsPermissible}"  readonly="true"    /></td>
				
				<td class="greybox" width="13%" ><s:text name="inspectionlbl.docket.existingUsage"/></td> 
	   			<td class="greybox" >  <s:textfield id="existingUsage" name="docket.existingUsage"  value="%{docket.existingUsage}"  readonly="true"  maxlength="50"/></td>
		 </tr>
		 <tr>
	 			<td class="bluebox" width="20%">&nbsp;</td>
				<td class="bluebox" width="25%" id="mandatoryfields"><s:text name="inspectionlbl.docket.oldPtTaxPaid"/><span class="mandatory">*</span></td> 
	   			<td class="bluebox"><s:select name="docket.existingSanctionPlanOrPtTaxPaidRecptEnclosed" id="existingSanctionPlanOrPtTaxPaidRecptEnclosed"   list="#{'NA':'NA','YES':'YES','NO':'NO'}"  readonly="true"   value="%{docket.existingSanctionPlanOrPtTaxPaidRecptEnclosed}"    /></td>
				
				<td class="bluebox" width="13%" >&nbsp;</td> 
	   			<td class="bluebox">  &nbsp;</td>
		 </tr>	
	</table>
	 <h1 class="subhead" ><s:text name="inspectionlbl.docket.abuttingRoad"/></h1>
	 <table  width="100%" border="0" cellspacing="0" cellpadding="0" class="tablebottom" id="abbutingRoad_Tbl">
	    <tr>
	 			<td class="bluebox" width="20%">&nbsp;</td>
				<td class="bluebox" width="13%"><s:text name="inspectionlbl.docket.abuttingRoad.width"/></td> 
	   			<td class="bluebox"  id="numbers"><s:textfield id="abuttingRoad_Width" name="docket.abuttingRoad_Width"  value="%{docket.abuttingRoad_Width}"/></td>
				
				<td class="bluebox" width="23%" id="mandatoryfields"><s:text name="inspectionlbl.docket.abuttingRoad.publicorprivate"/></td> 
	   			<td class="bluebox" > <s:select id="abuttingRoad_IsPrivateOrPublic" name="docket.abuttingRoad_IsPrivateOrPublic" list="#{'NA':'NA','Private':'Private','Public':'Public'}"  readonly="true"  value="%{docket.abuttingRoad_IsPrivateOrPublic}"/></td>
		 </tr>
		  <tr>
	 			<td class="greybox" width="20%">&nbsp;</td>
				<td class="greybox" width="13%"><s:text name="inspectionlbl.docket.abuttingRoad.gainaccessthg"/></td> 
	   			<td class="greybox"><s:select id="abuttingRoad_gainsAceessThroughPassage" name="docket.abuttingRoad_gainsAceessThroughPassage"  list="#{'NA':'NA','YES':'YES','NO':'NO'}"  readonly="true"  value="%{docket.abuttingRoad_gainsAceessThroughPassage}"/></td>
				
				<td class="greybox" width="23%" id="mandatoryfields"><s:text name="inspectionlbl.docket.abuttingRoadgain.width"/><span class="mandatory">*</span></td> 
	   			<td class="greybox" id="numbers">  <s:textfield id="abuttingRoad_gainWidth" name="docket.abuttingRoad_gainWidth"  value="%{docket.abuttingRoad_gainWidth}"/></td>
		 </tr>
		   <tr>
		   <td class="bluebox" width="20%">&nbsp;</td>
				<td class="bluebox" width="13%"><s:text name="inspectionlbl.docket.abuttingRoadgainpublic"/></td> 
	   			<td class="bluebox"><s:select id="abuttingRoad_gainPrivateOrPublic" name="docket.abuttingRoad_gainPrivateOrPublic"  list="#{'NA':'NA','Private':'Private','Public':'Public'}"  readonly="true"  value="%{docket.abuttingRoad_gainPrivateOrPublic}"/></td>
				
				<td class="bluebox" width="13%"><s:text name="inspectionlbl.docket.abuttingRoadgainplan"/></td> 
	   			<td class="bluebox"><s:select id="planCompliesWithSideCondition" name="docket.planCompliesWithSideCondition"  list="#{'NA':'NA','YES':'YES','NO':'NO'}"    readonly="true"     value="%{docket.planCompliesWithSideCondition}"/></td>
		   </tr>
	</table>	
