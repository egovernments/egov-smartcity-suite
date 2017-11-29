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
<%@ page language="java"%>


<html>
<head>
<title><s:text name="Grants Search" /></title>

</head>

<body onload="init()">
	<div class="formmainbox">
		<div class="subheadnew">
			<s:text name="Search Grants" />
		</div>

		<br />
		<br />

		<s:form name="searchRevenueReportForm" action="searchRevenueReport"
			theme="simple">
			<s:hidden name="mode" id="mode" value="%{mode}"></s:hidden>
			<table width="100%" cellpadding="0" cellspacing="0" border="0">
				<tr align="center">
					<td class="greybox" width="10%" align="center">Financial Year<span
						class="greybox"></span><span class="mandatory">*</span></td>
					<td class="greybox" align="center"><s:select name="finYearId"
							id="finYearId" list="dropdownData.finanYearList" listKey="id"
							listValue="finYearRange" headerKey="-1"
							headerValue="----Choose----" /></td>
					<td class="greybox" width="10%" align="center">Grant Type<span
						class="greybox"></span></td>
					<td class="greybox" align="center"><s:select
							name="grantTypeStr" id="grantTypeStr"
							list="dropdownData.grtTypeList" headerKey="-1"
							headerValue="----Choose----" value="%{grantTypeStr}" /></td>
					<td class="greybox" width="10%" align="center">Department<span
						class="greybox"></span></td>
					<td class="greybox" align="center"><s:select name="deptId"
							id="deptId" list="dropdownData.deptList" listKey="id"
							listValue="name" headerKey="-1" headerValue="----Choose----" />
					</td>
				</tr>
			</table>

			<br />
			<br />

			<div class="buttonbottom">
				<s:submit method="search" value="Search"
					onclick="return validateMandatoryFields();" cssClass="buttonsubmit" />
				<input type="button" value="Close"
					onclick="javascript:window.close()" class="button" />
			</div>

			<s:if test="%{grantsList.size!=0}">
				<table width="100%" border="0" align="center" cellpadding="0"
					cellspacing="0" class="tablebottom">

					<tr>

						<th class="bluebgheadtd" style="width: 2%; text-align: center"
							align="center">Sl No.</th>
						<th class="bluebgheadtd" style="width: 4%; text-align: center"
							align="center">Financial Year</th>
						<th class="bluebgheadtd" style="width: 8%; text-align: center"
							align="center">Department</th>
						<th class="bluebgheadtd" style="width: 8%; text-align: center"
							align="center">Grant Type</th>
					</tr>
					<c:set var="trclass" value="greybox" />
					<s:iterator var="fa" value="grantsList" status="f">
						<tr>

							<td class="<c:out value="${trclass}"/>"
								style="text-align: center" align="center"><s:property
									value="#f.index+1" /></td>
							<td class="<c:out value="${trclass}"/>"
								style="text-align: center" align="center"><a href="#"
								onclick="urlLoad('<s:property value="financialYear.id"/>','<s:property value="department.id"/>','<s:property value="grantType"/>','<s:property value="period"/>');"
								id="sourceLink" /> <s:property
									value="financialYear.finYearRange" /> </a></td>
							<td class="<c:out value="${trclass}"/>"
								style="text-align: center" align="center"><s:property
									value="department.deptName" /></td>
							<td class="<c:out value="${trclass}"/>"
								style="text-align: center" align="center"><s:property
									value="grantType" /></td>
							<c:choose>
								<c:when test="${trclass=='greybox'}">
									<c:set var="trclass" value="bluebox" />
								</c:when>
								<c:when test="${trclass=='bluebox'}">
									<c:set var="trclass" value="greybox" />
								</c:when>
							</c:choose>
						</tr>
					</s:iterator>

				</table>
			</s:if>
			<s:if test="%{grantsList.size==0}">
				<div id="msgdiv" style="display: block">
					<table align="center" class="tablebottom" width="80%">
						<tr>
							<th class="bluebgheadtd" colspan="7">No Records Found
							</td>
						</tr>
					</table>
				</div>
			</s:if>

		</s:form>
		<script>
	function urlLoad(fyID, depID, grType, prd){
		<s:if test="%{mode =='edit'}">
			if (grType == 'Central Finance Commission')
				url = "centralFC!beforeModify.action?model.financialYear.id="+fyID+"&model.department.id="+depID+"&model.grantType="+grType+"&mode=edit";
			if (grType == 'Entertainment Tax')
				url = "entertainmentTax!beforeModify.action?model.financialYear.id="+fyID+"&model.department.id="+depID+"&model.grantType="+grType+"&mode=edit";
			if (grType == 'State Finance Commission')
				url = "stateFC!beforeModify.action?model.financialYear.id="+fyID+"&model.department.id="+depID+"&model.grantType="+grType+"&mode=edit";
			if (grType == 'Stamp Duty')
				url = "stampDuty!beforeModify.action?model.financialYear.id="+fyID+"&model.department.id="+depID+"&model.grantType="+grType+"&mode=edit";
		</s:if>
		<s:if test="%{mode =='view'}">
			if (grType == 'Central Finance Commission')
				url = "centralFC!beforeModify.action?model.financialYear.id="+fyID+"&model.department.id="+depID+"&model.grantType="+grType+"&mode=view";
			if (grType == 'Entertainment Tax')
				url = "entertainmentTax!beforeModify.action?model.financialYear.id="+fyID+"&model.department.id="+depID+"&model.grantType="+grType+"&mode=view";
			if (grType == 'State Finance Commission')
				url = "stateFC!beforeModify.action?model.financialYear.id="+fyID+"&model.department.id="+depID+"&model.grantType="+grType+"&mode=view";
			if (grType == 'Stamp Duty')
				url = "stampDuty!beforeModify.action?model.financialYear.id="+fyID+"&model.department.id="+depID+"&model.grantType="+grType+"&mode=view";
		</s:if>
		window.open(url,'grantsEdit','resizable=yes,scrollbars=yes,left=300,top=40, width=900, height=700');
	}
	function validateMandatoryFields(){
		var finId=document.getElementById('finYearId').value;
		if(finId==-1){
			bootbox.alert("Select Financial Year");
			return false;
		}
		return true;
	}
	function init(){
	//bootbox.alert(document.getElementById("mode").value);
	}
	</script>
</body>
</html>
