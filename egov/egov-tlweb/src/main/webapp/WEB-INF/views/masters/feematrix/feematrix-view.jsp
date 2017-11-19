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

<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ include file="/includes/taglibs.jsp" %>
<%@ taglib uri="/WEB-INF/taglib/cdn.tld" prefix="cdn" %>

<div class="row">
    <div class="col-md-12">
        <c:if test="${not empty message}">
            <div class="alert alert-success" role="alert"><spring:message code="${message}"/></div>
        </c:if>
        <div class="panel panel-primary" data-collapsed="0">
            <div class="panel-heading">
                <div class="panel-title"><spring:message code="title.feematrix.view"/></div>
            </div>
            <div class="panel-body">
                <form:form role="form" action="create" modelAttribute="feeMatrix" id="feematrix-new" name="feematrix-new"
                           cssClass="form-horizontal form-groups-bordered">
                    <div class="form-group">
                        <label class="col-sm-3 control-label">
                            <spring:message code="lbl.licenseapptype"/><span class="mandatory"></span>
                        </label>
                        <div class="col-sm-3 add-margin">
                            <form:input path="licenseAppType.name" id="licenseAppType" cssClass="form-control" readonly="true" required="true"
                                        cssErrorClass="form-control error"/>
                        </div>
                        <label class="col-sm-3 control-label">
                            <spring:message code="lbl.natureofbusiness"/><span class="mandatory"></span>
                        </label>
                        <div class="col-sm-3 add-margin">
                            <form:input path="natureOfBusiness.name" id="natureOfBusiness" cssClass="form-control" readonly="true" required="true"
                                        cssErrorClass="form-control error"/>
                        </div>
                    </div>

                    <div class="form-group">
                        <label class="col-sm-3 control-label">
                            <spring:message code="lbl.licensecategory"/><span class="mandatory"></span>
                        </label>
                        <div class="col-sm-3 add-margin">
                            <form:select path="licenseCategory" id="licenseCategory" cssClass="form-control" required="true"
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
                            <form:select path="subCategory" id="subCategory" cssClass="form-control select2" required="true"
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
                            <form:select path="feeType" id="feeType" cssClass="form-control" required="true"
                                         cssErrorClass="form-control error" disabled="true">
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
                            <form:input path="financialYear.finYearRange" id="financialYear" cssClass="form-control" readonly="true" required="true"
                                        cssErrorClass="form-control error"/>
                        </div>
                    </div>
                    <div class="form-group">
                        <label class="col-sm-3 control-label">
                            <spring:message code="lbl.effective.from"/><span class="mandatory"></span>
                        </label>
                        <div class="col-sm-3 add-margin">
                            <form:input id="effectiveFrom" readonly="true" path="effectiveFrom" class="form-control text-left" maxlength="10"/>
                        </div>

                        <label class="col-sm-3 control-label">
                            <spring:message code="lbl.effective.to"/><span class="mandatory"></span>
                        </label>
                        <div class="col-sm-3 add-margin">
                            <form:input readonly="true" id="effectiveTo" path="effectiveTo" class="form-control text-left" maxlength="10"/>
                        </div>
                    </div>
                    <div class="panel-heading">
                        <div class="col-md-12 panel-title text-left">
                            <spring:message code="lbl.fee.details"/>
                        </div>
                    </div>
                    <div class="col-sm-12">
                        <table class="table table-bordered fromto" id="result" data-from="<spring:message code='lbl.uomfrom'/>"
                               data-to="<spring:message code='lbl.uomto'/>">
                            <thead>
                            <th><spring:message code="lbl.uomfrom"/></th>
                            <th><spring:message code="lbl.uomto"/></th>
                            <th><spring:message code="lbl.amount"/></th>
                            </thead>
                            <tbody>
                            <c:if test="${not empty feeMatrix.feeMatrixDetail}">
                                <c:forEach items="${feeMatrix.feeMatrixDetail}" var="detail" varStatus="vs">
                                    <tr data-create="no">
                                        <td>
                                            <input type="text" name="feeMatrixDetail[${vs.index}].uomFrom" value="${detail.uomFrom}"
                                                   class="form-control fromRange patternvalidation fromvalue"
                                                   pattern="-?\d*" data-pattern="numerichyphen" data-fromto="from"
                                                   maxlength="8" readonly="readonly" required="required"/>
                                        </td>
                                        <td>
                                            <input type="text" name="feeMatrixDetail[${vs.index}].uomTo" value="${detail.uomTo}"
                                                   class="form-control patternvalidation tovalue"
                                                   pattern="-?\d*" data-pattern="numerichyphen" data-fromto="to"
                                                   maxlength="8" readonly="readonly" required="required"/>
                                        </td>
                                        <td>
                                            <input type="text" name="feeMatrixDetail[${vs.index}].amount" value="${detail.amount}"
                                                   class="form-control patternvalidation" data-pattern="number" maxlength="8" readonly="readonly" required="required"/>
                                        </td>
                                    </tr>
                                </c:forEach>
                            </c:if>
                            </tbody>
                        </table>
                    </div>
                    <div class="form-group text-center">
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
</script>
<script src="<cdn:url  value='/resources/js/app/license-fee-matrix.js?rnd=${app_release_no}'/>"></script>