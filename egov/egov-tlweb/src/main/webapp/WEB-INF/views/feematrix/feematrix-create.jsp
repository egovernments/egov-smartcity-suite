<%--
  ~ eGov suite of products aim to improve the internal efficiency,transparency,
  ~    accountability and the service delivery of the government  organizations.
  ~
  ~     Copyright (C) <2016>  eGovernments Foundation
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

<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ include file="/includes/taglibs.jsp" %>
<%@ taglib uri="/WEB-INF/taglib/cdn.tld" prefix="cdn" %>

<div class="row">
    <div class="col-md-12">
        <div class="panel panel-primary" data-collapsed="0">
            <div class="panel-heading">
                <div class="panel-title"><spring:message code="title.feematrix.new"/></div>
            </div>
            <div class="panel-body">
                <form:form role="form" action="create" modelAttribute="feeMatrix" id="feematrix-new" name="feematrix-new"
                           cssClass="form-horizontal form-groups-bordered">
                    <spring:hasBindErrors name="feeMatrix">
                        <form:errors path="" cssClass="error-msg add-margin"/><br/>
                    </spring:hasBindErrors>
                    <div class="form-group">
                        <label class="col-sm-3 control-label">
                            <spring:message code="lbl.licenseapptype"/><span class="mandatory"></span>
                        </label>
                        <div class="col-sm-3 add-margin">
                            <form:select path="licenseAppType" id="licenseAppType" cssClass="form-control" required="true"
                                         cssErrorClass="form-control error">
                                <form:option value="">
                                    <spring:message code="lbl.select"/>
                                </form:option>
                                <form:options items="${licenseAppTypeList}" itemValue="id" itemLabel="name"/>
                            </form:select>
                        </div>
                        <label class="col-sm-3 control-label">
                            <spring:message code="lbl.natureofbusiness"/><span class="mandatory"></span>
                        </label>
                        <div class="col-sm-3 add-margin">
                            <form:select path="natureOfBusiness" id="natureOfBusiness" cssClass="form-control" required="true"
                                         cssErrorClass="form-control error">
                                <form:option value="">
                                    <spring:message code="lbl.select"/>
                                </form:option>
                                <form:options items="${natureOfBusinessList}" itemValue="id" itemLabel="name"/>
                            </form:select>
                        </div>
                    </div>

                    <div class="form-group">
                        <label class="col-sm-3 control-label">
                            <spring:message code="lbl.licensecategory"/><span class="mandatory"></span>
                        </label>
                        <div class="col-sm-3 add-margin">
                            <form:select path="licenseCategory" id="licenseCategory" cssClass="form-control" required="true"
                                         cssErrorClass="form-control error">
                                <form:option value="">
                                    <spring:message code="lbl.select"/>
                                </form:option>
                                <form:options items="${licenseCategoryList}" itemValue="id" itemLabel="name"/>
                            </form:select>
                        </div>
                        <label class="col-sm-3 control-label">
                            <spring:message code="lbl.subcategory"/><span class="mandatory"></span>
                        </label>
                        <div class="col-sm-3 add-margin">
                            <form:select path="subCategory" id="subCategory" cssClass="form-control select2" required="true"
                                         cssErrorClass="form-control error select2">
                                <form:option value="">
                                    <spring:message code="lbl.select"/>
                                </form:option>
                            </form:select>
                        </div>
                    </div>

                    <div class="form-group">
                        <label class="col-sm-3 control-label">
                            <spring:message code="lbl.feetype"/><span class="mandatory"></span>
                        </label>
                        <div class="col-sm-3 add-margin">
                            <form:select path="feeType" id="feeType" cssClass="form-control" required="true"
                                         cssErrorClass="form-control error">
                                <form:option value="">
                                    <spring:message code="lbl.select"/>
                                </form:option>
                            </form:select>
                        </div>
                        <label class="col-sm-3 control-label">
                            <spring:message code="lbl.unitofmeasurement"/>
                        </label>
                        <div class="col-sm-3 add-margin">
                            <form:input path="" id="unitOfMeasurement" class="form-control" disabled="true"/>
                        </div>
                    </div>

                    <div class="form-group">
                        <label class="col-sm-3 control-label">
                            <spring:message code="lbl.rateType"/>
                        </label>
                        <div class="col-sm-3 add-margin">
                            <form:input id="rateType" path="" class="form-control text-left" disabled="true"/>
                        </div>

                        <label class="col-sm-3 control-label">
                            <spring:message code="lbl.financialyear"/><span class="mandatory"></span>
                        </label>
                        <div class="col-sm-3 add-margin">
                            <form:select path="financialYear" id="financialYear" cssClass="form-control" required="true"
                                         cssErrorClass="form-control error">
                                <form:option value="">
                                    <spring:message code="lbl.select"/>
                                </form:option>
                                <form:options items="${financialYears}" itemValue="id" itemLabel="finYearRange"/>
                            </form:select>
                            <c:forEach items="${financialYears}" var="finYear">
                                <fmt:formatDate value="${finYear.startingDate}" pattern="dd/MM/yyyy" var="startdate"/>
                                <fmt:formatDate value="${finYear.endingDate}" pattern="dd/MM/yyyy" var="enddate"/>
                                <input type="hidden" id="fin${finYear.id}" value="${startdate}-${enddate}">
                            </c:forEach>
                        </div>
                    </div>
                    <div class="form-group">
                        <label class="col-sm-3 control-label">
                            <spring:message code="lbl.effective.from"/><span class="mandatory"></span>
                        </label>
                        <div class="col-sm-3 add-margin">
                            <form:input id="effectiveFrom" path="effectiveFrom" class="form-control text-left" maxlength="10" readonly="true"/>
                        </div>

                        <label class="col-sm-3 control-label">
                            <spring:message code="lbl.effective.to"/><span class="mandatory"></span>
                        </label>
                        <div class="col-sm-3 add-margin">
                            <form:input id="effectiveTo" path="effectiveTo" class="datepicker form-control text-left" maxlength="10"/>
                            <form:errors path="effectiveTo" cssClass="add-margin error-msg"/>
                        </div>
                    </div>
                    <div class="panel-heading">
                        <div class="col-md-12 panel-title text-left">
                            <spring:message code="lbl.fee.details"/>
                            <button type="button" class="btn btn-secondary pull-right" id="addrow">
                                <i class="fa fa-plus-circle" aria-hidden="true"></i> &nbsp;Add Row
                            </button>
                        </div>
                    </div>
                    <div class="col-sm-12">
                        <table class="table table-bordered fromto" id="result" data-from="<spring:message code='lbl.uomfrom'/>"
                               data-to="<spring:message code='lbl.uomto'/>">
                            <thead>
                            <th><spring:message code="lbl.uomfrom"/></th>
                            <th><spring:message code="lbl.uomto"/></th>
                            <th><spring:message code="lbl.amount"/></th>
                            <th></th>
                            </thead>
                            <tbody>
                            <c:choose>
                                <c:when test="${not empty feeMatrix.feeMatrixDetail}">
                                    <c:forEach items="${feeMatrix.feeMatrixDetail}" var="feeMatrixDetail" varStatus="vs">
                                        <tr class="dynamicInput">
                                            <td>
                                                <input type="text" name="feeMatrixDetail[${vs.index}].uomFrom" value="${feeMatrixDetail.uomFrom}"
                                                       class="form-control fromRange patternvalidation fromvalue"
                                                       pattern="-?\d*" data-pattern="numerichyphen" data-fromto="from"
                                                       maxlength="8" readonly="readonly" required="required"/>
                                            </td>
                                            <td>
                                                <input type="text" name="feeMatrixDetail[${vs.index}].uomTo" value="${feeMatrixDetail.uomTo}"
                                                       class="form-control patternvalidation tovalue"
                                                       pattern="-?\d*" data-pattern="numerichyphen" data-fromto="to"
                                                       maxlength="8" required="required"/>
                                            </td>
                                            <td>
                                                <input type="text" name="feeMatrixDetail[${vs.index}].amount" value="${feeMatrixDetail.amount}"
                                                       class="form-control patternvalidation" data-pattern="number" maxlength="8" required="required"/>
                                            </td>
                                            <td><span class="add-padding"><i class="fa fa-trash delete-row" aria-hidden="true"></i></span></td>
                                        </tr>
                                    </c:forEach>
                                </c:when>
                                <c:otherwise>
                                    <tr>
                                        <td>
                                            <input type="text" name="feeMatrixDetail[0].uomFrom" value="0"
                                                   class="form-control patternvalidation fromvalue"
                                                   pattern="-?\d*" data-pattern="numerichyphen" data-fromto="from"
                                                   required="required" readonly="readonly"/>
                                        </td>
                                        <td>
                                            <input type="text" name="feeMatrixDetail[0].uomTo"
                                                   class="form-control patternvalidation tovalue"
                                                   pattern="-?\d*" data-pattern="numerichyphen" data-fromto="to"
                                                   required="required"/>
                                        </td>
                                        <td>
                                            <input type="text" name="feeMatrixDetail[0].amount"
                                                   class="form-control patternvalidation"
                                                   data-pattern="number" required="required"/>
                                        </td>
                                        <td><span class="add-padding"><i class="fa fa-trash delete-row" data-func="add" aria-hidden="true"></i></span></td>
                                    </tr>
                                </c:otherwise>
                            </c:choose>
                            </tbody>
                        </table>
                    </div>
                    <div class="form-group text-center">
                        <button type="submit" class="btn btn-primary" id="search">
                            <spring:message code="lbl.save"/>
                        </button>
                        <button type="button" class="btn btn-default" data-dismiss="modal" onclick="window.close();">
                            <spring:message code="lbl.close"/>
                        </button>
                    </div>
                </form:form>
            </div>
        </div>
    </div>
</div>
<script>
    //for repopulating all autocomplete upon validation error
    var subCategory = '${feeMatrix.subCategory.id}';
    var feeType = '${feeMatrix.feeType.id}';
</script>
<script src="<cdn:url  value='/resources/js/app/license-fee-matrix.js?rnd=${app_release_no}'/>"></script>
<script src="<cdn:url  value='/resources/js/app/value-range-checker.js?rnd=${app_release_no}'/>"></script>