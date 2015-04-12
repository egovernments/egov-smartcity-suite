<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@taglib  uri="http://www.joda.org/joda/time/tags" prefix="joda"%>
<!DOCTYPE html>
<html lang="en">
	<head>
		<meta charset="utf-8">
		<meta http-equiv="X-UA-Compatible" content="IE=edge">
		
		<meta name="viewport" content="width=device-width, initial-scale=1, maximum-scale=1, user-scalable=no">
		<meta name="description" content="Neon Admin Panel" />
		<meta name="author" content="" />
		
		<title>eGov Urban Portal</title>
		
		<link rel="stylesheet" href="/egi/resources/global/css/bootstrap/bootstrap.css">
		<link rel="stylesheet" href="/egi/resources/global/css/font-icons/entypo/css/entypo.css">
		<link rel="stylesheet" href="/egi/resources/global/css/font-icons/font-awesome-4.3.0/css/font-awesome.min.css">
		<link rel="stylesheet" href="/egi/resources/global/css/egov/custom.css">
		<link rel="stylesheet" href="/egi/resources/global/css/egov/header-custom.css">
		<script src="/egi/resources/global/js/jquery/jquery.js"></script>
		<script src="/egi/resources/global/js/bootstrap/bootstrap.js"></script>
		<script src="/egi/resources/js/app/homepage.js"></script>
		<script src="/egi/resources/js/app/homepagecitizen.js"></script>
		<script src="/egi/resources/global/js/egov/custom.js"></script>	
		
		<!--[if lt IE 9]><script src="resources/js/ie8-responsive-file-warning.js"></script><![endif]-->
		
		<!-- HTML5 shim and Respond.js IE8 support of HTML5 elements and media queries -->
		<!--[if lt IE 9]>
			<script src="https://oss.maxcdn.com/libs/html5shiv/3.7.0/html5shiv.js"></script>
			<script src="https://oss.maxcdn.com/libs/respond.js/1.4.2/respond.min.js"></script>
		<![endif]-->
		
		
	</head>
	<body class="page-body">
		
		<div class="page-container">
			
			<header><!-- set fixed position by adding class "navbar-fixed-top" -->
				
				<nav class="navbar navbar-default navbar-custom navbar-fixed-top">
					<div class="container-fluid">
						<div class="navbar-header col-lg-4 col-md-4 col-sm-2 col-xs-3">
							<a class="navbar-brand" href="javascript:void(0);">
								<img src="/egi/resources/global/images/chennai_logo.jpg" height="60">
								<div>
									<span class="title2 hidden-sm hidden-xs">Chennai Municipal Corporation</span>
								</div>
							</a>
						</div>
						<div class="nav-menu col-lg-4 col-md-6 col-sm-7 col-xs-12">
							<ul class="hr-menu text-center">
								<li class="active"><a class="menu-item " href="javascript:void(0);" data-show-screen="#inbox-template"> <span class="title">Inbox</span><span class="badge custom-badge">${unreadMessageCount}</span></a></li>
								<span class="separator">|</span>
								<li><a class="menu-item" href="javascript:void(0);" data-show-screen="#myaccount">My Account</a></li>
								<span class="separator">|</span>
								<li><a class="menu-item" href="javascript:void(0);" data-show-screen="#newreq">New Request</a></li>
							</ul>
						</div><!--/.nav-collapse -->
						
						<div class="col-lg-4 col-md-2 col-sm-3 col-xs-9 nav-right-menu home">
							<ul class="hr-menu text-right">
								
								<li class="ico-menu">
									<a href="javascript:void(0);">
										<i class="fa fa-question-circle"></i>
									</a>
								</li>
								
								<li class="ico-menu">
									<a class="dropdown-toggle" href="javascript:void(0);" data-toggle="dropdown">
										<i class="fa fa-user"></i>
									</a>
									<ul class="right-arrow dropdown-menu" role="menu">
										<li><a href="#"><i class="fa fa-cog"></i> Change Password</a></li>
										<li><a href="/egi/logout.do"><i class="fa fa-sign-out"></i> Sign out</a></li>
									</ul>
								</li>
								
								<li class="ico-menu">
									<a href="http://www.egovernments.org" target="_blank">
										<img src="/egi/resources/global/images/logo@2x.png" title="Powered by eGovernments" height="20px">
									</a>
								</li>
								
							</ul>
						</div>
						
					</div>
				</nav>
				
			</header>
			
			<div class="main-content login-page">
				
				<div class="main-before-footer">
					<div class="citizen-screens" id="inbox-template">
						
						<div class="row padding-tb"><!-- padding-tb -->
							<div class="search-box col-md-9 col-sm-8">
								<i class="fa fa-search"></i>
								<input type="text" class="form-control" placeholder="Search">
							</div>
							<div class="col-md-3 col-sm-4 text-center xs-margin-top-10">
								<div class="btn-group btn-input clearfix" id="sortby_drop">
									<button type="button" class="btn btn-default dropdown-toggle form-control" data-toggle="dropdown">
										<span data-bind="label">Sort by : <b><i class="fa fa-clock-o"></i> Recent Messages</b></span> <span class="caret"></span>
									</button>
									<ul class="dropdown-menu" role="menu">
										<li><a href="#"><i class="fa fa-clock-o"></i> Recent Messages</a></li>
										<li><a href="#"><i class="user-msg fa fa-user"></i> User Messages</a></li>
										<li><a href="#"><i class="fa fa-database"></i> System Notifications</a></li>
									</ul>
								</div>
							</div>
						</div>
						
						<div class="row container-msgs">
							
							<section class="col-lg-12">
								<c:forEach var="inboxMsg" items="${inboxMessages}">
								<div class="msg" data-toggle="collapse" data-target="#msgcontent0" aria-expanded="true">
									<header>
										
										<div class="row">
											<i class="user-msg fa fa-user col-sm-1 col-xs-2 read-msg"></i><h3 class="col-sm-11 col-xs-10">
											${inboxMsg.headerMessage}<span class="msg-status">${inboxMsg.status}</span> </h3> 
										</div>
										
										
										<div class="msg-info">
											<a href="javascript:void(0);">${inboxMsg.identifier}</a> <span class="dot">&bull;</span> 
											<joda:format value="${inboxMsg.messageDate}" var="messageDate" pattern="dd-MM-yyyy hh:mm:ss"/>
						<span class="msg-date">${messageDate}</span>
										</div>
									</header>
									
									<div id="msgcontent0" class="msg-content collapse in">
										<p> 
											${inboxMsg.detailedMessage} 
										</p>
									</div>
								</div>
								</c:forEach> 
							</section>
					</div>
					
				</div>
				
				<div class="citizen-screens tabs tabs-style-topline myacc display-hide" id="myaccount">
					<nav>
						<ul>
							<li class="tab-current-myacc" data-section="myaccount" data-myaccount-section="#section-myaccount-1">
								<a href="javascript:void(0);">
									<div class="text-center"><i class="fa fa-book"></i></div>
									<span class="hidden-sm hidden-xs">My Grievances</span>
								</a>
							</li>
						</ul>
					</nav>
					<div class="content-wrap">
						<section id="section-myaccount-1"  class="content-current-myacc">
							<c:forEach var="myAccountMsg" items="${myAccountMessages}">
								<div class="visible-xs visible-sm">My Grievance</div>
								<div class="msg" data-toggle="collapse" data-target="#myaccount1content1" aria-expanded="true">
									<header>
										<div class="row">
											<i class="fa fa-book col-sm-1 col-xs-2 unread-msg"></i><h3 class="col-sm-11 col-xs-10">${myAccountMsg.headerMessage}</h3>
										</div>
											<div class="myaccount-actions">
											<a href="${myAccountMsg.link}" target="_blank"><i class="fa fa-desktop col-sm-1 col-xs-2 unread-msg" data-toggle="tooltip" title="View Grievance"></i></a>
										</div>
										<div class="msg-info">
											<a href="javascript:void(0);">${myAccountMsg.identifier}</a> <span class="dot">&bull;</span> <span class="msg-date"><joda:format value="${myAccountMsg.messageDate}" var="messageDate" pattern="dd-MM-yyyy hh:mm:ss"/>
						<span class="msg-date">${messageDate}</span>
										</div>
									</header>
								</div> 
							</c:forEach>
						</section>
						
					</div>
				</div>
				
				<div class="citizen-screens tabs tabs-style-topline newrequest display-hide" id="newreq">
					<nav>
						<ul>
							<li class="tab-current-newreq" data-section="newrequest" data-newreq-section="#section-newrequest-1">
								<a href="javascript:void(0);">
									<div class="text-center"><i class="fa fa-book"></i></div>
									<span class="hidden-sm hidden-xs">Grievance Redressal</span>
								</a>
							</li>
							<!-- li data-section="newrequest" data-newreq-section="#section-newrequest-2">
								<a href="javascript:void(0);">
									<div class="text-center"><i class="fa fa-rupee"></i></div>
									<span class="hidden-sm hidden-xs">Property Tax</span>
								</a>
							</li>
							<li data-section="newrequest" data-newreq-section="#section-newrequest-3">
								<a href="javascript:void(0);">
									<div class="text-center"><i class="fa fa-building-o"></i></div>
									<span class="hidden-sm hidden-xs">Building Plan Approval</span>
								</a>
							</li>
							<li data-section="newrequest" data-newreq-section="#section-newrequest-4">
								<a href="javascript:void(0);">
									<div class="text-center"><i class="fa fa-medkit"></i></div>
									<span class="hidden-sm hidden-xs">Birth & Death</span>
								</a>
							</li>
							<li data-section="newrequest" data-newreq-section="#section-newrequest-5">
								<a href="javascript:void(0);">
									<div class="text-center"><i class="fa fa-text-width"></i></div>
									<span class="hidden-sm hidden-xs">Trade Licence</span>
								</a>
							</li>
							<li data-section="newrequest" data-newreq-section="#section-newrequest-6">
								<a href="javascript:void(0);">
									<div class="text-center"><i class="fa fa-rupee"></i></div>
									<span class="hidden-sm hidden-xs">Professional Tax</span>
								</a>
							</li>
							<li data-section="newrequest" data-newreq-section="#section-newrequest-7">
								<a href="javascript:void(0);">
									<div class="text-center"><i class="fa fa-rupee"></i></div>
									<span class="hidden-sm hidden-xs">Company Tax</span>
								</a>
							</li>
							<li data-section="newrequest" data-newreq-section="#section-newrequest-8">
								<a href="javascript:void(0);">
									<div class="text-center"><i class="fa fa-money"></i></div>
									<span class="hidden-sm hidden-xs">Shops</span>
								</a>
							</li>
							<li data-section="newrequest" data-newreq-section="#section-newrequest-9">
								<a href="javascript:void(0);">
									<div class="text-center"><i class="fa fa-adn"></i></div>
									<span class="hidden-sm hidden-xs">Advertisement</span>
								</a>
							</li>
							<li data-section="newrequest" data-newreq-section="#section-newrequest-10">
								<a href="javascript:void(0);">
									<div class="text-center"><i class="fa fa-hand-o-right"></i></div>
									<span class="hidden-sm hidden-xs">Others</span>
								</a>
							</li-->
						</ul>
					</nav>
					<div class="content-wrap">
						<section id="section-newrequest-1"  class="content-current-newreq">
							<div class="visible-xs visible-sm">Grievance Redressal</div>
							<div class="msg">
								<header>
									<div class="row">
										<i class="fa fa-edit col-sm-1 col-xs-2 unread-msg"></i><a href="/pgr/complaint/citizen/show-reg-form" class="open-popup col-sm-11 col-xs-10">Register Grievance</a>
									</div>
								</header>
							</div> 
						</section>
						<section id="section-newrequest-2">
							<div class="visible-xs visible-sm">Property Tax</div>
							<div class="msg">
								<header>
									<div class="row">
										<i class="fa fa-edit col-sm-1 col-xs-2 unread-msg"></i><a href="javascript:void(0)" class="col-sm-11 col-xs-10">File New Assessment</a>
									</div>
								</header>
							</div> 
							<div class="msg">
								<header>
									<div class="row">
										<i class="fa fa-rupee col-sm-1 col-xs-2 unread-msg"></i><a href="javascript:void(0)" class="col-sm-11 col-xs-10">Pay Property Tax</a>
									</div>
								</header>
							</div>
							<div class="msg">
								<header>
									<div class="row">
										<i class="fa fa-external-link col-sm-1 col-xs-2 unread-msg"></i><a href="javascript:void(0)" class="col-sm-11 col-xs-10">Link Property to My Account</a>
									</div>
								</header>
							</div>
							<div class="msg">
								<header>
									<div class="row">
										<i class="fa fa-search col-sm-1 col-xs-2 unread-msg"></i><a href="javascript:void(0)" class="col-sm-11 col-xs-10">Search Property</a>
									</div>
								</header>
							</div>
						</section>
						<section id="section-newrequest-3">
							<div class="visible-xs visible-sm">Building Plan Approval</div>
							<div class="msg">
								<header>
									<div class="row">
										<i class="fa fa-edit col-sm-1 col-xs-2 unread-msg"></i><a href="javascript:void(0)" class="col-sm-11 col-xs-10">Application for Additional Construction</a>
									</div>
								</header>
							</div> 
							<div class="msg">
								<header>
									<div class="row">
										<i class="fa fa-edit col-sm-1 col-xs-2 unread-msg"></i><a href="javascript:void(0)" class="col-sm-11 col-xs-10">Application for Demolition and Reconstruction</a>
									</div>
								</header>
							</div>
							<div class="msg">
								<header>
									<div class="row">
										<i class="fa fa-edit col-sm-1 col-xs-2 unread-msg"></i><a href="javascript:void(0)" class="col-sm-11 col-xs-10">Application for Demolition Only</a>
									</div>
								</header>
							</div>
							<div class="msg">
								<header>
									<div class="row">
										<i class="fa fa-edit col-sm-1 col-xs-2 unread-msg"></i><a href="javascript:void(0)" class="col-sm-11 col-xs-10">Application for New Building Permit</a>
									</div>
								</header>
							</div>
						</section>
						<section id="section-newrequest-4">
							<div class="visible-xs visible-sm">Birth & Death</div>
							<div class="msg">
								<header>
									<div class="row">
										<i class="fa fa-edit col-sm-1 col-xs-2 unread-msg"></i><a href="javascript:void(0)" class="col-sm-11 col-xs-10">Application for Birth/Death Certificate</a>
									</div>
								</header>
							</div> 
							<div class="msg">
								<header>
									<div class="row">
										<i class="fa fa-search col-sm-1 col-xs-2 unread-msg"></i><a href="javascript:void(0)" class="col-sm-11 col-xs-10">Search for Birth/Death records</a>
									</div>
								</header>
							</div>
							<div class="msg">
								<header>
									<div class="row">
										<i class="fa fa-edit col-sm-1 col-xs-2 unread-msg"></i><a href="javascript:void(0)" class="col-sm-11 col-xs-10">Apply for Name Inclusion</a>
									</div>
								</header>
							</div> 
							<div class="msg">
								<header>
									<div class="row">
										<i class="fa fa-edit col-sm-1 col-xs-2 unread-msg"></i><a href="javascript:void(0)" class="col-sm-11 col-xs-10">Record Correction</a>
									</div>
								</header>
							</div>
						</section>
						<section id="section-newrequest-5">
							<div class="visible-xs visible-sm">Trade Licence</div>
							<div class="msg">
								<header>
									<div class="row">
										<i class="fa fa-edit col-sm-1 col-xs-2 unread-msg"></i><a href="javascript:void(0)" class="col-sm-11 col-xs-10">Apply For Trade Licence</a>
									</div>
								</header>
							</div> 
							<div class="msg">
								<header>
									<div class="row">
										<i class="fa fa-rupee col-sm-1 col-xs-2 unread-msg"></i><a href="javascript:void(0)" class="col-sm-11 col-xs-10">Renew Licence</a>
									</div>
								</header>
							</div>
							<div class="msg">
								<header>
									<div class="row">
										<i class="fa fa-external-link col-sm-1 col-xs-2 unread-msg"></i><a href="javascript:void(0)" class="col-sm-11 col-xs-10">Link Licence to my Account</a>
									</div>
								</header>
							</div> 
						</section>
						<section id="section-newrequest-6">
							<div class="visible-xs visible-sm">Professional Tax</div>
							<div class="msg">
								<header>
									<div class="row">
										<i class="fa fa-edit col-sm-1 col-xs-2 unread-msg"></i><a href="javascript:void(0)" class="col-sm-11 col-xs-10">File New Assessment</a>
									</div>
								</header>
							</div> 
							<div class="msg">
								<header>
									<div class="row">
										<i class="fa fa-rupee col-sm-1 col-xs-2 unread-msg"></i><a href="javascript:void(0)" class="col-sm-11 col-xs-10">Pay Professional Tax</a>
									</div>
								</header>
							</div>
							<div class="msg">
								<header>
									<div class="row">
										<i class="fa fa-external-link col-sm-1 col-xs-2 unread-msg"></i><a href="javascript:void(0)" class="col-sm-11 col-xs-10">Link Profession to my Account</a>
									</div>
								</header>
							</div> 
						</section>
						<section id="section-newrequest-7">
							<div class="visible-xs visible-sm">Company Tax</div>
							<div class="msg">
								<header>
									<div class="row">
										<i class="fa fa-edit col-sm-1 col-xs-2 unread-msg"></i><a href="javascript:void(0)" class="col-sm-11 col-xs-10">File New Assessment</a>
									</div>
								</header>
							</div> 
							<div class="msg">
								<header>
									<div class="row">
										<i class="fa fa-rupee col-sm-1 col-xs-2 unread-msg"></i><a href="javascript:void(0)" class="col-sm-11 col-xs-10">Pay Company Tax</a>
									</div>
								</header>
							</div>
							<div class="msg">
								<header>
									<div class="row">
										<i class="fa fa-external-link col-sm-1 col-xs-2 unread-msg"></i><a href="javascript:void(0)" class="col-sm-11 col-xs-10">Link Company to my Account</a>
									</div>
								</header>
							</div>
						</section>
						<section id="section-newrequest-8">
							<div class="visible-xs visible-sm">Shops</div>
							<div class="msg">
								<header>
									<div class="row">
										<i class="fa fa-rupee col-sm-1 col-xs-2 unread-msg"></i><a href="javascript:void(0)" class="col-sm-11 col-xs-10">Pay Fees</a>
									</div>
								</header>
							</div>
							<div class="msg">
								<header>
									<div class="row">
										<i class="fa fa-external-link col-sm-1 col-xs-2 unread-msg"></i><a href="javascript:void(0)" class="col-sm-11 col-xs-10">Link Shop to my Account</a>
									</div>
								</header>
							</div> 
						</section>
						<section id="section-newrequest-9">
							<div class="visible-xs visible-sm">Advertisement</div>
							<div class="msg">
								<header>
									<div class="row">
										<i class="fa fa-rupee col-sm-1 col-xs-2 unread-msg"></i><a href="javascript:void(0)" class="col-sm-11 col-xs-10">Pay Fees</a>
									</div>
								</header>
							</div>
							<div class="msg">
								<header>
									<div class="row">
										<i class="fa fa-external-link col-sm-1 col-xs-2 unread-msg"></i><a href="javascript:void(0)" class="col-sm-11 col-xs-10">Link Hoarding to my Account</a>
									</div>
								</header>
							</div> 
						</section>
						<section id="section-newrequest-10">
							<div class="visible-xs visible-sm">Others</div>
							<div class="msg">
								<header>
									<div class="row">
										<i class="fa fa-rupee col-sm-1 col-xs-2 unread-msg"></i><a href="javascript:void(0)" class="col-sm-11 col-xs-10">Online Payment For Challans</a>
									</div>
								</header>
							</div>
							<div class="msg">
								<header>
									<div class="row">
										<i class="fa fa-edit col-sm-1 col-xs-2 unread-msg"></i><a href="javascript:void(0)" class="col-sm-11 col-xs-10">Apply For Road Cut</a>
									</div>
								</header>
							</div> 
						</section>
					</div>
				</div>
				
			</div>
			

			
		</div>
		
	</div>
	
	
</body>

</html>																																																							
