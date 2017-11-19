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

<%@ taglib uri="http://displaytag.sf.net" prefix="display" %>
<%@include file="/includes/taglibs.jsp" %>
<script type="text/javascript">
	function generateWardReport(bndryId) {
		document.forms[0].action = "activeDemandReport!search.action?reportType=Ward&boundaryId=" + bndryId;
		document.forms[0].submit();
	}
	function generatePartNoReport(bndryId) {
		document.forms[0].action = "activeDemandReport!search.action?reportType=PartNo&boundaryId=" + bndryId;
		document.forms[0].submit();
	}
</script>
<logic:empty name="resultList">
	<div class="headingsmallbgnew" style="text-align: center; width: 100%;">
		<s:text name="searchresult.norecord" />
	</div>
</logic:empty>
<logic:notEmpty name="resultList">
	<display:table name="resultList" uid="currentRowObject"
		class="tablebottom" style="width:99%;" cellpadding="0"
		cellspacing="0" export="true" requestURI="">
		<display:caption>
			<div class="headingsmallbgnew" align="center"
				style="text-align: center; width: 98%;">
				<span class="searchvalue1"><s:text name="activeDemandReport.title"/></span><br>
				<s:text name="txtReport" /> : <span class="mandatory"><s:property value="%{reportType}" /></span>, 
				<s:text name="PropertyType" /> : <span class="mandatory"><s:property value="%{selectedPropertyTypes}" /> </span>
			</div>
		</display:caption>
		<s:if test="%{reportType == 'Ward'}">
			<display:column headerClass="bluebgheadtd" class="blueborderfortd"
				title="Ward" style="text-align:center;width:10%;">
				<c:if test="${currentRowObject.boundaryName != 'Total' }">
					<a href="javascript:generatePartNoReport(<c:out value="${currentRowObject.boundaryId}"/>);"><c:out value="${currentRowObject.boundaryName}"/></a>
				</c:if>
				<c:if test="${currentRowObject.boundaryName == 'Total'}">
					<b><c:out value="${currentRowObject.boundaryName}"/><b>
				</c:if>
			</display:column>
		</s:if>
		<s:elseif test="%{reportType == 'PartNo'}">
			<display:column headerClass="bluebgheadtd" class="blueborderfortd"
				title="Part No" style="text-align:center;width:10%;">
				<c:if test="${currentRowObject.partNo != 'Total' }">
					<c:out value="${currentRowObject.partNo}"/>
				</c:if>
				<c:if test="${currentRowObject.partNo == 'Total'}">
					<b><c:out value="${currentRowObject.partNo}"/></b>
				</c:if>
			</display:column>
		</s:elseif>
		<s:else>
			<display:column headerClass="bluebgheadtd" class="blueborderfortd"
				title="Zone" style="text-align:center;width:10%;">
				<c:if test="${currentRowObject.boundaryName != 'Total' }">
					<a href="javascript:generateWardReport(<c:out value="${currentRowObject.boundaryId}"/>);"><c:out value="${currentRowObject.boundaryName}"/></a>
				</c:if>
				<c:if test="${currentRowObject.boundaryName == 'Total'}">
					<b><c:out value="${currentRowObject.boundaryName}"/></b>
				</c:if>
			</display:column>
		</s:else>
		<display:column headerClass="bluebgheadtd" class="blueborderfortd"
			title="Property Count" style="text-align:center;width:10%;">
			<c:if test="${currentRowObject.boundaryName == 'Total' || currentRowObject.partNo == 'Total'}">
				<b><c:out value="${currentRowObject.count}"/></b>
			</c:if>
			<c:if test="${currentRowObject.boundaryName != 'Total' && currentRowObject.partNo != 'Total'}">
				<c:out value="${currentRowObject.count}"/>
			</c:if>
		</display:column>
		<display:column headerClass="bluebgheadtd" class="blueborderfortd"
			title="Arrears Demand" style="text-align:right;width:10%;">
			<c:if test="${currentRowObject.boundaryName == 'Total' || currentRowObject.partNo == 'Total'}">
				<b><fmt:formatNumber pattern="#,##0.00" value="${currentRowObject.arrDmd}"/></b>
			</c:if>
			<c:if test="${currentRowObject.boundaryName != 'Total' && currentRowObject.partNo != 'Total'}">
				<fmt:formatNumber pattern="#,##0.00" value="${currentRowObject.arrDmd}"/>
			</c:if>
		</display:column>
		<display:column headerClass="bluebgheadtd" class="blueborderfortd"
			title="Current Demand" style="text-align:right;width:10%;">
			<c:if test="${currentRowObject.boundaryName == 'Total' || currentRowObject.partNo == 'Total'}">
				<b><fmt:formatNumber pattern="#,##0.00" value="${currentRowObject.currDmd}"/></b>
			</c:if>
			<c:if test="${currentRowObject.boundaryName != 'Total' && currentRowObject.partNo != 'Total'}">
				<fmt:formatNumber pattern="#,##0.00" value="${currentRowObject.currDmd}"/>
			</c:if>
		</display:column>
		<display:column headerClass="bluebgheadtd" class="blueborderfortd"
			title="Total Demand" style="text-align:right;width:10%;">
			<c:if test="${currentRowObject.boundaryName == 'Total' || currentRowObject.partNo == 'Total'}">
				<b><fmt:formatNumber pattern="#,##0.00" value="${currentRowObject.totDmd}"/></b>
			</c:if>
			<c:if test="${currentRowObject.boundaryName != 'Total' && currentRowObject.partNo != 'Total'}">
				<fmt:formatNumber pattern="#,##0.00" value="${currentRowObject.totDmd}"/>
			</c:if>
		</display:column>

		<display:setProperty name="export.csv" value="false" />
		<display:setProperty name="export.excel" value="true" />
		<display:setProperty name="export.xml" value="false" />
		<display:setProperty name="export.pdf" value="true" />
	</display:table>
</logic:notEmpty>
