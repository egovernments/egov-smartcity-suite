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
<%@	taglib uri="http://displaytag.sf.net" prefix="display" %>
<%@ include file="/includes/taglibs.jsp" %>
<%@taglib prefix="sj" uri="/WEB-INF/struts-jquery-tags.tld"%>

<%@ include file="/includes/taglibs.jsp" %>
<html>

<head>
<title>Application Status Report By Zone</title>
<script type="text/javascript">

function  showDetail(wardcode,statecode)
{
	//if(wardcode='Total')
	//	wardcode='0';
	document.searchForm.action =('${pageContext.request.contextPath}/extd/report/statusByZoneReportExtn!showdetail.action?wardcode='+wardcode+'&statecode='+statecode);	
 document.searchForm.submit();           
return true;
	}
</script>
</head>


<body  >
<div class="errorstyle" id="searchRecords_error" style="display: none;"></div>
	<div class="errorstyle" style="display: none" ></div>
	<s:if test="%{hasErrors()}">
		<div class="errorstyle">
			<s:actionerror />	
			<s:fielderror />
		</div>
	</s:if>
<s:form action="statusByZoneReportExtn" name="searchForm" theme="simple">
<s:hidden name="serviceType" id="serviceType" value="%{serviceType}"/>
		 <s:hidden name="applicationFromDate" id="applicationFromDate" value="%{applicationFromDate}" />
		 <s:hidden name="egwStatus" id="egwStatus" value="%{egwStatus}"/>
		 <s:hidden name="applicationToDate" id="applicationToDate" value="%{applicationToDate}" />
		 <s:hidden name="adminboundaryid" id="adminboundaryid" value="%{adminboundaryid}"/>
		 <s:hidden name="appMode" id="appMode" value="%{appMode}"/>
		  <s:hidden name="zonecode" id="zonecode" value="%{zonecode}"/>
<div id="tableData" >
	   <div class="infostyle" id="search_error" style="display:none;"></div> 
<s:if test="%{searchMode=='result'}">
<div id="searchdetails" >

<table width="100%" class="its" border="0" cellspacing="0" cellpadding="2">	
<tr>						   
<div align="center">
<th class="bluebgheadtd" width="10%"><s:text name="Status.lbll" />
</th>   
</div>
<div align="center">
<th class="bluebgheadtd" colspan="80%" ><s:text name="Ward" />
</th>   
</div>
</tr>
<tr>
<div align="center">
<th class="bluebgheadtd" width="10%">
</div></th>
<s:iterator value="appstatusmap" status="headermapstatusward">
 <s:if test="#headermapstatusward.index ==0">
     <s:iterator value="value">
  
     <th class="bluebgheadtd" width="10%">
     <div align="center">
     <s:property value="key"/>
     </div></th>  
   
   </s:iterator>
 </s:if>
</s:iterator>

</tr>

  <s:iterator value="appstatusmap" status="outerstatus">
  
 <tr>
     <td class="blueborderfortd">
  <s:property value="key" /> 
   <s:set var="count" value="key"/>

  
 </td>
    <s:iterator value="value">
 <td class="blueborderfortd">
 <div align="center">
   <s:if test="%{value>0}">
<a href="#" onclick="showDetail('<s:property value="key"/>','<s:property value="#count"/>')"> </s:if>
       <s:property value="value"/>
</a>
 </div>
     </td>
  </s:iterator>
</tr>
 
</s:iterator>


  </table>
</div>
</s:if>
</div>

	   
	   
	  
	   </s:form>
	   </body>
	   </html>
	   
	   
