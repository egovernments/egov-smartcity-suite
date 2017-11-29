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

<%@ taglib uri="http://www.springframework.org/tags" prefix="spring"%>
<%@ taglib uri="http://www.springframework.org/tags/form" prefix="form"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt" %>

<div class="row">
	<div class="col-md-12">
		<div class="panel panel-primary" data-collapsed="0">
			<div class="panel-heading">
				<div class="panel-title">
					<spring:message code="lbl.tapexecutiondetails"/>
				</div>					
			</div>
			<input type="hidden" value='<fmt:formatDate pattern="dd/MM/yyyy" value="${waterConnectionDetails.applicationDate}" />' id="appDate"/>
			<div class="panel-body">
				<div class="form-group">
					<div class="col-sm-3 control-label text-right"><spring:message code="lbl.executiondate" /><span class="mandatory"></span></div>
					<div class="col-sm-3 add-margin">
						<form:input id="executionDate" path="executionDate" class="form-control datepicker today" data-date-end-date="0d" required="required" />
					</div>
					<c:if test="${waterConnectionDetails.connectionType=='METERED'}">
						<div class="col-sm-3 control-label text-right"><spring:message code="lbl.metermake" /><span class="mandatory"></span></div>
							<div class="col-sm-3 add-margin">
							<form:select path="connection.meter" data-first-option="false"
								cssClass="form-control" required="required">
								<form:option value="">
									<spring:message code="lbl.select" />
								</form:option>
								<form:options items="${meterCostMasters}" itemValue="id" itemLabel="meterMake" />
							</form:select>
							<form:errors path="connection.meter" cssClass="add-margin error-msg" />
						</div>
					</div>	
					<div class="form-group">
						<div class="col-sm-3 control-label text-right">
							<spring:message code="lbl.initialreading"/><span class="mandatory"></span>
						</div>
						<div class="col-sm-3 add-margin">
							<form:input class="form-control patternvalidation" id="connection" path="connection.initialReading" data-pattern="number" maxlength="12" required="required" />
							<form:errors path="connection.initialReading" cssClass="add-margin error-msg" />
						</div>
						<div class="col-sm-3 control-label text-right">
							<spring:message code="lbl.meter.serial.no"/><span class="mandatory"></span>
						</div>
						<div class="col-sm-3 add-margin">
							<form:input class="form-control patternvalidation" id="meterSerialNumber" path="connection.meterSerialNumber" data-pattern="number" maxlength="12" required="required" />
							<form:errors path="connection.meterSerialNumber" cssClass="add-margin error-msg" />
						</div>
					</c:if>
				</div>	
			</div>	
		</div>
	</div>
</div>			