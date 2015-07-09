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
<%@ taglib prefix="egov" tagdir="/WEB-INF/tags"%>
<%@taglib uri="http://displaytag.sf.net" prefix="display" %>
<%@ include file="/includes/taglibs.jsp" %>
<%@ taglib prefix="egov" tagdir="/WEB-INF/tags"%>
<html>
<head>
<style>

</style>

<link href="../css/docmgmt/documentManager.css" rel="stylesheet" type="text/css"></link>	
<title></title>
	
	<script type="text/javascript">
	
	var assessee_id='<s:property value="%{model.id}"/>';	

  <jsp:useBean id="today" class="java.util.Date" />	
  <fmt:formatDate var = "currDate" pattern="dd/MM/yyyy" value="${today}" />
  	
  	function showWaiting() 
  	{
		document.getElementById('loading').style.display ='block';
	}
	
	function getNewForm()
	{ 	document.collectForm.action = "<%=request.getContextPath()%>" +"/extd/portal/citizenRegisterBpaExtn!newCitizenForm.action";
		document.collectForm.submit(); 
	}
</script>
	</head>
	<body onload="getNewForm();showWaiting();">
		<form method="post" name="collectForm" onsubmit="showWaiting();">
	<s:hidden id="serviceType" name="serviceType" value="%{serviceType}"/>
	<s:hidden id="serviceRegId" name="serviceRegId" value="%{serviceRegId}"/>
	<s:hidden id="requestID" name="requestID" value="%{requestID}"/>
			<div id="loading" class="loading" style="width: 700; height: 700" align="left">
				<span>Please wait...</span>
			</div>
		</form>
	</body>
</html>
