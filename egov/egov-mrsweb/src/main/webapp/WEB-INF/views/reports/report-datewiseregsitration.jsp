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
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt" %>
<%@ taglib uri="http://www.springframework.org/tags" prefix="spring" %>
<%@ taglib uri="http://www.springframework.org/tags/form" prefix="form" %>
<%@ taglib uri="/WEB-INF/taglib/cdn.tld" prefix="cdn" %>
<div class="row" id="page-content">
	<div class="col-md-12">
		<div class="panel" data-collapsed="0">
			<div class="panel-body">
			<form:form  method="post" action="" class="form-horizontal form-groups-bordered" id="form-registrationstatus" modelAttribute="registration">
			<div class="panel panel-primary" data-collapsed="0">
				<div class="panel-heading">
					<div class="panel-title">
							<strong><spring:message code="lbl.hdr.datewiseregistration"/></strong>
					</div>
				</div> 
				
				<div class="panel-body custom-form">	
				<div class="row">			
					<div class="form-group">
						<label class="col-sm-2 control-label">
							<spring:message code="lbl.fromDate"/>
						</label>
						<div class="col-sm-3 add-margin">
							<form:input path="fromDate" id="txt-fromdate" type="text" class="form-control low-width datepicker today" data-date-end-date="0d" data-date-today-highlight="true" placeholder="" autocomplete="off" />
            				<form:errors path="fromDate" cssClass="add-margin error-msg"/>
						</div>
					
					
						<label class="col-sm-2 control-label">
							<spring:message code="lbl.toDate"/>
						</label>
						<div class="col-sm-3 add-margin">
							<form:input path="toDate" id="txt-todate" type="text" class="form-control low-width datepicker today" data-date-end-date="0d" data-date-today-highlight="true" placeholder="" autocomplete="off" />
            				<form:errors path="toDate" cssClass="add-margin error-msg"/>
						</div>
					</div>
				</div>
					<div class="row">	
					<div class="form-group">
						<label for="field-1" class="col-sm-2 control-label"><spring:message code="lbl.registrationunit" />
						</label>
						<div class="col-sm-3 add-margin">
									<form:select path="marriageRegistrationUnit.id"
										id="select-registrationunit" cssClass="form-control"
										cssErrorClass="form-control error" >
										<form:option value="">
											<spring:message code="lbl.default.option" />
										</form:option>
										<form:options items="${marriageRegistrationUnit}"
											itemValue="id" itemLabel="name" />
									</form:select>
									<form:errors path="marriageRegistrationUnit.id" cssClass="add-margin error-msg"/>
						</div>

					
					
					
						<label for="field-1" class="col-sm-2 control-label"><spring:message code="lbl.registration.status" />
						</label>
						<div class="col-sm-3 add-margin">							
							<form:select path="status.code" id="status" cssClass="form-control" cssErrorClass="form-control error" >
										<form:option value=""><spring:message code="lbl.select"></spring:message></form:option>
										<c:forEach items="${status}" var="status">
											<option value="${status}">${status}</option>
										</c:forEach>
							</form:select>
							<form:errors path="status.code" cssClass="error-msg"/>
						</div>
					</div>
				</div>
				<div class="row">	
					<div class="form-group">
					
					<label class="col-sm-2 control-label">
			<spring:message code="lbl.Boundary"/>
		</label>
		<div class="col-sm-3 add-margin">	
			<form:select path="zone.id" id="select-zones" cssClass="form-control" 
						cssErrorClass="form-control error">
                 <form:option value=""> <spring:message code="lbl.default.option"/> </form:option>
                 <form:options items="${zones}" itemValue="id" itemLabel="name"/>
             </form:select>
            <form:errors path="zone.id" cssClass="add-margin error-msg"/>
					
			</div>
			</div>
			</div>
			<div class="row">
				<div class="text-center">					
					<button type="button" class="btn btn-primary" id="btn_dailyregistration_search"><spring:message code="lbl.search"/></button>
					<button type="reset" class="btn btn-default"><spring:message code="lbl.reset"/></button>
			        <a href="javascript:void(0)" class="btn btn-default" onclick="self.close()"><spring:message code="lbl.close"/></a>
				</div>
			</div>
			</div>
			</div>
		</form:form>
		
		</div>
        </div>
    </div>
</div>

<br /><br />
<div class="row display-hide report-section" id="regs_container">   
	<div class="col-md-12 table-header text-left">Registration Search result is</div>    
	<br />
	<div class="panel-heading">
		<div class="displayCount panel-title"></div>
	</div>           
	<div class="col-md-12 form-group report-table-container">
	    <table class="table table-bordered table-hover multiheadertbl" id="registration_table">
	    	<thead>
	    		<tr>
	    			<th><spring:message code="lbl.application.no"/></th>
	    			<th><spring:message code="lbl.registration.no"/></th>	
	    			<th><spring:message code="lbl.husband.name"/></th>
	    			<th><spring:message code="lbl.wife.name"/></th>
	    			<th>Registration Date</th>
	    			<th>Marriage Date</th>
	    			<th>Marriage Fee</th>
	    			<th><spring:message code="lbl.registration.status"/></th>
	    			<th><spring:message code="lbl.registrationunit"/></th>
	    			<th><spring:message code="lbl.Boundary"/></th>
	    			<th>Remarks</th>
	    		</tr>
	    	</thead>
	    	<tbody>
	    	</tbody>
		</table>
	</div>
</div>

<link rel="stylesheet" href="<cdn:url value='/resources/global/css/jquery/plugins/datatables/jquery.dataTables.min.css' context='/egi'/>"/>
<link rel="stylesheet" href="<cdn:url value='/resources/global/css/jquery/plugins/datatables/dataTables.bootstrap.min.css' context='/egi'/>">
<script	src="<cdn:url value='/resources/global/js/jquery/plugins/datatables/jquery.dataTables.min.js' context='/egi'/>"></script>
<script	src="<cdn:url value='/resources/global/js/jquery/plugins/datatables/responsive/js/datatables.responsive.js' context='/egi'/>"></script>
<script	src="<cdn:url value='/resources/global/js/jquery/plugins/datatables/dataTables.bootstrap.js' context='/egi'/>"></script>
<script type="text/javascript" src="<cdn:url value='/resources/global/js/jquery/plugins/datatables/dataTables.tableTools.js' context='/egi'/>"></script>
<script type="text/javascript" src="<cdn:url value='/resources/global/js/jquery/plugins/datatables/TableTools.min.js' context='/egi'/>"></script>

<script src="<cdn:url value='/resources/js/app/dailyregistrationreport.js?rnd=${app_release_no}'/> "></script>