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
	
	function getCollection()
	{ 	
		document.collectForm.action = "/collection/citizen/onlineReceipt!newform.action";
		document.collectForm.submit(); 
	}
</script>
	</head>
	<body onload="getCollection();showWaiting();">
		<form method="post" name="collectForm" onsubmit="showWaiting();">
			<input type="hidden" id="collectXML" name="collectXML" value="<s:property value="%{getCollectXML()}"/>" />
			<div id="loading" class="loading" style="width: 700; height: 700" align="left">
				<span>Please wait...</span>
			</div>
		</form>
	</body>
</html>
