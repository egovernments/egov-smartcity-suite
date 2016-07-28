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

<div id="assetValuesTable" class="panel panel-primary" data-collapsed="0">
	<div class="panel-heading">
		<div class="panel-title">
			<spring:message code="header.assetDetails" />
		</div>
	</div>
<div class="panel-body">

<table class="table table-bordered" id="tblassetdetails">
			<thead>
				<tr>
					<th><spring:message code="lbl.slno" /></th>
					<th><spring:message code="lbl.name" /></th>
					<th><spring:message code="lbl.code" /></th>
				</tr>
			</thead>
			<tbody id="assetDetailsTbl">
				<c:choose>
					<c:when test="${abstractEstimate.assetValues.size() != 0}">
						<c:forEach items="${abstractEstimate.getAssetValues()}" var="assetDtls" varStatus="item">
								<tr id="assetViewRow" onmouseover="changeColor(this, true);"	onmouseout="changeColor(this, false);">
								    <form:hidden path="assetValues[0].asset.id" name="assetValues[0].asset.id" value="${assetDtls.asset.id}" class="form-control table-input hidden-input" />
									<td><span class="spansno"><c:out value="${item.index + 1}" /></span></td>
									<td><c:out value="${assetDtls.asset.name}"></c:out></td>
								 	<td><c:out value="${assetDtls.asset.code}"></c:out></td>
								</tr>
						</c:forEach>
					</c:when>
					<c:otherwise>
					</c:otherwise>
				</c:choose> 
			</tbody>
		</table>
		
</div>

</div>