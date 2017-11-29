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

<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ taglib uri="http://www.springframework.org/tags" prefix="spring"%>
<div class="row display-hide report-section">
	<div id="balanceAvailable" class="text-center"></div>
	<div id="errorMessage" style="display: none;">
		<spring:message code="error.lineestimate.loa.search" />
	</div>
	<div class="report-table-container">
		<table
			class="table table-bordered table-responsive table-hover"
			id="resultTable">
			<thead>
				<tr>
					<th><spring:message code="lbl.slno" /></th>
					<th><spring:message code="lbl.appropriationnumber" /></th>
					<th><spring:message code="lbl.appropriationdate" /></th>
					<th><spring:message code="lbl.appropriatedvalue" /></th>
					<th><spring:message code="lbl.estimatenumber" /></th>
					<th><spring:message code="lbl.wincode" /></th>
					<th><spring:message code="lbl.nameofwork" /></th>
					<th><spring:message code="lbl.dateofproposal" /></th>
					<th><spring:message code="lbl.estimate.value" /></th>
					<th><spring:message code="lbl.cumulativetotal" /></th>
					<th><spring:message code="lbl.balanceavailable" /></th>
				</tr>
			</thead>
			<tbody>
			<tfoot id="report-footer">
				<tr>
					<td colspan="9"><div class="text-right">Latest status</div></td>
					<td ></td>
					<td ></td>
				</tr>
			</tfoot>
			</tbody>
		</table>
	</div>
	<div class="row">
		<div class="col-sm-12 text-center">
			<button style="display: none;" type='button' class='btn btn-primary'
				id="btndownloadpdf">
				<spring:message code='lbl.download.pdf' />
			</button>
			<button style="display: none;" type='button' class='btn btn-primary'
				id="btndownloadexcel">
				<spring:message code='lbl.download.excel' />
			</button>
			<a href='javascript:void(0)' class='btn btn-default'
				onclick='self.close()'><spring:message code='lbl.close' /></a>
		</div>
	</div>
</div>