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
<script></script>

<table width="100%" border="0" cellspacing="0" cellpadding="0">
<tr>
	<td colspan="4">
		<div class="headingbg">
		<span class="bold">
			<s:text name="page.title.viewPdf.details" />
		</span>
		</div>
	</td>
</tr>
	
<tr> 
<td>
	<div>
	  	<table width="100%" cellpadding="0" cellspacing="1" border="0"  class="tablebottom" id="regnOfficialActionsList" style="border:1px;width:100%;empty-cells:show;">
			<tr>
			<th class="bluebgheadtd" style="text-align:center;"> <s:text name="slNo.label" /> </th>
			<th  class="bluebgheadtd" style="text-align:center;"> <s:text name="regPdf.label" /> </th>
				<s:iterator value="regnOfficialActionsList[0].officialActions" status="s1"> 
					<th class="bluebgheadtd"  style="text-align:center;">
						<table width="100%" border="0" cellpadding="0" class="tablebottom" cellspacing="1" style="border:1px;width:100%;empty-cells:show;">
				       		<tr>
				       			<s:property value="userName"/>
				       		</tr>
			       			</table>						
					</th>
				</s:iterator>
			</tr>
			<s:iterator id="t" var="mo" value="regnOfficialActionsList" status="s">
			<tr>
				<td class="bluebox" style="text-align:center" ><s:property value='#s.index+1'/></td>	 		  	        		
				<td class="bluebox" style="text-align:left;" ><s:property value="viewType"/></td>
			
				 <s:iterator id="t2" var="mod" value="officialActions" status="s2">
				 	<td class="bluebox">					 	
				 	<table width="100%" border="0" cellpadding="0" cellspacing="1" style="border:0px;width:100%;empty-cells:show;">					       		
				       		<tr>
				       		<td class="bluebox" width="40%" style="white-space: nowrap;">
				       		<s:checkbox id="viewTypeCheckBox" name='viewTypeCheckBox' value="%{actionsValue}"></s:checkbox> 
						</td>					       			
				       		</tr>
			       		</table>						
				 </td> 
				 </s:iterator>
				
			</tr>						 
		        </s:iterator>				
		</table>
	</div>
</td>	
</tr>
</table>
