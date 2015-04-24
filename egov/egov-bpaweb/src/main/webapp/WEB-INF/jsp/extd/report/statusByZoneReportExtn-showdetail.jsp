<%@ taglib prefix="s" uri="/WEB-INF/struts-tags.tld"%>
<%@ taglib prefix="egov" tagdir="/WEB-INF/tags"%>
<%@	taglib uri="http://displaytag.sf.net" prefix="display" %>
<%@ include file="/includes/taglibs.jsp" %>
<html>

<head>
<title> Application Details</title>
</head>
<script type="text/javascript">
function viewRegisterBpa(registrationId){
	document.location.href="${pageContext.request.contextPath}/extd/register/registerBpaExtn!viewForm.action?registrationId="+registrationId;		
 }
</script>
<body>

<s:form action="statusByZoneReportExtn" name="searchForm" theme="simple">

		
<table>

 <div id="tableData">
 
 
	  
          		 <div id="displaytbl">	
          		     	 <display:table  name="regList" export="false" requestURI="" id="registrationId"  class="its" uid="currentRowObject" pagesize="25">
          			 	 <div STYLE="display: table-header-group" align="center">
          			 	  
 						 	<display:column title=" Sl No" style="text-align:center;"  >
 						 	 <s:property value="#attr.currentRowObject_rowNum"/></display:column>
 						 	 
 						<display:column title="Plan Submission Number " style="text-align:center;" >	
 						 	<a href="#" onclick="viewRegisterBpa('${currentRowObject.id}')">
 						 		 ${currentRowObject.planSubmissionNum}
 						 	</a>
 						 	</display:column><display:column title="Application Date " style="text-align:center;"  >	
								<s:date name="#attr.currentRowObject.planSubmissionDate" format="dd/MM/yyyy" />
							</display:column> 
 						
							<display:column title="Applicant Name " style="text-align:center;" property="owner.firstName" />	
							<display:column title="Mobile Number" style="text-align:center;" property="owner.mobilePhone" /> 
							<display:column title="Applicant Address " style="text-align:center;" property="bpaOwnerAddress" />	
						
						<display:column title="Current Owner" style="text-align:center;" >
          			        	<s:property value="%{getUsertName(#attr.currentRowObject.state.owner.id)}" />
          			   </display:column>
						<display:column title="Service Type" style="text-align:center;" property="serviceType.description" />		
						<display:column title="Surveyor Name"	 style="text-align:center" property="surveyorName.name"	/>
						<display:column title="Status " style="text-align:center;" property="egwStatus.code" />	
						<display:column title="Last Transaction Date " style="text-align:center;"  >	
								<s:date name="#attr.currentRowObject.modifiedDate" format="dd/MM/yyyy" />
							</display:column> 
 						 
 						 	
 						 	</div>
 						 	</display:table>
 						 	</div>
 						 	</div>
 						<div>
	   
	   </div>
	   </table>
 						 	</s:form>
 						 	


</body>
</html>