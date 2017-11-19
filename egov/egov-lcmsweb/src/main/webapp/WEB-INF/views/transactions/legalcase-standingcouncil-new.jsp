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
<%@ include file="/includes/taglibs.jsp"%>
<form:form role="form" method="post" modelAttribute="legalCaseAdvocate"
	id="legalCaseAdvocateform"
	cssClass="form-horizontal form-groups-bordered"
	enctype="multipart/form-data">
	<input type="hidden" name="legalCase" value="${legalCase.id}" />
	<input type="hidden" id="seniourAdvisRequired"
		name="seniourAdvisRequired" value="${legalCase.isSenioradvrequired}" />
	<jsp:include page="../transactions/view-summarizedcase.jsp" />
	<%@ include file="legalcase-standingcouncil-form.jsp"%>

	
	<div class="form-group">
		<div class="text-center">
			<button type="submit" name="submit" id="subitstandingcouncil" class="btn btn-default"
				value="Save">
				<spring:message code="lbl.submit" />
			</button>
			<button type='button' class='btn btn-default' id="btnclose">
				<spring:message code='lbl.close' />
		</div>
	</div>
</form:form>
<script
	src="<cdn:url value='/resources/global/js/bootstrap/bootstrap-datepicker.js' context='/egi'/>"></script>
<link rel="stylesheet"
	href="<cdn:url value='/resources/global/css/bootstrap/bootstrap-datepicker.css' context='/egi'/>">

<script
	src="<cdn:url value='/resources/js/app/standingCouncil.js?rnd=${app_release_no}'/>"></script>
	<script src="<cdn:url value='/resources/js/app/legalcase-ajax.js?rnd=${app_release_no}'/>"></script>
	<script>
	$('#btnclose').click(function(){
		bootbox.confirm({
		    message: 'Information entered in this screen will be lost if you close this page ? Please confirm if you want to close. ',
		    buttons: {
		        'cancel': {
		            label: 'No',
		            className: 'btn-default pull-right'
		        },
		        'confirm': {
		            label: 'Yes',
		            className: 'btn-danger pull-right'
		        }
		    },
		    callback: function(result) {
		        if (result) {
		             window.close();
		        }
		    }
		});
		
	});
	
	</script>