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
  
<div class="row">
  <div class="col-sm-12">
    <br>
    <div class="panel-heading">
				<div class="panel-title">Custom Fields</div>
			</div>
    <table class="table table-bordered" id="result">
      <thead>
        <th><spring:message code="lbl.name" /></th>
        <th><spring:message code="lbl.datatype" /></th>
        <th><spring:message code="lbl.format" /></th>
        <th><spring:message code="lbl.isactive" /></th>
        <th><spring:message code="lbl.ismandatory" /></th>
        <th><spring:message code="lbl.enumvalues" /></th>
        <th><spring:message code="lbl.localText" /></th>
        <th><spring:message code="lbl.action" /></th>
      </thead>
      <c:choose>
      <c:when test="${not empty assetCategory.getCategoryProperties()}">
        <tbody>
          <c:forEach items="${assetCategory.categoryProperties}" var="categoryProperties" varStatus="vs">
            <tr id="resultrow${vs.index}">
              <td><form:hidden path="categoryProperties[${vs.index}].id"/>
              <form:input path="categoryProperties[${vs.index}].name"
                  class="form-control text-left patternvalidation" data-pattern="alphanumeric" maxlength="50" />
                <form:errors path="categoryProperties[${vs.index}].name" cssClass="error-msg" /></td>
              <td><form:select path="categoryProperties[${vs.index}].dataType" id="dataType"
                  cssClass="form-control" cssErrorClass="form-control error">
                  <form:option value="">
                    <spring:message code="lbl.select" />
                  </form:option>
                  <form:options items="${categoryPropertyDataTypes}" />
                </form:select> <form:errors path="categoryProperties[${vs.index}].dataType" cssClass="error-msg" /></td>
              <td><form:input path="categoryProperties[${vs.index}].format"
                  class="form-control text-left patternvalidation" data-pattern="alphanumeric"
                  maxlength="200" /> <form:errors path="categoryProperties[${vs.index}].format"
                  cssClass="error-msg" /></td>
              <td><form:checkbox path="categoryProperties[${vs.index}].isActive" value="categoryProperties[${vs.index}].isActive"/> <form:errors
                  path="categoryProperties[${vs.index}].isActive" cssClass="error-msg" /></td>
              <td><form:checkbox path="categoryProperties[${vs.index}].isMandatory" value="categoryProperties[${vs.index}].isMandatory"/> <form:errors
                  path="categoryProperties[${vs.index}].isMandatory" cssClass="error-msg" /></td>
              <td><form:textarea path="categoryProperties[${vs.index}].enumValues" 
                  class="form-control text-left patternvalidation" data-pattern="alphanumeric"
                  maxlength="300" /> <form:errors path="categoryProperties[${vs.index}].enumValues"
                  cssClass="error-msg" /></td>
              <td><form:input path="categoryProperties[${vs.index}].localText" 
                  class="form-control text-left patternvalidation" data-pattern="alphanumeric"
                  maxlength="200" /> <form:errors path="categoryProperties[${vs.index}].localText"
                  cssClass="error-msg" /></td>
              <td><span class="add-padding">
                  <button type="button" id="del-row" class="btn btn-primary" onclick="deleteThisRow(this)" data-idx="${vs.index}">Delete
                    Row</button> </i>
              </span></td>
            </tr>
          </c:forEach>
        </tbody>
      </c:when>
      <c:otherwise>
        <tbody>
          <tr id="resultrow0">
            <td><form:input path="categoryProperties[0].name" class="form-control text-left patternvalidation"
                data-pattern="alphanumeric" maxlength="50" /> <form:errors path="categoryProperties[0].name"
                cssClass="error-msg" /></td>
            <td><form:select path="categoryProperties[0].dataType" id="dataType" cssClass="form-control"
                cssErrorClass="form-control error">
                <form:option value="">
                  <spring:message code="lbl.select" />
                </form:option>
                <form:options items="${categoryPropertyDataTypes}" />
              </form:select> <form:errors path="categoryProperties[0].dataType" cssClass="error-msg" /></td>
            <td><form:input path="categoryProperties[0].format" class="form-control text-left patternvalidation"
                data-pattern="alphanumeric" maxlength="200" /> <form:errors path="categoryProperties[0].format"
                cssClass="error-msg" /></td>
            <td><form:checkbox path="categoryProperties[0].isActive" /> <form:errors
                path="categoryProperties[0].isActive" cssClass="error-msg" /></td>
            <td><form:checkbox path="categoryProperties[0].isMandatory" /> <form:errors
                path="categoryProperties[0].isMandatory" cssClass="error-msg" /></td>
            <td><form:textarea path="categoryProperties[0].enumValues"
                class="form-control text-left patternvalidation" data-pattern="alphanumeric" maxlength="300" /> <form:errors
                path="categoryProperties[0].enumValues" cssClass="error-msg" /></td>
            <td><form:input path="categoryProperties[0].localText"
                class="form-control text-left patternvalidation" data-pattern="alphanumeric" maxlength="200" /> <form:errors
                path="categoryProperties[0].localText" cssClass="error-msg" /></td>    
            <td><span class="add-padding">
                <button type="button" id="del-row" class="btn btn-primary" onclick="deleteThisRow(this)" data-idx="0">Delete
                  Row</button> </i>
            </span></td>
          </tr>
        </tbody>
        </c:otherwise>   
        </c:choose>
    </table>
  </div>
  <div class="col-sm-12 text-center">
    <button type="button" id="addrow" class="btn btn-primary" >Add Row</button>
  </div>
</div>

