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
<%@ taglib prefix="s" uri="/struts-tags"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@page import="org.egov.infra.utils.EgovThreadLocals" %>
<html>
	<head>
		<title>Document Management</title>
		<link href="../../css/docmgmt/documentManager.css" rel="stylesheet" type="text/css"></link>		
	</head> 
	<body>
		<div class="container">		
		<div class="mainhead">Document Manager</div>
			<s:form action="uploadDocumentLicense" method="POST" enctype="multipart/form-data" theme="simple" onsubmit="showWaiting()">
				<table>
				<tr>
				<td valign="top">
					<fieldset id="filechooser" class="main1" style="height:auto;">
				 		<legend class="main1">Attach File</legend>
					 	<table id="uploadertbl" ><tbody>
					 		<tr>
					 			<td>
				 					File
				 				</td>
				 				<td>
				 				</td>
					 		</tr>
					 		<tr>			 				
				 				<td id="filetd">
				 					<input type="file" name="upload" id="file2" size="40" onchange="isValidFile(this,true)"/>			 					
								</td>
								<td>
									<button type="button" onclick="addBasicFileAttach()"><img src="../../images/addrow.gif"  alt="Attach" title="Attach More" width="14" height="14"  border="0" /></button>
								</td>						
							</tr>									 										
					 	</tbody></table>
					 	<br/><font size="1">[.exe file can not be uploaded]</font>
					</fieldset>					
				</td>
				<td valign="top">
					<fieldset class="main1"> 
					<legend class="main1" >Files Attached</legend>
					<div class="filecontainer">
					<table width="100%" border="0" cellspacing="0" cellpadding="2" id="fileListTbl"><tbody>
					   <c:forEach items="${fileNames}" var="item" varStatus = "status">
					   <tr id="fileItem${status.count}">
				            <td width="88%">
				            	${item}<input type="hidden" name="fileNames" value='${item}' id="fileName${status.count}" disabled="disabled" >
				            </td>
				            <td width="6%">
				            	<a href='${pageContext.request.contextPath}/citizen/uploaddocument/ajaxDocumentDownload.action?docNumber=${model.documentNumber}&fileName=${item}&moduleName=${model.moduleName}' target="_parent" >
				            	<img src="../../images/download.gif" alt="Download" title="Download File" width="16" height="16" border="0" id="downloadbtn${status.count}" name="downloadbtn" /></a>
				            </td>
				            <td width="6%">
				            	<a href='javascript:void(0);'  ><img src="../../images/recycle-empty.png" alt="Detach" title="Click to mark for detach" width="16" height="16" border="0" id="detachbtn${status.count}" name="detachbtn" onclick="detachFile(this,'${status.count}')"/></a>
				            </td>							            
				        </tr>
				        </c:forEach>							        											
					</tbody></table>								
					</div>								
					</fieldset>
				</td>
				</tr>
				</table>
				<table>
					<tbody>
						<tr>
							<td >
								<input type="hidden" name="model.moduleName" value="${model.moduleName}"/>
								<input type="hidden" name="model.documentNumber" id="docNumber" value="${model.documentNumber}" readonly="readonly"/>
								Search Tag
							</td>
								
							<td>
								<input type="text" name="model.tags" id="searchTag"  value="${model.tags}" size="55"/>
							</td>														
						</tr> 						
					</tbody> 
			 	</table>
			 	<table>
			 		<tbody>
			 			<tr>
			 				<td>&nbsp;</td>
			 			</tr>
						<tr>
							<td  valign="bottom">
								<s:submit action="uploadDocumentLicense%{actionCommand}"  value="Save Document" id="savebtn" cssClass="save"/>&nbsp;&nbsp;<input  type="button" class="close" value="     Close    " onclick="closeAndClearResource()"/> 
							</td>						
						</tr>
					</tbody>
				</table>				
		</s:form>
		<s:if test="hasFieldErrors() || hasActionMessages()">
			<hr class="hr" size="1">
		</s:if>
		<s:if test="hasActionMessages() && !hasFieldErrors()">
		<table border="0">
			<tr>
				<td>
					<img src="../../images/success.gif" alt="Detach" title="Document Save Successful" border="0" />
				</td>
				<td valign="bottom" style="padding-top:14px;">
					<font color="navy" size="2" ><s:actionmessage/></font>
				</td>
			</tr>
		</table>		
		</s:if>
		<s:if test="hasFieldErrors() || hasActionErrors()">
		<table border="0">
			<tr>
				<td>
					<img src="../../images/error.gif" alt="Detach" title="Error occurred" border="0" />
				</td>
				<td valign="bottom" style="padding-top:14px;">
					<font color="red" size="2"><s:actionerror/><s:fielderror /></font>
				</td>
			</tr>
		</table>		
		</s:if>		
		</div>
		<div class="urlcontainer"> eGovernments Foundations &copy; All rights reserved</div>
		<div id="loading" class="loading"><span>Please wait...</span></div>
<script type="text/javascript" src="../../javascript/docmgmt/documentManager.js"></script>
<script>
	var actionCmd = "${actionCommand}";	
	var docnum = "${model.documentNumber}";	
</script>
<s:if test="!hasFieldErrors() && hasActionMessages()">
<script>
	//updating child window docNumber property with generated Document Number
	if (opener) {
		try {
			if (typeof(opener.docNumberUpdater) === 'function' || typeof(opener.docNumberUpdater) === 'object' ) {
				opener.docNumberUpdater(document.getElementById("docNumber").value);
			} else if (opener.document.getElementById("docNumber") != null ){
				opener.document.getElementById("docNumber").value = document.getElementById("docNumber").value;		
			}
		} catch (e) {
			//This error can occur due to either docNumber field or docNumberUpdater function 
			//is missing in parent window OR Parent window got closed before child window had closed. 
			bootbox.alert("Failed to update Document Number...!");
		}
	}
	window.close();
</script>
</s:if>		
</body>
</html>
