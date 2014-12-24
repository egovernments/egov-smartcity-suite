<%@ taglib prefix="s" uri="/struts-tags" %> 
<html>
  <head>
    <title>Inbox</title>    
	<meta http-equiv="pragma" content="no-cache">
	<meta http-equiv="cache-control" content="no-cache">
	<meta http-equiv="expires" content="0">    
	<meta http-equiv="keywords" content="egovernments inbox,egovernments workflow,egovernments">
	<meta http-equiv="description" content="eGovernments Inbox">
	<link href="../commonyui/yui2.7/fonts/fonts-min.css" type="text/css" rel="stylesheet"></link>
	<link rel="stylesheet" type="text/css" href="../commonyui/yui2.7/paginator/assets/skins/sam/paginator.css"/>
	<link href="../commonyui/yui2.7/datatable/assets/skins/sam/datatable.css" type="text/css" rel="stylesheet"> </link>
	<link href="../commonyui/yui2.7/menu/assets/skins/sam/menu.css" rel="stylesheet" type="text/css" />
	<link href="../commonyui/yui2.7/calendar/assets/skins/sam/calendar.css" rel="stylesheet" type="text/css">
	<link href="../css/workflow/inbox.css" rel="stylesheet" type="text/css"> </link>
  </head>  
  <body class="yui-skin-sam" id="body" oncontextmenu="return false;">
  	<div id="calender" class="cal"></div> 
  	<div class="inboxstrip"><div style="padding-top:5px" id="inboxtitle">Inbox</div></div>
	<div id="toolsbar" class="toolsbar">
		<div class="refreshbtn hand"><img src="../images/refresh.gif" title="Refresh" onclick="egovInbox.refresh();" id="refresh"></div>
		<div class="filterbtn hand"><img src="../images/filter.gif"  title="Filter" onclick="egovInbox.showFilter();" id="filter"></div>
		<div style="position:absolute;right:10px;top:1px">
		<table>
			<tr>
				<td style="font-family:verdana;font-size: 10px">Show </td>
				<td><input type="text" title="Number of items to be showing" value="15" id="pageSize" value="1" name="pageSize" maxlength="3" size="3" style="text-align:center; font-weight:bold;height:15px;font-size: 10px;" onchange="egovInbox.refresh();"/></td>
			</tr>
		</table>
		</div>
	</div>
	<div id="inboxContainer" class="inboxContainer">
			<img src="../images/loading.gif" id="loadImg" class="loadImg"/>
			<span id="error" class="error"></span>
			<div class="inbxDiv1" id="inbxDiv1">
				<div id="inbox" title="Right click on a record to get its History"></div>
			</div>
			<div id="historyContainer" class="historyContainer" onmousedown="javascript:if (document.getElementById('min').title == 'Minimize') egovInbox.DragAndDrop(this,event,'movebar');">
				<span class="titles inbxtitle" >History</span>
				<span class="titler hisminimize" onclick="egovInbox.minimizeHistory(this)" id="min" title="Minimize">-</span>
				<span class="titler hisclose" onclick="egovInbox.closeHistory()" title="Close">X</span>
				<table  height="25px" border="0" cellspacing="0" width="100%" id="movebar">
					<tr height="11px" class="titlrTr1" width="100%"><td width="100%" class="bordertopb" align="right"></td></tr>
					<tr height="14px" class="titlrTr2" width="100%"><td width="100%" class="borderbtmb"></td></tr>
				</table>
				<div class="inbxDiv2">
					<div id="history"></div>
				</div>
			</div>		
			<div id="filterContainer" class="filterContainer" onmousedown="egovInbox.DragAndDrop(this,event,'movebar2')" >
				<span class="titles my" >Inbox Filter</span>
				<table  height="25px" border="0" cellspacing="0" width="100%" id="movebar2">
					<tr height="11px" class="titlrTr1" width="100%"><td width="100%" class="bordertopb" align="right"></td></tr>
					<tr height="14px" class="titlrTr2" width="100%"><td width="100%" class="borderbtmb"></td></tr>
				</table>
				<div class="filterDiv">
					<table>
						<tr>
							<td>From Date : </td>
							<td><input type="text" size="20" class="filterBox" id="fromDate" ></td>
							<td><img src="../images/calendaricon.gif" id="fromDateBtn" onclick="javascript:egovInbox.forDate='fromDate'" class="hand"/></td>
						</tr>
						<tr>
							<td>To Date : </td>
							<td><input type="text" size="20" class="filterBox" id="toDate" ></td>
							<td><img src="../images/calendaricon.gif" id="toDateBtn" onclick="javascript:egovInbox.forDate='toDate'" class="hand"/></td>
						</tr>
						<s:select headerValue="----Select a sender-----" headerKey="" id="sender" label="Sender" list="senderList"/>
						<s:select headerValue="----Select a task-----"   headerKey="" id="task" label="Task" list="taskList"/>
					</table>
					<div class="btndiv">
						<input type="button" value="   Filter   " onclick="egovInbox.filterInbox()"> &nbsp;&nbsp;
						<input type="button" value="   Close  " onclick="egovInbox.closeFilter()">
					</div>
				</div>				
			</div>			
		</div>
	</body>		
</html>
	<script type="text/javascript" src="../commonyui/yui2.7/yahoo-dom-event/yahoo-dom-event.js"></script>
	<script type="text/javascript" src="../commonyui/yui2.7/element/element-min.js"></script>
	<script type="text/javascript" src="../commonyui/yui2.7/yahoo/yahoo-min.js"></script> 
	<script type="text/javascript" src="../commonyui/yui2.7/json/json-min.js"></script>  
	<script type="text/javascript" src="../commonyui/yui2.7/event/event-min.js"></script>
	<script type="text/javascript" src="../commonyui/yui2.7/container/container_core-min.js"></script> 
	<script type="text/javascript" src="../commonyui/yui2.7/calendar/calendar-min.js"></script>
	<script type="text/javascript" src="../commonyui/yui2.7/connection/connection-min.js"></script> 		
	<script type="text/javascript" src="../commonyui/yui2.7/menu/menu-min.js"></script>	
	<script type="text/javascript" src="../commonyui/yui2.7/paginator/paginator-min.js"/> 
	<script type="text/javascript" src="../commonyui/yui2.7/dragdrop/dragdrop-min.js"></script>	
	<script type="text/javascript" src="../commonyui/yui2.7/datasource/datasource-min.js"></script>	
	<script type="text/javascript" src="../commonyui/yui2.7/datatable/datatable-min.js"></script>
	<script type="text/javascript" src="../javascript/workflow/inbox.js"></script>
	<script type="text/javascript">
		var inboxData = "<s:property value="inboxData" />";
		var draftData = "<s:property value="inboxDraft" />";
		document.getElementById('inbox').style.display='none';	
	</script>
	