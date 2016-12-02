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
	<c:choose>
		<c:when test="${mode != 'edit' }">
			<div class="panel-title" style="text-align:center;"><spring:message code="lbl.createoverhead" /></div>
		</c:when><c:otherwise>
			<div class="panel-title" style="text-align:center;"><spring:message code="lbl.modifyoverhead" /></div>
		</c:otherwise>
	</c:choose>
	</div>
	<input type="hidden" value="${overhead.id }" id="overheadid" name="overhead" />
	<input type="hidden" value="${overhead.accountCode.id }" id="accountCode" />
	
	<div class="panel-body">
		<div class="form-group">
			<label class="col-sm-3 control-label text-right"><spring:message code="lbl.name" /><span
						class="mandatory"></span></label>
			<div class="col-sm-3 add-margin">
				<form:input path="name" name="name" value="${name}" data-errormsg="Name is mandatory!" data-idx="0" data-optional="0" class="form-control table-input name" required="required"/>
				<form:errors path="name" cssClass="add-margin error-msg" />
			</div>
			<label class="col-sm-2 control-label text-right"><spring:message code="lbl.description" /><span
						class="mandatory"></span></label>
			<div class="col-sm-3 add-margin">
				<form:input path="description" class="form-control" id="overheadDescription" required="required" />
				<form:errors path="description" cssClass="add-margin error-msg" />
			</div>
		</div>
		<div class="form-group">
			<label class="col-sm-3 control-label text-right"><spring:message code="lbl.account" /><span
						class="mandatory"></span></label>	
			<div class="col-sm-3 add-margin">
				<form:select id="accountCode" path="accountCode" data-first-option="false" class="form-control" required="required">
					<form:option value="">
						<spring:message code="lbl.select" />
					</form:option>
					<c:forEach var="account" items="${accounts}">
						<form:option value="${account.id}"><c:out value="${account.glcode} - ${account.name}"/></form:option>  
					</c:forEach>   
				</form:select>
				<form:errors path="accountCode" cssClass="add-margin error-msg" />
			</div>
		</div>
	</div>
</div>
