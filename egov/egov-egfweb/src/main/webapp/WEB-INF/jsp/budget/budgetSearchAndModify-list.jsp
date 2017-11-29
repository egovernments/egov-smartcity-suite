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
<title><s:text name="budget.search.modify" /></title>
</head>
<body>
	<script>
			var budgetDetailsTable = null;
			if(opener != null && opener.top != null){
				opener.top.document.getElementById('inboxframe').contentWindow.egovInbox.refresh();
			}
		</script>
	<jsp:include page="budgetHeader.jsp" />
	<div class="formmainbox">
		<div class="subheadnew">
			<s:text name="budget.search.modify" />
		</div>
		<s:form action="budgetSearchAndModify" theme="simple">
			<s:actionmessage />
			<%@ include file='budgetSearch-form.jsp'%>
			<div class="buttonbottom" style="padding-bottom: 10px;">
				<s:hidden name="mode" />
				<s:submit method="list" value="Search" cssClass="buttonsubmit"
					onclick="document.getElementById('budgetDetail_budget').disabled = false;" />
				<s:reset value="Cancel" cssClass="button" />
				<s:submit value="Close" onclick="javascript:window.close()"
					cssClass="button" />
			</div>
		</s:form>

		<s:if test="%{!budgetList.isEmpty()}">
			<div id="detail" width="100%">
				<table align="center" border="0" cellpadding="0" cellspacing="0"
					width="100%" class="tablebottom"
					style="border-right: 0px solid #C5C5C5;">
					<tr>
						<td colspan="9">
							<div class="subheadsmallnew">
								<strong><s:text name="budgetdetail.budget" /></strong>
							</div>
						</td>
					</tr>
					<tr>
						<th class="bluebgheadtd" width="10%"><s:text
								name="budget.budgetname" /></th>
						<th class="bluebgheadtd" width="11%"><s:text
								name="budget.financialYear" /></th>
						<th class="bluebgheadtd" width="10%"><s:text
								name="budget.description" /></th>
					</tr>

					<s:iterator value="budgetList" status="stat">
						<tr>

							<td class="blueborderfortd"><a
								href='<s:url action="budgetSearchAndModify" method="modifyList"><s:param name="mode" value="%{mode}" />
							</s:url>'><s:property
										value="name" /></a> &nbsp;</td>
							<td class="blueborderfortd"><s:property
									value="financialYear.finYearRange" />&nbsp;</td>
							<td class="blueborderfortd"><s:property value="description" />&nbsp;</td>
						</tr>
					</s:iterator>
				</table>
			</div>
		</s:if>
		<s:if test='%{message != ""}'>
			<div class="error">
				<s:property value="message" />
			</div>
		</s:if>
		<s:if test="%{disableBudget}">
			<s:hidden name="budget" id="hidden_budget" />
		</s:if>
		<script>
			disable = <s:property value="disableBudget"/>;
			if(disable){
				document.getElementById('budgetDetail_budget').disabled = true;
				document.getElementById('hidden_budget').value = '<s:property value="budgetDetail.budget.id"/>';
			}
			<s:if test="%{showDetails}">
			var functionid="";
			<s:if test="%{function.id!=0}">
	   		   functionid=<s:property value="function.id"/>
   		   </s:if>
			var params='financialYear=<s:property value="financialYear"/>&department.id=<s:property value="department.id"/>&function='+functionid;
    		params=params+'&onSaveOrForward=true&type=All';
    		var reportUrl="${pageContext.request.contextPath}/budget/budgetReport!getFunctionwiseReport.action?"+params;
    		window.open(reportUrl,"report");
			</s:if>
		</script>
</body>
</html>
