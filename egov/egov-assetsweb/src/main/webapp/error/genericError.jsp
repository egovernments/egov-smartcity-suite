<%@ page language="java" pageEncoding="UTF-8"%>
<%@ page import="org.egov.infstr.client.filter.EGOVThreadLocals"%>
<%@ taglib prefix="s" uri="/WEB-INF/struts-tags.tld"%>
<html>
<head>
	<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
	<title>eGov - Application Error</title>
	<link href="/egi/css/commonegov.css" rel="stylesheet" type="text/css" />
	<style>
	.hiddenError {
		display: none;
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
									<img src="/egi/images/error.png" alt="Error" />
									<span class="bold">An error has occurred. Please try again or contact system administrator if the problem persists.</span>
								</div>
								<s:actionerror />
							</td>
						</tr>
						<tr>
							<td width="90%" class="hiddenError">
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
		eGov Urban Portal Designed and Implemented by <a href="http://www.egovernments.org/">eGovernments Foundation</a> &copy; All Rights Reserved.
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