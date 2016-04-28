<%@ page language="java" pageEncoding="UTF-8"%>
<%@ taglib prefix="s" uri="/WEB-INF/taglibs/struts-tags.tld"%>
<head>
	<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
	<title>eGov - Error Page</title>
	<!-- <link href="/egworks/resources/erp2/css/commonegov.css" rel="stylesheet" type="text/css" /> -->
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
	<!-- <div id="loadImg" class="loadImg"><img src="/egworks/resources/erp2/images/loading.gif"  />&nbsp;<span class="loadspan">Please wait... Sending error report...</span></div> -->
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
									<img src="/egworks/resources/erp2/images/error.png" width="128" height="128"
										alt="Error" />
									<div class="oopstext">
										Oops! Sorry your request cannot be processed!
									</div>
									<span class="bold">An error has occurred. Please try
										again or contact system administrator if the problem persists.</span>
								</div>
								<s:actionerror />
								<s:fielderror />
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

</body>
</html>