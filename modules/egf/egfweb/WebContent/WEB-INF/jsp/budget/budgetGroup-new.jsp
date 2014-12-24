<%@ taglib prefix="s" uri="/WEB-INF/struts-tags.tld"%>
<%@ taglib prefix="egov" tagdir="/WEB-INF/tags"%>
<%@ page language="java"%>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean"%>
<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html"%>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic"%>
<%@ taglib uri="/WEB-INF/struts-nested.tld" prefix="nested"%>
<html>  
<head>  
    <title><s:text name="budgetgroup.create"/></title>
</head> 
	<body>  
		<s:form action="budgetGroup" theme="simple" >
		<s:token/>  
			<jsp:include page="budgetHeader.jsp"/>
			<span class="mandatory">
				<s:actionerror/>  
				<s:fielderror />
				<s:actionmessage />
			</span>
			<div class="formmainbox"><div class="subheadnew"><s:text name="budgetgroup.create"/></div>
				<%@ include file='budgetGroup-form.jsp'%>
				<td><div align="left" class="mandatory">* <s:text name="mandatory.fields"/></div></td>
				<div class="buttonbottom">
					<label></label>
					<label><s:submit method="create" value="Save " cssClass="buttonsubmit" />  </label>
					<s:reset name="button" type="submit" cssClass="button" id="button" value="Cancel"/>
					<label><input type="submit" value="Close" onclick="javascript:window.close()" class="button"/></label> 
				</div>
			</div>
		</s:form>  
		<s:hidden name="targetvalue" value="%{target}" id="targetvalue"/>
		<s:hidden name="mode" value="%{mode}" id="mode"/>
		<script>
			if(dom.get('targetvalue').value=='SUCCESS')
			{
				document.forms[0].name.value="";
				document.forms[0].description.value="";
				document.forms[0].accountType.value=-1;
				document.forms[0].budgetingType.value=-1;
				document.forms[0].majorCode.value=-1;
				document.forms[0].minCode.value=-1;
				document.forms[0].maxCode.value=-1;
				document.forms[0].isActive.checked=false;
			}	
		</script>
	</body>  
</html>
