<!DOCTYPE html>
<!--[if lt IE 7 ]> <html lang="en" class="no-js ie6 lt8"> <![endif]-->
<!--[if IE 7 ]>    <html lang="en" class="no-js ie7 lt8"> <![endif]-->
<!--[if IE 8 ]>    <html lang="en" class="no-js ie8 lt8"> <![endif]-->
<!--[if IE 9 ]>    <html lang="en" class="no-js ie9"> <![endif]-->
<!--[if (gt IE 9)|!(IE)]><!--> 
<html lang="en" class="no-js"> 
<!--<![endif]-->
	<head>
		<meta charset="UTF-8">
		<meta http-equiv="X-UA-Compatible" content="IE=edge,chrome=1">
		<title>${model.citiname} Smart City Dashboard</title>
		<meta name="viewport" content="width=device-width, initial-scale=1.0">
       
		<link rel="stylesheet" href="resources/bootstrap/v3.3.1/css/bootstrap.min.css">
		<link rel="stylesheet" href="resources/bootstrap/v3.3.1/css/bootstrap-theme.min.css">
		<link rel="stylesheet" href="resources/fontawesome/v4.2.0/css/font-awesome.min.css">
		<link rel="stylesheet" href="resources/css/grayscale.css">
		<link rel="stylesheet" href="resources/css/plugins/social-buttons.css">
		<link rel="stylesheet" href="resources/css/home.css">
		
		<script src="resources/jquery/v2.1.3/jquery-2.1.3.min.js"></script>	
		
		<script>
			$(window).load(function() {
				$("#cover").delay(1000).slideUp(300);
				$("#preloader-container").delay(1000).slideUp(300);
			});
			var pgrRegdTrend = ${model.registeredTrend};
			var pgrCompletionTrend = ${model.completedTrend};
			var revenueTrend = ${model.revenueTrend};
			var paymentTrend = ${model.paymentTrend};		
			var revenueTrendForWeek = ${model.revenueTrendForWeek};
			var paymentTrendForWeek = ${model.paymentTrendForWeek};
		</script>		
    	<!-- HTML5 Shim and Respond.js IE8 support of HTML5 elements and media queries -->
    	<!-- WARNING: Respond.js doesn't work if you view the page via file:// -->
    	<!--[if lt IE 9]>
        <script src="resources/js/html5shiv.js"></script>
        <script src="resources/js/respond.min.js"></script>
    	<![endif]-->    		
    </head>
    <body id="page-top" data-spy="scroll" />
    	<div class="cover" id="cover"></div>
    	<div class="preloader-container" id="preloader-container">
    		<div class="preloader-logo">eGov<img src="resources/img/mask.gif">Dashboard</div>
    	</div>
    	<nav class="navbar navbar-inverse navbar-fixed-top effect-shadow clear-fix" role="navigation">
       		<div class="navbar-header pull-left" style="margin-bottom:5px">
               	<span class="navbar-brand page-scroll" href="#page-top">
                   <span class="visible-lg-block visible-md-block" style="color:white"><img src="resources/img/egov_logo.png" alt="EgovLogo"  class="img-thumbnail" style="margin-top:-10px;height:45px"> ${model.citiname} Smart City Dashboard</span>
                   <span class="visible-sm-block visible-xs-block" style="color:white"><img src="resources/img/egov_logo.png" alt="EgovLogo"  class="img-thumbnail" style="margin-top:-10px;height:45px"></i> Smart City Dashboard</span>
               	</span>
           	</div>
    	</nav>
    	<header class="intro">
    		<div class="intro-body">
          		<div class="container-fluid">
          			 <div class="row">
          			 	<div class="col-lg-4 col-md-4 col-sm-4">
	              			<a href="revenuedashboard/home.do" class="xa">
	                  		<div class="panel-footer-new">
	                           	<font class="text-center" style="font-size: 18px;">Property Tax</font>
	                        </div>
	                        <div id="newtimeLineRevenueTrend" class="chart-style" style="min-height: 300px;"></div>
	                  		</a>
	              		</div>
	            		<div class="col-lg-4 col-md-4 col-sm-4">
	            			<a href="pgrdashboard/home.do?isdefault=false" class="xa">
	                  		<div class="panel-footer-new">
	                          	<font class="text-center" style="font-size: 18px;">Complaints</font>
	                          </div>
	                          <div id="newtimeLineCompTrend" class="chart-style" style="min-height: 300px;"></div>
	                  		</a>
	              		</div>
	              		<div class="col-lg-4 col-md-4 col-sm-4">
	              			<a href="expendashboard/home.do" class="xa">
	                  		<div class="panel-footer-new">
                            	<font class="text-center" style="font-size: 18px;">Expenditure</font>
                          	</div>
                          	<div id="newtimeLinePaymentTrend" class="chart-style" style="min-height: 300px;"></div>
	                  		</a>
	              		</div>
					</div>
					<div class="row">
						<div class="col-lg-12 col-md-12 col-sm-12" style="min-height:10px">
						
						</div>
					</div>
					<div class="row">
	              		<div class="col-lg-4 col-md-4 col-sm-4">
	              			<a href="revenuedashboard/home.do" class="xa">
	              				<div id="overviewGraphCumilative" class="chart-style" style="min-height: 300px;"></div>
	                  		</a>
	              		</div>
	              		<div class="col-lg-4 col-md-4 col-sm-4">
	            			<a href="pgrdashboard/home.do?isdefault=false" class="xa">
	            				<div id="slaGraph" class="chart-style" style="min-height: 300px;" ></div>
	                  		</a>
	              		</div>
	              		<div class="col-lg-4 col-md-4 col-sm-4">
	              			<a href="expendashboard/home.do" class="xa">
	              				<div id="performanceLineGraph" class="chart-style" style="min-height: 300px;"></div>
	                  		</a>
	              		</div>
					</div>
					<div class="row">
						<div class="col-lg-12 col-md-12 col-sm-12" style="min-height:10px">
						
						</div>
					</div>
					<div class="row">
						<div class="col-lg-4 col-md-4 col-sm-4">
	              			<a href="revenuedashboard/home.do" class="xa">
	                  		<div id="timeLineRevenueTrend" class="chart-style" style="min-height: 300px;"></div>
	                  		</a>
	              		</div>
	            		<div class="col-lg-4 col-md-4 col-sm-4">
	            			<a href="pgrdashboard/home.do?isdefault=true" class="xa">
	                  			<div id="overviewGraph" class="chart-style" style="min-height: 300px;"></div>
	                  		</a>
	              		</div>
	              		<div class="col-lg-4 col-md-4 col-sm-4">
	              			<a href="expendashboard/home.do" class="xa">
	                  		<div id="timeLinePaymentTrend" class="chart-style" style="min-height: 300px;"></div>
	                  		</a>
	              		</div>
					</div>
          		</div>
        	</div>
        </header>
    	<footer class="footer" id="about">
    		<div id="zt-footer" class="clearfix ">
				<div class="container-fluid">
					<div id="zt-footer-inner" class="row">
						<div id="zt-footer-left" class="col-md-6">
							<div id="zt-footer-copy">
							&copy; 2014 <a href="http://www.egovernments.org" style="text-decoration:none" target="_blank">eGovernments Foundation</a><sup>&reg;</sup>. All rights reserved.							
							</div>
						</div>
						<div id="zt-social" class="col-md-6 text-right">
							<div>
								<a href="http://facebook.com/egfindia" title="Facebook" class="btn btn-social-icon btn-facebook btn-circle-small social-icon" target="_blank"><i class="fa fa-facebook"></i></a>
								<a href="http://twitter.com/egfindia" title="Twitter" class="btn btn-social-icon btn-twitter btn-circle-small social-icon" target="_blank"><i class="fa fa-twitter"></i></a>
								<a href="https://www.linkedin.com/company/egovernments-foundation" title="LinkedIn" class="btn btn-social-icon btn-linkedin btn-circle-small social-icon" target="_blank"><i class="fa fa-linkedin"></i></a>
								<a href="https://github.com/egovernments" title="Github" class="btn btn-social-icon btn-github btn-circle-small social-icon" target="_blank"><i class="fa fa-github"></i></a>
							</div>
						</div>
						<div class="clearfix"></div>
					</div>
				</div>
			</div>
    	</footer>    	
	</body>
	<script src="resources/jquery-ui/v1.11.2/jquery-ui.min.js"></script>
	<script src="resources/bootstrap/v3.3.1/js/bootstrap.min.js"></script>
	<script src="resources/highstock/v2.0.4/js/highstock.js"></script>
	<script src='resources/js/date.js'></script>
	<script src='resources/js/date.js' type="text/javascript"></script>
	<script src='resources/js/home.js' type="text/javascript"></script>
</html>