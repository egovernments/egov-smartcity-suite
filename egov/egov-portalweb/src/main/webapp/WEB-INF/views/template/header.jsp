<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib uri="http://tiles.apache.org/tags-tiles" prefix="tiles" %>
<%@ taglib uri="http://www.springframework.org/tags" prefix="spring" %>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib uri="http://tiles.apache.org/tags-tiles" prefix="tiles" %>
<%@ taglib uri="http://www.springframework.org/tags" prefix="spring" %>
<header><!-- set fixed position by adding class "navbar-fixed-top" -->
				
				<nav class="navbar navbar-default navbar-custom navbar-fixed-top">
					<div class="container-fluid">
						<div class="navbar-header col-lg-4 col-md-4 col-sm-2 col-xs-3">
							<a class="navbar-brand" href="javascript:void(0);">
								<img src="<c:url value='../egi/resources/global/images/${cityLogo}'/>" height="60">
								<div>
									<span class="title2 hidden-sm hidden-xs">${cityName}</span>
								</div>
							</a>
						</div>
						<div class="nav-menu col-lg-4 col-md-6 col-sm-7 col-xs-12">
							<ul class="hr-menu text-center">
								<li class="active"><a class="menu-item " href="javascript:void(0);" data-show-screen="#inbox-template"> <span class="title">Inbox</span><span id="unreadMessageCount" class="badge custom-badge">
								 </span></a></li>
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
										<li><a href="#"><i class="fa fa-sign-out"></i> Sign out</a></li>
									</ul>
								</li>
								
								<li class="ico-menu">
									<a href="http://www.egovernments.org" target="_blank">
										<img src="../egi/resources/global/images/logo@2x.png" title="Powered by eGovernments" height="20px">
									</a>
								</li>
								
							</ul>
						</div>
						
					</div>
				</nav>
				
			</header>