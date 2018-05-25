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
<link rel="stylesheet" href="<cdn:url  value='/resources/global/css/egov/map-autocomplete.css?rnd=${app_release_no}' context='/egi'/>"> 
<form:form method="post" action="" modelAttribute="notificationDraft" id="createDraftForm" cssClass="form-horizontal form-groups-bordered"
	enctype="multipart/form-data">
	<div class="row">
		<div class="col-md-12">
			<div class="panel panel-primary" data-collapsed="0">
				<div class="panel-heading">
					<div class="panel-title"><spring:message code="title.draft.new" /></div>
				</div>
				<form:hidden id="mode" path="" value="${mode}" />
				<div class="panel-body">
					<div class="form-group">
						<label class="col-sm-2 control-label text-right"><spring:message code="lbl.draft.name" />:<span class="mandatory"></span></label>
						<div class="col-sm-3 add-margin">
							<form:input path="name" id="name" name="name" 
								class="form-control text-left patternvalidation" maxlength="100" required="required"/>
							<form:errors path="name" cssClass="error-msg" />
						</div>
					</div>
					<div class="form-group">
						<label class="col-sm-2 control-label text-right"><spring:message code="lbl.draft.type" />:<span class="mandatory"></span></label>
						<div class="col-sm-3 add-margin">
							<form:select path="type" id="type" name="type"
								cssClass="form-control" cssErrorClass="form-control error"	required="required">
								<form:option value=""><spring:message code="lbl.select" /></form:option>
								<form:options items="${draftList}" />
							</form:select>
							<form:errors path="type" cssClass="error-msg" />
						</div>
					</div>
					<div class="form-group">
						<label class="col-sm-2 control-label text-right"><spring:message code="lbl.draft.module" />:<span class="mandatory"></span></label>
						<div class="col-sm-3 add-margin">
							<form:select path="module" id="module" name="module"
								cssClass="form-control" cssErrorClass="form-control error"	required="required">
								<form:option value=""><spring:message code="lbl.select" /></form:option>
								<form:options items="${TemplateModule}" itemLabel="name" itemValue="id" />
							</form:select>
							<form:errors path="module" cssClass="error-msg" />
						</div>
					</div>
					<div class="form-group">
						<label class="col-sm-2 control-label text-right"><spring:message code="lbl.draft.category" />:<span class="mandatory"></span></label>
						<div class="col-sm-3 add-margin">
							<form:select path="category" id="category" name="category"
								cssClass="form-control" cssErrorClass="form-control error"	required="required">
								<form:option value=""><spring:message code="lbl.select" /></form:option>
							</form:select>
							<form:errors path="category" cssClass="error-msg" />
						</div>
					</div>
					<div class="form-group">
						<label class="col-sm-2 control-label text-right"><spring:message code="lbl.draft.parameters" />:<span class="mandatory"></span></label>
						<div class="col-sm-3 add-margin" id="dragdiv">
							<ul id="allItems">
							<li class="li eachParameter" id="node">One</li>
							<li class="li eachParameter" id="node">Two</li>
							<li class="li eachParameter" id="node">Three</li>
                            </ul>
						</div>
					</div>
					<div class="form-group">
						<label class="col-sm-2 control-label text-right"><spring:message code="lbl.draft.notificationMessage" />:<span class="mandatory"></span></label>
						<div class="col-sm-3 add-margin">
							<form:textarea rows="4" cols="50" path="message" id="message" name="message" 
								class="form-control text-left patternvalidation" required="required"/>
							<form:errors path="message" cssClass="error-msg" />
						</div>
					</div>
			</div>
		</div>
	</div>
	<div class="form-group">
		<div class="text-center">
			<button type='submit' class='btn btn-primary' id="buttonSubmit">
				<spring:message code='lbl.create' />
			</button>
			<a href='javascript:void(0)' class='btn btn-default'
				id='buttonClose'><spring:message code='lbl.close' /></a>
		</div>
	</div>
	</div>
</form:form>
<link rel="stylesheet" href="<cdn:url value='/resources/global/css/font-icons/entypo/css/entypo.css' context='/egi'/>" />
<link rel="stylesheet" href="<cdn:url value='/resources/global/css/jquery/plugins/datatables/jquery.dataTables.min.css' context='/egi'/>"/>
<link rel="stylesheet" href="<cdn:url value='/resources/global/css/jquery/plugins/datatables/dataTables.bootstrap.min.css' context='/egi'/>">
<link rel="stylesheet" href="<cdn:url value='/resources/global/css/bootstrap/bootstrap-datepicker.css' context='/egi'/>" />
<link rel="stylesheet" href="<cdn:url value='/resources/css/draftCreate.css'/>" />

<script type="text/javascript" src="http://ajax.googleapis.com/ajax/libs/jquery/1.11.2/jquery.min.js"></script>  
<script type="text/javascript" src="https://ajax.googleapis.com/ajax/libs/jqueryui/1.11.4/jquery-ui.min.js" ></script>
<script type="text/javascript" src="<cdn:url value='/resources/global/js/jquery/plugins/datatables/jquery.dataTables.min.js' context='/egi'/>"></script>
<script type="text/javascript" src="<cdn:url value='/resources/global/js/jquery/plugins/datatables/dataTables.bootstrap.js' context='/egi'/>"></script>
<script type="text/javascript" src="<cdn:url value='/resources/global/js/jquery/plugins/datatables/dataTables.tableTools.js' context='/egi'/>"></script>
<script type="text/javascript" src="<cdn:url value='/resources/global/js/jquery/plugins/datatables/TableTools.min.js' context='/egi'/>"></script>
<script type="text/javascript" src="<cdn:url value='/resources/global/js/jquery/plugins/jquery.validate.min.js' context='/egi'/>"></script>
<script	type="text/javascript" src="<cdn:url value='/resources/global/js/bootstrap/bootstrap-datepicker.js' context='/egi'/>"></script>
<script type="text/javascript" src="<cdn:url  value='/resources/global/js/jquery/plugins/exif.js' context='/egi'/>"></script>
<script type="text/javascript" src="<cdn:url value='/resources/js/app/draftNewHelper.js?rnd=${app_release_no}'/>"></script>
<script type="text/javascript" src="<cdn:url value='/resources/js/app/common.js' />"></script>