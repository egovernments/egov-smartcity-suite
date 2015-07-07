<!-- #-------------------------------------------------------------------------------
# eGov suite of products aim to improve the internal efficiency,transparency, 
#    accountability and the service delivery of the government  organizations.
# 
#     Copyright (C) <2015>  eGovernments Foundation
# 
#     The updated version of eGov suite of products as by eGovernments Foundation 
#     is available at http://www.egovernments.org
# 
#     This program is free software: you can redistribute it and/or modify
#     it under the terms of the GNU General Public License as published by
#     the Free Software Foundation, either version 3 of the License, or
#     any later version.
# 
#     This program is distributed in the hope that it will be useful,
#     but WITHOUT ANY WARRANTY; without even the implied warranty of
#     MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
#     GNU General Public License for more details.
# 
#     You should have received a copy of the GNU General Public License
#     along with this program. If not, see http://www.gnu.org/licenses/ or 
#     http://www.gnu.org/licenses/gpl.html .
# 
#     In addition to the terms of the GPL license to be adhered to in using this
#     program, the following additional terms are to be complied with:
# 
# 	1) All versions of this program, verbatim or modified must carry this 
# 	   Legal Notice.
# 
# 	2) Any misrepresentation of the origin of the material is prohibited. It 
# 	   is required that all modified versions of this material be marked in 
# 	   reasonable ways as different from the original version.
# 
# 	3) This license does not grant any rights to any user of the program 
# 	   with regards to rights under trademark law for use of the trade names 
# 	   or trademarks of eGovernments Foundation.
# 
#   In case of any queries, you can reach eGovernments Foundation at contact@egovernments.org.
#------------------------------------------------------------------------------- -->

<%@ page contentType="text/html" language="java"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ taglib uri="http://www.springframework.org/tags/form" prefix="form"%>
<%@ taglib uri="http://www.springframework.org/tags" prefix="spring"%>
<%@ taglib prefix="joda" uri="http://www.joda.org/joda/time/tags"%>
<div class="row">
	<div class="col-md-12">

		<div class="panel panel-primary" data-collapsed="0">

			<div class="panel-heading">
				<div class="panel-title">
					<strong><spring:message code='title.watertaxSearch' />
					</strong>
				</div>

			</div>

			<div class="panel-body custom-form">

				<form:form  class="form-horizontal form-groups-bordered"
					id="waterSearchRequestForm" modelAttribute="connectionSearchRequest" action="">

					<div class="form-group">
				<label for="field-1" class="col-sm-4 control-label"> <spring:message
										code="lbl.mobilenumber" /></label>
				
						<div class="col-md-4 add-margin">
							<input type="text" name="mobileNumber" class="form-control" id="app-appcodo"
								 />
						</div></div>
						<div class="form-group">
				<label for="field-1" class="col-sm-4 control-label"> Consumer Code</label>
										<div class="col-md-4 add-margin">
							<input type="text" name="consumerCode" class="form-control" id="app-appcodo"/>
						</div>
						</div>
						
						
						<div class="form-group">
					<label for="field-1" class="col-sm-4 control-label"> <spring:message
										code="lbl.name" /></label>
						<div class="col-md-4 add-margin">
							<input type="text" name="applicationName" class="form-control" id="app-mobno"
								/>
						</div>
						</div>
						<div class="form-group">
					<label for="field-1" class="col-sm-4 control-label">Locality</label>
						<div class="col-md-4 add-margin">
							<input type="text" name="locality" class="form-control" id="app-mobno"
								/>
						</div>
						</div>
					
						
						
						
						
					
					<div class="form-group">
						<div class="text-center">
							<a href="javascript:void(0);" id="searchapplication"
								class="btn btn-primary"><spring:message code='lbl.search' /></a>
								
							<button class="btn btn-danger" type="reset">Reset</button>
							<a href="javascript:void(0);" id="closeComplaints"
								class="btn btn-default" onclick="self.close()"><spring:message code='lbl.close' /></a>
						</div>
				</div>


				</form:form>


			</div>


		</div>

	</div>
</div>


<div class="row">
					<div class="col-md-6 col-xs-6 table-header">The Search result is</div>
					<div class="col-md-6 col-xs-6 add-margin text-right">
						<span class="inline-elem">Search</span>
						<span class="inline-elem"><input type="text" id="searchemployee" class="form-control input-sm"></span>
					</div>
	<table class="table table-bordered datatable dt-responsive"
		id="aplicationSearchResults">

	</table>

</div>

<link rel="stylesheet"
	href="<c:url value='/resources/global/js/jquery/plugins/datatables/responsive/css/datatables.responsive.css' context='/egi'/>">
<script
	src="<c:url value='/resources/global/js/jquery/plugins/datatables/jquery.dataTables.min.js' context='/egi'/>"
	type="text/javascript"></script>
<script
	src="<c:url value='/resources/global/js/jquery/plugins/datatables/dataTables.bootstrap.js' context='/egi'/>"
	type="text/javascript"></script>
<script
	src="<c:url value='/resources/global/js/jquery/plugins/datatables/dataTables.tableTools.js' context='/egi'/>"
	type="text/javascript"></script>
<script
	src="<c:url value='/resources/global/js/jquery/plugins/datatables/TableTools.min.js' context='/egi'/>"
	type="text/javascript"></script>
<script
	src="<c:url value='/resources/global/js/jquery/plugins/datatables/responsive/js/datatables.responsive.js' context='/egi'/>"
	type="text/javascript"></script>

<script
	src="<c:url value='/resources/global/js/bootstrap/bootstrap-datepicker.js' context='/egi'/>"
	type="text/javascript"></script>

<script src="<c:url value='/resources/js/app/connectionsearch.js'/>"
	type="text/javascript"></script>
	<script>
		$('.dropchange').change(function(){
			 if( this.value == 0){
				window.location.href="applyforadditionalconnection.html"
			}else if( this.value == 2){
				window.location.href="closingwatertap.html"
			}else if( this.value == 6){
				window.location.href="disconnectionotice.html"
			}else if( this.value == 1){
				window.location.href="changeofuse.html"
			}else if( this.value == 3){
				window.location.href="reconnection.html"
			}
		});
	</script>
