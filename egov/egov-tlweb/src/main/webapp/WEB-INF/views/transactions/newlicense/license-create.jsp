<%--
  ~    eGov  SmartCity eGovernance suite aims to improve the internal efficiency,transparency,
  ~    accountability and the service delivery of the government  organizations.
  ~
  ~     Copyright (C) 2018  eGovernments Foundation
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
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib uri="http://www.springframework.org/tags/form" prefix="form" %>
<%@ taglib uri="http://www.springframework.org/tags" prefix="spring" %>
<%@ taglib uri="/WEB-INF/taglib/cdn.tld" prefix="cdn" %>
<%@ include file="/includes/taglibs.jsp" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/functions" prefix="fn" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt" %>

<div class="row">
    <div class="col-md-12">
        <c:if test="${not empty message}">
            <div class="alert alert-danger" role="alert"><spring:message code="${message}"/></div>
        </c:if>
        <form:form role="form" id="licenseForm"
                   modelAttribute="tradeLicense" method="POST"
                   class="form-horizontal form-groups-bordered"
                   enctype="multipart/form-data">
            <div class="panel panel-primary" data-collapsed="0">
                <div class="panel-heading">
                    <div class="panel-title" style="text-align: center">
                        <strong><spring:message code="lbl.license.application"
                                                arguments="${tradeLicense.licenseAppType.name}"/></strong>
                    </div>
                    <ul class="nav nav-tabs" id="settingstab">
                        <li class="active"><a data-toggle="tab" href="#tradedetails"
                                              data-tabidx="0" aria-expanded="true"> <spring:message
                                code="license.tradedetail"/></a></li>
                        <li class=""><a data-toggle="tab" href="#tradeattachments"
                                        data-tabidx="1" aria-expanded="false"> <spring:message
                                code="license.support.docs"/></a></li>
                    </ul>
                    <div class="text-center error-msg" style="font-size: 14px;">
                        <form:errors path=""/>
                    </div>
                </div>
                <input type="hidden" id="licenseId" value="${tradeLicense.id}"/>
                <input type="hidden" id="feeTypeId" value="${feeTypeId}"/>
                <input type="hidden" id="enableFields" value="${enableFields}"/>
                <div class="panel-body custom-form">
                    <div class="tab-content">
                        <div class="tab-pane fade active in" id="tradedetails">
                            <c:if test="${tradeLicense.applicationNumber != 'Auto'}">
                                <div class="panel-body">
                                    <div class="row">
                                        <div class="col-sm-3 text-right add-margin">
                                            <spring:message code='license.applicationnumber'/>
                                        </div>
                                        <div class="col-sm-3 add-margin view-content">
                                                ${tradeLicense.applicationNumber}
                                        </div>
                                    </div>
                                </div>
                            </c:if>
                            <div class="panel-heading custom_form_panel_heading">
                                <div class="panel-title">
                                    <spring:message code='license.title.applicantdetails'/>
                                </div>
                            </div>
                            <div class="row ">
                                <label class="col-sm-3 control-label text-right"><spring:message
                                        code='licensee.aadhaarNo'/></label>
                                <div class="col-sm-3 add-margin" style="margin-bottom: 15px;">
                                    <form:input path="licensee.uid" id="adhaarId"
                                                Class="form-control patternvalidation" data-pattern="number"
                                                value="${licensee.uid}" maxlength="12" minlength="12"
                                                placeholder="" autocomplete="off"/>
                                    <form:errors path="licensee.uid" class="error-msg tradelicenceerror"/>
                                </div>
                                <label class="col-sm-2 control-label text-right"><spring:message
                                        code='search.licensee.mobileNo'/><span class="mandatory"></span></label>
                                <div class="col-sm-3 add-margin" style="margin-bottom:15px;">
                                    <div class="input-group">
                                        <span class="input-group-addon" id="basic-addon1">+91</span>
                                        <form:input path="licensee.mobilePhoneNumber" id="mobilePhoneNumber"
                                                    minlength="10" maxlength="10" required="true"
                                                    cssClass="form-control patternvalidation" data-pattern="number"
                                                    value="${licensee.mobilePhoneNumber}"/>
                                    </div>
                                    <form:errors path="licensee.mobilePhoneNumber"
                                                 class="error-msg all-errors tradelicenceerror"/>
                                </div>
                            </div>
                            <div class="form-group">
                                <label class="col-sm-3 control-label text-right"><spring:message
                                        code='licensee.applicantname'/><span class="mandatory"></span></label>
                                <div class="col-sm-3 add-margin">
                                    <form:input path="licensee.applicantName" id="applicantName"
                                                value="${licensee.applicantName}"
                                                cssClass="form-control patternvalidation"
                                                maxlength="250" placeholder="" autocomplete="off"
                                                data-pattern="alphabetwithspace" required="true"/>
                                    <form:errors path="licensee.applicantName" class="error-msg tradelicenceerror"/>
                                </div>
                                <label class="col-sm-2 control-label text-right"><spring:message
                                        code='licensee.fatherorspousename'/><span class="mandatory"></span></label>
                                <div class="col-sm-3 add-margin">
                                    <form:input path="licensee.fatherOrSpouseName" id="fatherOrSpouseName"
                                                value="${licensee.fatherOrSpouseName}" class="form-control "
                                                maxlength="250" placeholder=""
                                                cssClass="form-control patternvalidation"
                                                data-pattern="alphabetwithspace" required="true"/>
                                    <form:errors path="licensee.fatherOrSpouseName"
                                                 cssClass="error-msg tradelicenceerror"/>
                                </div>
                            </div>
                            <div class="form-group">
                                <label class="col-sm-3 control-label text-right"><spring:message
                                        code='lbl.emailid'/><span class="mandatory"></span></label>
                                <div class="col-sm-3 add-margin">
                                    <form:input path="licensee.emailId" id="emailId"
                                                value="${licensee.emailId}" class="form-control" maxlength="64"
                                                placeholder="abc@xyz.com" type="email" required="true"/>
                                    <form:errors path="licensee.emailId" cssClass="error-msg"/>
                                </div>
                                <label class="col-sm-2 control-label text-right"><spring:message
                                        code='licensee.address'/><span class="mandatory"></span></label>
                                <div class="col-sm-3 add-margin">
                                    <form:textarea path="licensee.address" id="licenseeAddress"
                                                   maxlength="250"
                                                   class="form-control typeahead" required="true"/>
                                    <form:errors path="licensee.address" cssClass="error-msg"/>
                                </div>
                            </div>
                            <div class="panel-heading custom_form_panel_heading">
                                <div class="panel-title">
                                    <spring:message code='license.location.lbl'/>
                                </div>
                            </div>
                            <div class="form-group">
                                <label class="col-sm-3 control-label text-right"><spring:message
                                        code='license.propertyNo.lbl'/></label>
                                <div class="col-sm-3 add-margin">
                                    <form:input path="assessmentNo" id="propertyNo"
                                                value="${tradeLicense.assessmentNo}" class="form-control"
                                                onblur="getPropertyDetails();" maxlength="15" placeholder=""
                                                autocomplete="off"/>
                                </div>
                                <label class="col-sm-2 control-label text-right"><spring:message
                                        code='lbl.locality'/> <span class="mandatory"></span></label>
                                <div class="col-sm-3 add-margin">
                                    <form:select path="boundary" id="boundary" class="form-control disableremoved"
                                                 required="true">
                                        <form:option value="">
                                            <spring:message code="lbl.select"/>
                                        </form:option>
                                        <form:options items="${boundary}" itemValue="id" itemLabel="name"/>
                                    </form:select>
                                    <form:errors path="boundary" cssClass="error-msg tradelicenceerror"/>
                                </div>
                            </div>
                            <div class="form-group">
                                <label class="col-sm-3 control-label text-right">
                                    <spring:message code='baseregister.ward'/>
                                    <span class="mandatory"></span>
                                </label>
                                <div class="col-sm-3 add-margin">
                                    <form:select path="parentBoundary" id="parentBoundary"
                                                 class="form-control disableremoved" required="true"
                                                 data-selected-id="${tradeLicense.parentBoundary.id}">
                                        <form:option value="">
                                            <spring:message code="lbl.select"/>
                                        </form:option>
                                        <form:options items="${parentBoundary}" itemValue="id" itemLabel="name"/>
                                    </form:select>
                                    <form:errors path="parentBoundary" cssClass="error-msg tradelicenceerror"/>
                                </div>
                                <label class="col-sm-2 control-label text-right">
                                    <spring:message code='lbl.admin.ward'/>
                                </label>
                                <div class="col-sm-3 add-margin">
                                    <form:select path="adminWard" id="adminWard"
                                                 class="form-control disableremoved"
                                                 data-selected-id="${tradeLicense.adminWard.id}">
                                        <form:option value="">
                                            <spring:message code="lbl.select"/>
                                        </form:option>
                                        <form:options items="${adminWard}" itemValue="id" itemLabel="name"/>
                                    </form:select>
                                    <form:errors path="adminWard" cssClass="error-msg tradelicenceerror"/>
                                </div>
                            </div>
                            <div class="form-group">
                                <label class="col-sm-3 control-label text-right">
                                    <spring:message code='license.ownerShipType.lbl'/>
                                    <span class="mandatory"></span>
                                </label>
                                <div class="col-sm-3 add-margin">
                                    <form:select path="ownershipType" id="ownershipType"
                                                 class="form-control " required="true">
                                        <form:option value="">
                                            <spring:message code="lbl.select"/>
                                        </form:option>
                                        <form:options items="${ownershipType}"/>
                                    </form:select>
                                    <form:errors path="ownershipType" cssClass="error-msg tradelicenceerror"/>
                                </div>
                                <label class="col-sm-2 control-label text-right">
                                    <spring:message code='license.address'/>
                                    <span class="mandatory"></span>
                                </label>
                                <div class="col-sm-3 add-margin">
                                    <form:textarea path="address" id="address" maxlength="250" class="form-control"
                                                   required="required"/>
                                    <form:errors path="address" cssClass="error-msg tradelicenceerror"/>
                                </div>
                            </div>
                            <div class="panel-heading custom_form_panel_heading">
                                <div class="panel-title">
                                    <spring:message code='license.tradedetail'/>
                                </div>
                            </div>
                            <div class="form-group">
                                <label class="col-sm-3 control-label text-right"><spring:message
                                        code='search.license.establishmentname'/> <span
                                        class="mandatory"></span></label>
                                <div class="col-sm-3 add-margin">
                                    <form:input path="nameOfEstablishment" id="nameOfEstablishment"
                                                value="${nameOfEstablishment}" class="form-control newpatternvalidation"
                                                maxlength="250" required="true"/>
                                    <form:errors path="nameOfEstablishment" cssClass="error-msg tradelicenceerror"/>
                                </div>

                                <label class="col-sm-2 control-label text-right"><spring:message
                                        code='license.tradeType.lbl'/> <span class="mandatory"></span></label>
                                <div class="col-sm-3 add-margin">
                                    <form:select path="natureOfBusiness" id="natureOfBusiness"
                                                 cssClass="form-control" ErrorClass="form-control error"
                                                 required="true">
                                        <form:option value="">
                                            <spring:message code="lbl.select"/>
                                        </form:option>
                                        <form:options items="${natureOfBusiness}" itemValue="id"
                                                      itemLabel="name"/>
                                    </form:select>
                                    <form:errors path="nameOfEstablishment" cssClass="error-msg tradelicenceerror"/>
                                </div>
                            </div>
                            <div class="form-group">
                                <label class="col-sm-3 control-label text-right"><spring:message
                                        code='license.category.lbl'/><span class="mandatory"></span></label>
                                <div class="col-sm-3 add-margin">
                                    <form:select path="category" id="category" cssClass="form-control "
                                                 required="true">
                                        <form:option value="">
                                            <spring:message code="lbl.select"/>
                                        </form:option>
                                        <form:options items="${category}" itemValue="id" itemLabel="name"/>
                                    </form:select>
                                    <form:errors path="category" cssClass="error-msg tradelicenceerror"/>
                                </div>
                                <label class="col-sm-2 control-label text-right"><spring:message
                                        code='license.subCategory.lbl'/><span
                                        class="mandatory"></span></label>
                                <div class="col-sm-3 add-margin">
                                    <form:select path="tradeName" id="subCategory"
                                                 cssClass="form-control select2"
                                                 data-selected-id="${tradeLicense.tradeName.id}"
                                                 data-selected-text="${tradeLicense.tradeName.name}" required="true">
                                        <form:option value="">
                                            <spring:message code="lbl.select"/>
                                        </form:option>
                                        <form:options items="${tradeName}" itemValue="id" itemLabel="name"/>
                                    </form:select>
                                    <form:errors path="tradeName" cssClass="error-msg tradelicenceerror"/>
                                </div>
                            </div>
                            <div class="form-group">
                                <label class="col-sm-3 control-label text-right"><spring:message
                                        code='license.uom.lbl'/><span class="mandatory"></span></label>
                                <div class="col-sm-3 add-margin">
                                    <form:input path="tradeName.licenseSubCategoryDetails[0].uom.name"
                                                id="uom" value="${tradeName.licenseSubCategoryDetails[0].uom.name}"
                                                readOnly="true" class="form-control" required="true"/>
                                    <form:errors path="tradeName.licenseSubCategoryDetails[0].uom.name"
                                                 cssClass="error-msg tradelicenceerror"/>
                                </div>
                                <label class="col-sm-2 control-label text-right"><spring:message
                                        code='license.premises.lbl'/><span class="mandatory"></span></label>
                                <div class="col-sm-3 add-margin">
                                    <form:input path="tradeArea_weight" id="tradeArea_weight"
                                                value="${tradeArea_weight}" maxlength="8"
                                                class="form-control patternvalidation" required="true"
                                                data-pattern="number"/>
                                    <form:errors path="tradeArea_weight" cssClass="error-msg tradelicenceerror"/>
                                </div>
                            </div>
                            <div class="form-group">
                                <label class="col-sm-3 control-label text-right"><spring:message
                                        code='license.remarks'/></label>
                                <div class="col-sm-3 add-margin">
                                    <form:input path="remarks" id="remarks" value="${remarks}"
                                                maxlength="250" class="form-control"/>
                                    <form:errors path="remarks" cssClass="error-msg"/>
                                </div>
                                <label class="col-sm-2 control-label text-right">
                                    <spring:message code='license.startdate'/><span class="mandatory"></span>
                                </label>
                                <div class="col-sm-3 add-margin">
                                    <fmt:formatDate type="date" value="${commencementDate}"
                                                    pattern="dd/MM/yyyy" var="commencementDateFrmttd"/>
                                    <form:input path="commencementDate" id="startDate"
                                                value="${commencementDateFrmttd}"
                                                format="dd/MM/yyyy"
                                                class="form-control datepicker"
                                                required="true" maxlength="10"/>
                                    <form:errors path="commencementDate" cssClass="error-msg tradelicenceerror"/>
                                </div>
                            </div>
                            <div class="form-group">
                                <label class="col-sm-3 control-label text-right">
                                    <spring:message code='license.traderCheckbox.lbl'/>
                                </label>
                                <div class="col-sm-3 add-margin">
                                    <c:if test="${tradeLicense.agreementDate !=null}">
                                        <c:set value="${true}" var="showdetail"/>
                                    </c:if>
                                    <input type="checkbox" name=""
                                           id="showAgreementDtl" theme="simple" onclick="showHideAgreement()" <c:if
                                            test="${showdetail}">
                                        checked="checked"</c:if>/>
                                </div>
                            </div>
                            <div id="agreementSec" style="display: none">
                                <div class="panel-heading custom_form_panel_heading">
                                    <div class="panel-title">
                                        <spring:message code='license.AgreementDetails.lbl'/>
                                    </div>
                                </div>
                                <div class="form-group">
                                    <label class="col-sm-3 control-label text-right"><spring:message
                                            code='license.agreementDate.lbl'/><span class="mandatory"></span>
                                    </label>
                                    <div class="col-sm-3 add-margin">
                                        <fmt:formatDate type="date" value="${trdaeLicense.agreementDate}"
                                                        pattern="dd/MM/yyyy" var="agreementDateFrmttd"/>
                                        <form:input path="agreementDate" id="agreementDate"
                                                    value="${agreementDateFrmttd}"
                                                    Class="form-control datepicker"
                                                    maxlength="10"/>
                                        <form:errors path="agreementDate" cssClass="error-msg tradelicenceerror"/>
                                    </div>
                                    <label class="col-sm-2 control-label text-right"><spring:message
                                            code='license.agreementDocNo.lbl'/><span class="mandatory"></span></label>
                                    <div class="col-sm-3 add-margin">
                                        <form:input path="agreementDocNo" id="agreementDocNo"
                                                    value="${tradeLicense.agreementDocNo}" maxlength="50"
                                                    Class="form-control patternvalidation"
                                                    data-pattern="alphanumerichyphenbackslash"/>
                                        <form:errors path="agreementDocNo" cssClass="error-msg tradelicenceerror"/>
                                    </div>
                                </div>
                            </div>
                        </div>
                        <div class="tab-pane fade" id="tradeattachments">
                            <div id="documents">
                                <jsp:include page="license-supportdocs.jsp"/>
                            </div>
                        </div>
                    </div>
                </div>
            </div>
            <c:set value="${outstandingFee}" var="feeInfo"></c:set>
            <c:if test="${feeInfo.size()> 0}">
                <div class="panel panel-primary" data-collapsed="0">
                    <div class="panel-heading  custom_form_panel_heading subheadnew">
                        <div class="panel-title">
                            <spring:message code='license.title.feedetail'/>
                        </div>
                    </div>
                    <div class="panel-body">
                        <table class="table table-bordered" style="width: 97%; margin: 0 auto;">
                            <thead>
                            <tr>
                                <th><spring:message code='lbl.feetype'/></th>
                                <th><spring:message code='lbl.current'/></th>
                                <th><spring:message code='license.fee.arrears'/></th>
                            </tr>
                            </thead>
                            <tbody>
                            <c:forEach items="${feeInfo}" var="fee" varStatus="status">
                                <tr>
                                    <td>${fee.key}</td>
                                    <td>${fee.value['current']}</td>
                                    <td>${fee.value['arrear']}</td>
                                </tr>
                            </c:forEach>
                            </tbody>
                            <tfoot>
                            <tr>
                                <td colspan="3">
                                    <div style="text-align:center">
                                        <a name="viewdcb" class="btn btn-secondary align-right" id="viewdcb"
                                           onclick="window.open('/tl/dcb/view/'+ ${tradeLicense.id}, '_blank', 'height=650,width=980,scrollbars=yes,left=0,top=0,status=yes');">
                                            <spring:message code='lbl.show.dcb'/></a>
                                    </div>
                                </td>
                            </tr>
                            </tfoot>
                        </table>
                    </div>
                </div>
            </c:if>
            <c:if test="${not empty licenseHistory && !tradeLicense.state.isEnded()}">
                <div class="panel panel-primary" data-collapsed="0">
                    <div class="panel-heading">
                        <div class="panel-title">
                                ${tradeLicense.state.natureOfTask}
                            <spring:message code='lbl.licensehistory'/>
                        </div>
                    </div>
                    <div class="panel-body">
                        <div class="row add-border">
                            <table class="table table-bordered"
                                   style="width:97%;margin:0 auto;">
                                <thead>
                                <tr>
                                    <th class="bluebgheadtd"><spring:message code="lbl.wf.date"/></th>
                                    <th class="bluebgheadtd"><spring:message code="lbl.wf.updatedby"/></th>
                                    <th class="bluebgheadtd"><spring:message code="lbl.wf.currentowner"/></th>
                                    <th class="bluebgheadtd"><spring:message code="lbl.wf.status"/></th>
                                    <th class="bluebgheadtd"><spring:message code="lbl.wf.comments"/></th>
                                </tr>
                                </thead>
                                <tbody>
                                <c:forEach items="${licenseHistory}" varStatus="stat"
                                           var="history">
                                    <tr>
                                        <td class="blueborderfortd" style="text-align: left">${history.date}</td>
                                        <td class="blueborderfortd" style="text-align: left">${history.updatedBy}</td>
                                        <td class="blueborderfortd" style="text-align: left">${history.user}</td>
                                        <td class="blueborderfortd" style="text-align: left">${history.status}</td>
                                        <td class="blueborderfortd" style="text-align: left">${history.comments}</td>
                                    </tr>
                                </c:forEach>
                                </tbody>
                            </table>
                        </div>
                    </div>
                </div>
            </c:if>
            <div id="workflowDiv">
                <jsp:include page="../common/license-approver-detail.jsp"/>
            </div>
            <div class="row">
                <c:if test="${tradeLicense.state!=null}">
                    <div class="text-center">
                        <button type="button" id="btsave" class="btn btn-primary savebutton"
                                style="margin:0 5px">
                            <spring:message code="lbl.save"/>
                        </button>
                    </div>
                </c:if>
            </div>
        </form:form>
    </div>
</div>
<jsp:include page="../common/process-owner-reassignment.jsp"/>
<script>
    var digiSignEnabled = '${digiSignEnabled}';
</script>
<script type="text/javascript"
        src="<cdn:url  value='/resources/js/app/license-create.js?rnd=${app_release_no}'/>"></script>
<script type="text/javascript"
        src="<cdn:url  value='/resources/js/app/license-create-approval-detail.js?rnd=${app_release_no}'/>"></script>