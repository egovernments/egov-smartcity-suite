<%@ include file="/includes/taglibs.jsp"%>

<%@ page language="java"%>
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title></title>
</head>
<body>
			<center><h3><div style="color: red">
				<s:actionerror />
				<s:fielderror />
			</div>
			<div style="color: green">
				<s:actionmessage />
			</div></h3>
			<s:token />
	
		<br /><br /> <input type="button" id="Close" value="Close"
			onclick="javascript:window.close()" class="button" /></center>
</body>
</html>