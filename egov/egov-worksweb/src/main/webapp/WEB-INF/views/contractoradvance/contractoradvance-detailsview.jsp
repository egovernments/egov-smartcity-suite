<%--
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
  --%>

<%@ page contentType="text/html" language="java"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ taglib uri="http://www.springframework.org/tags" prefix="spring"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt"%>
<style>
      .position_alert{
        position:fixed;z-index:9999;top:85px;right:20px;background:#F2DEDE;padding:10px 20px;border-radius: 5px;
      }
</style>
<div class="panel panel-primary" data-collapsed="0">
	<div class="row">
		<div class="col-md-12">
			<div class="panel-heading">
				<div class="panel-title">
					<spring:message code="lbl.advance.details" />
				</div>
			</div>
			<div class="panel-body">
				<div class="row add-border">
					<div class="col-xs-3 add-margin">
						<spring:message code="lbl.arfnumber" />
					</div>
					<div class="col-xs-3 add-margin view-content">
						<c:out value="${contractorAdvanceRequisition.advanceRequisitionNumber}"></c:out>
					</div>
					<div class="col-xs-3 add-margin">
						<spring:message code="lbl.arfdate" />
					</div>
					<div class="col-xs-3 add-margin view-content">
						<fmt:formatDate
								value="${contractorAdvanceRequisition.advanceRequisitionDate}"
								pattern="dd/MM/yyyy" />
					</div>
				</div>
				<div class="row add-border">
					<div class="col-xs-3 add-margin">
						<spring:message code="lbl.remarks" />
					</div>
					<div class="col-xs-9 add-margin view-content">
						<c:out default="N/A"
							value="${contractorAdvanceRequisition.narration}"></c:out>
					</div>
				</div>
			</div>
			<div class="panel-heading">
				<div class="panel-title">
					<spring:message code="lbl.debit.details" />
				</div>
			</div>
			<div class="panel-body">
				<table class="table table-bordered">
					<thead>
						<th><spring:message code="lbl.account.code" /></th>
						<th><spring:message code="lbl.account.head" /></th>
						<th><spring:message code="lbl.debit.amount" /></th>
					</thead>
					<tbody>
						<c:forEach
							items="${contractorAdvanceRequisition.getEgAdvanceReqDetailses()}"
							var="advanceDtls" varStatus="item">
							<c:if test="${advanceDtls.debitamount > 0 }">
								<tr>
									<td><c:out value="${advanceDtls.chartofaccounts.glcode}" /></td>
									<td><c:out value="${advanceDtls.chartofaccounts.name}" /></td>
									<td class="text-right"><c:out value="${advanceDtls.debitamount}" /></td>
								</tr>
							</c:if>
						</c:forEach>
					</tbody>
				</table>
			</div>
			<div class="panel-heading">
				<div class="panel-title">
					<spring:message code="lbl.credit.details" />
				</div>
			</div>
			<div class="panel-body">
				<table class="table table-bordered">
					<thead>
						<th><spring:message code="lbl.account.code" /></th>
						<th><spring:message code="lbl.account.head" /></th>
						<th><spring:message code="lbl.credit.amount" /></th>
					</thead>
					<tbody>
						<c:forEach
							items="${contractorAdvanceRequisition.getEgAdvanceReqDetailses()}"
							var="advanceDtls" varStatus="item">
							<c:if test="${advanceDtls.creditamount > 0 }">
								<tr>
									<td><c:out value="${advanceDtls.chartofaccounts.glcode}" /></td>
									<td><c:out value="${advanceDtls.chartofaccounts.name}" /></td>
									<td class="text-right"><c:out value="${advanceDtls.creditamount}" /></td>
								</tr>
							</c:if>
						</c:forEach>
					</tbody>
				</table>
			</div>
		</div>
	</div>
</div>