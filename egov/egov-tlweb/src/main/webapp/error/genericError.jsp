#-------------------------------------------------------------------------------
# eGov suite of products aim to improve the internal efficiency,transparency, 
#     accountability and the service delivery of the government  organizations.
#  
#      Copyright (C) <2015>  eGovernments Foundation
#  
#      The updated version of eGov suite of products as by eGovernments Foundation 
#      is available at http://www.egovernments.org
#  
#      This program is free software: you can redistribute it and/or modify
#      it under the terms of the GNU General Public License as published by
#      the Free Software Foundation, either version 3 of the License, or
#      any later version.
#  
#      This program is distributed in the hope that it will be useful,
#      but WITHOUT ANY WARRANTY; without even the implied warranty of
#      MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
#      GNU General Public License for more details.
#  
#      You should have received a copy of the GNU General Public License
#      along with this program. If not, see http://www.gnu.org/licenses/ or 
#      http://www.gnu.org/licenses/gpl.html .
#  
#      In addition to the terms of the GPL license to be adhered to in using this
#      program, the following additional terms are to be complied with:
#  
#  	1) All versions of this program, verbatim or modified must carry this 
#  	   Legal Notice.
#  
#  	2) Any misrepresentation of the origin of the material is prohibited. It 
#  	   is required that all modified versions of this material be marked in 
#  	   reasonable ways as different from the original version.
#  
#  	3) This license does not grant any rights to any user of the program 
#  	   with regards to rights under trademark law for use of the trade names 
#  	   or trademarks of eGovernments Foundation.
#  
#    In case of any queries, you can reach eGovernments Foundation at contact@egovernments.org.
#-------------------------------------------------------------------------------
<%@ page language="java" pageEncoding="UTF-8"%>
<%@ page import="org.egov.infstr.client.filter.EGOVThreadLocals"%>
<%@ taglib prefix="html" uri="/WEB-INF/struts-html.tld"%>
<%@ taglib prefix="s" uri="/WEB-INF/struts-tags.tld"%>
<head>
	<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
	<title>eGov - Error Page</title>
	<link href="/egi/css/commonegov.css" rel="stylesheet" type="text/css" />
	<style>
.hiddenError {
	display: none;
}

.oopstext {
	font-family: Verdana, Geneva, sans-serif;
	font-size: 14px;
	font-weight: bold;
	color: #F00;
	margin-bottom: 15px;
}
.loadImg {
		position:absolute;top:15%;left:20%;display:none;
		background:white;  color:#444; font:bold 11px tohoma,arial,helvetica;
}
.loadspan {
	position: absolute;width:200px;margin-left:5px;margin-top:8px
}
</style>
</head>
<body>
	<div id="loadImg" class="loadImg"><img src="../images/loading.gif"  />&nbsp;<span class="loadspan">Please wait... Sending error report...</span></div>
	<div class="formmainbox">
		<div class="insidecontent">
			<div class="errorroundbox2">
				<div class="errortop2">
					<div></div>
				</div>
				<div class="errorcontent2">
					<table width="100%" border="0" cellspacing="0" cellpadding="0">
						<tr>
							<td width="59%">
								<div class="logouttext">
									<img src="/egi/images/error.png" width="128" height="128"
										alt="Error" />
									<div class="oopstext">
										Oops! Sorry your request cannot be processed!
									</div>
									<span class="bold">An error has occurred. Please try
										again or contact system administrator if the problem persists.</span>
								</div>
								<s:property value="exception.message" />
								<s:actionerror />
								<s:fielderror />
							</td>
						</tr>
						<tr>
							<td width="90%" class="hiddenError">
								<s:property value="exceptionStack" />
								<%session.setAttribute("message"+EGOVThreadLocals.getUserId(),request.getAttribute("exceptionStack"));%>
							</td>
						</tr>
						<tr id="msgBtn">
							<td width="90%" height="200px">
								<input class="button" type="button" value="Send Error Report" onclick="sentMail()"/>
							</td>
						</tr>
						<tr id="msgResp" class="hiddenError">
							<td width="90%" align="center" height="200px">
								<span id="resp" style="color:blue"></span>
							</td>
						</tr>
					</table>
				</div>
				<div class="errorbot2">
					<div></div>
				</div>
			</div>
		</div>
	</div>
	<div class="buttonbottom" style="position:absolute;bottom:0px;width:100%;clear:both;left:0px;right:0px">
		City Administration System Designed and Implemented by
		<a href="http://www.egovernments.org/">eGovernments Foundation</a> All
		Rights Reserved
	</div>

</body>
</html>
<script type="text/javascript" src="../commonyui/yui2.7/yahoo/yahoo-min.js"></script>
<script type="text/javascript" src="../commonyui/yui2.7/yahoo-dom-event/yahoo-dom-event.js"></script> 
<script type="text/javascript" src="../commonyui/yui2.7/event/event-min.js"></script> 
<script type="text/javascript" src="../commonyui/yui2.7/connection/connection-min.js"></script>

<script type="text/javascript"> 
	function sentMail(){
		var flag = confirm("Do you want to send Error Report ?"); 
		if(flag) {
			document.getElementById('msgBtn').style.display = 'none';
			document.getElementById('loadImg').style.display = 'block';
		    var url = "../common/mailSender!sendError.action?rnd="+Math.random();
			var callback = {
				success:function (oResponse) {
					document.getElementById('msgBtn').style.display = 'none';
					document.getElementById('msgResp').style.display = 'block';
					document.getElementById('loadImg').style.display = 'none';
					document.getElementById('resp').innerHTML = oResponse.responseText;
				}, 
				failure:function (oResponse) {
					document.getElementById('msgBtn').style.display = 'block';
					document.getElementById('msgResp').style.display = 'block';
					document.getElementById('loadImg').style.display = 'none';
					document.getElementById('resp').innerHTML = "Could not send mail, server is down..?";
				}, 
				argument:{
				}, 
				timeout:60000
			};
			YAHOO.util.Connect.asyncRequest("GET", url, callback);
		} else {
		    return false;   
		} 
	 } 
</script>
