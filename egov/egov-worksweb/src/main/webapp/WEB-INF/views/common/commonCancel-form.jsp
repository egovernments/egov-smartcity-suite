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
<%@ taglib uri="/WEB-INF/taglibs/cdn.tld" prefix="cdn"%>
<div class="panel display-hide panel-primary report-section" data-collapsed="0" >
	<form:form name="cancelForm" role="form" action="" id="cancelForm" class="form-horizontal form-groups-bordered">
		<input type="hidden" name="id" id="id" />
		<div class="panel-body">
			<div class="row">
				<label class="col-sm-2 control-label text-right"><spring:message code="lbl.cancellation.reason"/><span class="mandatory"></span></label>
				<div class="col-sm-3 add-margin">
					<select data-first-option="false" name="cancellationReason" id="cancellationReason" class="form-control" required="required">
						<option value="Data entry mistake">Data entry mistake</option>
						<option value="Others">Others</option>
					</select>
				</div>
				<div hidden="true" id="remarksDiv">
					<label class="col-sm-2 control-label text-right"><spring:message code="lbl.cancellation.remarks"/><span class="mandatory"></span></label>
					<div class="col-sm-3 add-margin">
						<textarea class="form-control" id="cancellationRemarks" name="cancellationRemarks" maxlength="126" ></textarea>
					</div>
				</div>
			</div>
		</div>
		<div class="row">
			<div class="col-sm-12 text-center">
				<button type='button' class='btn btn-primary' id="btncancel">
					<spring:message code='lbl.cancel' />
				</button>
				<a href='javascript:void(0)' class='btn btn-default'
			onclick='self.close()'><spring:message code='lbl.close' /></a>
			</div>
		</div>
	</form:form>
</div>
<script src="<cdn:url value='/resources/js/common/commoncancel.js?rnd=${app_release_no}'/>"></script>