<%@ taglib prefix="s" uri="/WEB-INF/struts-tags.tld"%>
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
	{ 	document.collectForm.action = "/bpa/portal/citizenRegisterBpa!newCitizenForm.action";
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
