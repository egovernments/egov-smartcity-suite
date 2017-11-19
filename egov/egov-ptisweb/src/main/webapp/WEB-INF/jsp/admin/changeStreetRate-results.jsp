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

<%@ include file="/includes/taglibs.jsp"%>

<html>
	<head>
		<title><s:text name="chngLocScrRes"/></title>
		<script type="text/javascript" src="/javascript/calendar.js">	
		</script>
	</head>
	<body>
		<div class="formmainbox">
			<table width="100%" border="0" class="tablebottom">
				<s:form name="searchResultsForm" theme="simple" method="post">
					<div class="headingsmallbgnew">
							<s:text name="scrhCriteria"></s:text>
						<span class="mandatory"> By Area  </span> /
							<s:text name="totRec"></s:text>
						<span class="mandatory"><s:property
								value="%{readOnlyFields.size}" /> <s:text name="matchRecFound" /></span>
						<div class="searchvalue1">
							<s:text name="scrhVal"></s:text>
							<s:property value="%{searchValue}" />
						</div>
					</div>
				</s:form>
				<s:if test="%{readOnlyFields != null && readOnlyFields.size > 0}">
					<tr>
						<display:table name="readOnlyFields" id="linksTables"
							pagesize="10" export="false" requestURI="" class="tablebottom"
							style="width:100%" uid="currentRowObject">
							<display:column property="usageFactor" title="Usage Factor"
								headerClass="bluebgheadtd" class="blueborderfortd"
								style="text-align:center" />
							<display:column property="structFactor" title="Structural Factor"
								headerClass="bluebgheadtd" class="blueborderfortd"
								style="text-align:center" />
							<display:column property="currentRate" title="Current Rate"
								headerClass="bluebgheadtd" class="blueborderfortd"
								style="text-align:center" />
							<display:column property="currLocFactor"
								title="Current Location Factor" headerClass="bluebgheadtd"
								class="blueborderfortd" style="text-align:center" />
							<display:column title="" headerClass="bluebgheadtd"
								class="blueborderfortd" style="text-align:center">
								<a
									href='../admin/changeStreetRate-editPage.action?usageFactor=
											<s:property value="%{#attr.currentRowObject.usageFactor}"/>&structFactor=
											<s:property value="%{#attr.currentRowObject.structFactor}"/>&currentRate=
											<s:property value="%{#attr.currentRowObject.currentRate}"/>&currLocFactor=
											<s:property value="%{#attr.currentRowObject.currLocFactor}"/>&areaId=
											<s:property value="%{areaId}"/>'>
									edit </a>
							</display:column>
							<display:setProperty name="paging.banner.item" value="Record" />
							<display:setProperty name="paging.banner.items_name"
								value="Records" />
						</display:table>
					</tr>
				</s:if>
				<s:else>
					<tr>
						<td align="center">
							<span class="mandatory"><s:text name="noRecFound"></s:text> </span>
						</td>
					</tr>
				</s:else>
					<div class="buttonsearch" align="center">
							<input value="Search Again" class="buttonsubmit" onclick="window.location='../admin/changeStreetRate-searchForm.action';"/>
							<input type="button" value="Close" class="button" onClick="return confirmClose()"/>
					</div>
			</table>
		</div>
	</body>
</html>
