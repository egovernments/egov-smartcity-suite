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


<s:set name="theme" value="'simple'" scope="page" />


<div align="center"> 
 <div id="plotdetails" class="formmainbox">

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
	 			<td class="bluebox" id="mandatoryfields"><s:text name="inspectionlbl.msr.bldgextnt"/><span class="mandatory">*</span></td> 
	   			<td class="bluebox" id="wholenumbers"><s:textfield id="buildingextent" name="inspection.inspectionDetails.buildingExtent" value="%{inspection.inspectionDetails.buildingExtent}" maxlength="10"/></td>
				<td class="bluebox" id="mandatoryfields"><s:text name="inspectionlbl.msr.plots"/><span class="mandatory">*</span></td> 
				<td class="bluebox" id="wholenumbers"><s:textfield id="numberofplots" name="inspection.inspectionDetails.numOfPlots" value="%{inspection.inspectionDetails.numOfPlots}" maxlength="10"/></td>
				<td class="bluebox">&nbsp;</td>
	         </tr>         
	      </table>
	 </div>
	  
	</s:if>	 
    </div>
  
	</div> 

	
	
</div>

