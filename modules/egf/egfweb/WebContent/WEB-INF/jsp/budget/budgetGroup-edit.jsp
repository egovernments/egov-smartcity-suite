<%@ taglib prefix="s" uri="/WEB-INF/struts-tags.tld"%>
<%@ taglib prefix="egov" tagdir="/WEB-INF/tags"%>
<%@ page language="java"%>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean"%>
<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html"%>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic"%>
<%@ taglib uri="/WEB-INF/struts-nested.tld" prefix="nested"%>
<html>  
<head>  
    <title><s:text name="budgetgroup.modify"/></title>
   
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
			<div class="formmainbox"><div class="subheadnew"><s:text name="budgetgroup.modify"/></div>
				<%@ include file='budgetGroup-form.jsp'%>
				<td><div align="left" class="mandatory">* <s:text name="mandatory.fields"/></div></td>
				<s:hidden  name="model.id" />
			</div>
			<div class="buttonbottom">
				<s:submit method="save" value="Save " cssClass="buttonsubmit"/>  </label>
				<s:reset name="button" type="submit" cssClass="button" id="button" value="Cancel"/>
				<label><input type="submit" value="Close" onclick="javascript:window.close()" class="button"/></label>
			</div>
		</s:form> 
		<s:hidden name="target" id="target" value="%{target}" />
		<s:hidden name="modevalue" id="modevalue" value="%{mode}"/>
	</body>  
</html>
