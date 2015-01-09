<%@ taglib prefix="s" uri="/struts-tags"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@page import="org.egov.infstr.client.filter.EGOVThreadLocals" %>
<html>
	<head>
		<title>Document Management</title>
		<link href="../css/docmgmt/documentManager.css" rel="stylesheet" type="text/css"></link>		
	</head> 
	<%
		String domainName = EGOVThreadLocals.getDomainName();
		String userId = EGOVThreadLocals.getUserId();
	%>
	<body>
		<div class="container">		
		<div class="mainhead">Document Manager</div>
			<s:form action="documentManager" method="POST" enctype="multipart/form-data" theme="simple" onsubmit="showWaiting()">
				<table >
					<tbody>
						<tr>
							<td >
							<input type="hidden" name="model.moduleName" value="${model.moduleName}"/>
							<fieldset class="main"> 
								<legend class="main1">Document Info</legend>
								<table width="285" cellpadding="2" cellspacing="3">
									<tr>
										<td>
					 					Document Number
						 				</td>
						 				<td >
											<input type="text" name="model.documentNumber" id="docNumber" value="${model.documentNumber}" readonly="readonly"/>
										</td>
									</tr>
									<tr>
						 				<td>
						 					Search Tag
						 				</td>
						 				<td>
											<input type="text" name="model.tags" id="searchTag"  value="${model.tags}"/>
										</td>
									</tr>
								</table>
							</fieldset>
							</td>
							<td>
								<fieldset class="main2"> 
								<legend class="main1">Files Attached</legend>
								<div class="filecontainer">
								<table width="100%" border="0" cellspacing="0" cellpadding="2" id="fileListTbl"><tbody>
								   <c:forEach items="${fileNames}" var="item" varStatus = "status">
								   <tr id="fileItem${status.count}">
							            <td width="82%">${item}<input type="hidden" name="fileNames" value='${item}' id="fileName${status.count}" disabled="disabled" ></td>
							            <td width="6%"><a href='javascript:void(0);'><img src="../images/information.png" alt="Info" title="File Info" width="16" height="16" border="0" onclick="showInfo(${status.count},true)"/></a></td>
							            <td width="6%">
							            <a href='${pageContext.request.contextPath}/docmgmt/ajaxFileDownload.action?docNumber=${model.documentNumber}&fileName=${item}&moduleName=${model.moduleName}' target="_parent" >
							            <img src="../images/download.gif" alt="Download" title="Download File" width="16" height="16" border="0" id="downloadbtn${status.count}" name="downloadbtn" /></a></td>
							            <td width="6%"><a href='javascript:void(0);'  ><img src="../images/recycle-empty.png" alt="Detach" title="Click to mark for detach" width="16" height="16" border="0" id="detachbtn${status.count}" name="detachbtn" onclick="detachFile(this,'${status.count}')"/></a></td>							            
							        </tr>
							        </c:forEach>							        											
								</tbody></table>								
								</div>								
								</fieldset>
							</td>
						</tr> 						
					</tbody> 
			 	</table>
			 	<c:forEach items="${fileInfo}" var="item" varStatus = "status">
					<div id="info${status.count}" class="info cursor">
						<img src="../images/cancel.jpg" onclick="showInfo(${status.count},false)" class="infocls"/><br/>									
						<div class="infodiv">${item}</div>
					</div>
				</c:forEach>
			 	<fieldset id="filechooser" class="main3">
			 		<legend class="main1">Attach File</legend>
				 	<table id="uploadertbl"><tbody>
				 		<tr>
				 			<td>
			 					File
			 				</td>
			 				<td style="padding-left:25px">
			 					File Name
			 				</td>
				 		</tr>
				 		<tr>			 				
			 				<td >
			 					<input type="file" name="upload" id="file2" onchange="setFileCaption(this,'fileCaption2')" size="40" />
							</td>
							<td class="captd">
								<input type="text" name="fileCaption"  id="fileCaption2" size="30" />
								<div id="remarkdiv2" class="remarkdiv">
									<fieldset>
									<legend>File Remarks</legend>
									<table>
										<tr>
											<td>
												<textarea rows="10" cols="50" id="remark2" name="fileInfo" class="remarkarea" onclick="clearText(this);" onkeypress="clearText(this);">enter file remarks</textarea>
												<br/>
											</td>
										</tr>
										<tr>
											<td align="center">
												<input type="button" value="    OK    " class="cursor bold"  name="okbtn" onclick="showHideRemark(2)"/>
											</td>
										</tr>
									</table>
									</fieldset>																		
								</div><img src="../images/draft.png" id="remarkbtn2" alt="Remarks" title="Add Remarks" border="0" onclick="showHideRemark(2)" class="cursor remarksbtn"/>&nbsp;&nbsp;&nbsp;
								<button type="button" onclick="addFileAttachHolder()" title="Attach More" ><img src="../images/addrow.gif" alt="Attach" title="Attach More" width="14" height="14"  border="0" style="margin-top:2px"/></button>
							</td> 							
						</tr>									 										
				 	</tbody></table>
				</fieldset>
				<table>
			 		<tbody>
			 			<tr>
			 				<td>&nbsp;</td>
			 			</tr>
						<tr>
							<td  valign="bottom">
								<s:submit action="documentManager%{actionCommand}"  value="Save Document" id="savebtn" cssClass="save"/>&nbsp;&nbsp;<input  type="button" class="close" value="     Close    " onclick="closeAndClearResource()"/> 
							</td>						
						</tr>
					</tbody>
				</table>				
		</s:form>
		<s:if test="hasFieldErrors() || hasActionMessages() || hasActionErrors()">
			<hr class="hr" size="1">
		</s:if>
		<s:if test="hasActionMessages() && !hasFieldErrors() && !hasActionErrors()">
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
		<table border="0">
			<tr>
				<td>
					<img src="../images/success.gif" alt="Sucessful" title="Document Save Successful" border="0" />
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
					<img src="../images/error.gif" alt="Failed" title="Error occurred" border="0" />
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
	</body>
</html>
<script>
	var actionCmd = "${actionCommand}";	
	var docnum = "${model.documentNumber}";	
</script>
<script type="text/javascript" src="../javascript/docmgmt/documentManager.js"></script>	
