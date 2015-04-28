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
<%@ page language="java" pageEncoding="UTF-8"%>
<%@ include file="/includes/taglibs.jsp" %>
<%@ taglib prefix="s" uri="/WEB-INF/struts-tags.tld"%>
<%@ taglib prefix="egov" tagdir="/WEB-INF/tags"%>
<%@taglib prefix="sj" uri="/WEB-INF/struts-jquery-tags.tld"%>

<meta http-equiv="Content-Type" content="text/html; charset=utf-8" /> 
<html>
	<title><s:text name="Site Inspection Details"/></title>

	<script>
	function gotoRegistration(requestID,serviceRegId){
		document.location.href="${pageContext.request.contextPath}/extd/portal/citizenRegisterBpaExtn!viewForm.action?requestID="+requestID+"&serviceRegId="+serviceRegId;
	}

	function showPageHeader(){
		var surveyorLogin= document.getElementById('isSurveyor').value; 
		if(surveyorLogin=='true'){
			dom.get("breadcrumbHeader").innerHTML='<s:text name="surveyorPortal.header" />';
		}
		else{
			dom.get("breadcrumbHeader").innerHTML='<s:text name="cocUser.header" />';
		}
	}
	</script> 

	<body onload="refreshInbox();showPageHeader();">
		<s:if test="%{hasActionMessages()}">
			<div class="errorstyle">
				<s:actionmessage /> <br>
			</div>
		</s:if> 

		<s:form theme="simple" name="successForm">
			<s:hidden id="serviceRegId" name="serviceRegId" value="%{serviceRegId}"/>
			<s:hidden id="requestID" name="requestID" value="%{requestID}"/>
			<s:hidden id="isSurveyor" name="isSurveyor" value="%{isSurveyor}"/>
			<s:if test="%{isSurveyor}">
				<table  width="100%" border="0" cellspacing="0" cellpadding="0" class="tablebottom">
					<tr>
						<td>
							 <div align="center" class="errorstyle">  
					 			<a href="#" onclick="gotoRegistration('<s:property value="%{requestID}"/>','<s:property value="%{serviceRegId}"/>')" style="width: 20px;height: 20px;">
					 						 	Click Here</a> for further actions.
					 		 </div>
				 		</td> 
				 	</tr>
				</table>
			</s:if>	
		</s:form>	
	</body>
</html>
