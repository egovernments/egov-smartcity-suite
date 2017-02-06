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

<%@ taglib uri="http://www.springframework.org/tags" prefix="spring"%>
<%@ taglib uri="http://www.springframework.org/tags/form" prefix="form"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt"%>
<%@ taglib uri="/WEB-INF/taglibs/cdn.tld" prefix="cdn"%>
<div class="new-page-header"><spring:message code="lbl.old.contractormbs" /></div>
<div class="panel panel-primary" data-collapsed="0">
	<div class="panel-heading">
	</div>
	<div class="panel-body">
		<div class="row">
			<div class="col-xs-12 col-md-8 col-md-offset-2 col-md-offset-2">
				<table class="table table-bordered" id="contractorMbs">
					<thead>
						<tr>
							<th><spring:message code="lbl.contractor.mb.referencenumber" /></th>
							<th><spring:message code="lbl.contractor.mb.date" /></th>
							<th><spring:message code="lbl.contractor.mbamount" /></th>
						</tr>
						<c:choose>
							<c:when test="${empty contractorMBHeaders }">
								<tr>
									<td><c:out value="NA" /></td>
									<td><c:out value="NA" /></td>
									<td><c:out value="NA" /></td>
								</tr>
							</c:when>
							<c:otherwise>
								<c:forEach items="${contractorMBHeaders }" var="mbHeader">
									<tr>
										<td><a href="javascript:void(0);" onclick="viewContractorMB(${mbHeader.id })">${mbHeader.mbRefNo }</a></td>
										<td><fmt:formatDate value="${mbHeader.mbDate }" pattern="dd/MM/yyyy" /></td>
										<td align="right"><fmt:formatNumber value="${mbHeader.mbAmount }" groupingUsed="false" maxFractionDigits="2" minFractionDigits="2"></fmt:formatNumber></td>
									</tr>
								</c:forEach>
							</c:otherwise>
						</c:choose>
					</thead>
					<tbody>
					</tbody>
				</table>
			</div>
		</div>
	</div>
	<div class="buttonbottom" align="center">
		<input type="button" name="button2" id="button2" value="Close"
					class="btn btn-default" onclick="window.close();" /></td>
	</div>
</div>
<script type="text/javascript"
	src="<cdn:url value='/resources/js/contractorportal/contractormb.js?rnd=${app_release_no}'/>"></script>