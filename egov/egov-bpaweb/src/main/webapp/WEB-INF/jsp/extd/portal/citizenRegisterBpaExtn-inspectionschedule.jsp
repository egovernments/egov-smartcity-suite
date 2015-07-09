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
<%@ taglib prefix="egov" tagdir="/WEB-INF/tags"%>
<%@taglib prefix="sj" uri="/WEB-INF/struts-jquery-tags.tld"%>
<%@ include file="/includes/taglibs.jsp" %>

<html>

<head>
 <sj:head jqueryui="true" jquerytheme="redmond" />
	<title>View Inspection Schedule</title>
	
<script type="text/javascript">

 
  jQuery.noConflict();
</script>
</head>



<body>

<div align="center"> 
 <div id="dplandetails" class="formmainbox">
  
	<div align="center" id="checklistdiv">
	<s:if test="postponedInspectionDetails.size!=0">
	<div id="inspectdetls" align="center">
	<h1 class="subhead" ><s:text name="Inspection Schedule Details"/></h1>
   <table id="postponetable" width="80%" border="0" cellspacing="0" cellpadding="0" class="tablebottom">
	     
	      <tr>
			<th  class="bluebgheadtd" width="3%"><div align="center">Sl No</div></th>
		    <th  class="bluebgheadtd" width="8%"><div align="center">Site Inspection fixed Date</div></th>
		    <th  class="bluebgheadtd" width="8%"><div align="center"> Site Inspection Postponed Date</div></th>
		     <th  class="bluebgheadtd" width="8%"><div align="center"> PostPone Reason</div></th>
		   
		 </tr>
		  <s:iterator value="postponedInspectionDetails" status="row_status">
		    <tr>
		  	<td  class="blueborderfortd"> <div align="center"><s:text name="#row_status.count" /></div></td>
		    <td class="blueborderfortd"><div align="center"><s:property value="%{inspectionDateString}" /></div></td>
		    <td  class="blueborderfortd"><div align="center"><s:property value="%{postponedDateString}" /></div></td>
		  	<td  class="blueborderfortd"><div align="center"><s:property value="%{postponementReason}" /></div></td>
		  			  
		    </tr>
		    </s:iterator>
	  </table>
		 
    </div>
    </s:if>
    <s:else>
    
    <div align="center">
    <p>No Details Found.</p>
    </div>
    
    </s:else>
	 
	<div class="buttonbottom" align="center">
			<table>
				<tr>
					<td><input type="button" name="back" id="back" class="button" value="Back" onclick="history.back()"/></td>
			  		
			  	</tr>
	        </table>
	   </div>	   	 
    </div>
    
    
  
	</div> 

	
	
</div>



 </body>
</html>
