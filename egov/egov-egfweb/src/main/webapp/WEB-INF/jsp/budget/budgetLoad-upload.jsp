<!--  #-------------------------------------------------------------------------------
# eGov suite of products aim to improve the internal efficiency,transparency, 
#      accountability and the service delivery of the government  organizations.
#   
#       Copyright (C) <2015>  eGovernments Foundation
#   
#       The updated version of eGov suite of products as by eGovernments Foundation 
#       is available at http://www.egovernments.org
#   
#       This program is free software: you can redistribute it and/or modify
#       it under the terms of the GNU General Public License as published by
#       the Free Software Foundation, either version 3 of the License, or
#       any later version.
#   
#       This program is distributed in the hope that it will be useful,
#       but WITHOUT ANY WARRANTY; without even the implied warranty of
#       MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
#       GNU General Public License for more details.
#   
#       You should have received a copy of the GNU General Public License
#       along with this program. If not, see http://www.gnu.org/licenses/ or 
#       http://www.gnu.org/licenses/gpl.html .
#   
#       In addition to the terms of the GPL license to be adhered to in using this
#       program, the following additional terms are to be complied with:
#   
#   	1) All versions of this program, verbatim or modified must carry this 
#   	   Legal Notice.
#   
#   	2) Any misrepresentation of the origin of the material is prohibited. It 
#   	   is required that all modified versions of this material be marked in 
#   	   reasonable ways as different from the original version.
#   
#   	3) This license does not grant any rights to any user of the program 
#   	   with regards to rights under trademark law for use of the trade names 
#   	   or trademarks of eGovernments Foundation.
#   
#     In case of any queries, you can reach eGovernments Foundation at contact@egovernments.org.
#-------------------------------------------------------------------------------  -->
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
	function urlLoad(fileStoreId, type) {
		if (type == 'original') {
			var url = "/EGF/budget/budgetLoad-export.action?originalFileStoreId="
					+ fileStoreId;
			window.open(url, '','height=650,width=980,scrollbars=yes,left=0,top=0,status=yes');
		} else {
			var url = "/EGF/budget/budgetLoad-export.action?outPutFileStoreId="
					+ fileStoreId;
			window.open(url, '','height=650,width=980,scrollbars=yes,left=0,top=0,status=yes');
		}

	}
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

		<s:if test="%{mode.equalsIgnoreCase('result')}">
			<table align="center" class="tablebottom" width="100%">
				<tr>
					<th class="bluebgheadtd" style="width: 2%; text-align: center"
						align="center"><a href="#"
						onclick="urlLoad('<s:property value="%{originalFileStoreId}" />','original');"
						id="sourceLink" /> Download Original File </a></th>

					<th class="bluebgheadtd" style="width: 2%; text-align: center"
						align="center"><a href="#"
						onclick="urlLoad('<s:property value="%{outPutFileStoreId}" />','output');"
						id="sourceLink" /> Download OutPut File </a></th>
				</tr>
			</table>
		</s:if>
	</s:form>
</body>
</html>
