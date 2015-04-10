<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib uri="http://www.springframework.org/tags" prefix="spring" %>
<%@taglib  uri="http://www.joda.org/joda/time/tags" prefix="joda"%>
<div class="row">
	<div class="col-md-12">
		<div class="panel panel-primary" data-collapsed="0">
			<div class="panel-heading">
				<div class="panel-title text-center no-float">
					<strong><spring:message code="msg.complaint.reg.success"/></strong>
					<div><spring:message code="lbl.complaint.reg.no"/>(<spring:message code="lbl.crn"/>) : <span id="ctn_no"><strong>${complaint.CRN}</strong></span>.<spring:message code="msg.crn.info"/></div>
				</div>
			</div>
			<div class="panel-body">
				<div class="row">
					<div class="col-md-3 col-xs-6 add-margin">
						<spring:message code="lbl.complaintDate"/>
					</div>
					<div class="col-md-3 col-xs-6 add-margin" id="ct-date">
						<joda:format value="${complaint.createdDate}" var="complaintDate" pattern="dd-MM-yyyy hh:mm:ss"/>
						${complaintDate}
					</div>
					<div class="col-md-3 col-xs-6 add-margin">
						<spring:message code="lbl.complainant.name"/>
					</div>
					<div class="col-md-3 col-xs-6 add-margin" id="ct-name">
						<c:choose>
							<c:when test="${not empty complaint.complainant.name}">
							${complaint.complainant.name}
							</c:when>
							<c:otherwise>
							${complaint.complainant.userDetail.name}
							</c:otherwise>
						</c:choose>
						
					</div>
				</div>
				<div class="row">
					<div class="col-md-3 col-xs-6 add-margin">
						<spring:message code="lbl.phoneNumber" />
					</div>
					<div class="col-md-3 col-xs-6 add-margin" id="ct-mobno">
						${complaint.complainant.mobile}
					</div>
					<div class="col-md-3 col-xs-6 add-margin">
						<spring:message code="lbl.email"/>
					</div>
					<div class="col-md-3 col-xs-6 add-margin" id="ct-email">
						${complaint.complainant.email}
					</div>
				</div>
			</div>
		</div>
	</div>
</div>
		
<div class="row">
	<div class="col-md-12">
		<div class="panel panel-primary" data-collapsed="0">
			<div class="panel-body">
				<div class="row">
					<div class="col-md-3 col-xs-6 add-margin">
						<spring:message code="lbl.complaintType"/>
					</div>
					<div class="col-md-3 col-xs-6 add-margin" id="ct-type">
						${complaint.complaintType.name}
					</div>
					<div class="col-md-3 col-xs-6 add-margin">
						Complaint Title
					</div>
					<div class="col-md-3 col-xs-6 add-margin" id="ct-title">
						${complaint.complaintType.name}
					</div>
				</div>
				<div class="row">
					<div class="col-md-3 col-xs-6 add-margin">
						<spring:message code="lbl.compDetails"/>
					</div>
					<div class="col-md-9 col-xs-6 add-margin" id="ct-details">
						${complaint.details}
					</div>
				</div>
				<div class="row">
					<div class="col-md-3 col-xs-6 add-margin">
						<spring:message code="lbl.complaintLocation"/>
					</div>
					<div class="col-md-9 col-xs-6 add-margin" id="ct-location">
						${complaint.location.boundaryNameLocal}
					</div>
				</div>
				<div class="row">
					<div class="col-md-3 col-xs-6 add-margin">
						<spring:message code="lbl.landmark"/>
					</div>
					<div class="col-md-3 col-xs-6 add-margin" id="ct-landmark">
						${complaint.landmarkDetails}
					</div>
				</div>
			</div>
		</div>
	</div>
</div>
		
<div class="row text-center">
	<div class="add-margin">
		<button type="submit" class="btn btn-default"><spring:message code="lbl.print"/></button>
		<a href="javascript:void(0)" class="btn btn-default" onclick="self.close()"><spring:message code="lbl.close"/></a>
	</div>
</div>