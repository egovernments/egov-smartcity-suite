<%--
  ~ eGov suite of products aim to improve the internal efficiency,transparency,
  ~ accountability and the service delivery of the government  organizations.
  ~
  ~  Copyright (C) 2017  eGovernments Foundation
  ~
  ~  The updated version of eGov suite of products as by eGovernments Foundation
  ~  is available at http://www.egovernments.org
  ~
  ~  This program is free software: you can redistribute it and/or modify
  ~  it under the terms of the GNU General Public License as published by
  ~  the Free Software Foundation, either version 3 of the License, or
  ~  any later version.
  ~
  ~  This program is distributed in the hope that it will be useful,
  ~  but WITHOUT ANY WARRANTY; without even the implied warranty of
  ~  MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
  ~  GNU General Public License for more details.
  ~
  ~  You should have received a copy of the GNU General Public License
  ~  along with this program. If not, see http://www.gnu.org/licenses/ or
  ~  http://www.gnu.org/licenses/gpl.html .
  ~
  ~  In addition to the terms of the GPL license to be adhered to in using this
  ~  program, the following additional terms are to be complied with:
  ~
  ~      1) All versions of this program, verbatim or modified must carry this
  ~         Legal Notice.
  ~
  ~      2) Any misrepresentation of the origin of the material is prohibited. It
  ~         is required that all modified versions of this material be marked in
  ~         reasonable ways as different from the original version.
  ~
  ~      3) This license does not grant any rights to any user of the program
  ~         with regards to rights under trademark law for use of the trade names
  ~         or trademarks of eGovernments Foundation.
  ~
  ~  In case of any queries, you can reach eGovernments Foundation at contact@egovernments.org.
  --%>

<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ include file="/includes/taglibs.jsp" %>
<%@ taglib uri="/WEB-INF/taglib/cdn.tld" prefix="cdn" %>

<div class="row">
    <div class="col-md-12">
        <div class="panel panel-primary" data-collapsed="0">
            <div class="panel-heading">
                <div class="panel-title"><spring:message code="title.feematrix.edit"/></div>
            </div>
            <div class="panel-body">
                <form:form role="form" modelAttribute="feeMatrix" id="feematrix-new" name="feematrix-new"
                           cssClass="form-horizontal form-groups-bordered" method="post">
                    <spring:hasBindErrors name="feeMatrix">
                        <form:errors path="natureOfBusiness" cssClass="error-msg add-margin"/><br/>
                    </spring:hasBindErrors>
                    <div class="form-group">
                        <label class="col-sm-3 control-label">
                            <spring:message code="lbl.licenseapptype"/><span class="mandatory"></span>
                        </label>
                        <div class="col-sm-3 add-margin">
                            <form:select path="licenseAppType" id="licenseAppType" cssClass="form-control" required="required"
                                         cssErrorClass="form-control error" disabled="true">
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
                            <form:select path="natureOfBusiness" id="natureOfBusiness" cssClass="form-control" required="required"
                                         cssErrorClass="form-control error" disabled="true">
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
                            <form:select path="licenseCategory" id="licenseCategory" cssClass="form-control" required="required"
                                         cssErrorClass="form-control error" disabled="true">
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
                            <form:select path="subCategory" id="subCategory" cssClass="form-control select2" required="required"
                                         cssErrorClass="form-control error select2" disabled="true">
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
                            <form:select path="feeType" id="feeType" cssClass="form-control" required="required"
                                         cssErrorClass="form-control error" disabled="true">
                                <form:option value="">
                                    <spring:message code="lbl.select"/>
                                </form:option>
                            </form:select>
                        </div>
                        <label class="col-sm-3 control-label">
                            <spring:message code="lbl.unitofmeasurement"/><span class="mandatory"></span>
                        </label>
                        <div class="col-sm-3 add-margin">
                            <form:select path="unitOfMeasurement" id="unitOfMeasurement" cssClass="form-control" required="required"
                                         cssErrorClass="form-control error" disabled="true">
                                <form:option value="">
                                    <spring:message code="lbl.select"/>
                                </form:option>
                            </form:select>
                        </div>
                    </div>

                    <div class="form-group">
                        <label class="col-sm-3 control-label">
                            <spring:message code="lbl.rateType"/>
                        </label>
                        <div class="col-sm-3 add-margin">
                            <form:input id="rateType" path="" class="form-control text-left" maxlength="32" disabled="true"/>
                        </div>

                        <label class="col-sm-3 control-label">
                            <spring:message code="lbl.financialyear"/><span class="mandatory"></span>
                        </label>
                        <div class="col-sm-3 add-margin">
                            <form:select path="financialYear" id="financialYear" cssClass="form-control" required="required"
                                         cssErrorClass="form-control error" disabled="true">
                                <form:option value="">
                                    <spring:message code="lbl.select"/>
                                </form:option>
                                <form:options items="${financialYears}" itemValue="id" itemLabel="finYearRange"/>
                            </form:select>
                        </div>
                    </div>
                    <div class="form-group">
                        <label class="col-sm-3 control-label">
                            <spring:message code="lbl.effective.from"/><span class="mandatory"></span>
                        </label>
                        <div class="col-sm-3 add-margin">
                            <form:input id="effectiveFrom" path="effectiveFrom" class="form-control text-left" maxlength="10" disabled="true"/>
                        </div>

                        <label class="col-sm-3 control-label">
                            <spring:message code="lbl.effective.to"/><span class="mandatory"></span>
                        </label>
                        <div class="col-sm-3 add-margin">
                            <form:input id="effectiveTo" path="effectiveTo" class="form-control text-left" maxlength="10" disabled="true"/>
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
                            <c:if test="${not empty feeMatrix.feeMatrixDetail}">
                                <c:forEach items="${feeMatrix.feeMatrixDetail}" var="detail" varStatus="vs">
                                    <c:set var="display" value="table-row"/>
                                    <c:if test="${detail.markedForRemoval}">
                                        <c:set var="display" value="none"/>
                                    </c:if>
                                    <tr style="display:${display}">
                                        <td>
                                            <form:input path="feeMatrixDetail[${vs.index}].uomFrom" value="${detail.uomFrom}"
                                                        cssClass="form-control fromRange patternvalidation fromvalue"
                                                        pattern="-?\d*" data-pattern="numerichyphen" data-fromto="from"
                                                        maxlength="8" readonly="true" required="true"/>
                                        </td>
                                        <td>
                                            <form:input path="feeMatrixDetail[${vs.index}].uomTo" value="${detail.uomTo}"
                                                        cssClass="form-control patternvalidation tovalue"
                                                        pattern="-?\d*" data-pattern="numerichyphen" data-fromto="to"
                                                        maxlength="8" required="true"/>
                                        </td>
                                        <td>
                                            <form:input path="feeMatrixDetail[${vs.index}].amount" value="${detail.amount}"
                                                        cssClass="form-control patternvalidation" data-pattern="number" maxlength="8" required="true"/>
                                            <form:hidden path="feeMatrixDetail[${vs.index}].markedForRemoval" id="feeMatrixDetail[${vs.index}].markedForRemoval"
                                                         value="${detail.markedForRemoval}" cssClass="markedForRemoval"/>
                                        </td>
                                        <td><span class="add-padding"><i class="fa fa-trash delete-row" aria-hidden="true"></i></span></td>
                                    </tr>
                                </c:forEach>
                            </c:if>
                            </tbody>
                        </table>
                    </div>
                    <div class="form-group text-center">
                        <button type="submit" class="btn btn-primary" id="submit">
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
    //for repopulating all autocomplete upon load
    var subCategory = '${feeMatrix.subCategory.id}';
    var feeType = '${feeMatrix.feeType.id}';
    var uom = '${feeMatrix.unitOfMeasurement.id}';
</script>
<script src="<cdn:url  value='/resources/js/app/license-fee-matrix.js?rnd=${app_release_no}'/>"></script>
<script src="<cdn:url  value='/resources/js/app/value-range-checker.js?rnd=${app_release_no}'/>"></script>
