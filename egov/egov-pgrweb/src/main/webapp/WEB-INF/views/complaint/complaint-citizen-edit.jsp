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
<%@ taglib uri="http://www.springframework.org/tags" prefix="spring" %>
<%@ taglib uri="http://www.springframework.org/tags/form" prefix="form" %>
<%@ taglib uri="/WEB-INF/taglib/cdn.tld" prefix="cdn" %>

<jsp:include page="view-complaint.jsp"></jsp:include>

<c:if test="${complaintStatus != 'WITHDRAWN'}">
    <div class="panel panel-primary" data-collapsed="0">
        <div class="panel-heading">
            <div class="panel-title">
                <strong><spring:message code="lbl.actions"/></strong>
            </div>
        </div>
        <div class="panel-body">

            <form:form id="complaintUpdate" modelAttribute="complaint" method="post" role="form"
                       class="form-horizontal form-groups-bordered" enctype="multipart/form-data">
                <div class="form-group">
                    <div class="col-md-3 add-margin">
                        <spring:message code="lbl.include.message"/><span class="mandatory"></span>
                    </div>
                    <div class="col-md-9 add-margin">
					    <textarea class="form-control" id="inc_messge" placeholder="" required="required"
                              maxlength="400" name="approvalComent"></textarea>
                        <input type="hidden" id="currentstatus" value="${complaint.status.id}">
                        <form:hidden path="statusUpdate" id="statusUpdate" value="true"/>
                        <form:errors path="" cssClass="error-msg"/>
                    </div>
                </div>
                <c:if test="${complaintStatus == 'COMPLETED'}">
                    <div class="form-group">
                        <div class="col-md-3 col-xs-6 add-margin" title="<spring:message code='lbl.feedback.tooltip'/>">
                            <spring:message code="lbl.feedback"/><span class="mandatory"></span>
                        </div>
                        <div class="col-md-3 col-xs-6 add-margin" title="<spring:message code='lbl.feedback.tooltip'/>">
                            <input id="citizenRating" type="hidden" class="rating" data-filled="fa fa-star fa-2x symbol-filled"
                                   data-empty="fa fa-star-o fa-2x" name="citizenRating"/>
                        </div>
                    </div>
                </c:if>
                <div class="form-group">
                    <div class="text-center">
                        <c:forEach items="${status}" var="validStatus">
                            <spring:message code="btn.status.${validStatus.name}" var="btnname"/>
                            <button type="submit" class="btn btn-primary" title="<spring:message code='btn.status.title'  arguments="${btnname}"/>">
                                ${btnname}
                                <form:hidden path="status" id="status" value="${validStatus.id}"/>
                            </button>
                        </c:forEach>
                        <button type="button" id="submitbtn" class="btn btn-primary">
                            <spring:message code="lbl.submit"/>
                        </button>
                        <button type="reset" class="btn btn-default">
                            <spring:message code="lbl.reset"/>
                        </button>
                        <button type="button" class="btn btn-default" onclick="window.close();"><spring:message code="lbl.close"/></button>
                    </div>
                </div>
            </form:form>
        </div>
    </div>
</c:if>
<c:if test="${complaintStatus == 'WITHDRAWN'}">
    <div class="form-group">
        <div class="text-center">
            <button type="button" class="btn btn-default" onclick="window.close();"><spring:message code="lbl.close"/></button>
        </div>
    </div>
</c:if>
<style>
    .symbol-filled {
        color: #f5861f;
    }
</style>
<script>
    $(document).ready(function () {
        $("select").each(function () {
            $(this).find('option').eq(0).prop('selected', true);
        });
        var feedback = '${citizenRating}';
        $("#citizenRating").val(feedback);

        $("#submitbtn").click(function () {
            $("#statusUpdate").val(false);
            $("#status").val($("#currentstatus").val());
            $("#complaintUpdate").submit();
        });
    });
</script>

<script src="<cdn:url  value='/resources/global/js/bootstrap/bootstrap-rating.min.js' context='/egi'/>"></script>
<script src="<cdn:url value='/resources/global/js/egov/inbox.js?rnd=${app_release_no}' context='/egi'/>"></script>