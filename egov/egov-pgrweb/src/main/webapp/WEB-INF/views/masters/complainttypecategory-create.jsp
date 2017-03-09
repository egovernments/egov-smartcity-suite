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

<div class="row">
	<div class="col-md-12">
		<form:form method="post" class="form-horizontal form-groups-bordered"	modelAttribute="complaintTypeCategory">
			<c:if test="${not empty message}">
				<div class="alert alert-success" role="alert"><spring:message code="${message}"/></div>
			</c:if>
			<div class="panel panel-primary" data-collapsed="0">
				<div class="panel-heading">
					<div class="panel-title">
					&nbsp;
					</div>
				</div>
				<div class="panel-body custom-form">
					<div class="form-group">
						<label class="col-sm-3 control-label">
							<spring:message code="lbl.name"/><span class="mandatory"></span>
						</label>
						<div class="col-sm-6 add-margin">
							<form:input path="name" id="name" cssClass="form-control is_valid_alphabet" cssErrorClass="form-control error" required="required" minlength="5" maxlength="100"/>
							<form:errors path="name" cssClass="error-msg" />
						</div>
					</div>		
					<div class="form-group">
						<label class="col-sm-3 control-label">
							<spring:message code="lbl.local.name"/>
						</label>
						<div class="col-sm-6 add-margin">
							<form:input path="localName" id="localname" cssClass="form-control" cssErrorClass="form-control error" maxlength="200"/>
							<form:errors path="localName" cssClass="error-msg" />
						</div>
					</div>				
					<div class="form-group">
						<label class="col-sm-3 control-label"> 
							<spring:message code="lbl.description"/>
						</label>
						<div class="col-sm-6 add-margin">
							<form:textarea path="description" id="description"
								cssClass="form-control is_valid_alphabet"
								cssErrorClass="form-control error" maxlength="250"/>
							<div class="text-left"><small><spring:message code="lbl.max.length" arguments="250"/></small></div>
						</div>
					</div>
				</div>
			</div>
			<div class="form-group">
				<div class="text-center">
					<button type="submit" class="btn btn-primary"><spring:message code="btn.lbl.create.category"/></button>
					<button type="reset" class="btn btn-default"><spring:message code="lbl.reset"/></button>
					<a href="javascript:void(0)" class="btn btn-default" onclick="self.close()"><spring:message code="lbl.close" /></a>
				</div>
			</div>
		</form:form>
	</div>
</div>