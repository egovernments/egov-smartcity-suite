<%--
  ~ eGov suite of products aim to improve the internal efficiency,transparency,
  ~    accountability and the service delivery of the government  organizations.
  ~
  ~     Copyright (C) <2017>  eGovernments Foundation
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
<%@ page contentType="text/html;charset=UTF-8" language="java"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ taglib uri="http://www.springframework.org/tags" prefix="spring"%>
<%@ taglib uri="http://www.springframework.org/tags/form" prefix="form"%>
<%@ taglib uri="/WEB-INF/taglib/cdn.tld" prefix="cdn"%>


<form:form role="form" method="post"
	modelAttribute="councilSequenceNumber" id="CouncilSequenceNumberform"
	cssClass="form-horizontal form-groups-bordered"
	enctype="multipart/form-data">
	<div class="row">
		<div class="col-md-12">
			<div class="panel panel-primary" data-collapsed="0">
				<div class="panel-heading">
					<div class="panel-title">Add Sequence numbers</div>
				</div>
				<div class="panel-body">
					<div class="form-group">
						<label class="col-sm-3 control-label text-right"><spring:message
								code="lbl.preamble.number" /> (seq no.)<span class="mandatory"></span></label>
						<div class="col-sm-1 add-margin">
							<form:input path="preambleSeqNumber" maxlength="3"
								class="form-control text-left patternvalidation"
								data-pattern="number" required="required" />
							<form:errors path="preambleSeqNumber" cssClass="error-msg" />
						</div>

					</div>
					<div class="form-group">
						<label class="col-sm-3 control-label text-right"><spring:message
								code="lbl.agendaNumber" /> (seq no.)<span class="mandatory"></span></label>
						<div class="col-sm-1 add-margin">
							<form:input path="agendaSeqNumber" maxlength="3"
								class="form-control text-left patternvalidation"
								data-pattern="number" required="required" />
							<form:errors path="agendaSeqNumber" cssClass="error-msg" />
						</div>


					</div>
					<div class="form-group">
						<label class="col-sm-3 control-label text-right"><spring:message
								code="lbl.resolutionNumber" /> (seq no.)<span class="mandatory"></span></label>
						<div class="col-sm-1 add-margin">
							<form:input path="resolutionSeqNumber" maxlength="3"
								class="form-control text-left patternvalidation"
								data-pattern="number" required="required" />
							<form:errors path="resolutionSeqNumber" cssClass="error-msg" />
						</div>
					</div>

					<div class=" mandatory text-left">Note: Enter the last number
						used, system will generate +1 number entered. Please double
						confirm once you enter all the Manual sequence numbers. Once
						submitted can not be modified.</div>

				</div>
			</div>
		</div>
	</div>
	<div class="form-group">
		<div class="text-center">
			<button type='button' class='btn btn-primary' id="btnsave">
				<spring:message code='lbl.save' />
			</button>
			<button type="reset" class="btn btn-danger">
				<spring:message code="lbl.reset" />
			</button>
			<a href='javascript:void(0)' class='btn btn-default'
				onclick='self.close()'><spring:message code='lbl.close' /></a>
		</div>
	</div>
</form:form>

<script>
	$('#btnsave').click(function(e) {
		if ($('form').valid()) {
			$('form').submit();
		} else {
			e.preventDefault();
		}
	});
</script>

<link rel="stylesheet"
	href="<cdn:url value='/resources/app/css/council-style.css?rnd=${app_release_no}'/>" />
<script
	src="<cdn:url value='/resources/global/js/egov/inbox.js?rnd=${app_release_no}' context='/egi'/>"></script>
<script
	src="<cdn:url value='/resources/app/js/documentsupload.js?rnd=${app_release_no}'/>"></script>
<script type="text/javascript"
	src="<cdn:url value='/resources/app/js/common-util-helper.js?rnd=${app_release_no}'/>"></script>
