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

<%@ include file="/includes/taglibs.jsp" %>

<div class="row">
    <div class="col-md-12">
        <form:form role="form" id="licenseForm" class="form-horizontal form-groups-bordered">
            <div class="panel panel-primary" data-collapsed="0">
                <div class="panel-heading">
                    <div class="panel-title" style="text-align: center">
                        <strong><spring:message code="title.licensefee.verification"/></strong>
                    </div>
                </div>
                <c:choose>
                    <c:when test="${not empty paymentdone}">
                        <div class="text-center">
                            <div class="alert alert-info align-center" role="alert"><spring:message
                                    code="${paymentdone}"/></div>
                            <button type="button" id="close" class="btn btn-default" onclick="window.close();">
                                <spring:message code="lbl.close"/>
                            </button>
                        </div>
                    </c:when>
                    <c:otherwise>
                        <c:set value="${outstandingFee}" var="feeInfo"></c:set>
                        <c:if test="${feeInfo.size()> 0}">
                            <div class="panel panel-primary" data-collapsed="0">
                                <div class="panel-heading  custom_form_panel_heading subheadnew">
                                    <div class="panel-title">
                                        <spring:message code='title.licensefee.detail'/>
                                    </div>
                                </div>
                                <table class="table table-bordered" style="width: 97%; margin: 0 auto;">
                                    <thead>
                                    <tr>
                                        <th><spring:message code='lbl.installment.year'/></th>
                                        <th><spring:message code='lbl.feetype'/></th>
                                        <th><spring:message code='lbl.amount'/></th>
                                    </tr>
                                    </thead>
                                    <tbody>
                                    <c:forEach items="${feeInfo}" var="installment" varStatus="status">
                                        <c:forEach items="${installment.value}" var="val" varStatus="status">
                                            <tr>
                                                <td>${installment.key}</td>
                                                <td>${val.key}</td>
                                                <td>${val.value}</td>
                                            </tr>
                                        </c:forEach>
                                    </c:forEach>
                                    </tbody>
                                </table>
                            </div>
                        </c:if>
                        <div style="text-align:center">
                            <a name="payment" class="btn btn-secondary align-right" id="payment"
                               onclick="window.open('/tl/license/fee/collection/'+ '${applicationNumber}' ,'_self');">
                                <spring:message code='lbl.payment'/></a>
                            <button type="button" id="btnclose" class="btn btn-default" onclick="window.close();">
                                <spring:message code="lbl.close"/>
                            </button>
                        </div>
                    </c:otherwise>
                </c:choose>
            </div>
        </form:form>
    </div>
</div>