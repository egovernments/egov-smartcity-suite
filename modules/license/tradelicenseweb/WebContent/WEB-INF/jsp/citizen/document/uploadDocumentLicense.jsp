<%@ taglib prefix="s" uri="/struts-tags"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@page import="org.egov.infstr.client.filter.EGOVThreadLocals" %>
<html>
	<head>
		<title>Document Management</title>
		<link href="/egi/css/docmgmt/documentManager.css" rel="stylesheet" type="text/css"></link>		
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
				            	<a href='${pageContext.request.contextPath}/citizen/document/ajaxDocumentDownload.action?docNumber=${model.documentNumber}&fileName=${item}&moduleName=${model.moduleName}' target="_parent" >
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
<script type="text/javascript" src="/egi/javascript/docmgmt/documentManager.js"></script>
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
			alert("Failed to update Document Number...!");
		}
	}
	window.close();
</script>
</s:if>		
</body>
</html>
