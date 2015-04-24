<%@ include file="/includes/taglibs.jsp"%>
<%@ taglib prefix="html" uri="/WEB-INF/struts-html.tld" %> 
<head>
<title>File Upload</title>
<script>
function callCreateVouchers(){
	document.forms[0].action="historicDataUpload!save.action";
	document.forms[0].submit();
}

</script>
</head>
<body>

<s:form theme="simple" name="fileUpload" enctype="multipart/form-data" method="POST"> 
	<div class="formmainbox"><div class="subheadnew">File Upload</div>
		<table width="100%" border="0" align="center" cellpadding="0"
			cellspacing="0" class="tablebottom">
		</table>	
			
			


			<div align="left" class="mandatorycoll"><s:text name="common.mandatoryfields"/></div>
				<div class="buttonbottom">
					
					
					<input name="button" type="button" class="buttonsubmit" id="buttonmiscvalidate" value="Upload Historic Data" onclick="callCreateVouchers();" />
					
							
					<input name="button" type="button" class="button" id="buttonclose2" value="Close" onclick="window.close();" />
					
				</div>
			</div>
</s:form>
</body>
