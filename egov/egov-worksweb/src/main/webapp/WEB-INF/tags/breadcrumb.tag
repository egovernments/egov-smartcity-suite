<%@ tag body-content="empty"  isELIgnored="false" pageEncoding="UTF-8"%>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%-- <c:set var="now" value="<%=new java.util.Date()%>" />
<div class='commontopyellowbg'>Works Management System</div>
<div class='commontopbluebg'><div class='commontopdate'>
Today is: <span class='bold' style='color:black'><fmt:formatDate value="${now}" pattern="dd/MM/yyyy"/></span>
</div>
Welcome <span class='bold' style='color:#cccccc'>${username}</span></div>
<div class='commontopbreadc' id='breadcrumb'>&nbsp;</div> --%>

<header class="navbar navbar-fixed-top"><!-- set fixed position by adding class "navbar-fixed-top" -->
	<nav class="navbar navbar-default navbar-custom navbar-fixed-top">
		<div class="container-fluid">
			<div class="navbar-header col-md-10 col-xs-10">
				<a class="navbar-brand" href="javascript:void(0);">
					<img src="<c:url value='${sessionScope.citylogo}' context='/egi'/>" height="60">
					<div>
						<span class="title2">
						  Works Management System
						</span>
						
					</div>
				</a>
			</div>
			
			<div class="nav-right-menu col-md-2 col-xs-2">
				<ul class="hr-menu text-right">
					<li class="ico-menu">
						<a href="http://www.egovernments.org" target="_blank">
							<img src="<c:url value='/resources/global/images/logo@2x.png' context='/egi'/>" title="Powered by eGovernments" height="20px">
						</a>
					</li>
					
				</ul>
			</div>
			
		</div>
	</nav>
</header>

	
 