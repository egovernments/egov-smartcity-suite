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
<title><s:text name="budgetReAppropriation.search" /></title>
</head>
<body>
	<s:form action="budgetReAppropriation" theme="simple">
		<jsp:include page="budgetHeader.jsp" />
		<span class="mandatory"> <s:actionerror /> <s:fielderror /> <s:actionmessage />
		</span>
		<div class="formmainbox">
			<div class="subheadnew">
				<s:text name="budgetReAppropriation.search" />
			</div>
			<table align="center" width="80%" cellpadding="0" cellspacing="0">
				<jsp:include page="budgetReAppropriation-filter.jsp" />
				<tr>
					<td class="greybox">&nbsp;</td>
					<td class="greybox"><s:text name="budgetdetail.budgetGroup" /></td>
					<td class="greybox"><s:select
							list="dropdownData.budgetGroupList" listKey="id" listValue="name"
							name="budgetDetail.budgetGroup.id" headerKey="0"
							headerValue="--- Select ---" value="budgetGroup.id"
							id="budgetReAppropriation_budgetGroup"></s:select></td>

					<td class="greybox"><s:text name="budget.reappropriation.type" /></td>
					<td class="greybox"><s:select
							list="#{'B':'Both','A':'Addition','R':'Reduction'}" name="type"
							id="type"></s:select></td>
				</tr>
				<tr />
				<tr>
					<td>&nbsp;</td>
				</tr>
			</table>
			<div class="buttonbottom"
				style="padding-bottom: 10px; position: relative">
				<s:submit method="search" value="Search" cssClass="buttonsubmit"
					onclick="return checkMandatory()" />
				<input type="submit" value="Close"
					onclick="javascript:window.close()" class="button" />
			</div>
			<br />
			<div id="listid" style="display: none">
				<table width="100%" border="0" align="center" cellpadding="0"
					cellspacing="0" class="tablebottom">
					<tr>
						<th class="bluebgheadtd">Sl.No.</th>
						<s:if
							test="%{shouldShowHeaderField('fund')|| shouldShowGridField('fund')}">
							<th class="bluebgheadtd"><s:text name="fund" /></th>
						</s:if>
						<s:if
							test="%{shouldShowHeaderField('executingDepartment')|| shouldShowGridField('executingDepartment')}">
							<th class="bluebgheadtd">Executing Department</th>
						</s:if>
						<s:if
							test="%{shouldShowField('function')|| shouldShowGridField('function')}">
							<th class="bluebgheadtd"><s:text name="function" /></th>
						</s:if>
						<s:if
							test="%{shouldShowHeaderField('functionary')|| shouldShowGridField('functionary')}">
							<th class="bluebgheadtd"><s:text name="functionary" /></th>
						</s:if>
						<s:if
							test="%{shouldShowHeaderField('scheme')|| shouldShowGridField('scheme')}">
							<th class="bluebgheadtd"><s:text name="scheme" /></th>
						</s:if>
						<s:if
							test="%{shouldShowHeaderField('subScheme')|| shouldShowGridField('subScheme')}">
							<th class="bluebgheadtd"><s:text name="subscheme" /></th>
						</s:if>
						<s:if
							test="%{shouldShowHeaderField('boundary')|| shouldShowGridField('boundary')}">
							<th class="bluebgheadtd"><s:text name="field" /></th>
						</s:if>
						<th class="bluebgheadtd">Seq No</th>
						<th class="bluebgheadtd">Sanctioned Budget(Rs)</th>
						<th class="bluebgheadtd">Addition Amount Sought(Rs)</th>
						<th class="bluebgheadtd">Reduction Amount Sought(Rs)</th>
						<th class="bluebgheadtd">Cumulative Amount(Rs)</th>
					</tr>
					<c:set var="trclass" value="greybox" />
					<c:set var="budgetdetailid" value="0" />
					<s:iterator var="p" value="reAppropriationList" status="s">
						<tr>
							<c:if test='${budgetdetailid!=budgetDetail.id}'>
								<c:set var="totalAmt" value="${budgetDetail.approvedAmount}" />
							</c:if>
							<td class="<c:out value="${trclass}"/>"><s:property
									value="#s.index+1" /></td>
							<s:if
								test="%{shouldShowHeaderField('fund')|| shouldShowGridField('fund')}">
								<td class="<c:out value="${trclass}"/>"><s:property
										value="%{budgetDetail.fund.name}" /></td>
							</s:if>
							<s:if
								test="%{shouldShowHeaderField('executingDepartment')|| shouldShowGridField('executingDepartment')}">
								<td class="<c:out value="${trclass}"/>"><s:property
										value="%{budgetDetail.executingDepartment.deptName}" /></td>
							</s:if>
							<s:if
								test="%{shouldShowField('function')|| shouldShowGridField('function')}">
								<td class="<c:out value="${trclass}"/>"><s:property
										value="%{budgetDetail.function.name}" /></td>
							</s:if>
							<s:if
								test="%{shouldShowHeaderField('functionary')|| shouldShowGridField('functionary')}">
								<td class="<c:out value="${trclass}"/>"><s:property
										value="%{budgetDetail.functionary.name}" /></td>
							</s:if>
							<s:if
								test="%{shouldShowHeaderField('scheme')|| shouldShowGridField('scheme')}">
								<td class="<c:out value="${trclass}"/>"><s:property
										value="%{budgetDetail.scheme.name}" /></td>
							</s:if>
							<s:if
								test="%{shouldShowHeaderField('subScheme')|| shouldShowGridField('subScheme')}">
								<td class="<c:out value="${trclass}"/>"><s:property
										value="%{budgetDetail.subScheme.name}" /></td>
							</s:if>
							<s:if
								test="%{shouldShowHeaderField('boundary')|| shouldShowGridField('boundary')}">
								<td class="<c:out value="${trclass}"/>"><s:property
										value="%{budgetDetail.boundary.name}" /></td>
							</s:if>
							<td style="text-align: right" class="<c:out value="${trclass}"/>">
								<s:property value="%{reAppropriationMisc.sequenceNumber}" />
							</td>
							<td style="text-align: right" class="<c:out value="${trclass}"/>">
								<s:text name="format.number">
									<s:param value="%{budgetDetail.approvedAmount}" />
								</s:text>
							</td>
							<td style="text-align: right" class="<c:out value="${trclass}"/>">
								<s:text name="format.number">
									<s:param value="%{additionAmount}" />
								</s:text>
							</td>
							<td style="text-align: right" class="<c:out value="${trclass}"/>">
								<s:text name="format.number">
									<s:param value="%{deductionAmount}" />
								</s:text>
							</td>
							<c:if
								test='${additionAmount==null || additionAmount==0 || additionAmount==0.0}'>
								<c:set var="totalAmt" value="${totalAmt-deductionAmount}" />
							</c:if>
							<c:if
								test='${deductionAmount==null || deductionAmount==0 || deductionAmount==0.0}'>
								<c:set var="totalAmt" value="${totalAmt+additionAmount}" />
							</c:if>
							<td style="text-align: right" class="<c:out value="${trclass}"/>">
								<c:out value="${totalAmt}" />.00
							</td>
							<c:choose>
								<c:when test="${trclass=='greybox'}">
									<c:set var="trclass" value="bluebox" />
								</c:when>
								<c:when test="${trclass=='bluebox'}">
									<c:set var="trclass" value="greybox" />
								</c:when>
							</c:choose>
							<c:set var="budgetdetailid" value="${budgetDetail.id}" />
						</tr>
					</s:iterator>
					<s:hidden name="targetvalue" value="%{target}" id="targetvalue" />
				</table>
			</div>
			<br /> <br />
			<div id="msgdiv" style="display: none">
				<table align="center" class="tablebottom" width="80%">
					<tr>
						<th class="bluebgheadtd" colspan="7"><s:text
								name="no.data.found" />
						</td>
					</tr>
				</table>
			</div>
			<br /> <br />
	</s:form>
	<script>
			<s:if test="%{reAppropriationList.size==0}">
				dom.get('msgdiv').style.display='block';
			</s:if>
			<s:if test="%{reAppropriationList.size!=0}">
				dom.get('msgdiv').style.display='none';
				dom.get('listid').style.display='block';
			</s:if>
			
			function checkMandatory()
			{
				if(document.getElementById('financialYear').value==0)
				{
					bootbox.alert('Please Select Financial Year');
					return false;
				}
				if(document.getElementById('budgetReAppropriation_fund').value==0 && document.getElementById('budgetReAppropriation_executingDepartment').value==0 && document.getElementById('budgetReAppropriation_function').value==0)
				{
					bootbox.alert('Please select Fund or Department or Functionary');
					return false;
				}
				return true;
			}
		</script>
</body>
</html>
