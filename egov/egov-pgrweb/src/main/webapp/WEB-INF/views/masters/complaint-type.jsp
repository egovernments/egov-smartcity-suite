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
<%@ taglib uri="/WEB-INF/taglib/cdn.tld" prefix="cdn" %>
<script src="<cdn:url  value='/resources/js/app/complaintype.js?rnd=${app_release_no}'/>"></script>

<div class="row">
	<div class="col-md-12">
		<form:form id="addcomplaint" method="post" class="form-horizontal form-groups-bordered"	modelAttribute="complaintType">
			<div class="panel panel-primary" data-collapsed="0">
				<div class="panel-heading">
					<div class="panel-title">
						<strong><spring:message code="lbl.complaintType" /></strong>
					</div>
				</div>
				<div class="panel-body custom-form">
					<div class="form-group">
						<label class="col-sm-3 control-label"> 
							<spring:message code="lbl.name" /><span class="mandatory"></span>
						</label>
						<div class="col-sm-6 add-margin">
							<form:input path="name" id="comp_type_name" cssClass="form-control is_valid_alphabet" cssErrorClass="form-control error" required="required"/>
							<form:errors path="name" cssClass="error-msg" />
						</div>
					</div>
					<div class="form-group">
						<label class="col-sm-3 control-label"> 
							<spring:message code="lbl.local.name" />
						</label>
						<div class="col-sm-6 add-margin">
							<form:input path="localName" id="comp_type_localname" cssClass="form-control " cssErrorClass="form-control error" maxlength="200"/>
							<form:errors path="localName" cssClass="error-msg" />
						</div>
					</div>
					<div class="form-group">
						<label class="col-sm-3 control-label"> 
							<spring:message	code="lbl.complaintTypeCode" /><span class="mandatory"></span>
						</label>
						<c:choose>
							<c:when test="${mode != 'update'}">
								<div class="col-sm-6 add-margin">
									<form:input path="code" id="comp_type_code" cssClass="form-control is_valid_alphaNumWithsplchar" cssErrorClass="form-control error" required="required"/>
									<form:errors path="code" cssClass="error-msg" />
								</div>
							</c:when>
							<c:otherwise>
								<div class="col-sm-6 add-margin">
									<form:input path="code" id="comp_type_code" cssClass="form-control is_valid_alphaNumWithsplchar" cssErrorClass="form-control error" disabled="true"/>
									<form:errors path="code" cssClass="error-msg" />
								</div>
							</c:otherwise>
						</c:choose>
					</div>
					<div class="form-group">
						<label class="col-sm-3 control-label">
							<spring:message code="lbl.category" /><span class="mandatory"></span>
						</label>
						<div class="col-sm-6 add-margin">
							<form:select path="category" id="category" cssClass="form-control" cssErrorClass="form-control error" required="required">
								<form:option value="">
									<spring:message code="lbl.select" />
								</form:option>
								<form:options items="${categories}" itemValue="id" itemLabel="name" />
							</form:select>
							<form:errors path="category" cssClass="error-msg" />
						</div>
					</div>
					<div class="form-group">
						<label class="col-sm-3 control-label"> 
							<spring:message	code="lbl.department" />
						</label>
						<div class="col-sm-6 add-margin">
							<form:select path="department" id="comp_type_dept" cssClass="form-control" cssErrorClass="form-control error">
								<form:option value="">
									<spring:message code="lbl.select" />
								</form:option>
								<form:options items="${departments}" itemValue="id"
									itemLabel="name" />
							</form:select>
							<form:errors path="department" cssClass="error-msg" />
						</div>
					</div>
					<div class="form-group">
						<label for="field-1" class="col-sm-3 col-xs-2 control-label">
							<spring:message	code="lbl.isactive"/>
						</label>
						<div class="col-sm-3 col-xs-8 add-margin">
							<form:checkbox path="isActive" id="comp_type_isactive" />
							<form:errors path="isActive" cssClass="error-msg" />
						</div>
					</div>
					<div class="form-group">
						<label class="col-sm-3 control-label"> 
							<spring:message	code="lbl.slahrs" /><span class="mandatory"></span>
						</label>
						<div class="col-sm-6 add-margin">
							<form:input path="slaHours" id="slahours" maxlength="4" cssClass="form-control is_valid_number" cssErrorClass="form-control error" required="required"/>
							<form:errors path="slaHours" cssClass="error-msg" />
						</div>
					</div>
					<div class="form-group">
						<label for="field-1" class="col-sm-3 col-xs-2 control-label">
							<spring:message	code="lbl.hasFinImpact"/>
						</label>
						<div class="col-sm-3 col-xs-8 add-margin">
							<form:checkbox path="hasFinancialImpact" id="hasFinancialImpact" />
							<form:errors path="hasFinancialImpact" cssClass="error-msg" />
						</div>
					</div>  
					<div class="form-group">
						<label class="col-sm-3 control-label"> 
							<spring:message	code="lbl.complaintTypeDesc" />
						</label>
						<div class="col-sm-6 add-margin">
							<form:textarea path="description" id="comp_type_desc" cssClass="form-control is_valid_alphabet" cssErrorClass="form-control error" maxlength="100"/>
							<div class="text-left"><small><spring:message code="lbl.comp.type.details"/></small></div>
						</div>
					</div>
				</div>
			</div>
			<div class="form-group">
				<div class="text-center">
					<button type="submit" class="btn btn-primary"><spring:message code="lbl.submit"/></button>
					<c:if test="${mode != 'update'}">
						<button type="reset" class="btn btn-default"><spring:message code="lbl.reset"/></button>
					</c:if>
					<a href="javascript:void(0)" class="btn btn-default" onclick="self.close()"><spring:message code="lbl.close" /></a>
				</div>
			</div>
		</form:form>
	</div>
</div>
<script>
var complaintTypeId = '${complaintType.id}';
if (complaintTypeId == '') {
 $("#comp_type_isactive").prop('checked',true);
}
</script>
