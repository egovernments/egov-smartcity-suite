<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib uri="http://tiles.apache.org/tags-tiles" prefix="tiles" %>
<%@ taglib uri="http://www.springframework.org/tags" prefix="spring" %>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib uri="http://tiles.apache.org/tags-tiles" prefix="tiles" %>
<%@ taglib uri="http://www.springframework.org/tags" prefix="spring" %>
<header class="navbar navbar-fixed-top"><!-- set fixed position by adding class "navbar-fixed-top" -->
	<nav class="navbar navbar-default navbar-custom navbar-fixed-top">
		<div class="container-fluid">
			<div class="navbar-header col-md-10 col-xs-10">
				<a class="navbar-brand" href="javascript:void(0);">
					<img src="<c:url value='/resources/global/images/chennai_logo.jpg'/>" height="60">
					<div>
						<span class="title2">
						<c:set var="titleKey">
                			<tiles:getAsString name="page-title"/>
            			</c:set>
						<spring:message code="${titleKey}"/>
						</span>
					</div>
				</a>
			</div>
			
			<div class="nav-right-menu col-md-2 col-xs-2">
				<ul class="hr-menu text-right">
					<li class="ico-menu">
						<a href="javascript:void(0);">
							<img src="<c:url value='/resources/global/images/logo@2x.png'/>" title="Powered by eGovernments" height="20px">
						</a>
					</li>
					
				</ul>
			</div>
			
		</div>
	</nav>
</header>