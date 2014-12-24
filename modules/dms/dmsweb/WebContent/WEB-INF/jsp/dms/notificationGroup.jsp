<%@ taglib prefix="s" uri="/struts-tags"%>
<%@ include file="/includes/taglibs.jsp" %>
<html>
	<head>
		<title><s:text name="lbl.notifgrpmstr"/></title>
		<link href="../commonyui/yui2.7/fonts/fonts-min.css" type="text/css" rel="stylesheet"></link>
		 <link rel="stylesheet" type="text/css" href="../commonyui/yui2.7/paginator/assets/skins/sam/paginator.css"/>
		<link href="../commonyui/yui2.7/datatable/assets/skins/sam/datatable.css" type="text/css" rel="stylesheet"></link>
		<link rel="stylesheet" type="text/css" href="../commonyui/yui2.7/container/assets/skins/sam/container.css">
		 <link href="../commonyui/yui2.7/calendar/assets/skins/sam/calendar.css" rel="stylesheet" type="text/css"></link>
		 <link href="../css/dms/fileManagement.css" rel="stylesheet" type="text/css" />	
	</head>	
	<body style="background-color: white" class="yui-skin-sam"> 
			<egovtags:breadcrumb/>
			<div class="mainhead">
				<s:text name="lbl.notifgrpmstr"/>
			</div>
			<s:form action="notificationGroup" method="POST"  theme="simple">
				<s:push value="model">               
                <s:token name="%{tokenName()}"/>
				<fieldset>
				<div id="calender" class="cal"></div>
				<legend align="left"><b><s:text name="lbl.notigrpinfo"/></b></legend>
				<table border="0" cellpadding="4" cellspacing="0" width="100%">
				<tbody>
					<tr class="graybox">
						<td>
							<s:text name="lbl.grpname"/><span class="mandatory">*</span>
							<s:hidden name="id"  id="id" />
						</td>
						<td>
							<s:if test="id == null || id==''">
								<s:textfield name="groupName"  onblur="checkGroupNameExist(this)" tabindex="1"/>
							</s:if>
							<s:else>
								<s:textfield name="groupName"  onblur="checkGroupNameExist(this)" tabindex="1" readonly="readonly"/>
								<span id="editGrpName" style="display:none">${groupName}</span>
							</s:else>
						</td>
						<td>
							<s:text name="lbl.grpdesc"/>
						</td>
						<td >
							<s:textarea name="groupDesc" cols="20" rows="3" tabindex="1"/>
						</td>
					</tr>
					<tr class="whitebox">
						<td>
							<s:text name="lbl.active"/><span class="mandatory">*</span>
						</td>
						<td >
							<input type="radio" value="Y" name="active" checked="checked" tabindex="1"><s:text name="lbl.yes"/></input>
							<input type="radio" value="N" name="active" tabindex="1"><s:text name="lbl.no"/></input>
						</td>
						<td>
							<s:text name="lbl.effdt"/><span class="mandatory">*</span>
						</td>
						<td >
							<s:date name="effectiveDate" id="effectiveDateFrmtd" format="dd/MM/yyyy"/>
							<s:textfield value="%{effectiveDateFrmtd}" name="effectiveDate" onkeyup="DateFormat(this,this.value,event,false,'3')" onblur="validateDateFormat(this);" id="date" tabindex="1"/>
							<img src="../images/calendaricon.gif" onclick="showCalendar(this,event)" alt="Date" width="18" id="dateBtn" align="absmiddle" tabindex="1"/>
						</td>
					</tr>
					<s:if test="id!=null">
						<tr >
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
							<select name="members" id="positions" tabindex="1" multiple="multiple" size="5" style="min-width:130px;visibility:hidden">
								<c:forEach items="${members}" var="position" >
									<option value="${position.id}">${position.name}</option>
								</c:forEach>
							</select>
							<button type="button" onclick="addPositions()" style="float:left"><img src="../images/add.png" title="Add Positions"  align="absmiddle"><s:text name="lbl.addpos"/></button>
						</td>									
						</tr>
					</s:if>
					<tr class="graybox" id="posSearch">
						<td colspan="4">
						<br/>
							<fieldset>
							<legend><b><s:text name="lbl.searchpos"/></b></legend>
							<table border="0" cellpadding="4" cellspacing="0" width="100%">
								<tbody>
								<tr class="whitebox">
									<td>
										<s:text name="lbl.zone"/> 
									</td>
									<td>
										<s:select headerKey="" headerValue="%{getText('zone')}" id="firstLevelBndry"  list="firstLevelBoundries" listValue="name" listKey="id"  theme="simple" tabindex="1" onblur="loadWards(this)"  multiple="true" size="5" cssStyle="min-width:130px"/>
									</td>
									<td >
										<s:text name="lbl.ward"/>
									</td>
									<td>
										<select id="secondLevelBndry"  multiple="multiple" size="5" tabindex="1" style="min-width:130px" />
									</td>
								</tr>
								<tr class="graybox">
									<td>
										<s:text name="lbl.dept"/>
									</td>
									<td>
										<s:select headerKey="" headerValue="%{getText('sel.dept')}" id="departments"  list="dropdownData.departments" listValue="deptName" listKey="id"  theme="simple" tabindex="1"  multiple="true" size="5" cssStyle="min-width:130px"/>
									</td>
									<td>
										<s:text name="lbl.desig"/>
									</td>
									<td >
										<s:select  headerKey="" headerValue="%{getText('sel.desig')}" id="designations"  list="dropdownData.designations" listValue="designationName" listKey="designationId"  theme="simple" tabindex="1"  multiple="true" size="5" cssStyle="min-width:130px"/>
									</td>
								</tr>
								<tr class="whitebox">
									<td colspan="4" align="center">
										<br/>
										<input type="button" tabindex="1" value="<s:text name='lbl.getpos'/>" onclick="loadPositions()">
									</td>
								</tr>
								
								<tr class="graybox">
									<td >
										<s:text name="lbl.position"/><span class="mandatory">*</span>
									</td>
									<td colspan="3">
										<select name="members" id="positions" tabindex="1" multiple="multiple" size="5" style="min-width:130px"/>																				
									</td>									
								</tr>
								<tr id="usrinfo" style="display:none;">
									<td>&nbsp;</td>
									<td colspan="3"><div style="color:green;font-size:10px;"><s:text name="info.selposforgroup"/></div></td>
								</tr>
								</tbody>
							</table>
							</fieldset>
							<br/>
						</td>
					</tr>					
				</tbody>
			</table>
			</fieldset>
			<br/>
			<table border="0" width="100%">
				<tr align="center" class="graybox">
					<td>
						<s:if test="id == null">
							<s:submit method="save"  value="%{getText('lbl.create')}" id="savebtn" tabindex="1" onclick="showWaiting()"/>&nbsp;&nbsp;&nbsp;
						</s:if>
						<s:else>
							<input type="button" value="<s:text name='lbl.save'/>" id="savebtn" tabindex="1" onclick="showWaiting();editSubmit()"/>&nbsp;&nbsp;&nbsp;
						</s:else>
						<input type="reset" value="<s:text name='lbl.reset'/>" tabindex="1">&nbsp;&nbsp;&nbsp;
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
<script type="text/javascript" src="../commonyui/yui2.7/calendar/calendar.js"></script> 
<script type="text/javascript" src="../commonyui/yui2.7/connection/connection-min.js"></script> 
<script type="text/javascript" src="../commonyui/yui2.7/menu/menu-min.js"></script>
<script type="text/javascript" src="../commonyui/yui2.7/paginator/paginator-min.js"></script>
<script type="text/javascript" src="../commonyui/yui2.7/dragdrop/dragdrop-min.js"></script>	 
<script type="text/javascript" src="../commonyui/yui2.7/datasource/datasource-min.js"></script>
<script type="text/javascript" src="../commonyui/yui2.7/datatable/datatable-min.js"></script>
<script type="text/javascript" src="../javascript/dateValidation.js"></script>  
<script type="text/javascript" src="../commonjs/calendar.js"></script>
<script type="text/javascript" src="../javascript/dms/fileManagement.js"></script> 
<script type="text/javascript" src="../javascript/dms/notificationGroup.js"></script> 
<script>
if(document.getElementById('id').value == '') {
 	document.getElementById('posSearch').style.display = '';
} else {
	document.getElementById('posSearch').style.display = 'none';
}
try {
	listPositions(positions,false);
} catch(e){}
 </script>
