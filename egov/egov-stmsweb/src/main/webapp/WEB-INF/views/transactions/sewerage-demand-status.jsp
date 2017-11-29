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
<%@ taglib uri="http://www.springframework.org/tags" prefix="spring" %>
<%@ taglib uri="http://www.springframework.org/tags/form" prefix="form" %>
<%@ taglib uri="/WEB-INF/taglib/cdn.tld" prefix="cdn" %>

<div class="row">
    <div class="col-md-12">
    	<c:if test="${not empty message}">
			<div class="alert alert-success" role="alert"><spring:message code="${message}"/></div>
		</c:if>
    	<form:form id="seweragedemandstatussearchform" method="post" class="form-horizontal form-groups-bordered" modelAttribute="sewerageDemandStatusDetails" commandName="sewerageDemandStatusDetails">
	        <div class="panel panel-primary" data-collapsed="0">
	            <div class="panel-heading">
	                <div class="panel-title"></div>
	            </div>
	            <div class="panel-body custom-form">
                  
                    <div class="form-group">	
						<label for="field-1" class="col-sm-3 control-label">
						<spring:message code="lbl.financial.year" /><span class="mandatory"></span>
						</label>	
						<div class="col-sm-3 add-margin">
							<form:select path="financialYear" data-first-option="false"	id="financialyear" cssClass="form-control"  >
								<form:option value=""><spring:message code="lbl.select" /></form:option>
								<form:options items="${financialYear}" itemLabel="description" itemValue="description"/>
							</form:select>
						</div>		
					</div>		
       		
	            </div>
	        </div>
        	<div class="row">
       			<div class="text-center">
       				<button type="button" id="submit" class="btn btn-primary" ><spring:message code="lbl.submit"/></button>
          		    <button type="reset" id="reset" class="btn btn-default"><spring:message code="lbl.reset"/></button>
          		    <a href="javascript:void(0)" class="btn btn-default" onclick="self.close()"><spring:message code="lbl.close"/></a>
          	</div>
        	</div>
        </form:form>

		<div id="showhide" class="showhide">
	         <div  class="row display-hide report-section">
	        	<div  class="col-md-12 table-header text-left">Scheduled Status for Demand Generation</div>
	        	<div class="col-md-12 form-group report-table-container">
		        	<table class="table table-bordered table-hover multiheadertbl" id="demandresultTable">
		        		<thead>
		        			<tr>
		        				<th><spring:message code="lbl.demand.Scheduled" /></th>
		        				<th><spring:message code="lbl.status" /></th>
		        				<th><spring:message code="lbl.demand.created.Date" /></th>
		        				
		        			</tr>
		        		</thead>
		        	</table>
	        	</div>
	        </div>
        </div>
        <div class="row display-hide report-section">
        	<div class="col-md-12 table-header text-left">Demand Generation Status Search Result</div>
        	<div class="col-md-12 form-group report-table-container">
	        	<table class="table table-bordered table-hover multiheadertbl" id="resultTable">
	        		<thead>
	        			<tr>
	        				<th><spring:message code="lbl.financial.year" /></th>
	        				<th><spring:message code="lbl.demand.gen.status.success" /></th>
	        				<th><spring:message code="lbl.demand.gen.status.failure" /></th>
	        			</tr>
	        		</thead>
	        	</table>
        	</div>
        </div>
               
	</div>
</div>

<link rel="stylesheet"
	href="<cdn:url value='/resources/global/css/jquery/plugins/datatables/jquery.dataTables.min.css' context='/egi'/>" />
<link rel="stylesheet"
	href="<cdn:url value='/resources/global/css/jquery/plugins/datatables/dataTables.bootstrap.min.css' context='/egi'/>">
<script type="text/javascript"
	src="<cdn:url  value='/resources/global/js/jquery/plugins/datatables/jquery.dataTables.min.js' context='/egi'/>"></script>
<script type="text/javascript"
	src="<cdn:url  value='/resources/global/js/jquery/plugins/datatables/dataTables.bootstrap.js' context='/egi'/>"></script>
<script src="<cdn:url value='/resources/js/transactions/sewerage-demand-status.js?rnd=${app_release_no}'/>"></script>
