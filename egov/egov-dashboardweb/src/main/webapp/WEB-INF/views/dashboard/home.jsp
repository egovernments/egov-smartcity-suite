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
       
		<!-- link rel="stylesheet" href="resources/bootstrap/v3.3.1/css/bootstrap-theme.min.css">
		<link rel="stylesheet" href="resources/fontawesome/v4.2.0/css/font-awesome.min.css"-->
		<link rel="stylesheet" href="resources/css/grayscale.css">
		<!-- link rel="stylesheet" href="resources/css/plugins/social-buttons.css" -->
		<link rel="stylesheet" href="resources/css/home.css">
		
		<script>
			$(window).load(function() {
				$("#cover").delay(1000).slideUp(300);
				$("#preloader-container").delay(1000).slideUp(300);
			});
			/*var pgrRegdTrend = ${model.registeredTrend};
			var pgrCompletionTrend = ${model.completedTrend};
			var revenueTrend = ${model.revenueTrend};
			var paymentTrend = ${model.paymentTrend};		
			var revenueTrendForWeek = ${model.revenueTrendForWeek};
			var paymentTrendForWeek = ${model.paymentTrendForWeek};*/

			var pgrRegdTrend = [0,0,0,0,0,0,0];
			var pgrCompletionTrend = [0,0,0,0,0,0,0];
			var revenueTrend = {"actual":[],"targeted":[]};
			var paymentTrend = [{"amt":".003","name":"303202-Anti Mosquito Operation","y":0.05},{"amt":"5","name":"202500-Storm Water Drains","y":99.95}];		
			var revenueTrendForWeek = [{"name":"Mon-13","y":0.0,"tooltipFormat":"Monday, 13 Jul 2015"},{"name":"Tue-14","y":0.0,"tooltipFormat":"Tuesday, 14 Jul 2015"},{"name":"Wed-15","y":0.0,"tooltipFormat":"Wednesday, 15 Jul 2015"},{"name":"Thu-16","y":0.0,"tooltipFormat":"Thursday, 16 Jul 2015"},{"name":"Fri-17","y":0.0,"tooltipFormat":"Friday, 17 Jul 2015"},{"name":"Sat-18","y":0.0,"tooltipFormat":"Saturday, 18 Jul 2015"},{"name":"Sun-19","y":0.0,"tooltipFormat":"Sunday, 19 Jul 2015"}];
			var paymentTrendForWeek = [{"name":"Tue-14","y":0.0,"tooltipFormat":"Tuesday, 14 Jul 2015"},{"name":"Wed-15","y":0.0,"tooltipFormat":"Wednesday, 15 Jul 2015"},{"name":"Thu-16","y":0.0,"tooltipFormat":"Thursday, 16 Jul 2015"},{"name":"Fri-17","y":0.0,"tooltipFormat":"Friday, 17 Jul 2015"},{"name":"Sat-18","y":0.0,"tooltipFormat":"Saturday, 18 Jul 2015"},{"name":"Sun-19","y":0.0,"tooltipFormat":"Sunday, 19 Jul 2015"},{"name":"Mon-20","y":0.0,"tooltipFormat":"Monday, 20 Jul 2015"}];
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
    	
    	<header class="intro">
    		<div class="intro-body">
          		<div class="container-fluid">
          			 <div class="row">
	            		<div class="col-xs-12">
	            			<a href="pgrdashboard/home.do?isdefault=false" class="xa">
	                  		  <div class="panel-footer-new">
	                          	<font class="text-center" style="font-size: 18px;">COMPLAINTS</font>
	                          </div>
	                  		</a>
	              		</div>
					</div>
					<div class="row">
						<div class="col-lg-12 col-md-12 col-sm-12" style="min-height:10px">
						  
						</div>
					</div>
					<div class="row">
	              		<div class="col-lg-4 col-md-4 col-sm-4">
	              			<a href="/pgr/dashboard/home" class="xa">
	              				<div id="newtimeLineCompTrend" class="chart-style" style="min-height: 300px;"></div>
	                  		</a>
	              		</div>
	              		<div class="col-lg-4 col-md-4 col-sm-4">
	            			<a href="/pgr/dashboard/home" class="xa">
	            				<div id="slaGraph" class="chart-style" style="min-height: 300px;" ></div>
	                  		</a>
	              		</div>
	              		<div class="col-lg-4 col-md-4 col-sm-4">
	              			<a href="/pgr/dashboard/home" class="xa">
	                  			<div id="overviewGraph" class="chart-style" style="min-height: 300px;"></div>
	                  		</a>
	              		</div>
					</div>
					<div class="row">
						<div class="col-lg-12 col-md-12 col-sm-12" style="min-height:10px">
						
						</div>
					</div>
          		</div>
        	</div>
        </header>
    	  	
	</body>
	<!--script src="resources/jquery-ui/v1.11.2/jquery-ui.min.js"></script>
	< script src="resources/bootstrap/v3.3.1/js/bootstrap.min.js"></script-->
	<script src="resources/js/highchart/highstock.js"></script>
	<script src='resources/js/date.js' type="text/javascript"></script>
	<script src='resources/js/home.js' type="text/javascript"></script>
</html>