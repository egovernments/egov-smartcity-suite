<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib uri="http://www.springframework.org/tags" prefix="spring" %>
<%@ taglib uri="http://www.springframework.org/tags/form" prefix="form" %>

<script src="<c:url value='/resources/pgr/js/complaint-type.js'/>"></script>

<div class="row" id="page-content">
    <div class="col-md-12">
        <div class="panel" data-collapsed="0">
            <div class="panel-body">
                <c:if test="${not empty message}">
                    <div id="message" class="success">${message}</div>
                </c:if>

                <form:form id="addcomplaint" method="post"
                           class="form-horizontal form-groups-bordered" modelAttribute="complaintType">

                    <div class="form-group">
                        <label class="col-sm-3 control-label">
                            <spring:message code="lbl.complaintType"/>
                        </label>

                        <div class="col-sm-6 add-margin">
                            <form:input path="name" id="comp_type_name" cssClass="form-control" cssErrorClass="form-control error"/>
                            <form:errors path="name" cssClass="error-msg"/>
                        </div>
                    </div>

                    <div class="form-group">
                        <label class="col-sm-3 control-label">
                            <spring:message code="lbl.department"/>
                        </label>

                        <div class="col-sm-6 add-margin">
                            <form:select path="department"
                                         id="comp_type_dept" cssClass="form-control" cssErrorClass="form-control error">
                                <form:option value=""> <spring:message code="lbl.select"/> </form:option>
                                <form:options items="${departments}" itemValue="deptCode" itemLabel="deptName"/>
                            </form:select>
                            <form:errors path="department" cssClass="error-msg"/>
                        </div>

                    </div>

                    <div class="form-group">
                        <label class="col-sm-3 control-label"><spring:message code="lbl.complaintType.nod"/></label>

                        <div class="col-sm-6 add-margin">
                            <form:input path="daysToResolve" type="text" class="form-control" cssErrorClass="form-control error" id="comp_type_nod"
                                        placeholder="" maxlength="2"/>
                            <form:errors path="daysToResolve" cssClass="error-msg"/>
                        </div>
                    </div>

                    <div class="form-group">
                        <label class="col-sm-3 control-label"><spring:message code="lbl.complaintType.loc"/></label>

                        <div class="col-sm-6">
                            <form:radiobutton path="locationRequired" id="comp_type_loc_yes" value="yes"/>
                            <label>Yes</label>
                            <form:radiobutton path="locationRequired" id="comp_type_loc_yno" value="no"/>
                            <label>No</label>
                        </div>
                    </div>

                    <div class="form-group">
                        <div class="text-center">
                            <button type="submit" class="btn btn-success"><spring:message code="lbl.submit"/></button>
                            <button type="reset" class="btn btn-default"><spring:message code="lbl.reset"/></button>
                        </div>
                    </div>
                </form:form>
            </div>
        </div>
    </div>
</div>