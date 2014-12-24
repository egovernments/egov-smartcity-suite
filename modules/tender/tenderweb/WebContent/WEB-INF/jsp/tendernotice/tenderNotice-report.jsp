<%@ include file="/includes/taglibs.jsp"%>
<%@ taglib prefix="s" uri="/WEB-INF/struts-tags.tld"%>
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
<title>Tender Notice</title>
</head>

<body>
<s:form action="tenderNotice" theme="simple"> 
<s:hidden id="idTemp" name="idTemp" value="%{idTemp}" />
<s:hidden id="mode" name="mode" value="view" />
<iframe src="../reportViewer?reportId=<s:property value='reportId'/>" width="98%"    
		height="70%">
	<p>Your browser does not support iframes.</p>
	</iframe>
	<br />
<div class="buttonbottom">  
	
	<input name="close" type="button" class="button"
		id="buttonClose" value="Close" onclick="window.close();" /> &nbsp;
	
	</div>
   
</s:form>
</body>
</html>