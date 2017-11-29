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
 
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ taglib uri="http://www.springframework.org/tags" prefix="spring"%>
<%@ taglib uri="http://www.springframework.org/tags/form" prefix="form"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt" %>	
<%@ taglib uri="/WEB-INF/taglibs/cdn.tld" prefix="cdn"%>		
<div id="errorBlock" class="help-block"></div>
<link rel="stylesheet" href="<cdn:url value='/resources/global/css/jquery/plugins/filer/jquery.filer-dragdropbox-theme.css' context='/egi'/>">
<link rel="stylesheet" href="<cdn:url value='/resources/global/css/jquery/plugins/filer/jquery.filer.css' context='/egi'/>">
<script type="text/javascript" src="<cdn:url value='/resources/global/js/jquery/plugins/filer/jquery.filer.min.js' context='/egi'/>"></script>

<link rel="stylesheet" href="<cdn:url value='/resources/global/css/bootstrap/plugins/image-gallery/bootstrap-image-gallery.css' context='/egi'/>">
<link rel="stylesheet" href="<cdn:url value='/resources/global/js/image-gallery/css/blueimp-gallery.min.css' context='/egi'/>">

<script type="text/javascript" src="<cdn:url value='/resources/global/js/image-gallery/js/jquery.blueimp-gallery.min.js' context='/egi'/>"></script>
<script type="text/javascript" src="<cdn:url value='/resources/global/js/image-gallery/js/bootstrap-image-gallery.js' context='/egi'/>"></script>
<style>
#links{
	    position: relative;
	    width: 190px;
	    height: 145px;
	    min-height: 115px;
	    border: 1px solid #e1e1e1;
	    overflow: hidden;
}
.jFiler-items-grid .jFiler-item .jFiler-item-container .jFiler-item-thumb-overlay{
         height:20%;
     }
</style>

<form:form id="estimatePhotographs" class="form-horizontal form-groups-bordered" modelAttribute="estimatePhotographs" role="form" action="loa-save" method="post" enctype="multipart/form-data">
		<div class="new-page-header"><spring:message code="lbl.uploadestimatephotograph" /> </div> 
		
		<input type="hidden" value="${lineEstimateDetails.id}" id="ledId" name="ledId"/>
		<input type="hidden" value='${photographStages}'  id = "photographStages"/>
		<input type="hidden" value="${workOrder.id}" name="workOrderId" />
		<input type="hidden" path="latitude" name="latitude" />
		<input type="hidden" path="longitude" name="longitude" />
		
		
		<input type="hidden" value="${lineEstimateDetails.lineEstimate.id}" name="lineEstimateId" />
		<input type="hidden" value="<spring:message code="lbl.before" />" id="beforeId" />
		<input type="hidden" value="<spring:message code="lbl.after" />" id="afterId" />
		<input type="hidden" value="<spring:message code="lbl.during" />" id="duringId" />
<div class="row">
	<div class="col-md-12">
		 		<div class="panel panel-primary" data-collapsed="0">
					<div class="panel-body">
						<div class="panel-heading">
						<%-- <div class="panel-title" style="text-align:center;"><spring:message code="lbl.header" /></div> --%>
						</div>
						<div class="row add-border">
								<div class="col-md-2 col-xs-6 add-margin"><spring:message code="lbl.estimatenumber" />.</div> 
								<div class="col-md-2 col-xs-6 add-margin view-content">
								<%-- <a href='javascript:void(0)' onclick="viewLineEstimate('<c:out value="${abstractEstimate.estimateNumber}"/>')"> <c:out value="${abstractEstimate.estimateNumber}"/> </a> --%>
								<c:out value="${lineEstimateDetails.estimateNumber}"/>
								</div>
								<div class="col-md-2 col-xs-6 add-margin">
									<spring:message code="lbl.lineestimatenumber" />.
								</div> 
								<div class="col-md-2 col-xs-6 add-margin view-content">
									<%-- <a href='javascript:void(0)' onclick="openLineEstimate('<c:out value="${abstractEstimate.lineEstimateDetails.lineEstimate.id}"/>')"><c:out value="${abstractEstimate.lineEstimateDetails.lineEstimate.lineEstimateNumber}"/></a> --%>
									<a href='javascript:void(0)' onclick="openLineEstimate('<c:out value="${lineEstimateDetails.lineEstimate.id}"/>')"><c:out value="${lineEstimateDetails.lineEstimate.lineEstimateNumber}"/></a>
								</div>
							 <c:if test="${workOrder.workOrderNumber != null}"> 
							<div class="col-md-2 col-xs-6 add-margin"><spring:message code="lbl.workordernumber" />.</div>
							<div class="col-md-2 col-xs-6 add-margin view-content">
							<a href='javascript:void(0)' onclick="openLOA('<c:out value="${workOrder.id}"/>')"><c:out value="${workOrder.workOrderNumber}"/></a>
							</div>
							</c:if>
						</div>
<%-- 						<c:if test="${abstractEstimate != null}"> 
 --%>						<div class="row add-border">
							<div class="col-md-2 col-xs-6 add-margin"><spring:message code="lbl.nameofwork" />.</div>
							<div class="col-md-9 col-xs-12 add-margin view-content">
							<%-- <c:out value="${abstractEstimate.name}"></c:out> --%>
								<c:out value="${lineEstimateDetails.nameOfWork}"></c:out>
							</div>
						</div>
						<%-- </c:if> --%>
					</div>
				</div>
			</div>
		</div>
		<div class="row">
			<div class="col-md-12">
		 		<div class="panel panel-primary" data-collapsed="0">
		 			<div class="panel-heading">
						<div class="panel-title" style="text-align:left;"><spring:message code="lbl.before" /></div>
					</div>
					<div class="panel-body">
						<input id="before" name="file" type="file" multiple  >
					</div>
				</div>
			</div>
		</div>
		<div class="row">
			<div class="col-md-12">
		 		<div class="panel panel-primary" data-collapsed="0">
		 			<div class="panel-heading">
						<div class="panel-title" style="text-align:left;"><spring:message code="lbl.during" /></div>
					</div>
					<div class="panel-body">
						<input id="during" name="file" type="file" multiple  >
					</div>
				</div>
			</div>
		</div>
		<div class="row">
			<div class="col-md-12">
		 		<div class="panel panel-primary" data-collapsed="0">
		 			<div class="panel-heading">
						<div class="panel-title" style="text-align:left;"><spring:message code="lbl.after" /></div>
					</div>
					<div class="panel-body">
		    			<input id="after" name="file" type="file" multiple  >
					</div>
				</div>
			</div>
		</div>
		<div class="row">
				<div class="col-sm-12 text-center">
					<a href='javascript:void(0)' class='btn btn-default'
						onclick='self.close()'><spring:message code='lbl.close' /></a>
				</div>
			</div>
			
			
</form:form>

		<!-- The Bootstrap Image Gallery lightbox, should be a child element of the document body -->
   <div id="blueimp-gallery" class="blueimp-gallery" data-use-bootstrap-modal="false">
       <!-- The container for the modal slides -->
       <div class="slides"></div>
       <!-- Controls for the borderless lightbox -->
       <h3 class="title"></h3>
       <a class="prev">‹</a>
       <a class="next">›</a>
       <a class="close">×</a>
       <a class="play-pause"></a>
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
                           <i class="glyphicon glyphicon-chevron-left"></i>
                           Previous
                       </button>
                       <button type="button" class="btn btn-primary next">
                           Next
                           <i class="glyphicon glyphicon-chevron-right"></i>
                       </button>
                   </div>
               </div>
           </div>
       </div>
   </div>

<script src="<cdn:url value='/resources/js/estimatephotograph/estimatephotograph.js?rnd=${app_release_no}'/>"></script>