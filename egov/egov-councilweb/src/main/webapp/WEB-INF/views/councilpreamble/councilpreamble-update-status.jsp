<%@ page contentType="text/html;charset=UTF-8" language="java"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ taglib uri="http://www.springframework.org/tags" prefix="spring"%>
<%@ taglib uri="http://www.springframework.org/tags/form" prefix="form"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt"%>
<%@ taglib uri="/WEB-INF/taglib/cdn.tld" prefix="cdn" %>

<form:form role="form" action="../updateimplimentaionstatus" modelAttribute="councilPreamble" id="councilPreambleform" cssClass="form-horizontal form-groups-bordered"
	enctype="multipart/form-data">
	
		<%@ include file="councilpreamble-view.jsp"%>
		<div class="form-group" >
		<label class="col-sm-2 control-label text-right"><spring:message
						code="lbl.impl.status" /></label>
				<div class="col-sm-3 add-margin">
					<form:select path="implementationStatus" id="implementationStatus" 
						cssClass="form-control" cssErrorClass="form-control error">
						<form:option value="">
							<spring:message code="lbl.select" />
						</form:option>
						<form:options items="${implementationStatus}"
						itemValue="id" itemLabel="code"  />
					</form:select>
					<form:errors path="implementationStatus" cssClass="error-msg" />
				</div>
				</div>	
				<input type="hidden" name="councilPreamble" value="${councilPreamble.id}" />	
		<div class="form-group">
		<div class="text-center">
			<button type='submit' class='btn btn-primary' id="buttonSubmit">
				<spring:message code='lbl.update' />
			</button>
		</div>
	</div>
	
	</form:form>
	<script
	src="<cdn:url value='/resources/app/js/councilPreambleHelper.js?rnd=${app_release_no}'/>"></script>