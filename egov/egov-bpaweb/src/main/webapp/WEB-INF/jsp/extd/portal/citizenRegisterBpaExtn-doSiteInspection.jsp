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

	function showWaiting() 
  	{
		document.getElementById('loading').style.display ='block';
	}
	
	function getInspection()
	{ 	
		document.citizenRegisterBpaForm.action = "/bpa/extd/inspection/inspectionExtn!inspectionForm.action?fromreg=true";
		document.citizenRegisterBpaForm.submit();  
	}
</script>
	</head>
	<body onload="getInspection();showWaiting();">
		<form method="post" name="citizenRegisterBpaForm" onsubmit="showWaiting();">
			<input type="hidden" id="registrationId" name="registrationId" value="<s:property value="%{model.id}"/>" />
			<div id="loading" class="loading" style="width: 700; height: 700" align="left">
				<span>Please wait...</span>
			</div>
		</form>
	</body>
</html>
