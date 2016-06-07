
<%--
  ~ eGov suite of products aim to improve the internal efficiency,transparency,
  ~    accountability and the service delivery of the government  organizations.
  ~
  ~     Copyright (C) <2015>  eGovernments Foundation
  ~
  ~     The updated version of eGov suite of products as by eGovernments Foundation
  ~     is available at http://www.egovernments.org
  ~
  ~     This program is free software: you can redistribute it and/or modify
  ~     it under the terms of the GNU General Public License as published by
  ~     the Free Software Foundation, either version 3 of the License, or
  ~     any later version.
  ~
  ~     This program is distributed in the hope that it will be useful,
  ~     but WITHOUT ANY WARRANTY; without even the implied warranty of
  ~     MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
  ~     GNU General Public License for more details.
  ~
  ~     You should have received a copy of the GNU General Public License
  ~     along with this program. If not, see http://www.gnu.org/licenses/ or
  ~     http://www.gnu.org/licenses/gpl.html .
  ~
  ~     In addition to the terms of the GPL license to be adhered to in using this
  ~     program, the following additional terms are to be complied with:
  ~
  ~         1) All versions of this program, verbatim or modified must carry this
  ~            Legal Notice.
  ~
  ~         2) Any misrepresentation of the origin of the material is prohibited. It
  ~            is required that all modified versions of this material be marked in
  ~            reasonable ways as different from the original version.
  ~
  ~         3) This license does not grant any rights to any user of the program
  ~            with regards to rights under trademark law for use of the trade names
  ~            or trademarks of eGovernments Foundation.
  ~
  ~   In case of any queries, you can reach eGovernments Foundation at contact@egovernments.org.
  --%>

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
