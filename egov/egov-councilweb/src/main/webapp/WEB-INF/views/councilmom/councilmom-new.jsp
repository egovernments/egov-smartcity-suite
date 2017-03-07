<%--
  ~ eGov suite of products aim to improve the internal efficiency,transparency,
  ~    accountability and the service delivery of the government  organizations.
  ~
  ~     Copyright (C) <2016>  eGovernments Foundation
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
<%@ taglib uri="/WEB-INF/taglib/cdn.tld" prefix="cdn" %>
<form:form role="form" action="../update"
	modelAttribute="councilMeeting" id="councilMomform"
	cssClass="form-horizontal form-groups-bordered"
	enctype="multipart/form-data">
	<div class="panel-heading">
		<ul class="nav nav-tabs" id="settingstab">
			<li class="active"><a data-toggle="tab" href="#councilmom"
				data-tabidx=0><spring:message code="tab.council.mom" /></a></li>
			<li><a data-toggle="tab" href="#councilattendance" data-tabidx=1><spring:message
						code="tab.council.attendance" /></a></li>
		</ul>
	</div>
	<div class="panel-body custom-form">
		<div class="tab-content">
			<div class="tab-pane fade in active" id="councilmom">
				<%@ include file="councilmom-form.jsp"%>
				<div class="form-group">
					<div class="text-center">
						<button type='button' class='btn btn-primary' id="buttonSubmit">
							<spring:message code='lbl.update' />
						</button>
						<button type="button" id="add-sumoto" class='btn btn-primary'><spring:message code='lbl.AddSumoto'/></button>
						<button type="button" id="buttonFinalSubmit" class='btn btn-primary'><spring:message code='lbl.resolutionpdf'/></button>
						<a href='javascript:void(0)' class='btn btn-default'
							onclick='self.close()'><spring:message code='lbl.close' /></a>
					</div>
				</div>
			</div>
			<div id="councilattendance" class="tab-pane fade">
				<%@ include file="councilmeeting-attendsearch-view.jsp"%>
			</div>
		</div>
	</div>

</form:form>

<script
	src="<cdn:url value='/resources/global/js/bootstrap/bootstrap-datepicker.js' context='/egi'/>"></script>
<script
	src="<cdn:url value='/resources/app/js/councilMomHelper.js?rnd=${app_release_no}'/>"></script>
<link rel="stylesheet"
	href="<cdn:url value='/resources/global/css/bootstrap/bootstrap-datepicker.css' context='/egi'/>">
<script type="text/javascript"
	src="<cdn:url value='/resources/app/js/councilMom.js?rnd=${app_release_no}'/>"></script>
<script>
	$('#buttonSubmit').click(function(e) {
		document.forms["councilMomform"].submit();
	});
</script>
