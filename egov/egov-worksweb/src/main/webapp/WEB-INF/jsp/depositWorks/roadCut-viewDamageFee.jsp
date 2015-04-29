<%@ taglib prefix="s" uri="/WEB-INF/struts-tags.tld" %> 

<head>

<title><s:text name="depositworks.roadcut.damagefee.letter.title" /> </title>
</head>
<script>
function setLogo() {
	try {
		document.getElementById('report').contentWindow.document.getElementsByTagName('img')[0].src = '/egi/images/<s:property value="logoforHTML"/>';
		} catch (e) {
	}
}
</script>
<body>
		<input type="hidden" name="appDetailsId" value='<s:property value="appDetailsId"/>'/>
		<div class="formmainbox">
			<div class="insidecontent">
				<div class="rbroundbox2">
					<div class="rbtop2">
						<div></div>
					</div>
					<div class="rbcontent2" align="center">
						<iframe id="report" style="width:615px;height:630px" onload="setLogo()" name="report" src='${pageContext.request.contextPath}/depositWorks/roadCut!exportHtml.action?mode=view&sourcepage=damageFeePDF&appDetailsId=<s:property value="appDetailsId"/>' ></iframe>
					</div>
				</div>
			</div>
		</div>
		<div class="buttonholderwk" id="buttons">
			<input type="button" onclick="window.open('${pageContext.request.contextPath}/depositWorks/roadCut!exportPdf.action?mode=view&sourcepage=damageFeePDF&appDetailsId=<s:property value="appDetailsId"/>');" class="buttonpdf" value="Save as PDF" id="pdfButton" name="pdfButton"/>
			<input type="button" class="buttonfinal" value="CLOSE" id="closeButton" name="closeButton" onclick="window.close();"/>
		</div>
</body>