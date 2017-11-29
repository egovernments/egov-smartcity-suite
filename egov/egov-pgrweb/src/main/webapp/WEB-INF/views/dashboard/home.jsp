<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
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

<script>
var isdefault = true;
</script>
<div id="performanceWin" class="container-win"  style="">
	<div class="row">
		<div class="col-md-6 col-lg-6 col-sm-12">
			<div id="performanceGIS" style="width:97%;min-height:500px;margin:0 auto;border:1px solid #41311d;" class="gm-style"></div>
		</div>
		<div class="col-md-6 col-lg-6 col-sm-12">
			<div id="performanceGraph" style="min-height:500px;margin:0 auto;" class="gm-style"></div>
		</div>
	</div>
	<div class="row">
		<div class="col-md-12 col-lg-12 col-sm-12">
			<div id="performanceTable" class="gm-style"></div>
		</div>
	</div>
</div>
<div id="slaWin" class="container-win" style="display:none;">
	<div class="row">
		<div class="col-md-6 col-lg-6 col-sm-12">
			<div id="slaGIS" style="min-height:500px;border:1px solid #41311d;" class="gm-style"></div>
		</div>
		<div class="col-md-6 col-lg-6 col-sm-12">
			<div id="slaGraph" class="gm-style"></div>
		</div>
	</div>
</div>
<div id="overviewWin" class="container-win" style="display:none;">
	<div id="overviewGIS" style="position:absolute;width:100%;min-height:100%;margin:0 auto;border:1px solid #41311d;" class="gm-style"></div>
</div>
<div id="topFiveCompTypeWin" class="container-win" style="display:none;">
	<div class="row" style="height:97%">
		<div class="col-md-12 col-sm-12 col-ms-12 col-lg-12" style="height:97%;">
			<div id="topFiveCompTypeGraph" style="width:100%;height:100%" class="gm-style"></div>
		</div>
	</div>
</div>
<!--<div id="wardwiseAnalysisWin" class="container-win" style="display:none">
	<div class="row">
		<div class="col-lg-12 col-md-12 col-sm-12">
			<div class="make-switch switch-small pull-right label-toggle-switch" id="sync" data-text-label="SYNC" data-on="success" data-off="danger"> 
				<input type="checkbox" checked class="label-toggle-switch">
			</div>
		</div>
	</div>
	<div class="row" >
		<div class="col-md-4 col-sm-4 col-lg-4" style="height:500px;margin-bottom:50px">
			<div class="bs-callout bs-callout-info header-darkred">Registered</div> 
			<div id="wardwiseAnalysis1"  style="min-height:500px"></div>
		</div>
		<div class="col-md-4 col-sm-4 col-ms-4 col-lg-4" style="height:500px;margin-bottom:50px">
			<div class="bs-callout bs-callout-info header-blue">Redressed</div>
			<div id="wardwiseAnalysis2" style="min-height:500px"></div>
		</div>
		<div class="col-md-4 col-sm-4 col-ms-4 col-lg-4" style="height:500px;margin-bottom:50px">
			<div class="bs-callout bs-callout-info header-brown">Complaints per Property</div>
			<div id="wardwiseAnalysis3" style="height:500px"></div>
		</div>
	</div>
	 <div class="row">
		<div class="col-md-4 col-sm-4 col-ms-4 col-lg-4" style="height:500px;margin-bottom:50px">
			<div class="bs-callout bs-callout-info header-lightblue">Top 1 > <span id="top1"></span></div>
			<div id="topGis1" style="min-height:500px"></div>			
		</div>
		<div class="col-md-4 col-sm-4 col-ms-4 col-lg-4" style="height:500px;margin-bottom:50px">
			<div class="bs-callout bs-callout-info header-olivegreen">Top 2 > <span id="top2"></span></div>
			<div id="topGis2" style="min-height:500px"></div>
		</div>
		<div class="col-md-4 col-sm-4 col-ms-4 col-lg-4" style="height:500px;margin-bottom:50px">
			<div class="bs-callout bs-callout-info header-lightgreen">Top 3 > <span id="top3"></span></div>
			<div id="topGis3" style="min-height:500px"></div>
		</div>
	</div> 
</div>-->

<!--div id="gisSearchWin" class="container-win" style="display:none;">
	<div class="row well" style="height:97%;margin-top:10px;background-color:transparent;">
		<div class="col-md-4 col-sm-4 col-ms-4 col-lg-4">
       		<div class="container-fluid">
       			<form  action="search" class="form-horizontal" id="gmapsearchform" name="gmapsearchform" style="color:black;">			    
  						<div class="row form-group">
  							<label for="complt" class="control-label">Complaint Type</label>
		       			<div>
							<select name="complaintType" id="complt" class="form-control"> 
								<option value="">Select a Complaint Type</option>
								<c:forEach items="${model.compTypeDropdown}" var="compTypeDropdown">
								<option value="${compTypeDropdown.key}">${compTypeDropdown.value}</option>
								</c:forEach>
							</select>
		       			</div>		 
		   				<label for="status" class="control-label">Complaint Status</label>
		       			<div >
							<select name="complaintStatus" id="status" class="form-control" >
								<option value="">Select a Complaint Status</option>
								<c:forEach items="${model.compStatusDropdown}" var="compStatusDropdown">
								<option value="${compStatusDropdown.key}">${compStatusDropdown.value}</option>
											</c:forEach>
							</select>
		       			</div>
		       			<label for="priority" class="control-label">Priority</label>
		       			<div>
							<select name="priority" id="priority" class="form-control" >
								<option value="">Select a Priority</option>
								<c:forEach items="${model.compPriorityDropdown}" var="compPriorityDropdown">
								<option value="${compPriorityDropdown.key}">${compPriorityDropdown.value}</option>
											</c:forEach>
							</select>
		       			</div>
		       			<label for="dept" class="control-label">Department</label>
		       			<div >
							<select name="dept" id="dept" class="form-control" >
								<option value="">Select a Department</option>
								<c:forEach items="${model.compDeptDropdown}" var="compDeptDropdown">
								<option value="${compDeptDropdown.key}">${compDeptDropdown.value}</option>
											</c:forEach>
							</select>
		       			</div>
		       			<label for="createdDateFrom" class="control-label">From Date</label>
		      			<div >		           
							<input type="text" name="fromDate" class="form-control" id="fromDate" placeholder="From Date" readonly="readonly"/>
		       			</div>
		       			<label for="createdDateTo" class="control-label">To Date</label>
		       			<div >
							<input type="text" name="toDate" class="form-control" id="toDate" placeholder='To Date' readonly="readonly"/>
		       			</div>		    	
		   			</div>
		   			<div class="row form-group">
		   				<div>
		   					<p class="text-center">
								<button class="btn btn-primary" id="search" type="button">
		   							<i class="glyphicon glyphicon-search"></i>&nbsp;Search
			 						</button>
		 						<button class="btn btn-default" id="reset" type="reset">
		   							<i class="glyphicon glyphicon-repeat"></i>&nbsp;Reset
		 						</button>
		     				</p>
		  				</div>
					</div>
				</form>
			</div>          
   		</div>
   		<div class="col-md-8 col-sm-8 col-ms-8 col-lg-8" style="height:100%;">
   			<div id="searchGIS" style="width:100%;height:100%;margin:0 auto;" class="gm-style"></div>
   		</div>
	</div>
</div-->