<%--
  ~ eGov suite of products aim to improve the internal efficiency,transparency,
  ~    accountability and the service delivery of the government  organizations.
  ~
  ~     Copyright (C) <2015>  eGovernments Foundation
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
<jsp:include page="view-complaint.jsp"></jsp:include>
<c:if test="${complaint.status.name != 'WITHDRAWN'}">
<div class="panel panel-primary" data-collapsed="0">
	<div class="panel-heading">
		<div class="panel-title">
			<strong><spring:message code="lbl.actions"/></strong>
		</div>
	</div>
	<div class="panel-body">

		<form:form id="complaintUpdate" modelAttribute="complaint"
			method="post" role="form"
			class="form-horizontal form-groups-bordered"  enctype="multipart/form-data">
			<div class="form-group">
				<div class="col-md-3 col-xs-6 add-margin">
					<spring:message code="lbl.change.status" />
				</div>
				<div class="col-md-3 col-xs-6 add-margin">
					<form:select path="status" data-first-option="true" id="status"
						cssClass="form-control" cssErrorClass="form-control error">
						<form:option  value="${complaint.status.id}">
							<spring:message code="lbl.select" />
						</form:option>
						<form:options items="${status}" itemValue="id" itemLabel="name" />
					</form:select>
					<form:errors path="status" cssClass="error-msg" />
				</div>
				
			</div>
		
			<div class="form-group">
				<div class="col-md-3 add-margin">
					<spring:message code="lbl.include.message" /><span class="mandatory"></span>
				</div>
				<div class="col-md-9 add-margin">
					<textarea class="form-control" id="inc_messge" placeholder="" required="required"
						maxlength="400" name="approvalComent"></textarea>
					<form:errors path="" cssClass="error-msg" />
				</div>
			</div>
			<c:if test="${complaint.status.name == 'COMPLETED'}">
				<div class="form-group">
					<div class="col-md-3 col-xs-6 add-margin">
						<spring:message code="lbl.feedback" /><span class="mandatory"></span>
					</div>
					<div class="col-md-3 col-xs-6 add-margin">
						<form:select path="citizenFeedback" id="feedback_dropdown" cssClass="form-control" 
							cssErrorClass="form-control error" >
						<form:options items="${citizenFeedback}" />
						</form:select>
						<form:errors path="citizenFeedback" cssClass="error-msg" />
					</div>
				</div>
			</c:if>
	<div class="form-group">
		<div class="text-center">
			 <button type="submit" class="btn btn-primary">
				<spring:message code="lbl.submit" />
			</button>
			<button type="reset" class="btn btn-default">
				<spring:message code="lbl.reset" />
			</button>
			<button type="button" class="btn btn-default" onclick="window.close();"><spring:message code="lbl.close" /></button>  
		</div>
	</div>
	</form:form>
</div>
</div>
</c:if>
<c:if test="${complaint.status.name == 'WITHDRAWN'}">
	<div class="form-group">
		<div class="text-center">
		<button type="button" class="btn btn-default" onclick="window.close();"><spring:message code="lbl.close" /></button>  
		</div>
	</div>
		
</c:if>
<script>
$(document).ready(function()
{
	$("select").each(function() { 
		  $(this).find('option').eq(0).prop('selected', true);
	});
});
</script>