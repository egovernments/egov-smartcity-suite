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
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt" %>
<%@ taglib uri="http://www.springframework.org/tags" prefix="spring" %>
<div class="panel panel-primary" data-collapsed="0">
    <div class="panel-heading slide-history-menu">
        <div class="panel-title">
            <spring:message code="lbl.apphistory"/>
        </div>
        <div class="history-icon">
            <i class="fa fa-angle-up fa-2x" id="toggle-his-icon"></i>
        </div>
    </div>
    <div class="panel-body history-slide display-hide">
        <div class="row add-border">
            <table class="table table-bordered"
                   style="width:97%;margin:0 auto;">
                <thead>
                <tr>
                    <th class="bluebgheadtd"><spring:message code="lbl.wf.date"/></th>
                    <th class="bluebgheadtd"><spring:message code="lbl.wf.updatedby"/></th>
                    <th class="bluebgheadtd"><spring:message code="lbl.wf.status"/></th>
                    <th class="bluebgheadtd"><spring:message code="lbl.currentowner"/></th>
                    <th class="bluebgheadtd"><spring:message code="lbl.department"/></th>
                    <th class="bluebgheadtd"><spring:message code="lbl.wf.comments"/></th>
                </tr>
                </thead>
                <tbody>
                <c:choose>
                    <c:when test="${!applicationHistory.isEmpty()}">
                        <c:forEach items="${applicationHistory}" var="history">
                            <tr>
                                <td class="blueborderfortd" style="text-align: left">
                                    <fmt:formatDate value="${history.date}" var="historyDate"
                                                    pattern="dd-MM-yyyy HH:mm a E"/>
                                        ${historyDate}</td>
                                <td class="blueborderfortd" style="text-align: left">
                                        ${history.updatedBy}</td>
                                <td class="blueborderfortd" style="text-align: left">
                                        ${history.status}</td>
                                <td class="blueborderfortd" style="text-align: left">
                                        ${history.user}</td>
                                <td class="blueborderfortd" style="text-align: left">
                                        ${history.department}</td>
                                <td class="blueborderfortd" style="text-align: left">
                                        ${history.comments}</td>
                            </tr>
                        </c:forEach>
                    </c:when>
                    <c:otherwise>
                        <div class="col-md-3 col-xs-6 add-margin"><spring:message
                                code="lbl.nohistorydetails.code"/></div>
                    </c:otherwise>
                </c:choose>
                </tbody>
            </table>
        </div>
    </div>
</div>
