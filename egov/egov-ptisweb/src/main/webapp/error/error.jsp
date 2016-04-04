<%@ page language="java" pageEncoding="UTF-8" isErrorPage="true"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<html>
<head>
	<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
	<title>eGov - Application Error</title>
	<link href="<c:url value='/resources/global/css/bootstrap/bootstrap.css' context='/egi'/>" rel="stylesheet" type="text/css" />
	<link href="<c:url value='/resources/global/css/egov/custom.css' context='/egi'/>" rel="stylesheet" type="text/css" />
	</head>
	<body>
	<div class="formmainbox">
		<div class="insidecontent">
			<div class="errorroundbox2">
				<div class="errortop2">
					<div></div>
				</div>
				<div class="errorcontent2">
					<table border="0" cellspacing="0" cellpadding="0" style="position:relative;top:50px;left:50px;">
						 <tr>
							<td width="10%">
							    <img width="100px" alt="Error" src="/egi/resources/erp2/images/error.png">
							</td>
                            <td>
							    <span class="bold">Server has encountered a problem. Please try again or contact system administrator if the problem persists.</span>
							</td>
						</tr>
					</table>
				</div>				
			</div>
		</div>
	</div>
</body>
</html>