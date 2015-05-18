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
<SCRIPT>
function ismaxlength(obj){
	var mlength=obj.getAttribute? parseInt(obj.getAttribute("maxlength")) : ""
	if (obj.getAttribute && obj.value.length>mlength)
	obj.value=obj.value.substring(0,mlength)
}
</SCRIPT>
<div align="center"> 
  
	<div id="lpchecketails" class="formmainbox">
	<s:if test="%{lpChkListDet.size!=0}">
	<h1 class="subhead" >Check List Details</h1>
          
		  <s:iterator value="lpChkListDet" status="row_status">
		 
		   <table id="checklists" width="100%" border="0" cellspacing="0" cellpadding="0" class="tablebottom">
		 
			    <tr>	     
				    <td class="bluebox" width="30%">&nbsp;</td>			
				   	<td class="bluebox" width="30%"><s:text name="lpChkListDet[%{#row_status.index}].checkListDetails.description"/></td>		   	
					<td class="bluebox" id="mandatorycheck"><s:hidden  name="lpChkListDet[%{#row_status.index}].checkListDetails.isMandatory"/>
					<s:checkbox  name="lpChkListDet[%{#row_status.index}].isChecked"/></td>	
					<td class="bluebox" ><s:textarea   cols="20" rows="3" name="lpChkListDet[%{#row_status.index}].remarks"  maxlength="300" onkeyup="return ismaxlength(this)" /></td>	
					<s:hidden name="lpChkListDet[%{#row_status.index}].checkListDetails.id"/>
					<s:hidden name="lpChkListDet[%{#row_status.index}].checkListDetails.checkList.id"/>
					<s:hidden name="lpChkListDet[%{#row_status.index}].checkListDetails.description"/>  
					<s:hidden name="lpChkListDet[%{#row_status.index}].id"/>
					<td class="bluebox" width="">&nbsp;</td>				 
			  	</tr>   
		    
		    </table>
		 
		    </s:iterator>
		       <s:hidden id="checklistsize" name="lpChkListDet.size"/>
	  </s:if>	 
		 
    </div>
</div>

