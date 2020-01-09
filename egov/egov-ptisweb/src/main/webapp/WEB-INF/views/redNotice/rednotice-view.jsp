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
<%@ page contentType="text/html;charset=UTF-8" language="java"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ taglib uri="http://www.springframework.org/tags" prefix="spring"%>
<%@ taglib uri="/WEB-INF/taglib/cdn.tld" prefix="cdn"%>
<%@taglib uri="http://www.springframework.org/tags/form" prefix="form"%>

<script
	src="<cdn:url value='/resources/global/js/bootstrap/bootstrap.js' context='/egi'/>">
	
</script>

<form:form modelAttribute="redNoticeInfo" id="redNoticeInfo"
	theme="simple" enctype="multipart/form-data"
	class="form-horizontal form-groups-bordered">
	<input type="hidden" name="assessmentNo" id="assessmentNo"
		value="${redNoticeInfo.assessmentNo}" />
	<div style="overflow:scroll;height:500px;width:110%;overflow:auto"> 
		<div class="panel-body" >
			<table
				class="table table-bordered datatable dt-responsive table-hover"
				id="redNoticeTable">
				<thead>
					<tr>
						<th align="center" class="bluebgheadtd" style="width: 10%"><spring:message
								code="assessment.no" /></th>
						<th align="center" class="bluebgheadtd" style="width: 15%"><spring:message
								code="lbl.ownerName" /></th>
						<th align="center" class="bluebgheadtd" style="width: 15%" ><spring:message
								code="lbl.revenue.ward" /></th>
						<th align="center" class="bluebgheadtd" style="width: 10%"><spring:message
								code="lbl.doorNumber" /></th>
						<th align="center" class="bluebgheadtd" style="width: 15%"><spring:message
								code="lbl.locality" /></th>
						<th align="center" class="bluebgheadtd" style="width: 15%"><spring:message
								code="lbl.mobileNumber" /></th>
						<th align="center" class="bluebgheadtd" style="width: 10%" ><spring:message
								code="due.from" /></th>
						<th align="center" class="bluebgheadtd" style="width: 15%"><spring:message
								code="due.to" /></th>
						<th align="center" class="bluebgheadtd" style="width: 15%" ><spring:message
								code="arrear.due" /></th>
						<th align="center" class="bluebgheadtd" style="width: 10%"><spring:message
								code="arrear.penalty.due" /></th>
						<th align="center" class="bluebgheadtd" style="width: 10%" ><spring:message
								code="current.due" /></th>
						<th align="center" class="bluebgheadtd" style="width: 10%"><spring:message
								code="current.penalty.due" /></th>
						<th align="center" class="bluebgheadtd" style="width: 8%" ><spring:message
								code="total.due" /></th>
					</tr>
				</thead>
				<tr>
					<td class="blueborderfortd"><c:out
							value="${redNoticeInfo.assessmentNo}" /></td>
					<td class="blueborderfortd"><c:out value="${redNoticeInfo.ownerName}" /></td>
					<td class="blueborderfortd"><c:out
							value="${redNoticeInfo.revenueWard}" /></td>
					<td class="blueborderfortd"><c:out value="${redNoticeInfo.doorNo}" /></td>
					<td class="blueborderfortd"><c:out value="${redNoticeInfo.locality}" /></td>
					<td class="blueborderfortd"><c:out value="${redNoticeInfo.mobileNo}" /></td>
					<td class="blueborderfortd"><c:out
							value="${redNoticeInfo.fromInstallment}" /></td>
					<td class="blueborderfortd"><c:out
							value="${redNoticeInfo.toInstallment}" /></td>
					<td class="blueborderfortd"><c:out value="${redNoticeInfo.arrearTax}" /></td>
					<td class="blueborderfortd"><c:out
							value="${redNoticeInfo.arrearPenaltyTax}" /></td>
					<td class="blueborderfortd"><c:out value="${redNoticeInfo.currentTax}" /></td>
					<td class="blueborderfortd"><c:out
							value="${redNoticeInfo.currentTaxPenalty}" /></td>
					<td class="blueborderfortd"><c:out value="${redNoticeInfo.totalDue}" /></td>

				</tr>
			</table>
			<div class="row">
				<div class="text-center">
					<a
						href="/ptis/rednotice/generatenotice?assessmentNo=${redNoticeInfo.assessmentNo}"
						class="btn btn-default"><spring:message
							code="red.notice.button" /></a> <a href="javascript:void(0)"
						class="btn btn-default" onclick="self.close()"><spring:message
							code="lbl.close" /></a>
				</div>
			</div>
		</div>
	</div>
</form:form>