<%--
  ~ eGov  SmartCity eGovernance suite aims to improve the internal efficiency,transparency,
  ~ accountability and the service delivery of the government  organizations.
  ~
  ~  Copyright (C) <2017>  eGovernments Foundation
  ~
  ~  The updated version of eGov suite of products as by eGovernments Foundation
  ~  is available at http://www.egovernments.org
  ~
  ~  This program is free software: you can redistribute it and/or modify
  ~  it under the terms of the GNU General Public License as published by
  ~  the Free Software Foundation, either version 3 of the License, or
  ~  any later version.
  ~
  ~  This program is distributed in the hope that it will be useful,
  ~  but WITHOUT ANY WARRANTY; without even the implied warranty of
  ~  MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
  ~  GNU General Public License for more details.
  ~
  ~  You should have received a copy of the GNU General Public License
  ~  along with this program. If not, see http://www.gnu.org/licenses/ or
  ~  http://www.gnu.org/licenses/gpl.html .
  ~
  ~  In addition to the terms of the GPL license to be adhered to in using this
  ~  program, the following additional terms are to be complied with:
  ~
  ~      1) All versions of this program, verbatim or modified must carry this
  ~         Legal Notice.
  ~ 	Further, all user interfaces, including but not limited to citizen facing interfaces,
  ~         Urban Local Bodies interfaces, dashboards, mobile applications, of the program and any
  ~         derived works should carry eGovernments Foundation logo on the top right corner.
  ~
  ~ 	For the logo, please refer http://egovernments.org/html/logo/egov_logo.png.
  ~ 	For any further queries on attribution, including queries on brand guidelines,
  ~         please contact contact@egovernments.org
  ~
  ~      2) Any misrepresentation of the origin of the material is prohibited. It
  ~         is required that all modified versions of this material be marked in
  ~         reasonable ways as different from the original version.
  ~
  ~      3) This license does not grant any rights to any user of the program
  ~         with regards to rights under trademark law for use of the trade names
  ~         or trademarks of eGovernments Foundation.
  ~
  ~  In case of any queries, you can reach eGovernments Foundation at contact@egovernments.org.
  --%>

<%@ taglib uri="/WEB-INF/taglib/cdn.tld" prefix="cdn" %>
			<header class="navbar navbar-fixed-top"><!-- set fixed position by adding class "navbar-fixed-top" -->
				
				<nav class="navbar navbar-default navbar-custom navbar-fixed-top">
					<div class="container-fluid">
						<div class="navbar-header col-md-10 col-xs-10">
							<a class="navbar-brand" href="javascript:void(0);">
								<!-- <img src="egov-egiweb/src/main/webapp/resources/global/images/chennai_logo.jpg" height="60"> -->
								<div>
									<span class="title2" id="header-text" data-append-text="ERP Reports">ERP Reports</span>
								</div>
							</a>
						</div>
						
						<div class="nav-right-menu col-md-2 col-xs-2">
							<ul class="hr-menu text-right">
								<li class="ico-menu">
									<a href="javascript:void(0);">
						<img src="<cdn:url  value='/resources/global/images/logo@2x.png' context='/egi'/>" title="Powered by eGovernments" height="20px">
				
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
							  		<span class="more"><a href="javascript:void(0)" class="accordion" data-collapse="more">More <i class="fa fa-angle-down" aria-hidden="true"></i></a></span>
			                        <li class="report-li"><a href="/pgr/report/grievancetypewise" class="open-popup">Grievance Type Wise Report</a></li>
			                        <li class="report-li"><a href="/pgr/report/ageing/departmentwise" class="open-popup">Ageing Report - Department wise</a></li>
			                        <li class="report-li hide"><a href="/pgr/report/ageing/boundarywise" class="open-popup">Ageing Report - Boundary wise</a></li>
			                        <li class="report-li hide"><a href="/pgr/report/drilldown/departmentwise" class="open-popup">Status Drill Down Report - Department wise</a></li>
			                        <li class="report-li hide"><a href="/pgr/report/drilldown/boundarywise" class="open-popup">Status Drill Down Report - Boundary wise</a></li>
							 	</div>
							</div>
						</div>
						
						<div class="col-md-4">
				        	<div class="panel panel-default">
       		        			<div class="panel-heading">
       		        				Property Tax
       		        			</div>
								<div class="panel-body">
									<span class="more"><a href="javascript:void(0)" class="accordion" data-collapse="more">More <i class="fa fa-angle-down" aria-hidden="true"></i></a></span>
									<li class="report-li"><a href="/ptis/reports/collectionSummaryReport-wardWise.action" class="open-popup">Revenue ward wise collection report</a></li>
				                    <li class="report-li"><a href="/ptis/reports/collectionSummaryReport-localityWise.action" class="open-popup">Locality wise collection report</a></li>
				                    <li class="report-li hide"><a href="/ptis/reports/collectionSummaryReport-zoneWise.action" class="open-popup">Zone wise collection report</a></li>
				                    <li class="report-li hide"><a href="/ptis/reports/collectionSummaryReport-blockWise.action#no-back-button" class="open-popup">Block wise collection report</a></li>
				                    <li class="report-li hide"><a href="/ptis/reports/defaultersReport-search.action#no-back-button" class="open-popup">Defaulters Report<span class="lock"> <i class="fa fa-lock"></i></span></a></li>
				                    <li class="report-li hide"><a href="/ptis/reports/dCBReport-search.action#no-back-button" class="open-popup">DCB Report</a></li>
				                    <li class="report-li hide"><a href="/ptis/report/baseRegister" class="open-popup">Base Register<span class="lock"> <i class="fa fa-lock"></i></span></a></li>
				                    <li class="report-li hide"><a href="/ptis/report/dailyCollection" class="open-popup">Daily collection report</a></li>
				                    <li class="report-li hide"><a href="/ptis/reports/arrearRegisterReport-index.action#no-back-button" class="open-popup">Arrear Register report<span class="lock"> <i class="fa fa-lock"></i></span></a></li>
				                    <li class="report-li hide"><a href="/ptis/report/currentInstDCB" class="open-popup">Current installment DCB report</a></li>
								</div>
      						</div>
				        </div>
				        
				        <div class="col-md-4">
				        	<div class="panel panel-default">
       		        			<div class="panel-heading">
       		        				Water Charge Management
       		        			</div>
								<div class="panel-body">
									<span class="more"><a href="javascript:void(0)" class="accordion" data-collapse="more">More <i class="fa fa-angle-down" aria-hidden="true"></i></a></span>
									<li class="report-li"><a href="/wtms/reports/dCBReport/wardWise" class="open-popup">DCB Report revenue ward wise<span class="lock"> <i class="fa fa-lock"></i></span></a></li>
				                    <li class="report-li"><a href="/wtms/report/defaultersWTReport/search" class="open-popup">Defaulters Report<span class="lock"> <i class="fa fa-lock"></i></span></a></li>
				                    <li class="report-li hide"><a href="/wtms/report/dailyWTCollectionReport/search" class="open-popup">Daily collection report</a></li>
				                    <li class="report-li hide"><a href="/wtms/reports/dCBReport/localityWise" class="open-popup">DCB Report locality wise</a></li>
				                    <li class="report-li hide"><a href="/wtms/reports/coonectionReport/wardWise" class="open-popup">Number of connections</a></li>
				                    <li class="report-li hide"><a href="/wtms/report/baseRegister" class="open-popup">Base Register report<span class="lock"> <i class="fa fa-lock"></i></span></a></li>
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
									<span class="more"><a href="javascript:void(0)" class="accordion" data-collapse="more">More <i class="fa fa-angle-down" aria-hidden="true"></i></a></span>
									<li class="report-li"><a href="/collection/reports/collectionSummary-criteria.action#no-back-button" class="open-popup">Collection summary report</a></li>
				                    <li class="report-li"><a href="/collection/reports/remittanceVoucherReport-criteria.action#no-back-button" class="open-popup">Remittance voucher report<span class="lock"> <i class="fa fa-lock"></i></span></a></li>
				                    <li class="report-li hide"><a href="/collection/reports/receiptRegisterReport-criteria.action#no-back-button" class="open-popup">Receipt register report<span class="lock"> <i class="fa fa-lock"></i></span></a></li>
								</div>
							</div>
				        </div>
				        
				        <div class="col-md-4">
				        	<div class="panel panel-default">
     		        			<div class="panel-heading">
     		        				Works Management
     		        			</div>
								<div class="panel-body">
									<span class="more"><a href="javascript:void(0)" class="accordion" data-collapse="more">More <i class="fa fa-angle-down" aria-hidden="true"></i></a></span>
									<li class="report-li"><a href="/egworks/reports/workprogressregister/searchform" class="open-popup">Work Progress Register</a></li>
				                    <li class="report-li"><a href="/egworks/reports/estimateabstractreport/departmentwise-searchform" class="open-popup">Estimate abstract by department</a></li>
				                    <li class="report-li hide"><a href="/egworks/reports/estimateabstractreport/typeofworkwise-searchform" class="open-popup">Estimate abstract by type of work</a></li>
								</div>
     						</div>
				        </div>
				        
				        <div class="col-md-4">
				        	<div class="panel panel-default">
       		        			<div class="panel-heading">
       		        				Advertisement Tax
       		        			</div>
								<div class="panel-body">
									<li class="report-li"><a href="/adtax/reports/search-for-dcbreport" class="open-popup">Advertisement Collection Report</a></li>
				                    <li class="report-li"><a href="/adtax/reports/dcbreport-search" class="open-popup">Agency wise Collection Report</a></li>
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
									<li class="report-li"><a href="/tl/public/report/dcbreport/licensenumberwise#no-back" class="open-popup">DCB Report by Trade</a></li>
				                    <li class="report-li"><a href="/tl/search/searchTrade-newForm.action#no-back" class="open-popup">View/Search Trade licenses<span class="lock"> <i class="fa fa-lock"></i></span></a></li>
								</div>
       						</div>
				        </div>
				        
				        <div class="col-md-4">
				        	<div class="panel panel-default">
       		        			<div class="panel-heading">
       		        				Employee Management
       		        			</div>
								<div class="panel-body">
				                    <li class="report-li"><a href="/eis/reports/employeeassignments/searchform" class="open-popup">Employee Assignment Report<span class="lock"> <i class="fa fa-lock"></i></span></a></li>
								</div>
       						</div>
				        </div>
				        
				    </div> 

				</div>

			</div>
		<link rel="stylesheet" href="<cdn:url  value='/resources/global/css/bootstrap/bootstrap.css' context='/egi'/>">
		<link rel="stylesheet" href="<cdn:url  value='/resources/global/css/font-icons/font-awesome/css/font-awesome.min.css' context='/egi'/>">
<link rel="stylesheet"
      href="<cdn:url  value='/resources/global/css/egov/custom.css?rnd=${app_release_no}' context='/egi'/>">
		<link rel="stylesheet" href="<cdn:url  value='/resources/css/report.css' />">
		
		<script src="<cdn:url  value='/resources/global/js/jquery/jquery.js' context='/egi'/>"></script>
		<script src="<cdn:url  value='/resources/global/js/bootstrap/bootstrap.js' context='/egi'/>"></script>
		<script src="<cdn:url  value='/resources/js/report.js' />"></script>
		



