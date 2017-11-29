<%--
  ~    eGov  SmartCity eGovernance suite aims to improve the internal efficiency,transparency,
  ~    accountability and the service delivery of the government  organizations.
  ~
  ~     Copyright (C) 2017  eGovernments Foundation
  ~
  ~     The updated version of eGov suite of products as by eGovernments Foundation
  ~     is available at http://www.egovernments.org
  ~
  ~     This program is free software: you can redistribute it and/or modify
  ~     it under the terms of the GNU General Public License as published by
  ~     the Free Software Foundation, either version 3 of the License, or
  ~     any later version.
  ~
  ~     This program is distributed in the hope that it will be useful,
  ~     but WITHOUT ANY WARRANTY; without even the implied warranty of
  ~     MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
  ~     GNU General Public License for more details.
  ~
  ~     You should have received a copy of the GNU General Public License
  ~     along with this program. If not, see http://www.gnu.org/licenses/ or
  ~     http://www.gnu.org/licenses/gpl.html .
  ~
  ~     In addition to the terms of the GPL license to be adhered to in using this
  ~     program, the following additional terms are to be complied with:
  ~
  ~         1) All versions of this program, verbatim or modified must carry this
  ~            Legal Notice.
  ~            Further, all user interfaces, including but not limited to citizen facing interfaces,
  ~            Urban Local Bodies interfaces, dashboards, mobile applications, of the program and any
  ~            derived works should carry eGovernments Foundation logo on the top right corner.
  ~
  ~            For the logo, please refer http://egovernments.org/html/logo/egov_logo.png.
  ~            For any further queries on attribution, including queries on brand guidelines,
  ~            please contact contact@egovernments.org
  ~
  ~         2) Any misrepresentation of the origin of the material is prohibited. It
  ~            is required that all modified versions of this material be marked in
  ~            reasonable ways as different from the original version.
  ~
  ~         3) This license does not grant any rights to any user of the program
  ~            with regards to rights under trademark law for use of the trade names
  ~            or trademarks of eGovernments Foundation.
  ~
  ~   In case of any queries, you can reach eGovernments Foundation at contact@egovernments.org.
  ~
  --%><html>
<%@ taglib prefix="s" uri="/WEB-INF/tags/struts-tags.tld"%>
<head>
<script type="text/javascript">
function goToParent(glcode,coaid) {
	window.opener.popupCallbackForAccountCode(glcode,coaid);
	window.close();
}
</script>
<style type="text/css">
.searchscroll {
	overflow-y: scroll;
	height: 390px;
	position: relative;
}
</style>

</head>
<body>
	<s:form action="common">
		<table width="100%" border="0" align="center" cellpadding="0"
			cellspacing="0">
			<tr>
				<td>
					<table width="95%" border="0" align="left" cellpadding="0"
						cellspacing="0">
						<tr>
							<th class="bluebgheadtd" width="30%"><s:property
									value="%{accountDetailTypeName}" />&nbsp;&nbsp;Code</th>
							<th class="bluebgheadtd" width="70%">Description</th>
						</tr>
					</table>
				</td>
			</tr>
			<tr>
				<td style="width: 345px; overflow-y: scroll;">
					<div style="overflow: auto; width: 370px; height: 380px;">
						<table width="100%" border="0" align="center" cellpadding="0"
							cellspacing="0">
							<s:iterator var="s" value="accountCodesList" status="status">
								<tr>
									<td align="left" width="40%">&nbsp;&nbsp;<a href="#"
										onclick="goToParent('<s:property value="%{glcode}" />','<s:property value="%{id}" />');"><s:property
												value="%{glcode}" /></a></td>
									<td align="left" width="60%">&nbsp;&nbsp;<s:property
											value="%{name}" /></td>
								</tr>
							</s:iterator>
							<s:if test="accountCodesList == null || accountCodesList.size==0">
								<tr>
									<td colspan="2"><div class="subheadsmallnew">No
											Records Found</div></td>
								</tr>
							</s:if>
						</table>
					</div>
				</td>
			</tr>
		</table>
		<br />
		<div class="buttonbottom">
			<input type="submit" value="Close"
				onclick="javascript:window.close()" class="button" />
		</div>
	</s:form>
</body>
</html>