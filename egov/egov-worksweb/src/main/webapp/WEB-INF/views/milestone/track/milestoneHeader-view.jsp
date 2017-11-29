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

<%@ page contentType="text/html" language="java"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt"%>
<%@ taglib uri="http://www.springframework.org/tags/form" prefix="form"%>
<%@ taglib uri="http://www.springframework.org/tags" prefix="spring"%>

<div class="row">
	<div class="col-md-12">
		<input type="hidden" name="id" id="id" />
		<div class="panel panel-primary" data-collapsed="0"
			style="text-align: left">
			<div class="panel-heading">
				<div class="panel-title">
					<spring:message code="lbl.header" />
				</div>
			</div>
			<div class="panel-body">
				<div class="row add-border">
					<div class="col-xs-3 add-margin">
						<spring:message code="lbl.estimatenumber" />
					</div>
					<div class="col-xs-3 add-margin view-content" id="estimateNumber">
					</div>
					<div class="col-xs-3 add-margin">
						<spring:message code="lbl.dateofproposal" />
					</div>
					<div class="col-xs-3 add-margin view-content" id="lineEstimateDate">
					</div>
				</div>
				<div class="row add-border">
					<div class="col-xs-3 add-margin">
						<spring:message code="lbl.nameofwork" />
					</div>
					<div class="col-xs-3 add-margin view-content" id="nameOfWork">
					</div>
					<div class="col-xs-3 add-margin">
						<spring:message code="lbl.workidentificationnumber" />
					</div>
					<div class="col-xs-3 add-margin view-content" id="projectCode">
					</div>
				</div>
				<div class="row add-border">
					<div class="col-xs-3 add-margin">
						<spring:message code="lbl.typeofwork" />
					</div>
					<div class="col-xs-3 add-margin view-content" id="typeOfWork">
					</div>
					<div class="col-xs-3 add-margin">
						<spring:message code="lbl.subtypeofwork" />
					</div>
					<div class="col-xs-3 add-margin view-content" id="subTypeOfWork">
					</div>
				</div>
				<div class="row add-border">
					<div class="col-xs-3 add-margin">
						<spring:message code="lbl.estimatepreparedby" />
					</div>
					<div class="col-xs-3 add-margin view-content" id="lineEstimateCreatedBy">
					</div>
					<div class="col-xs-3 add-margin">
						<spring:message code="lbl.department" />
					</div>
					<div class="col-xs-3 add-margin view-content" id="department">
					</div>
				</div>
				<div class="row add-border">
					<div class="col-xs-3 add-margin">
						<spring:message code="lbl.loanumber" />
					</div>
					<div class="col-xs-3 add-margin view-content">
						<a onclick="openLetterOfAcceptance();" style="cursor:pointer;"><span id="workOrderNumber"></span></a><input
							type="hidden" id="workOrderId" />
					</div>
					<div class="col-xs-3 add-margin">
						<spring:message code="lbl.loadate" />
					</div>
					<div class="col-xs-3 add-margin view-content" id="divWorkOrderDate">
					</div>
					<input type="hidden" id="workOrderDate"
							value='' />
				</div>
				<div class="row add-border">
					<div class="col-xs-3 add-margin">
						<spring:message code="lbl.workvalue" />
					</div>
					<div class="col-xs-3 add-margin view-content" id="workOrderAmount">
					</div>
					<div class="col-xs-3 add-margin">
						<spring:message code="lbl.contractor" />
					</div>
					<div class="col-xs-3 add-margin view-content" id="contractorName">
					</div>
				</div>
			</div>
		</div>
	</div>
</div>