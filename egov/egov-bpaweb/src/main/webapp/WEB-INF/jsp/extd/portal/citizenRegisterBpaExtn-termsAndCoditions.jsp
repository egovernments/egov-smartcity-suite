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
 <SCRIPT>
</SCRIPT>
 <div id="termsConditiondetail" align="center"> 
	 <div class="formmainbox">
		<div align="center" > 
			<h1 class="subhead"><u>Terms And Conditions</u></h1>
			<s:if test="%{!isUserMappedToSurveyorRole()}">
				<table  width="100%" border="0" cellspacing="0" cellpadding="0" class="tablebottom">
					<tr>
						<td class="bluebox"><span class="bold">1. I here with submit that all the details and particulars furnished are true to my best of the knowledge.</span></td> 
					 </tr>
					 <tr>
						<td class="bluebox"><span class="bold">2. If anything is found later that the document is forged or any facts are suppressed by me, I may be held responsible.</span></td> 
					 </tr>
					 <tr>
						<td class="bluebox"><span class="bold">3. Corporation of Chennai shall initiate any action if the documents are forged by me or any facts are suppressed even at later date.</span></td> 
					 </tr>
	 			</table>
	 		</s:if>
	 	
		 	<s:else>
			 	<table  width="100%" border="0" cellspacing="0" cellpadding="0" class="tablebottom">
						<tr>
							<td class="bluebox"><span class="bold">1. I here with undertake that it has been inspected by me and the details furnished in this regard are true to the best of my knowledge.</span></td> 
						 </tr>
						 <tr>
							<td class="bluebox"><span class="bold">2. The document have been verified by me and submitted as per the document history enclosed.</span></td> 
						 </tr>
						 <tr>
							<td class="bluebox"><span class="bold">3. Corporation of Chennai shall initiates any action and suspend my license if it is found that the document are forged or any facts are suppressed even at later date.</span></td> 
						 </tr>
		 			</table>
		 	</s:else>
		</div>
	</div>
</div>
