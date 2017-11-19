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
  --%>

<%@ page language="java" pageEncoding="UTF-8"%>
<%@ include file="/includes/taglibs.jsp" %>

<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />

<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN">
<html>

<head>
<title><s:text name="Wardwise.billGen.status"></s:text></title>
<script type="text/javascript">
</script>
</head>

<body>
	<div class="formmainbox">
		<div class="headingbg">
			<s:text name="parwise.billGen.status" />
		</div>
		<s:form theme="simple" action="billGeneration" name="BillGenerationForm">
			<s:if test="%{reportInfos.isEmpty()}">
				<span class="bold"  style="font-size: 14px"> <br> There are no bills generated for current financial year. </span>
			</s:if>
			<s:else>
				<table width="100%" border="0" align="center" cellpadding="0"
					cellspacing="0" class="tablebottom">

					<tr>
						<th class="bluebgheadtd" width="25%"/>
						<th class="bluebgheadtd" width="15%">
							<s:text name="partNo" />
						</th>
						<th class="bluebgheadtd" width="15%" align="center">
							<s:text name="noOfProps" />
						</th>
						<th class="bluebgheadtd" width="15%" align="center">
							<s:text name="noof.bills.generated" />
						</th>
						<th class="bluebgheadtd" width="25%"/>
					</tr>
						<s:iterator value="(reportInfos.size).{#this}" status="reportInfo">
						<tr>
							<td class="blueborderfortd" width="25%"></td>
							<td class="blueborderfortd" style="border-left: 1px solid #E9E9E9">
								<div align="center">
									<s:property value="%{reportInfos[#reportInfo.index].partNo}" />
								</div>
							</td>
							<td class="blueborderfortd">
								<div align="center">
									<s:property value="%{reportInfos[#reportInfo.index].totalNoProps}" />
								</div>
							</td>
							<td class="blueborderfortd">
								<div align="center">
									<s:property value="%{reportInfos[#reportInfo.index].totalGenBills}" />
								</div>
							</td>
							<td class="blueborderfortd" width="25%"></td>
						</tr>
					</s:iterator>
				</table>
			</s:else>
			<div class="buttonbottom" align="center">
				<input type="button" name="button2" id="button2" value="Close" class="button" onclick="return confirmClose();" />
			</div>
		</s:form>
	</div>
</body>

</html>
