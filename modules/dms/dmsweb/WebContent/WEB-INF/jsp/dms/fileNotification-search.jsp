<%@ taglib prefix="s" uri="/struts-tags"%>
<%@ include file="/includes/taglibs.jsp"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<html>
	<head>
		<title><s:text name="lbl.filenotifsrch"/></title>
		<link href="../commonyui/yui2.7/fonts/fonts-min.css" type="text/css" rel="stylesheet"></link>
		<link rel="stylesheet" type="text/css" href="../commonyui/yui2.7/paginator/assets/skins/sam/paginator.css"/>
		<link href="../commonyui/yui2.7/datatable/assets/skins/sam/datatable.css" type="text/css" rel="stylesheet"></link>
		<link href="../commonyui/yui2.7/calendar/assets/skins/sam/calendar.css" rel="stylesheet" type="text/css"></link>
		<link rel="stylesheet" type="text/css" href="../commonyui/yui2.7/container/assets/skins/sam/container.css">
		<link href="../css/dms/fileManagement.css" rel="stylesheet" type="text/css" />
		<style type="text/css">
		 a {color:blue;	text-decoration: underline;}
		 tr.odd {background-color: #fff}
		 tr.even {background-color: #eeeeee}
		 thead tr th{background:url('../images/tablebg.gif') repeat-x;text-align:left}
		 tbody td {padding: 2px 4px 2px 4px;}
		 div.exportlinks {
			background-color: #eee;
			border: 1px dotted #999;
			padding: 2px 4px 2px 4px;
			margin: 10px 0 10px 0;
			width: 100%;
		}
		</style>
	</head>
	<body style="background-color: white" class="yui-skin-sam">
		<egovtags:breadcrumb />
		<div id="calender" class="cal"></div>
		<div class="mainhead">
		  <div><s:text name="lbl.filenotifsrch"/></div>
		</div>
		<div class="formmainbox">
		<s:form action="fileNotification!searchFileNotification.action" method="POST" theme="simple">
		<table border="0" cellpadding="6" cellspacing="0" width="100%">
			<tbody>
				<tr class="graybox">
					<td>
						<s:text name="lbl.file"/>#
					</td>
					<td>
						<input type="text" name="fileNumber" id="fileNumber" tabindex="1" value="${fileNumber}"/>			
					</td>
					<td>
						<s:text name="lbl.filecat"/>
					</td>
					<td>
						<s:select headerValue="%{getText('sel.catgry')}" headerKey="" name="fileCategory" id="fileCategory"  list="dropdownData.fileCategoryList" listValue="name" listKey="id"  theme="simple" onchange="loadFileSubcategories(this)" tabindex="1" />
						<egovtags:ajaxdropdown id="fileSubcategoryDropdown"  fields="['Text','Value']" dropdownId='fileSubcategory' url='dms/fileManagement!getFileSubcategories.action' selectedValue="%{fileSubcategory.id}"/>
					</td>		
				</tr>
				
				<tr class="whitebox">
					<td>
						<s:text name="lbl.fileheadng"/>
					</td>
					<td>
						<input type="text" name="fileHeading" id="fileHeading" tabindex="1"/>			
					</td>
					<td>
						<s:text name="lbl.filercvdfrm"/>
					</td>
					<td>
						<s:select headerValue="%{getText('sel.src')}" headerKey="" name="userSource" id="userSource"  list="dropdownData.fileSourceList" listValue="name" listKey="id"  theme="simple"  tabindex="1" />
					</td>					
				</tr>
				
				<tr class="graybox">
					<td>
						<s:text name="lbl.startdt"/>
					</td>

					<td>
						<input name="startDate" onkeyup="DateFormat(this,this.value,event,false,'3')" onblur="validateDateFormat(this);" id="startDate" type="text" tabindex="1">
						<img src="../images/calendaricon.gif" onclick="showCalendar(this,event)" alt="Date" width="18" id="startDateBtn" align="absmiddle" tabindex="1"/>
					</td>
					<td>
						<s:text name="lbl.enddt"/>
					</td>
					<td>
						<input name="endDate" onkeyup="DateFormat(this,this.value,event,false,'3')" onblur="validateDateFormat(this);" id="endDate" type="text" tabindex="1">
						<img src="../images/calendaricon.gif" onclick="showCalendar(this,event)" alt="Date" width="18" id="endDateBtn" align="absmiddle" tabindex="1"/>
					</td>							
				</tr>
				<tr class="whitebox">
					<td colspan="4" height="25px">
						&nbsp;
					</td>
				</tr>
				<tr class="graybox">
					<td colspan="4" align="center">
						<s:submit  method="searchFileNotification" name="Search" value="%{getText('lbl.search')}" theme="simple"></s:submit>&nbsp;&nbsp;
						<input name="reset" tabindex="1" value="<s:text name='lbl.reset'/>" type="reset">&nbsp;&nbsp;
						<input name="close" tabindex="1" value="<s:text name='lbl.close'/>" onclick="window.close()" type="button">
						<s:hidden name="mode"></s:hidden>
					</td>
				</tr>
			</tbody>
		</table>
				
			<s:if test="pagedResults != null && pagedResults.getList() != null && !pagedResults.getList().isEmpty()">
			<s:if test="%{mode == 'report'}">
				<s:set name="export" value="true"/>
			</s:if>
			<s:else>
				<s:set name="export" value="false"/>
			</s:else>			
			<display:table name="pagedResults" uid="currentRowObject" style="width:100%;" pagesize = "10" export="${export}" requestURI="fileNotification!searchFileNotification.action?reportSize=${reportSize}"  excludedParams="reportSize" cellpadding="0" cellspacing="0" >
			
			<display:caption style="text-align: center; margin: 10px 0 10px 0;" class="headerbold">Notification File Results</display:caption>
			
			<display:column title="Srl No."><s:property value="%{#attr.currentRowObject_rowNum+(page == 0  ? 0: (page-1))*10}"/>
			</display:column>				
							
			<display:column title="File Date" >
				<fmt:formatDate value="${currentRowObject.fileDate}" pattern="dd/MM/yyyy"/>
			</display:column>
			<s:if test="%{mode == 'report'}">
				<display:column title="File Number"><c:out value="${currentRowObject.fileNumber}"/></display:column>
			</s:if>
			<s:else>
				<display:column title="File Number" ><a href="#" onclick="openWindow('${currentRowObject.id}')"><c:out value="${currentRowObject.fileNumber}"/></a></display:column>
			</s:else>			
			
			<display:column title="File Category" property="fileCategory.name" />
			
			<display:column title="File Heading" property="fileHeading" />

			<display:column title="Received From" property="sender.userSource.name" />
			
			<display:column title="Priority" property="filePriority.name" />
			<div style="display:table-header-group;">									      
				<display:setProperty name="basic.show.header" value="true" />
				<display:setProperty name="basic.empty.showtable" value="true" />
				<display:setProperty name="export.excel.class" value="org.egov.infstr.displaytag.export.EGovExcelView"/>
                <display:setProperty name="export.excel.filename" value="notificationFile.xls"/>
				<display:setProperty name="export.csv" value="false"/>
				<display:setProperty name="export.xml" value="false"/>
			</div>
		</display:table >		
		</s:if>
		<% if(request.getAttribute("hasResult") != null && (!(Boolean)request.getAttribute("hasResult"))){%>
			<br/>
			<br/>
			<center style="color:red"><s:text name="err.noresult"/></center> 
		<%} %>		
		</s:form>		
		</div>
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
<script type="text/javascript" src="../commonyui/yui2.7/dragdrop/dragdrop-min.js"></script>	 
<script type="text/javascript" src="../commonyui/yui2.7/animation/animation-min.js"></script>
<script type="text/javascript" src="../javascript/dateValidation.js"></script>  
<script type="text/javascript" src="../commonjs/calendar.js"></script>
<script type="text/javascript" src="../javascript/dms/fileManagement.js"></script> 
<script type="text/javascript" src="../javascript/dms/fileNotification.js"></script>