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
<div class="panel panel-primary" data-collapsed="0">
<div class="panel-body">
<div class="row">
<div class="col-xs-12 col-md-8 col-md-offset-2">
<br/>
	<table class="table table-bordered">
		<thead>
			<tr>
				<th><spring:message code="lbl.nameofwork" /></th>
				<th><spring:message code="lbl.loanumber" /></th>
				<th><spring:message code="lbl.loaamount" /></th>
			</tr>
		</thead>
		<tbody>
			<tr>
				<td ><span id="nameOfWork1">${nameOfWork}</span></td>
				<td ><span id="loaNumber">${loaNumber}</span></td>
				<td class="text-right"><span id="loaAmount"><fmt:formatNumber groupingUsed="false" maxFractionDigits="2"
							minFractionDigits="2" value="${loaAmount}" /></span></td>
			</tr>
		</tbody>
	</table>
</div>

<div class="col-xs-12 col-md-8 col-md-offset-2 col-md-offset-2">
	<table class="table table-bordered" id="contractorMbs">
		<thead>
			<tr>
				<th><spring:message code="lbl.contractor.mb.referencenumber" /></th>
				<th><spring:message code="lbl.contractor.mb.date" /></th>
				<th><spring:message code="lbl.contractor.mbamount" /></th>
			</tr>
		</thead>
		<tbody>
		</tbody>
	</table>
</div>

	</div>
	</div>
</div>