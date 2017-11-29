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
<script>
 <s:set var="colratio" value="%{'[50,80,80,200,80,80,80,80,80,80,80,80,150,150,200,150]'}"/>
<s:if test="%{isConsolidatedScreen()}">
<s:set var="colratio" value="%{'[50,80,80,200,80,80,80,80,80,80,80,80,80,150,80,150,200,150]'}"/>
</s:if>	

jQuery.noConflict();
jQuery(document).ready(function() {
	    jQuery('#detailsTable').fixheadertable({
         caption: 'Budget Detail', 
         height: 400,
         minColWidth: 10,  
         resizeCol: true,
         colratio:<s:property value="colratio"/>
    });
});
 
</script>

<div id="detail">

	<table id="detailsTable" class="table-header-fix">
		<thead>
			<tr>
				<th><s:text name="budgetdetail.budget.department" /></th>
				<th><s:text name="fund" /></th>
				<th><s:text name="budgetdetail.function" /></th>
				<th><s:text name="budgetdetail.budgetGroup" /></th>

				<th><s:text name="budgetdetail.actuals" /> <s:property
						value="twopreviousfinYearRange" /></th>
				<th><s:text name="budgetdetail.actuals" /> <s:property
						value="previousfinYearRange" /></th>
				<th>BE <s:property value="currentfinYearRange" />(A)
				</th>
				<th><s:text name="budget.reappropriation" />(B)</th>
				<th>Total (A+B)</th>
				<th><s:text name="budgetdetail.actuals" /> upto <s:property
						value="currentfinYearRange" /></th>
				<th id="anticipatoryAmountheading"><s:text
						name="budgetdetail.anticipatoryAmount" /> <s:property
						value="currentfinYearRange" /></th>
				<th><s:text name="budget.re" /> <s:property
						value="savedbudgetDetailList.get(0).getBudget().getFinancialYear().getFinYearRange()" />
					Proposed</th>
				<s:if test="%{isConsolidatedScreen()}">
					<th>RE <s:property
							value="savedbudgetDetailList.get(0).getBudget().getFinancialYear().getFinYearRange()" />
						Fixed
					</th>
				</s:if>
				<th><s:text name="budget.be" /> <s:property
						value="nextfinYearRange" /> Proposed</th>
				<s:if test="%{isConsolidatedScreen()}">
					<th width="10%">BE <s:property value="nextfinYearRange" />
						Fixed
					</th>
				</s:if>

			</tr>
		</thead>
		<tbody>
			<s:iterator value="bpBeanList" status="stat">
				<tr>
					<s:if test="%{rowType=='heading'}">

					</s:if>
					<s:elseif test="%{rowType=='majorcode'}">
					</s:elseif>

					<s:elseif test="%{rowType=='total'}">
					</s:elseif>

					<s:else>
						<input type='hidden'
							name="bpBeanList[<s:property value='#stat.index'/>].id"
							id="bpBeanList[<s:property value='#stat.index'/>].id"
							value="<s:property value='id'/>" />
						<input type='hidden'
							name="bpBeanList[<s:property value='#stat.index'/>].nextYrId"
							id="bpBeanList[<s:property value='#stat.index'/>].nextYrId"
							value="<s:property value='nextYrId'/>" />
						<input type='hidden'
							name="bpBeanList[<s:property value='#stat.index'/>].documentNumber"
							id="bpBeanList[<s:property value='#stat.index'/>].documentNumber"
							value="<s:property value='docNo'/>" />
						<td><s:property value="executingDepartment" /> &nbsp;</td>
						<td><s:property value="fund" />&nbsp;</td>
						<td><s:property value="function" />&nbsp;</td>
						<td><s:property value="budgetGroup" />&nbsp;</td>



						<td style="text-align: right;"><s:property
								value="twoPreviousYearActuals" />&nbsp;</td>
						<td style="text-align: right;"><s:property
								value="previousYearActuals" />&nbsp;</td>
						<td style="text-align: right;"><s:property
								value="currentYearBE" />&nbsp;</td>
						<td style="text-align: right;"><s:property
								value="reappropriation" />&nbsp;</td>
						<td style="text-align: right;"><s:property value="total" />&nbsp;</td>
						<td style="text-align: right;"><s:property
								value="currentYearActuals" />&nbsp;</td>
						<td style="text-align: right;"><s:property
								value="anticipatory" />&nbsp;</td>


						<s:if test="%{isConsolidatedScreen()}">

						</s:if>
						<s:else>
							<td><input type="text" onchange="update(this);"
								style="text-align: right; size: 50px;"
								id='bpBeanList[<s:property value="#stat.index"/>].proposedRE'
								name='bpBeanList[<s:property value="#stat.index"/>].proposedRE'
								value='<s:text name="format.number"><s:param value="proposedRE"/></s:text>' />&nbsp;</td>
						</s:else>
						<s:if test="%{isConsolidatedScreen()}">
							<td style="text-align: right;"><s:property
									value="proposedBE" />&nbsp;</td>
							<td><input type="text" onchange="update(this);" size="10"
								style="text-align: right;"
								id='bpBeanList[<s:property value="#stat.index"/>].approvedBE'
								name='bpBeanList[<s:property value="#stat.index"/>].approvedBE'
								value='<s:text name="approvedBE"><s:param value="approvedBE"/></s:text>' />&nbsp;</td>
						</s:if>
						<s:else>
							<td><input type="text" onChange="update(this);"
								style="text-align: right; size: 50px;"
								id='bpBeanList[<s:property value="#stat.index"/>].proposedBE'
								name='bpBeanList[<s:property value="#stat.index"/>].proposedBE'
								value='<s:text name="format.number"><s:param value="proposedBE"/></s:text>' />&nbsp;</td>
						</s:else>

					</s:else>
				</tr>

			</s:iterator>

		</tbody>

	</table>

</div>



