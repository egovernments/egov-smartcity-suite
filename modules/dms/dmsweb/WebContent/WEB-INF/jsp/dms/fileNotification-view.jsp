<%@ taglib prefix="s" uri="/struts-tags"%>
<%@ include file="/includes/taglibs.jsp" %>
<html>
	<head>
		<title><s:text name="lbl.filenotifsys"/></title>
		<link href="../css/dms/fileManagement.css" rel="stylesheet" type="text/css" />
	</head>	
	<body style="background-color: white"> 
		<egovtags:breadcrumb/>
		<div class="mainhead">
			<s:text name="lbl.filenotifsys"/>
		</div>
		<s:form action="fileNotification" method="POST" theme="simple">
		<s:hidden name="id"/>
		<s:push value="model">
			<fieldset>
			<legend align="left"><b><s:text name="lbl.filedtl"/></b></legend>
			<table border="0" cellpadding="4" cellspacing="0" width="100%"> 
				<tbody>
					<tr class="graybox">
						<td>
							<s:text name="lbl.file"/> #
						</td>
						<td>
							<span>${fileNumber}</span>
						</td>
						<td>
							<s:text name="lbl.filedt"/>
						</td>
						<td>
							<span><s:date name="fileDate" format="dd/MM/yyyy" /></span>
						</td>
					</tr>
					<tr class="whitebox">
						<td>
							<s:text name="lbl.filecat"/>
						</td>
						<td>
							<span>${fileCategory.name}</span>
						</td>
						<td>
							<s:text name="lbl.filesubcatgry"/>
						</td>
						<td>
							<span>${fileSubcategory.name}</span>
						</td>
					</tr>
					<tr class="graybox">
						<td>
							<s:text name="lbl.fileheadng"/>
						</td>
						<td>
							<span>${fileHeading}</span>
						</td>
						<td >
							<s:text name="lbl.priority"/>
						</td>
						<td >
							<span>${filePriority.name}</span>
						</td>
					</tr>
				
					<tr class="whitebox">
						<td>
							<s:text name="lbl.smmryofdoc"/>
						</td>
						<td>
							<div class='viewdiv'>${fileSummary}</div>
						</td>
						<td>
							<s:text name="lbl.searchtag"/>
						</td>
						<td >
							<div class='viewdiv'>${fileSearchTag}</div>
						</td>				
					</tr>
				</tbody>
				</table>
				</fieldset>
				<br/>	
				<fieldset>
				<legend align="left"><b><s:text name="lbl.senderdtl"/></b></legend>	
				<table border="0" cellpadding="4" cellspacing="0" width="100%">
					<tbody>
						<tr class="graybox">
							<td>
								<s:text name="lbl.recvdfrm"/>
							</td>
							<td colspan="3">
								<span>${sender.userSource.name}</span>
							</td>
							
						</tr>
			
						<tr class="whitebox">
							<td>
								<s:text name="lbl.nameofsndr"/>
							</td>
							<td>
								<span>${sender.userName}</span>
							</td>								
							<td>
								<s:text name="lbl.addr"/>
							</td>
							<td>
								<div class='viewdiv'>${sender.userAddress}</div>
							</td> 
						</tr>
			
						<tr class="graybox">
							<td >
								<s:text name="lbl.phno"/>
							</td>
							<td>
								<span>${sender.userPhNumber}</span>
							</td>
							<td>
								<s:text name="lbl.email"/>
							</td>
							<td>
								<span>${sender.userEmailId}</span>
							</td>
						</tr>
					</tbody>
				</table>
				</fieldset>
				<br/>
				<fieldset>
				<legend align="left"><b><s:text name="lbl.docattchd"/></b></legend>	
				<table width="100%" border="0" cellspacing="0" cellpadding="2" id="fileListTbl"><tbody>
					<tr class="graybox" align="left">
						<th><s:text name="lbl.file"/></th>
						<th><s:text name="lbl.cmmt"/></th>
					</tr>
				   <c:forEach items="${fileNames}" var="item" varStatus = "status">
				   		<tr class="${status.count%2 == 0 ? 'graybox' : 'whitebox'}" >
				           <td style="padding:10px;"><span style="background-color:whitesmoke;border:1 solid gray;padding:3px;"><a href='/egi/docmgmt/ajaxFileDownload.action?docNumber=${fileNumber}&fileName=${item}&moduleName=DMS' target="_parent" >${item}&nbsp;&nbsp;<img src="../images/download.gif"></a></span></td>
				           <td style="padding:10px;"><div class='viewdiv'>${fileComment[status.count-1]}</div></td>
				       </tr>
				   </c:forEach>							        											
				</tbody>
				</table>	
				</fieldset>
				<br/>
				<br/>
				<fieldset>
				<legend align="left"><b><s:text name="lbl.notifsentto"/></b></legend>	
				<table width="100%" border="0" cellspacing="0" cellpadding="2" id="fileListTbl"><tbody>
					<tr class="graybox" align="left">
						<th><s:text name="lbl.grpname"/></th>
					</tr>
				   <c:forEach items="${notificationGroups}" var="item" varStatus = "status">
				   		<tr class="${status.count%2 == 0 ? 'graybox' : 'whitebox'}" >
				           <td style="padding:10px;">${item.groupName}</td>
				       </tr>
				   </c:forEach>							        											
				</tbody>
				</table>	
				</fieldset>
				<br/>
				<table border="0" width="100%">
					<tr align="center" class="graybox">
						<td>
							<s:hidden name="deleteAll" id="deleteAll"/>							
							<s:submit value="%{getText('lbl.del')}" id="deletebtn" method="deleteFileNotification" tabindex="1" onclick="return confirmDelete('%{deleteAll}')"/>&nbsp;&nbsp;&nbsp;
							<input type="button" value='<s:text name="lbl.close"/>' tabindex="1" onclick="window.close()">
						</td>
					</tr>
				</table>
			</s:push>
		</s:form>
		<s:if test="hasFieldErrors() || hasActionMessages() || hasActionErrors()">
			<div id="mask" class="loading" style="display:block"></div>
			<div id="errorconsole" >
			<s:if test="hasActionMessages() && !hasFieldErrors() && !hasActionErrors()">
				<table border="0">
					<tr>
						<td>
							<img src="../images/success.gif" alt="Sucessful" border="0" />
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
			<button onclick="hideElement('mask'),hideElement('errorconsole')" style="float:right;margin-bottom: 10px;margin-right: 40px;">&nbsp;&nbsp;&nbsp;OK&nbsp;&nbsp;&nbsp;</button>	
			</div>
		</s:if>
		
		<table width="100%">
			<tr>
				<td class="urlcontainer">
					eGovernments Foundation &copy; All rights reserved
				</td>
			</tr>
		</table>
		<div id="loading" class="loading"><span><s:text name="info.plswait"/></span></div>		
	</body>
</html>
<script type="text/javascript" src="../javascript/dms/fileNotification.js"></script>