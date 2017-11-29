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

<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib uri="http://www.springframework.org/tags" prefix="spring" %>
<%@ taglib uri="http://www.springframework.org/tags/form" prefix="form" %>
<div class="row">
	<div class="col-md-12">
		<c:if test="${not empty message}">
			<div class="alert alert-success" role="alert"><spring:message code="${message}"/></div>
		</c:if>
		<div class="panel panel-primary" data-collapsed="0">
			<div class="panel-heading">
				<div class="panel-title">
					<spring:message code="lbl.profile.acc.dtls"/>
				</div>
			</div>
			<div class="panel-body custom-form">
				<form:form role="form" action="edit" modelAttribute="user" commandName="user" id="user" cssClass="form-horizontal form-groups-bordered" >
					<div class="form-group">
						<label for="field-1" class="col-sm-3 control-label"><spring:message code="lbl.fullname"/><span class="mandatory set-mandatory"></span></label>
						<div class="col-sm-2 col-md-1 add-margin">
							<form:select class="form-control" id="salutation" path="salutation" required="required">
								<form:option value=""></form:option>
								<form:option value="Mr"><spring:message code="lbl.mr"/></form:option>
								<form:option value="Mrs"><spring:message code="lbl.mrs"/></form:option>
								<form:option value="Miss"><spring:message code="lbl.miss"/></form:option>
							</form:select>
						</div>
						<div class="col-sm-4 col-md-5 add-margin">
							<form:input path="name" cssClass="form-control" placeholder="Name" maxlength="100" required="required"/>
						</div>
					</div>
					
					<div class="form-group">
						
						<label for="field-1" class="col-sm-3 control-label"><spring:message code="lbl.gender"/><span class="mandatory set-mandatory"></span></label>
						<div class="col-sm-6 col-xs-12 add-margin dynamic-span capitalize">
							<form:radiobuttons path="gender" element="span" required="required"/>
						</div>
					</div>
					
					<div class="form-group">
						<label class="col-sm-3 control-label"><spring:message code="lbl.mobile"/><span class="mandatory set-mandatory"></span></label>
						<div class="col-sm-2 add-margin">
							<div class="input-group">
								<span class="input-group-addon">+91</span>
                                <form:input path="mobileNumber" cssClass="form-control" data-inputmask="'mask': '9999999999'" id="mobileNumber" placeholder="Mobile Number" maxlength="10" disabled="true" required="required"/>
							</div>
							<form:errors path="mobileNumber" cssClass="add-margin error-msg"/>
						</div>
						
						<label for="field-1" class="col-sm-1 control-label">
							<spring:message code="lbl.email"/>
						</label>
						
						<div class="col-sm-3">
							<form:input path="emailId" cssClass="form-control" id="emailId" placeholder="abc@xyz.com" maxlength="50"/>
							<form:errors path="emailId" cssClass="add-margin error-msg"/>
						</div>
					</div>
					
					<div class="form-group">
						<label for="field-1" class="col-sm-3 control-label"><spring:message code="lbl.alt.contct.no"/></label>
						
						<div class="col-sm-2 add-margin">
							<form:input path="altContactNumber" cssClass="form-control" data-inputmask="'mask': '9999999999'" id="altContactNumber" placeholder="8080808080" maxlength="10"/>
						</div>
						
						<label for="field-1" class="col-sm-1 control-label">
							<spring:message code="lbl.dob" />
						</label>
						
						<div class="col-sm-3 add-margin">
							<form:input path="dob" cssClass="form-control datepicker"  data-inputmask="'mask': 'd/m/y'" id="dob" placeholder="24/01/1992"/>
						</div>
					</div>
					
					<div class="form-group">
						
						<label for="field-1" class="col-sm-3 control-label">
							<spring:message code="lbl.aadhar"/>
						</label>
						
						<div class="col-sm-2 add-margin">
							<form:input path="aadhaarNumber" cssClass="form-control" placeholder="123456789012" maxlength="12"/>
							<form:errors path="aadhaarNumber" cssClass="add-margin error-msg"/>
						</div>
						
						<label for="field-1" class="col-sm-1 control-label"><spring:message code="lbl.pan"/></label>
						
						<div class="col-sm-3 add-margin">
							<form:input path="pan" cssClass="form-control" placeholder="AHWPU1117T" maxlength="10"/>
							<form:errors path="pan" cssClass="add-margin error-msg"/>
						</div>
						
					</div>
					
					<div class="form-group">
						<label for="field-1" class="col-sm-3 control-label"><spring:message code="lbl.preferred.lang"/></label>
						<div class="col-sm-6 add-margin">
							<form:select class="form-control" id="locale" path="locale">
								<form:option value=""></form:option>
								<form:option value="en_IN"><spring:message code="lbl.eng"/></form:option>
								<form:option value="hi_IN"><spring:message code="lbl.hindi"/></form:option>
							</form:select>
						</div>
					</div>
					
					<div class="form-group">
						<div class="text-center">
							<button type="submit" class="btn btn-primary"><spring:message code="lbl.save.changes"/></button>
							<a href="javascript:void(0)" class="btn btn-default" onclick="self.close()"><spring:message code="lbl.close"/></a>
						</div>
					</div>
					
				</form:form>
			</div>
		</div>
	</div>
</div>