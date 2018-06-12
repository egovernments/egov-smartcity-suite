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
<%@ taglib uri="http://www.springframework.org/tags" prefix="spring" %>

<div class="panel panel-primary" data-collapsed="0">
    <div class="panel-body custom-form">
        <div class="panel-heading custom_form_panel_heading">
            <div class="panel-title">
                <spring:message code="lbl.approverdetails"/>
            </div>
        </div>
        <input type="hidden" id="additionalRule" name="workflowContainer.additionalRule"
               value="${tradeLicense.workflowContainer.additionalRule}"/>
        <c:choose>
            <c:when test="${isEmployee}">
                <c:choose>
                    <c:when test="${!tradeLicense.state.isEnded()}">
                        <input type="hidden" id="currentState" name="workflowContainer.currentState"
                               value="${tradeLicense.state.value}"/>
                    </c:when>
                    <c:otherwise>
                        <input type="hidden" id="currentState" name="workflowContainer.currentState" value=""/>
                    </c:otherwise>
                </c:choose>
                <input type="hidden" id="currentDesignation" name="workflowContainer.currentDesignation"
                       value="${tradeLicense.workflowContainer.currentDesignation}"/>
                <c:if test="${forwardEnabled}">
                    <input type="hidden" id="stateType" value="${tradeLicense.stateType}"/>
                    <input type="hidden" id="amountRule" name="workflowContainer.amountRule"
                           value="${tradeLicense.workflowContainer.amountRule}"/>
                    <input type="hidden" id="pendingActions" name="workflowContainer.pendingActions"
                           value="${tradeLicense.workflowContainer.pendingActions}"/>
                    <input type="hidden" name="workflowContainer.approverName" id="approverName"/>
                    <div class="form-group">
                        <label class="col-sm-3 control-label text-right"><spring:message
                                code='lbl.approverdepartment'/><span class="mandatory"/></label>
                        <div class="col-sm-3 add-margin">
                            <form:select path="workflowContainer.approverDepartment" id="approvalDepartment"
                                         Class="form-control">
                                <form:option value="">
                                    <spring:message code="lbl.select"/>
                                </form:option>
                                <form:options items="${approvalDepartmentList}" itemValue="id" itemLabel="name"/>
                            </form:select>
                        </div>
                        <label class="col-sm-2 control-label text-right"><spring:message
                                code='lbl.approverdesignation'/><span class="mandatory"/></label>
                        <div class="col-sm-3 add-margin">
                            <form:select path="workflowContainer.approverDesignation" id="approvaldesignation"
                                         class="form-control">
                                <form:option value="">
                                    <spring:message code="lbl.select"/>
                                </form:option>
                            </form:select>
                            <form:errors path="workflowContainer.approverDesignation"
                                         cssClass="add-margin error-msg"/>
                        </div>
                    </div>
                    <div class="form-group">
                        <label class="col-sm-3 control-label text-right"><spring:message
                                code="lbl.approver"/><span class="mandatory"/></label>
                        <div class="col-sm-3 add-margin">
                            <form:select path="workflowContainer.approverPositionId" data-first-option="false"
                                         id="approverPositionId" cssClass="form-control">
                                <form:option value="">
                                    <spring:message code="lbl.select"/>
                                </form:option>
                            </form:select>
                            <form:errors path="workflowContainer.approverPositionId"
                                         cssClass="add-margin error-msg"/>
                        </div>
                    </div>
                </c:if>
            </c:when>
        </c:choose>
        <div class="row">
            <label class="col-sm-3 control-label text-right"><spring:message
                    code="license.remarks"/><span class="mandatory"/></label>
            <div class="col-sm-8 add-margin">
                <form:textarea class="form-control" path="workflowContainer.approverComments"
                               id="approvalComment" required="required"/>
                <form:errors path="workflowContainer.approverComments" cssClass="add-margin error-msg"/>
            </div>
        </div>
    </div>
</div>
<div class="buttonbottom" align="center">
    <input type="hidden" id="workFlowAction" name="workflowContainer.workFlowAction"/>
    <table>
        <tr>
            <td>
                <c:forEach items="${validActionList}" varStatus="stat" var="name">
                <c:if test="${name!=''}">
            <td>
                <input type="button" class="buttonsubmit custom-button" value="${name}"
                       id="${name}" name="${name}" style="margin:0 5px"/>
            </td>
            </c:if>
            </c:forEach>
            <td><input type="button" name="button2" id="button2" value="Close"
                       class="button" onclick="window.close();" style="margin:0 5px"/></td>
            </td>
        </tr>
    </table>
</div>
<script>
    var nextAction = '${nextAction}';
</script>