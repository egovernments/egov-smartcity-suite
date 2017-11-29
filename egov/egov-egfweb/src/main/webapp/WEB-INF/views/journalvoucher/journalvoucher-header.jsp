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

<div class="panel panel-primary" data-collapsed="0">
	<div class="panel-heading">
		
	</div>
	<div class="form-group">
		<c:choose>
			<c:when test="${voucherNumberGenerationAuto}">
				<label class="col-sm-3 control-label text-right"><spring:message code="lbl.vouchernumber" />
					<span class="mandatory"></span>
				</label>
				<div class="col-sm-3 add-margin">
					<form:input path="voucherNumber" id="voucherNumber" class="form-control patternvalidation" data-pattern="alphanumericwithspecialcharacters" maxlength="50" required="required" />
					<form:errors path="voucherNumber" cssClass="add-margin error-msg" />
				</div>
				
				<label class="col-sm-2 control-label text-right"><spring:message code="lbl.voucherdate" />
				</label>
				<div class="col-sm-3 add-margin">
					<form:input id="voucherDate" path="voucherDate" class="form-control datepicker" data-date-end-date="0d" required="required" />
					<form:errors path="voucherDate" cssClass="add-margin error-msg" />
				</div>
			</c:when>
			<c:otherwise>
				<label class="col-sm-3 control-label text-right"><spring:message code="lbl.voucherdate" />
				</label>
				<div class="col-sm-3 add-margin">
					<form:input id="voucherDate" path="voucherDate" class="form-control datepicker" data-date-end-date="0d" required="required" />
					<form:errors path="voucherDate" cssClass="add-margin error-msg" />
				</div>
				<label class="col-sm-2 control-label text-right"></label>
				<div class="col-sm-3 add-margin">
				</div>
			</c:otherwise>
		</c:choose>
		
	</div>
	
	<jsp:include page="journalvoucher-subtype.jsp"/>
	
	<jsp:include page="voucher-trans-filter.jsp"/>
	
	<div class="form-group">
		<label class="col-sm-3 control-label text-right">
			<spring:message code="lbl.narration" />
		</label>
		<div class="col-sm-3 add-margin">
			<form:textarea path="description" id="description" class="form-control" maxlength="300" ></form:textarea>
			<form:errors path="description" cssClass="add-margin error-msg" />
		</div>
				
		<label class="col-sm-2 control-label text-right">
		</label>
		<div class="col-sm-3 add-margin">
		</div>
	</div>
	
</div>