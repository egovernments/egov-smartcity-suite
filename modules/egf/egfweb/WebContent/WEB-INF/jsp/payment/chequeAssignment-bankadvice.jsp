<%@ taglib prefix="s" uri="/WEB-INF/struts-tags.tld" %> 
<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html" %>
<style type="text/css">
#container iframe {
width: 100%;
height: 2000px;
border: none;
}

#container {
width: 100%;
height: 550px;
padding: 0;
overflow-y: scroll; }

@media print {
input#btnPrint {
display: none;
}
}

@media print {
input#printPDF {
display: none;
}
}

@media print {
input#printXLS {
display: none;
}
}
</style>

<body>
<div id="container">
<iframe id="report" name="report" src='/EGF/payment/chequeAssignment!ajaxGenerateAdviceHtml.action?instHeaderId=<s:property value="%{instHeaderList[0].id}"/>'></iframe>
</div> 

<s:form name="chequeAssignmentForm" action="chequeAssignment" theme="simple" >
	<s:hidden name="instHeaderId" id="instHeaderId" value="%{instHeaderList[0].id}"/>
	<div id="buttons">
		<input type="button" id="btnPrint" onclick="javascript:parent.report.print();" value="PRINT" class="button"/>
		<s:submit id="printPDF" value="SAVE AS PDF" method="generateAdvicePdf" cssClass="buttonsubmit"/>
		<s:submit id="printXLS" value="SAVE AS EXCEL" method="generateAdviceXls" cssClass="buttonsubmit"/>        
	</div>
</s:form>
</body>