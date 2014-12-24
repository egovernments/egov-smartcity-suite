<%@ taglib prefix="s" uri="/WEB-INF/struts-tags.tld" %> 
<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html" %>
<style type="text/css">
#container iframe {
width: 100%;
height: 500px;
border: none; }

#container {
width: 100%;
height: 100%;
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
@media print {
div.commontopyellowbg {
display: none;
}
}
@media print {
div.commontopbluebg {
display: none;
}
}
</style>
<script>
function exportXls(){
	window.open('/EGF/voucher/journalVoucherPrint!exportXls.action?id=<s:property value="id"/>','','resizable=yes,height=650,width=900,scrollbars=yes,left=30,top=30,status=no');
}

function exportPdf(){
	window.open('/EGF/voucher/journalVoucherPrint!exportPdf.action?id=<s:property value="id"/>','','resizable=yes,height=650,width=900,scrollbars=yes,left=30,top=30,status=no');
}
</script>
<body>
<div id="container">
<iframe id="report" name="report" src='/EGF/voucher/journalVoucherPrint!ajaxPrint.action?id=<s:property value="id"/>' ></iframe>
</div> 

<s:form name="journalVoucherPrint" action="journalVoucherPrint" theme="simple" >
	<input type="hidden" name="id" value='<s:property value="id"/>'/>
	<div id="buttons">
		<input type="button" id="btnPrint" onclick="javascript:parent.report.print();" value="Print" class="button"/>
		<input type="button" id="printPDF" onclick="return exportPdf();" value="Save as PDF" class="button"/>
		<input type="button" id="printXLS" onclick="return exportXls();" value="Save as Excel" class="button"/>
	</div>
</s:form>
</body>