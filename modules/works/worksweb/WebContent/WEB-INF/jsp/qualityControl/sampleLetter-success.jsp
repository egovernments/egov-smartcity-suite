<%@ taglib prefix="egov" tagdir="/WEB-INF/tags" %>
<%@ taglib uri="http://www.opensymphony.com/sitemesh/decorator" prefix="decorator"%>
<%@ taglib prefix="s" uri="/WEB-INF/struts-tags.tld" %>  
<html>

<title>Sample Letter</title>
<body onload="refreshInbox();">
<script>
function refreshInbox(){
    var x=opener.top.opener;
    if(x==null){
        x=opener.top;
    }
    x.document.getElementById('inboxframe').contentWindow.egovInbox.refresh();
}
</script> 

<table align="center">
<tr>
<td align="center">
	<s:if test="%{hasErrors()}">
		<div class="errorstyle">
			<s:actionerror />
			<s:fielderror />
		</div>
	</s:if>
	<s:if test="%{hasActionMessages()}">
		<div class="messagestyle">
			<s:actionmessage theme="simple" />
		</div>
	</s:if>
	<s:if test="%{fileNotifySuccessMessage != ''}">
		<s:property value="%{fileNotifySuccessMessage}"/>
	</s:if>
	<br/>
	<s:if test="%{egwStatus!=null && egwStatus.code=='APPROVED'}">
		<div class="buttonholdersearch" align = "center">
   			<input type="button" value="Sample Letter PDF" class="buttonpdf" id="pdfSampleButton" onclick="window.open('${pageContext.request.contextPath}/qualityControl/sampleLetter!generateSampleLetterPdf.action?slId=<s:property value="id"/>');"  name="button" />
   			<input type="button" value="Covering Letter PDF" class="buttonpdf" id="pdfCoveringButton" onclick="window.open('${pageContext.request.contextPath}/qualityControl/sampleLetter!generateCoveringLetterPdf.action?slId=<s:property value="id"/>');" name="button" />
   		</div>
   	</s:if>	
</td>
</tr>
</table>
	