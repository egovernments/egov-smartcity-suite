<%--
  ~ eGov suite of products aim to improve the internal efficiency,transparency,
  ~ accountability and the service delivery of the government  organizations.
  ~
  ~  Copyright (C) 2016  eGovernments Foundation
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

<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib uri="http://www.springframework.org/tags" prefix="spring" %>
<%@taglib prefix="joda" uri="http://www.joda.org/joda/time/tags" %>
<%@ taglib uri="/WEB-INF/taglib/cdn.tld" prefix="cdn" %>
<!DOCTYPE html>
<html class="no-js" oncontextmenu="return false;">
	<head>
		<meta charset="utf-8">
		<meta http-equiv="X-UA-Compatible" content="IE=edge">
		
		<meta name="viewport" content="width=device-width, initial-scale=1, maximum-scale=1, user-scalable=no">
		<meta name="description" content="eGov Urban Portal" />
		<meta name="author" content="" />
		<spring:eval expression="@environment.getProperty('user.pwd.strength')" var="pwdstrengthmsg"/>
		<spring:message code="usr.pwd.strength.msg.${pwdstrengthmsg}" var="pwdmsg" htmlEscape="true"/>
		<title>eGov Urban Portal</title>
		
		<link rel="icon" href="<cdn:url value='/resources/global/images/favicon.png'/>" sizes="32x32">
		<link rel="stylesheet" href="<cdn:url value='/resources/global/css/bootstrap/bootstrap.css'/>">
		<link rel="stylesheet" href="<cdn:url value='/resources/global/css/font-icons/font-awesome/css/font-awesome.min.css'/>">
		<link rel="stylesheet" href="<cdn:url value='/resources/global/css/multi-level-menu/jquery.multilevelpushmenu.css'/>">
		<link rel="stylesheet" href="<cdn:url value='/resources/global/css/egov/custom.css?rnd=${app_release_no}'/>">
		<link rel="stylesheet" href="<cdn:url value='/resources/global/css/jquery/plugins/datatables/jquery.dataTables.min.css' context='/egi'/>"/>
		<link rel="stylesheet" href="<cdn:url value='/resources/global/css/jquery/plugins/datatables/dataTables.bootstrap.min.css' context='/egi'/>">
		<link rel="stylesheet" href="<cdn:url value='/resources/global/js/jquery/plugins/datatables/responsive/css/datatables.responsive.css'/>">
		
		<script src="<cdn:url value='/resources/global/js/jquery/jquery.js'/>"></script>
		<script src="<cdn:url value='/resources/global/js/bootstrap/bootbox.min.js'/>"></script>
		<script src="<cdn:url value='/resources/global/js/bootstrap/bootstrap.js'/>"></script>
		<script src="<cdn:url value='/resources/global/js/multi-level-menu/jquery.multilevelpushmenu.js'/>"></script>
		
		
		<!-- HTML5 shim and Respond.js IE8 support of HTML5 elements and media queries -->
		<!--[if lt IE 9]>
		<script src="<cdn:url value='/resources/global/js/ie8/html5shiv.min.js'/>"></script>
		<script src="<cdn:url value='/resources/global/js/ie8/respond.min.js'/>"></script>
		<![endif]-->
		<style>
		body{padding:0;}
		footer {
			background-color:#f3f4f5;bottom: 0;clear:both;position: fixed;width:100%;z-index: 999;
		}
		
		footer a{
			color: rgb(116, 101, 101);text-decoration: none;
		}
		
		footer span.copyright {
			float: left;font-size: 11px;padding: 7px;
		}
		
		footer span.version {
			float: right;font-size: 11px;padding: 7px;
		}
		    
		footer span.copyright a {
			color: rgb(116, 101, 101);
		}
		
		footer #legal {
			clear: left;display: block;
		}
		
		.constrain {
				padding-left: 10px;padding-right: 10px;margin-bottom:0px;
		}
		</style>
	</head>
	<body class="page-body">
		<div id="loadingMask"></div>
		<div id="loading">
		    <div class="loading-indicator"><i class="fa fa-spinner fa-pulse fa-2x"></i></div>
		</div>
		<div class="page-container horizontal-menu">
			<div class="search">
				<input type="text" id="searchtree" autofocus="" placeholder="Quick Find"><span class="fa fa-search searchicon tooltip-secondary" data-toggle="tooltip" data-original-title="Search menu item"></span>
				<span class="applyanimation"></span>
			</div>
			<div class="search_list">
				<div class="list"><ul class="ullist"></ul></div>
			</div>
			<header class="navbar navbar-fixed-top border-header"><!-- set fixed position by adding class "navbar-fixed-top" -->
				
				<div class="navbar-inner">
					
					<!-- logo -->
					<div class="navbar-brand">
						<a href="javascript:void(0);">
							<img src="<c:url value='${sessionScope.logopath == null ? sessionScope.citylogo : sessionScope.logopath}'/>" height="60" class="homepage_logo">
						</a>
					</div>
					
					<div class="navbar-brand">
						<h3 class="horizontal-page-title homepage hidden-xs hidden-sm" id="hp-citizen-title">${sessionScope.citymunicipalityname}</h3>
					</div>
					
					<!-- notifications and other links -->
					<ul class="nav navbar-right pull-right">
						
						<li class="dropdown">
							<a href="javascript:void(0);" class="tooltip-secondary workspace active" data-toggle="tooltip" title="Worklist" data-work="worklist">
								<i class="fa fa-list fa-fw"></i>
							</a>
						</li>
						<li class="dropdown">
							<a href="javascript:void(0);" class="tooltip-secondary workspace" data-toggle="tooltip" title="Drafts" data-work="drafts">
								<i class="fa fa-pencil fa-fw"></i>
							</a>
							
						</li>
						<li class="dropdown">
							<a href="javascript:void(0);" class="tooltip-secondary workspace" data-toggle="tooltip" title="Notifications" data-work="notifications">

								<i class="fa fa-bell fa-fw"></i>
							</a>
							
						</li>
						
						<li class="hidden-xs menu-responsive">
							<a href="javascript:void(0);" class="profile-name">
								<i class="fa fa-user img-circle"></i> ${userName}
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
										<span class="title">Feedback</span>
									</a>
								</li>
								<c:if test="${not empty issue_report_url}">
								<li>
									<a href="${issue_report_url}" data-strwindname="r&i"  class="open-popup">
										<i class="fa fa-bug"></i>
										<span class="title">Report an Issue</span>
									</a>
								</li>
								</c:if>
								<li>
									<a href="help" data-strwindname="help"  class="open-popup">
										<i class="fa fa-question"></i>
										<span class="title">Help</span>
									</a>
								</li>
								<li>
									<a href="/egi/logout" class="signout">
										<i class="fa fa-sign-out"></i>
										<span class="title">Sign Out</span>
									</a>
								</li>
							</ul>
						</li>
						
						<li class="dropdown visible-xs hidden-sm">
							<a href="/egi/logout" class="tooltip-secondary signout" data-toggle="tooltip" title="Sign Out">
								<i class="fa fa-sign-out"></i>
							</a>
						</li>
						
						<li class="dropdown">
							<a href="http://www.egovernments.org" target="_blank">
								<img src="<cdn:url value='/resources/global/images/logo@2x.png'/>" title="Powered by eGovernments" height="25px" style="padding-top:5px">
							</a>
						</li>
					</ul>
				</div>
			</header>
			
			<!--New side bar menu content-->
			<div class="home-container">
				
				<div id="menu" class="homepage">
					
				</div>
				
				<div class="inline-main-content">
					<div class="row main-space" id="worklist">
						<div class="col-xs-12">
							<div class="row">
								<div class="col-md-9 col-xs-9 table-header">
									Worklist
								</div>
								<div class="col-md-3 col-xs-3 add-margin text-right">
									<div class="input-group">
									  <input type="text" class="form-control input-sm search-table" id="inboxsearch">
									  <span class="input-group-addon"><i class="fa fa-search" aria-hidden="true"></i></span>
									</div>
								</div>
							</div>
							<div class="row">
								<div class="col-md-12" id="natureofwork" >
								</div>
							</div>
							<table class="table table-bordered datatable" id="official_inbox">
								<thead>
									<tr>
										<th>Date</th>
										<th>Sender</th>
										<th>Nature of Work</th>
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
								<div class="col-md-9 col-xs-9 table-header">
									Drafts
								</div>
								<div class="col-md-3 col-xs-3 add-margin text-right">
									<div class="input-group">
									  <input type="text" class="form-control input-sm search-table" id="draftsearch">
									  <span class="input-group-addon"><i class="fa fa-search" aria-hidden="true"></i></span>
									</div>
								</div>
							</div>
							<table class="table table-bordered datatable" id="official_drafts" style="width:100%;">
								<thead>
									<tr>
										<th>Date</th>
										<th>Sender</th>
										<th>Nature of Work</th>
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
								<div class="col-md-9 col-xs-9 table-header">
									Notifications
								</div>
								<div class="col-md-3 col-xs-3 add-margin text-right">
									<div class="input-group">
									  <input type="text" class="form-control input-sm search-table" id="notifysearch">
									  <span class="input-group-addon"><i class="fa fa-search" aria-hidden="true"></i></span>
									</div>
								</div>
							</div>
							<table class="table table-bordered datatable" id="official_notify" style="width:100%;">
								<thead>
									<tr>
										<th>Date</th>
										<th>Sender</th>
										<th>Nature of Work</th>
										<th>Status</th>
										<th>Details</th>
									</tr>
								</thead>
							</table>
						</div>
					</div>
					
				</div>
				
			</div>
			<footer class="clearfix simple">
				<div class="constrain">
					<div id="legal">
						<c:set var="now" value="<%=new org.joda.time.DateTime()%>" />
						<span class="copyright">Copyright <span><i class="fa fa-copyright"></i></span> <joda:format value="${now}" pattern="yyyy" /> <a href="http://www.egovernments.org" target="_blank"> eGovernments Foundation.<sup>&reg;</sup></a></span>
						<span class="version">eGov ERP - ${app_version}_${app_buildno}<c:if test="${not empty app_core_build_no}"> @ Core - ${app_core_build_no}</c:if></span>
					</div>
				</div>
			</footer>	
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
									 Mail will be sent to ${sessionScope.corpContactEmail}
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
		<div class="modal fade change-password" data-backdrop="static" data-keyboard="false">
			<div class="modal-dialog">
				<div class="modal-content">
					
					<div class="modal-header">
						<button type="button" class="close pass-cancel" data-dismiss="modal" aria-hidden="true">&times;</button>
						<h4 class="modal-title">Change Password</h4>
					</div>
					
					<div class="modal-body">
						<c:if test="${dflt_pwd_reset_req}">
							<div class="alert alert-warning" role="alert" id="pass-alert">
							<i class="fa fa-exclamation-triangle"></i> Security alert...! You are using default password, please reset your password.
							</div>
						</c:if>
						<form id="password-form" class="form-horizontal form-groups-bordered">
							<div class="form-group">
								<div class="col-md-4">
									<label class="control-label">Old Password</label>
								</div>
								<div class="col-md-8 add-margin">
									<input style="display:none">
									<input type="password" class="form-control" id="old-pass" required="required">
								</div>
							</div>
							<div class="form-group" id="wrap">
								<div class="col-md-4">
									<label class="control-label">New Password</label>
								</div>
								<div class="col-md-8 add-margin">
									<input type="password" class="form-control check-password" id="new-pass" maxlength="32" data-container="#wrap" data-toggle="popover" data-content="${pwdmsg}">
								</div>
							</div>
							<div class="form-group">
								<div class="col-md-4">
									<label class="control-label">Re-type Password</label>
								</div>
								<div class="col-md-8 add-margin">
									<input type="password" class="form-control check-password" id="retype-pass" maxlength="32">
									<div class="password-error error-msg display-hide">Password is incorrect</div>
									<div class="password-error-msg display-hide">${pwdmsg}</div>
								</div>
							</div>
							<div class="form-group text-right">
								<div class="col-md-12 add-margin">
									<button type="submit" class="btn btn-primary">Change Password</button>
									<button type="button" class="btn btn-default pass-cancel" data-dismiss="modal">Cancel</button>
								</div>
							</div>
							</form>
						
						
					</div>
				</div>
			</div>
		</div>
		<c:if test="${dflt_pwd_reset_req}">
			<script>
			$('.change-password').modal('show');
			$('.pass-cancel').attr('disabled','disabled');
			</script>
		</c:if>
		<c:if test="${warn_pwd_expire}">
			<script>
				var pwdExpireInDays = ${pwd_expire_in_days};
				bootbox.alert("Your password will expire in "+pwdExpireInDays+" day(s), please update your password.");
			</script>
		</c:if>
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
											<th>Nature of Work</th>
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
		var focussedmenu = "worklist";
		var now;
		</script>
		<script src="<cdn:url value='/resources/global/js/jquery/plugins/datatables/jquery.dataTables.min.js'/>"></script>
		<script src="<cdn:url value='/resources/global/js/jquery/plugins/datatables/dataTables.bootstrap.js'/>"></script>
		<script src="<cdn:url value='/resources/global/js/jquery/plugins/datatables/responsive/js/datatables.responsive.js'/>"></script>
		<script src="<cdn:url value='/resources/global/js/jquery/plugins/datatables/moment.min.js'/>"></script>
		<script src="<cdn:url value='/resources/global/js/jquery/plugins/datatables/datetime-moment.js'/>"></script>
		<script src="<cdn:url value='/resources/global/js/egov/custom.js?rnd=${app_release_no}'/>"></script>
		<script src="<cdn:url value='/resources/js/app/custom-menu.js?rnd=${app_release_no}'/>"></script>
		<script src="<cdn:url value='/resources/js/app/homepageofficial.js?rnd=${app_release_no}'/>"></script>
		<script type="text/javascript">
			$(window).load(function(){
		        document.getElementById("loading").style.display = "none";
				document.getElementById("loadingMask").style.display = "none";
		    });
		</script>
	</body>
</html>																						
