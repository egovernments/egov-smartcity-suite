<%@ taglib prefix="s" uri="/struts-tags"%>
<%@ include file="/includes/taglibs.jsp" %>
<html>
	<head>
		<title><s:text name="lbl.notifgrpmstr"/></title>
		<link href="../commonyui/yui2.7/fonts/fonts-min.css" type="text/css" rel="stylesheet"></link>
		<link rel="stylesheet" type="text/css" href="../commonyui/yui2.7/paginator/assets/skins/sam/paginator.css"/>
		<link href="../commonyui/yui2.7/datatable/assets/skins/sam/datatable.css" type="text/css" rel="stylesheet"></link>
		<link rel="stylesheet" type="text/css" href="../commonyui/yui2.7/container/assets/skins/sam/container.css">
		<link href="../css/dms/fileManagement.css" rel="stylesheet" type="text/css" />	
	</head>	
	<body style="background-color: white" class="yui-skin-sam">
			<egovtags:breadcrumb/>
			<div class="mainhead">
				<s:text name="lbl.notifgrpmstr"/>
			</div>
			<s:form action="notificationGroup" method="POST"  theme="simple">
				<s:push value="model">
				<fieldset>
				<div id="calender" class="cal"></div>
				<legend align="left"><b><s:text name="lbl.notigrpinfo"/></b></legend>
				<table border="0" cellpadding="4" cellspacing="0" width="100%">
				<tbody>
					<tr class="graybox">
						<td>
							<s:text name="lbl.grpname"/>
							<s:hidden name="id" id="id"></s:hidden>
						</td>
						<td>
							<span>${groupName}</span>
						</td>
						<td>
							<s:text name="lbl.grpdesc"/>
						</td>
						<td >
							<div class='viewdiv'>${groupDesc}</div>
						</td>
					</tr>
					<tr class="whitebox">
						<td>
							<s:text name="lbl.active"/>
						</td>
						<td >
							<span>
								<s:if test="active == 'Y'" >
								<s:text name="lbl.yes"/>
								</s:if>
								<s:else>
								<s:text name="lbl.no"/>
								</s:else>
							</span>
						</td>
						<td>
							<s:text name="lbl.effdt"/> 
						</td>
						<td >
							<span><s:date name="effectiveDate" format="dd/MM/yyyy" /></span>
						</td>
					</tr>
					<tr>
						<td colspan="4">
							<script>
							var positions = "["
							<c:forEach items="${members}" var="position" >
									positions = positions+"{id:"+${position.id}+",Position:'"+"${position.name}"+"'},";
							</c:forEach>
							positions = positions.substr(0,positions.length-1);
							positions = positions+"]";
							</script>
							<div id="searchresult"></div>
						</td>									
					</tr>
					</tbody>
				</table>							
			</fieldset>
			<br/>
			<table border="0" width="100%">
				<tr align="center" class="graybox">
					<td>
						<s:submit method="delete" value="%{getText('lbl.del')}" id="delete" tabindex="1" onclick="showWaiting();"/>&nbsp;&nbsp;&nbsp;
						<input type="button" value="<s:text name='lbl.edit'/>" id="edit" tabindex="1" onclick="submits();showWaiting();"/>&nbsp;&nbsp;&nbsp;
						<input type="button" value="<s:text name='lbl.close'/>" tabindex="1" onclick="window.close()">
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
						<img src="../images/success.gif" alt="Sucessful" title="Notification Group Created Successfully" border="0" />
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
	<div id="loading" class="loading" style="height:100%"><span><s:text name="info.plswait"/></span></div>
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
<script type="text/javascript" src="../commonyui/yui2.7/connection/connection-min.js"></script> 
<script type="text/javascript" src="../commonyui/yui2.7/paginator/paginator-min.js"></script>
<script type="text/javascript" src="../commonyui/yui2.7/dragdrop/dragdrop-min.js"></script>	 
<script type="text/javascript" src="../commonyui/yui2.7/datasource/datasource-min.js"></script>
<script type="text/javascript" src="../commonyui/yui2.7/datatable/datatable-min.js"></script>
<script type="text/javascript" src="../javascript/dms/fileManagement.js"></script> 
<script type="text/javascript" src="../javascript/dms/notificationGroup.js"></script> 
<script>
listPositions(positions,true);
</script> 