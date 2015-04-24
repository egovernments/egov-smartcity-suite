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