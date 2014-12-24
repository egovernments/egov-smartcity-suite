<%@ taglib prefix="s" uri="/struts-tags"%>
<%@ include file="/includes/taglibs.jsp" %>

		<s:if test="hasFieldErrors() || hasActionMessages() || hasActionErrors()">
			<div id="mask" class="loading" style="display:block"></div>
			<div id="errorconsole" >
			<s:if test="hasActionMessages() && !hasFieldErrors() && !hasActionErrors()">
				<table border="0">
					<tr>
						<td>
							<img src="../images/success.gif" alt="Sucessful" title="File Save Successful" border="0" />
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
		<div class="info cursor" id='fileCmmtHstry'>
			<img src="../images/cancel.jpg" onclick="hideElement('fileCmmtHstry')" class="infocls"/><br/>									
			<div class="infodiv" id="infodiv"></div>
		</div>
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
<script type="text/javascript" src="../commonyui/yui2.7/calendar/calendar-min.js"></script> 
<script type="text/javascript" src="../commonyui/yui2.7/connection/connection-min.js"></script>
<script type="text/javascript" src="../commonyui/yui2.7/datasource/datasource-min.js"></script>
<script type="text/javascript" src="../commonyui/yui2.7/dragdrop/dragdrop-min.js"></script>	
<script type="text/javascript" src="../commonyui/yui2.7/datatable/datatable-min.js"></script>
<script type="text/javascript" src="../javascript/dateValidation.js"></script>  
<script type="text/javascript" src="../commonjs/calendar.js"></script>
<script type="text/javascript" src="../javascript/helper.js"></script>
<script type="text/javascript" src="../javascript/dms/fileManagement.js"></script> 

<s:if test="%{id !=null}">
<script>
setReadOnly();
if (document.forms[0]) {
	document.forms[0].onsubmit = updateComment;
}
</script>
</s:if>
