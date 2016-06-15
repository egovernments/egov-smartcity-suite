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
<!DOCTYPE html>
<html lang="en">
	<head>
		<meta charset="utf-8">
		<meta http-equiv="X-UA-Compatible" content="IE=edge">
		<meta name="viewport" content="width=device-width, initial-scale=1, maximum-scale=1, user-scalable=no">
		<meta name="description" content="Neon Admin Panel" />
		<meta name="author" content="" />
		
		<title>eGov Urban Portal</title>
		
		<link rel="stylesheet" href="../../../../../../../egov-egiweb/src/main/webapp/resources/global/css/bootstrap/bootstrap.css">
		<link rel="stylesheet" href="../../../../../../../egov-egiweb/src/main/webapp/resources/global/css/egov/custom.css">
		<link rel="stylesheet" href="../../../../../../../egov-egiweb/src/main/webapp/resources/global/css/font-icons/font-awesome/css/font-awesome.min.css">
		<link rel="stylesheet" href="../../../../../../../egov-egiweb/src/main/webapp/resources/global/css/bootstrap/treed.css">
		
		<!--[if lt IE 9]><script src="resources/js/ie8-responsive-file-warning.js"></script><![endif]-->
		
		<!-- HTML5 shim and Respond.js IE8 support of HTML5 elements and media queries -->
		<!--[if lt IE 9]>
			<script src="https://oss.maxcdn.com/libs/html5shiv/3.7.0/html5shiv.js"></script>
			<script src="https://oss.maxcdn.com/libs/respond.js/1.4.2/respond.min.js"></script>
		<![endif]-->
		
		
	</head>
	<body class="page-body">
		<div class="page-container" id="page-container">
			<header class="navbar navbar-fixed-top"><!-- set fixed position by adding class "navbar-fixed-top" -->
				
				<nav class="navbar navbar-default navbar-custom navbar-fixed-top">
					<div class="container-fluid">
						<div class="navbar-header col-md-10 col-xs-10">
							<a class="navbar-brand" href="javascript:void(0);">
								<!-- <img src="egov-egiweb/src/main/webapp/resources/global/images/chennai_logo.jpg" height="60"> -->
								<div>
									
									<span class="title2">Grievance Type Wise Report</span>
								</div>
							</a>
						</div>
						
						<div class="nav-right-menu col-md-2 col-xs-2">
							<ul class="hr-menu text-right">
								<li class="ico-menu">
									<a href="javascript:void(0);">
										<img src="../../../../../../../egov-egiweb/src/main/webapp/resources/global/images/logo@2x.png" title="Powered by eGovernments" height="20px">
									</a>
								</li>
								
							</ul>
						</div>
						
					</div>
				</nav>
				
			</header>
			<div class="main-content">

				<div class="container" style="margin-top:30px;">
				    <div class="row">

				        <div class="col-md-4">
				        	<div class="panel panel-default">
		        			<div class="panel-heading">
		        				PGR
		        			</div>
							  <div class="panel-body">
							  	<ul class="treed">
					                <li><a href="#">PGR</a>
					                    <ul>
					                        <li><a href="/pgr/report/complaintTypeReport" target="_blank">Grievance Type Wise Report</a></li>
					                        <li><a href="/pgr/report/ageingReportByDept" target="_blank">Ageing Report - Department wise</a></li>
					                        <li><a href="/pgr/report/ageingReportByBoundary" target="_blank">Ageing Report - Boundary wise</a></li>
					                        <li><a href="/pgr/report/drillDownReportByBoundary" target="_blank">Status Drill Down Report - Department wise</a></li>
					                        <li><a href="/pgr/report/drillDownReportByDept" target="_blank">Status Drill Down Report - Boundary wise</a></li>
					                        <!-- <li><a href="report.html" target="_blank">Functionary wise Ageing</a></li>
					                        <li><a href="report.html" target="_blank">Functionary wise Status</a></li> -->
					                    </ul>
					                </li>
					            </ul>
							  </div>
							</div>
				        </div>

				        <div class="col-md-4">
				        	<div class="panel panel-default">
		        			<div class="panel-heading">
		        				Collections
		        			</div>
							  <div class="panel-body">
							  	<ul class="treed">
					                <li><a href="#">Collections</a>
					                    <ul>
					                        <li><a href="/collection/reports/collectionSummary-criteria.action#no-back-button" target="_blank">Collection summary report</a></li>
					                        <li><a href="/collection/reports/remittanceVoucherReport-criteria.action#no-back-button" target="_blank">Remittance voucher report</a></li>
					                        <li><a href="/collection/reports/receiptRegisterReport-criteria.action#no-back-button" target="_blank">Receipt register report</a></li>
					                    </ul>
					                </li>
					            </ul>
							  </div>
							</div>
				        </div>

				        <div class="col-md-4">
				        	<div class="panel panel-default">
		        			<div class="panel-heading">
		        				Works
		        			</div>
							  <div class="panel-body">
							  	<ul class="treed">
					                <li><a href="#">Works</a>
					                    <ul>
					                        <li><a href="/egworks/reports/workprogressregister/searchform" target="_blank">Work Progress Register</a></li>
					                        <li><a href="/egworks/reports/estimateabstractreport/departmentwise-searchform" target="_blank">Estimate abstract by department</a></li>
					                        <li><a href="/egworks/reports/estimateabstractreport/typeofworkwise-searchform" target="_blank">Estimate abstract by type of work</a></li>
					                    </ul>
					                </li>
					            </ul>
							  </div>
							</div>
				        </div>

				        <div class="col-md-4">
				        	<div class="panel panel-default">
		        			<div class="panel-heading">
		        				PTIS
		        			</div>
							  <div class="panel-body">
							  	<ul class="treed">
					                <li><a href="#">PTIS</a>
					                    <ul>
					                        <li><a href="/ptis/reports/collectionSummaryReport-wardWise.action" target="_blank">Revenue zone wise collection report</a></li>
					                        <li><a href="/ptis/reports/defaultersReport-search.action#no-back-button" target="_blank">Defaulters Report</a></li>
					                        <li><a href="/ptis/reports/dCBReport-search.action#no-back-button" target="_blank">DCB Report</a></li>
					                        <li><a href="/ptis/report/baseRegister" target="_blank">Base Register</a></li>
					                        <li><a href="/ptis/report/dailyCollection" target="_blank">Daily collection report</a></li>
					                        <li><a href="/ptis/reports/arrearRegisterReport-index.action#no-back-button" target="_blank">Arrear Register report</a></li>
					                    </ul>
					                </li>
					            </ul>
							  </div>
							</div>
				        </div>

				        <div class="col-md-4">
				        	<div class="panel panel-default">
		        			<div class="panel-heading">
		        				Advertisement
		        			</div>
							  <div class="panel-body">
							  	<ul class="treed">
					                <li><a href="#">Advertisement</a>
					                    <ul>
					                        <li><a href="/adtax/reports/search-for-dcbreport" target="_blank">Advertisement Collection Report</a></li>
					                        <li><a href="/adtax/reports/search-dcbreport" target="_blank">Agency wise Collection Report</a></li>
					                    </ul>
					                </li>
					            </ul>
							  </div>
							</div>
				        </div>

				        <div class="col-md-4">
				        	<div class="panel panel-default">
		        			<div class="panel-heading">
		        				TL
		        			</div>
							  <div class="panel-body">
							  	<ul class="treed">
					                <li><a href="#">TL</a>
					                    <ul>
					                        <li><a href="/tl/tlreports/dCBReport/licenseNumberWise#no-back" target="_blank">DCB Report by Trade</a></li>
					                        <li><a href="/tl/search/searchTrade-newForm.action#no-back" target="_blank">View/Search Trade licenses</a></li>
					                    </ul>
					                </li>
					            </ul>
							  </div>
							</div>
				        </div>

				        <div class="col-md-4">
				        	<div class="panel panel-default">
		        			<div class="panel-heading">
		        				Water Charges
		        			</div>
							  <div class="panel-body">
							  	<ul class="treed">
					                <li><a href="#">Water Charges</a>
					                    <ul>
					                        <li><a href="/wtms/reports/dCBReport/wardWise" target="_blank">DCB Report revenue ward wise</a></li>
					                        <li><a href="/wtms/report/defaultersWTReport/search" target="_blank">Defaulters Report</a></li>
					                        <li><a href="/wtms/report/dailyWTCollectionReport/search/" target="_blank">Daily collection report</a></li>
					                        <li><a href="/wtms/reports/dCBReport/localityWise" target="_blank">DCB Report locality wise</a></li>
					                        <li><a href="/wtms/reports/coonectionReport/wardWise" target="_blank">Number of connections</a></li>
					                        <li><a href="/wtms/report/baseRegister" target="_blank">Base Register report</a></li>
					                    </ul>
					                </li>
					            </ul>
							  </div>
							</div>
				        </div>



				    </div>
				</div>

			</div>
			<footer class="main">
					
					Powered by <a href="http://eGovernments.org" target="_blank">eGovernments Foundation</a>
					
			</footer> 
		</div>
		<script src="../../../../../../../egov-egiweb/src/main/webapp/resources/global/js/jquery/jquery.js"></script>
		<script src="../../../../../../../egov-egiweb/src/main/webapp/resources/global/js/bootstrap/bootstrap.js"></script>
		<script src="../../../../../../../egov-egiweb/src/main/webapp/resources/global/js/bootstrap/treed.js"></script>
	</body>
</html>



