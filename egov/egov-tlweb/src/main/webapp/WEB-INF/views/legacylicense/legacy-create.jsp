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

<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib uri="http://www.springframework.org/tags/form" prefix="form" %>
<%@ taglib uri="http://www.springframework.org/tags" prefix="spring" %>
<%@ taglib uri="/WEB-INF/taglib/cdn.tld" prefix="cdn" %>
<%@ include file="/includes/taglibs.jsp" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/functions" prefix="fn" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt" %>

<div id="enterLicense_error" class="error-msg" style="display: none;"></div>
<div class="row">
    <div class="col-md-12">

        <form:form role="form" id="legacyLicenseForm"
                   modelAttribute="tradeLicense" commandName="tradeLicense" method="POST"
                   class="form-horizontal form-groups-bordered"
                   enctype="multipart/form-data">
        <div class="panel panel-primary" data-collapsed="0">
        
            <input type="hidden" id="feeTypeId" value="${feeTypeId}"/>
            <div class="panel-heading">
                <div class="panel-title" style="text-align: center">
                    <strong><spring:message code='page.title.entertrade'/></strong>
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
            <div class="panel-body custom-form">
                <div class="tab-content">
                    <div class="tab-pane fade active in" id="tradedetails">
                        <div class="form-group">
                            <label class="col-sm-3 control-label text-right"> <spring:message
                                    code='license.oldlicensenum'/><span class="mandatory"></span></label>
                            <div class="col-sm-3 add-margin">
                                <form:input path="oldLicenseNumber" id="oldLicenseNumber"
                                            class="form-control typeahead" placeholder=""
                                            autocomplete="off" maxlength="50" required="true"
                                            oninvalid="this.setCustomValidity('Please Enter the Old License Number')"
                                            oninput="setCustomValidity('')"
                                            Class="form-control patternvalidation"/>
                                <form:errors path="oldLicenseNumber" cssClass="error-msg"/>
                            </div>
                        </div>

                        <%@  include file='licensee.jsp' %>
                        <%@  include file='license-address.jsp' %>
                        <%@  include file='license.jsp' %>

                        <div class="panel-heading custom_form_panel_heading">
                            <div class="panel-title">
                                <spring:message code='license.title.feedetail'/>
                            </div>
                        </div>
                        <div class="col-md-12">
                            <table class="table table-bordered feedetails">
                                <thead>
                                <tr>
                                    <th><spring:message code='license.fin.year'/></th>
                                    <th><spring:message code='lbl.amount'/></th>
                                    <th class="text-center"><spring:message
                                            code='license.fee.paid.y.n'/></th>
                                </tr>
                                </thead>
                                <tbody>
                                <c:set value="" var="startfinyear"/>
                                <c:forEach items="${legacyInstallmentwiseFees}" var="LIFee"
                                           varStatus="stat">
                                    <c:if test="${stat.index < legacyInstallmentwiseFees.size()}">
                                    <tr>
                                        <c:set
                                                value="${fn:substring(LIFee.key,0, 4)}-${fn:substring(LIFee.key,2, 4)+1}"
                                                var="finyear"/>
												<c:forEach items="${legacyFeePayStatus}" var="status" varStatus="check">
													<c:if test="${stat.index==check.index}">
														<c:set value="${status.value}" var="checkbox"></c:set>
													</c:if>
												</c:forEach>
                                        <c:if test="${stat.index == 0}">
                                            <c:set value="${finyear}" var="startfinyear"/>
                                        </c:if>
                                        <input type="hidden" name="financialyear[${stat.index}]"
                                               value="${LIFee.key}">
                                        <td><input type="text" class="form-control feeyear"
                                                   readonly="readonly" value="${finyear}" 
                                                   tabindex="-1"/></td>
                                        <td ><input type="text"
                                                   name="legacyInstallmentwiseFees[${stat.index}]"
                                                   id="amountpaid${stat.index}"
                                                   class="form-control patternvalidation feeamount"
                                                   value="${LIFee.value}" 
                                                   data-pattern="number" maxlength="7"
                                                <c:if test="${stat.index > 5}">
                                                    readonly="readonly"
                                                </c:if>/>
                                        </td>
                                        <td class="text-center"><input type="checkbox"
													name="legacyFeePayStatus[${stat.index}]" 
													class="case"
													id="feestatus" 
													value="true"
													<c:if test="${checkbox==true}">
													 checked="checked"</c:if> />
										</td>
                                    </tr>
                                 </c:if>   
                                </c:forEach>
                                </tbody>
                                <tfoot>
                                <tr>
                                    <td class="error-msg" colspan="3"><spring:message
                                            code="license.legacy.info" arguments="${startfinyear}"/></td>
                                </tr>
                                </tfoot>
                            </table>
                        </div>
                    </div>
                    <div class="tab-pane fade" id="tradeattachments">
                        <div id="documents">
                            <%@  include file="legacy-supportdocs.jsp" %>
                        </div>
                    </div>
                    <div class="row">
                        <div class="text-center">
                            <button type="submit" id="btnsave"
                                    onclick="return validateForm();" class="btn btn-primary">
                                <spring:message code="lbl.save"/></button>
                            <form:button type="button" id="btnclose" class="btn btn-default"
                                         onclick="window.close();"><spring:message code="lbl.close"/></form:button>
                        </div>
                    </div>
                </div>
            </div>
            </form:form>
        </div>
    </div>
    <script>
        $(document).ready(function () {
            $("#oldLicenseNumber").change(function () {
                checkOldLicenseNumberUnique();
            });
        })
    </script>
    <script type="text/javascript"
            src="<cdn:url  value='/resources/js/app/legacy-license.js?rnd=${app_release_no}'/>"></script>
    <script type="text/javascript"
            src="<cdn:url  value='/resources/js/app/license-support-docs.js?rnd=${app_release_no}'/>"></script>
    <script type="text/javascript"
            src="<cdn:url  value='/resources/js/app/trade-license.js?rnd=${app_release_no}'/>"></script>
