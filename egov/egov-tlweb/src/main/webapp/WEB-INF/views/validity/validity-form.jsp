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
<div class="main-content">
  <div class="row">
    <div class="col-md-12">
      <div class="panel panel-primary" data-collapsed="0">
        <div class="panel-heading">
          <div class="panel-title">Validity</div>
        </div>
        <div class="panel-body">
          <div class="form-group">
            <label class="col-sm-3 control-label text-right"><spring:message code="lbl.natureofbusiness" /> <span
              class="mandatory"></span> </label>
            <div class="col-sm-3 add-margin">
              <form:select path="natureOfBusiness.id" id="natureOfBusiness.id" cssClass="form-control"
                cssErrorClass="form-control error">
                <form:option value="">
                  <spring:message code="lbl.select" />
                </form:option>
                <form:options items="${natureOfBusinesss}" itemValue="id" itemLabel="name" required="required" />
              </form:select>
              <form:errors path="natureOfBusiness" cssClass="error-msg" />
            </div>
            <label class="col-sm-3 control-label text-right"><spring:message code="lbl.licensecategory" /> </label>
            <div class="col-sm-3 add-margin">
              <form:select path="licenseCategory.id" id="licenseCategory.id" cssClass="form-control"
                cssErrorClass="form-control error">
                <form:option value="">
                  <spring:message code="lbl.select" />
                </form:option>
                <form:options items="${licenseCategorys}" itemValue="id" itemLabel="name" />
              </form:select>
              <form:errors path="licenseCategory" cssClass="error-msg" />
            </div>
          </div>
          <div class="form-group">
            <label class="col-sm-3 control-label text-right"><spring:message code="lbl.day" /> </label>
            <div class="col-sm-3 add-margin">
              <form:input path="day" class="form-control text-right patternvalidation" data-pattern="number" />
              <form:errors path="day" cssClass="error-msg" />
            </div>
            <label class="col-sm-3 control-label text-right"><spring:message code="lbl.week" /> </label>
            <div class="col-sm-3 add-margin">
              <form:input path="week" class="form-control text-right patternvalidation" data-pattern="number" />
              <form:errors path="week" cssClass="error-msg" />
            </div>
          </div>
          <div class="form-group">
            <label class="col-sm-3 control-label text-right"><spring:message code="lbl.month" /> </label>
            <div class="col-sm-3 add-margin">
              <form:input path="month" class="form-control text-right patternvalidation" data-pattern="number" />
              <form:errors path="month" cssClass="error-msg" />
            </div>
            <label class="col-sm-3 control-label text-right"><spring:message code="lbl.year" /> </label>
            <div class="col-sm-3 add-margin">
              <form:input path="year" class="form-control text-right patternvalidation" data-pattern="number" />
              <form:errors path="year" cssClass="error-msg" />
            </div>
          </div>
          <input type="hidden" name="validity" value="${validity.id}" />