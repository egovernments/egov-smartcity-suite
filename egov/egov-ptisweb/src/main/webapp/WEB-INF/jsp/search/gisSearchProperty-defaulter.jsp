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
<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
<html>
	<head>
		<script type="text/javascript">
		function populateWard(){
		populatewardId({zoneId:document.getElementById("zoneId").value});
		document.getElementById("areaId").options.length = 0;
		document.getElementById("areaId").value="-1";
	}
	function populateArea(){
		populateareaId({wardId:document.getElementById("wardId").value});
	}
	function zoomward(obj)
	{
	var selWard = obj.options[obj.selectedIndex].value;
	var mgsession='<s:property value="%{SESSION}"/>';
	var gisVer='<s:property value="%{gisVersion}"/>';
	var gisCity='<s:property value="%{gisCity}"/>';
	var gisUrl=gisVer+gisCity;
	
	parent.parent.formFrame.Submit(gisUrl+"/ZoomToWardSelect.jsp?mgSession="+mgsession+"&DomainName="+gisCity+"&wardNum="+selWard,null,"scriptFrame");
	}
</script>
	</head>
	<body>
		<div class="formmainbox">
			<s:if test="%{hasErrors()}">
				<div align="left">
					<s:actionerror />
				</div>
			</s:if>
			<table width="320px" border="0">
				<s:form action="gisSearchProperty" name="srchform" theme="simple">
					<div class="headingbg">
						<center>
					<tr>
						<td class="headingbg"
							style="font-weight: bold; font-size: 13px; text-align: center;">
							<s:text name="gissrchbydefaulter" />
						</td>
					</tr>

					</center>
					</div>
					<tr>

						<td class="greybox">
							<s:text name="Zone" />
							<span class="mandatory1">*</span> :
						</td>
						<td class="greybox">
							<s:select headerKey="-1"
								headerValue="%{getText('default.select')}" name="zoneId"
								id="zoneId" listKey="key" listValue="value" list="ZoneBndryMap"
								cssClass="selectnew" onchange="return populateWard();"
								value="%{zoneId}" />
						</td>
					</tr>
					<tr>

						<egov:ajaxdropdown id="wardId" fields="['Text','Value']"
							dropdownId="wardId" url="common/ajaxCommon!wardByZone.action" />
						<td class="bluebox">
							<s:text name="Ward" />
							:
						</td>
						<td class="bluebox">
							<s:select name="wardId" id="wardId" list="dropdownData.wardList"
								listKey="id" listValue="name" headerKey="-1"
								headerValue="%{getText('default.select')}"
								onchange="return populateArea(),zoomward(this);;"
								value="%{wardId}" />
						</td>
						</td>
					</tr>
					<tr>

						<egov:ajaxdropdown id="areaId" fields="['Text','Value']"
							dropdownId="areaId" url="common/ajaxCommon!areaByWard.action" />
						<td class="greybox">
							<s:text name="Area" />
							:
						</td>
						<td class="greybox">
							<s:select name="areaId" id="areaId" list="dropdownData.areaList"
								listKey="id" listValue="name" headerKey="-1"
								headerValue="%{getText('default.select')}" value="%{areaId}" />
						</td>
					</tr>
					<tr>
						<td class="bluebox">

							<s:text name="PropertyType" />

						</td>
						<td class="bluebox">
							<s:select name="propTypeId" id="propTypeId"
								list="dropdownData.PropType" listKey="id" listValue="type"
								headerKey="-1" headerValue="----Choose----"
								value="%{propTypeId}" />
						</td>
					</tr>
					<tr>
						<td class="greybox">

							<s:text name="fromamt" />
							<span class="mandatory1">*</span> :

						</td>
						<td class="greybox">
							<s:textfield name="defaulterFromAmt" onblur="validNumber(this);">
							</s:textfield>
						</td>
					</tr>
					<tr>
						<td class="bluebox">

							<s:text name="toamt" />
							<span class="mandatory1">*</span> :

						</td>
						<td class="bluebox">
							<s:textfield name="defaulterToAmt" onblur="validNumber(this);">
							</s:textfield>
						</td>
					</tr>
					<tr>
						<td>
							<s:hidden name="SESSION" value="%{SESSION}"></s:hidden>
							<s:hidden id="mode" name="mode" value="defaulter"></s:hidden>
							<s:submit name="search" value="Search" cssClass="button"
								method="srchByDefaulter"></s:submit>
						</td>
					</tr>

				</s:form>
			</table>
	</body>
</html>
