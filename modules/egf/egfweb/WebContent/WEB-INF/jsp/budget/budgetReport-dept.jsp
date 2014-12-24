<%@ taglib prefix="s" uri="/WEB-INF/struts-tags.tld"%>
<%@ page language="java"%>
<%@ page import="org.egov.budget.model.*"%>
<jsp:include page="budgetHeader.jsp">
    <jsp:param name="heading" value="Budget Report - Departmentwise" />
</jsp:include> 
<s:actionmessage theme="simple"/>
<s:actionerror/>  
<s:fielderror />  
<script>
	function popUp(url) {
		newwindow=window.open(url,'name','height=200,width=150');
		if (window.focus) {newwindow.focus()}
		return false;
	}
</script>
<s:actionmessage theme="simple"/>
<s:form name="budgetDetailReportForm" action="budgetReport" theme="simple" >
	<div class="formmainbox"><div class="subheadnew">Budget Report - Departmentwise</div>
		<%@include file="budgetReport-form.jsp" %>
		<div class="buttonbottom" style="padding-bottom:10px;">
			<s:submit value="PRINT " method="printDepartmentWiseReport" cssClass="buttonsubmit"/>
			<s:submit value="SAVE AS PDF" method="generateDepartmentWisePdf" cssClass="buttonsubmit"/>
			<s:submit value="SAVE AS EXCEL" method="generateDepartmentWiseXls" cssClass="buttonsubmit"/>       
		</div>
	</div>
</s:form>  
<script>
	document.getElementById('function').style.display="none";
	document.getElementById('function_label').style.visibility="hidden";
</script>
