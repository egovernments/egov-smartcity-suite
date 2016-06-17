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
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<!DOCTYPE html>
<html lang="en">
		<body class="page-body">
		<div class="page-container" id="page-container">
			<header class="navbar navbar-fixed-top"><!-- set fixed position by adding class "navbar-fixed-top" -->
				
				<nav class="navbar navbar-default navbar-custom navbar-fixed-top">
					<div class="container-fluid">
						<div class="navbar-header col-md-10 col-xs-10">
							<a class="navbar-brand" href="javascript:void(0);">
								<!-- <img src="egov-egiweb/src/main/webapp/resources/global/images/chennai_logo.jpg" height="60"> -->
								<div>
									
									<span class="title2">ERP Reports</span>
								</div>
							</a>
						</div>
						
						<div class="nav-right-menu col-md-2 col-xs-2">
							<ul class="hr-menu text-right">
								<li class="ico-menu">
									<a href="javascript:void(0);">
						<img src="<c:url value='/resources/global/images/logo@2x.png' context='/egi'/>" title="Powered by eGovernments" height="20px">
				
									</a>
								</li>
								
							</ul>
						</div>
						
					</div>
				</nav>
				
			</header>
			
			<div class="main-content">

				<div class="container-fluid">
				    
				    <div class="row">
				    	<div class="col-sm-8 text-right">
				    		<button type="button" class="btn btn-secondary expand">Expand All</button>
				    		<button type="button" class="btn btn-secondary collapsable">Collapse All</button>
				    	</div>
				    	<div class="col-sm-4 text-center">
				    		<div class="input-group">
							    <input type="text" class="form-control search"/>
							    <span class="input-group-addon">
							        <i class="fa fa-search"></i>
							    </span>
							</div>
							<ul class="search-ul">
							</ul>
				    	</div>
				    </div>
				    
				    <br>
				    
				    <div class="row">
				    	<div class="col-md-4">
				        	<div class="panel panel-default">
			        			<div class="panel-heading">
			        				Grievance Redressal
			        			</div>
							  	<div class="panel-body">
							  		<span><a href="javascript:void(0)" class="accordion" data-collapse="more">More <i class="fa fa-angle-down" aria-hidden="true"></i></a></span>
			                        <li class="report-li"><a href="/pgr/report/complaintTypeReport" class="open-popup" data-strwindname="Grievance Type Wise Report">Grievance Type Wise Report</a></li>
			                        <li class="report-li"><a href="/pgr/report/ageingReportByDept" class="open-popup" data-strwindname="Ageing Report - Department wise">Ageing Report - Department wise</a></li>
			                        <li class="report-li hide"><a href="/pgr/report/ageingReportByBoundary" class="open-popup" data-strwindname="Ageing Report - Boundary wise">Ageing Report - Boundary wise</a></li>
			                        <li class="report-li hide"><a href="/pgr/report/drillDownReportByBoundary" class="open-popup" data-strwindname="Status Drill Down Report - Department wise">Status Drill Down Report - Department wise</a></li>
			                        <li class="report-li hide"><a href="/pgr/report/drillDownReportByDept" class="open-popup" data-strwindname="Status Drill Down Report - Boundary wise">Status Drill Down Report - Boundary wise</a></li>
							 	</div>
							</div>
						</div>
						
						<div class="col-md-4">
				        	<div class="panel panel-default">
       		        			<div class="panel-heading">
       		        				Property Tax
       		        			</div>
								<div class="panel-body">
									<span><a href="javascript:void(0)" class="accordion" data-collapse="more">More <i class="fa fa-angle-down" aria-hidden="true"></i></a></span>
									<li class="report-li"><a href="/ptis/reports/collectionSummaryReport-wardWise.action" class="open-popup" data-strwindname="Revenue zone wise collection report">Revenue zone wise collection report</a></li>
				                    <li class="report-li"><a href="/ptis/reports/defaultersReport-search.action#no-back-button" class="open-popup" data-strwindname="Defaulters Report">Defaulters Report</a></li>
				                    <li class="report-li hide"><a href="/ptis/reports/dCBReport-search.action#no-back-button" class="open-popup" data-strwindname="DCB Report">DCB Report</a></li>
				                    <li class="report-li hide"><a href="/ptis/report/baseRegister" class="open-popup" data-strwindname="Base Register">Base Register</a></li>
				                    <li class="report-li hide"><a href="/ptis/report/dailyCollection" class="open-popup" data-strwindname="Daily collection report">Daily collection report</a></li>
				                    <li class="report-li hide"><a href="/ptis/reports/arrearRegisterReport-index.action#no-back-button" class="open-popup" data-strwindname="Arrear Register report">Arrear Register report</a></li>
								</div>
      						</div>
				        </div>
				        
				        <div class="col-md-4">
				        	<div class="panel panel-default">
       		        			<div class="panel-heading">
       		        				Water Charge Management
       		        			</div>
								<div class="panel-body">
									<span><a href="javascript:void(0)" class="accordion" data-collapse="more">More <i class="fa fa-angle-down" aria-hidden="true"></i></a></span>
									<li class="report-li"><a href="/wtms/reports/dCBReport/wardWise" class="open-popup" data-strwindname="DCB Report revenue ward wise">DCB Report revenue ward wise</a></li>
				                    <li class="report-li"><a href="/wtms/report/defaultersWTReport/search" class="open-popup" data-strwindname="Defaulters Report">Defaulters Report</a></li>
				                    <li class="report-li hide"><a href="/wtms/report/dailyWTCollectionReport/search/" class="open-popup" data-strwindname="Daily collection report">Daily collection report</a></li>
				                    <li class="report-li hide"><a href="/wtms/reports/dCBReport/localityWise" class="open-popup" data-strwindname="DCB Report locality wise">DCB Report locality wise</a></li>
				                    <li class="report-li hide"><a href="/wtms/reports/coonectionReport/wardWise" class="open-popup" data-strwindname="Number of connections">Number of connections</a></li>
				                    <li class="report-li hide"><a href="/wtms/report/baseRegister" class="open-popup" data-strwindname="Base Register report">Base Register report</a></li>
								</div>
       						</div>
				        </div>
				    </div>
				    
				    <div class="row">
				    	<div class="col-md-4">
				        	<div class="panel panel-default">
					   			<div class="panel-heading">
					   				Collection
					   			</div>
								<div class="panel-body">
									<span><a href="javascript:void(0)" class="accordion" data-collapse="more">More <i class="fa fa-angle-down" aria-hidden="true"></i></a></span>
									<li class="report-li"><a href="/collection/reports/collectionSummary-criteria.action#no-back-button" class="open-popup" data-strwindname="Collection summary report">Collection summary report</a></li>
				                    <li class="report-li"><a href="/collection/reports/remittanceVoucherReport-criteria.action#no-back-button" class="open-popup" data-strwindname="Remittance voucher report">Remittance voucher report</a></li>
				                    <li class="report-li hide"><a href="/collection/reports/receiptRegisterReport-criteria.action#no-back-button" class="open-popup" data-strwindname="Receipt register report">Receipt register report</a></li>
								</div>
							</div>
				        </div>
				        
				        <div class="col-md-4">
				        	<div class="panel panel-default">
     		        			<div class="panel-heading">
     		        				Works Management
     		        			</div>
								<div class="panel-body">
									<span><a href="javascript:void(0)" class="accordion" data-collapse="more">More <i class="fa fa-angle-down" aria-hidden="true"></i></a></span>
									<li class="report-li"><a href="/egworks/reports/workprogressregister/searchform" class="open-popup" data-strwindname="Work Progress Register">Work Progress Register</a></li>
				                    <li class="report-li"><a href="/egworks/reports/estimateabstractreport/departmentwise-searchform" class="open-popup" data-strwindname="Estimate abstract by department">Estimate abstract by department</a></li>
				                    <li class="report-li hide"><a href="/egworks/reports/estimateabstractreport/typeofworkwise-searchform" class="open-popup" data-strwindname="Estimate abstract by type of work">Estimate abstract by type of work</a></li>
								</div>
     						</div>
				        </div>
				        
				        <div class="col-md-4">
				        	<div class="panel panel-default">
       		        			<div class="panel-heading">
       		        				Advertisement Tax
       		        			</div>
								<div class="panel-body">
									<li class="report-li"><a href="/adtax/reports/search-for-dcbreport" class="open-popup" data-strwindname="Advertisement Collection Report">Advertisement Collection Report</a></li>
				                    <li class="report-li"><a href="/adtax/reports/search-dcbreport" class="open-popup" data-strwindname="Agency wise Collection Report">Agency wise Collection Report</a></li>
								</div>
       						</div>
				        </div>
				    </div>
				    
				    <div class="row">
				    	<div class="col-md-4">
				        	<div class="panel panel-default">
       		        			<div class="panel-heading">
       		        				Trade License
       		        			</div>
								<div class="panel-body">
									<li class="report-li"><a href="/tl/tlreports/dCBReport/licenseNumberWise#no-back" class="open-popup" data-strwindname="DCB Report by Trade">DCB Report by Trade</a></li>
				                    <li class="report-li"><a href="/tl/search/searchTrade-newForm.action#no-back" class="open-popup" data-strwindname="View/Search Trade licenses">View/Search Trade licenses</a></li>
								</div>
       						</div>
				        </div>
				    </div> 

				</div>

			</div>
		</div>
		<link rel="stylesheet" href="<c:url value='/resources/global/css/bootstrap/bootstrap.css' context='/egi'/>">
		<link rel="stylesheet" href="<c:url value='/resources/global/css/font-icons/font-awesome/css/font-awesome.min.css' context='/egi'/>">
		<link rel="stylesheet" href="<c:url value='/resources/global/css/egov/custom.css' context='/egi'/>">
		<link rel="stylesheet" href="<c:url value='/resources/css/report.css' />">
		
		<script src="<c:url value='/resources/global/js/jquery/jquery.js' context='/egi'/>"></script>
		<script src="<c:url value='/resources/global/js/bootstrap/bootstrap.js' context='/egi'/>"></script>
		<script src="<c:url value='/resources/js/report.js' />"></script>
		
		</body>
</html>



