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
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title><s:text name="tneb.download.text.title" /></title>
</head>
<script type="text/javascript" src="/EGF/resources/javascript/helper.js?rnd=${app_release_no}"></script>
<body>
	<s:actionmessage />
	<s:actionerror />
	<s:form name="downloadTextForm" action="bankAdviceReport"
		theme="simple">
		<s:token />
		<div class="formmainbox"></div>
		<div class="formheading" />
		<div class="subheadnew">
			<s:text name="tneb.download.text.title" />
		</div>
		<br />
		<table align="center" width="100%" cellspacing="0">
			<tr>
				<td width="40%"></td>
				<td width="10%"><s:text name="tneb.month" /><span
					class="mandatory">*</span> :</td>
				<td width="10%"><s:select name="month" id="month"
						list="%{fullNameMonthMap}" headerKey=""
						headerValue="----Choose----" value="%{month}" /></td>
				<td width="40%"></td>
			</tr>
			<tr>
				<td width="40%"></td>
				<td width="10%"><s:text name="tneb.financialYear" /><span
					class="mandatory">*</span> :</td>
				<td width="10%"><s:select name="financialYearId"
						id="financialYearId" list="%{dropdownData.financialYearsList}"
						listKey="id" listValue="finYearRange" headerKey=""
						headerValue="----Choose----" value="%{financialYearId}" /></td>
				<td width="40%"></td>
			</tr>

		</table>
		<s:hidden name="mode" id="mode" value="%{mode}" />
		<div class="buttonbottom" align="center">
			<table border="0px" cellpadding="0" cellspacing="10"
				class="buttonbottom">
				<tr align="center">
					<td style="padding: 0px"><s:submit method="TNEBsearch"
							cssClass="buttonsubmit" value="Submit"
							onclick="return validate();" /></td>
					<td style="padding: 0px"><input type="button" value="Close"
						onclick="javascript:window.close();" class="button" /></td>
				</tr>
			</table>
		</div>
		<s:if test="%{instrumentHeaderList.size!=0}">
			<table width="100%" border="0" align="center" cellpadding="0"
				cellspacing="0">
				<tr>
					<td style="text-align: center" align="center"><s:property
							value="%{heading}" /></td>
				</tr>
			</table>
			<table width="100%" border="0" align="center" cellpadding="0"
				cellspacing="0" class="tablebottom">
				<tr>
					<th class="bluebgheadtd" style="width: 2%; text-align: center"
						align="center">Sl No.</th>
					<th class="bluebgheadtd" style="width: 4%; text-align: center"
						align="center">RTGS Number</th>
					<th class="bluebgheadtd" style="width: 8%; text-align: center"
						align="center">Download Link</th>
				</tr>
				<c:set var="trclass" value="greybox" />
				<s:iterator var="instrumentHeader" value="instrumentHeaderList"
					status="f">
					<tr>
						<td class="<c:out value="${trclass}"/>" style="text-align: center"
							align="center" width="10%"><s:property value="#f.index+1" /></td>
						<td class="<c:out value="${trclass}"/>" style="text-align: center"
							align="center" width="15%"><s:property
								value="transactionNumber" /></td>
						<td class="<c:out value="${trclass}"/>" style="text-align: center"
							align="center" width="15%"><a href="#"
							onclick="urlLoad('<s:property value="%{id}" />',
							'<s:property value="%{bankAccountId.id}" />',
							'<s:property value="%{bankAccountId.bankbranch.id}" />',
							'<s:property value="%{bankAccountId.bankbranch.bank.id}" />');"
							id="sourceLink" /> Download TXT </a></td>
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
		<s:if test="%{mode.equalsIgnoreCase('search')}">
			<s:if test="%{instrumentHeaderList.size==0}">
				<div id="msgdiv" style="display: block">
					<table align="center" class="tablebottom" width="80%">
						<tr>
							<th class="bluebgheadtd" colspan="7">No Records Found
							</td>
						</tr>
					</table>
				</div>
			</s:if>
		</s:if>
	</s:form>
	<script>
		
		function validate() {			
			var monthValue = document.getElementById('month').value;
			var finYearId = document.getElementById('financialYearId').value;
			
			if (monthValue == null || monthValue.trim().length == 0 || monthValue == undefined) {
				bootbox.alert('Please select month');				
				document.getElementById('month').focus();
				return false;
			}
			
			if (finYearId == null || finYearId.trim().length == 0 || finYearId == undefined) {
				bootbox.alert('Please select financial year');				
				document.getElementById('financialYearId').focus();
				return false;
			}
			return true;
		}

		function urlLoad(instrumentnumber,bankaccount,bankbranch,bank){
			
			 var url="${pageContext.request.contextPath}/report/bankAdviceReport!exportText.action?bank.id="+
				bank+"&bankbranch.id="+bankbranch+"&bankaccount.id="+bankaccount+"&instrumentnumber.id="+instrumentnumber;
			  window.open(url,'','height=650,width=980,scrollbars=yes,left=0,top=0,status=yes');
			}
		
	</script>
</body>
</html>
