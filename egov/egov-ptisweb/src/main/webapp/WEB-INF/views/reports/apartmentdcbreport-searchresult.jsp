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
<%@ taglib uri="http://www.springframework.org/tags/form" prefix="form"%>

<div class="row">
	<div class="text-center">
		<button type="button" id="btnsearch" class="btn btn-primary">
			Search</button>
		<button type="button" id="btnclose" class="btn btn-default"
			onclick="window.close();">Close</button>
	</div>
</div>
<br />
<div class="row display-hide report-section">
	<br />
	<spring:message code="reports.note.text" />
	<input type="hidden" value="${sessionScope.citymunicipalityname}, ${sessionScope.districtName} District" id="pdfTitle" />
	<div class="col-md-12 table-header text-left"><spring:message code="lbl.title.apartmentDCBReport.report" /></div> 
	<div class="col-md-12 form-group report-table-container">
		<table class="table table-bordered table-hover multiheadertbl"
			id="tbldcbdrilldown">
			<thead>
				<tr>
					<th rowspan="2"><spring:message code="lbl.name" /></th> 
					<th rowspan="2"><spring:message code="lbl.doorno" /></th> 
					<th rowspan="2"><spring:message code="lbl.OwnerName" /></th>
					<th rowspan="2"><spring:message code="lbl.assessment.count" /></th>
					<th colspan="7"><spring:message code="lbl.actual.demand" /></th>
					<th colspan="7"><spring:message code="lbl.actual.collection" /></th>
					<th colspan="5"><spring:message code="lbl.balance"/></th>
				</tr>

				<tr>
					<th><spring:message code="lbl.arrear.propertytax"/></th>
					<th><spring:message code="lbl.arrear.penalty"/></th>
					<th><spring:message code="lbl.arreartotal"/></th>
					<th><spring:message code="lbl.current.propertytax"/></th>
					<th><spring:message code="lbl.current.penalty"/></th>
					<th><spring:message code="lbl.current.total"/></th>
					<th><spring:message code="lbl.total.demand"/></th>
					<th><spring:message code="lbl.arrear.propertytax"/></th>
					<th><spring:message code="lbl.arrear.penalty"/></th>
					<th><spring:message code="lbl.arreartotal"/></th>
					<th><spring:message code="lbl.current.propertytax"/></th>
					<th><spring:message code="lbl.current.penalty"/></th>
					<th><spring:message code="lbl.current.total"/></th>
					<th><spring:message code="lbl.total.collection"/></th>
					<th><spring:message code="lbl.arrear.propertytax"/></th>
					<th><spring:message code="lbl.arrear.penalty"/></th>
					<th><spring:message code="lbl.current.propertytax"/></th>
					<th><spring:message code="lbl.current.penalty"/></th>
					<th><spring:message code="lbl.balance.propertytax"/></th>
				</tr>
			</thead>
			<tfoot id="report-footer">
				<tr>
					<td>Total</td>
					<td></td>
					<td></td>
					<td></td>
					<td></td>
					<td></td>
					<td></td>
					<td></td>
					<td></td>
					<td></td>
					<td></td>
					<td></td>
					<td></td>
					<td></td>
					<td></td>
					<td></td>
					<td></td>
					<td></td>
					<td></td>
					<td></td>
					<td></td>
					<td></td>
					<td></td>
				</tr>
			</tfoot>
		</table>
	</div>
</div>

<div id="report-backbutton" class="col-xs-12 text-center">
	<div class="form-group">
		<button class="btn btn-primary" id="backButton">Back</button>
	</div>
</div>