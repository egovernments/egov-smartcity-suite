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
		
		<link rel="stylesheet" href="../resources/global/css/bootstrap/bootstrap.css">
		<link rel="stylesheet" href="../resources/global/css/font-icons/entypo/css/entypo.css">
		<link rel="stylesheet" href="../resources/global/css/font-icons/font-awesome-4.3.0/css/font-awesome.min.css">
		<link rel="stylesheet" href="../resources/global/css/multi-level-menu/jquery.multilevelpushmenu.css"> 
		<link rel="stylesheet" href="../resources/global/css/egov/custom.css">
		<link rel="stylesheet" href="../resources/global/js/jquery/plugins/datatables/responsive/css/datatables.responsive.css">
		<script src="../resources/global/js/jquery/jquery.js"></script>
		<script src="../resources/global/js/bootstrap/bootstrap.js"></script>
		<script src="../resources/global/js/multi-level-menu/jquery.multilevelpushmenu.js"></script>
		<script src="../resources/js/app/homepage.js"></script>
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
							<img src="${cityLogo}" height="60">
						</a>
					</div>
					
					<div class="navbar-brand">
						<h3 class="horizontal-page-title homepage" id="hp-citizen-title">${cityName}</h3>
					</div>
					
					<!-- notifications and other links -->
					<ul class="nav navbar-right pull-right">
						
						<li class="dropdown">
							
							<a href="javascript:void(0);" class="tooltip-secondary" data-toggle="tooltip" title="Worklist">
								<i class="entypo-list"></i>
							</a>
						</li>
						<li class="dropdown">
							
							<a href="javascript:void(0);" class="tooltip-secondary" data-toggle="tooltip" title="Drafts" >
								<i class="entypo-pencil"></i>
							</a>
							
						</li>
						<li class="dropdown">
							
							<a href="javascript:void(0);" class="tooltip-secondary" data-toggle="tooltip" title="Notifications" >
								<i class="entypo-bell"></i>
							</a>
							
						</li>
						
						<li class="hidden-xs menu-responsive">
							<a href="javascript:void(0);">
								<i class="entypo-user img-circle"></i>${userName}
							</a>
							<ul>
								<li>
									<a href="javascript:void(0);">
										<i class="fa fa-user"></i>
										<span class="title">Edit Profile</span>
									</a>
								</li>
								<li>
									<a href="javascript:void(0);">
										<i class="fa fa-ellipsis-h"></i>
										<span class="title">Self Service</span>
									</a>
									<ul class="left-ul-overflow">
										<c:forEach items="${selfServices}" var="selfService">
											<li>
												<a id='ss#${selfService.id}'  name='ss'  href='javascript:void(0);' onclick= "PopupCenter('/${selfService.contextRoot}${selfService.baseUrl}','portalApp${selfService.id}', 850,600)">
												<span class="title">${selfService.moduleName}</span>
												</a>
											</li>
										</c:forEach>
									</ul>
								</li>
								<li>
									<a href="javascript:void(0);">
										<i class="fa fa-key"></i>
										<span class="title">Change Password</span>
									</a>
								</li>
								<li>
									<a href="/egi/logout.do">
										<i class="fa fa-sign-out"></i>
										<span class="title">Sign Out</span>
									</a>
								</li>
							</ul>
						</li>
						
						<li class="dropdown">
							
							<a href="javascript:void(0);" class="tooltip-secondary" data-toggle="tooltip" title="Feedback">
								<i class="entypo-comment"></i>
							</a>
						</li>
						
						<li class="dropdown">
							
							<a href="javascript:void(0);" class="tooltip-secondary" data-toggle="tooltip" title="Help">
								<i class="entypo-help"></i>
							</a>
						</li>
						<li class="dropdown visible-xs hidden-sm">
							<a href="/egi/logout.do" class="tooltip-secondary" data-toggle="tooltip" title="Sign Out">
								<i class="entypo-logout"></i>
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
					<div class="row">
						<div class="col-xs-12">
							<div class="row">
								<div class="col-md-6 col-xs-6 table-header">
									Inbox
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
					</tr>
					</thead>
					
					</table>
						</div>
					</div>
					
				</div>
				
			</div>
			
		</div>
		
		<!-- Manage Saved Searches -->
		<div class="modal fade add-to-favourites" data-backdrop="static">
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
						<button type="button" class="btn btn-primary">Save</button>
						<button type="button" class="btn btn-default" data-dismiss="modal">Cancel</button>
					</div>
				</div>
			</div>
		</div>
		<!-- Modal 6 (Long Modal) -->
		<div class="modal fade loader-class" data-backdrop="static">
			<div class="modal-dialog">
				<div class="modal-body">
					<div class="row spinner-margin text-center">
						
						<div class="col-md-12 ">
							<div class="spinner">
								<div class="rect1"></div>
								<div class="rect2"></div>
								<div class="rect3"></div>
								<div class="rect4"></div>
								<div class="rect5"></div>
							</div>
						</div>
						<div class="col-md-12 spinner-text">
							Loading..
						</div>
						
					</div>
				</div>
			</div>
		</div>
		<script>
		var menuItems = ${menu};
		</script>
		<script src="../resources/global/js/bootstrap/bootstrap.js"></script>
		<script src="../resources/js/app/custom-menu.js"></script>
		<script src="../resources/global/js/jquery/plugins/datatables/jquery.dataTables.min.js"></script>
		<script src="../resources/global/js/jquery/plugins/datatables/dataTables.bootstrap.js"></script>
		<script src="../resources/global/js/jquery/plugins/datatables/responsive/js/datatables.responsive.js"></script>
		<script src="../resources/js/app/homepageofficial.js"></script>
		<script src="../resources/global/js/egov/custom.js"></script>
		
	</body>
</html>																						