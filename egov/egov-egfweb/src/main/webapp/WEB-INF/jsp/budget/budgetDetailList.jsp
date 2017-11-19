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
<div id="detail"
	style="width: 100%; overflow-x: auto; overflow-y: hidden;">
	<table align="center" border="0" cellpadding="0" cellspacing="0"
		width="100%" class="tablebottom"
		style="border-right: 0px solid #C5C5C5;">
		<tr>
			<td colspan="9">
				<div class="subheadsmallnew">
					<strong><s:text name="existing.budgetdetails" /></strong>
				</div>
			</td>
		</tr>
		<tr>
			<th class="bluebgheadtd" width="10%"><s:text
					name="budgetdetail.budget" /></th>
			<s:if test="%{shouldShowField('fund')}">
				<th class="bluebgheadtd" width="10%"><s:text name="fund" /></th>
			</s:if>
			<s:if test="%{shouldShowField('function')}">
				<th class="bluebgheadtd" width="10%"><s:text name="function" /></th>
			</s:if>

			<th class="bluebgheadtd" width="11%"><s:text
					name="budgetdetail.budgetGroup" /></th>

			<s:if test="%{shouldShowField('executingDepartment')}">
				<th class="bluebgheadtd" width="16%"><s:text
						name="budgetdetail.executingDepartment" /></th>
			</s:if>
			<s:if test="%{shouldShowField('functionary')}">
				<th class="bluebgheadtd" width="10%"><s:text name="functionary" /></th>
			</s:if>
			<s:if test="%{shouldShowField('scheme')}">
				<th class="bluebgheadtd" width="10%"><s:text name="scheme" /></th>
			</s:if>
			<s:if test="%{shouldShowField('subScheme')}">
				<th class="bluebgheadtd" width="10%"><s:text name="subScheme" /></th>
			</s:if>
			<s:if test="%{shouldShowField('boundary')}">
				<th class="bluebgheadtd" width="10%"><s:text name="field" /></th>
			</s:if>

			<th class="bluebgheadtd" id="anticipatoryAmountheading" width="16%"><s:text
					name="budgetdetail.anticipatoryAmount" /></th>
			<th class="bluebgheadtd" width="16%">RE <s:property
					value="currentYearRange" />(Rs)
			</th>
			<th class="bluebgheadtd" width="16%">BE <s:property
					value="nextYearRange" />(Rs)
			</th>
		</tr>

		<s:iterator value="savedbudgetDetailList" status="stat" var="p">
			<tr>
				<td class="blueborderfortd"><s:property value="budget.name" />
					&nbsp;</td>
				<s:if test="%{shouldShowField('fund')}">
					<td class="blueborderfortd"><s:property value="fund.name" />&nbsp;</td>
				</s:if>
				<s:if test="%{shouldShowField('function')}">
					<td class="blueborderfortd"><s:property value="function.name" />&nbsp;</td>
				</s:if>
				<s:if test="%{shouldShowField('executingDepartment')}">
					<td class="blueborderfortd"><s:property
							value="executingDepartment.deptName" />&nbsp;</td>
				</s:if>


				<td class="blueborderfortd"><s:property
						value="budgetGroup.name" />&nbsp;</td>
				<s:if test="%{shouldShowField('functionary')}">
					<td class="blueborderfortd"><s:property
							value="functionary.name" />&nbsp;</td>
				</s:if>
				<s:if test="%{shouldShowField('scheme')}">
					<td class="blueborderfortd"><s:property value="scheme.name" />&nbsp;</td>
				</s:if>
				<s:if test="%{shouldShowField('subScheme')}">
					<td class="blueborderfortd"><s:property value="subScheme.name" />&nbsp;</td>
				</s:if>
				<s:if test="%{shouldShowField('boundary')}">
					<td class="blueborderfortd"><s:property value="boundary.name" />&nbsp;</td>
				</s:if>
				<td class="blueborderfortd" style="text-align: right;"><s:text
						name="format.number">
						<s:param name="value" value="anticipatoryAmount" />
					</s:text> &nbsp;</td>
				<td class="blueborderfortd" style="text-align: right;"><s:text
						name="format.number">
						<s:param name="value" value="originalAmount" />
					</s:text> &nbsp;</td>
				<td class="blueborderfortd" style="text-align: right;"><s:property
						value="%{beNextYearAmounts[#p.id]}" /> &nbsp;</td>
			</tr>
		</s:iterator>
	</table>
</div>
<script>
		var anticipatoryAmounthead=document.getElementById("anticipatoryAmountheading").innerHTML;
		var currentYearRange = '<s:property value="currentYearRange"/>(Rs)'; 
		var currentFullYear=curentYearRangeWithoutRs.substr(0,2)+curentYearRangeWithoutRs.substr(5,7);
		anticipatoryAmounthead=anticipatoryAmounthead+currentFullYear+'(Rs)';
		document.getElementById("anticipatoryAmountheading").innerHTML=anticipatoryAmounthead;
</script>

