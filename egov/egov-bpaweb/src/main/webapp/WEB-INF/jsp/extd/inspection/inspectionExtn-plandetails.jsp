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
<script>

<s:if test="%{serviceTypeId==7}"> 
jQuery('#dplandetails').find("td").each(function(){
if(jQuery(this).attr('id')=='mandatoryfields')
jQuery(this).find('span').remove();
jQuery(this).attr('id', '');
}
)
</s:if>

</script>

<div align="center"> 
 <div id="dplandetails" class="formmainbox">
  
	<div align="center">
	<s:if test="%{inspectMeasurementDtls.size!=0}">
	
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
		    <td class="greybox" width="20%">&nbsp;</td>			
		   	<td class="greybox" id="mandatoryfields"><s:text name="inspectionlbl.msr.fsb"/><span class="mandatory">*</span></td>
			<td class="greybox" id="numbers"><s:textfield id="fsb" name="inspectMeasurementDtls[%{#row_status.index}].fsb" maxlength="10"/></td>
			<td class="greybox" id="mandatoryfields"><s:text name="inspectionlbl.msr.rsb"/><span class="mandatory">*</span></td>
			<td class="greybox" id="numbers"><s:textfield id="rsb" name="inspectMeasurementDtls[%{#row_status.index}].rsb " maxlength="10"/></td>
		
		    </tr>
		    <tr>
		    <td class="bluebox" width="20%">&nbsp;</td>
		   	<td class="bluebox" id="mandatoryfields"><s:text name="inspectionlbl.msr.ssb1"/><span class="mandatory">*</span></td>
			<td class="bluebox" id="numbers"><s:textfield id="ssb1" name="inspectMeasurementDtls[%{#row_status.index}].ssb1" maxlength="10"/></td>
			<td class="bluebox" id="mandatoryfields"><s:text name="inspectionlbl.msr.ssb2"/><span class="mandatory">*</span></td>
			<td class="bluebox" id="numbers"><s:textfield id="ssb2" name="inspectMeasurementDtls[%{#row_status.index}].ssb2" maxlength="10"/></td>
			<s:hidden name="inspectMeasurementDtls[%{#row_status.index}].inspectionSource.id"/>
			
		    </tr>
		    
		    
		    <tr>
		    <td class="greybox" width="20%">&nbsp;</td>	
		    <td class="greybox" width="">&nbsp;</td>	
		    <td class="greybox" width="">&nbsp;</td>	
		    <td class="greybox" width="">&nbsp;</td>	
		    <td class="greybox" width="">&nbsp;</td>	
		    <s:hidden name="inspectMeasurementDtls[%{#row_status.index}].id"/>	
		  
		
		    </tr>
		    </table>
		    </s:iterator>
	  
	</s:if>	 
    </div>
  
	</div> 

	
	
</div>

