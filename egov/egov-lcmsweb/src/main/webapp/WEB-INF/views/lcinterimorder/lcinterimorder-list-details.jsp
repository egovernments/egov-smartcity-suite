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

<div class="panel-heading">
	<div class="panel-title">View Past Interim Order</div>
</div>
<c:choose>
	<c:when test="${lcInterimOrderList.isEmpty()}">
		<div class="form-group" align="center">No Master Data</div>
	</c:when>
	<c:otherwise>
		<table width="100%" border="1" align="center" cellpadding="0"
			cellspacing="0" class="table table-bordered datatable"
			id="lcInterimOrderTbl">
			<thead>
				<tr>
					<th colspan="1" class="text-center"><spring:message
							code="lbl.interimorder" /></th>
					<th colspan="1" class="text-center"><spring:message
							code="lbl.iodate" /></th>
					<th align="center" colspan="1" class="text-center"><spring:message
							code="lbl.mpnumber" /></th>
					<th align="center" colspan="1" class="text-center"><spring:message
							code="lbl.notes" /></th>


					<th colspan="1" class="text-center"><spring:message
							code="lbl.edit" /></th>

				</tr>
			</thead>
			<c:forEach var="lcInterimOrder" items="${lcInterimOrderList}">
				<tr>
					<td colspan="1">
						<div align="center">
							<c:out value="${lcInterimOrder.interimOrder.interimOrderType}" />
						</div>
					</td>

					<td colspan="1" id="iodate">
						<div align="center">
							<a style="cursor: pointer;" onclick="viewInterimorder();"> <c:out
									value="${lcInterimOrder.ioDate}" /> <input type="hidden"
								value="${lcInterimOrder.legalCase.lcNumber}" name="lcNumberHY"
								id="lcNumberHY" />
						</div>
					</td>
					<td colspan="1">
						<div align="center">
							<c:out value="${lcInterimOrder.mpNumber}" />
						</div>
					</td>
					<td colspan="1">
						<div align="center">
							<c:out value="${lcInterimOrder.notes}" />
						</div>
					</td>
					<td colspan="1">
						<div align="center">
							<a href="javascript:void(0);"
								onclick="edit('<c:out value="${lcInterimOrder.legalCase.lcNumber}" />');">Edit</a>
						</div>
					</td>
				</tr>
			</c:forEach>
		</table>
	</c:otherwise>
</c:choose>

<script
	src="<c:url value='/resources/global/js/bootstrap/bootstrap-datepicker.js' context='/egi'/>"></script>
<link rel="stylesheet"
	href="<c:url value='/resources/global/css/bootstrap/bootstrap-datepicker.css' context='/egi'/>">
<script type="text/javascript"
	src="<c:url value='/resources/js/app/lcInterimOrderHelper.js?rnd=${app_release_no}'/>"></script>
<script type="text/javascript"
	src="<c:url value='/resources/js/app/legalcaseSearch.js?rnd=${app_release_no}'/>"></script>