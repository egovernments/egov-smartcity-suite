<%@ taglib prefix="s" uri="/struts-tags"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@page import="org.egov.infstr.client.filter.EGOVThreadLocals" %>
<html>
	<head>
		<title>Document Search</title>
		<link href="../commonyui/yui2.7/fonts/fonts-min.css" type="text/css" rel="stylesheet"></link>
		<link href="../commonyui/yui2.7/datatable/assets/skins/sam/datatable.css" type="text/css" rel="stylesheet"></link>
		<link href="../commonyui/yui2.7/menu/assets/skins/sam/menu.css" rel="stylesheet" type="text/css" />
		<link href="../commonyui/yui2.7/calendar/assets/skins/sam/calendar.css" rel="stylesheet" type="text/css"></link>
		<link href="../css/docmgmt/documentManager.css" rel="stylesheet" type="text/css"></link>
		<link href="../css/docmgmt/documentManager-searchNotice.css" rel="stylesheet" type="text/css"></link>			
	</head> 
	<body class="yui-skin-sam" >
		<div id="calender" class="cal"></div>
		<div class="container">		
			<div class="mainhead">Document Search</div>
			<table >
				<tbody>
					<tr>
						<td >
						<fieldset id="fieldset" class="main3" > 
							<legend class="main1">Notice Search Criteria</legend>
							<table width="100%" cellpadding="2" cellspacing="3">
								<tr>
									<td class="tbltd">
				 					Notice Number : 
					 				</td>
					 				<td>
					 					<input type="text" id="documentNumber" name="documentNumber" value=""/>					 					
									</td>
									<td class="tbltd" >
				 					Notice Type : 
					 				</td>
					 				<td>
					 					<input type="text" id="noticeType" name="noticeType" value=""/>					 					
									</td>										
								</tr>
								<tr>
									<td class="tbltd" >
				 					Notice From Date : 
					 				</td>
					 				<td>
					 					<input type="text" id="noticeDate1" name="noticeDate" value=""/>
					 					<img src="../images/calendaricon.gif" onclick="javascript:dateCompId=this.id" id="fromDateBtn" class="hand"/>					 					
									</td>
									<td class="tbltd" >
				 					Notice To Date : 
					 				</td>
					 				<td>
					 					<input type="text" id="noticeDate2" name="noticeDate" value=""/>
					 					<img src="../images/calendaricon.gif" onclick="javascript:dateCompId=this.id"  id="toDateBtn"  class="hand"/>					 					
									</td>										
								</tr>
								<tr>
									<td class="tbltd" >
				 					Address To : 
					 				</td>
					 				<td colspan="3">
					 					<input type="text" id="addressedTo" name="addressedTo" value=""/>					 					
									</td>									
								</tr>
								<tr height="50px">
									<td >										
									</td>
									<td></td>
									<td>										
									</td>
									<td>										
										<input type="hidden" value="Notice" id="type" name="type"/>
										<input type="hidden" value="${moduleName}" id="moduleName" name="moduleName"/>
										<input type="button" value="Search" id="searchbtn" class="save" onclick="searchDocument()"/>
										&nbsp;&nbsp;<input  type="button" class="close" value="  Close  " onclick="window.close()"/>
									</td>									
								</tr>
							</table>
							<table border="0" id="error" class="error">
								<tr>
									<td width="100%" colspan="2">
										<hr size="1" width="810px"/>										
									</td>
								</tr>
								<tr>
									<td width="10%">
										<img src="../images/error.gif" alt="Detach" title="Error" border="0" />
									</td>
									<td valign="middle">
										<font color="red" size="2"><span id="errorMsg"></span></font>
									</td>
								</tr>
							</table>		
						</fieldset>
						</td>
					</tr>
				</tbody>
			</table>
			<fieldset id="searchrslt" class="main3" > 
				<legend class="main1">Notice Search Results</legend>
				<div id="srchrsltdiv" class="srchrsltdiv">
					<div id="searchresult">
					</div>
				</div>
			</fieldset>			
		</div>
		<div id="filedwnldpanel" class="filedwnldpanel cursor">
			<img src="../images/cancel.jpg"  class="infocls" onclick="closeFileDownload()"/><br/>
			<fieldset class="filedwnlfdset">	
			<legend>File Download</legend>								
			<div id="filedwnld" class="filediv"></div>
			</fieldset>
			<span style="margin-left:10px"><font size="1">[Click on the file name to download]</font></span>			
		</div>	
		<div class="urlcontainer"> eGovernments Foundations &copy; All rights reserved</div>
		<div id="loading" class="loading"><span>Please wait...</span></div>			
	</body>
</html>
<script type="text/javascript" src="../commonyui/yui2.7/yahoo-dom-event/yahoo-dom-event.js"></script>
<script type="text/javascript" src="../commonyui/yui2.7/calendar/calendar.js"></script>
<script type="text/javascript" src="../commonyui/yui2.7/yahoo/yahoo-min.js"></script> 
<script type="text/javascript" src="../commonyui/yui2.7/json/json-min.js"></script>  
<script type="text/javascript" src="../commonyui/yui2.7/event/event-min.js"></script>  
<script type="text/javascript" src="../commonyui/yui2.7/connection/connection-min.js"></script> 
<script type="text/javascript" src="../commonyui/yui2.7/dragdrop/dragdrop-min.js"></script>		
<script type="text/javascript" src="../commonyui/yui2.7/container/container_core-min.js"></script>
<script type="text/javascript" src="../commonyui/yui2.7/menu/menu-min.js"></script>
<script type="text/javascript" src="../commonyui/yui2.7/element/element-min.js"></script>
<script type="text/javascript" src="../commonyui/yui2.7/datasource/datasource-min.js"></script>
<script type="text/javascript" src="../commonyui/yui2.7/datatable/datatable-min.js"></script>
<script type="text/javascript" src="../javascript/docmgmt/documentManager-searchNotice.js"></script>
