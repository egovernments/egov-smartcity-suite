<%@ page language="java" pageEncoding="UTF-8"%>
<%@ taglib prefix="s" uri="/struts-tags"%>
<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN">
<html lang="true">
	<head>
		<title>Workflow Administration</title>
		<link href="../commonyui/yui2.7/fonts/fonts-min.css" type="text/css" rel="stylesheet"></link>
		<link rel="stylesheet" type="text/css" href="../commonyui/yui2.7/paginator/assets/skins/sam/paginator.css"/>
		<link href="../commonyui/yui2.7/datatable/assets/skins/sam/datatable.css" type="text/css" rel="stylesheet"></link>
		<link href="../commonyui/yui2.7/menu/assets/skins/sam/menu.css" rel="stylesheet" type="text/css" />
		<link href="../commonyui/yui2.7/calendar/assets/skins/sam/calendar.css" rel="stylesheet" type="text/css"></link>
		<link type="text/css" rel="stylesheet" href="../commonyui/yui2.7/autocomplete/assets/skins/sam/autocomplete.css">
		<link rel="stylesheet" type="text/css" href="../commonyui/yui2.7/container/assets/skins/sam/container.css">
		<link rel="stylesheet" type="text/css" href="../commonyui/yui2.7/button/assets/skins/sam/button.css">
		<link href="../css/workflow/workflowAdmin.css" rel="stylesheet" type="text/css" />
		
	</head>

	<body class="yui-skin-sam">
		<center>
		<div class="wfadmin">
			<div class="wfadminheading"> 
				Workflow Administration 
			</div>
			<div id="search">
				<table class="searchtbl" width="1243px" border="0" cellspacing="0"
					cellpadding="0">
					<tr>
						<td width="9%" class="whitebox">
							&nbsp;
						</td>
						<td width="15%" class="whitebox">
							Document Type
							<span class="mandatory">*</span>
						</td>
						<td width="26%" class="whitebox">
							<div id="acCont1" class="autoComContainer">
								<input name="wfType" id="wfType" type="text" tabindex="1" onblur="populateWorkflowState();populateWFSearchFields()" style="width:300px"/>
								<input  id="wfTypeValue" type="hidden"/>
								<div id="docTypeAC"></div>								
							</div>
						</td>
						<td width="15%" class="whitebox">
							Workflow State
						</td>
						<td width="35%" class="whitebox">
							<select name="wfState" tabindex="2"  id="wfState" style="width:350px">
							</select>
						</td>
					</tr>
					<tr>
						<td class="graybox">
							&nbsp;
						</td>
						<td class="graybox">
							Pending With
						</td>
						<td class="graybox">
							<div id="acCont2" class="autoComContainer2 yui-ac">
								<input name="owner" id="owner" type="text" tabindex="3"  onclick="document.getElementById('owner').value='';" style="width:150px"/>
								<div id="pendingWithAC"></div>																
							</div>
							
							
						</td>
						<td class="graybox">
							Initiated By
						</td>
						<td class="graybox">
							<div id="acCont3" class="autoComContainer2 yui-ac">
								<input name="sender" id="sender" type="text" tabindex="3"  style="width:150px"/>
								<div id="initByAC"></div>	
							</div>
						</td>
					</tr>
					<tr>
						<td class="whitebox">
							&nbsp;
						</td>
						<td class="whitebox">
							Created Date From
						</td>
						<td class="whitebox">
							<input name="fromDate" id="fromDate" type="text" width="95%" tabindex="5" />
							<img src="../images/calendaricon.gif" onclick="showCalendar(this,event)" alt="Date" width="18" id="fromDateBtn" height="18" align="absmiddle" tabindex="6" />
						</td>
						<td class="whitebox">
							To Created Date
						</td>
						<td class="whitebox">
							<input name="toDate" id="toDate" type="text" width="95%" tabindex="7" />
							<img src="../images/calendaricon.gif" onclick="showCalendar(this,event)" alt="Date" width="18" id="toDateBtn" height="18" align="absmiddle" tabindex="8" />
						</td>
					</tr>
					<tr id="idearchrow" class="invisowner">
						<td class="graybox">
							&nbsp;
						</td>
						<td class="graybox">
							Identifier
						</td>
						<td class="graybox" colspan="3">
							<select name="searchField" tabindex="8"  id="searchField" onchange="setSearchOp(this)" >
							</select>
							<span id="searchOp"></span>
							<input name="identifier" id="identifier" type="text" width="95%" tabindex="8" class="invisowner"/>
							<img src="../images/calendaricon.gif" onclick="showCalendar(this,event)" alt="Date" width="18" id="identifierBtn" class="invisowner" height="18" align="absmiddle" tabindex="9" />
							<span id="opbtwn" class="invisowner">&nbsp;And&nbsp;</span>
							<input name="identifier2" id="identifier2" type="text" width="95%" tabindex="10" class="invisowner"/>
							<img src="../images/calendaricon.gif" onclick="showCalendar(this,event)" alt="Date" width="18" id="identifier2Btn" height="18" class="invisowner" align="absmiddle" tabindex="11" />
						</td>						
					</tr>
					<tr style="height:32px">
						<td class="whitebox" colspan="5">
						&nbsp;
						</td>
					</tr>
				</table>
				<table width="100%">
					<tr>
						<td colspan="5" class="mandatory graybox" align="center">
							<input type="button" value="Search" onclick="searchWfItems()" tabindex="12" class="searchbtn" />	
						</td>
					</tr>
					<tr>
						<td colspan="5" class="mandatory">
							<hr />
							<font size="1.5">* Mandatory Field</font>
						</td>
					</tr>
				</table>
				<table border="0" id="msg" class="msg">
					<tr>
						<td align="center">
							<img src="../images/error.gif" border="0" />
						</td>
						<td valign="middle">
							<font color="red" size="2"><span id="msgSpace"></span></font>
						</td>
					</tr>
				</table>
				<div id="reassignDialogue" class="dialogue">
				    <div class="hd">Re-assign Work</div>
				    <div class="bd">
				    	<table cellspacing="5" width="500px">
				    		<tr>
				    			<td>
			           			 	<label class="label">Current Owner </label>
			           			</td>
			           			<td>
			           				<span id="uname" class="label"></span>
			           			</td>
			           		</tr> 
			           		<tr>
				    			<td>
			           			 	<label for="department" class="label">Department <span id='mandate1' class="mandate">*</span> 
			           			</td>
			           			<td>
			           			 	<s:select headerValue="--Select a Department--" headerKey="" id="department"  list="departmentList" listValue="deptName" listKey="id"  theme="simple" onchange="populateDesignation(this)"/>
			           			</td>
			           		</tr>
			           		<tr>
				    			<td>
			           			 	<label for="designation" class="label">Designation <span id='mandate2' class="mandate">*</span>
			           			</td>
			           			<td>
			           			 	<select name="designation"   id="designation" onchange="populateUser(this)">
									</select>
			           			</td>
			           		</tr>
			           		<tr>
				    			<td>
			           			 	 <label for="user" class="label">User <font color="red" size="2">*</font> </label>
			           			</td>
			           			<td>
			           				<div id="acCont4" class="autoComContainer2 yui-ac">
										 <input type="text" id="newOwner" style="width:157px"/>
										<div id="newOwnerAC"></div>	
									</div>
									<select name="newOwner"   id="newOwner1" class="invisowner">
									</select>    
			           			</td>
			           		</tr>
			           	</table>			              
				    </div>
				</div>			
				<div id="searchrslt" class="searchrslt">
					<table cellpadding="0" cellspacing="0" class="btnitem" width="100%">
						<tr height="35px" class="toolsbar">
							<td id="admintoolbar"></td>
						</tr>
					</table>
					<div id="wfItemsDiv" class="srchrsltdiv">
						<div id="searchresult"></div>
					</div>
				</div>							
			</div>
			<div id="calender" class="cal"></div>
			</center>
			<center class="bttm">
  					<input type="button" value="   Exit   " onclick="closeWin()"  class="searchbtn"/>
			</center>
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
<script type="text/javascript" src="../commonyui/yui2.7/paginator/paginator-min.js"/>
<script type="text/javascript" src="../commonyui/yui2.7/dragdrop/dragdrop-min.js"></script>	 
<script type="text/javascript" src="../commonyui/yui2.7/datasource/datasource-min.js"></script>
<script type="text/javascript" src="../commonyui/yui2.7/datatable/datatable-min.js"></script>
<script type="text/javascript" src="../commonyui/yui2.7/autocomplete/autocomplete-min.js"></script>
<script type="text/javascript" src="../commonyui/yui2.7/animation/animation-min.js"></script>
<script type="text/javascript" src="../commonyui/yui2.7/button/button-min.js"></script>
<script type="text/javascript" src="../javascript/common/utils.js"></script>
<script type="text/javascript" src="../javascript/workflow/workflowAdmin.js"></script>
