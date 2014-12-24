<%@ include file="/includes/taglibs.jsp"%>
<%@ taglib prefix="s" uri="/WEB-INF/struts-tags.tld"%>
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
<title>Comparision Report</title>

</head>

<body>
<s:form action="comparisionReport" theme="simple"> 
<s:hidden id="idTemp" name="idTemp" value="%{idTemp}" />
<s:hidden id="mode" name="mode" value="view" />
<p> <s:text name="noResponsesAvailablelbl"/> </p>  
	<div class="buttonbottom">  
	
	<input name="close" type="button" class="button"
		id="buttonClose" value="Close" onclick="window.close();" /> &nbsp;
	
	</div>
   
</s:form>
</body>
</html>