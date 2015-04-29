<%@ taglib prefix="s" uri="/WEB-INF/struts-tags.tld" %>  
<html>
<title>
<s:text name='project.closure'/>
</title>
<body>
<align="center">
<br>
<br>
	<h4><s:text name="projectCompletionReport.project.close"><s:param name="value" value="%{projectCode.getCode()}" /></s:text><br></h4>

	<div class="buttonholderwk">
		<input type="button" class="buttonfinal" value="CLOSE" id="closeButton" name="button" onclick="window.close();" />
    </div>
</align>
</body>
</html>   