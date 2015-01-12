<%@ page language="java" pageEncoding="UTF-8" isErrorPage="true"%>
<%@ taglib prefix="s" uri="/WEB-INF/struts-tags.tld"%>
<html>
<head>
	<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
	<title>eGov - Application Error</title>
	<link href="/egi/css/commonegov.css" rel="stylesheet" type="text/css" />
	</head>
	<body>
	<div class="formmainbox">
		<div class="insidecontent">
			<div class="errorroundbox2">
				<div class="errortop2">
					<div>
						<s:actionerror/>
					</div>
				</div>
				<div class="errorcontent2">
				</div>				
			</div>
		</div>
	</div>
	<div class="buttonbottom" style="position:absolute;bottom:0px;width:100%;clear:both;left:0px;right:0px">
		eGov Urban Portal Designed and Implemented by <a href="http://www.egovernments.org/">eGovernments Foundation</a> &copy; All Rights Reserved.
	</div>
</body>
</html>