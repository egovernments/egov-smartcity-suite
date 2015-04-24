<%@ taglib prefix="s" uri="/WEB-INF/struts-tags.tld"%>
<%@ taglib prefix="egov" tagdir="/WEB-INF/tags"%>
<%@ include file="/includes/taglibs.jsp"%>
<%@taglib prefix="sj" uri="/WEB-INF/struts-jquery-tags.tld"%>

<html>
<head>

<title><s:text name="surveyor.master.create"/></title>
<script>
function actionInfo()
{
	document.forms[0].action="surveyorMaster!save.action";
	document.forms[0].submit();
}
</script>
</head>
<body onload="" >
<div class="subheadnew"></div>
<div class="errorstyle" id="surveyor_error" style="display:none;"></div>
	
	<s:if test="%{hasErrors()}">
		<div class="errorstyle">
			<s:actionerror />	
			<s:fielderror />
		</div>
	</s:if>

	<s:form action="surveyorMaster" theme="simple" >
	
	<%@ include file='surveyorMaster-form.jsp'%>
	<s:hidden id="id" name="id" value="%{id}"/>
	<div align="left" class="mandatory">* Mandatory Fields</div>
	<div class="buttonbottom" align="center">
			<table>
				<tr>
		<td><s:submit cssClass="buttonsubmit" id="Save" name="Save" value="Save"  method="save" onclick="return validation();" /></td>
		<td><input name="button2" type="button" class="button"  value="Reset" onClick="this.form.reset()" /></td>
		<td><input name="button2" type="button" class="button" id="button" onclick="window.close()" value="Close"/></td>
		</tr>
		</table>
	</div>
	 
	</s:form>
</body>
</html>
