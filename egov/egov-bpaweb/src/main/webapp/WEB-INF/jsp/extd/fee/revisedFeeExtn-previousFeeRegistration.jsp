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
 <%@taglib prefix="sj" uri="/WEB-INF/taglibs/struts-jquery-tags.tld"%>
<%@page pageEncoding="utf-8" contentType="text/html; charset=utf-8"%>

<s:set name="theme" value="simple" scope="page" />
<SCRIPT>
  jQuery.noConflict();

</SCRIPT>
 <div id="prevfeedtls" align="center"> 
<s:if test="%{existingFeeDetails.size>0}">
  <h1 class="subhead" ><s:text name="Previous Fee Details"/></h1>
	
	<sj:dialog 
    	id="prevfeedtl" 
    	autoOpen="false" 
    	modal="true" 
    	title="Previous Fee Details"
    	openTopics="openprevfeeDialog"
    	height="500"
    	width="700"
    	dialogClass="formmainbox"
    	showEffect="slide" 
    	hideEffect="slide" 
    	onOpenTopics="openTopicDialog" cssStyle="BACKGROUND-COLOR: #ffffff"
    	onCompleteTopics="dialogopentopic" 
    	loadingText="Please Wait ...."
    	errorText="Permission Denied"
    />

  <div id="header" class="formmainbox" align="center">
	    <table id="previousfeedetailtable" width="80%" border="0" cellspacing="0" cellpadding="0" class="tablebottom">
	       
	      <tr>
			<th  class="bluebgheadtd" width="3%"><div align="center">Sl No</div></th>
		    <th  class="bluebgheadtd" width="8%"><div align="center">Date</div></th>	
		    <th  class="bluebgheadtd" width="8%"><div align="center">Challan Number</div></th>	
		     <th  class="bluebgheadtd" width="8%"><div align="center">Status</div></th>		   
		    <th  class="bluebgheadtd" width="8%"><div align="center">Fee Details</div></th>
		  </tr>
		  <s:iterator value="existingFeeDetails" status="rowfee_status">
		    <tr>
		  	<td  class="blueborderfortd"> <div align="center"><s:text name="#rowfee_status.count" /></div></td>
		    <td  class="blueborderfortd"><div align="center"><s:property value="%{formattedFeeDate}" /></div></td>
		   <td  class="blueborderfortd"><div align="center"><s:property value="%{challanNumber}" /></div></td>
		   <td  class="blueborderfortd"><div align="center"><s:property value="%{egwStatus.code}" /></div></td>
		   
		    <s:if test="%{legacyFee!='true'}">
		   <s:url id="prevfeelink" value="/extd/fee/revisedFeeExtn!showFeeDetails.action" escapeAmp="false">
		       <s:param name="registrationId" value="%{registration.id}"></s:param>	
		      <s:param name="registrationFeeId" value="%{id}"></s:param>	
		   </s:url> 
		   </s:if>
		   <s:else>
		     <s:url id="prevfeelink" value="/extd/fee/feeDetailsExtn!showFeeDetailsinRegistration.action" escapeAmp="false">
		       <s:param name="registrationId" value="%{registration.id}"></s:param>	
		        <s:param name="registrationFeeId" value="%{id}"></s:param>	
		      
		   </s:url> 	   
		   </s:else>
		  	<td  class="blueborderfortd"><div align="center"> <sj:a  onClickTopics="openprevfeeDialog" href="%{prevfeelink}" button="true" buttonIcon="ui-icon-newwin">View</sj:a></div></td>		  
		      
		    </tr>
		    </s:iterator>
		   
	  </table>
	 </div>
	 
 </s:if>
</div>
