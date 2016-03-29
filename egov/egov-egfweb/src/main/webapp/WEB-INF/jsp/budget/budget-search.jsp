<!--
  ~ eGov suite of products aim to improve the internal efficiency,transparency,
  ~    accountability and the service delivery of the government  organizations.
  ~
  ~     Copyright (C) <2015>  eGovernments Foundation
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
  -->
<%@ include file="/includes/taglibs.jsp"%>
<%@ page language="java"%>
<html>
<head>
<title><s:text name="budget.search" /></title>
</head>
<script>
		function check()
		{
			if(document.getElementById('financialYear').value==-1)
			{
				document.getElementById('errormsg').style.display='block';
				return false;
			}
			else
				return true;
		}
	</script>
<body>
	<s:form action="budget" theme="simple">
		<jsp:include page="budgetHeader.jsp">
			<jsp:param name="heading" value="Budget Definition Search" />
		</jsp:include>
		<div class="formmainbox">
			<div class="subheadnew">
				<s:text name="budget.definition.search" />
			</div>
			<span id="errormsg" style="display: none" class="mandatory"><s:text
					name="budget.financialyear.mandatory" /></span>
			<table align="center" width="100%">
				<tr>
					<td class="bluebox" width="10%">&nbsp;</td>
					<td class="bluebox" width="30%"><s:text
							name="budget.financialYear" /> <span class="mandatory"></span></td>
					<td class="bluebox"><s:select label="Financial Year"
							name="financialYear" id="financialYear"
							list="dropdownData.financialYearList" listKey="id"
							listValue="finYearRange" headerKey="-1"
							headerValue="----Choose----" value="%{budget.financialYear.id}" /></td>
				</tr>
			</table>
			<div class="buttonbottom">
				<s:submit method="list" value="Search" onclick="return check()"
					cssClass="buttonsubmit" />
				<input type="submit" value="Close"
					onclick="javascript:window.close()" class="button" />
			</div>
		</div>
		<div id="listid" style="display: block">
			<table width="100%" border="0" align="center" cellpadding="0"
				cellspacing="0" class="tablebottom">
				<tr>
					<th class="bluebgheadtdnew"><s:text name="name" /></th>
					<th class="bluebgheadtdnew"><s:text
							name="budget.primarybudget" /></th>
					<th class="bluebgheadtdnew"><s:text name="isactive" /></th>
					<th class="bluebgheadtdnew"><s:text name="budget.bere" /></th>
					<th class="bluebgheadtdnew"><s:text name="budget.parent" /></th>
					<th class="bluebgheadtdnew"><s:text name="financialyear" /></th>
					<th class="bluebgheadtdnew"><s:text name="status" /></th>
				</tr>
				<s:iterator var="p" value="budgetList" status="1">
					<tr>
						<td class="blueborderfortdnew"><a href="#"
							onclick="javascript:window.open('budget!edit.action?id=<s:property value='%{id}'/>','Search','resizable=yes,scrollbars=yes,left=300,top=40, width=900, height=700')"><s:property
									value="%{name}" /> </a></td>
						<td class="blueborderfortdnew"><s:if
								test="isPrimaryBudget ==true">
								<s:text name="yes" />
							</s:if>
							<s:else>
								<s:text name="no" />
							</s:else></td>
						<td class="blueborderfortdnew"><s:if
								test="isActiveBudget ==true">
								<s:text name="yes" />
							</s:if>
							<s:else>
								<s:text name="no" />
							</s:else></td>
						<td class="blueborderfortdnew"><s:property value="%{isbere}" />
						</td>
						<td class="blueborderfortdnew"><a
							href="budget!edit.action?id=<s:property value='%{parent.id}'/>"><s:property
									value="%{parent.name}" /> </a></td>
						<td class="blueborderfortdnew"><s:property
								value="%{financialYear.finYearRange}" /></td>
						<td class="blueborderfortdnew"><s:property
								value="%{state.value}" /></td>
					</tr>
				</s:iterator>
				<s:hidden name="targetvalue" value="%{target}" id="targetvalue" />
			</table>
		</div>
		<div id="msgdiv" style="display: none">
			<table align="center" width="100%">
				<tr>
					<td colspan="7"><s:text name="no.data.found" /></td>
				</tr>
			</table>
		</div>
	</s:form>
	<script>
		if(dom.get('targetvalue').value=='NONE')
		{
			dom.get('listid').style.display='none';
		}	
		else if(dom.get('targetvalue').value=='EMPTY')
		{
			dom.get('listid').style.display='none';
			dom.get('msgdiv').style.display='block';
		}
		</script>
</body>
</html>
