<%@ taglib prefix="s" uri="/struts-tags"%>
<%@ include file="/includes/taglibs.jsp"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<html>
	<head>
		<title><s:text name="lbl.notifgrpsrch"/></title>
		<link href="../commonyui/yui2.7/fonts/fonts-min.css" type="text/css" rel="stylesheet"></link>
		<link rel="stylesheet" type="text/css" href="../commonyui/yui2.7/paginator/assets/skins/sam/paginator.css"/>
		<link href="../commonyui/yui2.7/datatable/assets/skins/sam/datatable.css" type="text/css" rel="stylesheet"></link>
		<link href="../commonyui/yui2.7/menu/assets/skins/sam/menu.css" rel="stylesheet" type="text/css" />
		<link href="../commonyui/yui2.7/calendar/assets/skins/sam/calendar.css" rel="stylesheet" type="text/css"></link>
		<link type="text/css" rel="stylesheet" href="../commonyui/yui2.7/autocomplete/assets/skins/sam/autocomplete.css">
		<link rel="stylesheet" type="text/css" href="../commonyui/yui2.7/container/assets/skins/sam/container.css">
		<link href="../css/dms/fileManagement.css" rel="stylesheet" type="text/css" />
	</head>
	<body style="background-color: white" class="yui-skin-sam">
		<egovtags:breadcrumb />
		
		
		<div id="calender" class="cal"></div>
		<div class="mainhead">
		  <div style="margin-top:10px"><s:text name="lbl.notifgrpsrch"/></div>
		  
		</div>
		<div class="formmainbox">
		
		<table border="0" cellpadding="6" cellspacing="0" width="100%">
			<tbody>
				<tr class="graybox">
					<td>
						<s:text name="lbl.notifgrpname"/>
					</td>
					<td width="22%" >
						<div id="acCont" class="autoComContainer yui-ac" style="margin-bottom:20px;padding-bottom:10px">
							<input type="text" name="groupName" id="groupName" style="width:150px;"/>									
							<div id="groupNameAC"></div>	
						</div>
					</td>
					<td>
						<s:text name="lbl.active"/>
					</td>

					<td>
						<select name="active" id="active">
							<option value=""></option>
							<option value="Y"><s:text name="lbl.yes"/></option>
							<option value="N"><s:text name="lbl.no"/></option>
						</select>
					</td>
				</tr>

				<tr class="whitebox">
					<td>
						<s:text name="lbl.efffrm"/>
					</td>

					<td>
						<input name="effDtFrom"  onkeyup="DateFormat(this,this.value,event,false,'3')" onblur="validateDateFormat(this);" id="effDtFrom" type="text">
						<img src="../images/calendaricon.gif" onclick="showCalendar(this,event)" alt="Date" width="18" id="effDtFromBtn" align="absmiddle" />
					</td>
					<td>
						<s:text name="lbl.effto"/>
					</td>
					<td>
						<input name="effDtTo" onkeyup="DateFormat(this,this.value,event,false,'3')" onblur="validateDateFormat(this);" id="effDtTo" type="text">
						<img src="../images/calendaricon.gif" onclick="showCalendar(this,event)" alt="Date" width="18" id="effDtToBtn" align="absmiddle" />
					</td>							
				</tr>
				<tr class="graybox">
					<td colspan="8" align="center">
						<input name="search" tabindex="1" value="<s:text name='lbl.search'/>" onclick="searchNotificationGroup()" type="button">
						<input name="close" tabindex="1" value="<s:text name='lbl.close'/>" onclick="window.close()" type="button">
					</td>
				</tr>
			</tbody>
		</table>
		<div id="searchrslt" class="searchrslt">
			<div id="ntfgrpDiv" class="srchrsltdiv">
				<div id="searchresult"></div>
			</div>
			<input type="hidden" name="ngLink" id="ngLink" />
			<center class="graybox">
				<input name="view" tabindex="1" value="<s:text name='lbl.view'/>" onclick="openNotification('view')" type="button">&nbsp;&nbsp;
				<input name="edit" tabindex="1" value="<s:text name='lbl.edit'/>" onclick="openNotification('edit')" type="button">&nbsp;&nbsp;
				<input name="delete" tabindex="1" value="<s:text name='lbl.del'/>" onclick="openNotification('delete')" type="button">&nbsp;&nbsp;
			</center>
		</div>
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
<script type="text/javascript" src="../commonyui/yui2.7/connection/connection-min.js"></script> 
<script type="text/javascript" src="../commonyui/yui2.7/menu/menu-min.js"></script>
<script type="text/javascript" src="../commonyui/yui2.7/paginator/paginator-min.js"></script>
<script type="text/javascript" src="../commonyui/yui2.7/dragdrop/dragdrop-min.js"></script>	 
<script type="text/javascript" src="../commonyui/yui2.7/datasource/datasource-min.js"></script>
<script type="text/javascript" src="../commonyui/yui2.7/datatable/datatable-min.js"></script>
<script type="text/javascript" src="../commonyui/yui2.7/autocomplete/autocomplete-min.js"></script>
<script type="text/javascript" src="../commonyui/yui2.7/animation/animation-min.js"></script>
<script type="text/javascript" src="../javascript/dateValidation.js"></script>  
<script type="text/javascript" src="../commonjs/calendar.js"></script>
<script type="text/javascript" src="../javascript/dms/fileManagement.js"></script> 
<script type="text/javascript" src="../javascript/dms/notificationGroup.js"></script>
<script type="text/javascript" src="../javascript/dms/notificationGroup-search.js"></script>
