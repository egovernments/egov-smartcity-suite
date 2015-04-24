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
<html class="no-js">
	<head>
		<meta charset="utf-8">
		<meta http-equiv="X-UA-Compatible" content="IE=edge">
		
		<meta name="viewport" content="width=device-width, initial-scale=1, maximum-scale=1, user-scalable=no">
		<meta name="description" content="eGov Urban Portal" />
		<meta name="author" content="" />
		
		<title>eGov Urban Portal</title>
		
		<script src="<c:url value='/resources/global/js/jquery/jquery.js'/>"></script>
		<script src="<c:url value='/resources/global/js/pace/pace.min.js'/>"></script>
		<script>
			paceOptions = {
				ajax: false, // Monitors all ajax requests on the page
				document: false, // Checks for the existance of specific elements on the page
				eventLag: false, // Checks the document readyState
				elements: {
					selectors: ['.my-page'] // Checks for event loop lag signaling that javascript is being executed
				}
			};
		</script>
		<link rel="icon" href="<c:url value='/resources/global/images/chennai_fav.ico'/>" sizes="32x32">
		<link rel="stylesheet" href="<c:url value='/resources/global/css/bootstrap/bootstrap.css'/>">
		<link rel="stylesheet" href="<c:url value='/resources/global/css/font-icons/entypo/css/entypo.css'/>">
		<link rel="stylesheet" href="<c:url value='/resources/global/css/font-icons/font-awesome-4.3.0/css/font-awesome.min.css'/>">
		<link rel="stylesheet" href="<c:url value='/resources/global/css/multi-level-menu/jquery.multilevelpushmenu.css'/>"> 
		<link rel="stylesheet" href="<c:url value='/resources/global/css/egov/custom.css'/>">
		<link rel="stylesheet" href="<c:url value='/resources/global/js/jquery/plugins/datatables/responsive/css/datatables.responsive.css'/>">
		<link rel="stylesheet" href="<c:url value='/resources/global/css/pace/pace.css'/>">
		
		<script src="<c:url value='/resources/global/js/bootstrap/bootbox.min.js'/>"></script>
		<script src="<c:url value='/resources/global/js/bootstrap/bootstrap.js'/>"></script>
		<script src="<c:url value='/resources/global/js/multi-level-menu/jquery.multilevelpushmenu.js'/>"></script>
		<script src="<c:url value='/resources/js/app/homepage.js'/>"></script>
		<!--[if lt IE 9]><script src="resources/js/ie8-responsive-file-warning.js"></script><![endif]-->
		
		<!-- HTML5 shim and Respond.js IE8 support of HTML5 elements and media queries -->
		<!--[if lt IE 9]>
			<script src="https://oss.maxcdn.com/libs/html5shiv/3.7.0/html5shiv.js"></script>
			<script src="https://oss.maxcdn.com/libs/respond.js/1.4.2/respond.min.js"></script>
		<![endif]-->
		
		
	</head>
	<body class="page-body">
		
		<div class="page-container horizontal-menu">
			<header class="navbar navbar-fixed-top border-header"><!-- set fixed position by adding class "navbar-fixed-top" -->
				
				<div class="navbar-inner">
					
					<!-- logo -->
					<div class="navbar-brand">
						<a href="javascript:void(0);">
							<img src="<c:url value='/resources/global/images/${cityLogo}'/>" height="60">
						</a>
					</div>
					
					<div class="navbar-brand">
						<h3 class="horizontal-page-title homepage" id="hp-citizen-title">${cityName}</h3>
					</div>
					
					<!-- notifications and other links -->
					<ul class="nav navbar-right pull-right">
						
						<li class="dropdown">
							<a href="javascript:void(0);" class="tooltip-secondary workspace" data-toggle="tooltip" title="Worklist" data-work="worklist">
								<i class="entypo-list"></i>
							</a>
						</li>
						<li class="dropdown">
							<a href="javascript:void(0);" class="tooltip-secondary workspace" data-toggle="tooltip" title="Drafts" data-work="drafts">
								<i class="entypo-pencil"></i>
							</a>
							
						</li>
						<li class="dropdown">
							<a href="javascript:void(0);" class="tooltip-secondary workspace" data-toggle="tooltip" title="Notifications" data-work="notifications">

								<i class="entypo-bell"></i>
							</a>
							
						</li>
						
						<li class="hidden-xs menu-responsive">
							<a href="javascript:void(0);" class="profile-name">
								<i class="entypo-user img-circle"></i>${userName}
							</a>
							<ul>
								<li>
									<a href="home/profile/edit" class="open-popup">
										<i class="fa fa-user"></i>
										<span class="title">Edit Profile</span>
									</a>
								</li>
								<li>
									<a href="javascript:void(0);" onclick="jQuery('.change-password').modal('show', {backdrop: 'static'});">
										<i class="fa fa-key"></i>
										<span class="title">Change Password</span>
									</a>
								</li>
								<li>
									<a href="javascript:void(0);"  onclick="jQuery('.add-feedback').modal('show', {backdrop: 'static'});">
										<i class="fa fa-comment"></i>
										<span class="title signout">Feedback</span>
									</a>
								</li>
								<li>
									<a href="help" data-strwindname="help"  class="open-popup">
										<i class="fa fa-question"></i>
										<span class="title signout">Help</span>
									</a>
								</li>
								<li>
									<a href="/egi/logout.do">
										<i class="fa fa-sign-out"></i>
										<span class="title signout">Sign Out</span>
									</a>
								</li>
							</ul>
						</li>
						
						<li class="dropdown visible-xs hidden-sm">
							<a href="/egi/logout.do" class="tooltip-secondary signout" data-toggle="tooltip" title="Sign Out">
								<i class="entypo-logout"></i>
							</a>
						</li>
						
						<li class="dropdown">
							<a href="javascript:void(0);">
								<img src="<c:url value='/resources/global/images/logo@2x.png'/>" title="Powered by eGovernments" height="25px" style="padding-top:5px">
							</a>
						</li>
					</ul>
				</div>
			</header>
			
			<!--New side bar menu content-->
			<div class="table-row">
				
				<div id="menu" class="homepage">
					
				</div>
				
				<div class="inline-main-content">
					<div class="row main-space" id="worklist">
						<div class="col-xs-12">
							<div class="row">
								<div class="col-md-6 col-xs-6 table-header">
									Worklist
								</div>
								<div class="col-md-6 col-xs-6 add-margin text-right">
									<span class="inline-elem">Search</span>
									<span class="inline-elem"><input type="text" id="inboxsearch" class="form-control input-sm"></span>
								</div>
							</div>
							<table class="table table-bordered datatable" id="official_inbox">
								<thead>
									<tr>
										<th>Date</th>
										<th>Sender</th>
										<th>Task</th>
										<th>Status</th>
										<th>Details</th>
										<th></th>
									</tr>
								</thead>
							</table>
						</div>
					</div>
					<div class="row main-space display-hide" id="drafts">
						<div class="col-xs-12">
							<div class="row">
								<div class="col-md-6 col-xs-6 table-header">
									Drafts
								</div>
								<div class="col-md-6 col-xs-6 add-margin text-right">
									<span class="inline-elem">Search</span>
									<span class="inline-elem"><input type="text" id="draftsearch" class="form-control input-sm"></span>
								</div>
							</div>
							<table class="table table-bordered datatable" id="official_drafts" style="width:100%;">
								<thead>
									<tr>
										<th>Date</th>
										<th>Sender</th>
										<th>Task</th>
										<th>Status</th>
										<th>Details</th>
									</tr>
								</thead>
							</table>
						</div>
					</div>
					
					<div class="row main-space display-hide" id="notifications">
						<div class="col-xs-12">
							<div class="row">
								<div class="col-md-6 col-xs-6 table-header">
									Notifications
								</div>
								<div class="col-md-6 col-xs-6 add-margin text-right">
									
									<span class="inline-elem">Search</span>
									<span class="inline-elem"><input type="text" id="notifysearch" class="form-control input-sm"></span>
								</div>
							</div>
							<table class="table table-bordered datatable" id="official_notify" style="width:100%;">
								<thead>
									<tr>
										<th>Date</th>
										<th>Sender</th>
										<th>Task</th>
										<th>Status</th>
										<th>Details</th>
									</tr>
								</thead>
							</table>
						</div>
					</div>
					
				</div>
				
			</div>
			
		</div>
		
		<!--feedback -->
		<div class="modal fade add-feedback" data-backdrop="static">
			<div class="modal-dialog">
				<div class="modal-content">
					
					<div class="modal-header">
						<button type="button" class="close" data-dismiss="modal" aria-hidden="true">&times;</button>
						<h4 class="modal-title">Feedback</h4>
					</div>
					
					<div class="modal-body">
						<form id="feedback-form" class="form-horizontal form-groups-bordered" >
							<div class="form-group">
								<div class="col-md-12 add-margin">
									<input type="text" class="form-control" id="subject" placeholder="Subject">
								</div>
							</div>
							<div class="form-group">
								<div class="col-md-12 add-margin">
									 <textarea class="form-control" rows="5" id="comment" placeholder="Message"></textarea>
								</div>
							</div>
							<div class="form-group text-right">
								<div class="col-md-12 add-margin">
									 <button type="submit" class="btn btn-primary">Send Feedback</button>
									 <button type="button" class="btn btn-default" data-dismiss="modal">Cancel</button>
								</div>
							</div>
						</form>
					</div>
				</div>
			</div>
		</div>
		<!-- change password -->
		<div class="modal fade change-password" data-backdrop="static">
			<div class="modal-dialog">
				<div class="modal-content">
					
					<div class="modal-header">
						<button type="button" class="close" data-dismiss="modal" aria-hidden="true">&times;</button>
						<h4 class="modal-title">Change Password</h4>
					</div>
					
					<div class="modal-body">
						
						<form id="password-form" class="form-horizontal form-groups-bordered">
							<div class="form-group">
								<div class="col-md-4">
									<label class="control-label">Old Password</label>
								</div>
								<div class="col-md-8 add-margin">
									<input type="password" class="form-control" id="old-pass">
								</div>
							</div>
							<div class="form-group">
								<div class="col-md-4">
									<label class="control-label">New Password</label>
								</div>
								<div class="col-md-8 add-margin">
									<input type="password" class="form-control check-password" id="new-pass">
								</div>
							</div>
							<div class="form-group">
								<div class="col-md-4">
									<label class="control-label">Re-type Password</label>
								</div>
								<div class="col-md-8 add-margin">
									<input type="password" class="form-control check-password" id="retype-pass"><br>
									<div class="password-error error-msg display-hide">Password is incorrect</div>
								</div>
							</div>
							<div class="form-group text-right">
								<div class="col-md-12 add-margin">
									<button type="submit" class="btn btn-primary">Change Password</button>
									<button type="button" class="btn btn-default" data-dismiss="modal">Cancel</button>
								</div>
							</div>
							</form>
						
						
					</div>
				</div>
			</div>
		</div>
		
		<div class="modal fade favourites" data-backdrop="static">
			<div class="modal-dialog">
				<div class="modal-content">
				
					<div class="modal-header">
						<button type="button" class="close" data-dismiss="modal" aria-hidden="true">&times;</button>
						<h4 class="modal-title">Favourites</h4>
					</div>
					
					<div class="modal-body">
						<div class="row">
							<div class="col-md-4">
								<label class="control-label">Favourites Name</label>
							</div>
							<div class="col-md-8">
								
								<div class="form-group">
									<input type="text" class="form-control" id="fav-name" placeholder="">
								</div>	
							</div>
						</div>
					</div>
					
					<div class="modal-footer">
						<button type="button" class="btn btn-primary add-fav">Add Favourites</button>
						<button type="button" class="btn btn-default" data-dismiss="modal">Cancel</button>
					</div>
					
				</div>
			</div>
		</div>
		<div class="modal fade history-inbox">
			<div class="modal-dialog history">
				<div class="modal-content">
					
					<div class="modal-header">
						<button type="button" class="close" data-dismiss="modal" aria-hidden="true">&times;</button>
						<h4 class="modal-title">History</h4>
					</div>
					
					<div class="modal-body">
						<div class="row">
							<div class="col-md-12">
								<table id="historyTable" class="table table-bordered datatable dataTable no-footer">
									<thead> 
										<tr>
											<th>Date</th>
											<th>Sender</th>
											<th>Task</th>
											<th>Status</th>
											<th>Comments</th>
										</tr>
									</thead>
								</table>
							</div>
						</div>
					</div>
					
					<div class="modal-footer">
						<button type="button" class="btn btn-default" data-dismiss="modal">Cancel</button>
					</div>
					
				</div>
			</div>
		</div>
		<script>
		var menuItems = ${menu};
		</script>
		<script src="<c:url value='/resources/global/js/bootstrap/bootstrap.js'/>"></script>
		<script src="<c:url value='/resources/js/app/custom-menu.js'/>"></script>
		<script src="<c:url value='/resources/global/js/jquery/plugins/datatables/jquery.dataTables.min.js'/>"></script>
		<script src="<c:url value='/resources/global/js/jquery/plugins/datatables/dataTables.bootstrap.js'/>"></script>
		<script src="<c:url value='/resources/global/js/jquery/plugins/datatables/responsive/js/datatables.responsive.js'/>"></script>
		<script src="<c:url value='/resources/global/js/egov/custom.js'/>"></script>
		<script src="<c:url value='/resources/js/app/homepageofficial.js'/>"></script>
	</body>
</html>																						
