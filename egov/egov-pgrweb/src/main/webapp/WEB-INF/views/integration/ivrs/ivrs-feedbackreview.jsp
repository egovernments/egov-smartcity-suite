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

<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib uri="http://www.springframework.org/tags" prefix="spring" %>
<%@ taglib uri="http://www.springframework.org/tags/form" prefix="form" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@ taglib uri="/WEB-INF/taglib/cdn.tld" prefix="cdn" %>

<div class="row">
    <div class="col-md-12">
        <c:if test="${not empty successMessage}">
            <div class="panel panel-primary" data-collapsed="0">
            <div class="alert alert-info view-content" role="alert"><spring:message code="${successMessage}" arguments="${complaint.crn}"/></div>
            <div class="text-center">
                <button type="button" class="btn btn-default" data-dismiss="modal" onclick="window.close();">
                    <spring:message code="lbl.close"/>
                </button>
            </div>
            </div>
        </c:if>
        <form:form class="form-horizontal form-groups-bordered" action=""
                   id="searchform" modelAttribute="ivrsFeedbackReview" method="post">
            <%@  include file='/WEB-INF/views/complaint/view-complaint.jsp' %>
            <div class="panel panel-primary" data-collapsed="0">
                <div class="panel-heading">
                    <div class="panel-title"><spring:message code="title.feedbackreason"/>
                    </div>
                </div>
                <div class="panel-body custom-form">
                    <div class="form-group">
                        <label class="col-sm-3 control-label text-right"><spring:message code="lbl.feedback.reason"/>
                            <span class="mandatory"></span>
                        </label>
                        <div class="col-sm-3 add-margin">
                            <c:forEach items="${feedbackReasons}" var="feedback">
                                <div class="radio">
                                    <label>
                                        <input type="radio" name="feedbackReason" id="feedbackReason"
                                               value="${feedback.id}"
                                                <c:if test="${feedback eq ivrsFeedbackReview.feedbackReason}"> checked="checked" </c:if>/>
                                            ${feedback.name}
                                    </label>
                                </div>
                            </c:forEach>
                        </div>
                    </div>
                    <form:hidden path="id" id="checkexisting"/>
                    <form:hidden path="rating" id="rating" value="${rating.id}"/>
                    <form:hidden path="complaint" id="complaint" value="${complaint.id}"/>
                    <form:hidden path="existing" id="isexisting"/>
                    <div class="form-group">
                        <label class="col-sm-3 control-label text-right"><spring:message code="lbl.feedBack"/>
                            <span class="mandatory"></span>
                        </label>
                        <div class="col-sm-6 add-margin" style="margin-bottom: 15px;">
                            <form:textarea path="detail" id="detail" maxlength="250" class="form-control"
                                           required="required"/>
                            <form:errors path="detail" cssClass="error-msg"/>
                        </div>
                    </div>
                    <div class="form-group">
                        <div class="text-center">
                            <button type="button" id="btnsubmit" class="btn btn-primary">
                                <spring:message code="lbl.submit"/>
                            </button>
                            <button type="button" class="btn btn-default" data-dismiss="modal"
                                    onclick="window.close();">
                                <spring:message code="lbl.close"/>
                            </button>
                        </div>
                    </div>
                </div>
            </div>
        </form:form>
    </div>
</div>
<script src="<cdn:url  value='/resources/js/app/ivrs-feedbackreview.js?rnd=${app_release_no}'/>"></script>
