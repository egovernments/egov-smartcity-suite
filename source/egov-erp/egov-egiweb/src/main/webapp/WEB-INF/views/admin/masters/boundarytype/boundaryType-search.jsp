<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib uri="http://www.springframework.org/tags" prefix="spring" %>
<%@ taglib uri="http://www.springframework.org/tags/form" prefix="form" %>
<%@ taglib prefix="egov" tagdir="/WEB-INF/tags" %>

<link rel="stylesheet" href="<c:url value='/resources/global/css/font-icons/entypo/css/entypo.css'/>">
<link rel="stylesheet" href="<c:url value='/resources/global/css/bootstrap/typeahead.css'/>">
<script type="text/javascript">
        var contextPath = "<%=request.getContextPath() %>";
</script>

<div class="row" id="page-content">
	<div class="col-md-12">
		<div class="panel" data-collapsed="0">
			<div class="panel-body">
				 <c:if test="${not empty message}">
                    <div id="message" class="success">${message}</div>
                </c:if>
		<form:form  mothod ="post" class="form-horizontal form-groups-bordered" modelAttribute="boundaryType" id="boundaryTypeSearch" >
			<div class="panel panel-primary" data-collapsed="0">
				<div class="panel-heading">
					<div class="panel-title">
						<strong><spring:message code="title.searchBoundaryType"/></strong>
					</div>
				</div> 
				<div class="panel-body custom-form">
							<div class="form-group">
								<label class="col-sm-3 control-label">
									<spring:message code="lbl.hierarchyType" />
									<small><i class="entypo-star error-msg"></i></small>
								</label>
								<div class="col-sm-6 add-margin">
		                            <form:select path="name"
		                                         id="hierarchyTypeSelect" cssClass="form-control" onchange="populateBoundaryTypes(this);" cssErrorClass="form-control error" required="required">
		                                <form:option value=""> <spring:message code="lbl.select"/> </form:option>
		                                <form:options items="${hierarchyTypes}" itemValue="id" itemLabel="name"/>
		                            </form:select>
		                            <form:errors path="name" cssClass="error-msg"/>
	                        	</div>
	                        </div>
	                        <div class="form-group">
								<label class="col-sm-3 control-label"><spring:message
										code="lbl.boundaryType" /><small><i
										class="entypo-star error-msg"></i></small></label>
								<div class="col-sm-6 add-margin">
									<egov:ajaxdropdown id="boundaryTypeAjax" fields="['Text','Value']"
												dropdownId="boundaryTypeSelect" url="boundaryTypes-by-hierarchyType" />
		                            <form:select path="name"
		                                         id="boundaryTypeSelect" cssClass="form-control" cssErrorClass="form-control error" required="required">
		                                <form:option value=""> <spring:message code="lbl.select"/> </form:option>
		                            </form:select>
		                            <form:errors path="name" cssClass="error-msg"/>
		                        </div>
							</div>
	                	</div>
			</div>
			<div class="row">
				<div class="text-center">
					<c:if test="${mode == 'addChild'}">
						<button type="button" class="btn btn-primary" onclick="checkForChild()"><spring:message code="lbl.search"/></button>
					</c:if>
					<c:if test="${mode != 'addChild'}">
						<button type="submit" class="btn btn-primary" ><spring:message code="lbl.search"/></button>
					</c:if>
			        <button type="button" class="btn btn-default" data-dismiss="modal" onclick="self.close()"><spring:message code="lbl.close"/></button>
				</div>
			</div>
				</form:form>
			</div>
		</div>
	</div>
</div>



<script src="<c:url value='/resources/global/js/bootstrap/typeahead.bundle.js'/>"></script>
<script src="<c:url value='/resources/global/js/jquery/plugins/exif.js'/>"></script>
<script src="<c:url value='/resources/global/js/jquery/plugins/jquery.inputmask.bundle.min.js'/>"></script>
<script src="<c:url value='/resources/js/app/boundary.js'/>"></script>
