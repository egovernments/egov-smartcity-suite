<%@ taglib prefix="s" uri="/struts-tags"%>
<%@ include file="/includes/taglibs.jsp" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<html>
	<head>
		<title><s:text name="lbl.filenotifsys"/></title>
		 <link href="../commonyui/yui2.7/fonts/fonts-min.css" type="text/css" rel="stylesheet"></link>
		 <link rel="stylesheet" type="text/css" href="../commonyui/yui2.7/container/assets/skins/sam/container.css">
		 <link href="../commonyui/yui2.7/calendar/assets/skins/sam/calendar.css" rel="stylesheet" type="text/css"></link>
		 <link href="../css/dms/fileManagement.css" rel="stylesheet" type="text/css" />	
	</head>	
	<body style="background-color: white" class="yui-skin-sam">
		<egovtags:breadcrumb/>
		<div class="mainhead">
			<s:text name="lbl.filenotifsys"/>
		</div>
		<s:form action="fileNotification" method="POST" enctype="multipart/form-data" theme="simple">
		<s:token name="%{tokenName()}"/>
		<s:push value="model">
			<fieldset>
			<legend align="left"><b><s:text name="lbl.filedtl"/></b></legend>
			<div id="calender" class="cal"></div>
			<table border="0" cellpadding="4" cellspacing="0" width="100%">
				<tbody>
					<tr class="graybox">
						<td>
							<s:text name="lbl.file"/> #<span class="mandatory">*</span>
						</td>
						<td>
							<s:if test="%{fileNumber == null || fileNumber==''}">
								<s:textfield name="fileNumber" id="fileNumber" tabindex="1"/>
							</s:if>
							<s:else>
								<s:textfield name="fileNumber" id="fileNumber" tabindex="1" readonly="true"/>
							</s:else>
							<s:hidden name="id" id="id" />
						</td>
						<td>
							<s:text name="lbl.filedt"/><span class="mandatory">*</span>
						</td>
						<td>
							<s:date name="fileDate" id="fileDateFmrtd" format="dd/MM/yyyy"/>
							<s:textfield value="%{fileDateFmrtd}" name="fileDate" id="fileDate" tabindex="1" size="20" onkeyup="DateFormat(this,this.value,event,false,'3')" onblur="validateDateFormat(this);" />
							<img src="../images/calendaricon.gif" onclick="showCalendar(this,event)" alt="Date" width="18" id="fileDateBtn" height="18" align="absmiddle" tabindex="1" onkeypress="showCalendar(this,event)"/>
						</td>
					</tr>
					<tr class="whitebox">
						<td>
							<s:text name="lbl.filecat"/><span class="mandatory">*</span>
						</td>
						<td>
							<s:select headerValue="%{getText('sel.catgry')}" headerKey="" name="fileCategory" id="fileCategory"  list="dropdownData.fileCategoryList" listValue="name" listKey="id"  theme="simple" onchange="loadFileSubcategories(this)" tabindex="1" value="fileCategory.id"/>
							<egovtags:ajaxdropdown id="fileSubcategoryDropdown"  fields="['Text','Value']" dropdownId='fileSubcategory' url='dms/fileManagement!getFileSubcategories.action' selectedValue="%{fileSubcategory.id}"/>
		
						</td>
						<td>
							<s:text name="lbl.filesubcatgry"/>
						</td>
						<td>
							<s:select name="fileSubcategory" id="fileSubcategory" list="fileSubCategoriesList" listKey="id" listValue='name' headerKey="-1" headerValue="%{getText('sel.subcatgry')}" value="fileSubcategory.id" />
		
						</td>
					</tr>
					<tr class="graybox">
						<td>
							<s:text name="lbl.fileheadng"/><span class="mandatory">*</span>
						</td>
						<td>
							<s:textfield name="fileHeading" type="text" tabindex="1" id="fileHeading" />
						</td>
						<td >
							<s:text name="lbl.priority"/>
						</td>
						<td >
							<s:select headerValue="%{getText('sel.prity')}" headerKey="" name="filePriority" id="priority"  list="dropdownData.filePriorityList" listValue="name" listKey="id"  theme="simple" tabindex="1" value="filePriority.id"/>
						</td>
					</tr>
				
					<tr class="whitebox">
						<td>
							<s:text name="lbl.smmryofdoc"/>
						</td>
						<td>
							<s:textarea name="fileSummary" cols="20" rows="2" tabindex="1"></s:textarea>
						</td>
						<td>
							<s:text name="lbl.searchtag"/>
						</td>
						<td >
							<s:textarea name="fileSearchTag" cols="20" rows="2" tabindex="1"></s:textarea>
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
								<s:text name="lbl.recvdfrm"/><span class="mandatory">*</span>
							</td>
							<td colspan="3">
								<s:select headerValue="%{getText('sel.src')}" headerKey="" name="sender.userSource" id="userSource"  list="dropdownData.fileSourceList" listValue="name" listKey="id"  theme="simple"  tabindex="1" value="sender.userSource.id"/>
							</td>
							
						</tr>
			
						<tr class="whitebox">
							<td>
								<s:text name="lbl.nameofsndr"/><span class="mandatory">*</span>
							</td>
							<td>
								<s:textfield name="sender.userName" tabindex="1" id="senderName"/>
							</td>								
							<td>
								<s:text name="lbl.addr"/>
							</td>
							<td>
								<s:textarea name="sender.userAddress" cols="20" rows="2"  tabindex="1"></s:textarea>
								</td> 
							</tr>
			
						<tr class="graybox">
							<td >
								<s:text name="lbl.phno"/>
							</td>
							<td>
								<s:textfield name="sender.userPhNumber" tabindex="1" />
							</td>
							<td>
								<s:text name="lbl.email"/>
							</td>
							<td>
								<s:textfield name="sender.userEmailId" tabindex="1" />
							</td>
						</tr>
					</tbody>
				</table>
				</fieldset>
				<br/>
				<fieldset>
					<legend align="left"><b><s:text name="lbl.attcdoc"/></b></legend>	
					<table border="0" cellpadding="4" cellspacing="0" width="100%" id="uploadtbl"><tbody>
						<tr class="graybox">
							<th>
								<s:text name="lbl.doc"/>
							</th>
							<th>
								<s:text name="lbl.cmmts"/>
							</th>
							<th>
								&nbsp;
							</th>
						</tr>
						<tr class="whitebox" align="center">
							<td>
								<input type="file" name="file" tabindex="1"/>
							</td>					
							<td >
								<textarea name="comment" cols="20" rows="2" tabindex="1"></textarea>
							</td>
							<td>
								<img src="../images/add.png"  tabindex="1" onclick="addFileAttachment(this)" title="Add More Files" >
							</td>
						</tr>
					</tbody></table>
				</fieldset>
				<br/>
				<fieldset>
				<legend align="left"><b><s:text name="lbl.notifrcvrinfo"/></b></legend>	
				<table border="0" cellpadding="4" cellspacing="0" width="100%">
					<tbody>
						<tr class="graybox">
							<td>
								<s:text name="lbl.recvrgrp"/><span class="mandatory">*</span>
							</td>
							<td>
								<s:select name="notificationGroupsIds" id="notificationGroupsIds"  list="dropdownData.notificationGroupsList" listValue="groupName" listKey="id"  theme="simple"  tabindex="1" value="id" multiple="true" size="5" />
							</td>							
						</tr>						
					</tbody>
				</table>
				</fieldset>
				<br/>
				<table border="0" width="100%">
					<tr align="center" class="graybox">
						<td>
							<s:submit method="createFileNotification" value="%{getText('lbl.broadcast')}" id="savebtn" tabindex="1" onclick="showWaiting();return checkBeforeSubmit()"/>&nbsp;&nbsp;&nbsp;
							<input type="reset" value="<s:text name='lbl.reset'/>" tabindex="1">&nbsp;&nbsp;&nbsp;
							<input type="button" value="<s:text name='lbl.close'/>" tabindex="1" onclick="window.close()">
						</td>
					</tr>
				</table>
			</s:push>
		</s:form>
<jsp:include page="fileManagementFooter.jsp"/>
<script src="../javascript/dms/fileNotification.js" type="text/javascript"></script>