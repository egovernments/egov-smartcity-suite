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
		<title><s:text name="chngLocScrRes"></s:text></title>
		<script type="text/javascript">
			function populateCategoryList(usage, struct){
				populatecategoryList({
					revisedRate: document.getElementById("revisedRate").value, usageFactor: usage, structFactor: struct  
				});						
			}		
			function onSubmit(obj){
				document.forms[0].action=obj;
				document.forms[0].submit;
			   return true;
			}
				
		</script>
		<style type="text/css">
			td {
				width: 100px
			}
		</style>
	</head>
	<body>
		<div align="left">
			<s:actionerror />
		</div>
		<s:form name="changeStreetRateEditForm" theme="simple" method="post">
			<div class="formmainbox">
				<div class="formheading"></div>
				<div class="headingbg">
					<s:text name="ChBaseRateAreaHeader" />
				</div>
				<table width="100%" border="0" class="tablebottom">
					<tr>
						<th class="bluebgheadtd">
							<s:text name="useFact"></s:text>
						</th>
						<th class="bluebgheadtd">
							<s:text name="structFact"></s:text>
						</th>
						<th class="bluebgheadtd">
							<s:text name="currRate"></s:text>
						</th>
						<th class="bluebgheadtd">
							<s:text name="currLocFact"></s:text>
						</th>
						<th class="bluebgheadtd">
							<s:text name="revRate"></s:text>
						</th>
						<th class="bluebgheadtd">
							<s:text name="revLocation"></s:text>
						</th>
					</tr>
					<tr>
						<td class="blueborderfortd">
							<s:property value="%{usageFactor}" />
							<s:hidden name="usageFactor"></s:hidden>
						</td>
						<td class="blueborderfortd">
							<s:property value="%{structFactor}" />
							<s:hidden name="structFactor"></s:hidden>
						</td>
						<td class="blueborderfortd" width="100px">
							<s:property value="%{currentRate}" />
							<s:hidden name="currentRate"></s:hidden>
						</td>
						<td class="blueborderfortd">
							<s:property value="%{currLocFactor}" />
							<s:hidden name="currLocFactor"></s:hidden>
						</td>
						<td class="blueborderfortd">
							<s:textfield id="revisedRate" name="revisedRate"
								value="%{revisedRate}" onblur="trim(this,this.value)"
								onchange="populateCategoryList('%{usageFactor}', '%{structFactor}')"></s:textfield>
						</td>
						<egov:ajaxdropdown id="categoryList" fields="['Text','Value']"
							dropdownId="categoryList"
							url="common/ajaxCommon-categoryByRateUsageAndStructClass.action" />
						<td class="blueborderfortd">
							<s:select id="categoryList" list="dropdownData.categoryList" 
								name="revisedLocFactor" headerKey="-1"
								headerValue="%{getText('default.select')}" listKey="id"
								listValue="categoryName">
							</s:select>
						</td>
						<s:hidden name="areaId"></s:hidden>
					</tr>
				</table>
				<div class="buttonsearch" align="center">

					<s:hidden name="saveAction" value="saveData"></s:hidden>
					<s:submit value="Save" onclick="return onSubmit('changeStreetRate-saveData.action');"  cssClass="button">
					</s:submit>
					<s:submit value="Search Results" cssClass="button"
						 onclick="return onSubmit('changeStreetRate-showSearchResults.action');"  ></s:submit>
					<input type="button" value="Close" class="button"
						onClick="window.close()" />
				</div>


			</div>
		</s:form>
	</body>
</html>
