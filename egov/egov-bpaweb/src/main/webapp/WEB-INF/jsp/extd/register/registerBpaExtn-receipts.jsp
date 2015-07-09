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
<%@taglib prefix="sj" uri="/WEB-INF/struts-jquery-tags.tld"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@page pageEncoding="utf-8" contentType="text/html; charset=utf-8"%>
<s:set name="theme" value="'simple'" scope="page" />

 <SCRIPT>
  jQuery.noConflict();

</SCRIPT>

 <div align="center" id="orddetdiv">
 	<table width="100%" border="0" cellspacing="0" cellpadding="0" id="orderTbl">
 		<tr>
						<td colspan="6"><div class="headingbg"><span class="bold">Fee collection Details</span></div></td>
					</tr>
 		<tr>
 			<td>
 		
 	      <s:if test="%{billRecptInfoList.size!=0}">
  	        <c:set value="0" var="i"/>
  	       
 			<s:iterator value="billRecptInfoList" var="receipt">
			 		<table width="100%" border="0" cellspacing="0" cellpadding="0" id="signDet">   
					 <tr>
						<td class="bluebox" width="13%"><c:set value="${i+1}" var="i"/>
						</td>
							<td class="bluebox" width="13%">Receipt Number:</td>
						<td class="bluebox"> 
							<b> 	<a href="/../collection/citizen/onlineReceipt!viewReceipt.action?receiptNumber=<s:property value="#receipt.getReceiptNum()" />&consumerCode=<s:property value="%{registrationId}" />&serviceCode=EXTDBPA" target="_blank" >
                                                       <s:property value="#receipt.getReceiptNum()" /> 
                                                    </a></b>
						</td>
						<td class="bluebox">Dated : </td>
						<td class="bluebox">
							<s:date name="#receipt.getReceiptDate()" format="dd/MM/yyyy" var="rcptdate" /><s:property value="rcptdate" /></div></td>
						<td class="bluebox">with amount Rs. :<b> <s:property value="#receipt.getTotalAmount()" /></b></td>
					</tr>
						
				</table>
	 		
	 		
	 	
			</s:iterator> 
		  </s:if>
		  <s:else>
		  <td  class="blueborderfortd"> No Receipts found.</td>
 		</s:else>	
			</td></tr>   
		</table>
 </div>
 
