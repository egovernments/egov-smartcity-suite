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
<form:form method="post" action="" modelAttribute="NotificationDraft" id="updateDraftForm" cssClass="form-horizontal form-groups-bordered"
	enctype="multipart/form-data" onsubmit="return checkcreateform()">
	<div class="row">
		<div class="col-md-12">
			<div class="panel panel-primary" data-collapsed="0">
				<div class="panel-heading">
					<div class="panel-title"><spring:message code="lbl.draft.update" /></div>
				</div>
				<input type="hidden" id="mode" path="" value="${mode}" />
				<input type="hidden" id="id" name="id" value="${NotificationDraft.id}" />
				<input type="hidden" id="draftName" value="${NotificationDraft.name}" />
				<input type="hidden" id="draftType" value="${NotificationDraft.type}" />
				<input type="hidden" id="draftModule" value="${NotificationDraft.module.id}" />
				<input type="hidden" id="draftCategory" value="${NotificationDraft.category.id}" />
				<input type="hidden" id="draftMessage" value="${NotificationDraft.message}" />
				<div class="panel-body">
					<div class="form-group">
						<label class="col-sm-2 control-label text-right"><spring:message code="lbl.draft.name" />:<span class="mandatory"></span></label>
						<div class="col-sm-10 add-margin">
							<form:input path="name" id="name" name="name" 
								class="form-control text-left patternvalidation" maxlength="100" required="required" value="${NotificationDraft.name}"/>
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
								<form:options items="${ModuleCategory}" itemLabel="name" itemValue="id" />
							</form:select>
							<form:errors path="category" cssClass="error-msg" />
						</div>
					</div>
					<div class="form-group">
						<label class="col-sm-2 control-label text-right"><spring:message code="lbl.draft.parameters" />:<span class="mandatory"></span></label>
						<div class="col-sm-3 add-margin" id="dragdiv">
							<ul id="allItems">
							<c:if test="${not empty CategoryParameters}">
								<c:forEach var="listVar" items="${CategoryParameters}">
	    							<li class="li eachParameter" id="node">${listVar.name}</li>
								</c:forEach>
							</c:if>
                            </ul>
						</div>
					</div>
					<div class="form-group">
						<label class="col-sm-2 control-label text-right"><spring:message code="lbl.draft.notificationMessage" />:<span class="mandatory"></span></label>
						<div class="col-sm-10 add-margin">
							<form:textarea path="message" id="message" name="message"
								class="form-control text-left patternvalidation"
								maxlength="200" required="required" value="${NotificationDraft.message}"/>
							<form:errors path="message" cssClass="error-msg" />
						</div>
					</div>
		</div>
	</div>
	<div class="form-group">
		<div class="text-center">
			<button type='submit' class='btn btn-primary' id="buttonSubmit">
				<spring:message code='lbl.update.button' />
			</button>
			<a href='javascript:void(0)' class='btn btn-default'
				onclick='self.close()'><spring:message code='lbl.close' /></a>
		</div>
	</div>
</form:form>
<script>
	$('#buttonSubmit').click(function(e) {
		if ($('form').valid()) {
		} else {
			e.preventDefault();
		}
	});
</script><link rel="stylesheet" href="<cdn:url value='/resources/global/css/font-icons/entypo/css/entypo.css' context='/egi'/>" />
<link rel="stylesheet" href="<cdn:url value='/resources/global/css/jquery/plugins/datatables/jquery.dataTables.min.css' context='/egi'/>"/>
<link rel="stylesheet" href="<cdn:url value='/resources/global/css/jquery/plugins/datatables/dataTables.bootstrap.min.css' context='/egi'/>">
<link rel="stylesheet" href="<cdn:url value='/resources/global/css/bootstrap/bootstrap-datepicker.css' context='/egi'/>" />

<script type="text/javascript" src="http://ajax.googleapis.com/ajax/libs/jquery/1.11.2/jquery.min.js"></script>  
<script type="text/javascript" src="https://ajax.googleapis.com/ajax/libs/jqueryui/1.11.4/jquery-ui.min.js" ></script>
<script type="text/javascript" src="<cdn:url value='/resources/global/js/jquery/plugins/datatables/jquery.dataTables.min.js' context='/egi'/>"></script>
<script type="text/javascript" src="<cdn:url value='/resources/global/js/jquery/plugins/datatables/dataTables.bootstrap.js' context='/egi'/>"></script>
<script type="text/javascript" src="<cdn:url value='/resources/global/js/jquery/plugins/datatables/dataTables.tableTools.js' context='/egi'/>"></script>
<script type="text/javascript" src="<cdn:url value='/resources/global/js/jquery/plugins/datatables/TableTools.min.js' context='/egi'/>"></script>
<script type="text/javascript" src="<cdn:url value='/resources/global/js/jquery/plugins/jquery.validate.min.js' context='/egi'/>"></script>
<script	type="text/javascript" src="<cdn:url value='/resources/global/js/bootstrap/bootstrap-datepicker.js' context='/egi'/>"></script>
<script type="text/javascript" src="<cdn:url  value='/resources/global/js/jquery/plugins/exif.js' context='/egi'/>"></script>
<script type="text/javascript" src="<cdn:url value='/resources/js/app/draftUpdate.js?rnd=${app_release_no}'/>"></script>
<script type="text/javascript">
$(document).ready(function(){
           $("#dragdiv li").draggable({
             helper: "clone",
             cursor: "move",
             revert: "invalid"
           });

           initDroppable($("#message"));

           function initDroppable($elements) {
             $elements.droppable({
               activeClass: "ui-state-default",
               hoverClass: "ui-drop-hover",
               accept: ":not(.ui-sortable-helper)",

               over: function(event, ui) {
                 var $this = $(this);
               },
               drop: function(event, ui) {
                 var $this = $(this);
                   $this.val($this.val() + "{{" + ui.draggable.text().trim() + "}}");
               }
             });
           }

           $('#module').change(function() {
               $.ajax({
                   url: '/api/draft/getCategoriesForModule/'+ $('#module').val(),
                   dataType: 'json',
                   type: 'GET',
                   // This is query string i.e. country_id=123
                   success: function(data) {
                       $('#category').empty(); // clear the current elements in select box
                       $('#category').append($('<option value="">Select</option>'));
                       for (row in data) {
                           $('#category').append($('<option value="'+data[row].id+'">'+data[row].name+'</option>'));
                       }
                       
                   },
                   error: function(jqXHR, textStatus, errorThrown) {
                       alert(errorThrown);
                   }
               });
       });

       $('#category').change(function() {
           $.ajax({
               url: '/api/draft/getParametersForCategory/'+ $('#category').val(),
               dataType: 'json',
               type: 'GET',
               // This is query string i.e. country_id=123
               success: function(data) {
               	$('#allItems').empty(); // clear the current elements in select box
                   for (row in data) {
                       $('#allItems').append($('<li class="li eachParameter" id="node">'+data[row].name+'</li>'));
                   }
                   $("#dragdiv li").draggable({
                       helper: "clone",
                       cursor: "move",
                       revert: "invalid"
                     });
               },
               error: function(jqXHR, textStatus, errorThrown) {
                   alert(errorThrown);
               }
           });
       });
});
</script>
