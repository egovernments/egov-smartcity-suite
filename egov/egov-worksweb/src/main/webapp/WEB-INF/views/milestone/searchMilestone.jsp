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
<%@ taglib uri="/WEB-INF/taglibs/cdn.tld" prefix="cdn"%>
<form:form name="searchMilestoneTemplateForm" role="form" action="create" modelAttribute="milestoneTemplate" id="milestoneTemplate" class="form-horizontal form-groups-bordered" enctype="multipart/form-data">
  	<div class="panel panel-primary" data-collapsed="0">
		<div class="panel-heading">
			<div class="panel-title" style="text-align: center;">
				<spring:message code="lbl.searchmilestonetemplate" />
			</div>
		</div>
		<div class="form-group">
		<label class="col-sm-3 control-label text-right"><spring:message code="lbl.typeofwork" /><span class="mandatory"></span></label>
		<div class="col-sm-3 add-margin">
			<form:select path="typeOfWork" data-first-option="false" id="typeOfWork" class="form-control" required="required"  >
				<form:option value="">
					<spring:message code="lbl.select" />
				</form:option>
				<form:options items="${typeOfWork}" itemLabel="description" itemValue="id" />
			</form:select>
			<form:errors path="typeOfWork" cssClass="add-margin error-msg" />
		</div>
			<label class="col-sm-2 control-label text-right"><spring:message code="lbl.subtypeofwork" /></label>
		<div class="col-sm-3 add-margin">
		<input type="hidden" id="subTypeOfWorkValue" value="${lineEstimate.subTypeOfWork.id }"/>
			<form:select path="subTypeOfWork" data-first-option="false" id="subTypeOfWork" class="form-control" >
				<form:option value=""><spring:message code="lbl.select"/></form:option>
			</form:select>
			<form:errors path="subTypeOfWork" cssClass="add-margin error-msg" />
		</div>
		</div>
	<div class="form-group">
		<label class="col-sm-3 control-label text-right"><spring:message code="lbl.templatename" /></span></label>
		<div class="col-sm-3 add-margin">
			<form:input name="name" path="name" id="name" value="${name}" class="form-control" maxlength="124"></form:input>
			<form:errors path="name" cssClass="add-margin error-msg" />
		</div>
		<label class="col-sm-2 control-label text-right"><spring:message code="lbl.description" /></label>
		<div class="col-sm-3 add-margin">
			<form:input name="description" path="description" id="description" class="form-control" value="${description}" maxlength="124"></form:input>
			<form:errors path="description" cssClass="add-margin error-msg" />
		</div>
	</div>
</div>
</form:form>
			<div class="row">
				<div class="col-sm-12 text-center">
					<button type='button' class='btn btn-primary' id="btnsearch">
						<spring:message code='lbl.search' />
					</button>
					<a href='javascript:void(0)' class='btn btn-default'
						onclick='self.close()'><spring:message code='lbl.close' /></a>
				</div>
			</div>

<jsp:include page="milestoneTemplate-searchresults.jsp"/>
<script>
	$('#btnsearch').click(function(e) {
		if ($('form').valid()) {
		} else {
			e.preventDefault();
		}
	});
</script>
<script type="text/javascript"
	src="<cdn:url value='/resources/js/milestonetemplate/searchmilestonetemplate.js?rnd=${app_release_no}'/>"></script>
			