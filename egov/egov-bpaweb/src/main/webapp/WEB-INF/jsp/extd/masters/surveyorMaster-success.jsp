<%@ taglib prefix="s" uri="/WEB-INF/struts-tags.tld"%>
<%@ taglib prefix="egov" tagdir="/WEB-INF/tags"%>
<%@ include file="/includes/taglibs.jsp"%>
<%@ taglib prefix="egov" tagdir="/WEB-INF/tags"%>

<html>
<head>
<title><s:text name="surveyor.master.create"/></title>
</head>
<body>
<body>
<s:if test="%{hasErrors()}">
    <div class="errorstyle">
      <s:actionerror/>
      <s:fielderror/>
    </div>
</s:if>
<s:else>
	<table cellpadding="0" cellspacing="0" border="0" class="main" align="center">
	<tr>Surveyor created successfully with username and password as: <b><s:property value="%{code}" /></b>. Default password should be changed upon login in portal.</tr>
	
	</table>
	
</s:else>

<br/>
<div class="buttonbottom">
<input name="button2" type="button" class="button" id="button" onclick="window.close()" value="Close"/>
</div>
</body>
</body>
</html>
