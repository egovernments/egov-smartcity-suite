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
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ taglib uri="http://www.springframework.org/tags" prefix="spring"%>
<%@ taglib uri="http://www.springframework.org/tags/form" prefix="form"%>

<div class="col-md-12">
	<div class="panel panel-primary" data-collapsed="0">
		<div class="panel-heading">
			<div class="panel-title">Council Member</div>
		</div>
		<div class="panel-body">
			<div class="form-group">
				<label class="col-sm-2 control-label text-right"><spring:message
						code="lbl.electionward" /> </label>
				<div class="col-sm-3 add-margin">
					<form:select path="electionWard" id="electionWard"
						cssClass="form-control" cssErrorClass="form-control error">
						<form:option value="">
							<spring:message code="lbl.select" />
						</form:option>
						<form:options items="${boundarys}" itemValue="id" itemLabel="name" />
					</form:select>
					<form:errors path="electionWard" cssClass="error-msg" />
				</div>
				<label class="col-sm-2 control-label text-right"><spring:message
						code="lbl.designation" /> <span class="mandatory"></span> </label>
				<div class="col-sm-3 add-margin">
					<form:select path="designation" id="designation"
						cssClass="form-control" required="required"
						cssErrorClass="form-control error">

						<form:option value="">
							<spring:message code="lbl.select" />
						</form:option>
						<form:options items="${councilDesignations}" itemValue="id"
									itemLabel="name" />
					</form:select>
					<form:errors path="designation" cssClass="error-msg" />
				</div>
			</div>
			<div class="form-group">
				<label class="col-sm-2 control-label text-right"><spring:message
						code="lbl.qualification" /> <span class="mandatory"></span></label>
				<div class="col-sm-3 add-margin">
					<form:select path="qualification" id="qualification"
						cssClass="form-control" required="required"
						cssErrorClass="form-control error">
						<form:option value="">
							<spring:message code="lbl.select" />
						</form:option>
						<form:options items="${councilQualifications}" itemValue="id"
									itemLabel="name" />
					</form:select>
					<form:errors path="qualification" cssClass="error-msg" />
				</div>
				<label class="col-sm-2 control-label text-right toggle-madatory-caste"><spring:message
						code="lbl.caste" /><span class="mandatory"></span></label>
				<div class="col-sm-3 add-margin">
					<form:select path="caste" id="caste"
						cssClass="form-control" cssErrorClass="form-control error" required="required">
						<form:option value="">
							<spring:message code="lbl.select" />
						</form:option>
						<form:options items="${councilCastes}" itemValue="id"
									itemLabel="name" />
					</form:select>
					<form:errors path="caste" cssClass="error-msg" />
				</div>
			</div>
			<div class="form-group">
				<label class="col-sm-2 control-label text-right"><spring:message
						code="lbl.name" /> <span class="mandatory"></span> </label>
				<div class="col-sm-3 add-margin">
					<form:input path="name"
						class="form-control text-left patternvalidation"
						data-pattern="alphanumeric" maxlength="100" required="required" />
					<form:errors path="name" cssClass="error-msg" />
				</div>
				<label class="col-sm-2 control-label text-right"><spring:message
						code="lbl.gender" /> <span class="mandatory"></span></label>
				<div class="col-sm-3 add-margin">
					<form:select path="gender" id="gender" required="required"
						cssClass="form-control" cssErrorClass="form-control error">
						<form:option value="">
							<spring:message code="lbl.select" />
						</form:option>
						<form:options items="${gender}" />
					</form:select>
					<form:errors path="gender" cssClass="error-msg" />
				</div>
			</div>

			<div class="form-group">
				<label class="col-sm-2 control-label text-right"><spring:message
						code="lbl.mobilenumber" /><span class="mandatory"></span></label>
				<div class="col-sm-3 add-margin">
					<form:input type="text" cssClass="form-control patternvalidation"
						data-pattern="number" data-inputmask="'mask': '9999999999'"
						placeholder="Mobile Number" path="mobileNumber" minlength="10"
						maxlength="10" id="mobileNumber" required="required" />
					<form:errors path="mobileNumber" cssClass="error-msg" />
				</div>
				<label class="col-sm-2 control-label text-right"><spring:message
						code="lbl.birthdate" /> <span class="mandatory"></span> </label>
				<div class="col-sm-3 add-margin">
					<form:input type="text" cssClass="form-control datepicker"
						path="birthDate" id="birthDate" data-date-end-date="-18y"
						required="required" />
					<form:errors path="birthDate" cssClass="error-msg" />
				</div>
			</div>
			<div class="form-group">

				<label class="col-sm-2 control-label text-right"><spring:message
						code="lbl.emailid" /><span class="mandatory"></span></label>
				<div class="col-sm-3 add-margin">
					<form:input type="text" cssClass="form-control patternvalidation"
						data-pattern="regexp_alphabetspecialcharacters" path="emailId"
						id="emailId" maxlength="52" required="required" />
					<form:errors path="emailId" cssClass="error-msg" />
				</div>
				<label class="col-sm-2 control-label text-right"><spring:message
						code="lbl.residentialaddress" /> <span class="mandatory"></span>
				</label>
				<div class="col-sm-3 add-margin">
					<form:textarea path="residentialAddress" id="residentialAddress"
						cols="5" rows="2" class="form-control patternvalidation"
						data-pattern="regexp_alphabetspecialcharacters" required="required"
						minlength="5" maxlength="256" />
					<form:errors path="residentialAddress" cssClass="error-msg" />
				</div>
			</div>
			<div class="dateofjoining" id="dateofjoining">
				<div class="form-group">
					<label class="col-sm-2 control-label text-right"><spring:message
							code="lbl.date.of.joining" /> </label>
					<div class="col-sm-3 add-margin">
						<form:input type="text" cssClass="form-control dateval"
							path="dateOfJoining"  id="txt-dateOfJoining" data-date-end-date="0d" />
						<form:errors path="dateOfJoining" cssClass="error-msg" />
					</div>
				</div>
			</div>
			<c:if test="${state != 'NEW'}">
				<div class="form-group">
						<label class="col-sm-2 control-label text-right date-toggle-mandatory"><spring:message
								code="lbl.status" /></label>
						<div class="col-sm-3 add-margin">
						 <label class="radio-inline">
						<form:radiobutton path="status" value="ACTIVE" /> Active
						 </label>
    					<label class="radio-inline">
						<form:radiobutton path="status" value="INACTIVE" />In Active
						</label>
							<form:errors path="status" cssClass="error-msg" />
						</div>
				</div>
			</c:if>	
			<input type="hidden" name="councilMember" value="${councilMember.id}" />
			<form:hidden path="status" id="status"
				value="${councilMember.status}" />

		</div>
	</div>
</div>




<div class="col-md-12">
	<div class="panel panel-primary" data-collapsed="0">
		<div class="panel-body">
			<div class="form-group hide-input-fields">
				<label class="col-sm-2 control-label text-right toggle-madatory"><spring:message
						code="lbl.electiondate" /> <span class="mandatory"></span> </label>
				<div class="col-sm-3 add-margin">
					<form:input type="text" cssClass="form-control dateval addremoverequired"
						path="electionDate"  id="electionDate" data-date-end-date="0d"
						required="true" />
					<form:errors path="electionDate" cssClass="error-msg" />
				</div>
				<label class="col-sm-2 control-label text-right toggle-madatory"><spring:message
						code="lbl.oathdate" /> <span class="mandatory"></span> </label>
				<div class="col-sm-3 add-margin">
					<form:input type="text" cssClass="form-control dateval addremoverequired"
						path="oathDate" id="oathDate" data-date-end-date="0d"
						required="true" />
					<form:errors path="oathDate" cssClass="error-msg" />
				</div>
			</div>
			<div class="form-group">
			<div class="party hide-input-fields" id="party">
				<label class="col-sm-2 control-label text-right toggle-madatory"><spring:message
						code="lbl.partyaffiliation" /> <span class="mandatory"></span></label>
				<div class="col-sm-3 add-margin">
					<form:select path="partyAffiliation" id="partyAffiliation"
						required="true" cssClass="form-control addremoverequired"
						cssErrorClass="form-control error">
						<form:option value="">
							<spring:message code="lbl.select" />
						</form:option>
						<form:options items="${councilPartys}" itemValue="id"
							itemLabel="name" />
					</form:select>
					<form:errors path="partyAffiliation" cssClass="error-msg" />
				</div>
			</div>
			<div class="category hide-input-fields" id="category">
				<label class="col-sm-2 control-label text-right"><spring:message
						code="lbl.category" /> <span class="mandatory"></span></label>
				<div class="col-sm-3 add-margin">
					<form:select path="category" id="category"
						 cssClass="form-control" required="true"
						cssErrorClass="form-control error">
						<form:option value="">
							<spring:message code="lbl.select" />
						</form:option>
						<form:options items="${category}"/>
					</form:select>
					<form:errors path="category" cssClass="error-msg" />
				</div>
			</div>
				<label class="col-sm-2 control-label text-right"><spring:message
						code="lbl.photo" /></label>
				<div class="col-sm-3 add-margin">
					<c:choose>
						<c:when test="${councilMember.photo != null}">

							<input type="file" id="attachments" name="attachments"
								data-id="1" class="filechange inline btn upload-file" />
							<form:errors path="attachments" cssClass="error-msg" />

							<form:hidden path="photo.id" value="${councilMember.photo.id}" />
							<form:hidden path="photo.fileStoreId"
								value="${councilMember.photo.fileStoreId}" />

							<a
								href="/council/councilmember/downloadfile/${councilMember.photo.fileStoreId}"
								data-gallery> <img class="img-width add-margin"
								style="max-width: 25%; max-height: 25%;"
								src="/council/councilmember/downloadfile/${councilMember.photo.fileStoreId}"
								alt="${councilMember.photo.fileName}" /></a>
								<small class="error-msg"><spring:message code="lbl.mesg.upload.size"/></small>
						</c:when>
						<c:otherwise>
							<input type="file" id="attachments" name="attachments"
								data-id="1" class="filechange inline btn upload-file" />
								<small class="error-msg"><spring:message code="lbl.mesg.upload.size"/></small>
							<form:errors path="attachments" cssClass="error-msg" />
						</c:otherwise>
					</c:choose>

				</div>
			</div>
		</div>
	</div>
</div>

