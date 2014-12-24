<%@ taglib prefix="s" uri="/WEB-INF/struts-tags.tld" %> 
<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html" %>
<style type="text/css">
#container iframe {
width: 100%;
height: 1000px;
border: none; }

#container {
width: 100%;
height: 800px;
padding: 0;
overflow: hidden; }

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
<iframe id="report" name="report" src='/EGF/report/expenseJournalVoucherPrint!ajaxPrint.action?id=<s:property value="id"/>' ></iframe>
</div> 
<s:form name="expenseJournalVoucherPrint" action="expenseJournalVoucherPrint" theme="simple" >
	<input type="hidden" name="id" value='<s:property value="id"/>'/>
	<div id="buttons">
		<input type="button" id="btnPrint" onclick="javascript:parent.report.print();" value="Print" class="button"/>
		<s:submit cssClass="button" value="Save as PDF" method="exportPdf"	id="printPDF"/>
		<s:submit cssClass="button" value="Save as Excel" method="exportXls"	id="printXLS"/>
	</div>
</s:form>
</body>