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
		<div class="panel-title" style="text-align:center;"><spring:message code="lbl.search.estimates" /></div>
	</div>
	<div class="panel-body">
		<div class="form-group">
			<label class="col-sm-3 control-label text-right"><spring:message code="lbl.abstractestimatenumber" /></label>
			<div class="col-sm-3 add-margin">
				<input name="estimateNumber" id="estimateNumber" class="form-control" placeholder="Type first 3 letters of Abstract/Detailed Estimate Number" />
			</div>
			<label class="col-sm-2 control-label text-right"><spring:message code="lbl.status" /></label>
			<div class="col-sm-3 add-margin">
				<input name="status" id="status" value="APPROVED" readonly="readonly" class="form-control"/>
			</div>
		</div>
		<div class="form-group">
			<label class="col-sm-3 control-label text-right"><spring:message code="lbl.createdby" /></label>
			<div class="col-sm-3 add-margin">
				<select name="abstractEstimateCreatedBy" data-first-option="false" id="abstractEstimateCreatedBy" class="form-control">
					<option value=""><spring:message code="lbl.select" /></option>
					<c:forEach items="${abstractEstimateCreatedByUsers}" var="user">
						<option value="${user.id }">${user.name }</option>
					</c:forEach>
				</select>
			</div>
			<label class="col-sm-2 control-label text-right"> <spring:message code="lbl.typeofwork" /></label>
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
		</div>
	</div>
</div>
