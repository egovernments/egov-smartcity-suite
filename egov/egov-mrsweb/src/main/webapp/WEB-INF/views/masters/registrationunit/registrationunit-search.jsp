<%--
  ~ eGov suite of products aim to improve the internal efficiency,transparency,
  ~    accountability and the service delivery of the government  organizations.
  ~
  ~     Copyright (C) <2016>  eGovernments Foundation
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
  --%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt" %>
<%@ taglib uri="http://www.springframework.org/tags" prefix="spring" %>
<%@ taglib uri="http://www.springframework.org/tags/form" prefix="form" %>
<%@ taglib uri="/WEB-INF/taglib/cdn.tld" prefix="cdn" %>	
<form:form role="form"  modelAttribute="marriageRegistrationUnit" id="registrationunitsearchform" name="registrationunitsearchform"
	cssClass="form-horizontal form-groups-bordered"
	enctype="multipart/form-data">
	<div class="row">
		<div class="col-md-12">
				<div class="panel panel-primary" data-collapsed="0">
					<div class="panel-heading">
						<div class="panel-title">
								<strong><spring:message code="lbl.search"/></strong>
						</div>
					</div> 
					
					<div class="panel-body custom-form">				
						<div class="form-group">
						<label class="col-sm-3 control-label">
							<spring:message code="lbl.registrationunitname"/>
						</label>
						<div class="col-sm-6">
							<form:input path="name" id="name" type="text" cssClass="form-control patternvalidation" 
                        	  data-pattern="alphanumericwithspacehyphenunderscore" maxlength="50" placeholder="" autocomplete="off" />
                            <form:errors path="name" cssClass="add-margin error-msg"/>
						</div>
					</div>

					<div class="form-group">
					<label class="col-sm-3 control-label">
						<spring:message code="lbl.zone"/>
					</label>
			<div class="col-sm-3">
			<form:select path="zone.id" id="select-zones" cssClass="form-control" 
						cssErrorClass="form-control error">
                 <form:option value=""> <spring:message code="lbl.default.option"/> </form:option>
                 <form:options items="${zones}" itemValue="id" itemLabel="name"/>
             </form:select>
            <form:errors path="zone.id" cssClass="add-margin error-msg"/>
            </div>
			</div>
			<div class="form-group">
			<label class="col-sm-3 control-label text-right"><spring:message
								code="lbl.isactive" /> </label>
						<div class="col-sm-3 add-margin">
							<form:checkbox path="isActive" checked="checked" />
							<form:errors path="isActive" cssClass="error-msg" />
						</div>
					</div>

				<input type="hidden" id="mode" name="mode" value="${mode}" />
				</div>
				<div class="row">
					<div class="text-center">					
						<button id="btnSearch" class="btn btn-primary"><spring:message code="lbl.search"/></button>
						<button type="reset" class="btn btn-default"><spring:message code="lbl.reset"/></button>
				        <a href="javascript:void(0)" class="btn btn-default" onclick="self.close()"><spring:message code="lbl.close"/></a>
					</div>
				</div>
	    </div>
	</div>
	</div>
</form:form>

 <div class="row display-hide report-section">
	<div class="col-md-12 table-header text-left">Registration Unit Search Result</div>
	<div class="col-md-12 form-group report-table-container">
		<table class="table table-bordered table-hover multiheadertbl"
			id="registrationUnitResultTable">
			<thead>
				<tr>
					<th><spring:message code="lbl.registrationunitname" /></th>
					<th><spring:message code="lbl.address" /></th>
					<th><spring:message code="lbl.zone" /></th>
					<th><spring:message code="lbl.isactive" /></th>
					<th><spring:message code="lbl.IsMainRegistrationUnit" /></th>
					<th><spring:message code="lbl.action"/></th>
				</tr>
			</thead>
			
		</table>
	</div>
</div> 
<script> 
$('#btnsearch').click(function(e){
 if($('form').valid()){
 }else{
 e.preventDefault();
 }  });
 
 
 

</script>
<link rel="stylesheet"
	href="<cdn:url value='/resources/global/js/jquery/plugins/datatables/responsive/css/datatables.responsive.css' context='/egi'/>">
<link rel="stylesheet" href="<cdn:url value='/resources/global/css/jquery/plugins/datatables/jquery.dataTables.min.css' context='/egi'/>"/>
<link rel="stylesheet" href="<cdn:url value='/resources/global/css/jquery/plugins/datatables/dataTables.bootstrap.min.css' context='/egi'/>">	
<script
	src="<cdn:url value='/resources/global/js/jquery/plugins/datatables/jquery.dataTables.min.js' context='/egi'/>"
	type="text/javascript"></script>
<script
	src="<cdn:url value='/resources/global/js/jquery/plugins/datatables/dataTables.bootstrap.js' context='/egi'/>"
	type="text/javascript"></script>
<script
	src="<cdn:url value='/resources/global/js/jquery/plugins/datatables/dataTables.tableTools.js' context='/egi'/>"
	type="text/javascript"></script>
<script
	src="<cdn:url value='/resources/global/js/jquery/plugins/datatables/TableTools.min.js' context='/egi'/>"
	type="text/javascript"></script>
<script
	src="<cdn:url value='/resources/global/js/jquery/plugins/datatables/responsive/js/datatables.responsive.js' context='/egi'/>"
	type="text/javascript"></script>
<script src="<cdn:url value='/resources/global/js/jquery/plugins/datatables/moment.min.js' context='/egi'/>"></script>
<script src="<cdn:url value='/resources/global/js/jquery/plugins/datatables/datetime-moment.js' context='/egi'/>"></script>

<script type="text/javascript" src="<cdn:url value='/resources/js/app/registrationunit.js?rnd=${app_release_no}'/>"></script>

