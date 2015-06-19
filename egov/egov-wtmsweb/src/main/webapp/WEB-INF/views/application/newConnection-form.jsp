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
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib uri="http://www.springframework.org/tags" prefix="spring" %>
<%@ taglib uri="http://www.springframework.org/tags/form" prefix="form" %>
<div class="row">
	<div class="col-md-12">
		<form:form role="form" action="application" modelAttribute="waterConnectionDetails"  id="newWaterConnectionform" cssClass="form-horizontal form-groups-bordered" enctype="multipart/form-data">
			<div class="panel panel-primary" data-collapsed="0">
				<div class="panel-heading">
					<div class="panel-title">
						<spring:message code="lbl.applicant.details"/>
					</div>
				</div>
				<div class="panel-body custom-form ">
					<div class="form-group">
						<label class="col-sm-3 control-label text-right"><spring:message code="lbl.ptassesmentnumber"/><span class="mandatory"></span></label>
						<div class="col-sm-3 add-margin">
							<div class="input-group">
							    <input type="text" class="form-control"/>
							    <span class="input-group-addon">
								<i class="fa fa-search"></i>
							    </span>
							</div>
						</div>
						<label class="col-sm-2 control-label text-right"><spring:message code="lbl.bpanumber"/></label>
						<div class="col-sm-3 add-margin">
							<input type="text" class="form-control">
						</div>
					</div>
					<div class="form-group">
						<label class="col-sm-3 control-label text-right"><spring:message code="lbl.applicantname"/></label>
						<div class="col-sm-3 add-margin">
							<input type="text" class="form-control" disabled>
						</div>
						<label class="col-sm-2 control-label text-right"><spring:message code="lbl.mobilenumber"/></label>
						<div class="col-sm-3 add-margin">
							<input type="text" class="form-control">
						</div>
					</div>
					<div class="form-group">
						<label class="col-sm-3 control-label text-right"><spring:message code="lbl.address"/></label>
						<div class="col-sm-3 add-margin">
							<textarea class="form-control" disabled></textarea>
						</div>
						<label class="col-sm-2 control-label text-right"><spring:message code="lbl.zonewardblock"/></label>
						<div class="col-sm-3 add-margin">
							<input type="text" class="form-control" disabled>
						</div>
					</div>
					<div class="panel-heading custom_form_panel_heading">
						<div class="panel-title">
							<spring:message code="lbl.connection.details"/>
						</div>
					</div>
					<div class="form-group">
						<label class="col-sm-3 control-label text-right"><spring:message code="lbl.connectiontype"/><span class="mandatory"></span></label>
						<div class="col-sm-3 add-margin">
							<form:select name="connectionType" path="" data-first-option="false" 
								cssClass="form-control" >
								<form:option value="">
									<spring:message code="lbl.connectiontype" />
								</form:option>
								<form:options items="${connectionTypes}"  />  
							</form:select>
						</div>
						<label class="col-sm-2 control-label text-right"><spring:message code="lbl.usagetype"/><span class="mandatory"></span></label>
						<div class="col-sm-3 add-margin">
							<select class="form-control"><option value=""><spring:message code="lbl.select"/></option></select>
						</div>
					</div>
					<div class="form-group">
						<label class="col-sm-3 control-label text-right"><spring:message code="lbl.category"/><span class="mandatory"></span></label>
						<div class="col-sm-3 add-margin">
							<select class="form-control"><option value=""><spring:message code="lbl.select"/></option></select>
						</div>
						<label class="col-sm-2 control-label text-right"><spring:message code="lbl.watersourcetype"/><span class="mandatory"></span></label>
						<div class="col-sm-3 add-margin">
							<select class="form-control"><option value=""><spring:message code="lbl.select"/></option></select>
						</div>
					</div>
					<div class="form-group">
						<label class="col-sm-3 control-label text-right"><spring:message code="lbl.hscpipesize.inches"/><span class="mandatory"></span></label>
						<div class="col-sm-3 add-margin">
							<select class="form-control"><option value=""><spring:message code="lbl.select"/></option></select>
						</div>
						<label class="col-sm-2 control-label text-right"><spring:message code="lbl.sumpcapacity.litres"/><span class="mandatory"></span></label>
						<div class="col-sm-3 add-margin">
							<select class="form-control"><option value=""><spring:message code="lbl.select"/></option></select>
						</div>
					</div>
				<!-- <div class="panel-heading custom_form_panel_heading">
						<div class="panel-title">
							<spring:message code="lbl.encloseddocuments"/> - <spring:message code="lbl.checklist"/>
						</div>
					</div>
					<div class="col-sm-12 view-content header-color hidden-xs">
					<div class="col-sm-4 table-div-column"><spring:message code="lbl.documentname"/></div>											
					<div class="col-sm-4 table-div-column"><spring:message code="lbl.documentnumber"/> (<span class="mandatory"></span> )</div>										
					<div class="col-sm-4 table-div-column"><spring:message code="lbl.documentdate"/> (<span class="mandatory"></span> )</div>																					
					</div>
					<div class="form-group">
						<div class="col-sm-4 add-margin">
							<input type="checkbox"> House Tax Registration Document
						</div>
						<div class="col-sm-4 add-margin">
							<input type="textbox" class="form-control">
						</div>
						<div class="col-sm-4 add-margin">
							<input type="date" class="form-control">
						</div>
						<div class="col-sm-4 add-margin">
							<input type="checkbox"> House Tax Registration Document
						</div>
						<div class="col-sm-4 add-margin">
							<input type="textbox" class="form-control">
						</div>
						<div class="col-sm-4 add-margin">
							<input type="date" class="form-control">
						</div>
					</div> -->	 
				</div>
			</div>
			<div class="row">
				<div class="text-center">
					<button class="btn btn-primary"><spring:message code="lbl.createapplication"/></button>
					<button class="btn btn-primary"><spring:message code="lbl.clear"/></button>
					<a href="javascript:void(0);" id="closeComplaints"
								class="btn btn-primary" onclick="self.close()"><spring:message code='lbl.close' /></a>
				</div>
			</div>
		</form:form>
	</div>
</div>

<script src="<c:url value='/resources/global/js/jquery/plugins/exif.js' context='/egi'/>"></script>
<script src="<c:url value='/resources/js/app/newconnection.js'/>"></script>
<script>
	
</script>