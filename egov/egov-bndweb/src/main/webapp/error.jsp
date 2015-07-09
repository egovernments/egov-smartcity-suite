#-------------------------------------------------------------------------------
# eGov suite of products aim to improve the internal efficiency,transparency,
# accountability and the service delivery of the government  organizations.
# 
# Copyright (C) <2015>  eGovernments Foundation
# 
# The updated version of eGov suite of products as by eGovernments Foundation
# is available at http://www.egovernments.org
# 
# This program is free software: you can redistribute it and/or modify
# it under the terms of the GNU General Public License as published by
# the Free Software Foundation, either version 3 of the License, or
# any later version.
# 
# This program is distributed in the hope that it will be useful,
# but WITHOUT ANY WARRANTY; without even the implied warranty of
# MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
# GNU General Public License for more details.
# 
# You should have received a copy of the GNU General Public License
# along with this program. If not, see http://www.gnu.org/licenses/ or
# http://www.gnu.org/licenses/gpl.html .
# 
# In addition to the terms of the GPL license to be adhered to in using this
# program, the following additional terms are to be complied with:
# 
# 1) All versions of this program, verbatim or modified must carry this
#    Legal Notice.
# 
# 2) Any misrepresentation of the origin of the material is prohibited. It
#    is required that all modified versions of this material be marked in
#    reasonable ways as different from the original version.
# 
# 3) This license does not grant any rights to any user of the program
#    with regards to rights under trademark law for use of the trade names
#    or trademarks of eGovernments Foundation.
# 
# In case of any queries, you can reach eGovernments Foundation at contact@egovernments.org.
#-------------------------------------------------------------------------------
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
