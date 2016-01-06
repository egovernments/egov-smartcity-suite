<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib uri="http://www.springframework.org/tags" prefix="spring" %>
<%@ taglib uri="http://www.springframework.org/tags/form" prefix="form" %>

<div class="row">
	<div class="col-md-12">
		<form:form  id="hoardingsuccess" method ="post" class="form-horizontal form-groups-bordered" modelAttribute="hoarding" >
	 		
			<%-- <div class="row">
				<div class="text-center">
					<c:if test="${not empty message}">
	                   <div class="alert" align="center"><strong><spring:message code="${message}"/></strong></div>
	            	</c:if>
	       			<button type="button" class="btn btn-default" data-dismiss="modal" onclick="self.close()">
	       				<spring:message code="lbl.close"/>
	       			</button>
				</div>
			</div>  --%>
			
			<div class="panel panel-primary" data-collapsed="0">
				<div class="panel-heading">
					<div class="panel-title text-center">
						<spring:message code="hoarding.create.success" />
						<span ><spring:message code="msg.sussess.forward" />${approverName}~${nextDesign}</span>
					</div>
				</div>
			</div>
					
		</form:form>
	</div>
</div>
<div class="row text-center">
	<div class="add-margin">
		<a href="javascript:void(0)" class="btn btn-default" onclick="self.close()"><spring:message code="lbl.close" /></a>
	</div>
</div>