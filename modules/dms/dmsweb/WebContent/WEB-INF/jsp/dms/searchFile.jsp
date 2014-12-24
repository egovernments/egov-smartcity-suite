<%@ taglib prefix="s" uri="/struts-tags"%>
<%@ include file="/includes/taglibs.jsp"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<html>
	<head>
		<title><s:text name="titl.filesearch"/></title>
		<link href="../commonyui/yui2.7/fonts/fonts-min.css" type="text/css" rel="stylesheet"></link>
		<link href="../commonyui/yui2.7/calendar/assets/skins/sam/calendar.css"	rel="stylesheet" type="text/css"></link>
		<link href="../css/dms/fileManagement.css" rel="stylesheet" type="text/css" />
		<style type="text/css">
		 a {color:blue;	text-decoration: underline;}
		 tr.odd {background-color: #fff}
		 tr.even {background-color: #eeeeee}
		 thead tr , th{border-top:0px solid #000000;border-bottom:0px solid #000000;padding: 2px 4px 2px 4px;text-align: left;vertical-align: top;color: black;}
		 tbody td {padding: 2px 4px 2px 4px;}
		 div.exportlinks {
			background-color: #eee;
			border: 1px dotted #999;
			padding: 2px 4px 2px 4px;
			margin: 10px 0 10px 0;
			width: 100%;
		}
		</style>
	
<script type="text/javascript">
	function returnfileNumber(fileId,fileNo)
	{		
		window.opener.document.getElementById("fileNo").value=fileNo;			
		window.opener.document.getElementById("fileId").value=fileId;
		if(window.opener.fileUpdater != undefined)
		{			
			window.opener.fileUpdater(fileId); 
		}
		window.close();
	}
</script>

	</head>
	<body style="background-color: white" class="yui-skin-sam">
		<egovtags:breadcrumb />
		<div class="mainhead">
			<s:text name="lbl.searchfile"/>
		</div>		
		
		<s:form action="searchFile" theme="simple" id="fileForm">	
		<s:hidden name="module" />					
		<s:hidden name="mode" />	
			<div class="formmainbox">
				<div id="calender" class="cal"></div>
				
				<table border="0" cellpadding="6" cellspacing="0" width="100%">

					<tbody>

						<tr class="graybox">
							<td>
								<s:text name="lbl.file"/> #
							</td>
							<td>
								<input type="text" name="fileNumber" id="fileNumber" value="${fileNumber}"/>
							</td>
							<td>
								<s:text name="lbl.filedtfrm"/>
							</td>

							<td>
								<input value="<s:date name='fileDateFrom' format='dd/MM/yyyy'/>" name="fileDateFrom" size="10" onkeyup="DateFormat(this,this.value,event,false,'3')" onblur="validateDateFormat(this);" id="fileDateFrom" type="text">
								<img src="../images/calendaricon.gif" onclick="showCalendar(this,event)" alt="Date" width="18" id="fileDateFromBtn" align="absmiddle" />
							</td>
							<td>
								<s:text name="lbl.filedtto"/>
							</td>
							<td>
								<label>
									<input  value="<s:date name='fileDateTo' format='dd/MM/yyyy'/>" name="fileDateTo" size="10" onkeyup="DateFormat(this,this.value,event,false,'3')" onblur="validateDateFormat(this);" id="fileDateTo" type="text">
									<img src="../images/calendaricon.gif" onclick="showCalendar(this,event)" alt="Date" width="18" id="fileDateToBtn" align="absmiddle" />
								</label>
							</td>
						</tr>

						<tr class="whitebox">
							<td>
								<s:text name="lbl.dept"/>
							</td>

							<td>
								<s:select headerValue="%{getText('lbl.dept')}" headerKey="" name="departmentId" id="departmentId" list="dropdownData.departmentList" listValue="deptName" listKey="id" theme="simple" tabindex="1" />
							</td>
							<td>
								<s:text name="lbl.zone"/>
							</td>
							<td>

								<s:select headerValue="%{getText('lbl.zone')}" headerKey="" name="firstLevelBndry" id="zone" list="firstLevelBoundries" listValue="name" listKey="id" theme="simple" onchange="populateSecondLevelBoundaries(this)" tabindex="1"  />

							</td>
							<td>
								<s:text name="lbl.ward"/>
							</td>
							<td>

								<select name="secondLevelBndry" id="ward" tabindex="1">
									<option value="">
										<s:text name="sel.ward"/>
									</option>
								</select>

							</td>
						</tr>

						<tr class="graybox">
							<td>
								<s:text name="lbl.filercvddtfrm"/>
							</td>
							<td>
								<label>
									<input value="<s:date name='fileReceivedDateFrom' format='dd/MM/yyyy'/>" name="fileReceivedDateFrom" size="10" onkeyup="DateFormat(this,this.value,event,false,'3')" onblur="validateDateFormat(this);" id="fileReceivedDateFrom" type="text">
									<img src="../images/calendaricon.gif" onclick="showCalendar(this,event)" alt="Date" id="fileReceivedDateFromBtn" align="absmiddle" />
								</label>
							</td>
							<td>
								<s:text name="lbl.filercvddtto"/>
							</td>
							<td>
								<label>
									<input value="<s:date name='fileReceivedDateTo' format='dd/MM/yyyy'/>" name="fileReceivedDateTo" size="10" onkeyup="DateFormat(this,this.value,event,false,'3')" onblur="validateDateFormat(this);" id="fileReceivedDateTo" type="text">
									<img src="../images/calendaricon.gif" onclick="showCalendar(this,event)" alt="Date" id="fileReceivedDateToBtn" align="absmiddle" />
								</label>
							</td>
							<td>
								<s:text name="lbl.filestatus"/>
							</td>
							<td>
								<s:select headerValue="%{getText('sel.filestatus')}" headerKey="" name="fileStatusId" id="fileStatusId" list="dropdownData.fileStatusList" listValue="code" listKey="id" theme="simple" tabindex="1" />
							</td>
						</tr>

						<tr class="whitebox">
							<td>
								<s:text name="lbl.recvdfrmsntto"/>
							</td>
							<td>
								<s:select headerValue="%{getText('sel.filesrc')}" headerKey="" name="fileSourceId" id="fileSourceId" list="dropdownData.fileSourceList" listValue="name" listKey="id" theme="simple" />
							</td>
							<td>
								<s:text name="lbl.filetype"/>
								
							</td>

							<td>
								<s:select headerValue="%{getText('sel.filetype')}" headerKey="" name="fileType" id="fileTypeId" list="dropdownData.fileTypeList" listValue="value" listKey="value" theme="simple" />
							</td>
							<td>
								<s:text name="lbl.filecat"/>
								
							</td>
							<td>
								<s:select headerValue="%{getText('sel.catgry')}" headerKey="" name="fileCategoryId" id="fileCategoryId" list="dropdownData.fileCatList" listValue="name" listKey="id" theme="simple" />
							</td>
						</tr>

						<tr class="graybox">
							<td>
								<s:text name="lbl.filesubcatgry"/>
							</td>

							<td>
								<s:select headerValue="%{getText('sel.subcatgry')}" headerKey="" name="fileSubCategoryId" id="fileSubCategoryId" list="dropdownData.fileSubCatList" listValue="name" listKey="id" theme="simple" />
							</td>
							<td>

								<s:text name="lbl.priority"/>
							</td>
							<td>
								<s:select headerValue="%{getText('sel.fileprio')}" headerKey="" name="filePriorityId" id="filePriorityId" list="dropdownData.filePriorityList" listValue="name" listKey="id" theme="simple" />
							</td>
							<td>

								<s:text name="lbl.outboundfileno"/>
							</td>
							<td>
								<input type="text" name="outboundFileNumber" id="outboundFileNumber" value="${outboundFileNumber}"/>
							</td>
						</tr>


						<tr class="graybox">
							<td colspan="6">
								&nbsp;
							</td>
						</tr>

						<tr class="whitebox">
							<td colspan="6">
								&nbsp;
							</td>
						</tr>

						<tr class="whitebox">

							<td colspan="8" align="center">
								<s:submit method="search" name="Search" value="%{getText('lbl.search')}"></s:submit>
								<input name="close" tabindex="1" value="<s:text name='lbl.close'/>" onclick="window.close()" type="button">
								<input value="<s:text name='lbl.reset'/>" name="reset" onclick="resetFields();" type="button">
							</td>
						</tr>
					</tbody>
				</table>

			
<s:if test="!genericfileList.isEmpty()">
			

			
<s:if test="%{mode == 'report'}">
	<s:set name="export" value="true"/>
</s:if>
<s:else>
	<s:set name="export" value="false"/>
</s:else>



<display:table name="genericfileList" uid="currentRowObject" style="width:100%;" pagesize = "10" export="${export}" requestURI=""  >
	<display:caption style="text-align: center; margin: 10px 0 10px 0;" class="headerbold">Files By Type & Category </display:caption>
<display:column title="Si.no" >	 <c:out value="${currentRowObject_rowNum}"/></display:column>
				
							
<display:column title="File Date" >
<fmt:formatDate value="${currentRowObject.fileDate}" pattern="dd/MM/yyyy"/>
</display:column>

<display:column title="File #" >
<s:if test="%{module=='external'}">
<a style="color:blue;text-decoration:underline;" href="#" onclick="returnfileNumber('${currentRowObject.id}','${currentRowObject.fileNumber}')">
<c:out value="${currentRowObject.fileNumber}"/></a>
</s:if>
<s:else>
<a style="color:blue;text-decoration:underline;"  href="javascript:popnewwindow('/dms/dms/genericFile.action?fileId=${currentRowObject.id}')"><c:out value="${currentRowObject.fileNumber}"/>
</a>
</s:else>


</display:column>
<display:column title="Type" property="fileType" />
<display:column title="Department" property="department.deptName"/>
<display:column title="Zone" property="firstLevelBndry.name" />
<display:column title="Ward" property="secondLevelBndry.name"/>
<display:column title="Assigned to">
<c:if test="${currentRowObject.fileType == 'INBOUND' || currentRowObject.fileType == 'INTERNAL'}">
<c:out value="${currentRowObject.receiver.designation.designationName}"/>
&nbsp;
</c:if>
<c:if test="${currentRowObject.fileType == 'OUTBOUND'}">
	<c:out value="${currentRowObject.receiver.userName}" />&nbsp;
</c:if>
</display:column>
<display:column title="Received From/Sent to">
<c:if test="${currentRowObject.fileType == 'OUTBOUND'}">
<c:out value="${currentRowObject.receiver.userSource.name}"/>&nbsp;
</c:if>
<c:if test="${currentRowObject.fileType == 'INBOUND'}">
<c:out value="${currentRowObject.sender.userSource.name}"/>&nbsp;
</c:if>
<c:if test="${currentRowObject.fileType == 'INTERNAL'}">
<c:out value="${currentRowObject.sender.department.deptName}"/>&nbsp;
</c:if>
</display:column>
<display:column title="Priority" property="filePriority.name"/>

<display:column title="File Category" property="fileCategory.name"/>
<display:column title="File Sub Category" property="fileSubcategory.name"/>
<display:column title="File Heading" property="fileHeading"/>
<display:column title="File Current Status" property="fileStatus.code"/>
							<div STYLE="display:table-header-group;">									      
							<display:setProperty name="basic.show.header" value="true" />
							<display:setProperty name="basic.empty.showtable" value="true" />
							<display:setProperty name="export.excel.class" value="org.egov.infstr.displaytag.export.EGovExcelView"/>	
                            <display:setProperty name="export.pdf.filename" value="filesByTypeandCategory.pdf" />
							<display:setProperty name="export.excel.filename" value="filesByTypeandCategory.xls"/>
							<display:setProperty name="export.csv" value="false"/>
							<display:setProperty name="export.xml" value="false"/>
						</div>
							

						
					</display:table >

		
		</s:if>
		<s:elseif test='{fileType != null || !fileType.equals("")}'>
			<center style="color:red"><s:text name="err.noresult"/></center>
		</s:elseif>
		</div>
		
		</s:form>
		
	</body>
</html>
<script type="text/javascript" src="../commonyui/yui2.7/yahoo-dom-event/yahoo-dom-event.js"></script>
<script type="text/javascript" src="../commonyui/yui2.7/yahoo/yahoo-min.js"></script>
<script type="text/javascript" src="../commonyui/yui2.7/get/get-min.js"></script>
<script type="text/javascript" src="../commonyui/yui2.7/json/json-min.js"></script>
<script type="text/javascript" src="../commonyui/yui2.7/event/event-min.js"></script>
<script type="text/javascript" src="../commonyui/yui2.7/element/element-min.js"></script>
<script type="text/javascript" src="../commonyui/yui2.7/container/container_core-min.js"></script>
<script type="text/javascript" src="../commonyui/yui2.7/container/container-min.js"></script>
<script type="text/javascript" src="../commonyui/yui2.7/calendar/calendar.js"></script>
<script type="text/javascript" src="../commonyui/yui2.7/connection/connection-min.js"></script>
<script type="text/javascript" src="../javascript/dateValidation.js"></script>
<script type="text/javascript" src="../commonjs/calendar.js"></script>
<script type="text/javascript" src="../javascript/dms/fileManagement.js"></script>
<script type="text/javascript" src="../javascript/dms/fileManagement-searchFile.js"></script>
