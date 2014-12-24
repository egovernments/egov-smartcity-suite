<%@ taglib prefix="s" uri="/WEB-INF/struts-tags.tld"%>
<%@ taglib prefix="egov" tagdir="/WEB-INF/tags"%>
<%@ page language="java"%>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean"%>
<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html"%>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic"%>
<%@ taglib uri="/WEB-INF/struts-nested.tld" prefix="nested"%>
<html>  
<head>  
    <title><s:text name="budgetgroup.view"/></title>
   
  	</head>
 	<body>  
		<s:form action="budgetGroup" theme="simple" > 
			<jsp:include page="budgetHeader.jsp"/>
			<div class="formmainbox"><div class="subheadnew"><s:text name="budgetgroup.view"/></div>
				<%@ include file='budgetGroup-form.jsp'%>
			</div>
			<div class="buttonbottom">
				<label><input type="submit" id="closeButton" value="Close" onclick="javascript:window.close()" class="button"/></label>
			</div>
		</s:form>
		<script>
			for(var i=0;i<document.forms[0].elements.length;i++)
			{
				document.forms[0].elements(i).disabled=true;
			}
			document.forms[0].closeButton.disabled=false;
		</script>
		 
	</body>  
</html>
