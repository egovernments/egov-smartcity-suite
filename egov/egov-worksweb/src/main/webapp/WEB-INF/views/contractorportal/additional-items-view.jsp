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
  <c:set var="net" value="0" />
<div class="panel panel-primary" data-collapsed="0">
	<div class="panel-heading">
		<div class="position_alert3"><spring:message code="lbl.pagetotal" /> : &#8377 <span id="pageTotal"></span></div>
		<div class="panel-title">
			<spring:message code="lbl.additional.items" />
		</div>
	</div>
	<div class="panel-body">
		<table class="table table-bordered" style="overflow: auto;">
			<thead>
				<tr>
					<th><spring:message code="lbl.slNo" /></th>
					<th><spring:message code="lbl.description" /></th>
					<th><spring:message code="lbl.uom" /></th>
					<th><spring:message code="lbl.rate" /></th>
					<th><spring:message code="lbl.current.entry" /></th>
					<th><spring:message code="lbl.amount" /></th>
				</tr>
			</thead>
			<tbody id="sorTable">
				<c:forEach items="${contractorMB.additionalMBDetails }" var="details" varStatus="item">
					<tr align="center">
						<td>
							<span class="spansorslno">${item.index + 1 }</span>
						</td>
						<td align="left">
							<span class="description_${item.index }">${details.description }</span>
						</td>
						<td>
							<span class="uom_${item.index }">${details.uom.uom }</span>
						</td>
						<td align="right">
							<span class="rate_${item.index }"><fmt:formatNumber groupingUsed="false" minFractionDigits="2" maxFractionDigits="4">${details.rate }</fmt:formatNumber></span>
						</td>
						<td align="right">
							<span class="spansorslno">${details.quantity }</span>
							<c:choose>
								<c:when test="${!details.measurementSheets.isEmpty() }">
									<button class="btn btn-default openmbsheet" name="additionalMBDetails[${item.index }].msadd" id="additionalMBDetails[${item.index }].msadd" data-idx="0" onclick="addMBMSheet(this);return false;"><i  class="fa fa-plus-circle" aria-hidden="true"></i></button>
								</c:when>
								<c:otherwise>
									<button style="display: none;" class="btn btn-default openmbsheet" name="additionalMBDetails[${item.index }].msadd" id="additionalMBDetails[${item.index }].msadd" data-idx="0" onclick="addMBMSheet(this);return false;"><i  class="fa fa-plus-circle" aria-hidden="true"></i></button>
								</c:otherwise>
							</c:choose>
						</td>
						<td hidden="true">
                            <input class="classmspresent" type="hidden" disabled="disabled" name="additionalMBDetails[${item.index }].mspresent" id="additionalMBDetails[${item.index }].mspresent" data-idx="0"/>
                            <input class="classmsopen" type="hidden" disabled="disabled" name="additionalMBDetails[${item.index }].msopen" id="additionalMBDetails[${item.index }].msopen" data-idx="0"/>
							<span  class="additionalMBDetails[${item.index }].mstd" id="additionalMBDetails[${item.index }].mstd" data-idx="0">
								<%@ include file="additional-items-measurementsheet-view.jsp"%>
							</span>
						</td>
						<td align="right">
							<span id="amount_${item.index }"><fmt:formatNumber groupingUsed="false" minFractionDigits="2" maxFractionDigits="4">${details.amount }</fmt:formatNumber></span>
							<c:set var="net" value="${net + details.amount }" />
						</td>
					</tr>
				</c:forEach>
			</tbody>
			<tfoot>
				<tr>
					<td colspan="5" class="text-right"><spring:message code="lbl.total" /></td>
					<td class="text-right"> <span id="mbTotal">
						<fmt:formatNumber maxFractionDigits="2" minFractionDigits="2" groupingUsed="false" value="${net }" />
					</span> </td>
				</tr>
			</tfoot>
		</table>
		<div class="add-margin" style="width:50%;float:right">
			<spring:message code="lbl.mbamount" /> (<spring:message code="lbl.mbamount.note" />) : 
			<fmt:formatNumber maxFractionDigits="2" minFractionDigits="2" groupingUsed="false" value="${contractorMB.mbAmount }" />
		</div>
	</div>
</div>
