<%@ page contentType="text/html" language="java"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ taglib uri="http://www.springframework.org/tags/form" prefix="form"%>
<%@ taglib uri="http://www.springframework.org/tags" prefix="spring"%>
<%@taglib prefix="joda" uri="http://www.joda.org/joda/time/tags" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>

<div class="row">
	<div class="col-md-12">
		<div class="panel panel-primary" data-collapsed="0">
			<div class="panel-heading">
				<div class="panel-title">
					<strong><c:out value="${complaint.CRN}"></c:out></strong>
				</div>
			</div>
			<div class="panel-body">
				<c:if test="${not empty message}">
					<div id="message" class="success">${message}</div>
				</c:if>
				<div class="row add-border">
					<div class="col-md-3 col-xs-6 add-margin"><spring:message code="lbl.name" /></div>
					<div class="col-md-3 col-xs-6 add-margin view-content" id="ct-name">
						<c:out value="${complaint.complainant.name}"></c:out>
					</div>
					<div class="col-md-3 col-xs-6 add-margin"><spring:message code="lbl.phoneNumber" /></div>
					<div class="col-md-3 col-xs-6 add-margin view-content" id="ct-mobno">
						<c:out value="${complaint.complainant.mobile}"></c:out>
					</div>
				</div>
				<div class="row add-border">
					<div class="col-md-3 col-xs-6 add-margin"><spring:message code="lbl.ctn" /></div>
					<div class="col-md-3 col-xs-6 add-margin view-content" id="ct-ctnumber">
						<c:out value="${complaint.CRN}"></c:out>
					</div>
					<div class="col-md-3 col-xs-6 add-margin"><spring:message code="lbl.complaintDate" /></div>
					<div class="col-md-3 col-xs-6 add-margin view-content" id="ct-date">
					<joda:format value="${complaint.createdDate}" var="complaintDate" pattern="yyyy-MM-dd"/>
					<c:out value="${complaintDate}"></c:out> 
					</div>
				</div>
				<div class="row add-border">
					<div class="col-md-3 col-xs-6 add-margin"><spring:message code="lbl.complaintDepartment" /></div>
					<div class="col-md-3 col-xs-6 add-margin view-content" id="ct-dept">
						<c:out value="${complaint.complaintType.department.name}"></c:out>
					</div>
					<div class="col-md-3 col-xs-6 add-margin"><spring:message code="lbl.filedVia" /></div>
					<div class="col-md-3 col-xs-6 add-margin view-content" id="ct-filedvia">
						Internet</div>
				</div>
				<div class="row add-border">
					<div class="col-md-3 col-xs-6 add-margin"><spring:message code="lbl.complaintType" /></div>
					<div class="col-md-3 col-xs-6 add-margin view-content" id="ct-type">
						<c:out value="${complaint.complaintType.name}"></c:out>
					</div>
					<div class="col-md-3 col-xs-6 add-margin"><spring:message code="lbl.priority" /></div>
					<div class="col-md-3 col-xs-6 add-margin" id="ct-priority">
						</div>
					<div class="col-md-3 col-xs-6 add-margin view-content" id="ct-priority">
						Waterlogged road</div>
				</div>
				<div class="row add-border">
					<div class="col-md-3 col-xs-6 add-margin"><spring:message code="lbl.compDetails" /></div>
					<div class="col-md-3 col-xs-6 add-margin view-content" id="ct-details">
						<c:out value="${complaint.details }"></c:out>
					</div>
				</div>
				<div class="row add-border">
					<div class="col-md-3 col-xs-6 add-margin">
						<spring:message code="lbl.photovideo" />
					</div>
					<div class="col-md-9 col-xs-12 add-margin down-file "
						id="links">
						<c:choose>
							<c:when test="${!complaint.getSupportDocs().isEmpty()}">
								<c:forEach items="${complaint.getSupportDocs()}" var="file">
									<c:choose>
										<c:when test="${(file.contentType == 'image/jpg') || (file.contentType == 'image/jpeg')|| (file.contentType == 'image/gif')|| 
										(file.contentType == 'image/png')}">
										<a href="/egi/downloadfile?fileStoreId=${file.fileStoreId}&moduleName=PGR"
												data-gallery> <img class="img-width add-margin"
												src="/egi/downloadfile?fileStoreId=${file.fileStoreId}&moduleName=PGR" /></a>
										</c:when>
										<c:otherwise>
											<a href="/egi/downloadfile?fileStoreId=${file.fileStoreId}&moduleName=PGR"
												data-gallery> <video class="img-width add-margin"controls="controls"
													src="/egi/downloadfile?fileStoreId=${file.fileStoreId}&moduleName=PGR">
													<source	src="/egi/downloadfile?fileStoreId=${file.fileStoreId}&moduleName=PGR"
														type="video/mp4" /></video></a>
										</c:otherwise>
									</c:choose>
								</c:forEach>
							</c:when>
							<c:otherwise>
								<div class="col-md-3 col-xs-6 add-margin">No attachments found</div>
							</c:otherwise>
						</c:choose>
					</div>
				</div>

				<div class="row add-border">
					<div class="col-md-3 col-xs-6 add-margin">
						<spring:message code="lbl.location" />
					</div>
					<div class="col-md-9 col-xs-6 add-margin view-content">
						<span class="map-tool-class btn-secondary" data-toggle="tooltip"
							data-placement="top" title="" data-original-title="Locate on map"
							onclick="jQuery('#complaint-locate').modal('show', {backdrop: 'static'});"><i
							class="entypo-globe"></i></span> <span id="address_locate"><c:out
								value="${complaint.location.name}"></c:out></span>
					</div>
				</div>
				<div class="row">
					<div class="col-md-3 col-xs-6 add-margin">
						<spring:message code="lbl.landmark" /> 
					</div>
					<div class="col-md-9 col-xs-6 add-margin view-content" id="ct-lanmark">
						<c:choose>
							<c:when test="${complaint.landmarkDetails != null}">
								<c:out value="${complaint.landmarkDetails}"></c:out>
							</c:when>
							<c:otherwise>N/A</c:otherwise>
						</c:choose>
					</div>
				</div>
			</div>
		</div>
	</div>
</div>
<div class="row">
	<div class="col-md-12">
		<div class="panel-group">
			<div class="panel panel-primary">
				<div class="panel-heading slide-history-menu">
					<div class="panel-title">
						<strong><spring:message code="lbl.hdr.complaintHistory" /></strong>
					</div>
					<div class="history-icon">
						<i class="entypo-down-open" id="toggle-his-icon"></i>
					</div>
				</div>
				<div class="panel-body history-slide">
					<div class="row view-content header-color hidden-xs">
						<div class="col-md-2 col-xs-6 add-margin">Date</div>
						<div class="col-md-2 col-xs-6 add-margin">Updater</div>
						<div class="col-md-2 col-xs-6 add-margin">Status</div>
						<div class="col-md-2 col-xs-6 add-margin">Current Owner</div>
						<div class="col-md-2 col-xs-6 add-margin">Department</div>
						<div class="col-md-2 col-xs-6 add-margin">Comments</div>
					</div>
					<c:choose>
							<c:when test="${!complaintHistory.isEmpty()}">
								<c:forEach items="${complaintHistory}" var="history">
								<div class="row  add-border">
									<div class="col-md-2 col-xs-12 add-margin ">
										<fmt:formatDate value="${history.date}" var="historyDate"
											pattern="HH:MM a E dd MMM yyyy" />
										<c:out value="${historyDate}" />
									</div>
									<div class="col-md-2 col-xs-12 add-margin">
										<c:out value="${history.updater}" />
									</div>
									<div class="col-md-2 col-xs-12 add-margin ">
										<c:out value="${history.status}" />
									</div>
									<div class="col-md-2 col-xs-12 add-margin ">
										<c:out value="${history.user}" />
									</div>
									<div class="col-md-2 col-xs-12 add-margin ">
										<c:out value="${history.department}" />
									</div>
									<div class="col-md-2 col-xs-12 add-margin ">
										<c:out value="${history.comments}" />&nbsp;
									</div>
								</div>
								</c:forEach>
							</c:when>
							<c:otherwise>
								<div class="col-md-3 col-xs-6 add-margin">No history
									details for complaint</div>
							</c:otherwise>
						</c:choose>
					
				</div>
			</div>
		</div>
	</div>
</div>

<!-- Modal 6 (Long Modal)-->
<div class="modal fade" id="complaint-locate">
	<div class="modal-dialog">
		<div class="modal-content">
			<div class="modal-body">
				<div class="row">
					<div class="col-md-12">
						<div class="panel panel-primary" data-collapsed="0">
							<!-- to apply shadow add class "panel-shadow" -->
							<!-- panel head -->
							<div class="panel-heading">
								<div class="panel-title" id="show_address_in_map"><spring:message code="lbl.hdr.complaintHistory" /></div>
							</div>
							<!-- panel body -->
							<div class="panel-body no-padding">
								<script src="https://maps.googleapis.com/maps/api/js"></script>
								<div id="normal" class="img-prop"></div>
							</div>
						</div>
					</div>
				</div>
			</div>
			<div class="modal-footer">
				<button type="button" class="btn btn-default" data-dismiss="modal">Close</button>
			</div>
		</div>
	</div>
</div>

<!-- The Bootstrap Image Gallery lightbox, should be a child element of the document body -->
<div id="blueimp-gallery" class="blueimp-gallery"
	data-use-bootstrap-modal="false">
	<!-- The container for the modal slides -->
	<div class="slides"></div>
	<!-- Controls for the borderless lightbox -->
	<h3 class="title"></h3>
	<ol class="indicator"></ol>
	<!-- The modal dialog, which will be used to wrap the lightbox content -->
	<div class="modal fade">
		<div class="modal-dialog">
			<div class="modal-content">
				<div class="modal-header">
					<button type="button" class="close" aria-hidden="true">&times;</button>
					<h4 class="modal-title"></h4>
				</div>
				<div class="modal-body next"></div>
				<div class="modal-footer">
					<button type="button" class="btn btn-default pull-left prev">
						<i class="glyphicon glyphicon-chevron-left"></i> Previous
					</button>
					<button type="button" class="btn btn-primary next">
						Next <i class="glyphicon glyphicon-chevron-right"></i>
					</button>
				</div>
			</div>
		</div>
	</div>
</div>

<link rel="stylesheet"	href="<c:url value='/resources/global/js/image-gallery/css/blueimp-gallery.min.css' context='/egi'/>">
<link rel="stylesheet"	href="<c:url value='/resources/global/js/image-gallery/css/blueimp-gallery.min.css' context='/egi'/>">
<link rel="stylesheet"	href="<c:url value='/resources/global/css/font-icons/entypo/css/entypo.css' context='/egi'/>">
<script	src="<c:url value='/resources/global/js/image-gallery/js/jquery.blueimp-gallery.min.js' context='/egi'/>"></script>
<script	src="<c:url value='/resources/global/js/image-gallery/js/bootstrap-image-gallery.js' context='/egi'/>"></script>
<script src="<c:url value='/resources/js/app/complaintview.js'/>"></script>
<script src="<c:url value='/resources/js/app/complaintviewmap.js'/>"></script>
