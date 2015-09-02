<%@ include file="/includes/taglibs.jsp"%>
<html>
	<head>
		<title><s:text name="license.search" /></title>
		<link href="../../css/license/searchTrade.css" rel="stylesheet" type="text/css"></link>
		<script type="text/javascript" src="../../javascript/license/searchTrade.js"></script>
	</head>
	<body onload="init()">
		<table align="center" width="100%">
			<tbody>
				<tr>
					<td>
						<div align="center">
							<center>
								<div class="formmainbox">
									<div class="headingbg">
										<s:text name="license.search" />
									</div>
									<s:push value="model">
										<s:form action="searchTrade" theme="simple" name="searchForm">
											<s:hidden name="actionName" value="search" />
											<div id="error" style="color: #FF0000">
											</div>
											<table border="0" cellpadding="0" cellspacing="0" width="100%" align="center">
												<tbody>
													<%@ include file='searchTrade-form.jsp'%>
												</tbody>
											</table>
											<div class="buttonbottom">
												<s:submit name="button32" onclick="return validateForm()" cssClass="buttonsubmit" id="button32" method="search" value="Search" />
												<s:reset name="button" cssClass="button" id="button" value="Reset" />
												<input name="button2" type="button" class="button" id="button" onclick="window.close()" value="Close" />
											</div>
											<%@ include file='searchTrade-result.jsp'%>
										</s:form>
									</s:push>
								</div>
							</center>
						</div>
					</td>
				</tr>
			</tbody>
		</table>
	</body>
</html>