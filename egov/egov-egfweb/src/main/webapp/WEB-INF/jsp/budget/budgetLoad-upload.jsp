<%--
  ~    eGov  SmartCity eGovernance suite aims to improve the internal efficiency,transparency,
  ~    accountability and the service delivery of the government  organizations.
  ~
  ~     Copyright (C) 2017  eGovernments Foundation
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
  ~            Further, all user interfaces, including but not limited to citizen facing interfaces,
  ~            Urban Local Bodies interfaces, dashboards, mobile applications, of the program and any
  ~            derived works should carry eGovernments Foundation logo on the top right corner.
  ~
  ~            For the logo, please refer http://egovernments.org/html/logo/egov_logo.png.
  ~            For any further queries on attribution, including queries on brand guidelines,
  ~            please contact contact@egovernments.org
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
  ~
  --%>


<html>
<%@ include file="/includes/taglibs.jsp"%>
<%@ page language="java"%>

<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title><s:text name="budgetload" /></title>
<script type="text/javascript">
	function validate() {
		document.getElementById("msg").innerHTML = "";
		document.getElementById("Errors").innerHTML = "";

		if (document.getElementById("budgetInXls").value == "") {
			bootbox.alert("Select File to upload");
			return false;
		}
		document.budgetLoad.action = '/EGF/budget/budgetLoad-upload.action';
		document.budgetLoad.submit();
		return true;
	}
	function urlLoad(fileStoreId) {
		var sUrl = "/egi/downloadfile?fileStoreId=" + fileStoreId
				+ "&moduleName=EGF";
		window.open(sUrl, "window",
				'scrollbars=yes,resizable=no,height=400,width=400,status=yes');

	}
	jQuery(document).ready(function() {
		var fileformats = [ 'xls' ];

		jQuery('#budgetInXls').on('change.bs.fileinput', function(e) {
			/*validation for file upload*/
			myfile = jQuery(this).val();
			var ext = myfile.split('.').pop();
			if (jQuery.inArray(ext, fileformats) > -1) {
				//do something    
			} else {
				bootbox.alert(ext + " file format is not allowed");
				jQuery(this).val("");
				return;
			}
		});
	});
</script>
</head>
<body>
	<s:form action="budgetLoad" theme="css_xhtml" name="budgetLoad"
		id="budgetLoad" enctype="multipart/form-data" method="post">
		<div class="formmainbox">
			<div class="formheading"></div>
			<div class="subheadnew">
				<s:text name="budgetload" />
			</div>

			<div align="center">
				<font style='color: red;'>
					<div id="msg">
						<s:property value="message" />
					</div>
					<p class="error-block" id="lblError"></p>
				</font>
			</div>
			<span class="mandatory1">
				<div id="Errors">
					<s:actionerror />
					<s:fielderror />
				</div> <s:actionmessage />
			</span>
										
			<center>
				<table border="0" width="100%" cellspacing="0" cellpadding="0">
					<tr width="50%">
						<th width="50%"></th>
						<th width="25%" class="bluebgheadtd"
							style="width: 4%; text-align: center" align="center">Original
							Files</th>
						<th width="25%" class="bluebgheadtd"
							style="width: 4%; text-align: center" align="center">Output
							Files</th>
					</tr>
					<tr>
						<td width="50%">
							<table border="0" width="100%" cellspacing="0" cellpadding="0">
								<tr>
									<td class="greybox" width="20%"></td>
									<td class="greybox"></td>
									<td class="greybox"><s:text name="budgetupload" /> <span
										class="greybox"><span class="mandatory1">*</span></span></td>
									<td class="greybox"><s:file name="budgetInXls"
											id="budgetInXls" /></td>
									<span class="greybox" colspan="2">
										</td>
										<td class="greybox"></td>
								</tr>
								
							</table>
						</td>
						<td width="25%">
							<table border="1" width="100%" cellspacing="0" cellpadding="0">
								<s:iterator var="scheme" value="originalFiles" status="f">
									<tr>

										<td style="text-align: center" align="center"><a href="#"
											onclick="urlLoad('<s:property value="%{fileStoreId}" />');"
											id="originalFileId" /> <s:label value="%{fileName}" /> </a></td>
									</tr>
								</s:iterator>
							</table>
						</td>
						<td width="25%">
							<table border="1" width="100%" cellspacing="0" cellpadding="0">
								<s:iterator var="scheme" value="outPutFiles" status="f">
									<tr>

										<td style="text-align: center" align="center"><a href="#"
											onclick="urlLoad('<s:property value="%{fileStoreId}" />');"
											id="outputFileId" /> <s:label value="%{fileName}" /> </a></td>
									</tr>
									
								</s:iterator>
							</table>
						</td>
					</tr>
					<tr>

						<td class="greybox" colspan="2"><a
							href="/EGF/resources/app/formats/Budget_Data_Template.xls">Download
								Template</a></td>
					</tr>
				</table>
				<div  class="text-left error-msg" style="color: #C00000">&nbsp;* Only .xls files are supported.</span></div>
				<div class="buttonbottom" id="buttondiv">
					<table>
						<tr>

							<td><s:submit type="submit" cssClass="buttonsubmit"
									value="Upload Budget" name="upload" method="upload"
									onclick="return validate();" /></td>
							<td><input type="button" value="Close"
								onclick="javascript:window.close()" class="buttonsubmit" /></td>
						</tr>
					</table>
				</div>
			</center>
		</div>

	</s:form>
</body>
</html>
