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



<div class="form-group">
	<c:choose>
		<c:when test="${headerFields.contains('department')}">
			<c:choose>
				<c:when test="${mandatoryFields.contains('department')}">
					<label class="col-sm-3 control-label text-right"><spring:message
							code="lbl.department" /> <span class="mandatory"></span> </label>
					<div class="col-sm-3 add-margin">
						<form:select path="vouchermis.departmentid"
							data-first-option="false" id="department" class="form-control"
							required="required">
							<form:option value="">
								<spring:message code="lbl.select" />
							</form:option>
							<form:options items="${departments}" itemValue="id"
								itemLabel="name" />
						</form:select>
						<form:errors path="vouchermis.departmentid"
							cssClass="add-margin error-msg" />
					</div>
				</c:when>
				<c:otherwise>
					<label class="col-sm-3 control-label text-right"><spring:message
							code="lbl.department" /> </label>
					<div class="col-sm-3 add-margin">
						<form:select path="vouchermis.departmentid"
							data-first-option="false" id="department" class="form-control">
							<form:option value="">
								<spring:message code="lbl.select" />
							</form:option>
							<form:options items="${departments}" itemValue="id"
								itemLabel="name" />
						</form:select>
						<form:errors path="vouchermis.departmentid"
							cssClass="add-margin error-msg" />
					</div>
				</c:otherwise>
			</c:choose>
		</c:when>
		<c:otherwise>
			<label class="col-sm-3 control-label text-right"></label>
			<div class="col-sm-3 add-margin"></div>
		</c:otherwise>
	</c:choose>
	<c:choose>
		<c:when test="${headerFields.contains('function')}">
			<c:if test="${voucherHeader.vouchermis.function != null}">
				<c:choose>
					<c:when test="${mandatoryFields.contains('function')}">
						<label class="col-sm-2 control-label text-right"><spring:message
								code="lbl.function" /> <span class="mandatory"></span> </label>
						<div class="col-sm-3 add-margin">
							<form:select path="vouchermis.function" data-first-option="false"
								id="function" class="form-control" required="required">
								<c:forEach items="${functions}" var="functions">
									<form:option value="${functions.id}">${functions.code} - ${functions.name}</form:option>
								</c:forEach>
							</form:select>
							<form:errors path="vouchermis.function"
								cssClass="add-margin error-msg" />
						</div>
					</c:when>
					<c:otherwise>
						<label class="col-sm-2 control-label text-right"><spring:message
								code="lbl.function" /> </label>
						<div class="col-sm-3 add-margin">
							<form:select path="vouchermis.function" data-first-option="false"
								id="function" class="form-control">
								<c:forEach items="${functions}" var="functions">
									<form:option value="${functions.id}">${functions.code} - ${functions.name}</form:option>
								</c:forEach>
							</form:select>
							<form:errors path="vouchermis.function"
								cssClass="add-margin error-msg" />
						</div>
					</c:otherwise>
				</c:choose>
			</c:if>
			<c:if test="${voucherHeader.vouchermis.function == null}">
				<c:when test="${mandatoryFields.contains('function')}">
					<label class="col-sm-2 control-label text-right"><spring:message
							code="lbl.function" /> <span class="mandatory"></span> </label>
					<div class="col-sm-3 add-margin">
						<form:select path="vouchermis.function" data-first-option="false"
							id="function" class="form-control" required="required">
							<form:option value="">
								<spring:message code="lbl.select" />
							</form:option>
							<form:options items="${functions}" itemValue="id"
								itemLabel="name" />
						</form:select>
						<form:errors path="vouchermis.function"
							cssClass="add-margin error-msg" />
					</div>
				</c:when>
				<c:otherwise>
					<label class="col-sm-2 control-label text-right"><spring:message
							code="lbl.function" /> </label>
					<div class="col-sm-3 add-margin">
						<form:select path="vouchermis.function" data-first-option="false"
							id="function" class="form-control">
							<form:option value="">
								<spring:message code="lbl.select" />
							</form:option>
							<form:options items="${functions}" itemValue="id"
								itemLabel="name" />
						</form:select>
						<form:errors path="vouchermis.function"
							cssClass="add-margin error-msg" />
					</div>
				</c:otherwise>
			</c:if>
		</c:when>
		<c:otherwise>
			<label class="col-sm-2 control-label text-right"></label>
			<div class="col-sm-3 add-margin"></div>
		</c:otherwise>
	</c:choose>
</div>