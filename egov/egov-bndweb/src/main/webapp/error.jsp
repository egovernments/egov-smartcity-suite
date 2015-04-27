<%@ page isErrorPage="true"%>
<html>
	<head>
		<link rel="stylesheet" type="text/css" media="all" href="/egi/css/egov.css" />
		<title>Error</title>
	</head>
	<body>
		<center>
		<table class="tableStyle">
			<tr>
				<td>
					<div id=main>
						<div id=m2>
							<div id=m3>
								<div align="center">
									<table class="tableStyle" border=1 width="754" summary>
										<tbody>
											<tr>
												<td class="tableStyle" align="center" height="27"
													width="772">
													<p align="center">
														An error has occurred ! Please try again or contact the
														administrator if the problem persists.
													</p>
												</td>
											</tr>
											<% String msg = (String)request.getAttribute("MESSAGE");%>
											<tr>
												<td class="tableStyle" width="728" height="10">
											<tr>
												<td class="tableheader" align="center" width="728"
													height="30">
													<p align="center">
														<b><font color="blue" size="1px"><%= msg != null ? msg : exception.getMessage() %></font> </b>
													</p>
												</td>
											</tr>
										</tbody>
									</table>
								</div>
							</div>
						</div>
					</div>
				</td>
			</tr>
		</table>
		</center>
	</body>
</html>