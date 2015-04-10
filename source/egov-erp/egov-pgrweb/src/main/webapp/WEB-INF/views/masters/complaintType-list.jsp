<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib uri="http://www.springframework.org/tags" prefix="spring" %>
<%@ taglib uri="http://www.springframework.org/tags/form" prefix="form" %>

<div class="row" id="page-content">
    <div class="col-md-12">
        <div class="panel" data-collapsed="0">
            <div class="panel-body">
                <c:if test="${not empty message}">
                    <div id="message" class="success">${message}</div>
                </c:if>

                <form:form id="viewHierarchyType" method="post"   modelAttribute="complaintType" class="form-horizontal form-groups-bordered">
					<div class="panel panel-primary" data-collapsed="0">
						<div class="panel-heading">
							<div class="panel-title">
								<strong><spring:message code="title.searchComplaintType"></spring:message></strong>
							</div>
						</div> 
						
						<div class="panel-body custom-form">
							<div class="form-group">
								<label class="col-sm-3 control-label"><spring:message code="lbl.complaintType"></spring:message><small><i
										class="entypo-star error-msg"></i></small></label>
								<div class="col-sm-6 add-margin">
	                            <form:select path="name"
	                                         id="comp_type_dept" cssClass="form-control" cssErrorClass="form-control error" >
	                                <form:option value=""> <spring:message code="lbl.select"/> </form:option>
	                                <form:options items="${complaintTypes}" itemValue="name" itemLabel="name"/>
	                            </form:select>
	                            <form:errors path="name" cssClass="error-msg"/>
	                        </div>
						</div>
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
