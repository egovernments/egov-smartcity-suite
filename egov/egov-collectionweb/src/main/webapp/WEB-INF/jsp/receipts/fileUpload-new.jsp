<%@ include file="/includes/taglibs.jsp"%>
<%@ taglib prefix="html" uri="/WEB-INF/struts-html.tld" %> 
<head>
<title>File Upload</title>
<script>
function callMisc(){
	document.forms[0].action="miscellaneousFileUpload!save.action";
	document.forms[0].submit();
}
function callValidateChallan(){
	document.forms[0].action="fileUpload!fileValidate.action";
	document.forms[0].submit();
}

function callValidateMisc(){
	document.forms[0].action="miscellaneousFileUpload!fileValidate.action";
	document.forms[0].submit();
}

function callCreateVouchers(){
	document.forms[0].action="fileUpload!createVouchers.action";
	document.forms[0].submit();
}

</script>
</head>
<body>

<s:form theme="simple" name="fileUpload" enctype="multipart/form-data" method="POST"> 
	<div class="formmainbox"><div class="subheadnew">File Upload</div>
		<table width="100%" border="0" align="center" cellpadding="0"
			cellspacing="0" class="tablebottom">
			
				<s:hidden name="caption" id="caption"/>
				<tr>
					<td width="4%" class="bluebox2">&nbsp;</td>
				     	<td width="21%" class="bluebox2">Upload File</td>
					<td width="24%" class="bluebox2"><s:file name="importFile" /></td>
				     	<td width="21%" class="bluebox2">&nbsp;</td>
					<td width="30%" class="bluebox2">&nbsp;</td>
  				</tr>
				</table>	
			
			


			<div align="left" class="mandatorycoll"><s:text name="common.mandatoryfields"/></div>
				<div class="buttonbottom">
					<s:submit type="submit" cssClass="buttonsubmit" id="button" value="Upload Challan Receipts" method="save" />
					<input name="button" type="button" class="buttonsubmit" id="buttonchallanvalidate" value="Validate Challan Receipts" onclick="callValidateChallan();" />
					&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
					<input name="button" type="button" class="buttonsubmit" id="buttonmisc" value="Upload Misc Receipts" onclick="callMisc();" />
					<input name="button" type="button" class="buttonsubmit" id="buttonmiscvalidate" value="Validate Misc Receipts" onclick="callValidateMisc();" />
					
					<input name="button" type="button" class="buttonsubmit" id="buttonmiscvalidate" value="Create Vouchers for Approved Receipts" onclick="callCreateVouchers();" />
					
					<br/>					
					<input name="button" type="button" class="button" id="buttonclose2" value="Close" onclick="window.close();" />
					
				</div>
			</div>
</s:form>
</body>
