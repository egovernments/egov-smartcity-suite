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
<div class="panel panel-primary" data-collapsed="0">
	<div class="panel-heading">
		<div class="panel-title" style="text-align:center;"><spring:message code="lbl.searchestimatetemplate" /></div>
	</div>
	<div class="panel-body">
		<div class="form-group">
			<label class="col-sm-3 control-label text-right"> <spring:message code="lbl.typeofwork" /></label>
			<div class="col-sm-3 add-margin">
				<select name="typeOfWork" data-first-option="false" id="typeOfWork" class="form-control disablefield">
					<option value="">
						<spring:message code="lbl.select" />
					</option>
					<c:forEach items="${typeOfWork}" var="work">
						<c:choose>
							<c:when test="${typeOfWorkId == work.id }">
								<option value="${work.id }" selected="selected">${work.name }</option>
							</c:when>
							<c:otherwise>
								<option value="${work.id }">${work.name }</option>
							</c:otherwise>
						</c:choose>
					</c:forEach>
				</select>
			</div>
			<label class="col-sm-2 control-label text-right"> <spring:message code="lbl.subtypeofwork" /></label>
			<div class="col-sm-3 add-margin">
				<select name="subTypeOfWork" data-first-option="false" id="subTypeOfWork" class="form-control">
					<option value="">
						<spring:message code="lbl.select" />
					</option>
					<c:forEach items="${subTypeOfWork}" var="work">
						<c:choose>
							<c:when test="${subTypeOfWorkId == work.id }">
								<option value="${work.id }" selected="selected">${work.name }</option>
							</c:when>
							<c:otherwise>
								<option value="${work.id }">${work.name }</option>
							</c:otherwise>
						</c:choose>
					</c:forEach>
				</select>
			</div>
		</div>
		<div class="form-group">
			<label class="col-sm-3 control-label text-right"><spring:message code="lbl.templatecode" /></label>
			<div class="col-sm-3 add-margin">
				<input name="templateCode" id="templateCode" class="form-control" />
			</div>
			<label class="col-sm-2 control-label text-right"><spring:message code="lbl.templatename" /></label>
			<div class="col-sm-3 add-margin">
				<input name="templateName" id="templateName" value="" class="form-control"></textarea>
			</div>
		</div>
		<div class="form-group">
			<label class="col-sm-3 control-label text-right"><spring:message code="lbl.templatestatus" /></label>
			<div class="col-sm-3 add-margin">
			<c:choose>
				<c:when test="${mode != 'edit' && mode != 'view' }">
					<select name="status" data-first-option="false" id="status" disabled="disabled" class="form-control">
						<option value="true"><c:out value = "ACTIVE" /></option>
					</select>
				</c:when>
				<c:otherwise>
					<form:select path="status" id="status" cssClass="form-control">
						<form:option value="false" ><c:out value="INACTIVE" /></form:option>
						<form:option value="true" ><c:out value="ACTIVE" /></form:option>
			        </form:select>
			        <form:errors path="status" cssClass="error" />
				</c:otherwise>
			</c:choose>
			</div>
		</div>
	</div>
</div>
